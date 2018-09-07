package farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation;

import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import farmerline.com.dev.farmerserviceapp.login.UserModel;
import farmerline.com.dev.farmerserviceapp.root.App;
import farmerline.com.dev.farmerserviceapp.utils.Constant;

public class MapFragmentModel implements MapFragmentMVP.Model {

    public MapFragmentModel() {
    }

    @Override
    public void saveLocation(LatLng latLng) {

        LocationModel locationModel=new LocationModel(latLng.latitude,latLng.longitude);
        SharedPreferences.Editor editor= App.getSharedPrefs().edit();
        String location= new Gson().toJson(locationModel);
        editor.putString(Constant.location,location);
        editor.apply();

    }

    @Override
    public LocationModel getLatLng() {
         String location= App.getSharedPrefs().getString(Constant.location,null);
        if (location !=null)
        {
            return new Gson().fromJson(location,LocationModel.class);

        }else {
            return new LocationModel();
        }
    }
}
