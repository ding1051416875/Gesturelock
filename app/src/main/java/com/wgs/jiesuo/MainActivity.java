package com.wgs.jiesuo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wgs.jiesuo.LockPatternView.Cell;
import com.wgs.jiesuo.LockPatternView.DisplayMode;
import com.wgs.jiesuo.LockPatternView.OnPatternListener;

public class MainActivity extends Activity{
	// private OnPatternListener onPatternListener;
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	private TextView titleTV;
	private Button cliearBtn;
	private int inputNum = 4;

	private boolean isFristGestrue = true;  //是否是第一次绘制手势
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		FindViewById();
		lockPatternView.setOnPatternListener(new OnPatternListener() {
			
			String currentPattern = "";
			public void onPatternStart() {}
			@SuppressWarnings("static-access")
			public void onPatternDetected(List<Cell> pattern) {
				//判断有没有设置手势密码
				if (ShareUtils.getIsLock(MainActivity.this)) {
					int result = lockPatternUtils.checkPattern(pattern);
					if (result != 1 && result == 0) {
						if(inputNum == 0){
							Toast.makeText(MainActivity.this, "输入次数已过，请重新启动", Toast.LENGTH_LONG).show();
							finish();
							return;
						}
						lockPatternView.setDisplayMode(DisplayMode.Wrong);
						titleTV.setText("密码输入错误，您还可以输入" + inputNum + "次");
						inputNum -= 1;
						titleTV.setTextColor(Color.RED);
						lockPatternView.clearPattern();
					} else if(result == 1){
						Toast.makeText(MainActivity.this, "欢迎加入我们的大家庭", Toast.LENGTH_LONG).show();
						lockPatternView.clearPattern();
						startActivity(new Intent(MainActivity.this, FristActivity.class));
						finish();
					}
				} else {
					if (!isFristGestrue) {
						if(currentPattern.equals(lockPatternUtils.patternToString(pattern))){
							Toast.makeText(MainActivity.this, "设置密码成功", Toast.LENGTH_LONG).show();
							lockPatternUtils.saveLockPattern(pattern);
							ShareUtils.setIsLock(MainActivity.this, true);
							lockPatternView.clearPattern();
							startActivity(new Intent(MainActivity.this, FristActivity.class));
							finish();
						}else{
							if(inputNum == 0){
								titleTV.setText("请点击重置按钮重新绘制");
								lockPatternView.clearFocus();
								titleTV.setTextColor(Color.RED);
								cliearBtn.setVisibility(View.VISIBLE);
								return;
							}
							titleTV.setText("两次绘制不一样，您还可以绘制" + inputNum + "次");
							inputNum -= 1;
							titleTV.setTextColor(Color.RED);
							lockPatternView.clearPattern();
						}
					} else {
						currentPattern = lockPatternUtils.patternToString(pattern);
						titleTV.setText("请再一次绘制手势密码");
						titleTV.setTextColor(Color.WHITE);
						lockPatternView.clearPattern();
						isFristGestrue = false;
					}
				}

			}

			public void onPatternCleared() {}

			public void onPatternCellAdded(List<Cell> pattern) {}
		});
	}

	private void FindViewById() {
		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);
		titleTV = (TextView) findViewById(R.id.title_text);
		cliearBtn = (Button) findViewById(R.id.button);
		cliearBtn.setVisibility(View.INVISIBLE);
		if(ShareUtils.getIsLock(MainActivity.this)){
			titleTV.setText("请输入手势密码");
		}else{
			titleTV.setText("请绘制您的手势密码");
		}
		cliearBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isFristGestrue = true;
				titleTV.setText("请绘制您的手势密码");
				titleTV.setTextColor(Color.WHITE);
				lockPatternView.clearPattern();
				inputNum = 4;
			}
		});
	}
}