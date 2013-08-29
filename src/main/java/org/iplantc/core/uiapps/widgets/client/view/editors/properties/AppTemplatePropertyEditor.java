package org.iplantc.core.uiapps.widgets.client.view.editors.properties;

import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardPropertyContentPanelAppearance;
import org.iplantc.core.uicommons.client.validators.DiskResourceNameValidator;
import org.iplantc.core.uicommons.client.widgets.PreventEntryAfterLimitHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

public class AppTemplatePropertyEditor extends Composite implements ValueAwareEditor<AppTemplate> {

    private static AppTemplatePropertyEditorUiBinder BINDER = GWT.create(AppTemplatePropertyEditorUiBinder.class);

    interface AppTemplatePropertyEditorUiBinder extends UiBinder<Widget, AppTemplatePropertyEditor> {}

    @UiField
    ContentPanel cp; 
    
    @UiField
    FieldLabel toolLabel, appNameLabel, appDescriptionLabel;

    @Ignore
    @UiField
    TextField tool;

    @Ignore
    @UiField
    TextButton searchBtn;

    @UiField
    @Path("name")
    TextField name;

    @UiField
    @Path("description")
    TextArea description;

    private final AppTemplateWizardPresenter presenter;

    public AppTemplatePropertyEditor(final AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        initWidget(BINDER.createAndBindUi(this));

        cp.setHeaderVisible(false);
        name.addKeyDownHandler(new PreventEntryAfterLimitHandler(name));
        name.addValidator(new MaxLengthValidator(PreventEntryAfterLimitHandler.DEFAULT_LIMIT));
        name.addValidator(new DiskResourceNameValidator());
        description.addValidator(new MaxLengthValidator(PreventEntryAfterLimitHandler.DEFAULT_LIMIT));
        description.addKeyDownHandler(new PreventEntryAfterLimitHandler(description));

        initLabels();
    }

    private void initLabels() {
        AppTemplateWizardAppearance appearance = presenter.getAppearance();
        AppsWidgetsPropertyPanelLabels labels = appearance.getPropertyPanelLabels();
        String requiredHtml = appearance.getTemplates().fieldLabelRequired().asString();

        String toolHelp = appearance.getContextHelpMessages().appToolUsed();
        SafeHtml toolLabelHtml = appearance.createContextualHelpLabel(labels.toolUsedLabel(), toolHelp);
        toolLabel.setHTML(requiredHtml + toolLabelHtml.asString());
        new QuickTip(toolLabel).getToolTipConfig().setDismissDelay(0);

        appNameLabel.setHTML(requiredHtml + labels.appNameLabel());
        appDescriptionLabel.setHTML(requiredHtml + labels.appDescriptionLabel());
    }

    @UiFactory
    ContentPanel createContentPanel() {
        return new ContentPanel(new AppTemplateWizardPropertyContentPanelAppearance());
    }

    @UiHandler({"name", "description"})
    void onNameChanged(ValueChangeEvent<String> event) {
        presenter.onArgumentPropertyValueChange();
    }

    @UiHandler("searchBtn")
    public void onSearchBtnClick(SelectEvent event) {
        presenter.showToolSearchDialog();
    }

    @Override
    public void setValue(AppTemplate value) {
        if (value == null) {
            return;
        }

        if (value.getDeployedComponent() != null) {
            tool.setValue(value.getDeployedComponent().getName());
        } else {
            tool.clear();
        }
        toolLabel.setEnabled(!presenter.isOnlyLabelEditMode());
        appNameLabel.setEnabled(!presenter.isOnlyLabelEditMode());
        
        cp.setHeadingHtml(presenter.getAppearance().getPropertyPanelLabels().detailsPanelHeader(value.getName()));
    }

    @Override
    public void setDelegate(EditorDelegate<AppTemplate> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}
}
