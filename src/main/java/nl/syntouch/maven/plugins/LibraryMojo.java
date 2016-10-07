package nl.syntouch.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;


@Mojo(name = "library", defaultPhase = LifecyclePhase.PACKAGE)
public class LibraryMojo extends AbstractTibcoMojo {

    @Parameter(required = true, alias = "lib")
    private String lib;

    public void execute() throws MojoExecutionException, MojoFailureException {
        String workingDirectory = getDesignerHome() + "/bin";
        File artifact = new File(getMavenProject().getBuild().getDirectory() + "/" + getMavenProject().getArtifactId() + ".projlib");

        Commandline cli = new Commandline();
        cli.setWorkingDirectory(workingDirectory);
        cli.setExecutable(getExecutable("buildlibrary"));
        cli.createArg().setValue("-x");
        cli.createArg().setValue("-p");
        cli.createArg().setValue(getProjectDirectory().toString());
        cli.createArg().setValue("-lib");
        cli.createArg().setValue(getTibcoArtifactUri(lib));
        cli.createArg().setValue("-o");
        cli.createArg().setValue(artifact.toString());

        CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
        CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

        try {
            CommandLineUtils.executeCommandLine(cli, stdout, stderr);

            if (stderr.getOutput().length() > 0) {
                throw new MojoExecutionException(stderr.getOutput());
            }

            getLog().debug(stdout.getOutput());

        } catch (CommandLineException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        getMavenProject().getArtifact().setFile(artifact);
    }
}
