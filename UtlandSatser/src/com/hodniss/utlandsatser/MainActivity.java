package com.hodniss.utlandsatser;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupSpinner();
	}

	private void setupSpinner() {
		final MainActivity a = this;
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.continents_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
				if (index == 0) {
					ImageView imageView = (ImageView)findViewById(R.id.imageView1);
					imageView.setImageResource(R.drawable.five_continents);
					imageView.setVisibility(View.VISIBLE);
					
					ListView listView = (ListView) findViewById(R.id.listView1);
					listView.setVisibility(View.GONE);
					return;
				}
				
				ImageView imageView = (ImageView)findViewById(R.id.imageView1);
				imageView.setVisibility(View.GONE);
				
				if (!isOnline()) {
					Toast.makeText(a, R.string.no_network, Toast.LENGTH_LONG).show();
					return;
				}

				Toast.makeText(a, R.string.fetching, Toast.LENGTH_SHORT).show();
				
				String[] continents = getResources().getStringArray(R.array.continents_array);
				String selected = continents[index];

				String urlString = "http://hotell.difi.no/api/json/fad/reise/utland?verdensdel=" + selected;
                new FetchCountries(a).execute(urlString);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				//Do nothing
			}
		});
	}

	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
