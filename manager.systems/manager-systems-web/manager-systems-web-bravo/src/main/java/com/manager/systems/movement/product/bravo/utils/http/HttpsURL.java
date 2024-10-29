/**
 * Date create 19/09/2017.
 */
package com.manager.systems.movement.product.bravo.utils.http;

public class HttpsURL 
{
	private String url;
	private String params;
	private String host;
	private String referer;
	
	/**
	 * Constructor.
	 */
	public HttpsURL() 
	{
		super();
	}

	public final String getUrl() 
	{
		return this.url;
	}

	public final String getParams() 
	{
		return this.params;
	}

	public final String getHost() 
	{
		return this.host;
	}

	public final String getReferer() 
	{
		return this.referer;
	}

	public final void setUrl(final String url) 
	{
		this.url = url;
	}

	public final void setParams(final String params) 
	{
		this.params = params;
	}

	public final void setHost(final String host) 
	{
		this.host = host;
	}

	public final void setReferer(final String referer) 
	{
		this.referer = referer;
	}
}