package com.rokus.ritregistratie;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EnterData extends AppCompatActivity implements selectDriverDialogListener {
    private ArrayList<Integer> selectedDrivers;
    private String[] driverOptions;
    private Resources res;
    private TextView tvSelectedDrivers;
    private selectDriverDialog sdd = new selectDriverDialog();
    private int REQUEST_EXIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        res = getResources();
        driverOptions = res.getStringArray(R.array.possible_drivers);
        tvSelectedDrivers = (TextView) findViewById(R.id.selected_drivers);
        selectedDrivers = new ArrayList<Integer>();
    }

    public void showSelectDriverDialog(View view) {
        sdd.show(getSupportFragmentManager(), "select driver");
    }

    private void showNoDriverSelectedDialog() {
        new AlertDialog.Builder(this)
                .setTitle(res.getString(R.string.no_driver_warning_title))
                .setMessage(res.getString(R.string.no_driver_warning_text))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onOkay(ArrayList<Integer> drivers) {
        StringBuilder stringBuilder = new StringBuilder();
        selectedDrivers = drivers;

        if (drivers.size() != 0) {
            stringBuilder.append(res.getString(R.string.chosen_drivers));
            for (int i = 0; i < drivers.size(); i++) {
                String driver = driverOptions[drivers.get(i)];
                if ( i > 0 && i < drivers.size()-1) {
                    stringBuilder.append(",");
                }
                if ( i > 0 && i == drivers.size()-1) {
                    stringBuilder.append(" " + res.getString(R.string.and));
                }
                stringBuilder = stringBuilder.append(" " + driver);
            }

            tvSelectedDrivers.setText(stringBuilder.toString());
        } else {
            tvSelectedDrivers.setText(res.getString(R.string.no_chosen_drivers));
        }
    }

    @Override
    public void onCancel() {
        tvSelectedDrivers.setText(res.getString(R.string.no_chosen_drivers));
        selectedDrivers.clear();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_enter_data, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        if (selectedDrivers.size() == 0) {
            showNoDriverSelectedDialog();
        } else {
            Intent intent = new Intent(this, EnterEnd.class);
            EditText etStart = (EditText) findViewById(R.id.start_number);
            EditText etName = (EditText) findViewById(R.id.name);
            String trip_name = etName.getText().toString();
            String start = etStart.getText().toString();

            intent.putExtra("name", trip_name);
            intent.putExtra("start", start);
            intent.putExtra("drivers", selectedDrivers);
            startActivityForResult(intent, REQUEST_EXIT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }
}



