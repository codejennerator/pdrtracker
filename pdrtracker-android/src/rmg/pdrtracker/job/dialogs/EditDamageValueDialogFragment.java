package rmg.pdrtracker.job.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import rmg.pdrtracker.R;

public class EditDamageValueDialogFragment extends DialogFragment {

    private DialogClickListener callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (DialogClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.edit_price_matrix_dialog_layout, null);
        builder.setView(view);
        final EditText valueInput = (EditText)view.findViewById(R.id.price_matrix_value);
        //final EditText valueInput = new EditText(getActivity().getApplicationContext());
       // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
       //         LinearLayout.LayoutParams.MATCH_PARENT,
       //         LinearLayout.LayoutParams.MATCH_PARENT);
       // valueInput.setLayoutParams(lp);
       // valueInput.setTextColor(R.drawable.label_foreground);
       // valueInput.setGravity(View.TEXT_ALIGNMENT_CENTER);
       // builder.setView(valueInput);

        builder.setMessage(R.string.change_damage_matrix_price_dialog)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        callback.onOkClick(valueInput.getText().toString());
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                callback.onCancelClick();
            }
        });

        return builder.create();
    }

    public interface DialogClickListener {
        public void onOkClick(String input);
        public void onCancelClick();
    }
}