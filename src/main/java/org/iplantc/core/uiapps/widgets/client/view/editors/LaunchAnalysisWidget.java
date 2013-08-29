package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsDisplayMessages;
import org.iplantc.core.uiapps.widgets.client.models.metadata.JobExecution;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uiapps.widgets.client.view.editors.validation.AnalysisOutputValidator;
import org.iplantc.core.uicommons.client.models.CommonModelUtils;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.validators.DiskResourceNameValidator;
import org.iplantc.core.uicommons.client.widgets.PreventEntryAfterLimitHandler;
import org.iplantc.core.uidiskresource.client.views.widgets.FolderSelectorField;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ConverterEditorAdapter;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;

/**
 * @author jstroot
 * 
 */
public class LaunchAnalysisWidget implements IsWidget, Editor<JobExecution> {

    private static LaunchAnalysisWidgetUiBinder BINDER = GWT.create(LaunchAnalysisWidgetUiBinder.class);

    interface LaunchAnalysisWidgetUiBinder extends UiBinder<Widget, LaunchAnalysisWidget> {}

    interface EditorDriver extends SimpleBeanEditorDriver<JobExecution, LaunchAnalysisWidget> {}

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    interface Style extends CssResource {
        String warning();
    }

    @Ignore
    @UiField
    ContentPanel contentPanel;

    @UiField
    TextField name;

    @UiField
    TextArea description;

    @UiField
    CheckBox retainInputs;

    @Ignore
    @UiField
    FolderSelectorField awFolderSel;

    ConverterEditorAdapter<String, HasId, FolderSelectorField> outputDirectory;
    private final UserSettings userSettings;
    private final AppsWidgetsDisplayMessages displayMessages;

    private AppTemplateWizardAppearance appearance;

    public LaunchAnalysisWidget(UserSettings userSettings, AppsWidgetsDisplayMessages displayMessages, AppTemplateWizardAppearance appearance) {
        this.appearance = appearance;
        this.userSettings = userSettings;
        this.displayMessages = displayMessages;
        BINDER.createAndBindUi(this);
        name.addValidator(new DiskResourceNameValidator());
        name.addKeyDownHandler(new PreventEntryAfterLimitHandler(name));
        name.addValidator(new MaxLengthValidator(PreventEntryAfterLimitHandler.DEFAULT_LIMIT));
        name.setAllowBlank(false);
        description.addKeyDownHandler(new PreventEntryAfterLimitHandler(description));
        description.addValidator(new MaxLengthValidator(PreventEntryAfterLimitHandler.DEFAULT_LIMIT));
        outputDirectory = new ConverterEditorAdapter<String, HasId, FolderSelectorField>(awFolderSel, new Converter<String, HasId>() {
            @Override
            public String convertFieldValue(HasId object) {
                if (object == null) {
                    return null;
                }
                return object.getId();
            }

            @Override
            public HasId convertModelValue(String object) {
                return CommonModelUtils.createHasIdFromString(object);
            }
        });
        awFolderSel.setValidatePermissions(true);
        awFolderSel.addValidator(new AnalysisOutputValidator());
        editorDriver.initialize(this);
    }

    @UiHandler("awFolderSel")
    void onFolderChanged(ValueChangeEvent<HasId> event) {
        if ((event.getValue() != null) && !event.getValue().getId().equals(userSettings.getDefaultOutputFolder())) {
            awFolderSel.setInfoErrorText(displayMessages.nonDefaultFolderWarning());
        } else {
            awFolderSel.setInfoErrorText(null);
        }

        flushJobExecution();
    }

    @UiHandler("name")
    void onNameChange(ValueChangeEvent<String> event) {
        updateHeader(name.getCurrentValue());
    }

    @UiHandler("awFolderSel")
    void onInvalid(InvalidEvent event) {
        updateHeader(name.getCurrentValue());
    }

    public void edit(JobExecution je) {
        editorDriver.edit(je);
    }

    public JobExecution flushJobExecution() {
        JobExecution flush = editorDriver.flush();
        updateHeader(flush.getName());
        return flush;
    }

    public boolean hasErrors() {
        return ((editorDriver.getErrors() != null) && editorDriver.hasErrors()) || !awFolderSel.getErrors().isEmpty();
    }

    public List<EditorError> getErrors() {
        List<EditorError> errors = Lists.newArrayList(editorDriver.getErrors());
        errors.addAll(awFolderSel.getErrors());
        return errors;
    }

    private void updateHeader(String appName) {
        SafeHtmlBuilder headerLabel = new SafeHtmlBuilder();
        if (hasErrors()) {
            headerLabel.appendHtmlConstant(appearance.getErrorIconImg().getString());
        }
        headerLabel.appendEscaped(displayMessages.launchAnalysisDetailsHeadingPrefix() + appName);
        contentPanel.setHeadingHtml(headerLabel.toSafeHtml());
    }

    @Override
    public Widget asWidget() {
        return contentPanel;
    }
}
