package rmg.pdrtracker.login.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    //static String json = "";
    private String json = "";
    static List<NameValuePair> params;
    private boolean isFinished = false;

    // constructor
    public JSONParser() {

    }

    public String getJSONFromUrl(String url, List<NameValuePair> params) {
        this.params = params;
        isFinished = false;
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[] {url});
        // return JSON String
        int i = 0;
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // Done
        }

        return json;

    }


    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            BufferedReader reader = null;

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                URL url = new URL(urls[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();
                // give it 15 seconds to respond
                urlConnection.setReadTimeout(15*1000);
                urlConnection.connect();
                try{
                    is = urlConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        response += line;
                        System.out.println("response ---------------------------------------------------------------------------------------------------------------------------------------------->"+response);

                    }
                    System.out.println("response1 ---------------------------------------------------------------------------------------------------------------------------------------------->"+response);

                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }finally {
                    System.out.println("response2 ---------------------------------------------------------------------------------------------------------------------------------------------->"+response);

                   // urlConnection.disconnect();
                    System.out.println("response3 ---------------------------------------------------------------------------------------------------------------------------------------------->"+response);

                    is.close();
                    urlConnection.disconnect();
                    System.out.println("response4 ---------------------------------------------------------------------------------------------------------------------------------------------->"+response);

                    return response;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("response ---------------------------------------------------------------------------------------------------------------------------------------------->"+response);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            json = result;
            // try parse the string to a JSON object
           // try {
               // jObj = new JSONObject(json);
                System.out.println("response on post execute ---------------------------------------------------------------------------------------------------------------------------------------------->"+json.toString());

                setIsFinished(result);
           // } catch (JSONException e) {
           //     Log.e("JSON Parser", "Error parsing data " + e.toString());
           //     isFinished = true;
           // }
           // Log.e("JSON", json);

        }
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private void setIsFinished(String result){
        json = result;
        System.out.println("response is finished ---------------------------------------------------------------------------------------------------------------------------------------------->"+json);
        isFinished = true;

    }

}