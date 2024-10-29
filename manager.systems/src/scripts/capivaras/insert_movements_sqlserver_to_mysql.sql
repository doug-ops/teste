-- select * from tb_fin_contas where nome_titular = 'ZL MANDACARU'
-- create table tmp_produto(codigo_produto bigint, et_inicial bigint, et_final bigint, sa_inicial bigint, sa_final bigint, data varchar(20), sa_inicial_2 bigint, sa_final_2 bigint);

use remoto 
select
'insert into tmp_produto(codigo_produto, et_inicial, et_final, sa_inicial, sa_final, data, sa_inicial_2, sa_final_2) select ' +
 CONVERT(varchar, codigo_produto)+', '
 +CONVERT(varchar, et_inicial)+', '
 +CONVERT(varchar, et_final)+', '
 +CONVERT(varchar, sa_inicial)+', '
 +CONVERT(varchar, sa_final)+', ' 
 +''''+replace(CONVERT(varchar, data, 102),'.','-') + ' ' + CONVERT(varchar, data, 108)+''', '
 +CONVERT(varchar, isnull(sa_inicial_2,0))+', '
 +CONVERT(varchar, sa_final_2)+'; ' 
from 
tb_bin_turno_fechamento 
where 
codigo_loja = 1078 
and id in (
193547
,193548
,193549
,193550
,193551
,193552
,193553
,193554
,195509
) order by id

use remoto
select *  from tb_bin_turno_fechamento where codigo_loja = 1078 
--and codigo_turno in (66115, 66545) group by codigo_produto  
order by id

SELECT * FROM TB_FIN_CONTAS WHERE NOME_TITULAR = 'SKNIA MATEUS'

SELECT * FROM tb_fin_contas_loja WHERE CODIGO_LOJA = 106

SELECT c.* FROM tb_cad_loja l
inner join tb_fin_contas c on l.codigo_conta = c.Codigo_Conta 
WHERE l.nome_loja like '%RGB%'