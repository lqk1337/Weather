package no.semester.oppgave.weather;
/* class to check if user has granted permissions */
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

class UtilPermissions {
        static boolean hasPermissions(Context context, String... allPermissionNeeded)
        {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && context != null && allPermissionNeeded != null)
                for (String permission : allPermissionNeeded)
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                        return false;
            return true;
        }
}
