package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.meta.db.MetaModel;

public interface SequenceService {

	public String getNextIndex(Sequence sequence);
	public Sequence getSequenceByModel(MetaModel model);
	public void updateNextIndex(Sequence sequence);
}
