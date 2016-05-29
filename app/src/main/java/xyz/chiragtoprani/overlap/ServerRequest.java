package xyz.chiragtoprani.overlap;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class ServerRequest {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";


    public ServerRequest() {

    }

    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {

        Log.v("IO", "Trying to get JSON");


        try {

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            Log.e("IO", "about to get the information");

            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.e("IO", "Finished");

            HttpEntity httpEntity = httpResponse.getEntity();

            is = httpEntity.getContent();


        }catch (ConnectTimeoutException e){

            Log.e("IO", "Connection timed out");
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }


        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        return jObj;

    }
    JSONObject jobj;
    public JSONObject getJSON(String url, List<NameValuePair> params) {

        Params param = new Params(url,params);
        Request myTask = new Request();
        Log.v("IO", "Sending Request");
        try{
            jobj= myTask.execute(param).get(500, TimeUnit.MILLISECONDS);
        }catch (InterruptedException e) {
            Log.e("IO", "IO Exception");

            e.printStackTrace();
        }catch (ExecutionException e){
            Log.e("IO", "IO Exception");

            e.printStackTrace();
        } catch (TimeoutException e){
            return null;
        }
        return jobj;
    }


    private static class Params {
        String url;
        List<NameValuePair> params;


        Params(String url, List<NameValuePair> params) {
            this.url = url;
            this.params = params;

        }
    }

    private class Request extends AsyncTask<Params, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(Params... args) {

            ServerRequest request = new ServerRequest();
            JSONObject json = request.getJSONFromUrl(args[0].url,args[0].params);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            super.onPostExecute(json);

        }

    }
}