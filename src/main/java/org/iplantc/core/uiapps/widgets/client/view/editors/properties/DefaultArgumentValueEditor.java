package org.iplantc.core.uiapps.widgets.client.view.editors.properties;

import java.util.Collections;
import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentValueField;
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
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.CheckBox;
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

    DefaultArgumentValueEditor(AppTemplateWizardAppearance appearance, final AppMetadataServiceFacade appMetadataService) {
        this.appMetadataService = appMetadataService;
        this.appearance = appearance;
        propertyLabel = new FieldLabel();
        propertyLabel.setLabelAlign(LabelAlign.TOP);
        initWidget(propertyLabel);
        new QuickTip(propertyLabel);
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
                    fieldLabelText = SafeHtmlUtils.fromTrustedString(I18N.DISPLAY.labelDefaultCheckboxLabel());
                    createDefaultValueSubEditor(model);
                    break;

                case EnvironmentVariable:
                    fieldLabelText = appearance.createContextualHelpLabel(I18N.DISPLAY.labelDefaultEnvVarLabel(), I18N.DISPLAY.propertyDefaultNameEnvVar());
                    createDefaultValueSubEditor(model);
                    break;
                case Text:
                case MultiLineText:
                    fieldLabelText = appearance.createContextualHelpLabel(I18N.DISPLAY.labelDefaultTextLabel(), I18N.DISPLAY.propertyDefaultText());
                    createDefaultValueSubEditor(model);
                    break;

                case Double:
                case Integer:
                    fieldLabelText = appearance.createContextualHelpLabel(I18N.DISPLAY.labelDefaultValueLabel(), I18N.DISPLAY.propertyDefaultValueInteger());
                    createDefaultValueSubEditor(model);
                    break;

                case FileOutput:
                    fieldLabelText = SafeHtmlUtils.fromTrustedString(I18N.DISPLAY.labelDefaultFileOutputlabel());
                    createDefaultValueSubEditor(model);
                    break;

                case FolderOutput:
                    fieldLabelText = SafeHtmlUtils.fromTrustedString(I18N.DISPLAY.labelDefaultFolderOutputLabel());
                    createDefaultValueSubEditor(model);
                    break;
                case MultiFileOutput:
                    fieldLabelText = SafeHtmlUtils.fromTrustedString(I18N.DISPLAY.labelDefaultMultiFileOutputLabel());
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
            if ((subEditor != null) && (subEditor.asWidget() instanceof CheckBox)) {
                propertyLabel.setHTML("");
                propertyLabel.setLabelSeparator("");
                ((CheckBox)subEditor.asWidget()).setBoxLabel(fieldLabelText.asString());
            }
            Splittable defaultValue = model.getDefaultValue();
            subEditor.setValue(defaultValue);
        } else {
            Splittable defaultValue = model.getDefaultValue();
            subEditor.setValue(defaultValue);
            subEditor.applyValidators(model.getValidators());
        }
    }

    private void createDefaultValueSubEditor(Argument argument) {
        subEditor = AppWizardFieldFactory.createArgumentValueField(argument, false, appMetadataService);
        if (subEditor != null) {
            // Apply any validators which may have been set at init-time
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
