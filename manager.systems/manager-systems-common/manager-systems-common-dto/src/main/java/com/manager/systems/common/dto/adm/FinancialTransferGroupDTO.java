package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ChangeData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinancialTransferGroupDTO implements Serializable
{
	private static final long serialVersionUID = -2582149694097740897L;

	private int id;
	private int groupItemId;
	private int bankAccountOriginId;
	private int bankAccountDestinyId;
	private int bankAccountAutomaticTransferId;
	private String bankAccountOriginDescription;
	private String bankAccountDestinyDescription;
	private String bankAccountAutomaticTransferDescription;
	private String description;
	private int order;
	private int executionPeriod;
	private boolean inactive;
	private ChangeData changeData;
	private Map<Integer, Map<Integer, FinancialTransferGroupItemDTO>> itens = new TreeMap<Integer, Map<Integer, FinancialTransferGroupItemDTO>>();
	private Map<Integer, List<FinancialTransferProductDTO>> products = new TreeMap<Integer, List<FinancialTransferProductDTO>>();
	private boolean saveAll;
	private Date initialExecution;
	private String initialExecutionString;
    private int weekDay;
    private boolean fixedDay;
    private boolean fixedDayMonth;
    private boolean automaticProcessing;
    private String[] executionDays;
    private String executionTime;
	
	public FinancialTransferGroupDTO() 
	{
		super();
	}
	
	public final int getInactiveInt()
	{
		return (this.inactive ? 1 : 0);
	}
	
	public void setExecutionDayToArray(final String executionDaysString) {
		final List<String> daysResult = new ArrayList<>();
		if(!StringUtils.isNull(executionDaysString)) {
			final String[] daysArray = executionDaysString.split(ConstantDataManager.VIRGULA_STRING);
			if(daysArray != null && daysArray.length > 0) {
				for(int i = 0; i < daysArray.length; i++) {
					if(StringUtils.isLong(daysArray[i].trim())) {
						daysResult.add(daysArray[i].trim());
					}
				}
			}
		}
		
		this.executionDays = new String[daysResult.size()];
		daysResult.toArray(this.executionDays);
	}
	public final String getExecutionDaysString() {
        final StringBuilder result = new StringBuilder();
        if(executionDays != null && executionDays.length > 0) {
	        int countExecutionDay = 0;
        	for(int i = 0; i < executionDays.length; i++) {
        		if(StringUtils.isLong(executionDays[i])) {
            		if(countExecutionDay > 0) {
            			result.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
            		}
            		result.append(Integer.parseInt(executionDays[i].trim()));
            		countExecutionDay++;        			
        		}
        	}
        }
        return result.toString();
	}

	public void setInitialExecution(Date initialExecution) {
		this.initialExecution = initialExecution;
		if(this.initialExecution != null) {
			this.initialExecutionString = StringUtils.formatDate(this.initialExecution, StringUtils.DATE_PATTERN_DD_MM_YYYY);
		}
	}

	public final List<FinancialTransferGroupItemDTO> getItensList()
	{
		final List<FinancialTransferGroupItemDTO> list = new ArrayList<FinancialTransferGroupItemDTO>();
		for (final Map.Entry<Integer, Map<Integer, FinancialTransferGroupItemDTO>> itemMap : this.itens.entrySet()) 
		{
			for (final Map.Entry<Integer, FinancialTransferGroupItemDTO> subItemMap : itemMap.getValue().entrySet()) 
			{
				list.add(subItemMap.getValue());
			}
		}
		return list;
	}
	
	public final void addItem(final FinancialTransferGroupItemDTO item)
	{
		Map<Integer, FinancialTransferGroupItemDTO> itensMap = this.itens.get(item.getGroupId());
		if(itensMap==null)
		{
			itensMap = new TreeMap<Integer, FinancialTransferGroupItemDTO>();
		}
		itensMap.put(item.getId(), item);
		this.itens.put(item.getGroupId(), itensMap);
	}
	
	public final String getProductsIdsByGroupItem(final int groupItemId)
	{
		final StringBuilder itensId = new StringBuilder();
		final List<FinancialTransferProductDTO> products = this.products.get(groupItemId);
		if(products!=null && products.size()>0)
		{
			int count = 0;
			for (final FinancialTransferProductDTO product : products) 
			{
				if(count>0)
				{
					itensId.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);					
				}
				itensId.append(product.getProductId());
				count++;
			}			
		}
		return itensId.toString();
	}
	
	public final void addProduct(final int key, final FinancialTransferProductDTO item)
	{
		List<FinancialTransferProductDTO> itens = this.products.get(key);
		if(itens==null)
		{
			itens = new ArrayList<FinancialTransferProductDTO>();
		}
		itens.add(item);
		this.products.put(key, itens);
	}
	
	public final void convertArrayProductsIdToList(final int groupId, final String products)
	{
		if(!StringUtils.isNull(products))
        {
        	final List<String> itens = Arrays.asList(products.split(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING));
        	if(itens!=null && itens.size()>0)
        	{
        		for (final String item : itens) 
        		{
            		final FinancialTransferProductDTO product = new FinancialTransferProductDTO();
            		product.setBankAccountOriginId(this.bankAccountOriginId);
            		product.setBankAccountDestinyId(this.bankAccountDestinyId);
            		product.setProductId(Long.valueOf(item));
            		product.setChangeData(this.changeData);
            		this.addProduct(groupId, product);
				}
        	}
        }
	}
	
	public final void convertArrayProductsIdToList(final int groupId, final String[] products)
	{
		if(products!=null && products.length>0)
        {
			final List<String> itens = Arrays.asList(products);
        	if(itens!=null && itens.size()>0)
        	{
	    		for (final String item : products) 
	    		{
	        		final FinancialTransferProductDTO product = new FinancialTransferProductDTO();
	        		product.setBankAccountOriginId(this.bankAccountOriginId);
	        		product.setBankAccountDestinyId(this.bankAccountDestinyId);
	        		product.setProductId(Long.valueOf(item));
	        		product.setChangeData(this.changeData);
	        		this.addProduct(groupId, product);
				}
        	}
        }
	}
	
	public void validateFormSave() throws Exception
	{
		if(this.id==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_TRANSFER_GROUP_ID, String.valueOf(this.id)));
		}
		if(this.bankAccountOriginId==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_BANK_ACCOUNT_ORIGIN, String.valueOf(this.bankAccountOriginId)));
		}
		if(StringUtils.isNull(this.description))
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_DESCRIPTION_GROUP, com.manager.systems.common.utils.ConstantDataManager.BLANK));
		}
		if(this.order==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_EXECUTION_ORDER_GROUP, com.manager.systems.common.utils.ConstantDataManager.BLANK));
		}
		if(this.isSaveAll())
		{
			if(this.itens==null || this.itens.size()==0)
			{
				throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_TRANSFER_GROUP_ITENS, com.manager.systems.common.utils.ConstantDataManager.BLANK));
			}
			for (final Map.Entry<Integer, Map<Integer, FinancialTransferGroupItemDTO>> itemMap : this.itens.entrySet()) 
			{
				for(Map.Entry<Integer, FinancialTransferGroupItemDTO> subItemMap : itemMap.getValue().entrySet())
				{
					final FinancialTransferGroupItemDTO item = subItemMap.getValue();
					if(item.getId()==0)
					{
						throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_TRANSFER_ID, String.valueOf(item.getId())));
					}
					//if(StringUtils.isNull(item.getDescription()))
					//{
					//	throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_DESCRIPTION_TRANSFER_ITEM, com.manager.systems.common.utils.ConstantDataManager.BLANK));
					//}
					if(item.getProviderId()==0)
					{
						throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_PROVIDER_TRANSFER_ITEM, com.manager.systems.common.utils.ConstantDataManager.BLANK));
					}
					if(item.getValueTransfer()==0 && item.getExpense() != 4)
					{
						throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_VALUE_TRANSFER_ITEM, com.manager.systems.common.utils.ConstantDataManager.BLANK));
					}
					if(item.getOrder()==0)
					{
						throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_EXECUTION_ORDER_TRANSFER_ITEM, com.manager.systems.common.utils.ConstantDataManager.BLANK));
					}	
				}			
			}			
		}
	}
	
	public void validateFormClone() throws Exception
	{
		if(this.bankAccountOriginId==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_BANK_ACCOUNT_ORIGIN, String.valueOf(this.bankAccountOriginId)));
		}
		if(this.bankAccountDestinyId==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_BANK_ACCOUNT_DESTINY, String.valueOf(this.bankAccountOriginId)));
		}
		if(this.bankAccountOriginId==this.bankAccountDestinyId)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_BANK_ACCOUNT_ORIGIN_DESTINY_NOT_SAME, String.valueOf(this.bankAccountOriginId), String.valueOf(this.bankAccountDestinyId)));
		}
	}
	
	public void validateFormGet() throws Exception
	{
		if(this.bankAccountOriginId==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_BANK_ACCOUNT_ORIGIN, String.valueOf(this.bankAccountOriginId)));
		}
	}
	
	public void validateFormEditTransfer() throws Exception
	{
		if(this.id==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_TRANSFER_GROUP_ID, String.valueOf(this.id)));
		}
		if(this.bankAccountOriginId==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_BANK_ACCOUNT_ORIGIN, String.valueOf(this.bankAccountOriginId)));
		}
	}
	
	public void validateFormEditGroupItemTransfer() throws Exception
	{
		if(this.id==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_TRANSFER_GROUP_ID, String.valueOf(this.id)));
		}
		if(this.groupItemId==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_TRANSFER_ID, String.valueOf(this.groupItemId)));
		}
		if(this.bankAccountOriginId==0)
		{
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_BANK_ACCOUNT_ORIGIN, String.valueOf(this.bankAccountOriginId)));
		}
	}
}