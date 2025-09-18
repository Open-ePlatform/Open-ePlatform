$(function() {
	const $collectionsTable = $("#collections-table");
	
	$collectionsTable.tablesorter();
	
	$collectionsTable.on("click", ".delete-collection", function(e) {
		const $link = $(this);
		
		const confirmed = $collectionsTable.data("confirmed");
		
		if (confirmed == true || !confirm($link.data("confirm"))) {
			e.preventDefault();
		}
		else {
			$collectionsTable.data("confirmed", true);
		}
	});
	
	var $addPersonBtn = $("#add-person");
	var $personError = $("#person-error");
	var $citizenidentifier = $("#citizenidentifier");
	var $personTemplate = $("#person-template");
	var $personTable = $("#person-table");
	var $encryptionPassword = $("#encryptionPassword");
	
	$personTable.on("click", ".delete-person", function(e) {
		e.preventDefault();
		
		$(this).closest("tr").remove();
	});
	
	$addPersonBtn.click(function(e) {
		e.preventDefault();
		
		$personError.hide();
		
		var data = {};
		data.citizenID = $citizenidentifier.val();
		
		if (data.citizenID) {
			if ($('input[name=citizenID][value="' + data.citizenID + '"]').length > 0) {
				$personError.html($addPersonBtn.data("duplicateid")).show();
			}
			else {
				$.post($addPersonBtn.data("url"), data, function(response) {
					if (response.isValid) {
						var clone = $personTemplate.html();
						
						$personTable.find("tbody").append(clone.replace(/\$citizenID/g, data.citizenID));
						
						$citizenidentifier.val("");
						
						$("#no-persons-yet").remove();
					}
					else if (response.invalidID) {
						$personError.html($addPersonBtn.data("invalidid")).show();
					}
					else {
						$personError.html($addPersonBtn.data("unknown")).show();
					}
				});
			}
		}
	});
	
	if ($("#collection-form").length > 0) {
		updateContentIndexes();
	}
	
	const $publicCollection = $("#publicCollection");
	const $secureCollection = $("#secureCollection");
	const $encrypted = $("#encrypted");
	const $accessCollection = $("#accessCollection");
	const $allowsCollaboration = $("#allowsCollaboration");
	
	$publicCollection.change(function() {
		
		if ($publicCollection.is(":checked")) {
			$("#persons-wrapper").hide();
			$("#user-group-wrapper").hide();
			
			if ($encrypted.is(":checked")) {
				$allowsCollaboration.prop("disabled", false);
			} else {
				$allowsCollaboration.prop("disabled", true);
			}
		} else {
			$allowsCollaboration.prop("disabled", true);
		}
	}).change();
	
	$secureCollection.change(function() {
		
		if ($secureCollection.is(":checked")) {
			$("#persons-wrapper").show();
			$("#user-group-wrapper").hide();
			$allowsCollaboration.prop("disabled", false);
		}
	}).change();
	
	$accessCollection.change(function() {
		
		if ($accessCollection.is(":checked")) {
			$("#persons-wrapper").hide();
			$("#user-group-wrapper").show();
			$allowsCollaboration.prop("disabled", false);
		}
	}).change();
	
	$encrypted.change(function() {
		
		$("#encryption-wrapper").toggle($encrypted.is(":checked"));
		
		if ($encrypted.is(":checked")) {
			if ($publicCollection.is(":checked")) {
				$allowsCollaboration.prop("disabled", false);
			}
		} else if ($publicCollection.is(":checked")) {
			$allowsCollaboration.prop("disabled", true);
		}
	});
	
	$("#setUserKeepDays").change(function() {
		
		$("#userkeepdays-wrapper").toggle($(this).is(":checked"));
	});
	
	$("#copy-password").on("click", function() {
		$encryptionPassword.select();
		
		document.execCommand("copy");
		
		alert($(this).data("copied"));
		
		setTimeout(function() {
			document.getSelection().removeAllRanges();
		}, 500);
	});
	
	$("#generate-password").click(function() {
		var charset = "abcdefghijklnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
			password = "";
		
		for (var i = 0, n = charset.length; i < $(this).data("length"); ++i) {
			password += charset.charAt(Math.floor(Math.random() * n));
		}
		
		$encryptionPassword.val(password);
	});
	
	$("#collection-form").submit(function() {
		
		var $submit = $(this).find("button[type=submit]");
		
		$submit.prop("disabled", true);
		$submit.find("img").show();
		$submit.find("span").html($submit.data("uploading"));
	});
	
	function updateContentIndexes() {

		$(".contentitem:visible").each(function(i) {
			if (i === 0) return;

			const $title = $(this).find("h2");
			const titleText = $title.text();

			$title.text(`${i}. ` + titleText);
		});
	}
});