package farmerline.com.dev.farmerserviceapp.dashboard.home.analytics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmerline.com.dev.farmerserviceapp.R;
import farmerline.com.dev.farmerserviceapp.dashboard.home.baseFragment.BaseFragment;
import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.MapFragment;
import farmerline.com.dev.farmerserviceapp.root.App;

public class AnalyticsFragment extends BaseFragment implements AnalyticsFragmentMVP.View,View.OnClickListener{

    @Inject
    AnalyticsFragmentMVP.Presenter presenter;

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.rlAddLocation)
    RelativeLayout rlAddLocation;

    @BindView(R.id.tvAddLocation)
    TextView tvAddLocation;

    @BindView(R.id.cLAnalytics)
    ConstraintLayout constraintLayout;

    @BindView(R.id.llNoLocation)
    LinearLayout llNolocation;

    GoogleMap mMap;

    private float DEFAULT_ZOOM=9;


    @Override
    protected int getLayout() {
        return R.layout.dasboard_activity;
    }

    public static AnalyticsFragment newInstance() {
        AnalyticsFragment fragment=new AnalyticsFragment();

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this,getActivity());
        rlAddLocation.setOnClickListener(this);
        tvAddLocation.setOnClickListener(this);
        presenter.getLocation();
        mapViewListItemViewOnCreate(savedInstanceState);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationPresenter.addFragment(MapFragment.newInstance());
            }
        });



    }



    @Override
    public void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.getLocation();
        mapViewListItemViewOnResume();
    }

    @Override
    public void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(constraintLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void setLocation(final LatLng latLng) {
        llNolocation.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                showSnackBar("Map is ready");
                mMap=googleMap;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);

                MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                        .icon(icon);

                mMap.addMarker(markerOptions);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        navigationPresenter.addFragment(MapFragment.newInstance());
                    }
                });

            }
        });
        mapViewListItemViewOnResume();

        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationPresenter.addFragment(MapFragment.newInstance());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rlAddLocation:
                navigationPresenter.addFragment(MapFragment.newInstance());
                break;
            case R.id.tvAddLocation:
                navigationPresenter.addFragment(MapFragment.newInstance());
                break;
        }
    }

    public void mapViewListItemViewOnCreate(Bundle savedInstanceState) {
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    public void mapViewListItemViewOnResume() {
        if (mapView != null) {
            mapView.onResume();
        }
    }

    public void mapViewListItemViewOnPause() {
        if (mapView != null) {
            mapView.onPause();
        }
    }

    public void mapViewListItemViewOnDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    public void mapViewListItemViewOnLowMemory() {
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    public void mapViewListItemViewOnSaveInstanceState(Bundle outState) {
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }
}
