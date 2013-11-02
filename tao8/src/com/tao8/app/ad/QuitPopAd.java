package com.tao8.app.ad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuitPopAd {

	private static Dialog dialog;
	
	private static QuitPopAd quitPopAd;
	
	public static QuitPopAd getInstance(){
		if(quitPopAd == null){
			quitPopAd = new QuitPopAd();
		}
		return quitPopAd;
	}
	
	/**
	 * 展示退屏广告
	 * @param context
	 */
	public void show(final Context context){
		
		new AlertDialog.Builder(context)
		.setTitle("退出提示")
		.setMessage("确定要退出当前应用吗？")
		.setPositiveButton("确定", new AlertDialog.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(dialog != null){
					dialog.cancel();
				}
				((Activity)context).finish();
			}
		})
		.setNegativeButton("取消", new AlertDialog.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.create().show();
	}
	
	/**
	 * 关闭退屏广告对话框
	 */
	public void close(){
		if(dialog != null && dialog.isShowing()){
			dialog.cancel();
		}
	}
	
	
	
}
