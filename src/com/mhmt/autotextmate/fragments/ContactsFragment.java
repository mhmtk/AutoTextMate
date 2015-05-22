package com.mhmt.autotextmate.fragments;

import com.mhmt.autotextmate.R;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 22, 2015
 *  
 **/
public class ContactsFragment extends Fragment implements LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

	public ContactsFragment() {}
	
//	// A UI Fragment must inflate its View
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        // Inflate the fragment layout
//        return inflater.inflate(R.layout.contact_list_fragment,
//            container, false);
//    }
    
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

}
