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
		response.setValue("invoiceItems", invoice.getInvoiceItems());
		response.setValue("netAmount", invoice.getNetAmount());
		response.setValue("netIgst", invoice.getNetIgst());
		response.setValue("netCgst", invoice.getNetCgst());
		response.setValue("netSgst", invoice.getNetSgst());
		response.setValue("grossAmount", invoice.getGrossAmount());
	}
}