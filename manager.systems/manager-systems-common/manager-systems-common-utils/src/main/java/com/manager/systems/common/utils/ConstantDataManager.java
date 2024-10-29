package com.manager.systems.common.utils;

public final class ConstantDataManager {

	//Application
	public static final String PROJECT_ALIAS = "manager-systems";
	public static final String PROJECT_XML_ALIAS = "xml";
	public static final String VERSAO_APLICATIVO = "2.1";
	public static final String SEND_EMAIL_AMAZON = "send_email_amazon";
	public static final String TIMER_SINCRONISMO = "timer.sincronismo";
	
	public static final Boolean TRUE = true;
	public static final Boolean FALSE = false;
	public static final String TRUE_STRING = "true";
	public static final String FALSE_STRING = "false";
	public static final String TRUE_INT = "1";
	public static final String FALSE_INT = "0";
	public static final String PATTERN_YYYY_MM_DD ="yyyy-MM-dd";	
	public static final String PATTERN_HH_MM_SS ="HH:mm:ss";
	public static final String PATTERN_DD_MM_YYYY_HH_MM_SS ="dd/MM/yyyy HH:mm";	
	public static final String PATTERN_DD_MM_YYYY ="dd/MM/yyyy";	
	public static final String PATTERN_YY_MM_DD_HH_MM_SS ="yy-MM-dd HH:mm";	
	public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String BARRA = "/";
	public static final String TRACO = "-";
	public static final String UNDERSCORE = "_";
	public static final String BLANK = "";
	public static final String PONTO = ".";
	public static final String SPACE = " ";
	public static final String REAL = "R$";
	public static final String PARENTESES_LEFT = "(";
	public static final String PARENTESES_RIGHT = ")";
	public static final String ZERO_STRING = "0";
	public static final String ONE_STRING = "1";
	public static final String NINE_STRING = "9";
	public static final String VIRGULA_ZERO_ZERO= ",00";
	public static final String ZERO_VIRGULA_ZERO_ZERO= "0,00";
	public static final String VIRGULA_STRING = ",";
	public static final String PONTO_VIRGULA_STRING = ";";
	public static final String SIMPLE_ASPAS = "'";
	public static final String IGUAL = "=";
	public static final String QUESTION = "?";
	public static final String AND = "AND";
	public static final String ACTIVE = "AT";
	public static final String INACTIVE = "IN";
	public static final String STATUS_DESCRIPTION = "status";
	public static final String MESSAGE_CODE = "messageCode";
	public static final String MESSAGE_DESCRIPTION = "message";
	public static final String SIM = "S";
	public static final String NAO = "N";
	public static final String DOIS_PONTOS = ":";
	public static final String TRES_PONTOS = "...";
	public static final String D = "D";
	public static final String M = "M";
	public static final String C = "C";
	public static final String DATA_01_01_1900_00_00 = "01/01/1900 00:00";
	public static final String ASTERISCO = "*";
	public static final String SCAPE_PIPE = "\\|";
	public static final String PIPE = "|";
	public static final String PT = "pt";
	public static final String BR = "BR";
	public static final String ON = "on";
	public static final String OFF = "off";
	public static final String HOUR_ZERO = "00:00:00";
	public static final String HOUR_FINAL = "23:59:59";
	public static final String HOUR_SEVEN = "07:00";
	public static final String ZERO_ZERO = "00";
	public static final String FIVE_NINE = "59";
	public static final String CREDIT_ALIAS = "C";
	public static final String DEBIT_ALIAS = "D";
	public static final String INPUT_ALIAS = "E";
	public static final String OUTPUT_ALIAS = "S";
	public static final String TYPE_WATCH_INPUT_ALIAS = "In";
	public static final String TYPE_WATCH_OUTPUT_ALIAS = "Out";
	public static final String INPUT_INITIAL_DOCUMENT_NOTE_ALIAS = "EI";
	public static final String OUTPUT_INITIAL_DOCUMENT_NOTE_ALIAS = "SI";
	public static final String INPUT_FINAL_DOCUMENT_NOTE_ALIAS = "EF";
	public static final String OUTPUT_FINAL_DOCUMENT_NOTE_ALIAS = "SF";	
	public static final String CLOCK_INITIAL_DOCUMENT_NOTE_ALIAS = "CI";
	public static final String CLOCK_FINAL_DOCUMENT_NOTE_ALIAS = "CF";
	public static final String PRODUCT_DOCUMENT_NOTE_ALIAS = "PROD";	
	public static final String HASHTAG = "#";
	public static final String EXCLAMATION = "!";
	public static final String WEEK_LABEL = "Semana";
	
