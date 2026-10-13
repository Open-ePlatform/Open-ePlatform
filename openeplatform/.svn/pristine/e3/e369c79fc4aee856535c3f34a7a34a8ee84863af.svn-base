document.addEventListener("DOMContentLoaded", () => {

	document.querySelector("#formatValidator").addEventListener("change", toggleValidatorInvalidFormatMessage);

	toggleValidatorInvalidFormatMessage();
});

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

function setFlexElementHidden(element, hidden = true) {

	if (hidden) {
		element.classList.add("hidden");
		element.classList.remove("d-flex");
	} else {
		element.classList.remove("hidden");
		element.classList.add("d-flex");
	}
}