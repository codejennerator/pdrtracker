package rmg.pdrtracker.login.requestNewUser;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import rmg.pdrtracker.R;
import rmg.pdrtracker.db.LoginDao;
import rmg.pdrtracker.job.constants.AddInfo;
import rmg.pdrtracker.job.constants.AddInfoItemType;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.damagematrix.additionalinfo.AddInfoTable;
import rmg.pdrtracker.job.model.AddInfoItemModel;
import rmg.pdrtracker.job.model.AddInfoModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.job.model.ModelService;
import rmg.pdrtracker.login.constants.Login;
import rmg.pdrtracker.login.loginUser.LoginUserFragment;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.RequestNewUserModel;
import rmg.pdrtracker.login.model.UserModel;
import rmg.pdrtracker.login.registerUser.RegisterUserFragment;
import rmg.pdrtracker.login.utils.LoginService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static rmg.pdrtracker.util.AppUtils.toDip;

/**
 * Panel for requesting to be contacted about getting a subscription for the app
 */
public class RequestNewUserPanel extends RelativeLayout {

    private boolean isVisible;

    private LoginService loginService = LoginService.get();

    private int width;

    private LinearLayout root;

    private RequestNewUserForm requestNewUserForm;

    static List<NameValuePair> params;

    private InputStream is = null;

    private String json = "";


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
    public RequestNewUserPanel(Context context) {
        super(context);
    }

    public RequestNewUserPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RequestNewUserPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs);
    }

    private void init(AttributeSet attrs) {



        Context context = getContext();

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RequestNewUserPanel);

        width = a.getDimensionPixelSize(R.styleable.RequestNewUserPanel_android_layout_width, 0);
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

        RequestNewUserPanel.this.setVisibility(INVISIBLE);
        requestNewUserForm.setVisibility(INVISIBLE);
        requestNewUserForm.loadForm();

    }

    private void createRequestNewUserForm(Context context) {
        requestNewUserForm = new RequestNewUserForm(context);
        root.addView(requestNewUserForm, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
        Button button = new Button(context);
        parent.addView(button, new LinearLayout.LayoutParams(toDip(150), toDip(40)));
        button.setPadding(0, 0, 0, 0);
        button.offsetTopAndBottom(0);
        button.setText("Done");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            if(loginService.getRequestNewUserName()!= null && loginService.getRequestNewUserPhone() != null && loginService.getRequestNewUserEmail() != null){
                if(loginService.getRequestNewUserName().length() > 0 && loginService.getRequestNewUserPhone().length() > 0 && loginService.getRequestNewUserEmail().length() > 0){
                    saveRequestNewUserInfo(loginService.getRequestNewUserName(), loginService.getRequestNewUserEmail(), loginService.getRequestNewUserPhone());
                    requestNewUser();
                }
            }
            RequestNewUserPanel.this.flyOut();
            }
        });
        return button;
    }

    public void flyOut() {
        animate().translationX(-width);
        isVisible = false;
    }

    public void toggleFlyIn() {

        if (requestNewUserForm.getSize() != 0) {
            RequestNewUserPanel.this.setVisibility(VISIBLE);
            requestNewUserForm.setVisibility(VISIBLE);
        } else {
            requestNewUserForm.setVisibility(INVISIBLE);
        }

        if (isVisible) {
            animate().translationX(-width);
        } else {
            animate().translationX(0);
        }

        isVisible = !isVisible;

    }

    public void clearAddInfoPanel(CarArea carArea) {

        ModelService modelService = ModelService.getInstance();
        JobModel jobModel = modelService.getJobModel();
        AddInfoModel addInfoModel = jobModel.getAddInfoModel();       //jenn change when have model


    }

    private void requestNewUser(){

        final Context context = root.getContext();

        String name = loginService.getRequestNewUserName();
        String email = loginService.getRequestNewUserEmail();
        String phone = loginService.getRequestNewUserPhone();

        int i = 0;
        //String json = userFunction.loginUser(email, password);
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("tag", "new_user_request"));
        params2.add(new BasicNameValuePair("email", email));
        params2.add(new BasicNameValuePair("name", name));
        params2.add(new BasicNameValuePair("phone", phone));
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

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        response += line;
                    }
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
           // mCallback.OnRequestNewUser();

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

