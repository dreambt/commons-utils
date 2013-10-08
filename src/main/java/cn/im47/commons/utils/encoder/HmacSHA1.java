package cn.im47.commons.utils.encoder;

import org.springside.modules.utils.Encodes;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * HmacSHA1 算法用于腾讯微博加密
 * <p/>
 * User: baitao.jibt@gmail.com
 * Date: 12-8-11
 * Time: 下午7:12
 */
public class HmacSHA1 {

	public static String encode(String data, String key) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException ignore) {
		}
		return Encodes.encodeBase64(byteHMAC);
	}

}
