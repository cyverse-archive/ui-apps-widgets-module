package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.ArgumentPropertyEditor;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
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
class ArgumentEditor extends Composite implements HasPropertyEditor, ValueAwareEditor<Argument> {

    @Path("")
    ArgumentPropertyEditor argPropEditor = null;

    private final SimpleContainer con;
    private ValueAwareEditor<Argument> subEditor = null;

    private final AppTemplateWizardPresenter presenter;

    private ClickHandler clickHandler;

    ArgumentEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService) {
        this.presenter = presenter;
        con = new SimpleContainer();
        if (presenter.isEditingMode()) {
            con.sinkEvents(Event.MOUSEEVENTS);
            con.addStyleName(presenter.getSelectionCss().selectionTargetMargin());
            argPropEditor = new ArgumentPropertyEditor(presenter, uuidService);
            clickHandler = new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    presenter.asWidget().fireEvent(new ArgumentSelectedEvent(argPropEditor));
                }
            };
        }
        initWidget(con);
    }

    @Override
    @Ignore
    public IsWidget getPropertyEditor() {
        return argPropEditor;
    }

    @Override
    public void setValue(Argument value) {
        /*
         * 1. Have to determine which non-bound argument "editor" will need to be used. Once it is
         * chosen, we need to set its value.
         * 
         * 2. After this first step, each subsequent call to this method will need to pass the given
         * value to the chosen "editor"
         */
        if (subEditor == null) {
            // Then we need to initialize
            /*
             * JDS - All selection argument types need to be handled separately.
             * This is because there are two things which need to be bound;
             * The lists, and the item which the user chooses from the list.
             */
            if (AppTemplateUtils.isSelectionArgumentType(value.getType())) {
                ArgumentSelectionEditor argSelectionEditor = new ArgumentSelectionEditor(presenter);
                subEditor = argSelectionEditor;
                addClickHandler(argSelectionEditor);
                con.add(argSelectionEditor);
            } else {
                ArgumentValueEditor argValueEditor = new ArgumentValueEditor(presenter);
                subEditor = argValueEditor;
                addClickHandler(argValueEditor);
                con.add(argValueEditor);
            }
            subEditor.setValue(value);
            setEnabled(value.isVisible());
            if (!presenter.isEditingMode()) {
                setVisible(value.isVisible());
            }
        } else {
            subEditor.setValue(value);
            setEnabled(value.isVisible());
            if (!presenter.isEditingMode()) {
                setVisible(value.isVisible());
            }
        }
    }

    private void addClickHandler(HasClickHandlers subEditor) {
        if (presenter.isEditingMode()) {
            subEditor.addClickHandler(clickHandler);
        }
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void flush() {
        /*
         * Since our subEditor is not actually bound, we need to manually flush it.
         */
        subEditor.flush();
    }

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}
