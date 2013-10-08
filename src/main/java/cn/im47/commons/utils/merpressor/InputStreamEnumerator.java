package cn.im47.commons.utils.merpressor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class InputStreamEnumerator implements Enumeration<FileInputStream> {
	private Enumeration<String> files;

	public InputStreamEnumerator(Vector<String> files) {
		this.files = files.elements();
	}

	public boolean hasMoreElements() {
		return files.hasMoreElements();
	}

	public FileInputStream nextElement() throws FileListException {
		String path = files.nextElement().toString();

		try {
			FileInputStream input = new FileInputStream(path);
			return input;
		} catch (IOException e) {
			throw new FileListException(path);
		}
	}
}
