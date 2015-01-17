package rmg.pdrtracker.login.loginUser;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import rmg.pdrtracker.job.model.JobDetailsModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.login.constants.Login;
import rmg.pdrtracker.login.model.RequestNewUserModel;
import rmg.pdrtracker.login.model.UserModel;
import rmg.pdrtracker.R;
import rmg.pdrtracker.db.LoginDao;
import rmg.pdrtracker.login.dialogPopUps.LoginErrorDialogFragment;
import rmg.pdrtracker.login.dialogPopUps.LoginInactiveErrorDialogFragment;
import rmg.pdrtracker.login.dialogPopUps.TokenExpiredDialogFragment;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.LoginModelService;
import rmg.pdrtracker.login.requestNewUser.RequestNewUserPanel;
import rmg.pdrtracker.login.loginUser.changePassword.ChangePasswordPanel;
import rmg.pdrtracker.login.utils.AESEncryption;
import rmg.pdrtracker.login.utils.LoginService;
import rmg.pdrtracker.main_menu.activities.MainMenuActivity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LoginUserFragment extends Fragment {

    private ScrollView root;

    private UserModel userModel;

    private LoginModel loginModel;

    private RequestNewUserModel requestNewUserModel;

    private List<LoginModel> loginList;

    private LoginService loginService = LoginService.get();

    private RequestNewUserPanel requestNewUserPanel;
    private ChangePasswordPanel changePasswordPanel;

    private boolean userRegistered = false;

    OnRegisterAdminLoginListener mCallback;

    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    Button btnRequestNewUser;
    Button btnLogin;
    Button btnChangePassword;

    //seven day expireTime
    private static int EXPIRE_TIME_IN_MILLISECS = 604800000;

    private InputStream is = null;
    private  JSONObject jObj = null;
    //static String json = "";
    private String json = "";
    static List<NameValuePair> params;

    byte[] iv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loginService.setTheActivity(getActivity());

        root = (ScrollView) inflater.inflate(R.layout.login_user_layout, container, false);

        requestNewUserPanel = (RequestNewUserPanel) root.findViewById(R.id.request_new_user_panel);
        changePasswordPanel = (ChangePasswordPanel) root.findViewById(R.id.change_password_panel);

        final Context context = root.getContext();

        initLoginList(context);

        initFields();
        // Importing all assets like buttons, text fields
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                disableFields();
                HostAvailabilityTask task = new HostAvailabilityTask();
                task.execute(new String[]{""});
            }
        });

        btnRequestNewUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                disableFields();
                requestNewUserPanel.toggleFlyIn();
                requestNewUserPanel.bringToFront();
                requestNewUserPanel.invalidate();
                reEnableFields();
                // root.invalidate();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                disableFields();
                changePasswordPanel.toggleFlyIn();
                changePasswordPanel.bringToFront();
                changePasswordPanel.invalidate();
                reEnableFields();
                // root.invalidate();
            }
        });

        return root;
    }

    private void initFields() {

        inputEmail = (EditText) root.findViewById(R.id.loginEmail);
        btnLogin = (Button) root.findViewById(R.id.btnLogin);
        btnChangePassword = (Button) root.findViewById(R.id.btnChangePassword);
        btnRequestNewUser = (Button) root.findViewById(R.id.btnRequestNewUser);

        inputPassword = (EditText) root.findViewById(R.id.loginPassword);
        loginErrorMsg = (TextView) root.findViewById(R.id.login_error);

    }

    private void initLoginList(Context context) {
        LoginDao loginDao = new LoginDao(context);
        loginDao.open();
        loginList = loginDao.selectAll();
        loginDao.close();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        saveModel();
        LoginModelService loginModelService = LoginModelService.getInstance();
        loginModelService.saveModelState(savedInstanceState, loginModel);
    }

    public void saveModel() {
        LoginDao loginDao = new LoginDao(root.getContext());
        loginDao.open();
        loginDao.saveLogin(loginModel);
        loginDao.close();
    }

    public interface OnRegisterAdminLoginListener {
        public void OnRegisterAdminLogin();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnRegisterAdminLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRegisterUser");
        }
    }

    public void reEnableFields() {

        btnChangePassword.setVisibility(View.VISIBLE);
        btnRequestNewUser.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);

    }

    public void disableFields() {

        btnChangePassword.setVisibility(View.INVISIBLE);
        btnRequestNewUser.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);

    }

    public class HostAvailabilityTask extends AsyncTask<String, Void, Boolean> {

        protected Boolean doInBackground(String... params) {

            return isOnline();

        }
        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {



            if(isConnected) {

                loginOnline();

            }  else{

                loginOnDevice();
            }
        }
    }
    public void showLoginErrorDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new LoginErrorDialogFragment();
        dialog.show(getActivity().getFragmentManager(), "LoginErrorDialogFragment");
    }

    public void showLoginInactiveError() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new LoginInactiveErrorDialogFragment();
        dialog.show(getActivity().getFragmentManager(), "LoginInactiveErrorDialogFragment");
    }

    public void showTokenExpiredDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new TokenExpiredDialogFragment();
        dialog.show(getActivity().getFragmentManager(), "TokenExpiredDialogFragment");
    }

    private void loginOnline(){

        final Context context = root.getContext();

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        // UserFunctions userFunction = new UserFunctions();
        int i = 0;
        //String json = userFunction.loginUser(email, password);
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("tag", "login"));
        params2.add(new BasicNameValuePair("email", email));
        params2.add(new BasicNameValuePair("password", password));
        this.params = params2;


        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[] {Login.LOGIN_URL.getLabel()});
        // return JSON String



    }

    private void loginOnDevice(){

        final Context context = root.getContext();

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        for(LoginModel aLoginModel : loginList) {

            if(email.equalsIgnoreCase(aLoginModel.getUserModel().getUser()) && password.equals(aLoginModel.getUserModel().getPassword())){

                loginModel = aLoginModel;
                System.out.println("logged in user1--------------------********************************************--------->"+loginModel.getUserModel().getUser());

                if(loginModel.getUserModel().getUser().equals(Login.LOGIN_REGISTRATION_ADMIN.getLabel())){
                    System.out.println("registration admin--------------------********************************************--------->"+loginModel.getUserModel().getUser());
                    mCallback.OnRegisterAdminLogin();

                }
                else{
                    System.out.println("logged in user2--------------------********************************************--------->"+loginModel.getUserModel().getUser());

                    AESEncryption encryption = new AESEncryption();
                    String decryptedString = encryption.decryptString(loginModel.getUserModel().getToken());
                    System.out.println("decrypted text               "+decryptedString);
                    System.out.println("user--------------------********************************************--------->"+loginModel.getUserModel().getUser()+loginModel.getUserModel().getToken());

                    if(loginModel.getUserModel().getLastLoginTime() > System.currentTimeMillis()){
                        showTokenExpiredDialog();
                        return;
                    }
                    if(Long.parseLong(decryptedString) <= System.currentTimeMillis()){
                        showTokenExpiredDialog();
                        reEnableFields();
                        return;
                    }
                    loginModel.getUserModel().setLastLoginTime(System.currentTimeMillis());
                    saveModel();
                    Intent intent = new Intent(context, MainMenuActivity.class);
                    Bundle bundle = new Bundle();

                    JobModel jobModel = new JobModel();
                    bundle.putSerializable("jobModel", jobModel);
                    bundle.putSerializable("loginModel", loginModel);

                    JobDetailsModel jobDetailsModel = new JobDetailsModel();
                    jobModel.setJobDetailsModel(jobDetailsModel);

                    intent.putExtras(bundle);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                    reEnableFields();
                }

            } else{

                showLoginErrorDialog();
                reEnableFields();
            }
        }
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            BufferedReader reader = null;

            // Making HTTP request
            try {
                // defaultHttpClient
                // DefaultHttpClient httpClient = new DefaultHttpClient();
                URL url = new URL(urls[0]);



                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();
                // give it 15 seconds to respond
                urlConnection.setReadTimeout(15*1000);
                urlConnection.connect();
                try{
                    is = urlConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        response += line;
                    }
                   // loginOnDevice();
                } catch (Exception e) {
                    loginOnDevice();
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }finally {
                    // urlConnection.disconnect();
                    if(is != null){
                        is.close();

                        urlConnection.disconnect();
                    }
                    System.out.println("response4 ---------------------------------------------------------------------------------------------------------------------------------------------->"+response);

                    return response;
                }
            } catch (UnsupportedEncodingException e) {
                loginOnDevice();
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                loginOnDevice();
                e.printStackTrace();
            } catch (IOException e) {
                loginOnDevice();
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            json = result;
            System.out.println("json------------------------------------------------------------------------------------------------*******----------------->"+json);
            if(json.contains(Login.LOGIN_ERROR.getLabel())){
                showLoginErrorDialog();
                reEnableFields();
                return;
            }
            if(json.contains(Login.LOGIN_INACTIVE_ERROR.getLabel())){
                showLoginInactiveError();
                reEnableFields();
                return;
            }

            findUser();
        }
    }

    private void findUser(){

        String email = inputEmail.getText().toString();
        Boolean foundUser = false;

        for(LoginModel aLoginModel : loginList) {

            if(email.equalsIgnoreCase(aLoginModel.getUserModel().getUser())){

                loginModel = aLoginModel;
                foundUser = true;

                System.out.println("logged in user online1--------------------********************************************--------->"+loginModel.getUserModel().getUser());

                doLogin();
                userRegistered = false;
                return;
            }
        }
        if(!foundUser && !userRegistered){
            System.out.println("user not found--------------------********************************************--------->"+email);

            registerOnDevice();
        }
    }


    private void registerOnDevice(){

        final Context context = root.getContext();

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        saveUserInfo(context, email, password);
        userRegistered = true;
        initLoginList(context);
        findUser();


    }

    private void saveUserInfo(Context context, String userName, String password) {

        LoginDao loginDao = new LoginDao(context);
        loginDao.open();
        LoginModel saveUserLoginModel = new LoginModel();
        userModel = new UserModel();
        userModel.setUser(userName);
        userModel.setPassword(password);
        saveUserLoginModel.setUserModel(userModel);
        loginDao.insertLogin(saveUserLoginModel);
        loginDao.close();
    }

    private void doLogin(){

        final Context context = root.getContext();
        Intent intent = new Intent(context, MainMenuActivity.class);
        Bundle bundle = new Bundle();

        JobModel jobModel = new JobModel();
        bundle.putSerializable("jobModel", jobModel);

        if(loginModel.getUserModel().getUser().equals(Login.LOGIN_REGISTRATION_ADMIN.getLabel())){

            mCallback.OnRegisterAdminLogin();
            reEnableFields();

        }
        else{
            System.out.println("logged in user online2--------------------********************************************--------->"+loginModel.getUserModel().getUser()+Login.LOGIN_REGISTRATION_ADMIN);

            AESEncryption encryption = new AESEncryption();
            String encryptedString = encryption.encryptString((System.currentTimeMillis()+EXPIRE_TIME_IN_MILLISECS)+"");
            System.out.println("encypted text               " + encryptedString);
            loginModel.getUserModel().setToken(encryptedString);
            loginModel.getUserModel().setLastLoginTime(System.currentTimeMillis());

            saveModel();

            bundle.putSerializable("loginModel", loginModel);

            JobDetailsModel jobDetailsModel = new JobDetailsModel();
            jobModel.setJobDetailsModel(jobDetailsModel);

            intent.putExtras(bundle);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
            reEnableFields();
        }
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}