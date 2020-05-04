package com.nordicpeak.flowengine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import se.unlogic.standardutils.fileattachments.FileAttachment;
import se.unlogic.standardutils.fileattachments.FileAttachmentHandler;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.streams.ByteArrayInputStreamProvider;
import se.unlogic.standardutils.streams.InputStreamProvider;

import com.nordicpeak.flowengine.beans.BaseAttachment;
import com.nordicpeak.flowengine.beans.BaseMessage;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;

public class FlowEngineFileAttachmentUtils {

	public static String[] getAttachmentPath(Flow flow) {

		String[] path = new String[1];
		
		path[0] = "flow-" + flow.getFlowID();
		
		return path;
	}
	
	public static String[] getAttachmentPath(FlowInstance flowInstance) {

		String[] path = new String[2];
		
		path[0] = "flow-" + flowInstance.getFlow().getFlowID();
		path[1] = "instance-" + flowInstance.getFlowInstanceID();
		
		return path;
	}
	
	public static String[] getAttachmentPath(BaseMessage message, BaseAttachment attachment) {

		String[] path = new String[4];
		
		path[0] = "flow-" + message.getFlowInstance().getFlow().getFlowID();
		path[1] = "instance-" + message.getFlowInstance().getFlowInstanceID();
		path[2] = message.getTypeLogName() + "-message-" + message.getMessageID();
		path[3] = "attachment-" + attachment.getAttachmentID();
		
		return path;
	}
	
	public static List<FileAttachment> saveAttachmentData(FileAttachmentHandler fileAttachmentHandler, BaseMessage message) throws IOException {
		
		if(message.getAttachments() != null && fileAttachmentHandler != null) {
			
			List<FileAttachment> addedFileAttachments = new ArrayList<>(message.getAttachments().size());
			
			for(BaseAttachment attachment : message.getAttachments()) {
				
				InputStream inputStream = attachment.getInputStreamProvider().getInputStream();
				
				try {
					
					FileAttachment fileAttachment = fileAttachmentHandler.addFileAttchment(inputStream, getAttachmentPath(message, attachment));
					
					addedFileAttachments.add(fileAttachment);
					
				}finally {
					
					CloseUtils.close(inputStream);
				}
			}
			
			return addedFileAttachments;
		}
		
		return null;
	}
	
	public static <T extends BaseAttachment> T createAttachment(FileAttachmentHandler fileAttachmentHandler, String fileName, long fileSize, byte[] data, Class<T> attachmentClass) throws SQLException {
		
		if(fileAttachmentHandler != null) {
			
			return createFileAttachment(fileName, fileSize, new ByteArrayInputStreamProvider(data), attachmentClass);
			
		}else {
			
			return createBlobAttachment(fileName, fileSize, data, attachmentClass);
		}
	}	
	
	public static <T extends BaseAttachment> T createBlobAttachment(String fileName, long fileSize, byte[] data, Class<T> attachmentClass) throws SQLException {

		T attachment = ReflectionUtils.getInstance(attachmentClass);

		attachment.setFilename(fileName);
		attachment.setSize(fileSize);
		attachment.setData(new SerialBlob(data));
		
		return attachment;
	}
	
	public static <T extends BaseAttachment> T createFileAttachment(String fileName, long fileSize, InputStreamProvider inputStreamProvider, Class<T> attachmentClass) {

		T attachment = ReflectionUtils.getInstance(attachmentClass);

		attachment.setFilename(fileName);
		attachment.setSize(fileSize);
		attachment.setInputStreamProvider(inputStreamProvider);
		
		return attachment;
	}

	public static void deleteAttachments(FileAttachmentHandler fileAttachmentHandler, FlowInstance flowInstance) throws IOException {

		if(fileAttachmentHandler != null) {
			
			String[] path = getAttachmentPath(flowInstance);
			
			fileAttachmentHandler.deleteAttachments(path);
		}
	}

	public static void deleteAttachments(FileAttachmentHandler fileAttachmentHandler, Flow flow) throws IOException {

		if(fileAttachmentHandler != null) {
			
			String[] path = getAttachmentPath(flow);
			
			fileAttachmentHandler.deleteAttachments(path);
		}
	}	
}
