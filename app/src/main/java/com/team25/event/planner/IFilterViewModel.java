package com.team25.event.planner;

import android.content.Context;
import android.view.View;

public interface IFilterViewModel {
    abstract public void setFilter(Context context, View view);
    abstract public void showFilter();
}
