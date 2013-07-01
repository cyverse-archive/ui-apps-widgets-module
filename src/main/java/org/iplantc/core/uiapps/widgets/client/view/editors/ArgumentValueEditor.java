package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentValueField;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

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
class ArgumentValueEditor extends Composite implements ValueAwareEditor<Argument>, ValueChangeHandler<Splittable>, HasClickHandlers {

    private final FieldLabel propertyLabel;
    HasTextEditor label = null;
    private ArgumentValueField subEditor = null;

    private Argument model;
    
    private ClickHandler clickHandler;
    private final AppTemplateWizardPresenter presenter;
    private final AppMetadataServiceFacade appMetadataService;

    ArgumentValueEditor(final AppTemplateWizardPresenter presenter, final AppMetadataServiceFacade appMetadataService) {
        this.presenter = presenter;
        this.appMetadataService = appMetadataService;
        propertyLabel = new FieldLabel();
        initWidget(propertyLabel);
        propertyLabel.setLabelAlign(LabelAlign.TOP);
        
        if (presenter.isEditingMode()) {
            label = HasTextEditor.of(propertyLabel);
        }
    }

    @Override
    public void setValue(Argument value) {
        if ((value == null) || ((value != null) && AppTemplateUtils.isSelectionArgumentType(value.getType()))) {
            // JDS If the value is null, or if it is a selection type, then we must do nothing.
            return;
        }
        this.model = value;

        AppWizardFieldFactory.setDefaultValue(model);
        if ((subEditor == null) && !model.getType().equals(ArgumentType.Info)) {
            subEditor = AppWizardFieldFactory.createArgumentValueField(model, presenter.isEditingMode(), appMetadataService);
            assert subEditor != null : "ArgumentValueEditor subEditor should not be null.";
            propertyLabel.setWidget(subEditor);

            subEditor.addValueChangeHandler(this);

            // Apply any validators which may have been set at init-time
            Widget o = subEditor.asWidget();
            if (o instanceof CheckBox) {
                if (clickHandler != null) {
                    o.addDomHandler(clickHandler, ClickEvent.getType());
                }
            }

            AppWizardFieldFactory.setRequiredValidator(model, subEditor);
            
            subEditor.setValue(model.getValue());
            subEditor.setToolTipConfig(new ToolTipConfig(model.getDescription()));
            ((HasEnabled)subEditor.asWidget()).setEnabled(value.isVisible());
            if (!presenter.isEditingMode()) {
                setVisible(value.isVisible());
            }
        } else if (!model.getType().equals(ArgumentType.Info)) {
            ((HasEnabled)subEditor.asWidget()).setEnabled(value.isVisible());
            subEditor.setValue(model.getValue());
            subEditor.setToolTipConfig(new ToolTipConfig(model.getDescription()));
        }

        // Update label
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(model);
        if ((subEditor != null) && (subEditor.asWidget() instanceof CheckBox)) {
            propertyLabel.setHTML("");
            propertyLabel.setLabelSeparator("");
            ((CheckBox)subEditor.asWidget()).setBoxLabel(fieldLabelText.asString());
        } else {
            propertyLabel.setHTML(fieldLabelText);
        }

    }

    @Override
    public void flush() {
        if (presenter.getValueChangeEventSource() != this) {
            return;
        }
        // TODO JDS Investigate getting rid of ArgumentValueField definition.
        ((ConverterFieldAdapter<?, ?>)subEditor).flush();
        Splittable split = subEditor.getValue();

        // Manually put the value into the argument
        if (split != null) {
            // Need to set the actual backing copy
            model.setValue(split);

            model.setDefaultValue(split);
            if (presenter.isEditingMode()) {
                model.setDefaultValue(split);
            }
        }
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void onValueChange(ValueChangeEvent<Splittable> event) {
        presenter.onArgumentPropertyValueChange(this);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        this.clickHandler = handler;
        HandlerRegistration reg;
        // If the subEditor's field is a CheckBox, add the click handler to it, since it won't have a
        // field label.
        if ((subEditor != null) && (subEditor.getField() instanceof CheckBox)) {
            reg = subEditor.getField().asWidget().addDomHandler(clickHandler, ClickEvent.getType());
        } else {
            reg = propertyLabel.addDomHandler(clickHandler, ClickEvent.getType());
        }
        return reg;
    }

}
