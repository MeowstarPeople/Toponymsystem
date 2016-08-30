package toponymsystem.island.com.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import toponymsystem.island.com.R;

public class UpdateService extends Service {
    public static final String FILE_PATH = "file_path";
    private static final String FILE_NAME = "island.apk";
    private static final int NOTIFY_ID = 999;
    private static final int BUFF_SIZE = 1024 * 4;
    private String mDownloadTextFormat;
    private File mFile;
    private String mDownloadPath;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mFile.exists()) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(mFile),
                                "application/vnd.android.package-archive");
                        startActivity(intent);
                    }
                    mNotificationManager.cancel(NOTIFY_ID);
                    break;
                default:
                    mNotificationManager.cancel(NOTIFY_ID);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setAutoCancel(false);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File folder = new File(Environment.getExternalStorageDirectory(), "IslandFile");
            if (!folder.exists() || !folder.isDirectory()) {
                folder.mkdir();
            }

            mFile = new File(folder, FILE_NAME);
            if (mFile.exists()) {
                mFile.delete();
            }
        }
        mDownloadTextFormat = getString(R.string.download_text_format);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        if (mFile == null) {
            Toast.makeText(this, R.string.warning_external_storage, Toast.LENGTH_SHORT).show();
            stopSelf();
        } else {
            mDownloadPath = intent.getStringExtra(FILE_PATH);
            if (mDownloadPath == null) {
                stopSelf();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        URL url;
                        HttpURLConnection connection = null;
                        try {
                            url = new URL(mDownloadPath);
                            connection = (HttpURLConnection) url.openConnection();
                            mFile.createNewFile();

                            InputStream inputStream = connection.getInputStream();
                            OutputStream outputStream = new FileOutputStream(mFile);
                            byte[] buffer = new byte[BUFF_SIZE];

                            connection.connect();

                            int readCount = 0;
                            int downloadCount = 0;
                            int length = connection.getContentLength();
                            int progress = -1;
                            while ((readCount = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, readCount);
                                downloadCount += readCount;
                                int newProgress = downloadCount * 100 / length;
                                if (newProgress > progress) {
                                    progress = newProgress;
                                    mBuilder.setProgress(100, progress, false);
                                    mBuilder.setContentText(String.format(mDownloadTextFormat, progress));
                                    mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
                                }
                            }
                            outputStream.flush();

                            Message msg = new Message();
                            msg.what = 0;
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (mFile.exists()) {
                                mFile.delete();
                            }
                            handler.obtainMessage(-1);
                        } finally {
                            if (connection != null) {
                                connection.disconnect();
                            }
                        }
                    }
                }).start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

