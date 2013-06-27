package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.editors.dnd.ContainerDropTarget;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * @author jstroot
 * 
 */
class ArgumentListEditor extends Composite implements IsEditor<ListEditor<Argument, ArgumentEditor>> {

    /**
     * A handler which controls the visibility, placement, and selection of a button over the children of
     * a vertical layout container.
     * 
     * @author jstroot
     * 
     */
    private final class ArgumentWYSIWYGDeleteHandler implements MouseOverHandler, MouseOutHandler, SelectHandler {
        int currentItemIndex = -1;
        private final AppTemplateWizardPresenter presenter;
        private final ListEditor<Argument, ArgumentEditor> listEditor;
        private final VerticalLayoutContainer layoutContainer;
        private final IconButton button;

        public ArgumentWYSIWYGDeleteHandler(ListEditor<Argument, ArgumentEditor> listEditor, VerticalLayoutContainer layoutContainer, IconButton button, AppTemplateWizardPresenter presenter) {
            this.listEditor = listEditor;
            this.layoutContainer = layoutContainer;
            this.button = button;
            this.presenter = presenter;
        }

        @Override
        public void onMouseOver(MouseOverEvent event) {
            int childCount = layoutContainer.getElement().getChildCount();
            for (int j = 0; j < childCount; j++) {
                Element child = layoutContainer.getElement().getChild(j).cast();
                Element target = Element.as(event.getNativeEvent().getEventTarget());
                if (child.isOrHasChild(target) && (target != button.getElement())) {
                    // Determine if button needs to be placed,
                    // if so, then place it.
                    currentItemIndex = j;
                    button.setEnabled(true);
                    button.setVisible(true);
                    button.setPagePosition(child.getAbsoluteRight() - button.getOffsetWidth(), child.getAbsoluteTop());

                    break;
                }
            }
        }

        @Override
        public void onMouseOut(MouseOutEvent event) {
            // event target gives us the thing we are leaving.

            EventTarget relatedTarget = event.getNativeEvent().getRelatedEventTarget();
            if (relatedTarget == null || Element.as(relatedTarget) == button.getElement()) {
                return;
            }
            currentItemIndex = -1;
            button.disable();
            button.setVisible(false);
        }

        @Override
        public void onSelect(SelectEvent event) {
            if (currentItemIndex >= 0) {
                listEditor.getList().remove(currentItemIndex);
                presenter.onArgumentPropertyValueChange();

                /*
                 * JDS Fire ArgumentSelectedEvent with null parameter. This is to inform handlers that
                 * the selection should be cleared, or the previous argument selected, if possible.
                 */
                if (listEditor.getList().size() > 0) {
                    int index = (currentItemIndex > 0) ? currentItemIndex - 1 : 0;
                    ArgumentEditor toBeSelected = listEditor.getEditors().get(index);
                    presenter.asWidget().fireEvent(new ArgumentSelectedEvent(toBeSelected.getPropertyEditor()));
                } else {
                    presenter.asWidget().fireEvent(new ArgumentSelectedEvent(null));
                }
            }
        }
    }

    /**
     * A drop target class which handles the addition of new arguments to this editor's ListEditor.
     * 
     * @author jstroot
     * 
     */
    private final class ArgListEditorDropTarget extends ContainerDropTarget<VerticalLayoutContainer> {

        private final AppTemplateWizardPresenter presenter;
        private final ListEditor<Argument, ArgumentEditor> listEditor;
        private int argCountInt = 1;

        private ArgListEditorDropTarget(VerticalLayoutContainer container, AppTemplateWizardPresenter presenter, ListEditor<Argument, ArgumentEditor> editor) {
            super(container);
            this.presenter = presenter;
            this.listEditor = editor;
        }

        @Override
        protected boolean verifyDragData(Object dragData) {
            // Only accept drag data which is an Argument
            return (dragData instanceof Argument) && super.verifyDragData(dragData);
        }

        @Override
        protected void onDragDrop(DndDropEvent event) {
            super.onDragDrop(event);
            List<Argument> list = listEditor.getList();
            Argument newArg = AppTemplateUtils.copyArgument((Argument)event.getData());

            // Update new argument label.
            String label = newArg.getLabel() + " - " + argCountInt++;
            newArg.setLabel(label);

            if (list != null) {
                // JDS Protect against OBOB issues (caused by argument delete button, which is actually a
                // child of the argumentsContainer
                setFireSelectedOnAdd(true);
                if (insertIndex >= list.size()) {
                    list.add(newArg);
                } else {
                    list.add(insertIndex, newArg);
                }
                presenter.onArgumentPropertyValueChange();
            }
        }
    }

    private class PropertyListEditorSource extends EditorSource<ArgumentEditor> {
        private static final int DEF_ARGUMENT_MARGIN = 10;
        private final VerticalLayoutContainer con;
        private final AppTemplateWizardPresenter presenter;
        private final UUIDServiceAsync uuidService;

        public PropertyListEditorSource(final VerticalLayoutContainer con, final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService) {
            this.con = con;
            this.presenter = presenter;
            this.uuidService = uuidService;
        }

        @Override
        public ArgumentEditor create(int index) {
            final ArgumentEditor subEditor = new ArgumentEditor(presenter, uuidService);
            con.insert(subEditor, index, new VerticalLayoutData(1, -1, new Margins(DEF_ARGUMENT_MARGIN)));
            if (isFireSelectedOnAdd()) {
                setFireSelectedOnAdd(false);
                presenter.asWidget().fireEvent(new ArgumentSelectedEvent(subEditor.getPropertyEditor()));
            }
            con.forceLayout();
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
    private IconButton argDeleteBtn;

    public ArgumentListEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService) {
        argumentsContainer = new VerticalLayoutContainer();
        initWidget(argumentsContainer);
        argumentsContainer.setAdjustForScroll(true);
        argumentsContainer.setScrollMode(ScrollMode.AUTOY);

        editor = ListEditor.of(new PropertyListEditorSource(argumentsContainer, presenter, uuidService));

        if (presenter.isEditingMode()) {
            // If in editing mode, add drop target and DnD handlers
            ContainerDropTarget<VerticalLayoutContainer> dt = new ArgListEditorDropTarget(argumentsContainer, presenter, editor);
            dt.setFeedback(Feedback.BOTH);

            // JDS Create delete button and add it to argumentsContainer
            IplantResources res = GWT.create(IplantResources.class);
            res.argumentListEditorCss().ensureInjected();
            argDeleteBtn = new IconButton(new IconConfig(res.argumentListEditorCss().delete(), res.argumentListEditorCss().deleteHover()));
            argDeleteBtn.disable();
            argDeleteBtn.setVisible(false);
            argumentsContainer.add(argDeleteBtn);

            // JDS Create the argument delete handler, and add it to the argumentsContainer and the
            // argument delete button.
            ArgumentWYSIWYGDeleteHandler deleteButtonHandler = new ArgumentWYSIWYGDeleteHandler(editor, argumentsContainer, argDeleteBtn, presenter);
            argumentsContainer.addDomHandler(deleteButtonHandler, MouseOverEvent.getType());
            argumentsContainer.addDomHandler(deleteButtonHandler, MouseOutEvent.getType());
            argDeleteBtn.addSelectHandler(deleteButtonHandler);

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
