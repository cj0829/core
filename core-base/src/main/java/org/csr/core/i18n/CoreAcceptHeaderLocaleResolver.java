package org.csr.core.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

public class CoreAcceptHeaderLocaleResolver extends AcceptHeaderLocaleResolver {
	public static final String LOCALE_KEY = "WW_TRANS_I18N_LOCALE";
	Locale myLocale;

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		return myLocale;
	}

	@Override
	public void setLocale(HttpServletRequest request,HttpServletResponse response, Locale locale) {
		myLocale = locale;
	}

}