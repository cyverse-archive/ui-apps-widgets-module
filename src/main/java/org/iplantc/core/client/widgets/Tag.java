package org.iplantc.core.client.widgets;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;

/**
 * A component that displays a string and has a remove button. When multiple Tags are added to a
 * FlowLayout container, they "flow" as needed to fit in the container.<br/>
 * Clicking the remove button removes the Tag from its parent and calls the afterRemove method.
 * 
 * @author hariolf
 * 
 */
public class Tag extends HorizontalPanel {
    private String text;

    /**
     * Creates a new Tag using the default style.
     * 
     * @param text the text to display
     */
    public Tag(String text) {
        this.text = text;
        setStyleAttribute("border-style", "solid"); //$NON-NLS-1$ //$NON-NLS-2$
        setStyleAttribute("border-width", "1px"); //$NON-NLS-1$ //$NON-NLS-2$
        setStyleAttribute("border-radius", "3px"); //$NON-NLS-1$ //$NON-NLS-2$
        setStyleAttribute("-moz-border-radius", "3px"); //$NON-NLS-1$ //$NON-NLS-2$
        setStyleAttribute("margin", "3px"); //$NON-NLS-1$ //$NON-NLS-2$
        setStyleAttribute("float", "left"); //$NON-NLS-1$ //$NON-NLS-2$
        init("<span style='font-size: 10px; font-weight: bold; cursor: pointer'>&nbsp;x&nbsp;</span>"); //$NON-NLS-1$
    }

    /**
     * Creates a new Tag with a given CSS style.
     * 
     * @param text the text to display
     * @param styleName the CSS style to apply to the Tag
     */
    public Tag(String text, String styleName) {
        this.text = text;
        setStyleName(styleName);
        init("<span class='tag-remove'>&nbsp;</span>"); //$NON-NLS-1$
    }

    /**
     * Initialization method
     * 
     * @param buttonHtml HTML code for the remove button
     */
    private void init(String buttonHtml) {
        add(new Html(text));

        Html removeBtn = new Html(buttonHtml);
        removeBtn.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                removeFromParent();
                afterRemove();
            }
        });
        add(removeBtn);
    }

    /**
     * Returns the tag text.
     * 
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Called after the tag has been removed via the remove button
     */
    public void afterRemove() {
    }
}