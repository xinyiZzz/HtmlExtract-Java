/**
 * 
 */
package shujia.test;

/**
 * @author XinYi 609610350@qq.com
 *  
 *  name: 对html中进行处理
 * 
 * 	method：
 * 		String get_title(Html); //获取网页Title
 *		String match_chinese(Html, 1); //punc为1时抽取1中所有的中文和标点符号, punc为0时只抽取中文
 *		String jsoup_get_text(Html);  //获取网页中所有正文
 *		String get_ICP(Html);  //获取网页ICP
 *		double get_js_long(Html); // 获取html中js标签中内容所占比例
 *		double get_style_long(Html);  // 获取html中style标签中内容所占比例
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
import shujia.test.text_extract;


public class html_extract {
	
	public String replace_InvalidTag(String Html){
		//替换HTML中的无效标签
		String pattern_cdata ="//<!\\[CDATA\\[[^>]*//\\]\\]>";// 匹配CDATA
		Pattern re_cdata = Pattern.compile(pattern_cdata, Pattern.CASE_INSENSITIVE);
		Matcher temp = re_cdata.matcher(Html);
		Html = temp.replaceAll("");
		String pattern_cdata2 ="<!\\[CDATA\\[[^>]*//\\]\\]>";// 匹配CDATA
		Pattern re_cdata2 = Pattern.compile(pattern_cdata2, Pattern.CASE_INSENSITIVE);
		temp = re_cdata2.matcher(Html);
		Html = temp.replaceAll("");
		String pattern_br="<br\\s*?/?>"; // 处理换行
		Pattern re_br = Pattern.compile(pattern_br);
		temp = re_br.matcher(Html);
		Html = temp.replaceAll("");
		String pattern_blank_link="\\s+"; // 去掉多余的空行
		Pattern re_blank_link = Pattern.compile(pattern_blank_link);
		temp = re_blank_link.matcher(Html);
		Html = temp.replaceAll("");
		String pattern_comment="<!--[^>]*-->"; // 去掉HTML注释
		Pattern re_comment = Pattern.compile(pattern_comment);
		temp = re_comment.matcher(Html);
		Html = temp.replaceAll("");
		String pattern_style="<style\\s*[^>]*>(.*?)</style\\s*>"; 
		Pattern re_style = Pattern.compile(pattern_style);
		temp = re_style.matcher(Html);
		Html = temp.replaceAll("");
		String pattern_script="<script\\s*[^>]*>(.*?)</script>"; 
		Pattern re_script = Pattern.compile(pattern_script);
		temp = re_script.matcher(Html);
		Html = temp.replaceAll("");
		String pattern_h="</?[^>]*>"; // 处理html标签
		Pattern re_h = Pattern.compile(pattern_h); 
		temp = re_h.matcher(Html);
		Html = temp.replaceAll("");
		return Html;
	}
	
	public String replace_CharEntity(String Html){
        // 替换常用HTML字符实体, 使用正常的字符替换HTML中特殊的字符实体
		Dictionary <String,String> char_entities = new Hashtable <String,String>();
		char_entities.put("nbsp", " ");
		char_entities.put("160", " ");
		char_entities.put("lt", "<");
		char_entities.put("60", "<");
		char_entities.put("gt", ">");
		char_entities.put("62", ">");
		char_entities.put("amp", "&");
		char_entities.put("38", "&");
		char_entities.put("quot", "\"");
		char_entities.put("34", "\"");
		String pattern ="(&(?<name>(#?\\w+)))";// 正则匹配结果即为 gt，nbsp等
		Pattern re_charEntity = Pattern.compile(pattern);
		Matcher sz =re_charEntity.matcher(Html);
		while(sz.find())
		{
			//if (sz.group("name").equals("CO")==false){// 可能会匹配到 &CO，这是HTTrack的特殊字符，无需替换
			if(char_entities.get(sz.group("name")) != null){
				Matcher temp = re_charEntity.matcher(Html);
				String replacement = char_entities.get(sz.group("name"));
				Html = temp.replaceAll(replacement);
			}
			//}
		}
		return Html;
	}
	
	public String html_filter(String Html){
		//对html过滤无效标签和字符实体
		Html = this.replace_InvalidTag(Html);
		Html = this.replace_CharEntity(Html);
		Html = Html.replace(" ", "");
		return Html;
	}

	
	public String get_title(String Html){
		//获取网页Title
		//String Html2 = "asdfasdf<title> 俏十岁面膜总代-诚招一二级代理商</title>asfsdfsdaf";
		String pattern = "<title>.*?</title>";
		//Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE
		//		| Pattern.MULTILINE);
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(Html); // 获取 matcher 对象
		if (m.find( )) {
			return new String(m.group(0)).substring(7, m.group(0).length()-8).trim();
		} else {
			return new String();
		}
	}

	public String get_ICP(String Html){
		//抽取网页ICP
		//Html = this.replace_CharEntity(Html);
		String pattern = "([\\u4e00-\\u9fa5]).?ICP([\\u4e00-\\u9fa5|&nbsp;]*)(\\d{0,12})";
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(Html); // 获取 matcher 对象
		while(m.find())
		{
				//System.out.println("456");
				String pattern_num = ".+?\\d.+?";
				Pattern p_num =Pattern.compile(pattern_num);
				Matcher isNum =p_num.matcher(m.group(3));
				if(isNum.find()){
					//System.out.println("233");
					return new String(m.group(1) + m.group(3));
				}
			
		}
		return new String();
	}

	public String jsoup_get_text(String Html){
		// 获取网页中所有正文(弃用，不专业）
		Document doc = Jsoup.parse(Html);//解析HTML字符串返回一个Document实现
		String text = doc.body().text(); // 取得字符串中的文本
		return text;
	}


	public double get_js_long(String Html)
	{
		// 获取html中js标签中内容所占比例
		double script_num = 0;
		double len_html = Html.length();
		Document doc = Jsoup.parse(Html);
		Elements contents = doc.select("script");
		for (Element data : contents)
		{
			script_num += (double) data.data().length();
		}
		BigDecimal b = new BigDecimal((double) script_num / (double) len_html);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public double get_style_long(String Html)
	// 获取html中style标签中内容所占比例
	{
		double style_num = 0;
		double len_html = Html.length();
		Document doc = Jsoup.parse(Html);
		Elements contents = doc.select("style");
		for (Element data : contents)
		{
			style_num += (double) data.data().length();
		}
		BigDecimal b = new BigDecimal((double) style_num / (double) len_html);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void test_main(){
		html_extract st=new html_extract();
		//String Html = encode_test.readAbsFileToStringUtf8("D:\\Workspaces\\MyEclipse\\test\\src\\test\\java\\shujia\\test\\main.html");
		String Html = encode_test.readCurrentFileToStringUtf8("/shujia/test/main.html");
		//try {
		//	Html = encode_test.gdk2utf8(Html);
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}
		String title = st.get_title(Html); //获取网页Title
		System.out.println("Title3: "+title);
		
		String chinese_text = text_extract.match_chinese(Html, 1); //punc为1时抽取1中所有的中文和标点符号, punc为0时只抽取中文
		System.out.println("chinese_text: "+chinese_text);
		
		String jsoup_get_text = st.jsoup_get_text(Html);  //获取网页中所有正文
		//String jsoup_get_text = text_extract.match_chinese(st.jsoup_get_text(Html));
		System.out.println("jsoup_get_text: "+jsoup_get_text);
		
		String ICP = st.get_ICP(Html);  //获取网页ICP
		System.out.println("ICP5: "+ICP);
		
		double get_js_long = st.get_js_long(Html); // 获取html中js标签中内容所占比例
		System.out.println("get_js_long41: " + get_js_long);
		double get_style_long = st.get_style_long(Html);  // 获取html中style标签中内容所占比例
		System.out.println("get_style_long42: " + get_style_long);
	}

	public static void main(String args[]) {
		try
		{
			html_extract.test_main();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}// main
}

