package com.mhmt.autotextmate.adapters;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.activities.Main;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 16, 2015
 * 
 */
public class RuleListViewAdapter extends BaseAdapter   implements OnClickListener {

	/*********** Declare Used Variables *********/
	private Activity activity;
	private ArrayList data;
	private static LayoutInflater inflater=null;
	public Resources res;
	Rule tempValues=null;
	int i=0;

	/*************  RuleListViewAdapter Constructor *****************/
	public RuleListViewAdapter(Activity a, ArrayList d,Resources resLocal) {

		/********** Take passed values **********/
		activity = a;
		data=d;
		res = resLocal;

		/***********  Layout inflator to call external xml layout () ***********/
		inflater = ( LayoutInflater )activity.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	/******** What is the size of Passed Arraylist Size ************/
	public int getCount() {

		if(data.size()<=0)
			return 1;
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder{

		public TextView nameText;
		public TextView descriptionText;
		public ToggleButton statusToggle;
		public ImageView smsImage;
		public ImageView callImage;

	}

	/****** Depends upon data size called for each row , Create each ListView row *****/
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if(convertView==null){

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.rule_list_item, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.nameText = (TextView) vi.findViewById(R.id.list_textView_name);
			holder.descriptionText =(TextView) vi.findViewById(R.id.list_textView_description);
			holder.statusToggle = (ToggleButton) vi.findViewById(R.id.list_toggleButton_status);
			holder.smsImage = (ImageView) vi.findViewById(R.id.list_sms_image);
			holder.callImage = (ImageView) vi.findViewById(R.id.list_call_image);

			/************  Set holder with LayoutInflater ************/
			vi.setTag( holder );
		}
		else 
			holder=(ViewHolder)vi.getTag();

		if(data.size()<=0)
		{
			holder.nameText.setText("No Data");

		}
		else
		{
			/***** Get each Model object from Arraylist ********/
			tempValues=null;
			tempValues = ( Rule ) data.get( position );

			/************  Set Model values in Holder elements ***********/

			holder.nameText.setText( tempValues.getName());
			holder.descriptionText.setText( tempValues.getDescription() );
			holder.statusToggle.setChecked((tempValues.getStatus() == 1) ? true : false);  

			/******** Set Item Click Listner for LayoutInflater for each row *******/

			vi.setOnClickListener(new OnItemClickListener( position ));
		}
		return vi;
	}

	@Override
	public void onClick(View v) {
		Log.v("RuleListViewAdapter", "Row button clicked");
	}

	/********* Called when Item click in ListView ************/
	private class OnItemClickListener implements OnClickListener{           
		private int mPosition;

		OnItemClickListener(int position){
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {


			Main sct = (Main)activity;

			/****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

			sct.onItemClick(mPosition);
		}               
	}   
}