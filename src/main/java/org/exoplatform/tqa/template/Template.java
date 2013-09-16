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

import java.io.IOException;

import org.apache.log4j.Logger;

public class Template {
  static Logger           logger = Logger.getLogger("org.exoplatform.cross.accessibility.report.Template");
  TemplateReport reportInfo;
    
  public Template(){	  
    reportInfo = new TemplateReport();
    reportInfo.generateReport();
    
    //send template through WEBDAV
    //reportInfo.useSenderWebdav();  
  }
  
  
  /**
   * @param args
 * @throws IOException 
   */
  public static void main(String[] args){
	    try {
	    	new Template();
	    	logger.info("report done !");
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	logger.error(e.getMessage());
	    }
      System.exit(0);
    }
}
