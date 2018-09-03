package com.example.terminator.skripsi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.AppController;
import pojo.Adapter;
import pojo.ListModel;
import pojo.NewsAdapter;
import server.Server;

public class ListTempat extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener{
    ListView list;
    //SwipeRefreshLayout swipe;
    ArrayList<ListModel> newsList = new ArrayList<ListModel>();

    private static final String TAG = MainActivity.class.getSimpleName();

    private static String url_list   = Server.server + "listTempat.php?offset=";

    public static final String url_cari = Server.server +"search.php";

    private int offSet = 0;

    int no;

    Adapter adapter;

    private ProgressDialog pDialog;

    // Listview Adapter
    ArrayAdapter<String> adapter1;

    public static final String TAG_NO       = "no";
    public static final String TAG_ID       = "id";
    public static final String TAG_Nama    = "nama";
    public static final String TAG_alamat      = "alamat";
    public static final String TAG_GAMBAR   = "gambar";
    public static final String TAG_RATING   = "rate";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "value";

    String tag_json_obj = "json_obj_req";

    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tempat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Daftar Photo Studio");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list_news);
        newsList.clear();

        adapter = new Adapter(ListTempat.this, newsList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ListTempat.this, DetailFromLsView.class);
                intent.putExtra(TAG_ID, newsList.get(position).getId());
                startActivity(intent);
                finish();
            }
        });


        //swipe.setOnRefreshListener(this);

        /*swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           newsList.clear();
                           adapter.notifyDataSetChanged();

                       }
                   }
        );*/
        callNews(0);

        /*list.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {

                    swipe.setRefreshing(true);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                            callNews(offSet);
                        }
                    };

                    handler.postDelayed(runnable, 3000);
                }
            }

        });*/

    }

    @Override
    public void onRefresh() {
        //newsList.clear();
        //adapter.notifyDataSetChanged();
        //callNews(0);
    }

    private void callNews(int page){

        //swipe.setRefreshing(true);
        pDialog.setMessage("Please Wait.....");
        showDialog();

        // Creating volley request obj
        JsonArrayRequest arrReq = new JsonArrayRequest(url_list + page,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        hideDialog();
                        if (response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    ListModel news = new ListModel();

                                    no = obj.getInt(TAG_NO);

                                    news.setId(obj.getString(TAG_ID));
                                    news.setNama(obj.getString(TAG_Nama));

                                    //if (obj.getString(TAG_GAMBAR) != "") {
                                        news.setGambar1(obj.getString(TAG_GAMBAR));
                                    //}

                                    news.setAlamat(obj.getString(TAG_alamat));

                                    String rate = String.valueOf(obj.getString(TAG_RATING));
                                    if(rate.equals("")){
                                        news.setRating(0);
                                        news.setRateText("Rate : 0");
                                    }else {
                                        double pRate = Double.parseDouble(rate);
                                        news.setRating(pRate);
                                        DecimalFormat df = new DecimalFormat("#0.0");
                                        String sRating = String.valueOf(df.format(pRate));
                                        news.setRateText("Rate : "+sRating);
                                    }

                                    // adding news to news array
                                    newsList.add(news);

                                    if (no > offSet)
                                        offSet = no;

                                    Log.d(TAG, "offSet " + offSet);

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }
                  //      swipe.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //swipe.setRefreshing(false);
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(ListTempat.this, MainActivity.class);
        startActivity(a);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        cariData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Search....");
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private void cariData(final String keyword) {

        pDialog.setMessage("Loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_cari, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);

                    int value = jObj.getInt(TAG_VALUE);

                    if (value == 1) {
                        newsList.clear();
                        adapter.notifyDataSetChanged();

                        String getObject = jObj.getString(TAG_RESULTS);
                        JSONArray jsonArray = new JSONArray(getObject);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            ListModel data = new ListModel();

                            data.setId(obj.getString(TAG_ID));
                            data.setNama(obj.getString("nama"));
                            data.setAlamat(obj.getString("alamat"));
                            data.setGambar1(obj.getString(TAG_GAMBAR));
                            String rate = String.valueOf(obj.getString(TAG_RATING));
                            if(rate.equals("")){
                                data.setRating(0);
                                data.setRateText("Rate : 0");
                            }else {
                                double pRate = Double.parseDouble(rate);
                                data.setRating(pRate);
                                DecimalFormat df = new DecimalFormat("#0.0");
                                String sRating = String.valueOf(df.format(pRate));
                                data.setRateText("Rate : "+sRating);
                            }
                            newsList.add(data);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("keyword", keyword);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
