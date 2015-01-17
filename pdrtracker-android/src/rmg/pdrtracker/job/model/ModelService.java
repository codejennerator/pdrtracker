package rmg.pdrtracker.job.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ModelService {

    public static final String JOB_MODEL = "jobModel";

    private static ModelService singleton = new ModelService();

    private JobModel jobModel;

    private ModelService() {
    }

    public static ModelService getInstance() {
        return singleton;
    }

    public void init(Activity activity, Bundle savedInstanceState) {

        Intent intent = activity.getIntent();

        // This is the temporary state bundle that saves state when the app is minified.
        if (savedInstanceState != null) {
            jobModel = (JobModel) savedInstanceState.get(JOB_MODEL);
        }

        // This is the bundle that was loaded out of the database
        Bundle savedBundle = intent.getExtras();
        jobModel = (JobModel) savedBundle.get(JOB_MODEL);
        if (jobModel != null) {
            return;
        }

        jobModel = new JobModel();

    }

    public JobModel getJobModel() {
        return jobModel;
    }

    public void saveModelState(Bundle savedInstanceState, JobModel jobModel) {
        savedInstanceState.putSerializable(JOB_MODEL, jobModel);
    }

}
