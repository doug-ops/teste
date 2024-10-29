alter procedure [sqlserver_insert_mov_prod_automatic]
(
	@company_id int, 
	@company_description varchar(100),
	@document_parent_id bigint,
	@bank_account int
)
as 
begin

declare @total_movement money,
	    @grupo_duplicata bigint,
		@obs_grupo_duplicata varchar(1000),
		@id bigint,
		@codigo_produto bigint, 
		@et_final bigint,
		@sa_final bigint,
		@sa_final_2 bigint,
		@codigo_turno bigint;

drop table if exists #tmp_product_movement;
drop table if exists #tmp_document_movement;
drop table if exists #tmp_tb_bin_turno_fechamento;

-- puxa movimentos manager
select * into #tmp_product_movement
from MYSQL_MANAGER...product_movement where company_id = @company_id and document_parent_id = @document_parent_id;

-- insert [tb_bin_turno_fechamento_online]
insert into [LinkedtoRDS].[REMOTO].dbo.[tb_bin_turno_fechamento_online]
(codigo_produto, et_final, sa_final, data, hora, codigo_loja, tipo_movimento, sincronizado, registro_online, dta_registro, codigo_usuario, sa_final_2)
select product_id codigo_produto, credit_in_final et_final, credit_out_final sa_final, FORMAT(reading_date, 'yyyy-MM-dd') data, 
FORMAT(reading_date, 'HH:mm:ss') hora, @company_description codigo_loja, 'L' tipo_movimento, 0 sincronizado, 0 registro_online, 
FORMAT(reading_date, 'yyyy-MM-dd HH:mm:ss') dta_registro, 11 codigo_usuario, credit_clock_final  sa_final_2
from #tmp_product_movement;

select * INTO #tmp_document_movement from MYSQL_MANAGER...document_movement where 
document_parent_id = @document_parent_id and company_id = @company_id;

select * INTO #tmp_tb_bin_turno_fechamento from [LinkedtoRDS].[REMOTO].dbo.tb_bin_turno_fechamento  where codigo_loja = 947 and et_final is null;

select @total_movement = sum(case when credit = 1 then document_value else (document_value * -1) end)   from #tmp_document_movement where 
document_parent_id = @document_parent_id and company_id = @company_id;

select  @total_movement;

SELECT @obs_grupo_duplicata = STUFF((
            SELECT ' ' + 'maq: ' + convert(varchar, product_id) + ':' + 
			' E.I.' + convert(varchar, credit_in_initial) +
			' / E.F.' + convert(varchar, credit_in_final) +
			' // S.I.' + convert(varchar, credit_out_initial) +
			' / S.F.' + convert(varchar, credit_out_final)
            FROM #tmp_product_movement 
            FOR XML PATH('')
            ), 1, 1, '');

-- Insert duplicata grupo
insert into [LinkedtoRDS].[REMOTO].dbo.tb_fin_duplicatas_grupo(codigo_loja, data_inclusao, valor_documento, baixado, tipo_entrada_saida, 
tipo_movimento, data_vencimento, pagamento_data,	pagamento_valor, observacoes, pagamento_desconto, pagamento_acrescimo, codigo_usuario_inc, 
codigo_grp_usuario_inc, codigo_usuario_pag,	codigo_usuario_baixa,	descricao,	codigo_conta,	codigo_usuario_altera)
select @company_id codigo_loja, getdate() data_inclusao, @total_movement valor_documento, 
1 baixado, (case when @total_movement >= 0 then 'S' else 'E' end) tipo_entrada_saida, 
(case when @total_movement >= 0 then 'C' else 'D' end) tipo_movimento, getdate() data_vencimento,	
getdate() pagamento_data, @total_movement pagamento_valor, @obs_grupo_duplicata observacoes, 0 pagamento_desconto, 0 pagamento_acrescimo, 
11 codigo_usuario_inc, 0 codigo_grp_usuario_inc, 0 codigo_usuario_pag,	
11 codigo_usuario_baixa, @company_description descricao, @bank_account codigo_conta, 11 codigo_usuario_altera;

select @grupo_duplicata = max(grupo_duplicata) from [LinkedtoRDS].[REMOTO].dbo.tb_fin_duplicatas_grupo where codigo_loja = @company_id and 
valor_documento = @total_movement and Observacoes = @obs_grupo_duplicata;

