package org.iplantc.core.uiapps.widgets.client.view.editors.arguments;

import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.SplittableToHasIdConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceSelector.HasDisableBrowseButtons;
import org.iplantc.core.uidiskresource.client.views.widgets.FileSelectorField;

public class FileInputEditor extends AbstractArgumentEditor implements HasDisableBrowseButtons {
    private final ArgumentEditorConverter<HasId> editorAdapter;
    private final FileSelectorField fileSelector;

    public FileInputEditor(AppTemplateWizardAppearance appearance) {
        super(appearance);
        fileSelector = new FileSelectorField();
        editorAdapter = new ArgumentEditorConverter<HasId>(fileSelector, new SplittableToHasIdConverter());

        argumentLabel.setWidget(editorAdapter);
    }

    @Override
    public void disableBrowseButtons() {
        fileSelector.disableBrowseButtons();
    }

    @Override
    public ArgumentEditorConverter<?> valueEditor() {
        return editorAdapter;
    }

}