	//TimeZone
	public static final String TIMEZONE_SAO_PAULO = "America/Sao_Paulo";
	
	// Parameters XML
	public static final String UTF_8 = "UTF-8";
	public static final String ISO_8859 = "ISO-8859-1";
	public static final String ALIAS_ROOT = "root";
	public static final String RESPONSE_TYPE_XML = "text/xml";
	public static final String RESPONSE_TYPE_JSON = "application/json";
	public static final String RESPONSE_TYPE_TEXT = "text/plain";
	public static final String RESPONSE_CACHE_CONTROL = "Cache-Control";
	public static final String RESPONSE_NO_CACHE = "no-cache";
	public static final String RESPONSE_TYPE_EXCEL = "application/vnd.ms-excel";
	
	// Parameters PDF
	public static final String RESPONSE_TYPE_PDF = "application/pdf";
	public static final String RESPONSE_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String RESPONSE_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String RESPONSE_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	public static final String RESPONSE_GET = "GET";
	public static final String RESPONSE_POST = "POST";
	public static final String RESPONSE_PUT = "PUT";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String INLINE = "inline";
	public static final String FILENAME = "filename";
	public static final String RESPONSE_NO_STORE = "no-store";
	public static final String RESPONSE_MUST_REVALIDATE = "must-revalidate";
	public static final String RESPONSE_PRAGMA = "Pragma";
	public static final String RESPONSE_EXPIRES = "Expires";	
	public static final String EXTENSION_PDF = "pdf";
	public static final String EXTENSION_EXCEL = "xls";
	public static final String EXTENSION_EXCEL_XLSX = "xlsx";
	public static final String EXTENSION_XML = "xml";
	
	public static final String ERROR_BUSSINESS = "errorBussiness";
	
	//Object Types
	public static final String OBJECT_TYPE_COMPANY = "COM";
	public static final String OBJECT_TYPE_PROVIDER = "FOR";
	public static final String OBJECT_TYPE_USER = "USU";
	public static final String OBJECT_TYPE_BANK_ACCOUNT = "BANKAC";
	public static final String OBJECT_TYPE_FIN_GROUP = "FINGRU";
	public static final String OBJECT_TYPE_FIN_SUB_GROUP = "FINSUBGRU";
	public static final String OBJECT_TYPE_PROD_GROUP = "PROGRU";
	public static final String OBJECT_TYPE_ACCESS_PROFILE = "ACCESSPROFILE";
	public static final String OBJECT_TYPE_PROD_SUB_GROUP = "PROSUBGRU";
	public static final String OBJECT_TYPE_PRODUCT = "PRO";
	public static final String OBJECT_TYPE_FINANCIAL_TRANSFER = "FINTRANS";
	public static final String OBJECT_TYPE_DOCUMENT_MOVEMENT = "DOCMOV";
	public static final String OBJECT_TYPE_FINANCIAL_COST_CENTER = "FINCOSTCENTER";
	
	//Origins
	public static final String ORIGIN_BANK_ACCOUNT_STATEMENT = "bankAccountStatement";
	
