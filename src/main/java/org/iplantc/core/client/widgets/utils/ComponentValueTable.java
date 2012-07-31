package org.iplantc.core.client.widgets.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplantc.core.client.widgets.metadata.WizardNotification;
import org.iplantc.core.client.widgets.metadata.WizardPropertyGroupContainer;
import org.iplantc.core.client.widgets.panels.WizardWidgetPanel;
import org.iplantc.core.client.widgets.strategies.IWizardValidationBroadcastStrategy;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.groups.PropertyGroup;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * Value table that is useful for form level validation and parameter export.
 * 
 * @author amuir
 * 
 */
public class ComponentValueTable {
    private boolean isValidationEnabled;
    private String name;
    private String type;
    private String desc;
    private String templateId;
    private boolean isDebugEnabled;
    private boolean isNotifyEnabled;
    private String outputFolderId;
    private boolean createSubFolder;

    private WizardPropertyGroupContainer container;

    private ComponentTable tblComponents;

    private HashMap<String, ComponentValue> tblValue; // id , value
    private WizardNotificationManager observable;

    private final IWizardValidationBroadcastStrategy strategyValidationBroadcast;

    /**
     * Default constructor.
     */
    public ComponentValueTable(final IWizardValidationBroadcastStrategy strategyValidationBroadcast) {
        this.strategyValidationBroadcast = strategyValidationBroadcast;
        tblComponents = new ComponentTable();
        tblValue = new HashMap<String, ComponentValue>();
        observable = new WizardNotificationManager(tblComponents);
    }

    private void addValue(String id, String value) {
        tblValue.put(id, ComponentValue.create(value));
    }

    private void addValue(String id, JSONValue value) {
        tblValue.put(id, ComponentValue.create(value));
    }

    /**
     * Initialize our values from a property group container.
     * 
     * @param container initializing template.
     */
    public void seed(final WizardPropertyGroupContainer container) {
        tblValue.clear();
        this.container = container;

        if (container != null) {
            String id;

            name = container.getName();
            type = container.getType();
            desc = container.getDescription();
            templateId = container.getId();

            // loop through our groups to get at our properties
            List<PropertyGroup> groups = container.getGroups();

            for (PropertyGroup group : groups) {
                // loop through our properties to seed our table
                List<Property> properties = group.getProperties();

                for (Property property : properties) {
                    id = property.getId();

                    // does this property have an id?
                    if (!id.equals("")) {
                        addValue(property.getId(), property.getValue());
                    }
                }
            }

            registerNotifcations(container.getNotifications());
        }
    }

    public JSONObject getWizardPorpertyGroupContainerAsJson() {
        if (container != null) {
            String id;
            // loop through our groups to get at our properties
            List<PropertyGroup> groups = container.getGroups();

            for (PropertyGroup group : groups) {
                // loop through our properties to seed our table
                List<Property> properties = group.getProperties();

                for (Property property : properties) {
                    id = property.getId();

                    // does this property have an id?
                    if (!id.equals("")) {
                        property.setValue(getValue(id));
                    }
                }
            }
        }

        return container.toJson();

    }

    /**
     * @return the templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @return the outputFolderId
     */
    public String getOutputFolderId() {
        return (outputFolderId != null) ? outputFolderId : "";
    }

    /**
     * @param outputFolderId the outputFolderId to set
     */
    public void setOutputFolderId(String outputFolderId) {
        this.outputFolderId = outputFolderId;
    }

    /**
     * @return the createSubFolder
     */
    public boolean isCreateSubFolder() {
        return createSubFolder;
    }

    /**
     * @param createSubFolder the createSubFolder to set
     */
    public void setCreateSubFolder(boolean createSubFolder) {
        this.createSubFolder = createSubFolder;
        
    }

    private void registerNotifcations(final List<WizardNotification> notifications) {
        for (WizardNotification wz : notifications) {
            if (wz != null) {
                observable.addObserver(wz.getNotificationSender(), wz);
            }
        }
    }
    
    /**
     * @return the isNotifyEnabled
     */
    public boolean isNotifyEnabled() {
        return isNotifyEnabled;
    }

    /**
     * @param isNotifyEnabled the isNotifyEnabled to set
     */
    public void setNotifyEnabled(boolean isNotifyEnabled) {
        this.isNotifyEnabled = isNotifyEnabled;
    }

    /**
     * Retrieve number of values in our table.
     * 
     * @return number of stored values.
     */
    public int getNumValues() {
        return tblValue.size();
    }

    /**
     * Retrieve the string value for a given id. This will return null if the value does not exist.
     * 
     * @param id id of requested value.
     * @return requested value. Null on failure.
     */
    public String getValue(String id) {
        String ret = null; // assume failure
        ComponentValue val = tblValue.get(id);

        if (val != null) {
            ret = val.getValue();
        }

        return ret;
    }

