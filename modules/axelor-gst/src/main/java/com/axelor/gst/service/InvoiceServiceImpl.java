package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.google.inject.Inject;

public class InvoiceServiceImpl implements InvoiceService {

	@Inject
	private InvoiceLineService invoiceLineService;

	/*
	 * Calculate the gst cost of Invocie.
	 */
	@Override
	public Invoice calculateGST(Invoice invoice) {
		Boolean isDiff = isStateDifferent(invoice.getCompany(),invoice.getInvoiceAddress());
		if (isDiff != null && invoice.getInvoiceItems() != null && invoice.getParty() != null) {
				BigDecimal invoiceNetAmount = BigDecimal.ZERO;
				BigDecimal invoiceNetIgst = BigDecimal.ZERO;
				BigDecimal invoiceNetCgst = BigDecimal.ZERO;
				BigDecimal invoiceNetSgst = BigDecimal.ZERO;
				BigDecimal invoiceGrossAmount = BigDecimal.ZERO;
				List<InvoiceLine> invoiceItemList = invoice.getInvoiceItems();
				for (InvoiceLine item : invoiceItemList) {
					item = invoiceLineService.calculateInvoiceLineAmount(item,isDiff);
					invoiceNetAmount = invoiceNetAmount.add(item.getNetAmount());
					invoiceNetIgst = invoiceNetIgst.add(item.getIgst());
					invoiceNetCgst = invoiceNetCgst.add(item.getCgst());
					invoiceNetSgst = invoiceNetSgst.add(item.getSgst());
					invoiceGrossAmount = invoiceGrossAmount.add(item.getGrossAmount());
				}
				invoice.setNetAmount(invoiceNetAmount);
				invoice.setNetCgst(invoiceNetCgst);
				invoice.setNetIgst(invoiceNetIgst);
				invoice.setNetSgst(invoiceNetSgst);
				invoice.setGrossAmount(invoiceGrossAmount);
		} else {
			BigDecimal totalNetAmount = BigDecimal.ZERO;
			if (invoice.getInvoiceItems() != null) {
				for (InvoiceLine item : invoice.getInvoiceItems()) {
					item = invoiceLineService.calculateInvoiceLineAmount(item,isDiff);
					totalNetAmount = totalNetAmount.add(item.getNetAmount());
				}
			}
			invoice.setNetAmount(totalNetAmount);
			invoice.setNetCgst(BigDecimal.ZERO);
			invoice.setNetIgst(BigDecimal.ZERO);
			invoice.setNetSgst(BigDecimal.ZERO);
			invoice.setGrossAmount(totalNetAmount);
		}
		return invoice;
	}
	
	@Override
	public Boolean isStateDifferent(Company company,Address address) {
		Boolean stateDiff = null;
		if(company !=null && address !=null && company.getAddress() != null && company.getAddress().getState() != null && address.getState() != null) {
			if(company.getAddress().getState().equals(address.getState()))
				stateDiff = false;
			else
				stateDiff = true;
		}
		return stateDiff;
	}
}
