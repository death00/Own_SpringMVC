package com.git.exception;

/**
 * 自我注入异常
 * service里又出现了一个自己
 * @author 92432
 *
 */
public class SelfInjectionException extends RuntimeException{

	private static final long serialVersionUID = 2283526133551712425L;
	
	public SelfInjectionException() {
		super();
	}

	public SelfInjectionException(String message) {
		super(message);
	}

	public SelfInjectionException(Throwable cause) {
		super(cause);
	}

	public SelfInjectionException(String message, Throwable cause) {
		super(message, cause);
	}
}
