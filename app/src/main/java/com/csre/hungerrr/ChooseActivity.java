package com.csre.hungerrr;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.csre.hungerrr.Common.Common;
import com.csre.hungerrr.Model.User;
import com.csre.hungerrr.Server.HomeActivityServer;
import com.csre.hungerrr.Server.MainActivityServer;
import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

import static com.csre.hungerrr.Common.Common.CLIENT;
import static com.csre.hungerrr.Common.Common.SERVER;
import static com.csre.hungerrr.Common.Common.USER_NAME;
import static com.csre.hungerrr.Common.Common.USER_PASSWORD;
import static com.csre.hungerrr.Common.Common.USER_PHONE;


public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnClient, btnServer;
    TextView textSlogan;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Paper.init(this);

        //Get key hash for fb
        try {

            @SuppressLint("PackageManagerGetSignatures") PackageInfo info =
                    getPackageManager().getPackageInfo("com.csre.hungerrr",
                            PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        relativeLayout = findViewById(R.id.parent);
        btnClient = findViewById(R.id.btnClient);
        btnServer = findViewById(R.id.btnServer);
        progressBar = findViewById(R.id.progress);


        textSlogan = findViewById(R.id.textSlogan);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        textSlogan.setTypeface(typeface);

        btnClient.setOnClickListener(this);
        btnServer.setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                btnClient.animate().alpha(1).setDuration(300);
                btnServer.animate().alpha(1).setDuration(300);
            }
        }, 1000);

        //Services

    }

    @Override
    public void onClick(View v) {
        //checking internet firstly
        if (Common.isInternetAvailable(this)) {
            if (v.getId() == R.id.btnClient) {
                //check if user is already signed in
                String phone = Paper.book(CLIENT).read(USER_PHONE);
                String password = Paper.book(CLIENT).read(USER_PASSWORD);
                String name = Paper.book(CLIENT).read(USER_NAME);

                if (phone != null && password != null && name != null) {
                    Intent intent = new Intent(ChooseActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Common.currentUser = new User(name, password, phone, "false");
                    finish();
                } else {
                    Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else if (v.getId() == R.id.btnServer) {
                //check if user is already signed in
                String phone = Paper.book(SERVER).read(USER_PHONE);
                String password = Paper.book(SERVER).read(USER_PASSWORD);
                String name = Paper.book(SERVER).read(USER_NAME);

                if (phone != null && password != null && name != null) {
                    Intent intent = new Intent(ChooseActivity.this, HomeActivityServer.class);
                    startActivity(intent);
                    Common.currentUser = new User(name, password, phone, "true");
                    finish();
                } else {
                    Intent intent = new Intent(ChooseActivity.this, MainActivityServer.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else {
            Snackbar.make(relativeLayout, "No Internet Connection!", Snackbar.LENGTH_LONG).show();
        }
    }

}
