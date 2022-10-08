package com.mitulagr.office;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "DBTrips";

    private static final String TABLE_Employee = "TE";
    private static final String TABLE_Task = "TT";

    // Table Employee - Columns
    private static final String TE_email = "TE_email";
    private static final String TE_name = "TE_name";
    private static final String TE_number = "TE_number";
    private static final String TE_dep = "TE_dep";
    private static final String TE_join = "TE_join";
    private static final String TE_pass = "TE_pass";
    private static final String TE_active = "TE_active";

    // Table Task - Columns
    private static final String TT_ID = "TT_ID";
    private static final String TT_des = "TT_des";
    private static final String TT_type = "TT_type";
    private static final String TT_email = "TT_email";
    private static final String TT_date = "TT_date";
    private static final String TT_time = "TT_time";
    private static final String TT_mins = "TT_mins";

    DBHandler(Context context){
        super(context,DATABASE_NAME,null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TE = "CREATE TABLE "+TABLE_Employee+"("
                +TE_email+" TEXT PRIMARY KEY,"
                +TE_name+" TEXT,"
                +TE_number+" TEXT,"
                +TE_dep+" TEXT,"
                +TE_join+" TEXT,"
                +TE_pass+" TEXT,"
                +TE_active+" INTEGER"
                +")";
        db.execSQL(CREATE_TE);

        String CREATE_TT = "CREATE TABLE "+TABLE_Task+"("
                +TT_ID+" INTEGER PRIMARY KEY,"
                +TT_des+" TEXT,"
                +TT_type+" INTEGER,"
                +TT_email+" TEXT,"
                +TT_date+" TEXT,"
                +TT_time+" TEXT,"
                +TT_mins+" INTEGER,"
                +"FOREIGN KEY ("+TT_email+") REFERENCES "+TABLE_Employee+" ("+TE_email+"))";
        db.execSQL(CREATE_TT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Employee);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Task);
        onCreate(db);
    }

    /*
    =============================================================================
    Table Employee
    =============================================================================
     */

    public void addEmployee(Employee employee){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TE_email, employee.email);
        values.put(TE_name, employee.name);
        values.put(TE_number, employee.number);
        values.put(TE_dep, employee.dep);
        values.put(TE_join, employee.join);
        values.put(TE_pass, employee.pass);
        values.put(TE_active, employee.active);

        db.insert(TABLE_Employee, null, values);
        db.close();
    }

    public Employee getEmployee(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Employee, new String[]{
                        TE_email,TE_name,TE_number,TE_dep,TE_join,TE_pass,TE_active},
                TE_email+"=?", new String[]{email},
                null,null,null,null);

        if (c!=null) c.moveToFirst();

        Employee employee = new Employee();

        employee.email = c.getString(c.getColumnIndexOrThrow(TE_email));
        employee.name = c.getString(c.getColumnIndexOrThrow(TE_name));
        employee.number = c.getString(c.getColumnIndexOrThrow(TE_number));
        employee.dep = c.getString(c.getColumnIndexOrThrow(TE_dep));
        employee.join = c.getString(c.getColumnIndexOrThrow(TE_join));
        employee.pass = c.getString(c.getColumnIndexOrThrow(TE_pass));
        employee.active = c.getInt(c.getColumnIndexOrThrow(TE_active));

        return employee;
    }

    public List<Employee> getAllEmployees(){
        List<Employee> employeeList = new ArrayList<Employee>();

        String selectQuery = "SELECT * FROM "+TABLE_Employee;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        Employee employee = new Employee();
        if (c!=null && c.moveToFirst()){
            do {
                employee.email = c.getString(c.getColumnIndexOrThrow(TE_email));
                employee.name = c.getString(c.getColumnIndexOrThrow(TE_name));
                employee.number = c.getString(c.getColumnIndexOrThrow(TE_number));
                employee.dep = c.getString(c.getColumnIndexOrThrow(TE_dep));
                employee.join = c.getString(c.getColumnIndexOrThrow(TE_join));
                employee.pass = c.getString(c.getColumnIndexOrThrow(TE_pass));
                employee.active = c.getInt(c.getColumnIndexOrThrow(TE_active));
                employeeList.add(employee);
            } while (c.moveToNext());
        }

        return employeeList;
    }

    public int getEmployeesCount(){
        String countQuery = "SELECT "+TE_email+" FROM "+TABLE_Employee;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);
        return c.getCount();
    }

    public boolean EmailExist(String email){
        String countQuery = "SELECT "+TE_email+" FROM "+TABLE_Employee;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);
        if (c!=null && c.moveToFirst()) {
            do {
                if (email.equals(c.getString(c.getColumnIndexOrThrow(TE_email)))) return true;
            } while (c.moveToNext());
        }
        return false;
    }

    public int updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TE_email, employee.email);
        values.put(TE_name, employee.name);
        values.put(TE_number, employee.number);
        values.put(TE_dep, employee.dep);
        values.put(TE_join, employee.join);
        values.put(TE_pass, employee.pass);
        values.put(TE_active, employee.active);

        return db.update(TABLE_Employee, values, TE_email + "=?", new String[]{employee.email});
    }


    /*
    =============================================================================
    Table Task
    =============================================================================
     */

    public void addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TT_ID, task.ID);
        values.put(TT_des, task.des);
        values.put(TT_type, task.type);
        values.put(TT_email, task.email);
        values.put(TT_date, task.date);
        values.put(TT_time, task.time);
        values.put(TT_mins, task.mins);

        db.insert(TABLE_Task, null, values);
        db.close();
    }

    public int[] getDataToday(String email, String date) {
        int[] dataList = new int[]{0,0,0};

        String selectQuery = "SELECT "+TT_type+","+TT_mins+" FROM "+TABLE_Task
                +" WHERE "+TT_email+" = "+email+" AND "+TT_date+" = "+date;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        Employee employee = new Employee();
        if (c!=null && c.moveToFirst()){
            do {
                dataList[c.getInt(c.getColumnIndexOrThrow(TT_type))] +=
                        c.getInt(c.getColumnIndexOrThrow(TT_mins));
            } while (c.moveToNext());
        }

        return dataList;
    }

    public static String getLastWeek(String date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Date prev;
        try {
            prev = format.parse(date);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(prev);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public int[] getDataWeek(String email, String date) {
        int[] dataList = new int[]{0,0,0};

        String prev = getLastWeek(date);

        String selectQuery = "SELECT "+TT_type+","+TT_mins+" FROM "+TABLE_Task
                +" WHERE "+TT_email+" = "+email+" AND "+TT_date+" <= date("+date+")"
                +" AND "+TT_date+" >= date("+prev+")";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        Employee employee = new Employee();
        if (c!=null && c.moveToFirst()){
            do {
                dataList[c.getInt(c.getColumnIndexOrThrow(TT_type))] +=
                        c.getInt(c.getColumnIndexOrThrow(TT_mins));
            } while (c.moveToNext());
        }

        return dataList;
    }


}