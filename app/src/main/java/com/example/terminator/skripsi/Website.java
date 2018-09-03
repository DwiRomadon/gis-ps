package com.example.terminator.skripsi;

import android.content.Intent;
import android.support.annotation.AnimRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.thefinestartist.finestwebview.FinestWebView;

public class Website extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        Intent intent = getIntent();
        String url    = intent.getStringExtra("web");
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
        new FinestWebView.Builder(this).titleDefault("PS-Bandar Lampung")
                .show(url);
        finish();
    }
}
