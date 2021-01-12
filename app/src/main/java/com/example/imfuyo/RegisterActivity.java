package com.example.imfuyo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText idUname, idEmail, idPswd, idCnfrm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        idUname = findViewById(R.id.idUsername);
        idEmail = findViewById(R.id.idEmail);
        idPswd = findViewById(R.id.idPassword);
        idCnfrm = findViewById(R.id.idConfirm);

        Button button = findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = idEmail.getText().toString();
                String pswd = idPswd.getText().toString();
                String cnfrmpswd = idCnfrm.getText().toString();

                if (email.equals("")){
                    Toast.makeText(RegisterActivity.this, "Silahkan masukkan E-mail anda!",
                            Toast.LENGTH_SHORT).show();
                } else if (pswd.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Silahkan masukkan password anda!",
                            Toast.LENGTH_SHORT).show();
                } else if (cnfrmpswd.equals(idPswd)){
                    Toast.makeText(RegisterActivity.this, "Konfirmasi password anda tidak sesuai!!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(email, pswd)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Toast.makeText(RegisterActivity.this, "Pendaftaran akun berhasil!",
                                                Toast.LENGTH_SHORT).show();
                                        open();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, "Pendaftaran akun gagal!",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void open(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}