    /**
     * Update a table value.
     * 
     * @param id id of value to set.
     * @param value new value to set.
     * @param exportable boolean to set if the value should be exported as json sent to analysis services
     */
    public void setValue(String id, String value, boolean exportable) {
        setValue(id, value);
        tblValue.get(id).setExportable(exportable);
    }

    /**
     * Update a table value.
     * 
     * @param id id of value to set.
     * @param value new value to set.
     */
    public void setValue(String id, String value) {
        if (tblValue.containsKey(id)) {
            tblValue.get(id).setValue(value);
        } else {
            addValue(id, value);
        }

        // must notify only this widget ?
        observable.notifyObserver(id);

    }

    public void setValue(String id, JSONValue value) {
        if (tblValue.containsKey(id)) {
            tblValue.get(id).setValue(value);
        } else {
            addValue(id, value);
        }

        // must notify only this widget ?
        observable.notifyObserver(id);
    }

    /**
     * Replace a value's validator.
     * 
     * @param id id of component value that stores this validator.
     * @param validator new validator.
     */
    public void setValidator(String id, IPlantValidator validator) {
        if (tblValue.containsKey(id)) {
            tblValue.get(id).setValidator(validator);
        }
    }

    /**
     * Remove a value's validator.
     * 
     * @param id id of component value whose validator requires removal
     */
    public void clearValidator(String id) {
        if (tblValue.containsKey(id)) {
            tblValue.get(id).clearValidator();
        }
    }

    /**
     * Replace a value's formatter.
     * 
     * @param id id of component value that stores this formatter.
     * @param formatter new formatter.
     */
    public void setFormatter(String id, ComponentValueExportFormatter formatter) {
        if (tblValue.containsKey(id)) {
            tblValue.get(id).setFormatter(formatter);
        }
    }

    /**
     * Set table ready for validation.
     */
    public void enableValidation() {
        isValidationEnabled = true;
    }

    /**
     * Prevent table validation.
     */
    public void disableValidation() {
        isValidationEnabled = false;
    }

    /**
     * Retrieve validation state.
     * 
     * @return validation flag.
     */
    public boolean isValidationEnabled() {
        return isValidationEnabled;
    }

    /**
     * Validate all internal values.
     * 
     * @param broadcast Whether to broadcast validation results.
     * @return List of validation error messages on failure.
     */
    public List<String> validate(boolean broadcast) {
        return validate(broadcast, true);
    }

    /**
     * Validate all internal values.
     * 
     * @param broadcast Whether to broadcast validation results.
     * @param uiValidation Whether UI components should trigger validation, marking themselves invalid.
     * @return List of validation error messages on failure.
     */
    public List<String> validate(boolean broadcast, boolean uiValidation) {
        List<String> ret = new ArrayList<String>();

        if (isValidationEnabled) {
            Collection<ComponentValue> collection = tblValue.values();

            for (ComponentValue val : collection) {
                String error = val.validate();
                if (error != null) {
                    ret.add(error);
                }
            }

           if (uiValidation) {
                // make sure our individual components are also validated.
                tblComponents.validate();
           }

            if (broadcast && strategyValidationBroadcast != null) {
                strategyValidationBroadcast.broadcast(ret);
            }
        }

        return ret;
    }

    /**
     * Validate and broadcast results.
     * 
     * @return List of validation error messages on failure.
     */
    public List<String> validate() {
        return validate(true);
    }

    /**
     * enforce contracts for all components
     */
    public void enforceContracts() {
        if (isValidationEnabled) {
            // notify observers that new value has been added
            observable.notifyObservers();
        }
    }

    /**
     * Sets our table name.
     * 
     * @param name new name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Retrieve table name.
     * 
     * @return table name.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve table type.
     * 
     * @return table type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set our table description.
     * 
     * @param description new description to set.
     */
    public void setDescription(final String description) {
        desc = (description == null) ? "" : description;
    }

    /**
     * Retrieve table description.
     * 
     * @return table description.
     */
    public String getDescription() {
        return desc;
    }

    /**
     * Retrieve table values.
     * 
     * @return map of table key/values
     */
    public Map<String, ComponentValue> getValues() {
        return tblValue;
    }

    /**
     * Adds a component to be tracked.
     * 
     * @param id id of new component.
     * @param component component to be tracked.
     */
    public void addComponent(String id, WizardWidgetPanel component) {
        tblComponents.add(id, component);
    }

    /**
     * Retrieves a stored component.
     * 
     * @param id id of requested component.
     * @return stored component.
     */
    public WizardWidgetPanel getComponent(final String id) {
        return tblComponents.get(id);
    }

    /**
     * Provide access to our components.
     * 
     * @return component table.
     */
    public ComponentTable getComponents() {
        return tblComponents;
    }

    /**
     * @param isDebugEnabled the isDebugEnabled to set
     */
    public void setDebugEnabled(boolean isDebugEnabled) {
        this.isDebugEnabled = isDebugEnabled;
    }

    /**
     * @return the isDebugEnabled
     */
    public boolean isDebugEnabled() {
        return isDebugEnabled;
    }

}
