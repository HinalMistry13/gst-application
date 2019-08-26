package com.axelor.gst.service;

import java.util.List;

import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {

	public InvoiceLine calculateInvoiceLineAmount(InvoiceLine invoiceLine,Boolean isStateDiff);
	public List<InvoiceLine> getInvoiceItemsById(List<Integer> products);
}
