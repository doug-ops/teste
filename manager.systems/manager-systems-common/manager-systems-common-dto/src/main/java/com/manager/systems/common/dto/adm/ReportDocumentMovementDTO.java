package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportDocumentMovementDTO implements Serializable
{
	private static final long serialVersionUID = -6170583374309665265L;

	//private double credit;
	//private double debit;
	//private double total;
	List<ReportDocumentMovementItemDTO> itens = new ArrayList<ReportDocumentMovementItemDTO>();
}