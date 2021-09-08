var spMap = {};
var singlePolygonMapQueryMinScales = {};
var singlePolygonMapQueryLanguage = {
	"RETRIEVING_PUD" : "Retrieving property...",
	"ZOOMSCALE_BUTTON" : "Take me to correct zoomscale",
	"UNKOWN_ERROR_MESSAGE_TITLE" : "Unexpected error",
	"UNKOWN_ERROR_MESSAGE" : "An unexpected error occured. Contact the administrator.",
	"NO_PUD_FOUND_MESSAGE_TITLE" : "No property found",
	"NO_PUD_FOUND_MESSAGE" : "Can not find any property on the specified position."
};

$(function() {
	
	setQueryRequiredFunctions["SinglePolygonMapQueryInstance"] = makeSinglePolygonMapQueryRequired;
	
});

function initSinglePolygonMapQuery(queryID, providerURI, startExtent, lmSearchURL, lmUser, preview) {
	
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
				zoom: singlePolygonMapQueryMinScales[mapID]
			};
			
			if($("#" + mapID + "_polygonConfig").length > 0) {
				customConfig.objectConfig = { renderTo: mapID + "_objectconfigtool" };
			}
			
		}

		if($("#" + instance.mapID + "_extent").val() != "") {
			customConfig.extent = $("#" + instance.mapID + "_extent").val();
		} else if(startExtent && startExtent != '') {
			customConfig.extent = startExtent;
		}

		customConfig.basePathMapFish = providerURI + "/clientprint";
		
		customConfig.enableEdgeLabeling = true;
		
		var spMap = {};
		
		spMap.mapLoaded = function(object, instance) {
			
			var mapPanel = instance.map.gui.mapPanel;
			
			mapPanel.drawLayer.styleMap = mapPanel.parseStyle(instance.config.drawStyle);
			
		    var minZoomLayerStyle = new OpenLayers.Style({
		    	"pointRadius": 10,
		    	"externalGraphic": instance.externalGraphicsLocation + "/point_denied.png"
		    });
			
			spMap.minZoomLayer = new OpenLayers.Layer.Vector('PointLayer', {
				displayInLayerSwitcher: false,
				styleMap:  new OpenLayers.StyleMap(minZoomLayerStyle)
		    });
			
			spMap.minZoomLayer.events.on({
				featureremoved: function (e) { instance.removeFeatureDialog(e.feature); }
		    });
			
			mapPanel.map.addLayer(spMap.minZoomLayer);
			
			var properyUnitDesignation = $("#" + instance.mapID + "_propertyUnitDesignation").val();
			var polygon = $("#" + instance.mapID + "_polygon").val();
			var polygonConfig = $("#" + instance.mapID + "_polygonConfig").val();
			
			if(properyUnitDesignation != "" && polygon != "" && (!instance.config.gui.objectConfig || polygonConfig != "")) {
				
				var feature;
				
				if(instance.preview) {
					
					spMap.drawPolygon(polygon, mapPanel.drawLayer, instance.config.drawStyle["default"]["Polygon"]);
					
				} else if(!instance.config.gui.objectConfig) {
					
					feature = spMap.drawPolygon(polygon, mapPanel.drawLayer, instance.config.drawStyle["default"]["Polygon"]);
					
					spMap.getPUD({ feature: feature }, instance, false);
					
				} else {
					
					var jsonParser = new OpenLayers.Format.JSON();
					
					feature = spMap.drawPolygonFromConfig(jsonParser.read(polygonConfig), mapPanel.drawLayer);
					
					spMap.getPUD({ feature: feature }, instance, false);
					
				}
				
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
			
			mapPanel.drawLayer.events.on({
				
				featuremodified: function (e) { 
					
					var x = e.feature.attributes.config.lastPosition.x;
					var y = e.feature.attributes.config.lastPosition.y;
					
					var newCentroid = e.feature.geometry.getCentroid();
					
					var diffX = Number(newCentroid.x-x);
					var diffY = Number(newCentroid.y-y);
					var maxTolerance = 0.00001;
					var minTolerance = -0.00001;
					
					if(diffX > maxTolerance || diffX < minTolerance || diffY > maxTolerance || diffY < minTolerance) {
						
						spMap.getPUD(e, instance, true);
						
					} else {
						
						spMap.updatePolygonInfo(e.feature, instance);
						
					}
					
				}
				
		    });
			
			if(instance.config.gui.searchCoordinate) {
				$("#" + instance.mapID + "_searchcoordinate").find("span.epsg").text(" (" + mapPanel.map.getProjection() + ")");
			}
			
		};
		
		spMap.mapFeatureAdded = function(e, instance) {
			
			spMap.getPUD(e, instance, true);
			
		};
		
		spMap.getPUD = function(e, instance, enableModifyGeometryTools) {
			
			var gui = instance.map.gui;
			var mapPanel = gui.mapPanel;
			
			var feature = e.feature;
			
			if(enableModifyGeometryTools) {
				gui.activeAction = gui.toolbar.getComponent("ModifyGeometry").baseAction;
				gui.activeAction.control.activate();
				gui.activeAction.control.selectFeature(feature);
			} else {
				if (gui.controlToActivate) {
					gui.controlToActivate.deactivate();
				}
				if(gui.objectConfig) {
					gui.objectConfig.hide();
				}
			}
			
			var posX = feature.geometry.getCentroid().x;
			var posY = feature.geometry.getCentroid().y;
			
			var loadingDialog = instance.showLoadingDialog(singlePolygonMapQueryLanguage.RETRIEVING_PUD);
			
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
						
						instance.showValidationError(singlePolygonMapQueryLanguage.NO_PUD_FOUND_MESSAGE_TITLE, singlePolygonMapQueryLanguage.NO_PUD_FOUND_MESSAGE);
						
					} else {

						var contentHeight = 35;
						var centroid = feature.geometry.getCentroid();
						var pudFeature = new OpenLayers.Format.GeoJSON().read(response)[0];
						var pud = pudFeature.attributes.name;
						
						if(feature.attributes.config == undefined) {
							feature.attributes.config = {};
						}
						
						feature.attributes.config.lastPosition = centroid;
						
						spMap.updatePolygonInfo(feature, instance);
						
						$("#" + instance.mapID + "_pudinfo span").text(pud).parent().show();
						
						if(instance.config.gui.searchCoordinate) {
							
							var mapProjection = mapPanel.map.getProjection();
							
							$("#" + instance.mapID + "_coordinatesinfo span").text(Math.round(centroid.x) + ", " + Math.round(centroid.y) + " (" + mapProjection + ")");
							
							contentHeight += 20;
							
						}
						
						$("#" + instance.mapID + "_propertyUnitDesignation").val(pud);
						$("#" + instance.mapID + "_extent").val(mapPanel.map.getExtent().toArray());
						$("#" + instance.mapID + "_epsg").val(mapPanel.map.getProjection());
						$("#" + instance.mapID + "_baseLayer").val(mapPanel.map.baseLayer.name + "#" + instance.layerMapping[mapPanel.map.baseLayer.name]);
						
						instance.removeFeatureDialog(feature);
						
						if(feature.attributes.config.lastPUD != pud) {
							
							var $message = $("<div><div class='pudinfo'>" + 
									$("#" + instance.mapID + "_pudinfo").html() + 
									(instance.config.gui.searchCoordinate ? "<br/>" + $("#" + instance.mapID + "_coordinatesinfo").html() : "") +
									"</div></div>");
							
							instance.showFeatureDialog(feature, mapPanel.drawLayer, $message, new OpenLayers.Size(360, contentHeight), true);
							
						}
						
						feature.attributes.config.lastPUD = pud;
						
						mapPanel.searchLayer.destroyFeatures();
						mapPanel.searchLayer.addFeatures([pudFeature]);
						
						instance.mapDiv.removeClass("mapquery-error");
						
					}
					
					loadingDialog.hide();

				},
				error : function() {
					
					instance.removeValidationErrors();
					instance.showValidationError(singlePolygonMapQueryLanguage.UNKOWN_ERROR_MESSAGE_TITLE, singlePolygonMapQueryLanguage.UNKOWN_ERROR_MESSAGE);
					
					loadingDialog.hide();
					
				}
			});
			
		};
		
		spMap.mapFeatureRemoved = function(e, instance) {
			
			spMap.resetPUDSeletion(instance);
			
		};
		
		spMap.beforeMapFeatureAdded = function(e, instance) {
			
			var mapPanel = instance.map.gui.mapPanel;
			
			if (singlePolygonMapQueryMinScales[instance.mapID]){
				
				if ((instance.map.gui.mapPanel.map.getScale() - 1) >= singlePolygonMapQueryMinScales[instance.mapID]){

					spMap.destroyAllFeatures(instance);
					
					var posX = e.feature.geometry.getCentroid().x;
					var posY = e.feature.geometry.getCentroid().y;
					
					instance.mapDiv.addClass("mapquery-error");
					
					var $message = $("<div>" + instance.config.mapMessages.ZOOMSCALE_MESSAGE + "</div>");
					
					var $button = $("<input id='minscalebtn_" + instance.mapID + "' type='button' value='" + singlePolygonMapQueryLanguage.ZOOMSCALE_BUTTON + "' class='btn btn-blue' style='margin-top: 10px; display: inline;' />");
					
					$button.appendTo($message);
					
					var point = new OpenLayers.Geometry.Point(posX, posY);
					
					var newPoint = new OpenLayers.Feature.Vector(point);
					
					spMap.minZoomLayer.addFeatures([newPoint]);
					
					instance.showFeatureDialog(newPoint, spMap.minZoomLayer, $message, new OpenLayers.Size(360, 105), false);
					
					$("#minscalebtn_" + instance.mapID).click(function(e) {
						instance.mapDiv.removeClass("mapquery-error");
						mapPanel.map.setCenter(new OpenLayers.LonLat(posX, posY));
						mapPanel.map.zoomToScale(singlePolygonMapQueryMinScales[instance.mapID], true);
						spMap.destroyAllFeatures(instance);
					});
					
					return false;
					
				}
				
			}
			
			spMap.destroyAllFeatures(instance);
			
		};
		
		spMap.mapMoved = function(e, instance) {
			
			$("#" + instance.mapID + "_extent").val(instance.map.gui.mapPanel.map.getExtent().toArray());
			
		};

		spMap.baseLayerChanged = function(e, instance) {
			
			$("#" + instance.mapID + "_baseLayer").val(e.layer.name + "#" + instance.layerMapping[e.layer.name]);
			
		};

		spMap.resetPUDSeletion = function(instance) {
			
			$("#" + instance.mapID + "_pudinfo span").text("").parent().hide();
			$("#" + instance.mapID + "_coordinatesinfo span").text("");
			
			$("#" + instance.mapID + "_propertyUnitDesignation").val("");
			$("#" + instance.mapID + "_polygon").val("");
			$("#" + instance.mapID + "_epsg").val("");
			
		};
		
		spMap.destroyAllFeatures = function(instance) {
			
			spMap.minZoomLayer.destroyFeatures();
			instance.map.gui.mapPanel.drawLayer.destroyFeatures();
			
		};
		
		spMap.drawPolygonFromConfig = function(config, layer) {
			
			var factory = Ext.create('OpenEMap.ObjectFactory');
			
			var feature = factory.create(config);

			layer.addFeatures([feature]);
			
			return feature;
			
		};
		
		spMap.drawPolygon = function(wkt, layer, style) {
			
			var verties = OpenLayers.Geometry.fromWKT(wkt.replace("Z", "")).getVertices();

			var coords = new Array();
			
			var index = 0;
			
			for (i in verties) {
				coords[index++] = verties[i].x;
				coords[index++] = verties[i].y;
			}
			
			var points = new Array();
		    
			var i = 0;
		    
			for (var j = 0; j < coords.length; j += 2) {
		        points[i] = new OpenLayers.Geometry.Point(coords[j], coords[j + 1]);
		        i++;
		    }
			
			var feature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Polygon([new OpenLayers.Geometry.LinearRing(points)]), null, style);
			
			layer.addFeatures([feature]);
			
			return feature;
			
		};
		
		spMap.updatePolygonInfo = function(feature, instance) {
			
			var wtkParser = new OpenLayers.Format.WKT();
			
			$("#" + instance.mapID + "_polygon").val(wtkParser.extractGeometry(feature.geometry));
			
			if(instance.config.gui.objectConfig) {
				var jsonParser = new OpenLayers.Format.JSON();
				$("#" + instance.mapID + "_polygonConfig").val(jsonParser.write(feature.attributes.config));
			}
			
		};
		
		instance.mapLoadedEventCallback = spMap.mapLoaded;
		instance.mapMovedCallback = spMap.mapMoved;
		instance.featureAddedCallback = spMap.mapFeatureAdded;
		instance.featureRemovedCallback = spMap.mapFeatureRemoved;
		instance.sketchCompletedCallback = spMap.beforeMapFeatureAdded;
		instance.baseLayerChangedCallback = spMap.baseLayerChanged;
		
		instance.init(providerURI + "/mapconfiguration", null, customConfig, preview);
		
	}
	
}

function makeSinglePolygonMapQueryRequired(queryID) {
	
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