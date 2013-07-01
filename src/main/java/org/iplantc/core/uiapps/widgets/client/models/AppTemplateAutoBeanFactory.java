package org.iplantc.core.uiapps.widgets.client.models;

import org.iplantc.core.uiapps.widgets.client.models.metadata.DataObject;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSource;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSourceList;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoTypeList;
import org.iplantc.core.uiapps.widgets.client.models.metadata.JobExecution;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenome;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenomeList;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface AppTemplateAutoBeanFactory extends AutoBeanFactory {
    
    AutoBean<AppTemplate> appTemplate();
    
    AutoBean<ArgumentGroup> argumentGroup();
    
    AutoBean<Argument> argument();

    AutoBean<ArgumentValidator> argumentValidator();

    AutoBean<SelectionItem> selectionItem();

    AutoBean<SelectionItemList> selectionItemList();

    AutoBean<SelectionItemGroup> selectionItemGroup();

    AutoBean<DataObject> dataObject();

    AutoBean<JobExecution> jobExecution();

    AutoBean<FileInfoType> fileInfoType();

    AutoBean<FileInfoTypeList> fileInfoTypeList();

    AutoBean<DataSource> dataSource();

    AutoBean<DataSourceList> dataSourceList();

    AutoBean<ReferenceGenome> referenceGenome();

    AutoBean<ReferenceGenomeList> referenceGenomeList();

}
