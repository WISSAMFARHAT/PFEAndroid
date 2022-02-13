package com.example.time;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Register extends AppCompatActivity {
    EditText Fullname;
    EditText Username;
    EditText Email;
    EditText Password;
    EditText Confirm_Password;
    private FirebaseAuth mAuth;
    Button button;
    RadioGroup radioGroup;
    DatabaseReference reff;
    long maxid=0;
     String Full="";
     String User="";
     String mail="";
     String Pass="";
    String Conf_Pass="";
    String user="",full="",mails="",pss="",conf_pss="";

    ArrayList<String> ar;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register");

        Fullname=findViewById(R.id.fullname);
        Username=findViewById(R.id.username);
        Email=findViewById(R.id.email);
        Password=findViewById(R.id.password);
        Confirm_Password=findViewById(R.id.confirm_password);
        radioGroup=findViewById(R.id.Gender);
        button=findViewById(R.id.register);
        radioGroup.check(1);
        mAuth=FirebaseAuth.getInstance();

        ar=new ArrayList<>();


        reff=FirebaseDatabase.getInstance().getReference().child("Member");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid=dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Full=Fullname.getText().toString().trim();
                User=Username.getText().toString().trim();
                mail=Email.getText().toString().trim();
                Pass=Password.getText().toString().trim();
                Conf_Pass=Confirm_Password.getText().toString().trim();
                if(Full.equals("") || User.equals("") || mail.equals("") || Pass.equals("") || Conf_Pass.equals("")){
                    Fullname.setText("");
                    Username.setText("");
                    Email.setText("");
                    Password.setText("");
                    Confirm_Password.setText("");
                    Toast.makeText(getApplicationContext(), "put all text ", Toast.LENGTH_SHORT).show();

                }else{

                    if(Pass.equals(Conf_Pass)){
                        if(Pass.length()>6) {
                            if (isValid(mail)) {

                                final String gender;
                                int id = radioGroup.getCheckedRadioButtonId();
                                if (id == 1) {
                                    gender = "Male";
                                } else {
                                    gender = "Female";
                                }


                                register();

                                Fullname.setText("");
                                Username.setText("");
                                Email.setText("");
                                Password.setText("");
                                Confirm_Password.setText("");
                                Toast.makeText(getApplicationContext(), "Donne", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(Register.this,MainActivity.class));

                            } else {
                                Email.setText("");
                                Toast.makeText(getApplicationContext(), "put a valid email ", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Password.setText("");
                            Confirm_Password.setText("");
                            Toast.makeText(getApplicationContext(), "put a password greater than 6", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Password.setText("");
                        Confirm_Password.setText("");
                        Toast.makeText(getApplicationContext(), "Confirm your Password ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public void register(){


        full=Fullname.getText().toString().trim();
        user=Username.getText().toString().trim();
        mails=Email.getText().toString().trim();
        pss=Password.getText().toString().trim();
        conf_pss=Confirm_Password.getText().toString().trim();
        if(full.equals("") || user.equals("") || mails.equals("") || pss.equals("") || conf_pss.equals("")){
            Fullname.setText("");
            Username.setText("");
            Email.setText("");
            Password.setText("");
            Confirm_Password.setText("");
            Toast.makeText(getApplicationContext(), "put all text ", Toast.LENGTH_SHORT).show();

        }else{

            if(pss.equals(conf_pss)){
              if(pss.length()>6) {
                  if (isValid(mails)) {

                      final String gender;
                      int id = radioGroup.getCheckedRadioButtonId();
                      if (id == 1) {
                          gender = "Male";
                      } else {
                          gender = "Female";
                      }

                      mAuth.createUserWithEmailAndPassword(mails, pss)
                              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                  @Override
                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                      if (task.isSuccessful()) {
                                          ar.add(full);
                                          ar.add(user);
                                          ar.add(mails);
                                          ar.add(gender);
                                          ar.add(pss);
                                          reff.child(String.valueOf(maxid+1)).setValue(ar);

                                      } else {
                                          Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                      }
                                  }
                              });

                  } else {
                      Email.setText("");
                      Toast.makeText(getApplicationContext(), "put a valid email ", Toast.LENGTH_SHORT).show();
                  }
              }else{
                  Password.setText("");
                  Confirm_Password.setText("");
                  Toast.makeText(getApplicationContext(), "put a password greater than 6", Toast.LENGTH_SHORT).show();
              }

            }else{
                Password.setText("");
                Confirm_Password.setText("");
                Toast.makeText(getApplicationContext(), "Confirm your Password ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() !=null){

        }
    }
}
