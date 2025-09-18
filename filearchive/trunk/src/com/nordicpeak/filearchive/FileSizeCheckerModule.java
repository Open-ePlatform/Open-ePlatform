package com.nordicpeak.filearchive;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.SystemStartupListener;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;

import com.nordicpeak.filearchive.beans.File;

import it.sauronsoftware.cron4j.Scheduler;

public class FileSizeCheckerModule extends AnnotatedForegroundModule implements Runnable {

	@InstanceManagerDependency(required = true)
	private FileArchiveModule fileArchiveModule;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File size check expression", description = "Cron expression for when to check file sizes", required = true)
	protected String cronExp = "0 16 * * *";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File size amount (Gb)", description = "Total file size amount", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxFilesSize = 50;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File size threshold (%)", description = "Threshold as percentage for how much space should be occupied before sending email", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer fileSizeThreshold = 90;

	private Scheduler taskScheduler;

	@SystemStartupListener
	public void systemStarted() throws Exception {

		this.initTaskScheduler();
	}

	@Override
	public void run() {

		if (this.systemInterface.getSystemStatus() == SystemStatus.STARTED) {
			log.info("Checking file sizes against threshold");

			try {
				List<File> files = fileArchiveModule.getAllFiles();
				Long fileSizeTotal = 0L; // in bytes

				if (CollectionUtils.isEmpty(files)) {
					log.info("No files found");
					
					return;
				}

				for (File file : files) {
					fileSizeTotal += file.getSize();
				}

				boolean fileSizeTotalIsAboveThreshold = (fileSizeTotal / BinarySizes.GigaByte) >= maxFilesSize * ((float) fileSizeThreshold / 100.0);

				if (fileSizeTotalIsAboveThreshold) {
					log.error("File storage usage is now " + fileSizeTotal / BinarySizes.GigaByte + " GB of total " + maxFilesSize + " GB");
				}
				else {
					log.info("File size usage is below threshold");
				}
			} catch (SQLException e) {
				log.error("Could not get all files ", e);
			}
		}
	}

	@Override
	public void unload() throws Exception {

		stopTaskScheduler();

		super.unload();
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		this.stopTaskScheduler();
		this.initTaskScheduler();
	}

	private void initTaskScheduler() {

		this.taskScheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		this.taskScheduler.setDaemon(true);
		this.taskScheduler.schedule(cronExp, this);
		this.taskScheduler.start();
	}

	private void stopTaskScheduler() {

		if (this.taskScheduler != null && this.taskScheduler.isStarted()) {
			this.taskScheduler.stop();
		}
	}
}
