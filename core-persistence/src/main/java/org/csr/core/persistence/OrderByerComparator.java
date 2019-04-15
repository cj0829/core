/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.core.persistence;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;


/**
 * ClassName:OrderByerComparator.java <br/>
 * System Name：    core <br/>
 * Date:     2016年6月29日下午1:00:13 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class OrderByerComparator implements Comparator<Object> {

    private boolean ascend;
    private String orderField;

    /**
     * 实例化排序比较器
     * 
     * @param ascend true：升序；false：倒序
     */
    public OrderByerComparator(boolean ascend) {
        this.ascend = ascend;
    }

    /**
     * 实例化排序比较器
     * 
     * @param orderField 排序字段，以,分隔，如：title,grade
     * @param ascend true：升序；false：倒序
     */
    public OrderByerComparator(String orderField, boolean ascend) {
        this.orderField = orderField;
        this.ascend = ascend;
    }

    public int compare(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return 0;
        }
        if (orderField != null && !orderField.trim().equals("")) {
            String[] orderFields = orderField.split(",");
            Object ob1;
            Object ob2;
            for (String field : orderFields) {
                try {
                    ob1 = PropertyUtils.getProperty(o1, field);
                    ob2 = PropertyUtils.getProperty(o2, field);
                } catch (Exception e) {
                    ob1 = ((Orderbyer) o1).getOrderby();
                    ob2 = ((Orderbyer) o2).getOrderby();
                }
                int result = doCompare(ob1, ob2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        } else {
            Object ob1 = ((Orderbyer) o1).getOrderby();
            Object ob2 = ((Orderbyer) o2).getOrderby();
            return doCompare(ob1, ob2);
        }
    }

    private int doCompare(Object ob1, Object ob2) {
        int result = 0;
        if (ob1 == null && ob2 == null) {
            result = 0;
        } else if (ob1 == null && ob2 != null) {
            result = -1;
        } else if (ob1 != null && ob2 == null) {
            result = 1;
        } else {
            if (ob1 instanceof String) {
                result = GBKCompare((String) ob1, (String) ob2);
            } else if (ob1 instanceof Integer) {
                result = ((Integer) ob1).compareTo((Integer) ob2);
            } else if (ob1 instanceof Date) {
                result = ((Date) ob1).compareTo((Date) ob2);
            } else if (ob1 instanceof Long) {
                result = ((Long) ob1).compareTo((Long) ob2);
            } else if (ob1 instanceof Float) {
                result = ((Float) ob1).compareTo((Float) ob2);
            } else if (ob1 instanceof Double) {
                result = ((Double) ob1).compareTo((Double) ob2);
            } else if (ob1 instanceof Short) {
                result = ((Short) ob1).compareTo((Short) ob2);
            } else if (ob1 instanceof Date) {
                result = ((Date) ob1).compareTo((Date) ob2);
            } else {
                result = GBKCompare(ob1.toString(), ob2.toString());
            }
        }

        if (ascend) {
            return result;
        } else {
            return -(result);
        }
    }

    private static boolean isSet(String s) {
        if (s == null || "".equals(s)) {
            return false;
        }
        return true;
    }

    public static int GBKCompare(String s1, String s2) {
        // 1. handle special case
        if (!isSet(s1) && !isSet(s2)) {
            return 0;
        } else if (!isSet(s1) && isSet(s2)) {
            return -1;
        } else if (isSet(s1) && !isSet(s2)) {
            return 1;
        }

        int result = 0;
        for (int i = 0; i < s1.length() && i < s2.length(); i++) {
            String es1 = GBKEncode(s1.charAt(i));
            String es2 = GBKEncode(s2.charAt(i));
            result = es1.compareTo(es2);
            if (result != 0) {
                break;
            }
        }
        if (result == 0 && (s1.length() != s2.length())) {
            result = (s1.length() < s2.length() ? -1 : 1);
        }
        return result;
    }

    private static String GBKEncode(char c) {
        String s = String.valueOf(c);
        try {
            s = URLEncoder.encode(s, "GB2312").replace('%', '~');
        } catch (UnsupportedEncodingException e) {
            s = String.valueOf(c);
        }
        return s;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(GBKCompare(null, null));
        System.out.println(GBKCompare("", null));
        System.out.println(GBKCompare(null, ""));
        System.out.println(GBKCompare("", ""));

        System.out.println(GBKCompare("abcd", "abcde"));
        System.out.println("abcd".compareTo("abcde"));

        System.out.println(GBKCompare("", "abcde"));
        System.out.println("".compareTo("abcde"));

        System.out.println(GBKCompare("aa", ""));
        System.out.println("aa".compareTo(""));

        System.out.println(GBKCompare("中国中国我们压缩", "中国中国中国中国"));
        System.out.println("我们".compareTo("中国"));

        // System.out.println(GBKEncode('我'));
    }

}
