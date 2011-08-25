/**
 * file:   RCLoader.java
 * author: codejie (codejie@gmail.com)
 * date:   Aug 22, 2011 11:56:19 PM
 */
package jie.java.android.savingkeeper;

import android.app.Activity;

public class RCLoader {

	public static String getType(Activity act, int type) {
		
		String[] str = act.getResources().getStringArray(R.array.type);
		return str[type];		
	}
	
	//
	public static String getCurrency(Activity act, int currency) {
		
		String[] str = act.getResources().getStringArray(R.array.currency);
		return str[currency];
/*		
		if(currency == CURRENCY_TYPE_RMB) {
			return act.getResources().getString(R.string.currency_rmb);
		}
		else if(currency == CURRENCY_TYPE_US) {
			return act.getResources().getString(R.string.currency_us);
		}
		else if(currency == CURRENCY_TYPE_EU) {
			return act.getResources().getString(R.string.currency_eu);
		}
		else {
			return "unkn";
		}
*/		
	}	
	
}
