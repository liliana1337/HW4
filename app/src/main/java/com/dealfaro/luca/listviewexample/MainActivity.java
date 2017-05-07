package com.dealfaro.luca.listviewexample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "lv-ex";
    private static String myurl="https://luca-ucsc-teaching-backend.appspot.com/hw4/get_news_sites";
    RequestQueue queue;

    private class ListElement {
        ListElement() {};
        public String newspaperLabel;
        public String articleLabel;
        public String articleURL;

        ListElement(String news, String art,String URL) {
            newspaperLabel = news;
            articleLabel = art;
            articleURL=URL;
        }
    }

    private ArrayList<ListElement> aList;

    private class MyAdapter extends ArrayAdapter<ListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            ListElement w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource,  newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            TextView newspaper = (TextView) newView.findViewById(R.id.itemTitle);
            TextView article = (TextView) newView.findViewById(R.id.itemSubtitle);
            newspaper.setText(w.newspaperLabel);
            article.setText(w.articleLabel);


            // Set a listener for the whole list item.
            //tag each List row with the url value
            newView.setTag(w.articleURL);
            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String passURL = v.getTag().toString();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, passURL, duration);
                   // toast.show();
                    Intent i = new Intent(MainActivity.this, ReaderActivity.class);
                    i.putExtra("URL", passURL);
                    startActivity(i);
                }
            });

            return newView;
        }
    }

    private MyAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        aList = new ArrayList<ListElement>();
        aa = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
        this.processList();

    }


    public void clickRefresh (View v) {
       processList();
}
 public void processList() {
     //Log.i(LOG_TAG, "Requested a refresh of the list");
     JsonObjectRequest jsObjRequest = new JsonObjectRequest
             (Request.Method.GET, myurl, null, new Response.Listener<JSONObject>() {

                 @Override
                 public void onResponse(JSONObject response) {
                     Log.d(LOG_TAG, "Received: " + response.toString());
                     try {
                         JSONArray news = response.getJSONArray("news_sites");
                         aList.clear();
                         for (int i = 0; i < news.length(); i++) {
                             String url = news.getJSONObject(i).getString("url");
                             String title = news.getJSONObject(i).getString("title");
                             String subtitle = news.getJSONObject(i).getString("subtitle");
                             if (!url.equals("null") && !url.isEmpty() && !title.equals("null") && !title.isEmpty()) {
                                 aList.add(new ListElement(
                                         title, subtitle, url
                                 ));

                             }
                         }
                         aa.notifyDataSetChanged();


                     } catch (Exception e) {
                         e.printStackTrace();
                         Toast.makeText(getApplicationContext(),
                                 "Error: " + e.getMessage(),
                                 Toast.LENGTH_LONG).show();
                     }
                 }
             }, new Response.ErrorListener() {

                 @Override
                 public void onErrorResponse(VolleyError error) {
                     // TODO Auto-generated method stub
                     Log.d(LOG_TAG, error.toString());
                 }
             });
     queue.add(jsObjRequest);


 }



}
