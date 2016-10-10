package br.com.curubodenga.pimfeeder.bluetooth;

public class Properties {
    private static Properties mInstance = null;
    public boolean connected;

    public static synchronized Properties getInstance() {
        if (null == mInstance) {
            mInstance = new Properties();
            mInstance.setConnected(false);
        }
        return mInstance;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
