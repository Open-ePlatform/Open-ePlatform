var pudMapQueryMinScales = {};
var pudMapQueryLanguage = {
		"RETRIEVING_PUD" : "Retrieving property...",
		"ZOOMSCALE_BUTTON" : "Take me to correct zoomscale",
		"UNKOWN_ERROR_MESSAGE_TITLE" : "Unexpected error",
		"UNKOWN_ERROR_MESSAGE" : "An unexpected error occured. Contact the administrator.",
		"NO_PUD_FOUND_MESSAGE_TITLE" : "No property found",
		"NO_PUD_FOUND_MESSAGE" : "Can not find any property on the specified position."
};

$(document).ready(function() {
	
	setQueryRequiredFunctions["PUDMapQueryInstance"] = makePUDMapQueryRequired;
	
});

function initPUDMapQuery(queryID, providerURI, startExtent, lmSearchURL, lmUser, preview) {
	
	 setTimeout(function() {
		 if(!(typeof MapClientInstance === "undefined")) {
	
			var mapID = "q" + queryID;
			
			var instance = new MapClientInstance(queryID, mapID);		
			
			var customConfig = {
				"search": {
					renderTo : mapID + "_search",
					basePath: lmSearchURL,
					lmUser: lmUser
				},
			};
			
			if($("#" + mapID + "_extent").val() != "") {
				customConfig.extent = $("#" + mapID + "_extent").val();
			} else if(startExtent && startExtent != '') {
				customConfig.extent = startExtent;
			}
			
			customConfig.basePathMapFish = providerURI + "/clientprint";
			
			instance.mapLoadedEventCallback = mapLoaded;
			instance.mapMovedCallback = mapMoved;
			
			instance.init(providerURI + "/mapconfiguration", null, customConfig, preview);
		
	 	}
	 }, 1000);
	
}

function mapLoaded(object, instance) {
	
	// Define styles for features
	
	var options = getDrawGeometryOptions(instance);
	
	var styleTemplate = {
		"pointRadius": "${getSize}",
    	"externalGraphic": "${getGraphic}"
    };
	
	var featureStyleContext = {
        getGraphic: function(feature) {                    
        	if($("#" + instance.mapID + "_propertyUnitDesignation").val() == "" && (instance.map.gui.mapPanel.map.getScale() - 1) >= pudMapQueryMinScales[instance.mapID]) {
        		return instance.externalGraphicsLocation + "/" + options.pointDeniedIcon.externalGraphic;
        	} else {
    			return instance.externalGraphicsLocation + "/" + options.pointAddedIcon.externalGraphic;
        	}
        },
        getSize: function(feature) {              
        	if($("#" + instance.mapID + "_propertyUnitDesignation").val() == "" && (instance.map.gui.mapPanel.map.getScale() - 1) >= pudMapQueryMinScales[instance.mapID]) {
        		return options.pointDeniedIcon.pointRadius;
        	} else {
    			return options.pointAddedIcon.pointRadius;
        	}
        }
    };
	
    var drawLayerStyle = new OpenLayers.Style(styleTemplate, {context: featureStyleContext});

	instance.map.drawLayer.styleMap = new OpenLayers.StyleMap(drawLayerStyle);
	
	// Define styles for sketch helper
	
	var sketchStyleContext = {
        getGraphic: function(feature) {                    
        	return instance.externalGraphicsLocation + "/" + options.pointMoveIcon.externalGraphic;
        },
        getSize: function(feature) {
        	return options.pointMoveIcon.pointRadius;
        }
    };
	
	var sketchStyle = new OpenLayers.Style(styleTemplate, {context: sketchStyleContext});
	
	instance.map.setSketchStyleMap(sketchStyle);
	
	// check if any validation errors
	
	var properyUnitDesignation = $("#" + instance.mapID + "_propertyUnitDesignation").val();
	var posX = $("#" + instance.mapID + "_xCoordinate").val();
	var posY = $("#" + instance.mapID + "_yCoordinate").val();
	
	if(properyUnitDesignation != "" && posX != "" && posY != "") {
		
		var point = new OpenLayers.Geometry.Point(posX, posY);
		var feature = new OpenLayers.Feature.Vector(point);
		instance.map.drawLayer.addFeatures([feature]);
		
		$("#" + instance.mapID + "_coordinatesinfo span").text(posX + ", " + posY).parent().show();
		
		$("#" + instance.mapID + "_pudinfo span").text(properyUnitDesignation).parent().show();
			
	} else {
		
		if($("#" + instance.mapID + "_startinstruction").length > 0) {

			var $dialog = $("#" + instance.mapID + "_startinstruction").clone();
			
			$dialog.css({ "display": "block" });
			
			instance.showDialog($dialog, false);
		
			instance.dialog.find("a.btn").click(function(e) {
				instance.dialog.close(e);
			});
			
		}
		
	}
	
	// set base layer
	
	var visibleBaseLayer = $("input[name='" + instance.mapID + "_baseLayer']").val();
	
	if(visibleBaseLayer != "") {
		
		var layerName = visibleBaseLayer.split("#")[0];
		
		var layers = instance.map.gui.mapPanel.map.getLayersByName(layerName);
		
		if(layers.length > 0) {
			$.each(layers, function(i, layer) {
				if(layer.isBaseLayer) {
					instance.map.gui.mapPanel.map.setBaseLayer(layer);
					return false;
				}
			});
		}
		
	}
	
	// set callbacks
	
	instance.featureAddedCallback = mapFeatureAdded;
	instance.featureRemovedCallback = mapFeatureRemoved;
	instance.baseLayerChangedCallback = baseLayerChanged;
	
	// Temporary until config.autoClearDrawLayer is fixed in MapClient 
	instance.map.gui.mapPanel.drawLayer.events.register('beforefeatureadded', null, function() {
		instance.map.gui.mapPanel.drawLayer.destroyFeatures();
	});
	
}

