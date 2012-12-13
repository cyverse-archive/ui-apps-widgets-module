package org.iplantc.core.client.widgets.appWizard.view.fields;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * XXX Must
 * 
 * @author jstroot
 * 
 */
public class AppWizardComboBox implements TemplatePropertyEditorBase {
    
    ComboBox<String> field = new ComboBox<String>(null);

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return field.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return field.addValidHandler(handler);
    }

    @Override
    public Widget asWidget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setValue(Splittable value) {
        // TODO Auto-generated method stub

    }

    @Override
    public Splittable getValue() {
        // TODO Auto-generated method stub
        return null;
    }

}
