package nl.syntouch.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

@Mojo(name = "ear", defaultPhase = LifecyclePhase.PACKAGE)
public class EarMojo extends AbstractTibcoMojo {

    @Parameter(required = false, alias = "archive")
    private String archive;


    public void execute() throws MojoExecutionException, MojoFailureException {
        String workingDirectory = getTraHome() + "/bin";
        File artifact = new File(getMavenProject().getBuild().getDirectory(), getMavenProject().getArtifactId() + ".ear");

        getLog().info("Creating Enterprise Archive File " + artifact.getAbsolutePath());

        Commandline cli = new Commandline();
        cli.setWorkingDirectory(workingDirectory);
        cli.setExecutable(getExecutable("buildear"));
        cli.createArg().setValue("-x");
        cli.createArg().setValue("-p");
        cli.createArg().setValue(getProjectDirectory().toString());
        cli.createArg().setValue("-o");
        cli.createArg().setValue(artifact.toString());

        if (archive != null) {
            cli.createArg().setValue("-ear");
            cli.createArg().setValue(getTibcoArtifactUri(archive));
            updateArchiveVersion(getProjectDirectory() + getTibcoArtifactUri(archive));
        }

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
        getMavenProject().getArtifact().setFile(artifact);
    }

    private void updateArchiveVersion(String location) throws MojoExecutionException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(location);
            NodeList list = doc.getElementsByTagName("versionProperty");

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                node.setTextContent(getMavenProject().getVersion());
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(location));
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }
}
