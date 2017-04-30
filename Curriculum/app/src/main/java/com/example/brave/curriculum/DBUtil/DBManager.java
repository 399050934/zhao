package com.example.brave.curriculum.DBUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.brave.curriculum.Classtable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QD on 2017/4/13.
 */

public class DBManager {
    private static final String TAG = "DBManager";
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context,int version)
    {
        Log.d(TAG, "DBManager --> Constructor");
        helper = new DatabaseHelper(context,version);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add table
     *
     * @param classtable
     */
    public void addType1(Classtable classtable)
    {
        Log.d(TAG, "DBManager --> add");
        if(classtable == null){
            return;
        } else if(!(classtable.getInfo().equals(" "))){
            //判断有无重复数据
            if(selectFromDB(classtable.getParameter())==null){

                // 采用事务处理，确保数据完整性
                db.beginTransaction(); // 开始事务
                try
                {
                    Log.d(TAG, "add: "+classtable.getInfo());
                    db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME1
                            + " VALUES(null, ?, ?, ?)",new Object[] {classtable.getInfo(),
                            transToByte(classtable.getClasstable()),classtable.getParameter() });
                    // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                    // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                    // 使用占位符有效区分了这种情况

                    db.setTransactionSuccessful(); // 设置事务成功完成
                }
                finally
                {
                    db.endTransaction(); // 结束事务
                }
                Log.d(TAG, "addType1: 保存成功！！！！！！！！！");
            }else{
                Log.d(TAG, "addType1: 有重复数据");
                return;
            }
        }else {
            Log.d(TAG, "addType1: 没有课程表！！！！！！！！！");

            return;
        }
        return;
    }
    public void addType2(){

    }

//    /**
//     * update class Vlaue
//     *
//     * @param value
//     */
//    public void updateAge(Person value)
//    {
//        Log.d(AppConstants.LOG_TAG, "DBManager --> updateAge");
//        ContentValues cv = new ContentValues();
//        cv.put("age", person.age);
//        db.update(DatabaseHelper.TABLE_NAME1, cv, "name = ?",
//                new String[] { person.name });
//    }
//
//    /**
//     * delete old person
//     *
//     * @param person
//     */
//    public void deleteOldPerson(Person person)
//    {
//        Log.d(AppConstants.LOG_TAG, "DBManager --> deleteOldPerson");
//        db.delete(DatabaseHelper.TABLE_NAME1, "age >= ?",
//                new String[] { String.valueOf(person.age) });
//    }

    /**
     * query all persons, return list
     *
     * @return List<Person>
     */
    public List<Classtable> query()
    {
        Log.d(TAG, "DBManager --> query");
        ArrayList<Classtable> classtable = new ArrayList<Classtable>();
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            Classtable classname = new Classtable();
            classname.setId(c.getInt(c.getColumnIndex("id")));
            classname.setInfo(c.getString(c.getColumnIndex("info")));
            classname.setClasstable(transToString(c.getBlob(c.getColumnIndex("tables"))));
            classname.setParameter(c.getString(c.getColumnIndex("params")));
            classtable.add(classname);
        }
        c.close();
        return classtable;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor()
    {
        Log.d(TAG, "DBManager --> queryTheCursor");
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME1,
                null);
        return c;
    }

    public Cursor selectTable(String params) {
        Cursor c = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME1+" WHERE params = ?",new String[]{params});
        Log.d(TAG, "selectTable: 有几个数据"+c.getCount());
        if(c.getCount()==0) {
            Log.d(TAG, "selectTable: 表为空");
            return null;
        }
        Log.d(TAG, "selectTable: "+c.toString());
        Log.d(TAG, "selectTable: 表信息"+c.moveToFirst());

        return c;
    }
    public Classtable selectFromDB(String params){
        Log.d(TAG, "selectFromDB: 参数是"+params);
        Cursor c = selectTable(params);
        if(c!=null) {
            c.moveToFirst();

            Classtable classtable = new Classtable();
            classtable.setId(c.getInt(c.getColumnIndex("id")));
            classtable.setInfo(c.getString(c.getColumnIndex("info")));
            classtable.setClasstable(transToString(c.getBlob(c.getColumnIndex("tables"))));
            classtable.setParameter((c.getString(c.getColumnIndex("params"))));
            Log.d(TAG, "selectFromDB: 数据存在"+classtable.getInfo()+classtable.getParameter());
            return classtable;

        }else if (c==null) {
            Log.d(TAG, "selectFromDB: 返回空对象");
            return null;
        }
        Log.d(TAG, "selectFromDB: 数据库为空");
        return null;
    }

    /**
     * close database
     */
    public void closeDB()
    {
        Log.d(TAG, "DBManager --> closeDB");
        // 释放数据库资源
        db.close();
    }
    public byte[] transToByte(String [][] tables ) {
        ByteArrayOutputStream arrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        byte data[] = new byte[0];
        try {
            arrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(tables);
            objectOutputStream.flush();
            data = arrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                objectOutputStream.close();
                arrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
    public  String[][] transToString(byte[] data) {
        ByteArrayInputStream arrayInputStream = null;
        ObjectInputStream objectInputStream =null;
        String [][] table =null;
        try {
            arrayInputStream = new ByteArrayInputStream(data);
            objectInputStream=new ObjectInputStream(arrayInputStream);
            table = (String [][])objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                objectInputStream.close();
                arrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return table;
    }




}
