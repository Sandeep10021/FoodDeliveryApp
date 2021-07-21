package com.csre.hungerrr;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.csre.hungerrr.Common.Common;
import com.csre.hungerrr.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.csre.hungerrr.Common.Common.CLIENT;
import static com.csre.hungerrr.Common.Common.USER_NAME;
import static com.csre.hungerrr.Common.Common.USER_PASSWORD;
import static com.csre.hungerrr.Common.Common.USER_PHONE;


public class SignIn extends AppCompatActivity {

    EditText editPhone, editPassword;
    Button btnSignIn;
    TextView newUserLink;
    com.rey.material.widget.CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Paper.init(this);

        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        rememberMe = findViewById(R.id.remember_me);
        newUserLink = findViewById(R.id.newUserLink);

        //Firebase Init
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");
        
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Checking User avail
                        if(dataSnapshot.child(editPhone.getText().toString()).exists())
                        {
                            //Get User data
                            progressDialog.dismiss();
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            assert user != null;
                            if (user.getPassword().equals(editPassword.getText().toString()))
                            {
                                //remember me
                                if(rememberMe.isChecked())
                                {
                                    Paper.book(CLIENT).write(USER_PHONE, editPhone.getText().toString());
                                    Paper.book(CLIENT).write(USER_PASSWORD, editPassword.getText().toString());
                                    Paper.book(CLIENT).write(USER_NAME, user.getName());
                                }

                                user.setPhone(editPhone.getText().toString());
                                Common.currentUser = user;
                                Intent intent = new Intent(SignIn.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else
                            {
                                Toast.makeText(SignIn.this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exists!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        newUserLink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
