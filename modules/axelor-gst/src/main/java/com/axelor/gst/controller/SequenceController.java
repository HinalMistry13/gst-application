package com.axelor.gst.controller;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class SequenceController {

	@Inject SequenceService sequenceService;
	
	/*
	 * this method generate the Next Number for the sequence and set field.
	 */
	public void calculate(ActionRequest request,ActionResponse response) {
		Sequence sequence = request.getContext().asType(Sequence.class);
		String str = sequenceService.getNextIndex(sequence);
		response.setValue("nextNumber", str);
	}
}
