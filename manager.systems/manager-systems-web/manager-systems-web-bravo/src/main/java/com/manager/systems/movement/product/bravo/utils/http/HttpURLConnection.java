package com.manager.systems.movement.product.bravo.utils.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.manager.systems.movement.product.bravo.utils.ConstantDataManager;

public class HttpURLConnection 
{
	private List<String> cookies;
	private HttpsURLConnection conn;

	public void sendPost(final HttpsURL https) throws Exception 
	{
		final URL obj = new URL(https.getUrl());
		this.conn = (HttpsURLConnection) obj.openConnection();

		// Acts like a browser
		this.conn.setUseCaches(false);
		this.conn.setRequestMethod(ConstantDataManager.POST_METHOD);
		this.conn.setRequestProperty(ConstantDataManager.HOST, https.getHost());
		this.conn.setRequestProperty(ConstantDataManager.USER_AGENT, ConstantDataManager.MOZILA_USER_AGENT);
		this.conn.setRequestProperty(ConstantDataManager.ACCEPT, ConstantDataManager.ACCEPT_TYPE_ALL);
		this.conn.setRequestProperty(ConstantDataManager.ACCEPT_LANGUAGE, ConstantDataManager.ACCEPT_LANGUAGE_EN_US);
		for (String cookie : this.cookies) 
		{
			this.conn.addRequestProperty(ConstantDataManager.COOKIE, cookie.split(";", 1)[0]);
		}
		this.conn.setRequestProperty(ConstantDataManager.CONNECTION, ConstantDataManager.CONNECTION_KEEP_ALIVE);
		this.conn.setRequestProperty(ConstantDataManager.REFERER, https.getReferer());
		this.conn.setRequestProperty(ConstantDataManager.CONTENT_TYPE, ConstantDataManager.CONTENT_TYPE_FORM);
		this.conn.setRequestProperty(ConstantDataManager.CONTENT_LENGTH, Integer.toString(https.getParams().length()));

		this.conn.setDoOutput(true);
		this.conn.setDoInput(true);

		// Send post request
		final DataOutputStream wr = new DataOutputStream(this.conn.getOutputStream());
		wr.writeBytes(https.getParams());
		wr.flush();
		wr.close();

		int responseCode = this.conn.getResponseCode();

		final BufferedReader in = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
		String inputLine;
		final StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) 
		{
			response.append(inputLine);
		}
		in.close();
	}

	public String getPageContent(final String url) throws Exception 
	{
		final URL obj = new URL(url);
		this.conn = (HttpsURLConnection) obj.openConnection();

		// default is GET
		this.conn.setRequestMethod(ConstantDataManager.GET_METHOD);

		this.conn.setUseCaches(false);

		// act like a browser
		this.conn.setRequestProperty(ConstantDataManager.USER_AGENT, ConstantDataManager.MOZILA_USER_AGENT);
		this.conn.setRequestProperty(ConstantDataManager.ACCEPT, ConstantDataManager.ACCEPT_TYPE_ALL);
		this.conn.setRequestProperty(ConstantDataManager.ACCEPT_LANGUAGE, ConstantDataManager.ACCEPT_LANGUAGE_EN_US);
		
		if (this.cookies != null) 
		{
			for (String cookie : this.cookies) 
			{
				this.conn.addRequestProperty(ConstantDataManager.COOKIE, cookie.split(";", 1)[0]);
			}
		}
		
		this.conn.getResponseCode();

		final BufferedReader in = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) 
		{
			response.append(inputLine);
		}
		in.close();

		// Get the response cookies
		setCookies(this.conn.getHeaderFields().get(ConstantDataManager.SET_COOKIE));

		return response.toString();
	}

	public static String getFormParams(final String html, final String username, final String password) 
	{
		final Document doc = Jsoup.parse(html);
		
		final Element loginform = doc.getElementById("l_form");
		final Elements inputElements = loginform.getElementsByTag("input");
		final List<String> paramList = new ArrayList<String>();
		for (final Element inputElement : inputElements) 
		{
			String key = inputElement.attr("name");
			String value = inputElement.attr("value");

			if (key.equals("username"))
			{
				value = username;
			}				
			else if (key.equals("password"))
			{
				value = password;
			}
			if(""!=value)
			{
				//paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
				paramList.add(key + "=" + value);
			}
		}

		// build parameters list
		final StringBuilder result = new StringBuilder();
		for (String param : paramList) 
		{
			if (result.length() == 0) 
			{
				result.append(param);
			} 
			else 
			{
				result.append("&" + param);
			}
		}
		return result.toString();
	}

	public List<String> getCookies() 
	{
		return this.cookies;
	}

	public void setCookies(final List<String> cookies) 
	{
		this.cookies = cookies;
	}
}
