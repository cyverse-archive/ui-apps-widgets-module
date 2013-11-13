package org.iplantc.core.uiapps.widgets.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentAddedEvent.ArgumentAddedEventHandler;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm.ArgumentEditor;

/**
 * Event fired when an ArgumentEditor is added to the AppTemplateForm editor hierarchy.
 * 
 * @author jstroot
 * 
 */
public class ArgumentAddedEvent extends GwtEvent<ArgumentAddedEventHandler> {

    public interface ArgumentAddedEventHandler extends EventHandler {
        void onArgumentAdded(ArgumentAddedEvent event);
    }

    public static interface HasArgumentAddedEventHandlers {
        HandlerRegistration addArgumentAddedEventHandler(ArgumentAddedEventHandler handler);
    }

    public static final GwtEvent.Type<ArgumentAddedEventHandler> TYPE = new GwtEvent.Type<ArgumentAddedEvent.ArgumentAddedEventHandler>();
    private final ArgumentEditor argEditor;

    public ArgumentAddedEvent(ArgumentEditor argEditor) {
        this.argEditor = argEditor;
    }

    public ArgumentEditor getArgumentEditor() {
        return argEditor;
    }

    @Override
    public GwtEvent.Type<ArgumentAddedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ArgumentAddedEventHandler handler) {
        handler.onArgumentAdded(this);
    }

}
