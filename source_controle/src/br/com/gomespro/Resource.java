package br.com.gomespro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Resource {	
	@SuppressWarnings("resource")
	public static String get(String resource, String prefix){
		String path;
		
		path = Resource.class.getClassLoader().getResource(resource).getPath();
		
		File f = new File(path);
		
		if(!f.exists()){
			String jarfile = System.getProperty("java.class.path");
			
	        try {  
	            ZipFile zipFile = new ZipFile(jarfile);  
	            Enumeration<? extends ZipEntry> entries = zipFile.entries();  
	            while (entries.hasMoreElements()) {  
	                ZipEntry zipEntry = (ZipEntry) entries.nextElement();
	                
	                if (!zipEntry.isDirectory() && zipEntry.getName().contains(resource)) {    
	                	InputStream fileonjar = ClassLoader.getSystemResourceAsStream(zipEntry.getName());
	                	
	                	path = System.getProperty("java.io.tmpdir") + prefix + '_' + zipEntry.getName();
	                	
	                    OutputStream output = new FileOutputStream(path);
	                    byte[] buffer = new byte[256];
	                    int bytesRead = 0;
	                    while ((bytesRead = fileonjar.read(buffer)) != -1) {
	                        output.write(buffer, 0, bytesRead);
	                    }
	                }  
	            }
	        }catch(Exception e){
	            System.out.println("ERROR no Resource");
	        }
		}	
        
		return path;  
	}
}
