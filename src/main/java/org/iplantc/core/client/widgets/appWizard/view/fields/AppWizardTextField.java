package org.iplantc.core.client.widgets.appWizard.view.fields;


import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * XXX Needs to be a bounded text field
 * @author jstroot
 * 
 */
public class AppWizardTextField extends Composite implements TemplatePropertyEditorBase, LeafValueEditor<Splittable> {
    
    private final TextField field = new TextField();
    
    public AppWizardTextField(){
        field.addKeyDownHandler(new KeyDownHandler() {
            
            @Override
            public void onKeyDown(KeyDownEvent event) {
                // TODO Auto-generated method stub
                
            }
        });
        field.addKeyPressHandler(new KeyPressHandler() {
            
            @Override
            public void onKeyPress(KeyPressEvent event) {
                // TODO Auto-generated method stub
                
            }
        });
        field.addDomHandler(new KeyPressHandler() {
            
            @Override
            public void onKeyPress(KeyPressEvent event) {
                // TODO Auto-generated method stub
                
            }
        }, KeyPressEvent.getType());
        
        /*
         * Here's the idea.
         * In order to make sure that the user cannot enter further characters if they've reached a limit (field is 'bounded'),
         * we will listen for some kind of key press event. All of these events are derived from DomEvents. So, if we listen for
         * a key event and detect that we already have too many characters, we will prevent the event by calling
         * DomEvent.preventDefault() or DomEvent.stopPropogation(), or both.
         * It would be nice if we could listen for a change event, and prevent it if it is not valid.
         * We will have to experiment with this.
         */
            
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
}
