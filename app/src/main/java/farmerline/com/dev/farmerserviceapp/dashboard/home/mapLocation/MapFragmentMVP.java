package farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation;

import android.support.v4.app.FragmentActivity;
import android.widget.AdapterView;

import com.google.android.gms.maps.model.LatLng;

import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.adapter.PlacesAutoCompleteAdapter;

public interface MapFragmentMVP {

    interface View {

        void setAutoCompleteAdapter(PlacesAutoCompleteAdapter adapter);

        FragmentActivity getFragmentActivity();
        void showSnackBar(String msg);

        void setLocationOnMap(LatLng latLng);
        void showSaveButton();

    }

    interface Presenter {
        void setView(MapFragmentMVP.View view);
        PlacesAutoCompleteAdapter getAdapter();
        AdapterView.OnItemClickListener  setItemClickLister();
        void stopAutoManage();
        void disconnectGoogleClient();
        void saveLocation();
    }

    interface Model {
        void saveLocation(LatLng latLng);
        LocationModel getLatLng();
    }
}
