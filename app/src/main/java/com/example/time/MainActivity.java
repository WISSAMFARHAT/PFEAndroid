package com.example.time;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN=9001;
    private CallbackManager callbackManager;
    TextView login;
    private FirebaseAuth mAuth;
    public  SharedPreferences mPrefs ;
    //------------------------------------------------------------------------------------------------
    public static  String name="";
    //--------------------------------------------------------------------------------------------------
    //**************************************************************************************************

    //***************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs=getSharedPreferences("active", MODE_PRIVATE);


        login=findViewById(R.id.textView);
//****************************************************************************************************************
        //firebase data
        mAuth=FirebaseAuth.getInstance();
//***************************************************************************************************************
//
//        myDialog = new Dialog(this);
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient=GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            startActivity(new Intent(MainActivity.this,Main_page.class));

        }
                                                                        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                //GoogleSignInAccount account=result.getSignInAccount();
              // firebaseAuthWithGoogle(account);
                Intent i = new Intent(MainActivity.this, View_Guid.class);
                startActivity(i);

            }else{
                Toast.makeText(MainActivity.this,"Auth went wrong",Toast.LENGTH_SHORT).show();
            }
        }
              }
    //Onclick----------------------------------------------------------------------------------------------
    public void google_signin(View v ){
        signIn();
    }
    public void sign_out(View v){signout();}
    //-------------------------------------------------------------------------------------------------------
    //Google signIn------------------------------------------------------------------------------------------
    private void signIn() {

            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);

                        }

    //-------------------------------------------------------------------------------------------------------
//// result account
//    private void handelResult(){
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        String name =account.getDisplayName();
//        String email=account.getEmail();
//        String url=account.getPhotoUrl().toString();
//        System.out.println("account : "+name+" ,"+email+", "+url);
//                                                                    }
  //---------------------------------------------------------------------------------------------------------
    private void signout() {
        mGoogleSignInClient.signOut();
        LoginManager.getInstance().logOut();

    }
    //--------------------------------------------------------------------------------------------------------
//    public void  ShowPopup(View v){
//        myDialog.setContentView(R.layout.pop_register);
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();
//                                   }

//    public void close(View v ){
//        myDialog.dismiss();
//                              }

//    public void  register(View v) {
//            Intent intent = new Intent(MainActivity.this, Register.class);
//            startActivity(intent);
//
//        }

//    public void log(View v ){
//        Login();
//    }
//    private void  Login(){
//        String sname=Name.getText().toString().trim();
//        String spassword=Password.getText().toString().trim();
//        if(sname.equals("") || spassword.equals("")){
//            System.out.println("put all ");
//        }else{
//
//            mAuth.signInWithEmailAndPassword(sname,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        mPrefs= getSharedPreferences("active", 0);
//                        SharedPreferences.Editor editor=mPrefs.edit();
//                        editor.putInt("active",1);
//                        editor.apply();
//                        Intent intent = new Intent(MainActivity.this, View_Guid.class);
//                        startActivity(intent);
//                        finish();
//
//                    }else{
//                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//        }
//
//    }
 //*************************************************************************************************************


 //*************************************************************************************************************

       }
