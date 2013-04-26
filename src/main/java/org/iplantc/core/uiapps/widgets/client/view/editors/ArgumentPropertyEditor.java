package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.editors.lists.SelectionListEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.validation.ArgumentValidatorEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * The <code>Editor</code> provides a means for editing the properties of an <code>Argument</code>.
 * 
 * @author jstroot
 * 
 */
public class ArgumentPropertyEditor extends Composite implements Editor<Argument> {

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
    TextField name;

    @Path("")
    @UiField(provided = true)
    DefaultArgumentValueEditor defaultValue;

    @Path("")
    @UiField(provided = true)
    ArgumentValidatorEditor validatorsEditor;

    @Path("")
    @UiField(provided = true)
    SelectionListEditor selectionListEditor;

    private final AppTemplateWizardPresenter presenter;

    ArgumentPropertyEditor(final AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        defaultValue = new DefaultArgumentValueEditor(presenter);
        validatorsEditor = new ArgumentValidatorEditor(I18N.DISPLAY);
        selectionListEditor = new SelectionListEditor();

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

    @UiHandler({"requiredEditor", "omitIfBlank"})
    void onBooleanValueChanged(ValueChangeEvent<Boolean> event) {
        presenter.onArgumentPropertyValueChange();
        
    }

    @UiHandler({"label", "description", "name"})
    void onStringValueChanged(ValueChangeEvent<String> event) {
        presenter.onArgumentPropertyValueChange();
    }

    @UiHandler("defaultValue")
    void onSplittableValueChanged(ValueChangeEvent<Splittable> event) {
        presenter.onArgumentPropertyValueChange();
    }

}
