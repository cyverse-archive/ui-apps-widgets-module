package org.iplantc.de.apps.widgets.client.models.metadata;

import org.iplantc.de.commons.client.models.HasDescription;
import org.iplantc.de.commons.client.models.HasId;
import org.iplantc.de.commons.client.models.HasLabel;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface FileInfoType extends HasDescription, HasId, HasLabel {

    @PropertyName("name")
    FileInfoTypeEnum getType();

    String getHid();

}
