package com.example.test003;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    private TextView TextTitle;
    private EditText EditArtistname, EditAge,EditEmail, EditPassword;
    private Button btnsignup;
    private ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        EditArtistname = (EditText) findViewById(R.id.artistname);
        EditAge = (EditText) findViewById(R.id.age);
        EditEmail = (EditText) findViewById(R.id.email);
        EditPassword = (EditText) findViewById(R.id.password);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);
        TextTitle = (TextView) findViewById(R.id.title);
        TextTitle.setOnClickListener(this);
        btnsignup = (Button) findViewById(R.id.btnsignup);
        btnsignup.setOnClickListener(this);



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title:
                startActivity(new Intent(this,MainActivity.class  ));
                break;
            case R.id.btnsignup:
                registerUser();
                break; 
        }



    }


    ////////////////////Login Convalidate  ////////////////
    private void registerUser() {
        String email = EditEmail.getText().toString().trim();
        String password = EditPassword.getText().toString().trim();
        String age   = EditAge.getText().toString().trim();
        String artistname = EditArtistname.getText().toString().trim();


        if(email.isEmpty()){
            EditEmail.setError("Email is required");
            EditEmail.requestFocus();
            return;
        }

        if(age.isEmpty()){
            EditAge.setError("Age is required");
            EditAge.requestFocus();
            return;
        }
        if(password.isEmpty()){
            EditPassword.setError("Password is required");
            EditPassword.requestFocus();
            return;
        }
        if(artistname.isEmpty()){
            EditArtistname.setError("ArtistName/Name is required");
            EditArtistname.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EditEmail.setError("Please insert valide email");
            EditEmail.requestFocus();
            return;
        }
        if(password.length()<8){
            EditPassword.setError("Min password lenght should be 8 characters");
            EditPassword.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Users users = new Users(artistname, age, email ,password);
                    FirebaseDatabase.getInstance().getReference("user")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                progressbar.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                //Redirect to login layout
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Sorry, failed the register.. Try Again!",Toast.LENGTH_LONG).show();
                                progressbar.setVisibility(View.GONE);
                            }
                        }
                    });


                }
                else {
                    Toast.makeText(RegisterActivity.this, "Sorry, failed the register.. Try Again!",Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);

                }
            }
        });


    }
}