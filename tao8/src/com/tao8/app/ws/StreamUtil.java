package com.tao8.app.ws;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
   public static byte[] load(InputStream in ) throws IOException{
	   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   byte b [] = new byte[1024];
	   int len = -1;
	   while((len=in.read(b))!=-1){
		   baos.write(b,0,len);
	   }
	   baos.close();
	   return baos.toByteArray();
   }
}
