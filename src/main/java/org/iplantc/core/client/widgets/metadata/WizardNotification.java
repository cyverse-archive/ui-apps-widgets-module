package org.iplantc.core.client.widgets.metadata;

import java.util.List;

import org.iplantc.core.client.widgets.utils.Observer;

/**
 * 
 * Represents a Wizard Notification. A Wizard Notification typically consist of the sender, receivers.
 * Extends Observer.
 * 
 * @author sriram
 * 
 */
public abstract class WizardNotification implements Observer {

    private List<String> notificationReceviers;
    private String notificationSender;

    public void setNotificationReceviers(List<String> notificationReceviers) {
        this.notificationReceviers = notificationReceviers;
    }

    public List<String> getNotificationReceviers() {
        return notificationReceviers;
    }

    public void setNotificationSender(String notificationSender) {
        this.notificationSender = notificationSender;
    }

    public String getNotificationSender() {
        return notificationSender;
    }

}
