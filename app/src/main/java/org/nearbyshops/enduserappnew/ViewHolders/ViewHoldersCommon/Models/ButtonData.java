package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models;

/**
 * Created by sumeet on 4/12/16.
 */

public class ButtonData {

    public ButtonData(String heading) {
        this.heading = heading;
    }

    public ButtonData(String heading, int icon, int requestCode) {
        this.heading = heading;
        this.icon = icon;
        this.requestCode = requestCode;
    }

    public ButtonData() {
    }

    private String heading;
    private int icon;
    private int requestCode;




    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