	//Operation Types
	public static final String SAVE_METHOD_LABEL = "Save";
	public static final String INACTIVE_METHOD_LABEL = "Inactive";
	public static final String GET_METHOD_LABEL = "Get";
	public static final String GET_ALL_METHOD_LABEL = "GetAll";
	public static final String GET_ALL_COMBOBOX_METHOD_LABEL = "GetAllCombobox";
	public static final String DELETE_METHOD_LABEL = "Detele";
	public static final String AUTOCOMPLETE_METHOD_LABEL = "Autocomplete";
	public static final String UNGROUP_METHOD_LABEL = "Ungroup";
	public static final String CHANGE_STATUS_METHOD_LABEL = "ChangeStatus";
	public static final String PROCESS_MOVEMENT_LABEL = "ProcessMovement";
	public static final String PREVIEW_PROCESS_MOVEMENT_LABEL = "PreviewProcessMovement";	
	public static final String INACTIVE_ALL_METHOD_LABEL = "InactiveAll";
	public static final String CHANGE_METHOD_LABEL = "change";
	public static final String GROUP_PARENT_METHOD_LABEL = "groupParent";
	public static final String GET_DASHBOARD_GROUP_SUBGROUP_METHOD_LABEL = "GetDashboards";	
	
	//Ibge Code
	public static final String IBGE_CODE_COUNTRY_BRASIL = "1058";
	
	//Geral
	public static final String MESSAGE_SEM_CONEXAO = "Sem conex\u00E3o com Banco de Dados. Favor entrar em contato com a Manager Systems!";
	public static final String MESSAGE_ACESSO_EXPIRADO = "Acesso expirado!";
	public static final String MESSAGE_BALANCA_NAO_SUPORTADA = "Balanca nao suportada";
	public static final String MESSAGE_OPCAO_INVALIDA = "Op\u00e7\u00e3o n\u00E3o suportada";
	public static final String MESSAGE_TIPO_MOVIMENTO_NAO_SUPORTADO = "Tipo de movimento: %s n\u00E3o suportado";
	public static final String MESSAGE_TIPO_ESPECIE_NAO_SUPORTADO = "Tipo de esp\u00e8cie: %s n\u00E3o suportado";
	public static final String MESSAGE_INVALID_ID = "C\u00f3digo: %s inv\u00e1lido";
	public static final String MESSAGE_INVALID_NAME = "Nome: %s inv\u00e1lido";
	public static final String MESSAGE_INVALID_DESCRIPTION = "Descri\u00e7\u00e3o: %s inv\u00e1lida";
	public static final String MESSAGE_INVALID_SOCIAL_NAME = "Raz\u00E3o Social: %s inv\u00e1lida";
	public static final String MESSAGE_INVALID_OBJECT_TYPE = "Tipo de objeto: %s inv\u00e1lido";
	public static final String MESSAGE_INVALID_EMAIL = "Email: %s inv\u00e1lido";
	public static final String MESSAGE_INVALID_CEP = "Cep: %s inv\u00e1lido";
	public static final String MESSAGE_INVALID_DATA_ACCESS = "Dados de acesso inv\u00e1lido";
	public static final String MESSAGE_INVALID_FINANCIAL_GROUP = "Grupo Financeiro: %s inv\u00e1lido";
	public static final String MESSAGE_INVALID_PRODUCT_GROUP = "Grupo: %s inv\u00e1lido";
	public static final String MESSAGE_INVALID_PRODUCT_SUB_GROUP = "SubGrupo: %s inv\u00e1lido";
	public static final String MESSAGE_INVALID_SALE_PRICE = "Pre\u00E7o de Venda: %s inv\u00E1lido";
	public static final String MESSAGE_INVALID_COST_PRICE = "Pre\u00E7o de Custo: %s inv\u00E1lido";
	public static final String MESSAGE_INVALID_CONVERSION_FACTOR = "Fator de Convers\u00E3o: %s inv\u00E1lido";
	public static final String MESSAGE_INVALID_INPUT_MOVEMENT = "Movimento de Entrada: %s inv\u00E1lido";
	public static final String MESSAGE_INVALID_OUTPUT_MOVEMENT = "Movimento de Sa\u00EDda: %s inv\u00E1lido";
	public static final String MESSAGE_INVALID_CLOCK_MOVEMENT = "Movimento de 3 Relogio: %s inv\u00E1lido";
	
