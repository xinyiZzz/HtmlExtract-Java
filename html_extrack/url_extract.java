/**
 * 
 */
package shujia.test;

/**
 * @author XinYi 609610350@qq.com
 * 
 * name: 对html中进行处理
 * 
 * 	method：
 * 		String get_url_domain(url); //抽取url的域名
 *		String get_host_domain(url);  //获取主机域名
 *		String Foramt_url(url);  // 规范化url
 *		int get_url_at(url);  //判断URL中是否含有@，？，-,_等特殊符号
 *		int get_url_ip(url);  //判断URL中是否包含IP
 *		String get_url_path(url); //抽取url的路径
 *		int get_url_port(url);  //抽取url的端口
 *		String get_url_query(url);  //获取url参数
 * 
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

import java.util.Arrays;
import java.util.Collection;
import java.text.DecimalFormat;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.math.BigDecimal;  

import shujia.test.encode_test;
//import shujia.test.phishing_extract_xpath;


public class url_extract {
	
	public int  get_url_at(String url){
		//判断URL中是否含有@，？，-,_等特殊符号
		url = this.Foramt_url(url);
		url = url.toLowerCase();
		try {
			URL url_format = new URL(url);
			String url_new = url_format.getHost() + url_format.getPath();
			String pattern = "@|-|_|\\?|~";
			Pattern p = Pattern.compile(pattern);
			//System.out.println("url_new"+url_new);
			Matcher m = p.matcher(url_new); // 获取 matcher 对象
			if (m.find( )) {
				return 1;
			} else {
				return 0;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String get_host_domain(String url){
		//获取主机域名
		//url= "http://www.jfox-com.wfs.twefd.com.cn/java-huo-qu-url-zhong-de-ding-ji-yu-ming-bu-bao-kuo-zi-yu-ming-de-yuan-dai-ma";
		String domain = this.get_url_domain(url);
		try{
			//Pattern pattern = Pattern.compile("[^\\.]+(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk|\\.公司|\\.中国|\\.网络)");
			Pattern pattern = Pattern.compile("([^\\.]+)(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk|\\.公司|\\.中国|\\.网络)");
			Matcher matcher = pattern.matcher(domain);
			if (matcher.find( )) {
				String top_domain = matcher.group(2);
				domain = domain.substring(0, domain.indexOf(top_domain));
			 }
		} catch (Exception e) {
		}
		return domain;
	}
	
	public String Foramt_url(String url){
		// 规范化url
		if (url.startsWith("http://"))
		{
			url = url.substring(7);
		}
		if (url.startsWith("https://"))
		{
			url = url.substring(8);
		}
		url = "http://" + url;
		return url;
	}

	public String  get_url_path(String url){
		//抽取url的路径
		url = this.Foramt_url(url);
		url = url.toLowerCase();
		try {
			URL url_format = new URL(url);
			String url_path = url_format.getPath();
			return url_path;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String  get_url_query(String url){
		//获取url的参数
		url = this.Foramt_url(url);
		url = url.toLowerCase();
		try {
			URL url_format = new URL(url);
			String url_query = url_format.getQuery();
			return url_query;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public int  get_url_port(String url){
		//获取url的端口
		url = this.Foramt_url(url);
		url = url.toLowerCase();
		try {
			URL url_format = new URL(url);
			int url_Port = url_format.getPort();
			//System.out.println("url_Port"+url_Port);
			return url_Port;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String get_url_domain(String url){
		//抽取url的域名
		URL url_format;
		url = this.Foramt_url(url);
		url = url.toLowerCase();
		try {
			if(url.startsWith("http://http") || url.startsWith("http://https")){
				url = url.substring(7);
			}
			if(url.startsWith("http:///http") || url.startsWith("http:///https")){
				url = url.substring(8);
			}
			if(url.startsWith("http://://")){
				url = url.substring(0,7) + url.substring(10);
			}
			if(url.startsWith("http:///")){
				url = url.substring(0,7) + url.substring(8);
			}
			if(url.startsWith("http:/") && !url.startsWith("http://") && url.length() > 6){
				url = url.substring(0,6) + "/" + url.substring(7);
			}
			url_format = new URL(url);
			String domain = url_format.getHost();
//			String[] domain_split = url_format.getHost().split("\\.");
//			if(domain_split.length == 0 || domain_split.length == 1){
//				//System.out.println("get_url_domain error:" + url+ Arrays.toString(domain_split));
//				return new String(); 
//			}
//			String domain;
//			if(this.get_url_ip(url) == 0){
//				domain = domain_split[domain_split.length - 2] + "." + domain_split[domain_split.length - 1];
//			}
//			else if(domain_split.length >= 4){
//				domain = domain_split[domain_split.length - 4] + "." +
//						domain_split[domain_split.length - 3] + "."+
//						domain_split[domain_split.length - 2] + "."+
//						domain_split[domain_split.length - 1];
//			}
//			else{
//				domain = "";
//			}
			return domain;
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			return new String();
		}
	}
	
	public int get_url_ip(String url){
		//判断URL中是否包含IP
		URL url_format;
		url = this.Foramt_url(url);
		url = url.toLowerCase();
		try {
			url_format = new URL(url);
			String domain = url_format.getHost();
			if(domain.split("\\.").length == 4){
				String pattern = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(domain); // 获取 matcher 对象
				if (m.find( )) {
					return 1;
				} else {
					return 0;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void test_main(){
		url_extract st=new url_extract();
		String url = "http://www.tcbd@4654513m.bank.com/789454bank6589789/4564/12347";
		//String url = "http://www.sina.com.cn";
		//String url = "http:///boral.wang";
		//String url = "http:///http://c672146432.wezhan.cn/plugins/designer/index#page-2673";
		//String url = "http://http://www.fangkai-888.cn/";
		
		String domain = st.get_url_domain(url); //抽取url的域名
		System.out.println("domain9:" + domain);
		
		String get_host_domain = st.get_host_domain(url);  //获取主机域名
		System.out.println("get_host_domain: "+get_host_domain);
		
		String Foramt_url = st.Foramt_url(url);  // 规范化url
		System.out.println("Foramt_url: "+Foramt_url);
		
		int url_at = st.get_url_at(url);  //判断URL中是否含有@，？，-,_等特殊符号
		System.out.println("url_at10: "+url_at);

		int url_ip = st.get_url_ip(url);  //判断URL中是否包含IP
		System.out.println("url_ip40: "+url_ip);

		String get_url_path = st.get_url_path(url); //抽取url的路径
		System.out.println("get_url_path: "+get_url_path);
		
		int get_url_port = st.get_url_port(url);  //抽取url的端口
		System.out.println("get_url_port: "+get_url_port);
		
		String get_url_query = st.get_url_query(url);  //获取url参数
		System.out.println("get_url_query: "+get_url_query);

	}

	public static void main(String args[]) {
		try
		{
			url_extract.test_main();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}// main
	
}

