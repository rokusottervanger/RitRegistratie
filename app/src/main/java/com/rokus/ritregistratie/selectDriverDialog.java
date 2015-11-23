package com.rokus.ritregistratie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class selectDriverDialog extends DialogFragment {
    private ArrayList mSelectedItems = new ArrayList();
    private boolean[] boolList;
    private selectDriverDialogListener dialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if ( mSelectedItems.size() == 0 )
            boolList = new boolean[getResources().getStringArray(R.array.possible_drivers).length];
        try {
            dialogListener = (selectDriverDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement selectDriverDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.title_dialog_pick_driver)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.possible_drivers, boolList,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                boolList[which] = isChecked;
                                if (isChecked && !mSelectedItems.contains(which) ) {
                                        mSelectedItems.add(which);
                                } else if (!isChecked && mSelectedItems.contains(which)) {
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialogListener.onOkay(mSelectedItems);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mSelectedItems.clear();
                        dialogListener.onCancel();
                        Arrays.fill(boolList, false);
                    }
                });

        return builder.create();
    }
}