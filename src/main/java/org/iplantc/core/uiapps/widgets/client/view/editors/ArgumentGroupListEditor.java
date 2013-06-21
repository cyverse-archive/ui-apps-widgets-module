package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;
import java.util.List;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.RequestArgumentGroupDeleteEvent;
import org.iplantc.core.uiapps.widgets.client.events.RequestArgumentGroupDeleteEvent.RequestArgumentGroupDeleteEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.editors.dnd.ContainerDropTarget;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.user.client.ui.IsWidget;
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


        private final AppTemplateWizardPresenter presenter;
        private final ListEditor<ArgumentGroup, ArgumentGroupEditor> listEditor;

        private ArgGrpListEditorDropTarget(AccordionLayoutContainer container, AppTemplateWizardPresenter presenter, ListEditor<ArgumentGroup,ArgumentGroupEditor> editor) {
            super(container);
            this.presenter = presenter;
            this.listEditor = editor;
        }

        @Override
        protected boolean verifyDragData(Object dragData) {
            // Only accept drag data which is an ArgumentGroup
            return (dragData instanceof ArgumentGroup) && super.verifyDragData(dragData);
        }

        @Override
        protected void onDragDrop(DndDropEvent event) {
            super.onDragDrop(event);
            List<ArgumentGroup> list = listEditor.getList();
            ArgumentGroup newArgGrp = AppTemplateUtils.copyArgumentGroup((ArgumentGroup)event.getData());
            if (list != null) {
                setFireSelectedOnAdd(true);
                list.add(insertIndex, newArgGrp);
                presenter.onArgumentPropertyValueChange();
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
        private final AppTemplateWizardPresenter presenter;
        private final UUIDServiceAsync uuidService;

        public ArgumentGroupEditorSource(AccordionLayoutContainer con, AppTemplateWizardPresenter presenter, UUIDServiceAsync uuidService) {
            this.con = con;
            this.presenter = presenter;
            this.uuidService = uuidService;
        }

        @Override
        public ArgumentGroupEditor create(int index) {
            final ArgumentGroupEditor subEditor = new ArgumentGroupEditor(presenter, uuidService);
            ((ContentPanel)subEditor.asWidget()).setCollapsible(true);
            con.insert(subEditor, index);

            if (index == 0) {
                // Ensure that the first container is expanded automatically
                con.setActiveWidget(subEditor.asWidget());
            }

            if (presenter.isEditingMode()) {
                if (isFireSelectedOnAdd()) {
                    presenter.asWidget().fireEvent(new ArgumentGroupSelectedEvent(subEditor.getPropertyEditor()));
                    con.setActiveWidget(subEditor.asWidget());
                    setFireSelectedOnAdd(false);
                }
                subEditor.addRequestArgumentGroupDeleteEventHandler(handler);
            }

            con.forceLayout();
            return subEditor;
        }
        
        @Override
        public void dispose(ArgumentGroupEditor subEditor){
            subEditor.asWidget().removeFromParent();
        }
        
        @Override
        public void setIndex(ArgumentGroupEditor editor, int index){
            // con.insert(editor, index);
        }

    }
    
    private final AccordionLayoutContainer groupsContainer;
    private final ListEditor<ArgumentGroup, ArgumentGroupEditor> editor;
    private boolean fireSelectedOnAdd;
    private RequestArgumentGroupDeleteEventHandler handler;

    ArgumentGroupListEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService) {
        groupsContainer = new AccordionLayoutContainer();
        initWidget(groupsContainer);
        editor = ListEditor.of(new ArgumentGroupEditorSource(groupsContainer, presenter, uuidService));

        if (presenter.isEditingMode()) {
            groupsContainer.setTitleCollapse(false);
            // If in editing mode, add drop target and DnD handlers.
            ContainerDropTarget<AccordionLayoutContainer> dt = new ArgGrpListEditorDropTarget(groupsContainer, presenter, editor);
            dt.setFeedback(Feedback.BOTH);

            handler = new RequestArgumentGroupDeleteEventHandler() {

                @Override
                public void onDeleteRequest(RequestArgumentGroupDeleteEvent event) {

                    final ArgumentGroup argumentGroup = event.getArgumentGroup();
                    if (editor.getList().contains(argumentGroup)) {
                        // FIXME JDS Now check to see if it contains anything
                        if ((argumentGroup.getArguments() != null) && (argumentGroup.getArguments().size() > 0)) {
                            // JDS Prompt user if they are ok with deleting a non-empty group
                            editor.getList().remove(argumentGroup);
                        } else {
                            editor.getList().remove(argumentGroup);
                        }
                    }
                }
            };
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

    void insertFirstInAccordion(IsWidget widget) {
        groupsContainer.insert(widget, 0);
        groupsContainer.setActiveWidget(widget.asWidget());
    }
}
