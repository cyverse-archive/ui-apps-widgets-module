package org.iplantc.core.client.widgets.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.events.ValidationListener;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.Field;

/**
 * Abstract base class for all wizard widgets.
 * 
 * @author amuir
 * 
 */
public abstract class WizardWidgetPanel extends VerticalPanel {
    protected final ComponentValueTable tblComponentVals;
    private MetaDataValidator validator;
    private List<ValidationListener> validationListeners;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    protected WizardWidgetPanel(final Property property, final ComponentValueTable tblComponentVals,
            final List<String> params) {
        this(property, tblComponentVals, params, null);
    }

    protected WizardWidgetPanel(final Property property, final ComponentValueTable tblComponentVals,
            final List<String> params, final IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        this.tblComponentVals = tblComponentVals;
        validator = property.getValidator();

        initDiskResourceSelector(property, diskResourceSelectorBuilder);

        initInstanceVariables(property, params);

        setWidgetId(property.getId());

        addSupportingComponentVals();

        initValidators(property);

        initFormatters(property);

        setInitialState(property);

        updateComponentValueTable();

        compose();

        setValue(property.getValue());
    }

    /**
     * Adds a listener that will be called on validation events.
     * @param listener
     */
    public void addValidationListener(ValidationListener listener) {
        if (validationListeners == null)
        {
            validationListeners = new ArrayList<ValidationListener>();
        }
        validationListeners.add(listener);
    }
    
    /**
     * Causes all ValidationListeners to the WizardWidgetPanel to be fired when
     * a given field is validated.
     * @param inputField
     */
    protected void addValidationListener(Field<?> inputField) {
        inputField.addListener(Events.Valid, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                fireValidationListeners(true);
            }
        });
        inputField.addListener(Events.Invalid, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                fireValidationListeners(false);
            }
        });
    }
    
    protected void fireValidationListeners(boolean pass) {
        if (validationListeners != null) {
            for (ValidationListener listener: validationListeners) {
                listener.onValidation(pass);
            }
        }
    }
    
    private void initDiskResourceSelector(final Property property,
            final IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        if (diskResourceSelectorBuilder != null) {
            buildDiskResourceSelector(property, diskResourceSelectorBuilder);
        }
    }

    /**
     * Uses file selector builder to build a file selector.
     * 
     * @param property property that can potentially be used for initialization.
     * @param diskResourceSelectorBuilder builder class that can instantiate a file selector.
     */
    protected void buildDiskResourceSelector(final Property property,
            final IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        // do nothing by default
    }

    /**
     * Add any additional items needed in the component value table that are required for the widgets to
     * be validated, formatted, and exported.
     */
    protected void addSupportingComponentVals() {
    }

    /**
     * Initialize all validators needed by the widget panel.
     * 
     * @param property the associate property in the template JSON data.
     */
    protected void initValidators(final Property property) {
    }

    /**
     * Initialize a formatters required by the widget panel
     * 
     * @param property the associate property in the template JSON data.
     */
    protected void initFormatters(final Property property) {
    }

    /**
     * Set the initial state of the widget panel.
     * 
     * @param property the associate property in the template JSON data.
     */
    protected void setInitialState(final Property property) {
    }

    /**
     * Set any instance variables that are required by the widget panel.
     * 
     * @param property the associate property in the template JSON data.
     * @param params listing of string values associated with template JSON data.
     */
    protected abstract void initInstanceVariables(final Property property, final List<String> params);

    /**
     * Set the id of the primary widget in the panel. This id is used by both the component value table
     * and automated testing tools.
     * 
     * @param id a unique identifier for the widget.
     */
    protected abstract void setWidgetId(String id);

    /**
     * Perform the physical layout of the panel.
     */
    protected abstract void compose();

    /**
     * Returns a label that has an asterisk added at the end if the widget is a required field.
     * @param caption the label text
     */
    protected Label buildAsteriskLabel(String caption) {
        if (validator!=null && validator.isRequired()) {
            caption = "<span class='required_marker'>*</span> " + caption; //$NON-NLS-1$
        }
        return new Label(caption);
    }
    
    /**
     * Update the associated component value table when widget values change.
     */
    protected abstract void updateComponentValueTable();

    /**
     * Calls base enable and then allows subclasses to enable validators.
     */
    @Override
    public void enable() {
        super.enable();
        enableValidators();
    }

    /**
     * Calls base enable and then allows subclasses to disable validators.
     */
    @Override
    public void disable() {
        super.disable();
        disableValidators();
    }

    /**
     * Enable associated validators. This is called from enable();
     */
    protected void enableValidators() {
        // do nothing by default
    }

    /**
     * Disable associated validators. This is called from disable();
     */
    protected void disableValidators() {
        // do nothing by default
    }

    /**
     * Force a blur. This is a useful hook within the panel to force any contained widgets to blur.
     */
    public void validate() {
        // do nothing by default
    }

    /**
     * Refresh the panel with new data. This is useful when a change of data in a different panel needs
     * to be updated in the current panel
     */
    public void refresh() {
        // do nothing by default
    }

    /**
     * Sets the tool tip of a GXT component to the description field of a Property.
     * 
     * @param comp
     * @param property
     */
    protected void setToolTip(Component comp, Property property) {
        String toolTip = property.getDescription();
        if (toolTip != null && !toolTip.isEmpty()) {
            comp.setToolTip(toolTip);
        }
    }

    protected abstract void setValue(String value);
}