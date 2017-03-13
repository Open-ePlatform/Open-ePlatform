package com.nordicpeak.flowengine;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.enums.PathType;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.notifications.NotificationBackgroundModule;
import com.nordicpeak.flowengine.notifications.NotificationHandlerModule;

public class NewEventsBackgroundModule extends AnnotatedBackgroundModule {
	
	@Override
	public void init(BackgroundModuleDescriptor descriptor, final SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		if (descriptor instanceof SimpleBackgroundModuleDescriptor) { // Don't try to cache non-existent xsl
			
			SimpleBackgroundModuleDescriptor simpleBackgroundModuleDescriptor = (SimpleBackgroundModuleDescriptor) descriptor;
			simpleBackgroundModuleDescriptor.setXslPath(null);
		}
		
		final SimpleForegroundModuleDescriptor notificationHandlerDescriptor = new SimpleForegroundModuleDescriptor(descriptor);
		notificationHandlerDescriptor.setClassname(NotificationHandlerModule.class.getName());
		notificationHandlerDescriptor.setStaticContentPackage("staticcontent");
		notificationHandlerDescriptor.setXslPathType(PathType.Classpath);
		notificationHandlerDescriptor.setXslPath("NotificationHandlerModule.sv.xsl");
		notificationHandlerDescriptor.setName("Mina meddelanden");
		notificationHandlerDescriptor.setDescription("Mina meddelanden");
		notificationHandlerDescriptor.setAlias("notifications");
		
		if (ModuleUtils.migrateModuleToNewClass(NotificationBackgroundModule.class, "/com/nordicpeak/flowengine/staticcontent", PathType.Classpath, "NotificationBackgroundModule.sv.xsl", descriptor, sectionInterface, dataSource) != null) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						sectionInterface.getSystemInterface().getCoreDaoFactory().getForegroundModuleDAO().add(notificationHandlerDescriptor);
						sectionInterface.getSystemInterface().getEventHandler().sendEvent(ForegroundModuleDescriptor.class, new CRUDEvent<ForegroundModuleDescriptor>(CRUDAction.ADD, notificationHandlerDescriptor), EventTarget.ALL);
						
						sectionInterface.getForegroundModuleCache().cache(notificationHandlerDescriptor);
						log.info("Added " + notificationHandlerDescriptor + " to section " + sectionInterface.getSectionDescriptor());
						
					} catch (Throwable t) {
						log.error("Error adding " + notificationHandlerDescriptor + " to section " + sectionInterface.getSectionDescriptor(), t);
					}
				}
			}).start();
		}
	}
	
	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		return null;
	}
	
}
