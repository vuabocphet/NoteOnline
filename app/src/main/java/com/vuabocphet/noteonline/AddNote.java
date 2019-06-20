package com.vuabocphet.noteonline;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.vuabocphet.noteonline.database.BaseData;
import com.vuabocphet.noteonline.database.BaseDataALL;
import com.vuabocphet.noteonline.model.NoteModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class AddNote extends AppCompatActivity {
    IsRunningactivity isRunningActivity = new IsRunningactivity();
    private LinearLayout layout;
    private TextView date;
    private TextView save;
    private EditText note;
    private LinearLayout layout1;
    private ImageView image;
    private ImageView align;
    private Boolean isalign = true, isSave = true;
    private Animation zoom;
    private String path = "";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private BaseData baseData;
    private BaseDataALL baseDataALL;
    private long i = -1;
    private SharedPreferences sp;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private Typeface font;
    private EditText subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Bungee.fade(this);
        Log.e("IS", isRunningActivity.isActivityRunning(Home.class, this) + "");
        mapped();
        click();

    }


    private void mapped() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://onlinestore-3ac1a.appspot.com");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        baseData = new BaseData(this);
        baseDataALL=new BaseDataALL(this);
        sharedPreferences = getSharedPreferences("NOTETEST", MODE_PRIVATE);
        sp = getSharedPreferences("ISRUNNING", MODE_PRIVATE);
        zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);
        font = Typeface.createFromAsset(getAssets(), "caviar.ttf");
        layout = (LinearLayout) findViewById(R.id.layout);
        date = (TextView) findViewById(R.id.date);
        save = (TextView) findViewById(R.id.save);
        note = (EditText) findViewById(R.id.note);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        image = (ImageView) findViewById(R.id.image);
        align = (ImageView) findViewById(R.id.align);
        subject = (EditText) findViewById(R.id.subject);
        date.setText(getDate());
        date.setTypeface(font);
        save.setTypeface(font);

    }

    private void click() {

        align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignFun();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.startAnimation(zoom);
                finish();
            }
        });


//note.setCompoundDrawablesWithIntrinsicBounds(null,null,null,);
        note.setEnabled(false);
        note.setAlpha(0.5f);
        subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==20){
                    subject.setError(getString(R.string.err));
                }
                Log.e("LEG",s.length()+"");
                if (s.length() == 0) {
                    note.setEnabled(false);
                    note.setAlpha(0.5f);
                } else {
                    note.setAlpha(1f);
                    note.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editor = sharedPreferences.edit();
        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                date.setText(getDate());
                String id = sp.getString("id", "");
                if (s.length() == 0) {
                    save.setTextColor(getResources().getColor(R.color.black));
                    save.setAlpha(0.5f);
                    isSave = false;
                    if (i != -1) {
                        baseData.delete((int) i);
                    }


                } else {
                    save.setTextColor(getResources().getColor(R.color.colorAccent));
                    save.setAlpha(1f);
                    isSave = true;

                    if (!isInternetConnection() || isInternetConnection() && id.equals("")) {
                        if (i == -1) {
                            i = baseData.insert(date.getText().toString().trim(), s.toString(), String.valueOf(path), Calendar.getInstance().getTimeInMillis() + "", subject.getText().toString().trim());
                            Log.e("I", i + "");
                        } else {
                            Log.e("INEW", i + "");
                            baseData.update(date.getText().toString().trim(), s.toString(), String.valueOf(path), (int) i, subject.getText().toString().trim());
                        }


                    }


                }
                if (isSave) {
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = sp.getString("id", "");
                            if (id.equals("")) {
                                Toast.makeText(AddNote.this, "Đã lưu trữ offline", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (!isInternetConnection()) {
                                Toast.makeText(AddNote.this, "Đã lưu trữ offline", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            NoteModel model = new NoteModel("" + Calendar.getInstance().getTimeInMillis(), subject.getText().toString().trim(), date.getText().toString().trim(), note.getText().toString(), String.valueOf(path));
                            if (isInternetConnection() && !id.equals("")) {
                                mDatabase.child(id).child("GhiChu").child(model.getId()).setValue(model);
                                if (!path.equals("")) {
                                    uploadImage(path, model.getId(), id);
                                }
                                finish();

                            }
                        }
                    });
                } else {
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("CLICK", "NULL");
                        }
                    });
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.startAnimation(zoom);
                permission();
            }
        });


    }


    private void alignFun() {
        align.startAnimation(zoom);
        if (isalign) {
            note.setGravity(Gravity.CENTER_HORIZONTAL);
            isalign = false;

        } else {
            note.setGravity(Gravity.TOP | Gravity.LEFT);
            isalign = true;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("A", "HELLO");

    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

    private void permission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            img();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(AddNote.this, "Cần cấp quyền\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void img() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            Picasso.get().load(uri).resize(90, 90).centerCrop().into(image);
            path = String.valueOf(getRealPathFromURI(uri));
            Toast.makeText(this, "Đã chọn ảnh" + uri, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }


    private void uploadImage(String file, final String id, final String ID) {


        Calendar calendar = Calendar.getInstance();
        final StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + "");

        mountainsRef.putFile(Uri.fromFile(new File(file)))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;

                        Uri downloadUrl = urlTask.getResult();
                        com.vuabocphet.noteonline.model.Uri uri = new com.vuabocphet.noteonline.model.Uri(id, String.valueOf(downloadUrl));
                        mDatabase.child(ID).child("IMG").push().setValue(uri);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e("Failed", e.getMessage());
                        return;
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });


    }


}
