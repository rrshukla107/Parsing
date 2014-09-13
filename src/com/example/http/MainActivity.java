package com.example.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText name;
	Button go;
	TextView data;
	Button parse;
	String response; // clear this in async

	String nameOfMovie = null;
	String finalUrl = null;

	// static flag = 0;
	// ---------------------------------------------------------------------------------------------------------------
	//ASYNC TASK TO GET THE RAW JSON DATA
	private class NetworkActivity extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			 response = "";
			// String url = urls[0];
			for (String url : urls) { // take heed
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;

					}
					
					if (buffer != null) {
						buffer = null;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return response;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			data.setText("");
		}

		@Override
		protected void onPostExecute(String result) {

			// super.onPostExecute(result);
			data.setText(result);

		}

	}

	// ---------------------------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		name = (EditText) findViewById(R.id.editTextName);
		go = (Button) findViewById(R.id.buttonGo);
		data = (TextView) findViewById(R.id.textViewData);
		parse = (Button) findViewById(R.id.buttonP);

		go.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				nameOfMovie = name.getText().toString();
				nameOfMovie = nameOfMovie.replace(" ", "+");// BUG 1 space to +
				Log.e("" + name, "" + nameOfMovie);
				// check for network connection

				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected()) {
					// fetch data
					finalUrl = "http://www.omdbapi.com/?t=" + nameOfMovie;
					new NetworkActivity().execute(finalUrl);

				} else {
					// display error
					Log.i("NOCONNECTION", "Error in connectivity..!");
					data.setText("No network connection..!");
				}

			}
		});

		parse.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intentToParse = new Intent(
						"com.example.http.PARSEACTIVITY");
				intentToParse.putExtra("data", response);
				startActivity(intentToParse);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		nameOfMovie = "";
		data.setText("");
		// finish();

		// onCreate(null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		nameOfMovie = "";
		data.setText("");
	}

}
