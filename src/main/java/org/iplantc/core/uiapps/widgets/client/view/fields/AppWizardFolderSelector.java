package org.iplantc.core.uiapps.widgets.client.view.fields;

import org.iplantc.core.uicommons.client.models.CommonModelUtils;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.dialogs.FolderSelectDialog;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.TakesValue;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class AppWizardFolderSelector extends AppWizardDiskResourceSelector<Folder> {

    UserSettings userSettings = UserSettings.getInstance();

    @Override
    protected void onBrowseSelected() {
        HasId value = getValue();
        FolderSelectDialog folderSD = null;
        if (value != null) {
            folderSD = new FolderSelectDialog(value);
        } else {
            if (userSettings.isRememberLastPath()) {
                String id = userSettings.getLastPathId();
                if (id != null) {
                    folderSD = new FolderSelectDialog(CommonModelUtils.createHasIdFromString(id));
                } else {
                    folderSD = new FolderSelectDialog(null);
                }
            } else {
                folderSD = new FolderSelectDialog(null);
            }
        }
        folderSD.addHideHandler(new FolderDialogHideHandler(folderSD));
        folderSD.show();
    }

    @Override
    public void setValue(HasId value) {
        super.setValue(value);
    }

    private class FolderDialogHideHandler implements HideHandler {
        private final TakesValue<Folder> takesValue;

        public FolderDialogHideHandler(TakesValue<Folder> dlg) {
            this.takesValue = dlg;
        }

        @Override
        public void onHide(HideEvent event) {
            Folder value = takesValue.getValue();
            if (value == null)
                return;

            setSelectedResource(value);
            // cache the last used path
            if (userSettings.isRememberLastPath()) {
                userSettings.setLastPathId(value.getId());
            }
            ValueChangeEvent.fire(AppWizardFolderSelector.this, value);
        }
    }
}
