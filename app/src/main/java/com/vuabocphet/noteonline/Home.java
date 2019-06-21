package com.vuabocphet.noteonline;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vuabocphet.noteonline.adapter.AdapterNote;
import com.vuabocphet.noteonline.database.BaseData;
import com.vuabocphet.noteonline.database.BaseDataALL;
import com.vuabocphet.noteonline.model.NoteModel;


import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class Home extends AppCompatActivity {


    private ImageView file;
    private ImageView menu;
    private RelativeLayout addNote;
    public Animation zoom, zoom_one, zoom_in,cyc;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    private DatabaseReference mDatabase;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private CircularImageView imgFB;
    private TextView txFB;
    private LoginButton loginFB;
    private Button share;
    private Button comment, logout, exit, dismis;
    private RecyclerView gridView;
    private ArrayList<NoteModel> listSQL = new ArrayList<>();
    private ArrayList<NoteModel> listFIREBASE = new ArrayList<>();
    private GetSQLite sqLite;
    private AdapterNote adapterNote;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    private LoadingDailog dialogx, dialogxx;
    private ImageView notnote;
    private TextView txnotnote;
    private BaseData baseData;
    private Switch aSwitch;
    private BaseDataALL baseDataALL;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private ArrayList<com.vuabocphet.noteonline.model.Uri> uri = new ArrayList<>();
    private int i = 0;

    private long a;
    private String url;
    private BroadcastReceiver mNetworkReceiver;
    private  RelativeLayout aLayout;

    private GestureDetector gestureDetector;
    int SWIPE=100;
    int v=100;
    private LinearLayout xxx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.vuabocphet.noteonline",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        Bungee.zoom(this);
        Log.e("WIDTH", getPixel() + "");


        mapped();
        registerInternetReceiver();
        gridView.setVisibility(View.INVISIBLE);
        loading();
        final String id = sp.getString("id", "");
        if (isInternetConnection() && !id.equals("")) {
            Toasty.normal(this, "Đã kết nối", R.drawable.wifi).show();
        }
        if (!isInternetConnection()) {
            Toasty.normal(Home.this, "Đang ở chế độ offline", R.drawable.wifioff).show();
            dialogx.dismiss();
        }
        view();
        data();
        on();
    }




    private void mapped() {
        aLayout=findViewById(R.id.aLayout);
        xxx=findViewById(R.id.xxx);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://onlinestore-3ac1a.appspot.com");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        sqLite = new GetSQLite();
        baseData = new BaseData(this);
        baseDataALL = new BaseDataALL(this);
        sp = getSharedPreferences("ISRUNNING", MODE_PRIVATE);
        gridView = findViewById(R.id.gidview);
        file = (ImageView) findViewById(R.id.file);
        menu = (ImageView) findViewById(R.id.menu);
        addNote = (RelativeLayout) findViewById(R.id.addNote);
        notnote = (ImageView) findViewById(R.id.notnote);
        txnotnote = (TextView) findViewById(R.id.txnotnote);
        zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);
        zoom_one = AnimationUtils.loadAnimation(this, R.anim.translate_dialog_out_setting);
        zoom_in = AnimationUtils.loadAnimation(this, R.anim.translate_dialog_in_setting);
        boolean sw=sp.getBoolean("sw",true);
        if (sw){
            aLayout.setBackgroundColor(getResources().getColor(R.color.blackLight));

        }else {
            aLayout.setBackground(getResources().getDrawable(R.drawable.title_a));
        }

        gestureDetector=new GestureDetector(this,new MyGes());
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
    }


    private void view() {
        adapterNote = new AdapterNote(this, listSQL);
        gridView.setLayoutManager(linearLayoutManager);
        gridView.setHasFixedSize(true);
        gridView.setAdapter(adapterNote);
    }


    private void data() {
        final String id = sp.getString("id", "");
        if (isInternetConnection() && !id.equals("")) {

            insertSQLiteFirebase(id);
            getDataFirebase();
        }
        if (id.equals("")) {
            getDataSQL();
        }

        if (!isInternetConnection() && !id.equals("")) {
            listSQL.clear();
            listSQL = sqLite.Data(this);
            view();
            dialogx.dismiss();
        }


    }

    private void getDataSQL() {
        listSQL.clear();
        listSQL = sqLite.GetData(this);
        view();
        dialogx.dismiss();
    }

    private void getDataFirebase() {

        final String id = sp.getString("id", "");
        Log.e("DATA", id);
        listSQL.clear();
        baseData.deleteTABLE();

        uri.clear();

        if (!id.equals("")) {


            mDatabase.child(id).child("GhiChu").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    NoteModel noteModel = dataSnapshot.getValue(NoteModel.class);
                    a = baseData.insert(noteModel.getDate(), noteModel.getNode(), noteModel.getImg(), noteModel.getId(), noteModel.getSubject());
                    Log.e("SQL", a + "");
                    listSQL.add(0, noteModel);
                    view();
                    dialogx.dismiss();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }

    private void on() {

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.startAnimation(zoom_one);
                menu.setVisibility(View.INVISIBLE);
                setting();
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, AddNote.class));

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        ed = sp.edit();
        ed.putBoolean("run", true);
        ed.commit();
        String id = sp.getString("id", "");
        if (!id.equals("")) {
            isInternetConnection();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
        ed = sp.edit();
        ed.putBoolean("false", true);
        ed.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loading();
        Log.e("onRestart", "onRestart");
        gridView.setVisibility(View.INVISIBLE);
        data();

    }


    private void setting() {
        final Dialog dialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_dialog, null);
        dialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = this.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this, 16f);
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        contentView.setLayoutParams(params);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.center);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                menu.startAnimation(zoom_in);
                menu.setVisibility(View.VISIBLE);
            }
        });

        aSwitch=dialog.findViewById(R.id.sw);
        imgFB = dialog.findViewById(R.id.imgFB);
        txFB = dialog.findViewById(R.id.txFB);
        loginFB = dialog.findViewById(R.id.login_button);
        share = dialog.findViewById(R.id.share);
        comment = dialog.findViewById(R.id.comment);
        logout = dialog.findViewById(R.id.logout);
        exit = dialog.findViewById(R.id.exit);
        dismis = dialog.findViewById(R.id.dismiss);
        boolean sw=sp.getBoolean("sw",true);
        if (sw){
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ed=sp.edit();
                if (b){
                    ed.putBoolean("sw",true);
                    ed.commit();
                    aLayout.setBackgroundColor(getResources().getColor(R.color.blackLight));
                }
                else {
                    ed.putBoolean("sw",false);
                    ed.commit();
                    aLayout.setBackground(getResources().getDrawable(R.drawable.title_a));
                }
            }
        });

        dismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        String name = sp.getString("name", "");
        String img = sp.getString("img", "");
        String id = sp.getString("id", "");
        if (!name.equals("") && !img.equals("")) {
            Log.e("ID", id);
            txFB.setText(name);
            txFB.setVisibility(View.VISIBLE);
            imgFB.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            loginFB.setVisibility(View.GONE);
            Picasso.get().load(img).into(imgFB);

        }

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Good bye :))", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnection()) {

                    LoginManager.getInstance().logOut();
                    baseData.deleteTABLE();
                    ed = sp.edit();
                    txFB.setVisibility(View.GONE);
                    imgFB.setVisibility(View.GONE);
                    loginFB.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.GONE);
                    ed.putString("name", "");
                    ed.putString("img", "");
                    ed.putString("id", "");
                    ed.commit();
                    data();
                    loading();
                    Toasty.normal(Home.this, "Đã đăng xuất", R.drawable.wifi).show();
                    dialog.dismiss();
                    dialogx.dismiss();

                } else {
                    Toast.makeText(Home.this, "Không có mạng", Toast.LENGTH_SHORT).show();
                }

            }
        });


        loginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (isInternetConnection()) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                    dialog.dismiss();
                    loading();
                } else {
                    Toast.makeText(Home.this, "Không có mạng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Log.e("ID", "HAHA");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                txFB.setText(user.getDisplayName());
                                txFB.setVisibility(View.VISIBLE);
                                imgFB.setVisibility(View.VISIBLE);
                                loginFB.setVisibility(View.GONE);
                                logout.setVisibility(View.VISIBLE);
                                logout.startAnimation(zoom_in);
                                txFB.startAnimation(zoom_in);
                                imgFB.startAnimation(zoom_in);
                                Picasso.get().load(user.getPhotoUrl()).into(imgFB);
                                ed = sp.edit();
                                ed.putString("name", user.getDisplayName());
                                ed.putString("img", String.valueOf(user.getPhotoUrl()));
                                ed.putString("id", user.getUid());
                                ed.commit();
                                insertSQLiteFirebase(user.getUid());
                                getDataFirebase();
                                Toasty.normal(Home.this, "Đăng nhập thành công", R.drawable.wifi).show();


                            }

                        } else {

                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Home.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
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

    private void loading() {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage("Đang tải")
                .setCancelable(true)
                .setCancelOutside(true);
        dialogx = loadBuilder.create();
        dialogx.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                gridView.setVisibility(View.VISIBLE);
                isdata();

            }
        });
        dialogx.show();
    }


    private void loadingoffline() {

        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage("Đang tải chế độ offline")
                .setCancelable(true)
                .setCancelOutside(true);
        dialogxx = loadBuilder.create();
        dialogxx.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                gridView.setVisibility(View.VISIBLE);
            }
        });
        dialogx.show();

    }


    private void isdata() {

        if (!listSQL.isEmpty()) {
            txnotnote.setVisibility(View.INVISIBLE);
            notnote.setVisibility(View.INVISIBLE);
            String id = sp.getString("id", "");
        } else {
            gridView.setVisibility(View.INVISIBLE);
            txnotnote.setVisibility(View.VISIBLE);
            notnote.setVisibility(View.VISIBLE);
        }
    }

    private void insertSQLiteFirebase(String id) {
        listFIREBASE.clear();
        listFIREBASE = sqLite.Data(this);
        if (!listFIREBASE.isEmpty()) {
            for (NoteModel xData : listFIREBASE) {

                mDatabase.child(id).child("GhiChu").child(xData.getId()).setValue(xData);

            }
        }
    }



    private void restart(boolean value) {

        if (value) {


        } else {


        }

    }


    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(broadcastReceiver);

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

        }
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (isOnline(context)) {

                    restart(true);

                } else {

                    restart(false);

                }
            } catch (NullPointerException e) {

                e.printStackTrace();

            }
        }
    };

    private boolean isOnline(Context context) {
        try {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());

        } catch (NullPointerException e) {

            e.printStackTrace();
            return false;

        }
    }

    private void registerInternetReceiver() {


        registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));


    }

    public int getPixel() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;

    }

    class MyGes extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e2.getY()-e1.getY()>SWIPE && Math.abs(velocityY)>v){

                ;
            }

            if (e1.getY()-e2.getY()>SWIPE && Math.abs(velocityY)>v){


            }




            return super.onFling(e1, e2, velocityX, velocityY);

        }
    }
}
