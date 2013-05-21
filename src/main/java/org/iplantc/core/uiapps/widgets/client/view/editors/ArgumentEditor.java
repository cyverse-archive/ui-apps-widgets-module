package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.ArgumentPropertyEditor;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.Composite;

/**
 * This class has two sub-editors; an <code>ArgumentValueEditor</code> which is the only widget visually
 * supplied by this <code>Composite</code>, and an <code>ArgumentPropertyEditor</code> which is available
 * for retrieval when the presenter has 'editingMode' enabled. It is worth noting that the
 * <code>ArgumentPropertyEditor</code> is only constructed (and therefore, part of the Editor
 * hierarchy) if the presenter has 'editingMode' enabled.
 * 
 * It was necessary to construct the <code>ArgumentPropertyEditor</code> within this class to ensure that
 * all <code>Editor&lt;Argument&gt;</code>s which may be simultaneously editing the same
 * <code>Argument</code> are initialized within the same Editor hierarchy.
 * 
 * @author jstroot
 * 
 */
class ArgumentEditor extends Composite implements AppTemplateWizard.IArgumentEditor, ValueAwareEditor<Argument> {

    @Path("")
    ArgumentValueEditor argValueEditor;
    
    @Path("")
    ArgumentPropertyEditor argPropEditor = null;

    private Argument currValue;

    ArgumentEditor(final EventBus eventBus, final AppTemplateWizardPresenter presenter) {
        argValueEditor = new ArgumentValueEditor(presenter.isEditingMode(), presenter);
        if (presenter.isEditingMode()) {
            argPropEditor = new ArgumentPropertyEditor(presenter);
            argValueEditor.addArgumentClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    eventBus.fireEvent(new ArgumentSelectedEvent(ArgumentEditor.this));
                }
            });
        }
        
        initWidget(argValueEditor);
    }

    @Override
    @Ignore
    public IsWidget getArgumentPropertyEditor() {
        return argPropEditor;
    }

    @Override
    public void setValue(Argument value) {
        this.currValue = value;
    }

    @Override
    public Argument getCurrentArgument() {
        return currValue;
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}
