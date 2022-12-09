package qsol.qsoljecheonweb.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class QsolMessageDigest {

	public static String getSha512(String text, String salt) {

		String result = null;

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = text.getBytes(Charset.forName("UTF-8"));
			md.update(salt.getBytes());
			md.update(bytes);

			result = Base64.getEncoder().encodeToString(md.digest());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}
}
