package ir.blackswan.travelapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionRequest {
    public static void storage(Activity activity , int requestCode) {
        requestPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE , requestCode);
        requestPermission(activity , Manifest.permission.WRITE_EXTERNAL_STORAGE , requestCode);
    }

    public static void requestPermission(Activity activity , String permission , int requestCode){
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
        }
    }
}
