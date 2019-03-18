package util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chengkai on 18-12-28.
 */

public class MD5Util {

    private static final String MESSAGE_DIGEST_ALGORITHM = "MD5";
    private static MessageDigest MD5;

    static {
        try {
            MD5 = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getMD5(byte[] buf, int offset, int len)
    {
        if(buf == null || buf.length == 0){
            return null;
        }
        MD5.reset();
        MD5.update(buf,offset,len);
        return HexUtil.bytesToHex(MD5.digest());
    }

    public static String getMD5(byte[] buf)
    {
        if(buf == null)
            return "";
        return getMD5(buf,0,buf.length);
    }

    public static String getMD5(String string)
    {
        if(TextUtils.isEmpty(string)){
            return "";
        }
        return getMD5(string.getBytes());
    }

    public static String getMD5FromFile(File file) {
        try {
            return getMD5(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMD5(InputStream stream)
    {
        if(stream == null){
            return null;
        }
        try {
            MD5.reset();
            byte[] buffer = new byte[8192];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return HexUtil.bytesToHex(MD5.digest());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
