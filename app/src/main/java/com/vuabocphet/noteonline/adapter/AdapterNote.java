package com.vuabocphet.noteonline.adapter;

import android.content.Context;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
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
    private static int FADE_DURATION=900;

    public AdapterNote(Home context, ArrayList<NoteModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public HoderNote onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
            view  = LayoutInflater.from(context).inflate(R.layout.item_gidview, viewGroup, false);


        return new HoderNote(view);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }



    public void onBindViewHolder(@NonNull HoderNote hoderNote, int i) {
        setScaleAnimation(hoderNote.itemView);
        NoteModel note = list.get(i);
        hoderNote.txtDate.setText(note.getDate());
        if (note.getNode().length()>125 && context.getPixel()<1080){
            hoderNote.txtNote.setText(note.getNode()+"...");
        }else {
            hoderNote.txtNote.setText(note.getNode());
        }
        hoderNote.txtSubject.setText(note.getSubject());

        hoderNote.i.setText((i + 1) + "/" + list.size());

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(10)
                .oval(false)
                .build();

        if (isInternetConnection() && !note.getUrl().isEmpty()) {
            Picasso.get().load(note.getUrl()).fit().transform(transformation)
                            .centerCrop().placeholder(R.drawable.nullimg)
                            .error(R.drawable.nullimg).into(hoderNote.imgNote);
        }
        if (isInternetConnection() && note.getUrl().isEmpty()|| !isInternetConnection()) {
            Picasso.get().load(new File(note.getImg())).fit().transform(transformation)
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
