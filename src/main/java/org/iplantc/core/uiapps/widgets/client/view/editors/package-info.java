/**
 * This package contains {@link com.google.gwt.editor.client.Editor}s which can be used to edit
 * {@link AppTemplate} instances.
 * 
 * <h1>Editors</h1>
 * <dl>
 * <dt>{@link org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentGroupListEditor}</dt>
 * <dd>- bound to {@link org.iplantc.core.uiapps.widgets.client.models.AppTemplate#getArgumentGroups()}</dd>
 * 
 * <dt>{@link org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentGroupEditor}</dt>
 * <dd>- bound to individual <code>ArgumentGroup</code>s in
 * {@link org.iplantc.core.uiapps.widgets.client.models.AppTemplate#getArgumentGroups()}</dd>
 * 
 * <dt>{@link org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentListEditor}</dt>
 * <dd>- bound to {@link org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup#getArguments()}</dd>
 * 
 * <dt>{@link org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentEditor}</dt>
 * <dd>- bound to inidividual <code>Arguments</code>s in
 * {@link org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup#getArguments()}</dd>
 * 
 * <dt>{@link org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentPropertyEditor}</dt>
 * <dd>- bound to {@link org.iplantc.core.uiapps.widgets.client.models.Argument}</dd>
 * 
 * <dt>{@link org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentValueEditor}</dt>
 * <dd>- bound to {@link org.iplantc.core.uiapps.widgets.client.models.Argument#getValue()}</dd>
 * 
 * </dl>
 * 
 * 
 * FIXME JDS Needs to be updated.
 * 
 * @author jstroot
 * 
 */
package org.iplantc.core.uiapps.widgets.client.view.editors;