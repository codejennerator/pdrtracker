package rmg.pdrtracker.login.utils;


import android.app.Activity;
import android.widget.Button;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.constants.DamageClassifier;
import rmg.pdrtracker.job.constants.DentSize;
import rmg.pdrtracker.job.damagematrix.DamageMatrixFragment;
import rmg.pdrtracker.job.damagematrix.DentDamageKey;
import rmg.pdrtracker.job.model.AddInfoItemModel;
import rmg.pdrtracker.job.model.AddInfoModel;
import rmg.pdrtracker.job.model.DentDamageModel;
import rmg.pdrtracker.job.model.PriceEstimateModel;
import rmg.pdrtracker.job.prices.DentPriceMatrix;
import rmg.pdrtracker.login.loginUser.LoginUserFragment;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.RequestNewUserModel;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;


public class LoginService {

    private static final LoginService singleton = new LoginService();

    private LoginUserFragment loginUserFragment;

    LoginModel loginModel;

    private String requestNewUserName;
    private String requestNewUserPhone;
    private String requestNewUserEmail;
    private String changePasswordNewPassword;
    private String changePasswordCurrentPassword;
    private String changePasswordUserName;

    HashSet<String> usersContactedHS = new HashSet<String>();

    RequestNewUserModel requestNewUserModel;

    private Activity activity;

    private LoginService() {
    }

    public static LoginService get() {
        return singleton;
    }

    public void setRequestNewUserName(String name){

        this.requestNewUserName = name;
    }

    public String getRequestNewUserName(){
        return requestNewUserName;
    }

    public void setRequestNewUserPhone(String phone){

        this.requestNewUserPhone = phone;
    }

    public String getRequestNewUserPhone(){
        return requestNewUserPhone;
    }

    public void setRequestNewUserEmail(String email){

        this.requestNewUserEmail = email;
    }

    public String getRequestNewUserEmail(){
        return requestNewUserEmail;
    }

    public void addUserContactedToHS(String userContacted){
        System.out.println("in add user contacted "+userContacted);
        this.usersContactedHS.add(userContacted);
    }

    public void removeUserContactedFromHS(String userContacted){

        this.usersContactedHS.remove(userContacted);
    }

    public HashSet<String> getUsersContactedHS(){
        return usersContactedHS;
    }

    public void clearUsersContactedHS(){
        this.usersContactedHS.clear();
    }

    public void setChangePasswordUserName(String changePasswordUserName){

        this.changePasswordUserName = changePasswordUserName;
    }

    public String getChangePasswordUserName(){
        return changePasswordUserName;
    }

    public void setChangePasswordCurrentPassword(String changePasswordCurrentPassword){

        this.changePasswordCurrentPassword = changePasswordCurrentPassword;
    }

    public String getChangePasswordCurrentPassword(){
        return changePasswordCurrentPassword;
    }

    public void setChangePasswordNewPassword(String changePasswordNewPassword){

        this.changePasswordNewPassword = changePasswordNewPassword;
    }

    public String getChangePasswordNewPassword(){
        return changePasswordNewPassword;
    }


    public void setTheActivity(Activity activity){

        this.activity = activity;
    }

    public Activity getTheActivity(){

        return activity;
    }


}
