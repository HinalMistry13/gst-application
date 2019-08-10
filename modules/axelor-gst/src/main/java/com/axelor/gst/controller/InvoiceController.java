package com.axelor.gst.controller;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController {
	
	@Inject InvoiceService invoiceService;
	
	public void calculate(ActionRequest request,ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = invoiceService.calculateGST(invoice);
		response.setValues(invoice);
	}
	
	public void changeGstToZero(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = invoiceService.changeGstAmount(invoice);
		response.setValues(invoice);
	}

}