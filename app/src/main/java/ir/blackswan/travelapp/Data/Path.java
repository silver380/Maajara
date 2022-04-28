package ir.blackswan.travelapp.Data;

import android.content.Context;

import java.io.Serializable;

import ir.blackswan.travelapp.Controller.PathController;

public class Path implements Serializable {
    private String serverPath , localPath;

    public Path(String serverPath, String localPath) {
        this.serverPath = serverPath;
        this.localPath = localPath;
    }

    public void setLocalPath(Context context ,String localPath) {
        this.localPath = localPath;
        PathController.save(context);
        //updateServer
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getServerPath() {
        return serverPath;
    }
}
