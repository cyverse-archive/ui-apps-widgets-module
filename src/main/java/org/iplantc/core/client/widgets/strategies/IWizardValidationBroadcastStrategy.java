package org.iplantc.core.client.widgets.strategies;

import java.util.List;

public interface IWizardValidationBroadcastStrategy {
    public abstract void broadcast(List<String> errors);
}
