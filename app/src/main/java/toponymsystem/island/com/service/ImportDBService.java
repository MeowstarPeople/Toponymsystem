package toponymsystem.island.com.service;

import android.app.IntentService;
import android.content.Intent;

import toponymsystem.island.com.utils.ImportIslandDataBase;

/**
 * Created by Yong
 * on 2016/4/27.
 * E-Mail:hellocui@aliyun.com
 */
public class ImportDBService extends IntentService {

    private ImportIslandDataBase importDataBase;

    public ImportDBService() {
        super("ImportDBService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        importDataBase = new ImportIslandDataBase(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        importDataBase.openOrImportDatabase();
    }
}
