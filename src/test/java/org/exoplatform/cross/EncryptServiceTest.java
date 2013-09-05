/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
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
package org.exoplatform.cross;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.exoplatform.cross.accessibility.connector.EncryptService;
import org.junit.Test;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          annb@exoplatform.com
 * Dec 8, 2011  
 */
public class EncryptServiceTest {
  static Logger logger = Logger.getLogger("org.exoplatform.cross.EncryptServiceTest");

 @Test
  public void testEncryptService() throws Exception{
   logger.info("Test Encrypt Service");
    String encryptDefaut = "This is a string not encrypt !";    
    String encryptExpectd = EncryptService.decrypt(EncryptService.encrypt(encryptDefaut));  
    Assert.assertEquals(encryptDefaut, encryptExpectd);  
  }
 
 @Test
   public void testEncryptServiceWithString() throws Exception{
   String encryptExpectd = "a8eeb45f630b8470f235b8f78cddb4f583f470205d9a7b6a";
   String encryptDefaut = "String Test @#$%^&*()!";  
   logger.info("Test Encrypt Service With String: "+ encryptDefaut);
   Assert.assertEquals(encryptExpectd, EncryptService.encrypt(encryptDefaut));
  

 }


}
