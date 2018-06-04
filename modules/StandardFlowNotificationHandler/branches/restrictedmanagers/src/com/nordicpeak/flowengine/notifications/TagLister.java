package com.nordicpeak.flowengine.notifications;

import java.util.Set;



public class TagLister {

	public static void main(String[] args) {

		
		System.out.println("getContactTags");
		printTags(StandardFlowNotificationHandler.getContactTags());
		System.out.println();
		
		System.out.println("getManagerTags");
		printTags(StandardFlowNotificationHandler.getManagerTags());
		System.out.println();
		
		System.out.println("getGlobalTags");
		printTags(StandardFlowNotificationHandler.getGlobalTags());
		System.out.println();
		
		System.out.println("getSigningPartyTags");
		printTags(StandardFlowNotificationHandler.getSigningPartyTags());
	}

	public static void printTags(Set<String> tags){
		
		for(String tag : tags){
			
			System.out.println(tag);
		}
	}
}
