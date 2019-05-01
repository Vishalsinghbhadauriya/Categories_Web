package com.example.vishalsingh.categories_web.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "Categories";
    public static final String TABLE = "Category";
    public static final String COMPANYTABALE = "Company";
    public static final String DepartmentTABLE = "Department";

    public static final String id = "id";
    public static final String name = "name";
    public static final String icon = "icon";

    public static final String ctid = "categoryid";
    public static final String cname = "name";
    public static final String cpid = "company_id";

    public static final String categoryId = "category_id";
    public static final String departmentId = "department_id";
    public static final String departmentName = "departmentName";
    public static final String companyId = "company_id";
    public static final String companyName = "companyName";
    public static final String title = "title";
    public static final String mobile = "mobile";
    public static final String landLine = "land_line";
    public static final String colorCode = "colorCode";
    public static final String description = "description";
    public static final String openingTime = "openingTime";
    public static final String address1 = "address1";
    public static final String address2 = "address2";
    public static final String address3 = "address3";
    public static final String address4 = "address4";
    public static final String address5 = "address5";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String emailAddress = "emailAddress";
    public static final String companyLink = "companyLink";


    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" create table " + TABLE + "(id TEXT PRIMARY KEY,name TEXT,icon TEXT )");
        db.execSQL(" create table " + COMPANYTABALE + "(categoryid TEXT ,name TEXT,company_id TEXT PRIMARY KEY)");
        db.execSQL(" create table " + DepartmentTABLE + "(category_id TEXT ,department_id TEXT ,departmentName TEXT,company_id TEXT PRIMARY KEY,companyName TEXT ," +
                "title TEXT ,mobile TEXT,land_line TEXT,colorCode TEXT,description TEXT ," +
                "openingTime TEXT ,address1 TEXT,address2 TEXT,address3 TEXT,address4 TEXT ," +
                "address5 TEXT ,latitude TEXT,longitude TEXT,emailAddress TEXT,companyLink TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COMPANYTABALE);
        db.execSQL("DROP TABLE IF EXISTS " + DepartmentTABLE);
        onCreate(db);
    }

    public boolean insertData(String Id, String Name, String Icon) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(id, Id);
        contentValues.put(name, Name);
        contentValues.put(icon, Icon);
        //long result = db.insert(TABLE, null, contentValues);
      long result = db.insertWithOnConflict(TABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllDAta() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE, null);
        return res;
    }



    public boolean insertCompanyData(String ctId, String cName, String cpId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ctid, ctId);
        contentValues.put(cname, cName);
        contentValues.put(cpid, cpId);
        long result = db.insertWithOnConflict(COMPANYTABALE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getCompanyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor restwo = db.rawQuery("select * from " + COMPANYTABALE, null);
        return restwo;

    }

    public Cursor getCompanyDataId(String idd) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor restwo = db.rawQuery("select * from " + COMPANYTABALE + " where " + ctid + "=" +idd, null);
        return restwo;

    }

    public boolean insertDepartmentData(String CategoryId, String DepartmentId, String DepartmentName, String CompanyId, String CompanyName,
                                        String Title, String Mobile, String LandLine, String ColorCode, String Description,
                                        String OpeningTime, String Address1, String Address2, String Address3, String Address4,
                                        String Address5, String Latitude, String Longitude, String EmailAddress, String CompanyLink) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(categoryId,CategoryId);
        contentValues.put(departmentId,DepartmentId);
        contentValues.put(departmentName,DepartmentName);
        contentValues.put(companyId,CompanyId);
        contentValues.put(companyName,CompanyName);
        contentValues.put(title,Title);
        contentValues.put(mobile,Mobile);
        contentValues.put(landLine,LandLine);
        contentValues.put(colorCode,ColorCode);
        contentValues.put(description,Description);
        contentValues.put(openingTime,OpeningTime);
        contentValues.put(address1,Address1);
        contentValues.put(address2,Address2);
        contentValues.put(address3,Address3);
        contentValues.put(address4,Address4);
        contentValues.put(address5,Address5);
        contentValues.put(latitude,Latitude);
        contentValues.put(longitude,Longitude);
        contentValues.put(emailAddress,EmailAddress);
        contentValues.put(companyLink,CompanyLink);

        long result = db.insertWithOnConflict(DepartmentTABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        if (result == -1)
            return false;
        else
            return true;
    }
    public  Cursor getDepartmentData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resthree = db.rawQuery("select * from " + DepartmentTABLE, null);
        return resthree;
    }
    public Cursor getDepartmentDataCTIDandCPID(String ctid , String cpid ){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resthree = db.rawQuery("select * from " + DepartmentTABLE + " where " + categoryId + "=" + ctid + " and " + companyId + "=" + cpid ,null);
            return resthree;
    }
    public  Integer deleteData(String id){

        SQLiteDatabase db=this.getWritableDatabase();
        return  db.delete(TABLE,"ID = ?",new String[]{id});
    }
    public  Integer deletecomapnyData(String company_id){

        SQLiteDatabase db=this.getWritableDatabase();
        return  db.delete(COMPANYTABALE,"COMPANY_ID = ?",new String[]{company_id});
    }
        public  Integer deleteDepartmentData(String company_id){

        SQLiteDatabase db=this.getWritableDatabase();
        return  db.delete(DepartmentTABLE,"company_id = ?",new String[]{company_id});
    }
    public  Integer upadateData(String id,String Name){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
       // contentValues.put(ctid, ctId);
        contentValues.put(name, Name);
        return  db.update(TABLE,contentValues,"ID = ?",new String[]{id});
    }
    public  Integer upadateCompanyData(String id,String Name){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(cname, Name);
        return  db.update(COMPANYTABALE,contentValues,"company_id = ?",new String[]{id});
    }
    public  Integer upadateDepartmentData(String id,String Name){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(departmentName, Name);
        return  db.update(DepartmentTABLE,contentValues,"department_id = ?",new String[]{id});
    }
}
