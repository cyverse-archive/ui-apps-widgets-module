package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sencha.gxt.widget.core.client.Composite;

/**
 * This class has two sub-editors; an <code>ArgumentValueEditor</code> which is the only widget visually
 * supplied by this <code>Composite</code>,
 * and an <code>ArgumentPropertyEditor</code> which is available for retrieval if necessary.
 * 
 * It was necessary to construct the <code>ArgumentPropertyEditor</code> within this class to ensure that
 * all <code>Editor&lt;Argument&gt;</code>s
 * which may be simultaneously editing the same <code>Argument</code> are initialized within the same
 * Editor hierarchy.
 * 
 * @author jstroot
 * 
 */
public class ArgumentEditor extends Composite implements ValueAwareEditor<Argument>{

    @Path("")
    ArgumentValueEditor argValueEditor;
    
    @Path("")
    ArgumentPropertyEditor argPropBase;

    private Argument argument;
    
    public ArgumentEditor(final EventBus eventBus){
        argValueEditor = new ArgumentValueEditor(eventBus);
        argPropBase = new ArgumentPropertyEditor();
        argValueEditor.addArgumentClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                // FIXME JDS NEED to conceive of a way to prevent this from firing when it is used as an App Wizard
                eventBus.fireEvent(new ArgumentSelectedEvent(ArgumentEditor.this, argument, argValueEditor.getArgumentField()));
            }
        });
        
        initWidget(argValueEditor);
    }

    @Ignore
    public ArgumentPropertyEditor getArgumentPropertyEditor() {
        return argPropBase;
    }

    @Override
    public void setValue(Argument value) {
        this.argument = value;
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}