-- Inseri fin_duplicatas
insert into [LinkedtoRDS].[REMOTO].dbo.tb_fin_duplicatas(Codigo_Loja, Codigo_Conta, Numero_Documento, Tipo_Movimento, Data_Inclusao, 
Data_Vencimento, Valor_Documento, Gera_Boleto, Custo_Fixo, Baixado, Codigo_CFD, Tipo_Entrada_Saida, Pagamento_Data, Pagamento_Valor, 
Observacoes, Pagamento_Desconto, Codigo_Especie, Pagamento_Acrescimo, codigo_grupo_fdc, codigo_usuario, ordem_pgto, codigo_usuario_inc,
codigo_grp_usuario_inc, grupo_duplicata, status, codigo_usuario_baixa, id_pagamento, Data_Emissao, codigo_usuario_altera, residuo_agrupamento, 
Motivo_Baixa, codigo_cfd)
select @company_id codigo_loja, @bank_account Codigo_Conta, document_number Numero_Documento, 
(case when credit = 1 then 'C' else 'D' end) Tipo_Movimento, payment_data data_inclusao, payment_data Data_Vencimento, 
payment_value Valor_Documento, 0 Gera_Boleto, 0 Custo_Fixo, 1 Baixado, 0 Codigo_CFD, 
(case when credit = 1 then 'S' else 'E' end) Tipo_Entrada_Saida, payment_data Pagamento_Data, payment_value Pagamento_Valor, 
document_note Observacoes, 0 Pagamento_Desconto, 0 Codigo_Especie, 0 Pagamento_Acrescimo, 77 codigo_grupo_fdc,
0 codigo_usuario, 'P' ordem_pgto, 11 codigo_usuario_inc, 0 codigo_grp_usuario_inc, @grupo_duplicata grupo_duplicata, 
0 status, 11 codigo_usuario_baixa, 
0 id_pagamento, payment_data Data_Emissao, 0 codigo_usuario_altera, 0 residuo_agrupamento, '' Motivo_Baixa,
(case when credit = 1 then 4249 else 4250 end) codigo_cfd
from #tmp_document_movement;

update #tmp_tb_bin_turno_fechamento 
set 
#tmp_tb_bin_turno_fechamento.et_final = tm.credit_in_final,
#tmp_tb_bin_turno_fechamento.sa_final = tm.credit_out_final,
#tmp_tb_bin_turno_fechamento.sa_final_2 = tm.credit_clock_final,
#tmp_tb_bin_turno_fechamento.codigo_usuario = 11
from #tmp_tb_bin_turno_fechamento tf 
inner join #tmp_product_movement tm on tf.codigo_produto = tm.product_id;

insert into [LinkedtoRDS].[REMOTO].dbo.[tb_bin_turno]([descricao],[abertura],[codigo_status],[codigo_loja]) 
select @company_description,getdate(), 3, @company_id;

select @codigo_turno = max(codigo_turno) from [LinkedtoRDS].[REMOTO].dbo.[tb_bin_turno] where codigo_loja = @company_id; 

SET NOCOUNT ON; 
  
DECLARE vendor_cursor CURSOR FOR   
select max(id) id, codigo_produto, max(et_final) et_final, max(sa_final) sa_final, max(sa_final_2) sa_final_2 from #tmp_tb_bin_turno_fechamento 
group by codigo_produto;
  
OPEN vendor_cursor  
  
FETCH NEXT FROM vendor_cursor   
INTO @id, @codigo_produto, @et_final, @sa_final, @sa_final_2
  
WHILE @@FETCH_STATUS = 0  
BEGIN  
	
update [LinkedtoRDS].[REMOTO].dbo.tb_bin_turno_fechamento 
	set 
	et_final = @et_final,
	sa_final = @sa_final,
	sa_final_2 = @sa_final_2,
	codigo_usuario = 11, 
	duplicata_entrada_id = 0,
	duplicata_saida_id = 0
where
	id = @id and codigo_produto = @codigo_produto;
	
insert into [LinkedtoRDS].[REMOTO].dbo.tb_bin_turno_fechamento(codigo_turno,	codigo_produto,	et_inicial,	et_final,	sa_inicial,	sa_final, 
data,	codigo_usuario,	tipo,	codigo_loja, sa_inicial_2, sa_final_2)
select @codigo_turno,	@codigo_produto,	@et_final,	null,	@sa_final,	null, 
getdate() data,	11 codigo_usuario,	'M' tipo,	@company_id, @sa_final_2, null;


update [LinkedtoRDS].[REMOTO].dbo.[tb_cad_produto] set [codigo_turno]=@codigo_turno where codigo_produto=@codigo_produto;


    FETCH NEXT FROM vendor_cursor   
    INTO  @id, @codigo_produto, @et_final, @sa_final, @sa_final_2   
	
END   
CLOSE vendor_cursor;  
DEALLOCATE vendor_cursor; 

update [LinkedtoRDS].[REMOTO].dbo.[tb_fin_contas] set [Saldo_Disponivel] = [Saldo_Disponivel] + @total_movement where [Codigo_Conta] = @bank_account; 

end

/**
declare 
@company_id int = 947, 
@company_description varchar(100) = 'GRANWIN POA RS BRAVO',
@document_parent_id bigint = 265651, 
@bank_account int = 1000;

exec [sqlserver_insert_mov_prod_automatic] @company_id, @company_description, @document_parent_id, @bank_account;
*/


