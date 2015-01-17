package rmg.pdrtracker.main_menu.activities;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import rmg.pdrtracker.R;
import rmg.pdrtracker.job.activities.JobActivity;
import rmg.pdrtracker.job.activities.JobListActivity;
import rmg.pdrtracker.job.list.JobListFragment;
import rmg.pdrtracker.job.model.JobDetailsModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.login.activities.LoginActivity;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.LoginModelService;
import rmg.pdrtracker.main_menu.MainMenuFragment;
import rmg.pdrtracker.util.AppUtils;

import static rmg.pdrtracker.util.AppUtils.getLabelForEnum;

/**
 * Created with IntelliJ IDEA.
 * User: jennparise
 * Date: 5/8/14
 * Time: 8:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainMenuActivity extends Activity implements MainMenuFragment.OnCreateHailEstimateListener{
    public static String LOGTAG = "PdrTracker";

    private LoginModel loginModel;

    Fragment activeFragment;

    public MainMenuActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        DatabaseInitializer dbInit = new DatabaseInitializer();
//        dbInit.initJobList(this);
        LoginModelService loginModelService = LoginModelService.getInstance();
        loginModelService.init(this, savedInstanceState);
        loginModel = loginModelService.getLoginModel();



        AppUtils.newInstance(this);

        setContentView(R.layout.main_menu);

        Point screenSize = getScreenSize();

        Log.d(LOGTAG, "Screen size x: " + screenSize.x + " y: " + screenSize.y);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayShowTitleEnabled(true);

        MainMenuFragment mainMenuFragment = new MainMenuFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.attach(mainMenuFragment);
        ft.commit();

        setActiveFragment(mainMenuFragment);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public Fragment getActiveFragment() {
        return activeFragment;
    }

    public void setActiveFragment(Fragment activeFragment) {
        this.activeFragment = activeFragment;
    }

    public void logout() {

        Intent intent = new Intent(this, LoginActivity.class);
        Bundle bundle = new Bundle();

        JobModel jobModel = new JobModel();
        jobModel.setUserId(loginModel.getId());
        bundle.putSerializable("jobModel", jobModel);
        bundle.putSerializable("loginModel", loginModel);

        JobDetailsModel jobDetailsModel = new JobDetailsModel();
        jobModel.setJobDetailsModel(jobDetailsModel);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        return true;
    }

    public void createJob() {

        Intent intent = new Intent(this, rmg.pdrtracker.job.activities.JobActivity.class);
        Bundle bundle = new Bundle();

        JobModel jobModel = new JobModel();
        jobModel.setUserId(loginModel.getId());
        bundle.putSerializable("jobModel", jobModel);
        bundle.putSerializable("loginModel", loginModel);

        JobDetailsModel jobDetailsModel = new JobDetailsModel();
        jobModel.setJobDetailsModel(jobDetailsModel);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void OnCreateHailEstimate() {

        createJob();
    }




}