	//Financial Transfer
	public static final String MESSAGE_INVALID_BANK_ACCOUNT_ORIGIN = "Conta origem: %s inv\u00E1lida";
	public static final String MESSAGE_INVALID_BANK_ACCOUNT_DESTINY = "Conta destino: %s inv\u00E1lida";
	public static final String MESSAGE_INVALID_BANK_ACCOUNT_ORIGIN_DESTINY_NOT_SAME = "Conta origem: %s e destino n\u00e3o podem ser iguais.";

	public static final String MESSAGE_INVALID_DESCRIPTION_GROUP = "Descri\u00e7\u00e3o do Grupo: %s inv\u00E1lida";
	public static final String MESSAGE_INVALID_EXECUTION_ORDER_GROUP = "Orderm de execu\u00e7\u00e3o do Grupo: %s inv\u00E1lida";
	public static final String MESSAGE_INVALID_TRANSFER_GROUP_ITENS = "Pelo menos uma configura\u00e7\u00e3o de transfer\\u00encia deve ser informada.";
	public static final String MESSAGE_INVALID_DESCRIPTION_TRANSFER_ITEM = "Descri\u00e7\u00e3o do item de transfer\\u00encia: %s inv\u00E1lida";
	public static final String MESSAGE_INVALID_EXECUTION_ORDER_TRANSFER_ITEM = "Orderm de execu\u00e7\u00e3o do item de transfer\\u00encia: %s inv\u00E1lida";
	public static final String MESSAGE_INVALID_PROVIDER_TRANSFER_ITEM = "Fornecedor do item de transfer\\u00encia: %s inv\u00E1lida";
	public static final String MESSAGE_INVALID_VALUE_TRANSFER_ITEM = "Valor do item de transfer\\u00encia: %s inv\u00E1lida";
	public static final String MESSAGE_INVALID_TRANSFER_GROUP_ID = "C\u00f3digo grupo transfer\\u00encia: %s inv\u00E1lido";
	public static final String MESSAGE_INVALID_TRANSFER_ID = "C\u00f3digo transfer\\u00encia: %s inv\u00E1lido";
	
