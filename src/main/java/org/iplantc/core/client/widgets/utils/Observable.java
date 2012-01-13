package org.iplantc.core.client.widgets.utils;

/**
 * 
 * @author sriram
 * 
 */
public interface Observable {

    void addObserver(String id, Observer o);

    void deleteObserver(String id);

    void deleteObservers();

    void notifyObserver(String id);

    void notifyObservers();

    int getObserversCount();
}
