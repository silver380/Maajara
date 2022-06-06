package ir.blackswan.travelapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionRequest {
    public static boolean storage(Activity activity , int requestCode) {
        return requestPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE , requestCode);

    }

    public static boolean requestPermission(Activity activity , String permission , int requestCode){
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
            return false;
        }
        return true;
    }
}
