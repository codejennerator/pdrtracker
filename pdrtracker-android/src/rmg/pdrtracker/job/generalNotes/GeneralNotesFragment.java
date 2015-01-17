package rmg.pdrtracker.job.generalNotes;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import rmg.pdrtracker.job.model.GeneralNotesModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.R;
import rmg.pdrtracker.job.model.ModelService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class GeneralNotesFragment extends Fragment {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private ScrollView root;


    private JobModel jobModel;
    private GeneralNotesModel generalNotesModel;

    private TextView generalNotes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ScrollView) inflater.inflate(R.layout.general_notes_layout, container, false);

        final Context context = root.getContext();

        initModel();

        initFields();

        return root;

    }

    private void initFields() {

        generalNotes = (TextView) root.findViewById(R.id.general_notes);
        generalNotes.setText(generalNotesModel.getGeneralNotes());

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

        generalNotesModel.setGeneralNotes(generalNotes.getText().toString());
    }

}
