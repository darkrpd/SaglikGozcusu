package com.gencgirisimciler.saglikgozcusu.saglikgozcusu.android;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarSearchHandler;
import com.blunderer.materialdesignlibrary.listeners.OnSearchListener;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.HikayeActivity;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.MainActivity;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.NavigationDrawerClasses.NavDrawerListAdapter;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.R;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.listview.MaddeListAdapter;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.listview.MaddeListesi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultsActivity extends com.blunderer.materialdesignlibrary.activities.Activity {

	String outputPath;
	TextView tv;
	public static List<MaddeListesi> mMaddeListesi =new ArrayList<MaddeListesi>();
	public static MaddeListAdapter theAdapter;
	@Override
	protected boolean enableActionBarShadow() {
		return false;
	}

	@Override
	protected ActionBarHandler getActionBarHandler() {
		return new ActionBarSearchHandler(this, new OnSearchListener() {

			@Override
			public void onSearched(String text) {
				Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
				intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle());
				// catch event that there's no activity to handle intent
				if (intent.resolveActivity(getPackageManager()) != null) {
					startActivity(intent);
				}
				Toast.makeText(getApplicationContext(),
						"Searching \"" + text + "\"", Toast.LENGTH_SHORT).show();
			}

		});
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_results);

//		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
//
//		GeneralClasses.StatusBar statusBar = new GeneralClasses.StatusBar(this);
//		statusBar.setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.colorPrimaryDark));

		mMaddeListesi.clear();
		final ListView maddeListView = (ListView) findViewById(R.id.resultListView);
		theAdapter=new MaddeListAdapter(this, mMaddeListesi);
		assert maddeListView != null;
		maddeListView.setAdapter(theAdapter);

		maddeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Toast.makeText(ResultsActivity.this, theAdapter.getItemName(position), Toast.LENGTH_SHORT).show();
				Intent HikayeActivityGidenIntent = new Intent(ResultsActivity.this, HikayeActivity.class);
				HikayeActivityGidenIntent.putExtra("MaddeAdi",theAdapter.getItemName(position));
				if(resultListviewBooleanArraylist.get(position))
					HikayeActivityGidenIntent.putExtra("MaddeIndex",position);
				else
//					HikayeActivityGidenIntent.putExtra("MaddeIndex",-1);
					HikayeActivityGidenIntent.putExtra("MaddeIndex",position);

				startActivity(HikayeActivityGidenIntent);
			}
		});

		tv = (TextView)findViewById(R.id.hikayeActivitySonucTextView);
//		setContentView(tv);
		
		String imageUrl = "unknown";
		
		Bundle extras = getIntent().getExtras();
		if( extras != null) {
			imageUrl = extras.getString("IMAGE_PATH" );
			outputPath = extras.getString( "RESULT_PATH" );
		}
		
		// Starting recognition process
		new AsyncProcessTask(this).execute(imageUrl, outputPath);
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_results;
	}

	public void updateResults(Boolean success) {
		if (!success)
			return;
		try {
			StringBuffer contents = new StringBuffer();

			FileInputStream fis = openFileInput(outputPath);
			try {
				Reader reader = new InputStreamReader(fis, "UTF-8");
				BufferedReader bufReader = new BufferedReader(reader);
				String text = null;
				while ((text = bufReader.readLine()) != null) {
					contents.append(text).append(System.getProperty("line.separator"));
				}
			} finally {
				fis.close();
			}

			displayMessage(contents.toString());
		} catch (Exception e) {
			displayMessage("Error: " + e.getMessage());
		}
	}
	
	public void displayMessage( String text )
	{
		tv.post( new MessagePoster( text ) );
	}

	class MessagePoster implements Runnable {
		public MessagePoster( String message )
		{
			_message = message;
		}

		public void run() {
//			tv.append( _message + "\n" );

			maddelerListedeVarMi(_message);

			for(int i = 0; i< resultListviewTextValueArraylist.size(); i++)
			mMaddeListesi.add((new MaddeListesi( resultListviewTextValueArraylist.get(i), resultListviewBooleanArraylist.get(i))));

			theAdapter.notifyDataSetChanged();
		}

		private final String _message;
	}

	ArrayList<String> resultListviewTextValueArraylist = new ArrayList<>();
	ArrayList<Boolean> resultListviewBooleanArraylist = new ArrayList<>();

	public void maddelerListedeVarMi (String message){

		for (int i= 0 ; i<NavDrawerListAdapter.tikliMiArray.size();i++)
			if(NavDrawerListAdapter.tikliMiArray.get(i))
			{
				if (containsIgnoreCase(message,MainActivity.mMaddeListesi.get(i))) {
					resultListviewTextValueArraylist.add(MainActivity.mMaddeListesi.get(i));
					resultListviewBooleanArraylist.add(true);
				}
				else
				{
					resultListviewTextValueArraylist.add(MainActivity.mMaddeListesi.get(i));
					resultListviewBooleanArraylist.add(false);
				}
			}

	/*	for(String s : MainActivity.mMaddeListesi) {
			if (containsIgnoreCase(message,s)) {
				resultListviewTextValueArraylist.add(s);
				resultListviewBooleanArraylist.add(true);
			}
			else
			{
				resultListviewTextValueArraylist.add(s);
				resultListviewBooleanArraylist.add(false);
			}
		}*/
	}

	/**
	 * Kaynak String içerisinde aratılan kelimenin case-insensitive özelliği ile arama yapan methoddur.
	 *
	 * @param src Aratılacak Kelime
	 * @param what Kaynak İfade
     * @return Kelimenin Kaynak içerisinde olup olmadığını döndüren boolean ifadedir
     */
	public static boolean containsIgnoreCase(String src, String what) {
		final int length = what.length();
		if (length == 0)
			return true; // Empty string is contained

		final char firstLo = Character.toLowerCase(what.charAt(0));
		final char firstUp = Character.toUpperCase(what.charAt(0));

		for (int i = src.length() - length; i >= 0; i--) {
			// Quick check before calling the more expensive regionMatches() method:
			final char ch = src.charAt(i);
			if (ch != firstLo && ch != firstUp)
				continue;

			if (src.regionMatches(true, i, what, 0, length))
				return true;
		}

		return false;
	}
}