function mapFeatureAdded(e, instance) {
	
	var mapPanel = instance.map.gui.mapPanel;
	
	var feature = e.feature;
	
	var posX = feature.geometry.getCentroid().x;
	var posY = feature.geometry.getCentroid().y;
	
	if (pudMapQueryMinScales[instance.mapID]){
		
		if ((mapPanel.map.getScale() - 1) >= pudMapQueryMinScales[instance.mapID]){

			instance.mapDiv.addClass("mapquery-error");
			
			mapPanel.searchLayer.destroyFeatures();
			
			var $message = $("<div>" + instance.config.mapMessages.ZOOMSCALE_MESSAGE + "</div>");
			
			var $button = $("<input id='minscalebtn_" + instance.mapID + "' type='button' value='" + pudMapQueryLanguage.ZOOMSCALE_BUTTON + "' class='btn btn-blue' style='margin-top: 10px; display: inline;' />");
			
			$button.appendTo($message);
			
			instance.showFeatureDialog(feature, mapPanel.drawLayer, $message, new OpenLayers.Size(360, 105), false);
			
			$("#minscalebtn_" + instance.mapID).click(function(e) {
				instance.mapDiv.removeClass("mapquery-error");
				mapPanel.map.setCenter(new OpenLayers.LonLat(posX, posY));
				mapPanel.map.zoomToScale(pudMapQueryMinScales[instance.mapID], true);
				mapPanel.drawLayer.destroyFeatures();
			});
			
			return;
		}
		
	}
	
	var loadingDialog = instance.showLoadingDialog(pudMapQueryLanguage.RETRIEVING_PUD);
	
	$.ajax({
		url : instance.map.basePath + "enhetsomraden",
		dataType : "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			x : posX,
			y : posY
		},
		success : function(response) {
			
			instance.removeValidationErrors();
			
			if(response.Error) {
				
				instance.showValidationError(pudMapQueryLanguage.NO_PUD_FOUND_MESSAGE_TITLE, pudMapQueryLanguage.NO_PUD_FOUND_MESSAGE);
				
			} else {
				
				var pudFeature = new OpenLayers.Format.GeoJSON().read(response)[0];
				var pud = pudFeature.attributes.name;
				
				var mapProjection = instance.map.gui.mapPanel.map.getProjection();
				
				$("#" + instance.mapID + "_pudinfo span").text(pud).parent().show();
				$("#" + instance.mapID + "_coordinatesinfo span").text(Math.round(posX) + ", " + Math.round(posY) + " (" + mapProjection + ")").parent().show();
				
				$("#" + instance.mapID + "_propertyUnitDesignation").val(pud);
				$("#" + instance.mapID + "_xCoordinate").val(posX);
				$("#" + instance.mapID + "_yCoordinate").val(posY);
				$("#" + instance.mapID + "_extent").val(mapPanel.map.getExtent().toArray());
				$("#" + instance.mapID + "_epsg").val(mapProjection);
				$("#" + instance.mapID + "_baseLayer").val(mapPanel.map.baseLayer.name + "#" + instance.layerMapping[mapPanel.map.baseLayer.name]);
				
				var $message = $("<div><div class='pudinfo'>" + $("#" + instance.mapID + "_pudinfo").html() + "<br/>" + $("#" + instance.mapID + "_coordinatesinfo").html() + "</div></div>");
				
				instance.showFeatureDialog(feature, mapPanel.drawLayer, $message, new OpenLayers.Size(360, 55), true);
				
				mapPanel.searchLayer.destroyFeatures();
				mapPanel.searchLayer.addFeatures([pudFeature]);
				
				instance.mapDiv.removeClass("mapquery-error");
				
			}
			
			loadingDialog.hide();
			
		},
		error : function() {
			
			instance.removeValidationErrors();
			instance.showValidationError(pudMapQueryLanguage.UNKOWN_ERROR_MESSAGE_TITLE, pudMapQueryLanguage.UNKOWN_ERROR_MESSAGE);
			
			loadingDialog.hide();
			
		}
	});
	
}

function mapFeatureRemoved(e, instance) {
	
	resetPUDSeletion(instance);
	
}

function mapMoved(e, instance) {
	
	$("#" + instance.mapID + "_extent").val(instance.map.gui.mapPanel.map.getExtent().toArray());
	
}

function baseLayerChanged(e, instance) {
	
	$("#" + instance.mapID + "_baseLayer").val(e.layer.name + "#" + instance.layerMapping[e.layer.name]);
	
}

function makePUDMapQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}

function resetPUDSeletion(instance) {
	
	$("#" + instance.mapID + "_pudinfo span").text("").parent().hide();
	$("#" + instance.mapID + "_coordinatesinfo span").text("").parent().hide();
	
	$("#" + instance.mapID + "_propertyUnitDesignation").val("");
	$("#" + instance.mapID + "_xCoordinate").val("");
	$("#" + instance.mapID + "_yCoordinate").val("");
	$("#" + instance.mapID + "_epsg").val("");
	
}

function getDrawGeometryOptions(instance) {
	
	var options = {
		"pointAddedIcon": {
			"pointRadius": 10,
			"externalGraphic": "point_added.png"
		},
		"pointMoveIcon": {
			"pointRadius": 10,
			"externalGraphic": "point_move.png"
		},
		"pointDeniedIcon": {
			"pointRadius": 10,
			"externalGraphic": "point_denied.png"
		}
	};
	
	$.each(instance.originalTools, function(i, tool) {
		
		if(tool.type == "DrawGeometry" && tool.options){
			options = $.extend(options, tool.options);
			return;
		}
		
	});
	
	return options;
}