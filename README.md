## March 1 2017 8:31 AM

# HtmlExtract-Java

Extract all the text in the text, Chinese, keywords, Title, ICP, link and inside and outside the chain ratio, form form, alert, meta, jump, sensitive words and other information

抽取HTML中所有文本、中文、关键词、Title、ICP、链接及内外链比例、form表单、alert、meta、跳转、敏感词等信息

* * *


## function/系统功能

Extract all the text in the text, Chinese, keywords, Title, ICP, link and inside and outside the chain ratio, form form, alert, meta, jump, sensitive words and other information

抽取HTML中所有文本、中文、关键词、Title、ICP、链接及内外链比例、form表单、alert、meta、跳转、敏感词等信息

## examples/使用范例

```
    String get_title(Html); //获取网页Title
    String match_chinese(Html, 1); //punc为1时抽取1中所有的中文和标点符号, punc为0时只抽取中文
    String jsoup_get_text(Html);  //获取网页中所有正文
    String get_ICP(Html);  //获取网页ICP
    double get_js_long(Html); // 获取html中js标签中内容所占比例
    double get_style_long(Html);  // 获取html中style标签中内容所占比例

    int[] get_link_number(String url, String[] link_content_list) // 获得页面中链接（URL）总数、内链个数、外链个数、空链个数，返回一个字典
    double[] get_link_numbaer_rate(int[] link_numbers) // 计算网页内外链比例
    int[] get_a_link_number(String url, String Html) // 获取网页所有a标签中的链接，并分四类返回
    int[] get_css_link_number(String url, String Html) // 获取网页所有css标签中的链接，并分四类返回
    int[] get_js_link_number(String url, String Html) // 获取网页所有js标签中的链接，并分四类返回
    int[] get_pic_link_number(String url, String Html) // 获取网页所有pic标签中的链接，并分四类返回
    int[] get_html_link_number(String url, String Html) // 获取网页所有iframe标签中html的链接，并分四类返回
    int get_form_action_feature(String url,String Html)  // 提取form的Action特征
    public double get_form_input_feature(String Html) //提取form的Input特征

    String match_chinese(Html, 1); //punc为1时抽取1中所有的中文和标点符号, punc为0时只抽取中文
    int zh_check(Html);  //判断text中是否包含中文
    int get_text_string_num(Html, '.'); //判断text中指定字符串match_string的个数
    int get_text_number_length(Html); //text中最长的数字串长度
    int get_text_red_keyword(Html);  //判断text中是否包含敏感词汇

    String get_url_domain(url); //抽取url的域名
    String get_host_domain(url);  //获取主机域名
    String Foramt_url(url);  // 规范化url
    int get_url_at(url);  //判断URL中是否含有@，？，-,_等特殊符号
    int get_url_ip(url);  //判断URL中是否包含IP
    String get_url_path(url); //抽取url的路径
    int get_url_port(url);  //抽取url的端口
    String get_url_query(url);  //获取url参数
```

## contact/联系方式


609610350@qq.com
