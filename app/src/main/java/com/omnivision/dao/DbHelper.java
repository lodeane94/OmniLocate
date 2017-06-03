package com.omnivision.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.omnivision.core.DaoMaster;
import com.omnivision.core.DaoSession;

/**
 * Created by lkelly on 3/14/2017.
 */

public class  DbHelper  {
    private  static String TAG = "";
    private static SQLiteDatabase _db = null;
    private static DaoSession _session = null;
    private static final String _dbname = "OmniLocate";

    public DaoSession getSession(Context context){
        if(_session == null){
            _session = getMaster(context).newSession();
        }
        return _session;
    }

    public DaoSession getNewSession(Context context){
        return getMaster(context).newSession();
    }

    private DaoMaster getMaster(Context context){
        if(_db == null){
            _db = getDatabase(_dbname,false,context);
        }
        return new DaoMaster(_db);
    }

    private  synchronized SQLiteDatabase getDatabase(String dbName, boolean readOnly, Context context) {
        Log.i(TAG,"getDatabase name = "+dbName+" readOnly = "+readOnly);
        try{
            readOnly = false;
            SQLiteOpenHelper helper = new DAOOpenHelper(context,dbName,null);
            if(readOnly){
                return helper.getReadableDatabase();
            }else{
                return  helper.getWritableDatabase();
            }
        }catch (Exception ex){
            Log.e(TAG,"",ex);
            return null;
        }catch (Error err){
            Log.e(TAG,"",err);
            return null;
        }
    }

    private class DAOOpenHelper extends DaoMaster.OpenHelper{
        public DAOOpenHelper(Context context, String name,  SQLiteDatabase.CursorFactory factory){
            super(context, name,factory);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            Log.i(TAG,"Create DB-Schema (version = "+Integer.toString(DaoMaster.SCHEMA_VERSION)+")");
            super.onCreate(db);
        }
        /*
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "Update DB-Schema to version: "+Integer.toString(oldVersion)+"->"+Integer.toString(newVersion));
            switch (oldVersion) {
                case 1:
                    db.execSQL(SQL_UPGRADE_1To2);
                case 2:
                    db.execSQL(SQL_UPGRADE_2To3);
                    break;
                default:
                    break;
            }
        }*/
    }
}
