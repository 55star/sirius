package com.fivestar.sirius;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class Utils {
	final static String TAG = "Utils.java";
	final static String SHAREDPREFFILE = "mb_cred";
	private static int LOGLEVEL = 42;

	public static void log(String tag, String mess) {
		if (LOGLEVEL > 1) {
			Log.e(tag, mess);
		} else {
			if(LOGLEVEL > 0) {
				Log.w(tag, mess);
			}
		}
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size=1024;
		try {
			byte[] bytes=new byte[buffer_size];
			for(;;) {
				int count=is.read(bytes, 0, buffer_size);
				if(count == -1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){}
	}

	private static String convertToHex(byte[] data) { 
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) { 
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do { 
				if ((0 <= halfbyte) && (halfbyte <= 9)) 
					buf.append((char) ('0' + halfbyte));
				else 
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while(two_halfs++ < 1);
		} 
		return buf.toString();
	} 

	public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException  { 
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		return convertToHex(sha1hash);
	}

	public static String createSalt() {
		char data[] = new char[18];
		for(int i=0; i<18; i++) {
			int rand = new Random().nextInt((int)(1 + new Date().getTime() % 74));
			if (rand == 96 || rand == 94 || rand == 92) {
				rand -= 1;
			}
			data[i] = (char)new Random().nextInt((int)(1 + new Date().getTime() % 74));
		}
		return data.toString();
	}

	public static boolean StoreByteImage(Context mContext, byte[] imageData, int quality, String expName) {

		File sdImageMainDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/Mobilblogg/latest");
		if(!sdImageMainDirectory.exists()) {
			sdImageMainDirectory.mkdirs();
		}
		FileOutputStream fileOutputStream = null;
		String fileName = expName;

		try {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 5;

			Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);

			fileOutputStream = new FileOutputStream(sdImageMainDirectory.toString() +"/" + fileName + ".jpg");

			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			myImage.compress(CompressFormat.JPEG, quality, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	private static SharedPreferences getPrefs(Context c) {
		SharedPreferences sp = c.getSharedPreferences(SHAREDPREFFILE, c.MODE_PRIVATE);
		return sp;
	}

	public static void saveCredentials(Context c, String userName, String passWord) {
		Editor e = getPrefs(c).edit();
		e.putString("mb_cred_usr", userName);
		e.putString("mb_cred_pwd", passWord);
		e.commit();
	}

	public static String getCredentialsUsername(Context c) {
		SharedPreferences sp = getPrefs(c);
		if(sp.contains("mb_cred_usr")) {
			return sp.getString("mb_cred_usr", "default");
		} else {
			return null;
		}
	}

	public static String getCredentialsPassword(Context c) {
		SharedPreferences sp = getPrefs(c);
		if(sp.contains("mb_cred_pwd")) {
			return sp.getString("mb_cred_pwd", "default");
		} else {
			return null;
		}
	}

	public static void saveSecretWord(Context c, String secretWord) {
		Editor e = getPrefs(c).edit();
		e.putString("secret", secretWord);
		e.commit();
	}

	public static String getSecretWord(Context c) {
		SharedPreferences sp = getPrefs(c);
		if(sp.contains("secret")) {
			return sp.getString("secret", "default");
		} else {
			return null;
		}
	}

	public static void addVisitUser(Context context, String userName) {
		SharedPreferences sp = getPrefs(context);
		Editor e = getPrefs(context).edit();

		if(sp.contains("visitedUsers")) {
			String list = sp.getString("visitedUsers", "");
			if(list.indexOf(userName) == -1) {
				String append = list + "," + userName;
				Utils.log(TAG,"Ny lista: " + append);
				e.putString("visitedUsers", append);
				e.commit();
			}
		} else {
			e.putString("visitedUsers",userName);
			e.commit();
		}
	}

	public static String[] getVisitUser(Context context) {
		SharedPreferences sp = getPrefs(context);
		if(sp != null && sp.contains("visitedUsers")) {
			Utils.log(TAG,"Returns: " + sp.getString("visitedUsers", ""));
			String[] list = sp.getString("visitedUsers", "").split(",");
			Utils.log(TAG, "Returns " + list.length + " users");
			return list;
		} else {
			Utils.log(TAG,"Inga tidigare bes�kta bloggar");
			return null;
		}
	}

	public static void removeSavedCredentials(Context c) {
		Editor e = getPrefs(c).edit();
		
		e.remove("mb_cred_usr");
		e.remove("mb_cred_pwd");
		e.remove("secret");
		e.commit();
	}
}