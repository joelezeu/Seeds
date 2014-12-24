package com.diadementi.seeds.util;

public class SeedsException extends RuntimeException {
	/**
	 * if code>0 the it an http status error, else timeOut
	 */
	private static final long serialVersionUID = 1L;
	int code;
	public int getCode(){
		return code;
	}
	public SeedsException() {
		// TODO Auto-generated constructor stub
	}

	public SeedsException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}
	public SeedsException(int code){
		this.code=code;
	}
	public SeedsException(String detailMessage,int code) {
		super(detailMessage);
		this.code=code;
		// TODO Auto-generated constructor stub
	}
	public SeedsException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public SeedsException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

}
