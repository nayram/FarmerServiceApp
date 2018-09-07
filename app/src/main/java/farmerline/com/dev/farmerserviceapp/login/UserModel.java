package farmerline.com.dev.farmerserviceapp.login;

public class UserModel {

    String name,email,loginType,authKey,image;

    public UserModel(String name, String email, String loginType, String authKey,String image) {
        this.name = name;
        this.email = email;
        this.loginType = loginType;
        this.authKey = authKey;
        this.image=image;
    }

    public UserModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
