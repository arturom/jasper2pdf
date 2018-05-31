package com.github.arturom.jasper2pdf;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;

public class App {
	static {
		System.setProperty("java.awt.headless", "true");
	}

	public static void main(String[] args) throws JRException, JsonParseException, JsonMappingException, IOException {
		final String templatePath = args[0];
		final String jsonParamsFile = args[1];
		final String csvDataPath = args[2];
		final String pdfOutputPath = args[3];
		final Map<String, Object> data = ParamsReader.parseFile(new File(jsonParamsFile));
		compileFromCSV(templatePath, data, csvDataPath, pdfOutputPath);
	}

	public static void compileFromCSV(String templatePath, Map<String, Object> parameters, String csvDataPath, String pdfOutputPath) throws JRException {
		final JasperReport jasperReport = JasperCompileManager.compileReport(templatePath);
		final JRCsvDataSource dataSource = new JRCsvDataSource(csvDataPath);
		dataSource.setUseFirstRowAsHeader(true);
		final Map<String, Object> parsedParams = ParamsReader.fixParams(parameters, jasperReport.getParameters());
		final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parsedParams, dataSource);
		JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);
	}
}
