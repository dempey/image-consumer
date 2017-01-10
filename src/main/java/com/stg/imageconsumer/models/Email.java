package com.stg.imageconsumer.models;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String bcc;
	private String body;
	private String cc;
	private String from;
	private Date receivedDate;
	private Date sentDate;
	private String subject;
	private String to;

	public String getBCC() {
		return bcc;
	}
	
	public String getBody() {
		return body;
	}

	public String getCC() {
		return cc;
	}

	public String getFrom() {
		return from;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public String getSubject() {
		return subject;
	}

	public String getTo() {
		return to;
	}

	public void setBCC(String bcc) {
		this.bcc = bcc;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setCC(String cc) {
		this.cc = cc;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
