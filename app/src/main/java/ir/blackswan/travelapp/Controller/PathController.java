package ir.blackswan.travelapp.Controller;

import android.content.Context;

import java.util.Random;

import ir.blackswan.travelapp.Data.Path;
import ir.blackswan.travelapp.Utils.SharedPrefManager;
import ir.blackswan.travelapp.ui.Activities.AuthActivity;

public class PathController extends Controller {
    private static final String FAKE_PATHS = "paths";
    static Path[] paths;
    static boolean hasBeenLoaded = false;

    public PathController(AuthActivity authActivity) {
        super(authActivity);
    }

    public static Path getRandomPath(Context context) {
        load(context);
        return paths[new Random().nextInt(paths.length)];
    }

    public static void save(Context context) {
        SharedPrefManager sh = new SharedPrefManager(context);
        sh.putString(FAKE_PATHS , gson.toJson(paths));
    }

    public static void load(Context context) {
        if (hasBeenLoaded)
            return;
        hasBeenLoaded = true;
        SharedPrefManager sh = new SharedPrefManager(context);
        String pathListJson = sh.getString(FAKE_PATHS);
        if (pathListJson == null){
            paths = new Path[4];
            paths[0] = new Path("https://images.pexels.com/photos/2754200/pexels-photo-2754200.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",null);
            paths[1] = new Path("https://images.pexels.com/photos/3512848/pexels-photo-3512848.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",null);
            paths[2] = new Path("https://images.pexels.com/photos/1670187/pexels-photo-1670187.jpeg?auto=compress&cs=tinysrgb&w=600",null);
            paths[3] = new Path("https://images.pexels.com/photos/3571576/pexels-photo-3571576.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",null);
            save(context);
        }else
            paths = gson.fromJson(pathListJson , Path[].class);
    }
}
