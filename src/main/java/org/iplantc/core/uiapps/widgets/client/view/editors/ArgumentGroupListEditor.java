package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;

/**
 * An editor class for displaying the argument group list in an {@link AppTemplate#getArgumentGroups()}.
 * 
 * @author jstroot
 * 
 */
class ArgumentGroupListEditor extends Composite implements IsEditor<ListEditor<ArgumentGroup, ArgumentGroupEditor>> {

    private final class DnDHandler implements DndDragEnterHandler, DndDropHandler {
        private final AppTemplateAutoBeanFactory factory;
        private final ListEditor<ArgumentGroup, ArgumentGroupEditor> editor;

        public DnDHandler(ListEditor<ArgumentGroup, ArgumentGroupEditor> editor, AppTemplateAutoBeanFactory factory) {
            this.editor = editor;
            this.factory = factory;
        }

        @Override
        public void onDragEnter(DndDragEnterEvent event) {
            Object data = event.getDragSource().getData();
            // If the drag source data is not what we are expecting, return
            if ((data == null) || !data.equals(ArgumentType.Group)) {
                event.getStatusProxy().setEnabled(false);
                event.setCancelled(true);
                return;
            }

        }

        @Override
        public void onDrop(DndDropEvent event) {
            ArgumentGroup e = factory.argumentGroup().as();
            e.setArguments(Lists.<Argument> newArrayList());
            e.setLabel("DEFAULT");
            // If we are dropping, we need to add a new group.
            List<ArgumentGroup> list = editor.getList();
            if (list != null) {
                list.add(e);
            }
        }
    }

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
            ((ContentPanel)subEditor.asWidget()).setCollapsible(true);
            /* 
             * JDS Surround this call in a try-catch to guard against failed assertions in devmode.
             * The assertion occurs in AccordionLayoutContainer.onInsert() --> ContentPanel.setCollapsible()
             * The following bug was filed in GXT's forums:
             * http://www.sencha.com/forum/showthread.php?261470-Adding-new-ContentPanel-to-AccordionLayoutContainer-at-runtime-issue 
             */
            try {
                con.insert(subEditor.asWidget(), index);
            } catch (Exception e) {
                GWT.log("Known error condition caught: " + e.getStackTrace());
            }
            con.forceLayout();
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

        // Add drop target and DnD handlers.
        DropTarget dt = new DropTarget(groupsContainer);
        AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        DnDHandler dndHandler = new DnDHandler(editor, factory);
        dt.addDragEnterHandler(dndHandler);
        dt.addDropHandler(dndHandler);
    }

    @Override
    public ListEditor<ArgumentGroup, ArgumentGroupEditor> asEditor() {
        return editor;
    }

}
