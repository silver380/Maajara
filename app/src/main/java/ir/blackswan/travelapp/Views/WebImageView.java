package ir.blackswan.travelapp.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.content.res.AppCompatResources;

import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import ir.blackswan.travelapp.Data.Path;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.WebDownloader;

public class WebImageView extends RoundedImageView {

    public WebImageView(Context context) {
        super(context);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    public void setImagePath(Path path) {
        if (path.getLocalPath() != null) {
            setImageByFile(new File(path.getLocalPath()));
        } else if (path.getServerPath() != null) {

            new WebDownloader(getContext(), downloadedFile -> {
                if (downloadedFile != null) {
                    setImageByFile(downloadedFile);
                }else {
                    setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.image_not_found));
                }
                Log.d("WebDownloader", "setImagePath: onDownloadFinish");
            }).execute(path.getServerPath());
        }else {
            setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.image_not_found));
        }

    }

    public void setImageByFile(File pictureFile) {
        Bitmap myBitmap = BitmapFactory.decodeFile(pictureFile.getPath());
        setImageBitmap(myBitmap);
    }



}
