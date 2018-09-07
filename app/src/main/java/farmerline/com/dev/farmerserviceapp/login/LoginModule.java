package farmerline.com.dev.farmerserviceapp.login;


import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {


    @Provides
    public LoginActivityMVP.Presenter providesLoginActivityPresenter (LoginActivityMVP.Model model)
    {
        return new LoginActivityPresenter(model);
    }

    @Provides
    public LoginActivityMVP.Model providesLoginActivityModel()
    {
        return new LoginModel();
    }

}
