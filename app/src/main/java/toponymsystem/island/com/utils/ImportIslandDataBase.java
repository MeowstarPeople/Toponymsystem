package toponymsystem.island.com.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import toponymsystem.island.com.R;

public class ImportIslandDataBase {
    public static final String DB_NAME = "island.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "toponymsystem.island.com";
    public static final String DB_PATH = File.separator + "data"
            + Environment.getDataDirectory().getAbsolutePath() + File.separator + PACKAGE_NAME + File.separator;  //在手机里存放数据库的位置
    //    private SQLiteDatabase database;
    private Context context;

    public ImportIslandDataBase(Context context) {
        this.context = context;
    }

    public void openOrImportDatabase() {
        openOrImportDatabase(DB_PATH + DB_NAME);
    }

    private void openOrImportDatabase(String dbFile) {
        SQLiteDatabase db = null;
        File mFileDir = new File(DB_PATH);
        try {
            if (!(new File(dbFile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                File file = new File(mFileDir, DB_NAME);
                InputStream is = this.context.getResources().openRawResource(R.raw.island_db); //欲导入的数据库
                int len = is.available();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[len];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } else {
            }
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
    }
}
