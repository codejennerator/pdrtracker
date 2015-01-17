package rmg.pdrtracker.login.dialogPopUps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import rmg.pdrtracker.R;

public class NoInternetNoticeDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.dialog_no_internet_connection).setPositiveButton(R.string.ok, null)
                .create();
    }
}