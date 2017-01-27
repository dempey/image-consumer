package com.stg.imageconsumer.domain.attachment;

import java.io.InputStream;

public class KeyedFile {
	
	public String key;
	
	public InputStream inputStream;
	
	public KeyedFile(String key, InputStream inputStream) {
		this.key = key;
		this.inputStream = inputStream;
	}

	public String getKey() {
		return key;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

}