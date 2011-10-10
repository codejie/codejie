/**
 * file   : TOOLKIT.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 21, 2011 11:02:38 AM
 */
package jie.java.android.savingkeeper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;

public final class TOOLKIT {

	public static Date String2Date(final String str) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
		Date date = null;
		try {
			date = fmt.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
}
