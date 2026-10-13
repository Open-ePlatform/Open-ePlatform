package com.nordicpeak.flowengine.pdf.schedule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import se.unlogic.standardutils.io.StartsEndsWithFileFilter;
import se.unlogic.standardutils.time.MillisecondTimeUnits;

public class PDFTemporaryFileDeleter implements Runnable {

	private static final StartsEndsWithFileFilter FILE_FILTER = new StartsEndsWithFileFilter("temp-", ".pdf");

	private static final long MINIMUM_AGE = 12 * MillisecondTimeUnits.HOUR;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private String[] directories;
	
	public PDFTemporaryFileDeleter(String... directories) {

		this.directories = directories;
	}

	@Override
	public void run() {

		if (directories != null) {
			
			List<File> filestoDelete = new ArrayList<>();

			for (String directory : directories) {
			
				try {
				
					File folder = new File(directory);
					
					if (folder.exists() && folder.canRead()) {
					
						addFilesForDeletion(filestoDelete, folder);
						
					} else {
						
						log.error("Cannot find/access folder " + folder);
					}
					
				} catch (IOException e) {
					
					log.error("Problem reading temp files for deletion from directory " + directory, e);
				}

			}

			for (File file : filestoDelete) {
				
				try {
					log.info("Deleting temporary file " + file.getAbsolutePath());

					if (!file.delete()) {
					
						log.error("Failed to delete temporary file: " + file.getAbsolutePath());
					}
					
				} catch (Exception e) {
					
					log.error("Failed to delete temporary file: " + file.getAbsolutePath(), e);
				}
			}

		}

	}

	private void addFilesForDeletion(List<File> filesToDelete, File folder) throws IOException {

		File[] files = folder.listFiles(FILE_FILTER);
		
		if (files != null) {
		
			for (File file : files) {
			
				if((System.currentTimeMillis() - file.lastModified()) >= MINIMUM_AGE){
					
					filesToDelete.add(file);
				}
			}
		}
	}
}
