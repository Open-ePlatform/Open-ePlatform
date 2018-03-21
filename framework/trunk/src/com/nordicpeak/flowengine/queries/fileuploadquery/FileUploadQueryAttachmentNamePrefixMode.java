package com.nordicpeak.flowengine.queries.fileuploadquery;

import se.unlogic.standardutils.populators.EnumPopulator;

public enum FileUploadQueryAttachmentNamePrefixMode {
	QUERY_NAME,
	NO_PREFIX,
	CUSTOM;
	
	private static final EnumPopulator<FileUploadQueryAttachmentNamePrefixMode> POPULATOR = new EnumPopulator<FileUploadQueryAttachmentNamePrefixMode>(FileUploadQueryAttachmentNamePrefixMode.class);
	
	public static EnumPopulator<FileUploadQueryAttachmentNamePrefixMode> getPopulator() {
		return POPULATOR;
	}
}
