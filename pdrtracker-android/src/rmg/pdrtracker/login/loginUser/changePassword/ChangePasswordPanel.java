package rmg.pdrtracker.login.loginUser.changePassword;


import android.app.DialogFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rmg.pdrtracker.R;
import rmg.pdrtracker.db.LoginDao;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.model.AddInfoModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.job.model.ModelService;
import rmg.pdrtracker.login.RequestsForNewUser.RequestsForNewUserTable;
import rmg.pdrtracker.login.constants.Login;
import rmg.pdrtracker.login.dialogPopUps.LoginErrorDialogFragment;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.RequestNewUserModel;
import rmg.pdrtracker.login.utils.LoginService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static rmg.pdrtracker.util.AppUtils.toDip;

/**
 * Panel for viewing people waiting to be contacted about setting up a subscription
 */
public class ChangePasswordPanel extends RelativeLayout {

    private boolean isVisible;

    private LoginService loginService = LoginService.get();

    private int width;

    private LinearLayout root;

    private ChangePasswordForm changePasswordForm;

    static List<NameValuePair> params;

    private InputStream is = null;

    private String json = "";

    private boolean postExecuteFinished = false;

    private boolean flyInSelected = false;

    private Button btnDone;


    /**
     * Preferred to animate a view View Property Animator
     * view.animate().setDuration(2000);
     * view.animate().translationX(-100);
     * view.animate().translationY(-100);
     * <p/>
     * Another way to animate if View Property Animator is not an option:
     * PropertyValuesHolder pvhx = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -100F);
     * PropertyValuesHolder pvhy = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -100F);
     * ObjectAnimator.ofPropertyValuesHolder(view, pvhx, pvhy).start();
     */
    public ChangePasswordPanel(Context context) {
        super(context);
    }

    public ChangePasswordPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChangePasswordPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs);
    }

    private void init(AttributeSet attrs) {



        Context context = getContext();

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RequestsForNewUsersPanel);

        width = a.getDimensionPixelSize(R.styleable.RequestsForNewUsersPanel_android_layout_width, 0);
        a.recycle();

        setY(0);
        setX(-width);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prevents click bleed through
            }
        });

        ScrollView scrollView = new ScrollView(context);
        addView(scrollView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.setBackgroundColor(getResources().getColor(R.drawable.panel_background));

        root = new LinearLayout(context);
        scrollView.addView(root, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(getResources().getColor(R.drawable.label_background));

        createRequestNewUserForm(context);

        LinearLayout bottomRow = createBottomRow(context);
        createDoneButton(context, bottomRow);

        ChangePasswordPanel.this.setVisibility(INVISIBLE);
        changePasswordForm.setVisibility(INVISIBLE);


    }

    private void createRequestNewUserForm(Context context) {
        changePasswordForm = new ChangePasswordForm(context);
        root.addView(changePasswordForm, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void saveRequestNewUserInfo(String name, String email, String phone) {

        LoginDao loginDao = new LoginDao(getContext());
        loginDao.open();
        LoginModel loginModel = new LoginModel();
        RequestNewUserModel requestNewUserModel= new RequestNewUserModel();
        requestNewUserModel.setName(name);
        requestNewUserModel.setEmail(email);
        requestNewUserModel.setPhone(phone);
        loginModel.setRequestNewUserModel(requestNewUserModel);
        loginDao.insertLogin(loginModel);
        loginDao.close();
    }


    /**
     * Row at the bottome which contains submit button
     */
    private LinearLayout createBottomRow(Context context) {
        LinearLayout horizLayout = new LinearLayout(context);
        root.addView(horizLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        horizLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizLayout.setPadding(10, 20, 0, 0);
        return horizLayout;
    }

    private Button createDoneButton(Context context, LinearLayout parent) {

        btnDone = new Button(context);
        parent.addView(btnDone, new LinearLayout.LayoutParams(toDip(150), toDip(40)));
        btnDone.setPadding(0, 0, 0, 0);
        btnDone.offsetTopAndBottom(0);
        btnDone.setText("Done");
        btnDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                btnDone.setVisibility(View.INVISIBLE);
                if(loginService.getChangePasswordCurrentPassword()!= null && loginService.getChangePasswordNewPassword() != null && loginService.getChangePasswordUserName() != null){
                    if(loginService.getChangePasswordCurrentPassword().length() > 0 && loginService.getChangePasswordNewPassword().length() > 0 && loginService.getChangePasswordUserName().length() > 0){
                        //saveRequestNewUserInfo(loginService.getChangePasswordCurrentPassword(), loginService.getChangePasswordNewPassword(), loginService.getChangePasswordUserName());
                        changeUserPassword();
                    }
                }
                ChangePasswordPanel.this.flyOut();
                btnDone.setVisibility(View.VISIBLE);
            }
        });
        return btnDone;
    }

    public void flyOut() {
        animate().translationX(-width);
        isVisible = false;
    }

    public void toggleFlyIn() {

        changePasswordForm.reset();
        changePasswordForm.showChangePasswordForm();

        if (changePasswordForm.getSize() != 0) {
            ChangePasswordPanel.this.setVisibility(VISIBLE);
            changePasswordForm.setVisibility(VISIBLE);
        } else {
            changePasswordForm.setVisibility(INVISIBLE);
        }

        if (isVisible) {
            animate().translationX(-width);
        } else {
            animate().translationX(0);
        }

        isVisible = !isVisible;

    }

    private void changeUserPassword(){

        final Context context = root.getContext();

        String userName = loginService.getChangePasswordUserName();
        String currentPassword = loginService.getChangePasswordCurrentPassword();
        String newPassword = loginService.getChangePasswordNewPassword();

        int i = 0;
        //String json = userFunction.loginUser(email, password);
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("tag", "change_user_password"));
        params2.add(new BasicNameValuePair("user_name", userName));
        params2.add(new BasicNameValuePair("current_password", currentPassword));
        params2.add(new BasicNameValuePair("new_password", newPassword));
        this.params = params2;

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[] {Login.LOGIN_URL.getLabel()});
        // return JSON String

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
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    response = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }finally {
                    // urlConnection.disconnect();
                    is.close();
                    urlConnection.disconnect();
                    System.out.println("response4 ---------------------------------------------------------------------------------------------------------------------------------------------->"+response);

                    return response;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            final Context context = root.getContext();
            json = result;
            System.out.println("json----------------------------------------------------------------------------------------------------------------->"+json);
            postExecuteFinished = true;
            if(json.contains(Login.LOGIN_ERROR.getLabel())){
                showLoginErrorDialog();
                return;
            }
            if(flyInSelected){
                toggleFlyIn();
            }
           // mCallback.OnRequestNewUser();

        }
    }

    public void showLoginErrorDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new LoginErrorDialogFragment();
        dialog.show(loginService.getTheActivity().getFragmentManager(), "LoginErrorDialogFragment");
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

