package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.editors.dnd.ContainerDropTarget;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

/**
 * @author jstroot
 * 
 */
class ArgumentListEditor extends Composite implements IsEditor<ListEditor<Argument, ArgumentEditor>> {

    private final class ArgListEditorDropTarget extends ContainerDropTarget<VerticalLayoutContainer> {

        private ArgListEditorDropTarget(VerticalLayoutContainer container) {
            super(container);
        }

        @Override
        protected boolean verifyDragData(Object dragData) {
            // Only accept drag data which is an Argument
            return (dragData instanceof Argument) && super.verifyDragData(dragData);
        }

        @Override
        protected void onDragDrop(DndDropEvent event) {
            super.onDragDrop(event);
            List<Argument> list = editor.getList();
            Argument newArg = AppTemplateUtils.copyArgument((Argument)event.getData());
            if (list != null) {
                list.add(insertIndex, newArg);
                setFireSelectedOnAdd(true);
            }
        }
    }

    private class PropertyListEditorSource extends EditorSource<ArgumentEditor> {
        private static final int DEF_ARGUMENT_MARGIN = 10;
        private final VerticalLayoutContainer con;
        private final EventBus eventBus;
        private final AppTemplateWizardPresenter presenter;

        public PropertyListEditorSource(final VerticalLayoutContainer con, final EventBus eventBus, final AppTemplateWizardPresenter presenter) {
            this.con = con;
            this.eventBus = eventBus;
            this.presenter = presenter;
        }

        @Override
        public ArgumentEditor create(int index) {
            final ArgumentEditor subEditor = new ArgumentEditor(eventBus, presenter);
            con.insert(subEditor, index, new VerticalLayoutData(1, -1, new Margins(DEF_ARGUMENT_MARGIN)));
            if (isFireSelectedOnAdd()) {
                setFireSelectedOnAdd(false);
                eventBus.fireEvent(new ArgumentSelectedEvent(subEditor));
            }
            return subEditor;
        }
        
        @Override
        public void dispose(ArgumentEditor subEditor) {
            subEditor.removeFromParent();
        }
        
        @Override
        public void setIndex(ArgumentEditor editor, int index) {
            con.insert(editor, index, new VerticalLayoutData(1, -1, new Margins(DEF_ARGUMENT_MARGIN)));
        }
    }

    private final VerticalLayoutContainer argumentsContainer;

    private final ListEditor<Argument, ArgumentEditor> editor;

    private boolean fireSelectedOnAdd;

    public ArgumentListEditor(final EventBus eventBus, final AppTemplateWizardPresenter presenter) {
        argumentsContainer = new VerticalLayoutContainer();
        initWidget(argumentsContainer);
        argumentsContainer.setAdjustForScroll(true);
        argumentsContainer.setScrollMode(ScrollMode.AUTOY);
        editor = ListEditor.of(new PropertyListEditorSource(argumentsContainer, eventBus, presenter));

        if (presenter.isEditingMode()) {
            // If in editing mode, add drop target and DnD handlers
            ContainerDropTarget<VerticalLayoutContainer> dt = new ArgListEditorDropTarget(argumentsContainer);
            dt.setFeedback(Feedback.BOTH);
        }
    }

    @Override
    public ListEditor<Argument, ArgumentEditor> asEditor() {
        return editor;
    }

    private void setFireSelectedOnAdd(boolean fireSelectedOnAdd) {
        this.fireSelectedOnAdd = fireSelectedOnAdd;
    }

    private boolean isFireSelectedOnAdd() {
        return fireSelectedOnAdd;
    }
}
