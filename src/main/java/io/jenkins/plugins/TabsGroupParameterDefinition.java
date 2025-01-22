package io.jenkins.plugins;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
import hudson.model.SimpleParameterDefinition;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TabsGroupParameterDefinition extends SimpleParameterDefinition {

    private final List<TabParametersDefinition> tabs;

    @DataBoundConstructor
    public TabsGroupParameterDefinition(String name, List<TabParametersDefinition> tabs) {
        super(name);
        this.tabs = tabs;
    }

    @Override
    public TabsGroupParameterValue createValue(StaplerRequest2 req, JSONObject jo) {
        String name = jo.getString("name");
        var groupParameterValue = new TabsGroupParameterValue(name, new ArrayList<>());

        JSONArray tabsValues = jo.getJSONArray("tabsValues");
        var parametersValues = new ArrayList<ParameterValue>();

        tabsValues.forEach(tab -> {
            var tabJSONObject = JSONObject.fromObject(tab);
            var tabName = tabJSONObject.getString("name");

            Iterable<Object> parameters = toIterable(tabJSONObject.get("parameter"));
            parameters.forEach(parameter -> {
                JSONObject jsonParameter = JSONObject.fromObject(parameter);
                var parameterName = jsonParameter.getString("name");
                var paramDefinition = getParamDefinitionFromTab(parameterName);
                parametersValues.add(paramDefinition.createValue(req, jsonParameter));
            });
            groupParameterValue.getValue().add(new TabParametersValue(tabName, parametersValues));
        });

        return groupParameterValue;
    }

    private Iterable<Object> toIterable(Object maybeIterable) {
        if (maybeIterable instanceof JSONArray jsonArray) {
            return jsonArray;
        } else {
            return Collections.singletonList(maybeIterable);
        }
    }

    private ParameterDefinition getParamDefinitionFromTab(String name) {
        for (TabParametersDefinition tab : tabs) {
            for (ParameterDefinition parameter : tab.getParameters()) {
                if (parameter.getName().equals(name)) {
                    return parameter;
                }
            }
        }
        return null;
    }

    @Override
    public TabsGroupParameterValue createValue(String value) {
        //TODO
        return null;
    }

    public List<TabParametersDefinition> getTabs() {
        return tabs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TabsGroupParameterDefinition that = (TabsGroupParameterDefinition) o;
        return Objects.equals(tabs, that.tabs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tabs);
    }

    @Extension
    @Symbol("groupParam")
    public static final class ParameterDescriptorImpl extends ParameterDescriptor {
        @NonNull
        @Override
        public String getDisplayName() {
            return "Group Parameter";
        }
    }
}
