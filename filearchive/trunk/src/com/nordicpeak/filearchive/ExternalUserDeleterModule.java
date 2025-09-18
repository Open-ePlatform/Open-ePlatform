package com.nordicpeak.filearchive;

import java.util.List;

import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.SystemStartupListener;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.querys.BooleanQuery;

import it.sauronsoftware.cron4j.Scheduler;

public class ExternalUserDeleterModule extends AnnotatedForegroundModule implements Runnable {
	
	@ModuleSetting
	@GroupMultiListSettingDescriptor(name = "Groups to delete users from", description = "Groups to delete users from")
	protected List<Integer> deleteGroups;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Start time expression", description = "Cron expression for when to run archiving", required = true)
	protected String cronExp = "30 0 * * *";
	
	private Scheduler taskScheduler;
	
	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		this.stopTaskScheduler();

		this.initTaskScheduler();
	}

	@Override
	public void unload() throws Exception {

		stopTaskScheduler();

		super.unload();
	}

	@SystemStartupListener
	public void systemStarted() {

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

	@Override
	public void run() {

		try {
			if (!CollectionUtils.isEmpty(deleteGroups)) {
				List<User> users = systemInterface.getUserHandler().getUsersByGroups(deleteGroups, true);
				
				if (!CollectionUtils.isEmpty(users)) {
					for (User user : users) {
						if (user.getAttributeHandler().isSet("citizenIdentifier")) {
							BooleanQuery query = new BooleanQuery(dataSource, "SELECT * FROM file_archive_collection_persons WHERE citizenID = ?");
							query.setString(1, user.getAttributeHandler().getString("citizenIdentifier"));
							
							if (!query.executeQuery()) {
								log.info("Deleting user " + user + " not having access to any file collections");
								
								systemInterface.getUserHandler().deleteUser(user);
							}
						}
						else {
							log.info("Deleting user " + user + " not having citizenIdentifier set");
							
							systemInterface.getUserHandler().deleteUser(user);
						}
					}
				}
			}
		}
		catch (Exception e) {
			log.error("Unable to delete external users", e);
		}
	}
}