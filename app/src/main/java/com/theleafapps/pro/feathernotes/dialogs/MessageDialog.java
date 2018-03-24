package com.theleafapps.pro.feathernotes.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Created by aviator on 20/01/16.
 */
public class MessageDialog extends DialogFragment {

    Bundle bundle;
    private String code;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        bundle = getArguments();
        code = TextUtils.isEmpty(bundle.getString("Msg")) ? "" : bundle.getString("Msg");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(code);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
