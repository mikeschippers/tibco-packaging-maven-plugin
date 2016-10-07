package nl.syntouch.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

@Mojo(name = "validate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ValidateMojo extends AbstractTibcoMojo {

    public void execute() throws MojoExecutionException {
        if (getValidate() == true) {
            getLog().info("Validating project " + getProjectDirectory());

            String workingDirectory = getDesignerHome() + "/bin";

            Commandline cli = new Commandline();
            cli.setWorkingDirectory(workingDirectory);
            cli.setExecutable(getExecutable("validateproject"));
            cli.createArg().setFile(getProjectDirectory());

            CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
            CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

            try {
                CommandLineUtils.executeCommandLine(cli, stdout, stderr);
                if (stderr.getOutput().length() > 0) {
                    throw new MojoExecutionException(stderr.getOutput());
                }

            } catch (CommandLineException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        } else {
            getLog().info("Skipping validation");
        }
    }
}
