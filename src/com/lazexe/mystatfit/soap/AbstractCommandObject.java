package com.lazexe.mystatfit.soap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public abstract class AbstractCommandObject {

	protected SoapParams params;

	private SoapSerializationEnvelope envelope;
	private SoapObject request;
	private HttpTransportSE androidHttpTransport;

	public AbstractCommandObject(SoapParams params) {
		this.params = params;
		setup();
	}

	private void setup() {
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.xsd = SoapSerializationEnvelope.XSD;
		envelope.enc = SoapSerializationEnvelope.ENC;
		envelope.setAddAdornments(false);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.env = SoapSerializationEnvelope.ENV;
		envelope.implicitTypes = true;
		request = new SoapObject(params.getNamespace(), params.getMethdName());
		envelope.setOutputSoapObject(request);
		androidHttpTransport = new HttpTransportSE(params.getUrl());
		androidHttpTransport.debug = true;
	}

	public SoapObject getRequest() {
		return request;
	}

	public SoapSerializationEnvelope getEnvelope() {
		return envelope;
	}

	public HttpTransportSE getAndroidHttpTransport() {
		return androidHttpTransport;
	}

}
