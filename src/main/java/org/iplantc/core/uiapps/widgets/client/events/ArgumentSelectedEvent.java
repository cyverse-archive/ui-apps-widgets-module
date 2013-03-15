package org.iplantc.core.uiapps.widgets.client.events;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentField;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * An event to be fired when the user selects an {@link Argument} bound UI element.
 * 
 * @author jstroot
 * 
 */
public class ArgumentSelectedEvent extends GwtEvent<ArgumentSelectedEventHandler> {

    public interface ArgumentSelectedEventHandler extends EventHandler {
        void onArgumentSelected(ArgumentSelectedEvent event);
    }

    private final ArgumentField field;
    private final Argument argument;

    public ArgumentSelectedEvent(Object source, Argument argument, ArgumentField field) {
        this.argument = argument;
        this.field = field;
        setSource(source);
    }

    public static GwtEvent.Type<ArgumentSelectedEventHandler> TYPE = new GwtEvent.Type<ArgumentSelectedEventHandler>();

    @Override
    public GwtEvent.Type<ArgumentSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ArgumentSelectedEventHandler handler) {
        handler.onArgumentSelected(this);
    }

    public ArgumentField getField() {
        return field;
    }

    public Argument getArgument() {
        return argument;
    }
}
