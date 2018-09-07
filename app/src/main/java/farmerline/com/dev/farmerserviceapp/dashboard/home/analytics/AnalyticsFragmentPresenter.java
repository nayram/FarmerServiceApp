package farmerline.com.dev.farmerserviceapp.dashboard.home.analytics;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.LocationModel;

public class AnalyticsFragmentPresenter implements AnalyticsFragmentMVP.Presenter {

    @Nullable
    AnalyticsFragmentMVP.View view;
    AnalyticsFragmentMVP.Model model;

    public AnalyticsFragmentPresenter(AnalyticsFragmentMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(AnalyticsFragmentMVP.View view) {
        this.view=view;
    }

    @Override
    public void showMap() {

    }

    @Override
    public void hidMap() {

    }

    @Override
    public void getLocation() {
        if (model.getLocation() !=null)
        {
            LocationModel locationModel=model.getLocation();
            LatLng latLng=new LatLng(locationModel.getLat(),locationModel.getLng());
            if (view !=null)
            view.setLocation(latLng);
        }else {
            if (view !=null)
            view.showSnackBar("Yet to set Location");
        }
    }
}
