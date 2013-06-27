package org.iplantc.core.uiapps.widgets.client.models;

import org.iplantc.core.uiapps.widgets.client.models.metadata.DataObject;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSource;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSourceList;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoTypeList;
import org.iplantc.core.uiapps.widgets.client.models.metadata.JobExecution;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface AppTemplateAutoBeanFactory extends AutoBeanFactory {
    
    AutoBean<AppTemplate> appTemplate();
    
    AutoBean<ArgumentGroup> argumentGroup();
    
    AutoBean<Argument> argument();

    AutoBean<ArgumentValidator> argumentValidator();

    AutoBean<SelectionItem> selectionItem();

    AutoBean<SelectionItemGroup> selectionItemGroup();

    AutoBean<DataObject> dataObject();

    AutoBean<JobExecution> jobExecution();

    AutoBean<FileInfoType> fileInfoType();

    AutoBean<FileInfoTypeList> fileInfoTypeList();

    AutoBean<DataSource> dataSource();

    AutoBean<DataSourceList> dataSourceList();

}
