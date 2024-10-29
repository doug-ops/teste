package com.manager.systems.common.vo;

import java.lang.reflect.Field;

public class Address
{
	private String street;
	private String number;
	private String complement;
	private String district;
	private long zipCode;
	private City city;
	private State state;
	private Country county;
	
	/**
	 * Default Constructor.
	 */
	public Address() 
	{
		super();
	}
	
	public final String getStreet()
	{
		return this.street;
	}

	public final void setStreet(final String street) 
	{
		this.street = street;
	}

	public final String getNumber()
	{
		return this.number;
	}

	public final void setNumber(final String number) 
	{
		this.number = number;
	}

	public final String getComplement() 
	{
		return this.complement;
	}

	public final void setComplement(final String complement) 
	{
		this.complement = complement;
	}

	public final String getDistrict()
	{
		return this.district;
	}

	public final void setDistrict(final String district)
	{
		this.district = district;
	}

	public final long getZipCode()
	{
		return this.zipCode;
	}

	public final void setZipCode(final long zipCode)
	{
		this.zipCode = zipCode;
	}

	public final City getCity() 
	{
		return this.city;
	}

	public final void setCity(final City city) 
	{
		this.city = city;
	}

	public final State getState()
	{
		return this.state;
	}

	public final void setState(final State state)
	{
		this.state = state;
	}

	public final Country getCounty() 
	{
		return this.county;
	}

	public final void setCounty(final Country county) 
	{
		this.county = county;
	}

	@Override
	public String toString() 
	{
		final StringBuilder result = new StringBuilder();
		final String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		final Field[] fields = this.getClass().getDeclaredFields();

		for (final Field field : fields) 
		{
			result.append("  ");
			try 
			{
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(this));
			} 
			catch (final IllegalAccessException ex) 
			{
	
			}
			result.append(newLine);
		}
		result.append("}");
		return result.toString();
	}	
}