package org.csr.core.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public abstract class CRUDAction extends BasisAction {
	
	@RequestMapping(value = "prePage", method = RequestMethod.GET)
	public String prePage() {
		return prePage();
	}
}
