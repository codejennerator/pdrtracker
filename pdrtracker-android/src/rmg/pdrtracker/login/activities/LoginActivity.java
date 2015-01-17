package rmg.pdrtracker.login.activities;


import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import rmg.pdrtracker.login.loginUser.LoginUserTabListener;
import rmg.pdrtracker.login.registerUser.RegisterUserTabListener;
import rmg.pdrtracker.R;
import rmg.pdrtracker.login.registerUser.RegisterUserFragment;
import rmg.pdrtracker.login.loginUser.LoginUserFragment;
import rmg.pdrtracker.util.AppUtils;
import static rmg.pdrtracker.login.activities.LoginActivity.TabNames.*;

import static rmg.pdrtracker.util.AppUtils.getLabelForEnum;

/**
 * Created with IntelliJ IDEA.
 * User: jenn
 * Date: 10/8/13
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends Activity implements RegisterUserFragment.OnRegisterUserListener, LoginUserFragment.OnRegisterAdminLoginListener{


    public enum TabNames {
        LOGIN_USER,
        REGISTER_USER
    }

    TabNames activeTab = LOGIN_USER;

    private ActionBar.Tab loginTab;
    private ActionBar.Tab registerUserTab;

    Fragment activeFragment;

    public static String LOGTAG = "PdrTracker";


    private LoginUserTabListener<LoginUserFragment> loginUserTabListener;
    private RegisterUserTabListener<RegisterUserFragment> registerUserTabListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppUtils.newInstance(this);
        Point screenSize = getScreenSize();
        Log.d(LOGTAG, "Screen size x: " + screenSize.x + " y: " + screenSize.y);

        setContentView(R.layout.login_user_layout);

        setContentView(R.layout.login_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        loginTab = actionBar.newTab();
        loginTab.setText(getLabelForEnum(LOGIN_USER, "tab_label"));
        loginUserTabListener = new LoginUserTabListener<LoginUserFragment>(this, LOGIN_USER.toString(), LoginUserFragment.class);
        loginTab.setTabListener(loginUserTabListener);
        actionBar.addTab(loginTab);

        //jenn commenting out but leaving here to uncomment if a new registration admin needs to be created
       /* registerUserTab = getActionBar().newTab();
        registerUserTab.setText(getLabelForEnum(REGISTER_USER, "tab_label"));
        registerUserTabListener = new RegisterUserTabListener<RegisterUserFragment>(this, REGISTER_USER.toString(), RegisterUserFragment.class);
        registerUserTab.setTabListener(registerUserTabListener);
        actionBar.addTab(registerUserTab);  */




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_job_action:
                // saveJob();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    public void setActiveTab(TabNames activeTab) {
        this.activeTab = activeTab;
    }

    public Fragment getActiveFragment() {
        return activeFragment;
    }

    public void setActiveFragment(Fragment activeFragment) {
        this.activeFragment = activeFragment;
    }


    Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public void OnRegisterUser() {
        getActionBar().selectTab(loginTab);
    }

    public void OnRegisterAdminLogin() {
        registerUserTab = getActionBar().newTab();
        registerUserTab.setText(getLabelForEnum(REGISTER_USER, "tab_label"));
        registerUserTabListener = new RegisterUserTabListener<RegisterUserFragment>(this, REGISTER_USER.toString(), RegisterUserFragment.class);
        registerUserTab.setTabListener(registerUserTabListener);
        getActionBar().addTab(registerUserTab);
        getActionBar().selectTab(registerUserTab);
    }
}