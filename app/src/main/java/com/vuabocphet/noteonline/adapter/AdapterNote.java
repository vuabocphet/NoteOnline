package com.vuabocphet.noteonline.adapter;

import android.content.Context;

import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.vuabocphet.noteonline.Home;
import com.vuabocphet.noteonline.R;
import com.vuabocphet.noteonline.hodel.HoderNote;
import com.vuabocphet.noteonline.model.NoteModel;
import com.vuabocphet.noteonline.model.Uri;

import java.io.File;

import java.util.ArrayList;


public class AdapterNote extends RecyclerView.Adapter<HoderNote> {

    private Home context;
    private ArrayList<NoteModel> list;
    private ArrayList<Uri> listimg;

    public AdapterNote(Home context, ArrayList<NoteModel> list, ArrayList<Uri> listimg) {
        this.context = context;
        this.list = list;
        this.listimg = listimg;
    }

    @NonNull
    @Override
    public HoderNote onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (context.getPixel()<1080){
            view  = LayoutInflater.from(context).inflate(R.layout.item_gidview, viewGroup, false);
        }else {
            view  = LayoutInflater.from(context).inflate(R.layout.item_gidview_a, viewGroup, false);
        }

        return new HoderNote(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoderNote hoderNote, int i) {

        NoteModel note = list.get(i);
        hoderNote.txtSubject.setTypeface(Typeface.createFromAsset(context.getAssets(), "caviar.ttf"));
        hoderNote.txtDate.setText(note.getDate());
        if (note.getNode().length()>125 && context.getPixel()<1080){
            hoderNote.txtNote.setText(note.getNode()+"...");
        }else {
            hoderNote.txtNote.setText(note.getNode());
        }
        hoderNote.txtSubject.setText(note.getSubject());

        hoderNote.i.setText((i + 1) + "/" + list.size());

        if (!listimg.isEmpty()) {
            for (Uri url : listimg) {
                if (url.getId().equals(note.getId())) {
                    Picasso.get().load(url.getUrl()).resize(100, 100)
                            .centerCrop().placeholder(R.drawable.nullimg)
                            .error(R.drawable.nullimg).into(hoderNote.imgNote);
                }
            }

        } else {
            Picasso.get().load(new File(note.getImg())).resize(100, 100)
                    .centerCrop().placeholder(R.drawable.nullimg)
                    .error(R.drawable.nullimg).into(hoderNote.imgNote);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }
    }
}
