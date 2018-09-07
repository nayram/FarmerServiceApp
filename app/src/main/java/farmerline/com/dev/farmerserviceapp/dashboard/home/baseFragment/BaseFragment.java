package farmerline.com.dev.farmerserviceapp.dashboard.home.baseFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements FragmentNavigation.View {


    protected android.view.View rootView;

    protected FragmentNavigation.Presenter navigationPresenter;

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayout(), container, false);
        return rootView;
    }

    protected abstract int getLayout();

    @Override
    public void attachPresenter(FragmentNavigation.Presenter presenter) {

        navigationPresenter = presenter;

    }
}
