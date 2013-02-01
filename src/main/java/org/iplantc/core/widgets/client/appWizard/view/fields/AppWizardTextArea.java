package org.iplantc.core.widgets.client.appWizard.view.fields;

import java.util.List;

import org.iplantc.core.widgets.client.appWizard.models.Argument;
import org.iplantc.core.widgets.client.appWizard.models.ArgumentValidator;
import org.iplantc.core.widgets.client.appWizard.models.ArgumentValidatorType;
import org.iplantc.core.widgets.client.appWizard.util.AppWizardFieldFactory;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * 
 * XXX Needs to be bounded
 * 
 * @author jstroot
 * 
 */
public class AppWizardTextArea extends Composite implements ArgumentField, LeafValueEditor<Splittable> {

    private final TextArea field = new TextArea();

    public AppWizardTextArea() {
        initWidget(field);
    }

    @Override
    public void initialize(Argument property) {
        field.setAllowBlank(!property.isRequired());
        List<ArgumentValidator> validators = property.getValidators();
        if ((validators != null) && !validators.isEmpty()) {
            for (ArgumentValidator tv : validators) {
                if (tv.getType().equals(ArgumentValidatorType.CharacterLimit)) {
                    // Param should be array of one integer.
                    int limit = Double.valueOf(tv.getParams().get(0).asNumber()).intValue();
                    field.addKeyDownHandler(new PreventEntryAfterLimitHandler(field, limit));
                }
                if (tv.getType().equals(ArgumentValidatorType.CharacterLimit) || tv.getType().equals(ArgumentValidatorType.FileName) || tv.getType().equals(ArgumentValidatorType.Regex)) {
                    field.addValidator(AppWizardFieldFactory.createStringValidator(tv));
                }

            }
        }
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
    public void setValue(Splittable value) {
        field.setValue(value.asString());
    }

    @Override
    public Splittable getValue() {
        return StringQuoter.create(field.getValue());
    }

    @Override
    public void setToolTip(ToolTipConfig toolTip) {
        field.setToolTipConfig(toolTip);
    }
}
