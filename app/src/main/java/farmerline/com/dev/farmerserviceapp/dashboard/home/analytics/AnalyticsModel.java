package farmerline.com.dev.farmerserviceapp.dashboard.home.analytics;

import com.google.gson.Gson;

import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.LocationModel;
import farmerline.com.dev.farmerserviceapp.root.App;
import farmerline.com.dev.farmerserviceapp.utils.Constant;

public class AnalyticsModel implements AnalyticsFragmentMVP.Model{

    public AnalyticsModel() {
    }


    @Override
    public LocationModel getLocation() {
        String location= App.getSharedPrefs().getString(Constant.location,null);
        if (location !=null)
        {
            return new Gson().fromJson(location,LocationModel.class);

        }else {
            return null;
        }
    }
}
