package farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import farmerline.com.dev.farmerserviceapp.R;
import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.adapter.Place;
import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.adapter.PlacesAutoCompleteAdapter;
import farmerline.com.dev.farmerserviceapp.root.App;
import farmerline.com.dev.farmerserviceapp.utils.Constant;

public class MapFragmentPresenter implements MapFragmentMVP.Presenter,GoogleApiClient.OnConnectionFailedListener {

    @Nullable
    MapFragmentMVP.View view;
    MapFragmentMVP.Model model;

    LatLng queriedLocation;
    private GoogleApiClient mGoogleApiClient;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;


    PlacesAutoCompleteAdapter adapter;

    public MapFragmentPresenter(MapFragmentMVP.Model model) {
        this.model = model;


    }

    @Override
    public void setView(MapFragmentMVP.View view) {
        this.view=view;
        mGoogleApiClient = new GoogleApiClient.Builder(App.getContext())
                .enableAutoManage(view.getFragmentActivity(),0,
                        this /* OnConnectionFailedListener */)
                .addApi(Places.GEO_DATA_API)
                .build();
        this.adapter =new PlacesAutoCompleteAdapter(view.getFragmentActivity(), R.layout.dropdown_item);

    }

    @Override
    public PlacesAutoCompleteAdapter getAdapter() {
        Log.d(Constant.TAG,"adapter count "+this.adapter.getCount());
        return this.adapter;
    }

    @Override
    public AdapterView.OnItemClickListener setItemClickLister() {
        return this.onItemClickListener;
    }

    @Override
    public void stopAutoManage() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            this.mGoogleApiClient.stopAutoManage(view.getFragmentActivity());
        }
    }

    @Override
    public void disconnectGoogleClient() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            this.mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void saveLocation() {
        this.model.saveLocation(queriedLocation);
    }

    void setLatLng(LatLng latLng){
        if (view !=null)
        {
            view.setLocationOnMap(latLng);
            view.showSaveButton();
        }
    }


    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {

                    Log.d(Constant.TAG,
                            "selected place "
                                    + ((Place) adapterView.
                                    getItemAtPosition(i)).getPlaceText());

                    Places.GeoDataApi.getPlaceById(mGoogleApiClient, ((Place) adapterView.
                            getItemAtPosition(i)).getPlaceId())
                            .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (places.getStatus().isSuccess()) {
                                        final com.google.android.gms.location.places.Place myPlace = places.get(0);
                                         queriedLocation = myPlace.getLatLng();
                                        Log.d("Latitude is", "" + queriedLocation.latitude);
                                        Log.d("Longitude is", "" + queriedLocation.longitude);
                                        setLatLng(queriedLocation);

                                    }
                                    places.release();
                                }
                            });




                }
            };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(Constant.TAG,connectionResult.getErrorMessage());

    }
}
