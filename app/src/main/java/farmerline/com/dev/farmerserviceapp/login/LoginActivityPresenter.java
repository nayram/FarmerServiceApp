package farmerline.com.dev.farmerserviceapp.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import farmerline.com.dev.farmerserviceapp.root.App;
import farmerline.com.dev.farmerserviceapp.utils.Constant;

public class LoginActivityPresenter  implements LoginActivityMVP.Presenter{


    @Nullable
    private LoginActivityMVP.View view;
    private LoginActivityMVP.Model model;

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount google_account;


    CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;


    public LoginActivityPresenter(LoginActivityMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(LoginActivityMVP.View view) {
        this.view=view;
    }

    @Override
    public Intent googleLogin() {

        if (view!=null) {
            view.deactivateAllButtons();
            view.showProgress();
        }
        return  mGoogleSignInClient.getSignInIntent();
    }

    @Override
    public void googleSigninResult(Intent data) {
        if (view !=null)
        view.hideProgress();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.


            Log.d(Constant.TAG,"account data "+account.toJson());

            String image="";
            if (account.getPhotoUrl() !=null)
                if (account.getPhotoUrl().getPath() !=null)
                    image=account.getPhotoUrl().getPath();

            Log.d(Constant.TAG,"Image path "+image);

            UserModel userModel=new UserModel(account.getDisplayName(),account.getEmail(),
                    "google",account.getIdToken(),image);

            model.saveUser(userModel);
            view.showSnackBar("Signed In");
            view.startActivity();


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(Constant.TAG, "signInResult:failed code=" + e.getStatusCode());

            view.showSnackBar("Sign in failed");

        }
        view.activateAllButtons();
    }

    @Override
    public void facebookLogin() {

    }

    @Override
    public void linkedInLogin() {

    }

    @Override
    public void deactivateButtons() {
        if (view !=null)
        {
            view.deactivateAllButtons();
        }
    }

    @Override
    public void setupGoogleSignin() {
       this.gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();
    }

    @Override
    public void createGoogleSingInClient() {
        // Build a GoogleSignInClient with the options specified by gso.
         this.mGoogleSignInClient = GoogleSignIn.getClient(App.getContext(), this.gso);
    }

    @Override
    public void checkIfSignedInWithGoogle() {
         this.google_account = GoogleSignIn.getLastSignedInAccount(App.getContext());

    }

    @Override
    public void setupFacebookSignin() {
        this.mCallbackManager = CallbackManager.Factory.create();
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public CallbackManager getFacebookCallbackManager() {

        return this.mCallbackManager;
    }

    @Override
    public void setFacebookCallback(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        Log.d(Constant.TAG, "handleFacebookAccessToken:" + token);
        if (view !=null)
        {
            view.showProgress();
            view.deactivateAllButtons();
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential).addOnCompleteListener(view.getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(Constant.TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        view.hideProgress();
                        view.activateAllButtons();

                        String image="";
                        if (user.getPhotoUrl() !=null)
                            if (user.getPhotoUrl().getPath() !=null)
                                image=user.getPhotoUrl().getPath();



                        UserModel userModel=new UserModel(user.getDisplayName(),user.getEmail(),
                                "facebook",user.getUid(),image);

                        model.saveUser(userModel);
                        view.showSnackBar("Signed In");
                        view.startActivity();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(Constant.TAG, "signInWithCredential:failure", task.getException());
                        view.showSnackBar("Facebook Authentication Failed");
                        view.hideProgress();
                        view.activateAllButtons();

                    }
                }
            });
        }


    }

    @Override
    public void setupLinkedInSignIn() {

        if (view !=null)
            if (!LISessionManager.getInstance(view.getActivity()).getSession().isValid()) {
                //if not valid then start authentication
                LISessionManager.getInstance(App.getContext()).init(view.getActivity(), buildScope()//pass the build scope here
                        , new AuthListener() {
                            @Override
                            public void onAuthSuccess() {
                                fetchBasicProfileData();
                            }

                            @Override
                            public void onAuthError(LIAuthError error) {
                                Log.e(Constant.TAG, "Fetch profile Error   :" + error.toString());
                                view.showSnackBar("Failed to authenticate with LinkedIn. Please try again.");
                            }
                        }, true);
            } else {
                fetchBasicProfileData();
            }
    }

    @Override
    public void setLinkedInCallback(int requestCode, int resultCode, Intent data) {
        if (view !=null)
        LISessionManager.getInstance(App.getContext()).onActivityResult(view.getActivity(), requestCode, resultCode, data);
    }


    private static Scope buildScope() {
        //Check Scopes in Application Settings before passing here else you won't able to read that data
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.W_SHARE);
    }


    private void fetchBasicProfileData() {

        if (view !=null) {
            view.showProgress();
            view.deactivateAllButtons();
        }

        APIHelper apiHelper = APIHelper.getInstance(App.getContext());
        apiHelper.getRequest(view.getActivity(), Constant.LinkedIN_URL, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {

                Log.d(Constant.TAG, "API Res : " + apiResponse.getResponseDataAsString() + "\n" + apiResponse.getResponseDataAsJson().toString());
                if (view !=null){
                    view.hideProgress();
                    view.activateAllButtons();
                }

                JSONObject jsonObject = apiResponse.getResponseDataAsJson();

                UserModel userModel= null;
                try {
                    String image=jsonObject.getString("pictureUrl");;

                    userModel = new UserModel(jsonObject.getString("firstName") +" "+jsonObject.getString("lastName"),
                            jsonObject.getString("emailAddress"),
                            "linkedIn", "",image);
                    model.saveUser(userModel);
                    view.showSnackBar("Signed In");
                    view.startActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError liApiError) {

                if (view !=null){
                    view.hideProgress();
                    view.activateAllButtons();
                }

                Log.e(Constant.TAG, "Fetch profile Error   :" + liApiError.getLocalizedMessage());
                view.showSnackBar("Failed to fetch basic profile data. Please try again.");

            }
        });
    }




}
