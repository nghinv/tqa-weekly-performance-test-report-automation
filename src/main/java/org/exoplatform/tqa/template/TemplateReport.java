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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TemplateReport {

	final static Logger logger = Logger.getLogger(TemplateReport.class);
	
	private final static String ANALYSIS_FOLDER = "analysis";
	private final static String ANALYSIS_FILE = "analysis-AggregateReport.csv";
	private final static String DATA_LINK_FILE = "links.info";
	private final static String ANALYSIS_BOUNDARY_PARENT_GENERAL = "generalinfo";
	private final static String ANALYSIS_BOUNDARY_PARENT_SCENARIO = "scenario";
	
	private final static String CHANGE_STATUS_WORSE = "WORSE";
	private final static String CHANGE_STATUS_BETTER = "BETTER";
	private final static String CHANGE_STATUS_THE_SAME  = "THE SAME";
	private final static String CHANGE_STATUS_DECLINE  = "DECLINE"; 
	
	private final static String COLOR_WORSE = "red";
	private final static String COLOR_BETTER = "blue";
	private final static String COLOR_THE_SAME  = "black";
	private final static String COLOR_DECLINE  = "yellow"; 
	
	private final static String ENABLE_TRUE = "true";
	private final static String ENABLE_FALSE = "false";
	
	private final static String CONCLUSION_THE_SAME = "The result of Throughput and 90% line of response time show that performance of this week package is THE SAME as the previous week one";
	private final static String CONCLUSION_WORSE = "The result of Throughput and 90% line of response time show that performance of this week package is WORSE than the previous week one";
	private final static String CONCLUSION_BETTER = "The result of Throughput and 90% line of response time show that performance of this week package is BETTER than the previous week one";
	
	private final static String L1_TEMPLATE_FILE = "WEEKLY_TEST_REPORT_TEMPLATE_L1";
	private final static String L2_TEMPLATE_FILE = "WEEKLY_TEST_REPORT_TEMPLATE_L2";
	
	private Configurations configurations;
	private ScenarioObject scenarioObject;
	
	
	public TemplateReport() {

	}

	/**
	 * 
	 * Created on: Sep 16, 2013,11:28:01 AM
	 * Author: QuyenNT
	 * @param configFileXml
	 */
	void generateReport(String configFileXml) {		 
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
		
		String generatedReport;
		try {
			if(configurations.getReadTemplateOutsideProject() != null && configurations.getReadTemplateOutsideProject().equals(ENABLE_TRUE)){
				File templateFile = new File(configurations.getTemplatePath()	+ "/" + L1_TEMPLATE_FILE);			
				generatedReport = readTemplate(templateFile);
			} else {
				InputStream inputStream=ClassLoader.getSystemResourceAsStream("org/exoplatform/tqa/configs/WEEKLY_TEST_REPORT_TEMPLATE_L1");	
				generatedReport = readTemplate(inputStream);
			} 			
			generatedReport = replaceL1Info(generatedReport);
			
			writeTemplateReplaced(generatedReport, l1Folder.toString());			
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
		List<ScenarioObject> scenarioList = processData();
		ScenarioObject scenarioObject; 
		String l1Folder = configurations.getGeneratedPath() + "/"+ configurations.getPrefix();
		String templateToString;
		File templateFile;
		InputStream inputStream;
		
//		 for each scnario : read template file, replace and write a new report file
		 for (int i = 0; i < scenarioList.size(); i++) {	
			 scenarioObject = (ScenarioObject) scenarioList.get(i);
			
			try {
				if(configurations.getReadTemplateOutsideProject()!= null && configurations.getReadTemplateOutsideProject().equals(ENABLE_TRUE)){
					templateFile = new File(configurations.getTemplatePath()	+ "/" + L2_TEMPLATE_FILE);			
					templateToString = readTemplate(templateFile);
				} else {
					inputStream = ClassLoader.getSystemResourceAsStream("org/exoplatform/tqa/configs/WEEKLY_TEST_REPORT_TEMPLATE_L2");
					templateToString = readTemplate(inputStream);
				}
				
				templateToString = replaceL2Info(templateToString, scenarioObject);
				
				 //Create L2 folder under L1 folder
				 File l2Folder = new File(l1Folder +"/" +configurations.getPrefix() +
						 								"_-_" + scenarioObject.getScenarioName());
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
		ArrayList<ScenarioObject> listScenario = new ArrayList<ScenarioObject>();

		configurations = new Configurations();
		InputStream inputStream;
		
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// read config file			
			if(configFileXml != null){
				logger.info("Start loading configuration file - outside project...");
				//Read config file passed by user
				File file = new File(configFileXml);
				inputStream = new FileInputStream(file);
			}else{			
				//Read config file included in the project
				logger.info("Start loading configuration file - included in project...");
				inputStream=ClassLoader.getSystemResourceAsStream("org/exoplatform/tqa/configs/ReportConfig.xml");
			}
			
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using builder to get DOM representation of the XML file
			Document dom = db.parse(new InputSource(inputStream));
			Element root = dom.getDocumentElement();

			// Read config file
			NodeList prefix = root.getElementsByTagName("prefix");
			NodeList generatedPath = root.getElementsByTagName("generated_path");
			NodeList dataPath = root.getElementsByTagName("data_path");
			NodeList templatePath = root.getElementsByTagName("template_path");
			NodeList readTemplateOutsideProject = root.getElementsByTagName("read_template_outside_project");

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
			
			NodeList versionBase = root.getElementsByTagName("version_base");
			NodeList versionWeekPre = root.getElementsByTagName("version_week_previous");
			NodeList versionWeekThis = root.getElementsByTagName("version_week_this");
			
			NodeList listScenarioNode = root.getElementsByTagName("scenario");
			NodeList listGeneralBoundaryNode = root.getElementsByTagName("analysis_boundaries");
			NodeList listNameNode = root.getElementsByTagName("name");
			NodeList listResponseLabelIdNode = root.getElementsByTagName("responsetime_label_id");
			NodeList listResponseLabelNode = root.getElementsByTagName("responsetime_label");
			NodeList listThroughputLabelNode = root.getElementsByTagName("throughput_label");
			NodeList listResponseLabelAlias = root.getElementsByTagName("responsetime_label_alias");
			NodeList listScenarioAlias = root.getElementsByTagName("alias");
//			NodeList listScenarioUrl = root.getElementsByTagName("url");
				
			
			NodeList indicatorImgSrc = root.getElementsByTagName("image_source");
			NodeList indicatorBug = root.getElementsByTagName("bug");
			NodeList indicatorClear = root.getElementsByTagName("weather_clear");
			NodeList indicatorStorm = root.getElementsByTagName("weather_storm");
			NodeList indicatorAlert = root.getElementsByTagName("weather_severe_alert");
			
			NodeList imgResPercentNode = root.getElementsByTagName("responsetime_comparison_percent");
			NodeList imgResValueNode = root.getElementsByTagName("responsetime_comparison_value");
			NodeList imgResOverNode = root.getElementsByTagName("responsetime_overtime");
			NodeList imgResThruWeekNode = root.getElementsByTagName("responsetime_through_weeks");
			
			NodeList imgThruPercentNode = root.getElementsByTagName("throughput_comparison_percent");
			NodeList imgThruValueNode = root.getElementsByTagName("throughput_comparison_value");
			NodeList imgThruOverNode = root.getElementsByTagName("throughput_overtime");
			NodeList imgThruThruWeekNode = root.getElementsByTagName("throughput_through_weeks");
			
			// set values to configurations
			//Prefix
			String valueSpacesValues = prefix.item(0).getFirstChild().getNodeValue();
			configurations.setPrefix(valueSpacesValues);
			
			//generated Path
			valueSpacesValues = generatedPath.item(0).getFirstChild().getNodeValue();
			configurations.setGeneratedPath(valueSpacesValues);
			
			//template Path
			if(templatePath != null && templatePath.item(0)!= null){
				valueSpacesValues = templatePath.item(0).getFirstChild().getNodeValue();
				configurations.setTemplatePath(valueSpacesValues);
			}
			
			//read_template_outside_project
			if(readTemplateOutsideProject != null && readTemplateOutsideProject.item(0)!= null){
				valueSpacesValues = readTemplateOutsideProject.item(0).getFirstChild().getNodeValue();
				configurations.setReadTemplateOutsideProject(valueSpacesValues);
			}
			
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
			for (int iLevel = 0; iLevel < listScenarioNode.getLength(); iLevel++) {
				scenarioObject = new ScenarioObject();
				Element scenarioElement = (Element) listScenarioNode.item(iLevel);
				
				//name
				Node noditem = listNameNode.item(iLevel);
				scenarioObject.setScenarioName(noditem.getTextContent().trim());
								
				noditem = listResponseLabelIdNode.item(iLevel);
				scenarioObject.setResponseLabelId(noditem.getTextContent().trim());
				
				//responseLabel
				noditem = listResponseLabelNode.item(iLevel);				
				scenarioObject.setResponseLabel(noditem.getTextContent().trim());
				
				//responseLabel alias
				noditem = listResponseLabelAlias.item(iLevel);				
				scenarioObject.setResponseLabelAlias(noditem.getTextContent().trim());
				
				//scenario alias
				noditem = listScenarioAlias.item(iLevel);				
				scenarioObject.setScenarioAlias(noditem.getTextContent().trim());
				
				//scenario url
//				noditem = listScenarioUrl.item(iLevel);				
//				scenarioObject.setScenarioUrl(noditem.getTextContent().trim());				
				

				//throughputLabel
				noditem = listThroughputLabelNode.item(iLevel);
				scenarioObject.setThroughputLabel(noditem.getTextContent().trim());		
				
				//enable
				NodeList listEnableNode = scenarioElement.getElementsByTagName("enabled");
				if(listEnableNode.getLength() > 0){					
					valueSpacesValues = listEnableNode.item(0).getFirstChild().getNodeValue().trim();					
					scenarioObject.setEnable(valueSpacesValues);
				} else {
					scenarioObject.setEnable("true");
				}
								
				//analysis boundaries
				NodeList resList =  scenarioElement.getElementsByTagName("responsetime");
				NodeList thruList =  scenarioElement.getElementsByTagName("throughput");
				
				if(resList.getLength() > 0){
					scenarioObject.setBoundaryObject(readScenarioAnalysisBoundary(resList, thruList));					
				} else {
					scenarioObject.setBoundaryObject(readGeneralAnalysisBoundary(listGeneralBoundaryNode));
				}
				
				listScenario.add(scenarioObject);
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
			
			//Indicator Image source
			valueSpacesValues = indicatorImgSrc.item(0).getFirstChild().getNodeValue();
			configurations.setIndicatorImgSrc(valueSpacesValues);	
			
			//bug
			valueSpacesValues = indicatorBug.item(0).getFirstChild().getNodeValue();
			configurations.setIndicatorBug(valueSpacesValues);
			
			//Clear
			valueSpacesValues = indicatorClear.item(0).getFirstChild().getNodeValue();
			configurations.setIndicatorClear(valueSpacesValues);
			
			//Storm
			valueSpacesValues = indicatorStorm.item(0).getFirstChild().getNodeValue();
			configurations.setIndicatorStorm(valueSpacesValues);
			
			//Alert
			valueSpacesValues = indicatorAlert.item(0).getFirstChild().getNodeValue();
			configurations.setIndicatorAlert(valueSpacesValues);
			
			//version week previous
			valueSpacesValues = versionWeekPre.item(0).getFirstChild().getNodeValue();
			configurations.setVersionWeekPre(valueSpacesValues);
			
			//version week this
			valueSpacesValues = versionWeekThis.item(0).getFirstChild().getNodeValue();
			configurations.setVersionWeekThis(valueSpacesValues);			
			
			//version base
			valueSpacesValues = versionBase.item(0).getFirstChild().getNodeValue();
			configurations.setVersionBase(valueSpacesValues);
			
			//image response percent
			Element element = (Element) imgResPercentNode.item(0);
			NodeList nameNode =  element.getElementsByTagName("file_name");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgResPercent(valueSpacesValues);
			
			nameNode =  element.getElementsByTagName("image_label");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgResPercentLabel(valueSpacesValues);
			
			//image response value
			element = (Element) imgResValueNode.item(0);
			nameNode =  element.getElementsByTagName("file_name");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue();
			configurations.setImgResValue(valueSpacesValues);
			
			nameNode =  element.getElementsByTagName("image_label");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgResValueLabel(valueSpacesValues);	
			
			//image response over
			element = (Element) imgResOverNode.item(0);
			nameNode =  element.getElementsByTagName("file_name");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue();
			configurations.setImgResOver(valueSpacesValues);
			
			nameNode =  element.getElementsByTagName("image_label");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgResOverLabel(valueSpacesValues);
			
			//image response thru weeks
			element = (Element) imgResThruWeekNode.item(0);
			nameNode =  element.getElementsByTagName("file_name");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue();
			configurations.setImgResThruWeek(valueSpacesValues);
			
			nameNode =  element.getElementsByTagName("image_label");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgResThruWeekLabel(valueSpacesValues);		
			
			//image thru percent
			element = (Element) imgThruPercentNode.item(0);
			nameNode =  element.getElementsByTagName("file_name");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgThruPercent(valueSpacesValues);
			
			nameNode =  element.getElementsByTagName("image_label");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgThruPercentLabel(valueSpacesValues);
			
			//image Thru value
			element = (Element) imgThruValueNode.item(0);
			nameNode =  element.getElementsByTagName("file_name");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue();
			configurations.setImgThruValue(valueSpacesValues);
			
			nameNode =  element.getElementsByTagName("image_label");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgThruValueLabel(valueSpacesValues);	
			
			//image Thru over
			element = (Element) imgThruOverNode.item(0);
			nameNode =  element.getElementsByTagName("file_name");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue();
			configurations.setImgThruOver(valueSpacesValues);
			
			nameNode =  element.getElementsByTagName("image_label");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgThruOverLabel(valueSpacesValues);
			
			//image Thru thru weeks
			element = (Element) imgThruThruWeekNode.item(0);
			nameNode =  element.getElementsByTagName("file_name");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue();
			configurations.setImgThruThruWeek(valueSpacesValues);
			
			nameNode =  element.getElementsByTagName("image_label");
			valueSpacesValues = nameNode.item(0).getFirstChild().getNodeValue().trim();
			configurations.setImgThruThruWeekLabel(valueSpacesValues);			
					
		} catch (FileNotFoundException e) {
			logger.error("ReportInfo readConfig FileNotFoundException error: "+ e.getMessage());
		} catch (ParserConfigurationException e) {
			logger.error("ReportInfo readConfig ParserConfigurationException error: "+ e.getMessage());
		} catch (SAXException e) {
			logger.error("ReportInfo readConfig SAXException error: "+ e.getMessage());
		} catch (IOException e) {
			logger.error("ReportInfo readConfig IOException error: "+ e.getMessage());

		}
	}
	
	/**
	 * Read analysis boundaries value of a specific scenario
	 * Created on: Sep 12, 2013,9:57:11 AM
	 * Author: QuyenNT
	 * @param resNodeList
	 * @param thruNodeList
	 * @return
	 */
	public BoundaryObject readScenarioAnalysisBoundary(NodeList resNodeList, NodeList thruNodeList){
		BoundaryObject returnObj = new BoundaryObject();
		
		//Response time
		for(int j = 0; j < resNodeList.getLength(); j++){
			//res top
			Element childElement = (Element) resNodeList.item(j);
			NodeList childNode = childElement.getElementsByTagName("top");		
			String value = childNode.item(0).getFirstChild().getNodeValue();
			returnObj.setResTop(Double.parseDouble(value));
			
			//res upper			
			childNode = childElement.getElementsByTagName("upper");
			value = childNode.item(0).getFirstChild().getNodeValue();
			returnObj.setResUpper(Double.parseDouble(value));	
			
			//res lower			
			childNode = childElement.getElementsByTagName("lower");
			value = childNode.item(0).getFirstChild().getNodeValue();
			returnObj.setResLower(Double.parseDouble(value));
			
			//res bottom			
			childNode = childElement.getElementsByTagName("bottom");
			value = childNode.item(0).getFirstChild().getNodeValue();
			returnObj.setResBottom(Double.parseDouble(value));					
		}

		//Thruput
		for(int j = 0; j < thruNodeList.getLength(); j++){
			//thru top
			Element childElement = (Element) thruNodeList.item(j);
			NodeList childNode = childElement.getElementsByTagName("top");
			String value = childNode.item(0).getFirstChild().getNodeValue();
			returnObj.setThruTop(Double.parseDouble(value));
			
			//Thru upper			
			childNode = childElement.getElementsByTagName("upper");
			value = childNode.item(0).getFirstChild().getNodeValue();
			returnObj.setThruUpper(Double.parseDouble(value));
			
			//Thru lower			
			childNode = childElement.getElementsByTagName("lower");
			value = childNode.item(0).getFirstChild().getNodeValue();
			returnObj.setThruLower(Double.parseDouble(value));
			
			//Thru bottom			
			childNode = childElement.getElementsByTagName("bottom");
			value = childNode.item(0).getFirstChild().getNodeValue();
			returnObj.setThruBottom(Double.parseDouble(value));					
		}		
		
		return returnObj;
	}
	
		/**
		 * Read analysis boundaries value from generalinfor node
		 * Created on: Sep 11, 2013,5:17:11 PM
		 * Author: QuyenNT
		 * @param boundaryElement
		 * @return
		 */
	public BoundaryObject readGeneralAnalysisBoundary(NodeList generalBoundaryNodeList){
		BoundaryObject returnObj = new BoundaryObject();
		for(int i = 0; i < generalBoundaryNodeList.getLength(); i++){
			Element boundaryElement = (Element) generalBoundaryNodeList.item(i);
			
			String parentNode = boundaryElement.getParentNode().getNodeName();
			if(parentNode.equals(ANALYSIS_BOUNDARY_PARENT_GENERAL)){
				NodeList listNodes = boundaryElement.getElementsByTagName("responsetime");
				for(int j = 0; j < listNodes.getLength(); j++){
					//res top
					Element childElement = (Element) listNodes.item(j);
					NodeList childNodes = childElement.getElementsByTagName("top");
					String value = childNodes.item(0).getFirstChild().getNodeValue();
					returnObj.setResTop(Double.parseDouble(value));
					
					//res upper					
					childNodes = childElement.getElementsByTagName("upper");
					value = childNodes.item(0).getFirstChild().getNodeValue();
					returnObj.setResUpper(Double.parseDouble(value));
					
					//res lower
					childNodes = childElement.getElementsByTagName("lower");
					value = childNodes.item(0).getFirstChild().getNodeValue();
					returnObj.setResLower(Double.parseDouble(value));
					
					//res bottom					
					childNodes = childElement.getElementsByTagName("bottom");
					value = childNodes.item(0).getFirstChild().getNodeValue();
					returnObj.setResBottom(Double.parseDouble(value));					
				}
				
				//Thruput
				listNodes = boundaryElement.getElementsByTagName("throughput");
				for(int j = 0; j < listNodes.getLength(); j++){
					//thru top
					Element childElement = (Element) listNodes.item(j);
					NodeList childNodes = childElement.getElementsByTagName("top");
					String value = childNodes.item(0).getFirstChild().getNodeValue();
					returnObj.setThruTop(Double.parseDouble(value));
					
					//Thru upper					
					childNodes = childElement.getElementsByTagName("upper");
					value = childNodes.item(0).getFirstChild().getNodeValue();
					returnObj.setThruUpper(Double.parseDouble(value));
					
					//Thru lower					
					childNodes = childElement.getElementsByTagName("lower");
					value = childNodes.item(0).getFirstChild().getNodeValue();
					returnObj.setThruLower(Double.parseDouble(value));
					
					//Thru bottom					
					childNodes = childElement.getElementsByTagName("bottom");
					value = childNodes.item(0).getFirstChild().getNodeValue();
					returnObj.setThruBottom(Double.parseDouble(value));					
				}						
			}
		}
			
		return returnObj;
	}
	

	/**
	 * Read CSV file and process data
	 * Created on: Sep 10, 2013,9:53:19 AM
	 * Author: QuyenNT
	 * @return
	 */
	public List<ScenarioObject> processData() {
		List<ScenarioObject> returnList = new ArrayList<ScenarioObject>();

		List scenarioList = configurations.getScenariosList();		
		ScenarioObject scenarioObject;
		ScenarioObject processedObj;
		
		try {
			for(int i = 0; i < scenarioList.size(); i++){
				scenarioObject = (ScenarioObject)scenarioList.get(i);
				if(!scenarioObject.getEnable().equals(ENABLE_TRUE) )
					continue;
				processedObj = processDataForEachScenario(scenarioObject);
				returnList.add(processedObj);
			}			

		} catch (Exception e) {
			e.printStackTrace();		
		}

		return returnList;
	}
	
	/**
	 * Process data of each scenario
	 * Created on: Sep 10, 2013,1:57:53 PM
	 * Author: QuyenNT
	 * @param scenarioObject
	 * @return ScenarioObject
	 */
	public ScenarioObject processDataForEachScenario(ScenarioObject scenarioObject) {
		String INDICATOR_WORSE = configurations.getIndicatorImgSrc() + configurations.getIndicatorStorm();
		String INDICATOR_THE_SAME = configurations.getIndicatorImgSrc() + configurations.getIndicatorClear();
		String INDICATOR_BETTER = configurations.getIndicatorImgSrc() + configurations.getIndicatorClear();
		
		ScenarioObject returnObj = readScenarioData(scenarioObject);
						
		double baseResDiff;
		double preResDiff;
		double baseThruDiff;
		double preThruDiff;
		
		//Boundary values from config file
		double resTopConfig = scenarioObject.getBoundaryObject().getResTop();
		double resUpperConfig = scenarioObject.getBoundaryObject().getResUpper();
		double resLowerConfig = scenarioObject.getBoundaryObject().getResLower();
		double resBottomConfig = scenarioObject.getBoundaryObject().getResBottom();
		
		double thruTopConfig = scenarioObject.getBoundaryObject().getThruTop();
		double thruUpperConfig = scenarioObject.getBoundaryObject().getThruUpper();
		double thruLowerConfig = scenarioObject.getBoundaryObject().getThruLower();
		double thruBottomConfig = scenarioObject.getBoundaryObject().getThruBottom();		

		try {
			baseResDiff =  (double)(returnObj.getCurrentResponseTime() - returnObj.getBaseResponseTime())/returnObj.getBaseResponseTime();
			preResDiff  =  (double)(returnObj.getCurrentResponseTime() - returnObj.getPreResponseTime())/returnObj.getPreResponseTime();
			
			baseThruDiff =  (double)(returnObj.getCurrentThroughput() - returnObj.getBaseThroughput())/returnObj.getBaseThroughput();
			preThruDiff  =  (double)(returnObj.getCurrentThroughput() - returnObj.getPreThroughput())/returnObj.getPreThroughput();
			
			returnObj.setBaseResponseDiff(baseResDiff);
			returnObj.setPreResponseDiff(preResDiff);	
			
			returnObj.setBaseThroughputDiff(baseThruDiff);
			returnObj.setPreThroughputDiff(preThruDiff);
			
//			0.1 	10
//			0.05	5
//			-0.05	-5
//			-0.1	-10
			//NORMAL
//			Variation in range of [-5%,5%] should be treated as normal for [90% line of response time], [average response time]

			//IMPROVEMENT
//			Variation out of range [-,-10%] should be treated as an improvement of [90% line of response time], [average response time]
//			Variation in range of [-10%,-5%] should be considered as a improvement of [90% line of response time], [average response time]

			//DECLINE
//			Variation in range of [5%,10%] should be considered as a decline of [90% line of response time], [average response time]
//			Variation out of range [10%,++] should be treated as an decline of [90% line of response time], [average response time]

//		    Blue color -> OK/ BETTER
//		    Red color -> Perf REGRESSION
//		    Orange color -> should be treated as an DECLINE
//		    No color -> THE SAME
			
			//Compare
			//Base response
			
			if(baseResDiff < resBottomConfig){				
				returnObj.setBaseResponseChangeStatus(CHANGE_STATUS_BETTER);
				returnObj.setBaseResponseDiffColor(COLOR_BETTER);
				returnObj.setBaseResponseDiffIndicator(INDICATOR_BETTER);
			} else if(baseResDiff > resTopConfig){				
				returnObj.setBaseResponseChangeStatus(CHANGE_STATUS_WORSE);
				returnObj.setBaseResponseDiffColor(COLOR_WORSE);
				returnObj.setBaseResponseDiffIndicator(INDICATOR_WORSE);
			} else {
				returnObj.setBaseResponseChangeStatus(CHANGE_STATUS_THE_SAME);
				returnObj.setBaseResponseDiffColor(COLOR_THE_SAME);
				returnObj.setBaseResponseDiffIndicator(INDICATOR_THE_SAME);
			}			
			
			//Previous response
			if(preResDiff < resBottomConfig){
				returnObj.setPreResponseChangeStatus(CHANGE_STATUS_BETTER);
				returnObj.setPreResponseDiffColor(COLOR_BETTER);
				returnObj.setPreResponseDiffIndicator(INDICATOR_BETTER);
			} else if(preResDiff > resTopConfig){
				returnObj.setPreResponseChangeStatus(CHANGE_STATUS_WORSE);
				returnObj.setPreResponseDiffColor(COLOR_WORSE);
				returnObj.setPreResponseDiffIndicator(INDICATOR_WORSE);
			} else {
				returnObj.setPreResponseChangeStatus(CHANGE_STATUS_THE_SAME);
				returnObj.setPreResponseDiffColor(COLOR_THE_SAME);
				returnObj.setPreResponseDiffIndicator(INDICATOR_THE_SAME);
			}
			
			//Base Thruput
			if(baseThruDiff > thruTopConfig){				
				returnObj.setBaseThruDiffColor(COLOR_BETTER);
				returnObj.setBaseThruDiffIndicator(INDICATOR_BETTER);
			} else if(baseThruDiff < thruBottomConfig){				
				returnObj.setBaseThruDiffColor(COLOR_WORSE);
				returnObj.setBaseThruDiffIndicator(INDICATOR_WORSE);
			} else {				
				returnObj.setBaseThruDiffColor(COLOR_THE_SAME);
				returnObj.setBaseThruDiffIndicator(INDICATOR_THE_SAME);
			}			
			
			//Previous Thruput
			if(preThruDiff > thruTopConfig){				
				returnObj.setPreThruDiffColor(COLOR_BETTER);
				returnObj.setPreThruDiffIndicator(INDICATOR_BETTER);
			} else if(preThruDiff < thruBottomConfig){				
				returnObj.setPreThruDiffColor(COLOR_WORSE);
				returnObj.setPreThruDiffIndicator(INDICATOR_WORSE);
			} else {				
				returnObj.setPreThruDiffColor(COLOR_THE_SAME);
				returnObj.setPreThruDiffIndicator(INDICATOR_THE_SAME);
			}
						
								
			System.out.println("processDataForEachScenario - name=" + scenarioObject.getScenarioName());
			System.out.println("processDataForEachScenario - res base=" + returnObj.getBaseResponseTime());
			System.out.println("processDataForEachScenario - res pre =" + returnObj.getPreResponseTime());
			System.out.println("processDataForEachScenario - res this=" + returnObj.getCurrentResponseTime());
			
			System.out.println("processDataForEachScenario - res top=" + resTopConfig);
			System.out.println("processDataForEachScenario - res bottoom=" + resBottomConfig);
			System.out.println("processDataForEachScenario - res base diff=" + returnObj.getBaseResponseDiff());
			System.out.println("processDataForEachScenario - res pre diff=" + returnObj.getPreResponseDiff());

			System.out.println("processDataForEachScenario - thru top=" + resTopConfig);
			System.out.println("processDataForEachScenario - thru bottoom=" + resBottomConfig);
			System.out.println("processDataForEachScenario - thru base diff=" + returnObj.getBaseThroughputDiff());
			System.out.println("processDataForEachScenario - thru pre diff=" + returnObj.getPreThroughputDiff());
			
			System.out.println("processDataForEachScenario - res base status =" + returnObj.getBaseResponseChangeStatus());
			System.out.println("processDataForEachScenario - res base color =" + returnObj.getBaseResponseDiffColor());
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return returnObj;	
	}	
	
	/**
	 * Read scnario data from csv files
	 * Created on: Sep 11, 2013,9:08:29 AM
	 * Author: QuyenNT
	 * @param scenarioObject
	 * @return
	 */
	public ScenarioObject readScenarioData(ScenarioObject scenarioObject){
		ScenarioObject returnObj = new ScenarioObject();
		String dataFile;
		BufferedReader br = null;	
		String currentLine;
		String[] dataArr;
				
		//List of test run through weeks of each scenario
		List<String> scenarioWeekList = listDirNames(scenarioObject.getScenarioName());
		
		int responseIndex = configurations.getResponseColumn() - 1;
		int throughputIndex = configurations.getThroughputColumn() - 1;
	
		try {		
			for(int j = 0; j < scenarioWeekList.size();j ++){
				//Set scenario values to returned object				
				returnObj.setResponseLabel(scenarioObject.getResponseLabel());
				returnObj.setScenarioName(scenarioObject.getScenarioName());
				returnObj.setResponseLabelId(scenarioObject.getResponseLabelId());
				returnObj.setThroughputLabel(scenarioObject.getThroughputLabel());
				returnObj.setResponseLabelAlias(scenarioObject.getResponseLabelAlias());
				returnObj.setBoundaryObject(scenarioObject.getBoundaryObject());
				returnObj.setScenarioAlias(scenarioObject.getScenarioAlias());
				returnObj.setScenarioUrl(scenarioObject.getScenarioUrl());
								
				//Base
				if(scenarioWeekList.contains(configurations.getWeekBase())){					
					dataFile = configurations.getDataPath() + "/" + scenarioObject.getScenarioName() + "/" + 
								configurations.getWeekBase() + "/" + ANALYSIS_FOLDER + "/" + ANALYSIS_FILE;
					
					br = new BufferedReader(new FileReader(dataFile));
					br.readLine();//by pass the first line
					
					while ((currentLine = br.readLine()) != null) {
						dataArr = currentLine.split(configurations.getDataDelimiter());		

						for (int i = 0; i < dataArr.length; i++) {
						
							//base throughput
							if(i == throughputIndex && dataArr[0].trim().replaceAll("\"", "").startsWith(scenarioObject.getThroughputLabel())){
								returnObj.setBaseThroughput(Integer.parseInt(dataArr[throughputIndex]));
							}							
							
							//base responsetime
							if(i == responseIndex && dataArr[0].trim().replaceAll("\"", "").startsWith(scenarioObject.getResponseLabelId().trim())){
								returnObj.setBaseResponseTime(Integer.parseInt(dataArr[responseIndex]));
							}							
						}						
					}								
				}
				
				//Previous
				if(scenarioWeekList.contains(configurations.getWeekPrevious())){					
					dataFile = configurations.getDataPath() + "/" + scenarioObject.getScenarioName() + "/" + 
								configurations.getWeekPrevious() + "/" + ANALYSIS_FOLDER + "/" + ANALYSIS_FILE;
					
					br = new BufferedReader(new FileReader(dataFile));
					br.readLine();//by pass the first line
					
					while ((currentLine = br.readLine()) != null) {
						dataArr = currentLine.split(configurations.getDataDelimiter());						
						for (int i = 0; i < dataArr.length; i++) {
						
							//previous throughput
							if(i == throughputIndex && dataArr[0].trim().replaceAll("\"", "").startsWith(scenarioObject.getThroughputLabel())){
								returnObj.setPreThroughput(Integer.parseInt(dataArr[throughputIndex]));
							}							
							
							//previous responsetime
							if(i == responseIndex && dataArr[0].trim().replaceAll("\"", "").startsWith(scenarioObject.getResponseLabelId())){
								returnObj.setPreResponseTime(Integer.parseInt(dataArr[responseIndex]));
							}
						}						
					}						
				}
				
				//This
				if(scenarioWeekList.contains(configurations.getWeekThis())){					
					dataFile = configurations.getDataPath() + "/" + scenarioObject.getScenarioName() + "/" + 
								configurations.getWeekThis() + "/" + ANALYSIS_FOLDER + "/" + ANALYSIS_FILE;
					
					br = new BufferedReader(new FileReader(dataFile));
					br.readLine();//by pass the first line
					
					while ((currentLine = br.readLine()) != null) {
						dataArr = currentLine.split(configurations.getDataDelimiter());						
						for (int i = 0; i < dataArr.length; i++) {
						
							//this throughput
							if(i == throughputIndex && dataArr[0].trim().replaceAll("\"", "").startsWith(scenarioObject.getThroughputLabel())){
								returnObj.setCurrentThroughput(Integer.parseInt(dataArr[throughputIndex]));
							}							
							
							//this responsetime
							if(i == responseIndex && dataArr[0].trim().replaceAll("\"", "").startsWith(scenarioObject.getResponseLabelId())){
								returnObj.setCurrentResponseTime(Integer.parseInt(dataArr[responseIndex]));
							}
						}						
					}					
				}				
			}//End for			
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
	 * 
	 * Created on: Sep 13, 2013,11:32:36 AM
	 * Author: QuyenNT
	 * @param scenarioObject
	 * @return
	 */
	public List<String> readLinkInfo(ScenarioObject scenarioObject){	
		List<String> linkList = new ArrayList<String>();
		String dataFile;
		BufferedReader br = null;	
		String currentLine;
		
		try{
			dataFile = configurations.getDataPath() + "/" + scenarioObject.getScenarioName() + "/" + 
					configurations.getWeekThis() + "/" + ANALYSIS_FOLDER + "/" + DATA_LINK_FILE;
			
			br = new BufferedReader(new FileReader(dataFile));
					
			while ((currentLine = br.readLine()) != null) {				
				linkList.add(currentLine.trim());
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
			return linkList;		 		
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
			
			if(fileList!= null){
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].isDirectory()) {
						dirName = fileList[i].getName();
						dirNameList.add(dirName);					
					}
				}
			} else {
				logger.log(Priority.ERROR, "No folder found");
			}
			return dirNameList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	public void useSenderWebdav() {
		try {
			String webdavLogin = configurations.getWebdavLogin().trim();
			String webdavPass = configurations.getWebdavPass().trim();
			String webdavURL = configurations.getWebdavPath().trim();
			

			String folderLocal = configurations.getGeneratedPath().trim() + "/" + configurations.getPrefix().trim();			 
			
			String nameParentFolder = configurations.getPrefix().trim();
			
			SenderWebdav.sendTemlate(webdavLogin, webdavPass, webdavURL, folderLocal, nameParentFolder);
			logger.info("template sent by WEBDAV done !");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Created on: Sep 16, 2013,9:32:22 AM
	 * Author: QuyenNT
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private String readTemplate(InputStream inputStream) throws IOException {
		int byteAvailble = inputStream.available();
		byte buffer[] = new byte[byteAvailble];
		inputStream.read(buffer);
		inputStream.close();
		return new String(buffer);

	}
	
	/**
	 * 
	 * Created on: Sep 16, 2013,11:12:42 AM
	 * Author: QuyenNT
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


	//Create L1 page
	String replaceL1Info(String sTemplate) {
		 DecimalFormat df = new DecimalFormat("#.##");
		 		 
		List scenarioList = processData();
		 String statusStrTemplate =
		 "| SCENA_NUMBER | [[**SCENA_NAME**>>SCENA_LINK]] |(% style=\"text-align:right;color:PRE_RES_COLOR\" %)" +
		 "PRE_RES_PERCENT% |(% style=\"color:PRE_RES_COLOR\" %)PRE_STATUS_RESULT |" +
		 "(% style=\"text-align:right;color:BASE_RES_COLOR\" %)BASE_RES_PERCENT% |" +
		 "(% style=\"color:BASE_RES_COLOR\" %)BASE_STATUS_RESULT";
		 
		String staticChartStrTemplate = "=== CHART_SCENA_NAME ===\n" +
				"|[[image:RES_IMAGE||width=\"600\" height=\"400\" alt=\"90% line of response time (ms) over weeks\"]] " +
				"|[[image:THRU_IMAGE||width=\"600\" height=\"400\" alt=\"Throughput (requests) over weeks\"]]";
		StringBuffer scenaStatusBuf = new StringBuffer();
		StringBuffer scenaChartBuf = new StringBuffer();
		ScenarioObject scenario;
		int x = 1;

		for (int i = 0; i < scenarioList.size(); i++) {
			scenario = (ScenarioObject) scenarioList.get(i);
			
			// Build content for STATUS OF WEEK part
			String tmp = statusStrTemplate;
			tmp = tmp.replace("SCENA_NUMBER", x + "");
			tmp = tmp.replace("SCENA_NAME", scenario.getScenarioName());
			tmp = tmp.replace("SCENA_LINK", configurations.getWikiLink()+ configurations.getPrefix() 
												+ "_" + scenario.getScenarioName());
			tmp = tmp.replace("PRE_RES_COLOR", scenario.getPreResponseDiffColor());
			tmp = tmp.replace("PRE_RES_PERCENT", df.format(scenario.getPreResponseDiff()*100));
			tmp = tmp.replace("PRE_STATUS_RESULT", scenario.getPreResponseChangeStatus());
			tmp = tmp.replace("BASE_RES_COLOR", scenario.getBaseResponseDiffColor());
			tmp = tmp.replace("BASE_RES_PERCENT", df.format(scenario.getBaseResponseDiff()*100));
			tmp = tmp.replace("BASE_STATUS_RESULT", scenario.getBaseResponseChangeStatus());

			scenaStatusBuf.append(tmp + "\n");
			x++;

			// Build content for STATISTICS CHARTS part
			tmp = staticChartStrTemplate;
			tmp = tmp.replace("CHART_SCENA_NAME", scenario.getScenarioName());
			tmp = tmp.replace("RES_IMAGE", scenario.getScenarioName() + "_" + configurations.getImgResThruWeek());
			tmp = tmp.replace("THRU_IMAGE",scenario.getScenarioName() + "_" + configurations.getImgThruThruWeek());

			scenaChartBuf.append(tmp + "\n");
		}
		sTemplate = sTemplate.replace("@@SCENARIO_STATUS@@",scenaStatusBuf.toString());

		sTemplate = sTemplate.replace("@@STATISTICS_CHARTS@@",scenaChartBuf.toString());

		System.out.println("replaceSenarioInfo-replace string:\n"+ scenaStatusBuf.toString());

		return sTemplate;
	}
	
	String replaceL2Info(String sTemplate,ScenarioObject scenario) {	
		DecimalFormat df = new DecimalFormat("#.##");
		DecimalFormat intFormat = new DecimalFormat("#,###"); 
		
		//Response
		sTemplate = sTemplate.replace("@@VERSION_BASE@@",configurations.getVersionBase() + "-BASE");
		sTemplate = sTemplate.replace("@@REQUEST_PAGE@@", scenario.getResponseLabelAlias());
		sTemplate = sTemplate.replace("@@REPONSE_BASE_COLOR@@", scenario.getBaseResponseDiffColor());
		sTemplate = sTemplate.replace("@@REPONSE_BASE_DIFF@@", df.format(scenario.getBaseResponseDiff()*100));
		sTemplate = sTemplate.replace("@@REPONSE_BASE_INDICATOR@@", scenario.getBaseResponseDiffIndicator());
//		sTemplate = sTemplate.replace("@@REPONSE_BASE_VALUE_COLOR@@", scenario.getBaseResponseDiffColor());
		sTemplate = sTemplate.replace("@@REPONSE_VALUE@@", intFormat.format(scenario.getCurrentResponseTime()));
		
		sTemplate = sTemplate.replace("@@VERSION_WEEK@@",configurations.getVersionWeekPre());			
		sTemplate = sTemplate.replace("@@REPONSE_PREVIOUS_COLOR@@", scenario.getPreResponseDiffColor());
		sTemplate = sTemplate.replace("@@REPONSE_PREVIOUS_DIFF@@", df.format(scenario.getPreResponseDiff()*100));
		sTemplate = sTemplate.replace("@@REPONSE_PREVIOUS_INDICATOR@@", scenario.getPreResponseDiffIndicator());
//		sTemplate = sTemplate.replace("@@REPONSE_PREVIOUS_VALUE_COLOR@@", scenario.getPreResponseDiffColor());					
	
		//Thruput
		sTemplate = sTemplate.replace("@@THRU_BASE_COLOR@@", scenario.getBaseThruDiffColor());
		sTemplate = sTemplate.replace("@@THRU_BASE_DIFF@@", df.format(scenario.getBaseThroughputDiff()*100));
		sTemplate = sTemplate.replace("@@THRU_BASE_INDICATOR@@", scenario.getBaseThruDiffIndicator());
		sTemplate = sTemplate.replace("@@THRU_VALUE@@", intFormat.format(scenario.getCurrentThroughput()));
							
		sTemplate = sTemplate.replace("@@THRU_PREVIOUS_COLOR@@", scenario.getPreThruDiffColor());
		sTemplate = sTemplate.replace("@@THRU_PREVIOUS_DIFF@@", df.format(scenario.getPreThroughputDiff()*100));
		sTemplate = sTemplate.replace("@@THRU_PREVIOUS_INDICATOR@@", scenario.getPreThruDiffIndicator());
		
		sTemplate = sTemplate.replace("@@IMG_TRAN_PER_SECOND@@", configurations.getImgThruOver());
		sTemplate = sTemplate.replace("@@IMG_RES_OVER@@", configurations.getImgResOver());
		
		sTemplate = sTemplate.replace("@@SCENARIO_ALIAS@@", scenario.getScenarioAlias());
		sTemplate = sTemplate.replace("@@IMG_RES_NAV@@", configurations.getImgResPercent());
		sTemplate = sTemplate.replace("@@IMG_RES_LOW@@", configurations.getImgResValue());
		sTemplate = sTemplate.replace("@@IMG_THRU_HIG@@", configurations.getImgThruValue());
				
		if(scenario.getBaseResponseChangeStatus().equals(CHANGE_STATUS_BETTER)){
			sTemplate = sTemplate.replace("@@CONCLUSION_MESSAGE@@", CONCLUSION_BETTER);	
			sTemplate = sTemplate.replace("@@CONCLUSION_COLOR@@", COLOR_BETTER);
		}
		if(scenario.getBaseResponseChangeStatus().equals(CHANGE_STATUS_WORSE)){
			sTemplate = sTemplate.replace("@@CONCLUSION_MESSAGE@@", CONCLUSION_WORSE);
			sTemplate = sTemplate.replace("@@CONCLUSION_COLOR@@", COLOR_WORSE);
		}
		if(scenario.getBaseResponseChangeStatus().equals(CHANGE_STATUS_THE_SAME)){
			sTemplate = sTemplate.replace("@@CONCLUSION_MESSAGE@@", CONCLUSION_THE_SAME);
			sTemplate = sTemplate.replace("@@CONCLUSION_COLOR@@", COLOR_DECLINE);
		}
		
				
		List<String> dataLink = readLinkInfo(scenario);	
		System.out.println("Data link 1:" + dataLink.get(0));
		sTemplate = sTemplate.replace("@@DATA_LINK1@@", configurations.getVersionWeekThis() + "-1: " + dataLink.get(0));
		sTemplate = sTemplate.replace("@@DATA_LINK2@@", configurations.getVersionWeekThis() + "-2: " + dataLink.get(1));
		sTemplate = sTemplate.replace("@@DATA_LINK3@@", configurations.getVersionWeekThis() + "-3: " + dataLink.get(2));
		sTemplate = sTemplate.replace("@@DATA_LINK4@@", configurations.getVersionWeekThis() + "-BASE: " + dataLink.get(3));
		
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
