package rmg.pdrtracker.login.dialogPopUps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import rmg.pdrtracker.R;

public class TokenExpiredDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.token_expired).setPositiveButton(R.string.ok, null)
                .create();
    }
}