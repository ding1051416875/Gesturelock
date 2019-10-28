package com.wgs.jiesuo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareUtils {

	private static SharedPreferences sp;

	public static void setIsLock(Context context, boolean flag) {
		sp = context.getSharedPreferences("lock", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean("flag", flag);
		edit.commit();
	}

	public static boolean getIsLock(Context context) {
		sp = context.getSharedPreferences("lock", Context.MODE_PRIVATE);
		return sp.getBoolean("flag", false);
	}
}
