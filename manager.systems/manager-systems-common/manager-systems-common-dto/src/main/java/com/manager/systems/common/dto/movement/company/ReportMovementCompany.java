package com.manager.systems.common.dto.movement.company;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;

@Getter
public class ReportMovementCompany implements Serializable{
	
	private static final long serialVersionUID = 4548550814193572280L;

	private Map<Integer, ReportMovementCompanyGroup> movementsGroups = new TreeMap<>();
	private List<ReportMovementCompanyGroup> movementsGroupsList = new ArrayList<>();
	
	public void addProductMovement(final ReportMovementCompanyProduct item) {		
		ReportMovementCompanyGroup group = this.movementsGroups.get(item.getGroupId());
		if(group==null) {
			group = new ReportMovementCompanyGroup();
			group.setGroupId(item.getGroupId());
			group.setGroupDescription(item.getGroupDescription());
			group.setExecutionOrder(item.getExecutionOrder());
			group.setGroupExecutionOrder(item.getGroupExecutionOrder());
			group.setGroupItemExecutionOrder(item.getGroupItemExecutionOrder());
		}		
		group.addProductMovement(item);
		this.movementsGroups.put(item.getGroupId(), group);
	}
	
	public void addTransferMovement(final ReportMovementCompanyFinancialTransfer item) {			
		ReportMovementCompanyGroup group = this.movementsGroups.get(item.getGroupId());
		if(group==null) {
			group = new ReportMovementCompanyGroup();
			group.setGroupId(item.getGroupId());
			group.setGroupDescription(item.getGroupDescription());
			group.setExecutionOrder(item.getExecutionOrder());
			group.setGroupExecutionOrder(item.getGroupExecutionOrder());
			group.setGroupItemExecutionOrder(item.getGroupItemExecutionOrder());
		}		
		if(group.getMapProductsMovements()==null || group.getMapProductsMovements().size()==0) {
			
		}
		group.addTransferMovement(item);
		this.movementsGroups.put(item.getGroupId(), group);
	}
	
	public void sorListMovement()
	{
		double initialBalance = 0d;
		double finalBalance = 0d;
		for(final Map.Entry<Integer, ReportMovementCompanyGroup> entry : this.movementsGroups.entrySet()){
			entry.getValue().sorListMovement();
			if(entry.getValue().getProductsMovements()!=null && entry.getValue().getProductsMovements().size()>0) {
				initialBalance += entry.getValue().getFinalBalance();
			}
//			else {
//				initialBalance+=(entry.getValue().getFinalBalance() < 0 ? (entry.getValue().getFinalBalance() * -1) : entry.getValue().getFinalBalance());							
//			}
			this.movementsGroupsList.add(entry.getValue());
		}
		
		Collections.sort(this.movementsGroupsList, new Comparator<ReportMovementCompanyGroup>()
		{
			@Override
			public int compare(final ReportMovementCompanyGroup o1, final ReportMovementCompanyGroup o2)
			{
				return Integer.compare(o1.getOrder(), o2.getOrder());
			}
		});		
		
		final int lastGroupIndex = (this.getMovementsGroupsList().size()-1);
		final ReportMovementCompanyGroup lastGroup = this.movementsGroupsList.get(lastGroupIndex);
		if((lastGroup.getProductsMovements()==null || lastGroup.getProductsMovements().size()==0)) {
			//initialBalance -= (lastGroup.getFinalBalance() < 0  ? (lastGroup.getFinalBalance()*-1) : lastGroup.getFinalBalance());
			finalBalance = (initialBalance + lastGroup.getFinalBalance());
			lastGroup.setInitialBalance(initialBalance); 
			lastGroup.setFinalBalance(finalBalance);
			this.movementsGroupsList.remove(lastGroupIndex);
			this.movementsGroupsList.add(lastGroupIndex, lastGroup);
		}		
		this.movementsGroups = null;
	}
	
	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		final String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		final Field[] fields = this.getClass().getDeclaredFields();

		for (final Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(this));
			} catch (final IllegalAccessException ex) {
	
			}
			result.append(newLine);
		}
		result.append("}");
		return result.toString();
	}
}