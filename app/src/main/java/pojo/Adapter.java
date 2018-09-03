package pojo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.terminator.skripsi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import server.Server;

/**
 * Created by KUNCORO on 09/08/2017.
 */

public class Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ListModel> item;

    public Adapter(Activity activity, List<ListModel> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_rows, null);


        CircleImageView gambar = (CircleImageView) convertView.findViewById(R.id.gambar);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        AppCompatRatingBar ratingBar = (AppCompatRatingBar) convertView.findViewById(R.id.rt_bar);
        TextView rateText = (TextView) convertView.findViewById(R.id.textRating);

        String urlfoto = Server.baseURL+"admin/knowledge/gambar1/";
        String urlnya =item.get(position).getGambar1();
        String fotoNya = urlfoto+item.get(position).getGambar1();
        String a = fotoNya;

        if(urlnya.length()>1){
            Picasso.with(this.activity).load(a).into(gambar);
        }else{
            Picasso.with(this.activity).load(R.drawable.notload).into(gambar);
        }

        nama.setText(item.get(position).getNama());
        alamat.setText(item.get(position).getAlamat());
        id.setText(item.get(position).getId());
        ratingBar.setRating((float) item.get(position).getRating());
        rateText.setText(item.get(position).getRateText());

        return convertView;
    }
}