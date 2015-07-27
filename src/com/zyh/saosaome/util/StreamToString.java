package com.zyh.saosaome.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamToString {

	/**
	 * 将输入流转化成字符串
	 * @param inputStream 输入流
	 * @param paramString 字符编码
	 * @return 字符串
	 */
	
	  public static String InputStreamToString(InputStream inputStream, String paramString)
	  {
	    try
	    {
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      byte[] arrayOfByte = new byte[1024];
	      while (true)
	      {
	        int i = inputStream.read(arrayOfByte);
	        if (i == -1)
	        {
	          inputStream.close();
	          return new String(baos.toByteArray(), paramString);
	        }
	        baos.write(arrayOfByte, 0, i);
	      }
	    }
	    catch (IOException localIOException)
	    {
	      localIOException.printStackTrace();
	    }
	    return null;
	  }

}
