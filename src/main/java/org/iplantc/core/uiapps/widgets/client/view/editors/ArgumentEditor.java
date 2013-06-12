package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.ArgumentPropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

/**
 * This class contains a {@linkplain SimpleContainer} which is dynamically populated with one of two
 * <code>Editor<Argument></code> sub-editors based on the bound <code>Argument</code>'s type.
 * <p>
 * This class has three sub-editors;<br>
 * -- an {@link ArgumentValueEditor}<br>
 * -- an {@link ArgumentSelectionEditor}<br>
 * -- an <code>ArgumentPropertyEditor</code> which is available for retrieval when the presenter has
 * 'editingMode' enabled.<br>
 * When this class' bound <code>Argument</code> is set, if the argument type is any form of selection
 * type, the <code>ArgumentSelectionEditor</code> will be added to the container. Otherwise, the
 * <code>ArgumentValueEditor</code> will be added to the container. The editor which is not added to the
 * container will be disabled. It is worth noting that the <code>ArgumentPropertyEditor</code> is only
 * constructed (and therefore, part of the Editor hierarchy) if the presenter has 'editingMode' enabled.
 * </p>
 * <p>
 * It was necessary to construct the <code>ArgumentPropertyEditor</code> within this class to ensure that
 * all <code>Editor&lt;Argument&gt;</code>s which may be simultaneously editing the same
 * <code>Argument</code> are initialized within the same Editor hierarchy.
 * </p>
 * 
 * @author jstroot
 * 
 */
class ArgumentEditor extends Composite implements AppTemplateWizard.IArgumentEditor, ValueAwareEditor<Argument> {

    @Path("")
    ArgumentValueEditor argValueEditor;
    
    @Path("")
    ArgumentSelectionEditor argSelectionEditor;

    @Path("")
    ArgumentPropertyEditor argPropEditor = null;

    private Argument currValue;
    private final SimpleContainer con;

    private final AppTemplateWizardPresenter.Resources res = GWT.create(AppTemplateWizardPresenter.Resources.class);

    ArgumentEditor(final EventBus eventBus, final AppTemplateWizardPresenter presenter) {
        res.selectionCss().ensureInjected();
        con = new SimpleContainer() {
            @Override
            public void onBrowserEvent(Event event) {
                super.onBrowserEvent(event);
                int type = event.getTypeInt();
                if (getElement().isOrHasChild(event.getEventTarget().<Element> cast()) && (type == Event.ONMOUSEOVER || type == Event.ONMOUSEOUT)) {
                    XElement cast = getElement().<XElement> cast();
                    cast.setClassName(res.selectionCss().selectionTargetHover(), type == Event.ONMOUSEOVER);
                }
            }
        };
        argValueEditor = new ArgumentValueEditor(presenter);
        argSelectionEditor = new ArgumentSelectionEditor(presenter);
        if (presenter.isEditingMode()) {
            argPropEditor = new ArgumentPropertyEditor(presenter);
            ClickHandler clickHandler = new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    eventBus.fireEvent(new ArgumentSelectedEvent(ArgumentEditor.this));
                }
            };
            argValueEditor.addArgumentClickHandler(clickHandler);
            argSelectionEditor.addArgumentClickHandler(clickHandler);
        }
        con.sinkEvents(Event.MOUSEEVENTS);
        con.addStyleName(res.selectionCss().selectionTarget());
        initWidget(con);
    }

    @Override
    @Ignore
    public IsWidget getArgumentPropertyEditor() {
        return argPropEditor;
    }

    @Override
    public void setValue(Argument value) {
        this.currValue = value;
        if(con.getWidget() != null){
            // JDS If we've already set the container's child widget, we don't need to do anything else here.
            return;
        }
        /*
         * JDS - All selection argument types need to be handled separately.
         * This is because there are two things which need to be bound;
         * The lists, and the item which the user chooses from the list.
         */
        if (AppWizardFieldFactory.isSelectionArgumentType(value)) {
            con.add(argSelectionEditor);
            argValueEditor = null;
        } else {
            con.add(argValueEditor);
            argSelectionEditor = null;
        }
    }

    @Override
    public Argument getCurrentArgument() {
        return currValue;
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}
