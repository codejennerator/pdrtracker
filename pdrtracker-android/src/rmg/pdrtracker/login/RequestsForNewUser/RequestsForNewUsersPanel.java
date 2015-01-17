package rmg.pdrtracker.login.RequestsForNewUser;


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
import rmg.pdrtracker.login.constants.Login;
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
public class RequestsForNewUsersPanel extends RelativeLayout {

    private boolean isVisible;

    private LoginService loginService = LoginService.get();

    private int width;

    private LinearLayout root;

    private RequestsForNewUserTable requestsForNewUsersTable;

    static List<NameValuePair> params;

    private InputStream is = null;

    private String json = "";

    private boolean postExecuteFinished = false;

    private boolean flyInSelected = false;


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
    public RequestsForNewUsersPanel(Context context) {
        super(context);
    }

    public RequestsForNewUsersPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RequestsForNewUsersPanel(Context context, AttributeSet attrs, int defStyle) {
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

        RequestsForNewUsersPanel.this.setVisibility(INVISIBLE);
        requestsForNewUsersTable.setVisibility(INVISIBLE);


    }

    private void createRequestNewUserForm(Context context) {
        requestsForNewUsersTable = new RequestsForNewUserTable(context);
        root.addView(requestsForNewUsersTable, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                if(loginService.getUsersContactedHS().size() > 0){
                    updateContactedClients();
                }
                RequestsForNewUsersPanel.this.flyOut();
            }
        });
        return button;
    }

    public void flyOut() {
        animate().translationX(-width);
        isVisible = false;
    }

    public void toggleFlyIn() {

        requestsForNewUsersTable.reset();
        flyInSelected = true;

        if(!postExecuteFinished) {
            getRequestsForNewUsers();
        }else{
            try{

                System.out.println("jsonObjectBefore----------------------------------------------------------------------------------------------------------------->");
                String[] splitResultsHeader = json.split("\\{", 2);
                JSONObject jObject = new JSONObject("{"+splitResultsHeader[1]);
                System.out.println("jsonObject----------------------------------------------------------------------------------------------------------------->"+jObject);

                JSONArray jArray = jObject.getJSONArray("results");
                for (int i=0; i < jArray.length(); i++)
                {

                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    String id = oneObject.getString("uid");
                    String name = oneObject.getString("name");
                    String phone = oneObject.getString("phone_number");
                    String email = oneObject.getString("email");
                    requestsForNewUsersTable.loadRequests(name, email, phone, Integer.parseInt(id));
                System.out.println("jsonArray----------------------------------------------------------------------------------------------------------------->"+name+"  "+phone+"  "+email);
                }
            }catch(JSONException e){
                System.out.println("excpetion----------------------------------------------------------------------------------------------------------------->"+e);

                Log.println(1,"Error: ", "error parsing json"+e);

            }
            //String[] splitResultsHeader = json.split("results");
            //String[] splitResultsRows = splitResultsHeader[1].split("\\}");
            //System.out.println("json2results----------------------------------------------------------------------------------------------------------------->"+splitResultsRows[1]);

            if (requestsForNewUsersTable.getSize() != 0) {
                RequestsForNewUsersPanel.this.setVisibility(VISIBLE);
                requestsForNewUsersTable.setVisibility(VISIBLE);
            } else {
                requestsForNewUsersTable.setVisibility(INVISIBLE);
            }

            if (isVisible) {
                animate().translationX(-width);
            } else {
                animate().translationX(0);
            }

            isVisible = !isVisible;
            postExecuteFinished = false;
            flyInSelected = false;
        }
    }

    public void clearAddInfoPanel(CarArea carArea) {

        ModelService modelService = ModelService.getInstance();
        JobModel jobModel = modelService.getJobModel();
        AddInfoModel addInfoModel = jobModel.getAddInfoModel();       //jenn change when have model


    }

    private void updateContactedClients(){

        String idString = "";
        final Context context = root.getContext();
        for(String id : loginService.getUsersContactedHS()){
            idString += id+" || id = ";
        }
        idString = idString.substring(0, idString.length() - 9);
        int i = 0;
        //String json = userFunction.loginUser(email, password);
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("tag", "update_contacted_clients"));
        params2.add(new BasicNameValuePair("ids", idString));

        this.params = params2;

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[]{Login.LOGIN_URL.getLabel()});
        // return JSON String
    }

    private void getRequestsForNewUsers(){

        final Context context = root.getContext();
        System.out.println("here");
        int i = 0;
        //String json = userFunction.loginUser(email, password);
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("tag", "get_new_user_requests"));

        this.params = params2;

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[]{Login.LOGIN_URL.getLabel()});
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
            if(flyInSelected){
                toggleFlyIn();
            }
            else{
                postExecuteFinished = false;
                loginService.clearUsersContactedHS();
            }
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

