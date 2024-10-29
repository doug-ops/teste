package com.manager.systems.common.vo;

public enum DocumentMovementStatus 
{
	ABERTO(1, "ABERTO"),
	BAIXADO(2, "BAIXADO"),
	CONFERIDO(3, "CO"),
	PENDENTE(4, "PE"),
	PROVISIONADO(5, "PR");
	
	private int id;
	private String description;
	
	private DocumentMovementStatus(final int id, final String description) 
	{
		this.id = id;
		this.description = description;
	}

	public final int getId() 
	{
		return this.id;
	}

	public final String getDescription() 
	{
		return this.description;
	}
	
	public static DocumentMovementStatus valueOfType(final int id)
	{
		DocumentMovementStatus result = null;
		for (final DocumentMovementStatus item : DocumentMovementStatus.values()) 
		{
			if(id == item.getId())
			{
				result = item;
				break;
			}
		}
		return result;
	}
}