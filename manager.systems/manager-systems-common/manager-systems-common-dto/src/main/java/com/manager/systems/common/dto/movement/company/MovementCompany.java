package com.manager.systems.common.dto.movement.company;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dto.movement.product.MovementProductDTO;

public class MovementCompany implements Serializable 
{
	private static final long serialVersionUID = 6972206894250573106L;
	
	private long companyId;
	private String companyDescription;
	private long initialInput;
	private long finalInput;
	private long initialOutput;
	private long finalOutput;
	private long initialClock;
	private long finalClock;
	private double totalMovement;
	private LocalDateTime readingDate;
	private String movementType;
	private long changeUser;
	private boolean offline;
	private boolean enableClockMovement;
	private List<MovementProductDTO> itens = new ArrayList<MovementProductDTO>();
	
	public MovementCompany() 
	{
		super();
	}

	public final long getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId)
	{
		this.companyId = companyId;
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
	
	public final long getChangeUser() 
	{
		return this.changeUser;
	}
	
	public final void setChangeUser(final long changeUser) 
	{
		this.changeUser = changeUser;
	}

	public final String getCompanyDescription() 
	{
		return this.companyDescription;
	}

	public final void setCompanyDescription(final String companyDescription) 
	{
		this.companyDescription = companyDescription;
	}
	
	public final double getTotalMovement() 
	{
		return this.totalMovement;
	}

	public final void setTotalMovement(final double totalMovement) 
	{
		this.totalMovement = totalMovement;
	}
	
	public final List<MovementProductDTO> getItens() 
	{
		return this.itens;
	}
	
	public final boolean isOffline() 
	{
		return this.offline;
	}

	public final void setOffline(final boolean offline)
	{
		this.offline = offline;
	}
	
	public final boolean isEnableClockMovement() {
		return this.enableClockMovement;
	}

	public void setEnableClockMovement(final boolean enableClockMovement) {
		this.enableClockMovement = enableClockMovement;
	}
	
	public final void addItem(final MovementProductDTO item)
	{
		this.itens.add(item);
		this.initialInput += item.getInitialInput();
		this.finalInput += item.getFinalInput();
		this.initialOutput += item.getInitialOutput();
		this.finalOutput += item.getFinalOutput();
		this.initialClock += item.getInitialClock();
		this.finalClock += item.getFinalClock();
		this.totalMovement += item.getTotalMovement();
	}
}