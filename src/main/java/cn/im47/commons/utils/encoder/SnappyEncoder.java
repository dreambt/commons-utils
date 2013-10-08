package cn.im47.commons.utils.encoder;

import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Snappy 快速加解密
 * <p/>
 * User: baitao.jibt@gmail.com
 * Date: 12-9-3
 * Time: 下午2:51
 */
public class SnappyEncoder {

	public static String encode(String source) throws Exception {
		try {
			byte[] compressed = Snappy.compress(source.getBytes("UTF-8"));
			return new String(compressed, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Exception("不支持的编码格式.");
		} catch (IOException e) {
			throw new Exception("编码失败.");
		}
	}

	public static String decode(String source) throws Exception {
		try {
			byte[] uncompressed = Snappy.uncompress(source.getBytes("UTF-8"));
			return new String(uncompressed, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Exception("不支持的编码格式.");
		} catch (IOException e) {
			throw new Exception("编码失败.");
		}
	}

}
