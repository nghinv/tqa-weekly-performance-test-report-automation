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

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          annb@exoplatform.com
 * Apr 2, 2012  
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

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

  final static Logger     logger        = Logger.getLogger(TemplateReport.class);

  // map of general configuration
  HashMap<String, String> mapGeneralConfig;

  String                  ACCESS_REPORT = "ACCESS_Reports_";

  // list of use cases
  ArrayList<String>       listUseCase;

  // list of use cases
  ArrayList<String>       listSite;

  // list of template files
  ArrayList<File>         listTemplateFile;

  // list of Passed-Failed rules image chart

  ArrayList<String>       listFailedPassedImgA_replace;

  ArrayList<String>       listFailedPassedImgAA_replace;

  ArrayList<String>       listFailedPassedImgAAA_replace;

  ArrayList<String>       listFailedPassedImg508_replace;

  // list of Percentage image chart

  ArrayList<String>       listPercentageImgA_replace;

  ArrayList<String>       listPercentageImgAA_replace;

  ArrayList<String>       listPercentageImgAAA_replace;

  ArrayList<String>       listPercentageImg508_replace;

  // list of Known-Potential image chart

  ArrayList<String>       listPotentialKnownImgA_replace;

  ArrayList<String>       listPotentialKnownImgAA_replace;

  ArrayList<String>       listPotentialKnownImgAAA_replace;

  ArrayList<String>       listPotentialKnownImg508_replace;

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
    // for each use case : read template file, replace and write a new
    // report file
    for (int iCase = 0; iCase < getListUseCase().size(); iCase++) {

      File templateFile = listTemplateFile.get(iCase);
      String templateToString = readTemplate(templateFile);
      templateToString = replaceGeneralInfo(templateToString);
      templateToString = replaceChart(templateToString, iCase);

      String platformVersion = mapGeneralConfig.get("platform_replace");

      String writeFile = getMapGeneralConfig().get("pathTemplate") + ACCESS_REPORT
          + mapGeneralConfig.get("platform_replace") + "/" + ACCESS_REPORT + platformVersion + "_"
          + getListUseCase().get(iCase);

      writeTemplateReplaced(templateToString, writeFile);
    }

    // write a general report
    logger.info("GENERAL report generated");
    File generalReport = new File(mapGeneralConfig.get("pathTemplate") + "template/GENERAL");
    String templateGeneralReport = readTemplate(generalReport);
    templateGeneralReport = replaceGeneralInfo(templateGeneralReport);
    templateGeneralReport = generatePulishChart(templateGeneralReport);
    String writeFileGeneralReport = getMapGeneralConfig().get("pathTemplate") + ACCESS_REPORT
        + mapGeneralConfig.get("platform_replace");
    writeTemplateReplaced(templateGeneralReport, writeFileGeneralReport);

