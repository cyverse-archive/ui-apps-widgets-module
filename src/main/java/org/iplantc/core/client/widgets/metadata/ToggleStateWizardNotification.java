package org.iplantc.core.client.widgets.metadata;

import org.iplantc.core.client.widgets.utils.ComponentTable;

/**
 * A WizardNotification that toggles the state of a widget between enabled and disabled
 * 
 * @author sriram
 * 
 */
public class ToggleStateWizardNotification extends WizardNotification {

    @Override
    public void execute(ComponentTable table) {
        for (String panelId : getNotificationReceviers()) {
            if (table.get(panelId) != null) {
                if (table.get(panelId).isEnabled()) {
                    table.get(panelId).setEnabled(false);
                } else {
                    table.get(panelId).setEnabled(true);
                }
            }
        }
    }

}
