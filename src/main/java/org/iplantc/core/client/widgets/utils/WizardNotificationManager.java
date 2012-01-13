package org.iplantc.core.client.widgets.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * This class that implements publisher/subscriber model for ComponentValueTable. Any widget that needs
 * to be notified about changes/updates in any other widget will have to register. Implements Observable
 * interface
 * 
 * @author sriram
 * 
 */
public class WizardNotificationManager implements Observable {

    private HashMap<String, Observer> observers;
    private ComponentTable tblComponents;

    /**
     * Creates a new WizardNotificationManager.
     * 
     * @param tblComponents
     */
    public WizardNotificationManager(ComponentTable tblComponents) {
        observers = new HashMap<String, Observer>();
        this.tblComponents = tblComponents;
    }

    @Override
    public void addObserver(String id, Observer o) {
        observers.put(id, o);

    }

    @Override
    public void deleteObserver(String id) {
        observers.remove(id);

    }

    @Override
    public void deleteObservers() {
        observers.clear();
    }

    @Override
    public void notifyObserver(String id) {
        Observer o = observers.get(id);
        if (o != null) {
            o.execute(tblComponents);
        }
    }

    @Override
    public void notifyObservers() {
        Set<String> keys = observers.keySet();
        Iterator<String> i = keys.iterator();
        while (i.hasNext()) {
            observers.get(i.next()).execute(tblComponents);
        }
    }

    @Override
    public int getObserversCount() {
        return observers.size();
    }
}