	//Report Labels
	public static final String LABEL_TITLE_REPORT_PDF = "RELATÓRIO DE LANÇAMENTOS – EMPRESA:";
	public static final String LABEL_LAUNCH = "LAN\u00c7AMENTO";
	public static final String LABEL_REPORT = "RELAT\u00d3RIO";
	public static final String LABEL_FROM = "DE";
	public static final String LABEL_TO = "AT\u00c9";
	public static final String LABEL_DATE = "DATA";
	public static final String LABEL_WEEK = "SEMANA";
	public static final String LABEL_DATE_LAUNCH_CAPITALIZE = "Data";
	public static final String LABEL_DATE_FINANCIAL_CAPITALIZE = "Data";
	public static final String LABEL_INITIAL_DATE = "DATA INICIAL";
	public static final String LABEL_FINAL_DATE = "DATA FINAL";
	public static final String LABEL_COMPANY = "EMPRESA";
	public static final String LABEL_CREDIT = "CR\u00c9DITO";
	public static final String LABEL_TITLE_REPORT_MOVEMENTS_PDF = "RELATÓRIO DE LANÇAMENTOS";
	public static final String LABEL_INPUT_MOVEMENT = "MOVIMENTOS ENTRADA";
	public static final String LABEL_DOCUMENT = "DOCUMENTO";
	public static final String LABEL_DOC = "DOC";
	public static final String LABEL_PROVIDER = "FORNECEDOR";
	public static final String LABEL_FINANCIAL_GROUP = "GRUPO FINANCEIRO";
	public static final String LABEL_FINANCIAL_SUB_GROUP = "SUB-GRUPO FINANCEIRO";
	public static final String LABEL_NOTE = "OBSERVA\u00c7\u00c3O";
	public static final String LABEL_VALUE = "VALOR";
	public static final String LABEL_TOTAL = "TOTAL";	
	public static final String LABEL_OUTPUT_MOVEMENT = "MOVIMENTOS SA\u00cdDA";
	public static final String LABEL_DEBIT = "D\u00c9BITO";	
	public static final String LABEL_TOTAL_BALANCE = "SALDO BRUTO";
	public static final String LABEL_FINAL_BALANCE = "SALDO FINAL";
	public static final String LABEL_INPUT_PLUS_OUTPUT_MOVEMENT = "MOVIMENTOS ENTRADA / SA\u00cdDA";
	public static final String LABEL_TRANSFERS = "TRANSFER\u00caNCIAS";	
	public static final String LABEL_COMISSIONS_PLUS_EXPENSES = "COMISS\u00d5ES / DESPESAS";		
	public static final String LABEL_TOTAL_TRANSFERS = "SALDO DAS TRANSFER\u00caNCIAS";	
	public static final String LABEL_TOTAL_LAUNCH = "SALDO DOS LAN\u00c7AMENTOS";	
	public static final String LABEL_TYPE_OF_WATCH = "TIPO DE REL\u00d3GIO"; 
	public static final String LABEL_INITIAL_ENTRY = "ENTRADA INICIAL"; 
	public static final String LABEL_FINAL_ENTRY = "ENTRADA FINAL"; 
	public static final String LABEL_INITIAL_OUTPUT = "SA\u00cdDA INICIAL"; 
	public static final String LABEL_FINAL_OUTPUT = "SA\u00cdDA FINAL"; 
	public static final String LABEL_SPECIFICATION = "ESPECIFICA\u00c7\u00c3O";
	public static final String LABEL_USER = "USU\u00c1RIO";
	public static final String LABEL_LANCH_USER = "Usu\u00e1rio Lan\u00e7amento";
	public static final String LABEL_CLOSE_USER = "Usu\u00e1rio Fechamento";
	public static final String LABEL_MACHINE = "M\u00c1QUINA";
	public static final String LABEL_TYPE = "TIPO";
	public static final String COLETIVA_ALIAS = "C";
	public static final String INDIVIDUAL_ALIAS = "I";
	public static final String COLETIVA_DESCRIPTION = "COLETIVA";
	public static final String INDIVIDUAL_DESCRITPION = "INDIVIDUAL";	
	public static final String PRODUCT_CAPITALIZE = "Produtos";
	public static final String LABEL_ID = "ID";
	public static final String LABEL_INITIALS = "Iniciais";
	public static final String LABEL_FINALS = "Finais";
	public static final String LABEL_PERIOD = "Per\u00edodo";
	public static final String LABEL_BALANCE = "Saldo";
	public static final String LABEL_LEFT_OVER = "Sobra";
	public static final String LABEL_DEBTED = "Debitado";
	public static final String LABEL_INPUT = "Entrada";
	public static final String LABEL_OUTPUT = "Sa\u00edda";
	public static final String LABEL_BALANCE_INPUT_OUTPUT = "BRUTO (Entradas / Saidas)"; 
	public static final String LABEL_LAUNCH_CAPITALIZE = "Lan\u00e7amentos";  
	public static final String LABEL_DESCRIPTION = "Descri\u00e7\u00e3o";
	public static final String LABEL_CLIENT_PROVIDER = "Cliente / Fornecedor";
	public static final String LABEL_FINANCIAL = "Financeiro";
	public static final String LABEL_HOUR_CAPITALIZE = "Hora";
	public static final String LABEL_USER_CAPITALIZE = "Usu\u00e1rio";
	public static final String LABEL_OPERATION = "Opera\u00e7\u00e3o";
	public static final String LABEL_TOTAL_CAPITALIZE = "Saldo";
	public static final String DATA_CAPITALIZE = "10/06/2020"; 
	public static final String LABEL_USER_NUMBER = "9999";
	public static final String LABEL_TOTAL_SAL_CAPITALIZE = "5,00";
	public static final String LABEL_BILLING ="FATURADO";
	public static final String LABEL_DEBIT_CAPITALIZE = "D\u00e9bito";
	public static final String LABEL_CREDIT_CAPITALIZE = "Cr\u00e9dito";
	public static final String MOVEMENTS_CAPITALIZE = "Movimentos";
	public static final String LABEL_VALUE_DOCUMENTS = "Valor Documento";
	public static final String LABEL_DOCUMENTS = "Documentos";
	public static final String LABEL_MOVEMENT_TYPE = "Tipo Movimento";
	public static final String LABEL_EMISSION = "Emiss\u00e3o"; 
	public static final String LABEL_EXPIRATION = "Vencimento";
	public static final String LABEL_COMPANYS = "Empresas";
	public static final String LABEL_COMPANYS_PARENT_S = "Empresa(s)";
	public static final String LABEL_OPERETORS = "Operador(es)";
	public static final String LABEL_COMPANY_CODE = "C\u00f3digo";
	public static final String LABEL_STORE = "Loja";
	public static final String LABEL_ISSUE_DATE = "DATA DE EMISS\u00c3O"; 
	public static final String LABEL_EXPIRATION_DATE = "DATA DE VENCIMENTO";
	public static final String LABEL_MOVEMENT_EXTRACT = "EXTRATO DE MOVIMENTA\u00c7\u00c3O DE CONTAS";
	public static final String LABEL_MOVEMENT_EXTRACT_COMPANY = "EXTRATO DAS EMPRESAS";
	public static final String LABEL_INITIAL_BALANCE = "SALDO INICIAL";
	public static final String LABEL_TOTAL_CREDIT = "TOTAL CR\u00c9DITO";
	public static final String LABEL_TOTAL_DEBIT = "TOTAL D\u00c9BITO";
	public static final String LABEL_ACCOUNT = "CONTA";
	public static final String LABEL_ACCOUNTS = "CONTA(s)";
	public static final String LABEL_START = "IN\u00cdCIO";
	public static final String LABEL_BALANCE_TOTAL = "SALDO";
	public static final String LABEL_ATE = "at\u00e9";
	public static final String LABEL_DATA_PESQUISA = "Data de Pesquisa";
	public static final String LABEL_CASHIER = "FECHAMENTO";
	public static final String LABEL_CLOSING ="CAIXA";
	public static final String LABEL_PREVIEW ="PREVIEW";
	public static final String LABEL_TOTAL_PAYMENT = "Recebido";
	public static final String LABEL_RESIDUE = "RESIDUAL";	
	public static final String LABEL_TRANSFER = "TRANSF.";
	public static final String LABEL_DESPERSA = "DESPESA";
	public static final String LABEL_MOVIMENTO = "MOVEMENTO";
	public static final String LABEL_TRANSFERENCIA = "TRANSFER\u00caNCIA";
	public static final String LABEL_LANCAMENTO = "LAN\u00c7AMENTO";
	public static final String LABEL_TOTAL_RESIDUE = "Residual";	
	public static final String LABEL_TOTAL_MOVEMENTS = "Total Movimentos";
	public static final String LABEL_PENDENT_BEFORE = "Pendências Anteriores";
	public static final String LABEL_TOTAL_STORE = "Total Receber";
	public static final String LABEL_TOTAL_PAY = "Total Recebido";
	public static final String LABEL_TOTAL_DISCOUNT = "Total Desconto";
	public static final String LABEL_PENDENT_ACTUAL = "Pendência Atual";
	public static final String LABEL_PRODUCT = "Produto";
	public static final String LABEL_INPUT_PERIOD = "Entradas Periodo";
	public static final String LABEL_OUTPUT_PERIOD = "Saídas Periodo";
	public static final String LABEL_SOBRA = "Sobra";
	public static final String LABEL_INPUT_DOCUMENT = "Documento Entrada";
	public static final String LABEL_OUTPUT_DOCUMENT = "Documento Saída";
	public static final String LABEL_CASH_CLOSING_TRANSF_LABEL = "TRANSF. FEC. CAIXA";
	public static final String LABEL_CASH_CLOSING_RESIDUE_TRANSF_LABEL = "RESIDUAL TRANSF. FEC. CAIXA";
	public static final String LABEL_CASH_CLOSING_EXPENSE_TRANSF_LABEL = "DESPESA TRANSF. FEC. CAIXA";
	public static final String LABEL_MOVEMENT_DATE = "Data de Mov.";
	public static final String LABEL_INCOMES = "RECEITAS";
	public static final String LABEL_EXPENSES = "DESPESAS";
	public static final String LABEL_INCOME = "RECEITA";
	public static final String LABEL_EXPENSE = "DESPESA";
	public static final String LABEL_GROSS_PROFIT = "LUCRO BRUTO";	
	public static final String LABEL_PERSONAL_EXPENSE_PRL = "DESPESAS C/ PESSOAL - PLR";
	public static final String LABEL_BALANCE_NET_PROFIT = "SALDO LUCRO LIQUIDO";
	public static final String LABEL_TRANSF_CREDITO = "TRANSF. CREDITO";
	public static final String LABEL_TRANSF_DEBITO = "TRANSF. DEBITO";
	public static final String LABEL_PROFIT_DISTRIBUTION = "DIST. LUCRO SOCIOS";
	//public static final String LABEL_PROFIT_DISTRIBUTION_CREDITO = "DIST. LUCRO CREDITO";
	//public static final String LABEL_PROFIT_DISTRIBUTION_DEBITO = "DIST. LUCRO DEBITO";
	public static final String LABEL_EXPENSES_HAB = "Despesas HAB";
	public static final String LABEL_TRANSF_CASHING_CLOSE = "Transf. Fechamento Caixa";
	public static final String LABEL_INCOME_NEGATIVE = "NEGATIVA";
	public static final String LABEL_TRANSF_FEC_CAIXA_ORIGIN = "TRANSF. FEC. CAIXA >> ORIGEM EMPRESA:";
	public static final String LABEL_TRANSF_FEC_CAIXA_DESTINY = "TRANSF. FEC. CAIXA >> DESTINO EMPRESA:";
	public static final String LABEL_RESIDUAL_TRANSF_FEC_CAIXA_DESTINY = "RESIDUAL TRANSF. FEC. CAIXA >> EMPRESA:";
	public static final String LABEL_GROUP = "GRUPO";
	public static final String LABEL_SUB_GROUP = "SUB-GRUPO";
	
