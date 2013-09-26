package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateUpdatedEvent;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateUpdatedEvent.AppTemplateUpdatedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.AppTemplatePropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.ArgumentPropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppGroupContentPanelAppearance;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.impl.Refresher;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ContentPanel.ContentPanelAppearance;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.AddEvent;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;

/**
 * A wizard for editing <code>AppTemplate</code>s.
 * 
 * This view operates in one of two modes; editing or not editing.
 * While editing, selection of the {@link Argument} field within the the {@link ArgumentGroup}
 * <code>ContentPanel</code> is enabled. Selecting an Argument field will fire an
 * {@link ArgumentSelectedEvent} through the event bus. Then, the Argument
 * field's corresponding {@link ArgumentPropertyEditor} can be retrieved.
 * 
 * If editing is not enabled, the {@link ArgumentPropertyEditor} will not be available, and will not be
 * initialized with the editor hierarchy.
 * 
 * @author jstroot
 * 
 */
public class AppTemplateWizard extends Composite implements ValueAwareEditor<AppTemplate>, AppTemplateWizardPresenter {
    
    interface EditorDriver extends SimpleBeanEditorDriver<AppTemplate, AppTemplateWizard> {}
    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
    
    private final ContentPanel con;
    ArgumentGroupListEditor argumentGroups;

    @Path("")
    AppTemplatePropertyEditor appTemplatePropEditor;

    private final boolean editingMode;
    private AppTemplate appTemplate;

    private Object valueChangeEventSource;
    
    private final AppTemplateWizardAppearance appearance;

    private boolean onlyLabelEditMode = false;

    public AppTemplateWizard(boolean editingMode, final UUIDServiceAsync uuidService, final AppMetadataServiceFacade appMetadataService, AppTemplateWizardAppearance appearance) {
        this.appearance = appearance;
        this.editingMode = editingMode;
        argumentGroups = new ArgumentGroupListEditor(this, uuidService, appMetadataService);

        final VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        vlc.setScrollMode(ScrollMode.AUTOY);
        vlc.setAdjustForScroll(true);
        ContentPanelAppearance cpAppearance;
        if (editingMode) {
            cpAppearance = new AppGroupContentPanelAppearance();
        } else {
            cpAppearance = GWT.create(ContentPanelAppearance.class);
        }
        con = new AppTemplateContentPanel(cpAppearance);
        ExpandCollapseHander expandCollapseHandler = new ExpandCollapseHander(vlc);
        con.addExpandHandler(expandCollapseHandler);
        con.addCollapseHandler(expandCollapseHandler);
        argumentGroups.addCollapseHandler(expandCollapseHandler);
        argumentGroups.addExpandHandler(expandCollapseHandler);
        argumentGroups.addAddHandler(expandCollapseHandler);


        if (editingMode) {
            appTemplatePropEditor = new AppTemplatePropertyEditor(this);
            con.add(appTemplatePropEditor);
            vlc.add(con, new VerticalLayoutData(1.0, -1.0));
            con.setCollapsible(true);
            con.setAnimCollapse(false);
            con.setTitleCollapse(true);
            con.getHeader().addStyleName(appearance.getStyle().appHeaderSelect());
            con.sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
        } else {
            con.setHeaderVisible(false);
        }

        vlc.add(argumentGroups, new VerticalLayoutData(1.0, -1.0));
        initWidget(vlc);
        editorDriver.initialize(this);

        SelectionHandler selectionHandler = new SelectionHandler(con.getHeader(), appearance.getStyle());
        addArgumentSelectedEventHandler(selectionHandler);
        addArgumentGroupSelectedEventHandler(selectionHandler);
    }

    public AppTemplateWizard(boolean editingMode, final UUIDServiceAsync uuidService, final AppMetadataServiceFacade appMetadataService) {
        this(editingMode, uuidService, appMetadataService, GWT.<AppTemplateWizardAppearance> create(AppTemplateWizardAppearance.class));
    }

    /**
     * This method binds this component with the given <code>AppTemplate</code> instance.
     * 
     * @see {@link EditorDriver#edit(AppTemplate)}
     * @param apptemplate
     */
    public void edit(AppTemplate apptemplate) {
        editorDriver.edit(apptemplate);
        fireEvent(new AppTemplateSelectedEvent(null));
    }

    /**
     * This method updates the bound <code>AppTemplate</code> with the current
     * values from the editor.
     * 
     * @see {@link EditorDriver#flush()}
     * @return
     */
    public AppTemplate flushAppTemplate() {
        AppTemplate flush = editorDriver.flush();
        AppTemplate cleaned = AppTemplateUtils.removeEmptyGroupArguments(flush);
        if (hasErrors()) {
            editorDriver.accept(new Refresher());
        }
        return cleaned;
    }

    public AppTemplate flushAppTemplateRaw() {
        return editorDriver.flush();
    }

