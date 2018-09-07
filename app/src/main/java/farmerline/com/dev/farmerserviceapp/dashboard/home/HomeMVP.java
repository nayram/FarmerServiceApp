package farmerline.com.dev.farmerserviceapp.dashboard.home;

import farmerline.com.dev.farmerserviceapp.dashboard.home.baseFragment.BaseFragment;

public interface HomeMVP {

    interface View {
        void setFragement(BaseFragment Fragment);
        void removeFragment();

    }

    interface Presenter {

        void setView(HomeMVP.View view);
        void getDefaultFragment();
        void removeFragment();

    }

    interface Model {

    }
}
