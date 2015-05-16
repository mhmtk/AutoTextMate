package com.mhmt.autotextmate.adapters;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.activities.Main;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 16, 2015
 *  
 * Inspired by http://androidexample.com/How_To_Create_A_Custom_Listview_-_Android_Example/index.php?view=article_discription&aid=67&aaid=92
 */

public class RuleListViewAdapter extends BaseAdapter {

	private String logTag = "RuleListViewAdapter";

	/*********** Declare Used Variables *********/
	private Activity activity;
	private ArrayList<Rule> data;
	private static LayoutInflater inflater=null;
	public Resources res;
	Rule tempValue=null;
	private String tName;
	private String tText;

	/*************  RuleListViewAdapter Constructor *****************/
	public RuleListViewAdapter(Activity a, ArrayList<Rule> d,Resources resLocal) {

		// Store passed values into their respective fields
		activity = a;
		data=d;
		res = resLocal;

		// Get layout inflater
		inflater = ( LayoutInflater )activity.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return (data.size() < 0) ? 0 : data.size();
	}

	public Rule getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	// Holder for view elements
	public static class ViewHolder{

		private TextView nameText;
		private TextView descriptionText;
		private ToggleButton statusToggle;
		private ImageView onlyContactsImage;
		private ImageView smsImage;
		private ImageView callImage;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if(convertView==null){

			// Inflate the view (row)
			vi = inflater.inflate(R.layout.rule_list_item_table, null);

			// Match the view elements in the holder to their counterparts in the layout
			holder = new ViewHolder();
			holder.nameText = (TextView) vi.findViewById(R.id.list_textView_name);
			holder.descriptionText =(TextView) vi.findViewById(R.id.list_textView_description);
			holder.statusToggle = (ToggleButton) vi.findViewById(R.id.list_toggleButton_status);
			holder.onlyContactsImage = (ImageView) vi.findViewById(R.id.list_onlyContacts_image);
			holder.smsImage = (ImageView) vi.findViewById(R.id.list_sms_image);
			holder.callImage = (ImageView) vi.findViewById(R.id.list_call_image);

			/************  Set holder with LayoutInflater ************/
			vi.setTag( holder );
		}
		else 
			holder=(ViewHolder)vi.getTag();

		if(data.size()<=0) //No data in the given array
		{
		}
		else
		{
			// Get the current Rule from the ArrayList
			tempValue=null;
			tempValue = ( Rule ) data.get( position );
			tName = tempValue.getName();
			tText = tempValue.getText();

			// Configure the layout according to the current Rule
			holder.nameText.setText(tName);
			holder.descriptionText.setText(tText);
			holder.statusToggle.setChecked((tempValue.getStatus() == 1) ? true : false);
			if (tempValue.getOnlyContacts() == 0)
				holder.onlyContactsImage.setVisibility(View.INVISIBLE);
			switch (tempValue.getReplyTo()) {
				case 0:
					holder.smsImage.setVisibility(View.VISIBLE);
					holder.callImage.setVisibility(View.VISIBLE);
					break;
				case 1: // 1 = SMS, so hide call
					holder.smsImage.setVisibility(View.VISIBLE);
					holder.callImage.setVisibility(View.INVISIBLE);
					break;
				case 2: // 1 = Call, so hide SMS
					holder.smsImage.setVisibility(View.INVISIBLE);
					holder.callImage.setVisibility(View.VISIBLE);
					break;
			}

			// Set onLongClick for the row and onClick for the ToggleButton
			vi.setOnLongClickListener(new OnItemLongClickListener(tName, tText));
			holder.statusToggle.setOnCheckedChangeListener(new onItemToggleChangedListener(tName));
		} //end of else
		return vi;
	}

	/**
	 * OnCheckedChangeListener class for the usage of each ToggleButton of the list. Will call
	 * the onItemToggleClicked method of the Main activity, passing it the position and the isChecked
	 * status of the toggle
	 * 
	 * @author Mehmet Kologlu
	 *
	 */
	private class onItemToggleChangedListener implements CompoundButton.OnCheckedChangeListener {
		private String mName;

		//implement constructor to enable passing the position
		onItemToggleChangedListener(String name){
			mName = name;
		}
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			Main sct = (Main) activity;

			sct.onItemToggleClicked(mName, isChecked);
		}
	}

	/**
	 * OnItemLongClickListener class for the usage of each row of the list. Will call
	 * the onLongItemClick method of the Main activity, passing it the position of the row
	 * 
	 * @author Mehmet Kologlu
	 */
	private class OnItemLongClickListener implements OnLongClickListener {
		private String mName;
		private String mText;

		OnItemLongClickListener(String name, String text){
			mName = name;
			mText = text;
		}

		@Override
		public boolean onLongClick(View v) {
			Main sct = (Main) activity;
			sct.onLongItemClick(mName, mText);
			return true;
		}

	}
}