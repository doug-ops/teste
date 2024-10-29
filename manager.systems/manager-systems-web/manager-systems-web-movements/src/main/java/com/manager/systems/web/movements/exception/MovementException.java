package com.manager.systems.web.movements.exception;

public class MovementException extends Exception  
{
	private static final long serialVersionUID = -7564205807850434629L;

	private String message;
	private Integer errorCode;
	 
    public MovementException() 
    {
        super();
    }
 
    public MovementException(final String message) 
    {
        super(message);
        this.message = message;
    }
    
    public MovementException(final String message, final int errorCode) 
    {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
 
    public MovementException(final Throwable cause) 
    {
        super(cause);
    }
 
    @Override
    public String toString() 
    {
        return this.message;
    }
 
    @Override
    public String getMessage() 
    {
        return this.message;
    }

	public final Integer getErrorCode() 
	{
		return this.errorCode;
	}
}