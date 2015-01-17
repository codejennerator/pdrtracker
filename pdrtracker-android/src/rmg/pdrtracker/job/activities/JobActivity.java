package rmg.pdrtracker.job.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import rmg.pdrtracker.job.dialogs.SaveDialogFragment;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.R;
import rmg.pdrtracker.db.JobDao;
import rmg.pdrtracker.job.generalNotes.GeneralNotesFragment;
import rmg.pdrtracker.job.generalNotes.GeneralNotesTabListener;
import rmg.pdrtracker.job.damagematrix.DamageMatrixFragment;
import rmg.pdrtracker.job.damagematrix.DamageMatrixTabListener;
import rmg.pdrtracker.job.details.JobDetailsFragment;
import rmg.pdrtracker.job.details.JobDetailsTabListener;
import rmg.pdrtracker.job.model.ModelService;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.LoginModelService;
import rmg.pdrtracker.main_menu.activities.MainMenuActivity;

import java.text.NumberFormat;
import java.util.Locale;

import static rmg.pdrtracker.job.activities.JobListActivity.LOGTAG;
import static rmg.pdrtracker.job.activities.JobActivity.TabNames.*;
import static rmg.pdrtracker.util.AppUtils.getLabelForEnum;


public class JobActivity extends Activity implements SaveDialogFragment.NoticeDialogListener{

    private JobModel jobModel;
    private LoginModel loginModel;

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        JobDetailsFragment jobDetailsFragment = jobDetailsTabListener.getFragment();
        if (jobDetailsFragment != null) {
            jobDetailsFragment.saveModel();
        }

        DamageMatrixFragment damageMatrixFragment = damageMatrixTabListener.getFragment();
        if (damageMatrixFragment != null) {
            damageMatrixFragment.saveModel();
        }

        GeneralNotesFragment generalNotesFragment = generalNotesTabListener.getFragment();
        if (generalNotesFragment != null) {
            generalNotesFragment.saveModel();
        }

        jobModel.setUserId(loginModel.getId());
        JobDao jobDao = new JobDao(this);
        jobDao.open();
        jobDao.saveJob(jobModel);
        jobDao.close();
        Log.d(LOGTAG, "Saved job.");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
       // do nothing
    }

    public enum TabNames {
        JOB_DETAILS,
        DAMAGE_MATRIX,
        GENERAL_NOTES
    }

    TabNames activeTab = JOB_DETAILS;

    Fragment activeFragment;

    private JobDetailsTabListener<JobDetailsFragment> jobDetailsTabListener;
    private DamageMatrixTabListener<DamageMatrixFragment> damageMatrixTabListener;
    private GeneralNotesTabListener<GeneralNotesFragment> generalNotesTabListener;

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

        setContentView(R.layout.job_details_layout);

        setContentView(R.layout.job_activity);

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
        tab.setText(getLabelForEnum(JOB_DETAILS, "tab_label"));
        jobDetailsTabListener = new JobDetailsTabListener<JobDetailsFragment>(this, JOB_DETAILS.toString(), JobDetailsFragment.class);
        tab.setTabListener(jobDetailsTabListener);
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setText(getLabelForEnum(DAMAGE_MATRIX, "tab_label"));
        damageMatrixTabListener = new DamageMatrixTabListener<DamageMatrixFragment>(this, DAMAGE_MATRIX.toString(), DamageMatrixFragment.class);
        tab.setTabListener(damageMatrixTabListener);
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setText(getLabelForEnum(GENERAL_NOTES, "tab_label"));
        generalNotesTabListener = new GeneralNotesTabListener<GeneralNotesFragment>(this, GENERAL_NOTES.toString(), GeneralNotesFragment.class);
        tab.setTabListener(generalNotesTabListener);
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
