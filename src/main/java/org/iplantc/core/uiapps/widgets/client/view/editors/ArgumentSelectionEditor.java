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
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;

class ArgumentSelectionEditor extends Composite implements ValueAwareEditor<Argument>, ValueChangeHandler<List<SelectionItem>>, HasClickHandlers {

    private final FieldLabel propertyLabel;
    HasTextEditor label = null;
    private ArgumentSelectionField subEditor = null;

    private final AppTemplateWizardPresenter presenter;

    private Argument model;

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
        if ((value == null) || ((value != null) && !AppTemplateUtils.isSelectionArgumentType(value.getType()))) {
            // JDS If the value is null, or IS NOT a selection type, we must return.
            return;
        }
        this.model = value;

        // JDS Set default value to value
        if ((model.getDefaultValue() != null) && model.getDefaultValue().isKeyed()) {
            model.setValue(model.getDefaultValue());
        } else if (model.getSelectionItems() != null) {
            for (SelectionItem si : model.getSelectionItems()) {
                if (si.isDefault()) {
                    Splittable split = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(si));
                    model.setValue(split);
                    model.setDefaultValue(split);
                }
            }
        }

        if (subEditor == null) {
            if (value.getDefaultValue() != null) {
                value.setValue(value.getDefaultValue());
            }
            // JDS Then we must initialize
            switch (model.getType()) {
                case TextSelection:
                case ValueSelection:
                case Selection:
                case IntegerSelection:
                case DoubleSelection:
                    subEditor = new AppWizardComboBox(presenter);
                    break;
                case TreeSelection:
                    subEditor = new SelectionItemTreePanel(presenter);
                default:

                    break;
            }
            assert subEditor != null : "ArgumentSelectionEditor subEditor should not be null.";
            propertyLabel.setWidget(subEditor);
            subEditor.addValueChangeHandler(this);

            // TODO JDS Determine if any validators are necessary for Selection types

            // TODO JDS Determine how "required" field validation must occur.
            subEditor.setValue(model);
            HasEnabled w = ((HasEnabled)subEditor.asWidget());
            if (presenter.isOnlyLabelEditMode()) {
                w.setEnabled(false);
            } else {
                w.setEnabled(value.isVisible());
            }
            // TODO JDS Determine if Tool Tips should/can be applied.
        } else {
            // TODO JDS Determine if Tool Tips should/can be applied.
            subEditor.setValue(model);
            HasEnabled w = ((HasEnabled)subEditor.asWidget());
            if (presenter.isOnlyLabelEditMode()) {
                w.setEnabled(false);
            } else {
                w.setEnabled(value.isVisible());
            }
        }
        // Update label
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(model);
        propertyLabel.setHTML(fieldLabelText);
    }

    @Override
    public void flush() {
        if (presenter.getValueChangeEventSource() != this) {
            return;
        }
        subEditor.flush();
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void onValueChange(ValueChangeEvent<List<SelectionItem>> event) {
        presenter.onArgumentPropertyValueChange(this);
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
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return propertyLabel.addDomHandler(handler, ClickEvent.getType());
    }

}
