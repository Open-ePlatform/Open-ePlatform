package com.nordicpeak.flowengine.tags;

import java.util.Collection;
import java.util.List;

public interface TagSharingProvider {

	public void shareTags(List<TextTag> tags, List<TagSharingTarget> targets, boolean overwrite) throws Exception;
	
	public TagSharingTarget getTarget(String name);
	
	public Collection<? extends TagSharingTarget> getTargets();
}