	//Bank Account Statement Email
	//20/12/2021
	public static final String LABEL_BANK_ACCOUNT_STATEMENT = "EXTRATO DE CONTA";
	public static final String LABEL_MOVEMENET_ORIGEM = "MOVEMENTO ORIGEM";
	public static final String LABEL_MOVEMENET_DESTINO = "MOVEMENTO DESTINO";
	public static final String LARGER = ">>";
	public static final String INPUT_MOVEMENET_DESTINO_DOCUMENT_NOTE_ALIAS = "EMPRESA";
	public static final String LABEL_BANK_ACCCOUNT = "CONTA";
	public static final String LABEL_CASHING_CLOSE_COMPANYS = "EMPRESA(S)";
	public static final String LABEL_DESINY = "DESTINO";
	public static final String LABEL_ORIGIN = "ORIGEM";
	public static final String LABEL_ORIGINS = "ORIGENS";
	public static final String LABEL_OBS = "OBS:";
	public static final String INPUT_MOVEMENET_DOCUMENT_NOTE_ALIAS = "MOVIMENTO";
	
	//Provider Statement
	public static final String LABEL_PROVIDER_STATEMENT = "EXTRATO DE FORNECEDOR";
	
	//Cash Statement
	public static final String LABEL_CASH_STATEMENT = "EXTRATO DE CAIXA";
	
