package ir.blackswan.travelapp.ui.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;

import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.databinding.ActivityFullscreenImageBinding;

public class FullscreenImageActivity extends ToolbarActivity {

    public static final String IMAGE_URL = "image" , BITMAP = "bitmap";
    ActivityFullscreenImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityFullscreenImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setFullscreen();
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String uri = intent.getStringExtra(IMAGE_URL);
        if (uri != null) {
            File file = new File(uri);
            setImageByFile(file);
        }else {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(this
                        .openFileInput("myImage"));
                binding.myZoomageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", "onCreate: ", e);
            }
        }
        float alpha = binding.tbActivity.getAlpha();
        binding.tbActivity.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(binding.getRoot());
            if (binding.tbActivity.getAlpha() > 0)
                binding.tbActivity.setAlpha(0);
            else
                binding.tbActivity.setAlpha(alpha);
        });

    }

    public void setImageByFile(File pictureFile) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap myBitmap = BitmapFactory.decodeFile(pictureFile.getPath());
                return myBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                binding.myZoomageView.setImageBitmap(bitmap);
            }
        }.execute();
    }


    private void setFullscreen() {
        Utils.changeStatusColor(this , R.color.colorBlack);
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }
}