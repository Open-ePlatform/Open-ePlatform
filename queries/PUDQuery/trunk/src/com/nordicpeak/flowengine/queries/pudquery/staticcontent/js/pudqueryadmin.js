$(document).ready(function() {
	
	var useAddressAsResult = $("#useAddressAsResult");
	var allowedSearchService_PUD = $("#allowedSearchService_PUD");
	var allowedSearchService_Address = $("#allowedSearchService_Address");
	
	var enableDisableAllowedSearch = function(){
		
		var checked = useAddressAsResult.prop("checked");
		
		allowedSearchService_PUD.prop("disabled", checked);
		allowedSearchService_Address.prop("disabled", checked);
		
		if (checked) {
			
			allowedSearchService_PUD.prop("checked", false);
			allowedSearchService_Address.prop("checked", true);
		}
	};
	
	enableDisableAllowedSearch();
	useAddressAsResult.change(enableDisableAllowedSearch);
});