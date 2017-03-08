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
 * 		String match_chinese(Html, 1); //punc为1时抽取1中所有的中文和标点符号, punc为0时只抽取中文
 *		int zh_check(Html);  //判断text中是否包含中文
 *		int get_text_string_num(Html, '.'); //判断text中指定字符串match_string的个数
 *		int get_text_number_length(Html); //text中最长的数字串长度
 *		int get_text_red_keyword(Html);  //判断text中是否包含敏感词汇
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


public class text_extract {
	
	String[] red_keyword = new String[] {"account", "admin", "administrator", "pw",
			"auth", "bank", "client", "confirm", "email", "host", "login",
			"password", "pay", "private", "safe", "secure", "security", "itnues", "paipai", "qq",
			"sign", "user", "validation", "verification", "icbc", "10086", "apple", "icbc", "taobao"};
	
	public static String match_chinese(String text, int punc){
		//punc为1时抽取1中所有的中文和标点符号, punc为0时只抽取中文
		String Html_zh = new String();
		String re_zh = "[\u4e00-\u9fff，。、；！：（）“《》？”]+";// 正则匹配结果即为 gt，nbsp等
		if (punc == 0)
		{
			re_zh = "[\u4e00-\u9fff]+";
		}
		Pattern pattern = Pattern.compile(re_zh);
		Matcher sz =pattern.matcher(text);
		while(sz.find())
		{
			if(Html_zh.equals(" ") == false){
				Html_zh = Html_zh + sz.group(0).trim() + " ";
			}
		}
		return Html_zh;
	}
	
	public int  zh_check(String text){
		//判断text中是否包含中文
		String pattern = "[\\u4e00-\\u9fa5]+";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(text); // 获取 matcher 对象
		if (m.find( )) {
			return 1;
		} else {
			return 0;
		}
	}


	public int get_text_string_num(String text, char match_string){
		//判断text中指定字符串match_string的个数
		int dot_num = 0;
		for (int i = 0; i < text.length(); i++) {
			if(text.charAt(i) == match_string)
			{
				dot_num ++;
			}
		}
		return dot_num;
	}

	public int get_text_number_length(String text){
		//text中最长的数字串长度
		String pattern = "\\d+";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(text); // 获取 matcher 对象
		int max_length = 0;
		while(m.find()) {
			if(m.end() - m.start() > max_length){
				max_length = m.end() - m.start();
			}
		}
		return max_length;
	}

	public int  get_text_red_keyword(String text){
		//判断text中是否包含敏感词汇
		text = text.toLowerCase();
		for(String tmp:red_keyword)  
		{  
			if(text.indexOf(tmp)!=-1){
				return 1;
			}
		} 
		return 0;
	}
	
	public static void test_main(){
		text_extract st=new text_extract();
		String Html = encode_test.readCurrentFileToStringUtf8("/shujia/test/main.html");
		//try {
		//	Html = encode_test.gdk2utf8(Html);
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}
		String chinese = text_extract.match_chinese(Html, 1); //punc为1时抽取1中所有的中文和标点符号, punc为0时只抽取中文
		System.out.println("chinese8: " + chinese);
		
		int zh_check_result = st.zh_check(Html);  //判断text中是否包含中文
		System.out.println("zh_check_result:" + zh_check_result);
		
		int text_string_num = st.get_text_string_num(Html, '.'); //判断text中指定字符串match_string的个数
		System.out.println("text_string_num:" + text_string_num);
		
		int get_text_number_length = st.get_text_number_length(Html); //text中最长的数字串长度
		System.out.println("get_text_number_length:" + get_text_number_length);
		
		int text_check_result = st.get_text_red_keyword(Html);  //判断text中是否包含敏感词汇
		System.out.println("text_check_result:" + text_check_result);
	}

	public static void main(String args[]) {
		try
		{
			text_extract.test_main();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}// main

	
}

