package org.iplantc.core.uiapps.widgets.client.view.fields;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.views.dialogs.FolderSelectDialog;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class AppWizardFolderSelector extends AppWizardDiskResourceSelector<Folder> {

    @Override
    protected void onBrowseSelected() {
        FolderSelectDialog folderSD = new FolderSelectDialog();
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
            if (takesValue.getValue() == null)
                return;

            setSelectedResource(takesValue.getValue());
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<HasId> handler) {
        // TODO Auto-generated method stub
        return null;
    }
}
