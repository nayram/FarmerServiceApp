package farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation;


import dagger.Module;
import dagger.Provides;

@Module
public class MapModule {

    @Provides
    MapFragmentMVP.Presenter providesMapFragmentPresenter(MapFragmentMVP.Model model)
    {
        return new MapFragmentPresenter(model);
    }

    @Provides
    MapFragmentMVP.Model providesMapFragmentModel ()
    {
        return new MapFragmentModel();
    }
}
