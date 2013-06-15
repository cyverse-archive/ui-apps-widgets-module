package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentSelectionField;
import org.iplantc.core.uiapps.widgets.client.view.fields.treeSelector.SelectionItemTreePanel;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;

import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.ui.client.adapters.HasTextEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;

class ArgumentSelectionEditor extends Composite implements CompositeEditor<Argument, Argument, ArgumentSelectionField>, ValueChangeHandler<List<SelectionItem>> {

    private CompositeEditor.EditorChain<Argument, ArgumentSelectionField> chain;

    private final FieldLabel propertyLabel;
    HasTextEditor label = null;
    private ArgumentSelectionField subEditor = null;


    private final AppTemplateWizardPresenter presenter;

    /**
     * Copy of backing object from last set/update, used to determine if default values should be updated
     */
    private Argument argumentCopy;

    ArgumentSelectionEditor(AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        propertyLabel = new FieldLabel();
        initWidget(propertyLabel);
        propertyLabel.setLabelAlign(LabelAlign.TOP);

        if (presenter.isEditingMode()) {
            label = HasTextEditor.of(propertyLabel);
        }

    }

    @Override
    public void setValue(Argument value) {
        if (value == null) {
            return;
        }
        if (!AppWizardFieldFactory.isSelectionArgumentType(value)) {
            return;
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
            subEditor = AppWizardFieldFactory.createArgumentListField(value, presenter);
            if (subEditor != null) {

                subEditor.setList(value);
                subEditor.setValue(value);

                propertyLabel.setWidget(subEditor);

                if (presenter.isEditingMode()) {
                    subEditor.addValueChangeHandler(this);
                }
                // TODO JDS Determine if any validators are necessary for Selection types

                chain.attach(value, subEditor);
                
                // TODO JDS Determine how "required" field validation must occur.
            }
            
        } else {
            subEditor.setList(value);
            subEditor.setValue(value);
        }
        // Update label
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(value);
        propertyLabel.setHTML(fieldLabelText);

        this.argumentCopy = AppTemplateUtils.copyArgument(value);

    }

    @Override
    public ArgumentSelectionField createEditorForTraversal() {
        return new SelectionItemTreePanel(presenter);
    }

    /**
     * Adds a click handler to this class' field label.
     * 
     * @param clickHandler
     */
    void addArgumentClickHandler(ClickHandler clickHandler) {
        propertyLabel.addDomHandler(clickHandler, ClickEvent.getType());
    }

    @Override
    public void setEditorChain(CompositeEditor.EditorChain<Argument, ArgumentSelectionField> chain) {
        this.chain = chain;
    }

    @Override
    public String getPathElement(ArgumentSelectionField subEditor) {
        return "";
    }

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void onValueChange(ValueChangeEvent<List<SelectionItem>> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

}
