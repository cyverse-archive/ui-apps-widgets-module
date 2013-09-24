package org.iplantc.core.uiapps.widgets.client.services.impl.converters;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoTypeList;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class FileInfoTypeCallbackConverter extends AsyncCallbackConverter<String, List<FileInfoType>> {

    private final AppTemplateAutoBeanFactory factory;
    private final List<FileInfoType> fileInfoTypeList;

    public FileInfoTypeCallbackConverter(final List<FileInfoType> fileInfoTypeList, final AppTemplateAutoBeanFactory factory, final AsyncCallback<List<FileInfoType>> callback) {
        super(callback);
        this.factory = factory;
        this.fileInfoTypeList = fileInfoTypeList;
    }

    @Override
    protected List<FileInfoType> convertFrom(String object) {
        Splittable split = StringQuoter.split(object);

        FileInfoTypeList fitListWrapper = AutoBeanCodex.decode(factory, FileInfoTypeList.class, split).as();

        fileInfoTypeList.clear();
        fileInfoTypeList.addAll(fitListWrapper.getFileInfoTypes());
        return fileInfoTypeList;
    }

}
