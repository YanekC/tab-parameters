package io.jenkins.plugins;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
import hudson.model.SimpleParameterDefinition;
import hudson.util.FormValidation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entrypoint of tab parameters
 * Define a list of tabs {@link TabParametersDefinition} the contains other parameters
 * Parameters can be any Jenkins {@link ParameterDefinition}
 * <p>
 * The method {@link TabsGroupParameterDefinition#createValue(String)} make the work of generating values of parameters inside the tabs
 */
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
            //TabsGroupParameterValue#getValue Cannot be null
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
        throw new IllegalArgumentException("Cannot find parameter definition " + name + " in " + this.getName() + " tab definition");
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
            return Messages.TabsGroupParameterDefinition_DisplayName();
        }

        public FormValidation doCheckName(@QueryParameter String name) {
            if (name.isEmpty())
                return FormValidation.error(Messages.TabsGroupParameterDefinition_ParameterDescriptorImpl_NameEmpty());
            else return FormValidation.ok();
        }

        public FormValidation doCheckTabs(@QueryParameter String tabs) {
            if (tabs.isEmpty())
                return FormValidation.error(Messages.TabsGroupParameterDefinition_ParameterDescriptorImpl_TabsEmpty());
            else return FormValidation.ok();
        }
    }
}
