package com.manager.systems.web.financial.exception;

public class FinancialException extends Exception  
{
	private static final long serialVersionUID = -7929351598864966359L;

	private String message;
	private Integer errorCode;
	 
    public FinancialException() 
    {
        super();
    }
 
    public FinancialException(final String message) 
    {
        super(message);
        this.message = message;
    }
    
    public FinancialException(final String message, final int errorCode) 
    {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
 
    public FinancialException(final Throwable cause) 
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