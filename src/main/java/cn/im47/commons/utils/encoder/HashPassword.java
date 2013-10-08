package cn.im47.commons.utils.encoder;

import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

/**
 * 密码加密算法
 * <p/>
 * User: baitao.jibt@gmail.com
 * Date: 12-9-25
 * Time: 下午8:03
 */
public class HashPassword {

	private static final int SALT_SIZE = 8;
	public static final int HASH_INTERATIONS = 1024;

	private String salt;
	private String password;

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static HashPassword encrypt(String plainText) {
		HashPassword result = new HashPassword();
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		result.salt = Encodes.encodeHex(salt);

		byte[] hashPassword = Digests.sha1(plainText.getBytes(), salt, HASH_INTERATIONS);
		result.password = Encodes.encodeHex(hashPassword);
		return result;
	}

}
