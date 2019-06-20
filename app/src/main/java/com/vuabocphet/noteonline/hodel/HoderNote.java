package com.vuabocphet.noteonline.hodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.vuabocphet.noteonline.R;

public class HoderNote extends RecyclerView.ViewHolder {
    public CircularImageView imgNote;
    public TextView txtNote,i;
    public TextView txtDate;
    public TextView txtSubject;
    public LinearLayout linearLayout;


    public HoderNote(@NonNull View itemView) {
        super(itemView);
        imgNote = (CircularImageView) itemView.findViewById(R.id.img_note);
        txtNote = (TextView) itemView.findViewById(R.id.txtNote);
        txtDate = (TextView) itemView.findViewById(R.id.txtDate);
        i = (TextView) itemView.findViewById(R.id.i);
        txtSubject = (TextView) itemView.findViewById(R.id.subject);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.layout3);
    }
}
