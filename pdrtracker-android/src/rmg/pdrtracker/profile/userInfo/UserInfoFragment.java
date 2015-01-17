package rmg.pdrtracker.profile.userInfo;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import rmg.pdrtracker.R;
import rmg.pdrtracker.job.model.GeneralNotesModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.job.model.ModelService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UserInfoFragment extends Fragment{

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private ScrollView root;


    private JobModel jobModel;
    private GeneralNotesModel generalNotesModel;

    private TextView laborRate;
    private TextView companyName;
    private TextView companyEmail;
    private TextView companyPhone;
    private TextView companyAddress;
    private TextView companyWebsite;
    private TextView estimatorName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ScrollView) inflater.inflate(R.layout.user_info_layout, container, false);

        final Context context = root.getContext();

        initModel();

        initFields();

        return root;

    }

    private void initFields() {

        laborRate = (TextView) root.findViewById(R.id.labor_rate);
        laborRate.setText(generalNotesModel.getGeneralNotes());

        companyName = (TextView) root.findViewById(R.id.company_name);
        companyName.setText(generalNotesModel.getGeneralNotes());

        companyAddress = (TextView) root.findViewById(R.id.company_address);
        companyAddress.setText(generalNotesModel.getGeneralNotes());

        companyEmail = (TextView) root.findViewById(R.id.company_email);
        companyEmail.setText(generalNotesModel.getGeneralNotes());

        companyWebsite = (TextView) root.findViewById(R.id.company_website);
        companyWebsite.setText(generalNotesModel.getGeneralNotes());

        companyPhone = (TextView) root.findViewById(R.id.company_phone);
        companyPhone.setText(generalNotesModel.getGeneralNotes());

        estimatorName = (TextView) root.findViewById(R.id.estimator_name);
        estimatorName.setText(generalNotesModel.getGeneralNotes());

    }

    private void initModel() {
        ModelService modelService = ModelService.getInstance();
        jobModel = modelService.getJobModel();
        generalNotesModel = jobModel.getGeneralNotesModel();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        saveModel();
        ModelService modelService = ModelService.getInstance();
        modelService.saveModelState(savedInstanceState, jobModel);
    }

    public void saveModel() {

        generalNotesModel.setGeneralNotes(laborRate.getText().toString());
    }

}
