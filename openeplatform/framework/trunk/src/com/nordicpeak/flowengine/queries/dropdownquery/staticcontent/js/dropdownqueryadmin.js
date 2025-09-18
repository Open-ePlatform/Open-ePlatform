document.addEventListener("DOMContentLoaded", () => {

	document.querySelector("#useFreeTextAlternative").addEventListener("click", toggleValidator);
	document.querySelector("#formatValidator").addEventListener("change", toggleValidatorInvalidFormatMessage);

	toggleValidator();
	toggleValidatorInvalidFormatMessage();
});

function toggleValidator(event) {

	let useFreeTextAlternative;

	if (event) {
		useFreeTextAlternative = event.target;
	} else {
		useFreeTextAlternative = document.querySelector("#useFreeTextAlternative");
	}

	const formatValidator = document.querySelector("#formatValidator").parentElement;
	const invalidFormatMessage = document.querySelector("#invalidFormatMessage").parentElement;

	if (useFreeTextAlternative.checked) {
		setFlexElementHidden(formatValidator, false);
		toggleValidatorInvalidFormatMessage();
	} else {
		setFlexElementHidden(formatValidator, true);
		setFlexElementHidden(invalidFormatMessage, true);
	}
}

function toggleValidatorInvalidFormatMessage(event) {

	let formatValidator;

	if (event) {
		formatValidator = event.target;
	} else {
		formatValidator = document.querySelector("#formatValidator");
	}

	const invalidFormatMessage = document.querySelector("#invalidFormatMessage");
	const className = formatValidator.querySelector("option:checked")?.value.replaceAll(".", "_"); // There is no valid :selected pseudo-class, but :checked also works.

	if (className && event) {
		const defaultMessage = document.querySelector("#validatorMessage-" + className).value;

		invalidFormatMessage.value = defaultMessage;
	}

	if (formatValidator.value) {
		setFlexElementHidden(invalidFormatMessage.parentElement, false);
	} else {
		setFlexElementHidden(invalidFormatMessage.parentElement, true);
	}
}

function setFlexElementHidden(element, hide = true) {

	if (hide) {
		element.classList.add("hidden");
		element.classList.remove("d-flex");
	} else {
		element.classList.remove("hidden");
		element.classList.add("d-flex");
	}
}