package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.ListRuleArgument;

import com.extjs.gxt.ui.client.widget.Label;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;

/**
 * A WizardSelectorPanel for displaying a hierarchical list selector widget in a wizard.
 * 
 * @author psarando
 * 
 */
public class WizardTreeSelectorPanel extends WizardSelectorPanel {

    private ListRuleArgumentTreePanel treePanel;
    private Label caption;

    /**
     * Instantiate from a property, component value table.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     */
    public WizardTreeSelectorPanel(Property property, ComponentValueTable tblComponentVals) {
        super(property, tblComponentVals);
    }

    @Override
    protected void initInstanceVariables(Property property, List<String> params) {
        treePanel = new ListRuleArgumentTreePanel();

        treePanel.addCheckChangedHandler(new CheckChangedHandler<ListRuleArgument>() {
            @Override
            public void onCheckChanged(CheckChangedEvent<ListRuleArgument> event) {
                updateComponentValueTable();
            }
        });
    }

    @Override
    protected void setWidgetId(String id) {
        treePanel.setId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        tblComponentVals.setValidator(property.getId(), buildValidator(property));
    }

    @Override
    protected void setInitialState(final Property property) {
        caption = buildAsteriskLabel(property.getLabel());
        setToolTip(caption, property);

        List<String> items = getMustContainRuleItems(property);

        if (items != null && !items.isEmpty()) {
            treePanel.setItems(items.get(0));
        }
    }

    @Override
    protected void updateComponentValueTable() {
        List<ListRuleArgument> selected = treePanel.getSelection();
        JSONArray selectedValues = null;

        if (selected != null && !selected.isEmpty()) {
            selectedValues = new JSONArray();
            int jsonArrayIndex = 0;

            for (ListRuleArgument ruleArg : selected) {
                if (ruleArg.getName() != null && !ruleArg.getName().isEmpty()) {
                    AutoBean<ListRuleArgument> bean = AutoBeanUtils.getAutoBean(ruleArg);
                    String argJson = AutoBeanCodex.encode(bean).getPayload();
                    selectedValues.set(jsonArrayIndex++, JSONParser.parseStrict(argJson));
                }
            }
        }

        tblComponentVals.setValue(treePanel.getId(), selectedValues);
        tblComponentVals.validate();
    }

    @Override
    protected void compose() {
        add(caption);
        add(treePanel);
    }

    @Override
    protected void setValue(String value) {
        // not supported at this time
        // default values are set in setInitialState
    }
}
