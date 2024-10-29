package com.manager.systems.common.dto.movement.company;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.ConstantDataManager;

public class PreviewMovementCompanyDTO implements Serializable
{
	private static final long serialVersionUID = 8376214560768445645L;
	
	private double movementBalance;
	private double initialBalance;
	private double finalBalance;
	private long compnayId;
	private String companyDescription;
	private int bankAccountId;
	private String bankAccountDescription;
	private long userChange;
	private String userName;
	private boolean offline;
	private boolean preview;
	private double totalMovementInput;
	private double totalMovementOutput;
	private double balanceMovementInputOutput;
	private double balanceTransfer;
	private double balanceMovement;
	private long documentParentId;
	private Map<String, List<PreviewMovementCompanyItemDTO>> movementProductsIn = new TreeMap<String, List<PreviewMovementCompanyItemDTO>>();
	private Map<String, List<PreviewMovementCompanyItemDTO>> movementProductsOut = new TreeMap<String, List<PreviewMovementCompanyItemDTO>>();
	private Map<String, List<PreviewMovementCompanyItemDTO>> movementProducts = new TreeMap<String, List<PreviewMovementCompanyItemDTO>>();
	private Map<String, List<PreviewMovementCompanyItemDTO>> movementTransfers = new TreeMap<String, List<PreviewMovementCompanyItemDTO>>();
	private String movementsId;
	private List<Integer> listBanckAccountIdOutDocumentParent = new ArrayList<>();
	private ReportMovementCompany movementCompany;
	private Date movementDate;	

	public PreviewMovementCompanyDTO() 
	{
		super();
	}
	
	public final double getInitialBalance() 
	{
		return this.initialBalance;
	}

	public final void setInitialBalance(final double initialBalance)
	{
		this.initialBalance = initialBalance;
	}

	public final double getFinalBalance()
	{
		return this.finalBalance;
	}

	public final void calculateFinalBalance() 
	{
		this.finalBalance = (this.initialBalance + balanceMovement);
	}

	public final long getCompnayId() 
	{
		return this.compnayId;
	}

	public final void setCompnayId(final long compnayId)
	{
		this.compnayId = compnayId;
	}

	public final String getCompanyDescription() 
	{
		return this.companyDescription;
	}

	public final void setCompanyDescription(final String companyDescription)
	{
		this.companyDescription = companyDescription;
	}
	
	public final long getUserChange() 
	{
		return this.userChange;
	}

	public final void setUserChange(final long userChange) 
	{
		this.userChange = userChange;
	}

	public final boolean isOffline() 
	{
		return this.offline;
	}

	public final void setOffline(final boolean offline) 
	{
		this.offline = offline;
	}

	public final boolean isPreview() 
	{
		return this.preview;
	}

	public final void setPreview(final boolean preview) 
	{
		this.preview = preview;
	}
	
	public final long getDocumentParentId() 
	{
		return this.documentParentId;
	}

	public final void setDocumentParentId(final long documentParentId) 
	{
		this.documentParentId = documentParentId;
	}

	public final Map<String, List<PreviewMovementCompanyItemDTO>> getMovementProducts() 
	{
		return this.movementProducts;
	}
	
	public final Map<String, List<PreviewMovementCompanyItemDTO>> getMovementTransfers() 
	{
		return this.movementTransfers;
	}
	
	public final Map<String, List<PreviewMovementCompanyItemDTO>> getMovementProductsIn() 
	{
		return this.movementProductsIn;
	}
	
	public final Map<String, List<PreviewMovementCompanyItemDTO>> getMovementProductsOut() 
	{
		return this.movementProductsOut;
	}
	
	public final double getMovementBalance()
	{
		return this.movementBalance;
	}

	public final void setMovementBalance(final double movementBalance)
	{
		this.movementBalance = movementBalance;
	}

	public final double getTotalMovementInput() 
	{
		return this.totalMovementInput;
	}

	public final double getTotalMovementOutput() 
	{
		return this.totalMovementOutput;
	}

	public final double getBalanceMovementInputOutput()
	{
		return this.balanceMovementInputOutput;
	}

	public final double getBalanceTransfer() 
	{
		return this.balanceTransfer;
	}

	public final double getBalanceMovement() 
	{
		return this.balanceMovement;
	}

	public final void calculateBalanceMovement() 
	{
		this.balanceMovement = (this.balanceMovementInputOutput-this.balanceTransfer);
	}

	public final int getBankAccountId() 
	{
		return this.bankAccountId;
	}

	public final String getBankAccountDescription()
	{
		return this.bankAccountDescription;
	}

	public final void setBankAccountId(final int bankAccountId) 
	{
		this.bankAccountId = bankAccountId;
	}

	public final void setBankAccountDescription(final String bankAccountDescription) 
	{
		this.bankAccountDescription = bankAccountDescription;
	}