    @Override
    public void onArgumentPropertyValueChange() {
        AppTemplate atTmp = flushAppTemplate();
        editorDriver.accept(new Refresher());
        if (isEditingMode()) {
            // JDS If in editing mode, refresh second time to sync editor error validation detection.
            editorDriver.accept(new Refresher());
        }
        fireEvent(new AppTemplateUpdatedEvent(this, atTmp));
        valueChangeEventSource = null;
    }

    @Override
    public void onArgumentPropertyValueChange(Object source) {
        valueChangeEventSource = source;
        onArgumentPropertyValueChange();
    }

    public boolean hasErrors() {
        return editorDriver.hasErrors();
    }

    public List<EditorError> getErrors() {
        return editorDriver.getErrors();
    }

    @Override
    public boolean isEditingMode() {
        return editingMode;
    }

    public void collapseAllArgumentGroups() {
        argumentGroups.collapseAllArgumentGroups();
    }

    @Override
    public void setValue(AppTemplate value) {
        this.appTemplate = value;
        if (isEditingMode()) {
            SafeHtml appName = SafeHtmlUtils.fromString(value.getName());
            SafeHtml labelText = appearance.createContentPanelHeaderLabel(appName, false);
            con.setHeadingHtml(labelText);
        }
    }

    @Override
    public void setDelegate(EditorDelegate<AppTemplate> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    public void insertFirstInAccordion(IsWidget widget) {
        argumentGroups.insertFirstInAccordion(widget);
    }

    @Override
    public Object getValueChangeEventSource() {
        return valueChangeEventSource;
    }

    public void addAppTemplateSelectedEventHandler(AppTemplateSelectedEventHandler handler) {
        addHandler(handler, AppTemplateSelectedEvent.TYPE);
    }

    public void addAppTemplateUpdatedEventHandler(AppTemplateUpdatedEventHandler handler) {
        addHandler(handler, AppTemplateUpdatedEvent.TYPE);
    }

    public void addArgumentSelectedEventHandler(ArgumentSelectedEventHandler handler) {
        addHandler(handler, ArgumentSelectedEvent.TYPE);
    }

    public void addArgumentGroupSelectedEventHandler(ArgumentGroupSelectedEventHandler handler) {
        addHandler(handler, ArgumentGroupSelectedEvent.TYPE);
    }

    public void updateAppTemplateId(String id) {
        if (Strings.isNullOrEmpty(appTemplate.getId())) {
            appTemplate.setId(id);
        } else if (appTemplate.getId().equalsIgnoreCase(id)) {
            // JDS There was an app ID, but now we are changing it. This is undesired.
            GWT.log("Attempt to change app ID from \"" + appTemplate.getId() + "\" to \"" + id + "\"");
        }

    }

    @Override
    public boolean isOnlyLabelEditMode() {
        return onlyLabelEditMode;
    }

    @Override
    public void setOnlyLabelEditMode(boolean onlyLabelEditMode) {
        this.onlyLabelEditMode = onlyLabelEditMode;
    }

    @Override
    public AppTemplateWizardAppearance getAppearance() {
        return appearance;
    }

    
    private final class AppTemplateContentPanel extends ContentPanel {
        private AppTemplateContentPanel(ContentPanelAppearance appearance) {
            super(appearance);
        }

        @Override
        protected void onClick(Event ce) {
            XElement element = XElement.as(header.getElement());
            if (element.isOrHasChild(ce.getEventTarget().<Element> cast())) {
                AppTemplateWizard.this.fireEvent(new AppTemplateSelectedEvent(null));
                getHeader().addStyleName(AppTemplateWizard.this.appearance.getStyle().appHeaderSelect());
            }
            super.onClick(ce);
        }
    }

    private final class ExpandCollapseHander implements ExpandHandler, CollapseHandler, AddHandler {
        private final VerticalLayoutContainer vlc;
    
        private ExpandCollapseHander(VerticalLayoutContainer vlc) {
            this.vlc = vlc;
        }
    
        @Override
        public void onExpand(ExpandEvent event) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    vlc.forceLayout();
                }
            });
        }
    
        @Override
        public void onCollapse(CollapseEvent event) {
            vlc.forceLayout();
        }
    
        @Override
        public void onAdd(AddEvent event) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    vlc.forceLayout();
                }
            });
        }
    }

    private final class SelectionHandler implements ArgumentSelectedEventHandler, ArgumentGroupSelectedEventHandler {
        private final Header header;
        private final AppTemplateWizardAppearance.Style style;
    
        public SelectionHandler(Header header, AppTemplateWizardAppearance.Style style) {
            this.header = header;
            this.style = style;
        }
    
        @Override
        public void onArgumentSelected(ArgumentSelectedEvent event) {
            header.removeStyleName(style.appHeaderSelect());
        }
    
        @Override
        public void onArgumentGroupSelected(ArgumentGroupSelectedEvent event) {
            header.removeStyleName(style.appHeaderSelect());
        }
    }
}
