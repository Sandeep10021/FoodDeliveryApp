package com.csre.hungerrr.Server;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csre.hungerrr.MainActivity;
import com.csre.hungerrr.R;
import com.csre.hungerrr.SignIn;
import com.facebook.FacebookSdk;


public class MainActivityServer extends AppCompatActivity implements View.OnClickListener {
    Button btnSignIn, btnSignUp;
    TextView textSlogan;
//    private boolean isSinglePressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main_server);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        textSlogan = findViewById(R.id.textSlogan);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        textSlogan.setTypeface(typeface);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSignUp)
        {
//            SignUp
            Intent intent = new Intent(MainActivityServer.this, SignUpServer.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btnSignIn)
        {
//            Login
            Intent intent = new Intent(MainActivityServer.this, SignInServer.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
