package com.example.progmoblanjut;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    DBHelper helper;
    LayoutInflater inflater;
    View dialogView;
    TextView Tv_ISBN, Tv_judulbuku, Tv_Penulis, Tv_JB, Tv_TglTerbit, Tv_Penerbit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        helper = new DBHelper(this);
        listView = (ListView)findViewById(R.id.list_data);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    public void setListView(){
        Cursor cursor = helper.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        listView.setAdapter(customCursorAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long x) {
        TextView getId = (TextView)view.findViewById(R.id.listID);
        final long id = Long.parseLong(getId.getText().toString());
        final Cursor cur = helper.oneData(id);
        cur.moveToFirst();

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pilih Opsi");

        //Add a List
        String[] options = {"Lihat Data Buku", "Edit Data Buku", "Hapus Data Buku"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                    final AlertDialog.Builder viewData = new AlertDialog.Builder(MainActivity.this);
                    inflater = getLayoutInflater();
                    dialogView = inflater.inflate(R.layout.view_data, null);
                    viewData.setView(dialogView);
                    viewData.setTitle("----------------Lihat Data Buku--------------");
                        Tv_ISBN = (TextView)dialogView.findViewById(R.id.tv_ISBN);
                        Tv_judulbuku = (TextView)dialogView.findViewById(R.id.tv_judulbuku);
                        Tv_Penulis = (TextView)dialogView.findViewById(R.id.tv_Penulis);
                        Tv_TglTerbit = (TextView)dialogView.findViewById(R.id.tv_TglTerbit);
                        Tv_JB = (TextView)dialogView.findViewById(R.id.tv_JB);
                        Tv_Penerbit = (TextView)dialogView.findViewById(R.id.tv_Penerbit);

                        Tv_ISBN.setText("     ISBN                    : " + cur.getString(cur.getColumnIndex(DBHelper.row_isbn)));
                        Tv_judulbuku.setText("     Judul Buku        : " + cur.getString(cur.getColumnIndex(DBHelper.row_judul)));
                        Tv_Penulis.setText("     Penulis               : " + cur.getString(cur.getColumnIndex(DBHelper.row_penulis)));
                        Tv_TglTerbit.setText("     Tanggal Terbit  : " + cur.getString(cur.getColumnIndex(DBHelper.row_tglTerbit)));
                        Tv_JB.setText("     Jenis Buku        : " + cur.getString(cur.getColumnIndex(DBHelper.row_jb)));
                        Tv_Penerbit.setText("     Penerbit             : " + cur.getString(cur.getColumnIndex(DBHelper.row_penerbit)));

                        viewData.setPositiveButton("KEMBALI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        viewData.show();
                }
                switch (which){
                    case 1:
                        Intent iddata = new Intent(MainActivity.this, EditActivity.class);
                        iddata.putExtra(DBHelper.row_id, id);
                        startActivity(iddata);
                }
                switch (which){
                    case 2:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage("Data buku ini akan dihapus.");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteData(id);
                                Toast.makeText(MainActivity.this, "Data Buku Telah Terhapus", Toast.LENGTH_SHORT).show();
                                setListView();
                            }
                        });
                        builder1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }
}