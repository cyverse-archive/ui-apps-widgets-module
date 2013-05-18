package org.iplantc.core.uiapps.widgets.client.events;

import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AppTemplateSelectedEvent extends GwtEvent<AppTemplateSelectedEventHandler> {

    public interface AppTemplateSelectedEventHandler extends EventHandler {
        void onAppTemplateSelected(AppTemplateSelectedEvent appTemplateSelectedEvent);
    }

    public static final GwtEvent.Type<AppTemplateSelectedEventHandler> TYPE = new GwtEvent.Type<AppTemplateSelectedEventHandler>();

    public AppTemplateSelectedEvent(Object source) {
        setSource(source);
    }

    @Override
    public GwtEvent.Type<AppTemplateSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppTemplateSelectedEventHandler handler) {
        handler.onAppTemplateSelected(this);
    }

}
