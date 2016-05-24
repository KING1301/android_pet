package com.example.ml.petdemo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/*
 *闹钟信息SQLITE存储类
 * 初始化和维护闹钟数据库信息
 */
public class Database extends SQLiteOpenHelper {
    public static final String ALARM_TABLE = "alarm";
    public static final String COLUMN_ALARM_ID = "_id";
    public static final String COLUMN_ALARM_ACTIVE = "alarm_active";
    public static final String COLUMN_ALARM_TIME = "alarm_time";
    public static final String COLUMN_ALARM_DAYS = "alarm_days";
    public static final String COLUMN_ALARM_RING = "alarm_ring";
    public static final String COLUMN_ALARM_VIBRATE = "alarm_vibrate";
    public static final String COLUMN_ALARM_NAME = "alarm_name";
    static final String DATABASE_NAME = "DB";
    static final int DATABASE_VERSION = 1;
    static Database instance = null;
    static SQLiteDatabase database = null;

    /**
     * 调用父类构造器，创建数据库
     */
    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 获取对象事例
     */
    public static void init(Context context) {
        if (null == instance) {
            instance = new Database(context);
        }
    }

    /**
     * 通过对象事例获取数据库对象
     */
    public static SQLiteDatabase getDatabase() {
        if (null == database) {
            database = instance.getWritableDatabase();
        }
        return database;
    }

    /**
     * 关闭数据库
     */
    public static void deactivate() {
        if (null != database && database.isOpen()) {
            database.close();
        }
        database = null;
        instance = null;
    }

    /**
     * 向数据库中插入ALARM类对象数据
     */
    public static long create(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e) {
        }

        cv.put(COLUMN_ALARM_RING, alarm.getAlarmRing());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

        return getDatabase().insert(ALARM_TABLE, null, cv);
    }

    /**
     * 更新数据库中ALARM对象的数据
     */
    public static int update(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e) {
        }

        cv.put(COLUMN_ALARM_RING, alarm.getAlarmRing());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

        return getDatabase().update(ALARM_TABLE, cv, "_id=" + alarm.getId(), null);
    }

    public static int deleteEntry(Alarm alarm) {
        return deleteEntry(alarm.getId());
    }

    /**
     * 数据库中删除相应ID的ALARM对象数据
     */
    public static int deleteEntry(int id) {
        return getDatabase().delete(ALARM_TABLE, COLUMN_ALARM_ID + "=" + id, null);
    }

    public static Cursor getCursor() {

        String[] columns = new String[]{
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_RING,
                COLUMN_ALARM_VIBRATE,
                COLUMN_ALARM_NAME
        };
        return getDatabase().query(ALARM_TABLE, columns, null, null, null, null,
                null);
    }

    /**
     * 获取数据库中所有的ALARM对象并返回相应的ArrayList 用于初始化listview的适配器
     */
    public static List<Alarm> getAll() {
        List<Alarm> alarms = new ArrayList<Alarm>();
        Cursor cursor = Database.getCursor();
        if (cursor.moveToFirst()) {

            do {

                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setAlarmActive(cursor.getInt(1) == 1);
                alarm.setAlarmTime(cursor.getString(2));
                byte[] repeatDaysBytes = cursor.getBlob(3);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                        repeatDaysBytes);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(
                            byteArrayInputStream);
                    Alarm.Day[] repeatDays;
                    Object object = objectInputStream.readObject();
                    if (object instanceof Alarm.Day[]) {
                        repeatDays = (Alarm.Day[]) object;
                        alarm.setDays(repeatDays);
                    }
                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                alarm.setAlarmRing(cursor.getString(4));
                alarm.setVibrate(cursor.getInt(5) == 1);
                alarm.setAlarmName(cursor.getString(6));

                alarms.add(alarm);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return alarms;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**创建ALARM数据库表 数据库首次创建时执行*/
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARM_TABLE + " ( "
                + COLUMN_ALARM_ID + " INTEGER primary key autoincrement, "
                + COLUMN_ALARM_ACTIVE + " INTEGER NOT NULL, "
                + COLUMN_ALARM_TIME + " TEXT NOT NULL, "
                + COLUMN_ALARM_DAYS + " BLOB NOT NULL, "
                + COLUMN_ALARM_RING + " TEXT NOT NULL, "
                + COLUMN_ALARM_VIBRATE + " INTEGER NOT NULL, "
                + COLUMN_ALARM_NAME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        onCreate(db);
    }
}