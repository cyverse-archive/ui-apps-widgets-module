package org.iplantc.core.uiapps.widgets.client.events;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.Argument;

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

    public ArgumentSelectedEvent(Object source) {
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

}