package com.axelor.gst.csv;

import java.util.Map;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceService;
import com.google.inject.Inject;

public class ImportInvoiceLine {

	@Inject
	InvoiceLineService invoiceLineService;	
	@Inject InvoiceService invoiceService;
	
	public Object importInvoiceLineData(Object bean, Map<String, Object> values){
		assert bean instanceof InvoiceLine;
		InvoiceLine invoiceLine = (InvoiceLine) bean;
		invoiceLine = invoiceLineService.calculateInvoiceLineAmount(invoiceLine,invoiceService.isStateDifferent(invoiceLine.getInvoice().getCompany(), invoiceLine.getInvoice().getInvoiceAddress()));
		return invoiceLine;
	}
}
