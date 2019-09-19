package com.axelor.gst.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.db.Model;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.google.common.io.Files;
import com.google.inject.Inject;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Inject
	ProductRepository productRepository;

	/*
	 * Calculation of gst cost.
	 */
	@Override
	public InvoiceLine calculateInvoiceLineAmount(InvoiceLine invoiceLine, Boolean isStateDiff) {
		invoiceLine.setNetAmount(invoiceLine.getPrice().multiply(new BigDecimal(invoiceLine.getQty())));
		if (isStateDiff == null) {
			invoiceLine.setCgst(BigDecimal.ZERO);
			invoiceLine.setSgst(BigDecimal.ZERO);
			invoiceLine.setIgst(BigDecimal.ZERO);
			invoiceLine.setGrossAmount(invoiceLine.getNetAmount());
		} else if (isStateDiff == false) {
			invoiceLine.setIgst(BigDecimal.ZERO);
			invoiceLine.setSgst(
					(invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(new BigDecimal(200)));
			invoiceLine.setCgst(
					(invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(new BigDecimal(200)));
			invoiceLine
					.setGrossAmount(invoiceLine.getNetAmount().add(invoiceLine.getCgst()).add(invoiceLine.getSgst()));
		} else if (isStateDiff == true) {
			invoiceLine.setCgst(BigDecimal.ZERO);
			invoiceLine.setSgst(BigDecimal.ZERO);
			invoiceLine.setIgst(
					(invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(new BigDecimal(100)));
			invoiceLine.setGrossAmount(invoiceLine.getNetAmount().add(invoiceLine.getIgst()));
		}
		return invoiceLine;
	}

	/*
	 * Return Invoice item list by product id.
	 */
	@Override
	public List<InvoiceLine> getInvoiceItemsById(List<Integer> products) {
		List<InvoiceLine> invoiceItemList = new ArrayList<InvoiceLine>();
		if (products != null) {
			Product product = null;
			InvoiceLine invoiceLine = null;
			for (int id : products) {
				product = productRepository.find((long) id);
				invoiceLine = new InvoiceLine();
				invoiceLine.setProduct(product);
				invoiceLine.setQty(1);
				invoiceLine.setPrice(product.getSalePrice());
				invoiceLine.setGstRate(product.getGstRate());
				invoiceItemList.add(invoiceLine);
			}
		}
		return invoiceItemList;
	}

	@Override
	public void importInvoiceLines(MetaFile dataFile,Integer id) {
		try {
			File configXmlFile = this.getConfigXmlFile();
			File dataCsvFile = this.getDataCsvFile(dataFile);

//			CSVImporter importer = new CSVImporter("/home/axelor/hinal/GST/axelor-gst-app/modules/axelor-gst/src/main/resources/import-configs/invoice-lines-config.xml",
//					"/home/axelor/hinal/GST/axelor-gst-app/modules/axelor-gst/src/main/resources/data-demo/input");
			CSVImporter importer = new CSVImporter(configXmlFile.getAbsolutePath(),"/home/axelor/hinal/GST/axelor-gst-app/modules/axelor-gst/src/main/resources/import-configs/input/");
			
			final Map<String, Object> context = new HashMap<>();
	        context.put("invoice",id);
	        importer.setContext(context);
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
			this.deleteTempFiles(configXmlFile, dataCsvFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File getConfigXmlFile() {

		File configFile = null;
		try {
			configFile = File.createTempFile("input-config", ".xml");
			InputStream bindFileInputStream = this.getClass().getResourceAsStream("/import-configs/invoice-lines-config.xml");
			if (bindFileInputStream != null) {
				FileOutputStream outputStream = new FileOutputStream(configFile);
				IOUtils.copy(bindFileInputStream, outputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configFile;
	}

	private File getDataCsvFile(MetaFile dataFile) {

		File csvFile = null;
		try {
			File tempDir = Files.createTempDir();
			csvFile = new File(tempDir, "gst_invoiceline.csv");
			Files.copy(MetaFiles.getPath(dataFile).toFile(), csvFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return csvFile;
	}

	private void deleteTempFiles(File configXmlFile, File dataCsvFile) {
		try {
			if (configXmlFile.isDirectory() && dataCsvFile.isDirectory()) {
				FileUtils.deleteDirectory(configXmlFile);
				FileUtils.deleteDirectory(dataCsvFile);
			} else {
				configXmlFile.delete();
				dataCsvFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
