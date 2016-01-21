package cook.cuu.feathernotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by aviator on 18/01/16.
 */
public class SecQListDialog extends DialogFragment {
    String[] questionsArray;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Resources res = getResources();
        questionsArray = res.getStringArray(R.array.hintQuestionsArray);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Your Security Question !");
        builder.setItems(R.array.hintQuestionsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Auth.hintQuestion.setText(questionsArray[which]);
                //Toast.makeText(getActivity(), "Question Selected ->" + questionsArray[which], Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        return dialog;
    }
}
