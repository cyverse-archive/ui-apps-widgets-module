package org.iplantc.core.uiapps.widgets.client.view.editors.properties;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentValueField;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToStringConverter;

import com.google.common.collect.Lists;
import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
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

class DefaultArgumentValueEditor extends Composite implements CompositeEditor<Argument, Splittable, ArgumentValueField>, HasValueChangeHandlers<Splittable> {

    private CompositeEditor.EditorChain<Splittable, ArgumentValueField> chain;
    private final FieldLabel propertyLabel;
    private ConverterFieldAdapter<Splittable, ?> subEditor = null;
    private final List<ValueChangeHandler<Splittable>> valueChangeHandlers = Lists.newArrayList();

    /**
     * The live, bound backing object
     */
    private Argument model;

    DefaultArgumentValueEditor(AppTemplateWizardPresenter presenter) {
        propertyLabel = new FieldLabel();
        propertyLabel.setLabelAlign(LabelAlign.TOP);
        initWidget(propertyLabel);
    }

    @Override
    public void setValue(Argument value) {
        if (value == null)
            return;

        if (this.model != value) {
            this.model = value;
        }

        if (subEditor == null) {
            SafeHtml fieldLabelText = SafeHtmlUtils.fromTrustedString("Default Value");
            switch (model.getType()) {
                case Flag:
                    // Change FieldLabel text for the Flag
                    fieldLabelText = SafeHtmlUtils.fromTrustedString("Default State");
                    createDefaultValueSubEditor(model);
                    break;

                case Text:
                case EnvironmentVariable:
                case MultiLineText:
                    createDefaultValueSubEditor(model);
                    break;

                case Number:
                case Double:
                case Integer:
                    createDefaultValueSubEditor(model);
                    break;

                case FileInput:
                case FolderInput:
                case MultiFileSelector:
                case DoubleSelection:
                case Info:
                case IntegerSelection:
                case Output:
                case TreeSelection:
                case Selection:
                case TextSelection:
                case ValueSelection:
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
        }
        Splittable defaultValue = model.getDefaultValue();
        if ((subEditor != null) && (defaultValue != null)) {
            subEditor.setValue(defaultValue);
        }
    }

    private void createDefaultValueSubEditor(Argument argument) {
        subEditor = AppWizardFieldFactory.createArgumentValueField(argument, false);
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
            chain.attach(defaultValue, subEditor);
        }
    }

    @Override
    public void flush() {
        if (chain == null)
            return;

        Splittable split = chain.getValue(subEditor);
        // if (split != null && presenter.getValueChangeEventSource() == subEditor) {
        if (split != null) {
            model.setDefaultValue(split);
            // argumentCopy = AppTemplateUtils.copyArgument(argument);
        }
    }

    @Override
    public ArgumentValueField createEditorForTraversal() {
        TextField field = new TextField();
        field.setText("DEFAULT VALUE");
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

}
