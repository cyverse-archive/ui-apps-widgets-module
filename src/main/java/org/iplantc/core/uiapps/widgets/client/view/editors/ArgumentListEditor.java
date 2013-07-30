package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.editors.dnd.ContainerDropTarget;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragCancelEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * 
 * <h1>Editing Mode Drag and Drop</h1>
 * 
 * FIXME JDS [DESCRIBE EDITOR MODE DRAG AND DROP RESPONSIBILITIES OF THIS CLASS]
 * 
 * @author jstroot
 * 
 */
class ArgumentListEditor implements IsWidget, IsEditor<ListEditor<Argument, ArgumentEditor>> {
    private final VerticalLayoutContainer argumentsContainer;

    @Ignore
    final ListEditor<Argument, ArgumentEditor> editor;

    private boolean fireSelectedOnAdd;

    ArgumentListEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService, final AppMetadataServiceFacade appMetadataService, final XElement scrollElement) {
        argumentsContainer = new VerticalLayoutContainer();
        argumentsContainer.setAdjustForScroll(true);
        argumentsContainer.setScrollMode(ScrollMode.AUTOY);

        editor = ListEditor.of(new PropertyListEditorSource(argumentsContainer, presenter, uuidService, appMetadataService));
        ArgSelectedHandler appWizardSelectionHandler = new ArgSelectedHandler(editor, presenter.getAppearance().getStyle());

        if (presenter.isEditingMode()) {
            presenter.asWidget().addHandler(appWizardSelectionHandler, ArgumentGroupSelectedEvent.TYPE);
            presenter.asWidget().addHandler(appWizardSelectionHandler, ArgumentSelectedEvent.TYPE);
            presenter.asWidget().addHandler(appWizardSelectionHandler, AppTemplateSelectedEvent.TYPE);
            // If in editing mode, add drop target and DnD handlers
            ContainerDropTarget<VerticalLayoutContainer> dt = new ArgListEditorDropTarget(argumentsContainer, presenter, editor, scrollElement);
            dt.setFeedback(Feedback.BOTH);
            dt.setAllowSelfAsSource(true);
            new ArgListEditorDragSource(argumentsContainer, editor);

            IconButton argDeleteBtn = presenter.getAppearance().getArgListDeleteButton();
            argDeleteBtn.setVisible(false);
            argumentsContainer.add(argDeleteBtn);

            /*
             * JDS Create the argument delete handler, and add it to the argumentsContainer and the
             * argument delete button.
             */
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

    @Override
    public Widget asWidget() {
        return argumentsContainer;
    }

    private void setFireSelectedOnAdd(boolean fireSelectedOnAdd) {
        this.fireSelectedOnAdd = fireSelectedOnAdd;
    }

    private boolean isFireSelectedOnAdd() {
        return fireSelectedOnAdd;
    }

    public boolean hasErrors() {
        for (ArgumentEditor ae : editor.getEditors()) {
            if (ae.hasErrors()) {
                return true;
            }
        }
        return false;
    }

    private final class ArgSelectedHandler implements ArgumentSelectedEventHandler, ArgumentGroupSelectedEventHandler, AppTemplateSelectedEventHandler {
        private final AppTemplateWizardAppearance.Style style;
        private final ListEditor<Argument, ArgumentEditor> listEditor;

        public ArgSelectedHandler(ListEditor<Argument, ArgumentEditor> listEditor, AppTemplateWizardAppearance.Style style) {
            this.listEditor = listEditor;
            this.style = style;
        }

        @Override
        public void onArgumentSelected(ArgumentSelectedEvent event) {
            clearSelectionStyles();
        }

        @Override
        public void onArgumentGroupSelected(ArgumentGroupSelectedEvent event) {
            clearSelectionStyles();
        }

        @Override
        public void onAppTemplateSelected(AppTemplateSelectedEvent appTemplateSelectedEvent) {
            clearSelectionStyles();
        }

        public void clearSelectionStyles() {
            if (listEditor == null) {
                return;
            }
            for (ArgumentEditor ae : listEditor.getEditors()) {
                ae.removeStyleName(style.argumentSelect());
            }
        }

    }

    /**
     * A handler which controls the visibility, placement, and selection of a delete button over the
     * children of a <code>VerticalLayoutContainer</code>.
     * 
     * TODO JDS Need to disable the Delete hovers when a drag is occuring.
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
                    Argument arg = listEditor.getList().get(j);
                    if (presenter.isOnlyLabelEditMode() && !arg.getType().equals(ArgumentType.Info)) {
                        break;
                    }
                    
                    // JDS Do not display delete button for EmptyGroup argument.
                    if (!Strings.isNullOrEmpty(arg.getId()) && arg.getId().equalsIgnoreCase(AppTemplateUtils.EMPTY_GROUP_ARG_ID)) {
                        break;
                    }
                    currentItemIndex = j;
                    button.setVisible(true);
                    button.setPagePosition(child.getAbsoluteRight() - (2 * button.getOffsetWidth()), child.getAbsoluteTop());

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
            button.setVisible(false);
        }

        @Override
        public void onSelect(SelectEvent event) {
            if (currentItemIndex >= 0) {
                Argument arg = listEditor.getList().get(currentItemIndex);
                if (presenter.isOnlyLabelEditMode() && !arg.getType().equals(ArgumentType.Info)) {
                    return;
                }
                listEditor.getList().remove(currentItemIndex);

                /*
                 * JDS Fire ArgumentSelectedEvent with null parameter. This is to inform handlers that
                 * the selection should be cleared, or the previous argument selected, if possible.
                 */
                if (!listEditor.getList().isEmpty()) {
                    int index = (currentItemIndex > 0) ? currentItemIndex - 1 : 0;
                    ArgumentEditor toBeSelected = listEditor.getEditors().get(index);
                    presenter.asWidget().fireEvent(new ArgumentSelectedEvent(toBeSelected.getPropertyEditor()));
                } else {
                    /*
                     * JDS If the ArgumentGroup is empty after performing remove, add the empty group
                     * argument.
                     */
                    listEditor.getList().add(AppTemplateUtils.getEmptyGroupArgument());
                    layoutContainer.forceLayout();
                    presenter.asWidget().fireEvent(new ArgumentSelectedEvent(null));
                }

                presenter.onArgumentPropertyValueChange();
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

        private ArgListEditorDropTarget(VerticalLayoutContainer container, AppTemplateWizardPresenter presenter, ListEditor<Argument, ArgumentEditor> editor, XElement scrollElement) {
            super(container, scrollElement);
            this.presenter = presenter;
            this.listEditor = editor;
            setScrollDelay(presenter.getAppearance().getAutoScrollDelay());
            setScrollRegionHeight(presenter.getAppearance().getAutoScrollRegionHeight());
            setScrollRepeatDelay(presenter.getAppearance().getAutoScrollRepeatDelay());
        }

        @Override
        protected boolean verifyDragData(Object dragData) {
            // Only accept drag data which is an Argument
            return (dragData instanceof Argument) && super.verifyDragData(dragData)
                    && (!presenter.isOnlyLabelEditMode() || (presenter.isOnlyLabelEditMode() && ((Argument)dragData).getType().equals(ArgumentType.Info)));
        }

        @Override
        protected void onDragDrop(DndDropEvent event) {
            super.onDragDrop(event);
            List<Argument> list = listEditor.getList();
            boolean isNewArg = AutoBeanUtils.getAutoBean((Argument)event.getData()).getTag(Argument.IS_NEW) != null;
            Argument newArg = AppTemplateUtils.copyArgument((Argument)event.getData());

            // Update new argument label if needed.
            if (isNewArg) {
                String label = newArg.getLabel() + " - " + argCountInt++;
                newArg.setLabel(label);
            }

            if (list != null) {
                // JDS Protect against OBOB issues (caused by argument delete button, which is actually a
                // child of the argumentsContainer
                setFireSelectedOnAdd(true);
                if (insertIndex >= list.size()) {
                    list.add(newArg);
                } else {
                    list.add(insertIndex, newArg);
                }

                // JDS Remove placeholder, empty group argument on DnD add.
                if (list.size() > 1) {
                    Argument argToRemove = null;
                    for (Argument arg : list) {
                        if (!Strings.isNullOrEmpty(arg.getId()) && arg.getId().equalsIgnoreCase(AppTemplateUtils.EMPTY_GROUP_ARG_ID)) {
                            argToRemove = arg;
                            break;
                        }
                    }
                    if (argToRemove != null) {
                        list.remove(argToRemove);
                    }
                }
                presenter.onArgumentPropertyValueChange();
            }
        }
    }

    private final class ArgListEditorDragSource extends DragSource {

        private final VerticalLayoutContainer container;
        private final ListEditor<Argument, ArgumentEditor> listEditor;
        private int dragArgumentIndex = -1;
        private Argument dragArgument = null;

        ArgListEditorDragSource(VerticalLayoutContainer container, ListEditor<Argument, ArgumentEditor> listEditor) {
            super(container);
            this.container = container;
            this.listEditor = listEditor;
        }

        /*
         * -- Find the current drag child, and its index within the list
         * -- Store references to each.
         * -- Add the Argument as the drag data.
         * -- Using the corresponding editor, set the drag shadow
         * -- Remove the argument from this list.
         */
        @Override
        protected void onDragStart(DndDragStartEvent event) {
            EventTarget target = event.getDragStartEvent().getNativeEvent().getEventTarget();
            Element as = Element.as(target);

            // Only want to allow drag start when we are over a child
            IsWidget findWidget = container.findWidget(as);
            if ((findWidget != null) && listEditor.getEditors().contains(findWidget)) {

                dragArgumentIndex = listEditor.getEditors().indexOf(findWidget);

                Argument argument = listEditor.getList().get(dragArgumentIndex);
                // Only allow drag if it's not the empty grp argument
                if (Strings.isNullOrEmpty(argument.getId()) || !argument.getId().equalsIgnoreCase(AppTemplateUtils.EMPTY_GROUP_ARG_ID)) {
                    event.getStatusProxy().update(findWidget.asWidget().getElement().getString());
                    event.setCancelled(false);

                    // JDS For now, let's remove on drag start
                    dragArgument = listEditor.getList().remove(dragArgumentIndex);
                    event.setData(dragArgument);
                    return;
                }
            }

            // JDS In every other case, clean up and cancel drag.
            dragArgumentIndex = -1;
            dragArgument = null;
            event.setCancelled(true);
            event.getStatusProxy().update("");
        }

        /*
         * -- Clear local references of Argument and index
         */
        @Override
        protected void onDragDrop(DndDropEvent event) {
            dragArgumentIndex = -1;
            dragArgument = null;
        }

        /*
         * -- Re-insert the Argument back into the list at the stored index.
         */
        @Override
        protected void onDragCancelled(DndDragCancelEvent event) {

            // JDS Put the Argument back
            listEditor.getList().add(dragArgumentIndex, dragArgument);

            dragArgumentIndex = -1;
            dragArgument = null;
        }

        /*
         * -- Re-insert the Argument back into the list at the stored index.
         */
        @Override
        protected void onDragFail(DndDropEvent event) {

            // JDS Put the Argument back
            listEditor.getList().add(dragArgumentIndex, dragArgument);

            dragArgumentIndex = -1;
            dragArgument = null;
        }

    }

    private class PropertyListEditorSource extends EditorSource<ArgumentEditor> {
        private static final int DEF_ARGUMENT_MARGIN = 10;
        private final VerticalLayoutContainer con;
        private final AppTemplateWizardPresenter presenter;
        private final UUIDServiceAsync uuidService;
        private final AppMetadataServiceFacade appMetadataService;

        public PropertyListEditorSource(final VerticalLayoutContainer con, final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService,
                final AppMetadataServiceFacade appMetadataService) {
            this.con = con;
            this.presenter = presenter;
            this.uuidService = uuidService;
            this.appMetadataService = appMetadataService;
        }

        @Override
        public ArgumentEditor create(int index) {
            final ArgumentEditor subEditor = new ArgumentEditor(presenter, uuidService, appMetadataService);

            con.insert(subEditor, index, new VerticalLayoutData(1, -1, new Margins(DEF_ARGUMENT_MARGIN)));

            if (isFireSelectedOnAdd()) {
                setFireSelectedOnAdd(false);
                presenter.asWidget().fireEvent(new ArgumentSelectedEvent(subEditor.getPropertyEditor()));
                subEditor.addStyleName(presenter.getAppearance().getStyle().argumentSelect());
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
}
