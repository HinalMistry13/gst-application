package com.axelor.gst.service;

import java.util.List;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.meta.db.MetaFile;

public interface InvoiceLineService {

	public InvoiceLine calculateInvoiceLineAmount(InvoiceLine invoiceLine,Boolean isStateDiff);
	public List<InvoiceLine> getInvoiceItemsById(List<Integer> products);
	public void importInvoiceLines(MetaFile dataFile,Integer id);
}
