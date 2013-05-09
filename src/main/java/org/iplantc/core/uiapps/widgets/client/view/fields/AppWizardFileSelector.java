package org.iplantc.core.uiapps.widgets.client.view.fields;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.views.dialogs.FileSelectDialog;

import com.google.common.collect.Lists;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class AppWizardFileSelector extends AppWizardDiskResourceSelector<File> {

    @Override
    protected void onBrowseSelected() {
        List<HasId> selected = null;

        HasId value = getValue();
        if (value != null) {
            selected = Lists.newArrayList();
            selected.add(value);
        }

        FileSelectDialog fileSD = FileSelectDialog.singleSelect(selected);
        fileSD.addHideHandler(new FileDialogHideHandler(fileSD));
        fileSD.show();
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

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<HasId> handler) {
        // TODO Auto-generated method stub
        return null;
    }

}
