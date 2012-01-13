package org.iplantc.core.client.widgets.utils;

import java.util.Collection;
import java.util.HashMap;

import org.iplantc.core.client.widgets.panels.WizardWidgetPanel;

/**
 * Wrapper class to store our components.
 * 
 * @author amuir
 * 
 */
public class ComponentTable {
    private HashMap<String, WizardWidgetPanel> components; // id, component

    /**
     * Constructs an instance of the table.
     */
    public ComponentTable() {
        components = new HashMap<String, WizardWidgetPanel>();
    }

    /**
     * Add a component to the table.
     * 
     * @param id unique component id.
     * @param component component to track.
     */
    public void add(String id, final WizardWidgetPanel component) {
        components.put(id, component);
    }

    /**
     * Retrieve a component by id.
     * 
     * @param id unique component id.
     * @return requested component.
     */
    public WizardWidgetPanel get(final String id) {
        return components.get(id);
    }

    /**
     * Validates all components within our component table
     */
    public void validate() {
        Collection<WizardWidgetPanel> collection = components.values();

        for (WizardWidgetPanel component : collection) {
            component.validate();
        }
    }
}
