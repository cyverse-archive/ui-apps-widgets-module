package org.iplantc.core.client.widgets.appWizard.view.fields;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * 
 * XXX Needs to be bounded
 * 
 * @author jstroot
 * 
 */
public class AppWizardTextArea extends Composite implements TemplatePropertyEditorBase, LeafValueEditor<Splittable> {

    private final TextArea field = new TextArea();

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
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
