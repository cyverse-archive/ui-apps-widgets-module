package org.iplantc.core.client.widgets;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * A Hyperlink class that can be initialized with a click listener and that adds or removes its own style
 * on mouse over or out events.
 * 
 * @author psarando
 * 
 */
public class MenuHyperlink extends Hyperlink {
    private String hoverStyle;

    public MenuHyperlink(String text, String baseStyle, String hoverStyle,
            Listener<BaseEvent> clickListener) {
        super(text, baseStyle);
        this.hoverStyle = hoverStyle;

        initListeners(clickListener);
    }

    public MenuHyperlink(String text, String baseStyle, String hoverStyle,
            Listener<BaseEvent> clickListener,
            String toolTipText) {
        super(text, baseStyle);
        this.hoverStyle = hoverStyle;
        setToolTipText(toolTipText);
        initListeners(clickListener);
    }

    private void setToolTipText(String toolTipText) {
        if (toolTipText != null && !toolTipText.isEmpty()) {
            setToolTip(toolTipText);
        }
    }

    protected void initListeners(Listener<BaseEvent> clickListener) {
        if (clickListener != null) {
            addListener(Events.OnClick, clickListener);
        }

        addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                addStyleName(hoverStyle);
            }
        });

        addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                removeStyleName(hoverStyle);
            }
        });
    }
}
