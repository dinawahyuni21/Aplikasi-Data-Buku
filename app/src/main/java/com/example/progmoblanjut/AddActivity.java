package com.example.progmoblanjut;

import android.app.DatePickerDialog;
import android.content.ContentValues;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxISBN, Txjudulbuku, TxPenulis, TxTglTerbit, TxPenerbit;
    Spinner SpJB;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxISBN = (EditText)findViewById(R.id.txISBN_Add);
        Txjudulbuku = (EditText)findViewById(R.id.txjudulbuku_Add);
        TxPenulis = (EditText)findViewById(R.id.txPenulis_Add);
        TxTglTerbit = (EditText)findViewById(R.id.txTglTerbit_Add);
        TxPenerbit = (EditText)findViewById(R.id.txPenerbit_Add);
        SpJB = (Spinner)findViewById(R.id.spJB_Add);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTglTerbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxTglTerbit.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_add:
                String isbn = TxISBN.getText().toString().trim();
                String judulbuku = Txjudulbuku.getText().toString().trim();
                String penulis = TxPenulis.getText().toString().trim();
                String tanggal = TxTglTerbit.getText().toString().trim();
                String penerbit = TxPenerbit.getText().toString().trim();
                String jb = SpJB.getSelectedItem().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_isbn, isbn);
                values.put(DBHelper.row_judul, judulbuku);
                values.put(DBHelper.row_penulis, penulis);
                values.put(DBHelper.row_tglTerbit, tanggal);
                values.put(DBHelper.row_penerbit, penerbit);
                values.put(DBHelper.row_jb, jb);

                if (isbn.equals("") || judulbuku.equals("") || penulis.equals("") || tanggal.equals("") || penerbit.equals("")){
                    Toast.makeText(AddActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    helper.insertData(values);
                    Toast.makeText(AddActivity.this, "Data Buku Telah Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}