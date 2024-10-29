package com.manager.systems.common.vo;

public enum PersonType 
{
	FISICA("F", "Fisica"),
	JURIDICA("J", "Juridica");
	
	private String type;
	private String description;
	
	private PersonType(final String type, final String description) 
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
	
	public static PersonType valueOfTipo(final String type)
	{
		for (final PersonType personType : PersonType.values()) 
		{
			if(personType.getType().equalsIgnoreCase(type))
			{
				return personType;
			}
		}
		throw new IllegalArgumentException(String.format("Person Type %s not found.", type));
	}
} 