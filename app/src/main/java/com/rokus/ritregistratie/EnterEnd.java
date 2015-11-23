package com.rokus.ritregistratie;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class EnterEnd extends AppCompatActivity {
    String name, start, driversStr, end, toDataFileStr;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_end);

        // Get the information from the intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        start = intent.getStringExtra("start");
        ArrayList<Integer> drivers = (ArrayList<Integer>) intent.getSerializableExtra("drivers");

        // Assemble a string with the drivers' names
        String[] driverOptions;
        Resources res = getResources();
        driverOptions = res.getStringArray(R.array.possible_drivers);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < drivers.size(); i++) {
            String driver = driverOptions[drivers.get(i)];
            if ( i > 0 && i < drivers.size()-1) {
                stringBuilder.append(", ");
            }
            if ( i > 0 && i == drivers.size()-1) {
                stringBuilder.append(" " + res.getString(R.string.and) + " ");
            }
            stringBuilder = stringBuilder.append(driver);
        }
        driversStr = stringBuilder.toString();

        // Set text of text view objects
        ((TextView) findViewById(R.id.trip_name)).setText(name);
        ((TextView) findViewById(R.id.drivers)).setText(driversStr);
        ((TextView) findViewById(R.id.start_number)).setText(start);
    }

    public void saveData(View view) {
        EditText etEnd = (EditText) findViewById(R.id.end_number);
        end = etEnd.getText().toString();

        FileOutputStream outputStream;
        filename = "data.txt";
        toDataFileStr = name + "; " + driversStr + "; " + start + "; " + end + "\n";

        try {
            // Write data to file
            outputStream = openFileOutput(filename, MODE_APPEND);
            outputStream.write(toDataFileStr.getBytes());
            outputStream.close();

            // Let user know it worked
            Toast.makeText(this, R.string.wrote_data, Toast.LENGTH_SHORT).show();

            // Let parent activity know it worked
            setResult(RESULT_OK, null);

            // Create new instance of EnterData activity
            Intent intent = new Intent(this, EnterData.class);
            startActivity(intent);

            // Finish this activity
            finish();
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

//    public void showFile(View view) {
//        String fileContent = "";
//        try {
//            FileInputStream inputStream = openFileInput(filename);
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append(receiveString);
//                    stringBuilder.append("\n");
//                }
//
//                inputStream.close();
//                fileContent = stringBuilder.toString();
//            }
//        } catch (FileNotFoundException e) {
//            Toast.makeText(this, "File " + filename + " not found", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            Toast.makeText(this, "IO exception", Toast.LENGTH_SHORT).show();
//        }
//        ((TextView) findViewById(R.id.file_content)).setText(fileContent);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
