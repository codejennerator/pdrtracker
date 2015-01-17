package rmg.pdrtracker.login.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jenn
 * Date: 10/8/13
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserModel implements Serializable {

    String user;
    String password;
    String token;
    long lastLoginTime;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

}
