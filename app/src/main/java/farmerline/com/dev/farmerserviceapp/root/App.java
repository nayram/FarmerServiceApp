package farmerline.com.dev.farmerserviceapp.root;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;
import farmerline.com.dev.farmerserviceapp.dashboard.home.HomeModule;
import farmerline.com.dev.farmerserviceapp.dashboard.home.analytics.AnalyticsModule;
import farmerline.com.dev.farmerserviceapp.dashboard.home.mapLocation.MapModule;
import farmerline.com.dev.farmerserviceapp.login.LoginModule;
import farmerline.com.dev.farmerserviceapp.utils.Constant;
import io.fabric.sdk.android.Fabric;

public class App extends Application {

    private ApplicationComponent component;
    private static Context context;

    private static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context=this;
        sp= getSharedPreferences(Constant.AppPackage, Context.MODE_PRIVATE);

        component=DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .loginModule(new LoginModule())
                .homeModule(new HomeModule())
                .analyticsModule(new AnalyticsModule())
                .mapModule(new MapModule())
                .build();

    }

    public ApplicationComponent getComponent() {
        return component;
    }

    public static Context getContext()
    {
        return context;
    }

    public static SharedPreferences getSharedPrefs()
    {
        return sp;
    }
}
