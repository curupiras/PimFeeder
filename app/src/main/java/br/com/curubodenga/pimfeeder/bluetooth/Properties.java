package br.com.curubodenga.pimfeeder.bluetooth;

public class Properties {
    private static Properties mInstance = null;
    public boolean connected;
    public boolean dateSync;

    public static synchronized Properties getInstance() {
        if (null == mInstance) {
            mInstance = new Properties();
            mInstance.setConnected(false);
            mInstance.setDateSync(false);
        }
        return mInstance;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isDateSync() {
        return dateSync;
    }

    public boolean isConnectedAndDateSync() {
        return connected && dateSync;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setDateSync(boolean dateSync) {
        this.dateSync = dateSync;
    }
}
