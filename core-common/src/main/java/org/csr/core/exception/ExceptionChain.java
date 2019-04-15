package org.csr.core.exception;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName:VirtualFilterChain.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:20:27 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class ExceptionChain{

	private final List<ValidationChainException> additionalException;
	/**
	 * 判断现在执行第几个过滤器
	 */
	private int currentPosition = 0;

	public ExceptionChain(List<ValidationChainException> additionalException) {
		this.additionalException = additionalException;
	}

	public void validationException(HttpServletRequest request, HttpServletResponse response,Exception exception)throws IOException {
		// 如果过滤器没有执行完，则继续执行过滤器
		if (currentPosition < additionalException.size()) {
			currentPosition++;
			ValidationChainException nextExceptionr = additionalException.get(currentPosition - 1);
			nextExceptionr.validation(request, response, exception, this);
		}
	}
}
