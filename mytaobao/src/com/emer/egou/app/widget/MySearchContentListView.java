package com.emer.egou.app.widget;

import android.R.integer;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MySearchContentListView extends ListView {

	public MySearchContentListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	}

	public MySearchContentListView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public MySearchContentListView(Context context) {
		super(context);

	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		for (int i = 0; i < getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) getChildAt(i);
			for (int j = 0; j < layout.getChildCount(); j++) {
			RelativeLayout rlLayout = (RelativeLayout) layout.getChildAt(0);
			View view = layout.getChildAt(1);
			int l = view.getLeft();
			int t = view.getTop();
			int r = view.getRight();
			int b = view.getBottom();
			Rect rect = new Rect(l, t, r, b);
			if (!rect.contains(x,y)) {
				return true;
			}else {
				return super.dispatchTouchEvent(ev);
			}
			}
		}
		return super.dispatchTouchEvent(ev);
	}
}
