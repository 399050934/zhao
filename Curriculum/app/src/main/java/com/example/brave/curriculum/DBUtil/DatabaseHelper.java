package com.example.brave.curriculum.DBUtil;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by QD on 2017/4/13.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    // 数据库版本号
    private static final int DATABASE_VERSION = 1;
    // 数据库名
    private static final String DATABASE_NAME = "ClassDB.db";

    // 数据表名，一个数据库中可以有多个表
    public static final String TABLE_NAME1 = "curriculum_type1";
    public static final String TABLE_NAME2 = "curriculum_type2";


    // 构造函数，调用父类SQLiteOpenHelper的构造函数
    public DatabaseHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);

    }

    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        // SQLiteOpenHelper的构造函数参数：
        // context：上下文环境
        // name：数据库名字
        // factory：游标工厂（可选）
        // version：数据库模型版本号
    }

    public DatabaseHelper(Context context ,int verison) {
        super(context, DATABASE_NAME, null, verison);

        // 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时
        Log.d(TAG, "DatabaseHelper Constructor");
        // CursorFactory设置为null,使用系统默认的工厂类
    }

    // 继承SQLiteOpenHelper类,必须要覆写的三个方法：onCreate(),onUpgrade(),onOpen()
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // 调用时间：数据库第一次创建时onCreate()方法会被调用

        // onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
        // 这个方法中主要完成创建数据库后对数据库的操作

        Log.d(TAG, "DatabaseHelper onCreate");

        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE table " + TABLE_NAME1 + "(");
        sBuffer.append("id INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("info TEXT ,");
        sBuffer.append("tables BLOB ,");
        sBuffer.append("params TEXT)");
        Log.d("SBSBSB ",sBuffer.toString());
        //第二个表，形式二
        String s = "CREATE TABLE "+TABLE_NAME2+"(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "info TEXT,items BLOB,params TEXT)";
        // 执行创建表的SQL语句

        db.execSQL(sBuffer.toString());
        db.execSQL(s);

        // 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // 调用时间：如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade

        // onUpgrade方法的三个参数，一个 SQLiteDatabase对象，一个旧的版本号和一个新的版本号
        // 这样就可以把一个数据库从旧的模型转变到新的模型
        // 这个方法中主要完成更改数据库版本的操作

        Log.d(TAG, "DatabaseHelper onUpgrade");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
        // 上述做法简单来说就是，通过检查常量值来决定如何，升级时删除旧表，然后调用onCreate来创建新表
        // 一般在实际项目中是不能这么做的，正确的做法是在更新数据表结构时，还要考虑用户存放于数据库中的数据不丢失

    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        // 每次打开数据库之后首先被执行

        Log.d(TAG, "DatabaseHelper onOpen");
    }
}
