/**
 * qLoader is a plugin handling multiple file uploads
 * 
 */

(function($){
	
	var instances = new Array();

	var defaults = {  
			limit: 0,
			duplicates: false,
			file_container_element: '<div/>',
			filerow_element: '<div><span/>&nbsp;&nbsp;&nbsp;<span>delete</span></div>',
			filename_element_index: 0,
			remove_element_index: 1,
			i18n: {
				Limit_reached: 'Du får inte ladda upp fler filer',
				Invalid_format: 'Ogiltigt filformat',
				No_duplicates: 'Du får inte ladda upp samma fil två gånger'
			}
	};

	var private_methods = {

			size: function(assArray) {
				var size = 0;
				for(i in assArray) {
					++size;
				}
				return size;
			},
			
			contains: function(array,value) {
				for(i in array) {
					if(array[i] == value) {
						return true;
					}
				}
				return false;
			},
			
			generateUUID: function() {
				var chars = '0123456789abcdef'.split('');
				var uuid = [], rnd = Math.random;
				var r;
				uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
				uuid[14] = '4';
				for (var i = 0; i < 36; i++) {
					if (!uuid[i]) {
						r = 0 | rnd()*16;
						uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r & 0xf];
					}
				}
				return uuid.join('');
			},
			
			remove: function(id, key) {
				var newArray = new Array();
				for(i in instances[id].filenames) {
					if(i == key) {
						continue;
					}
					newArray[i] = instances[id].filenames[i];
				}
				instances[id].filenames = newArray;
			},
			
			callback: function(event) { 

				var $this = $(this);

				var instance = instances[$this.data("instance")]; 
				
				if(instance.settings.limit > 0 && private_methods.size(instance.filenames) >= instance.settings.limit) {
					
					$this.val('');
					alert(instance.settings.i18n.Limit_reached);

				} else if(!instance.settings.duplicates && private_methods.contains(instance.filenames,event.target.value)) {

					$this.val('');
					alert(instance.settings.i18n.No_duplicates);

				} else {

					// Clone upload and hide source
					var $clone = $this.clone(true, false);
					$clone.attr("id",private_methods.generateUUID()).val('').attr("id");
					$this.after($clone).toggle();
					
					// Add file to list
					private_methods.add.call($this,event);

				}
			},

			add: function(event,options) { 
				
				$this = this;
				
				var instance = instances[$this.data("instance")];
				
				// Determine multiple file selection
				var multiple;
				if(event != undefined && event.target.files != undefined && event.target.files.length > 1) {
					multiple = true;
				}
				
				var filename = (event == undefined ? options.filename : event.target.value);

				// Create file row
				var $filerow = $(instance.settings.filerow_element);
				if(options != undefined) {
					for(i in options.filerow_element_attributes) {
						$filerow.attr(options.filerow_element_attributes[i].name,options.filerow_element_attributes[i].value);
					}
				}
				if($filerow.attr("id") == undefined) {
					$filerow.attr("id","qloader-file-"+$this.attr("id"));
				}

				// Setup file row content
				$filerow.children().each(function(i) {

					switch(i) {
					case instance.settings.filename_element_index:
						if(event != undefined) {
							$(this).text(event.target.value.replace(/\\/g,'/').replace( /.*\//, '' ));
							if(multiple) {
								var $filename = $(this);
								var j = 1;
								while(j < event.target.files.length) {
									$filename.append(", " + event.target.files[j++].name);
								}
							}
						} else {
							$(this).text(options.filename.replace(/\\/g,'/').replace( /.*\//, '' ));
						}
					break;
					case instance.settings.remove_element_index:
						$(this).bind("click", {key: $filerow.attr("id"), instance: $this.data("instance")}, function(e){
							$this.trigger("beforeDelete.qloader",document.getElementById(e.data.key));
							private_methods.remove(e.data.instance, e.data.key);
							$filerow.remove();
							if(event != undefined) {
								$this.detach().trigger("afterDelete.qloader").remove();
							} else {
								$this.trigger("afterDelete.qloader");
							}
						}).css("cursor","pointer");
					break;
					}

				});

				$filerow.appendTo($("#qloader-filelist-"+instance.id));
				
				instance.filenames[$filerow.attr("id")] = filename;
				
			}
	};

	var methods = {

			init: function(options) {
				return this.each(function() {

					var $this = $(this);
					
					var instance = new Object();
					instance.settings = $.extend( {} , defaults, options);
					instance.filenames = new Array();
					instance.id = private_methods.generateUUID();
					instances.push(instance);
					
					$this.bind('change', private_methods.callback);
					$this.data("instance",instances.length - 1);
					var file_container = $(instance.settings.file_container_element).attr("id","qloader-filelist-"+instance.id);
					$this.after(file_container);
				});
			},

			add: function(options) {
				
				var $this = $(this);
				
				var instance = instances[$this.data("instance")];

				if(instance.settings.limit > 0 && private_methods.size(instance.filenames) >= instance.settings.limit) {

					$this.val('');
					alert(instance.settings.i18n.Limit_reached);

				} else {
					
					// Handle bad row element from user
					if(options.filerow_element_attributes != undefined) {
						for(var i = 0; i < options.filerow_element_attributes.length; ++i) {
							if(options.filerow_element_attributes[i].name == "id" && instance.filenames[options.filerow_element_attributes[i].value] != undefined) {
								throw "You cannot add a row with a non unique id attribute! - ignoring row";
							}
						}
					}
					
					private_methods.add.call($this,undefined,options);

				}

				return $this;
			}
			
	};

	$.fn.qloader = function(method) {

		if (methods[method]) {

			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));

		} else if ( typeof method === 'object' || ! method ) {

			return methods.init.apply(this, arguments);

		} else {

			$.error( 'Method ' +  method + ' does not exist on jQuery.upload' );

		}
	};
})(jQuery);