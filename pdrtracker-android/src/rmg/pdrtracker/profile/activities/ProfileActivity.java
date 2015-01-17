package rmg.pdrtracker.profile.activities;


import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import rmg.pdrtracker.R;
import rmg.pdrtracker.job.dialogs.SaveDialogFragment;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.job.model.ModelService;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.LoginModelService;
import rmg.pdrtracker.main_menu.activities.MainMenuActivity;
import rmg.pdrtracker.profile.dealerList.DealerListFragment;
import rmg.pdrtracker.profile.dealerList.DealerListTabListener;
import rmg.pdrtracker.profile.estimatesSetup.EstimatesSetupFragment;
import rmg.pdrtracker.profile.estimatesSetup.EstimatesSetupTabListener;
import rmg.pdrtracker.profile.invoicesSetup.InvoicesSetupFragment;
import rmg.pdrtracker.profile.invoicesSetup.InvoicesSetupTabListener;
import rmg.pdrtracker.profile.userInfo.UserInfoFragment;
import rmg.pdrtracker.profile.userInfo.UserInfoTabListener;

import java.text.NumberFormat;
import java.util.Locale;

import static rmg.pdrtracker.profile.activities.ProfileActivity.TabNames.*;
import static rmg.pdrtracker.util.AppUtils.getLabelForEnum;

/**
 * Created with IntelliJ IDEA.
 * User: jenn
 * Date: 10/8/13
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfileActivity extends Activity{


    private JobModel jobModel;
    private LoginModel loginModel;

    public enum TabNames {
        USER_INFO,
        DEALER_LIST,
        ESTIMATES_SETUP,
        INVOICES_SETUP
    }

    TabNames activeTab = USER_INFO;

    Fragment activeFragment;

    private UserInfoTabListener<UserInfoFragment> userInfoTabListener;
    private DealerListTabListener<DealerListFragment> dealerListTabListener;
    private EstimatesSetupTabListener<EstimatesSetupFragment> estimatesSetupTabListener;
    private InvoicesSetupTabListener<InvoicesSetupFragment> invoicesSetupTabListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        ModelService modelService = ModelService.getInstance();
        modelService.init(this, savedInstanceState);
        jobModel = modelService.getJobModel();

        LoginModelService loginModelService = LoginModelService.getInstance();
        loginModel = loginModelService.getLoginModel();
        System.out.println("login -------------------------------------------------------------------------"+loginModel.getId());

        setContentView(R.layout.user_info_layout);

        setContentView(R.layout.profile_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        try{
            actionBar.setSubtitle(currencyFormatter.format(Double.parseDouble(Float.toString(jobModel.getPriceEstimateModel().getPriceEstimate()))));
        }catch (NumberFormatException e){

            System.out.println("Number Format Exception trying to set subtitle. "+e);
            actionBar.setSubtitle(Float.toString(jobModel.getPriceEstimateModel().getPriceEstimate()));
        }

        ActionBar.Tab tab = actionBar.newTab();
        tab.setText(getLabelForEnum(USER_INFO, "tab_label"));
        userInfoTabListener = new UserInfoTabListener<UserInfoFragment>(this, USER_INFO.toString(), UserInfoFragment.class);
        tab.setTabListener(userInfoTabListener);
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setText(getLabelForEnum(DEALER_LIST, "tab_label"));
        dealerListTabListener= new DealerListTabListener<DealerListFragment>(this, DEALER_LIST.toString(), DealerListFragment.class);
        tab.setTabListener(dealerListTabListener);
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setText(getLabelForEnum(ESTIMATES_SETUP, "tab_label"));
        estimatesSetupTabListener = new EstimatesSetupTabListener<EstimatesSetupFragment>(this, ESTIMATES_SETUP.toString(), EstimatesSetupFragment.class);
        tab.setTabListener(estimatesSetupTabListener);
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setText(getLabelForEnum(INVOICES_SETUP, "tab_label"));
        invoicesSetupTabListener= new InvoicesSetupTabListener<InvoicesSetupFragment>(this, INVOICES_SETUP.toString(), InvoicesSetupFragment.class);
        tab.setTabListener(invoicesSetupTabListener);
        actionBar.addTab(tab);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_job_action:
                saveJob();
                return true;
            default:
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loginModel", loginModel);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            //return super.onOptionsItemSelected(item);
        }

    }

    private void saveJob() {

        showSaveDialog();

    }

    public void showSaveDialog() {

        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new SaveDialogFragment();
        dialog.show(getFragmentManager(), "SaveDialogFragment");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_job_menu, menu);
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

    public JobModel getJobModel() {
        return jobModel;
    }
}
