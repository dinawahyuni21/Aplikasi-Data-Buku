package com.example.progmoblanjut;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
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

public class EditActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxISBN, Txjudulbuku, TxPenulis, TxTglTerbit, TxPenerbit;
    Spinner SpJB;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxISBN= (EditText)findViewById(R.id.txISBN_Edit);
        Txjudulbuku = (EditText)findViewById(R.id.txjudulbuku_Edit);
        TxPenulis = (EditText)findViewById(R.id.txPenulis_Edit);
        TxTglTerbit = (EditText)findViewById(R.id.txTglTerbit_Edit);
        TxPenerbit = (EditText)findViewById(R.id.txPenerbit_Edit);
        SpJB = (Spinner)findViewById(R.id.spJB_Edit);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTglTerbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        getData();
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

    private void getData(){
        Cursor cursor = helper.oneData(id);
        if(cursor.moveToFirst()){
            String isbn = cursor.getString(cursor.getColumnIndex(DBHelper.row_isbn));
            String judulbuku = cursor.getString(cursor.getColumnIndex(DBHelper.row_judul));
            String penulis = cursor.getString(cursor.getColumnIndex(DBHelper.row_penulis));
            String jk = cursor.getString(cursor.getColumnIndex(DBHelper.row_jb));
            String tanggal = cursor.getString(cursor.getColumnIndex(DBHelper.row_tglTerbit));
            String penerbit = cursor.getString(cursor.getColumnIndex(DBHelper.row_penerbit));

            TxISBN.setText(isbn);
            Txjudulbuku.setText(judulbuku);

            if (jk.equals("Novel")){
                SpJB.setSelection(0);
            }else if(jk.equals("Komik")){
                SpJB.setSelection(1);
            }

            TxPenulis.setText(penulis);
            TxTglTerbit.setText(tanggal);
            TxPenerbit.setText(penerbit);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.savey_edit:
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
                    Toast.makeText(EditActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT);
                }else{
                    helper.updateData(values, id);
                    Toast.makeText(EditActivity.this, "Data Telah Berhasil Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        switch (item.getItemId()){
            case R.id.delete_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("Data buku ini akan dihapus.");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteData(id);
                        Toast.makeText(EditActivity.this, "Data Buku Telah Terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}