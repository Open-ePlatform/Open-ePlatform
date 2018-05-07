package com.nordicpeak.flowengine.interfaces;

import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;


public interface UserMenuProvider {

	/**
	 * @return null or a {@link AccessInterface}
	 */
	public AccessInterface getAccessInterface();
	
	public String getUserMenuAlias();
	
	/**
	 * @return
	 *  null or an {@link ExtensionLink}
	 */
	public ExtensionLink getUserMenuExtensionLink();
}