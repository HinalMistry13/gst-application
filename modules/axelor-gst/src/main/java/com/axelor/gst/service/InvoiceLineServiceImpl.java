package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.InvoiceLine;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Override
	public InvoiceLine calculateInvoiceLineAmount(InvoiceLine invoiceLine,Company company,Address invoiceAddress) {
		if (company != null && invoiceAddress != null && invoiceLine!=null) {
			if (company.getAddress().getState() != null && invoiceAddress.getState() != null) {
				String companyState = company.getAddress().getState().getName();
				String invoiceState = invoiceAddress.getState().getName();
				invoiceLine.setNetAmount(invoiceLine.getPrice().multiply(new BigDecimal(invoiceLine.getQty())));
				invoiceLine.setIgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(new BigDecimal(100)));
				invoiceLine.setSgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(new BigDecimal(200)));
				invoiceLine.setCgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(new BigDecimal(200)));
				if (companyState.equals(invoiceState))
					invoiceLine.setGrossAmount(invoiceLine.getNetAmount().add(invoiceLine.getCgst()).add(invoiceLine.getSgst()));
				else
					invoiceLine.setGrossAmount(invoiceLine.getNetAmount().add(invoiceLine.getCgst()).add(invoiceLine.getIgst()));
			}
		}
		return invoiceLine;
	}

}
