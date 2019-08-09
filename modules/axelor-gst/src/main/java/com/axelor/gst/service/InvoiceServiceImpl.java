package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.repo.InvoiceLineRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.google.inject.Inject;

public class InvoiceServiceImpl implements InvoiceService{

	@Inject InvoiceRepository invoiceRepository;
	@Inject InvoiceLineRepository invoiceLineRepository;
	
	@Override
	public Invoice calculateGST(Invoice invoice) {
		
		String companyState = invoice.getCompany().getAddress().getState().getName();
		String invoiceState = invoice.getInvoiceAddress().getState().getName();
		BigDecimal invoiceNetAmount = BigDecimal.ZERO;
		BigDecimal invoiceNetIgst = BigDecimal.ZERO;
		BigDecimal invoiceNetCgst = BigDecimal.ZERO;
		BigDecimal invoiceNetSgst = BigDecimal.ZERO;
		BigDecimal invoiceGrossAmount = BigDecimal.ZERO;
		
		List<InvoiceLine> invoiceItemList = invoice.getInvoiceItems();
		for(InvoiceLine item : invoiceItemList) {
			item.setNetAmount(item.getPrice().multiply(new BigDecimal(item.getQty())));
			item.setIgst((item.getNetAmount().multiply(item.getGstRate())).divide(new BigDecimal(100)));
			item.setSgst((item.getNetAmount().multiply(item.getGstRate())).divide(new BigDecimal(200)));
			item.setCgst((item.getNetAmount().multiply(item.getGstRate())).divide(new BigDecimal(200)));
			if(companyState.equals(invoiceState))
				item.setGrossAmount(item.getNetAmount().add(item.getCgst()).add(item.getSgst()));
			else
				item.setGrossAmount(item.getNetAmount().add(item.getCgst()).add(item.getIgst()));
			
			invoiceNetAmount = invoiceNetAmount.add(item.getNetAmount());
			invoiceNetIgst = invoiceNetIgst.add(item.getIgst());
			invoiceNetCgst = invoiceNetCgst.add(item.getCgst());
			invoiceNetSgst = invoiceNetSgst.add(item.getSgst());
			invoiceGrossAmount = invoiceGrossAmount.add(item.getGrossAmount());
		}
		invoice.setNetAmount(invoiceNetAmount);
		invoice.setNetCGST(invoiceNetCgst);
		invoice.setNetIGST(invoiceNetIgst);
		invoice.setNetSGST(invoiceNetSgst);
		invoice.setGrossAmount(invoiceGrossAmount);
		
		return invoice;
	}

}
