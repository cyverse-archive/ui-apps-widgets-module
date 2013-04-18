package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

/**
 * @author jstroot
 * 
 */
class ArgumentListEditor extends Composite implements IsEditor<ListEditor<Argument, ArgumentEditor>> {

    private final class DnDHandler implements DndDragEnterHandler, DndDropHandler {
        private final AppTemplateAutoBeanFactory factory;
        private ArgumentType type;
        private final ListEditor<Argument, ArgumentEditor> editor;

        public DnDHandler(ListEditor<Argument, ArgumentEditor> editor, AppTemplateAutoBeanFactory factory) {
            this.editor = editor;
            this.factory = factory;
        }

        @Override
        public void onDragEnter(DndDragEnterEvent event) {
            Object data = event.getDragSource().getData();
            // If the drag source data is not what we are expecting, return
            if ((data == null) || !(data instanceof ArgumentType) || data.equals(ArgumentType.Group)) {
                event.getStatusProxy().setEnabled(false);
                event.setCancelled(true);
                return;
            }
            // Set the type being dragged in
            type = (ArgumentType)data;

        }

        @Override
        public void onDrop(DndDropEvent event) {
            // Create a new argument, set its type, and add it to the editor's list.
            Argument argument = factory.argument().as();
            argument.setLabel("DEFAULT");
            argument.setDescription("DEFAULT");
            argument.setType(type);
            List<Argument> list = editor.getList();
            if (list != null) {
                if (adjustSize) {
                    list.add(argument);
                }
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
            con.add(subEditor, new VerticalLayoutData(1, -1, new Margins(DEF_ARGUMENT_MARGIN)));
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
            DropTarget dt = new DropTarget(argumentsContainer);
            AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
            DnDHandler dndHandler = new DnDHandler(editor, factory);
            dt.addDragEnterHandler(dndHandler);
            dt.addDropHandler(dndHandler);
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
