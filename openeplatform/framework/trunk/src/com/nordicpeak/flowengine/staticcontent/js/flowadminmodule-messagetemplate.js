window.addEventListener("DOMContentLoaded", () => {

	const messageTemplateForm = document.querySelector(".messageTemplateForm");

	if (messageTemplateForm) {
		const emailRadioButton = messageTemplateForm.querySelector("#messageTemplateEmail");
		const textArea = messageTemplateForm.querySelector("#messageTemplateMessage");
		const ckEditor = messageTemplateForm.querySelector("#messageTemplateMessageCK");
		let ckEditorInstance = null;
		
		CKEDITOR.on("instanceReady", () => {
			ckEditorInstance = CKEDITOR.instances[ckEditor.id];			
		});
		
		if (emailRadioButton && textArea) {
			const templateTypeRadios = document.querySelectorAll("input[type='radio'][name='messageTemplateType']");
			
			// The listeners are separate because the radio buttons belong to different radio groups.
			// The code below is essentially giving the illusion that they're part of the same group + and some textarea magic.
			emailRadioButton.addEventListener("change", (e) => {
				if (e.target.checked) {
					textArea.parentElement.classList.add("hidden");
					textArea.setAttribute("disabled", true);

					ckEditor.parentElement.classList.remove("hidden");
					ckEditor.removeAttribute("disabled");
					
					if (ckEditorInstance) {
						ckEditorInstance.setReadOnly(false);
					}
					
					templateTypeRadios.forEach((radio) => {
						radio.checked = false;
					});
				}
			});

			for (const radio of templateTypeRadios) {
				radio.addEventListener("change", (e) => {
					if (e.target.checked) {
						textArea.parentElement.classList.remove("hidden");
						textArea.removeAttribute("disabled");

						ckEditor.parentElement.classList.add("hidden");
						ckEditor.setAttribute("disabled", true);
						
						if (ckEditorInstance) {
							ckEditorInstance.setReadOnly(true);
						}
						
						emailRadioButton.checked = false;
					}
				});
			}
		}
	}
});