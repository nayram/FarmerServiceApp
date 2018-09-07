package farmerline.com.dev.farmerserviceapp.dashboard.home;

import android.support.annotation.Nullable;

import farmerline.com.dev.farmerserviceapp.dashboard.home.analytics.AnalyticsFragment;
import farmerline.com.dev.farmerserviceapp.dashboard.home.baseFragment.BaseFragment;
import farmerline.com.dev.farmerserviceapp.dashboard.home.baseFragment.FragmentNavigation;

public class HomePresenter implements HomeMVP.Presenter,FragmentNavigation.Presenter {

    @Nullable
    HomeMVP.View view;
    HomeMVP.Model model;

    public HomePresenter(HomeMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(HomeMVP.View view) {
        this.view=view;
    }

    @Override
    public void getDefaultFragment() {
        if (view !=null)
            view.setFragement(AnalyticsFragment.newInstance());
    }

    @Override
    public void addFragment(BaseFragment fragment) {
        if (view != null)
            view.setFragement(fragment);
    }

    @Override
    public void removeFragment() {
        if (view !=null)
            view.removeFragment();

    }


}
