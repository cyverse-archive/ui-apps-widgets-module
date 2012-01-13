package org.iplantc.core.client.widgets;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;

/**
 * A custom label class used in header to act/style like a menus
 * 
 * @author sriram
 * 
 */
public class MenuLabel extends Label {
    private String mouseOverStyle;

    public MenuLabel(String text, String baseStyle, String mouseOverStyle) {
        super(text);
        this.mouseOverStyle = mouseOverStyle;
        sinkBrowserEvents();
        setStyleName(baseStyle);
        addListeners();
    }

    private void sinkBrowserEvents() {
        sinkEvents(Events.OnClick.getEventCode());
        sinkEvents(Events.OnMouseOver.getEventCode());
        sinkEvents(Events.OnMouseOut.getEventCode());
    }

    private void addListeners() {
        addListener(Events.OnMouseOver, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                addStyleName(mouseOverStyle); //$NON-NLS-1$
            }
        });

        addListener(Events.OnMouseOut, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                removeStyleName(mouseOverStyle); //$NON-NLS-1$
            }
        });

    }

}
