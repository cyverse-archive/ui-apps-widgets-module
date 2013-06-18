package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardComboBox;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentSelectionField;
import org.iplantc.core.uiapps.widgets.client.view.fields.treeSelector.SelectionItemTreePanel;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.ui.client.adapters.HasTextEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.HasSplittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;

class ArgumentSelectionEditor extends Composite implements ValueAwareEditor<Argument>, ValueChangeHandler<List<SelectionItem>> {

    private final FieldLabel propertyLabel;
    HasTextEditor label = null;
    private ArgumentSelectionField subEditor = null;

    @Path("")
    AppWizardComboBox simpleSelectionEditor;

    @Path("")
    SelectionItemTreePanel treeSelectionEditor;

    private final AppTemplateWizardPresenter presenter;

    /**
     * Copy of backing object from last set/update, used to determine if default values should be updated
     */
    private Argument argumentCopy;

    ArgumentSelectionEditor(AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        propertyLabel = new FieldLabel();
        simpleSelectionEditor = new AppWizardComboBox(presenter);
        treeSelectionEditor = new SelectionItemTreePanel(presenter);
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

        if (presenter.isEditingMode()) {

            if (argumentCopy == null) {
                this.argumentCopy = AppTemplateUtils.copyArgument(value);
            }

            if (value.getSelectionItems() != null && argumentCopy.getDefaultValue() != null && !argumentCopy.getDefaultValue().getPayload().equals(value.getDefaultValue().getPayload())) {
                for (SelectionItem si : value.getSelectionItems()) {
                    if (si.isDefault()) {
                        Splittable split = ((HasSplittable)AutoBeanUtils.getAutoBean(si)).getSplittable();
                        value.setValue(split);
                        value.setDefaultValue(split);
                    }
                }
            }

        }

        if (subEditor == null) {
            if (value.getDefaultValue() != null) {
                value.setValue(value.getDefaultValue());
            }
            switch (value.getType()) {
                case TextSelection:
                case ValueSelection:
                case Selection:
                case IntegerSelection:
                case DoubleSelection:
                    subEditor = simpleSelectionEditor;
                    treeSelectionEditor.nullifyEditors();
                    treeSelectionEditor = null;
                    propertyLabel.setWidget(subEditor);
                    if (presenter.isEditingMode()) {
                        subEditor.addValueChangeHandler(this);
                    }
                    break;
                case TreeSelection:
                    subEditor = treeSelectionEditor;
                    simpleSelectionEditor.nullifyEditors();
                    simpleSelectionEditor = null;
                    propertyLabel.setWidget(subEditor);
                    if (presenter.isEditingMode()) {
                        subEditor.addValueChangeHandler(this);
                    }
                default:
                    break;
            }
            if (subEditor != null) {

                // TODO JDS Determine if any validators are necessary for Selection types

                // TODO JDS Determine how "required" field validation must occur.
            }
            
        }
        // Update label
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(value);
        propertyLabel.setHTML(fieldLabelText);

        this.argumentCopy = AppTemplateUtils.copyArgument(value);

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
