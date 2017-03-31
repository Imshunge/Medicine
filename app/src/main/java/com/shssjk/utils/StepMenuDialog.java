package com.shssjk.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.shssjk.activity.R;

public class StepMenuDialog extends AlertDialog {

	public StepMenuDialog(Context context, int theme) {
		super(context, theme);
	}

	public StepMenuDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_menu_dialog);
	}
}