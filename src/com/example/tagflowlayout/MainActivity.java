package com.example.tagflowlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private TagFlowLayout mTagFlowLayout;

	private String[] mstringArray;

	private ToggleButton togbtn;

	private LinearLayout lly_content;

	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mstringArray = getResources().getStringArray(R.array.StringArray);

		InitiateWidget();
		InintiateData();
		SetonCheckListnerForTogglebutton();
		Button btn = new Button(this);
		btn.setText("确定是在中间的位置,没有错");
		lly_content.addView(btn);

	}

	/**
	 * 为点击按钮设置点击事件
	 */
	private void SetonCheckListnerForTogglebutton() {
		togbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (buttonView.getId() == R.id.toggle_button) {
					mTagFlowLayout.setIsAssaginSpace(isChecked);
					mTagFlowLayout.removeAllViews();
					InintiateData();
				}
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void InitiateWidget() {
		mTagFlowLayout = (TagFlowLayout) findViewById(R.id.tagflowLayout);
		lly_content = (LinearLayout) findViewById(R.id.lly_content);
		togbtn = (ToggleButton) findViewById(R.id.toggle_button);
	}

	/**
	 * 初始化数据,并添加到到集合中
	 */
	private void InintiateData() {
		for (int i = 0; i < mstringArray.length; i++) {
			Button tv = new Button(this);
			tv.setGravity(Gravity.CENTER);
			tv.setText(mstringArray[i]);

			final int position = i;
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(MainActivity.this, mstringArray[position], 0).show();
				}
			});
			mTagFlowLayout.addView(tv);
		}
	}
}
