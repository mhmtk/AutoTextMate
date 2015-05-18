package com.mhmt.autotextmate.activities;

import com.mhmt.autotextmate.R;

import android.app.ListActivity;
import android.os.Bundle;

public class Outbox extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outbox);
	}
}
