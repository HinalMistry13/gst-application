package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.repo.InvoiceInvoiceRepository;
import com.axelor.gst.repo.PartyPartyRepository;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceLineServiceImpl;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.InvoiceServiceImpl;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImpl;

public class GSTModule extends AxelorModule{

	@Override
	protected void configure() {
		bind(InvoiceService.class).to(InvoiceServiceImpl.class);
		bind(PartyRepository.class).to(PartyPartyRepository.class);
		bind(SequenceService.class).to(SequenceServiceImpl.class);
		bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
		bind(InvoiceRepository.class).to(InvoiceInvoiceRepository.class);
	}
	
}
