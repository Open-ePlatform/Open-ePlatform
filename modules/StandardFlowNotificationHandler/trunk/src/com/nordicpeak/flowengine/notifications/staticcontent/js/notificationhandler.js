$(function(){
	
	$("#flowInstanceSubmittedGlobalEmailAttachPDF").on("change", function(event){
		
		showHideAttachmentsSeperately();
	});
	
	showHideAttachmentsSeperately();
});

function showHideAttachmentsSeperately(){
	
	var checked = $("#flowInstanceSubmittedGlobalEmailAttachPDF").prop('checked');
	
	$("#flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparatelyDiv").toggle(checked).find("input").prop("disabled", !checked);
}
