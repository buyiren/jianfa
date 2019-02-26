package tc.View.Server.utils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;


public class EncryptUtil {

	private static String oriKeyStroe="RsFu47b0PXA=";//非常重要，要和web端保持一致
//	private KeyGenerator keygen;
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		// TODO Auto-generated method stub
		String msg="afa4j";
		String encontent = EncryptUtil.encryptor(msg);
		String decontent = EncryptUtil.decryptor(encontent);
		System.out.println("明文["+msg+"]");
		System.out.println("密文["+encontent+"]");
		System.out.println("解密["+decontent+"]");
		
	}
	public static String encryptor(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
		byte[] encontent=Encryptor(str);
		String ret=new BASE64Encoder().encode(encontent);
		return ret;
	}
	private static byte[] Encryptor(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
		Cipher c = Cipher.getInstance("DES");
		SecretKey deskey=new SecretKeySpec(new BASE64Decoder().decodeBuffer(oriKeyStroe),"DES");
		c.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] src = str.getBytes();
		byte[] cipherByte = c.doFinal(src);
		return cipherByte;
	}
	public static String decryptor(String buff) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException{
		byte[] decontent = Decryptor((new BASE64Decoder()).decodeBuffer(buff));
		return new String(decontent);
	}
	private static byte[] Decryptor(byte[] buff) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
		Cipher c = Cipher.getInstance("DES");
		SecretKey deskey=new SecretKeySpec(new BASE64Decoder().decodeBuffer(oriKeyStroe),"DES");
		c.init(Cipher.DECRYPT_MODE, deskey);
		byte[] cipherByte = c.doFinal(buff);
		return cipherByte;
	}

}
