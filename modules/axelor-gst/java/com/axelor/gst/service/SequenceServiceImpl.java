package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.meta.db.MetaModel;
import com.google.inject.Inject;

public class SequenceServiceImpl implements SequenceService{

	@Inject SequenceRepository sequenceRepository;
	
	@Override
	public String getNextIndex(Sequence sequence) {				
		StringBuilder str=new StringBuilder();
		String oldNumber = null;
		int numSize = 0;
		long number=0;
		if(sequence.getId()==null) {
			numSize=1;
			number=1;
		}else {
			oldNumber = sequence.getNextNumber();
			number = Long.parseLong(oldNumber.substring(sequence.getPrefix().length(),sequence.getPrefix().length()+sequence.getPadding()))+1;
			numSize=String.valueOf(number).length();
		}
		if(sequence.getPrefix()!=null && sequence.getPadding()!=0) {
			str.append(sequence.getPrefix());
			for(int i=0;i<sequence.getPadding();i++)
				str.append((i == sequence.getPadding()-numSize) ? number : "0");
			if(sequence.getSuffix()!=null)
				str.append(sequence.getSuffix());
			if(sequence.getId()!=null){
				sequence.setNextNumber(oldNumber);
				sequenceRepository.save(sequence);			
			}
		}
		return str.toString();
	}
	
	@Override
	public Sequence getSequenceByModel(MetaModel model) {
		return sequenceRepository.findByModel(model);
	}	
}
