package org.iplantc.core.widgets.client.appWizard.view.fields;

import java.util.List;

import org.iplantc.core.widgets.client.appWizard.models.TemplateProperty;
import org.iplantc.core.widgets.client.appWizard.models.TemplateValidator;
import org.iplantc.core.widgets.client.appWizard.models.TemplateValidatorType;
import org.iplantc.core.widgets.client.appWizard.util.AppWizardFieldFactory;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * @author jstroot
 * 
 */
public class DoubleNumberField extends Composite implements TemplatePropertyField, LeafValueEditor<Splittable> {

    private final NumberField<Double> field = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
    
    public DoubleNumberField() {
        initWidget(field);
    }

    @Override
    public void initialize(TemplateProperty property) {
        // Apply any validators
        List<TemplateValidator> validators = property.getValidators();
        if (validators != null) {
            for (TemplateValidator tv : validators) {
                if (tv.getType().equals(TemplateValidatorType.IntAbove) 
                        || tv.getType().equals(TemplateValidatorType.IntBelow) 
                        || tv.getType().equals(TemplateValidatorType.IntRange)) {
                    field.addValidator(AppWizardFieldFactory.createDoubleValidator(tv));
                }
            }
        }
    }

    @Override
    public void setValue(Splittable value) {
        if (value == null)
            return;

        field.setValue(value.asNumber());
    }

    @Override
    public Splittable getValue() {
        Splittable split = StringQuoter.createSplittable();
        if (field.getValue() != null) {
            split = StringQuoter.create(field.getValue());
        }
        return split;
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return field.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return field.addValidHandler(handler);
    }

    @Override
    public void setToolTip(ToolTipConfig toolTip) {
        field.setToolTipConfig(toolTip);
    }

}
