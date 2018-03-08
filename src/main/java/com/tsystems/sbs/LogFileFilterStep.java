package com.tsystems.sbs;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.workflow.steps.AbstractStepExecutionImpl;
import org.jenkinsci.plugins.workflow.steps.BodyExecutionCallback;
import org.jenkinsci.plugins.workflow.steps.BodyInvoker;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.console.ConsoleLogFilter;

/**
 * Custom pipeline step that can be used without a node and build wrapper.
 */
public class LogFileFilterStep extends Step {

	@DataBoundConstructor
    public LogFileFilterStep() {}
	
	@Override
	public StepExecution start(StepContext context) throws Exception {
		return new Execution(context);
	}

    /**
     * Execution for {@link LogFileFilterStep}.
     */
    public static class Execution extends AbstractStepExecutionImpl {

        private static final long serialVersionUID = 1L;
        
        protected Execution(StepContext context) {
        	super(context);
        }
        
        @Override
        public void onResume() {}
        
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean start() throws Exception {
            StepContext context = getContext();
            context.newBodyInvoker().
            	withContext(createConsoleLogFilter(context)).
            	withCallback(BodyExecutionCallback.wrap(context)).
            	start();
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
    public static class StepDescriptorImpl extends StepDescriptor {

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

		@Override
		public Set<? extends Class<?>> getRequiredContext() {
			return new HashSet<Class<?>>();
		}

    }

}
