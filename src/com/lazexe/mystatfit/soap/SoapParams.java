package com.lazexe.mystatfit.soap;

public class SoapParams {

	private String soapAction;
	private String methodName;
	private String namespace;
	private String url;

	public SoapParams(String soapAction, String methdName, String namespace,
			String url) {
		this.soapAction = soapAction;
		this.methodName = methdName;
		this.namespace = namespace;
		this.url = url;
	}

	public String getSoapAction() {
		return soapAction;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethdName(String methdName) {
		this.methodName = methdName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
