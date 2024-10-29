package com.manager.systems.web.jobs.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

public class JobDTO 
{
	private int id;
	private int parentId;
	private String description;
	private String syncTimer;
	private String itemName;
	private int recordsProcessed;
    private String initialRecord;
    private String finalRecord;
    private byte[] versionInitialRecord;
    private byte[] versionFinalRecord;
    private boolean processingStatus;
    private String processingMessage;
    private LocalDateTime processingData;
    private boolean inactive;
    private long userChange;
    
    public JobDTO() 
    {
		super();
	}

	public final int getId() 
	{
		return this.id;
	}

	public final void setId(final int id)
	{
		this.id = id;
	}

	public final int getParentId() 
	{
		return this.parentId;
	}

	public final void setParentId(final int parentId) 
	{
		this.parentId = parentId;
	}

	public final String getDescription()
	{
		return this.description;
	}

	public final void setDescription(final String description) 
	{
		this.description = description;
	}

	public final String getSyncTimer() 
	{
		return this.syncTimer;
	}

	public final void setSyncTimer(final String syncTimer)
	{
		this.syncTimer = syncTimer;
	}
	
	public final String getItemName() 
	{
		return this.itemName;
	}
	
	public final void setItemName(final String itemName) 
	{
		this.itemName = itemName;
	}

	public final int getRecordsProcessed()
	{
		return this.recordsProcessed;
	}

	public final void setRecordsProcessed(final int recordsProcessed) 
	{
		this.recordsProcessed = recordsProcessed;
	}

	public final String getInitialRecord() 
	{
		return this.initialRecord;
	}

	public final void setInitialRecord(final String initialRecord) 
	{
		this.initialRecord = initialRecord;
	}

	public final String getFinalRecord()
	{
		return this.finalRecord;
	}

	public final void setFinalRecord(final String finalRecord) 
	{
		this.finalRecord = finalRecord;
	}

	public final boolean isProcessingStatus()
	{
		return this.processingStatus;
	}

	public final void setProcessingStatus(final boolean processingStatus)
	{
		this.processingStatus = processingStatus;
	}

	public final String getProcessingMessage()
	{
		return this.processingMessage;
	}

	public final void setProcessingMessage(final String processingMessage) 
	{
		this.processingMessage = processingMessage;
	}

	public final LocalDateTime getProcessingData()
	{
		return this.processingData;
	}

	public final void setProcessingData(final LocalDateTime processingData) 
	{
		this.processingData = processingData;
	}

	public final boolean isInactive() 
	{
		return this.inactive;
	}

	public final void setInactive(final boolean inactive) 
	{
		this.inactive = inactive;
	}

	public final byte[] getVersionInitialRecord()
	{
		return this.versionInitialRecord;
	}

	public final void setVersionInitialRecord(final byte[] versionInitialRecord) 
	{
		this.versionInitialRecord = versionInitialRecord;
	}

	public final byte[] getVersionFinalRecord() 
	{
		return this.versionFinalRecord;
	}

	public final void setVersionFinalRecord(final byte[] versionFinalRecord) 
	{
		this.versionFinalRecord = versionFinalRecord;
	}

	public final long getUserChange()
	{
		return this.userChange;
	}

	public final void setUserChange(final long userChange)
	{
		this.userChange = userChange;
	}
	
	public boolean canProcess() {
		boolean result = true;
		if(this.processingData!=null) {
			final ZonedDateTime actualDate = ZonedDateTime.now(ZoneId.of(ConstantDataManager.TIMEZONE_SAO_PAULO));
			final long actualDateLong = Long.valueOf(StringUtils.padLeft(String.valueOf(actualDate.getYear()), 4, ConstantDataManager.ZERO_STRING)+
													 StringUtils.padLeft(String.valueOf(actualDate.getMonthValue()), 2, ConstantDataManager.ZERO_STRING)+
													 StringUtils.padLeft(String.valueOf(actualDate.getDayOfMonth()), 2, ConstantDataManager.ZERO_STRING));
			final long lastProcessing = Long.valueOf(StringUtils.padLeft(String.valueOf(this.processingData.getYear()), 4, ConstantDataManager.ZERO_STRING)+
					 								 StringUtils.padLeft(String.valueOf(this.processingData.getMonthValue()), 2, ConstantDataManager.ZERO_STRING)+
					 								 StringUtils.padLeft(String.valueOf(this.processingData.getDayOfMonth()), 2, ConstantDataManager.ZERO_STRING));;
			if(lastProcessing>=actualDateLong) {
				result = false;
			}
		}		
		return result;
	}
}