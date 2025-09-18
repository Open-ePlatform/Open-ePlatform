$(function() {

	const $tags = $("#tags");

	if ($tags.length) {
		const tagHandler = new Tags({
			containerID: "tags",
			itemValue: (tag) => tag.tagID,
			itemText: (tag) => tag.value,
		});

		const tagAutocomplete = new Autocomplete({
			containerID: "tagAutocomplete",
			confirmOnBlur: false,
			uniqueOnly: true,
			showAllValues: true,
			resetStyle: false,

			itemValue: (tag) => tag.tagID,
			itemText: (tag) => tag.value,
		});

		$.get($tags.parent().data("url")).done((result) => {
			const tags = result.tags;

			tagAutocomplete.setResults(tags);
			tagAutocomplete.createTemplate();

			tagAutocomplete.on("itemSelected", (tag) => {
				tagAutocomplete.clearInput();

				tagHandler.addTag(tag);
			});
		});
	}

});