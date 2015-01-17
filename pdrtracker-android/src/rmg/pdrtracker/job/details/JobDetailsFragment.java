package rmg.pdrtracker.job.details;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import rmg.pdrtracker.job.model.JobDetailsModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.job.model.ModelService;
import rmg.pdrtracker.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobDetailsFragment extends Fragment {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private ScrollView root;


    private JobModel jobModel;
    private JobDetailsModel jobDetailsModel;

    private TextView jobName;
    private TextView jobEstimator;
    private TextView jobDate;
    private TextView insurance;
    private TextView claimNumber;
    private TextView customerName;
    private TextView customerPhone;
    private TextView vin;
    private TextView year;
    private TextView make;
    private TextView model;
    private TextView color;
    private TextView description;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ScrollView) inflater.inflate(R.layout.job_details_layout, container, false);

        final Context context = root.getContext();

        initModel();

        initFields();

        final TextView jobDate = (TextView) root.findViewById(R.id.job_date);
        jobDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        jobDate.setText(String.format("%d/%d/%d", month + 1, day, year));
                    }
                }, 2013, 0, 0).show();
            }
        });

        return root;

    }

    private void initFields() {

        jobName = (TextView) root.findViewById(R.id.job_name);
        jobName.requestFocus();
        jobName.setText(jobDetailsModel.getJobName());

        jobEstimator = (TextView) root.findViewById(R.id.estimator_name);
        jobEstimator.setText(jobDetailsModel.getJobCreatorName());

        jobDate = (TextView) root.findViewById(R.id.job_date);
        jobDate.setText(DATE_FORMAT.format(jobDetailsModel.getJobDate()));

        insurance = (TextView) root.findViewById(R.id.insurance_name);
        insurance.setText(jobDetailsModel.getInsuranceCompanyName());

        claimNumber = (TextView) root.findViewById(R.id.claim_number);
        claimNumber.setText(jobDetailsModel.getInsuranceClaimNumber());

        customerName = (TextView) root.findViewById(R.id.customer_name);
        customerName.setText(jobDetailsModel.getCustomerName());

        customerPhone = (TextView) root.findViewById(R.id.customer_phone);
        customerPhone.setText(jobDetailsModel.getCustomerPhone());

        vin = (TextView) root.findViewById(R.id.vin);
        vin.setText(jobDetailsModel.getVin());

        year = (TextView) root.findViewById(R.id.vehicle_year);
        year.setText(jobDetailsModel.getVehicleYear());

        make = (TextView) root.findViewById(R.id.vehicle_make);
        make.setText(jobDetailsModel.getVehicleMake());

        model = (TextView) root.findViewById(R.id.vehicle_model);
        model.setText(jobDetailsModel.getVehicleModel());

        color = (TextView) root.findViewById(R.id.vehicle_color);
        color.setText(jobDetailsModel.getVehicleColor());

        description = (TextView) root.findViewById(R.id.job_description);
        description.setText(jobDetailsModel.getJobDescription());

    }

    private void initModel() {
        ModelService modelService = ModelService.getInstance();
        jobModel = modelService.getJobModel();
        jobDetailsModel = jobModel.getJobDetailsModel();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        saveModel();
        ModelService modelService = ModelService.getInstance();
        modelService.saveModelState(savedInstanceState, jobModel);
    }

    public void saveModel() {
        jobDetailsModel.setJobName(jobName.getText().toString());
        jobDetailsModel.setJobCreatorName(jobEstimator.getText().toString());
        jobDetailsModel.setInsuranceCompanyName(insurance.getText().toString());
        jobDetailsModel.setInsuranceClaimNumber(claimNumber.getText().toString());
        jobDetailsModel.setCustomerName(customerName.getText().toString());
        jobDetailsModel.setCustomerPhone(customerPhone.getText().toString());
        jobDetailsModel.setJobDate(toDate(jobDate.getText().toString()));
        jobDetailsModel.setVin(vin.getText().toString());
        jobDetailsModel.setVehicleYear(year.getText().toString());
        jobDetailsModel.setVehicleMake(make.getText().toString());
        jobDetailsModel.setVehicleModel(model.getText().toString());
        jobDetailsModel.setVehicleColor(color.getText().toString());
        jobDetailsModel.setJobDescription(description.getText().toString());
    }



    private Date toDate(String date) {
        String dateStr = jobDate.getText().toString();
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Could not parse date: " + dateStr);
        }
    }

}
