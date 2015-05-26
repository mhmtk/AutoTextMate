package com.mhmt.autotextmate.activities;

import com.mhmt.autotextmate.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.widget.TextView;

public class Instructions extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructions);
		
		TextView instructionsTextView = (TextView)findViewById(R.id.instructions_instructions);
		instructionsTextView.setText(Html.fromHtml(getString(R.string.instructions)));
	}
}
