package com.hodniss.utlandsatser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class FetchCountries extends AsyncTask<String, Integer, String> {
	
	public MainActivity activity;
	ArrayList<Country> list = new ArrayList<Country>();
	
	public FetchCountries(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	protected String doInBackground(String... url) {		
		StringBuilder builder = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(url[0]);

	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        }
	      } else {
	        Toast.makeText(activity, R.string.server_error, Toast.LENGTH_LONG).show();
	      }
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
		return builder.toString();
	}
	
    @Override
    protected void onPostExecute(String result) {

    	if (result.length() == 0) {
    		Toast.makeText(activity, R.string.no_data, Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	try {
    		JSONObject json = (JSONObject) new JSONTokener(result).nextValue();
			JSONArray jArray = json.getJSONArray("entries");
			
			/* Parse through all entries and update array list */
			for (int i = 0; i < jArray.length(); i++) {
				String country = jArray.getJSONObject(i).getString("land");
				String city = jArray.getJSONObject(i).getString("by");
				if (city.length() > 0)
					 country = country + "(" + city + ")";
				int hotelRate = jArray.getJSONObject(i).getInt("makssatser natt");
				int dietRate = jArray.getJSONObject(i).getInt("kost");
				list.add(new Country(country,hotelRate,dietRate));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
		ArrayList<String> tmp = new ArrayList<String>();
    	
    	for (int i = 0; i < list.size(); i++) {
    		tmp.add(list.get(i).getName());
    	}
    	
    	String[] strList = new String[tmp.size()]; 
    	strList = tmp.toArray(strList);
    	
    	ListView lv = (ListView) activity.findViewById(R.id.listView1);
    	lv.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, strList));
    	lv.setOnItemClickListener(new OnItemClickListener() {
    		@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			showRates(list.get(position).getName(), list.get(position).getHotelRate(), list.get(position).getDietRate());
            }
          });
    	lv.setVisibility(View.VISIBLE);
    }
	public void showRates(String name, int hotelRate, int dietRate) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setTitle(name);
		alertDialog.setMessage("Hotell maxrate: NOK " + hotelRate + ",-\nDiett: NOK " + dietRate + ",-");
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
		    	dialog.dismiss();
		    } }); 
		alertDialog.show();
	}
}