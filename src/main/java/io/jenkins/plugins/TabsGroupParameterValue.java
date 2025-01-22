package io.jenkins.plugins;

import hudson.model.ParameterValue;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;
import java.util.Objects;

public class TabsGroupParameterValue extends ParameterValue {

    //TODO how to handle which tab was selected ?

    private final List<TabParametersValue> tabsValues;

    @DataBoundConstructor
    public TabsGroupParameterValue(String name, List<TabParametersValue> tabsValues) {
        super(name);
        this.tabsValues = tabsValues;
    }

    @Override
    public List<TabParametersValue> getValue() {
        return tabsValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TabsGroupParameterValue that = (TabsGroupParameterValue) o;
        return Objects.equals(tabsValues, that.tabsValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tabsValues);
    }
}
