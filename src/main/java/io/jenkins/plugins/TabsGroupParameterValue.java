package io.jenkins.plugins;

import hudson.model.ParameterValue;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;
import java.util.Objects;

public class TabsGroupParameterValue extends ParameterValue {

    private final List<TabParametersValue> tabsValues;

    private final String selectedTab;

    @DataBoundConstructor
    public TabsGroupParameterValue(String name, List<TabParametersValue> tabsValues, String selectedTab) {
        super(name);
        this.tabsValues = tabsValues;
        this.selectedTab = selectedTab;
    }

    @Override
    public List<TabParametersValue> getValue() {
        return getTabsValues();
    }

    public List<TabParametersValue> getTabsValues() {
        return tabsValues;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public String getTabButtonId(TabParametersValue tab) {
        if (tab.getName().equals(selectedTab)) {
            return "selected";
        }
        return "not-selected";
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
