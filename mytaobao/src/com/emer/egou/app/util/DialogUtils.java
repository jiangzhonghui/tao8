package com.emer.egou.app.util;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogUtils {
	
	public static void alertDialog(String title,String message,Context context){
		AlertDialog.Builder builder = new Builder(context);
		if (title!=null) {
			builder.setTitle(title);
		}
		if (message!=null) {
			builder.setMessage(message);
		}
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	public static void progressDialog(String title,String message,Context context,int icon){
		
	}
}
