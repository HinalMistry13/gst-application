package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class SequenceServiceImpl implements SequenceService {

	@Inject
	SequenceRepository sequenceRepository;

	@Override
	public String getNextIndex(Sequence sequence) {
		StringBuilder oldNumber = new StringBuilder();
		if (sequence.getPrefix() != null && sequence.getPadding() != 0) {
			oldNumber.append(sequence.getPrefix());
			for (int i = 0; i < sequence.getPadding(); i++)
				oldNumber = oldNumber.append((i == sequence.getPadding() - 1) ? "1" : "0");
			if (sequence.getSuffix() != null)
				oldNumber.append(sequence.getSuffix());
		}
		return oldNumber.toString();
	}
	
	@Override
	@Transactional
	public void updateNextIndex(Sequence sequence) {
		StringBuilder newNumber = new StringBuilder();
		String oldNumber = sequence.getNextNumber();
		newNumber.append(sequence.getPrefix());
		long number = Long.parseLong(oldNumber.substring(sequence.getPrefix().length(),sequence.getPrefix().length() + sequence.getPadding())) + 1;
		long numSize = String.valueOf(number).length();
		for (int i = 0; i < sequence.getPadding(); i++)
			newNumber.append((i == sequence.getPadding() - numSize) ? number : "0");
		if (sequence.getSuffix() != null)
			newNumber.append(sequence.getSuffix());
		sequence.setNextNumber(newNumber.toString());
		sequenceRepository.save(sequence);
	}

}
