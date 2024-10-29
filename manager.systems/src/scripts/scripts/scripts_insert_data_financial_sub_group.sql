use manager;
/**
SET SQL_SAFE_UPDATES = 0;

select * from financial_sub_group_integration_system;
select * from financial_sub_group;

delete from financial_sub_group_integration_system;
delete from financial_sub_group;
*/

/**
select 
'insert into financial_sub_group(' +
'id, ' + 
'financial_group_id, ' + 
'description, ' +
'inactive, user_creation, creation_date, user_change, change_date) select '
+convert(varchar(8), id)+','
+convert(varchar(8), grupo)+','
+''''+descricao+''','
+convert(varchar(1), 0)+',1,now(),1,now();' from tb_fin_grupos_estrutura where id<>0;

select 
'insert into financial_sub_group_integration_system(' +
'financial_sub_group_id, ' + 
'integration_system_id, ' +
'legacy_id, ' +
'inactive, user_creation, creation_date, user_change, change_date) select '
+convert(varchar(8), id)+','
+convert(varchar(1), 1)+','
+convert(varchar(8), id)+','
+convert(varchar(1), 0)+',1,now(),1,now();'  from tb_fin_grupos_estrutura where id<>0;

select * from tb_fin_grupos_estrutura where id<>0;
*/

insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 1,1,'JBL RS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 2,1,'JBL MT',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 59,1,'FINANCEIRO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 107,1,'JBL SP',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 3,2,'OPER.JJX (GRD)',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 4,2,'OPER.KRK RJ',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 5,2,'OPER. DAN',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 6,2,'*****INUTILIZADO****OPER.GORDO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 7,2,'OPER.TAVATA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 12,2,'OPER. JNR PR',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 61,2,'OPER. IND SP  TUS/SAU',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 80,2,'****INUTILIZADO****OPER. ADR',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 84,2,'***INUTILIZADO***OPER. HAL PE',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 92,2,'OPER. URS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 104,2,'OPER.IND RJ  JAP/KRK/AGR',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 109,2,'OPER. IDT RJ AGR/JAP',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 110,2,'GERMAN',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 111,2,'OPER. LAZ RS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 116,2,'***INUTILIZADO****OPER. EMR MG',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 117,2,'OPER. BL/RJ',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 118,2,'OPER. EDI',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 119,2,'OPER. HENR REC',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 123,2,'OPER. BOY',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 124,2,'OPER. IDT SP TST/SAU',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 11,3,'CUSTOS DE OPERAÇÃO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 8,5,'ALUGUEL',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 9,5,'PGTO FUNCIONARIO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 13,5,'MATERIA PRIMA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 23,5,'% LOJA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 51,5,'OBRAS LOJAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 56,5,'FLUXO CAIXA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 67,5,'MANUTENÇAO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 71,5,'13º SALARIO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 81,5,'DESPESAS COM CLIENTES ',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 82,5,'FERIAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 83,5,'BONIFICAÇÃO TECNICA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 86,5,'ENERGIA ELETRICA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 91,5,'PARCEIRO SOFTWARES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 101,5,'IMPOSTOS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 102,5,'NATAL VENDEDORES ',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 103,5,'DESPESAS EXTRAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 105,5,'TV ASSINATURA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 108,5,'PROMOÇÕES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 112,5,'AQUISIÇÃO PONTO/LUVAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 114,5,'PREJUIZO OPERACIONAL',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 120,5,'HONORARIOS CONTABILIDADE',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 121,5,'VALE REFEIÇÃO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 14,6,'CONTABILIDADE',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 19,6,'J CLUBES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 26,6,'ADVOCACIA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 27,6,'ALUGUEL',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 30,6,'BANCARIAS MANUTENÇÃO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 32,6,'SERVIDORES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 45,6,'CONDOMINIO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 52,6,'LICENCIAMENTO SOFTWARES MENSAL',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 64,6,'SEGURANÇA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 15,7,'VIAGENS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 16,7,'FRETES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 17,7,'BOBINAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 18,7,'REUNIOES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 22,7,'AQUISIÇÃO EQUIPAMENTOS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 24,7,'TRANSPORTE',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 28,7,'TELEFONE FIXO / INTERNET',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 29,7,'TELEFONE CELULAR / INTERNET',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 31,7,'BANCARIAS TARIFAS SERVIÇOS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 40,7,'FORUM',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 42,7,'REVENDA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 43,7,'SERVIÇOS MANUT. EQUIPAMENTOS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 44,7,'MKT',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 46,7,'ESCRIT. ADMINSTRATIVO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 47,7,'ESCRIT. OPERACIONAL',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 48,7,'DESENVOLVIMENTO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 49,7,'MATERIAL INSUMO INSTALAÇOES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 50,7,'OBRAS SALAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 55,7,'LUCROS E PERDAS DIFERENÇAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 57,7,'AQUISIÇAO COMPONENTES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 58,7,'FINANCEIRO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 60,7,'DESENVOLVIMENTO TI',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 62,7,'DESPESAS EXTRAS PESQ. MERCADO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 63,7,'LICENÇAS / ANUIDADES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 65,7,'VEICULOS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 73,7,'CONSULTORIA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 75,7,'DETRAN',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 88,7,'AQUISIÇÃO COMPONENTES ELETRONI',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 93,7,'ENERGIA ELETRICA',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 97,7,'HONORARIOS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 106,7,'CONGRESSOS / FEIRAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 113,7,'ACESSORIAS E ASSOCIAÇÕES',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 20,8,'SALARIO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 21,8,'EXTRAS CAIX.',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 25,8,'AJ. CUSTO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 72,8,'13º SAL',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 78,8,'BONIFICAÇÃO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 87,8,'INDENIZAÇÃO',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 96,8,'FÉRIAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 33,9,'DAS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 34,9,'FGTS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 35,9,'GPS',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 36,9,'IRRF',0,1,now(),1,now();
insert into financial_sub_group(id, financial_group_id, description, inactive, user_creation, creation_date, user_change, change_date) select 74,9,'IPVA',0,1,now(),1,now();

insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 1,1,1,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 2,1,2,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 59,1,59,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 107,1,107,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 3,1,3,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 4,1,4,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 5,1,5,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 6,1,6,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 7,1,7,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 12,1,12,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 61,1,61,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 80,1,80,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 84,1,84,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 92,1,92,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 104,1,104,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 109,1,109,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 110,1,110,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 111,1,111,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 116,1,116,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 117,1,117,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 118,1,118,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 119,1,119,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 123,1,123,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 124,1,124,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 11,1,11,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 8,1,8,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 9,1,9,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 13,1,13,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 23,1,23,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 51,1,51,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 56,1,56,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 67,1,67,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 71,1,71,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 81,1,81,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 82,1,82,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 83,1,83,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 86,1,86,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 91,1,91,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 101,1,101,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 102,1,102,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 103,1,103,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 105,1,105,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 108,1,108,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 112,1,112,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 114,1,114,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 120,1,120,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 121,1,121,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 14,1,14,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 19,1,19,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 26,1,26,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 27,1,27,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 30,1,30,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 32,1,32,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 45,1,45,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 52,1,52,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 64,1,64,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 15,1,15,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 16,1,16,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 17,1,17,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 18,1,18,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 22,1,22,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 24,1,24,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 28,1,28,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 29,1,29,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 31,1,31,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 40,1,40,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 42,1,42,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 43,1,43,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 44,1,44,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 46,1,46,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 47,1,47,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 48,1,48,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 49,1,49,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 50,1,50,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 55,1,55,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 57,1,57,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 58,1,58,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 60,1,60,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 62,1,62,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 63,1,63,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 65,1,65,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 73,1,73,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 75,1,75,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 88,1,88,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 93,1,93,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 97,1,97,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 106,1,106,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 113,1,113,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 20,1,20,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 21,1,21,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 25,1,25,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 72,1,72,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 78,1,78,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 87,1,87,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 96,1,96,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 33,1,33,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 34,1,34,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 35,1,35,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 36,1,36,0,1,now(),1,now();
insert into financial_sub_group_integration_system(financial_sub_group_id, integration_system_id, legacy_id, inactive, user_creation, creation_date, user_change, change_date) select 74,1,74,0,1,now(),1,now();

