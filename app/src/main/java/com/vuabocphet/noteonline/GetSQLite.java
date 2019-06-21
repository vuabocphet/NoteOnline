package com.vuabocphet.noteonline;

import android.content.Context;
import android.database.Cursor;
import android.graphics.ColorSpace;
import android.util.Log;

import com.vuabocphet.noteonline.database.BaseData;
import com.vuabocphet.noteonline.database.BaseDataALL;
import com.vuabocphet.noteonline.model.NoteModel;

import java.util.ArrayList;
import java.util.Calendar;

public class GetSQLite {


    protected ArrayList<NoteModel> GetData(Context context){

        ArrayList<NoteModel> list=new ArrayList<>();

        list.clear();

        BaseData db=new BaseData(context);

        Cursor cursor=db.getData();

        if (cursor!=null && cursor.moveToFirst()){
            do {
                NoteModel model=new NoteModel(cursor.getInt(0)+"",cursor.getString(5),cursor.getString(1),cursor.getString(2),cursor.getString(3),"");
                list.add(0,model);
            }while (cursor.moveToNext());
            Log.e("SIZE",list.size()+"");
        }
        return list;
    }

    protected ArrayList<NoteModel> Data(Context context){

        ArrayList<NoteModel> list=new ArrayList<>();

        list.clear();

        BaseData db=new BaseData(context);

        Cursor cursor=db.getData();

        if (cursor!=null && cursor.moveToFirst()){
            do {
                NoteModel model=new NoteModel(cursor.getString(4),cursor.getString(5),cursor.getString(1),cursor.getString(2),cursor.getString(3),"");
                list.add(0,model);
            }while (cursor.moveToNext());
            Log.e("SIZE",list.size()+"");
        }
        return list;
    }


    protected ArrayList<NoteModel> DataALL(Context context){

        ArrayList<NoteModel> list=new ArrayList<>();

        list.clear();

        BaseDataALL db=new BaseDataALL(context);

        Cursor cursor=db.getData();

        if (cursor!=null && cursor.moveToFirst()){
            do {
                NoteModel model=new NoteModel(cursor.getString(4),cursor.getString(5),cursor.getString(1),cursor.getString(2),cursor.getString(3),"");
                list.add(0,model);
            }while (cursor.moveToNext());
            Log.e("SIZE",list.size()+"");
        }
        return list;
    }



}
