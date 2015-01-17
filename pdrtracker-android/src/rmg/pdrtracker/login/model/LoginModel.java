package rmg.pdrtracker.login.model;

import java.io.Serializable;

public class LoginModel implements Serializable {

    long id = -1;

    UserModel userModel = new UserModel();
    RequestNewUserModel requestNewUserModel = new RequestNewUserModel();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public RequestNewUserModel getRequestNewUserModel() {
        return requestNewUserModel;
    }

    public void setRequestNewUserModel(RequestNewUserModel requestNewUserModel) {
        this.requestNewUserModel = requestNewUserModel;
    }

}
