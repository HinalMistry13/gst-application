package com.axelor.gst.repo;

import java.util.Map;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.repo.PartyRepository;

public class PartyPartyRepository extends PartyRepository{

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
}
