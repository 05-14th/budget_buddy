 package com.example.budgetbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

 public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
     private String lorems[] = {"Sample 1", "Sample 2", "Sample 3"};
     private File internalFile;
     private EditText productName;
     private EditText productCost;
     private EditText purchaseDate;
     private EditText purchaseDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Read File from internal storage
        internalFile = new File(getFilesDir(), "finance.txt");
        // Expense Category
        Spinner spinner = findViewById(R.id.expense_choices);
        if(spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(spinner != null){
            spinner.setAdapter(adapter);
        }
        // Recent List
        ListView recents = findViewById(R.id.recent_list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, lorems);
        recents.setAdapter(arr);
        //Get Contents

    }

    public void WriteText(String expenseString){
        try {
            FileOutputStream fos = new FileOutputStream(internalFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(expenseString);
            osw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String inputDetails(String name, int cost, Date date, String Desc){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = simpleDateFormat.format(date);
        String result = name + "~" + Integer.toString(cost) + "~" + dateString + "~" + Desc;
        return result;
    }

     @Override
     public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

     }

     @Override
     public void onNothingSelected(AdapterView<?> adapterView) {

     }
 }