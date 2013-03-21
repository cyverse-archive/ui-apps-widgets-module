package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;
import java.util.List;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.impl.Refresher;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;

/**
 * A wizard for editing <code>AppTemplate</code>s.
 * 
 * This view operates in one of two modes; editing or not editing.
 * While editing, selection of the {@link Argument} field within the the {@link ArgumentGroup}
 * <code>ContentPanel</code> is enabled. Selecting an Argument field will fire an
 * {@link ArgumentSelectedEvent} through the event bus. Then, the Argument
 * field's corresponding {@link ArgumentPropertyEditor} can be retrieved.
 * 
 * If editing is not enabled, the {@link ArgumentPropertyEditor} will not be available, and will not be
 * initialized with the
 * editor hierarchy.
 * 
 * @author jstroot
 * 
 */
public class AppTemplateWizard extends Composite implements HasWidgets, Editor<AppTemplate>, AppTemplateWizardPresenter {
    
    interface EditorDriver extends SimpleBeanEditorDriver<AppTemplate, AppTemplateWizard> {}
    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
    
    ArgumentGroupListEditor argumentGroups;

    private final boolean editingMode;
    
    public AppTemplateWizard(final EventBus eventBus, boolean editingMode){
        this.editingMode = editingMode;
        argumentGroups = new ArgumentGroupListEditor(eventBus, this);
        initWidget(argumentGroups);
        editorDriver.initialize(this);
    }

    /**
     * This method binds this component with the given <code>AppTemplate</code> instance.
     * 
     * @see {@link EditorDriver#edit(AppTemplate)}
     * @param apptemplate
     */
    public void edit(AppTemplate apptemplate) {
        editorDriver.edit(apptemplate);
    }

    /**
     * This method updates the bound <code>AppTemplate</code> with the current
     * values from the editor.
     * 
     * @see {@link EditorDriver#flush()}
     * @return
     */
    public AppTemplate flush() {
        return editorDriver.flush();
    }

    @Override
    public void onArgumentPropertyValueChange() {
        editorDriver.flush();
        editorDriver.accept(new Refresher());
    }

    @Override
    public void add(Widget w) {
        argumentGroups.add(w);
    }

    @Override
    public void clear() {
        argumentGroups.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return argumentGroups.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return argumentGroups.remove(w);
    }

    public boolean hasErrors() {
        return editorDriver.hasErrors();
    }

    public List<EditorError> getErrors() {
        return editorDriver.getErrors();
    }

    @Override
    public boolean isEditingMode() {
        return editingMode;
    }

}
