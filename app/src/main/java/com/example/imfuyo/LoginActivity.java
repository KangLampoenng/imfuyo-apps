package com.example.imfuyo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView TextDaftar;
    ImageView ImgFacebook;
    ImageView ImgGmail;
    private EditText idEmail, idPswd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        idEmail = findViewById(R.id.idUsername);
        idPswd = findViewById(R.id.idPassword);

        Button button = findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = idEmail.getText().toString();
                String pswd = idPswd.getText().toString();

                if (email.equals("")) {
                    Toast.makeText(LoginActivity.this, "Silahkan masukkan E-mail anda!",
                            Toast.LENGTH_SHORT).show();
                } else if (pswd.equals("")) {
                    Toast.makeText(LoginActivity.this, "Silahkan masukkan password anda!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.signInWithEmailAndPassword(email, pswd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        open();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Akun belum terdaftar!",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

        TextDaftar=(TextView)findViewById(R.id.textDaftar);
        TextDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        ImgFacebook=(ImageView)findViewById(R.id.facebook);
        ImgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });

        ImgGmail=(ImageView)findViewById(R.id.gmail);
        ImgGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });
    }

    public void open(){
        Intent intent = new Intent(this, HomeOwnerActivity.class);
        startActivity(intent);
    }

    public void register(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}