package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;

public interface InvoiceService {

	public Invoice calculateGST(Invoice invoice);
}
