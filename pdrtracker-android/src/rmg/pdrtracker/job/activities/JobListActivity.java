package rmg.pdrtracker.job.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import rmg.pdrtracker.job.list.JobListFragment;
import rmg.pdrtracker.job.model.JobDetailsModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.login.activities.LoginActivity;
import rmg.pdrtracker.main_menu.activities.MainMenuActivity;
import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.R;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.LoginModelService;

public class JobListActivity extends Activity {

    public static String LOGTAG = "PdrTracker";

    private LoginModel loginModel;

    public JobListActivity() {
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

        setContentView(R.layout.main_activity);

        Point screenSize = getScreenSize();

        Log.d(LOGTAG, "Screen size x: " + screenSize.x + " y: " + screenSize.y);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayShowTitleEnabled(true);

        JobListFragment jobListFragment = new JobListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.attach(jobListFragment);
        ft.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:
                logout();
                return true;

            case R.id.main_menu_action:
                gotoMainMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void createJob() {

        Intent intent = new Intent(this, JobActivity.class);
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

    public void gotoMainMenu() {

        Intent intent = new Intent(this, MainMenuActivity.class);
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
        MenuItem mainMenuItem = menu.findItem(R.id.main_menu_action);
        mainMenuItem.setVisible(true);

        return true;
    }

}
