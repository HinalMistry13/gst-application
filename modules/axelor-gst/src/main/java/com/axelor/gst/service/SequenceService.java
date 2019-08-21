package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public interface SequenceService {

	public String getNextIndex(Sequence sequence);
	public void updateNextIndex(Sequence sequence);
}
