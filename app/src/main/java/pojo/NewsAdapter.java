package pojo;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.terminator.skripsi.ListTempat;
import com.example.terminator.skripsi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import helper.AppController;
import server.Server;

/**
 * Created by Kuncoro on 29/02/2016.
 */
public class NewsAdapter extends ArrayAdapter<ListModel> {
    private Context context;


    public NewsAdapter(Context context, ArrayList<ListModel> newsItems) {
        super(context, 0, newsItems);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(R.layout.list_rows, parent, false);

        ListModel news = getItem(position);

        ImageView gambar = (ImageView) convertView.findViewById(R.id.gambar);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        TextView id = (TextView) convertView.findViewById(R.id.id);

        String urlfoto = Server.baseURL+"admin/knowledge/gambar1/";
        String urlnya =news.getGambar1();
        String fotoNya = urlfoto+news.getGambar1();
        String a = fotoNya;



        if(urlnya.length()>1){
            Picasso.with(this.context).load(a).into(gambar);
        }else{
            Picasso.with(this.context).load(R.drawable.notload).into(gambar);
        }

        nama.setText(news.getNama());
        alamat.setText(news.getAlamat());
        id.setText(news.getId());
        return convertView;
    }
}
