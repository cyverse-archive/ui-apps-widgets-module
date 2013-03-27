package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.editor.client.Editor;
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
public class ArgumentEditor extends Composite implements Editor<Argument>{

    @Path("")
    ArgumentValueEditor argValueEditor;
    
    @Path("")
    ArgumentPropertyEditor argPropBase;

    public ArgumentEditor(final EventBus eventBus, final AppTemplateWizardPresenter presenter){
        argValueEditor = new ArgumentValueEditor(presenter.isEditingMode());
        argPropBase = new ArgumentPropertyEditor(presenter);
        if (presenter.isEditingMode()) {
            argValueEditor.addArgumentClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    eventBus.fireEvent(new ArgumentSelectedEvent(ArgumentEditor.this));
                }
            });
        }
        
        initWidget(argValueEditor);
    }

    /**
     * TODO JDS May want to make this available through the {@link AppTemplateWizard} instead.
     * 
     * @return
     */
    @Ignore
    public ArgumentPropertyEditor getArgumentPropertyEditor() {
        return argPropBase;
    }

}
