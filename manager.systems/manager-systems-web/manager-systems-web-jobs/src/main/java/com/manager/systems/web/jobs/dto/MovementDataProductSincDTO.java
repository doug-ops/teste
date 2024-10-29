package com.manager.systems.web.jobs.dto;

import java.util.ArrayList;
import java.util.List;

import com.manager.systems.web.jobs.vo.OperationType;

public class MovementDataProductSincDTO 
{
	private OperationType operationType;
	private byte[] versionRecord;
	private List<MovementDataProductSincItemDTO> itens = new ArrayList<MovementDataProductSincItemDTO>();
	
	public MovementDataProductSincDTO() 
	{
		super();
	}

	public final OperationType getOperationType() 
	{
		return this.operationType;
	}

	public final void setOperationType(final OperationType operationType) 
	{
		this.operationType = operationType;
	}

	public final byte[] getVersionRecord() 
	{
		return this.versionRecord;
	}

	public final void setVersionRecord(final byte[] versionRecord) 
	{
		this.versionRecord = versionRecord;
	}

	public final List<MovementDataProductSincItemDTO> getItens() 
	{
		return this.itens;
	}
	
	public final void addItem(final MovementDataProductSincItemDTO item)
	{
		this.itens.add(item);
	}
}