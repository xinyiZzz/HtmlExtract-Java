/**
 * 
 */
package shujia.test;

/**
 * @author XinYi 609610350@qq.com
 * 	
 * 	name: 对html中各种标签中链接进行处理
 * 
 * 	method：
 *  		int[] get_link_number(String url, String[] link_content_list) // 获得页面中链接（URL）总数、内链个数、外链个数、空链个数，返回一个字典
 * 		double[] get_link_numbaer_rate(int[] link_numbers) // 计算网页内外链比例
 *		int[] get_a_link_number(String url, String Html) // 获取网页所有a标签中的链接，并分四类返回
 *		double[] get_a_link_rate(String url, String Html) // 获取网页所有a标签中的链接的个数比例，并分四类返回
 *		int[] get_css_link_number(String url, String Html) // 获取网页所有css标签中的链接，并分四类返回
 *		double[] get_css_link_rate(String url, String Html) // 获取网页所有css标签中的链接的个数比例，并分四类返回
 *		int[] get_js_link_number(String url, String Html) // 获取网页所有js标签中的链接，并分四类返回
 *		double[] get_js_link_rate(String url, String Html) // 获取网页所有js标签中的链接的个数比例，并分四类返回
 *		int[] get_pic_link_number(String url, String Html) // 获取网页所有pic标签中的链接，并分四类返回
 *		double[] get_pic_link_rate(String url, String Html) // 获取网页所有pic标签中的链接的个数比例，并分四类返回
 *		int[] get_html_link_number(String url, String Html) // 获取网页所有iframe标签中html的链接，并分四类返回
 *		double[] get_html_link_rate(String url, String Html) // 获取网页所有iframe标签中html的链接的个数比例，并分四类返回
 *		double[] merge_link_rate(String url, String Html) //将get_a_link_number、get_css_link_number、get_pic_link_number、get_html_link_number的返回结果合并输出
 *		int get_form_action_feature(String url,String Html)  // 提取form的Action 特征：post方法的action特征，post是将用户输入的值提交到指定服务器，判断提交服务器的域名是否与当前页面域名一致
 *		public double get_form_input_feature(String Html) //提取form的Input特征：提取input标签中 name id placeholder的值，判断其是否为敏感词汇，如 email password等
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


public class html_link_extract {
	
	String[] red_keyword = new String[] {"account", "admin", "administrator", "pw",
			"auth", "bank", "client", "confirm", "email", "host", "login",
			"password", "pay", "private", "safe", "secure", "security", "itnues", "paipai", "qq",
			"sign", "user", "validation", "verification", "icbc", "10086", "apple", "icbc", "taobao"};

	public int[] get_link_number(String url, String[] link_content_list){
		// 获得页面中链接（URL）总数、内链个数、外链个数、空链个数，返回一个字典
		// link_content_list：网页中所有链接（URL）的列表
		// 格式：{'linkNum':10,'nonelinkNum':3,'insidelinkNum':2,'outsidelinkNum':5}
		int linkNum = 0;  // 总链接数量
		int nonelinkNum = 0;  // 空链接数量
		int outsidelinkNum = 0; // 外链数量
		int insidelinkNum = 0;  // 内链数量
		String[] none_link_list = new String [] {null, "", "/", "#", " ", "http://#", "http://"}; //空串集合
		String web_domain = this.get_url_domain(url);
		for (String link_content : link_content_list) {
			linkNum ++;
			// 如果获得的链接为NONE或空字符串，则认定为空链接
			if(Arrays.asList(none_link_list).contains(link_content)){
				nonelinkNum ++;
			}
			// 如果链接内容符合以下情况，则认为是内链：
	        // 1 以#开头，且长度大于1
	        // 2 以'/'开头的为相对路径
	        // 3 以'javascript'开头
			else if((link_content.startsWith("#") && link_content.length() > 1) || 
					link_content.startsWith("/") || link_content.startsWith("javascript")
					 || link_content.startsWith("http://.")|| link_content.startsWith(".../")
					 || link_content.startsWith("./")|| link_content.startsWith("../")){
				insidelinkNum ++;
			}
			else{
				// 获取该链接的域名部分
				String link_domain = this.get_url_domain(link_content);
				if(!link_domain.equals("")){
					// 如果该链接的域名与本域域名不匹配，则认为该链接是外链
					Pattern p = Pattern.compile(web_domain, Pattern.CASE_INSENSITIVE);
					Matcher result = p.matcher(link_domain); // 获取 matcher 对象
					if (result.matches()) { //说明匹配
						insidelinkNum ++;
					} else {
						outsidelinkNum ++;
					}
				}
				else{ // 无法获取域名，例如url为'http://'
					nonelinkNum ++;
				}
			}

		}
		return new int[] {linkNum, nonelinkNum, outsidelinkNum, insidelinkNum};
	}

	public double[] get_link_numbaer_rate(int[] link_numbers){
		// 计算网页内外链比例
	    // linkNumDic：网页链接（URL）总数、内链个数、外链个数、空链个数的字典
	    // 返回四种链接类型的比例列表
		if(link_numbers[0] == 0){
			return new double[] {0, 0, 0};
		}
		else{
			int linkNum = link_numbers[0];
			DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
			double none_link_percent = Double.parseDouble(df.format((float)link_numbers[1]/linkNum));
			double inside_link_percent = Double.parseDouble(df.format((float)link_numbers[2]/linkNum)); 
			double outside_link_percent = Double.parseDouble(df.format((float)link_numbers[3]/linkNum));
			return new double[] {none_link_percent, inside_link_percent, outside_link_percent};
		}
	}

	public int[] get_a_link_number(String url, String Html){ 
		// 获取网页所有a标签中的链接，并分四类返回
		Document doc = Jsoup.parse(Html);
		Elements links = doc.select("a[href]"); //带有href属性的a元素, href 属性指向了一个外部链接的位置
		String[] link_content_list = new String[links.size()];
		int k = 0;
		for (Element link : links) {
			link_content_list[k] = link.attr("href");
			k ++;
		}
		return this.get_link_number(url, link_content_list);
	}

	public double[] get_a_link_rate(String url, String Html){ 
		// 获取网页所有a标签中的链接的个数比例，并分四类返回
		int[] link_numbers = this.get_a_link_number(url, Html);
		double[] link_rate = this.get_link_numbaer_rate(link_numbers);
		return link_rate;
	}
	
	public int[] get_css_link_number(String url, String Html){ 
		// 获取网页所有css标签中的链接，并分四类返回
		Document doc = Jsoup.parse(Html);
		Elements links = doc.select("link[rel=\"stylesheet\"][href]"); //带有href属性的a元素, href 属性指向了一个外部链接的位置
		String[] link_content_list = new String[links.size()];
		int k = 0;
		for (Element link : links) {
			link_content_list[k] = link.attr("href");
			k ++;
		}
		//System.out.println("1111111" + Arrays.toString(link_content_list)+link_content_list.length);
		return this.get_link_number(url, link_content_list);
	}
	
	public double[] get_css_link_rate(String url, String Html){ 
		// 获取网页所有css标签中的链接的个数比例，并分四类返回
		int[] link_numbers = this.get_css_link_number(url, Html);
		double[] link_rate = this.get_link_numbaer_rate(link_numbers);
		return link_rate;
	}
	
	public int[] get_js_link_number(String url, String Html){ 
		// 获取网页所有js标签中的链接，并分四类返回
		Document doc = Jsoup.parse(Html);
		Elements links = doc.select("script[src]"); //带有href属性的a元素, href 属性指向了一个外部链接的位置
		String[] link_content_list = new String[links.size()];
		int k = 0;
		for (Element link : links) {
			link_content_list[k] = link.attr("src");
			k ++;
		}
		//System.out.println("22222222" + Arrays.toString(link_content_list)+link_content_list.length);
		return this.get_link_number(url, link_content_list);
	}
	
	public double[] get_js_link_rate(String url, String Html){ 
		// 获取网页所有js标签中的链接的个数比例，并分四类返回
		int[] link_numbers = this.get_js_link_number(url, Html);
		double[] link_rate = this.get_link_numbaer_rate(link_numbers);
		return link_rate;
	}
	
	public int[] get_pic_link_number(String url, String Html){ 
		// 获取网页所有pic标签中的链接，并分四类返回
		Document doc = Jsoup.parse(Html);
		Elements links1 = doc.select("link[rel=$\"icon\"][href]");
		Elements links2 = doc.select("link[rel=$\"Icon\"][href]");
		Elements links3 = doc.select("img[original]");
		Elements links4 = doc.select("img[src]");
		Elements links5 = doc.select("div[src]");
		Elements links6 = doc.select("input[src]");
		Elements links7 = doc.select("[background]");
	    String pattern = "url\\((.*?)\\)";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(Html); // 获取 matcher 对象
		int url_num = 0;
		while(m.find()) {
			url_num++;
	      }
		String[] link_content_list = new String[links1.size()+links2.size()+links3.size()+
		                                        links4.size()+links5.size()+links6.size()+
		                                        links7.size()+url_num];
		int k = 0;
		for (Element link : links1) {
			link_content_list[k] = link.attr("href");
			k ++;
		}
		for (Element link : links2) {
			link_content_list[k] = link.attr("href");
			k ++;
		}
		for (Element link : links3) {
			link_content_list[k] = link.attr("original");
			k ++;
		}
		for (Element link : links4) {
			link_content_list[k] = link.attr("src");
			k ++;
		}
		for (Element link : links5) {
			link_content_list[k] = link.attr("src");
			k ++;
		}
		for (Element link : links6) {
			link_content_list[k] = link.attr("src");
			k ++;
		}
		for (Element link : links7) {
			link_content_list[k] = link.attr("background");
			k ++;
		}
		m = p.matcher(Html); // 获取 matcher 对象
		while(m.find()) {
			link_content_list[k] = m.group(0);
	        k ++;
	      }
		//System.out.println("333333333" + Arrays.toString(link_content_list)+link_content_list.length);
		return this.get_link_number(url, link_content_list);
	}
	
	public double[] get_pic_link_rate(String url, String Html){ 
		// 获取网页所有pic标签中的链接的个数比例，并分四类返回
		int[] link_numbers = this.get_pic_link_number(url, Html);
		double[] link_rate = this.get_link_numbaer_rate(link_numbers);
		return link_rate;
	}
	
	public int[] get_html_link_number(String url, String Html){ 
		// 获取网页所有iframe标签中html的链接，并分四类返回
		Document doc = Jsoup.parse(Html);
		Elements links1 = doc.select("iframe[src]");
		Elements links2 = doc.select("frame[src]");
		String[] link_content_list = new String[links1.size()+links2.size()];
		int k = 0;
		for (Element link : links1) {
			link_content_list[k] = link.attr("src");
			k ++;
		}
		for (Element link : links2) {
			link_content_list[k] = link.attr("src");
			k ++;
		}
		//System.out.println("444444444" + Arrays.toString(link_content_list)+link_content_list.length);
		return this.get_link_number(url, link_content_list);
	}
	
	public double[] get_html_link_rate(String url, String Html){ 
		// 获取网页所有iframe标签中html的链接的个数比例，并分四类返回
		int[] link_numbers = this.get_html_link_number(url, Html);
		double[] link_rate = this.get_link_numbaer_rate(link_numbers);
		return link_rate;
	}
	
	public double[] merge_link_rate(String url, String Html){ 
		//将get_a_link_number、get_css_link_number、get_pic_link_number、get_html_link_number的返回结果合并输出
		int[] a_link_numbers = this.get_a_link_number(url, Html);
		int[] css_link_numbers = this.get_css_link_number(url, Html);
		int[] js_link_numbers = this.get_js_link_number(url, Html);
		int[] pic_link_numbers = this.get_pic_link_number(url, Html);
		int[] html_link_numbers = this.get_html_link_number(url, Html);
		int[] all_link_numbers = new int[] {
				a_link_numbers[0] + css_link_numbers[0] + js_link_numbers[0] + pic_link_numbers[0] + html_link_numbers[0],
				a_link_numbers[1] + css_link_numbers[1] + js_link_numbers[1] + pic_link_numbers[1] + html_link_numbers[1],
				a_link_numbers[2] + css_link_numbers[2] + js_link_numbers[2] + pic_link_numbers[2] + html_link_numbers[2],
				a_link_numbers[3] + css_link_numbers[3] + js_link_numbers[3] + pic_link_numbers[3] + html_link_numbers[3]};
		double[] link_rate = this.get_link_numbaer_rate(all_link_numbers);
		return link_rate;
	}
	
	public int get_form_action_feature(String url,String Html) throws MalformedURLException{
		// 提取form的Action 特征：post方法的action特征，post是将用户输入的值提交到指定服务器
	    // 在此判断提交服务器的域名是否与当前页面域名一致
	    // post方法的action介绍：action有三种类型并为其赋权重值
	    //    1、action为空值，即未将用户输入的值上传，其权重为1
	    //    2、action为路径，仿冒网站多用相对路径，其权重为3
	    //    3、action为完整的URL，则抽取其域名判断是否与当前页面域名一致，其权重为5
		int result = 0;
		// action的内容 为空 则结果加1 ；使用相对路径 结果加3，绝对路径-3；使用是完整的URL 则判断域名是否一致，一致-5，不一致+5
	    // 遍历网页中所有的form表单，对结果进行累加，返回最后结果，结果越大，可疑程度越高
		int weight_none = 1;  // 链接为空权重
		int weight_path = 3; // 链接为路径权重
		int weight_domain = 5;  // 链接为完整url权重
		String[] none_list = new String [] {null, "", "/", "#", " ", "http://#", "http://"}; //空串集合
		Document doc = Jsoup.parse(Html);
		Elements form = doc.select("form");
		for (Element i:form)
		{
			String form_method = i.attr("method");
			String form_action = i.attr("action");
			// 如果action 为空
			if ((Arrays.asList(none_list).contains(form_method)==true) ||(Arrays.asList(none_list).contains(form_action)==true)){
				result +=weight_none;
			}
			else{// 如果action不为空，这里仅考虑method为post的情况
				if (form_method.equals("post")==true){
					// 获得action方法的域名，判定其是否为完整的URL，如果域名为空则不是。
					String action_netloc = null;
					action_netloc = this.get_url_domain(form_action);
					// 如果aciton 是完整的URL路径，则判断其域名是否与当前页面域名一致
					if (action_netloc.equals("")==false){
						// 如果action域名与当前页面不一致
						url = this.Foramt_url(url);
						url = url.toLowerCase();
						URL url_second = new URL(url);
						String domain = url_second.getHost();
						int match = domain.indexOf(action_netloc);
						if (match ==-1){
							result += weight_domain;
							// 如果action域名与当前页面一致
						}
						else{
							result -= weight_domain;
						}
					}
					// 如果aciton 是路径，仿冒网站多数会使用相对路径
					else{
						String [] new_none_list = new String [] {"/",".","#"};
						for (String j:new_none_list){
							if(form_action.startsWith(j)){
								result -= weight_path;
							}
							else{
								result += weight_path;
							}
						}
					}
				}
				else{
					continue;
				}
			}
		}
		return result;
	}
	
	public double get_form_input_feature(String Html){
		//提取form的Input特征：提取input标签中 name id placeholder的值，判断其是否为敏感词汇，如 email password等
		double result = 0; // 包含敏感词汇的标签总数
		int counter = 0; // form标签总数
		Document doc = Jsoup.parse(Html);
		Elements form_input = doc.select("input");
		for(Element i: form_input)
		{
			String input_name = i.attr("name");
			String input_id = i.attr("id");
			String input_placeholder = i.attr("placeholder");
			String input_type = i.attr("type");
			// 仅考虑 没有隐藏的input标签
			if (input_type != "hidden"){
				counter+=1;
				for (String key:red_keyword)
					//  判断 name id placeholder 中是否包含关键字，如有一个包含即可跳出循环进行下一次判断
				{
					if ((input_id != null) && (input_id.toLowerCase().indexOf(key)!=-1)){
						result+=1;
					}
					if ((input_name != null) && (input_name.toLowerCase().indexOf(key)!=-1)){
						result+=1;
					}
					if ((input_placeholder != null) && (input_placeholder.toLowerCase().indexOf(key)!=-1)){
						result+=1;
					}
				}
			}
		}
		if (counter != 0){
			result = result/counter;
			BigDecimal b = new BigDecimal(result);  
			result = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
		}
		return result;
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
	
	public static void test_main(){
		
	}

	public static void main(String args[]) {
		try
		{
			html_link_extract.test_main();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}// main
}

