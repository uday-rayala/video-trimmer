package net.video.trimmer.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
	public static String getTargetFileName(String inputFileName) {
		final File file = new File(inputFileName).getAbsoluteFile();
		String[] filenames = file.getParentFile().list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename != null && filename.startsWith("trimmed-") && filename.endsWith(file.getName());
			}
		});
		
		int count = 0;
		String targetFileName;
		List<String> fileList = Arrays.asList(filenames);
		for (;;){
			String name = "trimmed-" + String.format("%03d", count++)+ "-" + file.getName();
			if (!fileList.contains(name)) {targetFileName = name; break;}
		}
		String dir = file.getParent();
		return ("/".equals(dir)? dir : dir + File.separator) +  targetFileName;
	}

}
