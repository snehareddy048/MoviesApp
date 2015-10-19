package com.example.snehaanand.moviesapp.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by snehaanandyeluguri on 10/18/15.
 */
public class DownloadWebPageTask extends android.os.AsyncTask<String, Void, JsonArray> {
    public static final String RESULTS = "results";
    public static final String URL_GET = "GET";

        @Override
        protected JsonArray doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private JsonArray downloadUrl(String myurl) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod(URL_GET);
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();

                // Convert the InputStream into a string
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line;
                String result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                return parseResult(result);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        private JsonArray parseResult(String jsonElements) {
            JsonElement jsonElement = new JsonParser().parse(jsonElements);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray(RESULTS);
            return jsonArray;
        }

}
