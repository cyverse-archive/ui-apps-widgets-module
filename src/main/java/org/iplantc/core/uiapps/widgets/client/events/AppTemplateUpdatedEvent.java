package org.iplantc.core.uiapps.widgets.client.events;

import org.iplantc.core.uiapps.widgets.client.events.AppTemplateUpdatedEvent.AppTemplateUpdatedEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AppTemplateUpdatedEvent extends GwtEvent<AppTemplateUpdatedEventHandler> {

    public interface AppTemplateUpdatedEventHandler extends EventHandler {
        void onAppTemplateUpdate(AppTemplateUpdatedEvent event);
    }

    public static final GwtEvent.Type<AppTemplateUpdatedEventHandler> TYPE = new GwtEvent.Type<AppTemplateUpdatedEventHandler>();

    public AppTemplateUpdatedEvent(Object source) {
        setSource(source);
    }

    @Override
    public GwtEvent.Type<AppTemplateUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppTemplateUpdatedEventHandler handler) {
        handler.onAppTemplateUpdate(this);
    }

}
