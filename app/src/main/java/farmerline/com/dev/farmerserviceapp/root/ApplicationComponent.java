package farmerline.com.dev.farmerserviceapp.root;


import javax.inject.Singleton;

import dagger.Component;
import farmerline.com.dev.farmerserviceapp.dashboard.home.Home;
import farmerline.com.dev.farmerserviceapp.dashboard.home.HomeModule;
import farmerline.com.dev.farmerserviceapp.dashboard.home.analytics.AnalyticsFragment;
import farmerline.com.dev.farmerserviceapp.dashboard.home.analytics.AnalyticsModule;
import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.MapFragment;
import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.MapModule;
import farmerline.com.dev.farmerserviceapp.login.LoginActivity;
import farmerline.com.dev.farmerserviceapp.login.LoginModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        LoginModule.class,
        HomeModule.class,
        AnalyticsModule.class,
        MapModule.class
})
public interface ApplicationComponent {

    void inject (LoginActivity target);

    void inject(Home home);

    void inject(AnalyticsFragment analyticsFragment);

    void inject(MapFragment mapFragment);
}
