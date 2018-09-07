package farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.soulwolf.widget.dialogbuilder.DialogBuilder;
import net.soulwolf.widget.dialogbuilder.dialog.AlertMasterDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmerline.com.dev.farmerserviceapp.R;
import farmerline.com.dev.farmerserviceapp.dashboard.home.baseFragment.BaseFragment;
import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.adapter.PlacesAutoCompleteAdapter;
import farmerline.com.dev.farmerserviceapp.fonts.TextViewRobotoMedium;
import farmerline.com.dev.farmerserviceapp.root.App;
import farmerline.com.dev.farmerserviceapp.utils.Constant;

public class MapFragment extends BaseFragment implements MapFragmentMVP.View,OnMapReadyCallback,View.OnClickListener{

    @Inject
    MapFragmentMVP.Presenter presenter;


    @BindView(R.id.map_auto_complete)
    AutoCompleteTextView autoCompleteTextView;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @BindView(R.id.btnSaveLocation)
    Button btnSaveLocation;

    TextViewRobotoMedium textViewRobotoMedium;

    Toast toast;

    Toast secondToast;

    private float DEFAULT_ZOOM=13;

    GoogleMap mMap;

    Dialog dialog;

    Dialog savedDialog;

    public static MapFragment newInstance()
    {
        MapFragment fragment=new MapFragment();
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_maps;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this,getActivity());

        presenter.setView(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        autoCompleteTextView.setAdapter(presenter.getAdapter());
        autoCompleteTextView.setOnItemClickListener(presenter.setItemClickLister());

        btnSaveLocation.setOnClickListener(this);

        View layout = LayoutInflater.from(getContext()).inflate(R.layout.custom_toast, null);
         toast = new Toast(App.getContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        secondToast= new Toast(App.getContext());
        secondToast.setGravity(Gravity.TOP, 0, 0);
        secondToast.setDuration(Toast.LENGTH_LONG);
        secondToast.setView(layout);

        dialog=new Dialog(getContext(),android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        dialog.setContentView(R.layout.custom_toast);

        savedDialog=new Dialog(getContext(),android.R.style.Theme_Translucent_NoTitleBar);
        savedDialog.setContentView(R.layout.toast_saved_location);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopAutoManage();
        presenter.disconnectGoogleClient();
    }

    @Override
    public void setAutoCompleteAdapter(PlacesAutoCompleteAdapter adapter) {

    }

    @Override
    public FragmentActivity getFragmentActivity()
    {
        return getActivity();
    }

    @Override
    public void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(frameLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void setLocationOnMap(LatLng latLng) {
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);

        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .icon(icon);
        this.mMap.addMarker(markerOptions);
    }

    @Override
    public void showSaveButton() {
        btnSaveLocation.setVisibility(View.VISIBLE);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        LatLng farmerLine= new LatLng(5.5750545,-0.2097632);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(farmerLine,DEFAULT_ZOOM));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSaveLocation:
                    presenter.saveLocation();
                    navigationPresenter.removeFragment();
                    //showSavingDialog();
                   // showSecondToast();
                break;
        }
    }

    private void showSavingDialog(){

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity( Gravity.TOP);

        lp.x = 100; // The new position of the X coordinates
        lp.y = 100; // The new position of the Y coordinates


        dialogWindow.setAttributes(lp);

        dialog.show();




    }

    private void hideSavingDialog(){
        dialog.dismiss();
    }

    void showSavedDialog(){
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        lp.x = 100; // The new position of the X coordinates
        lp.y = 100; // The new position of the Y coordinates
        lp.width = 300; // Width
        lp.height = 300; // Height
        lp.alpha = 0.7f; // Transparency

        dialogWindow.setAttributes(lp);

        savedDialog.show();
    }

    void hideSavedDialog(){
        savedDialog.dismiss();
    }


    private void showSecondToast(){
        CountDownTimer counter = new CountDownTimer(30000, 1000){
            public void onTick(long millisUntilDone){
                Log.d(Constant.TAG,String.valueOf(millisUntilDone));
            }

            public void onFinish() {
                Log.d(Constant.TAG,"Finished");

            }
        }.start();
    }

    private void moveToHome(){
        CountDownTimer counter = new CountDownTimer(30000, 1000){
            public void onTick(long millisUntilDone){

            }

            public void onFinish() {
                navigationPresenter.removeFragment();

            }
        }.start();
    }


}
