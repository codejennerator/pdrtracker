package rmg.pdrtracker.login.registerUser;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
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
import rmg.pdrtracker.login.RequestsForNewUser.RequestsForNewUsersPanel;
import rmg.pdrtracker.login.constants.Login;
import rmg.pdrtracker.R;
import rmg.pdrtracker.db.LoginDao;
import rmg.pdrtracker.login.dialogPopUps.RegisterUserExistsErrorDialogFragment;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.LoginModelService;
import rmg.pdrtracker.login.model.UserModel;
import rmg.pdrtracker.login.dialogPopUps.NoInternetNoticeDialogFragment;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RegisterUserFragment extends Fragment{

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private ScrollView root;

    private ViewGroup viewGroup;

    OnRegisterUserListener mCallback;

    private UserModel userModel;

    private LoginModel loginModel;

    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;

    Button btnRegisterUser;
    Button btnViewRequestsForNewUsers;
    Button btnDeactivateUser;
    Button btnReactivateUser;
    Button btnResetUsersPassword;

    private RequestsForNewUsersPanel requestsForNewUsersPanel;

    private InputStream is = null;
    private JSONObject jObj = null;
    private String json = "";
    static List<NameValuePair> params;

    public void resetFields() {

        inputEmail.getText().clear();
        inputPassword.getText().clear();
        btnViewRequestsForNewUsers.setVisibility(View.VISIBLE);
        btnDeactivateUser.setVisibility(View.VISIBLE);
        btnReactivateUser.setVisibility(View.VISIBLE);
        btnResetUsersPassword.setVisibility(View.VISIBLE);
        btnRegisterUser.setVisibility(View.VISIBLE);
    }

    public void disableFields() {

        btnViewRequestsForNewUsers.setVisibility(View.INVISIBLE);
        btnDeactivateUser.setVisibility(View.INVISIBLE);
        btnReactivateUser.setVisibility(View.INVISIBLE);
        btnResetUsersPassword.setVisibility(View.INVISIBLE);
        btnRegisterUser.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ScrollView) inflater.inflate(R.layout.register_user_layout, container, false);

        viewGroup = container;

        requestsForNewUsersPanel = (RequestsForNewUsersPanel) root.findViewById(R.id.requests_for_new_user_panel);

        final Context context = root.getContext();
        initFields();

        btnRegisterUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                disableFields();
                HostAvailabilityTask task = new HostAvailabilityTask();
                task.execute(new String[]{""});

            }
        });

        btnViewRequestsForNewUsers.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                requestsForNewUsersPanel.toggleFlyIn();
                requestsForNewUsersPanel.bringToFront();
                requestsForNewUsersPanel.invalidate();

            }
        });

        btnDeactivateUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                disableFields();
                deactivateUser();
                resetFields();
            }
        });

        btnReactivateUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                disableFields();
                reactivateUser();
                resetFields();
            }
        });

        btnResetUsersPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                disableFields();
                resetUsersPassword();
                resetFields();
            }
        });

        return root;

    }

    private void initFields() {
        inputEmail = (EditText) root.findViewById(R.id.registrationUserName);
        inputPassword = (EditText) root.findViewById(R.id.registrationPassword);

        btnViewRequestsForNewUsers = (Button) root.findViewById(R.id.btnRequestNewUser);
        btnDeactivateUser = (Button) root.findViewById(R.id.btnDeactivateUser);
        btnReactivateUser = (Button) root.findViewById(R.id.btnReactivateUser);
        btnResetUsersPassword = (Button) root.findViewById(R.id.btnResetUsersPassword);
        btnRegisterUser = (Button) root.findViewById(R.id.btnRegistration);

        loginErrorMsg = (TextView) root.findViewById(R.id.registration_error);

    }
    private void saveUserInfo(Context context, String userName, String password) {

        LoginDao loginDao = new LoginDao(context);
        loginDao.open();
        LoginModel loginModel = new LoginModel();
        userModel = new UserModel();
        userModel.setUser(userName);
        userModel.setPassword(password);
        loginModel.setUserModel(userModel);
        loginDao.insertLogin(loginModel);
        loginDao.close();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        saveModel();
        LoginModelService modelService = LoginModelService.getInstance();
        modelService.saveModelState(savedInstanceState, loginModel);
    }

    public void saveModel() {

        //jenn UserModel(login.getText().toString());
    }

    public interface OnRegisterUserListener {
        public void OnRegisterUser();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnRegisterUserListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRegisterUser");
        }
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

                registerOnline();
                //String email = inputEmail.getText().toString();
                //String password = inputPassword.getText().toString();
                //UserFunctions userFunction = new UserFunctions();

                //String json = userFunction.registerUser("admin", email, password);
                //System.out.println("json----------------------------------------------------------------------------------------------------------------->"+json);

                //mCallback.OnRegisterUser();

            }  else{

                registerOnDevice();
                showNoticeDialog();
                mCallback.OnRegisterUser();
            }
        }
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NoInternetNoticeDialogFragment();
        dialog.show(getActivity().getFragmentManager(), "NoInternetNoticeDialogFragment");
    }

    private void registerOnline(){

        final Context context = root.getContext();

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        //UserFunctions userFunction = new UserFunctions();
        int i = 0;
        //String json = userFunction.loginUser(email, password);
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("tag", "register"));
        params2.add(new BasicNameValuePair("name", "tempName"));
        params2.add(new BasicNameValuePair("email", email));
        params2.add(new BasicNameValuePair("password", password));
        this.params = params2;

        registerOnDevice();
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[] {Login.LOGIN_URL.getLabel()});
        // return JSON String



    }

    private void reactivateUser(){

        String userName = inputEmail.getText().toString();

        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("tag", "reactivate_user"));
        params2.add(new BasicNameValuePair("user_name", userName));

        this.params = params2;

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[] {Login.LOGIN_URL.getLabel()});
        // return JSON String
    }

    private void resetUsersPassword(){

        String userName = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("tag", "reset_users_password"));
        params2.add(new BasicNameValuePair("user_name", userName));
        params2.add(new BasicNameValuePair("new_password", password));

        this.params = params2;

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[] {Login.LOGIN_URL.getLabel()});
        // return JSON String
    }

    private void deactivateUser(){

        String userName = inputEmail.getText().toString();

        List<NameValuePair> params2 = new ArrayList<NameValuePair>();

        params2.add(new BasicNameValuePair("tag", "deactivate_user"));
        params2.add(new BasicNameValuePair("user_name", userName));

        this.params = params2;

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[] {Login.LOGIN_URL.getLabel()});
        // return JSON String
    }

    private void registerOnDevice(){

        final Context context = root.getContext();

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        saveUserInfo(context, email, password);


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
                } catch (Exception e) {
                    registerOnDevice();
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }finally {
                    // urlConnection.disconnect();
                    is.close();
                    urlConnection.disconnect();
                    System.out.println("response4 ---------------------------------------------------------------------------------------------------------------------------------------------->"+response);

                    return response;
                }
            } catch (UnsupportedEncodingException e) {
                registerOnDevice();
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                registerOnDevice();
                e.printStackTrace();
            } catch (IOException e) {
                registerOnDevice();
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            final Context context = root.getContext();
            json = result;
            System.out.println("json----------------------------------------------------------------------------------------------------------------->"+json);
            if(json.contains(Login.REGISTER_USER_EXISTS_ERROR.getLabel())){
                showRegisterUserExistsErrorDialog();
                resetFields();
                return;
            }

            mCallback.OnRegisterUser();

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

    public void showRegisterUserExistsErrorDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new RegisterUserExistsErrorDialogFragment();
        dialog.show(getActivity().getFragmentManager(), "LoginErrorDialogFragment");
    }
}


