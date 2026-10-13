
var i18nFlowEngineQueries = {
	ValidationError_RequiredField: "Required field",
	ValidationError_InvalidFormat: "Invalid format",
	ValidationError_TooLong: "Too long",
	ValidationError_TooShort: "Too short",
	ValidationError_Other: "Unkown validation error",
	localized: false
};

function getValidationErrorMessage(error) {
	
	if (error.displayName) {
		
		return error.displayName;
	
	} else if (error.messageKey) {
	
		return error.messageKey;

	} else if (error.errorType == "RequiredField") {
	
		return i18nFlowEngineQueries.ValidationError_RequiredField;
		
	} else if (error.errorType == "InvalidFormat") {
	
		return i18nFlowEngineQueries.ValidationError_InvalidFormat;
		
	} else if (error.errorType == "TooLong") {
	
		return i18nFlowEngineQueries.ValidationError_TooLong;
		
	} else if (error.errorType == "TooShort") {
	
		return i18nFlowEngineQueries.ValidationError_TooShort;
	}
	
	return i18nFlowEngineQueries.ValidationError_Other;
}