	public final String getMovementsId() {
		return this.movementsId;
	}

	public final void setMovementsId(final String movementsId) {
		this.movementsId = movementsId;
	}

	public final List<Integer> getListBanckAccountIdOutDocumentParent() {
		return this.listBanckAccountIdOutDocumentParent;
	}

	public final void setListBanckAccountIdOutDocumentParent(final List<Integer> listBanckAccountIdOutDocumentParent) {
		this.listBanckAccountIdOutDocumentParent = listBanckAccountIdOutDocumentParent;
	}

	public final ReportMovementCompany getMovementCompany() {
		return this.movementCompany;
	}

	public final void setMovementCompany(final ReportMovementCompany movementCompany) {
		this.movementCompany = movementCompany;
	}

	public final String getUserName() {
		return this.userName;
	}

	public final void setUserName(final String userName) {
		this.userName = userName;
	}
	
	public final Date getMovementDate() {
		return this.movementDate;
	}

	public final void setMovementDate(final Date movementDate) {
		this.movementDate = movementDate;
	}

	public final void addItem(final String key, final PreviewMovementCompanyItemDTO item)
	{
		if(item.isProductMovement())
		{
			List<PreviewMovementCompanyItemDTO> value = this.movementProducts.get(key);
			if(value==null)
			{
				value = new ArrayList<PreviewMovementCompanyItemDTO>();
			}
			value.add(item);
			if(item.isCredit())
			{
				this.totalMovementInput+=item.getDocumentValue();
			}
			else
			{
				this.totalMovementOutput+=item.getDocumentValue();
			}
			this.balanceMovementInputOutput = this.totalMovementInput-this.totalMovementOutput;
			this.movementProducts.put(key, value);
		}
		else
		{
			List<PreviewMovementCompanyItemDTO> value = this.movementTransfers.get(key);
			if(value==null)
			{
				value = new ArrayList<PreviewMovementCompanyItemDTO>();
			}
			value.add(item);
			this.balanceTransfer+=(item.isCredit() ? (-1 * item.getDocumentValue()) : item.getDocumentValue());
			this.movementTransfers.put(key, value);
		}
	}
	
	public final void addItemReport(final String key, final PreviewMovementCompanyItemDTO item)
	{
		if(ConstantDataManager.INPUT_ALIAS.equalsIgnoreCase(key))
		{
			List<PreviewMovementCompanyItemDTO> value = this.movementProductsIn.get(key);
			if(value==null)
			{
				value = new ArrayList<PreviewMovementCompanyItemDTO>();
			}
			item.setTypeWatch(ConstantDataManager.TYPE_WATCH_INPUT_ALIAS);
			value.add(item);
			this.totalMovementInput+=item.getDocumentValue();
			this.movementProductsIn.put(key, value);
		}
		else if(ConstantDataManager.OUTPUT_ALIAS.equalsIgnoreCase(key))
		{
			List<PreviewMovementCompanyItemDTO> value = this.movementProductsOut.get(key);
			if(value==null)
			{
				value = new ArrayList<PreviewMovementCompanyItemDTO>();
			}
			item.setTypeWatch(ConstantDataManager.TYPE_WATCH_OUTPUT_ALIAS);
			value.add(item);
			this.totalMovementOutput+=item.getDocumentValue();
			this.movementProductsOut.put(key, value);
		}
		else
		{
			List<PreviewMovementCompanyItemDTO> value = this.movementTransfers.get(key);
			if(value==null)
			{
				value = new ArrayList<PreviewMovementCompanyItemDTO>();
			}
			value.add(item);
			this.balanceTransfer+=item.getDocumentValue();
			this.movementTransfers.put(key, value);
		}
		this.balanceMovementInputOutput = this.totalMovementInput-this.totalMovementOutput;
	}
	
	public void sorListMovementProducts()
	{
		for (final List<PreviewMovementCompanyItemDTO> itens : this.movementProducts.values()) 
		{
			Collections.sort(itens, new Comparator<PreviewMovementCompanyItemDTO>()
			{
				@Override
				public int compare(final PreviewMovementCompanyItemDTO o1, final PreviewMovementCompanyItemDTO o2)
				{
					return Integer.compare(o1.getOrder(), o2.getOrder());
				}
			});
		}
	}
	
	public void sorListMovementTransfers()
	{
		for (final List<PreviewMovementCompanyItemDTO> itens : this.movementTransfers.values()) 
		{
			Collections.sort(itens, new Comparator<PreviewMovementCompanyItemDTO>()
			{
				@Override
				public int compare(final PreviewMovementCompanyItemDTO o1, final PreviewMovementCompanyItemDTO o2)
				{
					return Integer.compare(o1.getOrder(), o2.getOrder());
				}
			});
		}
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