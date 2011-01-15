package net.video.trimmer.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
	public static String getTargetFileName(String inputFileName) {
		final File file = new File(inputFileName).getAbsoluteFile();
		final String fileName = file.getName();
		
		String[] filenames = file.getParentFile().list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename != null && filename.startsWith("trimmed-") && filename.endsWith(fileName);
			}
		});
		
		int count = 0;
		String targetFileName;
		List<String> fileList = Arrays.asList(filenames);
		
		do {
			targetFileName = "trimmed-" + String.format("%03d", count++)+ "-" + fileName; 
		} while(fileList.contains(targetFileName));
		
		return new File(file.getParent(), targetFileName).getPath();
	}

}
