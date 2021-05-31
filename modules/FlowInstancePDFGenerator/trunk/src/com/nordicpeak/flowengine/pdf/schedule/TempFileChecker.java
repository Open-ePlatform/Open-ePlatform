package com.nordicpeak.flowengine.pdf.schedule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import se.unlogic.standardutils.io.StartsWithFileFilter;

public class TempFileChecker implements Runnable {

	private String[] fileFolders;
	private StartsWithFileFilter tempFileFilter = new StartsWithFileFilter("temp-", false);
	private Logger log = Logger.getLogger(TempFileChecker.class);

	private static final long MINIMUM_HOURS = 12;

	public TempFileChecker(String... fileFolders) {

		this.fileFolders = fileFolders;
	}

	@Override
	public void run() {

		if (fileFolders != null) {
			List<File> allFilesForDeletion = new ArrayList<>();
			for (String currentFolderName : fileFolders) {
				try {
					File folder = new File(currentFolderName);
					if (folder.exists() && folder.canRead()) {
						addFilesForDeletion(allFilesForDeletion, folder);
					} else {
						log.warn("Cannot find/access folder " + folder);
					}
				} catch (IOException ioe) {
					log.error("Problem reading tempfiles for deletion", ioe);
				}

			}

			for (File file : allFilesForDeletion) {
				try {
					StringBuilder info = new StringBuilder();
					info.append("System deleting unused temp-file ");
					info.append(file);
					log.info(info.toString());
					if (!file.delete()) {
						log.error("Failed to delete temp-file: " + file);
					}
				} catch (Exception ex) {
					log.error("Failed to delete temp-file: " + file, ex);
				}
			}

		}

	}

	private void addFilesForDeletion(List<File> allFilesForDeletion, File folder) throws IOException {

		File[] files = folder.listFiles(tempFileFilter);
		if (files != null) {
			for (File file : files) {
				BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

				if (Duration.between(attr.creationTime().toInstant(), Instant.now()).toHours() >= MINIMUM_HOURS) {
					allFilesForDeletion.add(file);
				}
			}
		}
	}

}
