package com.theleafapps.pro.feathernotes.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;

import com.theleafapps.pro.feathernotes.ui.PassCode;

import com.theleafapps.pro.feathernotes.R;

/**
 * Created by aviator on 19/01/16.
 */
public class PassCodeActionListDialog extends DialogFragment{

    String[] actionsArray;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Resources res = getResources();
        actionsArray = res.getStringArray(R.array.passCodeActions);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.passCodeActions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(),PassCode.class);
                intent.putExtra("action",which);
                startActivity(intent);
//                Toast.makeText(getActivity(), "Question Selected ->" + actionsArray[which], Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        return dialog;

    }
}
