package com.javgame.utility;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignUtil {
	
	private static final String TAG = "SignUtil";
	
	private static final String ALGORITHM = "RSA";
	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	private static final String DEFAULT_CHARSET = "UTF-8";

	
	/**
	 * RSA 加密
	 * @param content
	 * @param privateKey
	 * @return
	 */
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * md5加密
	 * @param order
	 * @param key
	 * @return
	 */
	public static String encrypt(String order, String key) {
        String signString = order +"&key="+ key;
        signString= signString.substring(1);
        if (signString.length() > 100){
            signString = signString.substring(0,100);
        }
        String signedString = md5(signString);
        String result = "&sign="+encode(signedString);
        return result;
    }
	
	public static String md5(String signString) {
        Log.d(TAG, "md5signString " + signString);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(signString.getBytes("utf-8"));
            byte b[] = md.digest();
            return getMD5String(b);
        } catch (Exception e) {
            e.printStackTrace();
            return "" ;
        }
    }

    private static String getMD5String(byte[] b) {
        int i;
        String str ;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
         i = b[offset];
         if (i < 0)
          i += 256;
         if (i < 16)
          buf.append("0");
         buf.append(Integer.toHexString(i));
        }
        str = buf.toString();
        return str;
    }
    
    public static String encode(String string) {
        try {
            return URLEncoder.encode(string,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string ;
        }
    }
}
