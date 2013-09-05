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
package org.exoplatform.tqa.accessibility.connector;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**Encrypt Service is used to encrypt and decrypt a string
 * this class is used in file configuration xml
 * Created by The eXo Platform SAS Author : eXoPlatform annb@exoplatform.com Dec
 * 8, 2011
 */
public class EncryptService {

  private static String key = "AcExoKey";

  public static String decrypt(String message) throws Exception {
    byte[] bytesrc = convertHexString(message);
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
    IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
    byte[] retByte = cipher.doFinal(bytesrc);
    return new String(retByte);
  }

  public static String encrypt(String message) throws Exception {
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
    IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
    return toHexString(cipher.doFinal(message.getBytes("UTF-8")));
  }

  private static byte[] convertHexString(String ss) {
    byte digest[] = new byte[ss.length() / 2];
    for (int i = 0; i < digest.length; i++) {
      String byteString = ss.substring(2 * i, 2 * i + 2);
      int byteValue = Integer.parseInt(byteString, 16);
      digest[i] = (byte) byteValue;
    }
    return digest;
  }

  private static String toHexString(byte b[]) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < b.length; i++) {
      String plainText = Integer.toHexString(0xff & b[i]);
      if (plainText.length() < 2)
        plainText = "0" + plainText;
      hexString.append(plainText);
    }
    return hexString.toString();
  }

}
