package farmerline.com.dev.farmerserviceapp.dashboard.home.baseFragment;

public interface FragmentNavigation {

    interface View {
        void attachPresenter(Presenter presenter);
    }

    interface Presenter {
        void addFragment(BaseFragment fragment);
        void removeFragment();
    }
}
