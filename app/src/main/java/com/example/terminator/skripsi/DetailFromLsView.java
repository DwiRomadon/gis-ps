package com.example.terminator.skripsi;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import helper.AppController;
import server.Server;

public class DetailFromLsView extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    EditText nama, alamat, website, deskripsi, noTelp;
    TextView status, rate;
    ImageView imgView;
    private AppCompatRatingBar ratingBar;
    private Button btnRating;
    String id;

    private static final String TAG = DetailFromLsView.class.getSimpleName();

    private static final String url_detail  = Server.server + "details.php";
    private static final String url_rating  = Server.server + "rating.php";
    String tag_json_obj = "json_obj_req";

    private ProgressDialog pDialog;

    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;

    String namas;
    String Gambar1;
    String Gambar2;
    String Gambar3;
    String alamats;
    String deskripsis;
    String websites;
    String noTelps;
    String statuss;
    String rating;

    /*Deklarasi variable*/
    Button btn_navigasi;
    String goolgeMap = "com.google.android.apps.maps"; // identitas package aplikasi google masps android
    Uri gmmIntentUri;
    Intent mapIntent;
    String lat = "";
    String longi = "";
    /*Deklarasi variable*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_from_ls_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Photo Studio");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        imgView = (ImageView) findViewById(R.id.gambar1);
        nama = (EditText) findViewById(R.id.nama);
        alamat = (EditText) findViewById(R.id.alamat);
        website = (EditText) findViewById(R.id.website);
        deskripsi = (EditText) findViewById(R.id.deskripsi);
        noTelp    = (EditText) findViewById(R.id.no_telp);
        status    = (TextView) findViewById(R.id.status);
        rate     = (TextView) findViewById(R.id.tvRatting);
        ratingBar = (AppCompatRatingBar) findViewById(R.id.rt_bar);

        btnRating = (Button) findViewById(R.id.btnRating);

        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        id = getIntent().getStringExtra("id");

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRating(id, String.valueOf(ratingBar.getRating()));
                //Toast.makeText(getApplicationContext(), "Your Choose :" + ratingBar.getRating(), Toast.LENGTH_LONG).show();

            }
        });

        callDetailNews(id);
    }

    private void callDetailNews(final String id){

        pDialog.setMessage("Please Wait.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                hideDialog();
                try {
                    JSONObject obj = new JSONObject(response);

                     namas        = obj.getString("nama");
                     Gambar1      = obj.getString("gambar1");
                     Gambar2      = obj.getString("gambar2");
                     Gambar3      = obj.getString("gambar3");
                     alamats      = obj.getString("alamat");
                     deskripsis   = obj.getString("deskripsi");
                     websites     = obj.getString("website");
                     lat          = obj.getString("lat");
                     longi        = obj.getString("long");
                     noTelps      = obj.getString("no_telp");
                     statuss      = obj.getString("status");
                     rating       = obj.getString("rate");

                    DecimalFormat df = new DecimalFormat("#0.0");

                    String sRating;

                    if(rating.equals("")){
                        sRating = "Rate : 0";
                        ratingBar.setRating(0);
                    }else{
                        double dRating = Double.parseDouble(String.valueOf(rating));
                        sRating =  "Rate : " +String.valueOf(df.format(dRating));
                        ratingBar.setRating((float) dRating);
                    }

                    showImageSlide();
                    nama.setText(namas);
                    alamat.setText(alamats);
                    website.setText(websites);
                    deskripsi.setText(deskripsis);
                    noTelp.setText(noTelps);
                    rate.setText(sRating);

                    website.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(websites.equals("-")){
                                Toast.makeText(getApplicationContext(), "Photo studio tidak memiliki website", Toast.LENGTH_LONG).show();
                            }else {
                                Intent web = new Intent(DetailFromLsView.this, Website.class);
                                web.putExtra("web", websites);
                                startActivity(web);

                            }
                        }
                    });

                    noTelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                //callIntent.setData(Uri.parse("tel:xxxxxxx")); //This work
                                String string = noTelp.getText().toString().trim();
                                String number = "tel:" + string;//There work call
                                callIntent.setData(Uri.parse(number));
                                startActivity(callIntent);

                            } catch (ActivityNotFoundException activityException) {
                                Log.e("helloandroid dialing example", "Call failed");
                        }
                        }
                    });

                    if(statuss.equals("Aktif")){
                        status.setText(" ");
                    }else {
                        status.setText("Tidak Aktif");
                        status.setTextColor(DetailFromLsView.this.getResources().getColor(R.color.red));
                    }

                    routes();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                Toast.makeText(DetailFromLsView.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public void routes(){
        //routes
        // menyamakan variable pada layout activity_main.xml
        btn_navigasi    = (Button) findViewById(R.id.btnRoutes);

        // tombol untuk menjalankan navigasi goolge maps intents
        btn_navigasi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Buat Uri dari intent string. Gunakan hasilnya untuk membuat Intent.
                gmmIntentUri = Uri.parse("google.navigation:q=" + lat+","+longi);

                // Buat Uri dari intent gmmIntentUri. Set action => ACTION_VIEW
                mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Set package Google Maps untuk tujuan aplikasi yang di Intent yaitu google maps
                mapIntent.setPackage(goolgeMap);

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(DetailFromLsView.this, "Google Maps Belum Terinstal. Install Terlebih dahulu.",
                            Toast.LENGTH_LONG).show();
                }
            }

        });
    }


    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }


    public void showImageSlide(){
        String urlfoto = Server.baseURL+"admin/knowledge/gambar1/";
        String urlfoto2 = Server.baseURL+"admin/knowledge/gambar2/";
        String urlfoto3 = Server.baseURL+"admin/knowledge/gambar3/";
        //String urlnya =Gambar1;
        String foto1 = urlfoto+Gambar1;
        String foto2 = urlfoto2+Gambar2;
        String foto3 = urlfoto3+Gambar3;
        //String a = fotoNya;

        //if(urlnya.length()>1){
        //    Picasso.with(DetailFromLsView.this).load(a).into(imgView);
        //}else{
        //    Picasso.with(DetailFromLsView.this).load(R.drawable.ic_not_image_load).into(imgView);
        //}

        Hash_file_maps = new HashMap<String, String>();
        //Hash_file_maps.put("ha", foto1);
        //if(urlfoto.length() < 45 && urlfoto.length() < 45 && urlfoto.length() < 45){
            Hash_file_maps.put(namas+" Gambar 1", foto1);
            Hash_file_maps.put(namas+" Gambar 2", foto2);
            Hash_file_maps.put(namas+" Gambar 3", foto3);
        //}else {
        //    Hash_file_maps.put(namas, urlNotLoad);
        //}

        for(String name : Hash_file_maps.keySet()){

            TextSliderView textSliderView = new TextSliderView(DetailFromLsView.this);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(DetailFromLsView.this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(6000);
        sliderLayout.addOnPageChangeListener(DetailFromLsView.this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}


    @Override
    public void onBackPressed() {
        Intent a = new Intent(DetailFromLsView.this, ListTempat.class);
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


    private void sendRating(final String id, final String nilai){

        pDialog.setMessage("Please Wait.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_rating, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                hideDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray arr = obj.getJSONArray("results");
                    String pesan      = arr.getString(0);
                    if(pesan.equals("{\"msg\":\"Berhasil\"}")){
                        Intent a = new Intent(DetailFromLsView.this, DetailFromLsView.class);
                        a.putExtra("id", id);
                        startActivity(a);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                Toast.makeText(DetailFromLsView.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("nilai", nilai);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

}
