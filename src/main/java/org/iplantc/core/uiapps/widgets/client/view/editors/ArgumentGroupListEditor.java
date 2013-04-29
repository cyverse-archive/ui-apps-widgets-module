package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;
import java.util.List;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.editors.dnd.ContainerDropTarget;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
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

    private final class ArgGrpListEditorDropTarget extends ContainerDropTarget<AccordionLayoutContainer> {


        private ArgGrpListEditorDropTarget(AccordionLayoutContainer container) {
            super(container);
        }

        @Override
        protected boolean verifyDragData(Object dragData) {
            // Only accept drag data which is an ArgumentGroup
            return (dragData instanceof ArgumentGroup) && super.verifyDragData(dragData);
        }

        @Override
        protected void onDragDrop(DndDropEvent event) {
            super.onDragDrop(event);
            List<ArgumentGroup> list = editor.getList();
            ArgumentGroup newArgGrp = AppTemplateUtils.copyArgumentGroup((ArgumentGroup)event.getData());
            if (list != null) {
                list.add(insertIndex, newArgGrp);
                setFireSelectedOnAdd(true);
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
            final ArgumentGroupEditor subEditor = new ArgumentGroupEditor(eventBus, presenter);
            ((ContentPanel)subEditor.asWidget()).setCollapsible(true);
            con.insert(subEditor.asWidget(), index);
            con.forceLayout();

            if (index == 0) {
                // Ensure that the first container is expanded automatically
                con.setActiveWidget(subEditor.asWidget());
            }
            if (isFireSelectedOnAdd()) {
                setFireSelectedOnAdd(false);
                Scheduler.get().scheduleFinally(new ScheduledCommand() {

                    @Override
                    public void execute() {
                        eventBus.fireEvent(new ArgumentGroupSelectedEvent(subEditor));
                    }
                });
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
    private boolean fireSelectedOnAdd;

    ArgumentGroupListEditor(final EventBus eventBus, AppTemplateWizardPresenter presenter) {
        groupsContainer = new AccordionLayoutContainer();
        groupsContainer.setTitleCollapse(false);
        initWidget(groupsContainer);
        editor = ListEditor.of(new ArgumentGroupEditorSource(groupsContainer, eventBus, presenter));

        if (presenter.isEditingMode()) {
            // If in editing mode, add drop target and DnD handlers.
            ContainerDropTarget<AccordionLayoutContainer> dt = new ArgGrpListEditorDropTarget(groupsContainer);
            dt.setFeedback(Feedback.BOTH);
        }
    }

    @Override
    public ListEditor<ArgumentGroup, ArgumentGroupEditor> asEditor() {
        return editor;
    }

    /**
     * Sets a flag which is used to determine whether an {@link ArgumentGroupSelectedEvent} should be
     * fired.
     */
    private void setFireSelectedOnAdd(boolean fireSelectedOnAdd) {
        this.fireSelectedOnAdd = fireSelectedOnAdd;
    }

    private boolean isFireSelectedOnAdd() {
        return fireSelectedOnAdd;
    }

    public void collapseAllArgumentGroups() {
        // Collapse all AccordionPanelChildren on valid drag enter.
        Iterator<Widget> iterator = groupsContainer.iterator();
        while (iterator.hasNext()) {
            ContentPanel cp = (ContentPanel)iterator.next();
            cp.collapse();
        }
    }
}
