package org.iplantc.core.uiapps.widgets.client.events;

import org.iplantc.core.uiapps.widgets.client.events.AppTemplateUpdatedEvent.AppTemplateUpdatedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AppTemplateUpdatedEvent extends GwtEvent<AppTemplateUpdatedEventHandler> {

    public interface AppTemplateUpdatedEventHandler extends EventHandler {
        void onAppTemplateUpdate(AppTemplateUpdatedEvent event);
    }

    public static final GwtEvent.Type<AppTemplateUpdatedEventHandler> TYPE = new GwtEvent.Type<AppTemplateUpdatedEventHandler>();
    private final AppTemplate at;

    public AppTemplateUpdatedEvent(Object source, AppTemplate at) {
        setSource(source);
        this.at = at;
    }

    public AppTemplate getUpdatedAppTemplate() {
        return at;
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
