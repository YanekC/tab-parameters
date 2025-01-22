package io.jenkins.plugins;

import hudson.model.ParameterValue;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;
import java.util.List;

public class TabParametersValue implements Serializable {
    private final String name;
    private final List<ParameterValue> parameters;

    @DataBoundConstructor
    public TabParametersValue(String name, List<ParameterValue> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "TabParametersValue{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
