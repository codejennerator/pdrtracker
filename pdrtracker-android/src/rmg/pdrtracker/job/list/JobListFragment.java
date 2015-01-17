package rmg.pdrtracker.job.list;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import rmg.pdrtracker.job.activities.JobActivity;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.R;
import rmg.pdrtracker.db.JobDao;
import rmg.pdrtracker.job.model.JobDetailsModel;
import rmg.pdrtracker.job.activities.PrintJobActivity;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.LoginModelService;
import rmg.pdrtracker.util.AppUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class JobListFragment extends Fragment {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private LinearLayout linearLayout;

    private LoginModel loginModel;

    private List<JobModel> jobList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ScrollView root = (ScrollView) inflater.inflate(R.layout.list_jobs_layout, container, false);

        Context context = root.getContext();
        LoginModelService loginModelService = LoginModelService.getInstance();
        loginModel = loginModelService.getLoginModel();

        initJobList(context);

        linearLayout = (LinearLayout) root.getChildAt(0);
        //linearLayout.setDrawingCacheEnabled(true);

        for (JobModel nextJob : jobList) {
            if(nextJob.getUserId() == loginModel.getId()){
                addListRow(context, linearLayout, nextJob);
            }
        }


        return root;

    }

    private void initJobList(Context context) {
        JobDao jobDao = new JobDao(context);
        jobDao.open();
        jobList = jobDao.selectAll();
        jobDao.close();
    }

    private void addListRow(final Context context, ViewGroup parent, final JobModel jobModel) {

        LinearLayout row = new LinearLayout(context);
        parent.addView(row, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);

        GridLayout gridLayout = new GridLayout(context);
        row.addView(gridLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        gridLayout.setBackground(getResources().getDrawable(R.drawable.job_list_item));

        gridLayout.setClickable(true);
        gridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editJob(context, jobModel);
            }
        });

        JobDetailsModel jobDetailsModel = jobModel.getJobDetailsModel();

        TextView textView = addTextViewToGrid(context, gridLayout, 0, 0);
        textView.setText(jobDetailsModel.getJobName());

        // Top row
        textView = addTextViewToGrid(context, gridLayout, 1, 0);
        textView.setText(DATE_FORMAT.format(jobDetailsModel.getJobDate()));

        textView = addTextViewToGrid(context, gridLayout, 0, 1);
        textView.setText(jobDetailsModel.getCustomerName());

        // Bottom row
        textView = addTextViewToGrid(context, gridLayout, 1, 1);
        textView.setText(jobDetailsModel.getCustomerPhone());

        textView = addTextViewToGrid(context, gridLayout, 2, 1);
        textView.setText(jobDetailsModel.getVehicleYear());

        textView = addTextViewToGrid(context, gridLayout, 3, 1);
        textView.setText(jobDetailsModel.getVehicleMake());

        textView = addTextViewToGrid(context, gridLayout, 4, 1);
        textView.setText(jobDetailsModel.getVehicleModel());

        textView = addTextViewToGrid(context, gridLayout, 5, 1);
        textView.setText(jobDetailsModel.getVehicleColor());

        Button printButton = new Button(context);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.rowSpec = GridLayout.spec(0, 1);
        layoutParams.columnSpec = GridLayout.spec(6);
        layoutParams.topMargin = AppUtils.toDip(2);
        gridLayout.addView(printButton, layoutParams);
        printButton.setText("Print");
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // PrintDialog printDialog = new PrintDialog(context);

               // printDialog.show();
                printJob(context, jobModel);
            }
        });

    }

    private void editJob(Context context, JobModel jobModel) {

        Intent intent = new Intent(context, JobActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("jobModel", jobModel);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void printJob(Context context, JobModel jobModel) {

        Intent intent = new Intent(context, PrintJobActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("jobModel", jobModel);
        bundle.putSerializable("loginModel", loginModel);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private TextView addTextViewToGrid(Context context, GridLayout gridLayout, int col, int row) {
        TextView textView = new TextView(context);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;

        if (row == 0 && col == 1) {
            // Top row last column spans rest of columns
            layoutParams.columnSpec = GridLayout.spec(col, 5);
        } else {
            layoutParams.columnSpec = GridLayout.spec(col);
        }

        layoutParams.rowSpec = GridLayout.spec(row);
        layoutParams.topMargin = AppUtils.toDip(2);
        layoutParams.setGravity(Gravity.FILL_HORIZONTAL);

        gridLayout.addView(textView, layoutParams);
        textView.setPadding(0, 0, 10, 0);

        // Top and bottom row are different fonts
        if (row == 0) {
            textView.setTextSize(20);
        } else {
            textView.setTextSize(16);
        }

        // Top and bottom row are different colors
        if (row == 0) {
            textView.setBackgroundColor(getResources().getColor(R.drawable.white));
        } else {
            textView.setBackgroundColor(getResources().getColor(R.drawable.gray1));
        }

        return textView;
    }


}
