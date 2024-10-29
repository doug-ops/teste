package com.manager.systems.common.vo;

import java.io.Serializable;

import com.manager.systems.common.utils.ConstantDataManager;

public class Combobox implements Serializable
{
	private static final long serialVersionUID = 4071983878794525256L;

	private String key;
	private String key1;
	private String key2;
	private String key3;
	private String key4;
	private String value;
	
	public Combobox() 
	{
		super();
	}
	
	public Combobox(final String key, final String value) 
	{
		super();
		this.key = key;
		this.value = value;
	}
	
	public Combobox(final String key, final String value, final String key1) 
	{
		super();
		this.key = key;
		this.value = value;
		this.key1 = key1;
	}
	
	public Combobox(final String key, final String value, final String key1, final String key2) 
	{
		super();
		this.key = key;
		this.value = value;
		this.key1 = key1;
		this.key2 = key2;
	}
	
	public Combobox(final String key, final String value, final String key1, final String key2, final String key3) 
	{
		super();
		this.key = key;
		this.value = value;
		this.key1 = key1;
		this.key2 = key2;
		this.key3 = key3;
	}
	
	public Combobox(final String key, final String value, final String key1, final String key2, final String key3, final String key4) 
	{
		super();
		this.key = key;
		this.value = value;
		this.key1 = key1;
		this.key2 = key2;
		this.key3 = key3;
		this.key4 = key4;
	}


	public final String getKey() 
	{
		return this.key;
	}

	public final void setKey(final String key) 
	{
		this.key = key;
	}

	public final String getValue() 
	{
		return this.value;
	}
	
	public final String getKeyValueConcact()
	{
		return (this.key + ConstantDataManager.SPACE + ConstantDataManager.TRACO + ConstantDataManager.SPACE + this.value);
	}
	
	public final String getKeyKey3Concact()
	{
		return (this.key + ConstantDataManager.UNDERSCORE + this.key3);
	}
	
	public final String getValueKeyConcact()
	{
		return (this.value + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + this.key +  ConstantDataManager.PARENTESES_RIGHT);
	}
	
	public final String getKey4ValueKeyConcact()
	{
		return (this.key4 + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + this.key3 +  ConstantDataManager.PARENTESES_RIGHT + ConstantDataManager.SPACE + ConstantDataManager.BARRA + ConstantDataManager.SPACE + this.value + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + this.key +  ConstantDataManager.PARENTESES_RIGHT);
	}

	public final void setValue(final String value) 
	{
		this.value = value;
	}

	public final String getKey1()
	{
		return this.key1;
	}

	public final void setKey1(final String key1) 
	{
		this.key1 = key1;
	}

	public final String getKey2()
	{
		return this.key2;
	}

	public final void setKey2(final String key2)
	{
		this.key2 = key2;
	}

	public final String getKey3() {
		return this.key3;
	}

	public void setKey3(final String key3) {
		this.key3 = key3;
	}

	public final String getKey4() {
		return this.key4;
	}

	public final void setKey4(final String key4) {
		this.key4 = key4;
	}
}