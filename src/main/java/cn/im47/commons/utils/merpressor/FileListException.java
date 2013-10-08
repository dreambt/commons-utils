package cn.im47.commons.utils.merpressor;

import java.util.NoSuchElementException;

public class FileListException extends NoSuchElementException {
	public String path = "";

	public FileListException(String path) {
		super();
		this.path = path;
	}
}
