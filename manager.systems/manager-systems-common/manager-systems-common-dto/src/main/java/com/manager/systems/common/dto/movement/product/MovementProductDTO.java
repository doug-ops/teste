package com.manager.systems.common.dto.movement.product;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MovementProductDTO implements Serializable 
{
	private static final long serialVersionUID = 454222456841587417L;
	
	private long movementId;
	private long productId;
	private long companyId;
	private String productDescription;
	private String companyDescription;
	private long initialInput;
	private long finalInput;
	private long initialOutput;
	private long finalOutput;
	private long initialClock;
	private long finalClock;
	private double totalMovement;
	private String salePrice;
	private String conversionFactor;
	private LocalDateTime readingDate;
	private String readingDateString;
	private String movementType;
	private boolean inactive;
	private long changeUser;
	private boolean offline;
	private String lastProcessing;
	private boolean enableClockMovement;
	private String initialDateString;
	
	public MovementProductDTO() 
	{
		super();
	}

	public final long getMovementId()
	{
		return this.movementId;
	}

	public final void setMovementId(final long movementId)
	{
		this.movementId = movementId;
	}

	public final long getProductId() 
	{
		return this.productId;
	}

	public final void setProductId(final long productId) 
	{
		this.productId = productId;
	}

	public final long getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId)
	{
		this.companyId = companyId;
	}

	public final String getProductDescription() 
	{
		return this.productDescription;
	}

	public final void setProductDescription(final String productDescription)
	{
		this.productDescription = productDescription;
	}

	public final long getInitialInput()
	{
		return this.initialInput;
	}

	public final void setInitialInput(final long initialInput)
	{
		this.initialInput = initialInput;
	}

	public final long getFinalInput() 
	{
		return this.finalInput;
	}

	public final void setFinalInput(final long finalInput) 
	{
		this.finalInput = finalInput;
	}

	public final long getInitialOutput()
	{
		return this.initialOutput;
	}

	public final void setInitialOutput(final long initialOutput) 
	{
		this.initialOutput = initialOutput;
	}

	public final long getFinalOutput()
	{
		return this.finalOutput;
	}

	public final void setFinalOutput(final long finalOutput) 
	{
		this.finalOutput = finalOutput;
	}

	public final long getInitialClock()
	{
		return this.initialClock;
	}

	public final void setInitialClock(final long initialClock)
	{
		this.initialClock = initialClock;
	}

	public final long getFinalClock() 
	{
		return this.finalClock;
	}

	public final void setFinalClock(final long finalClock) 
	{
		this.finalClock = finalClock;
	}

	public final LocalDateTime getReadingDate()
	{
		return this.readingDate;
	}

	public final void setReadingDate(final LocalDateTime readingDate) 
	{
		this.readingDate = readingDate;
	}
	
	public final String getMovementType() 
	{
		return this.movementType;
	}

	public final void setMovementType(final String movementType) 
	{
		this.movementType = movementType;
	}
	
	public final boolean isInactive()
	{
		return this.inactive;
	}
	
	public final void setInactive(final boolean inactive) 
	{
		this.inactive = inactive;
	}
	
	public final long getChangeUser() 
	{
		return this.changeUser;
	}
	
	public final void setChangeUser(final long changeUser) 
	{
		this.changeUser = changeUser;
	}

	public final boolean isOffline() 
	{
		return this.offline;
	}

	public final void setOffline(final boolean offline)
	{
		this.offline = offline;
	}

	public final String getCompanyDescription() 
	{
		return this.companyDescription;
	}

	public final void setCompanyDescription(final String companyDescription) 
	{
		this.companyDescription = companyDescription;
	}

	public final String getLastProcessing() 
	{
		return this.lastProcessing;
	}

	public final void setLastProcessing(final String lastProcessing) 
	{
		this.lastProcessing = lastProcessing;
	}
	
	public final double getTotalMovement() 
	{
		return this.totalMovement;
	}

	public final void setTotalMovement(final double totalMovement) 
	{
		this.totalMovement = totalMovement;
	}
	
	public final String getSalePrice()
	{
		return this.salePrice;
	}

	public final void setSalePrice(final String salePrice) 
	{
		this.salePrice = salePrice;
	}

	public final String getConversionFactor()
	{
		return this.conversionFactor;
	}

	public final void setConversionFactor(final String conversionFactor)
	{
		this.conversionFactor = conversionFactor;
	}

	public final String getReadingDateString() 
	{
		return this.readingDateString;
	}

	public final void setReadingDateString(final String readingDateString) 
	{
		this.readingDateString = readingDateString;
	}
	
	public final boolean isEnableClockMovement() {
		return this.enableClockMovement;
	}

	public final void setEnableClockMovement(final boolean enableClockMovement) {
		this.enableClockMovement = enableClockMovement;
	}
	
	public final String getInitialDateString() 
	{
		return this.initialDateString;
	}

	public final void setInitialDateString(final String initialDateString) 
	{
		this.initialDateString = initialDateString;
	}
}