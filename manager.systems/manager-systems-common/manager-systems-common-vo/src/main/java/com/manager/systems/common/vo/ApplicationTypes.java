package com.manager.systems.common.vo;

public enum ApplicationTypes 
{
	PORTAL("PORTAL", "Application Portal"),
	PORTAL_DESENV("PORTAL_DESENV", "Application Portal Desenv"),
	RETAGUARDA("RETAGUARDA", "Application Retaguarda");
	
	private String type;
	private String description;
	
	private ApplicationTypes(final String type, final String description) 
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

	public static ApplicationTypes valueOfType(final String type)
	{
		for (final ApplicationTypes applicationType : ApplicationTypes.values()) 
		{
			if(applicationType.getType().equalsIgnoreCase(type))
			{
				return applicationType;
			}
		}
		throw new IllegalArgumentException(String.format("Aplication Type %s not found: ", type));
	}
}