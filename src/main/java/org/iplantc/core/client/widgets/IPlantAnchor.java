/**
 * 
 */
package org.iplantc.core.client.widgets;

import com.extjs.gxt.ui.client.event.Events;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;

/**
 * A widget to create html link like <a> tag
 * 
 * @author sriram
 * 
 */
public class IPlantAnchor extends Anchor {

    /**
     * 
     * A widget to create <a> like hyperlinks
     * 
     * @param text text to display
     * @param styleName default style name
     * @param mouseOverStyleName style to apply on mouse over
     * @param mouseOutStyleName style to apply on mouse out
     */
    public IPlantAnchor(String text, final String styleName, final String mouseOverStyleName,
            final String mouseOutStyleName) {

        setText(text);
        setStyleName(styleName);
        init();
        addMouseOverHandler(new MouseOverHandler() {
            
            @Override
            public void onMouseOver(MouseOverEvent arg0) {
                removeStyleName(mouseOutStyleName);
                addStyleName(mouseOverStyleName);
            }
        });
        
        addMouseOutHandler(new MouseOutHandler() {
            
            @Override
            public void onMouseOut(MouseOutEvent arg0) {
                removeStyleName(mouseOverStyleName);
                addStyleName(mouseOutStyleName);
                
            }
        });
    }

    private void init() {
        sinkEvents(Events.OnClick.getEventCode());
        sinkEvents(Events.OnMouseOver.getEventCode());
        sinkEvents(Events.OnMouseOut.getEventCode());
    }

}