	public static final String LABEL_TITLE_REPORT_ERRO_EMAIL = "ERRO PROCESSA MOVIMENTO PRODUTO.";
	
	//Dre
	//08/09/2022
	public static final String LABEL_TITLE_REPORT_DRE_PDF = "RELATÓRIO DRE";
	public static final String LABEL_DESCRIPTION_DRE = "DESCRI\u00c7\u00c3O";
	public static final String LABEL_PERCENT_SALES = " % VALOR";
	
	//Log 
	//Create Date 30/09/2022
	public static final String LABEL_WINDOW_ALTER = "TELA ALTERADO";
	public static final String LABEL_COLUMN_ALTER = "CAMPO ALTERADO";
	public static final String LABEL_TITLE_REPORT_LOG_SYSTEM_PDF = "RELATÓRIO LOG DO SISTEMA";
	public static final String LABEL_OLD = "ANTIGO";
	public static final String LABEL_NEW = "NOVO";
	
	public static final String LABEL_RECEIVABLE_VALUE = "Valores a receber";
	public static final String LABEL_VALUE_RECEIVED = "VALOR RECEBIDO";
	public static final String LABEL_PRESENT_PENDENCY = "PEND\u00caNCIA ATUAL";
	public static final String LABEL_PREVIOUS_PENDENCY = "PEND\u00caNCIA ANTERIO";
	
