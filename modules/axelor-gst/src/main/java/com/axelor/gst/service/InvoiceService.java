package com.axelor.gst.service;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;

public interface InvoiceService {

	public Invoice calculateGST(Invoice invoice);
	public Boolean isStateDifferent(Company company,Address address);
}
