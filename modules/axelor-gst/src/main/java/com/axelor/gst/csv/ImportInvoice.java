package com.axelor.gst.csv;

import java.util.Map;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.service.InvoiceService;
import com.google.inject.Inject;

public class ImportInvoice {

	@Inject
	InvoiceService invoiceService;	
	
	public Object importInvoiceData(Object bean, Map<String, Object> values){
		assert bean instanceof Invoice;
		Invoice invoice = (Invoice) bean;
		invoice = invoiceService.calculateGST(invoice);
		return invoice;
	}
}
