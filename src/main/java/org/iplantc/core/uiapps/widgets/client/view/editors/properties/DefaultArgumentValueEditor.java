package org.iplantc.core.uiapps.widgets.client.view.editors.properties;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentField;
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

class DefaultArgumentValueEditor extends Composite implements CompositeEditor<Argument, Splittable, ArgumentField>, HasValueChangeHandlers<Splittable> {

    private CompositeEditor.EditorChain<Splittable, ArgumentField> chain;
    private final FieldLabel propertyLabel;
    private ConverterFieldAdapter<Splittable, ?> subEditor = null;
    private final List<ValueChangeHandler<Splittable>> valueChangeHandlers = Lists.newArrayList();

    /**
     * The live, bound backing object
     */
    private Argument argument;

    /**
     * Copy of backing object from last set/update
     */
    private Argument argumentCopy;

    DefaultArgumentValueEditor() {
        propertyLabel = new FieldLabel();
        propertyLabel.setLabelAlign(LabelAlign.TOP);
        initWidget(propertyLabel);
    }

    @Override
    public void setValue(Argument value) {
        if (value == null)
            return;

        if (this.argument != value) {
            this.argument = value;
        }

        if (subEditor == null) {
            SafeHtml fieldLabelText = SafeHtmlUtils.fromTrustedString("Default Value");
            switch (value.getType()) {
                case FileInput:
                case FolderInput:
                case MultiFileSelector:
                    createDefaultValueSubEditor(value);
                    break;

                case Selection:
                case TextSelection:
                case ValueSelection:
                    createDefaultValueSubEditor(value);
                    break;

                case Flag:
                    // Change FieldLabel text for the Flag
                    fieldLabelText = SafeHtmlUtils.fromTrustedString("Default State");
                    createDefaultValueSubEditor(value);
                    break;

                case Text:
                case EnvironmentVariable:
                    createDefaultValueSubEditor(value);
                    break;

                case Number:
                case Double:
                case Integer:
                    createDefaultValueSubEditor(value);
                    break;

                case DoubleSelection:
                case Info:
                case IntegerSelection:
                case MultiLineText:
                case Output:
                case TreeSelection:
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
        Splittable defaultValue = value.getDefaultValue();
        if ((subEditor != null) && (defaultValue != null)) {
            subEditor.setValue(defaultValue);
        }
        this.argumentCopy = AppTemplateUtils.copyArgument(value);
    }

    private void createDefaultValueSubEditor(Argument argument) {
        subEditor = AppWizardFieldFactory.createArgumentField(argument, false);
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
        Splittable split = chain.getValue(subEditor);
        // Manually put the default value into the argument
        if (split != null) {
            Splittable defaultValue = argument.getDefaultValue();
            if (defaultValue == null) {
                argument.setDefaultValue(split);
            } else {

                boolean defaultValueChanged = !split.getPayload().equals(defaultValue.getPayload());

                Splittable argCpyValue = argumentCopy.getValue();
                Splittable argValue = argument.getValue();

                /*
                 * Determine if the value has been updated since last flush. If true, this
                 * indicates that the Argument value/ has been updated on a previous flush in
                 * the editor hierarchy.
                 */
                boolean argValNull = argValue == null;
                boolean argCpyValNull = argCpyValue == null;
                boolean valueUpdated = (!(argValNull && argCpyValNull) && (argValNull ^ argCpyValNull)) 
                        || (!(argValNull && argCpyValNull) && !argCpyValue.getPayload().equals(argValue.getPayload()));

                /*
                 * Determine if the defaultValue has been updated since last flush. If true, this
                 * indicates that the Argument defaultValue has been updated on a previous flush in
                 * the editor hierarchy.
                 */
                Splittable defaultValue2 = argumentCopy.getDefaultValue();
                boolean defaultValNull = defaultValue == null;
                boolean defaultCopyValNull = defaultValue2 == null;
                boolean defaultValueUpdated = (!(defaultValNull && defaultCopyValNull) && (defaultValNull ^ defaultCopyValNull))
                        || (!(defaultValNull && defaultCopyValNull) && !defaultValue.getPayload().equals(defaultValue2.getPayload()));
                
                /* Only update if:
                 * -- the flushed value differs from the current value, and 
                 * -- the argument value has not been updated by a higher node in this editor hierarchy, and
                 * -- the argument default value has not been updated by a higher node in this editor hierarchy. 
                 */
                if (defaultValueChanged && !valueUpdated && !defaultValueUpdated) {

                    // Need to set the actual backing object
                    argument.setDefaultValue(split);
                    argumentCopy = AppTemplateUtils.copyArgument(argument);
                }
            }
        }
    }

    @Override
    public ArgumentField createEditorForTraversal() {
        TextField field = new TextField();
        field.setText("DEFAULT VALUE");
        return new ConverterFieldAdapter<String, TextField>(field, new SplittableToStringConverter());
    }

    @Override
    public String getPathElement(ArgumentField subEditor) {
        return ".defaultValue";
    }

    @Override
    public void setEditorChain(CompositeEditor.EditorChain<Splittable, ArgumentField> chain) {
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
