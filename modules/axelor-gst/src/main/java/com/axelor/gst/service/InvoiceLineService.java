package com.axelor.gst.service;

import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {

	public InvoiceLine calculateInvoiceLineAmount(InvoiceLine invoiceLine,Company company,Address invoiceAddress);
	public List<InvoiceLine> getInvoiceItemsById(List<Integer> products);
}
