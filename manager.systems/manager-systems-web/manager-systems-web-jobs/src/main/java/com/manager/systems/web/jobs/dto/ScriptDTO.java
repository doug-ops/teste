package com.manager.systems.web.jobs.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ScriptDTO
{
	private String tableName;
	private Map<Integer, String> keyColumnsTable = new TreeMap<Integer, String>();
	private int orderExecution;
	private List<String> scriptItens = new ArrayList<String>();
	private byte[] initialRowVersion;
	private byte[] finalRowVersion;
	private int rowCount;
	private boolean isExecution;
	private String messageExecution;
	private LocalDateTime executionDate;
	
	public ScriptDTO() 
	{
		super();
	}

	public final String getTableName()
	{
		return this.tableName;
	}

	public final void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public final Map<Integer, String> getKeyColumnsTable() 
	{
		return this.keyColumnsTable;
	}
	
	public final void addKeyColumnTable(final int keyOrder, final String keyName)
	{
		this.keyColumnsTable.put(keyOrder, keyName);
	}
	
	public final List<String> getScriptItens() 
	{
		return this.scriptItens;
	}
	
	public final void addScriptItem(final String script)
	{
		this.scriptItens.add(script);
	}

	public final int getOrderExecution()
	{
		return this.orderExecution;
	}

	public final void setOrderExecution(final int orderExecution)
	{
		this.orderExecution = orderExecution;
	}

	public final byte[] getInitialRowVersion() 
	{
		return this.initialRowVersion;
	}

	public final void setInitialRowVersion(final byte[] initialRowVersion) 
	{
		this.initialRowVersion = initialRowVersion;
	}

	public final byte[] getFinalRowVersion()
	{
		return this.finalRowVersion;
	}

	public final void setFinalRowVersion(final byte[] finalRowVersion) 
	{
		this.finalRowVersion = finalRowVersion;
	}

	public final int getRowCount()
	{
		return this.rowCount;
	}

	public final void setRowCount(final int rowCount)
	{
		this.rowCount = rowCount;
	}

	public final boolean isExecution() 
	{
		return this.isExecution;
	}

	public final void setExecution(final boolean isExecution) 
	{
		this.isExecution = isExecution;
	}

	public final String getMessageExecution() 
	{
		return this.messageExecution;
	}

	public final void setMessageExecution(final String messageExecution)
	{
		this.messageExecution = messageExecution;
	}

	public final LocalDateTime getExecutionDate() 
	{
		return this.executionDate;
	}

	public final void setExecutionDate(final LocalDateTime executionDate) 
	{
		this.executionDate = executionDate;
	}	
}