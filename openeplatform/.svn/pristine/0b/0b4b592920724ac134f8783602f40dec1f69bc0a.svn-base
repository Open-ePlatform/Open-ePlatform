var mgMap = {};
var multiGeometryMapQueryMinScales = {};
var multiGeometryMapQueryLanguage = {
	"RETRIEVING_PUD" : "Retrieving property...",
	"ZOOMSCALE_BUTTON" : "Take me to correct zoomscale",
	"UNKOWN_ERROR_MESSAGE_TITLE" : "Unexpected error",
	"UNKOWN_ERROR_MESSAGE" : "An unexpected error occured. Contact the administrator.",
	"NO_PUD_FOUND_MESSAGE_TITLE" : "No property found",
	"NO_PUD_FOUND_MESSAGE" : "Can not find any property on the specified position."
};

$(function() {
	
	setQueryRequiredFunctions["MultiGeometryMapQueryInstance"] = makeMultiGeometryMapQueryRequired;
	
});

function initMultiGeometryMapQuery(queryID, providerURI, startExtent, lmSearchURL, lmUser, preview) {
	
	if(!(typeof MapClientInstance === "undefined")) {
	
		var mapID = "q" + queryID;
		
		var instance = new MapClientInstance(queryID, mapID);
		
		var customConfig = { };
		
		if(!preview) {
			
			customConfig.search = {
				renderTo : mapID + "_search",
				basePath: lmSearchURL,
				lmUser: lmUser
			};
			
			customConfig.searchCoordinate = {
				renderTo : mapID + "_searchcoordinate",
				zoom: multiGeometryMapQueryMinScales[mapID]
			};
			
			customConfig.objectConfig = { renderTo: mapID + "_objectconfigtool" };
			
		}
		
		if($("#" + instance.mapID + "_extent").val() != "") {
			customConfig.extent = $("#" + instance.mapID + "_extent").val();
		} else if(startExtent && startExtent != '') {
			customConfig.extent = startExtent;
		}
		
		customConfig.basePathMapFish = providerURI + "/clientprint";
		
		customConfig.enableEdgeLabeling = true;
		
		var mgMap = {};
		
		mgMap.mapLoaded = function(object) {
			
			var mapPanel = instance.map.gui.mapPanel;
			
			if(!instance.preview) {
				// styles for draw layer
				mapPanel.drawLayer.styleMap = mapPanel.parseStyle(instance.config.drawStyle);
				
				// styles for min zoom layer
			    var minZoomLayerStyle = new OpenLayers.Style({
			    	"pointRadius": 10,
			    	"externalGraphic": instance.externalGraphicsLocation + "/point_denied.png"
			    });
				
				mgMap.minZoomLayer = new OpenLayers.Layer.Vector('PointLayer', {
					displayInLayerSwitcher: false,
					styleMap:  new OpenLayers.StyleMap(minZoomLayerStyle)
			    });
				
				mgMap.minZoomLayer.events.on({
					featureremoved: function (e) { instance.removeFeatureDialog(e.feature); }
			    });
				
				mapPanel.map.addLayer(mgMap.minZoomLayer);
				
				// set sketch style for pud tool
				var sketchStyle = new OpenLayers.Style({
					"pointRadius": 10,
			    	"externalGraphic": instance.externalGraphicsLocation + "/point_move.png"
			    });;
					
				var tool = mgMap.getTool("SetPUD");
				
				if(tool != null) {
					
					var handler = tool.baseAction.control.handler;
					handler.layerOptions.styleMap = sketchStyle
					if(handler.layer) {
						handler.layer.styleMap = sketchStyle;
					}
					
				}
			
			}
			
			var properyUnitDesignation = $("#" + instance.mapID + "_propertyUnitDesignation").val();
			var propertUnitGeometry = $("#" + instance.mapID + "_propertyUnitGeometry").val();
			var $geometries = $("input[name='" + instance.mapID + "_geometry']");
			
			if((properyUnitDesignation != "" && propertUnitGeometry != "") || $geometries.length > 0) {
				
				if($geometries.length > 0) {
					mgMap.addGeometries($geometries);
				}
				
				if(properyUnitDesignation != "" && propertUnitGeometry != "") {
					
					var coords = convertWKTToCoordinates(propertUnitGeometry);

					var feature = mgMap.drawPoint(null, coords[0], coords[1], mapPanel.drawLayer, {
						"pointRadius": "10",
				    	"externalGraphic": instance.externalGraphicsLocation + "/pin-added.png"
				    });
					
					if(!instance.preview) {
						
						mgMap.getPUD({ feature: feature }, false);						
					
					}
					
					$("#" + instance.mapID + "_pudinfo span").text(properyUnitDesignation).parent().show();
					
				}
				
				if(!instance.preview) {
					
					mgMap.enableAllTools();
				
				}
				
			} else {
				
				if($("#" + instance.mapID + "_startinstruction").length > 0) {

					var $dialog = $("#" + instance.mapID + "_startinstruction").clone();
					
					$dialog.css({ "display": "block" });
					
					instance.showDialog($dialog, false);
				
					instance.dialog.find("a.btn").on("click", function(e) {
						instance.dialog.close(e);
					});
					
				}
				
			}
				
			var visibleBaseLayer = $("input[name='" + instance.mapID + "_baseLayer']").val();
			
			if(visibleBaseLayer != "") {
				
				var layerName = visibleBaseLayer.split("#")[0];
				
				var layers = mapPanel.map.getLayersByName(layerName);
				
				if(layers.length > 0) {
					$.each(layers, function(i, layer) {
						if(layer.isBaseLayer) {
							mapPanel.map.setBaseLayer(layer);
							return false;
						}
					});
				}
				
			}
			
			mapPanel.drawLayer.events.on({ featuremodified: mgMap.mapFeatureModified });
			
		};
		
		mgMap.mapFeatureAdded = function(e) {
			
			var feature = e.feature;
			
			var wtkParser = new OpenLayers.Format.WKT();
			
			if(feature.attributes.state == "PUD") {
				
				mgMap.getPUD(e, true);
				
				$("#" + instance.mapID + "_propertyUnitGeometry").val(wtkParser.extractGeometry(feature.geometry));
				
			} else if(feature.attributes.state == "GEOMETRY") {
			
				var value = wtkParser.extractGeometry(feature.geometry);
				
				if(feature.attributes.config) {
					
					var jsonParser = new OpenLayers.Format.JSON();
					value += "#" + jsonParser.write(feature.attributes.config);
					
				}
				
				instance.mapDiv.after($("<input id='" + feature.id.replace(/\./g, '_') + "' type='hidden' name='" + instance.mapID + "_geometry' value='" + value + "' />"));
			
			}
			
		};
		
		mgMap.mapFeatureModified = function(e) {
			
			var feature = e.feature;
			
			var wtkParser = new OpenLayers.Format.WKT();
			
			var value = wtkParser.extractGeometry(feature.geometry);
			
			if(feature.attributes.config) {
				
				var jsonParser = new OpenLayers.Format.JSON();
				value += "#" + jsonParser.write(feature.attributes.config);
				
			}
			
			$("#" + feature.id.replace(/\./g, '_')).val(value);
			
		};
		
		mgMap.mapFeatureRemoved = function(e) {
			
			var feature = e.feature;
			
			if(feature.attributes.state == "PUD") {
				
				mgMap.resetPUDSeletion();
				
			} else {
				
				$("#" + feature.id.replace(/\./g, '_')).remove();
				
			}
			
		};
		
		mgMap.getPUD = function(e, showPopup) {
			
			var gui = instance.map.gui;
			var mapPanel = gui.mapPanel;
			
			var feature = e.feature;
			
			var posX = feature.geometry.getCentroid().x;
			var posY = feature.geometry.getCentroid().y;
			
			var loadingDialog = instance.showLoadingDialog(multiGeometryMapQueryLanguage.RETRIEVING_PUD);
			
			if(mgMap.pudFeatures.length > 0) {
				mapPanel.drawLayer.removeFeatures(mgMap.pudFeatures);
			}
			
			mgMap.pudFeatures.push(feature);
			
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
						
						instance.showValidationError(multiGeometryMapQueryLanguage.NO_PUD_FOUND_MESSAGE_TITLE, multiGeometryMapQueryLanguage.NO_PUD_FOUND_MESSAGE);
						
					} else {

						var centroid = feature.geometry.getCentroid();
						var pudFeature = new OpenLayers.Format.GeoJSON().read(response)[0];
						var pud = pudFeature.attributes.name;
						
						instance.removeFeatureDialog(feature);
						
						if(showPopup) {
						
							var $pudDialog = $("#" + instance.mapID + "_puddialogtemplate");
							
							var $message = $("<div><div id='" + instance.mapID + "_puddialog' class='puddialog'>" + $pudDialog.html() + "</div></div>");
							
							$message.find("span").text(pud);
							
							instance.showFeatureDialog(feature, mapPanel.drawLayer, $message, new OpenLayers.Size(500, $pudDialog.height() + 5), true);
							
							$("#" + instance.mapID + "_puddialog").find("a.done-btn").on("click", function() {
								
								$("#" + instance.mapID + "_pudinfo span").text(pud).parent().show();
								$("#" + instance.mapID + "_propertyUnitDesignation").val(pud);
								$("#" + instance.mapID + "_extent").val(mapPanel.map.getExtent().toArray());
								$("#" + instance.mapID + "_epsg").val(mapPanel.map.getProjection());
								$("#" + instance.mapID + "_baseLayer").val(mapPanel.map.baseLayer.name + "#" + instance.layerMapping[mapPanel.map.baseLayer.name]);
								instance.removeFeatureDialog(feature);
								
								if(mapPanel.drawLayer.features.length == 1) {
								
									var $finishedMessage = $("<div class='puddialog'>" + instance.config.mapMessages.PUD_FINISHED_MESSAGE + "<br /><a class='btn btn-blue'>Ok</a></div>");
									
									instance.showDialog($finishedMessage, false);
									
									instance.dialog.find("a.btn").on("click", function(e) {
										instance.dialog.close(e);
									});
								
								}
								
								mgMap.enableAllTools();
								
							});
							
							$("#" + instance.mapID + "_puddialog").find("a.cancel-btn").on("click", function() {
	
								mapPanel.drawLayer.removeFeatures(mgMap.pudFeatures);
								mapPanel.searchLayer.destroyFeatures();
							
							});
						
						}
						
						mapPanel.searchLayer.destroyFeatures();
						mapPanel.searchLayer.addFeatures([pudFeature]);
						
						instance.mapDiv.removeClass("mapquery-error");
						
					}
					
					loadingDialog.hide();

				},
				error : function() {
					
					instance.removeValidationErrors();
					instance.showValidationError(multiGeometryMapQueryLanguage.UNKOWN_ERROR_MESSAGE_TITLE, multiGeometryMapQueryLanguage.UNKOWN_ERROR_MESSAGE);
					
					loadingDialog.hide();
					
				}
			});
			
		};
		
		mgMap.beforeMapFeatureAdded = function(e) {
			
			var mapPanel = instance.map.gui.mapPanel;
			
			if (multiGeometryMapQueryMinScales[instance.mapID]){
				
				if ((instance.map.gui.mapPanel.map.getScale() - 1) >= multiGeometryMapQueryMinScales[instance.mapID]){

					mgMap.minZoomLayer.destroyFeatures();
					
					var posX = e.feature.geometry.getCentroid().x;
					var posY = e.feature.geometry.getCentroid().y;
					
					instance.mapDiv.addClass("mapquery-error");
					
					var zoomScaleMessage = e.feature.attributes.state == "PUD" ? instance.config.mapMessages.PUD_ZOOMSCALE_MESSAGE : instance.config.mapMessages.GEOMETRY_ZOOMSCALE_MESSAGE;
					
					var $message = $("<div>" + zoomScaleMessage + "</div>");
					
					var $button = $("<input id='minscalebtn_" + instance.mapID + "' type='button' value='" + multiGeometryMapQueryLanguage.ZOOMSCALE_BUTTON + "' class='btn btn-blue' style='margin-top: 10px; display: inline;' />");
					
					$button.appendTo($message);
					
					var point = new OpenLayers.Geometry.Point(posX, posY);
					
					var newPoint = new OpenLayers.Feature.Vector(point);
					
					mgMap.minZoomLayer.addFeatures([newPoint]);
					
					instance.showFeatureDialog(newPoint, mgMap.minZoomLayer, $message, new OpenLayers.Size(360, 105), false);
					
					$("#minscalebtn_" + instance.mapID).on("click", function(e) {
						instance.mapDiv.removeClass("mapquery-error");
						mapPanel.map.setCenter(new OpenLayers.LonLat(posX, posY));
						mapPanel.map.zoomToScale(multiGeometryMapQueryMinScales[instance.mapID], true);
						mgMap.minZoomLayer.destroyFeatures();
					});
					
					return false;
					
				}
				
			}

			if(e.feature.attributes.state == "PUD") {
				
				e.feature.style = {
					"pointRadius": "10",
			    	"externalGraphic": instance.externalGraphicsLocation + "/pin-added.png"
			    };
				
			}
			
		};
		
		mgMap.mapMoved = function(e) {
			
			$("#" + instance.mapID + "_extent").val(instance.map.gui.mapPanel.map.getExtent().toArray());
			
		};

		mgMap.baseLayerChanged = function(e) {
			
			$("#" + instance.mapID + "_baseLayer").val(e.layer.name + "#" + instance.layerMapping[e.layer.name]);
			
		};

		mgMap.resetPUDSeletion = function() {
			
			$("#" + instance.mapID + "_pudinfo span").text("").parent().hide();
			$("#" + instance.mapID + "_coordinatesinfo span").text("");
			
			$("#" + instance.mapID + "_propertyUnitGeometry").val("");
			$("#" + instance.mapID + "_propertyUnitDesignation").val("");
			$("#" + instance.mapID + "_epsg").val("");
			
		};
		
		mgMap.destroyAllFeatures = function() {
			
			mgMap.minZoomLayer.destroyFeatures();
			instance.map.gui.mapPanel.drawLayer.destroyFeatures();
			
		};
		
		mgMap.addGeometries = function($geometries) {
			
			var layer = instance.map.gui.mapPanel.drawLayer;
			
			var geometryCounter = 0;
			
			$geometries.each(function(i) {
				
				var $this = $(this);
				
				var parts = $this.val().split("#");
				
				var wkt = parts[0];
				var config = parts[1];
				
				var id = "OpenLayers.Feature.Vector_" + geometryCounter++;
				
				$this.attr("id", id.replace(/\./g, '_'));
				
				if(config) {
					
					mgMap.drawPolygonFromConfig(id, config, layer);
					
				} else if(wkt.indexOf("POLYGON") == 0) {
					
					var coords = convertWKTToCoordinates(wkt);
					
					mgMap.drawPolygon(id, coords, layer, instance.config.drawStyle["default"]["Polygon"]);
					
				} else if(wkt.indexOf("LINESTRING") == 0) {
					
					var coords = convertWKTToCoordinates(wkt);
					
					mgMap.drawLine(id, coords, layer, instance.config.drawStyle["default"]["LineString"]);
					
				} else if(wkt.indexOf("POINT") == 0) {
					
					var coords = convertWKTToCoordinates(wkt);

					mgMap.drawPoint(id, coords[0], coords[1], layer, instance.config.drawStyle["default"]["Point"]);
					
				}
				
			});
			
		}
		
		mgMap.drawPolygon = function(id, coords, layer, style) {
			
			var points = new Array();
		    
			var i = 0;
		    
			for (var j = 0; j < coords.length; j += 2) {
		        points[i] = new OpenLayers.Geometry.Point(coords[j], coords[j + 1]);
		        i++;
		    }
			
			var feature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Polygon([new OpenLayers.Geometry.LinearRing(points)]), null, style);
			
			if (id != null && id != "") {
				feature.id = id;
			}
			
			layer.addFeatures([feature]);
			
			return feature;
			
		};
		
		mgMap.drawPolygonFromConfig = function(id, config, layer) {
			
			var jsonParser = new OpenLayers.Format.JSON();
			
			var factory = Ext.create('OpenEMap.ObjectFactory');
			
			var feature = factory.create(jsonParser.read(config));

			if (id != null && id != "") {
				feature.id = id;
			}
			
			layer.addFeatures([feature]);
			
			return feature;
			
		};
		
		mgMap.drawLine = function(id, coords, layer, style) {
			
			var points = new Array();
		    
			var i = 0;
		    
			for (var j = 0; j < coords.length; j += 2) {
		        points[i] = new OpenLayers.Geometry.Point(coords[j], coords[j + 1]);
		        i++;
		    }
			
			var feature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points), null, style);

			if (id != null && id != "") {
				feature.id = id;
			}
			
			layer.addFeatures([feature]);
			
			return feature;
			
		};
		
		mgMap.drawPoint = function(id, x, y, layer, style) {
			
			var feature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(x, y), null, style);
			
			if (id != null && id != "") {
				feature.id = id;
			}
			
			layer.addFeatures([feature]);
			
			return feature;
			
		};
		
		mgMap.enableAllTools = function() {
			
			instance.map.gui.leftPanel.items.each(function(item, index, length) {
				item.enable();
				if(item.baseAction.control) {
					item.baseAction.control.deactivate();
				}
			});
			
		}

		mgMap.getTool = function(itemId) {
			
			var tool = null;

			instance.map.gui.leftPanel.items.each(function(item, index, length) {
				if(item.itemId === itemId) {
					tool = item;
					return;
				}
			});
			
			return tool;
			
		};
		
		mgMap.pudFeatures = new Array();
		
		instance.mapLoadedEventCallback = mgMap.mapLoaded;
		instance.mapMovedCallback = mgMap.mapMoved;
		instance.featureAddedCallback = mgMap.mapFeatureAdded;
		instance.featureRemovedCallback = mgMap.mapFeatureRemoved;
		instance.sketchCompletedCallback = mgMap.beforeMapFeatureAdded;
		instance.baseLayerChangedCallback = mgMap.baseLayerChanged;
		
		instance.init(providerURI + "/mapconfiguration", null, customConfig, preview);
		
	}
	
}

function makeMultiGeometryMapQueryRequired(queryID) {
	
	$("#query_" + queryID).find(".heading-wrapper h2").addClass("required");
	
}

function convertWKTToCoordinates(wkt) {
	
	var verties = OpenLayers.Geometry.fromWKT(wkt.replace("Z", "")).getVertices();

	var coords = new Array();
	
	var index = 0;
	
	for (i in verties) {
		coords[index++] = verties[i].x;
		coords[index++] = verties[i].y;
	}
	
	return coords;
	
}