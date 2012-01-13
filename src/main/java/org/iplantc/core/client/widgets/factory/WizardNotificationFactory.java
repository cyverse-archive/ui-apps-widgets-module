package org.iplantc.core.client.widgets.factory;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.metadata.ToggleStateWizardNotification;
import org.iplantc.core.client.widgets.metadata.UpdateDataWizardNotification;
import org.iplantc.core.client.widgets.metadata.WizardNotification;
import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * A factory that builds WizardNotification with sender, receivers and right-type of notification
 * 
 * @author sriram
 * 
 */
public class WizardNotificationFactory extends NotificationFactory {

    @Override
    public WizardNotification buildNotification(String json) {
        WizardNotification wz = null;
        if (json != null && !json.equals("")) {
            JSONObject obj = JSONParser.parseStrict(json).isObject();
            List<String> receivers = parseReceivers(obj);
            wz = getNotificatioObject(JsonUtil.trim(obj.get("type").toString()));
            if (wz != null) {
                wz.setNotificationReceviers(receivers);
                wz.setNotificationSender(JsonUtil.trim(obj.get("sender").toString()));
            }
        }

        return wz;
    }

    private WizardNotification getNotificatioObject(String type) {
        if (type.equals("disableOnSelection")) {
            return new ToggleStateWizardNotification();
        } else if (type.equals("updateData")) {
            return new UpdateDataWizardNotification();
        } else {
            return null;
        }

    }

    private List<String> parseReceivers(JSONObject obj) {
        JSONArray arr = obj.get("receivers").isArray();
        List<String> l = new ArrayList<String>();
        if (arr != null) {
            for (int i = 0; i < arr.size(); i++) {
                l.add(JsonUtil.trim(arr.get(i).toString()));
            }
        }

        return l;
    }

}
