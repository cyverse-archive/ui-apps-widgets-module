package org.iplantc.core.uiapps.widgets.client.view.editors.properties;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.lists.SelectionItemPropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.trees.SelectionItemTreePropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.validation.ArgumentValidatorEditor;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardComboBox;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

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
    VerticalLayoutContainer con;

    @UiField
    CheckBox requiredEditor, omitIfBlank, visible;

    @UiField
    TextField label;

    @UiField
    TextField description;

    @UiField
    TextField name;

    @Path("")
    @UiField(provided = true)
    DefaultArgumentValueEditor defaultValue;

    @Path("")
    @UiField(provided = true)
    ArgumentValidatorEditor validatorsEditor;

    @Path("")
    @UiField(provided = true)
    AppWizardComboBox selectionItemDefaultValue;

    @UiField
    FieldLabel selectionItemDefaultValueLabel, nameLabel, argLabelLabel, descriptionLabel;

    @Path("")
    @UiField(provided = true)
    SelectionItemPropertyEditor selectionItemListEditor;

    @Path("")
    @UiField(provided = true)
    SelectionItemTreePropertyEditor selectionItemTreeEditor;

    private final AppTemplateWizardPresenter presenter;

    public ArgumentPropertyEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService) {
        this.presenter = presenter;
        defaultValue = new DefaultArgumentValueEditor(presenter);
        validatorsEditor = new ArgumentValidatorEditor(I18N.DISPLAY);
        selectionItemDefaultValue = new AppWizardComboBox(presenter);
        selectionItemListEditor = new SelectionItemPropertyEditor(presenter, uuidService);
        selectionItemTreeEditor = new SelectionItemTreePropertyEditor(presenter);

        /*
         * Validation control and selection creation control will be created here, bound to argument.
         * Each will be a value aware editor, and will only instantiate the visible components for the
         * appropriate Argument type.
         * 
         * You will want to get the selective instantiate logic in place before getting the functionality
         * together.
         */
        initWidget(BINDER.createAndBindUi(this));
    }

    /*
     * JDS The following UI Handlers are necessary to inform the presenter that values have changed.
     * This has the effect of propagating data back and forth through the editor hierarchy.
     */

    @UiHandler({"requiredEditor", "omitIfBlank", "visible"})
    void onBooleanValueChanged(ValueChangeEvent<Boolean> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
        
    }

    @UiHandler({"label", "description", "name"})
    void onStringValueChanged(ValueChangeEvent<String> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

    @UiHandler("defaultValue")
    void onSplittableValueChanged(ValueChangeEvent<Splittable> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

    @UiHandler({"selectionItemTreeEditor", "selectionItemListEditor"})
    void onSelectionItemListChanged(ValueChangeEvent<List<SelectionItem>> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

    @UiHandler("selectionItemDefaultValue")
    void onSelectionItemDefaultValueChanged(ValueChangeEvent<List<SelectionItem>> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void setValue(Argument value) {

        // FIXME JDS Visibility/Enabled of all property editors should be controlled here. I'm looking at you ArgumentValidatorEditor!!
        // JDS Remove any bound components which aren't applicable to the current ArgumentType
        if (!AppTemplateUtils.isSelectionArgumentType(value) && (selectionItemListEditor != null) && (selectionItemTreeEditor != null)) {
            con.remove(selectionItemListEditor);
            con.remove(selectionItemTreeEditor);
            con.remove(selectionItemDefaultValueLabel);
            selectionItemListEditor.nullifyEditors();
            selectionItemTreeEditor.nullifyEditors();
            selectionItemDefaultValueLabel = null;
            selectionItemDefaultValue = null;
            selectionItemListEditor = null;
            selectionItemTreeEditor = null;
        } else if (value.getType().equals(ArgumentType.TreeSelection) && (selectionItemListEditor != null)) {
            con.remove(selectionItemListEditor);
            con.remove(selectionItemDefaultValueLabel);
            selectionItemListEditor.nullifyEditors();
            selectionItemDefaultValueLabel = null;
            selectionItemDefaultValue = null;
            selectionItemListEditor = null;
        } else if (!value.getType().equals(ArgumentType.TreeSelection) && (selectionItemTreeEditor != null)) {
            con.remove(selectionItemTreeEditor);
            selectionItemTreeEditor.nullifyEditors();
            selectionItemTreeEditor = null;
        }

        // JDS Disable any controls which aren't for the current type
        switch (value.getType()) {
            case TextSelection:
            case IntegerSelection:
            case DoubleSelection:
            case Selection:
            case ValueSelection:
            case TreeSelection:
                nameLabel.disable();
                break;

            case Info:
                requiredEditor.setVisible(false);
                omitIfBlank.setVisible(false);
                visible.setVisible(false);
                descriptionLabel.setVisible(false);
                nameLabel.setVisible(false);
                break;

            default:
                break;
        }

        // JDS Change field labels based on Type
        switch (value.getType()) {
            case EnvironmentVariable:
                nameLabel.setText("Environment Variable name");
                break;
            case Info:
                argLabelLabel.setText("Text");

            default:
                break;
        }

    }

}
