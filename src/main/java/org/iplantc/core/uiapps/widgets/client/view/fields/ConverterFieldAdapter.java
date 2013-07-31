package org.iplantc.core.uiapps.widgets.client.view.fields;

import java.util.Collections;
import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidator;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidatorType;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.PreventEntryAfterLimitHandler;
import org.iplantc.core.uidiskresource.client.views.widgets.AbstractDiskResourceSelector;

import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.ConverterEditorAdapter;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * @author jstroot
 *
 * @param <U>
 * @param <F>
 */
public class ConverterFieldAdapter<U, F extends Component & IsField<U> & ValueAwareEditor<U> & HasValueChangeHandlers<U> & TakesValue<U>> extends ConverterEditorAdapter<Splittable, U, F> implements
        ArgumentValueField, HasKeyDownHandlers {

    protected final F field;
    private Splittable model;
    private final List<EditorError> errors;
    private EditorDelegate<Splittable> delegate;
    private final HandlerManager handlerManager;

    public ConverterFieldAdapter(F field, Converter<Splittable, U> converter) {
        super(field, converter);
        this.field = field;
        errors = Lists.newArrayList();
        handlerManager = new HandlerManager(this);
        this.field.addValueChangeHandler(new ValueChangeHandler<U>() {
            @Override
            public void onValueChange(ValueChangeEvent<U> event) {
                // Transform value and refire.
                ValueChangeEvent.fire(ConverterFieldAdapter.this, getConverter().convertFieldValue(event.getValue()));
            }
        });
    }

    @Override
    public void flush() {
        field.flush();
        model = getConverter().convertFieldValue(field.getValue());
        boolean isValid = validate(false);

        if (!isValid) {
            GWT.log("Not valid");
        }
        if ((errors != null) && (delegate != null)) {
            for (EditorError e : errors) {
                String message = e.getMessage();
                Object value = e.getValue();
                delegate.recordError(message, value, this);
            }
        }
    }

    @Override
    public F getField() {
        return field;
    }

    @Override
    public Widget asWidget() {
        return field.asWidget();
    }

    @Override
    public void setValue(Splittable value) {
        this.model = value;
        field.setValue(getConverter().convertModelValue(model));
    }

    @Override
    public Splittable getValue() {
        return model;
    }

    @Override
    public void clear() {
        field.clear();
    }

    @Override
    public void clearInvalid() {
        field.clearInvalid();
    }

    @Override
    public void reset() {
        field.reset();
    }

    @Override
    public boolean isValid(boolean preventMark) {
        boolean valid = field.validate(preventMark);
        transferErrorsToDelegate();
        return valid;
    }

    @Override
    public boolean validate(boolean preventMark) {
        boolean valid = field.validate(preventMark);
        transferErrorsToDelegate();
        return valid;
    }

    @SuppressWarnings("unchecked")
    private void transferErrorsToDelegate() {
        errors.clear();
        if (field instanceof Field<?>) {
            for (Validator<U> v : ((Field<U>)field).getValidators()) {
                List<EditorError> errs = v.validate(field, field.getValue());
                if (errs != null) {
                    errors.addAll(errs);
                }
            }
        } else if (field instanceof AbstractDiskResourceSelector<?>) {
            errors.addAll(((AbstractDiskResourceSelector<?>)field).getErrors());
        } else {
            return;
        }
    }

    @Override
    public void setToolTipConfig(ToolTipConfig config) {
        field.setToolTipConfig(config);
    }

    @SuppressWarnings("unchecked")
    public void addValidator(Validator<U> validator) {
        if(field instanceof Field<?>){
            ((Field<U>)field).addValidator(validator);
        }
    }

    @SuppressWarnings("unchecked")
    public void removeValidator(Validator<U> validator) {
        if (!(field instanceof Field<?>)) {
            return;
        }

        ((Field<U>)field).removeValidator(validator);
    }

    @SuppressWarnings("unchecked")
    public List<Validator<U>> getValidators() {
        if (!(field instanceof Field<?>)) {
            return Collections.emptyList();
        }

        return ((Field<U>)field).getValidators();
    }

    @Override
    public void applyValidators(List<ArgumentValidator> validators) {
        if (validators == null) {
            return;
        }
        if (field instanceof Field<?>) {
            @SuppressWarnings("unchecked")
            Field<U> fieldObj = (Field<U>)field;

            // JDS Clear all existing validators
            fieldObj.getValidators().clear();

            for (ArgumentValidator av : validators) {
                AutoBean<ArgumentValidator> autoBean = AutoBeanUtils.getAutoBean(av);

                if (av.getType().equals(ArgumentValidatorType.CharacterLimit)) {
                    // Remove previous keyDown handler (if it exists)
                    Object hndlrReg = autoBean.getTag(ArgumentValidator.KEY_DOWN_HANDLER_REG);
                    if ((hndlrReg != null)) {
                        ((HandlerRegistration)hndlrReg).removeHandler();
                    }

                    // Re-apply the keyDown handler (if it exists)
                    Object keyDownHndlr = autoBean.getTag(ArgumentValidator.KEY_DOWN_HANDLER);
                    if (keyDownHndlr != null) {
                        HandlerRegistration newHndlrReg = addKeyDownHandler((KeyDownHandler)keyDownHndlr);
                        autoBean.setTag(ArgumentValidator.KEY_DOWN_HANDLER_REG, newHndlrReg);
                    } else {
                        int maxCharLimit = Double.valueOf(av.getParams().get(0).asNumber()).intValue();
                        @SuppressWarnings("unchecked")
                        TakesValue<String> takesValue = (TakesValue<String>)field;
                        PreventEntryAfterLimitHandler newHandler = new PreventEntryAfterLimitHandler(takesValue, maxCharLimit);
                        HandlerRegistration newHndlrReg = addKeyDownHandler(newHandler);
                        autoBean.setTag(ArgumentValidator.KEY_DOWN_HANDLER_REG, newHndlrReg);
                        autoBean.setTag(ArgumentValidator.KEY_DOWN_HANDLER, newHandler);
                    }

                }

                // Re-apply the actual validator
                Object validator = autoBean.getTag(ArgumentValidator.VALIDATOR);
                if (validator != null) {
                    @SuppressWarnings("unchecked")
                    Validator<U> val = (Validator<U>)validator;
                    addValidator(val);
                }
            }
        }

        validate(false);

    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        // field.fireEvent(event);
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    @Override
    public void setDelegate(EditorDelegate<Splittable> delegate) {
        this.delegate = delegate;
    }

    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return field.addDomHandler(handler, KeyDownEvent.getType());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Splittable> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public List<EditorError> getErrors() {
        return errors;
    }

}
