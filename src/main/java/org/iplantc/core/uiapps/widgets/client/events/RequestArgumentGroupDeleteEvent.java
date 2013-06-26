package org.iplantc.core.uiapps.widgets.client.events;

import org.iplantc.core.uiapps.widgets.client.events.RequestArgumentGroupDeleteEvent.RequestArgumentGroupDeleteEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RequestArgumentGroupDeleteEvent extends GwtEvent<RequestArgumentGroupDeleteEventHandler> {

    public interface RequestArgumentGroupDeleteEventHandler extends EventHandler {

        void onDeleteRequest(RequestArgumentGroupDeleteEvent requestArgumentGroupDeleteEvent);

    }

    public static final GwtEvent.Type<RequestArgumentGroupDeleteEventHandler> TYPE = new GwtEvent.Type<RequestArgumentGroupDeleteEvent.RequestArgumentGroupDeleteEventHandler>();
    private final ArgumentGroup model;

    public RequestArgumentGroupDeleteEvent(ArgumentGroup model) {
        this.model = model;
    }

    @Override
    public GwtEvent.Type<RequestArgumentGroupDeleteEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestArgumentGroupDeleteEventHandler handler) {
        handler.onDeleteRequest(this);
    }

    public ArgumentGroup getArgumentGroup() {
        return model;
    }
}
