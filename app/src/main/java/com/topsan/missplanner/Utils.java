package com.topsan.missplanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Calendar;

/**
 * Created by JDG on 2018-06-08.
 */

public class Utils {
    public static final String TAG = "Utils";
    public static App mApp = null;

    // load image file and change Bitmap
    public static Bitmap loadImage(String filePath, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        // load image file
        try {
            options.inSampleSize = sampleSize;
            bitmap = BitmapFactory.decodeFile(filePath, options);

        }
        // if image size is too big, reduce size and try again
        catch( OutOfMemoryError e) {
            bitmap = loadImage(filePath, sampleSize * 2);
        }
        return bitmap;
    }

    // get file name from file path
    public static String filterFileName(String filePath) {
        int pos = -1;
        if( (pos = filePath.lastIndexOf("/")) >= 0 ) {
            filePath = filePath.substring(pos+1);
        }
        return filePath;
    }

    public static Calendar getThisWeekDate(Calendar date) {
        Calendar dateThisWeek = Calendar.getInstance();
        int nDayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        dateThisWeek.add(Calendar.DAY_OF_MONTH, nDayOfWeek - dateThisWeek.get(Calendar.DAY_OF_WEEK));

        dateThisWeek.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
        dateThisWeek.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
        dateThisWeek.set(Calendar.SECOND, date.get(Calendar.SECOND));

        return dateThisWeek;
    }

}
