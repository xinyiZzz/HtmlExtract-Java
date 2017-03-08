package shujia.test;

/**
 * @author XinYi/weict 609610350@qq.com
 * 
 * name: 对html中进行处理
 * 
 * method：
 *  String getFileEncode(File file)  // 根据文件对象获取文件的编码
 *	String getFileEncode(String filePath)  // 根据路径获取文件的编码
 *	String getUrlEncode(URL url)  // 判断url的编码类型
 *	String readToString(File file) //指定CLASSPATH文件的绝对路径 
 *	String readCurrentFileToStringUtf8(String fileName) //在当前目录下打开文件，并返回utf8格式的内容
 *	String readAbsFileToStringUtf8(String fileName) //以绝对路径打开文件，并返回utf8格式的内容
 *	String encodeStr(String text, String encode_name) //将任意字符串转为指定编码
 *	String gdk2utf8(String text) // gb2312转utf8
 *	boolean writeFileToString(FileOutputStream fos, String FileContent, String encode_name)  //将FileContent中字符串以encode_name的编码格式写入fos输出流中
 *	boolean writeAbsFileToString(String fileName, String FileContent)  //将FileContent中字符串以utf-8编码格式写入fileName文件中
 * 
 * describe:
	 * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
	 * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
	 * JChardetFacade、ASCIIDetector、UnicodeDetector。
	 * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
	 * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
	 * cpDetector是基于统计学原理的，不保证完全正确。
	 * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
	 * 指示是否显示探测过程的详细信息，为false不显示。
	 * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
	 * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
	 * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
	 * 用到antlr.jar、chardet.jar
	 * ASCIIDetector用于ASCII编码测定
	 * UnicodeDetector用于Unicode家族编码的测定
 */

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileOutputStream; 
import java.io.OutputStreamWriter; 
import java.io.PrintWriter;


public class encode_test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File f = new File("D:\\Workspaces\\MyEclipse\\test\\src\\test\\java\\shujia\\test\\main2.html");
		String encode1 = getFileEncode(f);
        System.out.println(encode1);// UTF-8
        
		try {
	          URL url = new URL("http://www.baidu.com");
	          String encode2 = getUrlEncode(url);
	          System.out.println(encode2);// UTF-8
	      } catch (MalformedURLException e) {
	          e.printStackTrace();
	      }
	}
	
	public static String getFileEncode(File file) {
		// 根据文件对象获取文件的编码
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));//如果不希望判断xml的encoding，而是要判断该xml文件的编码，则可以注释掉
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		Charset charset = null;
		try {
			charset = detector.detectCodepage(file.toURI().toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			//System.out.println(file.getName() + "编码是：" + charset.name());
			return charset.name();
		} else {
			//System.out.println(file.getName() + "未知");
			return null;
		}
	}
	
	public static String getFileEncode(String filePath) {
		// 根据路径获取文件的编码
		File file = new File(filePath);
		return encode_test.getFileEncode(file);
	}

	public static String getUrlEncode(URL url) {
		// 判断url的编码类型
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(new ByteOrderMarkDetector());
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		try {
			charset = detector.detectCodepage(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		}
		return null;
	}
	
	public static String readToString(File file) {  
		//指定CLASSPATH文件的绝对路径 
		InputStreamReader inputReader = null;
		BufferedReader bufferReader = null;
		//OutputStream outputStream = null;
		StringBuffer strBuffer = new StringBuffer();
		try
		{
			InputStream inputStream = new FileInputStream(file);
			inputReader = new InputStreamReader(inputStream, encode_test.getFileEncode(file));
			bufferReader = new BufferedReader(inputReader);
			// 读取一行
			String line = null;
			while ((line = bufferReader.readLine()) != null)
			{
				strBuffer.append(line);
			} 
		}
		catch (IOException e)
		{
			e.printStackTrace(); 
		}finally {
			if (inputReader != null) {
				try {
					inputReader.close();
				} catch (IOException e1) {
				}
			}
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e1) {
				}
			}
		}
		return strBuffer.toString();
	}
	
	public static String readCurrentFileToString(String fileName) {  
		//在当前目录下打开文件
		File file = new File(phishing_extract.class.getResource(fileName).getFile());  
		return encode_test.readToString(file);
	}
	
	public static String readAbsFileToString(String fileName) {  
		//以绝对路径打开文件
		File file = new File(fileName);
		return encode_test.readToString(file);
	}
	
	public static String readCurrentFileToStringUtf8(String fileName) {  
		//在当前目录下打开文件，并返回utf8格式的内容
		File file = new File(phishing_extract.class.getResource(fileName).getFile()); 
		return encode_test.encodeStr(encode_test.readToString(file), "UTF-8");
	}
	
	public static String readAbsFileToStringUtf8(String fileName) {  
		//以绝对路径打开文件，并返回utf8格式的内容
		File file = new File(fileName);
		return encode_test.encodeStr(encode_test.readToString(file), "UTF-8");
	}
	
	public static String encodeStr(String text, String encode_name){
		//将任意字符串转为指定编码
		try {
			text = new String(text.getBytes(), encode_name);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return text;
	}
	
	public static String gdk2utf8(String text) throws Exception {
		// gb2312转utf8
		String iso = new String(text.getBytes("UTF-8"),"ISO-8859-1"); 
		String utf8 = new String(iso.getBytes("ISO-8859-1"),"UTF-8");
		return utf8;
	}
	
	public static boolean writeFileToString(FileOutputStream fos, String FileContent, String encode_name) {  
		//将FileContent中字符串以encode_name的编码格式写入fos输出流中
	    try { 
	        OutputStreamWriter osw = new OutputStreamWriter(fos, encode_name); 
	        osw.write(FileContent); 
	        osw.flush(); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	        return false;
	    }finally {  
            if (fos!=null) {  
                try {  
                    fos.close();  
                } catch (IOException e) {  
                }  
            }  
        } 
	    return true;
	}
	
	public static void writeAbsFileToStringPW(String FileContent, String fileName, String encode_name) {  
		//将FileContent中字符串以encode_name的编码格式写入fileName文件中
		PrintWriter pw = null;  
        try {  
            pw = new PrintWriter(fileName, encode_name);  
            pw.write(FileContent);  
            pw.flush();  
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {  
            if (null != pw) {  
                pw.close();  
            }  
        }  
	}
	
	public static boolean writeAbsFileToString(String fileName, String FileContent) {  
		//将FileContent中字符串以utf-8编码格式写入fileName文件中
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} 
		return encode_test.writeFileToString(fos, FileContent, "UTF-8");
	}

}
