package com.stg.imageconsumer.domain.email;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import org.springframework.util.StringUtils;

import com.stg.imageconsumer.domain.attachment.Attachment;

@Entity
@Table(name = "email")
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String bccAddresses;
	
	@Column
	@Lob
	private String body;
	
	@Column
	private String ccAddresses;
	
	@Column
	private String fromAddresses;
	
	@Column
	private Date receivedDate;
	
	@Column
	private Date sentDate;
	
	@Column
	private String subject;
	
	@Column
	private String toAddresses;
	
	@OneToMany(mappedBy="email", cascade={CascadeType.ALL})
	private Set<Attachment> attachments;

	public Email() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBCC() {
		return bccAddresses;
	}

	public String getBody() {
		return body;
	}

	public String getCC() {
		return ccAddresses;
	}

	public String getFrom() {
		return fromAddresses;
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
		return toAddresses;
	}

	public void setBCC(String bcc) {
		this.bccAddresses = bcc;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setCC(String cc) {
		this.ccAddresses = cc;
	}

	public void setFrom(String from) {
		this.fromAddresses = from;
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
		this.toAddresses = to;
	}

	public void addAttachment(Attachment attachment) {
		if(attachments == null) {
			attachments = new HashSet<>();
		}
		attachment.setEmail(this);
		attachments.add(attachment);
	}

	public boolean hasBody() {
		return !StringUtils.isEmpty(body);
	}

	public Set<Attachment> getAttachments() {
		return (attachments != null) ? attachments : Collections.emptySet();
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

}
