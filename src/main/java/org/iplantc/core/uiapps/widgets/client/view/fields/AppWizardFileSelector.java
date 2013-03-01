package org.iplantc.core.uiapps.widgets.client.view.fields;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.views.dialogs.FileSelectDialog;

import com.google.gwt.user.client.TakesValue;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class AppWizardFileSelector extends AppWizardDiskResourceSelector<File> {

    @Override
    protected void onBrowseSelected() {
        FileSelectDialog fileSD = FileSelectDialog.singleSelect();
        fileSD.addHideHandler(new FileDialogHideHandler(fileSD));
        fileSD.show();
    }

    @Override
    public void setValue(HasId value) {
        super.setValue(value);
    }

    private class FileDialogHideHandler implements HideHandler {
        private final TakesValue<List<File>> takesValue;

        public FileDialogHideHandler(TakesValue<List<File>> dlg) {
            this.takesValue = dlg;
        }

        @Override
        public void onHide(HideEvent event) {
            if ((takesValue.getValue() == null) || takesValue.getValue().isEmpty())
                return;

            // This class is single select, so only grab first element
            setSelectedResource(takesValue.getValue().get(0));
        }
    }

}
