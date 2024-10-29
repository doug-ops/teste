package com.manager.systems.common.vo;

public enum DocumentMovementType 
{
	DUPLICATA(1, "Duplicata"),
	MOVIMENTO_PRODUCT(4, "Movimento Maquina"),
	LANCAMENTOS(2, "Lancamentos"),
	TRANSFER_SALDO(3, "Documento Fiscal");
	
	private int id;
	private String description;
	
	private DocumentMovementType(final int id, final String description) 
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
	
	public static DocumentMovementType valueOfType(final int id)
	{
		DocumentMovementType result = null;
		for (final DocumentMovementType item : DocumentMovementType.values()) 
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