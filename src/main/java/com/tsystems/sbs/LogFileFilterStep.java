package com.tsystems.sbs;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepExecutionImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.jenkinsci.plugins.workflow.steps.BodyExecutionCallback;
import org.jenkinsci.plugins.workflow.steps.BodyInvoker;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.kohsuke.stapler.DataBoundConstructor;

import com.google.inject.Inject;

import hudson.Extension;
import hudson.console.ConsoleLogFilter;

/**
 * Custom pipeline step that can be used without a node and build wrapper.
 */
public class LogFileFilterStep extends AbstractStepImpl {

	@DataBoundConstructor
    public LogFileFilterStep() {}
	
    /**
     * Execution for {@link LogFileFilterStep}.
     */
    public static class ExecutionImpl extends AbstractStepExecutionImpl {

        private static final long serialVersionUID = 1L;
        
        @Inject(optional = true)
        private transient LogFileFilterStep step;

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean start() throws Exception {
            StepContext context = getContext();
            context.newBodyInvoker().withContext(createConsoleLogFilter(context))
                    .withCallback(BodyExecutionCallback.wrap(context)).start();
            return false;
        }

        private ConsoleLogFilter createConsoleLogFilter(StepContext context)
                throws IOException, InterruptedException {
            ConsoleLogFilter original = context.get(ConsoleLogFilter.class);
            ConsoleLogFilter subsequent = new LogFileFilterConsoleLogFilter();
            return BodyInvoker.mergeConsoleLogFilters(original, subsequent);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void stop(@Nonnull Throwable cause) throws Exception {
            getContext().onFailure(cause);
        }
    }
    
    /**
     * Descriptor for {@link LogFileFilterStep}.
     */
    @Extension(optional = true)
    public static class StepDescriptorImpl extends AbstractStepDescriptorImpl {

        public StepDescriptorImpl() {
            super(ExecutionImpl.class);
        }

        @Override
        public String getDisplayName() {
            return "LogFileFilterStep";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getFunctionName() {
            return "logFileFilter";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean takesImplicitBlockArgument() {
            return true;
        }

    }

}
