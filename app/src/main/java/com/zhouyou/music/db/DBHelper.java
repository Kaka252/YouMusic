package com.zhouyou.music.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhouyou.music.entity.Audio;

import java.sql.SQLException;

/**
 * 作者：ZhouYou
 * 日期：2016/12/28.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DBHelper";

    private Dao<Audio, Integer> dao;

    private DBHelper(Context context) {
        super(context, DBConfig.DB_NAME, null, DBConfig.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        Log.i(TAG, "onCreate");
        try {
            TableUtils.createTableIfNotExists(connectionSource, Audio.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Audio.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DBHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DBHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    public Dao<Audio, Integer> getMusicDao() throws SQLException {
        if (dao == null) {
            dao = getDao(Audio.class);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        dao = null;
    }
}
