package farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import farmerline.com.dev.farmerserviceapp.R;
import farmerline.com.dev.farmerserviceapp.utils.Constant;

public class PlacesAutoCompleteAdapter extends ArrayAdapter {



    private List<Place> dataList;
    private Context mContext;
    private GeoDataClient geoDataClient;

    private CustomAutoCompleteFilter listFilter =
            new CustomAutoCompleteFilter();


    public PlacesAutoCompleteAdapter(@NonNull Context context, int resource) {
        super(context, resource,new ArrayList<Place>());
        this.mContext=context;
        this.dataList=new ArrayList<>();

        this.geoDataClient = Places.getGeoDataClient(mContext, null);
        Log.d(Constant.TAG,"PlaceAutoCompleteAdapter");
    }



    @Override
    public int getCount() {
        return dataList.size();
    }


    @Override
    public Place getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dropdown_item,
                            parent, false);
        }

        Log.d(Constant.TAG,"Place "+dataList.get(position).getPlaceText());

        TextView textOne =(TextView) view.findViewById(R.id.tvPlaceSelected);
        textOne.setText(dataList.get(position).getPlaceText());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }



    public class CustomAutoCompleteFilter extends Filter {
        private Object lock = new Object();
        private Object lockTwo = new Object();
        private boolean placeResults = false;


        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            Log.d(Constant.TAG,"Filter "+prefix);
            FilterResults results = new FilterResults();
            placeResults = false;
            final List<Place> placesList = new ArrayList<>();

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = new ArrayList<Place>();
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                Task<AutocompletePredictionBufferResponse> task
                        = getAutoCompletePlaces(searchStrLowerCase);

                task.addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {

                        if (task.isSuccessful()) {
                            Log.d(Constant.TAG, "Auto complete prediction successful");
                            AutocompletePredictionBufferResponse predictions = task.getResult();
                            Place autoPlace;
                            for (AutocompletePrediction prediction : predictions) {
                                autoPlace = new Place();
                                autoPlace.setPlaceId(prediction.getPlaceId());
                                autoPlace.setPlaceText(prediction.getFullText(null).toString());

                                placesList.add(autoPlace);
                            }
                            predictions.release();
                            Log.d(Constant.TAG, "Auto complete predictions size " + placesList.size());
                        } else {

                            //Log.d(Constant.TAG, "Auto complete prediction unsuccessful "+task.getResult().toString());
                        }
                        //inform waiting thread about api call completion
                        placeResults = true;
                        synchronized (lockTwo) {
                            lockTwo.notifyAll();
                        }
                    }
                });

                //wait for the results from asynchronous API call
                while (!placeResults) {
                    synchronized (lockTwo) {
                        try {
                            lockTwo.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                }
                results.values = placesList;
                results.count = placesList.size();
                Log.d(Constant.TAG, "Autocomplete predictions size after wait" + results.count);
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<Place>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        private Task<AutocompletePredictionBufferResponse> getAutoCompletePlaces(String query) {
            //create autocomplete filter using data from filter Views
            Log.d(Constant.TAG,"Query "+query);
            AutocompleteFilter.Builder filterBuilder = new AutocompleteFilter.Builder();
            filterBuilder.setCountry("gh");
            filterBuilder.setTypeFilter(0);

            Task<AutocompletePredictionBufferResponse> results =
                    geoDataClient.getAutocompletePredictions(query, null,
                            filterBuilder.build());

            return results;
        }
    }
}
