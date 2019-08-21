package com.axelor.gst.repo;

import javax.persistence.PersistenceException;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.Inject;

public class InvoiceInvoiceRepository extends InvoiceRepository {

	@Inject
	SequenceService sequenceService;
	@Inject
	SequenceRepository sequenceRepository;

	@Override
	public Invoice save(Invoice entity) {
		try {
			if (entity.getReference() == null) {
				MetaModel model = Beans.get(MetaModelRepository.class).findByName(entity.getClass().getSimpleName());
				Sequence invoiceSequence = sequenceRepository.findByModel(model);
				entity.setReference(invoiceSequence.getNextNumber());
				sequenceService.updateNextIndex(invoiceSequence);
			}
			return super.save(entity);
		} catch (Exception e) {
			throw new PersistenceException(
					"Model is not found for Sequence '" + entity.getClass().getSimpleName() + "'");
		}
	}
}
