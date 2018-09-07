package farmerline.com.dev.farmerserviceapp.dashboard.home;


import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    @Provides
    public HomePresenter providesHomePresenter (HomeMVP.Model model)
    {
        return new HomePresenter(model);
    }

    @Provides
    public HomeMVP.Model providesHomeModel ()
    {
        return new HomeModel();
    }


}
