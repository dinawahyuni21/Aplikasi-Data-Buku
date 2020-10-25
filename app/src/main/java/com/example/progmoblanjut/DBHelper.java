package com.example.progmoblanjut;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class DBHelper extends SQLiteOpenHelper {

        public static final String database_name = "db_buku";
        public static final String table_name = "tabel_buku";

        public static final String row_id = "_id";
        public static final String row_isbn = "ISBN";
        public static final String row_judul = "judulbuku";
        public static final String row_jb = "JB";
        public static final String row_penulis = "Penulis";
        public static final String row_tglTerbit = "TglTerbit";
        public static final String row_penerbit = "Penerbit";

        private SQLiteDatabase db;

        public DBHelper(Context context) {
            super(context, database_name, null, 2);
            db = getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + table_name + "(" + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + row_isbn + " TEXT, " + row_judul + " TEXT, " + row_jb + " TEXT, "
                    + row_penulis + " TEXT, " + row_tglTerbit + " TEXT, " + row_penerbit + " TEXT)";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int x) {
            db.execSQL("DROP TABLE IF EXISTS " + table_name);
        }

        //Get All SQLite Data
        public Cursor allData(){
            Cursor cur = db.rawQuery("SELECT * FROM " + table_name, null);
            return cur;
        }

        //Get 1 Data By ID
        public Cursor oneData(Long id){
            Cursor cur = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + row_id + "=" + id, null);
            return cur;
        }

        //Insert Data to Database
        public void insertData(ContentValues values){
            db.insert(table_name, null, values);
        }

        //Update Data
        public void updateData(ContentValues values, long id){
            db.update(table_name, values, row_id + "=" + id, null);
        }

        //Delete Data
        public void deleteData(long id){
            db.delete(table_name, row_id + "=" + id, null);
        }
    }
