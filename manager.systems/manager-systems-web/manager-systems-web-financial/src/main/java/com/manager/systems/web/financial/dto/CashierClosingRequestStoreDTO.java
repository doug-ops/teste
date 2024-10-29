package com.manager.systems.web.financial.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashierClosingRequestStoreDTO implements Serializable {

	private static final long serialVersionUID = 3947458963776522161L;

	private int companyId;
	private String companyDescription;
	private int bankAccountId;
	private String bankAccountDescription;
	private boolean checked;
	private double totalMovement;
	private double totalPendingBefore;
	private double totalStore;
	private double totalPayment;
	private double totalPendingAfter;
	private String documentsKeys;
	private String paymentDate;
	private List<CashierClosingRequestProductDTO> movProd;
	private List<CashierClosingRequestTransferDTO> movTransf;
}