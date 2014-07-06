package com.lazexe.mystatfit.soap;

public class Editor extends AbstractCommandObject implements
		SoapExecutableInterface {

	public static final String SOAP_ACTION = "http://mystatfit.com/EditProfile";
	public static final String METHOD_NAME = "EditProfile";
	public static final String NAMESPACE = "http://mystatfit.com/";
	public static final String URL = "http://mystatfit.com/aut/";
	
	public Editor(SoapParams params) {
		super(params);
	}

	@Override
	public void execute() {
		
	}

}
