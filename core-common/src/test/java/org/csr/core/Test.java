package org.csr.core;

import java.util.Date;

import org.csr.core.util.DateUtil;

public class Test {
	public static void main(String[] args) {
		Date parseDateTimeToMin = DateUtil.parseDateTimeToMin("2017-11-28 10:00");
		System.out.println(parseDateTimeToMin.getTime());
	}
}
