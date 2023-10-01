 package com.example.budgetbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

 public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
     private ArrayList<String> titles;
     private ArrayAdapter<String> arr;
     private ListView recents;
     private File internalFile;

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
        recents = findViewById(R.id.recent_list);
        titles = new ArrayList<>();
        arr = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titles);
        recents.setAdapter(arr);
        ReadFile();
        //Get Contents
        EditText productName = findViewById(R.id.product_name);
        EditText productCost = findViewById(R.id.item_cost);
        EditText purchaseDate = findViewById(R.id.purchase_date);
        EditText purchaseDesc = findViewById(R.id.purchase_desc);
        Button confirmProduct = findViewById(R.id.confirm_button);

        confirmProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String completeInput = inputDetails(productName.getText().toString(), productCost.getText().toString(),
                        purchaseDate.getText().toString(), purchaseDesc.getText().toString(), spinner.getSelectedItem().toString());
                WriteText(completeInput);
                displayToast("Expenses Recorded Successfully");
                addItem(completeInput);
            }
        });
    }

    public void WriteText(String expenseString){
        try {
            FileOutputStream fos = new FileOutputStream(internalFile, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(expenseString + "\n");
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ReadFile(){
        try {
            FileInputStream fis = new FileInputStream(internalFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;

            while ((line = br.readLine()) != null) {
                titles.add(line);
            }

            br.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String inputDetails(String name, String cost, String date, String Desc, String category){
        return name + "~" + cost + "~" + date + "~" + Desc + "~" + category;
    }

     @Override
     public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         String selectedItem = adapterView.getItemAtPosition(i).toString();
         Log.d("Selected Item", selectedItem);
     }

     @Override
     public void onNothingSelected(AdapterView<?> adapterView) {

     }

     public void displayToast(String message){
         Toast.makeText(this, "Expenses had been recorded.", Toast.LENGTH_SHORT).show();
     }

     public void addItem(String item){
         titles.add(item);
         arr.notifyDataSetChanged();
         if(titles.size() > 5){
             titles.remove(0);
             arr.notifyDataSetChanged();
         }
         boolean removeFirstLine = removeFirstLineFromFile(internalFile);
     }

     public static boolean removeFirstLineFromFile(File file) {
         try {
             File tempFile = new File(file.getParent(), "temp.txt");

             BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

             String currentLine;
             boolean firstLine = true;

             while ((currentLine = reader.readLine()) != null) {
                 if (firstLine) {
                     firstLine = false;
                 } else {
                     writer.write(currentLine + System.getProperty("line.separator"));
                 }
             }

             writer.close();
             reader.close();

             // Delete the original file
             if (file.delete()) {
                 // Rename the temporary file to the original file name
                 if (tempFile.renameTo(file)) {
                     return true;
                 }
             }

             return false;
         } catch (IOException e) {
             e.printStackTrace();
             return false;
         }
     }


 }