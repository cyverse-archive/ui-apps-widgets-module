package org.iplantc.core.uiapps.widgets.client.services.impl.converters;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenome;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenomeList;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class ReferenceGenomeCallbackConverter extends AsyncCallbackConverter<String, List<ReferenceGenome>> {

    private final AppTemplateAutoBeanFactory factory;
    private final List<ReferenceGenome> referenceGenomeList;

    public ReferenceGenomeCallbackConverter(final List<ReferenceGenome> referenceGenomeList, final AppTemplateAutoBeanFactory factory, AsyncCallback<List<ReferenceGenome>> callback) {
        super(callback);
        this.factory = factory;
        this.referenceGenomeList = referenceGenomeList;
    }

    @Override
    protected List<ReferenceGenome> convertFrom(String object) {
        Splittable split = StringQuoter.split(object);

        ReferenceGenomeList rgList = AutoBeanCodex.decode(factory, ReferenceGenomeList.class, split).as();
        referenceGenomeList.clear();
        referenceGenomeList.addAll(rgList.getReferenceGenomes());
        return referenceGenomeList;
    }

}
