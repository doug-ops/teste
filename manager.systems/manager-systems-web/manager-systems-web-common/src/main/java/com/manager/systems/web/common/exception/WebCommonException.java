package com.manager.systems.web.common.exception;

public class WebCommonException extends Exception  
{
	private static final long serialVersionUID = 2427697854293829058L;

	private String message;
	private Integer errorCode;
	 
    public WebCommonException() 
    {
        super();
    }
 
    public WebCommonException(final String message) 
    {
        super(message);
        this.message = message;
    }
    
    public WebCommonException(final String message, final int errorCode) 
    {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
 
    public WebCommonException(final Throwable cause) 
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