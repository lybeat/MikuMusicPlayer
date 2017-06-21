package cc.sayaki.music.exception;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import cc.sayaki.music.utils.FileUtil;
import cc.sayaki.music.utils.TimeUtil;

/**
 * Author: sayaki
 * Date: 2016/9/5
 */
public class CrashException implements Thread.UncaughtExceptionHandler {

    @SuppressLint("StaticFieldLeak")
    private static CrashException crashException;
    private Context context;

    private CrashException() {}

    public static CrashException getInstance() {
        if (crashException == null) {
            synchronized (CrashException.class) {
                if (crashException == null) {
                    crashException = new CrashException();
                }
            }
        }
        return crashException;
    }

    public void init(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable throwable) {
        saveInfo(context, throwable);

        try {
            Thread.sleep(2000);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveInfo(Context context, Throwable throwable) {
        String fileName;
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : obtainSimpleInfo(context).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        sb.append(obtainExceptionInfo(throwable));

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File root = FileUtil.createRoot("miku");
            final File child = FileUtil.createChild(root, "crash_log");

            try{
                fileName = child.toString() + File.separator + TimeUtil.getCurrentTime("yyyy-MM-dd_HH_mm_ss") + ".log";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();

        return mStringWriter.toString();
    }

    private HashMap<String, String> obtainSimpleInfo(Context context){
        HashMap<String, String> map = new HashMap<>();
        PackageManager pm = context.getPackageManager();
        PackageInfo mPackageInfo;
        try {
            mPackageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            map.put("versionName", mPackageInfo.versionName);
            map.put("versionCode", "" + mPackageInfo.versionCode);

            map.put("MODEL", "" + Build.MODEL);
            map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
            map.put("PRODUCT", "" +  Build.PRODUCT);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }

}
