package qsol.qsoljecheonweb.util;

public class QsolManagerInfoUtil {

	public static String getManagerId() {
		return QsolHttpSessionUtil.getManagerId();
	}
	
	public static String getManagerId(boolean nullable) {
		return QsolHttpSessionUtil.getManagerId(nullable);
	}

}
