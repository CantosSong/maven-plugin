package com.github.cantos.song.maven.plugin;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

public class GzipUtils {
	static Log log = new SystemStreamLog();

	public static void gzip(String path, String suffix, String filterSuffix) throws IOException {
		String[] paths = null;
		List<String> suffixs = null;
		List<String> filterSuffixs = null;
		log.info("path is " + path);
		if (!StringUtils.isEmpty(path)) {
			paths = path.split(",");
		}
		log.info("suffix is " + suffix);
		if (!StringUtils.isEmpty(suffix)) {
			suffixs = Arrays.asList(suffix.split(","));
		}
		log.info("filter suffix is " + filterSuffix);
		if (!StringUtils.isEmpty(filterSuffix)) {
			filterSuffixs = Arrays.asList(filterSuffix.split(","));
		}
		for (String pathTemp : paths) {
			File directory = new File(pathTemp);
			directory = new File(directory.getCanonicalPath());
			checkFile(directory, suffixs, filterSuffixs);
		}
	}

	public static void checkFile(File directory, List<String> suffixs, List<String> filterSuffixs) throws IOException {
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				if(file.isDirectory()){
					checkFile(file, suffixs, filterSuffixs);
				}
				log.info(file.getName());
				if(file.getName().lastIndexOf(".")==-1){
					continue;
				}
				String fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));
				// 过滤指定类型以外的
				if (suffixs != null && !suffixs.contains(fileSuffix)) {
					continue;
				}
				// 过滤指定文件
				if (filterSuffixs != null && filterSuffixs.contains(fileSuffix)) {
					continue;
				}
				createFile(file);
			}
		}
	}

	public static void createFile(File file) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String filename = file.getCanonicalPath() + ".gz";
		BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(filename)));
		log.info("create file " + filename);
		int c;
		while ((c = in.read()) != -1) {
			out.write(String.valueOf((char) c).getBytes("UTF-8"));
		}
		in.close();
		out.close();
	}
}
