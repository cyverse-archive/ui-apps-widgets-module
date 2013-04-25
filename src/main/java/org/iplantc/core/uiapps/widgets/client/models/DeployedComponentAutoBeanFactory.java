/**
 * 
 */
package org.iplantc.core.uiapps.widgets.client.models;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author sriram
 *
 */
public interface DeployedComponentAutoBeanFactory extends AutoBeanFactory {

    AutoBean<DeployedComponent> getDeployedComponent();

    AutoBean<DeployedComponentList> getDeployedComponentList();
}
