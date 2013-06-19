package org.iplantc.core.uiapps.widgets.client.events;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.IsWidget;

public class ArgumentGroupSelectedEvent extends GwtEvent<ArgumentGroupSelectedEventHandler> {

    public interface ArgumentGroupSelectedEventHandler extends EventHandler {
        void onArgumentGroupSelected(ArgumentGroupSelectedEvent event);
    }

    public static final GwtEvent.Type<ArgumentGroupSelectedEventHandler> TYPE = new GwtEvent.Type<ArgumentGroupSelectedEventHandler>();
    private final IsWidget propertyEditor;

    public ArgumentGroupSelectedEvent(IsWidget propertyEditor) {
        this.propertyEditor = propertyEditor;
    }

    @Override
    public GwtEvent.Type<ArgumentGroupSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ArgumentGroupSelectedEventHandler handler) {
        handler.onArgumentGroupSelected(this);
    }

    public IsWidget getArgumentGroupPropertyEditor() {
        return propertyEditor;
    }

}
