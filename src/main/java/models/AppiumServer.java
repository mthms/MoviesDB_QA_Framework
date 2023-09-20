package models;

import io.appium.java_client.service.local.AppiumDriverLocalService;

public class AppiumServer {
    private AppiumDriverLocalService appiumLocalService;
    private int driverPort;
    private int listenerPort;

    public AppiumDriverLocalService getAppiumLocalService() {
        return appiumLocalService;
    }

    public void setAppiumLocalService(AppiumDriverLocalService appiumLocalService) {
        this.appiumLocalService = appiumLocalService;
    }

    public int getDriverPort() {
        return driverPort;
    }

    public void setDriverPort(int driverPort) {
        this.driverPort = driverPort;
    }

    public int getListenerPort() {
        return listenerPort;
    }

    public void setListenerPort(int listenerPort) {
        this.listenerPort = listenerPort;
    }
}
