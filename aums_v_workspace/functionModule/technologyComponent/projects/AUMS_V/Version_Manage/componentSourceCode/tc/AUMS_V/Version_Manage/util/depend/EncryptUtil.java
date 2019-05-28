package tc.AUMS_V.Version_Manage.util.depend;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import tc.bank.constant.BusException;
import tc.bank.constant.ErrorCodeModule;



/**
 * 加密，解密工具类
 * @author rdopc2392
 *
 */
public class EncryptUtil {

	private static String oriKeyStroe="RsFu47b0PXA=";//非常重要，要和web端保持一致
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		// TODO Auto-generated method stub
		String msg="afa4sjcs";
		String encontent = EncryptUtil.encryptor(msg);
		String decontent = EncryptUtil.decryptor(encontent);
		System.out.println("明文["+msg+"]");
		System.out.println("密文["+encontent+"]");
		System.out.println("解密["+decontent+"]");
		
	}
	/**
	 * 加密
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	public static String encryptor(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
		byte[] encontent=Encryptor(str);
		String ret=new String(Base64Custom.encode(encontent));
		return ret;
	}
	
	private static byte[] Encryptor(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
		Cipher c = Cipher.getInstance("DES");
		SecretKey deskey=new SecretKeySpec(Base64Custom.decode(oriKeyStroe.getBytes()),"DES");
		c.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] src = str.getBytes();
		byte[] cipherByte = c.doFinal(src);
		return cipherByte;
	}
	
	/**
	 * 解密
	 * @param buff
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static String decryptor(String buff) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException{
		byte[] decontent = Decryptor(Base64Custom.decode(buff.getBytes()));
		return new String(decontent);
	}
	private static byte[] Decryptor(byte[] buff) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
		Cipher c = Cipher.getInstance("DES");
		SecretKey deskey=new SecretKeySpec(Base64Custom.decode(oriKeyStroe.getBytes()),"DES");
		c.init(Cipher.DECRYPT_MODE, deskey);
		byte[] cipherByte = c.doFinal(buff);
		return cipherByte;
	}
	
	/**
	 * @category 简单密码校验
	 * @param Number1
	 *            入参|校验的密码|{@link java.lang.String}
	 * @param Number2
	 *            入参|密码校验长度|{@link java.lang.String}
	 * @return true:为简单密码<br/>
	 *         false:非简单密码<br/>
	 * @throws BusException 
	 */
	
	@InParams(param = {
			@Param(name = "password", comment = "密码", dictId = "305DCD027A3749AC9B22EEC3621C7B13", type = String.class),
			@Param(name = "len", comment = "长度控制", dictId = "305DCD027A3749AC9B22EEC3621C7B13", type = String.class)})
	@Component(label = "简单密码校验", author = "Anonymous", date = "2016-11-30 03:01:38" )
			
	public static boolean A_checkPassword(String password, int len) throws BusException {
//		try {
			// 默认为简单密码 true
			boolean checkResult = false;
			//判断是否包含空格
			if(password.indexOf(" ")!=-1){
				//包含空格
				checkResult = true;
				throw new BusException(ErrorCodeModule.IME999,"密码不能包含空格");
			}

			if (password.trim().length() != len) { // 规定长度为6
				checkResult = true;
				throw new BusException(ErrorCodeModule.IME999,"密码长度为"+len+"位");
			}

			String reg[] = new String[5];
			reg[0] = "(\\d)\\1{2}(\\d)\\2{2}$"; // AAABBB
			reg[1] = "(\\d)\\1{1}(\\d)\\2{1}(\\d)\\3{1}$"; // AABBCC
			reg[2] = "(\\d\\d)\\1+$"; // ABABAB
			reg[3] = "(\\d\\d\\d)\\1+$"; // ABCABC
			// 1,依次判断密码是否符合上述
			for (int i = 0; i < 4; i++) {
				Pattern pattern = Pattern.compile(reg[i]);
				Matcher mthcher = pattern.matcher(password);
				if (mthcher.matches()) {
					checkResult = true;
					throw new BusException(ErrorCodeModule.IME999,"密码出现连续，请重新输入");
				}
			}
			for (int i = 0; i < password.length(); i++) {
				char a = password.charAt(i);
				if (a < '0' || a > '9') { // 非数字
					checkResult = true;
					throw new BusException(ErrorCodeModule.IME999,"密码非数字");
				}
			}
			// 判断是否是连续的
			// 1，当含有9时，判断9前面的是否连续和后面的是否是从0开始连续
			if (password.contains("9")) {
				int index = password.indexOf('9');
				// 默认为连续标志
				boolean flag = true;
				for (int i = 0; i < index; i++) {
					char a = password.charAt(i);
					if (a + 1 != password.charAt(i + 1)) {
						flag = false;
						break;
					}
				}

				if (flag) {
					checkResult = true;
					throw new BusException(ErrorCodeModule.IME999,"密码为连续数字");
				}

				flag = true;
				char ch = '0';
				for (int i = index; i < 5; i++) {
					if (ch != password.charAt(i + 1)) {
						flag = false;
						break;
					}
					ch = (char) (ch + 1);
				}

				if (flag) {
					checkResult = true;
					throw new BusException(ErrorCodeModule.IME999,"密码为连续数字");
				}

			} else { // 2,密码不含9时，判断是否为连续的

				// 默认为连续标志
				boolean flag = true;
				for (int i = 0; i < 5; i++) {
					char a = password.charAt(i);
					if (a + 1 != password.charAt(i + 1)) {
						flag = false;
						break;
					}
				}

				if (flag) {
					checkResult = true;
					throw new BusException(ErrorCodeModule.IME999,"密码为连续数字");
				}
			}
			return checkResult;
	}

}
