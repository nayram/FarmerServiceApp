package farmerline.com.dev.farmerserviceapp.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmerline.com.dev.farmerserviceapp.R;
import farmerline.com.dev.farmerserviceapp.dashboard.home.Home;
import farmerline.com.dev.farmerserviceapp.root.App;
import farmerline.com.dev.farmerserviceapp.utils.Constant;

public class LoginActivity extends AppCompatActivity implements LoginActivityMVP.View,View.OnClickListener{


    @Inject
    LoginActivityMVP.Presenter presenter;

    private static final int RC_SIGN_IN = 123;
    private static final int FACEBOOK_REQUEST_CODE = 64206;

    @BindView(R.id.btnGoogle)
    SignInButton btnGoogle;

    @BindView(R.id.btnFacebook)
    LoginButton btnFacebook;

    @BindView(R.id.btnLinkedIn)
    Button btnLinkedIn;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((App) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);



        btnFacebook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnLinkedIn.setOnClickListener(this);

        presenter.setupGoogleSignin();
        presenter.createGoogleSingInClient();

        presenter.setupFacebookSignin();

        btnFacebook.setText("Log in with Facebook");

        btnFacebook.setReadPermissions("email", "public_profile");

        btnFacebook.registerCallback(presenter.getFacebookCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(Constant.TAG, "facebook:onSuccess:" + loginResult);
                presenter.handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d(Constant.TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(Constant.TAG, "facebook:onError", error);
                showSnackBar("Facebook : error during sign in");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.checkIfSignedInWithGoogle();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            presenter.googleSigninResult(data);
        }else if (requestCode == FACEBOOK_REQUEST_CODE){
            Log.d(Constant.TAG,"Facebook request code "+requestCode);
            presenter.setFacebookCallback(requestCode,resultCode,data);
        }else {
            presenter.setLinkedInCallback(requestCode,resultCode,data);
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void deactivateAllButtons() {
        btnLinkedIn.setEnabled(false);
        btnGoogle.setEnabled(false);
        btnFacebook.setEnabled(false);
    }

    @Override
    public void activateAllButtons() {
        btnLinkedIn.setEnabled(true);
        btnGoogle.setEnabled(true);
        btnFacebook.setEnabled(true);
    }

    @Override
    public void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public LoginActivity getActivity() {
        return this;
    }

    @Override
    public void startActivity() {
        startActivity(new Intent(this,Home.class));
        finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnGoogle:

                    startActivityForResult(presenter.googleLogin(),RC_SIGN_IN);
                break;

            case R.id.btnLinkedIn:
                presenter.setupLinkedInSignIn();
                break;
        }

    }



}
