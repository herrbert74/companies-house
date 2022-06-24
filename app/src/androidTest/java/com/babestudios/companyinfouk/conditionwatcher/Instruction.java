package com.babestudios.companyinfouk.conditionwatcher;

import android.os.Bundle;

/**
 * Copied from
 * https://github.com/AzimoLabs/ConditionWatcher
 */
public abstract class Instruction {

    private Bundle dataContainer = new Bundle();

    public final void setData(Bundle dataContainer) {
        this.dataContainer = dataContainer;
    }

    public final Bundle getDataContainer() {
        return dataContainer;
    }

    public abstract String getDescription();

    public abstract boolean checkCondition();
}
