var mapClientScriptLocation;
var mapClientScriptLocationFallback;
var mapClientLanguages = {
	"DIMENSION_AND_ANGLE_SETTINGS" : "Dimension and angle settings",
}
var mapInstances = {};
var version = "1.8.0";

var LoadFiles = function(files, callback) {

	var fileCounter = 0;
	var file;
	var obj;
	var cssFilesIndex = document.styleSheets.length - 1;

	for (index in files) {

		var file = files[index];

		fileCounter++;
		
		if(getFileType(file) == "css") {
		
			addCssFile(file);
	
			cssFilesIndex++;
			
			if (!window.opera && navigator.userAgent.indexOf("MSIE") == -1) {
				checkCSSLoadingState(cssFilesIndex);
			}
		
		} else {
			
			addJsFile(file);
			
		}
			
	}

	function getFileType(file) {
	    
		file = file.toLowerCase()
	  
	    var jsIndex = file.indexOf("js"),
	        cssIndex = file.indexOf("css");
	    
	    if(jsIndex==-1 && cssIndex==-1) {
	      return false;
	    }
	    
	    return jsIndex > cssIndex ? "js" : "css";
	      
	  }
	
	function addCssFile(url) {
		
		var link = document.createElement("link")
		link.href = url;
		link.rel = "stylesheet";
		link.type = "text/css";
		
		link.onload = checkFinishedState;
		link.onreadystatechange = function() {
			
			if (this.readyState == 'loaded' || this.readyState == 'complete')
				checkFinishedState();
			
		};

		document.getElementsByTagName("head")[0].appendChild(link);

	}
	
	function addJsFile(file) {
		
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.src = file;
		script.onload = checkFinishedState;
		
		document.getElementsByTagName("head")[0].appendChild(script);

	}
	
	function checkCSSLoadingState(index) {

		try {
			
			if (document.styleSheets[index].cssRules) {
				
				checkFinishedState();
				
			} else {
				
				if (document.styleSheets[index].rules && document.styleSheets[index].rules.length) {
					
					checkFinishedState();
					
				} else {
					setTimeout(function() { checkCSSLoadingState(index); }, 250);
				}
			}
		} catch (e) {
			
			setTimeout(function() { checkCSSLoadingState(index); }, 250);
			
		}

	}

	function checkFinishedState() {
		
		fileCounter--;

		if (fileCounter == 0) {
			callback();
			fileCounter = -1;
		}
		
	}
	
};

(function () {

	if (!$("html").hasClass("map-client-riges")) {
	
		Ext = {
			buildSettings:{
				"scopeResetCSS": true
			}
		};
	
		var scriptNameRaw = "map-client-riges.js";
		var mapClientScriptName = new RegExp("(^|(.*?\\/))(" + scriptNameRaw + ")(\\?|$)");
	
		var scripts = document.getElementsByTagName('script');
	
		for (var i = 0; i < scripts.length; i++) {
	        
			var src = scripts[i].getAttribute('src');
	
			if (src) {
	            
				var match = src.match(mapClientScriptName);
	            
	            if (match) {
	
	            	mapClientScriptLocation = match[1];
	            	
	            	break;
	            }
	            
	        }
			
	    }
		
		if(mapClientScriptLocation == undefined && mapClientScriptLocationFallback != undefined) {
			mapClientScriptLocation = mapClientScriptLocationFallback.match(mapClientScriptName)[1];
		}
	
		var cssFiles = new Array(
			mapClientScriptLocation + version + "/lib/ext-theme-oep/ext-theme-oep-all.css",
			mapClientScriptLocation + version + "/resources/css/OpenEMap.css",
			mapClientScriptLocation + "/css/map-client-riges.css",
			mapClientScriptLocation + version + "/lib/Openlayers/theme/default/style.css"
		);
	
		var scripts = new Array(
			mapClientScriptLocation + version + "/lib/ext/ext-all.js",
			mapClientScriptLocation + version + "/lib/ext/ext-theme-neptune.js",
			mapClientScriptLocation + version + "/lib/ext/locale/ext-lang-sv_SE.js",
			mapClientScriptLocation + version + "/lib/Openlayers/OpenLayers.js",
			mapClientScriptLocation + version + "/lib/proj4js/proj4-compressed.js",
			mapClientScriptLocation + version + "/proj4_defs.js",
			mapClientScriptLocation + version + "/lib/geoext/geoext-all.js",
			mapClientScriptLocation + version + "/lib/es5-shim/es5-shim.min.js",	
			mapClientScriptLocation + version + "/OpenEMap-debug.js"
	    );
		
		LoadFiles(cssFiles, function() {
			
			loadScripts(scripts);
			
		});
		
		$("html").addClass("map-client-riges");
	
	}

})();

