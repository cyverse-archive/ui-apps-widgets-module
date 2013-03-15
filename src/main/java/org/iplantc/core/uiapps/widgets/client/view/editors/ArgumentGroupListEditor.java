package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

/**
 * An editor class for displaying the argument group list in an {@link AppTemplate#getArgumentGroups()}.
 * This class implements <code>HasWidgets</code> in order to support manual validation of view via
 * {@link FormPanelHelper#isValid(HasWidgets)}
 * 
 * @author jstroot
 * 
 */
public class ArgumentGroupListEditor extends Composite implements IsEditor<ListEditor<ArgumentGroup, ArgumentGroupEditor>>, HasWidgets {

    /**
     * The EditorSource class for adding indiv. ArgumentGroups to the ui.
     * 
     * Each ArgumentGroupEditor must be added/inserted into the accordion container using
     * asWidget(). The ArgumentGroupEditor.asWidget() method returns a ContentPanel, which
     * is the only widget that can be added to an accordion panel.
     * 
     * @author jstroot
     * 
     */
    private class ArgumentGroupEditorSource extends EditorSource<ArgumentGroupEditor> {

        private final AccordionLayoutContainer con;
        private final EventBus eventBus;

        public ArgumentGroupEditorSource(AccordionLayoutContainer con, EventBus eventBus) {
            this.con = con;
            this.eventBus = eventBus;
        }

        @Override
        public ArgumentGroupEditor create(int index) {
            ArgumentGroupEditor subEditor = new ArgumentGroupEditor(eventBus);
            con.insert(subEditor.asWidget(), index);
            if (index == 0) {
                // Ensure that the first container is expanded automatically
                con.setActiveWidget(subEditor.asWidget());
            }
            return subEditor;
        }
        
        @Override
        public void dispose(ArgumentGroupEditor subEditor){
            subEditor.asWidget().removeFromParent();
        }
        
        @Override
        public void setIndex(ArgumentGroupEditor editor, int index){
            con.insert(editor.asWidget(), index);
        }

    }
    
    private final AccordionLayoutContainer groupsContainer;
    private final ListEditor<ArgumentGroup, ArgumentGroupEditor> editor;

    public ArgumentGroupListEditor(final EventBus eventBus) {
        groupsContainer = new AccordionLayoutContainer();
        initWidget(groupsContainer);
        editor = ListEditor.of(new ArgumentGroupEditorSource(groupsContainer, eventBus));
    }

    @Override
    public ListEditor<ArgumentGroup, ArgumentGroupEditor> asEditor() {
        return editor;
    }

    @Override
    public void add(Widget w) {
        groupsContainer.add(w);
    }

    @Override
    public void clear() {
        groupsContainer.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return groupsContainer.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return groupsContainer.remove(w);
    }
}
