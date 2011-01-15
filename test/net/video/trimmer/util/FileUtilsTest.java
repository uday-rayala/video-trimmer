package net.video.trimmer.util;

import java.io.File;
import java.io.IOException;

import android.test.AndroidTestCase;


public class FileUtilsTest extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetTargetFileName() {
		assertEquals("/sdcard/trimmed-000-random.file", FileUtils.getTargetFileName("/sdcard/random.file"));
		assertEquals("/trimmed-000-random.file", FileUtils.getTargetFileName("random.file"));
	}

	public void testGetTargetFileNameAssignsANewNameForSubsequentTriummings() throws IOException {
		assertEquals("/sdcard/trimmed-000-random.file", FileUtils.getTargetFileName("/sdcard/random.file"));
		File file = new File("/sdcard/trimmed-000-random.file");
		try {
			file.createNewFile();
			assertEquals("/sdcard/trimmed-001-random.file", FileUtils.getTargetFileName("/sdcard/random.file"));
		} finally {
			file.delete();
		}
	}

}
