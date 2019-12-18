package com.axelor.gst.controller;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileUtils;

import com.axelor.data.Importer;
import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.data.xml.XMLImporter;
import com.axelor.db.Model;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.inject.Beans;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class ImportController {

	@Inject
	InvoiceLineService invoiceLineService;
	
	public void importCSV(ActionRequest request, ActionResponse response) {
//		Importer importer = new CSVImporter("/home/axelor/hinal/GST/axelor-gst-app/modules/axelor-gst/src/main/resources/data-demo/input-config.xml",
//				"/home/axelor/hinal/GST/axelor-gst-app/modules/axelor-gst/src/main/resources/data-demo/input");
		Importer importer = new XMLImporter("/home/axelor/hinal/GST/axelor-gst-app/modules/axelor-gst/src/main/resources/data-demo/input-xml-config.xml",
				"/home/axelor/hinal/GST/axelor-gst-app/modules/axelor-gst/src/main/resources/data-demo/input-xml");
		importer.addListener(new Listener() {
			
			@Override
			public void imported(Integer total, Integer success) {
				// TODO Auto-generated method stub
				System.out.println("Total Records : " + total);
				System.out.println("Success Records : " + success);
			}
			
			@Override
			public void imported(Model bean) {
			}
			
			@Override
			public void handle(Model bean, Exception e) {			
			}
		});
		importer.run();
		System.out.println("import call...");
	}
	
	public void importInvoiceLines(ActionRequest request, ActionResponse response) {
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) request.getContext().get("metaFile");
	    MetaFile dataFile = Beans.get(MetaFileRepository.class).find(((Integer) map.get("id")).longValue());
	    Integer id = (Integer)request.getContext().get("_id");
	    System.out.println("Id: "+id);
	    try {
	      invoiceLineService.importInvoiceLines(dataFile,id);
	      //response.setAttr("importHistoryList", "value:add", importHistory);
	      File readFile = MetaFiles.getPath("").toFile();
	      response.setNotify(FileUtils.readFileToString(readFile, StandardCharsets.UTF_8).replaceAll("(\r\n|\n\r|\r|\n)", "<br />"));
	    } catch (Exception e) {
	    }
	}
}
