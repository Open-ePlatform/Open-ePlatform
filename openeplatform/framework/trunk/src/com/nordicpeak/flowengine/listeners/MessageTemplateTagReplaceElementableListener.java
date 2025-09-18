package com.nordicpeak.flowengine.listeners;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.AttributeTagUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.MessageTemplate;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.utils.TextTagReplacer;
import com.nordicpeak.flowengine.utils.UserAttributeTagUtils;

public class MessageTemplateTagReplaceElementableListener implements ElementableListener<MessageTemplate> {

	private static final AnnotatedBeanTagSourceFactory<Flow> FLOW_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<>(Flow.class, "$flow.");
	private static final AnnotatedBeanTagSourceFactory<FlowInstance> FLOWINSTANCE_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<>(FlowInstance.class, "$flowInstance.");
	private static final AnnotatedBeanTagSourceFactory<User> USER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<>(User.class, "$user.");

	private final FlowInstance flowInstance;
	private final User user;
	private final SiteProfile profile;

	public MessageTemplateTagReplaceElementableListener(FlowInstance flowInstance, User user, SiteProfile profile) {

		this.flowInstance = flowInstance;
		this.user = user;
		this.profile = profile;
	}

	@Override
	public void elementGenerated(Document doc, Element element, MessageTemplate template) {

		Element replacedMessageElement = XMLUtils.createElement("message", replaceTags(template.getMessage(), flowInstance, user, profile), doc);

		XMLUtils.replaceSingleNode(element, replacedMessageElement);
	}

	private String replaceTags(String text, FlowInstance flowInstance, User user, SiteProfile profile) {

		TagReplacer tagReplacer = new TagReplacer();

		if (flowInstance != null) {

			text = AttributeTagUtils.replaceTags(text, flowInstance.getAttributeHandler());

			tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource(flowInstance.getFlow()));
			tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource(flowInstance));
		}

		if (user != null) {

			text = UserAttributeTagUtils.replaceTags(text, user);

			tagReplacer.addTagSource(USER_TAG_SOURCE_FACTORY.getTagSource(user));
		}

		text = tagReplacer.replace(text);

		if (profile != null) {

			text = TextTagReplacer.replaceTextTags(text, profile.getSettingHandler());
		}

		return text;
	}

}
