package farmerline.com.dev.farmerserviceapp.dashboard.home.analytics;


import dagger.Module;
import dagger.Provides;

@Module
public class AnalyticsModule {

    @Provides
    AnalyticsFragmentMVP.Presenter providesAnalyticsFragmentPresenter (AnalyticsFragmentMVP.Model model)
    {
        return new AnalyticsFragmentPresenter(model);
    }


    @Provides
    AnalyticsFragmentMVP.Model providesAnalyticsModel()
    {
        return new AnalyticsModel();
    }
}
