package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
		if (invoice.getCompany() != null && invoice.getInvoiceAddress() != null && invoice.getInvoiceItems()!=null) {
			if (invoice.getCompany() != null && invoice.getCompany().getAddress() != null && invoice.getCompany().getAddress().getState() != null && invoice.getInvoiceAddress().getState() != null) {
				Company company = invoice.getCompany();
				Address invoiceAddress = invoice.getInvoiceAddress();
				BigDecimal invoiceNetAmount = BigDecimal.ZERO;
				BigDecimal invoiceNetIgst = BigDecimal.ZERO;
				BigDecimal invoiceNetCgst = BigDecimal.ZERO;
				BigDecimal invoiceNetSgst = BigDecimal.ZERO;
				BigDecimal invoiceGrossAmount = BigDecimal.ZERO;
				List<InvoiceLine> invoiceItemList = invoice.getInvoiceItems();
				for (InvoiceLine item : invoiceItemList) {
					item = invoiceLineService.calculateInvoiceLineAmount(item, company, invoiceAddress);
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
			}
		} else {
			invoice.setNetAmount(BigDecimal.ZERO);
			invoice.setNetCgst(BigDecimal.ZERO);
			invoice.setNetIgst(BigDecimal.ZERO);
			invoice.setNetSgst(BigDecimal.ZERO);
			invoice.setGrossAmount(BigDecimal.ZERO);
			if (invoice.getInvoiceItems()!=null) {
				List<InvoiceLine> invoiceItemList = invoice.getInvoiceItems();
				List<InvoiceLine> newInvoiceItemList = new ArrayList<InvoiceLine>();
				for (InvoiceLine item : invoiceItemList) {
					item.setNetAmount(BigDecimal.ZERO);
					item.setCgst(BigDecimal.ZERO);
					item.setIgst(BigDecimal.ZERO);
					item.setSgst(BigDecimal.ZERO);
					item.setGrossAmount(BigDecimal.ZERO);
					newInvoiceItemList.add(item);
				}
				invoice.setInvoiceItems(newInvoiceItemList);
			}
		}
		return invoice;
	}
}
