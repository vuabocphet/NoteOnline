package com.vuabocphet.noteonline.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vuabocphet.noteonline.Home;
import com.vuabocphet.noteonline.R;
import com.vuabocphet.noteonline.hodel.HoderNote;
import com.vuabocphet.noteonline.model.NoteModel;
import com.vuabocphet.noteonline.model.Uri;

import java.io.File;
import java.util.ArrayList;

public class AdapterGid extends BaseAdapter {
    private Home context;
    private ArrayList<NoteModel> list;
    private ArrayList<Uri> listimg;
    private CircularImageView imgNote;
    private TextView txtNote;
    private TextView ii;
    private TextView txtDate;
    private TextView txtSubject;
    private LinearLayout linearLayout;

    public AdapterGid(Home context, ArrayList<NoteModel> list, ArrayList<Uri> listimg) {
        this.context = context;
        this.list = list;
        this.listimg = listimg;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gidview, parent, false);

        NoteModel note = list.get(i);

        imgNote = (CircularImageView) convertView.findViewById(R.id.img_note);
        txtNote = (TextView) convertView.findViewById(R.id.txtNote);
        txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        ii = (TextView) convertView.findViewById(R.id.i);
        txtSubject = (TextView) convertView.findViewById(R.id.subject);
        linearLayout = (LinearLayout) convertView.findViewById(R.id.layout3);
        txtSubject.setTypeface(Typeface.createFromAsset(context.getAssets(), "caviar.ttf"));
        txtDate.setText(note.getDate());
        txtSubject.setText(note.getSubject());
        txtNote.setText(note.getNode());
        ii.setText((i + 1) + "/" + list.size());

        if (!listimg.isEmpty()) {
            for (Uri url : listimg) {
                if (url.getId().equals(note.getId())) {
                    Picasso.get().load(url.getUrl()).resize(100, 100)
                            .centerCrop().placeholder(R.drawable.nullimg)
                            .error(R.drawable.nullimg).into(imgNote);
                }
            }

        } else {
            Picasso.get().load(new File(note.getImg())).resize(100, 100)
                    .centerCrop().placeholder(R.drawable.nullimg)
                    .error(R.drawable.nullimg).into(imgNote);
        }
        return convertView;

    }
}
