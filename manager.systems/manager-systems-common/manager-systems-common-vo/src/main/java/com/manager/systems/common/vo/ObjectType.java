package com.manager.systems.common.vo;

public enum ObjectType 
{
	CLIENT("CLI", "Cliente"),
	USER("USU", "Usuario"),
	COMPANY("COM", "Empresa"),
	PROVIDER("FOR", "Fornecedor"),
	BANK_ACCOUNT("BANKAC", "Conta Bancaria"), 
	FINANCIAL_GROUP("FINGRU", "Grupo Financeiro"), 
	FINANCIAL_SUB_GROUP("FINSUBGRU", "SubGrupo Financeiro"),
	PROD_GROUP("PROGRU", "Grupo Produto"), 
	PROD_SUB_GROUP("PROSUBGRU", "SubGrupo Produto"),
	PRODUCT("PRO", "Produto"), 
	ACESS_PROFILE("ACCESSPROFILE", "Perfil de Acesso"),
	FINANCIAL_COST_CENTER("FINCOSTCENTER", "Centro de Custo");
	
	
	private String type;
	private String description;
	
	private ObjectType(final String type, final String description) 
	{
		this.type = type;
		this.description = description;
	}

	public final String getType() 
	{
		return this.type;
	}

	public final String getDescription() 
	{
		return this.description;
	}
	
	public static ObjectType valueOfType(final String type)
	{
		ObjectType result = null;
		for (final ObjectType item : ObjectType.values()) 
		{
			if(type.equalsIgnoreCase(item.getType()))
			{
				result = item;
				break;
			}
		}
		return result;
	}
}