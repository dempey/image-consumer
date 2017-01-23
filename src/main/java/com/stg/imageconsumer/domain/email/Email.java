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
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.util.StringUtils;

import com.stg.imageconsumer.domain.attachment.Attachment;

@Entity
@Table(name = "EMAIL")
public class Email implements Persistable<String>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")
	private String id;
	
	@Column(name="bcc_addresses")
	private String bccAddresses;
	
	@Column(name="body")
	@Lob
	private String body;
	
	@Column(name="cc_addresses")
	private String ccAddresses;
	
	@Column(name="from_addresses")
	private String fromAddresses;
	
	@Column(name="received_date")
	private Date receivedDate;
	
	@Column(name="sent_date")
	private Date sentDate;
	
	@Column(name="subject")
	private String subject;
	
	@Column(name="to_addresses")
	private String toAddresses;
	
	@OneToMany(mappedBy="email", cascade={CascadeType.ALL})
	private Set<Attachment> attachments;

	public Email() {
	}

	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean isNew() {
		return this.id == null;
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
