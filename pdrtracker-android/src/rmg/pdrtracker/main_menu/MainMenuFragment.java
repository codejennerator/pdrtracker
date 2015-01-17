package rmg.pdrtracker.main_menu;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import rmg.pdrtracker.R;
import rmg.pdrtracker.db.JobDao;
import rmg.pdrtracker.db.LoginDao;
import rmg.pdrtracker.job.activities.JobActivity;
import rmg.pdrtracker.job.activities.JobListActivity;
import rmg.pdrtracker.job.activities.PrintJobActivity;
import rmg.pdrtracker.job.model.JobDetailsModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.login.constants.Login;
import rmg.pdrtracker.login.dialogPopUps.LoginErrorDialogFragment;
import rmg.pdrtracker.login.dialogPopUps.LoginInactiveErrorDialogFragment;
import rmg.pdrtracker.login.dialogPopUps.TokenExpiredDialogFragment;
import rmg.pdrtracker.login.loginUser.changePassword.ChangePasswordPanel;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.LoginModelService;
import rmg.pdrtracker.login.model.RequestNewUserModel;
import rmg.pdrtracker.login.model.UserModel;
import rmg.pdrtracker.login.requestNewUser.RequestNewUserPanel;
import rmg.pdrtracker.login.utils.AESEncryption;
import rmg.pdrtracker.login.utils.LoginService;
import rmg.pdrtracker.main_menu.activities.MainMenuActivity;
import rmg.pdrtracker.profile.activities.ProfileActivity;
import rmg.pdrtracker.util.AppUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainMenuFragment extends Fragment {

    private ScrollView root;

    OnCreateHailEstimateListener mCallbackCreateHailEstimate;


    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;

    private LoginModel loginModel;

    private Button btnProfile;
    private Button btnCreateHailEstimate;
    private Button btnEstimates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ScrollView) inflater.inflate(R.layout.main_menu_layout, container, false);

        Context context = root.getContext();
        LoginModelService loginModelService = LoginModelService.getInstance();
        loginModel = loginModelService.getLoginModel();

        relativeLayout = (RelativeLayout) root.getChildAt(0);
        //linearLayout.setDrawingCacheEnabled(true);

        initFields();

        // Importing all assets like buttons, text fields
        // Button Click Events

        btnCreateHailEstimate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                mCallbackCreateHailEstimate.OnCreateHailEstimate();
            }
        });

        btnEstimates.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //mCallbackGotoEstimates.OnGotoEstimates();
                Intent intent = new Intent(root.getContext(), JobListActivity.class);
                Bundle bundle = new Bundle();

                JobModel jobModel = new JobModel();
                bundle.putSerializable("jobModel", jobModel);
                bundle.putSerializable("loginModel", loginModel);

                JobDetailsModel jobDetailsModel = new JobDetailsModel();
                jobModel.setJobDetailsModel(jobDetailsModel);

                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //mCallbackGotoEstimates.OnGotoEstimates();
                Intent intent = new Intent(root.getContext(), ProfileActivity.class);
                Bundle bundle = new Bundle();

                JobModel jobModel = new JobModel();
                bundle.putSerializable("jobModel", jobModel);
                bundle.putSerializable("loginModel", loginModel);

                JobDetailsModel jobDetailsModel = new JobDetailsModel();
                jobModel.setJobDetailsModel(jobDetailsModel);

                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return root;

    }
    private void initFields() {

        btnProfile = (Button) root.findViewById(R.id.btnProfile);
        btnCreateHailEstimate = (Button) root.findViewById(R.id.btnCreateHailEstimate);
        btnEstimates = (Button) root.findViewById(R.id.btnEstimates);


    }

    public interface OnCreateHailEstimateListener {
        public void OnCreateHailEstimate();
    }

    public interface OnGotoEstimatesListener {
        public void OnGotoEstimates();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallbackCreateHailEstimate = (OnCreateHailEstimateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCreateHailEstimate");
        }

    }


}
