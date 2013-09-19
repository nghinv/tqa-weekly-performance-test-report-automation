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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.log4j.Logger;
import org.exoplatform.tqa.accessibility.connector.EncryptService;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          annb@exoplatform.com
 * Sep 20, 2012  
 */
public class SenderWebdav {
  final static Logger                       LOGGER = Logger.getLogger(SenderWebdav.class);

  static String rootFolder = "";
  String username;
  String password;
  static int spc_count=-1;
  
  public SenderWebdav(String username, String password) {
    super();
    this.username = username;
    this.password = password;
  }
  
  
  public static void sendTemlate(String webdavLogin, String webdavPass, 
		  						String webdavURL, String folderLocal, String nameParentFolder) {    
    rootFolder = nameParentFolder;

    HttpClient client = new HttpClient();
    try {
      webdavPass = EncryptService.decrypt(webdavPass);
      Credentials creds = new UsernamePasswordCredentials(webdavLogin, webdavPass);
      client.getState().setCredentials(AuthScope.ANY, creds);
      
      //delete parent folder
//      deleteFolder(client, webdavURL, rootFolder);
      
      //create parent folder
      createFolder(client, webdavURL, rootFolder); 
      createFile(client, webdavURL +"/" + nameParentFolder  , new File (folderLocal+"/"+"content"));
      
      SearchAllFiles(new File(folderLocal), client, webdavURL, nameParentFolder);
    } catch (Exception e) {
    	e.printStackTrace();
    	LOGGER.error(e.getMessage());
    }

  }
  
/**
 * Delete a WEBDAV folder
 * @param client 
 * @param pathREST
 * @param nameFolder
 * @throws HttpException
 * @throws IOException
 */
  static void createFolder(HttpClient client, String pathREST, String nameFolder) throws HttpException,
                                                                                 IOException {
   //do not create content folder
    if (!nameFolder.equals("content")) {
      String folderWEBDAV = pathREST + "/" + nameFolder + "/";
      MkColMethod folderCreate = new MkColMethod(folderWEBDAV);
      client.executeMethod(folderCreate);
//      LOGGER.info("CREATE folder: " + folderWEBDAV);
    }
  }

/**
 * Create a WEBDAV file by sending current file
 * @param client
 * @param pathRESTWithFolder
 * @param fileLocal
 * @throws HttpException
 * @throws IOException
 */
  static void createFile(HttpClient client, String pathRESTWithFolder, File fileLocal) throws HttpException, IOException{  
	  String fileWEBDAV = pathRESTWithFolder + "/" + fileLocal.getName();
	  
	  PutMethod method = new PutMethod(fileWEBDAV);
	  RequestEntity requestEntity = new InputStreamRequestEntity( new FileInputStream(fileLocal));
	  method.setRequestEntity(requestEntity);
	  client.executeMethod(method);
//	  LOGGER.info("CREATE file: " + fileWEBDAV);
  }
  
  
/**
 * Delete a WEBDAV folder  
 * @param client
 * @param pathREST
 * @param nameFolder
 * @throws HttpException
 * @throws IOException
 */
  static void deleteFolder(HttpClient client, String pathREST, String nameFolder) throws HttpException, IOException{
    String folderWEBDAV = pathREST +"/" + nameFolder + "/";
    
    //DELETE FOLDER
    DeleteMethod folderDelete = new DeleteMethod(folderWEBDAV);
    LOGGER.info("DELETE folder: "+folderWEBDAV);    
    client.executeMethod(folderDelete);  
  }
  

  static void SearchAllFiles(File aFile, HttpClient client, String pathREST, String nameParentFolder) throws IOException {
    spc_count++;
    String spcs = "";
    for (int i = 0; i < spc_count; i++)
      spcs += " ";
    if (aFile.isFile()) {
      String fileWithSlash = "" + aFile.getParentFile();
      String[] arrayFile = fileWithSlash.split("/");
      String folderParentName = arrayFile[arrayFile.length - 1];
      
      String pathRESTWithFolder  = pathREST + "/"+ rootFolder+ "/" + folderParentName;
      createFile(client, pathRESTWithFolder, new File(aFile.getPath()));

    } else if (aFile.isDirectory()) {
      LOGGER.info(spcs + "[DIR] " + aFile.getName());
      File[] listOfFiles = aFile.listFiles();
      if (listOfFiles != null) {
        for (int i = 0; i < listOfFiles.length; i++) {
          
          String pathRESTFolder = pathREST + "/"+ rootFolder;
          createFolder(client, pathRESTFolder, listOfFiles[i].getName());
          SearchAllFiles(listOfFiles[i], client, pathREST, nameParentFolder);
        }
      } else {
        LOGGER.info(spcs + " [ACCESS DENIED]");
      }
    }
    spc_count--;
  }

}
