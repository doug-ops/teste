package com.manager.systems.common.vo;

public class ProductInOut 
{
	private long initialIn;
	private long finalInt;
	private long initialOut;
	private long finalOut;
	
	public ProductInOut() 
	{
		super();
	}

	public final long getInitialIn() 
	{
		return this.initialIn;
	}

	public final void setInitialIn(final long initialIn) 
	{
		this.initialIn = initialIn;
	}

	public final long getFinalInt() 
	{
		return this.finalInt;
	}

	public final void setFinalInt(final long finalInt) 
	{
		this.finalInt = finalInt;
	}

	public final long getInitialOut() 
	{
		return this.initialOut;
	}

	public final void setInitialOut(final long initialOut) 
	{
		this.initialOut = initialOut;
	}

	public final long getFinalOut() 
	{
		return this.finalOut;
	}

	public final void setFinalOut(final long finalOut) 
	{
		this.finalOut = finalOut;
	}
}