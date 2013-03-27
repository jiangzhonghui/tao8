package com.emer.egou.app.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.emer.egou.app.BuildConfig;
import com.emer.egou.app.R;
import com.emer.egou.app.util.DialogUtils;

public class RegistActivity extends BaseActivity implements OnFocusChangeListener,
		OnClickListener, OnCheckedChangeListener{
	private static final String TAG = "RegistActivity";
	private EditText usernameEditText;
	private EditText passwordEditText;
	private EditText emailEditText;
	private EditText repasswordEditText;
	private Button hadButton;
	private Button registButton;
	private String password;
	private TextView protocolTextView;
	private String username;
	private String repassword;
	private String email;
	private CheckBox protocolBox;
	private boolean checked = false;
	private TextView headPrompTextView;
	private ImageButton backImageButton;
	private ScrollView containerScrollView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_login);
		findView();
		setLinstener();
	}

	private void setLinstener() {
		usernameEditText.setOnFocusChangeListener(this);
		passwordEditText.setOnFocusChangeListener(this);
		emailEditText.setOnFocusChangeListener(this);
		repasswordEditText.setOnFocusChangeListener(this);

		hadButton.setOnClickListener(this);
		registButton.setOnClickListener(this);
		registButton.setClickable(false);

		protocolTextView.setOnClickListener(this);
		backImageButton.setOnClickListener(this);

		protocolBox.setOnCheckedChangeListener(this);
	}

	private void findView() {
		usernameEditText = (EditText) findViewById(R.id.register_et_username);
		passwordEditText = (EditText) findViewById(R.id.register_et_password);
		emailEditText = (EditText) findViewById(R.id.register_et_email);
		repasswordEditText = (EditText) findViewById(R.id.register_et_repassword);

		hadButton = (Button) findViewById(R.id.register_btn_had_account);
		registButton = (Button) findViewById(R.id.register_btn_regist);
		backImageButton = (ImageButton) findViewById(R.id.register_head_back);

		protocolTextView = (TextView) findViewById(R.id.register_tv_protocol);
		protocolTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		protocolTextView.getPaint().setAntiAlias(true);// 抗锯齿

		headPrompTextView = (TextView) findViewById(R.id.register_tv_head_prompt);
		String headPromptInfo = getString(R.string.register_tv_head_prompt_info);
		headPromptInfo = String.format(headPromptInfo, "lishenshan2010");
		headPrompTextView.setText(headPromptInfo);

		protocolBox = (CheckBox) findViewById(R.id.register_cb_protocol);
		containerScrollView = (ScrollView) findViewById(R.id.register_login_sv_container);
		
	}

	// ^([a-zA-Z0-9]|[_]|[-]){3,12}$
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		EditText editText = (EditText) v;
		int id = v.getId();
		switch (id) {
		case R.id.register_et_username:
			username = editText.getText().toString().trim();
			if (!TextUtils.isEmpty(username) && !hasFocus) {
				if (username
						.matches("^([a-zA-Z0-9]|[_]|[-]|[\u4E00-\uFA29]|[\uE7C7-\uE7F3]){3,12}$")) {
					setRegisterBtn();
				} else {
					DialogUtils.alertDialog(null, "用户名不合法,请重新填写", RegistActivity.this);
					editText.setText("");
				}
			}
			break;
		case R.id.register_et_password:
			password = editText.getText().toString().trim();
			if (!TextUtils.isEmpty(password) && !hasFocus) {
				if (password.matches("^[a-zA-Z0-9]{6,16}$")) {
					setRegisterBtn();
				} else {
					DialogUtils.alertDialog(null, "您输入的密码不合法,请重新输入",
							RegistActivity.this);
					editText.setText("");
					password = "";
				}
			}
			break;
		case R.id.register_et_repassword:
			repassword = editText.getText().toString().trim();
			if (!TextUtils.isEmpty(repassword) && !hasFocus) {
				if (repassword.equals(password)) {
					setRegisterBtn();
				} else {
					DialogUtils.alertDialog(null, "您两次输入的密码不一致,请重新输入",
							RegistActivity.this);
					editText.setText("");
					repassword = "";
				}
			}
			break;
		case R.id.register_et_email:
			email = editText.getText().toString().trim();
			if (!TextUtils.isEmpty(email) && !hasFocus) {
				if (email.matches("^[\\w\\-\\.]+@[\\w\\-\\.]+(\\.\\w+)+$")) {
					setRegisterBtn();
				} else {
					DialogUtils.alertDialog(null, "您输入的邮箱不合法,请重新输入",
							RegistActivity.this);
					repassword = "";
				}
			}
			break;
		default:
			break;
		}
	}

	private void setRegisterBtn() {
		if (isEmpty(username) && isEmpty(email) && isEmpty(password)
				&& isEmpty(repassword) && checked) {
			registButton.setClickable(true);
			System.out.println(username + email + password + repassword
					+ checked);
			Toast.makeText(getApplicationContext(), "tongguo", 0).show();
		} else {
			registButton.setClickable(false);
		}

	}

	/**
	 * 检查是否为null或者为空
	 * 
	 * @param str
	 * @return
	 */
	private boolean isEmpty(String str) {
		boolean b = false;
		if (str != null && str.length() > 0) {
			return !b;
		} else {
			return b;
		}

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.register_btn_had_account:
			containerScrollView.removeAllViews();
			View loginView = View.inflate(RegistActivity.this, R.layout.login, null);
			containerScrollView.addView(loginView);
			
			
			
			
			
			headPrompTextView = (TextView) findViewById(R.id.register_tv_head_prompt);
			String headPromptInfo = getString(R.string.register_tv_head_prompt_info);
			headPromptInfo = String.format(headPromptInfo, "lishenshan2010");
			headPrompTextView.setText(headPromptInfo);
			break;
		case R.id.register_btn_regist:
			if (BuildConfig.DEBUG) {
				android.util.Log.e(TAG, "注册点击");
			}
			break;
		case R.id.register_head_back:
			onBackPressed();
			break;
		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		usernameEditText.clearFocus();
		emailEditText.clearFocus();
		passwordEditText.clearFocus();
		repasswordEditText.clearFocus();
		// TODO Auto-generated method stub
		checked = isChecked;
		System.out.println("password " + password);
		System.out.println("repassword " + repassword);
		if (repassword != null && (repassword.length() > 0)) {
			if (repassword.equals(password)) {
				setRegisterBtn();
			} else {
				DialogUtils
						.alertDialog(null, "您两次输入的密码不一致,请重新输入", RegistActivity.this);
				passwordEditText.setText("");
				repasswordEditText.setText("");
				repassword = "";
			}
		}
	}

}
