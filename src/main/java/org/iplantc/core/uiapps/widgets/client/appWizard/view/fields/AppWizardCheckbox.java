package org.iplantc.core.uiapps.widgets.client.appWizard.view.fields;

import org.iplantc.core.uiapps.widgets.client.appWizard.models.Argument;
import org.iplantc.core.uiapps.widgets.client.appWizard.view.fields.converters.SplittableToBooleanConverter;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * A LeafValueEditor for Arguments of type ArgumentType.FLAG
 * 
 * This is the only one which will be a {@link LeafValueEditor} since we don't need the editor framework
 * to descend
 * any lower (because the Argument value is a string, and we have to manually convert the value
 * to/from
 * Boolean).
 * 
 * @author jstroot
 * 
 */
public class AppWizardCheckbox implements ArgumentField, LeafValueEditor<Splittable> {
    
    private final CheckBox field;
    private final Converter<Splittable, Boolean> cvt;
    
    public AppWizardCheckbox(){
        field = new CheckBox();
        cvt = new SplittableToBooleanConverter();
    }

    @Override
    public void initialize(Argument property) {/* Do Nothing */}

    @Override
    public void setValue(Splittable value) {
        field.setValue(cvt.convertModelValue(value));
    }

    @Override
    public Splittable getValue() {
        return cvt.convertFieldValue(field.getValue());
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return field.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return field.addValidHandler(handler);
    }

    public void setBoxLabel(String boxLabel) {
        field.setBoxLabel(boxLabel);
    }

    @Override
    public Widget asWidget() {
        return field;
    }

    @Override
    public void setToolTip(ToolTipConfig toolTip) {
        field.setToolTipConfig(toolTip);
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

}
