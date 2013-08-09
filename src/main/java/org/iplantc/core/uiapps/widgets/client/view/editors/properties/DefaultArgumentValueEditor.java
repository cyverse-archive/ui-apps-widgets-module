package org.iplantc.core.uiapps.widgets.client.view.editors.properties;

import java.util.Collections;
import java.util.List;

import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentValueField;
import org.iplantc.core.uiapps.widgets.client.view.fields.CheckBoxAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToStringConverter;

import com.google.common.collect.Lists;
import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

/**
 * FIXME JDS This class is no longer part of the AppTemplateWizard editor driver chain. (It is not bound). Evaluate necessity of implemented interfaces. 
 *           The primary goal in doing so is to make sure that this class' intent is clear and unambiguous. 
 *           
 * @author jstroot
 *
 */
class DefaultArgumentValueEditor extends Composite implements CompositeEditor<Argument, Splittable, ArgumentValueField>, HasValueChangeHandlers<Splittable> {

    private CompositeEditor.EditorChain<Splittable, ArgumentValueField> chain;
    private final FieldLabel propertyLabel;
    private ConverterFieldAdapter<Splittable, ?> subEditor = null;
    private final List<ValueChangeHandler<Splittable>> valueChangeHandlers = Lists.newArrayList();

    /**
     * The live, bound backing object
     */
    private Argument model;
    private final AppMetadataServiceFacade appMetadataService;
    private final AppTemplateWizardAppearance appearance;
    private final AppsWidgetsPropertyPanelLabels labels;
    private final AppsWidgetsContextualHelpMessages help;

    DefaultArgumentValueEditor(AppTemplateWizardAppearance appearance, final AppMetadataServiceFacade appMetadataService) {
        this.appMetadataService = appMetadataService;
        this.appearance = appearance;
        this.labels = appearance.getPropertyPanelLabels();
        this.help = appearance.getContextHelpMessages();
        propertyLabel = new FieldLabel();
        propertyLabel.setLabelAlign(LabelAlign.TOP);
        initWidget(propertyLabel);
        QuickTip quickTip = new QuickTip(propertyLabel);
        quickTip.getToolTipConfig().setDismissDelay(0);
    }

    @Override
    public void setValue(Argument value) {
        if (value == null)
            return;

        this.model = value;

        if (subEditor == null) {
            SafeHtml fieldLabelText = SafeHtmlUtils.fromTrustedString("");
            switch (model.getType()) {
                case Flag:
                    // Change FieldLabel text for the Flag
                    fieldLabelText = SafeHtmlUtils.fromTrustedString(labels.checkboxDefaultLabel());
                    createDefaultValueSubEditor(model);
                    break;

                case EnvironmentVariable:
                    fieldLabelText = appearance.createContextualHelpLabel(labels.envVarDefaultLabel(), help.envVarDefaultName());
                    createDefaultValueSubEditor(model);
                    break;
                case Text:
                case MultiLineText:
                    fieldLabelText = appearance.createContextualHelpLabel(labels.textInputDefaultLabel(), help.textInputDefaultText());
                    createDefaultValueSubEditor(model);
                    break;

                case Double:
                case Integer:
                    fieldLabelText = appearance.createContextualHelpLabel(labels.integerInputDefaultLabel(), help.integerInputDefaultValue());
                    createDefaultValueSubEditor(model);
                    break;

                case FileOutput:
                    fieldLabelText = SafeHtmlUtils.fromTrustedString(labels.fileOutputDefaultLabel());
                    createDefaultValueSubEditor(model);
                    break;

                case FolderOutput:
                    fieldLabelText = SafeHtmlUtils.fromTrustedString(labels.folderOutputDefaultLabel());
                    createDefaultValueSubEditor(model);
                    break;
                case MultiFileOutput:
                    fieldLabelText = SafeHtmlUtils.fromTrustedString(labels.multiFileOutputLabel());
                    createDefaultValueSubEditor(model);
                    break;

                case FileInput:
                case FolderInput:
                case MultiFileSelector:
                case DoubleSelection:
                case Info:
                case IntegerSelection:
                case TreeSelection:
                case Selection:
                case TextSelection:
                case ValueSelection:
                case ReferenceAnnotation:
                case ReferenceGenome:
                case ReferenceSequence:
                    // These types currently do not support default values
                    propertyLabel.setEnabled(false);
                    propertyLabel.setVisible(false);
                    break;

                default:
                    break;
            }

            // Set the FieldLabel text
            propertyLabel.setHTML(fieldLabelText);
            if (subEditor != null) {
                if (subEditor.asWidget() instanceof CheckBoxAdapter) {
                    propertyLabel.setHTML("");
                    propertyLabel.setLabelSeparator("");
                    ((CheckBoxAdapter)subEditor.asWidget()).setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(fieldLabelText).toSafeHtml());

                }
                subEditor.setValue(model.getDefaultValue());

            }
        } else {
            subEditor.setValue(model.getDefaultValue());
            subEditor.applyValidators(model.getValidators());
        }
    }

    private void createDefaultValueSubEditor(Argument argument) {
        subEditor = AppWizardFieldFactory.createDefaultArgumentValueField(argument, appMetadataService);
        if (subEditor != null) {
            // Apply any handlers which may have been set at init-time
            if ((subEditor.getField() instanceof HasValueChangeHandlers) && !valueChangeHandlers.isEmpty()) {
                for (ValueChangeHandler<Splittable> valueChangeHandler : valueChangeHandlers) {
                    subEditor.addValueChangeHandler(valueChangeHandler);
                }
            }

            propertyLabel.setWidget(subEditor);
            // attach it to the chain. Attach the formvalue
            Splittable defaultValue = argument.getDefaultValue();
            if (chain != null) {
                chain.attach(defaultValue, subEditor);
            }
        }
    }

    @Override
    public void flush() {
        if (subEditor == null) {
            return;
        }
        subEditor.flush();
        Splittable split = subEditor.getValue();

        if (split != null) {
            model.setDefaultValue(split);
        }
    }

    @Override
    public ArgumentValueField createEditorForTraversal() {
        TextField field = new TextField();
        return new ConverterFieldAdapter<String, TextField>(field, new SplittableToStringConverter());
    }

    @Override
    public String getPathElement(ArgumentValueField subEditor) {
        return ".defaultValue";
    }

    @Override
    public void setEditorChain(CompositeEditor.EditorChain<Splittable, ArgumentValueField> chain) {
        this.chain = chain;
    }

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Splittable> handler) {
        if (subEditor == null) {
            // If subeditor is null, save handler for application on edit
            valueChangeHandlers.add(handler);
        } else if (subEditor instanceof HasValueChangeHandlers) {
            return subEditor.addValueChangeHandler(handler);
        }
        return null;
    }

    public List<EditorError> getErrors() {
        if (subEditor == null)
            return Collections.emptyList();

        return subEditor.getErrors();
    }

}
