package org.iplantc.de.apps.widgets.client.models.metadata;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

public interface ReferenceGenomeList {

    @PropertyName("genomes")
    List<ReferenceGenome> getReferenceGenomes();
}
