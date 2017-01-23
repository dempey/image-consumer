package com.stg.imageconsumer.domain.attachment;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.util.DigestUtils;

import com.stg.imageconsumer.domain.email.Email;

@Entity
@Table(name = "ATTACHMENT")
public class Attachment implements Persistable<String>, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")
	private String id;
	
	@ManyToOne
	@JoinColumn(name="email_id")
	private Email email;
	
	@Column(name="md5")
	private byte[] md5;
	
	@Column(name="filename")
	private String filename;
	
	@Column(name="length")
	private int length;
	
	@Column(name="s3_key")
	private String key;
	
	@Transient
	private byte[] data;
	
	public Attachment() {
	}
	
	public Attachment(String filename, byte[] data) {
		this.filename = filename;
		this.data = data;
		this.length = data.length;
		this.md5 = DigestUtils.md5Digest(data);
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public byte[] getMd5() {
		return md5;
	}
	
	public void setMd5(byte[] md5) {
		this.md5 = md5;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attachment other = (Attachment) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		return true;
	}

	@Override
	public boolean isNew() {
		return this.id == null;
	}
	
}
