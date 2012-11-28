package org.iplantc.core.client.widgets.images;

import com.google.gwt.resources.client.ImageResource;

/**
 * Provides access to bundled image resources.
 */
public interface Icons extends org.iplantc.core.uicommons.client.images.Icons {

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Override
    @Source("new.gif")
    ImageResource add();

    /**
     * Image resource.
     * 
     * @return image.
     */
    @Source("delete.gif")
    ImageResource delete();

}