	//Create Date 22/05/2023
	public static final String LAUNCH_MOVMENT_TYPE_EXPENSE = "EXPENSE";
	public static final String LAUNCH_MOVMENT_TYPE_TRANSFER_BALANCE = "TRANSFER BALANCE";
	public static final String LAUNCH_MOVMENT_TYPE_LAUNCH = "LAUNCH";
	public static final String LAUNCH_MOVMENT_PRODUCT_MOVEMENT = "PRODUCT MOVEMENT";
	
	//21/08/2023
	public static final String LABEL_CASHIER_STATEMENT = "EXTRATO DE CAIXA";
	
	//28/10/2023
	public static final String LABEL_INC0ME_FULFILLED = "RECIMENTO \n REALIZADO";	
	public static final String LABEL_INC0ME_EXPECTED = "RECIMENTO \n PREVISTO";
	public static final String LABEL_PAYMENT_FULFILLED = "PAGAMENTO \n REALIZADO";	
	public static final String LABEL_PAYMENT_EXPECTED = "PAGAMENTO \n PREVISTO";
	public static final String LABEL_TRANSFER_BANK_ACCOUNT = "TRANFERENCIA CONTAS";
	public static final String LABEL_TRANSFER_BANK_ACCOUNT_CREDIT = "TRANSFERENCIA \n CREDITO";
	public static final String LABEL_TRANSFER_BANK_ACCOUNT_DEBIT = "TRANSFERENCIA \n DEBITO";
	
	//02/01/2024
	public static final String LABEL_LAUNCH_CATEGORY = "Categoria de Lançamentos";
	public static final String LABEL_BALANCE_MONTH_BEFORE = "Saldo do m\u00eas anterior";
	public static final String LABEL_TOTAL_INCOME = "Total de Recebimentos";
	public static final String LABEL_TOTAL_PAYMENTS = "Total de Pagamentos";
	public static final String LABEL_TOTAL_TRANSFER = "Total de Transfer\u00eancias";
}