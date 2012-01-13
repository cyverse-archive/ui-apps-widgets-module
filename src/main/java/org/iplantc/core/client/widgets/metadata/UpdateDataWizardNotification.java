package org.iplantc.core.client.widgets.metadata;

import org.iplantc.core.client.widgets.utils.ComponentTable;

public class UpdateDataWizardNotification extends WizardNotification {

    @Override
    public void execute(ComponentTable table) {
        for (String panelId : getNotificationReceviers()) {
            if (table.get(panelId) != null) {
                table.get(panelId).refresh();
            }
        }

    }

}
