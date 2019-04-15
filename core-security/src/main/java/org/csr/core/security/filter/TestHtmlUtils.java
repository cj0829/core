package org.csr.core.security.filter;

import org.springframework.web.util.HtmlUtils;

/**
 * 测试htmlUtils 功能
 * @author hk
 *
 * 2012-12-27 下午11:22:53
 */
public class TestHtmlUtils {

    String html = "<ul class=\"nav\"><li><a href=\"http://www.mkfree.com\">首 页</a></li>"+
            "<li class=\"active\"><a href=\"http://blog.mkfree.com\">博客</a></li>"+
            "<li><a href=\"#\">RSS</a></li></ul>";
    /**
     * 把html的标签特殊字符转换成普通字符
     */
    public void testhtmlEscape(){
        String value = HtmlUtils.htmlEscape(html);
    }
    /**
     * 把html的特殊字符转换成普通数字
     */
    public void testhtmlEscapeDecimal(){
        String value = HtmlUtils.htmlEscapeDecimal(html);
    }
    /**
     * 把html的特殊字符转换成符合Intel HEX文件的字符串
     */
    public void htmlEscapeHex(){
        String value = HtmlUtils.htmlEscapeHex(html);
    }
    /**
     * 把html的特殊字符反转换成html标签
     * 以上三种方法都可以反转换
     */
    public void htmlUnescape(){
        String tmp = HtmlUtils.htmlEscapeDecimal(html);

        String value = HtmlUtils.htmlUnescape(tmp);
    }
    public static void main(String[] args) {
    	TestHtmlUtils test = new TestHtmlUtils();
    	test.testhtmlEscapeDecimal();
	}
}