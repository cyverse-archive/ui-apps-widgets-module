package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.RequestArgumentGroupDeleteEvent;
import org.iplantc.core.uiapps.widgets.client.events.RequestArgumentGroupDeleteEvent.RequestArgumentGroupDeleteEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.ArgumentGroupPropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppWizardQuickTip;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.ContentPanel;

/**
 * This class contains a {@link ContentPanel} whose header text is updated when the bound
 * <code>ArgumentGroup</code> is set/updated.
 * 
 * @author jstroot
 * 
 */
class ArgumentGroupEditor extends ContentPanel implements HasPropertyEditor, ValueAwareEditor<ArgumentGroup> {
    ArgumentListEditor argumentsEditor;

    @Path("")
    ArgumentGroupPropertyEditor argGrpPropEditor;
    private final AppTemplateWizardPresenter presenter;

    public ArgumentGroupEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService, final AppMetadataServiceFacade appMetadataService, final XElement scrollElement,
            ContentPanelAppearance myappearance) {
        super(myappearance);
        this.presenter = presenter;

        argumentsEditor = new ArgumentListEditor(presenter, uuidService, appMetadataService, scrollElement);
        add(argumentsEditor);
        if (presenter.isEditingMode()) {
            argGrpPropEditor = new ArgumentGroupPropertyEditor(presenter);
            sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
        }
        new AppWizardQuickTip(header);
    }
    
    @Override
    protected void assertPreRender() {
        // KLUDGE JDS Do nothing. This is a workaround for the following bug (which was submitted to the
        // GXT forums);
        // http://www.sencha.com/forum/showthread.php?261470-Adding-new-ContentPanel-to-AccordionLayoutContainer-at-runtime-issue
    }

    @Override
    protected void onClick(Event ce) {
        super.onClick(ce);
        if (presenter.isEditingMode() && header.getElement().isOrHasChild(ce.getEventTarget().<Element> cast())) {
            ArgumentGroupSelectedEvent argGrpSelectedEvent = new ArgumentGroupSelectedEvent(argGrpPropEditor);
            presenter.asWidget().fireEvent(argGrpSelectedEvent);
            getHeader().addStyleName(presenter.getAppearance().getStyle().appHeaderSelect());
        }
    }

    @Override
    public void setValue(ArgumentGroup value) {
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        if (argumentsEditor.hasErrors()) {
            ImageElement errImg = presenter.getAppearance().getErrorIconImgWithErrQTip(argumentsEditor.getErrors());
            labelText.appendHtmlConstant(errImg.getString());
        }
        if (value.getArguments().isEmpty()) {
            /* JDS If the argument group has no arguments, add special Empty group argument.
             * This argument should be removed if it still exists when this app gets saved.
             */
            value.getArguments().add(AppTemplateUtils.getEmptyGroupArgument());
        }
        boolean isRequired = false;
        for (Argument property : value.getArguments()) {
            if (property.getRequired()) {
                // If any field is required, mark header as required.
                isRequired = true;
                break;
            }
        }
        // When the value is set, update the FieldSet header text
        labelText.append(presenter.getAppearance().createContentPanelHeaderLabel(SafeHtmlUtils.fromString(value.getLabel()), isRequired));
        setHeadingHtml(labelText.toSafeHtml());
    }

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void setDelegate(EditorDelegate<ArgumentGroup> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public IsWidget getPropertyEditor() {
        return argGrpPropEditor;
    }

    public void addRequestArgumentGroupDeleteEventHandler(RequestArgumentGroupDeleteEventHandler handler) {
        argGrpPropEditor.addHandler(handler, RequestArgumentGroupDeleteEvent.TYPE);
    }

}
