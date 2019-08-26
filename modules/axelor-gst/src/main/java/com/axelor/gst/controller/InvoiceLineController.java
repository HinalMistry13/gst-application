package com.axelor.gst.controller;

import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceLineController {

	@Inject InvoiceLineService invoiceLineService;
	@Inject InvoiceService invoiceService;
	
	/*
	 * This method pass data from view to service for calculation and set value of it. 
	 */
	public void calculate(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
		Company company = (Company) request.getContext().getParent().get("company");
		Address invoiceAddress = (Address) request.getContext().getParent().get("invoiceAddress");
		invoiceLine = invoiceLineService.calculateInvoiceLineAmount(invoiceLine,invoiceService.isStateDifferent(company, invoiceAddress));
		response.setValue("product", invoiceLine.getProduct());
		response.setValue("qty", invoiceLine.getQty());
		response.setValue("price", invoiceLine.getPrice());
		response.setValue("hsbn", invoiceLine.getHsbn());
		response.setValue("netAmount", invoiceLine.getNetAmount());
		response.setValue("gstRate", invoiceLine.getGstRate());
		response.setValue("igst", invoiceLine.getIgst());
		response.setValue("sgst", invoiceLine.getSgst());
		response.setValue("cgst", invoiceLine.getCgst());
		response.setValue("grossAmount", invoiceLine.getGrossAmount());
	}
	
	/*
	 * This method pass invoice items to invoice form which are selected from product grid. 
	 */
	public void getProductsByIds(ActionRequest request, ActionResponse response) {
		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) request.getContext().get("products");
		List<InvoiceLine> items = invoiceLineService.getInvoiceItemsById(ids);
		response.setValue("invoiceItems", items);
	}
}
