package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.InvoiceServiceImpl;

public class GSTModule extends AxelorModule{

	@Override
	protected void configure() {
		bind(InvoiceService.class).to(InvoiceServiceImpl.class);
	}

	
}
