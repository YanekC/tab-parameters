package io.jenkins.plugins;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.ParameterDefinition;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.Serializable;
import java.util.List;

/**
 * Describe a tab and parameters contained inside
 */
public class TabParametersDefinition implements Describable<TabParametersDefinition>, Serializable {

    /**
     * Name of the tab
     */
    private final String name;
    /**
     * All parameters inside the tab
     */
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
            return "Tab parameters";
        }

        public FormValidation doCheckParameters(@QueryParameter String parameters) {
            if (parameters.isEmpty())
                return FormValidation.error(Messages.TabParametersDefinition_DescriptorImpl_ParametersEmpty());
            else return FormValidation.ok();
        }
    }

}
