package com.axelor.gst.repo;

import java.util.Map;

import javax.persistence.PersistenceException;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.Inject;

public class PartyPartyRepository extends PartyRepository{

	@Inject SequenceService sequenceService;
	
	@Override
	public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
		try {
			Long id = (Long) json.get("id");
			Party party = find(id);
			json.put("contact", party.getContact());
			json.put("address", party.getAddress());
		} catch (Exception e) {
		}
		return json;
	}
	
	@Override
	public Party save(Party entity) {
		try {
			MetaModel model = Beans.get(MetaModelRepository.class).findByName(entity.getClass().getSimpleName());
			Sequence partySequence = sequenceService.getSequenceByModel(model);
			if(entity.getReference()==null) {
				entity.setReference(partySequence.getNextNumber());
				sequenceService.updateNextIndex(partySequence);
			}
			return super.save(entity);
		}catch (Exception e) {
			throw new PersistenceException("Model is not found for Sequence '"+entity.getClass().getSimpleName()+"'");
		}
	}
}
