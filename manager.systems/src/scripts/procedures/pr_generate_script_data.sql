if exists(select 1 from sysobjects where name = 'pr_generate_script_data')
    begin
        drop procedure pr_generate_script_data;
    end
go

create procedure pr_generate_script_data
(
    @table_name varchar(100),
	@register_version timestamp
)
as
    begin
		if('tb_fin_duplicatas'=@table_name)
			begin
				SELECT top(50)
					[data_inclusao] as creation_date,
					[fin_duplicata_id] as id,
					[grupo_duplicata] as parent_id,
					(case when [tipo_Movimento]='C' then '1' else '0' end) as credit,
					[codigo_loja] as company_id,
					[codigo_cfd] as provider_id,
					[codigo_conta] as bank_account_id,
					1 as document_type,
					isnull([numero_documento],'') as document_number,
					isnull([valor_documento],0) as document_value,
					isnull([observacoes],'') as document_note,
					(case when [baixado]=1 then 2 else 1 end) as document_status,
					isnull([pagamento_valor],0) as payment_value,
					isnull([pagamento_desconto],0) as payment_discount,
					isnull([pagamento_acrescimo],0) as payment_extra,
					(case when [residuo_agrupamento]=1 then 1 else 0 end) as payment_residue,
					(case when isnull([pagamento_data],[data_inclusao]) is not null then format(isnull([pagamento_data],[data_inclusao]), 'yyyy-MM-dd HH:mm:ss') else NULL end) as payment_data,
					(case when isnull([data_vencimento],[data_inclusao]) is not null then format(isnull([data_vencimento],[data_inclusao]), 'yyyy-MM-dd HH:mm:ss') else NULL end) as payment_expiry_data,
					isnull([codigo_usuario_inc],0) as payment_user,
					isnull([numero_nota],0) as nfe_number,
					isnull([serie_nota],0) as nfe_serie_number,
					isnull([gera_boleto],0) as generate_billet,
					isnull([codigo_grupo_fdc],0) as financial_group_id,
					isnull([codigo_grupo_fdc],0) as financial_sub_group_id,
					0 as inactive,
					[versao_registro] as version_register
				FROM 
					[tb_fin_duplicatas] with (nolock)
				WHERE
					[versao_registro]>@register_version
					and [baixado]=1;
			end /** End tb_fin_duplicatas */
    end
go
/**
declare 
    @table_name varchar(50) = 'tb_fin_duplicatas',
    @register_version timestamp=0x0000000000026AF2

exec pr_generate_script_data @table_name, @register_version;
*/

-- select * from tb_fin_duplicatas where Data_Inclusao between '2019-09-01 00:00:00' and GETDATE() order by versao_registro

-- select (case when isnull([pagamento_data],[data_inclusao]) is not null then format(isnull([pagamento_data],[data_inclusao]), 'yyyy-MM-dd HH:mm:ss') else NULL end) as payment_data, Data_Emissao, Data_Inclusao, isnull(Pagamento_Data,data_emissao), * from tb_fin_duplicatas where fin_duplicata_id= 908423;

--update tb_fin_duplicatas set gera_boleto=gera_boleto where Data_Inclusao between '2019-09-01 00:00:00' and GETDATE()
