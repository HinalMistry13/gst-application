package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.repo.InvoiceLineRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.google.inject.Inject;

public class InvoiceServiceImpl implements InvoiceService {

	@Inject
	InvoiceRepository invoiceRepository;
	@Inject
	InvoiceLineRepository invoiceLineRepository;
	
	@Override
	public Invoice calculateGST(Invoice invoice) {
		if (invoice.getCompany() != null && invoice.getInvoiceAddress() != null && invoice.getInvoiceItems()!=null) {
			if (invoice.getCompany().getAddress() != null && invoice.getCompany().getAddress().getState() != null && invoice.getInvoiceAddress().getState() != null) {
//				String companyState = invoice.getCompany().getAddress().getState().getName();
//				String invoiceState = invoice.getInvoiceAddress().getState().getName();
				BigDecimal invoiceNetAmount = BigDecimal.ZERO;
				BigDecimal invoiceNetIgst = BigDecimal.ZERO;
				BigDecimal invoiceNetCgst = BigDecimal.ZERO;
				BigDecimal invoiceNetSgst = BigDecimal.ZERO;
				BigDecimal invoiceGrossAmount = BigDecimal.ZERO;
//				List<InvoiceLine> newInvoiceItemList = new ArrayList<InvoiceLine>();
				List<InvoiceLine> invoiceItemList = invoice.getInvoiceItems();
				for (InvoiceLine item : invoiceItemList) {
//					item.setNetAmount(item.getPrice().multiply(new BigDecimal(item.getQty())));
//					item.setIgst((item.getNetAmount().multiply(item.getGstRate())).divide(new BigDecimal(100)));
//					item.setSgst((item.getNetAmount().multiply(item.getGstRate())).divide(new BigDecimal(200)));
//					item.setCgst((item.getNetAmount().multiply(item.getGstRate())).divide(new BigDecimal(200)));
//					if (companyState.equals(invoiceState))
//						item.setGrossAmount(item.getNetAmount().add(item.getCgst()).add(item.getSgst()));
//					else
//						item.setGrossAmount(item.getNetAmount().add(item.getCgst()).add(item.getIgst()));
					invoiceNetAmount = invoiceNetAmount.add(item.getNetAmount());
					invoiceNetIgst = invoiceNetIgst.add(item.getIgst());
					invoiceNetCgst = invoiceNetCgst.add(item.getCgst());
					invoiceNetSgst = invoiceNetSgst.add(item.getSgst());
					invoiceGrossAmount = invoiceGrossAmount.add(item.getGrossAmount());
//					newInvoiceItemList.add(item);
				}
//				invoice.setInvoiceItems(newInvoiceItemList);
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
