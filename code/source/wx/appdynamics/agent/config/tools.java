package wx.appdynamics.agent.config;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.ServerAPI;
import com.ibm.icu.text.SimpleDateFormat;
import com.wm.util.coder.JSONCoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
// --- <<IS-END-IMPORTS>> ---

public final class tools

{
	// ---( internal utility methods )---

	final static tools _instance = new tools();

	static tools _newInstance() { return new tools(); }

	static tools _cast(Object o) { return (tools)o; }

	// ---( server methods )---




	public static final void convertStringArrayToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertStringArrayToString)>> ---
		// @sigtype java 3.5
		// [i] object:1:required list
		// [i] field:0:optional separator
		// [o] field:0:required out
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Object[] list = IDataUtil.getObjectArray(pipelineCursor, "list");
		String separator = IDataUtil.getString(pipelineCursor, "separator");
		
		// process
		
		if (separator == null)
			separator = System.lineSeparator();
		
		String out = "";
		
		if (list != null) {
			
			for(Object o : list) {
				out += "\"" + o.toString() + "\"" + separator;
			}
			
			if (list.length > 0)
				out = out.substring(0, out.length()-1);
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "out", out);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void copy (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(copy)>> ---
		// @sigtype java 3.5
		// [i] field:0:required srcDir
		// [i] field:0:required srcName
		// [i] field:0:required tgtDir
		// [i] field:0:required tgtName
		// [i] field:0:optional overwrite {"true","false"}
		// [o] field:0:required tgtPath
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String srcDirectory = IDataUtil.getString( pipelineCursor, "srcDir");
		String src = IDataUtil.getString( pipelineCursor, "srcName");
		String tgtDirectory = IDataUtil.getString( pipelineCursor, "tgtDir");
		String tgt = IDataUtil.getString( pipelineCursor, "tgtName");
		String overwrite = IDataUtil.getString(pipelineCursor, "overwrite");
		
		pipelineCursor.destroy();
		
		// process
		
		try {
			
			if (tgt == null && tgtDirectory == null)
				throw new ServiceException("Must specify either tgtName or tgtDir");
			
			if (tgt == null)
				tgt = src;
			
			if (tgtDirectory == null)
				tgtDirectory = srcDirectory;
			
			if (!new File(tgtDirectory).exists());
				new File(tgtDirectory).mkdirs();
			
			if  (new File(srcDirectory, src).isDirectory()) {
				throw new ServiceException("source is a directory, not allowed!");
			} else if (overwrite != null && overwrite.equalsIgnoreCase("true")) {
				Files.copy(FileSystems.getDefault().getPath(srcDirectory, src), FileSystems.getDefault().getPath(tgtDirectory, tgt), StandardCopyOption.REPLACE_EXISTING);
			} else {
				try {
					Files.copy(FileSystems.getDefault().getPath(srcDirectory, src), FileSystems.getDefault().getPath(tgtDirectory, tgt));
				} catch (FileAlreadyExistsException e) {
					ServerAPI.logError(e);
				}
			}
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "tgtPath", new File(tgtDirectory, tgt).getPath());
		// --- <<IS-END>> ---

                
	}



	public static final void diff (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(diff)>> ---
		// @sigtype java 3.5
		// [i] object:0:required date1
		// [i] object:0:optional date2
		// [o] field:0:required diff
		// [o] field:0:required diffInSeconds
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Date date1 = (Date) IDataUtil.get(pipelineCursor, "date1");
		Date date2 = (Date) IDataUtil.get(pipelineCursor, "date2");
		
		// process
		
		long diff = 0;
		
		if (date1 != null)
		{
			if (date2 == null)
				date2 = new Date();
			
			diff = date2.getTime() - date1.getTime();
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "diff", "" + diff);
		IDataUtil.put(pipelineCursor, "diffInSeconds", "" + diff / 1000);
		
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void fileExists (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(fileExists)>> ---
		// @sigtype java 3.5
		// [i] field:0:required fname
		// [o] field:0:required exists {"true","false"}
		// [o] field:0:optional isDir
		// [o] field:0:optional found
		//filePath pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	fname = IDataUtil.getString(pipelineCursor, "fname");
		
		// process
		
		boolean exists = new File(fname).exists();
		boolean isDir = new File(fname).isDirectory();
		
		String childName = null;
		
		if (isDir) {
			String[] files = new File(fname).list();
			
			if (files.length > 0) {
				childName = files[files.length-1];
			} else {
				exists = false;
			}
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "exists", "" + exists);
		
		if (exists) {
			IDataUtil.put(pipelineCursor, "isDir", "" + isDir);
			
			if (childName != null) {
				IDataUtil.put(pipelineCursor, "found", new File(new File(fname), childName).getPath());
			}
		}
		
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void isWindows (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(isWindows)>> ---
		// @sigtype java 3.5
		// [o] field:0:required os
		// [o] object:0:required isWindows
		String os = System.getProperty("os.name").toLowerCase();
		
		IDataCursor c = pipeline.getCursor();
		IDataUtil.put(c, "os", os);
		IDataUtil.put(c, "isWindows", os.contains("win"));
		c.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void last (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(last)>> ---
		// @sigtype java 3.5
		// [i] field:0:required path
		// [o] field:0:required filename
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String path = IDataUtil.getString(pipelineCursor, "path");
		
		// process
		
		String filename = new File(path).getName();
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "filename", filename);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void readFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(readFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required fname
		// [i] field:0:required loadAs {"bytes","string","stream"}
		// [i] field:0:optional ignoreError {"false","true"}
		// [o] field:0:required name
		// [o] object:0:optional bytes
		// [o] field:0:optional string
		// [o] object:0:optional stream
		IDataCursor c = pipeline.getCursor();
		String fname = IDataUtil.getString(c, "fname");
		String loadAs = IDataUtil.getString(c, "loadAs");
		String ignoreError = IDataUtil.getString(c, "ignoreError");
		
		if (fname == null)
			throw new ServiceException("provide file name please");
		
		// process
		
		byte[] data = null;
		InputStream in = null;
		
		try {
			if (loadAs != null && loadAs.equalsIgnoreCase("stream")) {
				in = new FileInputStream(new File(fname));
			} else {
				data = Files.readAllBytes(Paths.get(fname));
			}
		} catch (NoSuchFileException e ) {
			if (ignoreError == null || !ignoreError.equalsIgnoreCase("true"))
				throw new ServiceException(e);
		} catch (IOException e) {
			
			throw new ServiceException(e);
		}
		
		// pipeline out
		
		IDataUtil.put(c, "name", new File(fname).getName());
		
		if (data != null) {
			
			if (loadAs != null && loadAs.equalsIgnoreCase("string"))
				IDataUtil.put(c, "string", new String(data));
			else
				IDataUtil.put(c, "bytes", data);
			c.destroy();
		} else {
			IDataUtil.put(c, "stream", in);
		}
		// --- <<IS-END>> ---

                
	}



	public static final void readFileWithFilter (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(readFileWithFilter)>> ---
		// @sigtype java 3.5
		// [i] field:0:required fname
		// [i] field:1:optional filters
		// [i] field:0:required maxLines
		// [o] field:0:required matchedFile
		// [o] field:0:required matchedFileName
		// [o] field:1:required matchedLines
		// [o] field:0:required matchedCount
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String fname = IDataUtil.getString(pipelineCursor, "fname");
		String[] filters = IDataUtil.getStringArray(pipelineCursor, "filters");
		String maxLines = IDataUtil.getString(pipelineCursor, "maxLines");
		
		// process
		
		int max = -1;
				
		try {max = Integer.parseInt(maxLines);} catch (Exception e) {}
		
		if (fname.contains("*")) {
			// need to find file first
			File fileNameWithWithCard = new File(fname);
			File[] files = null;
			
			final String match = new File(fname).getName();
			
			if (fileNameWithWithCard.isDirectory()) {
				files = fileNameWithWithCard.listFiles();
			} else {
				files = fileNameWithWithCard.getParentFile().listFiles(new FileFilter() {
					
					@Override
					public boolean accept(File pathname) {						
						return (pathname.getName().matches(match));
					}
				});
			}
			
			if (files != null && files.length > 0) {
				fname = files[0].getAbsolutePath();
			}
			
		}
		
		String[] matchedLines = readFileAsStringList(fname, filters, max);
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "matchedFile", fname);
		IDataUtil.put(pipelineCursor, "matchedFileName", new File(fname).getName());
		IDataUtil.put(pipelineCursor, "matchedLines", matchedLines);
		IDataUtil.put(pipelineCursor, "matchedCount", "" + matchedLines.length);
		pipelineCursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void replaceAllInString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(replaceAllInString)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inString
		// [i] field:0:required pattern
		// [i] field:0:required with
		// [o] field:0:required outString
		// pipeline out
		IDataCursor pipelineCursor = pipeline.getCursor();
		String inString = IDataUtil.getString(pipelineCursor, "inString");
		String pattern = IDataUtil.getString(pipelineCursor, "pattern");
		String with = IDataUtil.getString(pipelineCursor, "with");
		
		// process
		
		String outString = null;
		
		if (inString != null) {
			
			if (with == null)
				with = System.lineSeparator();
			
			outString = inString.replaceAll(pattern, with);
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "outString", outString);
		pipelineCursor.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void stringToDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stringToDate)>> ---
		// @sigtype java 3.5
		// [i] field:0:required string
		// [i] field:0:required pattern
		// [o] object:0:required date
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String string = IDataUtil.getString(pipelineCursor, "string");
		String pattern = IDataUtil.getString(pipelineCursor, "pattern");
		
		// process
		
		if (pattern == null)
			throw new ServiceException("Please provide a valid date pattern");
		
		Date date = null;
		
		if (string == null)
		{
			date = new Date();
		}
		else
		{
			try {
				date = new SimpleDateFormat(pattern).parse(string);
			} catch (ParseException e) {
				throw new ServiceException(e);
			}
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "date", date);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void writeFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(writeFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required fileName
		// [i] record:0:required data
		// [i] - field:0:optional string
		// [i] - object:0:optional bytes
		// [i] - object:0:optional stream
		// [i] object:0:optional append
		// [i] object:0:optional appendNewLine
		// [o] field:0:required fileNameOnly
		// pipeline in
		
				IDataCursor pipelineCursor = pipeline.getCursor();
				String fileName = IDataUtil.getString(pipelineCursor, "fileName");
				IData data = IDataUtil.getIData( pipelineCursor, "data");
				IDataCursor dataCursor = data.getCursor();
				String	string = IDataUtil.getString( dataCursor, "string");
				byte[] bytes = (byte[]) IDataUtil.get(dataCursor, "bytes");
				InputStream in = (InputStream) IDataUtil.get(dataCursor, "stream");
				dataCursor.destroy();
				
				boolean append = false;
				boolean appendNewLine = false;
				
				try {
				append = (boolean) IDataUtil.get(pipelineCursor, "append");
				} catch(Exception e) {
					// ignore
				}
				try {
					appendNewLine = (boolean) IDataUtil.get(pipelineCursor, "appendNewLine");
					} catch(Exception e) {
						// ignore
					}
				// process
								
				try
				{		
					if (!new File(fileName).getParentFile().exists());
						new File(fileName).getParentFile().mkdirs();
					
					if (bytes != null)
					{
						in = new ByteArrayInputStream(bytes);
					}
					else if (string != null)
					{
						in = new ByteArrayInputStream(string.getBytes());
					} 
					
					File targetFile = new File(fileName);
				    OutputStream outStream = new FileOutputStream(targetFile, append);
				 
				    byte[] buffer = new byte[8 * 1024];
				    int bytesRead;		       
				    
				    while ((bytesRead = in.read(buffer)) != -1) {
				    	
				    	outStream.write(buffer, 0, bytesRead);
				    }
				    
				    if (appendNewLine) {
				    	outStream.write(System.lineSeparator().getBytes());
				    }
				    in.close();
				    outStream.close();
				    
				} catch(IOException e) {
					throw new ServiceException(e);
				}
				
				
				// pipeline out
				
				IDataUtil.put(pipelineCursor, "fileNameOnly", new File(fileName).getName());
				pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
		
	public static String findPath(String basePath, String file) {
	    
		File[] files = new File(basePath).listFiles();
		String foundPath;
	
		for (int i = 0; i < files.length; i++) {
						
			if (file.equals(files[i].getName())) {
				return files[i].getAbsolutePath();
			} else if (files[i].isDirectory()) {
				foundPath = findPath(files[i].getAbsolutePath(), file);
				
				if (foundPath != null) {
					return foundPath;
				}
			}
		}
		
		return null;
	}
	
	private static String[] readFileAsStringList(String fname, String[] filters, int maxLines) throws ServiceException {
		
		InputStream is = new ByteArrayInputStream(readFile(fname));
		
		List<String> lines = new ArrayList<String>();
		String line = null;
		
		try (BufferedReader rdr = new BufferedReader(new InputStreamReader(is));) {
			while ((line=rdr.readLine()) != null) {
				if (filters == null || match(line, filters))
				lines.add(line);
				
				if (maxLines > 0 && lines.size() > maxLines) {
					lines.remove(0);
				}
			}
		} catch (IOException e) {
			throw new ServiceException(e);
		}
			
		return lines.toArray(new String[lines.size()]);
	}
	
	private static boolean match(String line, String[] filters) {
		
		boolean match = false;
		
		for (String f : filters) {
			if (line.contains(f)) {
				match = true;
				break;
			}
		}
		
		return match;
	}
	
	private static byte[] readFile(String fname) throws ServiceException {
		
		if (fname == null)
			throw new ServiceException("provide file name please");
		
		// process
		
		byte[] data = null;
		
		try {
			data = Files.readAllBytes(Paths.get(fname));
		} catch(NoSuchFileException e) {
			
			throw new ServiceException(e);
		} catch (IOException e) {
			
			throw new ServiceException(e);
		}
				
		return data;
	}
		
	// --- <<IS-END-SHARED>> ---
}

