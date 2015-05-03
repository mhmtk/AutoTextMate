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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 2, 2015
 *  
 * Inspired by http://androidexample.com/How_To_Create_A_Custom_Listview_-_Android_Example/index.php?view=article_discription&aid=67&aaid=92
 */

public class RuleListViewAdapter extends BaseAdapter { //implements OnClickListener {

	private String logTag = "RuleListViewAdapter";

	/*********** Declare Used Variables *********/
	private Activity activity;
	@SuppressWarnings("rawtypes")
	private ArrayList data;
	private static LayoutInflater inflater=null;
	public Resources res;
	Rule tempValue=null;
	//	int i=0;

	/*************  RuleListViewAdapter Constructor *****************/
	public RuleListViewAdapter(Activity a, @SuppressWarnings("rawtypes") ArrayList d,Resources resLocal) {

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
			//No data in the given array.
		}
		else
		{
			/***** Get each Model object from Arraylist ********/
			tempValue=null;
			tempValue = ( Rule ) data.get( position );

			/************  Set Model values in Holder elements ***********/

			holder.nameText.setText( tempValue.getName());
			holder.descriptionText.setText( tempValue.getDescription() );
			holder.statusToggle.setChecked((tempValue.getStatus() == 1) ? true : false);  

			//Set onClick for each row, and their respective ToggleButton
			vi.setOnClickListener(new OnItemClickListener(position));
			holder.statusToggle.setOnCheckedChangeListener(new onItemToggleChangedListener(tempValue.getName()));

			//			vi.setOnTouchListener(new onItemTouchListener(position));

			// On touch listener to detect swipe event
			vi.setOnTouchListener(new View.OnTouchListener() {

				float downX = 0, downY = 0;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					final int MIN_X_DISTANCE = 50;
					final int MAX_Y_DISTANCE = 20;
					switch (event.getAction())  {
					case MotionEvent.ACTION_DOWN: {
						downX = event.getX();
						downY = event.getY();
						Log.i(logTag, "Action down detected. DownX = " + downX + ", downY = " + downY);
						return false; // allow other events like Click to be processed
					}
					case MotionEvent.ACTION_UP: {
						float deltaX = event.getX() - downX;
						float deltaY = event.getY() - downY;
						Log.i(logTag, "Action up detected. upX = " + event.getX() + ", upY = " + event.getY() +" dx = " + deltaX + ", dy = " + deltaY);
						if (deltaX > 0 && deltaX > MIN_X_DISTANCE && Math.abs(deltaY) < MAX_Y_DISTANCE) {
							Log.i(logTag, "Swipe right detected.");
							//TODO Swipe right action
							return true;
						}
						return false;
					}
//					case MotionEvent.ACTION_MOVE: {
//						Log.i(logTag, "Action move detected");
//						upX = event.getX();
//						upY = event.getY();
//						float deltaX = downX - upX;
//						float deltaY = downY - upY;
//
//						// horizontal swipe detection
//						if (Math.abs(deltaX) > MIN_DISTANCE) {
//							// left or right
//							if (deltaX < 0) {
//								Log.i(logTag, "Swipe Left to Right");
//								return true;
//							}
//							if (deltaX > 0) {
//								Log.i(logTag, "Swipe Right to Left");
//								return true;
//							}
//						} else {
//							// vertical swipe detection
//							if (Math.abs(deltaY) > MIN_DISTANCE) {
//								// top or down
//								if (deltaY < 0) {
//									Log.i(logTag, "Swipe Top to Bottom");
//									return false;
//								}
//								if (deltaY > 0) {
//									Log.i(logTag, "Swipe Bottom to Top");
//									return false;
//								}
//							} 
//						}
//						return true;
//					}
					
					}
					return false;
				}
			});
		}
		return vi;
	}

	//	@Override
	//	public void onClick(View v) {
	//		Log.v("RuleListViewAdapter", "Row button clicked");
	//	}

	//	/**
	//	 * 
	//	 * @author mehmetk
	//	 *
	//	 */
	//	private class onItemTouchListener implements View.OnTouchListener {
	//		private int mPosition;
	//		
	//		//implement constructor to enable passing the position
	//		onItemTouchListener(int position){
	//			mPosition = position;
	//		}
	//
	//		@Override
	//		public boolean onTouch(View v, MotionEvent event){
	//
	//
	//			return true;
	//		}
	//	}

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
	 * OnItemClickListener class for the usage of each row of the list. Will call
	 * the onItemClick method of the Main activity, passing it the position of the row
	 * 
	 * @author Mehmet Kologlu
	 */
	private class OnItemClickListener implements OnClickListener{           
		private int mPosition;

		//implement constructor to enable passing the position
		OnItemClickListener(int position){
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			//get an instance of the main activity
			Main sct = (Main) activity;

			//call the onItemClick method of the Main activity
			sct.onItemClick(mPosition);
		}               
	}   
}