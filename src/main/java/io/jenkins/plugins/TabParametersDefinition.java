package io.jenkins.plugins;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.ParameterDefinition;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class TabParametersDefinition implements Describable<TabParametersDefinition>, Serializable {

    private final String name;
    private final List<ParameterDefinition> parameters;

    @DataBoundConstructor
    public TabParametersDefinition(String name, List<ParameterDefinition> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public List<ParameterDefinition> getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    public String getAllNames() {
        return parameters.stream().map(ParameterDefinition::getName).collect(Collectors.joining());
    }

    @Override
    @NonNull
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) Jenkins.get().getDescriptorOrDie(getClass());
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<TabParametersDefinition> {
        @NonNull
        @Override
        public String getDisplayName() {
            return "Parameters Tab";
        }
    }

}
