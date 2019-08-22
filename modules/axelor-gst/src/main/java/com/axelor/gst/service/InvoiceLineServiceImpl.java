package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.google.inject.Inject;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Inject ProductRepository productRepository;
	
	/*
	 * Calculation of gst cost.
	 */
	@Override
	public InvoiceLine calculateInvoiceLineAmount(InvoiceLine invoiceLine,Company company,Address invoiceAddress) {
		if (company != null && invoiceAddress != null && invoiceLine!=null) {
			if (company.getAddress()!=null && company.getAddress().getState() != null && invoiceAddress.getState() != null) {
				String companyState = company.getAddress().getState().getName();
				String invoiceState = invoiceAddress.getState().getName();
				invoiceLine.setNetAmount(invoiceLine.getPrice().multiply(new BigDecimal(invoiceLine.getQty())));
				if(companyState.equals(invoiceState)) {
					invoiceLine.setSgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(new BigDecimal(200)));
					invoiceLine.setCgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(new BigDecimal(200)));
					invoiceLine.setGrossAmount(invoiceLine.getNetAmount().add(invoiceLine.getCgst()).add(invoiceLine.getSgst()));
				}else { 
					invoiceLine.setIgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(new BigDecimal(100)));
					invoiceLine.setGrossAmount(invoiceLine.getNetAmount().add(invoiceLine.getIgst()));
				}
			}
		}
		return invoiceLine;
	}
	
	/*
	 * Return Invoice item list by product id.
	 */
	@Override
	public List<InvoiceLine> getInvoiceItemsById(List<Integer> products) {
		List<InvoiceLine> invoiceItemList = new ArrayList<InvoiceLine>();
		if(products!=null) {
			Product product = null;
			InvoiceLine invoiceLine = null;
			for(int id : products){
				product = productRepository.find((long)id); 
				invoiceLine = new InvoiceLine();
				invoiceLine.setProduct(product);
				invoiceLine.setQty(1);
				invoiceLine.setPrice(product.getSalePrice());
				invoiceLine.setGstRate(product.getGstRate());
				invoiceItemList.add(invoiceLine);
			}
		}
		return invoiceItemList;
	}
	
}
