//package com.example.time.ui.Calander;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.bumptech.glide.Glide;
//import com.example.time.MainActivity;
//import com.example.time.R;
//import com.facebook.AccessToken;
//import com.facebook.login.LoginManager;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import org.json.JSONObject;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import static com.facebook.FacebookSdk.getApplicationContext;
//
//public class CalanderViewModel extends ViewModel {
//
//    private CalanderViewModel shareViewModel;
//    private FirebaseAuth mAuth;
//    String email="";
//    GoogleSignInClient mGoogleSignInClient;
//    GoogleSignInAccount account;
//    SharedPreferences sharedPreferences;
//    TextView name;
//    ImageView imageView;
//    Button logout;
//    private MutableLiveData<String> mText;
//
//    public CalanderViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is share fragment");
//
//        sharedPreferences = getContext().getSharedPreferences("active", 0);
//        int active = sharedPreferences.getInt("active", 0);
//
//
//
//
//        mAuth = FirebaseAuth.getInstance();
//        loadinformation();
//
//        name=root.findViewById(R.id.email);
//        imageView=root.findViewById(R.id.imageView7);
//        logout=root.findViewById(R.id.logout);
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        // [END config_signin]
//        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
//        account = GoogleSignIn.getLastSignedInAccount(getContext());
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signout();
//            }
//        });
//        if (active == 1) {
//
//            FirebaseUser user = mAuth.getCurrentUser();
//            if (user.getEmail() != null) {
//                email = user.getEmail();
//
//            }
//            name.setText("Logged as \n"+email);
//
//        } else if (account != null) {
//
//            String personName = account.getDisplayName();
//
//            String personEmail = account.getEmail();
//            String personPhoto = account.getPhotoUrl().toString();
//            System.out.println("settings "+personPhoto);
//            name.setText("Logged as "+personName +"\n("+personEmail+")");
//            name.setTextSize(18);
//            Glide.with(getContext()).load(personPhoto).into(imageView);
//
//        } else if (AccessToken.getCurrentAccessToken() != null) { }
//
//
//
//    }
//
//    public LiveData<String> getText() {
//        return mText;
//    }
//
//
//    public void signout() {
//
//        if(account!=null){
//            mGoogleSignInClient.revokeAccess()
//                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            // ...
//                            LoginManager.getInstance().logOut();
//                            getActivity().finish();
//                            Toast.makeText(getApplicationContext(), "log out ", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(getContext(), MainActivity.class);
//                            startActivity(i);
//                        }
//                    });
//
//        } else if(AccessToken.getCurrentAccessToken()!=null){
//            LoginManager.getInstance().logOut();
//            Intent i = new Intent(getContext(), MainActivity.class);
//            Toast.makeText(getApplicationContext(), "log out ", Toast.LENGTH_SHORT).show();
//            startActivity(i);
//            getActivity().finish();
//        }
//        else {
//            sharedPreferences=getContext().getSharedPreferences("active", 0);
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            editor.putInt("active",0);
//            editor.apply();
//            Intent i = new Intent(getContext(), MainActivity.class);
//            Toast.makeText(getApplicationContext(), "log out", Toast.LENGTH_SHORT).show();
//            getActivity().finish();
//            startActivity(i);
//
//        }
//    }
//
//    public void loadinformation() {
//        if (mAuth.getCurrentUser() != null) {
//            FirebaseUser user = mAuth.getCurrentUser();
//            if (user.getEmail() != null) {
//                email = user.getEmail();
//            }
//
//        }
//    }
//
//    private Bundle getFacebookData(JSONObject object) {
//        Bundle bundle = new Bundle();
//
//        try {
//            String id = object.getString("id");
//            URL profile_pic;
//            try {
//                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
//                Log.i("profile_pic", profile_pic + "");
//                bundle.putString("profile_pic", profile_pic.toString());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return null;
//            }
//
//            bundle.putString("idFacebook", id);
//            if (object.has("first_name"))
//                bundle.putString("first_name", object.getString("first_name"));
//            if (object.has("last_name"))
//                bundle.putString("last_name", object.getString("last_name"));
//            if (object.has("email"))
//                bundle.putString("email", object.getString("email"));
//            if (object.has("gender"))
//                bundle.putString("gender", object.getString("gender"));
//
//
//        } catch (Exception e) { }
//
//        return bundle;
//    }
//
//
//
//
//
//}