/**
 * 
 */
package org.iplantc.core.uiapps.widgets.client.models;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author sriram
 *
 */
public interface DCProperties extends PropertyAccess<DeployedComponent> {

    ValueProvider<DeployedComponent, String> name();

    ValueProvider<DeployedComponent, String> version();

    ValueProvider<DeployedComponent, String> location();

}
