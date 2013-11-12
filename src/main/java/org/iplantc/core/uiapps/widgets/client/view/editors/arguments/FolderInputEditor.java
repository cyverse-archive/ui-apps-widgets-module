package org.iplantc.core.uiapps.widgets.client.view.editors.arguments;

import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.SplittableToHasIdConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceSelector.HasDisableBrowseButtons;
import org.iplantc.core.uidiskresource.client.views.widgets.FolderSelectorField;

public class FolderInputEditor extends AbstractArgumentEditor implements HasDisableBrowseButtons {
    private final ArgumentEditorConverter<HasId> editorAdapter;
    private final FolderSelectorField folderSelector;

    public FolderInputEditor(AppTemplateWizardAppearance appearance) {
        super(appearance);
        folderSelector = new FolderSelectorField();
        editorAdapter = new ArgumentEditorConverter<HasId>(folderSelector, new SplittableToHasIdConverter());

        argumentLabel.setWidget(editorAdapter);
    }

    @Override
    public void disableBrowseButtons() {
        folderSelector.disableBrowseButtons();
    }

    @Override
    public ArgumentEditorConverter<?> valueEditor() {
        return editorAdapter;
    }

}
