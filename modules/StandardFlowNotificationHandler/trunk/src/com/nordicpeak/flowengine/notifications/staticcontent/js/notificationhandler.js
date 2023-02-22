const FlowInstances = {
	SubmittedGlobalEmail: "#flowInstanceSubmittedGlobalEmailAttachPDF",
	ArchivedGlobalEmail: "#flowInstanceArchivedGlobalEmailAttachPDF",
}

$(function() {

	$(FlowInstances.SubmittedGlobalEmail).on("change", () => {

		showHideAttachmentsSeperately(FlowInstances.SubmittedGlobalEmail);
	});

	$(FlowInstances.ArchivedGlobalEmail).on("change", () => {

		showHideAttachmentsSeperately(FlowInstances.ArchivedGlobalEmail);
	});

	for (const key in FlowInstances) {
		showHideAttachmentsSeperately(FlowInstances[key]);
	}
});

function showHideAttachmentsSeperately(flowInstanceID) {

	const checked = $(flowInstanceID).prop('checked');

	$(`${flowInstanceID}AttachmentsSeparatelyDiv`).toggle(checked).find("input").prop("disabled", !checked);
}
