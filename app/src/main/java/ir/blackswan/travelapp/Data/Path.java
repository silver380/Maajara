package ir.blackswan.travelapp.Data;

public class Path {
    private String serverPath , localPath;

    public Path(String serverPath, String localPath) {
        this.serverPath = serverPath;
        this.localPath = localPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getServerPath() {
        return serverPath;
    }
}