//    // write w3c report
//    logger.info("W3C report generated");
//    File w3cReport = new File(mapGeneralConfig.get("pathTemplate") + "template/W3C");
//    String templatew3cGeneralReport = readTemplate(w3cReport);
//    templatew3cGeneralReport = replaceGeneralInfo(templatew3cGeneralReport);
//    templatew3cGeneralReport = generatePulishChart(templatew3cGeneralReport);
//    String platformVersion = mapGeneralConfig.get("platform_replace");
//
//    String writew3cFileGeneralReport = getMapGeneralConfig().get("pathTemplate") + ACCESS_REPORT
//        + mapGeneralConfig.get("platform_replace") + "/" + ACCESS_REPORT + platformVersion + "_W3C";
//
//    writeTemplateReplaced(templatew3cGeneralReport, writew3cFileGeneralReport);

  }

  /**
   * read xml config file then register informations into hash map and list
   * 
   * @param configFileXml
   * @throws IOException
   */
  void readConfig(String configFileXml) {

    // map of general configuration
    mapGeneralConfig = new HashMap<String, String>();
    // list of use cases
    listUseCase = new ArrayList<String>();
    // list of site
    listSite = new ArrayList<String>();

    // list of template files
    listTemplateFile = new ArrayList<File>();

    // list of Passed-Failed rules image chart
    listFailedPassedImgA_replace = new ArrayList<String>();
    listFailedPassedImgAA_replace = new ArrayList<String>();
    listFailedPassedImgAAA_replace = new ArrayList<String>();
    listFailedPassedImg508_replace = new ArrayList<String>();

    // list of Percentage image chart
    listPercentageImgA_replace = new ArrayList<String>();
    listPercentageImgAA_replace = new ArrayList<String>();
    listPercentageImgAAA_replace = new ArrayList<String>();
    listPercentageImg508_replace = new ArrayList<String>();

    // list of Known-Potential image chart
    listPotentialKnownImgA_replace = new ArrayList<String>();
    listPotentialKnownImgAA_replace = new ArrayList<String>();
    listPotentialKnownImgAAA_replace = new ArrayList<String>();
    listPotentialKnownImg508_replace = new ArrayList<String>();

    // read config file
    // read Accessibility.xml file
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
      NodeList listInfoGeneralroot_replace = root.getElementsByTagName("root_replace");
      NodeList listInfoGeneralproductversion_replace = root.getElementsByTagName("productversion_replace");
      NodeList listInfoGeneralplatform_replace = root.getElementsByTagName("platform_replace");
      NodeList listInfoGeneralproductbuild_replace = root.getElementsByTagName("productbuild_replace");
      NodeList listInfoGeneralkeyGoogleChart = root.getElementsByTagName("keygoogledoc");
      NodeList listInfoGeneralweek_replace = root.getElementsByTagName("week_replace");
      NodeList listInfoGeneralpathTemplate = root.getElementsByTagName("pathTemplate");
      NodeList listInfoGeneraloutputsuffix = root.getElementsByTagName("outputsuffix");
      
      //WEBDAV
      NodeList listInfoGeneralWEBDAVLogin = root.getElementsByTagName("webdavLogin");
      NodeList listInfoGeneralWEBDAVPass = root.getElementsByTagName("webdavPass");
      NodeList listInfoGeneralWEBDAVTemplate = root.getElementsByTagName("webdavTemplate");

      
      NodeList listUseCaseNode = root.getElementsByTagName("name");

      NodeList listSiteNode = root.getElementsByTagName("site");

      // LEVEL A
      NodeList listFailedPassed_replaceA = root.getElementsByTagName("FailedPassed_replaceA");
      NodeList listPercentage_replaceA = root.getElementsByTagName("Percentage_replaceA");
      NodeList listPotentialKnown_replaceA = root.getElementsByTagName("PotentialKnown_replaceA");

      // LEVEL AA
      NodeList listFailedPassed_replaceAA = root.getElementsByTagName("FailedPassed_replaceAA");
      NodeList listPercentage_replaceAA = root.getElementsByTagName("Percentage_replaceAA");
      NodeList listPotentialKnown_replaceAA = root.getElementsByTagName("PotentialKnown_replaceAA");

      // LEVEL AAA
      NodeList listFailedPassed_replaceAAA = root.getElementsByTagName("FailedPassed_replaceAAA");
      NodeList listPercentage_replaceAAA = root.getElementsByTagName("Percentage_replaceAA");
      NodeList listPotentialKnown_replaceAAA = root.getElementsByTagName("PotentialKnown_replaceAAA");

      // LEVEL 508
      NodeList listFailedPassed_replace508 = root.getElementsByTagName("FailedPassed_replace508");
      NodeList listPercentage_replace508 = root.getElementsByTagName("Percentage_replace508");
      NodeList listPotentialKnown_replace508 = root.getElementsByTagName("PotentialKnown_replace508");

      // add to general list
      String valueSpacesValues = listInfoGeneralroot_replace.item(0).getFirstChild().getNodeValue();
      mapGeneralConfig.put("root_replace", valueSpacesValues);
      valueSpacesValues = listInfoGeneralproductversion_replace.item(0)
                                                               .getFirstChild()
                                                               .getNodeValue();
      mapGeneralConfig.put("productversion_replace", valueSpacesValues);
      valueSpacesValues = listInfoGeneralplatform_replace.item(0).getFirstChild().getNodeValue();
      mapGeneralConfig.put("platform_replace", valueSpacesValues);
      valueSpacesValues = listInfoGeneralproductbuild_replace.item(0)
                                                             .getFirstChild()
                                                             .getNodeValue();
      mapGeneralConfig.put("productbuild_replace", valueSpacesValues);
      valueSpacesValues = listInfoGeneralweek_replace.item(0).getFirstChild().getNodeValue();
      mapGeneralConfig.put("week_replace", valueSpacesValues);
      valueSpacesValues = listInfoGeneralpathTemplate.item(0).getFirstChild().getNodeValue();
      mapGeneralConfig.put("pathTemplate", valueSpacesValues);
      valueSpacesValues = listInfoGeneraloutputsuffix.item(0).getFirstChild().getNodeValue();
      mapGeneralConfig.put("outputsuffix", valueSpacesValues);
      valueSpacesValues = listInfoGeneralkeyGoogleChart.item(0).getFirstChild().getNodeValue();
      mapGeneralConfig.put("keygoogledoc", valueSpacesValues);
      
      //WEBDAV
      valueSpacesValues = listInfoGeneralWEBDAVLogin.item(0).getFirstChild().getNodeValue();
      mapGeneralConfig.put("webdavLogin", valueSpacesValues);
      valueSpacesValues = listInfoGeneralWEBDAVPass.item(0).getFirstChild().getNodeValue();
      mapGeneralConfig.put("webdavPass", valueSpacesValues);
      valueSpacesValues = listInfoGeneralWEBDAVTemplate.item(0).getFirstChild().getNodeValue();
      mapGeneralConfig.put("webdavTemplate", valueSpacesValues);      
      

      File report = new File(mapGeneralConfig.get("pathTemplate") + mapGeneralConfig.get("platform_replace"));

      report.mkdir();

      // usecase and read file template
      logger.info("Read GENERIC file");
      for (int iLevel = 0; iLevel < listUseCaseNode.getLength(); iLevel++) {
        Node noditem = listUseCaseNode.item(iLevel);
        listUseCase.add(noditem.getTextContent());
        // replace for generic template
        File newFile = new File(mapGeneralConfig.get("pathTemplate") + "template/GENERIC");
        listTemplateFile.add(newFile);
      }

      // site
      for (int iSite = 0; iSite < listSiteNode.getLength(); iSite++) {
        Node noditem = listSiteNode.item(iSite);
        listSite.add(noditem.getTextContent());
      }

      /**
       * listFailedPassed
       */
      for (int iLevel = 0; iLevel < listFailedPassed_replaceA.getLength(); iLevel++) {
        Node noditem = listFailedPassed_replaceA.item(iLevel);
        listFailedPassedImgA_replace.add(noditem.getTextContent());
      }
      for (int iLevel = 0; iLevel < listFailedPassed_replaceAA.getLength(); iLevel++) {
        Node noditem = listFailedPassed_replaceAA.item(iLevel);
        listFailedPassedImgAA_replace.add(noditem.getTextContent());
      }
      for (int iLevel = 0; iLevel < listFailedPassed_replaceAAA.getLength(); iLevel++) {
        Node noditem = listFailedPassed_replaceAAA.item(iLevel);
        listFailedPassedImgAAA_replace.add(noditem.getTextContent());
      }
      for (int iLevel = 0; iLevel < listFailedPassed_replace508.getLength(); iLevel++) {
        Node noditem = listFailedPassed_replace508.item(iLevel);
        listFailedPassedImg508_replace.add(noditem.getTextContent());
      }

      /**
       * Percentage_replace
       */
      for (int iLevel = 0; iLevel < listPercentage_replaceA.getLength(); iLevel++) {
        Node noditem = listPercentage_replaceA.item(iLevel);
        listPercentageImgA_replace.add(noditem.getTextContent());
      }
      for (int iLevel = 0; iLevel < listPercentage_replaceAA.getLength(); iLevel++) {
        Node noditem = listPercentage_replaceAA.item(iLevel);
        listPercentageImgAA_replace.add(noditem.getTextContent());
      }
      for (int iLevel = 0; iLevel < listPercentage_replaceAAA.getLength(); iLevel++) {
        Node noditem = listPercentage_replaceAAA.item(iLevel);
        listPercentageImgAAA_replace.add(noditem.getTextContent());
      }
      for (int iLevel = 0; iLevel < listPercentage_replace508.getLength(); iLevel++) {
        Node noditem = listPercentage_replace508.item(iLevel);
        listPercentageImg508_replace.add(noditem.getTextContent());
      }

      /**
       * PotentialKnown_replace
       */
      for (int iLevel = 0; iLevel < listPotentialKnown_replaceA.getLength(); iLevel++) {
        Node noditem = listPotentialKnown_replaceA.item(iLevel);
        listPotentialKnownImgA_replace.add(noditem.getTextContent());
      }
      for (int iLevel = 0; iLevel < listPotentialKnown_replaceAA.getLength(); iLevel++) {
        Node noditem = listPotentialKnown_replaceAA.item(iLevel);
        listPotentialKnownImgAA_replace.add(noditem.getTextContent());
      }
      for (int iLevel = 0; iLevel < listPotentialKnown_replaceAAA.getLength(); iLevel++) {
        Node noditem = listPotentialKnown_replaceAAA.item(iLevel);
        listPotentialKnownImgAAA_replace.add(noditem.getTextContent());
      }
      for (int iLevel = 0; iLevel < listPotentialKnown_replace508.getLength(); iLevel++) {
        Node noditem = listPotentialKnown_replace508.item(iLevel);
        listPotentialKnownImg508_replace.add(noditem.getTextContent());
      }

    } catch (FileNotFoundException e) {
      logger.error("ReportInfo readConfig FileNotFoundException error: " + e.getMessage());
    } catch (ParserConfigurationException e) {
      logger.error("ReportInfo readConfig ParserConfigurationException error: " + e.getMessage());
    } catch (SAXException e) {
      logger.error("ReportInfo readConfig SAXException error: " + e.getMessage());
    } catch (IOException e) {
      logger.error("ReportInfo readConfig IOException error: " + e.getMessage());

    }

  }
  
  public void useSenderWebdav() {
    String webdavLogin = mapGeneralConfig.get("webdavLogin");
    String webdavPass = mapGeneralConfig.get("webdavPass");
    String webdavURL = mapGeneralConfig.get("webdavTemplate");    
    
//    folderLocal = "/home/annb/java/eXoProjects/access-project/cross-access/target/access-report/ACCESS_Reports_PLF3.5.6-SNAPSHOT/";    
    String folderLocal = getMapGeneralConfig().get("pathTemplate") + ACCESS_REPORT
        + mapGeneralConfig.get("platform_replace") +"/";
    
    String nameParentFolder = ACCESS_REPORT + mapGeneralConfig.get("platform_replace");

    SenderWebdav.sendTemlate(webdavLogin, webdavPass, webdavURL, folderLocal, nameParentFolder);
    logger.info("template send by WEBDAV done !");    
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
  private void writeTemplateReplaced(String sTemplate, String fileTemplateReplacedName) {
    try {
      File folderfile = new File(fileTemplateReplacedName);
      folderfile.mkdir();
      logger.info("Create folder" + fileTemplateReplacedName);
      FileOutputStream os = new FileOutputStream(fileTemplateReplacedName + "/content");
      for (int i = 0; i < sTemplate.length(); i++) {
        os.write(sTemplate.charAt(i));
      }
      os.close();

    } catch (FileNotFoundException e) {
      logger.error("ReportInfo writeTemplateReplaced FileNotFoundException " + e.getMessage());
    } catch (IOException e) {
      logger.error("ReportInfo writeTemplateReplaced IOException " + e.getMessage());
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
    // replace chart type FailedPassed
    // LEVELONE A originA
    // LEVELTWO AA originAA
    // LEVELTREE AAA originAAA
    //

    sTemplate = sTemplate.replace("FailedPassed_LEVELONE",
                                  generatePulishChart(getListFailedPassedImgA_replace().get(useCase)));
    sTemplate = sTemplate.replace("FailedPassed_LEVELTWO",
                                  generatePulishChart(getListFailedPassedImgAA_replace().get(useCase)));
    sTemplate = sTemplate.replace("FailedPassed_LEVELTREE",
                                  generatePulishChart(getListFailedPassedImgAAA_replace().get(useCase)));
    sTemplate = sTemplate.replace("FailedPassed_origin508",
                                  generatePulishChart(getListFailedPassedImg508_replace().get(useCase)));

    // replace chart type Percentage
    sTemplate = sTemplate.replace("Percentage_LEVELONE",
                                  generatePulishChart(getListPercentageImgA_replace().get(useCase)));
    sTemplate = sTemplate.replace("Percentage_LEVELTWO",
                                  generatePulishChart(getListPercentageImgAA_replace().get(useCase)));
    sTemplate = sTemplate.replace("Percentage_LEVELTREE",
                                  generatePulishChart(getListPercentageImgAAA_replace().get(useCase)));
    sTemplate = sTemplate.replace("Percentage_origin508",
                                  generatePulishChart(getListPercentageImg508_replace().get(useCase)));

    // replace chart type PotentialKnown
    sTemplate = sTemplate.replace("PotentialKnown_LEVELONE",
                                  generatePulishChart(getListPotentialKnownImgA_replace().get(useCase)));
    sTemplate = sTemplate.replace("PotentialKnown_LEVELTWO",
                                  generatePulishChart(getListPotentialKnownImgAA_replace().get(useCase)));
    sTemplate = sTemplate.replace("PotentialKnown_LEVELTREE",
                                  generatePulishChart(getListPotentialKnownImgAAA_replace().get(useCase)));
    sTemplate = sTemplate.replace("PotentialKnown_origin508",
                                  generatePulishChart(getListPotentialKnownImg508_replace().get(useCase)));

    // replace USECASE_ORIGIN
    sTemplate = sTemplate.replace("USECASE_ORIGIN", getListUseCase().get(useCase));
    // replace ICASE
    sTemplate = sTemplate.replace("ICASE_ORIGIN", useCase + 1 + "");

    // replace SITE_ORIGIN
    String siteOrigin = getMapGeneralConfig().get("root_replace") + getListSite().get(useCase);
    sTemplate = sTemplate.replace("SITE_ORIGIN", siteOrigin);

    return sTemplate;
  }

  /**
   * replace general informations
   * 
   * @param sTemplate
   * @param useCase
   */
  String replaceGeneralInfo(String sTemplate) {
    // replace root information
    sTemplate = sTemplate.replace("root_origin", getMapGeneralConfig().get("root_replace"));
    // replace root information
    sTemplate = sTemplate.replace("productversion_origin",
                                  getMapGeneralConfig().get("productversion_replace"));
    // replace week information
    sTemplate = sTemplate.replace("PLATFORM_ORIGIN", getMapGeneralConfig().get("platform_replace"));
    // replace root information
    sTemplate = sTemplate.replace("productbuild_origin",
                                  getMapGeneralConfig().get("productbuild_replace"));
    // replace week information
    sTemplate = sTemplate.replace("week_origin", getMapGeneralConfig().get("week_replace"));

    return sTemplate;
  }

  /**
   * generate a chart by replace in excess information and return a !imagechart!
   * to copy to Wiki doc
   * 
   * @param sGoogleChart
   * @return
   */

  String generatePulishChart(String sGoogleChart) {
    String keyGoogledoc = mapGeneralConfig.get("keygoogledoc");
    sGoogleChart = sGoogleChart.replace("KEYGOOGLEDOC", keyGoogledoc);

    sGoogleChart = sGoogleChart.replace("<img src=\"", "");
    sGoogleChart = sGoogleChart.replace("\" />", "");
    return sGoogleChart;
  }

  public HashMap<String, String> getMapGeneralConfig() {
    return mapGeneralConfig;
  }

  public void setMapGeneralConfig(HashMap<String, String> mapGeneralConfig) {
    this.mapGeneralConfig = mapGeneralConfig;
  }

  public ArrayList<String> getListUseCase() {
    return listUseCase;
  }

  public void setListUseCase(ArrayList<String> listUseCase) {
    this.listUseCase = listUseCase;
  }

  public ArrayList<File> getListTemplateFile() {
    return listTemplateFile;
  }

  public void setListTemplateFile(ArrayList<File> listTemplateFile) {
    this.listTemplateFile = listTemplateFile;
  }

  public ArrayList<String> getListFailedPassedImgA_replace() {
    return listFailedPassedImgA_replace;
  }

  public void setListFailedPassedImgA_replace(ArrayList<String> listFailedPassedImgA_replac) {
    this.listFailedPassedImgA_replace = listFailedPassedImgA_replac;
  }

  public ArrayList<String> getListFailedPassedImgAA_replace() {
    return listFailedPassedImgAA_replace;
  }

  public void setListFailedPassedImgAA_replace(ArrayList<String> listPotentialKnownImgAA_replac) {
    this.listFailedPassedImgAA_replace = listPotentialKnownImgAA_replac;
  }

  public ArrayList<String> getListFailedPassedImgAAA_replace() {
    return listFailedPassedImgAAA_replace;
  }

  public void setListFailedPassedImgAAA_replace(ArrayList<String> listPotentialKnownImgAAA_replac) {
    this.listFailedPassedImgAAA_replace = listPotentialKnownImgAAA_replac;
  }

  public ArrayList<String> getListFailedPassedImg508_replace() {
    return listFailedPassedImg508_replace;
  }

  public void setListFailedPassedImg508_replace(ArrayList<String> list) {
    this.listFailedPassedImg508_replace = list;
  }

  public ArrayList<String> getListPercentageImgA_replace() {
    return listPercentageImgA_replace;
  }

  public void setListPercentageImgA_replace(ArrayList<String> listPercentageImgA_replace) {
    this.listPercentageImgA_replace = listPercentageImgA_replace;
  }

  public ArrayList<String> getListPercentageImgAA_replace() {
    return listPercentageImgAA_replace;
  }

  public void setListPercentageImgAA_replace(ArrayList<String> listPercentageImgAA_replace) {
    this.listPercentageImgAA_replace = listPercentageImgAA_replace;
  }

  public ArrayList<String> getListPercentageImgAAA_replace() {
    return listPercentageImgAAA_replace;
  }

  public void setListPercentageImgAAA_replace(ArrayList<String> listPercentageImgAAA_replace) {
    this.listPercentageImgAAA_replace = listPercentageImgAAA_replace;
  }

  public ArrayList<String> getListPercentageImg508_replace() {
    return listPercentageImg508_replace;
  }

  public void setListPercentageImg508_replace(ArrayList<String> listPercentageImg508_replace) {
    this.listPercentageImg508_replace = listPercentageImg508_replace;
  }

  public ArrayList<String> getListPotentialKnownImgA_replace() {
    return listPotentialKnownImgA_replace;
  }

  public void setListPotentialKnownImgA_replace(ArrayList<String> listPotentialKnownImgA_replace) {
    this.listPotentialKnownImgA_replace = listPotentialKnownImgA_replace;
  }

  public ArrayList<String> getListPotentialKnownImgAA_replace() {
    return listPotentialKnownImgAA_replace;
  }

  public void setListPotentialKnownImgAA_replace(ArrayList<String> listPotentialKnownImgAA_replace) {
    this.listPotentialKnownImgAA_replace = listPotentialKnownImgAA_replace;
  }

  public ArrayList<String> getListPotentialKnownImgAAA_replace() {
    return listPotentialKnownImgAAA_replace;
  }

  public void setListPotentialKnownImgAAA_replace(ArrayList<String> listPotentialKnownImgAAA_replace) {
    this.listPotentialKnownImgAAA_replace = listPotentialKnownImgAAA_replace;
  }

  public ArrayList<String> getListPotentialKnownImg508_replace() {
    return listPotentialKnownImg508_replace;
  }

  public void setListPotentialKnownImg508_replace(ArrayList<String> listPotentialKnownImg508_replace) {
    this.listPotentialKnownImg508_replace = listPotentialKnownImg508_replace;
  }

  public ArrayList<String> getListSite() {
    return listSite;
  }

  public void setListSite(ArrayList<String> listSite) {
    this.listSite = listSite;
  }



}