function loadScripts(jsFiles) {

    var scriptTags = new Array(jsFiles.length);
    
    for (var i = 0; i < jsFiles.length; i++) {
    	
		scriptTags[i] = "<script src='" + jsFiles[i] + "' type='text/javascript'></script>";
        
    }
    
    if (scriptTags.length > 0) {
    	
		var $head = $("head");

		for (var i = 0, len = scriptTags.length; i < len; i++) {
			$head.append(scriptTags[i]);
		}
    }

}

function MapClientInstance(queryID, mapID){

	this.MAP_RESOLUTIONS = [280, 140, 70, 28, 14, 7, 4.2, 2.8, 1.4, 0.56, 0.28, 0.14, 0.112, 0.056];
	this.mapID = mapID;
	this.mapDiv = $("#" + mapID);
	this.map;
	this.featureDialogs = {}; 
	this.dialog;
	this.preview;
	this.externalGraphicsLocation = mapClientScriptLocation + "images";
	this.mapImageLocation = mapClientScriptLocation + version + "/resources/images/";
	this.config;
	this.originalTools;
	this.layerMapping = {};
	this.enableEdgeLabeling = false;
	
	var instance = this;

	mapInstances[queryID] = instance;

	instance.setCustomConfig = function(config, customConfig, preview) {

		if(customConfig.extent) {
			config.extent = customConfig.extent;
		}

		if(customConfig.search && config.gui.searchFastighet) {
			
			config.gui.searchFastighet = {
				renderTo : customConfig.search.renderTo
			};
			
			if(customConfig.search.zoom) {
				config.gui.searchFastighet.zoom = instance.MAP_RESOLUTIONS.indexOf(customConfig.search.zoom / 4000);
			}
			
			$("#" + customConfig.search.renderTo).show();
			
			if(customConfig.search.basePath) {
				config.basePath = customConfig.search.basePath;	
			}
			
			if(customConfig.search.lmUser) {
				OpenEMap.lmUser = customConfig.search.lmUser;
			}
			
		}

		if(customConfig.objectConfig && config.gui.objectConfig) {
			config.gui.objectConfig = {
				renderTo : customConfig.objectConfig.renderTo
			};
		} else {
			config.gui.objectConfig = false;
		}

		if (customConfig.searchCoordinate && config.gui.searchCoordinate) {
			config.gui.searchCoordinate = {
				renderTo : customConfig.searchCoordinate.renderTo
			};
			
			if(customConfig.searchCoordinate.zoom) {
				config.gui.searchCoordinate.zoom = instance.MAP_RESOLUTIONS.indexOf(customConfig.searchCoordinate.zoom / 4000);
			}
			
			$("#" + customConfig.searchCoordinate.renderTo).show();
		}

		if(customConfig.enableEdgeLabeling) {
			instance.enableEdgeLabeling = true;
		}
		
		if(customConfig.basePathMapFish) {
			config.basePathMapFish = customConfig.basePathMapFish;
		}
		
		instance.originalTools = config.tools;

		if(preview) {
			config.gui.zoomTools = false;
			config.gui.baseLayers = false;
			config.gui.searchFastighet = false;
			config.gui.toolbar = false;
			config.gui.searchCoordinate = false;
			config.tools = false;
		}

	};

	var initLoadingDialog = null;

	instance.init = function(configURL, config, customConfig, preview) {

		instance.preview = preview;

		if(configURL) {

			$.getJSON(configURL, function (result) {
								
				try {
					
					config = eval(result);

				} catch (e) {
					
					instance.showValidationError("Felaktigt format på konfigurationen", "Ett fel uppstod när kartkonfigurationen skulle hämtas, kontakta administratören!");
					return;

				}

				if (!config) {
					
					instance.showValidationError("Ingen kartkonfiguration", "Ingen kartkonfiguration hittades, kontakta administratören!");
					return

				} else {
					
					waitUntilMapIsCreated(instance, config, customConfig, preview);

				}

			}).error(function(response, status, xhr) { 
				
				if(status == "parsererror") {
					instance.showValidationError("Felaktigt format på konfigurationen", "Ett fel uppstod när kartkonfigurationen skulle hämtas, kontakta administratören!");
				} else {
					instance.showValidationError("Anslutningsfel", "Kan inte ansluta till servern för hämtning av kartkonfiguration, kontakta administratören!"); 
				}

				return; 
			});

		} else if(!config) {
			
			instance.showValidationError("Ingen kartkonfiguration", "Ingen kartkonfiguration finns angiven!");
			return;

		} else {
			
			waitUntilMapIsCreated(instance, config, customConfig, preview);

		}

	};

	instance.initMapClient = function(config) {

		instance.showLoadingDialog = function(message) {
		
			var loadingDialog = new Ext.LoadMask(Ext.get(mapID), { msg: message, centered: true });

			loadingDialog.show();

			return loadingDialog;

		}

		initLoadingDialog = instance.showLoadingDialog("Laddar karta...");

		instance.mapDiv.bind("map-client-riges.loaded", instance.mapLoadedEventCallback);

		
		config.gui.map = {
			renderTo : mapID
		};

		OpenEMap.basePathImages = instance.mapImageLocation;
		
		instance.map = Ext.create("OpenEMap.Client");
		
		if(config.basePath) {
			instance.map.basePath = config.basePath;
			OpenEMap.basePathLM = config.basePath;
		}
		
		if(config.basePathMapFish) {
			OpenEMap.basePathMapFish = config.basePathMapFish;
		}
		
		var options = {
			"theme" : null
		};

		options.gui = config.gui;

		instance.config = config;

		if(instance.config.layers) {
			$.each(instance.config.layers, function(i, layer) {
				if(layer.params) {
					instance.layerMapping[layer.name] = layer.params["layers"];
				} else if(layer.wms && layer.wms.params) {
					instance.layerMapping[layer.name] = layer.wms.params["LAYERS"];
				}
			});
		}
		
		instance.map.configure(config, options);

		if(instance.preview) {
			$.each(instance.map.gui.mapPanel.map.getControlsByClass("OpenLayers.Control.Navigation"), function(i, control){
				control.deactivate();
			});
		}

		initLoadingDialog.hide();
		
		instance.mapDiv.trigger("map-client-riges.loaded", [instance]);

		instance.map.gui.mapPanel.map.events.register('move', this, function(e) { instance.mapMovedCallback(e, instance); });
	
		if (instance.map.gui.mapPanel && instance.map.gui.mapPanel.drawLayer) {
			
			instance.map.gui.mapPanel.drawLayer.events.register('featureadded', null, function(e) { instance.featureAddedCallback(e, instance); } ); 
		
			instance.map.gui.mapPanel.drawLayer.events.register('featureremoved', null, function(e) { instance.removeFeatureDialog(e.feature); instance.featureRemovedCallback(e, instance); } );
			
			instance.map.gui.mapPanel.drawLayer.events.register('beforefeatureadded', null, function(e) { return instance.beforeFeatureAddedCallback(e, instance); } );

			instance.map.gui.mapPanel.drawLayer.events.register('sketchcomplete', null, function(e) { return instance.sketchCompletedCallback(e, instance); } );

			instance.map.gui.mapPanel.map.events.register('changelayer', null, function(e) { 
				if(e.property == "visibility") { 
					instance.layerVisibilityChangedCallback(e, instance); 
				}
			});

			instance.map.gui.mapPanel.map.events.register('changebaselayer', null, function(e) { 
				instance.baseLayerChangedCallback(e, instance); 
			});

			if(instance.enableEdgeLabeling) {
			
				var labels = new OpenLayers.Rule({
	                filter: new OpenLayers.Filter.Comparison({
	                    type: OpenLayers.Filter.Comparison.EQUAL_TO,
	                    property: "type",
	                    value: "label",
	                }),
	                  symbolizer: {
	                    pointRadius: 20,
	                    fillOpacity: 0,
	                    strokeOpacity: 0,
	                    label: "${label}"   
	                 }
	                });
	            
				instance.map.gui.mapPanel.drawLayer.styleMap.styles['default'].addRules([labels]);
				instance.map.toggleEdgeLabels();
			
			}
			
		}

		if(instance.map.gui.searchCoordinate) {
			
			var coordinateMarkerStyle = new OpenLayers.Style({
		    	"pointRadius": 10,
		    	"externalGraphic": instance.externalGraphicsLocation + "/pin-added.png"
		    });
			
			var coordinateMarkerLayer = new OpenLayers.Layer.Vector('SearchCoordinateLayer', {
				displayInLayerSwitcher: false,
				styleMap:  new OpenLayers.StyleMap(coordinateMarkerStyle)
		    });
			
			instance.map.gui.mapPanel.map.addLayer(coordinateMarkerLayer);
			
			instance.map.gui.searchCoordinate.on("searchcomplete", function(coordinates) {
				
				coordinateMarkerLayer.destroyFeatures();
				
				var point = new OpenLayers.Geometry.Point(coordinates[0], coordinates[1]);
				
				var marker = new OpenLayers.Feature.Vector(point);
				
				coordinateMarkerLayer.addFeatures([marker]);
				
			});
			
		}
		
		if(instance.config.gui.objectConfig && instance.config.gui.objectConfig.renderTo) {
			
			instance.mapDiv.on("click", function(e) {
				
				var $configWrapper = $("#" + instance.config.gui.objectConfig.renderTo);
				
				var $header = $configWrapper.find("span.header");
				
				if($header.length == 0) {
					$configWrapper.prepend("<span class='header'>" + mapClientLanguages.DIMENSION_AND_ANGLE_SETTINGS + "</span>");
				} else {
					$header.text(mapClientLanguages.DIMENSION_AND_ANGLE_SETTINGS);
				}

				if($configWrapper.find(".x-panel-default").css("display") == "none") {
					$configWrapper.css("visibility", "hidden");
				} else {
					$configWrapper.css("visibility", "visible");
				}
				
			});
			
			instance.mapDiv.trigger("click");
			
		}
		
		var $oepToolBar = $(".oep-tools");
		
		$oepToolBar.css("width", ($oepToolBar.width() + 3) + "px");
		
	};

	instance.zoomToScale = function(scale) {
		
		instance.map.gui.mapPanel.map.zoomToScale(scale);

	};

	instance.showValidationError = function(title, message) {

		$("#query_" + queryID).find("article").addClass("error").addClass("jserror").before(
			'<div class="info-box first error jserror">' +
				'<span>' +
					'<strong data-icon-before="!">' + title +  '. </strong>' + message +
				'</span>' +
				'<div class="marker"></div>' +
			'</div>'
		);

		instance.map.gui.mapPanel.map.updateSize();
		
	};

	instance.removeValidationErrors = function() {
		
		$("#query_" + queryID).find("article.jserror").removeClass("error").removeClass("jserror").prev(".jserror").remove();

		instance.map.gui.mapPanel.map.updateSize();
		
	};

	instance.showFeatureDialog = function(feature, layer, message, contentSize, showCloseButton) {
					
		var $message = null;

		if(message.jquery) {
			$message = message;
		} else {
			$message = $(message);
		}

		var popup = new OpenLayers.Popup.Anchored("map-client-messagebox_" + mapID, feature.geometry.getBounds().getCenterLonLat(), contentSize, $message.html() + "<div class='marker' />", null, showCloseButton);
        
		popup.displayClass = "map-client-messagebox";
		popup.autoSize = false;

		var featureHeight = feature.geometry.getBounds().getHeight();

		var layerStyles = layer.styleMap.styles["default"];

		if(featureHeight == 0) {
			if(layerStyles.defaultStyle.pointRadius == "${getSize}") {
				featureHeight =layerStyles.context.getSize(feature);
			} else if(layerStyles.defaultStyle.pointRadius) {
				featureHeight = layerStyles.defaultStyle.pointRadius;
			} 
		}


		var anchor = {'size': new OpenLayers.Size(0, 0), 'offset': new OpenLayers.Pixel(-188, (featureHeight / 2) * -1)};
		popup.anchor = anchor;
		popup.panMapIfOutOfView = true;
		popup.relativePosition = "tr";
		popup.calculateRelativePosition = function () { return 'tr'; };
		feature.popup = popup;
		instance.map.gui.mapPanel.map.addPopup(popup);

		instance.featureDialogs[feature.id] = popup;

	};

	instance.removeFeatureDialog = function(feature) {
		
		var popup = instance.featureDialogs[feature.id];

		if(popup != null) {
			instance.map.gui.mapPanel.map.removePopup(popup);
			popup = null;
		}

	};
	
	instance.showDialog = function(message, showCloseButton, closeButtonCallback) {
		
		var $message = null;

		if(message.jquery) {
			$message = message.clone();
			$message.show();
		} else {
			$message = $(message);
		}
		
		var $dialog = $("<div class='mapquerydialog olPopup'><div class='olPopupContent'></div></div>");
		
		var $content = $dialog.find(".olPopupContent");
		
		$content.append($message);
		
		if(showCloseButton) {
			
			var $closeBox = $("<div class='olPopupCloseBox' />");
			
			$closeBox.click(function(e) {
				$dialog.close(e);
			});
			
			$content.append($closeBox);
			
		}
		
		$dialog.close = function(e) {
			
			if(closeButtonCallback) {
				closeButtonCallback($dialog);
			} 
			
			$dialog.remove();
			
			Ext.get(instance.mapID).unmask();
			
		};
		
		Ext.get(instance.mapID).mask();
		instance.mapDiv.append($dialog);
		
		$dialog.css({ "left": (instance.mapDiv.width()/2)-($dialog.width()/2), "top": (instance.mapDiv.height()/2)-($dialog.height()/2) });
		
		instance.dialog = $dialog;
	};

	instance.getVisibleLayers = function() {

		var visibleLayers = [];
		
		var layers = instance.map.gui.mapPanel.map.layers;
		
		for(var i = 0; i < layers.length; i++) {
			
			var layer = layers[i];
			
			if(layer.visibility == true) {
				visibleLayers.push(layer);
			}
			
		}
		
		return visibleLayers;
		
	};

	instance.getVisibleBaseLayers = function() {

		var visibleBaseLayers = [];
		
		var layers = instance.map.gui.mapPanel.map.layers;
		
		for(var i = 0; i < layers.length; i++) {
			
			var layer = layers[i];
			
			if(layer.isBaseLayer && layer.visibility == true) {
				visibleBaseLayers.push(layer);
			}
			
		}
		
		return visibleBaseLayers;
		
	};

	instance.mapMovedCallback = function(e, instance) { };

	instance.featureAddedCallback = function(e, instance) { };

	instance.featureRemovedCallback = function(e, instance) { };

	instance.beforeFeatureAddedCallback = function(e, instance) { };

	instance.sketchCompletedCallback = function(e, instance) { };

	instance.mapLoadedEventCallback = function(object, instance) { };

	instance.layerVisibilityChangedCallback =  function(e, instance) { };

	instance.baseLayerChangedCallback =  function(e, instance) { };

	return instance;

}

function waitUntilMapIsCreated(mapInstance, config, customConfig, preview) {
	
	if(!(typeof OpenEMap === "undefined") && !(typeof OpenEMap.Client === "undefined")) {

		if(customConfig) {
			mapInstance.setCustomConfig(config, customConfig, preview);
		}
		
		mapInstance.initMapClient(config);

	} else {

		setTimeout(function() { waitUntilMapIsCreated(mapInstance, config, customConfig, preview); }, 5);

	}

}