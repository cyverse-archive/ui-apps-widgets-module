package org.iplantc.core.client.widgets.metadata;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.factory.WizardNotificationFactory;
import org.iplantc.core.metadata.client.property.groups.PropertyGroupContainer;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class WizardPropertyGroupContainer extends PropertyGroupContainer {
    private List<WizardNotification> notifications;

    public WizardPropertyGroupContainer(String json) {
        super(json);
    }

    public WizardPropertyGroupContainer(final JSONObject objJson) {
        super(objJson);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void beforeParse() {
        super.beforeParse();
        notifications = new ArrayList<WizardNotification>();
    }

    /**
     * Retrieve wizard notifications
     * 
     * @return the notifications
     */
    public List<WizardNotification> getNotifications() {
        return notifications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseData(final JSONObject json) {
        super.parseData(json);
        parseWizardNotifications(json);
    }

    private void parseWizardNotifications(final JSONObject jsonObj) {
        WizardNotificationFactory factory = new WizardNotificationFactory();
        JSONValue val = jsonObj.get("wizardNotifications");

        if (val != null) {
            JSONArray arr = val.isArray();
            for (int i = 0,len = arr.size(); i < len; i++) {
                notifications.add(factory.buildNotification(arr.get(i).toString()));
            }
        }
    }

}
