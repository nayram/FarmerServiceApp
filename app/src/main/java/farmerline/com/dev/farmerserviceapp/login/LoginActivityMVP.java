package farmerline.com.dev.farmerserviceapp.login;


import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;

public interface LoginActivityMVP {

    interface View {
        void showProgress();
        void hideProgress();
        void deactivateAllButtons();
        void activateAllButtons();

        void showSnackBar(String msg);
        LoginActivity getActivity();
    }

    interface Presenter {
        void setView(LoginActivityMVP.View view);
        Intent googleLogin();

        void googleSigninResult(Intent data);

        void facebookLogin();
        void linkedInLogin();
        void deactivateButtons();

        //google sign in
        void setupGoogleSignin();
        void createGoogleSingInClient();
        void checkIfSignedInWithGoogle();

        //facebook sign in
        void setupFacebookSignin();
        CallbackManager getFacebookCallbackManager();
        void setFacebookCallback(int requestCode,int resultCode,Intent data);
        void handleFacebookAccessToken(AccessToken token);


        //linkedIn sign in
        void setupLinkedInSignIn();
        void setLinkedInCallback(int requestCode,int resultCode,Intent data);




    }

    interface Model{
        void saveUser(UserModel user);

        UserModel getUser();

    }
}
