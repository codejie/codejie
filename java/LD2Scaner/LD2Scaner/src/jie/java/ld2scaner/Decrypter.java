package jie.java.ld2scaner;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

//http://sucre.blog.51cto.com/1084905/531757
//http://www.cnblogs.com/SuperBrothers/archive/2012/11/24/2785971.html

public class Decrypter {

	public static void main(String[] args) throws NoSuchAlgorithmException,  
    InvalidKeyException, NoSuchPaddingException,  
    InvalidKeySpecException, IllegalBlockSizeException,  
    BadPaddingException {  
		
        SecureRandom sr = new SecureRandom();  
        KeyGenerator kg = KeyGenerator.getInstance("DES");  
        kg.init(sr);  
        SecretKey key = kg.generateKey();       

        byte rawKeyData[] = "11111111".getBytes();//key.getEncoded();// "key".getBytes();  
 
        String str = "123dfsdkfklsdfjksdjfsdklfjskl"; // ´ý¼ÓÃÜÊý¾Ý  
        byte[] encryptedData = encrypt(rawKeyData, str);  
        String ret = decrypt(rawKeyData, encryptedData);
        System.out.println("ret = " + ret);
	}
	
	public static void init() throws NoSuchAlgorithmException,  
    InvalidKeyException, NoSuchPaddingException,  
    InvalidKeySpecException, IllegalBlockSizeException,  
    BadPaddingException {

		
	}
	
    public static byte[] encrypt(byte rawKeyData[], String str)  
            throws InvalidKeyException, NoSuchAlgorithmException,  
            IllegalBlockSizeException, BadPaddingException,  
            NoSuchPaddingException, InvalidKeySpecException {  
        SecureRandom sr = new SecureRandom();  
        DESKeySpec dks = new DESKeySpec(rawKeyData);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey key = keyFactory.generateSecret(dks);  
        Cipher cipher = Cipher.getInstance("DES");  
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);  
        byte data[] = str.getBytes();  
        byte[] encryptedData = cipher.doFinal(data);  
        return encryptedData;  
    } 	

    public static String decrypt(byte rawKeyData[], byte[] encryptedData)  
            throws IllegalBlockSizeException, BadPaddingException,  
            InvalidKeyException, NoSuchAlgorithmException,  
            NoSuchPaddingException, InvalidKeySpecException {  
        SecureRandom sr = new SecureRandom();  
        DESKeySpec dks = new DESKeySpec(rawKeyData);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey key = keyFactory.generateSecret(dks);  
        Cipher cipher = Cipher.getInstance("DES");  
        cipher.init(Cipher.DECRYPT_MODE, key, sr);  
        byte decryptedData[] = cipher.doFinal(encryptedData);  
        return new String(decryptedData);  
    }
    
    public static String decrypt(byte[] encryptedData)  
            throws IllegalBlockSizeException, BadPaddingException,  
            InvalidKeyException, NoSuchAlgorithmException,  
            NoSuchPaddingException, InvalidKeySpecException {
    	
        SecureRandom sr = new SecureRandom();  
        KeyGenerator kg = KeyGenerator.getInstance("DES");  
        kg.init(sr);
//
        byte rawKeyData[] = null;
		try {
			rawKeyData = "language".getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// kg.generateKey().getEncoded();// "key".getBytes();
    	
//        SecureRandom sr = new SecureRandom();  
        DESKeySpec dks = new DESKeySpec(rawKeyData);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey key = keyFactory.generateSecret(dks);  
        Cipher cipher = Cipher.getInstance("DES");  
        cipher.init(Cipher.DECRYPT_MODE, key, sr);  
        byte decryptedData[] = cipher.doFinal(encryptedData);  
        return new String(decryptedData);  
    }     
	
}
