package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;

/**
 * An editor class for displaying the argument group list in an {@link AppTemplate#getArgumentGroups()}.
 * 
 * @author jstroot
 * 
 */
class ArgumentGroupListEditor extends Composite implements IsEditor<ListEditor<ArgumentGroup, ArgumentGroupEditor>> {

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
        private final AppTemplateWizardPresenter presenter;

        public ArgumentGroupEditorSource(AccordionLayoutContainer con, EventBus eventBus, AppTemplateWizardPresenter presenter) {
            this.con = con;
            this.eventBus = eventBus;
            this.presenter = presenter;
        }

        @Override
        public ArgumentGroupEditor create(int index) {
            ArgumentGroupEditor subEditor = new ArgumentGroupEditor(eventBus, presenter);
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

    ArgumentGroupListEditor(final EventBus eventBus, AppTemplateWizardPresenter presenter) {
        groupsContainer = new AccordionLayoutContainer();
        initWidget(groupsContainer);
        editor = ListEditor.of(new ArgumentGroupEditorSource(groupsContainer, eventBus, presenter));
    }

    @Override
    public ListEditor<ArgumentGroup, ArgumentGroupEditor> asEditor() {
        return editor;
    }

}
