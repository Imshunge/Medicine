package com.shssjk.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.shssjk.activity.R;

public class ExitDialog extends AlertDialog {

	public ExitDialog(Context context, int theme) {
		super(context, theme);
	}

	public ExitDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_dialog);
	}
}