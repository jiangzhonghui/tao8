package com.tao8.app.ad;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 
 * @author zgb
 *
 */
public class CustomProgressBar extends LinearLayout {

	public CustomProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public CustomProgressBar(Context context, AttributeSet attrs,int flag){
		this(context, attrs);
		setOrientation(LinearLayout.HORIZONTAL);
		FrameLayout.LayoutParams params = null;
		if(flag == 0){
			params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		}else if(flag == 1){
			params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		}
		setLayoutParams(params);
		TextView txt = new TextView(context);
		LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		txtParams.topMargin = 2;
		txt.setLayoutParams(txtParams);
		txt.setTextSize(14);
		txt.setTextColor(Color.BLACK);
		txt.setText("数据加载中.....");
		
		ProgressBar bar = new ProgressBar(context);
		LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		barParams.leftMargin = 3;
		bar.setLayoutParams(barParams);
		
		addView(txt);
		addView(bar);
	}

}
