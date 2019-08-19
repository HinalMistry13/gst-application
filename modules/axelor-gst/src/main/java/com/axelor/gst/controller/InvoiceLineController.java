package com.axelor.gst.controller;

import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceLineController {

	@Inject InvoiceLineService invoiceLineService;
	
	public void calculate(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
		Company company = (Company) request.getContext().getParent().get("company");
		Address invoiceAddress = (Address) request.getContext().getParent().get("invoiceAddress");
		invoiceLine = invoiceLineService.calculateInvoiceLineAmount(invoiceLine,company,invoiceAddress);
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
	
	public void getProductsByIds(ActionRequest request, ActionResponse response) {
		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) request.getContext().get("products");
		List<InvoiceLine> items = invoiceLineService.getInvoiceItemsById(ids);
		for(InvoiceLine line : items) {
			System.out.println(line);
		}
		response.setValue("invoiceItems", items);
	}
}
