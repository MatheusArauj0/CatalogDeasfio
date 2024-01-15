package com.matheus.catalog.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.matheus.catalog.entities.Product;
import com.matheus.catalog.repositories.ProductRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

	@Autowired
	private ProductRepository repository;

	@Transactional
	public byte[] exportReport(String reportFormat) throws JRException, FileNotFoundException {
		List<Product> produtcs = repository.findAll();

		// Exportar o relatório para um formato desejado (PDF, por exemplo)
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		File file = ResourceUtils.getFile("classpath:products.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(produtcs);
		Map<String, Object> parameters = new HashMap<>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

		switch (reportFormat.toLowerCase()) {
		case "pdf":
			JasperExportManager.exportReportToPdfFile(jasperPrint, System.getProperties().getProperty("user.dir") + "\\src\\main\\resources\\static\\products.pdf");
			break;
			
		case "html":
			JasperExportManager.exportReportToHtmlFile(System.getProperties().getProperty("user.dir") + "\\src\\main\\resources\\static\\products.html");
			break;

		case "csv":
			JRCsvExporter exporter = new JRCsvExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(System.getProperties().getProperty("user.dir") + "\\src\\main\\resources\\static\\products.csv"));
			//exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));

			exporter.exportReport();
			break;
			
		case "xlsx":
			JRXlsxExporter exporter1 = new JRXlsxExporter();
		    exporter1.setExporterInput(new SimpleExporterInput(jasperPrint));
		    exporter1.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperties().getProperty("user.dir") + "\\src\\main\\resources\\static\\products.xlsx"));

			exporter1.exportReport();
			break;
			
			
		
		default:
			throw new IllegalArgumentException("Formato de relatório não suportado: " + reportFormat);

		}
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
		return outputStream.toByteArray();
	}

}
