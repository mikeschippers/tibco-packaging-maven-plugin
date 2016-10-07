package nl.syntouch.maven.plugins;

import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

public abstract class AbstractTibcoMojo extends AbstractMojo {

    @Component
    private MavenProject mavenProject;

    protected MavenProject getMavenProject() {
        return mavenProject;
    }

    @Parameter(defaultValue = "${localRepository}", required = true, readonly = true)
    protected ArtifactRepository localRepository;

    @Parameter(defaultValue = "${project.basedir}/src/bw", required = true, alias = "projectDirectory")
    private File projectDirectory;

    protected File getProjectDirectory() {
        return projectDirectory;
    }

    @Parameter(defaultValue = "${project.artifactId}.aliaslib", required = false, alias = "aliasLibrary")
    private File aliasLibrary;

    protected File getAliasLibrary() {
        return aliasLibrary;
    }

    @Parameter(property = "designerHome", readonly = true)
    private String designerHome;

    protected String getDesignerHome() {
        return designerHome;
    }

    @Parameter(property = "traHome", readonly = true)
    private String traHome;

    protected String getTraHome() {
        return traHome;
    }

    @Parameter(defaultValue = "true", required = false, alias = "validate")
    private Boolean validate;

    protected Boolean getValidate() {
        return validate;
    }


    @Parameter(required = false, alias = "designerPrefs")
    private File designerPrefs;

    protected File getDesignerPrefs() {
        return designerPrefs;
    }


    protected String getArtifactPath(Artifact artifact) {
        String artifactPath;
        if (artifact.getFile() != null) {
            artifactPath = artifact.getFile().getAbsolutePath();
        } else {
            artifactPath = localRepository.getBasedir() + "\\" + localRepository.pathOf(artifact);
        }

        if (SystemUtils.IS_OS_WINDOWS) {
            artifactPath = artifactPath.replace("\\", "\\\\");
            artifactPath = artifactPath.replace("/", "\\");
            artifactPath = artifactPath.replace(":", "\\:");
        } else {
            artifactPath = artifactPath.replace("\\", "/");
        }
        return artifactPath;
    }

    protected String getFileAlias(Artifact artifact, Integer index) {
        StringBuffer fileAlias = new StringBuffer("filealias.pref.");
        fileAlias.append(index);
        fileAlias.append("=");
        fileAlias.append(artifact.getArtifactId());
        fileAlias.append("-");
        fileAlias.append(artifact.getVersion());
        fileAlias.append(".");
        fileAlias.append(artifact.getType());
        fileAlias.append("\\=");
        fileAlias.append(getArtifactPath(artifact));

        return fileAlias.toString();
    }

    protected String getExecutable(String executable) {
        if (SystemUtils.IS_OS_LINUX) {
            executable = "./" + executable;
        }
        return executable;
    }

    protected String getTibcoArtifactUri(String uri) {
        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }
        return uri;
    }
}
