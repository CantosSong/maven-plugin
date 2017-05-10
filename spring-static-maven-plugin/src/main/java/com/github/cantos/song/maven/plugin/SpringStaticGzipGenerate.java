package com.github.cantos.song.maven.plugin;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;
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
			String[] paths = null;
			List<String> suffixs = null;
			List<String> filterSuffixs = null;
			System.out.println("path is "+path);
			if(!StringUtils.isEmpty(path)){
				paths = path.split(",");
			}
			System.out.println("suffix is "+suffix);
			if(!StringUtils.isEmpty(suffix)){
				suffixs = Arrays.asList(suffix.split(","));
			}
			System.out.println("filter suffix is "+filterSuffix);
			if(!StringUtils.isEmpty(filterSuffix)){
				filterSuffixs = Arrays.asList(filterSuffix.split(","));
			}
			for(String pathTemp:paths){
				File directory = new File(pathTemp);
				directory=new File(directory.getCanonicalPath());
				if(directory.isDirectory()){
					for(File file : directory.listFiles()){
				        String fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));
						//过滤指定类型以外的
				        if(suffixs!=null&&!suffixs.contains(fileSuffix)){
				        	continue;
				        }
						//过滤指定文件
				        if(filterSuffixs!=null&&filterSuffixs.contains(fileSuffix)){
				        	continue;
				        }
						BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
						String filename = file.getCanonicalPath()+".gz";
				        BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(filename)));
				        System.out.println("create file "+filename);
				        int c;
				        while ((c = in.read()) != -1) {
				            out.write(String.valueOf((char) c).getBytes("UTF-8"));
				        }
				        in.close();
				        out.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
