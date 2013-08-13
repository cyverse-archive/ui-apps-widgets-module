package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;
import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.RequestArgumentGroupDeleteEvent;
import org.iplantc.core.uiapps.widgets.client.events.RequestArgumentGroupDeleteEvent.RequestArgumentGroupDeleteEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.editors.dnd.ContainerDropTarget;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppGroupContentPanelAppearance;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragCancelEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ContentPanel.ContentPanelAppearance;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;
import com.sencha.gxt.widget.core.client.event.AddEvent.HasAddHandlers;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;

/**
 * An editor class for displaying the argument group list in an {@link AppTemplate#getArgumentGroups()}.
 * 
 * 
 * @author jstroot
 * 
 */
class ArgumentGroupListEditor implements IsWidget, IsEditor<ListEditor<ArgumentGroup, ArgumentGroupEditor>>, HasAddHandlers, HasExpandHandlers, HasCollapseHandlers {

    private final AccordionLayoutContainer groupsContainer;
    private final ListEditor<ArgumentGroup, ArgumentGroupEditor> editor;
    private boolean fireSelectedOnAdd;
    private final List<CollapseHandler> collapseHandlers = Lists.newArrayList();
    private final List<ExpandHandler> expandHandlers = Lists.newArrayList();

    ArgumentGroupListEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService, final AppMetadataServiceFacade appMetadataService) {
        groupsContainer = new AccordionLayoutContainer();
        groupsContainer.setExpandMode(ExpandMode.SINGLE);
        editor = ListEditor.of(new ArgumentGroupEditorSource(groupsContainer, presenter, uuidService, appMetadataService));
        ArgGrpSelectedHandler appWizardSelectionHandler = new ArgGrpSelectedHandler(editor, presenter.getAppearance().getStyle());

        if (presenter.isEditingMode()) {
            presenter.asWidget().addHandler(appWizardSelectionHandler, ArgumentSelectedEvent.TYPE);
            presenter.asWidget().addHandler(appWizardSelectionHandler, ArgumentGroupSelectedEvent.TYPE);
            presenter.asWidget().addHandler(appWizardSelectionHandler, AppTemplateSelectedEvent.TYPE);
            // If in editing mode, add drop target and DnD handlers.
            ContainerDropTarget<AccordionLayoutContainer> dt = new ArgGrpListEditorDropTarget(groupsContainer, presenter, editor);
            dt.setFeedback(Feedback.BOTH);
            dt.setAllowSelfAsSource(true);
            new ArgGrpListDragSource(groupsContainer, editor);

        }
    }

    @Override
    public ListEditor<ArgumentGroup, ArgumentGroupEditor> asEditor() {
        return editor;
    }

    @Override
    public Widget asWidget() {
        return groupsContainer;
    }

    /**
     * Collapses all <code>ContentPanel</code> children in this class'
     * <code>AccordionLayoutContainer</code>.
     * 
     * This method is typically used when an {@link ArgumentGroup} is being dragged into this class'
     * <code>AccordionLayoutContainer</code>.
     */
    public void collapseAllArgumentGroups() {
        Iterator<Widget> iterator = groupsContainer.iterator();
        while (iterator.hasNext()) {
            ContentPanel cp = (ContentPanel)iterator.next();
            cp.collapse();
        }
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

    void insertFirstInAccordion(IsWidget widget) {
        groupsContainer.insert(widget, 0);
        groupsContainer.setActiveWidget(widget.asWidget());
    }

    private final class ArgGrpSelectedHandler implements ArgumentGroupSelectedEventHandler, ArgumentSelectedEventHandler, AppTemplateSelectedEventHandler {
        private final ListEditor<ArgumentGroup, ArgumentGroupEditor> listEditor;
        private final AppTemplateWizardAppearance.Style style;

        public ArgGrpSelectedHandler(ListEditor<ArgumentGroup, ArgumentGroupEditor> listEditor, AppTemplateWizardAppearance.Style style) {
            this.listEditor = listEditor;
            this.style = style;
        }

        @Override
        public void onArgumentGroupSelected(ArgumentGroupSelectedEvent event) {
            clearSelectionStyles();
        }

        @Override
        public void onArgumentSelected(ArgumentSelectedEvent event) {
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

            for (ArgumentGroupEditor age : listEditor.getEditors()) {
                age.getHeader().removeStyleName(style.appHeaderSelect());
            }

        }

    }

    private final class RequestDeleteHandler implements RequestArgumentGroupDeleteEventHandler {
        private final AppTemplateWizardPresenter presenter;

        private RequestDeleteHandler(AppTemplateWizardPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void onDeleteRequest(RequestArgumentGroupDeleteEvent event) {
            final ArgumentGroup argumentGroup = event.getArgumentGroup();
            if (editor.getList().contains(argumentGroup)) {
                // FIXME JDS Now check to see if it contains anything
                int indexRemoved = 0;
                if ((argumentGroup.getArguments() != null) && (argumentGroup.getArguments().size() > 0)) {
                    // JDS Prompt user if they are ok with deleting a non-empty group
                    indexRemoved = editor.getList().indexOf(argumentGroup);
                    editor.getList().remove(argumentGroup);
                } else {
                    indexRemoved = editor.getList().indexOf(argumentGroup);
                    editor.getList().remove(argumentGroup);
                }

                // JDS If possible, select the previous group, else, clear selection
                if (editor.getList().size() > 0) {
                    int index = (indexRemoved > 0) ? indexRemoved - 1 : 0;
                    ArgumentGroupEditor toBeSelected = editor.getEditors().get(index);
                    presenter.asWidget().fireEvent(new ArgumentGroupSelectedEvent(toBeSelected.getPropertyEditor()));
                    toBeSelected.addStyleName(presenter.getAppearance().getStyle().appHeaderSelect());
                } else {
                    presenter.asWidget().fireEvent(new ArgumentGroupSelectedEvent(null));

                }
            }
        }
    }

    /**
     * This <code>DropTarget</code> is responsible for handling {@link ArgumentGroup} additions to the
     * given {@link ListEditor}, as well as handling the auto-expansion of a child
     * <code>ContentPanel</code>s when a drag move containing an {@link Argument} is detected over a
     * <code>ContentPanel</code>'s header.
     * 
     * @author jstroot
     * 
     */
    private final class ArgGrpListEditorDropTarget extends ContainerDropTarget<AccordionLayoutContainer> {
        private final AppTemplateWizardPresenter presenter;
        private final ListEditor<ArgumentGroup, ArgumentGroupEditor> listEditor;
        private int grpCountInt = 2;
        private Header header;
        private final AppsWidgetsPropertyPanelLabels appsWidgetsDisplay = I18N.APPS_LABELS;

        private ArgGrpListEditorDropTarget(AccordionLayoutContainer container, AppTemplateWizardPresenter presenter, ListEditor<ArgumentGroup,ArgumentGroupEditor> editor) {
            super(container);
            this.presenter = presenter;
            this.listEditor = editor;
        }

        @Override
        protected boolean verifyDragData(Object dragData) {
            // Only accept drag data which is an ArgumentGroup
            return (dragData instanceof ArgumentGroup) && super.verifyDragData(dragData) && !presenter.isOnlyLabelEditMode();
        }

        @Override
        protected boolean verifyDragMove(EventTarget target, Object dragData) {
            XElement conElement = container.getElement();
            Element as = Element.as(target);
            if (Element.is(target) && conElement.isOrHasChild(as)) {
                if (verifyDragData(dragData)) {
                    header = null;
                    return true;
                } else if (dragData instanceof Argument) {
                    IsWidget findWidget = container.findWidget(as);
                    if ((findWidget != null)) {
                        boolean isCp = findWidget instanceof ContentPanel;
                        if (isCp && ((ContentPanel)findWidget).getHeader().getElement().isOrHasChild(as)) {
                            final ContentPanel cp = (ContentPanel)findWidget;
                            if(cp.isCollapsed()){
                            	header = cp.getHeader();
                            	// JDS Kick off timer for autoExpand of ArgumentGroup content panel
                            	Timer t = new Timer() {
                            		@Override
                            		public void run() {
                            			if ((cp.getHeader() == header) && cp.isCollapsed()) {
                            				container.setActiveWidget(cp);
                            			}
                            		}
                            	};
                            	t.schedule(presenter.getAppearance().getAutoExpandOnHoverDelay());
                            	
                            }
                        } else {
                            header = null;
                        }
                    } else {
                        header = null;
                    }
                }
            }

            return super.verifyDragMove(target, dragData);
        }

        @Override
        protected void onDragDrop(DndDropEvent event) {
            super.onDragDrop(event);
            List<ArgumentGroup> list = listEditor.getList();
            boolean isNewArgGrp = AutoBeanUtils.getAutoBean((ArgumentGroup)event.getData()).getTag(ArgumentGroup.IS_NEW) != null;
            ArgumentGroup newArgGrp = AppTemplateUtils.copyArgumentGroup((ArgumentGroup)event.getData());

            // Update new group label, if needed
            if (isNewArgGrp) {
                String defaultGroupLabel = appsWidgetsDisplay.groupDefaultLabel(grpCountInt++);
                newArgGrp.setLabel(defaultGroupLabel);
            }

            if (list != null) {
                setFireSelectedOnAdd(true);
                list.add(insertIndex, newArgGrp);
                presenter.onArgumentPropertyValueChange();
            }

            // TODO JDS Handle DnD Argument additions when drop occurs on a ContentPanel header.
            // Not sure if that should occur here, or elsewhere.
        }
    }

    private final class ArgGrpListDragSource extends DragSource {

        private final ListEditor<ArgumentGroup, ArgumentGroupEditor> listEditor;
        private final AccordionLayoutContainer container;
        private int dragArgGrpIndex = -1;
        private ArgumentGroup dragArgGrp = null;

        private ArgGrpListDragSource(AccordionLayoutContainer container, ListEditor<ArgumentGroup, ArgumentGroupEditor> listEditor) {
            super(container);
            this.container = container;
            this.listEditor = listEditor;
        }

        @Override
        protected void onDragStart(DndDragStartEvent event) {
            EventTarget target = event.getDragStartEvent().getNativeEvent().getEventTarget();
            Element as = Element.as(target);

            // Only want to allow drag start when we are over a child
            IsWidget findWidget = container.findWidget(as);
            List<ArgumentGroupEditor> editors = listEditor.getEditors();
            boolean contains = editors.contains(findWidget);
            if ((findWidget != null) && contains && ((ContentPanel)findWidget.asWidget()).getHeader().getElement().isOrHasChild(as)) {
                event.getStatusProxy().update(((ContentPanel)findWidget.asWidget()).getHeader().getElement().getString());
                event.setCancelled(false);

                dragArgGrpIndex = editors.indexOf(findWidget);
                // JDS For now, let's remove on drag start
                dragArgGrp = listEditor.getList().remove(dragArgGrpIndex);
                event.setData(dragArgGrp);

            } else {

                dragArgGrpIndex = -1;
                dragArgGrp = null;
                event.setCancelled(true);
                event.getStatusProxy().update("");
            }
        }

        /*
         * -- Clear local references of Argument and index
         */
        @Override
        protected void onDragDrop(DndDropEvent event) {
            dragArgGrpIndex = -1;
            dragArgGrp = null;
        }

        /*
         * -- Re-insert the ArgumentGroup back into the list at the stored index.
         */
        @Override
        protected void onDragCancelled(DndDragCancelEvent event) {

            // JDS Put the ArgumentGroup back
            listEditor.getList().add(dragArgGrpIndex, dragArgGrp);

            dragArgGrpIndex = -1;
            dragArgGrp = null;
        }

        /*
         * -- Re-insert the ArgumentGroup back into the list at the stored index.
         */
        @Override
        protected void onDragFail(DndDropEvent event) {

            // JDS Put the ArgumentGroup back
            listEditor.getList().add(dragArgGrpIndex, dragArgGrp);

            dragArgGrpIndex = -1;
            dragArgGrp = null;
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
        private final AppMetadataServiceFacade appMetadataService;
        private RequestDeleteHandler handler;

        public ArgumentGroupEditorSource(AccordionLayoutContainer con, AppTemplateWizardPresenter presenter, UUIDServiceAsync uuidService, AppMetadataServiceFacade appMetadataService) {
            this.con = con;
            this.presenter = presenter;
            this.uuidService = uuidService;
            this.appMetadataService = appMetadataService;
            if (presenter.isEditingMode()) {
                this.handler = new RequestDeleteHandler(presenter);
            }
        }

        @Override
        public ArgumentGroupEditor create(int index) {
            ContentPanelAppearance cpAppearance;
            if (presenter.isEditingMode()) {
                cpAppearance = new AppGroupContentPanelAppearance();
            } else {
                cpAppearance = GWT.create(ContentPanelAppearance.class);
            }
            final ArgumentGroupEditor subEditor = new ArgumentGroupEditor(presenter, uuidService, appMetadataService, con.getElement(), cpAppearance);
            subEditor.setCollapsible(true);
            for (CollapseHandler h : collapseHandlers) {
                subEditor.addCollapseHandler(h);
            }
            for (ExpandHandler h : expandHandlers) {
                subEditor.addExpandHandler(h);
            }
            con.insert(subEditor, index);

            if (index == 0) {
                // Ensure that the first container is expanded automatically
                con.setActiveWidget(subEditor.asWidget());
            }

            if (presenter.isEditingMode()) {
                if (isFireSelectedOnAdd()) {
                    presenter.asWidget().fireEvent(new ArgumentGroupSelectedEvent(subEditor.getPropertyEditor()));
                    con.setActiveWidget(subEditor.asWidget());
                    subEditor.getHeader().addStyleName(presenter.getAppearance().getStyle().appHeaderSelect());
                    setFireSelectedOnAdd(false);
                }
                subEditor.addRequestArgumentGroupDeleteEventHandler(handler);
            }

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

    @Override
    public HandlerRegistration addAddHandler(AddHandler handler) {
        return groupsContainer.addAddHandler(handler);
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
        collapseHandlers.add(handler);
        return null;
    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandHandler handler) {
        expandHandlers.add(handler);
        return null;
    }
}
