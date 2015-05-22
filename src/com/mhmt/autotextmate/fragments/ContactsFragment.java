package com.mhmt.autotextmate.fragments;

import com.mhmt.autotextmate.R;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 22, 2015
 *  
 **/
public class ContactsFragment extends ListFragment implements LoaderCallbacks<Cursor> {

	private final static String[] FROM_COLUMNS = { ContactsContract.Data.DISPLAY_NAME_PRIMARY};
	private final static int[] TO_IDS = {R.id.contactFilter_textView_contactName};

	// Define global mutable variables
	// Define a ListView object
	ListView mContactsList;
	// Define variables for the contact the user selects
	// The contact's _ID value
	long mContactId;
	// The contact's LOOKUP_KEY
	String mContactKey;
	// A content URI for the selected contact
	Uri mContactUri;
	// An adapter that binds the result Cursor to the ListView
	private SimpleCursorAdapter mCursorAdapter;

	static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
		ContactsContract.Data.DISPLAY_NAME_PRIMARY};
    
	public ContactsFragment() {}

	// A UI Fragment must inflate its View
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Gets a CursorAdapter
		mCursorAdapter = new SimpleCursorAdapter(
				getActivity(),
				R.layout.contact_filter_item,
				null,
				FROM_COLUMNS, TO_IDS,
				0);
		// Sets the adapter for the ListView
		setListAdapter(mCursorAdapter);

		getLoaderManager().initLoader(0,null,this);

		// Inflate the fragment layout
		return inflater.inflate(R.layout.fragment_contact_filter,
				container, false);
	}

	// Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(getActivity(), ContactsContract.Data.CONTENT_URI,
                PROJECTION, null, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mCursorAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mCursorAdapter.swapCursor(null);
    }

    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}