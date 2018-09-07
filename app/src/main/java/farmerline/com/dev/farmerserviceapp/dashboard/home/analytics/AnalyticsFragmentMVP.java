package farmerline.com.dev.farmerserviceapp.dashboard.home.analytics;

import com.google.android.gms.maps.model.LatLng;

import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.LocationModel;

public interface AnalyticsFragmentMVP {


    interface View {
        void showSnackBar(String msg);
        void setLocation(LatLng latLng);
    }

    interface Presenter {
        void setView(AnalyticsFragmentMVP.View view);
        void showMap();
        void hidMap();
        void getLocation();

    }

    interface Model {
        LocationModel getLocation();
    }
}
