package com.github.cantos.song.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generate")
public class SpringStaticGzipGenerate extends AbstractMojo {

	@Parameter(defaultValue = "")
	private String path;

	@Parameter(defaultValue = ".gz")
	private String filterSuffix;

	@Parameter(defaultValue = "")
	private String suffix;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			GzipUtils.gzip(path, suffix, filterSuffix);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
