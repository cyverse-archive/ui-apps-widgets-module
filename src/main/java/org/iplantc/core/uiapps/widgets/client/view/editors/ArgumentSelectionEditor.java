package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentSelectionField;
import org.iplantc.core.uiapps.widgets.client.view.fields.treeSelector.SelectionItemTreePanel;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;

import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.ui.client.adapters.HasTextEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;

class ArgumentSelectionEditor extends Composite implements CompositeEditor<Argument, Argument, ArgumentSelectionField> {

    private CompositeEditor.EditorChain<Argument, ArgumentSelectionField> chain;

    private final FieldLabel propertyLabel;
    HasTextEditor label = null;
    private ArgumentSelectionField subEditor = null;

    private final boolean editingMode;

    ArgumentSelectionEditor(boolean editingMode) {
        this.editingMode = editingMode;
        propertyLabel = new FieldLabel();
        initWidget(propertyLabel);
        propertyLabel.setLabelAlign(LabelAlign.TOP);

        if (editingMode) {
            label = HasTextEditor.of(propertyLabel);
            // Temporary handlers to visually show mouseovers
            propertyLabel.addDomHandler(new MouseOverHandler() {
                @Override
                public void onMouseOver(MouseOverEvent event) {
                    propertyLabel.setBorders(true);

                }
            }, MouseOverEvent.getType());

            propertyLabel.addDomHandler(new MouseOutHandler() {

                @Override
                public void onMouseOut(MouseOutEvent event) {
                    propertyLabel.setBorders(false);
                }
            }, MouseOutEvent.getType());
        }

    }

    @Override
    public void setValue(Argument value) {
        if (value == null) {
            return;
        }

        if (subEditor == null) {
            AppWizardFieldFactory.setDefaultValue(value);
            subEditor = AppWizardFieldFactory.createArgumentListField(value, editingMode);
            if (subEditor != null) {
                // if (value.getType().equals(ArgumentType.TreeSelection)) {

                propertyLabel.setWidget(subEditor);

                if (editingMode) {
                    // TODO JDS Determine if value change handlers are necessary.
                }
                // TODO JDS Determine if any validators are necessary for Selection types

                chain.attach(value, subEditor);
                
                // TODO JDS Determine how "required" field validation must occur.
            }
            
        } else {
            subEditor.setValue(value);
        }
        // Update label
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(value);
        propertyLabel.setHTML(fieldLabelText);

    }

    @Override
    public ArgumentSelectionField createEditorForTraversal() {
        return new SelectionItemTreePanel();
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

}
