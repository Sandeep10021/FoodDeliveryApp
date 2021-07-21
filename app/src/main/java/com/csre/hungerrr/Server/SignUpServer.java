package com.csre.hungerrr.Server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csre.hungerrr.Common.Common;
import com.csre.hungerrr.Model.User;
import com.csre.hungerrr.R;
import com.csre.hungerrr.SignIn;
import com.csre.hungerrr.SignUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignUpServer extends AppCompatActivity {

    EditText editPhone, editName, editPassword;
    Button btnSignUp;
    TextView haveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_server);


        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        haveAccount = findViewById(R.id.haveAccount);

        //Firebase Init
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SignUpServer.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();


                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(editPhone.getText().toString()).exists())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpServer.this, "User already exists!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            User user = new User(editName.getText().toString(), editPassword.getText().toString());
                            table_user.child(editPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUpServer.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpServer.this, HomeActivityServer.class);
                            Common.currentUser = user;
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        haveAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SignUpServer.this, SignInServer.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
