package farmerline.com.dev.farmerserviceapp.login;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import farmerline.com.dev.farmerserviceapp.root.App;
import farmerline.com.dev.farmerserviceapp.utils.Constant;

public class LoginModel implements LoginActivityMVP.Model {


    public LoginModel() {

    }

    @Override
    public void saveUser(UserModel user) {
        SharedPreferences.Editor editor= App.getSharedPrefs().edit();
        String userObj= new Gson().toJson(user);
        editor.putString(Constant.UserObj,userObj);
        editor.apply();
    }

    @Override
    public UserModel getUser() {
        String userObj= App.getSharedPrefs().getString(Constant.UserObj,null);
        if (userObj !=null)
        {
            return new Gson().fromJson(userObj,UserModel.class);

        }
        return new UserModel();
    }
}
