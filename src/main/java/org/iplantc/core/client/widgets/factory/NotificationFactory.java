package org.iplantc.core.client.widgets.factory;

import org.iplantc.core.client.widgets.metadata.WizardNotification;

public abstract class NotificationFactory {

    public abstract WizardNotification buildNotification(String json);

}
