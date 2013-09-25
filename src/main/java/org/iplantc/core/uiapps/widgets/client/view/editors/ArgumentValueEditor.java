package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Collections;
import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentValueField;
import org.iplantc.core.uiapps.widgets.client.view.fields.CheckBoxAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.ui.client.adapters.HasTextEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

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
    private EditorDelegate<Argument> delegate;


    ArgumentValueEditor(final AppTemplateWizardPresenter presenter, final AppMetadataServiceFacade appMetadataService) {
        this.presenter = presenter;
        this.appMetadataService = appMetadataService;
        propertyLabel = new FieldLabel();
        initWidget(propertyLabel);
        propertyLabel.setLabelAlign(LabelAlign.TOP);
        
        if (presenter.isEditingMode()) {
            label = HasTextEditor.of(propertyLabel);
        }
        new QuickTip(propertyLabel);
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

            subEditor.setValue(model.getValue());
            HasEnabled w = (HasEnabled)subEditor.asWidget();
            if (presenter.isOnlyLabelEditMode()) {
                w.setEnabled(false);
            }
            if (presenter.isEditingMode() && AppTemplateUtils.isReferenceType(model.getType())) {
                w.setEnabled(false);
            } else {
                w.setEnabled(value.isVisible());
            }

            if (!presenter.isEditingMode()) {
                setVisible(value.isVisible());
            }


        } else if (!model.getType().equals(ArgumentType.Info)) {
            HasEnabled w = (HasEnabled)subEditor.asWidget();
            if (presenter.isOnlyLabelEditMode()) {
                w.setEnabled(false);
            }
            if (presenter.isEditingMode() && AppTemplateUtils.isReferenceType(model.getType())) {
                w.setEnabled(false);
            } else {
                w.setEnabled(value.isVisible());
            }

            subEditor.setValue(model.getValue());
        }

        // Update label
        SafeHtml fieldLabelText = presenter.getAppearance().createArgumentLabel(model);
        if ((subEditor != null) && subEditor.asWidget() instanceof CheckBoxAdapter) {
            propertyLabel.setHTML("");
            propertyLabel.setLabelSeparator("");
            ((CheckBoxAdapter)subEditor.asWidget()).setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(fieldLabelText).toSafeHtml());
        } else if(model.getType().equals(ArgumentType.Info)){
        	propertyLabel.setHTML(fieldLabelText);
        	propertyLabel.setLabelSeparator("");
            final String id = Strings.nullToEmpty(model.getId());
            if(id.equals(AppTemplateUtils.EMPTY_GROUP_ARG_ID)){
        		propertyLabel.getElement().getStyle().setMarginTop(100, Unit.PX);
        		propertyLabel.getElement().getStyle().setMarginBottom(100, Unit.PX);
        	}
        }else {
        	propertyLabel.setHTML(fieldLabelText);
        }

    }

    @Override
    public void flush() {
        if ((subEditor == null) || (presenter.isEditingMode() && (presenter.getValueChangeEventSource() != this))) {
            return;
        }

        // TODO JDS Investigate getting rid of ArgumentValueField definition.
        ((ConverterFieldAdapter<?, ?>)subEditor).flush();
        Splittable split = subEditor.getValue();
        // JDS Transfer errors to delegate
        for (EditorError err : subEditor.getErrors()) {
            delegate.recordError(err.getMessage(), err.getValue(), err.getEditor());
        }

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
    public void setDelegate(EditorDelegate<Argument> delegate) {
        this.delegate = delegate;
    }

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

    boolean hasErrors() {
        if (subEditor == null) {
            return false;
        }
        subEditor.validate(false);
        return (subEditor.getErrors() != null) && !subEditor.getErrors().isEmpty();
    }

    List<EditorError> getErrors() {
        if ((subEditor == null) || (subEditor.getErrors() == null)) {
            return Collections.emptyList();
        }

        return subEditor.getErrors();
    }

}
