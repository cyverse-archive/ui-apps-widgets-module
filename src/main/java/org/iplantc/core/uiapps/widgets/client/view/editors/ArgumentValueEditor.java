package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentValueField;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToStringConverter;

import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.ui.client.adapters.HasTextEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * This <code>Editor</code> is responsible for dynamically binding <code>Splittable</code> sub-editors
 * based on the {@link Argument#getType()}.
 * 
 * It is necessary to do this within the scope of a <code>ValueAwareEditor</code> since GWT needs class
 * literal definitions at compile time.
 * 
 * @author jstroot
 * 
 */
class ArgumentValueEditor extends Composite implements CompositeEditor<Argument, Splittable, ArgumentValueField>, ValueChangeHandler<Splittable> {

    private CompositeEditor.EditorChain<Splittable, ArgumentValueField> chain;

    private final FieldLabel propertyLabel;
    HasTextEditor label = null;
    private ArgumentValueField subEditor = null;

    /**
     * The live, bound backing object
     */
    private Argument argument;
    
    /**
     * Copy of backing object from last set/update, used to determine if default values should be updated
     */
    private Argument argumentCopy;

    private ClickHandler clickHandler;
    private final AppTemplateWizardPresenter presenter;

    ArgumentValueEditor(final AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        propertyLabel = new FieldLabel();
        initWidget(propertyLabel);
        propertyLabel.setLabelAlign(LabelAlign.TOP);
        
        if (presenter.isEditingMode()) {
            label = HasTextEditor.of(propertyLabel);
        }
    }

    /**
     * Adds a click handler to this class' field label.
     * @param clickHandler
     */
    void addArgumentClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        propertyLabel.addDomHandler(clickHandler, ClickEvent.getType());
        // If the subEditor's field is a CheckBox, add the click handler to it, since it won't have a
        // field label.
        if ((subEditor != null) && (subEditor.getField() instanceof CheckBox)) {
            subEditor.getField().asWidget().addDomHandler(clickHandler, ClickEvent.getType());
        }
    }

    @Ignore
    public ArgumentValueField getArgumentField() {
        return subEditor;
    }

    @Override
    public void setValue(Argument value) {
        if (value == null) {
            return;
        }
        /*
         * JDS If the argument type is ANY kind of selection type (i.e. deals with lists or trees) then
         * return and do nothing.
         */
        if (AppWizardFieldFactory.isSelectionArgumentType(value)) {
            return;
        }
        if (this.argument != value) {
            this.argument = value;
        }

        // Perform editing mode actions.
        if (presenter.isEditingMode() && (argumentCopy != null)) {
            
            /*
             * Determine if default value has changed, for coordinating defaultValue changes while in
             * editing mode.
             */
            Splittable defaultValue = value.getDefaultValue();
            if (argumentCopy.getDefaultValue() != null) {
                if (!argumentCopy.getDefaultValue().getPayload().equals(defaultValue.getPayload())) {
                    // Default value has changed, update value with default value
                    value.setValue(defaultValue);
                }
            }
        }
        
        if (subEditor == null) {
            AppWizardFieldFactory.setDefaultValue(value);
            subEditor = AppWizardFieldFactory.createArgumentValueField(value, presenter.isEditingMode());
            if (subEditor != null) {
                propertyLabel.setWidget(subEditor);

                if (presenter.isEditingMode()) {
                    if (subEditor.getField() instanceof HasValueChangeHandlers) {
                        subEditor.addValueChangeHandler(this);
                    }
                }
                // Apply any validators which may have been set at init-time
                Widget o = subEditor.asWidget();
                if (o instanceof CheckBox) {
                    if (clickHandler != null) {
                        o.addDomHandler(clickHandler, ClickEvent.getType());
                    }
                }
                // attach it to the chain. Attach the formvalue
                Splittable formValue = value.getValue();
                chain.attach(formValue, subEditor);
            }
            AppWizardFieldFactory.setRequiredValidator(value, subEditor);
        }



        if (subEditor != null && !value.getType().equals(ArgumentType.Info)) {
            subEditor.setValue(value.getValue());
        }

        // Update label
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(value);
        if ((subEditor != null) && (subEditor.asWidget() instanceof CheckBox)) {
            propertyLabel.setHTML("");
            propertyLabel.setLabelSeparator("");
            ((CheckBox)subEditor.asWidget()).setBoxLabel(fieldLabelText.asString());
        } else {
            propertyLabel.setHTML(fieldLabelText);
        }
        
        this.argumentCopy = AppTemplateUtils.copyArgument(value);

    }

    @Override
    public void flush() {
        Splittable split = chain.getValue(subEditor);
        // Manually put the value into the argument
        if (split != null) {
            // Need to set the actual backing copy
            argument.setValue(split);

            if (presenter.isEditingMode()) {
                argument.setDefaultValue(split);
                argumentCopy = AppTemplateUtils.copyArgument(argument);
            }
        }
    }

    @Override
    public ArgumentValueField createEditorForTraversal() {
        return new ConverterFieldAdapter<String, TextField>(new TextField(), new SplittableToStringConverter());
    }

    @Override
    public void setEditorChain(CompositeEditor.EditorChain<Splittable, ArgumentValueField> chain) {
        this.chain = chain;
    }

    @Override
    public String getPathElement(ArgumentValueField subEditor) {
        return ".value";
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void onValueChange(ValueChangeEvent<Splittable> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

}
