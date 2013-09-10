/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.tqa.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TemplateReport {

	final static Logger logger = Logger.getLogger(TemplateReport.class);
	
	private final static String ANALYSIS_FOLDER = "analysis";
	private final static String ANALYSIS_FILE = "MERGE3-AggregateReport.csv";
	
	Configurations configurations;
	ScenarioConfiguration scenarioConfig;

	public TemplateReport() {

	}

	/**
	 * generate all report
	 * 
	 * @param configFileXml
	 * @throws IOException
	 */
	void generateReport(String configFileXml) throws IOException {

		// read configuration file
		readConfig(configFileXml);

		// Generate report
		logger.info("Generating report");
		buildL1Page();
		buildL2Page();
	}
	
	/**
	 * 
	 * Created on: Sep 9, 2013,10:57:01 AM
	 * Author: QuyenNT
	 */
	public void buildL1Page(){
		File l1Folder = new File(configurations.getGeneratedPath() + "/" + configurations.getPrefix());
		l1Folder.mkdir();
		
		File generalReport = new File(configurations.getGeneratedPath()	+ "/template/GENERAL");
		String templateGeneralReport;
		try {
			templateGeneralReport = readTemplate(generalReport);
			templateGeneralReport = replaceL1Info(templateGeneralReport);
			// templateGeneralReport = replaceGeneralInfo(templateGeneralReport);
			// templateGeneralReport = generatePulishChart(templateGeneralReport);
			
			writeTemplateReplaced(templateGeneralReport, l1Folder.toString());			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (Exception ex){
			ex.printStackTrace();
		}		
	}
	
	/**
	 * 
	 * Created on: Sep 9, 2013,10:56:43 AM
	 * Author: QuyenNT
	 */
	public void buildL2Page(){		
		List scenarioList = configurations.getScenariosList();
		String l1Folder = configurations.getGeneratedPath() + "/"+ configurations.getPrefix();
		
        File templateFile = new File(configurations.getGeneratedPath() + "/template/GENERIC");

//		 for each scnario : read template file, replace and write a new report file
		 for (int i = 0; i < scenarioList.size(); i++) {		
			 String templateToString;
			try {
				templateToString = readTemplate(templateFile);
				templateToString = replaceL2Info(templateToString);
				 // templateToString = replaceChart(templateToString, iCase);
				
				 //Create L2 folder under L1 folder
				 File l2Folder = new File(l1Folder +"/" +configurations.getPrefix() +
						 								"_-_" + scenarioList.get(i).toString().trim());
				 l2Folder.mkdir();
				
				 writeTemplateReplaced(templateToString, l2Folder.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		 }		
	}

	/**
	 * read xml config file then register informations into hash map and list
	 * 
	 * @param configFileXml
	 * @throws IOException
	 */
	void readConfig(String configFileXml) {
		// list of scenarios
		ArrayList listScenario = new ArrayList<String>();

		configurations = new Configurations();
		
		// read config file
		logger.info("Start loading configuration file..");
		File file = new File(configFileXml);
		InputStream inputStream;
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			inputStream = new FileInputStream(file);
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using builder to get DOM representation of the XML file
			Document dom = db.parse(new InputSource(inputStream));
			Element root = dom.getDocumentElement();

			// Read config file
			NodeList prefix = root.getElementsByTagName("prefix");
			NodeList generatedPath = root.getElementsByTagName("generated_path");
			NodeList dataPath = root.getElementsByTagName("data_path");

			// WEBDAV
			NodeList webdavLogin = root.getElementsByTagName("webdav_login");
			NodeList webdavPass = root.getElementsByTagName("webdav_pass");
			NodeList webdavPath = root.getElementsByTagName("webdav_path");
			NodeList wikiLink = root.getElementsByTagName("wiki_link");
			
			NodeList dataDelimiter = root.getElementsByTagName("data_delimiter");
			
			NodeList weekBase = root.getElementsByTagName("base");
			NodeList weekPrevious = root.getElementsByTagName("previous");
			NodeList weekThis = root.getElementsByTagName("this");
			
			NodeList responseColumn = root.getElementsByTagName("responsetime");
			NodeList throughputColumn = root.getElementsByTagName("throughput");
			NodeList responseAvgColumn = root.getElementsByTagName("responsetime_avg");
			NodeList errorColumn = root.getElementsByTagName("error");
			
			NodeList listScenarioNode = root.getElementsByTagName("name");
			NodeList listResponseLabelIdNode = root.getElementsByTagName("responsetime_label_id");
			NodeList listResponseLabelNode = root.getElementsByTagName("responsetime_label");
			NodeList listThroughputLabelNode = root.getElementsByTagName("throughput_label");

			// set values to configurations
			//Prefix
			String valueSpacesValues = prefix.item(0).getFirstChild().getNodeValue();
			configurations.setPrefix(valueSpacesValues);

			//generated Path
			valueSpacesValues = generatedPath.item(0).getFirstChild().getNodeValue();
			configurations.setGeneratedPath(valueSpacesValues);
			
			//data path
			valueSpacesValues = dataPath.item(0).getFirstChild().getNodeValue();
			configurations.setDataPath(valueSpacesValues);

			//webdav login
			valueSpacesValues = webdavLogin.item(0).getFirstChild().getNodeValue();
			configurations.setWebdavLogin(valueSpacesValues);

			//webdav pass 
			valueSpacesValues = webdavPass.item(0).getFirstChild().getNodeValue();
			configurations.setWebdavPass(valueSpacesValues);

			//webdav path
			valueSpacesValues = webdavPath.item(0).getFirstChild().getNodeValue();
			configurations.setWebdavPath(valueSpacesValues);

			//scenario list
			valueSpacesValues = listScenarioNode.item(0).getFirstChild().getNodeValue();
			
			for (int iLevel = 0; iLevel < listScenarioNode.getLength(); iLevel++) {
				scenarioConfig = new ScenarioConfiguration();
				//name
				Node noditem = listScenarioNode.item(iLevel);
				scenarioConfig.setScenarioName(noditem.getTextContent().trim());

				noditem = listResponseLabelIdNode.item(iLevel);
				scenarioConfig.setResponseLabelId(noditem.getTextContent().trim());
				
				//responseLabel
				noditem = listResponseLabelNode.item(iLevel);
				scenarioConfig.setResponseLabel(noditem.getTextContent().trim());

				//throughputLabel
				noditem = listThroughputLabelNode.item(iLevel);
				scenarioConfig.setThroughputLabel(noditem.getTextContent().trim());
				
				listScenario.add(scenarioConfig);
			}
			configurations.setScenariosList(listScenario);

			//wiki link
			valueSpacesValues = wikiLink.item(0).getFirstChild().getNodeValue();
			configurations.setWikiLink(valueSpacesValues);
			
			//data delimiter
			valueSpacesValues = dataDelimiter.item(0).getFirstChild().getNodeValue();
			configurations.setDataDelimiter(valueSpacesValues);
			
			//week base
			valueSpacesValues = weekBase.item(0).getFirstChild().getNodeValue();
			configurations.setWeekBase(valueSpacesValues);

			
			//week previous
			valueSpacesValues = weekPrevious.item(0).getFirstChild().getNodeValue();
			configurations.setWeekPrevious(valueSpacesValues);

			//week this
			valueSpacesValues = weekThis.item(0).getFirstChild().getNodeValue();
			configurations.setWeekThis(valueSpacesValues);
			
			//responseColumn
			valueSpacesValues = responseColumn.item(0).getFirstChild().getNodeValue();
			configurations.setResponseColumn(Integer.parseInt(valueSpacesValues));		
			
			//throughput
			valueSpacesValues = throughputColumn.item(0).getFirstChild().getNodeValue();
			configurations.setThroughputColumn(Integer.parseInt(valueSpacesValues));	
			
			//responseAvgColumn
			valueSpacesValues = responseAvgColumn.item(0).getFirstChild().getNodeValue();
			configurations.setResponseAvgColumn(Integer.parseInt(valueSpacesValues));	
			
			//error
			valueSpacesValues = errorColumn.item(0).getFirstChild().getNodeValue();
			configurations.setErrorColumn(Integer.parseInt(valueSpacesValues));	
			
		} catch (FileNotFoundException e) {
			logger.error("ReportInfo readConfig FileNotFoundException error: "
					+ e.getMessage());
		} catch (ParserConfigurationException e) {
			logger.error("ReportInfo readConfig ParserConfigurationException error: "
					+ e.getMessage());
		} catch (SAXException e) {
			logger.error("ReportInfo readConfig SAXException error: "
					+ e.getMessage());
		} catch (IOException e) {
			logger.error("ReportInfo readConfig IOException error: "
					+ e.getMessage());

		}

	}

	/**
	 * Read CSV file and process data
	 * Created on: Sep 10, 2013,9:53:19 AM
	 * Author: QuyenNT
	 * @return
	 */
	public List<ScenarioObject> processData() {
		List<ScenarioObject> returnList = new ArrayList<ScenarioObject>();

		BufferedReader br = null;		
		List scenarioList = configurations.getScenariosList();		
		ScenarioConfiguration scenarioConfig;
		
		try {
			for(int i = 0; i < scenarioList.size(); i++){
				scenarioConfig = (ScenarioConfiguration)scenarioList.get(i);
				returnList.add(processDataForEachScenario(scenarioConfig));
			}			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return returnList;
	}
	
	/**
	 * 
	 * Created on: Sep 10, 2013,1:57:53 PM
	 * Author: QuyenNT
	 * @param scenarioConfig
	 * @return
	 */
	public ScenarioObject processDataForEachScenario(ScenarioConfiguration scenarioConfig) {
		ScenarioObject returnObj = new ScenarioObject();
		String dataFile;
		BufferedReader br = null;	
		String currentLine;
		String[] dataArr;
				
		//List of test run through weeks of each scenario
		List<String> scenarioWeekList = listDirNames(scenarioConfig.getScenarioName());
		
		int responseIndex = configurations.getResponseColumn() - 1;
		int throughputIndex = configurations.getThroughputColumn() - 1;
		
		double baseResDiff;
		double preResDiff;
		double baseThruDiff;
		double preThruDiff;

		try {		
			for(int j = 0; j < scenarioWeekList.size();j ++){
				//Base
				if(scenarioWeekList.contains(configurations.getWeekBase())){					
					dataFile = configurations.getDataPath() + "/" + scenarioConfig.getScenarioName() + "/" + 
								configurations.getWeekBase() + "/" + ANALYSIS_FOLDER + "/" + ANALYSIS_FILE;
					
					br = new BufferedReader(new FileReader(dataFile));
					br.readLine();//by pass the first line
					
					while ((currentLine = br.readLine()) != null) {
						dataArr = currentLine.split(configurations.getDataDelimiter());						
						for (int i = 0; i < dataArr.length; i++) {
						
							//base throughput
							if(i == throughputIndex && dataArr[0].trim().startsWith(scenarioConfig.getThroughputLabel())){
								returnObj.setBaseThroughput(Integer.parseInt(dataArr[throughputIndex]));
							}							
							
							//base responsetime
							if(i == responseIndex && dataArr[0].trim().startsWith(scenarioConfig.getResponseLabelId())){
								returnObj.setBaseResponseTime(Integer.parseInt(dataArr[responseIndex]));
							}							
						}						
					}								
				}
				
				//Previous
				if(scenarioWeekList.contains(configurations.getWeekPrevious())){					
					dataFile = configurations.getDataPath() + "/" + scenarioConfig.getScenarioName() + "/" + 
								configurations.getWeekPrevious() + "/" + ANALYSIS_FOLDER + "/" + ANALYSIS_FILE;
					
					br = new BufferedReader(new FileReader(dataFile));
					br.readLine();//by pass the first line
					
					while ((currentLine = br.readLine()) != null) {
						dataArr = currentLine.split(configurations.getDataDelimiter());						
						for (int i = 0; i < dataArr.length; i++) {
						
							//previous throughput
							if(i == throughputIndex && dataArr[0].trim().startsWith(scenarioConfig.getThroughputLabel())){
								returnObj.setPreThroughput(Integer.parseInt(dataArr[throughputIndex]));
							}							
							
							//previous responsetime
							if(i == responseIndex && dataArr[0].trim().startsWith(scenarioConfig.getResponseLabelId())){
								returnObj.setPreResponseTime(Integer.parseInt(dataArr[responseIndex]));
							}
						}						
					}						
				}
				
				//This
				if(scenarioWeekList.contains(configurations.getWeekThis())){					
					dataFile = configurations.getDataPath() + "/" + scenarioConfig.getScenarioName() + "/" + 
								configurations.getWeekThis() + "/" + ANALYSIS_FOLDER + "/" + ANALYSIS_FILE;
					
					br = new BufferedReader(new FileReader(dataFile));
					br.readLine();//by pass the first line
					
					while ((currentLine = br.readLine()) != null) {
						dataArr = currentLine.split(configurations.getDataDelimiter());						
						for (int i = 0; i < dataArr.length; i++) {
						
							//this throughput
							if(i == throughputIndex && dataArr[0].trim().startsWith(scenarioConfig.getThroughputLabel())){
								returnObj.setCurrentThroughput(Integer.parseInt(dataArr[throughputIndex]));
							}							
							
							//this responsetime
							if(i == responseIndex && dataArr[0].trim().startsWith(scenarioConfig.getResponseLabelId())){
								returnObj.setCurrentResponseTime(Integer.parseInt(dataArr[responseIndex]));
							}
						}
						
					}					
				}				
			}
			//Caculate
			baseResDiff =  (double)(100*(returnObj.getCurrentResponseTime() - returnObj.getBaseResponseTime()))/returnObj.getBaseResponseTime();
			preResDiff  =  (double)(100*(returnObj.getCurrentResponseTime() - returnObj.getPreResponseTime()))/returnObj.getPreResponseTime();
			
			baseThruDiff =  (double)(100*(returnObj.getCurrentThroughput() - returnObj.getBaseThroughput()))/returnObj.getBaseThroughput();
			preThruDiff  =  (double)(100*(returnObj.getCurrentThroughput() - returnObj.getPreThroughput()))/returnObj.getPreThroughput();
			
			returnObj.setBaseResponseDiff(baseResDiff);
			returnObj.setPreResponseDiff(preResDiff);	
			
			returnObj.setBaseThroughputDiff(baseThruDiff);
			returnObj.setPreThroughputDiff(preThruDiff);	
			
			
			System.out.println("processDataForEachScenario - name=" + scenarioConfig.getScenarioName());
			System.out.println("processDataForEachScenario - res base=" + returnObj.getBaseResponseTime());
			System.out.println("processDataForEachScenario - res pre =" + returnObj.getPreResponseTime());
			System.out.println("processDataForEachScenario - res this=" + returnObj.getCurrentResponseTime());
			System.out.println("processDataForEachScenario - res base diff=" + returnObj.getBaseResponseDiff());
			System.out.println("processDataForEachScenario - res pre diff=" + returnObj.getPreResponseDiff());
			System.out.println("processDataForEachScenario - thru base diff=" + returnObj.getBaseThroughputDiff());
			System.out.println("processDataForEachScenario - thru pre diff=" + returnObj.getPreThroughputDiff());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return returnObj;
	}	
	
	/**
	 * Get list of folders of test run over weeks under a scenario folder
	 * Created on: Sep 10, 2013,9:23:52 AM
	 * Author: QuyenNT
	 * @param scenarioName
	 * @return
	 */
	public List<String> listDirNames(String scenarioName){
		try {
			ArrayList<String> dirNameList = new ArrayList<String>();
			String dirName;
			
			String path = configurations.getDataPath() + "/" + scenarioName;
			File scenarioFolder = new File(path);
			File[] fileList = scenarioFolder.listFiles();

			for (int i = 0; i < fileList.length; i++) {

				if (fileList[i].isDirectory()) {
					dirName = fileList[i].getName();
					dirNameList.add(dirName);					
				}
			}
			return dirNameList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	public void useSenderWebdav() {
		String webdavLogin = configurations.getWebdavLogin();
		String webdavPass = configurations.getWebdavPass();
		String webdavURL = configurations.getWebdavPath();

		// folderLocal =
		// "/home/annb/java/eXoProjects/access-project/cross-access/target/access-report/WEEKLY_REPORTs_PLF3.5.6-SNAPSHOT/";
		// String folderLocal = getMapGeneralConfig().get("pathTemplate") +
		// WEEKLY_REPORT
		// + mapGeneralConfig.get("platform_replace") +"/";
		//
		// String nameParentFolder = WEEKLY_REPORT +
		// mapGeneralConfig.get("platform_replace");
		//
		// SenderWebdav.sendTemlate(webdavLogin, webdavPass, webdavURL,
		// folderLocal, nameParentFolder);
		// logger.info("template send by WEBDAV done !");
	}

	/**
	 * Read template file and return a string
	 * 
	 * @param templateFile
	 * @return
	 * @throws IOException
	 */
	private String readTemplate(File templateFile) throws IOException {
		FileInputStream fileInput = new FileInputStream(templateFile);
		int byteAvailble = fileInput.available();
		byte buffer[] = new byte[byteAvailble];
		fileInput.read(buffer);
		fileInput.close();
		return new String(buffer);

	}

	/**
	 * write to a new report file with a replaced template file
	 * 
	 * @param sTemplate
	 * @param fileTemplateReplacedName
	 */
	private void writeTemplateReplaced(String sTemplate,String fileTemplateReplacedName) {
		try {
			File folderfile = new File(fileTemplateReplacedName);
			folderfile.mkdir();
			logger.info("Create folder" + fileTemplateReplacedName);
			FileOutputStream os = new FileOutputStream(fileTemplateReplacedName	+ "/content");
			for (int i = 0; i < sTemplate.length(); i++) {
				os.write(sTemplate.charAt(i));
			}
			os.close();

		} catch (FileNotFoundException e) {
			logger.error("ReportInfo writeTemplateReplaced FileNotFoundException "
					+ e.getMessage());
		} catch (IOException e) {
			logger.error("ReportInfo writeTemplateReplaced IOException "
					+ e.getMessage());
		}
	}

	/**
	 * replace chart informations
	 * 
	 * @param sTemplate
	 * @param useCase
	 * @param level
	 */
	String replaceChart(String sTemplate, int useCase) {
//		// replace chart type FailedPassed
//		// LEVELONE A originA
//		// LEVELTWO AA originAA
//		// LEVELTREE AAA originAAA
//		//
//
//		sTemplate = sTemplate.replace(
//				"FailedPassed_LEVELONE",
//				generatePulishChart(getListFailedPassedImgA_replace().get(
//						useCase)));
//		sTemplate = sTemplate.replace(
//				"FailedPassed_LEVELTWO",
//				generatePulishChart(getListFailedPassedImgAA_replace().get(
//						useCase)));
//		sTemplate = sTemplate.replace(
//				"FailedPassed_LEVELTREE",
//				generatePulishChart(getListFailedPassedImgAAA_replace().get(
//						useCase)));
//		sTemplate = sTemplate.replace(
//				"FailedPassed_origin508",
//				generatePulishChart(getListFailedPassedImg508_replace().get(
//						useCase)));
//
//		// replace chart type Percentage
//		sTemplate = sTemplate.replace("Percentage_LEVELONE",
//				generatePulishChart(getListPercentageImgA_replace()
//						.get(useCase)));
//		sTemplate = sTemplate.replace(
//				"Percentage_LEVELTWO",
//				generatePulishChart(getListPercentageImgAA_replace().get(
//						useCase)));
//		sTemplate = sTemplate.replace(
//				"Percentage_LEVELTREE",
//				generatePulishChart(getListPercentageImgAAA_replace().get(
//						useCase)));
//		sTemplate = sTemplate.replace(
//				"Percentage_origin508",
//				generatePulishChart(getListPercentageImg508_replace().get(
//						useCase)));
//
//		// replace chart type PotentialKnown
//		sTemplate = sTemplate.replace(
//				"PotentialKnown_LEVELONE",
//				generatePulishChart(getListPotentialKnownImgA_replace().get(
//						useCase)));
//		sTemplate = sTemplate.replace(
//				"PotentialKnown_LEVELTWO",
//				generatePulishChart(getListPotentialKnownImgAA_replace().get(
//						useCase)));
//		sTemplate = sTemplate.replace(
//				"PotentialKnown_LEVELTREE",
//				generatePulishChart(getListPotentialKnownImgAAA_replace().get(
//						useCase)));
//		sTemplate = sTemplate.replace(
//				"PotentialKnown_origin508",
//				generatePulishChart(getListPotentialKnownImg508_replace().get(
//						useCase)));
//
//		// replace USECASE_ORIGIN
//		sTemplate = sTemplate.replace("USECASE_ORIGIN",
//				getListSenario().get(useCase));
//		// replace ICASE
//		sTemplate = sTemplate.replace("ICASE_ORIGIN", useCase + 1 + "");

		return sTemplate;
	}
	
	//Create L1 page
	String replaceL1Info(String sTemplate) {
		// String statusStrTemplate =
		// "| SENA_NUMBER | [[**SENA_NAME**>>SENA_LINK]] |(% style=\"text-align:right;color:PRE_RES_COLOR\" %)PRE_RES_PERCENT% |(% style=\"color:PRE_STATUS_COLOR\" %)PRE_STATUS_RESULT |(% style=\"text-align:right;color:BASE_RES_COLOR\" %)BASE_RES_PERCENT% |(% style=\"color:BASE_STATUS_COLOR\" %)BASE_STATUS_RESULT";
		String statusStrTemplate = "| SCENA_NUMBER | [[**SCENA_NAME**>>SCENA_LINK]] |";
		String staticChartStrTemplate = "=== CHART_SCENA_NAME ===";
		StringBuffer senaStatusBuf = new StringBuffer();
		StringBuffer senaChartBuf = new StringBuffer();
		int x = 1;

		for (int i = 0; i < configurations.getScenariosList().size(); i++) {
			// Build content for STATUS OF WEEK part
			String tmp = statusStrTemplate;
			tmp = tmp.replace("SCENA_NUMBER", x + "");
			tmp = tmp.replace("SCENA_NAME", configurations.getScenariosList().get(i).toString().trim());
			tmp = tmp.replace("SCENA_LINK", configurations.getWikiLink()
					+ configurations.getPrefix() + "_"
					+ configurations.getScenariosList().get(i).toString().trim());

			senaStatusBuf.append(tmp + "\n");
			x++;

			// Build content for STATISTICS CHARTS part
			tmp = staticChartStrTemplate;
			tmp = tmp.replace("CHART_SCENA_NAME", configurations.getScenariosList().get(i).toString().trim());

			senaChartBuf.append(tmp + "\n");
		}
		sTemplate = sTemplate.replace("@@SCENARIO_STATUS@@",
				senaStatusBuf.toString());

		sTemplate = sTemplate.replace("@@STATISTICS_CHARTS@@",
				senaChartBuf.toString());

		System.out.println("replaceSenarioInfo-replace string:\n"
				+ senaStatusBuf.toString());

		return sTemplate;
	}
	
	String replaceL2Info(String sTemplate) {
		// String statusStrTemplate =
		// "| SENA_NUMBER | [[**SENA_NAME**>>SENA_LINK]] |(% style=\"text-align:right;color:PRE_RES_COLOR\" %)PRE_RES_PERCENT% |(% style=\"color:PRE_STATUS_COLOR\" %)PRE_STATUS_RESULT |(% style=\"text-align:right;color:BASE_RES_COLOR\" %)BASE_RES_PERCENT% |(% style=\"color:BASE_STATUS_COLOR\" %)BASE_STATUS_RESULT";
		String statusStrTemplate = "| SCENA_NUMBER | [[**SCENA_NAME**>>SCENA_LINK]] |";
		String staticChartStrTemplate = "=== CHART_SCENA_NAME ===";
		StringBuffer senaStatusBuf = new StringBuffer();
		StringBuffer senaChartBuf = new StringBuffer();
		int x = 1;

		for (int i = 0; i < configurations.getScenariosList().size(); i++) {
			// Build content for STATUS OF WEEK part
			String tmp = statusStrTemplate;
			tmp = tmp.replace("SCENA_NUMBER", x + "");
			tmp = tmp.replace("SCENA_NAME", configurations.getScenariosList().get(i).toString().trim());
			tmp = tmp.replace("SCENA_LINK", configurations.getWikiLink()
					+ configurations.getPrefix() + "_"
					+ configurations.getScenariosList().get(i).toString().trim());

			senaStatusBuf.append(tmp + "\n");
			x++;

			// Build content for STATISTICS CHARTS part
			tmp = staticChartStrTemplate;
			tmp = tmp.replace("CHART_SCENA_NAME", configurations.getScenariosList().get(i).toString().trim());

			senaChartBuf.append(tmp + "\n");
		}
		sTemplate = sTemplate.replace("@@SCENARIO_STATUS@@",
				senaStatusBuf.toString());

		sTemplate = sTemplate.replace("@@STATISTICS_CHARTS@@",
				senaChartBuf.toString());

		System.out.println("replaceSenarioInfo-replace string:\n"
				+ senaStatusBuf.toString());
		
		processData();

		return sTemplate;
	}	

	/**
	 * replace general informations
	 * 
	 * @param sTemplate
	 * @param useCase
	 */
	String replaceGeneralInfo(String sTemplate) {
		// // replace root information
		// sTemplate = sTemplate.replace("root_origin",
		// getMapGeneralConfig().get("root_replace"));
		// // replace root information
		// sTemplate = sTemplate.replace("productversion_origin",
		// getMapGeneralConfig().get("productversion_replace"));
		// // replace week information
		// sTemplate = sTemplate.replace("PLATFORM_ORIGIN",
		// getMapGeneralConfig().get("platform_replace"));
		// // replace root information
		// sTemplate = sTemplate.replace("productbuild_origin",
		// getMapGeneralConfig().get("productbuild_replace"));
		// // replace week information
		// sTemplate = sTemplate.replace("week_origin",
		// getMapGeneralConfig().get("week_replace"));

		return sTemplate;
	}

	/**
	 * generate a chart by replace in excess information and return a
	 * !imagechart! to copy to Wiki doc
	 * 
	 * @param sGoogleChart
	 * @return
	 */

	String generatePulishChart(String sGoogleChart) {
		// String keyGoogledoc = mapGeneralConfig.get("keygoogledoc");
		// sGoogleChart = sGoogleChart.replace("KEYGOOGLEDOC", keyGoogledoc);

		sGoogleChart = sGoogleChart.replace("<img src=\"", "");
		sGoogleChart = sGoogleChart.replace("\" />", "");
		return sGoogleChart;
	}
}
