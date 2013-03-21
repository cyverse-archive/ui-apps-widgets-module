package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Set;

import org.iplantc.core.uiapps.widgets.client.models.Argument;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;

/**
 * The <code>Editor</code> provides a means for editing the properties of an <code>Argument</code>.
 * 
 * @author jstroot
 * 
 */
public class ArgumentPropertyEditor extends Composite implements ValueAwareEditor<Argument> {

    interface ArgumentPropertyBaseUiBinder extends UiBinder<Widget, ArgumentPropertyEditor> {}
    private static ArgumentPropertyBaseUiBinder BINDER = GWT.create(ArgumentPropertyBaseUiBinder.class);

    @UiField
    CheckBox requiredEditor;

    @UiField
    CheckBox omitIfBlank;

    @UiField
    TextField label;

    @UiField
    TextField description;

    @UiField
    TextField cmdLineOption;

    @UiField
    SpinnerField<Integer> order;

    
    // ---- Transitive field labels
    @UiField
    FieldLabel fileTypeField;

    @UiField
    FieldLabel listSelectionField;

    @UiField
    FieldLabel flagField;

    private final Set<FieldLabel> transitiveFields;

    private final AppTemplateWizardPresenter presenter;

    public ArgumentPropertyEditor(final AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        initWidget(BINDER.createAndBindUi(this));
        transitiveFields = Sets.<FieldLabel> newHashSet(fileTypeField, listSelectionField, flagField);
    }

    private void setTransitiveFieldsEnabled(boolean enabled) {
        for (FieldLabel fl : transitiveFields) {
            fl.setEnabled(enabled);
        }
    }

    private void setTransitiveFieldsVisible(boolean visible) {
        for (FieldLabel fl : transitiveFields) {
            fl.setVisible(visible);
        }
    }

    @Ignore
    @UiFactory
    SpinnerField<Integer> createSpinnerField() {
        SpinnerField<Integer> spinnerField = new SpinnerField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
        spinnerField.setMinValue(0);
        return spinnerField;
    }

    /**
     * This handler is necessary for ensuring that {@link ValueBaseField#getCurrentValue()} is available
     * via the field's overridden {@link LeafValueEditor#getValue()} method. This is important for
     * relaying changes made in one part of the Editor hierarchy to other parts of the Editor hierarchy
     * (thus mimicking an "Observable" style behaviour.
     * 
     * This method then calls the {@link AppTemplateWizardPresenter#onArgumentPropertyValueChange()}
     * method, which initiates an {@link EditorDriver#flush()} and refresh. The act of flushing the
     * Editor hierarchy results in all {@link Field} derived classes being validated.
     * 
     * @param event
     */
    @UiHandler({"requiredEditor", "omitIfBlank", "label", "description", "cmdLineOption", "order"})
    void onChange(ChangeEvent event) {
        presenter.onArgumentPropertyValueChange();
    }

    /**
     * This handler is necessary to ensure that values updated within {@link ValueBaseField}-derived
     * fields are made available through the field's overridden {@link LeafValueEditor#getValue()}
     * method.
     * The call to {@link Field#finishEditing()} results in the value contained in
     * {@link ValueBaseField#getCurrentValue()} is available in {@link LeafValueEditor#getValue()}. This
     * call should only be forced if the field is currently being edited.
     * 
     * @param event
     */
    @UiHandler({"requiredEditor", "omitIfBlank", "label", "description", "cmdLineOption", "order"})
    void onValid(final ValidEvent event) {
        if (event.getSource().isEditing()) {
            event.getSource().finishEditing();
        }
    }

    @Override
    public void setValue(Argument value) {
        if (value == null) {
            GWT.log("Passed Null value!!");
            return;
        }
        // Disable all transitive fields
        setTransitiveFieldsEnabled(false);
        // Set all transitive fields invisible
        setTransitiveFieldsVisible(false);
        switch (value.getType()) {
            case FileInput:
            case FolderInput:
            case MultiFileSelector:
                fileTypeField.setEnabled(true);
                fileTypeField.setVisible(true);
                break;

            case Selection:
            case TextSelection:
            case ValueSelection:
                listSelectionField.setEnabled(true);
                listSelectionField.setVisible(true);
                break;

            case Flag:
                flagField.setEnabled(true);
                flagField.setVisible(true);
                break;

            case Double:
            case DoubleSelection:
            case EnvironmentVariable:
            case Info:
            case Integer:
            case IntegerSelection:
            case MultiLineText:
            case Number:
            case Output:
            case TreeSelection:
            case Text:
                break;

            default:
                break;
        }
    }

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}
