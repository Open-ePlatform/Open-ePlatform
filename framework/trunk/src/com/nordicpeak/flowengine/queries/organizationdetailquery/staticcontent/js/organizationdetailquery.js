var organizationDetailQueryi18n = {
	"AddToMyOrganizations": "Add this company's information to my companies",
	"UpdateToMyOrganizations": "Update this company's information to my companies"
}

$(document).ready(function() {
	
	setQueryRequiredFunctions["OrganizationDetailQueryInstance"] = makeOrganizationDetailQueryRequired;
	
});

function initOrganizationDetailQuery(queryID) {
	
	var shortQueryID = "#q" + queryID;
	
	$(shortQueryID + "_mobilephone").on("keyup blur change", function() {
		
		if($(this).val() != "") {
			$(shortQueryID + "_contactBySMS").removeAttr("disabled").next("label").removeClass("disabled");
		} else {
			$(shortQueryID + "_contactBySMS").removeAttr("checked").attr("disabled", "disabled").next("label").addClass("disabled");
		}
		
	});
	
	$(shortQueryID + "_email").on("keyup blur change", function() {
		
		if($(this).val() != "") {
			$(shortQueryID + "_contactByEmail").attr("checked", true);
		} else {
			$(shortQueryID + "_contactByEmail").removeAttr("checked");
		}
		
	});
	
	$(shortQueryID + "_mobilephone").trigger("change");
	$(shortQueryID + "_email").trigger("change");
	
	$(shortQueryID + "_newOrganization").click(function(e) {
		
		e.preventDefault();
		
		$(shortQueryID + "_organization").val("");
		
		resetOrganizationDetailForm(shortQueryID);
		
		$(shortQueryID + "_persistOrganization").next("label").text(organizationDetailQueryi18n.AddToMyOrganizations);
		
		$("#query_" + queryID).find(".form-wrapper").slideDown();
		
	});
	
	$(shortQueryID + "_organization").change(function(e) {
		
		var $this = $(this);
		
		var organizationID = $this.val();
		
		resetOrganizationDetailForm(shortQueryID);
		
		if(organizationID != "") {
			
			var id = "#organization" + organizationID;
			
			$(shortQueryID + "_name").val($(id + "_name").val());
			$(shortQueryID + "_organizationNumber").val($(id + "_organizationNumber").val());
			$(shortQueryID + "_address").val($(id + "_address").val());
			$(shortQueryID + "_zipcode").val($(id + "_zipcode").val());
			$(shortQueryID + "_postaladdress").val($(id + "_postaladdress").val());
			$(shortQueryID + "_mobilephone").val($(id + "_mobilephone").val());
			$(shortQueryID + "_email").val($(id + "_email").val());
			$(shortQueryID + "_phone").val($(id + "_phone").val());
			
			if(!$(shortQueryID + "_firstname").prop("disabled")){
				$(shortQueryID + "_firstname").val($(id + "_firstname").val());
				$(shortQueryID + "_lastname").val($(id + "_lastname").val());
			}
			
			$(shortQueryID + "_mobilephone").trigger("change");
			$(shortQueryID + "_email").trigger("change");
			
			if($(id + "_contactBySMS").val() == "true") {
				$(shortQueryID + "_contactBySMS").attr("checked", "checked").removeAttr("disabled").next("label").removeClass("disabled");
			}
			
			$(shortQueryID + "_persistOrganization").next("label").text(organizationDetailQueryi18n.UpdateToMyOrganizations);
			
			$("#query_" + queryID).find(".form-wrapper").slideDown();
			
		} else {
			
			$("#query_" + queryID).find(".form-wrapper").hide();
			
			resetOrganizationDetailForm(shortQueryID);
			
		}
		
	});
	
	var organizationID = $(shortQueryID + "_organization").val();
	
	if(organizationID == undefined || organizationID == "") {
		$(shortQueryID + "_persistOrganization").next("label").text(organizationDetailQueryi18n.AddToMyOrganizations);	
	} else {
		$(shortQueryID + "_persistOrganization").next("label").text(organizationDetailQueryi18n.UpdateToMyOrganizations);
	}
	
	$("#query_" + queryID).find("input.input-error").parent().addClass("input-error");
	
}

function resetOrganizationDetailForm(shortQueryID) {
	
	$(shortQueryID + "_name").val("");
	$(shortQueryID + "_organizationNumber").val("");
	$(shortQueryID + "_address").val("");
	$(shortQueryID + "_zipcode").val("");
	$(shortQueryID + "_postaladdress").val("");
	$(shortQueryID + "_mobilephone").val("");
	$(shortQueryID + "_email").val("");
	$(shortQueryID + "_phone").val("");
	
	if(!$(shortQueryID + "_firstname").prop("disabled")){
		$(shortQueryID + "_firstname").val("");
		$(shortQueryID + "_lastname").val("");
	}
	
	$(shortQueryID + "_contactBySMS").removeAttr("checked").attr("disabled", "disabled").next("label").addClass("disabled");
	$(shortQueryID + "_contactByEmail").removeAttr("checked");
	
	$(shortQueryID + "_persistOrganization").removeAttr("checked");
	
}

function makeOrganizationDetailQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}