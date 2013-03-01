package org.iplantc.core.uiapps.widgets.client.view.fields;

import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.ConverterEditorAdapter;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * 
 * @author jstroot
 *
 * @param <T>
 * @param <U>
 * @param <F>
 */
public class ConverterFieldAdapter<U, F extends Component & IsField<U>> extends ConverterEditorAdapter<Splittable, U, F> implements ArgumentField, HasKeyDownHandlers {

    protected final F field;
    private Splittable value;

    public ConverterFieldAdapter(F field, Converter<Splittable, U> converter) {
        super(field, converter);
        this.field = field;
    }

    @Override
    public void flush() {
        value = getConverter().convertFieldValue(field.getValue());
    }

    public F getField() {
        return field;
    }

    @Override
    public Widget asWidget() {
        return field.asWidget();
    }

    @Override
    public void setValue(Splittable value) {
        field.setValue(getConverter().convertModelValue(value));
    }

    @Override
    public Splittable getValue() {
        return value;
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
        return field.isValid(preventMark);
    }

    @Override
    public boolean validate(boolean preventMark) {
        return field.validate(preventMark);
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

    @Override
    public void fireEvent(GwtEvent<?> event) {
        field.fireEvent(event);
    }

    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return field.addDomHandler(handler, KeyDownEvent.getType());
    }

}
