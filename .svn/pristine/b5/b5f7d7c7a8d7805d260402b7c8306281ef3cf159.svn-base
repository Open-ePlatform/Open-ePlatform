var generalMap = {};
var generalMapQueryMinScales = {};
var generalMapQueryLanguage = {
	"ZOOMSCALE_BUTTON" : "Take me to correct zoomscale",
	"UNKOWN_ERROR_MESSAGE_TITLE" : "Unexpected error",
	"UNKOWN_ERROR_MESSAGE" : "An unexpected error occured. Contact the administrator."
};

$(function() {
	
	setQueryRequiredFunctions["GeneralMapQueryInstance"] = makeGeneralMapQueryRequired;
	
});

function initGeneralMapQuery(queryID, providerURI, searchURI, startExtent, lmUser, preview) {
	
	setTimeout(function() {
		if(!(typeof MapClientInstance === "undefined")) {
	
			var mapID = "q" + queryID;
			
			var instance = new MapClientInstance(queryID, mapID);
			
			var customConfig = { };
			
			if(!preview) {
				
				customConfig.search = {
					renderTo : mapID + "_search",
					basePath: searchURI,
					lmUser: lmUser,
					zoom: 1120
				};
				
				customConfig.searchCoordinate = {
					renderTo : mapID + "_searchcoordinate",
					zoom: generalMapQueryMinScales[mapID]
				};
				
				customConfig.objectConfig = { renderTo: mapID + "_objectconfigtool" };
				
			}
			
			if($("#" + instance.mapID + "_extent").val() != "") {
				customConfig.extent = $("#" + instance.mapID + "_extent").val();
			} else if(startExtent && startExtent != '') {
				customConfig.extent = startExtent;
			}
			
			customConfig.basePathMapFish = providerURI + "/clientprint/" + queryID;
			
			customConfig.enableEdgeLabeling = true;
			
			var generalMap = {};
			
			generalMap.mapLoaded = function(object) {
				
				var mapPanel = instance.map.gui.mapPanel;
				
				if(!instance.preview) {
	
					var mapPanel = instance.map.gui.mapPanel;
					
					mapPanel.drawLayer.styleMap = mapPanel.parseStyle(instance.config.drawStyle);
					
				    var minZoomLayerStyle = new OpenLayers.Style({
				    	"pointRadius": 10,
				    	"externalGraphic": instance.externalGraphicsLocation + "/point_denied.png"
				    });
					
				    generalMap.minZoomLayer = new OpenLayers.Layer.Vector('PointLayer', {
						displayInLayerSwitcher: false,
						styleMap:  new OpenLayers.StyleMap(minZoomLayerStyle)
				    });
					
				    generalMap.minZoomLayer.events.on({
						featureremoved: function (e) { instance.removeFeatureDialog(e.feature); }
				    });
					
					mapPanel.map.addLayer(generalMap.minZoomLayer);
					
				}
				
				var $geometries = $("input[name='" + instance.mapID + "_geometry']");
				
				if($geometries.length > 0) {
					
					generalMap.addGeometries($geometries);
					
					if(!instance.preview) {
						
						generalMap.enableAllTools();
					
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
				
				$("#" + instance.mapID + "_epsg").val(mapPanel.map.getProjection());
				$("#" + instance.mapID + "_baseLayer").val(mapPanel.map.baseLayer.name + "#" + instance.layerMapping[mapPanel.map.baseLayer.name]);
				
				if(mapPanel.map.getExtent() != null){
					$("#" + instance.mapID + "_extent").val(mapPanel.map.getExtent().toArray());	
				}
				
				mapPanel.drawLayer.events.on({ featuremodified: generalMap.mapFeatureModified });
				
			};
			
			generalMap.mapFeatureAdded = function(e) {
				
				var feature = e.feature;
				
				var id = feature.id;
	
				if(!instance.config.autoClearDrawLayer && feature.attributes.gmqOnlyOneGeometry) {
				
					var features = instance.map.gui.mapPanel.drawLayer.getFeaturesByAttribute("gmqToolid", feature.attributes.gmqToolid);
					
					$.each(features, function(i, ftre) {
						
						if(id != ftre.id) {
							instance.map.gui.mapPanel.drawLayer.removeFeatures(ftre);
						}
					});
				
				}
				
				var wtkParser = new OpenLayers.Format.WKT();
				
				var value = wtkParser.extractGeometry(feature.geometry);
				
				if(feature.attributes.config) {
					
					var jsonParser = new OpenLayers.Format.JSON();
					value += "#" + jsonParser.write(feature.attributes.config);
					
				}
				
				instance.mapDiv.after($("<input id='" + feature.id.replace(/\./g, '_') + "' type='hidden' name='" + instance.mapID + "_geometry' value='" + value + "' />"));
				
			};
			
			generalMap.mapFeatureModified = function(e) {
				
				var feature = e.feature;
				
				var wtkParser = new OpenLayers.Format.WKT();
				
				var value = wtkParser.extractGeometry(feature.geometry);
				
				if(feature.attributes.config) {
					
					var jsonParser = new OpenLayers.Format.JSON();
					value += "#" + jsonParser.write(feature.attributes.config);
					
				}
				
				$("#" + feature.id.replace(/\./g, '_')).val(value);
				
			};
			
			generalMap.mapFeatureRemoved = function(e) {
				
				var feature = e.feature;
					
				$("#" + feature.id.replace(/\./g, '_')).remove();
					
			};
			
			generalMap.beforeMapFeatureAdded = function(e) {
				
				var mapPanel = instance.map.gui.mapPanel;
				
				if (generalMapQueryMinScales[instance.mapID]) {
					
					if ((instance.map.gui.mapPanel.map.getScale() - 1) >= generalMapQueryMinScales[instance.mapID]) {
	
						generalMap.minZoomLayer.destroyFeatures();
						
						var posX = e.feature.geometry.getCentroid().x;
						var posY = e.feature.geometry.getCentroid().y;
						
						instance.mapDiv.addClass("mapquery-error");
						
						var $message = $("<div><span>" + instance.config.mapMessages.gmqIncorrectDrawingMessage + "<span><br/></div>");
						
						var $button = $("<input id='minscalebtn_" + instance.mapID + "' type='button' value='" + generalMapQueryLanguage.ZOOMSCALE_BUTTON + "' class='btn btn-blue' style='margin-top: 10px; display: inline;' />");
						
						$button.appendTo($message);
						
						var point = new OpenLayers.Geometry.Point(posX, posY);
						
						var newPoint = new OpenLayers.Feature.Vector(point);
						
						generalMap.minZoomLayer.addFeatures([newPoint]);
						
						instance.showFeatureDialog(newPoint, generalMap.minZoomLayer, $message, new OpenLayers.Size(360, 105), false);
						
						$("#minscalebtn_" + instance.mapID).on("click", function(e) {
							instance.mapDiv.removeClass("mapquery-error");
							mapPanel.map.setCenter(new OpenLayers.LonLat(posX, posY));
							mapPanel.map.zoomToScale(generalMapQueryMinScales[instance.mapID], true);
							generalMap.minZoomLayer.destroyFeatures();
						});
						
						return false;
						
					}
					
				}
				
			};
			
			generalMap.mapMoved = function(e) {
				
				$("#" + instance.mapID + "_extent").val(instance.map.gui.mapPanel.map.getExtent().toArray());
				
			};
	
			generalMap.baseLayerChanged = function(e) {
				
				$("#" + instance.mapID + "_baseLayer").val(e.layer.name + "#" + instance.layerMapping[e.layer.name]);
				
			};
	
			generalMap.destroyAllFeatures = function() {
				
				generalMap.minZoomLayer.destroyFeatures();
				instance.map.gui.mapPanel.drawLayer.destroyFeatures();
				
			};
			
			generalMap.addGeometries = function($geometries) {
				
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
						
						generalMap.drawPolygonFromConfig(id, config, layer);
						
					} else if(wkt.indexOf("POLYGON") == 0) {
						
						var coords = convertWKTToCoordinates(wkt);
						
						generalMap.drawPolygon(id, coords, layer, instance.config.drawStyle["default"]["Polygon"]);
						
					} else if(wkt.indexOf("LINESTRING") == 0) {
						
						var coords = convertWKTToCoordinates(wkt);
						
						generalMap.drawLine(id, coords, layer, instance.config.drawStyle["default"]["LineString"]);
						
					} else if(wkt.indexOf("POINT") == 0) {
						
						var coords = convertWKTToCoordinates(wkt);
	
						generalMap.drawPoint(id, coords[0], coords[1], layer, instance.config.drawStyle["default"]["Point"]);
						
					}
					
				});
				
			}
			
			generalMap.drawPolygon = function(id, coords, layer, style) {
				
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
			
			generalMap.drawPolygonFromConfig = function(id, config, layer) {
				
				var jsonParser = new OpenLayers.Format.JSON();
				
				var factory = Ext.create('OpenEMap.ObjectFactory');
				
				var feature = factory.create(jsonParser.read(config));
	
				if (id != null && id != "") {
					feature.id = id;
				}
				
				layer.addFeatures([feature]);
				
				return feature;
				
			};
			
			generalMap.drawLine = function(id, coords, layer, style) {
				
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
			
			generalMap.drawPoint = function(id, x, y, layer, style) {
				
				var feature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(x, y), null, style);
				
				if (id != null && id != "") {
					feature.id = id;
				}
				
				layer.addFeatures([feature]);
				
				return feature;
				
			};
			
			generalMap.enableAllTools = function() {
				
				instance.map.gui.toolbar.items.each(function(item, index, length) {
					item.enable();
					if(item.baseAction.control) {
						item.baseAction.control.deactivate();
					}
				});
				
			}
	
			generalMap.getTool = function(itemId) {
				
				var tool = null;
	
				instance.map.gui.toolbar.items.each(function(item, index, length) {
					if(item.itemId === itemId) {
						tool = item;
						return;
					}
				});
				
				return tool;
				
			};
			
			generalMap.pudFeatures = new Array();
			
			instance.mapLoadedEventCallback = generalMap.mapLoaded;
			instance.mapMovedCallback = generalMap.mapMoved;
			instance.featureAddedCallback = generalMap.mapFeatureAdded;
			instance.featureRemovedCallback = generalMap.mapFeatureRemoved;
			instance.sketchCompletedCallback = generalMap.beforeMapFeatureAdded;
			instance.baseLayerChangedCallback = generalMap.baseLayerChanged;
			
			instance.init(providerURI + "/mapconfiguration/" + queryID, null, customConfig, preview);
		
		}
	}, 1000);
	
}

function makeGeneralMapQueryRequired(queryID) {
	
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