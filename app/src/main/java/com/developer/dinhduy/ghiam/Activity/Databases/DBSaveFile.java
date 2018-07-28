package com.developer.dinhduy.ghiam.Activity.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.developer.dinhduy.ghiam.Activity.File;



public class DBSaveFile extends SQLiteOpenHelper{
   private static OnDatabaseChangedListener databaseChangedListener;
    private static final int DBVersion=1;
    private static final String DBName="SAVE365.db";
    private static final String NameTable="TABLE_365";
    private static final String COL1="ID";
    private static final String COL2="NAME_FILE";
    private static final String COL3="FILE_LENGTH";
    private static final String COL4="DATE_TIME";
    private static final String COL5="FILE_PATH";
    private static  final String CREATE_SQL="CREATE TABLE "+NameTable+" ("+
            COL1+" INTEGER PRIMARY KEY," +
            COL2+" TEXT,"+
            COL3+" INTEGER,"+
            COL4+" INTEGER,"+
            COL5+" TEXT)";
    public DBSaveFile(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SQL);

    }
    public  void setOnDatabaseChangedListener(OnDatabaseChangedListener listener) {
        databaseChangedListener = listener;
    }
    public File GetItem(int Postion){
       SQLiteDatabase db=getReadableDatabase();
       //Mảng chứa Các Cột
       String Arr []={COL1, COL2, COL3, COL4, COL5};
       //Thuc chất nó là Select * From Table
       Cursor cursor=db.query(NameTable,Arr,null,null,null,null,null);
       if(cursor.moveToPosition(Postion)){
           File item=new File();
           item.setmId(cursor.getInt(cursor.getColumnIndex(COL1)));
           item.setNameFile(cursor.getString(cursor.getColumnIndex(COL2)));
           item.setTimeFile(cursor.getInt(cursor.getColumnIndex(COL3)));
           item.setDateCreateFile(cursor.getLong(cursor.getColumnIndex(COL4)));
           item.setFilePath(cursor.getString(cursor.getColumnIndex(COL5)));
           cursor.close();
         return item;
       }
        return  null;
    }
    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COL1 };
        Cursor c = db.query(NameTable, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long InsertData(String recordingName,String filePath,long length){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, recordingName);
        cv.put(COL5, filePath);
        cv.put(COL3, length);
        cv.put(COL4, System.currentTimeMillis());
        long rowId = db.insert(NameTable, null, cv);

        if (databaseChangedListener != null) {
            databaseChangedListener.onNewDatabaseEntryAdded();
            Log.d("365", "InsertData: Success! ");
        }
        return rowId;
    }
    public void DeleteData(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = { String.valueOf(id) };
        db.delete(NameTable, COL1+"=?", whereArgs);
    }
    public void UpdateData(File item ,String NewName,String FilePath){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, NewName);
        cv.put(COL5, FilePath);
        db.update(NameTable, cv,COL1+ "=" + item.getmId(), null);

        if (databaseChangedListener != null) {
            databaseChangedListener.onDatabaseEntryRenamed();
            Log.d("365", "Update: Success! ");
        }
    }
}
