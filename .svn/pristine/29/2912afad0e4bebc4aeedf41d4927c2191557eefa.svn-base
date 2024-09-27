/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Base class for adding common functionality upon GeoExt.Action
 * @param {Object} [config] configuration object setting common action properties on cretaion of an action
 * @param {string} [config.minScale] disable tool below this scale
 * @param {string} [config.maxScale] disable tool above this scale
 */
Ext.define('OpenEMap.action.Action', {
    extend:  GeoExt.Action ,
    constructor: function(config) {
        var mapPanel = config.mapPanel;
        var map = mapPanel.map;
                
        if (config.minScale || config.maxScale) {
            if (!config.minScale) config.minScale = 0;
            if (!config.maxScale) config.maxScale = 99999999999999;
            
            var onZoomend = function() {
                if (map.getScale() >= config.maxScale ||
                    map.getScale() <= config.minScale) {
                    this.disable();
                } else {
                    this.enable();
                }
            };
            
            map.events.register('zoomend', this, onZoomend);
        }
        
        this.callParent(arguments);
    },
    /**
     * To be implemented by actions that need special logic on toggle
     * @abstract
     */
    toggle: function() {}
});

/**
 * Action for delete all features in map
 */
Ext.define('OpenEMap.action.DeleteAllFeatures', {
    extend:  OpenEMap.action.Action ,
    
    constructor : function(config){
        
         config.control = new OpenLayers.Control.Button({
            trigger: function() {
                config.mapPanel.drawLayer.removeAllFeatures();
            }
        });

        config.iconCls = config.iconCls || 'action-deleteallfeatures';
        config.tooltip = config.tooltip || 'Rensa kartan fr&aring;n ritade objekt.';
        
        this.callParent(arguments);
    }
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action to delete geometry
 * 
 * The snippet below is from configuration to MapClient.view.Map
 * 
 *      "tools" : [{
 *            "type": "DeleteGeometry",
 *            "tooltip": "Ta bort valt objekt/geometri"
 *        }]
 */
Ext.define('OpenEMap.action.DeleteGeometry', {
    extend:  OpenEMap.action.Action ,
    /**
     * @param config
     * @param {string} config.typeAttribute string to write to new feature attribute type
     */
    constructor: function(config) {
        var mapPanel = config.mapPanel;
        var layer = mapPanel.drawLayer;
        
        config.handler = function() {
            layer.selectedFeatures.forEach(function(feature) {
                mapPanel.map.controls.forEach(function(control) {
                    if (control.CLASS_NAME == "OpenLayers.Control.ModifyFeature" && control.active) {
                        control.unselectFeature(feature);
                    }
                });
                layer.destroyFeatures([feature]);
            });
        };
        
        config.iconCls = config.iconCls || 'action-deletegeometry';
        config.tooltip = config.tooltip || 'Ta bort ritat objekt';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action for delete measurements
 */
Ext.define('OpenEMap.action.DeleteMeasure', {
    extend:  OpenEMap.action.Action ,
    
    constructor : function(config){
        
         config.control = new OpenLayers.Control.Button({
            trigger: function(){
            
                config.mapPanel.measureLayer.removeAllFeatures();
                config.mapPanel.measureLayerArea.removeAllFeatures();
                config.mapPanel.measureLayerLength.removeAllFeatures();
                config.mapPanel.measureLayerSegments.removeAllFeatures();

                config.mapPanel.map.controls.forEach(function(c){
                    if(c instanceof OpenLayers.Control.DynamicMeasure){
                        c.deactivate();
                    }
                });
            }
        });

        config.iconCls = config.iconCls || 'action-deletemeasure';
        config.tooltip = config.tooltip || 'Ta bort m&auml;tning(ar).';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * 
 */
Ext.define('OpenEMap.view.DetailReportResults', {
    extend :  Ext.view.View ,
    autoScroll : true,
    padding: 5,
    geometry: null,
    initComponent : function() {
        this.store = Ext.create('GeoExt.data.FeatureStore', {
            features: [],
            fields: ['COUNT',
                     'CATEGORY',
                     'CLARIFICAT',
                     'DESCRIPTIO',
                     'REMARK',
                     'MAPTEXT',
                     'MAX',
                     'MIN',
                     'HEIGHT'
                     ]
        });
        
        this.tpl = new Ext.XTemplate(
                '<h3>' + this.fbet + '</h3>',
                '<h4>' + this.aktbet + '</h4>',
                '<tpl for=".">',
                    '<div style="margin-bottom: 10px;" class="thumb-wrap">',
                      '<h4>{COUNT}. {DESCRIPTIO}</h4>',
                      '<p>{REMARK}</p>',
                    '</div>',
                '</tpl>'
            );
        this.itemSelector = 'div.thumb-wrap';

        this.callParent(arguments);
        
        this.doSearch();
    },
    
    doSearch: function() {
        var store = this.store;
        var layer = this.layer;
        var geometry = this.geometry;
        
        layer.destroyFeatures();
        
        var options = Ext.apply({
            url: "wfs",
            version: "1.1.0",
            srsName: "EPSG:3006",
            featureType: "EgenskapsBestammelser_yta",
            featurePrefix: "RIGES"
        });
        
        var protocol = new OpenLayers.Protocol.WFS(options);
        
        protocol.read({
            filter: new OpenLayers.Filter({
                type: OpenLayers.Filter.Spatial.INTERSECTS,
                value: geometry
            }),
            callback: function(response) {
                var features = response.features;
                if (!features) return;
                features.forEach(function(feature) {
                    feature.attributes.COUNT = store.getCount()+1;
                    store.addFeatures([feature]);
                });
                layer.addFeatures(features);
            }
        });
    },
    
    onSelect: function(model, record, index) {
        var source = {};
        var feature = record.raw.feature;
        var layer = record.raw.layer;
        
        var filterAttributesMeta = function(key) {
            if (layer.metadata.attributes[key]) {
                var alias = layer.metadata.attributes[key].alias || key;
                source[alias] = feature.attributes[key];
            }
        };
        
        if (feature) {
            if (layer.metadata && layer.metadata.attributes) {
                Object.keys(feature.attributes).forEach(filterAttributesMeta);
            } else {
                source = feature.attributes;
            }
            this.mapPanel.searchLayer.selectedFeatures.forEach(function(feature) {
                this.mapPanel.selectControl.unselect(feature);
            }, this);
            if (record.raw.feature.layer) {
                this.mapPanel.selectControl.select(feature);
            }
        }
    }
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
Ext.define('OpenEMap.action.DetailReport', {
    extend:  OpenEMap.action.Action ,
                                                    
    
    constructor: function(config) {
        
        var mapPanel = config.mapPanel;
        var layer = mapPanel.searchLayer;
        var map = config.map;
        
        var Click = OpenLayers.Class(OpenLayers.Control, {
            initialize: function(options) {
                OpenLayers.Control.prototype.initialize.apply(
                    this, arguments
                );
                this.handler = new OpenLayers.Handler.Click(
                    this, {
                        'click': this.onClick
                    }, this.handlerOptions
                );
            },
            onClick: function(evt) {
                mapPanel.setLoading(true);
                layer.destroyFeatures();
                
                var lonlat = map.getLonLatFromPixel(evt.xy);
                
                var x = lonlat.lon;
                var y = lonlat.lat;
                
                var point = new OpenLayers.Geometry.Point(x, y);
                var feature = new OpenLayers.Feature.Vector(point);
                layer.addFeatures([feature]);
                
                OpenEMap.requestLM({
                    url: 'enhetsomraden?x=' + x + '&y=' + y,
                    success: function(response) {
                        var features = new OpenLayers.Format.GeoJSON().read(response.responseText);
                        layer.addFeatures(features);
                        var extent = layer.getDataExtent();
                        map.zoomToExtent(extent);
                        
                        var geometry = features[0].geometry;
                        var fbet = features[0].attributes.name;
                        
                        var options = Ext.apply({
                            url: "wfs",
                            version: "1.1.0",
                            srsName: "EPSG:3006",
                            featureType: "DetaljplanGallande_yta",
                            featurePrefix: "RIGES"
                        });
                        
                        var protocol = new OpenLayers.Protocol.WFS(options);
                        
                        protocol.read({
                            filter: new OpenLayers.Filter({
                                type: OpenLayers.Filter.Spatial.INTERSECTS,
                                value: geometry
                            }),
                            callback: function(response) {
                                var features = response.features;
                                if (features && features.length>0) {
                                	
                                    layer.addFeatures(features);
                                    
                                    var detailReportResults = Ext.create('OpenEMap.view.DetailReportResults', {
                                        mapPanel : mapPanel,
                                        fbet: fbet,
                                        aktbet: features[0].attributes.AKTBET,
                                        geometry: features[0].geometry,
                                        layer: mapPanel.drawLayer
                                    });
                                    
                                    var popup = Ext.create('GeoExt.window.Popup', {
                                        title: 'Rapport',
                                        anchored: false,
                                        unpinnable: false,
                                        draggable: true,
                                        map: mapPanel,
                                        maximizable : false,
                                        minimizable : false,
                                        resizable: true,
                                        width: 300,
                                        height: 400,
                                        layout: 'fit',
                                        items: detailReportResults,
                                        collapsible: false
                                    });
                                    
                                    popup.show();
                                }
                            }
                        });
                        
                    },
                    scope: this,
                    callback: function() {
                        mapPanel.setLoading(false);
                    }
                });
            }
        });
        
        config.control = new Click({
            type: OpenLayers.Control.TYPE_TOGGLE
        });
        
        config.iconCls = config.iconCls || 'action-detailreport';
        config.tooltip = config.tooltip || 'Detaljerad rapport';
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action for draw geometry
 * 
 * The snippet below is from configuration to MapClient.view.Map
 *
 *         "tools" : [{
 *           "type": "DrawGeometry",
 *           "geometry": "Path",
 *           "tooltip": "Markera väg",
 *           "attributes": {
 *             "type": "Väg"
 *             "metadata": {
 *               "type": {
 *                 "hidden": true
 *               }
 *             }
 *           }
 *       }]
 *       
 * NOTE: metadata attribute can be used to hide another attributes from showing up in ObjectConfig dialog.
 *  
 * DrawGeometry can also be used to draw text features, since they are simply point features with an attribute to be labeled with styling.
 */
Ext.define('OpenEMap.action.DrawGeometry', {
    extend:  OpenEMap.action.Action ,

    isText : function(feature){
        if (feature){
            var isPoint = feature.geometry === 'Point' || feature.geometry instanceof OpenLayers.Geometry.Point;
            if (isPoint){
                var isText =  feature.attributes && 
                    feature.attributes.type && 
                    feature.attributes.type === 'label';
                return isText;
            }
        }
        return false;
    },

    /**
     * @param config
     * @param {string} config.typeAttribute string to write to new feature attribute type
     * @param {boolean} config.singleObject Set to true to clear layer before adding feature effectively restricing 
     */
    constructor: function(config) {
        var mapPanel = config.mapPanel;
        var layer = mapPanel.drawLayer;


        config.attributes = config.attributes || {};
        
        config.geometry = config.geometry || 'Polygon';
        
        var Control = OpenLayers.Class(OpenLayers.Control.DrawFeature, {
            // NOTE: override drawFeature to set custom attributes
            drawFeature: function(geometry) {
                var feature = new OpenLayers.Feature.Vector(geometry, config.attributes, config.style);
                var proceed = this.layer.events.triggerEvent(
                    "sketchcomplete", {feature: feature}
                );
                if(proceed !== false) {
                    feature.state = OpenLayers.State.INSERT;
                    this.layer.addFeatures([feature]);
                    this.featureAdded(feature);
                    this.events.triggerEvent("featureadded",{feature : feature});
                }
            }
        });
        
        config.control = new Control(layer, OpenLayers.Handler[config.geometry]);

        layer.events.register('beforefeatureadded', this, function(evt){
            if (this.isText(evt.feature)) {
            	if (evt.feature.state || evt.feature.state === 'insert') {
	                Ext.Msg.prompt('Text', 'Mata in text:', function(btn, text){
	                    if (btn == 'ok'){
	                        evt.feature.attributes.label = text;
	                        evt.feature.data.label = text;
	                        layer.redraw();
	                    }
	                });
	            } 
            }
        });

        config.iconCls = config.geometry === 'Polygon' ? 'action-drawpolygon' :
     		config.geometry === 'Path' ? 'action-drawline' :
     		config.geometry === 'Point' ? 'action-drawpoint' : 'action-drawgeometry';
                
       	if (!config.tooltip){
       		config.tooltip = config.geometry === 'Polygon' ? 'Rita område' :
         		config.geometry === 'Path' ? 'Rita linje' :
         		config.geometry === 'Point' ? 'Rita punkt' : 'Rita geometri';
         	if (this.isText(config)){
         		config.tooltip = 'Placera ut text.';	
         		config.iconCls = 'action-drawtext';
         	}
       	}
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Creates predefined objects
 */
Ext.define('OpenEMap.ObjectFactory', {

    toPoint: function(coord) {
        return new OpenLayers.Geometry.Point(coord[0], coord[1]);
    },
    
    createR: function(config) {
        var x = config.x;
        var y = config.y;
        var l = config.l;
        var w = config.w;
        var coords = [
            [x    , y],
            [x    , y - l],
            [x + w, y - l],
            [x + w, y]
        ];
        var linearRing = new OpenLayers.Geometry.LinearRing(coords.map(this.toPoint));
        var polygon = new OpenLayers.Geometry.Polygon([linearRing]);
        return polygon;
    },
    
    createL: function(config) {
        var x = config.x;
        var y = config.y;
        var l = config.l;
        var w = config.w;
        var m1 = config.m1;
        var m2 = config.m2;
        var coords = [
            [x     , y],
            [x     , y - w],
            [x + m1, y - w],
            [x + m1, y - m2],
            [x + l , y - m2],
            [x + l , y]
        ];
        var linearRing = new OpenLayers.Geometry.LinearRing(coords.map(this.toPoint));
        var polygon = new OpenLayers.Geometry.Polygon([linearRing]);
        return polygon;
    },
            
    createD: function(config) {
        var x = config.x;
        var y = config.y;
        var l = config.l;
        var w = config.w;
        var m1 = config.m1;
        var m2 = config.m2;
        
        var o = (l-m1)/2;
        var coords = [
            [x        , y],
            [x        , y - w + o],
            [x + o    , y - w],
            [x + l - o, y - w],
            [x + l    , y - w + o],
            [x + l    , y]
        ];
                    
        var linearRing = new OpenLayers.Geometry.LinearRing(coords.map(this.toPoint));
        var polygon = new OpenLayers.Geometry.Polygon([linearRing]);
        return polygon;
    },
    
    createO: function(config) {
        var x = config.x;
        var y = config.y;
        var l = config.l;
        var w = config.w;
        var m1 = config.m1;
        var m2 = config.m2;
        var r = ((m1/2)*(Math.sqrt((4+(2*Math.SQRT2)))));
        var origin = new OpenLayers.Geometry.Point(x + r, y - r);
        var polygon = OpenLayers.Geometry.Polygon.createRegularPolygon(origin, r, 8);
        return polygon;
    },
    
    // NOTE: monkey patch rotate and resize to write resulting attributes on
    // change
    figurHooks: function(feature) {
        var oldMove = feature.geometry.move;
        feature.geometry.move = function(x, y) {
            feature.attributes.config.x += x;
            feature.attributes.config.y += y;
            oldMove.apply(feature.geometry, arguments);
        };
        
        var oldRotate = feature.geometry.rotate;
        feature.geometry.rotate = function(angle, origin) {
            feature.attributes.config.angle += angle;
            // TODO: rotate will change point of origin causing jumps if object is recreated
            // TODO: tried to fix below but isn't correct...
            //var point = feature.geometry.components[0].components[0];
            //feature.attributes.config.x = point.x;
            //feature.attributes.config.y = point.y;
            oldRotate.apply(feature.geometry, arguments);
        };
        
        // NOTE: dynamic resizing isn't allowed
        /*var oldResize = feature.geometry.resize;
        feature.geometry.resize = function(scale, origin, ratio) {
            feature.attributes.scale += scale;
            feature.attributes.origin = origin;
            feature.attributes.ratio = ratio;
            oldResize.apply(feature.geometry, arguments);
        };*/
    },
    create: function(config, attributes, style) {
        var geometry;
        if (config.type=='R') {
            geometry = this.createR(config);
        } else if (config.type=='L')    {
            geometry = this.createL(config);
        } else if (config.type=='D') {
            geometry = this.createD(config);
        } else {
            geometry = this.createO(config);
        }
        var center = geometry.getCentroid();
        var originGeometry = new OpenLayers.Geometry.Point(center.x, center.y);
        geometry.rotate(config.angle, originGeometry);
        var feature = new OpenLayers.Feature.Vector(geometry, attributes, style);
        this.figurHooks(feature);
        feature.attributes.config = config;
        return feature;
    }
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * 
 */
Ext.define('OpenEMap.view.ObjectConfig', {
    extend :  Ext.form.Panel ,
    statics: {
        config: {
            type: 'R',
            w: 10,
            l: 10,
            m1: 2,
            m2: 2,
            angle: 0
        }
    },
    fieldDefaults: {
        labelWidth: 60
    },
    autoHeight: true,
    width: 400,
    border: false,
    selectedFeature: undefined,
    layer: undefined,
    factory: undefined,
    hidden: true,
    defaults: {
        border: false
    },
    typeLabel: 'Type',
    widthLabel: 'Width',
    lengthLabel: 'Length',
    m1Label: 'M1',
    m2Label: 'M2',
    angleLabel: 'Angle',
    initComponent : function() {
        this.layer = this.mapPanel.drawLayer;
        this.factory = Ext.create('OpenEMap.ObjectFactory');
                        
        var types = {
            xtype : 'radiogroup',
            vertical : true,
            fieldLabel: this.typeLabel,
            itemId: 'type',
            hidden: true,
            items : [ {
                boxLabel : '<div class="objectconfig-radio-r"></div>',
                name : 'rb',
                inputValue : 'R',
                checked : true
            }, {
                boxLabel : '<div class="objectconfig-radio-l"></div>',
                name : 'rb',
                enabled: false,
                inputValue : 'L'
            }, {
                boxLabel : '<div class="objectconfig-radio-d"></div>',
                name : 'rb',
                enabled: false,
                inputValue : 'D'
            }, {
                boxLabel : '<div class="objectconfig-radio-o"></div>',
                name : 'rb',
                enabled: false,
                inputValue : 'O'
            } ],
            listeners: {
                change: function(field, value) {
                    this.config.type = value.rb;
                    this.updateHelpImage(this.config.type);
                    this.setFormItemVisibility(this.config.type);
                    this.createObject();
                },
                scope: this
            }
        };
        
        var formItems = [];
        
        formItems.push(types);
        
        formItems = formItems.concat([{
            xtype: 'numberfield',
            fieldLabel: this.widthLabel,
            itemId: 'w',
            minValue: 0,
            listeners: {
                change: function(field, value) {
                    this.config.w = value;
                    this.createObject();
                },
                scope: this
            }
        },{
            xtype: 'numberfield',
            fieldLabel: this.lengthLabel,
            itemId: 'l',
            minValue: 0,
            listeners: {
                change: function(field, value) {
                    this.config.l = value;
                    this.createObject();
                },
                scope: this
            }
        },{
            xtype: 'numberfield',
            fieldLabel: this.m1Label,
            itemId: 'm1',
            minValue: 0,
            listeners: {
                change: function(field, value) {
                    this.config.m1 = value;
                    this.createObject();
                },
                scope: this
            }
        },{
            xtype: 'numberfield',
            fieldLabel: this.m2Label,
            itemId: 'm2',
            minValue: 0,
            listeners: {
                change: function(field, value) {
                    this.config.m2 = value;
                    this.createObject();
                },
                scope: this
            }
        },{
            xtype: 'numberfield',
            itemId: 'angle',
            fieldLabel: this.angleLabel,
            value: 0,
            listeners: {
                change: function(field, value) {
                    this.config.angle = value;
                    this.createObject();
                },
                scope: this
            }
        }]);
        
        this.attributeFields = Ext.create('Ext.container.Container', { title: 'Attributes' });
        formItems.push(this.attributeFields);
        
        this.items = [ {
            layout: 'column',
            defaults: {
                border: false
            },
            padding: 5,
            items: [{
                width: 180,
                layout: 'form',
                items: formItems
            }, {
                columnWidth: 1,
                padding: 5,
                items: {
                    itemId: 'objectimage',
                    border: false,
                    height: 200
                }
            }]
        }];
        
        this.layer.events.register('featuremodified', this, this.onFeaturemodified);
        this.layer.events.register('beforefeatureselected', this, this.onBeforefeatureselected);
        this.layer.events.register('featureunselected', this, this.onFeatureunselected);

        this.callParent(arguments);
    },
    setConfig: function(config) {
        if (config === undefined) {
            this.config = Ext.clone(OpenEMap.view.ObjectConfig.config);
            this.down('#type').show();
        } else if (config.type) {
            this.config = Ext.clone(config);
            Ext.applyIf(this.config, OpenEMap.view.ObjectConfig.config);
            this.down('#type').hide();
        } else {
            this.config = Ext.clone(config);
            Ext.applyIf(this.config, OpenEMap.view.ObjectConfig.config);
            this.down('#type').show();
        }
        this.setFormValues();
        this.show();
        return this.config;
    },
    setFormValues: function() {
        if (this.config) {
            this.down('#type').setValue({'rb': this.config.type});
            this.updateHelpImage(this.config.type);
            this.down('#w').setRawValue(this.config.w);
            this.down('#l').setRawValue(this.config.l);
            this.down('#m1').setRawValue(this.config.m1);
            this.down('#m2').setRawValue(this.config.m2);
            this.down('#angle').setRawValue(this.config.angle);
            this.setFormItemVisibility(this.config.type);
            this.down('#angle').show();
        } else {
            this.down('#type').hide();
            this.down('#w').hide();
            this.down('#l').hide();
            this.down('#m1').hide();
            this.down('#m2').hide();
            this.down('#angle').hide();
            this.down('#objectimage').hide();
        }
        
        this.attributeFields.removeAll();
        if (this.selectedFeature) {
            Object.keys(this.selectedFeature.attributes).forEach(function(key) {
                this.createAttributeField(this.selectedFeature, key);
            }, this);
        }
        this.doLayout();
    },
    createAttributeField: function(feature, key) {
        if (key == 'config' || key == 'metadata') return;
        
        var metadata = feature.attributes.metadata;
        
        if (metadata && metadata[key] && metadata[key].hidden ) return;
        
        var value = feature.attributes[key];
        
        this.attributeFields.add({
            xtype: 'textfield',
            fieldLabel: key,
            value: value,
            listeners: {
                change: function(field, value) {
                    this.selectedFeature.attributes[key] = value;
                    this.layer.drawFeature(this.selectedFeature);
                },
                scope: this
            }
        });
    },
    setFormItemVisibility: function(type) {
        if (type == 'R') {
            this.down('#w').show();
            this.down('#l').show();
            this.down('#m1').hide();
            this.down('#m2').hide();
        } else if (type == 'L') {
            this.down('#w').show();
            this.down('#l').show();
            this.down('#m1').show();
            this.down('#m2').show();
        } else if (type == 'D') {
            this.down('#w').show();
            this.down('#l').show();
            this.down('#m1').show();
            this.down('#m2').hide();
        } else if (type == 'O') {
            this.down('#w').hide();
            this.down('#l').hide();
            this.down('#m1').show();
            this.down('#m2').hide();
        }
    },
    onFeaturemodified: function(e) {
        var feature = e.feature;
        config = feature.attributes.config;
        
        if (!config) return;
        
        this.down('#angle').setRawValue(config.angle);
    },
    onBeforefeatureselected: function(e) {
        var feature = e.feature;
        this.selectedFeature = feature;
        this.config = feature.attributes.config;
        
        var action = this.gui.activeAction;
        
        // customise modify feature behaviour depending on object type and modification mode flag
        if (action && action.control instanceof OpenLayers.Control.ModifyFeature) {
            if (this.config && (action.control._mode & OpenLayers.Control.ModifyFeature.RESHAPE)) {
                // use mode without reshape on predefined objects
                action.control.mode = action.control._mode ^ OpenLayers.Control.ModifyFeature.RESHAPE;
            } else {
                // restore mode
                action.control.mode = action.control._mode;
            }
            action.control.resetVertices();
        }
        
        this.show();
        
        this.setFormValues();
    },
    onFeatureunselected: function(e) {
        if (this.layer.selectedFeatures.length === 0) this.hide();
        
        this.selectedFeature = undefined;
    },
    createObject: function(x, y, style) {
        if (this.selectedFeature && this.selectedFeature.attributes.config) {
            // NOTE: for some strange reason replacing geometry components works, whereas replacing geometry causes strange render bugs
            var geometry = this.factory.create(this.config, style).geometry;
            this.selectedFeature.geometry.removeComponent(this.selectedFeature.geometry.components[0]);
            this.selectedFeature.geometry.addComponent(geometry.components[0]);
            this.selectedFeature.modified = true;
            this.selectedFeature.geometry.calculateBounds();
            this.mapPanel.map.controls.forEach(function(control) {
                if (control.CLASS_NAME == "OpenLayers.Control.ModifyFeature" && control.active) {
                    control.resetVertices();
                }
            });
            this.layer.drawFeature(this.selectedFeature);
            this.layer.events.triggerEvent('featuremodified', {
                feature: this.selectedFeature
            });
        }
    },
    updateHelpImage: function(type) {
        var name = 'figur-' + type + '-help.png';
        this.down('#objectimage').show();
        this.down('#objectimage').update('<img src="' + OpenEMap.basePathImages + name + '"></img>');
    }
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Specialised draw Action that draws predefined objects
 * 
 * Predefined objects can either be configured when constructing the action and/or defined
 * by a corresponding (optional) ObjectConfig view.
 */
Ext.define('OpenEMap.action.DrawObject', {
    extend:  OpenEMap.action.Action ,
                                        
                                             
    /**
     * NOTE: objectConfigView is assumed to be supplied as a config object
     * 
     * @param {Object} config
     * @param {Object} config.objectConfig
     * @param {string} config.objectConfig.type available types are 'R', 'L', 'D', 'O'
     * @param {number} config.objectConfig.w
     * @param {number} config.objectConfig.l
     * @param {number} config.objectConfig.m1
     * @param {number} config.objectConfig.m2
     * @param {number} config.objectConfig.angle rotation
     */
    constructor: function(config) {
        this.mapPanel = config.mapPanel;
        this.layer = this.mapPanel.drawLayer;
        this.style = config.style;
        this.attributes = config.attributes;
        this.objectConfig = config.objectConfig;
        this.objectConfigView = config.objectConfigView;
        this.factory = Ext.create('OpenEMap.ObjectFactory');
        
        this.attributes = this.attributes || {};
                    
        var Click = OpenLayers.Class(OpenLayers.Control, {
            initialize: function(options) {
                OpenLayers.Control.prototype.initialize.apply(
                    this, arguments
                );
                this.handler = new OpenLayers.Handler.Click(
                    this, {
                        'click': this.onClick
                    }, this.handlerOptions
                );
            },
            onClick: Ext.bind(this.onClick, this)
        });
        
        config.control = new Click({
            type: OpenLayers.Control.TYPE_TOGGLE
        });
        
        config.iconCls = config.iconCls || 'action-drawobject';
        config.tooltip = config.tooltip || 'Rita f&ouml;rdefinierad form.';
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    },
    onClick: function(e) {
        var lonlat = this.mapPanel.map.getLonLatFromPixel(e.xy);
        var config = this.objectConfigView ? this.objectConfigView.config : OpenEMap.view.ObjectConfig.config;
        config = Ext.clone(config);
        config.x = lonlat.lon;
        config.y = lonlat.lat;
        var feature = this.factory.create(config, this.attributes, this.style);
        this.mapPanel.unselectAll();
        this.layer.addFeatures([feature]);
        this.mapPanel.selectControl.select(feature);
    },
    toggle: function(pressed) {
        if (pressed) this.objectConfigView.setConfig(this.objectConfig);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action in toolbar that zooms the user to full extent
 * 
 * The snippet below is from configuration to MapClient.view.Map
 * 
 * {@img Fullextent.png fullextent}
 * 
 *        "tools": ["FullExtent"]
 */
Ext.define('OpenEMap.action.FullExtent', {
    extend:  OpenEMap.action.Action ,
    constructor: function(config) {
        config.control = new OpenLayers.Control.ZoomToMaxExtent();
        
        config.iconCls = config.iconCls || 'action-fullextent';
        config.tooltip = config.tooltip || 'Zooma till full utberedning';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * 
 */
Ext.define('OpenEMap.view.IdentifyResults', {
    extend :  Ext.panel.Panel ,
    autoScroll : true,
    layout: {
        type: 'vbox',
        pack:'start',
        align: 'stretch',
        resizable: true
    },
    initComponent : function() {
        var store = Ext.create('Ext.data.TreeStore', {
            root : {
                expanded : true
            }
        });
        
        this.root = store.getRootNode();
        
        var propertyGrid = Ext.create('Ext.grid.property.Grid', {
            flex: 2,
            autoScroll : true,
            title: 'Egenskaper',
            collapsible : true,
            collapsed: false,
            xtype : 'propertygrid',
            stripeRows: true,
            clicksToEdit: 100
        });
        
        //propertryGrid.editors = {};
        
        this.items = [{
            xtype : 'treepanel',
            flex: 1,
            rootVisible : false,
            store : store,
            minHeight: 200,
            listeners: {
                select: this.onSelect,
                scope: this
            }
        }, propertyGrid ];

        this.callParent(arguments);
    },
    onSelect: function(model, record, index) {
        var source = {};
        var feature = record.raw.feature;
        var layer = record.raw.layer;
        
        var filterAttributesMeta = function(key) {
            if (layer.metadata.attributes[key]) {
                var alias = layer.metadata.attributes[key].alias || key;
                source[alias] = feature.attributes[key];
            }
        };
        
        if (feature) {
            if (layer.metadata && layer.metadata.attributes) {
                Object.keys(feature.attributes).forEach(filterAttributesMeta);
            } else {
                source = feature.attributes;
            }
            this.mapPanel.searchLayer.selectedFeatures.forEach(function(feature) {
                this.mapPanel.selectControl.unselect(feature);
            }, this);
            if (record.raw.feature.layer) {
                this.mapPanel.selectControl.select(feature);
            }
        }
        
        source = Ext.clone(source);
        var sourceConfig = Ext.clone(source);
        
        Object.keys(source).forEach(function(key) {
            var value = sourceConfig[key];
            value = value ? value : '';
            if (value.match('http://') || value.match('//') || value.match('https://')) {
                source[key] = '<a href="'+value+'" target="_blank">Länk</a>';
                sourceConfig[key] = {
                    renderer: function(value) {return value;},
                    editor: Ext.create('Ext.form.DisplayField')
                };
            } else {
                sourceConfig[key] = {
                    editor: Ext.create('Ext.form.DisplayField')
                };
            }
        });
        
        this.down('propertygrid').setSource(source, sourceConfig);
    },
    /**
     * @param {Array.<OpenLayers.Feature.Vector>} features
     */
    addResult: function(features, layer) {
        var layerNode = this.root.appendChild({
            text: layer.name,
            leaf: false,
            expanded : true
        });
        
        var processFeature = function(feature) {
            layerNode.appendChild({
                text: feature.attributes[Object.keys(feature.attributes)[0]],
                leaf: true,
                feature: feature,
                layer: layer
            });
        };
        
        // TODO - add code to specify main attribute
		        
        features.forEach(processFeature);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Identify action
 * 
 * @param {Object} [config] config object to set properties on cretaion
 * @param {string} [config.feature] not used?
 * @param {string} [config.mapPanel] mapPanel to add popup window on
 * @param {string} [config.items] items to show in result window
 * @param {string} [config.useRegisterenhet=true] wheter or not to use identify on registerenhet 
 * @param {string} [config.tolerance=3] tolerance to use when identifying in map. Radius in image pixels. 
 * @param {string} [config.onlyVisibleLayers=false] wheter or not to drill through invisble layers 
 */
Ext.define('OpenEMap.action.Identify', {
    extend:  OpenEMap.action.Action ,
                                                
    popup : null,
    
    getPopup : function(config){
        if (this.popup){
            this.popup.destroy();
        }
        this.popup = Ext.create('GeoExt.window.Popup', {
            title: 'Sökresultat',
            location: config.feature,
            anchored: false,
            unpinnable: false,
            draggable: true,
            map: config.mapPanel,
            maximizable : false,
            minimizable : false,
            resizable: true,
            width: 300,
            height: 400,
            layout: 'fit',
            items: config.items,
            collapsible: false,
            x : 200,
            y: 100,
            listeners : {
                close : function(){
                    config.mapPanel.searchLayer.removeAllFeatures();
                }
            }
        });

        return this.popup;
    },

    
    constructor: function(config) {
        var self = this;

        var mapPanel = config.mapPanel;
        var layer = mapPanel.searchLayer;
        var map = config.map;
        var layers = config.layers;
        var client = config.client;
        
        // Defaults to identify registerenhet
        if (config.useRegisterenhet === null) {
        	config.useRegisterenhet = true;
        }
        config.tolerance = config.tolerance || 3;
        if (config.onlyVisibleLayers === null) {
        	config.onlyVisibleLayers = true;	
        }   

        var Click = OpenLayers.Class(OpenLayers.Control, {
            initialize: function(options) {
                OpenLayers.Control.prototype.initialize.apply(
                    this, arguments
                );
                this.handler = new OpenLayers.Handler.Click(
                    this, {
                        'click': this.onClick
                    }, this.handlerOptions
                );
            },
            onClick: function(evt) {
                // Show graphhic for start loading
                mapPanel.setLoading(true);
                layer.destroyFeatures();
                
                var lonlat = map.getLonLatFromPixel(evt.xy);
                var x = lonlat.lon;
                var y = lonlat.lat;
                
				var lowerLeftImage = {};
				var upperRightImage = {};
				upperRightImage.x = evt.xy.x+config.tolerance;
                lowerLeftImage.x = evt.xy.x-config.tolerance;
                upperRightImage.y = evt.xy.y+config.tolerance;
                lowerLeftImage.y = evt.xy.y-config.tolerance;
                var lowerLeftLonLat = map.getLonLatFromPixel(lowerLeftImage);
                var upperRightLonLat = map.getLonLatFromPixel(upperRightImage);

                // Create search bounds for identify
                var point = new OpenLayers.Geometry.Point(x, y);
                
                var bounds = new OpenLayers.Bounds();
                bounds.extend(lowerLeftLonLat);
                bounds.extend(upperRightLonLat);
				
                var feature = new OpenLayers.Feature.Vector(point);
                layer.addFeatures([feature]);
                
                var identifyResults = Ext.create('OpenEMap.view.IdentifyResults', {
                    mapPanel : mapPanel
                });

                var popup = self.getPopup({mapPanel : mapPanel, location: feature, items: identifyResults});
                popup.show();

				// Identify registerenhet
				if (config.useRegisterenhet) {
	                OpenEMap.requestLM({
	                    url: 'registerenheter?x=' + x + '&y=' + y,
	                    success: function(response) {
	                        var registerenhet = Ext.decode(response.responseText);
	
	                        var feature = new OpenLayers.Feature.Vector(point, {
	                            name: registerenhet.name
	                        });
	                        identifyResults.addResult([feature], {name:"Fastigheter"});
	                    },
	                    failure: function(response) {
	                        Ext.Msg.alert('Fel i fastighetstjänsten', 'Kontakta systemadministratör<br>Felkod: ' + response.status + ' ' + response.statusText);
	                    }
	                });
                }
               
               // Identify WFS-layers in map
                var parser = Ext.create('OpenEMap.config.Parser');
               
               	var layers = client.getConfig(true).layers;
		        layers = parser.extractPlainLayers(layers);
                var clickableLayers =  parser.extractClickableLayers(layers);

                // Only return layers that are visible
                if (config.onlyVisibleLayers) {
                	clickableLayers = parser.extractVisibleLayers(clickableLayers);
                } 
  				// Extract layers in draw order
//  			clickableLayers = parser.extractLayersInDrawOrder(clickableLayers);

             	OpenLayers.ProxyHost = OpenEMap.basePathProxy;

                var wfsLayers = parser.extractWFS(clickableLayers);
                
                var wfsIdentify = function(wfsLayer) {
                	wfsLayer.wfs.url = wfsLayer.wfs.url;
                    var options = Ext.apply({
                        version: "1.1.0",
                        srsName: map.projection
                    }, wfsLayer.wfs);
                    
                    var protocol = new OpenLayers.Protocol.WFS(options);
                    
                    protocol.read({
                        filter: new OpenLayers.Filter({
                            type: OpenLayers.Filter.Spatial.BBOX,
                            value: bounds
                        }),
                        callback: function(response) {
                            var features = response.features;
                            if (features && features.length>0) {
                                identifyResults.addResult(features, wfsLayer);
                                layer.addFeatures(features);
                            }
                        }
                    });
                };
                
                wfsLayers.forEach(wfsIdentify);

                var wmsLayers = parser.extractWMS(parser.extractNoWFS(clickableLayers));

                // TODO - use WMSGetFeatureInfo wmsLayers array functionality instead of looping
//                var wmsLayersOL = [];
//                for (var i=0; i<wmsLayers.length; i++) {
//                	wmsLayersOL.push(wmsLayers[i].layer);
//                }
                var wmsIdentify = function(wmsLayer) {
                    var showResults = function(response) {
                        var features = response.features;
                        if (features && features.length>0) {
                            identifyResults.addResult(features, wmsLayer);
                            layer.addFeatures(features);
                        }
                    };
                    var options = {
                    	url: wmsLayer.layer.url,
                        layers: [wmsLayer.layer],
//                        layers: [wmsLayersOL],
                        infoFormat: 'application/vnd.ogc.gml',
                        clickCallback: showResults
                    };
                    var wmsctrl = new OpenLayers.Control.WMSGetFeatureInfo(options);
                    wmsctrl.setMap(map);
					wmsctrl.events.register('getfeatureinfo', wmsctrl, showResults);
					wmsctrl.getInfoForClick(evt, {proxy: OpenEMap.basePathProxy});                    
                };
                
                wmsLayers.forEach(wmsIdentify);

                // Hide graphhic for loading
				mapPanel.setLoading(false);
            }
        });
        
        config.control = new Click({
            type: OpenLayers.Control.TYPE_TOGGLE
        });
        
        config.iconCls = config.iconCls || 'action-identify';
        config.tooltip = config.tooltip || 'Identifiera';
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action that measure area.
 *{@img Measurearea.png measurearea}
 * 
 * The example below is from configuration adding the tool to MapClient.view.Map:
 * 
 *         "tools": [ "FullExtent", "ZoomSelector", "MeasureLine", "MeasureArea"]
 */
Ext.define('OpenEMap.action.MeasureArea', {
    extend:  OpenEMap.action.Action ,
    
    constructor: function(config) {
        
        var mapPanel = config.mapPanel;
        if (config.accuracy === null) {
        	config.accuracy = 2;
        }
        
        config.control = new OpenLayers.Control.DynamicMeasure(OpenLayers.Handler.Polygon, {
            mapPanel: mapPanel, 
            accuracy: config.accuracy
        });

        config.iconCls = config.iconCls || 'action-measurearea';
        config.tooltip = config.tooltip || 'M&auml;t area';
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action that measure line.
 * {@img Measureline.png measureline}
 * 
 * The example below is from configuration:
 * 
* The example below is from configuration adding the tool to MapClient.view.Map:
 * 
 *         "tools": [ "FullExtent", "ZoomSelector", "MeasureLine", "MeasureArea"]
 */
 
Ext.define('OpenEMap.action.MeasureLine', {
    extend:  OpenEMap.action.Action ,
    
    constructor: function(config) {
        
        var mapPanel = config.mapPanel;
        if (config.accuracy === null) {
        	config.accuracy = 2;
        }

        config.control = new OpenLayers.Control.DynamicMeasure(OpenLayers.Handler.Path, {
            maxSegments : null,
            accuracy: config.accuracy,
            mapPanel: mapPanel
        });        
        
        config.iconCls = config.iconCls || 'action-measureline';
        config.tooltip = config.tooltip || 'Mät str&auml;cka';
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Grid action column that shows layer metadata
 * 
 */

Ext.define('OpenEMap.action.MetadataInfoColumn', {
    extend:  Ext.grid.column.Action ,

    requrires: [
        'Ext.tip.ToolTip',
        'OpenEMap.data.DataHandler',
        'OpenEMap.view.MetadataWindow'
    ],

    text: '',
    width: 22,
    menuDisabled: true,
    align: 'center',
    iconCls: 'action-identify',

    initComponent: function(options) {
        var me = this;

        this.tip = Ext.create('Ext.tip.ToolTip', {
            trackMouse: true
        });

        this.listeners = {
            mouseover: function(grid, rowIndex, colIndex, actionItem, event, record, row) {
                me.tip.setTarget(event.target);
                if(me.dataHandler) {
                    me.dataHandler.getMetadataAbstract(me.getUUIDFromMetadataUrl(record.get('urlToMetadata')), function(json){
                        if(json['abstract']) {
                            me.updateTooltip(json['abstract']);
                        }
                    });
                }
            },
            mouseout: function() {
                me.tip.update(null);
                me.tip.hide();
            },
            click: function(grid, element, rowIndex, colIndex, event, record) {
                if(me.metadataWindow) {
                    me.tip.update(null);
                    me.metadataWindow.showMetadata(me.getUUIDFromMetadataUrl(record.get('urlToMetadata')));
                }
                
            }
        };

        this.callParent(arguments);
    },

    /**
    * Update tooltip for this column
    * @param {string}  string   string to show in tooltip
    */
    updateTooltip: function(str) {
        if(str) {
            this.tip.update(str.substr(0,180) + '...');
            this.tip.show();
        }
    },

    /**
    * Experimetnal function to get metadata uuid from an url
    * @param  {string}           url  metadata-url containing metadata uuid
    * @return {string/undefined} 
    */
    getUUIDFromMetadataUrl: function(url) {
        if(url) {
            var start = url.indexOf('id=');
            if(start > 0) {
                return url.substr(start+3,36);
            }
        }
        return url;
    }

});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action that modify geometry
 * 
 * Example configuration using experimental support in OL to customize drag and radius handles:
 *    {
 *      "type": "ModifyGeometry",
 *      "reshape": true,
 *      "tooltip": "Redigera geometri",
 *      "options": {
 *        "dragHandleStyle": {
 *          "pointRadius": 8,
 *          "externalGraphic": "css/images/arrow-move.png",
 *          "fillOpacity": 1
 *        },
 *        "radiusHandleStyle": {
 *          "pointRadius": 8,
 *          "externalGraphic": "css/images/arrow-circle.png",
 *          "fillOpacity": 1
 *        }
 *      }
 *    } 
 *    
 * @param {Object} [config] 
 * @param {boolean} config.drag Allow dragging of features
 * @param {boolean} config.rotate Allow rotation of features
 * @param {boolean} config.resize Allow resizing of features
 * @param {boolean} config.reshape Allow reshaping of features
 * @param {Object} config.options Additional options to send to OpenLayers.Control.ModifyFeature
 */
Ext.define('OpenEMap.action.ModifyGeometry', {
    extend:  OpenEMap.action.Action ,
    constructor: function(config) {
        var mapPanel = config.mapPanel;
        var layer = mapPanel.drawLayer;
        
        if (config.drag === undefined) config.drag = true;
        if (config.rotate === undefined) config.rotate = true;
        if (config.reshape === undefined) config.reshape = true;
        
        var mode = 0;
        if (config.drag) mode = mode | OpenLayers.Control.ModifyFeature.DRAG;
        if (config.rotate) mode = mode | OpenLayers.Control.ModifyFeature.ROTATE;
        if (config.resize) mode = mode | OpenLayers.Control.ModifyFeature.RESIZE;
        if (config.reshape) mode = mode | OpenLayers.Control.ModifyFeature.RESHAPE;
        
        var options = Ext.apply({mode: mode}, config.options);
        config.control = new OpenLayers.Control.ModifyFeature(layer, options);
        config.control._mode = config.control.mode;
        
        config.iconCls = config.iconCls || 'action-modifygeometry';
        config.tooltip = config.tooltip || '&Auml;ndra ritat objekt';
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action that selelcts geometry.
 */
Ext.define('OpenEMap.action.ModifyText', {
    extend:  OpenEMap.action.Action ,

    constructor: function(config) {
        var mapPanel = config.mapPanel;
        var layer = config.mapPanel.drawLayer;

        config.attributes = config.attributes || {};
        
        config.control = config.mapPanel.selectControl;

        config.control.events.register('deactivate', this, function(){
        	console.log('deactivate');
        	
        });

        config.control.events.register('activate', this, function(){
            var self = this;
            layer.events.register('featureselected', self, function(evt){
                Ext.Msg.prompt('Text', 'Mata in text:', function(btn, text){
                    if (btn == 'ok'){
                        evt.feature.attributes.label = text;
                        evt.feature.data.label = text;
                        layer.redraw();
                    }
                });
            });
        });

        config.control.events.register('deactivate', this, function(){
            layer.events.unregister('featureselected');
                
        });

        config.iconCls = config.iconCls || 'action-selectgeometry';
        config.tooltip = config.tooltip || '&Auml;ndra text';
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action in toolbar that zooms the user to full extent
 * 
 * The snippet below is from configuration to MapClient.view.Map
 * 
 * 
 *        "tools": ["Permalink"]
 */
Ext.define('OpenEMap.action.Permalink', {
    extend:  OpenEMap.action.Action ,
    constructor: function(config) {
        this.client = config.client;
    
        config.control = config.control = new OpenLayers.Control.Button({
            trigger: this.onClick.bind(this)
        });
        
        config.iconCls = config.iconCls || 'action-permalink';
        config.tooltip = config.tooltip || 'Skapa länk till kartan';
        
        this.callParent(arguments);
    },
    onClick: function() {
        this.generate();
    },
    generate: function() {
        var success = function(response) {
            var id = Ext.decode(response.responseText);
            
            this.createWindow(id);
        };
        
        Ext.Ajax.request({
            url: OpenEMap.wsUrls.permalinks,
            method: 'POST',
            jsonData: this.client.getPermalinkdata(),
            success: success.bind(this)
        });
    },
    createWindow: function(id) {
	 	var url = document.location.protocol + '//'+ document.location.host + document.location.pathname + '?permalink=' + id;
        var label = '<a href="' + url + '" target="_blank">' + url + '</a>';
    
        if (this.w) {
            if (this.w.isHidden()) this.w.show();
            this.w.items.getAt(0).getComponent('permalink').update(label);
            return;
        }
        
        this.w = Ext.create('Ext.Window', {
            title: 'Permalink',
            layout: 'fit',
            xtype: 'form',
            width: 400,
            bodyPadding: 5,
            bodyStyle: {
                border: '0px'
            },
            plain: true,
            border: false,
            closeAction: 'hide',
            items: {
                layout: 'anchor',
                border: false,
                items: {
                    xtype: 'label',
                    name: 'permalink',
                    itemId: 'permalink',
                    readOnly: true,
                    border: false,
                    anchor: '100%',
                    html: label
                }
            },
            bbar: [ '->',
                {
                    text: 'Uppdatera',
                    handler: this.onClick.bind(this)
                }
            ]
        });
        this.w.show();
    }
    
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/** 
 * @class OpenEMap.view.PopupResults
 * @author Anders Erlandsson, Sundsvalls kommun 
 * 
 * Holds the definition for popup window that shows when a feature in a popup layer is
 * selected
 */
Ext.define('OpenEMap.view.PopupResults', {
    extend :  GeoExt.window.Popup ,
    autoScroll : true,
    layout: {
        type: 'vbox',
        pack:'start',
        align: 'stretch'
    },
    popup: null,
	 /** 
	 * Creates a new popup window for a popup layer
	 * @param {Object} [config] Configuration of the popup behaviour   
	 * @param {Number} [config.tolerance=3] Tolerance to use when identifying in map. Radius in image pixels.
	 * @param {OpenLayers.Feature.Vector} [config.location] Where to anchor the popup
	 * @param {String} [config.icon] Path to image that should be used as icon in the header of the popup
	 * @param {String} [config.title] Title in the popup header
	 * @param {String} [config.popupText] Text to show in the body of the popup. Can be formatted as HTML. Must be URLEncoded
	 * @param {OpenEMap.view.Map} [config.mapPanel] 
	 * @param {OpenLayers.Feature.Vector} [feature] Feature that this popup is connected to
	 */
   constructor: function(config) {
        if (this.popup) {
            this.popup.destroy();
        }
	    this.popup = Ext.create('GeoExt.window.Popup', {
            ancCls: 'oep-popup-anc',
            popupCls: 'oep-popup',
            bodyCls: 'oep-popup-body',
            anchored: true,
            anchorPosition: 'bottom-left',
            animCollapse: true,
            collapsible: false,
            draggable: false,
			feature: config.feature,
		    html: config.popupText,
			icon: config.icon,
            layout: 'fit',
		    location: config.location,
            map: config.mapPanel,
            maxWidth: 300,
            maximizable : false,
            minimizable : false,
            resizable: false,
			title: config.title,
            unpinnable: false,
            listeners : {
                beforeclose : function(){
			        if (this) {
			            this.destroy();
			        }
		            // Unhiglight feature
		    		this.feature.renderIntent = 'default';
		    		this.feature.layer.drawFeature(this.feature);
			    	// Fire action "popupfeatureunselected" on the feature including layer and featureid
			    	map.events.triggerEvent("popupfeatureunselected",{layer: this.feature.layer, featureid: this.feature.attributes[this.feature.layer.idAttribute]});
                }
            }
		});
		return this.popup;
    }
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/** 
 * @class OpenEMap.action.Popup
 * @author Anders Erlandsson, Sundsvalls kommun 
 * This action is triggered when a feature of an vectorPopup layer is clicked in the map.
 * A vectorPopup layer must contain an property named popupTextAttribute. Each feature shall have
 * an attribute whit that name that holds the information that should be shown in the popup.
 * A vectorPopup layer must also contain an property named idAttribute. Each feature shall have
 * an attribute whit that name that holds a unique id.
 * The layer may also contain popupAttributePrefix and popupAttributeSuffix that will be presented
 * as constant text before and after the popupTextAttribute
 * @param {Object} [config] Configuration of the popup behaviour   
 * @param {Number} [config.tolerance=3] Tolerance to use when identifying in map. Radius in image pixels.
 * @param {Boolean} [config.showOnlyFirstHit=true] true to show popup only for the first hit. false to show popups for each hit.
 */
Ext.define('OpenEMap.action.Popup', {
    extend:  OpenEMap.action.Action ,
                                             
    popup: null,
    map: null,
    constructor: function(config) {
        var self = this;
        map = config.map;
        var mapPanel = config.mapPanel;
        var layers = config.layers;
        config.popup = null;

        // Defaults to 3 pixels tolerance
        config.tolerance = config.tolerance || 3;  
        // Deafults to only show first hit
        if (config.showOnlyFirstHit === undefined || config.showOnlyFirstHit === null) {
        	config.showOnlyFirstHit = true;
        }
        
        var Click = OpenLayers.Class(OpenLayers.Control, {
            initialize: function(options) {
                OpenLayers.Control.prototype.initialize.apply(
                    this, arguments
                );
                this.handler = new OpenLayers.Handler.Click(
                    this, {
                        'click': this.onClick
                    }, this.handlerOptions
                );
            },
            onClick: function(evt) {
            	var lonlat = map.getLonLatFromPixel(evt.xy);
                layers = map.layers;
                
                var x = lonlat.lon;
                var y = lonlat.lat;
                var clkPoint = new OpenLayers.Geometry.Point(x, y);
                var clkFeature = new OpenLayers.Feature.Vector(clkPoint);

				// Buffering click point with tolerance  
				var lowerLeftImage = {};
				var upperRightImage = {};
				upperRightImage.x = evt.xy.x+config.tolerance;
                lowerLeftImage.x = evt.xy.x-config.tolerance;
                upperRightImage.y = evt.xy.y+config.tolerance;
                lowerLeftImage.y = evt.xy.y-config.tolerance;
                var lowerLeftLonLat = map.getLonLatFromPixel(lowerLeftImage);
                var upperRightLonLat = map.getLonLatFromPixel(upperRightImage);

                
                // Create search bounds for identify
                var bounds = new OpenLayers.Bounds();
                bounds.extend(lowerLeftLonLat);
                bounds.extend(upperRightLonLat);
                
                // get popup layers 
                var parser = Ext.create('OpenEMap.config.Parser');
                var popupLayers = parser.extractPopupLayers(layers);
                
                var hitFound =  false;
                var popupIdentify = function(popupLayer) {
                	// identify features in popup layers
                	// Remove all popups for this layer
			    	if (popupLayer.popup) { 
						// Remove popup window
						popupLayer.popup.forEach(function(p) {p.destroy();p = null;});
						popupLayer.popup = [];
			    	}
                	var popupFeature = function(feature) {
                		if (feature.geometry.intersects(bounds.toGeometry())) {
					    	// get text to populate popup 
                			var popupText = popupLayer.popupTextPrefix+feature.attributes[popupLayer.popupTextAttribute]+popupLayer.popupTextSuffix;
					    	var popupTitle = '';
					    	if (popupLayer.popupTitleAttribute) {
					    		popupTitle = feature.attributes[popupLayer.popupTitleAttribute];
					    	}
					    	
					    	if (feature.geometry.getVertices().length == 1) {
					    		clkFeature = feature.clone();
					    	}
					    	// Create popup 
					    	var popup = new OpenEMap.view.PopupResults({mapPanel : mapPanel, location: clkFeature, popupText: popupText, feature: feature, title: popupTitle});
					
							// Show popup
					        popup.show();
							
							// TODO - move popup window from layer to feature
							// Adds popup to array of popups in map  
					        popupLayer.popup.push(popup);
					        
				    		// Highlight feature
				    		feature.renderIntent = 'select';
				    		feature.layer.drawFeature(feature);
					    	
					    	// Fire action "popupfeatureselected" on the feature including layer and featureid
					    	map.events.triggerEvent("popupfeatureselected",{layer: popupLayer, featureid: feature.attributes[popupLayer.idAttribute]});
		                	return true;
				    	} else {
				    		// Remove highlight feature if selected
			    			if (feature.renderIntent == 'select') {
					    		feature.renderIntent = 'default';
					    		feature.layer.drawFeature(feature);
						    	// Fire action "popupfeatureunselected" on the feature including layer and featureid
						    	map.events.triggerEvent("popupfeatureunselected",{layer: popupLayer, featureid: feature.attributes[popupLayer.idAttribute]});
					    	}
				    		return false;
				    	}
                	};
					
					// Loop throgh each feature in the layer
                	var featureIndex=0;
                	var hitFoundInLayer = false;
                	while ((featureIndex < popupLayer.features.length)  && (!(hitFoundInLayer && config.showOnlyFirstHit))) {
                		hitFoundInLayer = popupFeature(popupLayer.features[featureIndex]);
                		featureIndex++;	
                	}
                	return hitFound;
                };
                
                // Loop through each popupLayer
            	var layerIndex=0;
            	while ((layerIndex<popupLayers.length)  && (!(hitFound && config.showOnlyFirstHit))) {
            		hitFound = popupIdentify(popupLayers[layerIndex]);
            		layerIndex++;
            	}
            }
        });
        
        config.control = new Click({
            type: OpenLayers.Control.TYPE_TOGGLE
        });
        
        config.iconCls = config.iconCls || 'action-popup';
        config.tooltip = config.tooltip || 'Popup';
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    },

	/*
	 * Cleanup on close
	 */    
    destroy: function() {
        if (this.popup){
            this.popup.destroy();
        }
    	this.destroyPopupLayers();
    }
    //this.popup.destroy();
    
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action for print using mapfish.
 */
Ext.define('OpenEMap.action.Print', {
    extend :  OpenEMap.action.Action ,
    require : 'GeoExt.plugins.PrintExtent',
    constructor : function(config) {
        var mapPanel = config.mapPanel;
        var printExtent = mapPanel.plugins[0];
        var printProvider = printExtent.printProvider;
        printProvider.customParams = {attribution: config.mapPanel.config.attribution.trim(), mapTitle: ''};
        var printDialog = null;
        var page = null;

        var onTransformComplete = function() {
            var scale = printDialog.down('#scale');
            scale.select(page.scale);
        };
        var onBeforedownload = function() {
            if (printDialog) printDialog.setLoading(false);
        };
        var onPrintexception = function(printProvider, response) {
            if (printDialog) printDialog.setLoading(false);
            Ext.Msg.show({
                 title:'Felmeddelande',
                 msg: 'Print failed.\n\n' + response.responseText,
                 icon: Ext.Msg.ERROR
            });
        };
        var close = function() {
            printProvider.un('beforedownload', onBeforedownload);
            printProvider.on('printexception', onPrintexception);
            printExtent.control.events.unregister('transformcomplete', null, onTransformComplete);
            printExtent.removePage(page);
            printExtent.hide();
            printDialog = null;
        };
        var onClose = function() {
            close();
            control.deactivate();
        };
        
        config.iconCls = config.iconCls || 'action-print';
        config.tooltip = config.tooltip || 'Skriv ut';
        config.toggleGroup = 'extraTools';
        
        var Custom =  OpenLayers.Class(OpenLayers.Control, {
            initialize: function(options) {
                OpenLayers.Control.prototype.initialize.apply(
                    this, arguments
                );
            },
            type: OpenLayers.Control.TYPE_TOGGLE,
            activate: function() {
                if (printDialog) {
                    return;
                }
                // NOTE: doing a hide/show at first display fixes interaction problems with preview extent for unknown reasons
                printExtent.hide();
                printExtent.show();
                page = printExtent.addPage();
                
                
                printProvider.dpis.data.items.forEach(function(d){
                	var validDpi = false;
                    if (d.data.name === '56'){
                        validDpi = true;
                        d.data.name = 'Låg (' +d.data.name + ' dpi)';
                    } 
                    else if (d.data.name === '127'){
                        validDpi = true;
                        d.data.name = 'Medel (' +d.data.name + ' dpi)';
                    }
                    else if (d.data.name === '254'){
                        validDpi = true;
                        d.data.name = 'Hög (' +d.data.name + ' dpi)';
                    } 
                });
                
                
                printProvider.layouts.data.items.forEach(function(p){
                	if (/landscape$/.test(p.data.name)){
                		p.data.displayName = p.data.name.replace('landscape', 'liggande');
                	} else if (/portrait$/.test(p.data.name)){
                		p.data.displayName = p.data.name.replace('portrait', 'stående');	
                	}
                });

                
                
                printDialog = new Ext.Window({
                    autoHeight : true,
                    width : 290,
                    resizable: false,
                    layout : 'fit',
                    bodyPadding : '5 5 0',
                    title: 'Utskriftsinst&auml;llningar',
                    listeners: {
                        close: onClose
                    },
                    items : [ {
                        xtype : 'form',
                        layout : 'anchor',
                        defaults : {
                            anchor : '100%'
                        },
                        fieldDefaults : {
                            labelWidth : 120
                        },
                        items : [ {
                            xtype : 'textfield',
                            fieldLabel: 'Rubrik',
                            valueField: 'mapTitle',
                            itemId : 'mapTitle',
                            queryMode: 'local',
                            value: printProvider.customParams.mapTitle,
                            listeners: {
                                change: function(textfield){
                                    printProvider.customParams.mapTitle = textfield.value;
                                }
                            }
                        },{
                            xtype : 'combo',
                            fieldLabel: 'Pappersformat',
                            store : printProvider.layouts,
                            displayField : 'displayName',
                            valueField : 'name',
                            itemId : 'printLayouts',
                            queryMode: 'local',
                            value : printProvider.layouts.getAt(0).get("name"),
                            listeners: {
                                select: function(combo, records, eOpts) {
                                    var record = records[0];
                                    printProvider.setLayout(record);
                                }
                            }
                        }, {
                            xtype : 'combo',
                            fieldLabel: 'Kvalité',
                            store : printProvider.dpis,
                            displayField : 'name',
                            valueField : 'value',
                            queryMode: 'local',
                            value: printProvider.dpis.first().get("value"),
                            listeners: {
                                select: function(combo, records, eOpts) {
                                    var record = records[0];
                                    printProvider.setDpi(record);
                                }
                            }
                        }, {
                            xtype : 'combo',
                            fieldLabel: 'Skala',
                            store : printProvider.scales,
                            displayField : 'name',
                            valueField : 'value',
                            queryMode: 'local',
                            itemId: 'scale',
                            value: printProvider.scales.first().get("value"),
                            listeners: {
                                select: function(combo, records, eOpts) {
                                    var record = records[0];
                                    page.setScale(record, "m");
                                }
                            }
                        } ]
                    } ],
                    bbar : [ '->', {
                        text : "Skriv ut",
                        handler : function() {
                            printDialog.setLoading(true);
                            printExtent.print();
                        }
                    } ]
                });
                printDialog.show();
                var scale = printDialog.down('#scale');
                scale.select(page.scale);
                
                var layoutId = 6;
                var printLayouts = printDialog.down('#printLayouts');
                printLayouts.select(printLayouts.store.data.get(layoutId));
                var currentPrintLayout = printLayouts.store.data.items[layoutId];
                printProvider.setLayout(currentPrintLayout);
                
                
                printExtent.control.events.register('transformcomplete', null, onTransformComplete);
                printExtent.control.events.register('transformcomplete', null, onTransformComplete);
                printProvider.on('beforedownload', onBeforedownload);
                printProvider.on('printexception', onPrintexception);
                
                OpenLayers.Control.prototype.activate.apply(this, arguments);
            },
            deactivate: function() {
                if (printDialog) printDialog.close();
                OpenLayers.Control.prototype.deactivate.apply(this, arguments);
            }
        });
        var control = new Custom({
            type: OpenLayers.Control.TYPE_TOGGLE
        });
        config.control = control;
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Action that selelcts geometry.
 */
Ext.define('OpenEMap.action.SelectGeometry', {
    extend:  OpenEMap.action.Action ,
    constructor: function(config) {
        var mapPanel = config.mapPanel;
        
        config.control = mapPanel.selectControl;
        
        config.iconCls = config.iconCls || 'action-selectgeometry';
        config.tooltip = config.tooltip || 'V&auml;lj ritat objekt';
        config.toggleGroup = 'extraTools';
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Custom floating panel to switch baselayers
 */
Ext.define('OpenEMap.view.BaseLayers', {
    extend :  Ext.toolbar.Toolbar ,
    border: false,
    cls: 'oep-map-tools',
    constructor : function(config){
        var mapPanel = config.mapPanel;
        var map = mapPanel.map;
        
        var baseLayers = mapPanel.map.layers.filter(function(layer) { return layer.isBaseLayer; });
        
        var createButton = function(layer){
            var cls;
            
            if (layer == baseLayers[baseLayers.length-1]) cls = 'oep-tools-last';
            
            var button = Ext.create('Ext.Button', {
                text : layer.name,
                toggleGroup : 'baseLayers',
                allowDepress: false,
                layer: layer,
                pressed : layer.visibility,
                cls: cls,
                listeners : {
                    toggle : function(btn, pressed, opts){
                        if (pressed) map.setBaseLayer(layer);
                    }
                }
            });

            return button;
        };
        
        this.items = baseLayers.map(createButton);
        
        map.events.register('changebaselayer', this, this.onChangeBaseLayer);
        
        this.callParent(arguments);
    },
    onChangeBaseLayer: function(e) {
        this.items.each(function(button) {
            button.toggle(button.layer == e.layer, true);
        });
    }
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * 
 */
Ext.define('OpenEMap.view.Map' ,{
    extend:  GeoExt.panel.Map ,
    require : 'GeoExt.plugins.PrintExtent',
    border: false,
    anchor: '100% 100%',
    constructor: function(config) {
        this.initDefaultLayers(config.config);
        
        var printProvider = Ext.create('GeoExt.data.MapfishPrintProvider', {
            url: OpenEMap.basePathMapFish,
            autoLoad: true,
            timeout: 60*1000,
            listeners: {
                "encodelayer": function(printProvider, layer, encodedLayer) {
                    if (encodedLayer && encodedLayer.baseURL) {
                        encodedLayer.baseURL = encodedLayer.baseURL.replace('gwc/service/', '');
                    }
                }
/*                "loadcapabilities": function(printProvider, capabilities) {
                    // NOTE: need to override to test locally...
                    capabilities.createURL = "/print/pdf/create.json";
                }
                "beforedownload": function(printProvider, url) {
                    console.log("beforedownload");
                }
*/
               }
        });
        
        var printExtent = Ext.create('GeoExt.plugins.PrintExtent', {
            printProvider: printProvider
        });
        
        this.encode = function(layout) {
            var page = printExtent.addPage();
            if (layout) {
                var record = printProvider.layouts.findRecord('name', layout);
                printProvider.setLayout(record);
            }
            var json = printProvider.encode(printExtent.map, printExtent.pages);
            printExtent.removePage(page);
            return json;
        };
        
        printProvider.encode = function(map, pages, options) {
            if(map instanceof GeoExt.MapPanel) {
                map = map.map;
            }
            pages = pages instanceof Array ? pages : [pages];
            options = options || {};
            if(this.fireEvent("beforeprint", this, map, pages, options) === false) {
                return;
            }

            var jsonData = Ext.apply({
                units: map.getUnits(),
                srs: map.baseLayer.projection.getCode(),
                layout: this.layout.get("name"),
                dpi: this.dpi.get("value")
            }, this.customParams);

            var pagesLayer = pages[0].feature.layer;
            var encodedLayers = [];

            // ensure that the baseLayer is the first one in the drawingLayer encoded list
            var layers = map.layers.concat();

            Ext.Array.remove(layers, map.baseLayer);
            Ext.Array.insert(layers, 0, [map.baseLayer]);

            Ext.each(layers, function(layer){
                if(layer !== pagesLayer && layer.getVisibility() === true) {
                    var enc = this.encodeLayer(layer);
                    enc && encodedLayers.push(enc);
                }
            }, this);
            jsonData.layers = encodedLayers;

            var encodedPages = [];
            Ext.each(pages, function(page) {

                encodedPages.push(Ext.apply({
                    center: [page.center.lon, page.center.lat],
                    scale: page.scale.get("value"),
                    rotation: page.rotation
                }, page.customParams));
            }, this);
            jsonData.pages = encodedPages;

            if (options.overview) {
                var encodedOverviewLayers = [];
                Ext.each(options.overview.layers, function(layer) {
                    var enc = this.encodeLayer(layer);
                    enc && encodedOverviewLayers.push(enc);
                }, this);
                jsonData.overviewLayers = encodedOverviewLayers;
            }

            if (options.legend && this.fireEvent("beforeencodelegend", this, jsonData, options.legend)) {
                var legend = options.legend;
                var rendered = legend.rendered;
                if (!rendered) {
                    legend = legend.cloneConfig({
                        renderTo: document.body,
                        hidden: true
                    });
                }
                var encodedLegends = [];
                legend.items && legend.items.each(function(cmp) {
                    if(!cmp.hidden) {
                        var encFn = this.encoders.legends[cmp.getXType()];
                        // MapFish Print doesn't currently support per-page
                        // legends, so we use the scale of the first page.
                        encodedLegends = encodedLegends.concat(
                            encFn.call(this, cmp, jsonData.pages[0].scale));
                    }
                }, this);
                if (!rendered) {
                    legend.destroy();
                }
                jsonData.legends = encodedLegends;
            }
            
            return jsonData;
        };
        
        config.plugins = [printExtent];
        
        this.callParent(arguments);
                
        this.layers.add(this.searchLayer);
        this.layers.add(this.drawLayer);
        this.layers.add(this.measureLayer);
        this.layers.add(this.measureLayerArea);
        this.layers.add(this.measureLayerLength);
        this.layers.add(this.measureLayerSegments);
        
        this.map.setLayerIndex(this.measureLayer, 98);
        this.map.setLayerIndex(this.measureLayerArea, 98);
        this.map.setLayerIndex(this.measureLayerLength, 98);
        this.map.setLayerIndex(this.measureLayerSegments, 98);
        
        this.selectControl = new OpenLayers.Control.SelectFeature(this.drawLayer);
        this.selectControl.handlers.feature.stopDown = false;
        this.map.addControl(this.selectControl);

        // Define container for popup windows - initialize when first popupLayer is created. 
        this.map.popup = [];
    },
    unselectAll: function() {
        this.drawLayer.selectedFeatures.forEach(function(feature) {
            this.selectControl.unselect(feature);
        }, this);
    },
    parseStyle: function(style) {
        var template = {
                "Point": {
                    pointRadius: 4,
                    graphicName: "square",
                    fillColor: "#e8ffee",
                    fillOpacity: 0.9,
                    strokeWidth: 1,
                    strokeOpacity: 1,
                    strokeColor: "#29bf4c"
                },
                "Line": {
                    strokeWidth: 3,
                    strokeColor: "#29bf4c",
                    strokeOpacity: 1
                },
                "Polygon": {
                    strokeWidth: 3,
                    strokeOpacity: 1,
                    strokeColor: "#29bf4c",
                    fillColor: "#e8ffee",
                    fillOpacity: 0.9
                }
            };
        
        var createSymbolizer = function(style) {
            var clone = Ext.clone(template);
            if (style["Point"]) {
                Ext.apply(clone["Point"], style["Point"]);
                Ext.apply(clone["Line"], style["Line"]);
                Ext.apply(clone["Polygon"], style["Polygon"]);
                if (style.labelSegments){
                    Ext.apply(clone["labelSegments"], style["labelSegments"]);
                }
                if (style.labelLength){
                    Ext.apply(clone["labelLength"], style["labelLength"]);
                }
            } else {
                Ext.apply(clone["Point"], style);
                Ext.apply(clone["Line"], style);
                Ext.apply(clone["Polygon"], style);
            }
            return clone;
        };
        
        var defaultStyle = new OpenLayers.Style(null, {rules: [ new OpenLayers.Rule({symbolizer: template})]});
        var selectStyle;
        var temporaryStyle;
        var measureStyle;
        if (style) {
            if (style["default"]) {
                defaultStyle = createSymbolizer(style["default"]);
                defaultStyle = new OpenLayers.Style(null, {rules: [ new OpenLayers.Rule({symbolizer: defaultStyle})]});
            }
            if (style["select"]) {
                selectStyle = createSymbolizer(style["select"]);
                selectStyle = new OpenLayers.Style(null, {rules: [ new OpenLayers.Rule({symbolizer: selectStyle})]});
            }
            if (style["temporary"]) {
                temporaryStyle = createSymbolizer(style["temporary"]);
                temporaryStyle = new OpenLayers.Style(null, {rules: [ new OpenLayers.Rule({symbolizer: temporaryStyle})]});
            }
            if (style["labelLength"]){
                measureStyle = createSymbolizer(style);
                measureStyle = new OpenLayers.Style(null, {rules: [new OpenLayers.Rule({symbolizer: measureStyle}) ] });

            }
            if (!style["default"]) {
                defaultStyle = createSymbolizer(style);
                defaultStyle = new OpenLayers.Style(null, {rules: [ new OpenLayers.Rule({symbolizer: defaultStyle})]});
            }
        }
        
        var map = {
            "default": defaultStyle
        };
        if (selectStyle) {
            map["select"] = selectStyle;
        }
        if (temporaryStyle) {
            map["temporary"] = temporaryStyle;
        }

        if (measureStyle){
            map["default"] = measureStyle;
        }

        var styleMap = new OpenLayers.StyleMap(map);
        
        return styleMap;
    },
    initDefaultLayers: function(config) {
        if (!config.drawStyle) {
            config.drawStyle = {
                    "default": {
                        "Point": {
                            pointRadius: 4,
                            graphicName: "square",
                            fillColor: "#ffffff",
                            fillOpacity: 1,
                            strokeWidth: 1,
                            strokeOpacity: 1,
                            strokeColor: "#2969bf"
                        },
                        "Line": {
                            strokeWidth: 3,
                            strokeColor: "#2969bf",
                            strokeOpacity: 1
                        },
                        "Polygon": {
                            strokeWidth: 3,
                            strokeOpacity: 1,
                            strokeColor: "#2969bf",
                            fillOpacity: 0.3,
	                        fillColor: "#deecff"
                        }
                    },
                    "select": {
                        strokeWidth: 3,
                        strokeOpacity: 1,
                        fillColor: "#deecff",
                        fillOpacity: 0.75,
                        strokeColor: "#2969bf"
                    },
                    "temporary": {
                        strokeWidth: 3,
                        strokeOpacity: 0.75,
                        fillColor: "#ff00ff",
                        fillOpacity: 0,
                        strokeColor: "#ff00ff"
                    }
            };
        }
        
        this.drawLayer = new OpenLayers.Layer.Vector('Drawings', {
            displayInLayerSwitcher: false,
            styleMap: this.parseStyle(config.drawStyle||this.drawLayerDefaultStyle)
        });
	    var labelRule = new OpenLayers.Rule({
	      	filter: new OpenLayers.Filter.Comparison({
	          	type: OpenLayers.Filter.Comparison.EQUAL_TO,
	          	property: "type",
	          	value: "label"
	      	}),
	        symbolizer: {
	          	pointRadius: 20,
	          	fillOpacity: 0,
	          	strokeOpacity: 0,
	          	label: "${label}"   
	       	}
	    });
		this.drawLayer.styleMap.styles['default'].addRules([labelRule]);

        if (config.autoClearDrawLayer) {
            this.drawLayer.events.register('beforefeatureadded', this, function() {
                this.drawLayer.destroyFeatures();
            });
        }
        
        function onFeatureadded(e) {
            var feature = e.feature;
            this.selectControl.select(feature);
        }
        
        function onBeforefeaturemodified(e) {
            var feature = e.feature;
            this.selectControl.select(feature);
        }
        
        function onAfterfeaturemodified(e) {
            var feature = e.feature;
            //this.selectControl.unselect(feature);
        }
        
        //this.drawLayer.events.register('featureadded', this, onFeatureadded);
        this.drawLayer.events.register('beforefeaturemodified', this, onBeforefeaturemodified);
        this.drawLayer.events.register('afterfeaturemodified', this, onAfterfeaturemodified);
        
        var searchStyle = {
                "Point": {
                    externalGraphic: OpenEMap.basePathImages + "point_added.png", 
                    graphicWidth: 15, 
                    graphicOpacity: 1
                },
                "Line": {
                    strokeWidth: 3,
                    strokeColor: "#2969bf",
                    strokeOpacity: 1
                },
                "Polygon": {
                    strokeDashstyle: 'dot',
                    strokeWidth: 3,
                    strokeOpacity: 1,
                    strokeColor: "#f58d1e",
                    fillOpacity: 0
                }
        };
        
        this.searchLayer = new OpenLayers.Layer.Vector('Searchresult', {
            displayInLayerSwitcher: false,
            styleMap: this.parseStyle(searchStyle)
        });
        
        var defaultStyles = OpenLayers.Control.DynamicMeasure.styles;
        
        var style = new OpenLayers.Style(null, {rules: [
            new OpenLayers.Rule({symbolizer: {
                'Point': defaultStyles.Point,
                'Line': defaultStyles.Line,
                'Polygon': defaultStyles.Polygon
            }})
        ]});
        var styleMap = new OpenLayers.StyleMap({"default": style});
        
        var createStyleMap = function(name) {
            return new OpenLayers.StyleMap({ 'default': OpenLayers.Util.applyDefaults(null, defaultStyles[name])});
        };
        
        this.measureLayer = new OpenLayers.Layer.Vector('MeasureLayer', {
            displayInLayerSwitcher: false,
            styleMap: styleMap
        });
        
        this.measureLayerArea = new OpenLayers.Layer.Vector('MeasureLayerArea', {
            displayInLayerSwitcher: false,
            styleMap: createStyleMap('labelArea')
        });
        
        this.measureLayerSegments = new OpenLayers.Layer.Vector('MeasureLayerSegments', {
            displayInLayerSwitcher: false,
            styleMap: createStyleMap('labelSegments')
        });
        
        this.measureLayerLength = new OpenLayers.Layer.Vector('MeasureLayerLength', {
            displayInLayerSwitcher: false,
            styleMap: createStyleMap('labelLength')
        });
    },
    setInitialExtent: function() {
        var map = this.map;
        if (!map.getCenter()) {
            if (this.center || this.zoom ) {
                // center and/or zoom?
                map.setCenter(this.center, this.zoom);
            } else if (this.extent instanceof OpenLayers.Bounds) {
                // extent
                map.zoomToExtent(this.extent, false);
            }else {
                map.zoomToMaxExtent();
            }
        }
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * @param {Object} [config] config object to set properties on cretaion
 * @param {number} [config.zoom] Set to a zoom level to override the default
 */
Ext.define('OpenEMap.view.SearchCoordinate', {
    extend :  Ext.container.Container ,
    layout: 'column',
    defaults: {
        labelWidth: 20
    },
    width: 300,
    border: false,
    zoom: 5,
    initComponent : function(config) {
        this.items = [ {
            itemId: 'e',
            fieldLabel: 'E',
            xtype : 'textfield',
            columnWidth: 0.5
        },{
            itemId: 'n',
            fieldLabel: 'N',
            xtype : 'textfield',
            columnWidth: 0.5
        }, {
            xtype: 'button',
            text: 'Sök',
            handler: function() {
                var x = this.down('#e').getValue();
                var y = this.down('#n').getValue();
                this.mapPanel.map.setCenter([x, y], this.zoom);
                this.fireEvent('searchcomplete', [x, y]);
            },
            scope: this
        }];
        
        this.addEvents([/**
                         * @event searchcomplete
                         * Fires after coordinate search is complete
                         * @param {Array.<Number>} coordinate
                         */
                        'searchcomplete']);
        
        this.callParent(arguments);
    }
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Combobox that searches from LM with type ahead.
 */
Ext.define('OpenEMap.form.SearchRegisterenhet', {
    extend :  Ext.form.field.ComboBox ,
    alias: 'widget.searchregisterenhet',
    require: ['Ext.data.*',
              'Ext.form.*'],
    queryDelay: 800,
    initComponent : function() {
        var registeromrade;
        var zoom;
        if (this.search && this.search.options){
            registeromrade = this.search.options.municipalities.join(',');
            zoom = this.search.options.zoom;
        }
        var layer = this.mapPanel.searchLayer;
        
        function doSearch(id) {
            this.mapPanel.setLoading(true);
            this.mapPanel.searchLayer.destroyFeatures();
            OpenEMap.requestLM({
                url: 'registerenheter/' + id + '/enhetsomraden?',
                success: function(response) {
                    this.resultPanel.expand();
                    var features = new OpenLayers.Format.GeoJSON().read(response.responseText);
                    layer.addFeatures(features);
                    var extent = layer.getDataExtent();
                    if (zoom) {
                        this.mapPanel.map.setCenter(extent.getCenterLonLat(), zoom);
                    } else {
                        this.mapPanel.map.zoomToExtent(extent);
                    }
                },
                failure: function() {
                    Ext.Msg.alert('Fel', 'Ingen träff.');
                },
                callback: function() {
                    this.mapPanel.setLoading(false);
                },
                scope: this
            });
        }
        
        this.store = Ext.create('Ext.data.Store', {
            proxy: {
                type: 'ajax',
                url : OpenEMap.basePathLM + 'registerenheter',
                extraParams: {
                    lmuser: OpenEMap.lmUser
                },
                reader: {
                    type: 'json',
                    root: 'features'
                }
            },
            fields: [
                 {name: 'id', mapping: 'properties.objid'},
                 {name: 'name', mapping: 'properties.name'}
             ]
        });
        
        this.store.on('beforeload', function(store, operation) {
          store.lastOperation = operation;
        }, this);
        
        this.labelWidth = 60;
        this.displayField = 'name';
        this.valueField = 'id';
        this.queryParam = 'q';
        this.typeAhead = true;
        this.forceSelection = true;
        
        this.listeners = {
            'select':  function(combo, records) {
                var id = records[0].get('id');
                doSearch.call(this, id);
            },
            'beforequery': function(queryPlan) {
                if (registeromrade && queryPlan.query.match(registeromrade) === null) {
                    queryPlan.query = registeromrade + ' ' + queryPlan.query;
                }
                var lastQ = this.store.lastOperation && this.store.lastOperation.request && this.store.lastOperation.request.params && this.store.lastOperation.request.params.q ? this.store.lastOperation.request.params.q : undefined;
		        if (this.store.loading && this.store.lastOperation) {
		          var requests = Ext.Ajax.requests;
		          for (var id in requests)
		            if (requests.hasOwnProperty(id) && requests[id].options == this.store.lastOperation.request) {
		              Ext.Ajax.abort(requests[id]);
		            }
		        }
            },
            scope: this
        };
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Combobox that searches from LM with type ahead.
 */
Ext.define('OpenEMap.form.SearchAddress', {
    extend :  Ext.form.field.ComboBox ,
    alias: 'widget.searchaddress',
    require: ['Ext.data.*',
              'Ext.form.*'],
    initComponent : function() {
        var registeromrade;
        var zoom = 11;
        if (this.search && this.search.options){
            registeromrade = this.search.options.municipalities.join(',');
            zoom = this.search.options.zoom;
        }
        
        var layer = this.mapPanel.searchLayer;
        
        function doSearch(fnr, x, y) {
            this.mapPanel.setLoading(true);
            this.mapPanel.searchLayer.destroyFeatures();
            OpenEMap.requestLM({
                url: 'registerenheter?fnr=' + fnr,
                success: function(response) {
                    var json = Ext.decode(response.responseText);
                    if (json.success === false) {
                        Ext.Msg.alert('Meddelande', 'Ingen fastighet kunde hittas på adressen.');
                        return;
                    }
                    this.resultPanel.expand();
                    var features = new OpenLayers.Format.GeoJSON().read(response.responseText, "FeatureCollection");
                    layer.addFeatures(features);
                    var point = new OpenLayers.Geometry.Point(x, y);
                    feature = new OpenLayers.Feature.Vector(point);
                    layer.addFeatures([feature]);
                    this.mapPanel.map.setCenter([x,y], zoom);
                },
                failure: function() {
                    Ext.Msg.alert('Fel', 'Okänt.');
                },
                callback: function() {
                    this.mapPanel.setLoading(false);
                },
                scope: this
            });
        }
        
        this.store = Ext.create('Ext.data.Store', {
            proxy: {
                type: 'ajax',
                url : OpenEMap.basePathLM + 'addresses',
                extraParams: {
                    lmuser: OpenEMap.lmUser
                },
                reader: {
                    type: 'array'
                }
            },
            fields: ['id', 'name', 'x', 'y', 'fnr']
        });
        
        this.store.on('beforeload', function(store, operation) {
          store.lastOperation = operation;
        }, this);
        
        this.labelWidth = 60;
        this.displayField = 'name';
        this.valueField = 'id';
        this.queryParam ='q';
        this.typeAhead = true;
        this.forceSelection = true;
        
        this.listeners = {
            'select':  function(combo, records) {
                doSearch.call(this, records[0].data.fnr, records[0].data.x, records[0].data.y);
            },
            'beforequery': function(queryPlan) {
                if (registeromrade && queryPlan.query.match(registeromrade) === null) {
                    queryPlan.query = registeromrade + ' ' + queryPlan.query;
                }

		        if (this.store.loading && this.store.lastOperation) {
		          var requests = Ext.Ajax.requests;
		          for (var id in requests)
		            if (requests.hasOwnProperty(id) && requests[id].options == this.store.lastOperation.request) {
		              Ext.Ajax.abort(requests[id]);
		            }
		        }
            },
            scope: this
        };
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Combobox that searches from LM with type ahead.
 */
Ext.define('OpenEMap.form.SearchPlacename', {
    extend :  Ext.form.field.ComboBox ,
    alias: 'widget.searchplacename',
    require: ['Ext.data.*',
              'Ext.form.*'],
    initComponent : function() {
        var kommunkod;
        var zoom = 5;
        if (this.search && this.search.options) {
            kommunkod = this.search.options.municipalities.join(',');
            zoom = this.search.options.zoom;
        }
                
        this.store = Ext.create('Ext.data.Store', {
            proxy: {
                type: 'ajax',
                url : OpenEMap.basePathLM + 'placenames',
                extraParams: {
                    lmuser: OpenEMap.lmUser,
                    kommunkod: kommunkod
                },
                reader: {
                    type: 'json',
                    root: 'features'
                }
            },
            fields: [
                 {name: 'id', mapping: 'properties.id'},
                 {name: 'name', mapping: 'properties.name'}
             ]
        });
        
        this.store.on('beforeload', function(store, operation) {
          store.lastOperation = operation;
        }, this);
        
        this.labelWidth= 60;
        this.displayField= 'name';
        this.valueField= 'id';
        this.queryParam='q';
        this.typeAhead= true;
        this.forceSelection= true;
        
        this.listeners = {
            'select':  function(combo, records) {
                var fake = records[0].raw;
                var coords = fake.geometry.coordinates;
                var switchedAxis = [coords[1], coords[0]];
                this.mapPanel.map.setCenter(switchedAxis, zoom);
            },
            'beforequery': function() {
		        if (this.store.loading && this.store.lastOperation) {
		          var requests = Ext.Ajax.requests;
		          for (var id in requests)
		            if (requests.hasOwnProperty(id) && requests[id].options == this.store.lastOperation.request) {
		              Ext.Ajax.abort(requests[id]);
		            }
		        }
		    },
            scope: this
        };
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * Combobox that uses ES for generic search
 */
Ext.define('OpenEMap.form.SearchES', {
    extend :  Ext.form.field.ComboBox ,
    alias: 'widget.searches',
    require: ['Ext.data.*',
              'Ext.form.*'],
    initComponent : function() {
        var map = this.mapPanel.map;
        var layer = this.mapPanel.searchLayer;

        this.store = Ext.create('Ext.data.Store', {
            proxy: {
                type: 'ajax',
                url: OpenEMap.basePathES + '_search',
                reader: {
                    type: 'json',
                    root: 'hits.hits',
                    totalProperty: 'hits.total',
                    idProperty: '_id'
                }
            },
            fields: [
                { name: 'type', mapping: '_type' },
                { name: 'hit', mapping: '_source.properties.TEXT' }, // Could be AKT="2281K-NJU-268" or PLANNUMMER="S5086"
                { name: 'geometry', mapping: '_source.geometry' }
            ]
        });
        
        this.displayField = 'hit';
        this.valueField = 'id';
        this.queryParam ='q';
        this.typeAhead = true;
        this.forceSelection = true;
        this.allowBlank = false;
        this.allowOnlyWhitespace = false;
        this.minChars = 4;
        this.minLength = 4;
        this.preventMark = true;
        this.hideTrigger = true;
        
        this.listeners = {
            'select':  function(combo, records) {
                var geojson = records[0].data.geometry;
                var format = new OpenLayers.Format.GeoJSON({
                    ignoreExtraDims: true
                });
                var geometry = format.read(geojson, 'Geometry');
                var feature = new OpenLayers.Feature.Vector(geometry);
                layer.destroyFeatures();
                layer.addFeatures([feature]);
                map.zoomToExtent(feature.geometry.getBounds());
            },
            'beforequery': function(queryPlan) {
                queryPlan.query = '"' + queryPlan.query + '"' + '*';
            },
            scope: this
        };
        
        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * @param {Object} [config] config object to set properties on cretaion
 * @param {number} config.zoom Set to a zoom level to override default zooming behaviour and always zoom to the desired level
 */
Ext.define('OpenEMap.view.SearchFastighet', {
    extend :  Ext.form.Panel ,
                
                                                    
                                              
                                                
                                         
                                                
                                            
    border: false,
    initComponent : function() {

        if (!this.renderTo) {
            this.title = 'Sök';
            this.bodyPadding = 5;
        }
        
        var data = [];
        if (this.search && this.search.searchEstates) {
			data.push(['searchregisterenhet', 'Fastighet' ]);
		}
        if (this.search && this.search.searchAddresses) {
			data.push(['searchaddress', 'Adress']);
		}
        if (this.search && this.search.searchPlacenames) {
			data.push(['searchplacename', 'Ort']);
		}
		if (this.search && this.search.searchES && this.search.searchES.detaljplan) {
			data.push(['searches', 'Detaljplaner']);
		}
//        data.push(['searchbyggnad', 'Byggnad']);

        var columns = [ {
            text : 'Namn',
            dataIndex : 'name',
            flex : 1
        } ];

        var store = Ext.create('GeoExt.data.FeatureStore', {
            layer : this.mapPanel.searchLayer,
            featureFilter: new OpenLayers.Filter.Function({
                evaluate: function(context) {
                    if (context.attributes.name) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }),
            fields : [ {
                name : 'name'
            }, {
                name : 'fid'
            }, {
                name : 'objid'
            } ]
        });
        
        // Creating selectControl to make it stopDown, which will make mousedown event available to other handlers too
        // thus making map panable even when grabbing inside a feature 
        var selectControl = new OpenLayers.Control.SelectFeature(this.mapPanel.searchLayer);
        selectControl.handlers.feature.stopDown = false;
        this.mapPanel.map.addControl(selectControl);
        var selectionModel = Ext.create('GeoExt.selection.FeatureModel', {selectControl: selectControl});
        
        var grid = Ext.create('Ext.grid.Panel', {
            columns : columns,
            store : store,
            selModel : selectionModel
        });
        
        function defSearchCombo(type) {
            var searchCriteria = null;
            if (type === 'searchregisterenhet'){
                searchCriteria = this.search && this.search.searchEstates ? this.search.searchEstates : null; 
            } else if (type === 'searchaddress'){
                searchCriteria = this.search && this.search.searchAddresses ? this.search.searchAddresses : null;
            } else {
                searchCriteria = this.search && this.search.searchPlacenames ? this.search.searchPlacenames : null;
            }

            return {
                xtype : type,
                mapPanel : this.mapPanel,
                basePath: this.basePath,
                search : searchCriteria,
                resultPanel : grid
            };
        }
        
        function onChange(combo, value) {
            var container = this.down('#search');
            this.mapPanel.searchLayer.destroyFeatures();
            container.removeAll();
            container.add(defSearchCombo.call(this,value));
        }

        this.items = [ {
            layout : 'column',
            border: false,
            items : [ {
                xtype : 'combo',
                width : 110,
                store : data,
                forceSelection : true,
                queryMode : 'local',
                value : 'searchregisterenhet',
                border: false,
                listeners : {
                    change : onChange,
                    scope : this
                }
            }, {
                itemId : 'search',
                columnWidth : 1,
                layout : 'fit',
                border: false,
                items : defSearchCombo.call(this,'searchregisterenhet')
            } ]    }
        ];
        
        //if (!this.renderTo) {
            this.items.push(grid);
        //}

        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Gui for showing coordinates in map
 * @param {Object} [config] config object to set properties on cretaion
 * @param {Object} [config.cls] CSS-class to apply when rendering 
 */
Ext.define('OpenEMap.view.Scalebar', {
    extend :  Ext.panel.Panel ,
    bodyStyle : 'background : transparent',
    border: false,
    getTools : function() {
    },

    constructor : function(config) {
        Ext.apply(this, config);

    	var scalebarControl = null;

    	var div = document.getElementById(this.renderTo);
    	if (div) {
    		// Renders scalebar in div if renderTo
	    	scalebarControl = new OpenLayers.Control.ScaleLine({div: div});
    	} else {
    		// Renders scalebar in map if not renderTo
    		scalebarControl =  new OpenLayers.Control.ScaleLine();
    	}

        this.mapPanel.map.addControl(scalebarControl);
        this.callParent(arguments);
    }

});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Gui for showing coordinates in map
 * @param {Object} [config] config object to set properties on cretaion
 * @param {Object} [config.cls] CSS-class to apply when rendering 
 */
Ext.define('OpenEMap.view.ShowCoordinate', {
    extend :  Ext.container.Container ,
    defaults: {
        labelWidth: 10
    },
    border: false,
    layout: 'column',
    width: 150,
    srs: '',
    constructor : function(config) {
        this.items = [ {
            itemId: 'e',
            fieldLabel: 'E',
            xtype : 'textfield',
            columnWidth: 0.5,
            baseCls: config.cls,
            baseBodyCls: config.cls,
            bodyCls: config.cls,
            fieldCls: config.cls,
            fieldBodyCls: config.cls,
            formItemCls: config.cls, 
            inputRowCls: config.cls,
            labelCls: config.cls
        },{
            itemId: 'n',
            fieldLabel: 'N',
            xtype : 'textfield',
            columnWidth: 0.5,
            baseCls: config.cls,
            baseBodyCls: config.cls,
            bodyCls: config.cls,
            fieldCls: config.cls,
            fieldBodyCls: config.cls,
            formItemCls: config.cls, 
            inputRowCls: config.cls,
            labelCls: config.cls
        }];
        
        this.callParent(arguments);
    }
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Custom panel to represent a floating zoom slider
 */
Ext.define('OpenEMap.view.ZoomTools', {
    extend :  Ext.panel.Panel ,
                                     
    bodyStyle : 'background : transparent',
    border: false,
    getTools : function() {
        var oep = Ext.util.CSS.getRule('.oep-tools');
//        var scale = oep ? 'large' : 'medium';
//        var margin = oep ? '5 0 5 0' : '5 0 5 8';
        var scale = oep ? 'large' : 'large';
        var margin = oep ? '5 0 5 0' : '5 0 5 0';
        
        var pile = [];
        var slider = Ext.create('GeoExt.slider.Zoom', {
            height : 160,
            vertical : true,
            aggressive : true,
            margin  : margin,
            map : this.mapPanel.map
        });
        pile.push({
            xtype : 'button',
            iconCls: 'zoomtools-plus',
            mapPanel : this.mapPanel,
            scale: scale,
            cls: 'x-action-btn',
            listeners : {
                'click' : function() {
                    this.mapPanel.map.zoomIn();
                },
                scope: this
            }
        });
        pile.push(slider);
        pile.push({
            xtype : 'button',
            scale: scale,
            cls: 'x-action-btn',
            iconCls: 'zoomtools-minus',
            mapPanel : this.mapPanel,
            listeners : {
                'click' : function() {
                    this.mapPanel.map.zoomOut();
                },
                scope: this
            }
        });
        return pile;
    },

    constructor : function(config) {
        Ext.apply(this, config);

        this.items = this.getTools();

        this.callParent(arguments);
    }

});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * OpenEMap layer configuration model
 * Adds layer configuration specific fields
 */

Ext.define('OpenEMap.model.GroupedLayerTreeModel' ,{
    extend:  Ext.data.Model ,

    fields: [ 
    	{ name: 'text', type: 'string' },
    	{ name: 'checkedGroup', type: 'string' },
    	{ name: 'isGroupLayer' },
    	{ name: 'layer' },
    	{ name: 'queryable', type: 'boolean' },
    	{ name: 'clickable', type: 'boolean' },
    	{ name: 'isGroupLayer', type: 'boolean' },
    	{ name: 'expanded', type: 'boolean' },

        { name: 'layerId' },
    	{ name: 'name', type: 'string' },
    	{ name: 'urlToMetadata' },
        { name: 'wms' },
    	{ name: 'wfs' },
        { name: 'serverId' },
        { name: 'metadata' },
        
        { name: 'legendURL' },
        { name: 'layers'}
    ]
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Grouped layer tree store
 * Ext.data.TreeStore extended to support OpenEMap layer configuration including layer groups
 */

Ext.define('OpenEMap.data.GroupedLayerTree' ,{
    extend:  Ext.data.TreeStore ,

               
                                     
                                              
      

    model: 'OpenEMap.model.GroupedLayerTreeModel',
    defaultRootProperty: 'layers',

    proxy: {
        type: 'memory'
    },

    maxLayerIndex: 1000,

    listeners: {
        beforeinsert: function(store, node, refNode, eOpts) { return this.onBeforeInsert(store, node, refNode); },
        beforeappend: function(store, node, eOpts) { return this.onBeforeAppend(store, node); },
        insert: function(store, node, refNode, eOpts) { this.onInsertAndAppend(node); },
        append: function(store, node, index, eOpts) { this.onInsertAndAppend(node); },
        remove: function(store, node, isMove, eOpts) { this.onRemove(store, node, isMove); }
    },

    constructor: function(config) {
        config = Ext.apply({}, config);
        this.callParent([config]);
    },
        
    /**
    * Returns all layers as OpenEMap layer configuration tree.
    * @return {Object} layerConfig  OpenEMap layer configuration
    */
    getLayerConfiguration: function(includeLayerRef) {
        var layerConfig = [];

        function configAddLayer(node, includeLayerRef) {
            var layerCfg = {
                name: node.get('name'),
                isGroupLayer: node.get('isGroupLayer'),
                expanded: node.get('expanded'),
                queryable: node.get('queryable'),
                clickable: node.get('clickable'),
                wms: typeof node.get('wms') === 'string' ? {} : node.get('wms'),
                wfs: typeof node.get('wfs') === 'string' ? {} : node.get('wfs'),
                layer: includeLayerRef ? node.get('layer') : undefined,
                metadata: typeof node.get('metadata') === 'string' ? {} : node.get('metadata'),
                layers: []
            };
            
            if (!layerCfg.wms || Object.keys(layerCfg.wms).length === 0) layerCfg.wms = undefined;
            if (!layerCfg.wfs || Object.keys(layerCfg.wfs).length === 0) layerCfg.wfs = undefined;
//            if (!layerCfg.layers || layerCfg.layers.length === 0) layerCfg.layers = undefined;

	        for(var j=0; j<node.childNodes.length;j++) {
		        layerCfg.layers.push(configAddLayer(node.childNodes[j], includeLayerRef));
	        }
			return layerCfg;
        }

        var childNodes = this.getRootNode().childNodes;
        for (var i=0; i<childNodes.length;i++) {
	        layerConfig.push(configAddLayer(childNodes[i], includeLayerRef));
/*
            var parseLayer = function(layer) {
                return {
                    name: layer.name,
                    isGroupLayer: layer.isGroupLayer,
                    queryable: layer.queryable,
                    clickable: layer.clickable,
                    wms: typeof layer.wms === 'string' ? {} : layer.wms,
                    wfs: typeof layer.wfs === 'string' ? {} : layer.wfs,
                    layer: includeLayerRef ? layer.layer : undefined,
                    layers: layer.layers ? layer.layers.map(parseLayer) : undefined,
                    metadata: typeof layer.metadata === 'string' ? {} : layer.metadata
                };
            };
            
        	var node = this.getRootNode();
            node.childNodes.forEach(function(subnode) {
                layerConfig[i].layers.push({
                    name: subnode.get('name'),
                    isGroupLayer: subnode.get('isGroupLayer'),
                    queryable: subnode.get('queryable'),
                    clickable: subnode.get('clickable'),
                    wms: typeof subnode.get('wms') === 'string' ? {} : subnode.get('wms'),
                    wfs: typeof subnode.get('wfs') === 'string' ? {} : subnode.get('wfs'),
                    layer: includeLayerRef ? subnode.get('layer') : undefined,
                    layers: subnode.get('layers') instanceof Array ? subnode.get('layers').map(parseLayer) : undefined,
                    metadata: typeof subnode.get('metadata') === 'string' ? {} : subnode.get('metadata')
                });
            });
*/        }
       return layerConfig;
    },

    /**
    * Before append to store
    * @param {Ext.data.Model} node
    * @param {Ext.data.Model} appendNode
    */
    onBeforeAppend: function(node, appendNode) {
        return true;
    },
    
    createInlineLegend: function(node) {
        if (!node.raw.layer || node.get('hasInlineLegend')) {
            return;
        }
        var layer = node.raw.layer;
        var url;
        if (layer.legendURL !== undefined) {
            url = layer.legendURL;
        } else if (node.raw.wms && node.raw.wms.params && (node.raw.wms.params.LAYERS || node.raw.wms.params.layers)) {
            var layerRecord = GeoExt.data.LayerModel.createFromLayer(layer);
            var legend = Ext.create('GeoExt.container.WmsLegend', {
                layerRecord: layerRecord,
                useScaleParameter: false
            });
            url = legend.getLegendUrl(node.raw.wms.params.LAYERS || node.raw.wms.params.layers);
        }
        if (url && url.length > 0) {
            node.set('text', '<div style="display:inline-block;width:20px;height:20px;margin-right:2px;overflow:hidden;"><img class="legendimg" src="' + url + '" style="height:20px;"></div>' + node.get('text')); 
        }
        node.set('hasInlineLegend', true);
    },

    /**
    * Before insert to store
    * @param {Ext.data.Store} store
    * @param {Ext.data.Model} node
    * @param {Ext.data.Model} refNode
    */
    onBeforeInsert: function(store, node, refNode) {
        return true;
    },

    /**
     * Handler for a store's insert and append event.
     *
     * @param {Ext.data.Model} node
     */
    onInsertAndAppend: function(node) {
        if(!this._inserting) {
            this._inserting = true;
            
            var isFromAdd = node.getOwnerTree() instanceof OpenEMap.view.layer.Add;
           
            if (!isFromAdd) {
                // use from add internal checked status
                if (node.raw.checked_) {
                    node.set('checked', node.raw.checked_);
                }
            
                // Add this node layers and subnodes to map.
                node.cascadeBy(function(subnode) {
                    var layer = subnode.get('layer');

                    // Add getLayer function to support GeoExt
                    subnode.getLayer = function() {
                        return this.get('layer');
                    };

                    if(layer && layer !== '' && this.map) {
                        var mapLayer = this.map.getLayer(layer);
                        if(mapLayer === null && layer && layer.displayInLayerSwitcher === true) {
                            this.map.addLayer(layer);
                        }
                    }
                }, this);
            
                this.reorderLayersOnMap();
                
                this.createInlineLegend(node);
            }
            
            delete this._inserting;
        }
    },

    /**
     * Handler for a store's remove event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Model} node
     * @param {Boolean} isMove
     * @private
     */
    onRemove: function(store, node, isMove) {
        if(!this._removing && !isMove) {
            this._removing = true;
            // Remove layer and sublayers from map
            node.cascadeBy(function(subnode) {
                var layer = subnode.get('layer');
                if(layer && layer.map) {
                    this.map.removeLayer(layer);
                }
            }, this);

            delete this._removing;
        }
    },

    /**
     * Reorder map layers from store order
     *
     * @private
     */
    reorderLayersOnMap: function() {
        var node = this.getRootNode();
        if(node) {
            var i = this.maxLayerIndex;
            node.cascadeBy(function(subnode) {
                var layer = subnode.get('layer');
                
                if(layer) {
                    layer.setZIndex(i);
                    i--;
                }
               
            }, this);
        }
    },
    /**
     * Unbind this store from the map it is currently bound.
     */
    unbind: function() {
        var me = this;
        me.un('beforeinsert', me.onBeforeInsert, me);
        me.un('beforeappend', me.onBeforeAppend, me);
        me.un('insert', me.onInsertAndAppend, me);
        me.un('append', me.onInsertAndAppend, me);
        me.un('remove', me.onRemove, me);
        me.map = null;
    },

    /**
     * Unbinds listeners by calling #unbind prior to being destroyed.
     *
     * @private
     */
    destroy: function() {
        //this.unbind();
        //this.callParent();
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * 
 */
Ext.define('OpenEMap.view.layer.Tree' ,{
    extend:  Ext.tree.Panel ,
               
                                         
                            
      

    rootVisible: false,
    hideHeaders: true,

    initComponent: function() {
        if(!this.store && this.mapPanel) {
            this.store = Ext.create('OpenEMap.data.GroupedLayerTree', {
                root: {
                    text: (this.mapPanel.config && this.mapPanel.config.name ? this.mapPanel.config.name : 'Lager'),
                    expanded: true,
                    isGroupLayer: true,
                    layers: this.mapPanel.map.layerSwitcherLayerTree
                },
                map: this.mapPanel.map
            });
        }

        this.on('checkchange', function(node, checked, eOpts) {
            // Loop this node and children
            node.cascadeBy(function(n){
                n.set('checked', checked);
                var olLayerRef = n.get('layer');
                // Change layer visibility (Layer groups have no layer reference)
                if(olLayerRef) {
                    olLayerRef.setVisibility(checked);
                  	olLayerRef.options.visibility = checked;
                }
                var wms = n.get('wms');
                if (wms) {
                    wms.options.visibility = checked;
                }

           	});
           
           	function checkParent(n, checked) {
	            var parent = n.parentNode;
	            if (parent.get("checked") != n.get("checked")) {
		           	if (checked) {
		           	    // check parent if not root
		            	if (!parent.isRoot()) {
		                	parent.set("checked", checked);
		                	checkParent(parent, checked);
		               	}
		           	} else {
		               	if (!parent.isRoot() && !parent.childNodes.some(function(node) { return node.get('checked'); })) {
		                   	parent.set("checked", checked);
		                   	checkParent(parent, checked);
		               	}
		           	}
	            }
	       	}
           	// Checking/unchecking parent node
			checkParent(node, checked);
			
        });

        this.on('cellclick', function(tree, td, cellIndex, node, el, columnIndex, e) {
            
            // function to create legend tooltip
            var createLegend = function(url) {
                var img = Ext.create('Ext.Img', {
                    src: url
                });
                var tip = Ext.create('Ext.tip.ToolTip', {
                    title: 'Legend ' + node.raw.name,
                    closable: true,
                    items: img
                });
                tip.showBy(el);
                img.getEl().on('load', function() {
                    tip.doLayout();
                });
            };
            
            // function to get legend url
            // TODO: could share code with inline legend creation in GroupedLayerTree
            var getLegendUrl = function(node) {
                var layer = node.raw.layer;
                var url;
                if (layer.legendURL !== undefined) {
                    url = layer.legendURL;
                } else if (node.raw.wms && (node.raw.wms.params.LAYERS || node.raw.wms.params.layers)) {
                    var layerRecord = GeoExt.data.LayerModel.createFromLayer(layer);
                    var legend = Ext.create('GeoExt.container.WmsLegend', {
                        layerRecord: layerRecord,
                        useScaleParameter: false
                    });
                    url = legend.getLegendUrl(node.raw.wms.params.LAYERS || node.raw.wms.params.layers);
                }
                return url;
            };
        
            // Get target element in an IE9 compatible way
            var target = e.browserEvent.target || e.browserEvent.srcElement;
            
            // check if target is the inline legend image
            if (Ext.get(target).hasCls('legendimg') && node.raw.layer) {
                var url = getLegendUrl(node);
                if (url && url.length > 0) {
                    createLegend(url);
                }
            }
        });
        
        this.callParent(arguments);
    },
    getBaseLayersConfiguration: function() {
        var layerConfigs = [];

        function configAddLayer(layer) {
	        var layerCfg = {
	            name: layer.name,
	            wms: {
	            	url: layer.url, 
	            	options: layer.options, 
	            	params: layer.params
	        	},
            	visibility: layer.visibility
	        };
			layerCfg.wms.options.visibility = layer.visibility; 
			return layerCfg;
        }

        var baseLayers = this.mapPanel.map.layers.filter(function(layer) { return layer.isBaseLayer; });
        for (var i=0; i<baseLayers.length;i++) {
	        layerConfigs.unshift(configAddLayer(baseLayers[i]));
        }
	    return layerConfigs;
    },

    getConfig: function(includeLayerRef) {
    	// Start with initial config to get a complete config object
        var config = Ext.clone(this.client.initialConfig);
        
        // Get layers config from layer tree, to reflect changes made by user   
       	var layers = this.getStore().getLayerConfiguration(includeLayerRef);

        // layer tree does not include base layers so extract them from initial config
    	var baseLayers = config.layers.filter(function(layer) {
    		return (layer.wms && layer.wms.options && layer.wms.options.isBaseLayer) ? layer : false;
    	});
    	
    	baseLayers = this.getBaseLayersConfiguration();
    	
    	config.layers = baseLayers.concat(layers);
    	
    	return config;
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Data handler, handles ajax-request against ws-backend
 * Used when no store is associated with the data
 */

Ext.define('OpenEMap.data.DataHandler', {

    metadataAbstractWsUrl: null,
    metadataWsUrl: null,
    layersWsUrl: null,

    metadataAbstractCache: {},
    
    constructor: function(options) {
        this.wsUrls = OpenEMap.wsUrls;
        Ext.apply(this,options);
    },

    /**
    * GET-request to get a specific layer
    * @param {number}   id          layer id
    * @param {Function} callback    callback-function on success
    */
    getLayer: function(id, callback) {
        /*if(this.wsUrls.layers && id) {
            this.doRequest(
                {
                    url: this.wsUrls.basePath + this.wsUrls.layers + '/' + id
                },
                function(json) {
                    callback(json);
                }
            );
        }*/
    },


    /**
    * GET-request to get layers
    * @param {Function} callback    callback-function on success
    */
    getLayers: function(callback) {
        /*if(this.wsUrls.layers) {
            this.doRequest(
                {
                    url: this.wsUrls.basePath + this.wsUrls.layers
                },
                callback
            );
        }*/
    },

    /**
    * GET-request to get metadata for specific UUID
    * @param {string}   UUID        layer metadata UUID
    * @param {Function} callback    callback-function on success
    */
    getMetadata: function(UUID, callback) {
        if(UUID && this.wsUrls.metadata) {
            this.doRequest(
                {
                    url: this.wsUrls.basePath + this.wsUrls.metadata + '/' + UUID
                },
                callback
            );
        }
    },

    /**
    * GET-request to get metadata abstract for specific UUID
    * @param {string}   UUID        layer metadata UUID
    * @param {Function} callback    callback-function on success
    */
    getMetadataAbstract: function(UUID, callback) {
        if(UUID && this.wsUrls.metadata_abstract) {
            var me = this;
            // Cache metadata temporarily until page reload, to minize ajax requests
            if(me.metadataAbstractCache[UUID]) {
                callback(me.metadataAbstractCache[UUID]);
            } else {
                this.doRequest(
                    {
                        url: this.wsUrls.basePath + this.wsUrls.metadata_abstract + '/' + UUID
                    },
                    function(json) {
                        callback(json);
                        me.metadataAbstractCache[UUID] = json;
                    }
                );
            }
        }
    },

    /**
    * PUT-request to update a configuration
    * @param {number}   id         map configuration id
    * @param {Object}   conf       map config object
    * @param {Function} callback   callback-function on success
    */
    updateConfiguration: function(id, conf, callback) {
        this.doRequest({
            url: this.wsUrls.basePath + this.wsUrls.adminconfigs + '/config/' + id,
            method: 'PUT',
            jsonData: conf
        }, callback);
    },

    /**
    * POST-request to save a new configuration
    * @param {Object}   conf       map config object
    * @param {Function} callback   callback-function on success
    */
    saveNewConfiguration: function(conf, callback) {
        this.doRequest({
            url: this.wsUrls.basePath + this.wsUrls.adminconfigs + '/config/',
            method: 'POST',
            jsonData: conf
        }, callback);
    },

    /**
    * DELETE-request to remove a configuration
    * @param {number}   id         map configuration id
    * @param {Object}   conf       map config object
    * @param {Function} callback   callback-function on success
    */
    deleteConfiguration: function(id, conf, callback) {
        this.doRequest({
            url: this.wsUrls.basePath + this.wsUrls.adminconfigs + '/config/' + id,
            method: 'DELETE',
            jsonData: conf
        }, callback);
    },

    /**
    * Handles Ajax-request.
    * @param {Object}   options     Ext.Ajax.request options 
    * @param {Function} callback    callback-function on success
    */
    doRequest: function(options, callback) {
        var me = this;
        if(options && (options.method && options.method === 'POST' && options.method === 'PUT') && !callback) {
            me.onFailure('no callback function');
            return false;
        }
        Ext.Ajax.request(Ext.apply({
                success: function(response) {
                    if(response && response.responseText) {
                        var json = Ext.decode(response.responseText);
                        if(callback) {
                            callback(json);
                        }
                    } else {
                        me.onFailure();
                    }
                },
                failure: function(e) {
                    me.onFailure(e.status + ' ' + e.statusText + ', ' + options.url);
                }
            }, (options ? options : {})));
    },

    /**
    * Called on ajax-request failure or data not correct
    * @param {string}   msg     error message
    */
    onFailure: function(msg) {
        //TODO! handle failure
        console.error(msg);
        //Ext.Error.raise(msg);
    }


});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * @class OpenEMap.view.layer.TreeFilter
 */

Ext.define('OpenEMap.view.layer.TreeFilter', {
    extend:  Ext.AbstractPlugin , 
    alias: 'plugin.treefilter', 
    
    collapseOnClear: true, 
    allowParentFolders: false, 

    init: function (tree) {
        var me = this;
        me.tree = tree;

        tree.filter = Ext.Function.bind(me.filter, me);
        tree.clearFilter = Ext.Function.bind(me.clearFilter, me);
    },

    filter: function (value, property, re) {
            var me = this, 
                tree = me.tree,
                matches = [],
                root = tree.getRootNode(),
                visibleNodes = [],
                viewNode;
               
            // property is optional - will be set to the 'text' propert of the  treeStore record by default 
            property = property || 'text';
            
            // the regExp could be modified to allow for case-sensitive, starts  with, etc.
            re = re || new RegExp(value, "ig");

            // if the search field is empty
            if (Ext.isEmpty(value)) {                                           
                me.clearFilter();
                return;
            }

            // expand all nodes for the the following iterative routines
            tree.expandAll();

            // iterate over all nodes in the tree in order to evalute them against the search criteria
            root.cascadeBy(function (node) {
                // if the node matches the search criteria and is a leaf (could be  modified to searh non-leaf nodes)
                if (node.get(property).match(re)) {
                    // add the node to the matches array
                    matches.push(node);
                }
            });

            // if me.allowParentFolders is false (default) then remove any  non-leaf nodes from the regex match
            if (me.allowParentFolders === false) {
                Ext.each(matches, function (match) {
                    if (!match.isLeaf()) {
                        Ext.Array.remove(matches, match);
                    }
                });
            }

            // loop through all matching leaf nodes
            Ext.each(matches, function (item, i, arr) {
                // find each parent node containing the node from the matches array
                root.cascadeBy(function (node) {
                    if (node.contains(item) === true) {
                        // if it's an ancestor of the evaluated node add it to the visibleNodes  array
                        visibleNodes.push(node);
                    }
                });
                // if me.allowParentFolders is true and the item is  a non-leaf item
                if (me.allowParentFolders === true && !item.isLeaf()) {
                    // iterate over its children and set them as visible
                    item.cascadeBy(function (node) {
                        visibleNodes.push(node);
                    });
                }
                // also add the evaluated node itself to the visibleNodes array
                visibleNodes.push(item);
            });

            // finally loop to hide/show each node
            root.cascadeBy(function (node) {
                // get the dom element assocaited with each node
                viewNode = Ext.fly(tree.getView().getNode(node));
                // the first one is undefined ? escape it with a conditional
                if (viewNode) {
                    viewNode.setVisibilityMode(Ext.Element.DISPLAY);
                    // set the visibility mode of the dom node to display (vs offsets)
                    viewNode.setVisible(Ext.Array.contains(visibleNodes, node));
                }
            });
        }, 

        clearFilter: function () {
            var me = this,
                tree = this.tree,
                root = tree.getRootNode();

            if (me.collapseOnClear) {
                // collapse the tree nodes
                tree.collapseAll();
            }
            // final loop to hide/show each node
            root.cascadeBy(function (node) {
                // get the dom element assocaited with each node
                viewNode = Ext.fly(tree.getView().getNode(node));
                // the first one is undefined ? escape it with a conditional and show  all nodes
                if (viewNode) {
                    viewNode.show();
                }
            });
        }
    });

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Add layer view
 * View includes a drag/drop and collapsable layer tree
 */

Ext.define('OpenEMap.view.layer.Add' ,{
    extend:  OpenEMap.view.layer.Tree ,
    
               
                                    
                                         
                                            
      

    title: 'Lägg till lager',
    headerPosition: 'top',
    collapsible: true,
    collapseMode: 'header',
    collapseDirection : 'right',
    titleCollapse: true,

    viewConfig: {
         plugins: {
            ptype: 'treeviewdragdrop',
            enableDrop: false
        },
        copy: true
    },

    // Layer tree filtering
    plugins: {
        ptype: 'treefilter',
        allowParentFolders: true
    },
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            layout: 'fit',
            items: [{
                xtype: 'trigger',
                triggerCls: 'x-form-clear-trigger',
                onTriggerClick: function () {
                    this.reset();
                    this.focus();
                },
                listeners: {
                    change: function (field, newVal) {
                        var tree = field.up('treepanel');
                        tree.filter(newVal);
                    },
                    buffer: 250
                }
            }]
        }
    ],

    initComponent: function() {
        var me = this;
        
        this.columns = [
            {
                xtype: 'treecolumn',
                flex: 1,
                dataIndex: 'text'
            }
//            , me.metadataColumn
        ];

        this.store = Ext.create('OpenEMap.data.GroupedLayerTree');
        
        Ext.Ajax.request({
            url: OpenEMap.basePathProxy + OpenEMap.wmsURLs.getCapabilities,
            success: this.parseCapabilities,
            scope: this,
            disableCaching: false
        });

        this.callParent(arguments);      
    },
    
    parseCapabilities: function(response) {
        var format = new OpenLayers.Format.WMSCapabilities();
        var wms = format.read(response.responseText);
        
        var root = this.store.setRootNode({});
        
        var stripName = function(name) {
            var parts = name.split(':');
            return parts.length > 1 ? parts[1] : name;
        };
        
        wms.capability.layers.sort(function(a, b) {
            if (stripName(a.name) < stripName(b.name)) {
                return -1;
            }
            if (stripName(a.name) > stripName(b.name)) {
                return 1;
            }
            return 0;
        });
        
        var children = wms.capability.layers.map(function(layer) {
            var layerConfig = {
                'text': stripName(layer.name),
                'leaf': true,
                'checked_': true, // internal checked status
                'title': layer.title,
                'name': layer.title,
                'queryable': layer.queryable,
                'clickable': layer.queryable,
                'isGroupLayer': false,
                'visibility': true,
                'metadataURL': layer.metadataURLs.length > 0 ? layer.metadataURLs[0] : null,
                'wms':{
                    'url': OpenEMap.wmsURLs.url,
                    'params': {
                        'LAYERS': layer.name,
                        'FORMAT': 'image/png',
                        'TRANSPARENT': true
                    },
                    'options': {
                        'isBaseLayer': false,
                        'visibility': true
                    }
                }
            };
            
            var parser = Ext.create('OpenEMap.config.Parser');
            layerConfig.layer = parser.createLayer(layerConfig);

            return layerConfig;
        });
        
        root.appendChild(children);
    }

});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
Ext.define('OpenEMap.view.MetadataWindow' ,{
	extend:  Ext.Window ,

               
                       
      

	title: 'Metadata',
	width: 600,
	height: 500,
	border: 0,
    layout: 'fit',
	closeAction: 'hide',

    /**
    * Translation constant
    */
    TRANSLATION: {
        sv: {
            tag: {
                // Hide some elements
                'gmd:citation': '',
                'gmd:CI_Address': '',
                'gmd:CI_Citation': '',
                'gmd:CI_Contact': '',
                'gmd:CI_Date': '',
                'gmd:CI_Telephone': '',
                'gmd:CI_ResponsibleParty': '',
                'gmd:identificationInfo': '',
                'gmd:EX_BoundingPolygon': '',
                'gmd:EX_Extent': '',
                'gmd:EX_GeographicBoundingBox': '',
                'gmd:EX_GeographicDescription': '',
                'gmd:EX_TemporalExtent': '',
                'gmd:EX_VerticalExtent': '',
                'gmd:MD_BrowseGraphic': '',
                'gmd:MD_Constraints': '',
                'gmd:MD_Identifier': '',
                'gmd:MD_Keywords': '',
                'gmd:MD_LegalConstraints': '',
                'gmd:MD_Metadata': '',
                'gmd:MD_MaintenanceInformation': '',
                'gmd:MD_SecurityConstraints': '',
                'gmd:thesaurusName': '',
                'gmd:voice': '',
                'srv:SV_ServiceIdentification': '',

                // Swedish translation
                'gmd:accessConstraints': 'Nyttjanderestriktioner',
                'gmd:abstract': 'Sammanfattning',
                'gmd:address': 'Adress',
                'gmd:alternateTitle': 'Alternativ titel',
                'gmd:city': 'Stad',
                'gmd:classification': 'Klassificering',
                'gmd:contact': 'Metadatakontakt',
                'gmd:contactInfo': 'Kontaktinformation',
                'gmd:date': 'Datum',
                'gmd:dateStamp': 'Datum',
                'gmd:dateType': 'Datumtyp',
                'gmd:descriptiveKeywords': 'Nyckelordslista',
                'gmd:electronicMailAddress': 'E-post',
                'gmd:fileIdentifier': 'Identifierare för metadatamängden',
                'gmd:graphicOverview': 'Exempelbild',
                'gmd:hierarchyLevel': 'Hierarkisk nivå (Resurstyp)',
                'gmd:individualName': 'Person',
                'gmd:identifier': 'Identifierare',
                'gmd:keyword': 'Nyckelord',
                'gmd:language': 'Språk',
                'gmd:metadataStandardName': 'Metadatastandardversion',
                'gmd:metadataStandardVersion': 'Metadatastandard',
                'gmd:organisationName': 'Organisation',
                'gmd:otherConstraints': 'Andra restriktioner',
                'gmd:phone': 'Telefonnummer',
                'gmd:pointOfContact': 'Kontakt',
                'gmd:resourceConstraints': 'Restriktioner och begränsningar',
                'gmd:role': 'Ansvarsområde',
                'gmd:status': 'Status',
                'gmd:title': 'Titel',
                'gmd:type': 'Typ',
                'gmd:useLimitation': 'Användbarhetsbegränsningar'
            },
            codeListValue: {
                'swe': 'Svenska',
                'service': 'Tjänst',
                'pointOfContact': 'Kontakt'
            }
        }
    },

    /**
    * Init component
    */
    initComponent : function() {
        
        this.overviewTab = new Ext.Panel ({
            title: 'Översikt'
        });

        this.metadataTab = new Ext.Panel ({
            title: 'Information om metadata'
        });

        this.dataTab = new Ext.Panel ({
            title: 'Information om data'
        });
      
        this.qualityTab = new Ext.Panel ({
            title: 'Kvalitet'
        });

        this.distributionTab = new Ext.Panel ({
            title: 'Distribution'
        });

        this.restTab = new Ext.Panel ({
            title: 'Rest'
        });

        this.items = Ext.create('Ext.tab.Panel', {
            activeTab: 0,
            defaults: {
                autoScroll: true
            },
            items: [
                this.overviewTab,
                this.metadataTab,
                this.dataTab,
                this.qualityTab,
                this.distributionTab,
                this.restTab
            ]
        });

        this.callParent(arguments);
    },

    /**
    * Render metadata into tab-panel for specific UUID
    * @param {string}   UUID    metadata uuid
    */
	showMetadata: function(UUID) {
		var me = this;
		this.dataHandler.getMetadata(UUID, function(json) {
			if(json.children) {
                var result = me.parseMetadata(json.children);
                me.overviewTab.html = result.overview;
    			me.metadataTab.html = result.metadata_info;
                me.dataTab.html = result.data_info;
                me.qualityTab.html = result.quality;
                me.distributionTab.html = result.distribution;
                me.restTab.html = result.rest;
    			me.show();
            }
		});
	},



    /**
    * Try to translate value of specific type
    * @param {string}   type    tag-type
    * @param {string}   value   value to translate
    */
    translate: function(type, value) {
        var language = 'sv';
        var traslatedTag = null;
        try {
            traslatedTag = this.TRANSLATION[language][type][value];
            if(typeof traslatedTag !== 'string') {
                traslatedTag = value;
            }
        }
        catch(err) {
            translateTag = null;
        }
        return traslatedTag;
        
    },

    /**
    * Parse text element for specific node
    * @param {Object} [node] xml-node
    */
    parseMetadataTextTag: function (node) {
        var text = null;
        if(node.tag) {
            text = this.translate('tag', node.tag);
            text = (text !== null) ? (text !== '' ? '<b>' + text + '</b>' : '') : null;
        }
        if(node.text) {
            text = node.text;
        }
        if(node.attributes) {
            if(node.attributes.codeListValue) {
                text = this.translate('codeListValue',node.attributes.codeListValue);
            }
        }
        
        return text;
    },

    /**
    * Get groups for specific metadata node key. If no matching group place it in rest.
    * @param {string} [str] string to group
    * @param {Object} [group_by] object to group by
    **/
    getGroups: function(str, group_by) {
        var groups = [];
        for (var key in group_by) {
            for (var i = 0; i < group_by[key].length; i++) {
                if(str.indexOf(group_by[key][i]) !== -1) {
                    groups.push(key);
                }
            }
        }
        if(groups.length === 0) {
            groups.push('rest');
        }
        return groups;
    },

    /**
    * Iterates over metadata json to convert to renderable html
    * @param {Object} [node] xml-node
    * @param {Object} [result] resulting object
    * @param {Object} [group_by] object to group by
    * @param {string} [parent_node_tag] parent tag name
    */
    metadataIterator: function(node, result, group_by, parent_node_tag) {
        // Node tag
        var nodeTag = this.parseMetadataTextTag(node);
        // Current node identifier
        var currentTag = (typeof parent_node_tag !== 'undefined' ? (parent_node_tag + '>') : '') + node.tag;
        // Goups to include tag in
        var groups = this.getGroups(currentTag, group_by);
        // For each group
        for (var i = 0; i < groups.length; i++) {
            var group = groups[i];
            if(typeof result[group] !== 'string') {
                result[group] = '';
            }

            if(nodeTag !== null) {
                result[group] += '<li>';
                result[group] += nodeTag;

                // Loop over child nodes
                if(node.children && i === 0) {
                    result[group] += nodeTag !== '' ? '<ul>' : '';
                    for (var j = 0; j < node.children.length; j++) {
                        this.metadataIterator(node.children[j], result, group_by, currentTag);
                    }
                    result[group] += nodeTag !== '' ? '</ul>' : '';
                }

                result[group] += '</li>';
            }
        }
    },

    /**
    * Parse metadata-json-response into html
    * @param  {Object} [json] json response object
    * @return {Object} [result] grouped result object  
    */
    parseMetadata: function(json) {
        var result = {};
        // Group metadata to prepare to show in tabs
        var group_by = {
            overview: [
                'gmd:MD_Metadata>gmd:identificationInfo>srv:SV_ServiceIdentification>gmd:citation>gmd:CI_Citation>gmd:title',
                'gmd:MD_Metadata>gmd:identificationInfo>srv:SV_ServiceIdentification>gmd:abstract',
                'gmd:MD_Metadata>gmd:identificationInfo>srv:SV_ServiceIdentification>gmd:descriptiveKeywords',
                'gmd:MD_Metadata>gmd:identificationInfo>srv:SV_ServiceIdentification>gmd:graphicOverview'
            ],
            metadata_info: [
                'gmd:MD_Metadata>gmd:fileIdentifier',
                'gmd:MD_Metadata>gmd:language',
                'gmd:MD_Metadata>gmd:dateStamp',
                'gmd:MD_Metadata>gmd:hierarchyLevel',
                'gmd:MD_Metadata>gmd:metadataStandardName',
                'gmd:MD_Metadata>gmd:metadataStandardVersion',
                'gmd:MD_Metadata>gmd:contact'
            ],
            data_info: [
                'gmd:MD_Metadata>gmd:identificationInfo'
            ],
            quality: [
                'gmd:MD_Metadata>gmd:dataQualityInfo'
            ],
            distribution: [
                'gmd:MD_Metadata>gmd:distributionInfo'
            ]
        };

        // Iterate over metadata to render html
    	this.metadataIterator(json[0], result, group_by);
        return result;
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * OpenEMap map configuration model
 */

Ext.define('OpenEMap.model.MapConfig' ,{
    extend:  Ext.data.Model ,

    fields: [ 
    	'configId', 
    	'name',
    	'isPublic',
    	'username' 
    ]
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
//@requires OpenEMap

/**
 * Map configuration store
 * Store to list map configurations
 */

Ext.define('OpenEMap.data.SavedMapConfigs' ,{
    extend:  Ext.data.Store ,

               
                                  
      
    model: 'OpenEMap.model.MapConfig',
    storeId: 'savedMapConfigs',
    autoLoad: true,
    
    constructor: function(config) {
        this.proxy = {
            type: 'rest',
            appendId: true,
	        url: ((OpenEMap && OpenEMap.wsUrls && OpenEMap.wsUrls.basePath) ? OpenEMap.wsUrls.basePath : '') + 
	        		((OpenEMap && OpenEMap.wsUrls && OpenEMap.wsUrls.adminconfigs) ? OpenEMap.wsUrls.adminconfigs : '') + '/configlist',
            reader: {
                type: 'json',
                root: 'configs'
            },
            writer: {            
                type: 'json'
            }
        };

        this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * @class OpenEMap.view.SavedMapConfigs
 */
Ext.define('OpenEMap.view.SavedMapConfigs' ,{
    extend:  Ext.grid.Panel ,
                                                
    autoScroll: true,
    hideHeaders: true,

    id: 'savedMapConfigsGrid',

    constructor: function() {
	    this.store = Ext.create('OpenEMap.data.SavedMapConfigs');
        this.columns = [
            { 
            	header: 'Name',  
            	dataIndex: 'name',
            	flex: 1,
                tooltip: 'Ladda',
                renderer: function(value, metadata, record) {
                	metadata.tdAttr = 'data-qtip="Ladda karta"';
                	return value;
                },
			    listeners: {
		            click: function(grid, rowIndex, cellIndex, column, e, record, tr) {
						var requestUrl = ((OpenEMap && OpenEMap.wsUrls && OpenEMap.wsUrls.basePath) ? OpenEMap.wsUrls.basePath : '') + 
							((OpenEMap && OpenEMap.wsUrls && OpenEMap.wsUrls.configs) ? OpenEMap.wsUrls.configs : '') + 
							'/config/' + record.get('configId');
						Ext.Ajax.request({
							scope: this,
							url: requestUrl,
							success: function(response) {
								this.client.destroy();
								this.client.configure(JSON.parse(response.responseText), this.client.initialOptions);
								e.stopEvent();
								return false;
							},
							failure: function(response) {
								Ext.MessageBox.alert('Kommunikationsproblem', 'Kartan kan inte öppnas. Kontakta systemadministratör.');
							}
						});
	               }.bind(this)
			    }
            },
            {
                xtype: 'actioncolumn',
                width: 40,
                getClass: function(value, meta) {
			    	return meta.record.get('isPublic') ? 'action-none' : 'action-remove';
	    		},
                tooltip: 'Ta bort',
                handler: function(grid, rowIndex, cellIndex, column, e, record, tr) {
                    //TODO! change to proper rest store delete
                    if (record.get('isPublic')) {
                    	return false;
                    }
                    
                    Ext.MessageBox.confirm('Ta bort', 'Vill du verkligen ta bort konfigurationen?', function(btn) {
                        if(btn === 'yes') {
                            var store = grid.getStore();
                            grid.panel.dataHandler.deleteConfiguration(record.get('configId'),{ configId: record.get('configId') });
                            store.removeAt(rowIndex);
                        }
                    });
                    e.stopEvent();
                    return false;
                }
            }
        ];
    
    	this.callParent(arguments);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * 
 */

Ext.define('OpenEMap.view.layer.Advanced' ,{
	extend:  Ext.container.Container ,

	           
		                                     
		                          
		                           
		                               
		                                
		                            
		                                   
		                                                           
	  
//	resizable: true,
//	resizeHandles: "s",
	layout: {
		type: 'vbox',
		align: 'stretch'
	},
	
 	initComponent: function(config) {
 		var me = this;

 		this.dataHandler = Ext.create('OpenEMap.data.DataHandler');

 		this.metadataWindow = Ext.create('OpenEMap.view.MetadataWindow', {
 			dataHandler: this.dataHandler
 		});

 		this.savedMapConfigs = Ext.create('OpenEMap.view.SavedMapConfigs', {
 			dataHandler: this.dataHandler,
 			autoScroll: true,
 			client: this.client,
	        title: 'Kartor',
	        collapsible: true,
	        flex: 1,
	        minHeight: 100
 		});
 		
 		var renameAction = Ext.create('Ext.Action', {
            //iconCls: 'action-load',
            text: 'Byt namn...',
            disabled: true,
            handler: function(widget, event) {
                var node = this.showOnMapLayerView.getSelectionModel().getSelection()[0];
                if (node && node.get('isGroupLayer')) {
                    Ext.Msg.prompt('Byt namn...', 'Ange nytt namn:', function(btn, text) {
                        if (btn == 'ok'){
                            node.set('text', text);
                        }
                    }, window, false, node.get('text'));

                    // TODO - Add functionality for changing name of a ordinary layer 
/*                } else if (node && node.get('name')) {
                    Ext.Msg.prompt('Byt namn...', 'Ange nytt namn:', function(btn, text) {
                        if (btn == 'ok'){
                            node.set('name', text);
                        }
                    }, window, false, node.get('name'));
*/                	
                }
            }.bind(this)
        });
 		
 		var createGroupAction = Ext.create('Ext.Action', {
            //iconCls: 'action-load',
            text: 'Nytt grupplager',
            disabled: true,
            handler: function(widget, event) {
                var node = this.showOnMapLayerView.getSelectionModel().getSelection()[0];
                if (node) {
                    Ext.Msg.prompt('Nytt grupplager', 'Ange namn:', function(btn, text) {
                        if (btn == 'ok'){
                            var group = Ext.create('OpenEMap.model.GroupedLayerTreeModel', {
                                text: text,
                                checked: true,
                                isGroupLayer: true
                            });
                            node.appendChild(group);
                        }
                    });
                }
            }.bind(this)
        });
 		
 		var contextMenu = Ext.create('Ext.menu.Menu', {
            items: [
                renameAction,
                createGroupAction
            ]
        });

		this.showOnMapLayerView = Ext.create('OpenEMap.view.layer.Tree', {
			title: 'Visas på kartan',
    		split: true,
    		border: false,
    		mapPanel: this.mapPanel,
    		client: this.client,
    		rootVisible: false,
	        collapsible: true,
	        autoscroll: true,
	        flex: 4,
    		
    		viewConfig: {
		        plugins: {
	                ptype: 'treeviewdragdrop',
	                allowContainerDrops: true,
	                allowParentInserts: true
	            },
	            listeners: {
                    itemcontextmenu: function(view, rec, node, index, e) {
                        e.stopEvent();
                        contextMenu.showAt(e.getXY());
                        return false;
                    }
                }
		    },

    		columns: [
	            {
	                xtype: 'gx_treecolumn',
	                flex: 1,
	                dataIndex: 'text'
	            }, 
/*	            Ext.create('OpenEMap.action.MetadataInfoColumn', {
		 			metadataWindow: this.metadataWindow,
		 			dataHandler: this.dataHandler
		 		}),
*/	            {
	                xtype: 'actioncolumn',
	                width: 40,
	                iconCls: 'action-remove',
	                tooltip: 'Ta bort',
	                handler: function(grid, rowIndex, colIndex) {
	                	var node = grid.getStore().getAt(rowIndex);
	                	// Remove childs
	                	for (var i = 0; i < node.childNodes.length; i++) {
	                		node.removeChild(node.childNodes[i]);
	                	}
					    node.remove();
					},
					dataHandler: this.dataHandler
	            }
	        ],
	        buttons: [
	        	{
		            text: 'Spara kartinnehåll',
		            handler: function() {
		            	if(me.orginalConfig) {
		            		var conf = Ext.clone(me.orginalConfig);
		            		Ext.MessageBox.prompt(
		            			'Namn', 
		            			'Ange ett namn:', 
		            			function(btn, text) {
		            				if (btn == 'ok' && text.length > 0) {
		            				    conf = me.getConfig();
		            				    // Set username and isPublic
		            				    conf.isPublic = false;
		            				    if (!(OpenEMap && OpenEMap.username)) {
		            				    	Ext.Error.raise('Username undefined!');
		            				    }
		            				    conf.username = OpenEMap.username;
						            	if(text !== conf.name) {
						            		// Save new config
						            		conf.name = text;
						            		me.dataHandler.saveNewConfiguration(conf, function(json) {
						            			me.savedMapConfigs.getStore().load();
            				                    me.savedMapConfigs.client.destroy();
										        me.savedMapConfigs.client.configure(JSON.parse(json.json), me.savedMapConfigs.client.initialOptions);
						            		});
						            		
						            	} else if(conf.configId){
						            		// Update config
						            		me.dataHandler.updateConfiguration(conf.configId, conf);
					            			me.savedMapConfigs.getStore().load();
						            	}
						            }
		            			},
		            			this,
		            			false,
		            			conf.name
		            		);
			            	
		            	}
		            }
		        }
		    ]
    	});
    	
    	this.showOnMapLayerView.getSelectionModel().on({
            selectionchange: function(sm, selections) {
//                renameAction.enable();
                if (selections.length === 1 && selections[0].data.isGroupLayer) {
                    renameAction.enable();
                    createGroupAction.enable();
                } else {
                    renameAction.disable();
                    createGroupAction.disable();
                }
            }
        });

	  	this.items = [
			me.showOnMapLayerView,
			me.savedMapConfigs
/*			
	    	{
				title: 'Sparade kartor',
				region: 'center',
				xtype: 'panel',
				border: false,
				collapsible: false,
				titleCollapse: true,
				items: me.savedMapConfigs
			}
*/		];

    	this.callParent(arguments);
    },
    getConfig: function(includeLayerRef) {
        return this.showOnMapLayerView.getConfig(includeLayerRef);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
Ext.define('OpenEMap.view.layer.Basic' ,{
    extend:  OpenEMap.view.layer.Tree ,

    rootVisible: false,
    width: 300,
	resizable: true,
	resizeHandles: 's',

    initComponent: function() {
        if (!this.renderTo) {
            this.title = 'Lager';
            this.bodyPadding = 5;
            this.collapsible = true;
        }
        
        this.callParent(arguments);
    }


});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Initializes GUI from configuration
 * Initialize where to place the different GUI components, and if they should be floating components or not 
 */
Ext.define('OpenEMap.Gui', {
    activeAction: null,
                                                   
                                                
                                               
                                              
                                              
                                            
                                            
                                          
                                             
                                             
                                                    
                                                
                                            
                                           
                                       
                                       
                                                
                                          
                                                   
                                               
                                   
                                            
                                                
                                               
                                        
                                              
                                         
                                              
                                           
                                            
                                            
                                                
    objectConfigWindowTitle: 'Object configuration',
    constructor : function(config) {
        this.config = config.config;
        this.gui = config.gui;
        this.map = config.map;
        this.orginalConfig = config.orginalConfig;
        this.serverStore = config.serverStore;
        this.search = config.config.search;
        this.client = config.client;

        // GUI defaults
        if (this.gui === undefined) {
            this.gui = {};
        }
        if (this.gui.map === undefined) {this.gui.map = false;}
        if (this.gui.map === {}) {this.gui.map = false;}
        if (this.gui.layerControl === undefined) {this.gui.layerControl = {};}
        if (this.gui.addLayerControl === undefined) {this.gui.addLayerControl = {};}
//        if (this.gui.rightPanel === undefined) {this.gui.rightPanel = {};}
        
        this.mapPanel = Ext.create('OpenEMap.view.Map', {
            map: this.map,
            extent: this.config.extent,
            config: this.config,
            listeners: {
                'afterrender': function() {
                    if (this.config.attribution) {
                        var el = this.mapPanel.getEl();
                        Ext.DomHelper.append(el, '<span class="unselectable attribution">'+this.config.attribution+'</span>');
                    }
                },
                scope: this
            }
        });
                
        this.createToolbar();
        this.createZoomToolPanel();
        this.createObjectConfigPanel();
        this.createScalebarPanel();
        this.createShowCoordinatePanel();
        this.createSearchCoordinatePanel();
        this.createLayerControl();
        this.createBaseLayersPanel();
        
        var items = [];
        items.push(this.mapPanel);
        if (this.zoomTools && !this.gui.zoomTools.renderTo) items.push(this.zoomTools);
        if (this.objectConfig && !this.gui.objectConfig.renderTo) items.push(this.objectConfig);
        if (this.scalebar && !this.gui.scalebar.renderTo) items.push(this.scalebar);
        if (this.showCoordinate && !this.gui.showCoordinate.renderTo) items.push(this.showCoordinate);
        if (this.searchCoordinate && !this.gui.searchCoordinate.renderTo) items.push(this.searchCoordinate);
        if (this.layerControl && !this.gui.layerControl.renderTo) items.push(this.layerControl);
        if (this.addLayerControl && !this.gui.addLayerControl.renderTo) items.push(this.addLayerControl);
//        if (this.rightPanel && !this.gui.rightPanel.renderTo) items.push(this.rightPanel);
        if (this.baseLayers && !this.gui.baseLayers.renderTo) items.push(this.baseLayers);
        if (this.toolbar && !this.gui.toolbar.renderTo) items.push(this.toolbar);
        
        // NOTE: Generic ES search as a floating centered field
        /*this.search = Ext.create('OpenEMap.form.Search', {
            mapPanel : this.mapPanel,
            width: 300,
            style: {
                right: '20px',
                bottom: '76px'
            }
        });
        items.push(this.search);*/
        
        // create map rendered to a target element or as viewport depending on config
        if (this.gui.map) {
            var element = this.gui.map.renderTo ? Ext.get(this.gui.map.renderTo) : undefined;
            this.container = Ext.create('Ext.container.Container', Ext.apply({
                layout : 'absolute',
                border: false,
                width: element ? element.getWidth() : undefined,
                height: element ? element.getHeight() : undefined,
                items : items
            }, this.gui.map));
//            window.addEventListener('resize', function() {
//            	element.setWidth(window.innerWidth);
//            	element.setHeight(window.innerHeight);
//            	this.mapClient.mapPanel.setSize(window.innerWidth, window.innerHeight);
//            	element.setSize(window.innerWidth, window.innerHeight);
//           });
        } else {
            this.container = Ext.create('Ext.container.Viewport', {
                layout : 'absolute',
                items : items
            });
        }
    },
    destroy: function() {
        if (this.mapPanel) this.mapPanel.destroy();
        if (this.zoomTools) this.zoomTools.destroy();
        if (this.mapLayers) this.mapLayers.destroy();
        if (this.searchFastighet) this.searchFastighet.destroy();
        if (this.searchCoordinate) this.searchCoordinate.destroy();
        if (this.showCoordinate) this.showCoordinate.destroy();
        if (this.toolbar) this.toolbar.destroy();
        if (this.layerControl) this.layerControl.destroy();
        if (this.addLayerControl) this.addLayerControl.destroy();
//        if (this.rightPanel) this.rightPanel.destroy();
        if (this.baseLayers) this.baseLayers.destroy();
        if (this.objectConfig) this.objectConfig.destroy();
        if (this.objectConfigWindow) this.objectConfigWindow.destroy();
        if (this.scalebar) this.scalebar.destroy();
        if (this.container) this.container.destroy();
        //if (this.search) this.search.destroy();
    },
    onToggle: function(button, pressed) {
        var action = button.baseAction;
        
        if (!this.objectConfig) return;
        
        // NOTE: want the effect of unselecting all and hiding object dialog on detoggle, but that is triggered after toggle... so this will have to do
        if (pressed) {
            this.mapPanel.unselectAll();
            this.objectConfig.hide();
            this.activeAction = action;
        }
        
        action.toggle(pressed);
    },
	/**
	 * Create toolbar
     * @private
	 */
    createToolbar: function() {
        var basePath = this.config.basePath;
        var layers = this.config.layers;
        
        var createAction = function(type) {
            var cls;
            if((Ext.isObject(type) && Ext.Object.getSize(type) > 0) || (!Ext.isObject(type) && !Ext.isEmpty(type))) {
                if (type === this.config.tools[this.config.tools.length-1]) cls = 'oep-tools-last';
                
                var config = {
                    map: this.map,
                    mapPanel: this.mapPanel,
                    cls: cls
                };
                
                if (type.constructor === Object) {
                    Ext.apply(config, type);
                    type = config.type;
                    delete config.type;
                }

                if (type == 'ZoomSelector') {
                    return Ext.create('OpenEMap.form.ZoomSelector', {
                        map: this.map,
                        cls: cls
                    });
                } else if (type == 'DrawObject') {
                     config.objectConfigView = this.objectConfig;
                } else if (type == 'Identify') {
                    config.basePath = basePath;
                    config.layers = layers;
                    config.client = this.client;
                } else if (type == 'Permalink') {
                    config.client = this.client;
                } else if (type == 'Popup') {
                    config.layers = layers;
					if ((config.showOnlyFirstHit === undefined) || (config.showOnlyFirstHit === null)) {
						config.showOnlyFirstHit = true;
	                }
				}
				
                var action = Ext.create('OpenEMap.action.' + type, config);
                if (config.activate && action.control) {
                    this.controlToActivate = action.control;
                }
                var button = Ext.create('Ext.button.Button', action);
                button.on('toggle', this.onToggle, this);
                return button;
            }
        };
        
        if (!this.config.tools) {
            this.config.tools = [];
        }
        
        // TODO: need to apply cls: 'oep-tools-last' to last item...
        var tbar = this.config.tools.map(createAction, this);
        
        // calc width of toolbar
        var width = 0; // padding
        tbar.forEach(function(item) {
            if (item){
                if (!item.hideFromToolbar) {
	                if (item.constructor == String) {
	                    width += 1; // separator
	                } else if (item.width) {
	                    width += item.width + 1;
	                } else {
	                    width += 38; // button
	                }
	                // add spacing to next control
	                //width += 8;
                }
            }
        });
        //width += 3; // padding
                
        // create toolbar as floating left panel if no renderTo target is configured
        if (this.gui.toolbar && !this.gui.toolbar.renderTo) {
            this.toolbar = Ext.create('Ext.toolbar.Toolbar', Ext.apply({
                cls: 'oep-tools',
                y : 20,
                x : 20,
                width: width,
                items: tbar
            }, this.gui.toolbar));
        } else if (this.gui.toolbar && this.gui.toolbar.renderTo) {
            this.toolbar = Ext.create('Ext.toolbar.Toolbar', Ext.apply({
                cls: 'oep-tools',
                width : this.gui.toolbar.width || width,
                items: tbar
            }, this.gui.toolbar));
        }
    },
    /**
     * Create right panel including 
     * - Layers panel
     * - SearchParcel panel
     * @private
     */
    createLayerControl: function() {
        
        var layerControlItems = [];
        // default position for layerControl and addLayerControl
        if (!this.gui.layerControl.y) {this.gui.layerControl.y = 20;}
        if (!this.gui.layerControl.right) {this.gui.layerControl.right = 20;}
        if (!this.gui.layerControl.style) {this.gui.layerControl.style = 'right: ' + this.gui.layerControl.right + 'px';}
        if (!this.gui.layerControl.width) {this.gui.layerControl.width = 300;}
        if (!this.gui.layerControl.maxHeight) {this.gui.layerControl.maxHeight = window.innerHeight-this.gui.layerControl.y-60;}

        if (!this.gui.addLayerControl.y) {this.gui.addLayerControl.y = this.gui.layerControl.y;}
        if (!this.gui.addLayerControl.right) {this.gui.addLayerControl.right = this.gui.layerControl.width + this.gui.layerControl.right;}
        if (!this.gui.addLayerControl.style) {this.gui.addLayerControl.style = 'right: ' + this.gui.addLayerControl.right + 'px';}
        if (!this.gui.addLayerControl.width) {this.gui.addLayerControl.width = 300;}
        if (!this.gui.addLayerControl.maxHeight) {this.gui.addLayerControl.maxHeight = window.innerHeight-this.gui.addLayerControl.y-56;}
        
        if (this.gui.layers) {
	        // Checks whether the advanced or basic Layer control should be used
	        if (this.gui.layers && this.gui.layers.type === 'advanced') {
	            this.mapLayers = Ext.create('OpenEMap.view.layer.Advanced', Ext.apply({
	                mapPanel : this.mapPanel,
	                orginalConfig: this.orginalConfig,
	                client: this.client,
	                flex: 1
	            }, this.gui.layers));
	            
	            // Adds the Add layer control
				this.addLayerControl = Ext.create('OpenEMap.view.layer.Add', {
				    mapPanel: this.mapPanel,
				    border: false,
		        	renderTo: this.gui.addLayerControl.renderTo,
		        	y: this.gui.addLayerControl.y,
		            style : this.gui.addLayerControl.style,
				    width: this.gui.addLayerControl.width,
				    maxHeight: this.gui.addLayerControl.maxHeight,
				    collapsible: true,
				    dataHandler: this.dataHandler,
				    metadataColumn: Ext.create('OpenEMap.action.MetadataInfoColumn',{
			 			metadataWindow: this.metadataWindow,
			 			dataHandler: this.dataHandler
			 		})
				});

	        } else if (this.gui.layers && this.gui.layers.type === 'listconfigs') { 
	            this.mapLayers = Ext.create('OpenEMap.view.layer.Advanced', Ext.apply({
	                mapPanel : this.mapPanel,
	                orginalConfig: this.orginalConfig,
	                client: this.client,
	                flex: 1
	            }, this.gui.layers));
	        } else {
	            this.mapLayers = Ext.create('OpenEMap.view.layer.Basic', Ext.apply({
	                mapPanel : this.mapPanel,
	                client: this.client,
	                flex: 1
	            }, this.gui.layers));
	        }
	        
	        // If the layers panel not should be rendered to div, add it to the right panels items
	        if (!this.gui.layers.renderTo) {
	        	layerControlItems.push(this.mapLayers);
	        }
	    }
        
        
        if (this.gui.searchFastighet && this.search)  {
	        // Create SearchParcel control
	        this.searchFastighet = Ext.create('OpenEMap.view.SearchFastighet', Ext.apply({
	            mapPanel : this.mapPanel,
	            basePath: this.config.basePath,
	            search: this.search,
		        collapsible: true,
	            height: 120,
	            width: this.gui.layerControl.width
	        }, this.gui.searchFastighet));
            if (!this.gui.searchFastighet.renderTo) {
            	layerControlItems.push(this.searchFastighet);
            }
        }

        // create right panel containing layers and search panels if no renderTo target is configured
        if (layerControlItems.length > 0) {
            this.layerControl = Ext.create('Ext.panel.Panel', {
	        	renderTo: this.gui.layerControl.renderTo,
	            y : this.gui.layerControl.y,
                style : this.gui.layerControl.style,
                layout : {
                    type: 'vbox',
                    align : 'stretch'
                },
                width : this.gui.layerControl.width,
                maxHeight: this.gui.layerControl.maxHeight,
                border: false,
                bodyStyle: {
                    background: 'transparent'
                },
                items : layerControlItems
	        });
	    }
	    
/*	    if (this.addLayerControl) {
	        this.rightPanel = Ext.create('Ext.panel.Panel', {
	            title: 'Lager',
	            collapsible: true,
	        	renderTo: this.gui.rightPanel.renderTo,
	            y : this.gui.layerControl.y,
	            style : this.gui.layerControl.style,
	            layout : {
	                type: 'hbox'
	            },
	            width : this.gui.layerControl.width + this.gui.addLayerControl.width,
	            maxHeight: this.gui.layerControl.maxHeight,
	            border: false,
	            bodyStyle: {
	                background: 'transparent'
	            },
	            items : [
	            	this.addLayerControl, 
	            	this.layerControl
	            ]
	        });
       	}
*/

	},
        
	/** 
	 * Create base layers control
	 * @private
	 */ 
    createBaseLayersPanel: function() {
        // Create BaseLayers control
        if (!this.map.allOverlays && this.gui.baseLayers) {
            var layers = this.map.layers;
            var parser = Ext.create('OpenEMap.config.Parser');
            var baseLayers = parser.extractBaseLayers(layers);
	        // Only create if config has baselayers
        	if (baseLayers) {
        		if (!(this.gui.baseLayers.renderTo || this.gui.baseLayers.style)) {
    				this.gui.baseLayers.style = {
    					right: '20px',
    					bottom: '20px'
    				};
        		}
	            this.baseLayers = Ext.create("OpenEMap.view.BaseLayers", Ext.apply({
	                mapPanel : this.mapPanel,
	                renderTo : this.gui.baseLayers.renderTo
	            }, this.gui.baseLayers));
	        }
        }
	},

	/**
	 * Create ZoomTool panel
	 * @private 
	 */	
    createZoomToolPanel: function() {
        // Create ZoomTool control
        if (this.gui.zoomTools) {
    		if (!(this.gui.zoomTools.renderTo || this.gui.zoomTools.style)) {
				this.gui.zoomTools.style = {
					left: '20px',
					top: '76px'
				};
    		}
            this.zoomTools = Ext.create('OpenEMap.view.ZoomTools', Ext.apply({
                mapPanel : this.mapPanel,
                renderTo : this.gui.zoomTools.renderTo,
                width: 36
            }, this.gui.zoomTools));
        }
    },

	/**
	 * Create search coodinates panel
	 * @private 
	 */	
    createSearchCoordinatePanel: function() {
        // Create SearchCoordinate control                
        // only create if renderTo
        if (this.gui.searchCoordinate && this.gui.searchCoordinate.renderTo) {
            this.searchCoordinate = Ext.create('OpenEMap.view.SearchCoordinate', Ext.apply({
                mapPanel : this.mapPanel
            }, this.gui.searchCoordinate));
        }
    },

	/** 
	 * Create object config window
	 * always created inside map div 
	 * @private
	 */ 
    createObjectConfigPanel: function() {
        // Create Object config
        if (this.gui.objectConfig) {
            this.objectConfig = Ext.create('OpenEMap.view.ObjectConfig', Ext.apply({
                mapPanel : this.mapPanel,
                gui: this
            }, this.gui.objectConfig));
            this.objectConfigWindow = Ext.create('Ext.window.Window', Ext.apply({
                title: this.objectConfigWindowTitle,
                width: 480,
                height: 300,
                layout: 'fit',
                closable: false,
                collapsible: true,
                items: this.objectConfig
            }, this.gui.objectConfig));
            this.objectConfigWindow.show();
        }
	},

	/**
	 * Create show coordinate panel
	 * @private 
	 */	
    createShowCoordinatePanel: function() {
        // Create Show Coordinate control 
        // only create if renderTo 
        if (this.gui.showCoordinate && this.gui.showCoordinate.renderTo) {
        	if (!this.cls) {
        		this.cls = 'oep-show-coordinate';
        	} 
        	var cfg = {
                mapPanel : this.mapPanel,
                cls : this.cls,
			    setCoord: function(e) {
			    	var lonlat = this.getLonLatFromPixel(e.xy);
			    	var eC = OpenEMap.mapClient.gui.showCoordinate.getComponent('e');
			    	var nC = OpenEMap.mapClient.gui.showCoordinate.getComponent('n');
			    	eC.setValue(Math.round(lonlat.lon));
			    	nC.setValue(Math.round(lonlat.lat));
			    }
        	};
            this.showCoordinate = Ext.create('OpenEMap.view.ShowCoordinate', Ext.apply(cfg, this.gui.showCoordinate));
		    this.map.events.register("mousemove", this.map, this.showCoordinate.setCoord);
        }
	},

	/**
	 * Create scalebar panel
	 * @private 
	 */	
    createScalebarPanel: function() {
        // Create scalebar control
       	// Position defined in CSS - defaults to lower left corner of map
        if (this.gui.scalebar) {
            this.scalebar = Ext.create('OpenEMap.view.Scalebar', Ext.apply({
                mapPanel : this.mapPanel
            }, this.gui.scalebar));
        }
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Server configuration model
 */

Ext.define('OpenEMap.model.Server' ,{
    extend:  Ext.data.Model ,

    fields: [ 
    	'id', 
    	'type',
    	'url',
    	'proxy'
    ]

});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Server configuration store
 * Stores server configruations
 */

Ext.define('OpenEMap.data.Servers' ,{
    extend:  Ext.data.Store ,

               
                               
      

    model: 'OpenEMap.model.Server',

    storeId: 'servers',

    singelton: true,

    constructor: function(config) {
        config = Ext.apply(this, config);
        /*if(this.url) {
            this.proxy = {
                type: 'ajax',
                url: this.url,
                reader: {
                    type: 'json',
                    root: 'configs'
                }
            };
        }*/
        this.callParent([config]);
    }
});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * ###Parser for configuration JSON
 * Sets defaults, initializes OpenLayers and Ext JS stuff.
 * ###[Config documentation](https://github.com/Sundsvallskommun/OpenEMap-WebUserInterface/blob/master/doc/config.md)
 * ###[Config example](https://github.com/Sundsvallskommun/OpenEMap-WebUserInterface/blob/develop/doc/testAll.json)
 */
Ext.define('OpenEMap.config.Parser', {
                 
                                
                   
      

    constructor: function(config) {
        Ext.apply(this, config);
        this.callParent(arguments);
    },

    /**
     * Parse configuration JSON
     * 
     * Set defaults, initializes OpenLayers and Ext JS stuff.
     */
    parse : function(config) {
        // construct OpenLayers.Map options object
        var options = {
            "fallThrough" : true,
            "controls": ["Navigation", "KeyboardDefaults"],
            "projection": "EPSG:3006",
            "resolutions": [280, 140, 70, 28, 14, 7, 4.2, 2.8, 1.4, 0.56, 0.28, 0.14, 0.112],
            "extent": [608114, 6910996, 641846, 6932596],
            "maxExtent": [487000.0, 6887000.0, 749144.0, 7149144.0],
            "units": "m",
            "municipalities": ['Sundvsall', 'Timrå', 'Kramfors', 'Örnsköldsvik', 'Härnösand']
        };
        
        options.resolutions = config.resolutions || options.resolutions;
        options.units = config.units || options.units;
        options.projection = config.projection || options.projection;
        options.maxExtent = config.maxExtent;
        options.extent = config.extent;
        options.municipalities = config.municipalities || options.municipalities;
        options.controls = options.controls.map(this.createControl);
        
        // allow to override/add other options from map property 
        Ext.apply(options, config.map);
        
        // transform layers for backward compat
        layers = config.layers.map(this.transformLayer);
        
        // Create a Extjs layertree from configuration
        var layerTree = this.parseLayerTree(layers);

        // filter out plain layer definitions (no group)
        var layers = this.extractLayers(layerTree);
        
        // Sort baselayers to put the layer with visibility=true first, because OpenLayers uses the first baselayer as the active one.
        layers = this.sortBaseLayers(layers);

        options.allOverlays = !layers.some(this.isBaseLayer, this);
        
        // Create OpenLayers.Layer.WMS from layer definitions that describe WMS source
        options.layers = layers
            //.filter(this.isWMSLayer)
            .map(function(layer) {
                return layer.layer;
            });
        
        options.layers = options.layers.filter(function(layer) { return layer; });
        
        var map = new OpenLayers.Map(options);
        // Store a reference to the full layer tree
        map.layerTree = layerTree;
        // Store a reference to the layer switcher layer tree
        map.layerSwitcherLayerTree = this.getLayerSwitcherLayers(layerTree);

        return map;
    },

    sortBaseLayers: function(layers) {
    	var baseLayers = layers.filter(function(layer) {return (layer.layer && layer.layer.isBaseLayer);});
    	var overlays = layers.filter(function(layer) {return !(layer.layer && layer.layer.isBaseLayer);});
    	var sorted = false;
    	var sortedBaseLayers = [];
    	for (var ix=0; baseLayers.length > ix; ix++) {
    		if (baseLayers[ix].visibility) {
    			sortedBaseLayers.unshift(baseLayers[ix]);
    		} else {
    			sortedBaseLayers.push(baseLayers[ix]);
    		}
    	}
    	return sortedBaseLayers.concat(overlays);
    },
    /**
    * Iterate over the layertree and create a ExtJS-tree structure
    * @private
    */
    parseLayerTree: function(layers) {
        layers.forEach(this.iterateLayers, this);
        return layers;
    },

    /**
    * Get all layers and layer groups that should show up in the layer switcher
    */
    // TODO - implement support for config tag layer.wms.options.displayInLayerSwitcher
    getLayerSwitcherLayers: function(layers) {
        return layers.filter(function(layer) { 
            return (layer.layers || (this.isWMSLayer(layer) && !this.isBaseLayer(layer))) ? true : false;
        }, this);
    },

    /**
     * Process layers config to return a flat array with layer definitions
     */
    extractLayers: function(layers) {
        // filter out plain layer definitions (no group)
        var plainLayers = layers.filter(function(layer) { return !layer.layers; });
/*        var plainLayers = layers.filter(function(layer) {
        	// Checking layer.layers for backward compability
        	if (layer.isGroupLayer === undefined) {layer.isGroupLayer = layer.layers ? true : false} 
        	return !layer.isGroupLayer; 
        });
*/
        // filter out groups
        var groups = layers.filter(function(layer) { return layer.layers; }).map(function(layer) { return layer.layers; });  
        
        // flatten groups into an array of layer definitions 
        var flattenedGroups = [].concat.apply([], groups);
        // concat all layer definitions
        layers = plainLayers.concat(flattenedGroups);
        // Reverse layers to make the top layer in config land on top in OpenLayers
        layers.reverse();

        return layers;
    },
    /**
     * Extract plain layers from a layertree structure
     * @param layers [Array] array of layers in a tree structure 
     */
    extractPlainLayers: function(layers) {
    	var plainLayers = [];
    	for (var i=0,  tot=layers.length; i < tot; i++) {
   			plainLayers.push(layers[i]);
    		if (layers[i].layers) {
    			plainLayers = plainLayers.concat(this.extractPlainLayers(layers[i].layers));
    		}
    	}
    	return plainLayers;
    },
    /**
     * Extracts WFS-layers
     * @param layers [Array] array of layers
     */
    extractWFS: function(layers) {
        layers = layers.filter(function(layer){ return (layer.wfs && layer.wfs.url); });
        return layers;
    },
    /**
     * Extracts layers missing WFS-tag
     * @param layers [Array] array of layers
     */
    extractNoWFS: function(layers) {
        layers = layers.filter(function(layer){ return !(layer.wfs && layer.wfs.url); });
        return layers;
    },
    /**
     * Extracts layers with valid WMS-tag
     * @param layers [Array] array of layers
     */
    extractWMS: function(layers) {
        layers = layers.filter(function(layer){ return (layer.wms && layer.wms.url); });
        return layers;
    },
    /**
     * Extracts queryable layers
     * @param plainLayers [array] array of flattened layers. 
     */
    extractClickableLayers: function(plainLayers) {
        plainLayers = plainLayers.filter(function(layer) { 
        	return (layer.clickable && layer.queryable); 
        });
        return plainLayers;
    },
    /**
     * Extracts visible layers
     * @param plainLayers [array] array of flattened layers. 
     */
    extractVisibleLayers: function(plainLayers) {
        plainLayers = plainLayers.filter(function(layer) { 
        	return (layer.layer && layer.layer.visibility); 
        });
        return plainLayers;
    },
    /**
     * Extracts Vector-layers
     */
    extractVector: function(layers) {
        layers = this.extractLayers(layers);
        layers = layers.filter(function(layer){ return layer.vector; });
        return layers;
    },
    /**
     * Extracts base-layers
     */
    extractBaseLayers: function(layers) {
        layers = this.extractLayers(layers);
        layers = layers.filter(function(layer){ return layer.isBaseLayer; });
        return layers;
    },
    /**
     * Process layers config to return a flat array with popupLayer definitions
     */
    extractPopupLayers: function(layers) {
        layers = this.extractLayers(layers);
        layers = layers.filter(function(layer) {
        	if (layer.idAttribute && layer.popupTextAttribute) {
        		return true;
        	} else {
        		return false;
        	} 
    	});
    	layers = layers.filter(function(layer){return layer.visibility;});
        return layers;
    },
    getOptions: function(layer) {
        if (layer.wms) {
            return layer.wms.options;
        } else if (layer.osm) {
            return layer.osm.options;
        } else if (layer.google) {
            return layer.google.options;
        } else if (layer.bing) {
            return layer.bing.options;
        }
    },
    isOpenLayersLayer: function(layer) {
        if (layer.wms || layer.osm || layer.google || layer.bing) {
            return true;
        } else {
            return false;
        }
    },
    isBaseLayer: function(layer) {
        var options = this.getOptions(layer);
        if (options && options.isBaseLayer) {
            return true;
        } else {
            return false;
        }
    },
    createControl: function(control) {
        if (control.constructor == String) {
            return new OpenLayers.Control[control]();
        } else {
            return new OpenLayers.Control[control.type](control.options);
        }
    },
    isWMSLayer: function(layer) {
        return layer.wms ? true : false;
    },
    /**
     * Transform layer definition for back compatibility reasons
     */
    transformLayer: function(layer) {
        // if layer def has url assume it should be moved to wms property
        if (layer.url) {
            layer.wms = {
                    url: layer.url,
                    params: layer.params,
                    options: layer.options
            };
        }
        return layer;
    },
    createLayer: function(layer) {
        if (layer.wms) {
            return new OpenLayers.Layer.WMS(layer.name, layer.wms.url, layer.wms.params, layer.wms.options);
        } else if (layer.osm) {
            return new OpenLayers.Layer.OSM(layer.name, layer.osm.url, layer.osm.options);
        } else if (layer.google) {
            return new OpenLayers.Layer.Google(layer.name, layer.google.options);
        } else if (layer.bing) {
            return new OpenLayers.Layer.Bing(Ext.apply({name: layer.name}, layer.bing.options));
        } else {
            throw new Error("Unknown layer type");
        }
    },
	/**
	 *	Checks if the layer or any sublayer is checked
	 *	@private 
	 *  @param layer [Object] layer object in the config structure
	 */
	isLayerChecked: function(layer) {
		if (layer.isGroupLayer && layer.layers && layer.layers.length) {
			return layer.layers.some(this.isLayerChecked, this);
		} else {
			return layer.wms && layer.wms.options && layer.wms.options.visibility ? layer.wms.options.visibility : false;
		}
	},
	/**
	 *	Sets properties for layers based on config options and creates OpenLayers layer 
	 *	@private 
	 *  @param layer [Object] layer object in the config structure
	 */
    iterateLayers: function(layer) {
        // Set node text
        layer.text = layer.name;
        // Is node checked?
        layer.checked = this.isLayerChecked(layer);
         
        // Get url from Server and set to layer
        if(typeof layer.serverId !== 'undefined' && layer.serverId !== '') {
            var server = this.serverStore.getById(layer.serverId);
            if(server) {
                if(layer.wms && !layer.wms.url) {
                    var wmsService = '/wms';
                    if(layer.wms.gwc) {
                        wmsService = '/gwc/service/wms';
                    }
                    layer.wms.url = server.get('url') + wmsService;
                }

                if(layer.wfs && !layer.wfs.url) {
                    layer.wfs.url = server.get('url');
                }
            }
        }

        // Create and store a reference to OpenLayers layer for this node
        if(this.isOpenLayersLayer(layer)) {
            layer.layer = this.createLayer(layer);
            layer.layer.queryable = layer.queryable ? layer.queryable : false;
        }
        // Do the node have sublayers, iterate over them
        if(layer.layers && (Object.prototype.toString.call(layer.layers) === '[object Array]') && layer.layers.length > 0) {
            layer.isGroupLayer = true;
            layer.expanded = layer.expanded === undefined ? true : layer.expanded;
            layer.layers.forEach(arguments.callee, this);
        } else {
            // If no sublayers, this is a leaf
            layer.leaf = true;
        }
    }

});

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * Form field based on combobox to select zoom level
 */
Ext.define('OpenEMap.form.ZoomSelector' ,{
    extend:  Ext.form.ComboBox ,
               
                                
      
    emptyText: "Zoom Level",
    listConfig: {
        getInnerTpl: function() {
            return "1: {scale:round(0)}";
        }
    },
    width: 120,
    editable: false,
    triggerAction: 'all',
    queryMode: 'local',
    initComponent: function() {
        this.store = Ext.create("GeoExt.data.ScaleStore", {map: this.map});
        this.listeners = {
            select: {
                fn:function(combo, record, index) {
                    this.map.zoomTo(record[0].get("level"));
                },
                scope:this
            }
        };
        this.map.events.register('zoomend', this, function() {
            var scale = this.store.queryBy(function(record){
                return this.map.getZoom() == record.data.level;
            });

            if (scale.length > 0) {
                scale = scale.items[0];
                this.setValue("1 : " + parseInt(scale.data.scale));
            } else {
                if (!zoomSelector.rendered) return;
                this.clearValue();
            }
        });
        this.callParent(arguments);
    }
});

        

/* Copyright (c) 2006-2013 by OpenLayers Contributors (see authors.txt for
 * full list of contributors). Published under the 2-clause BSD license.
 * See license.txt in the OpenLayers distribution or repository for the
 * full text of the license. */

/**
 * @class OpenEMap.OpenLayers.Control.ModifyFeature
 * @requires OpenLayers/Control.js
 * @requires OpenLayers/Handler/Drag.js
 * @requires OpenLayers/Handler/Keyboard.js
 */

// NOTE: Overriding OpenLayers.Control.ModifyFeature with code from https://github.com/openlayers/openlayers/pull/1145.
// This override should be removed when possible.

// need this empty Ext JS class definition to make Ext JS class loader happy
Ext.define('OpenEMap.OpenLayers.Control.ModifyFeature', { });

/**
 * Class: OpenLayers.Control.ModifyFeature
 * Control to modify features.  When activated, a click renders the vertices
 *     of a feature - these vertices can then be dragged.  By default, the
 *     delete key will delete the vertex under the mouse.  New features are
 *     added by dragging "virtual vertices" between vertices.  Create a new
 *     control with the <OpenLayers.Control.ModifyFeature> constructor.
 *
 * Inherits From:
 *  - <OpenLayers.Control>
 */
OpenLayers.Control.ModifyFeature = OpenLayers.Class(OpenLayers.Control, {

    /**
     * APIProperty: bySegment
     * {Boolean} If set to true, one segment at a time will be editable (the
     *     one under the mouse cursor on hover). This supports editing much
     *     larger geometries. This requires the rbush library
     *     (https://github.com/mourner/rbush) for spatial indexing. Default is
     *     false.
     */
    bySegment: false,

    /**
     * APIProperty: documentDrag
     * {Boolean} If set to true, dragging vertices will continue even if the
     *     mouse cursor leaves the map viewport. Default is false.
     */
    documentDrag: false,

    /**
     * APIProperty: geometryTypes
     * {Array(String)} To restrict modification to a limited set of geometry
     *     types, send a list of strings corresponding to the geometry class
     *     names.
     */
    geometryTypes: null,

    /**
     * APIProperty: clickout
     * {Boolean} Unselect features when clicking outside any feature.
     *     Default is true.
     */
    clickout: true,

    /**
     * APIProperty: toggle
     * {Boolean} Unselect a selected feature on click.
     *      Default is true.
     */
    toggle: true,

    /**
     * APIProperty: standalone
     * {Boolean} Set to true to create a control without SelectFeature
     *     capabilities. Default is false.  If standalone is true, to modify
     *     a feature, call the <selectFeature> method with the target feature.
     *     Note that you must call the <unselectFeature> method to finish
     *     feature modification in standalone mode (before starting to modify
     *     another feature).
     */
    standalone: false,

    /**
     * Property: layer
     * {<OpenLayers.Layer.Vector>}
     */
    layer: null,

    /**
     * Property: feature
     * {<OpenLayers.Feature.Vector>} Feature currently available for modification.
     */
    feature: null,

    /**
     * Property: vertex
     * {<OpenLayers.Feature.Vector>} Vertex currently being modified.
     */
    vertex: null,

    /**
     * Property: vertices
     * {Array(<OpenLayers.Feature.Vector>)} Verticies currently available
     *     for dragging.
     */
    vertices: null,

    /**
     * Property: virtualVertices
     * {Array(<OpenLayers.Feature.Vector>)} Virtual vertices in the middle
     *     of each edge.
     */
    virtualVertices: null,

    /**
     * Property: handlers
     * {Object}
     */
    handlers: null,

    /**
     * APIProperty: deleteCodes
     * {Array(Integer)} Keycodes for deleting verticies.  Set to null to disable
     *     vertex deltion by keypress.  If non-null, keypresses with codes
     *     in this array will delete vertices under the mouse. Default
     *     is 46 and 68, the 'delete' and lowercase 'd' keys.
     */
    deleteCodes: null,

    /**
     * APIProperty: virtualStyle
     * {Object} A symbolizer to be used for virtual vertices.
     */
    virtualStyle: null,
    
    /**
     * APIProperty: dragHandleStyle
     * {Object} A symbolizer to be used for drag handles.
     */
    dragHandleStyle: null,
    
    /**
     * APIProperty: dragHandleStyle
     * {Object} A symbolizer to be used for radius handles.
     */
    radiusHandleStyle: null,

    /**
     * APIProperty: vertexRenderIntent
     * {String} The renderIntent to use for vertices. If no <virtualStyle> is
     * provided, this renderIntent will also be used for virtual vertices, with
     * a fillOpacity and strokeOpacity of 0.3. Default is null, which means
     * that the layer's default style will be used for vertices.
     */
    vertexRenderIntent: null,

    /**
     * APIProperty: mode
     * {Integer} Bitfields specifying the modification mode. Defaults to
     *      OpenLayers.Control.ModifyFeature.RESHAPE. To set the mode to a
     *      combination of options, use the | operator. For example, to allow
     *      the control to both resize and rotate features, use the following
     *      syntax
     * (code)
     * control.mode = OpenLayers.Control.ModifyFeature.RESIZE |
     *                OpenLayers.Control.ModifyFeature.ROTATE;
     *  (end)
     */
    mode: null,

    /**
     * APIProperty: createVertices
     * {Boolean} Create new vertices by dragging the virtual vertices
     *     in the middle of each edge. Default is true.
     */
    createVertices: true,

    /**
     * Property: modified
     * {Boolean} The currently selected feature has been modified.
     */
    modified: false,

    /**
     * Property: radiusHandle
     * {<OpenLayers.Feature.Vector>} A handle for rotating/resizing a feature.
     */
    radiusHandle: null,

    /**
     * Property: dragHandle
     * {<OpenLayers.Feature.Vector>} A handle for dragging a feature.
     */
    dragHandle: null,

    /**
     * APIProperty: onModificationStart 
     * {Function} *Deprecated*.  Register for "beforefeaturemodified" instead.
     *     The "beforefeaturemodified" event is triggered on the layer before
     *     any modification begins.
     *
     * Optional function to be called when a feature is selected
     *     to be modified. The function should expect to be called with a
     *     feature.  This could be used for example to allow to lock the
     *     feature on server-side.
     */
    onModificationStart: function() {},

    /**
     * APIProperty: onModification
     * {Function} *Deprecated*.  Register for "featuremodified" instead.
     *     The "featuremodified" event is triggered on the layer with each
     *     feature modification.
     *
     * Optional function to be called when a feature has been
     *     modified.  The function should expect to be called with a feature.
     */
    onModification: function() {},

    /**
     * APIProperty: onModificationEnd
     * {Function} *Deprecated*.  Register for "afterfeaturemodified" instead.
     *     The "afterfeaturemodified" event is triggered on the layer after
     *     a feature has been modified.
     *
     * Optional function to be called when a feature is finished 
     *     being modified.  The function should expect to be called with a
     *     feature.
     */
    onModificationEnd: function() {},

    /**
     * Constructor: OpenLayers.Control.ModifyFeature
     * Create a new modify feature control.
     *
     * Parameters:
     * layer - {<OpenLayers.Layer.Vector>} Layer that contains features that
     *     will be modified.
     * options - {Object} Optional object whose properties will be set on the
     *     control.
     */
    initialize: function(layer, options) {
        options = options || {};
        this.layer = layer;
        this.vertices = [];
        this.virtualVertices = [];
        this.virtualStyle = OpenLayers.Util.extend({},
            this.layer.style ||
            this.layer.styleMap.createSymbolizer(null, options.vertexRenderIntent)
        );
        this.virtualStyle.fillOpacity = 0.3;
        this.virtualStyle.strokeOpacity = 0.3;
        this.deleteCodes = [46, 68];
        this.mode = OpenLayers.Control.ModifyFeature.RESHAPE;
        OpenLayers.Control.prototype.initialize.apply(this, [options]);
        if(!(OpenLayers.Util.isArray(this.deleteCodes))) {
            this.deleteCodes = [this.deleteCodes];
        }
        
        // configure the drag handler
        var dragCallbacks = {
            down: function(pixel) {
                this.vertex = null;
                var feature = this.layer.getFeatureFromEvent(
                        this.handlers.drag.evt);
                if (feature) {
                    this.dragStart(feature);
                } else if (this.clickout) {
                    this._unselect = this.feature;
                }
            },
            move: function(pixel) {
                delete this._unselect;
                if (this.vertex) {
                    this.dragVertex(this.vertex, pixel);
                }
            },
            up: function() {
                this.handlers.drag.stopDown = false;
                if (this._unselect) {
                    this.unselectFeature(this._unselect);
                    delete this._unselect;
                }
            },
            done: function(pixel) {
                if (this.vertex) {
                    this.dragComplete(this.vertex);
                }
            }
        };
        var _self = this;
        var dragOptions = {
            documentDrag: this.documentDrag,
            setEvent: function(evt) {
                var feature = _self.feature;
                _self._lastVertex = feature ?
                                  feature.layer.getFeatureFromEvent(evt) : null;
                OpenLayers.Handler.Drag.prototype.setEvent.apply(
                                                               this, arguments);
            },
            stopDown: false
        };

        // configure the keyboard handler
        var keyboardOptions = {
            keydown: this.handleKeypress
        };
        this.handlers = {
            keyboard: new OpenLayers.Handler.Keyboard(this, keyboardOptions),
            drag: new OpenLayers.Handler.Drag(this, dragCallbacks, dragOptions)
        };

        if (this.bySegment) {
            if (!window.rbush) {
                throw new Error("The rbush library is required");
            }
            if (!OpenLayers.Control.ModifyFeature.BySegment) {
                throw new Error("OpenLayers.Control.ModifyFeature.BySegment is missing from the build");
            } else {
                OpenLayers.Util.extend(this, OpenLayers.Control.ModifyFeature.BySegment);
            }
        }
    },

    /**
     * Method: createVirtualVertex
     * Create a virtual vertex in the middle of the segment.
     *
     * Parameters:
     * point1 - {<OpenLayers.Geometry.Point>} First point of the segment.
     * point2 - {<OpenLayers.Geometry.Point>} Second point of the segment.
     *
     * Returns:
     * {<OpenLayers.Feature.Vector>} The virtual vertex created.
     */
    createVirtualVertex: function(point1, point2) {
        var x = (point1.x + point2.x) / 2;
        var y = (point1.y + point2.y) / 2;
        var point = new OpenLayers.Feature.Vector(
            new OpenLayers.Geometry.Point(x, y),
            null, this.virtualStyle
        );
        point._sketch = true;
        return point;
    },

    /**
     * APIMethod: destroy
     * Take care of things that are not handled in superclass.
     */
    destroy: function() {
        if (this.map) {
            this.map.events.un({
                "removelayer": this.handleMapEvents,
                "changelayer": this.handleMapEvents,
                scope: this
            });
        }
        this.layer = null;
        OpenLayers.Control.prototype.destroy.apply(this, []);
    },

    /**
     * APIMethod: activate
     * Activate the control.
     * 
     * Returns:
     * {Boolean} Successfully activated the control.
     */
    activate: function() {
        if (OpenLayers.Control.prototype.activate.apply(this, arguments)) {
            this.moveLayerToTop();
            this.map.events.on({
                "removelayer": this.handleMapEvents,
                "changelayer": this.handleMapEvents,
                scope: this
            });
            this._lastVertex = null;
            return this.handlers.keyboard.activate() &&
                    this.handlers.drag.activate();
        }
        return false;
    },

    /**
     * APIMethod: deactivate
     * Deactivate the control.
     *
     * Returns: 
     * {Boolean} Successfully deactivated the control.
     */
    deactivate: function() {
        var deactivated = false;
        // the return from the controls is unimportant in this case
        if(OpenLayers.Control.prototype.deactivate.apply(this, arguments)) {
            this.moveLayerBack();
            this.map.events.un({
                "removelayer": this.handleMapEvents,
                "changelayer": this.handleMapEvents,
                scope: this
            });
            this.layer.removeFeatures(this.vertices, {silent: true});
            this.layer.removeFeatures(this.virtualVertices, {silent: true});
            this.vertices = [];
            this.handlers.drag.deactivate();
            this.handlers.keyboard.deactivate();
            var feature = this.feature;
            if (feature && feature.geometry && feature.layer) {
                this.unselectFeature(feature);
            }
            deactivated = true;
        }
        return deactivated;
    },

    /**
     * Method: beforeSelectFeature
     * Called before a feature is selected.
     *
     * Parameters:
     * feature - {<OpenLayers.Feature.Vector>} The feature about to be selected.
     */
    beforeSelectFeature: function(feature) {
        return this.layer.events.triggerEvent(
            "beforefeaturemodified", {feature: feature}
        );
    },

    /**
     * APIMethod: selectFeature
     * Select a feature for modification in standalone mode. In non-standalone
     * mode, this method is called when a feature is selected by clicking.
     * Register a listener to the beforefeaturemodified event and return false
     * to prevent feature modification.
     *
     * Parameters:
     * feature - {<OpenLayers.Feature.Vector>} the selected feature.
     */
    selectFeature: function(feature) {
        if (this.feature === feature ||
           (this.geometryTypes && OpenLayers.Util.indexOf(this.geometryTypes,
           feature.geometry.CLASS_NAME) == -1)) {
            return;
        }
        if (this.beforeSelectFeature(feature) !== false) {
            if (this.feature) {
                this.unselectFeature(this.feature);
            }
            this.feature = feature;
            this.layer.selectedFeatures.push(feature);
            this.layer.drawFeature(feature, 'select');
            this.modified = false;
            this.resetVertices();
            this.onModificationStart(this.feature);
        }
        // keep track of geometry modifications
        var modified = feature.modified;
        if (feature.geometry && !(modified && modified.geometry)) {
            this._originalGeometry = feature.geometry.clone();
        }
    },

    /**
     * APIMethod: unselectFeature
     * Called when the select feature control unselects a feature.
     *
     * Parameters:
     * feature - {<OpenLayers.Feature.Vector>} The unselected feature.
     */
    unselectFeature: function(feature) {
        this.layer.removeFeatures(this.vertices, {silent: true});
        this.vertices = [];
        this.layer.destroyFeatures(this.virtualVertices, {silent: true});
        this.virtualVertices = [];
        if(this.dragHandle) {
            this.layer.destroyFeatures([this.dragHandle], {silent: true});
            delete this.dragHandle;
        }
        if(this.radiusHandle) {
            this.layer.destroyFeatures([this.radiusHandle], {silent: true});
            delete this.radiusHandle;
        }
        this.layer.drawFeature(this.feature, 'default');
        this.feature = null;
        OpenLayers.Util.removeItem(this.layer.selectedFeatures, feature);
        this.onModificationEnd(feature);
        this.layer.events.triggerEvent("afterfeaturemodified", {
            feature: feature,
            modified: this.modified
        });
        this.modified = false;
    },
    
    
    /**
     * Method: dragStart
     * Called by the drag handler before a feature is dragged.  This method is
     *     used to differentiate between points and vertices
     *     of higher order geometries.
     *
     * Parameters:
     * feature - {<OpenLayers.Feature.Vector>} The point or vertex about to be
     *     dragged.
     */
    dragStart: function(feature) {
        var isPoint = feature.geometry.CLASS_NAME ==
                'OpenLayers.Geometry.Point';
        if (!this.standalone &&
                ((!feature._sketch && isPoint) || !feature._sketch)) {
            if (this.toggle && this.feature === feature) {
                // mark feature for unselection
                this._unselect = feature;
            }
            this.selectFeature(feature);
        }
        if (this.feature &&
                (feature._sketch || isPoint && feature === this.feature)) {
            // feature is a drag or virtual handle or point
            this.vertex = feature;
            this.handlers.drag.stopDown = true;
        }
    },

    /**
     * Method: dragVertex
     * Called by the drag handler with each drag move of a vertex.
     *
     * Parameters:
     * vertex - {<OpenLayers.Feature.Vector>} The vertex being dragged.
     * pixel - {<OpenLayers.Pixel>} Pixel location of the mouse event.
     */
    dragVertex: function(vertex, pixel) {
        var pos = this.map.getLonLatFromViewPortPx(pixel);
        var geom = vertex.geometry;
        geom.move(pos.lon - geom.x, pos.lat - geom.y);
        this.modified = true;
        /*
         * Five cases:
         * 1) dragging a simple point
         * 2) dragging a virtual vertex
         * 3) dragging a drag handle
         * 4) dragging a real vertex
         * 5) dragging a radius handle
         */
        if(this.feature.geometry.CLASS_NAME == "OpenLayers.Geometry.Point") {
            // dragging a simple point
            this.layer.events.triggerEvent("vertexmodified", {
                vertex: vertex.geometry,
                feature: this.feature,
                pixel: pixel
            });
        } else {
            if(vertex._index) {
                if (vertex._index == -1) {
                    vertex._index = OpenLayers.Util.indexOf(vertex.geometry.parent.components, vertex._next);
                }
                // dragging a virtual vertex
                vertex.geometry.parent.addComponent(vertex.geometry,
                                                    vertex._index);
                // move from virtual to real vertex
                delete vertex._index;
                OpenLayers.Util.removeItem(this.virtualVertices, vertex);
                this.vertices.push(vertex);
            } else if(vertex == this.dragHandle) {
                // dragging a drag handle
                this.layer.removeFeatures(this.vertices, {silent: true});
                this.vertices = [];
                if(this.radiusHandle) {
                    this.layer.destroyFeatures([this.radiusHandle], {silent: true});
                    this.radiusHandle = null;
                }
            } else if(vertex !== this.radiusHandle) {
                // dragging a real vertex
                this.layer.events.triggerEvent("vertexmodified", {
                    vertex: vertex.geometry,
                    feature: this.feature,
                    pixel: pixel
                });
            }
            // dragging a radius handle - no special treatment
            if(this.virtualVertices.length > 0) {
                this.layer.destroyFeatures(this.virtualVertices, {silent: true});
                this.virtualVertices = [];
            }
            this.layer.drawFeature(this.feature, this.standalone ? undefined :
                                            'select');
        }
        // keep the vertex on top so it gets the mouseout after dragging
        // this should be removed in favor of an option to draw under or
        // maintain node z-index
        this.layer.drawFeature(vertex);
    },

    /**
     * Method: dragComplete
     * Called by the drag handler when the feature dragging is complete.
     *
     * Parameters:
     * vertex - {<OpenLayers.Feature.Vector>} The vertex being dragged.
     */
    dragComplete: function(vertex) {
        this.resetVertices();
        this.setFeatureState();
        this.onModification(this.feature);
        this.layer.events.triggerEvent("featuremodified", {feature: this.feature});
    },
    
    /**
     * Method: setFeatureState
     * Called when the feature is modified.  If the current state is not
     *     INSERT or DELETE, the state is set to UPDATE.
     */
    setFeatureState: function() {
        if(this.feature.state != OpenLayers.State.INSERT &&
           this.feature.state != OpenLayers.State.DELETE) {
            this.feature.state = OpenLayers.State.UPDATE;
            if (this.modified && this._originalGeometry) {
                var feature = this.feature;
                feature.modified = OpenLayers.Util.extend(feature.modified, {
                    geometry: this._originalGeometry
                });
                delete this._originalGeometry;
            }
        }
    },
    
    /**
     * Method: resetVertices
     */
    resetVertices: function() {
        if(this.vertices.length > 0) {
            this.layer.removeFeatures(this.vertices, {silent: true});
            this.vertices = [];
        }
        if(this.virtualVertices.length > 0) {
            this.layer.removeFeatures(this.virtualVertices, {silent: true});
            this.virtualVertices = [];
        }
        if(this.dragHandle) {
            this.layer.destroyFeatures([this.dragHandle], {silent: true});
            this.dragHandle = null;
        }
        if(this.radiusHandle) {
            this.layer.destroyFeatures([this.radiusHandle], {silent: true});
            this.radiusHandle = null;
        }
        if(this.feature &&
           this.feature.geometry.CLASS_NAME != "OpenLayers.Geometry.Point") {
            if((this.mode & OpenLayers.Control.ModifyFeature.DRAG)) {
                this.collectDragHandle();
            }
            if((this.mode & (OpenLayers.Control.ModifyFeature.ROTATE |
                             OpenLayers.Control.ModifyFeature.RESIZE))) {
                this.collectRadiusHandle();
            }
            if(this.mode & OpenLayers.Control.ModifyFeature.RESHAPE){
                // Don't collect vertices when we're resizing
                if (!(this.mode & OpenLayers.Control.ModifyFeature.RESIZE)){
                    this.collectVertices();
                }
            }
        }
    },
    
    /**
     * Method: handleKeypress
     * Called by the feature handler on keypress.  This is used to delete
     *     vertices. If the <deleteCode> property is set, vertices will
     *     be deleted when a feature is selected for modification and
     *     the mouse is over a vertex.
     *
     * Parameters:
     * evt - {Event} Keypress event.
     */
    handleKeypress: function(evt) {
        var code = evt.keyCode;
        
        // check for delete key
        if(this.feature &&
           OpenLayers.Util.indexOf(this.deleteCodes, code) != -1) {
            var vertex = this._lastVertex;
            if (vertex &&
                    OpenLayers.Util.indexOf(this.vertices, vertex) != -1 &&
                    !this.handlers.drag.dragging && vertex.geometry.parent) {
                // remove the vertex
                vertex.geometry.parent.removeComponent(vertex.geometry);
                this.layer.events.triggerEvent("vertexremoved", {
                    vertex: vertex.geometry,
                    feature: this.feature,
                    pixel: evt.xy
                });
                this.layer.drawFeature(this.feature, this.standalone ?
                                       undefined : 'select');
                this.modified = true;
                this.resetVertices();
                this.setFeatureState();
                this.onModification(this.feature);
                this.layer.events.triggerEvent("featuremodified",
                                               {feature: this.feature});
            }
        }
    },

    /**
     * Method: collectVertices
     * Collect the vertices from the modifiable feature's geometry and push
     *     them on to the control's vertices array.
     */
    collectVertices: function() {
        this.vertices = [];
        this.virtualVertices = [];
        var control = this;
        function collectComponentVertices(geometry) {
            var i, vertex, component, len;
            if(geometry.CLASS_NAME == "OpenLayers.Geometry.Point") {
                vertex = new OpenLayers.Feature.Vector(geometry);
                vertex._sketch = true;
                vertex.renderIntent = control.vertexRenderIntent;
                control.vertices.push(vertex);
            } else {
                var numVert = geometry.components.length;
                if(geometry.CLASS_NAME == "OpenLayers.Geometry.LinearRing") {
                    numVert -= 1;
                }
                for(i=0; i<numVert; ++i) {
                    component = geometry.components[i];
                    if(component.CLASS_NAME == "OpenLayers.Geometry.Point") {
                        vertex = new OpenLayers.Feature.Vector(component);
                        vertex._sketch = true;
                        vertex.renderIntent = control.vertexRenderIntent;
                        control.vertices.push(vertex);
                    } else {
                        collectComponentVertices(component);
                    }
                }
                
                // add virtual vertices in the middle of each edge
                if (control.createVertices && geometry.CLASS_NAME != "OpenLayers.Geometry.MultiPoint") {
                    for(i=0, len=geometry.components.length; i<len-1; ++i) {
                        var prevVertex = geometry.components[i];
                        var nextVertex = geometry.components[i + 1];
                        if(prevVertex.CLASS_NAME == "OpenLayers.Geometry.Point" &&
                           nextVertex.CLASS_NAME == "OpenLayers.Geometry.Point") {
                            var point = control.createVirtualVertex.call(control, prevVertex, nextVertex);
                            // set the virtual parent and intended index
                            point.geometry.parent = geometry;
                            point._index = i + 1;
                            control.virtualVertices.push(point);
                        }
                    }
                }
            }
        }
        collectComponentVertices.call(this, this.feature.geometry);
        this.layer.addFeatures(this.virtualVertices, {silent: true});
        this.layer.addFeatures(this.vertices, {silent: true});
    },

    /**
     * Method: collectDragHandle
     * Collect the drag handle for the selected geometry.
     */
    collectDragHandle: function() {
        var geometry = this.feature.geometry;
        var center = geometry.getBounds().getCenterLonLat();
        var originGeometry = new OpenLayers.Geometry.Point(
            center.lon, center.lat
        );
        var origin = new OpenLayers.Feature.Vector(originGeometry, null, this.dragHandleStyle);
        originGeometry.move = function(x, y) {
            OpenLayers.Geometry.Point.prototype.move.call(this, x, y);
            geometry.move(x, y);
        };
        origin._sketch = true;
        this.dragHandle = origin;
        this.dragHandle.renderIntent = this.vertexRenderIntent;
        this.layer.addFeatures([this.dragHandle], {silent: true});
    },

    /**
     * Method: collectRadiusHandle
     * Collect the radius handle for the selected geometry.
     */
    collectRadiusHandle: function() {
        var geometry = this.feature.geometry;
        var bounds = geometry.getBounds();
        var center = bounds.getCenterLonLat();
        var originGeometry = new OpenLayers.Geometry.Point(
            center.lon, center.lat
        );
        var radiusGeometry = new OpenLayers.Geometry.Point(
            bounds.right, bounds.bottom
        );
        var radius = new OpenLayers.Feature.Vector(radiusGeometry, null, this.radiusHandleStyle);
        var resize = (this.mode & OpenLayers.Control.ModifyFeature.RESIZE);
        var reshape = (this.mode & OpenLayers.Control.ModifyFeature.RESHAPE);
        var rotate = (this.mode & OpenLayers.Control.ModifyFeature.ROTATE);

        radiusGeometry.move = function(x, y) {
            OpenLayers.Geometry.Point.prototype.move.call(this, x, y);
            var dx1 = this.x - originGeometry.x;
            var dy1 = this.y - originGeometry.y;
            var dx0 = dx1 - x;
            var dy0 = dy1 - y;
            if(rotate) {
                var a0 = Math.atan2(dy0, dx0);
                var a1 = Math.atan2(dy1, dx1);
                var angle = a1 - a0;
                angle *= 180 / Math.PI;
                geometry.rotate(angle, originGeometry);
            }
            if(resize) {
                var scale, ratio;
                // 'resize' together with 'reshape' implies that the aspect 
                // ratio of the geometry will not be preserved whilst resizing 
                if (reshape) {
                    scale = dy1 / dy0;
                    ratio = (dx1 / dx0) / scale;
                } else {
                    var l0 = Math.sqrt((dx0 * dx0) + (dy0 * dy0));
                    var l1 = Math.sqrt((dx1 * dx1) + (dy1 * dy1));
                    scale = l1 / l0;
                }
                geometry.resize(scale, originGeometry, ratio);
            }
        };
        radius._sketch = true;
        this.radiusHandle = radius;
        this.radiusHandle.renderIntent = this.vertexRenderIntent;
        this.layer.addFeatures([this.radiusHandle], {silent: true});
    },

    /**
     * Method: setMap
     * Set the map property for the control and all handlers.
     *
     * Parameters:
     * map - {<OpenLayers.Map>} The control's map.
     */
    setMap: function(map) {
        this.handlers.drag.setMap(map);
        OpenLayers.Control.prototype.setMap.apply(this, arguments);
    },

    /**
     * Method: handleMapEvents
     * 
     * Parameters:
     * evt - {Object}
     */
    handleMapEvents: function(evt) {
        if (evt.type == "removelayer" || evt.property == "order") {
            this.moveLayerToTop();
        }
    },

    /**
     * Method: moveLayerToTop
     * Moves the layer for this handler to the top, so mouse events can reach
     * it.
     */
    moveLayerToTop: function() {
        var index = Math.max(this.map.Z_INDEX_BASE['Feature'] - 1,
            this.layer.getZIndex()) + 1;
        this.layer.setZIndex(index);
        
    },

    /**
     * Method: moveLayerBack
     * Moves the layer back to the position determined by the map's layers
     * array.
     */
    moveLayerBack: function() {
        var index = this.layer.getZIndex() - 1;
        if (index >= this.map.Z_INDEX_BASE['Feature']) {
            this.layer.setZIndex(index);
        } else {
            this.map.setLayerZIndex(this.layer,
                this.map.getLayerIndex(this.layer));
        }
    },

    CLASS_NAME: "OpenLayers.Control.ModifyFeature"
});

/**
 * Constant: RESHAPE
 * {Integer} Constant used to make the control work in reshape mode
 */
OpenLayers.Control.ModifyFeature.RESHAPE = 1;
/**
 * Constant: RESIZE
 * {Integer} Constant used to make the control work in resize mode
 */
OpenLayers.Control.ModifyFeature.RESIZE = 2;
/**
 * Constant: ROTATE
 * {Integer} Constant used to make the control work in rotate mode
 */
OpenLayers.Control.ModifyFeature.ROTATE = 4;
/**
 * Constant: DRAG
 * {Integer} Constant used to make the control work in drag mode
 */
OpenLayers.Control.ModifyFeature.DRAG = 8;
/* Copyright 2011-2014 Xavier Mamano, http://github.com/jorix/OL-DynamicMeasure
 * Published under MIT license. */

/**
 * @class OpenEMap.OpenLayers.Control.DynamicMeasure
 *
 * @requires OpenLayers/Control/Measure.js
 * @requires OpenLayers/Rule.js
 * @requires OpenLayers/StyleMap.js
 */

// need this empty Ext JS class definition to make Ext JS class loader happy
Ext.define('OpenEMap.OpenLayers.Control.DynamicMeasure', { });

/**
 * Class: OpenLayers.Control.DynamicMeasure
 * Allows for drawing of features for measurements.
 *
 * Inherits from:
 *  - <OpenLayers.Control.Measure>
 */
OpenLayers.Control.DynamicMeasure = OpenLayers.Class(
		OpenLayers.Control.Measure, {

    /**
     * APIProperty: accuracy
     * {Integer} Digits measurement accuracy, default is 2.
     */
    accuracy: 2,

    /**
     * APIProperty: persist
     * {Boolean} Keep the temporary measurement after the
     *     measurement is complete.  The measurement will persist until a new
     *     measurement is started, the control is deactivated, or <cancel> is
     *     called. Default is true.
     */
    persist: false,

    /**
     * APIProperty: styles
     * {Object} Alterations of the default styles of the points lines poligons
     *     and labels text, could use keys: "Point", "Line",
     *     "Polygon", "labelSegments", "labelHeading", "labelLength" and
     *     "labelArea". Default is <OpenLayers.Control.DynamicMeasure.styles>.
     */
    styles: null,

    /**
     * APIProperty: positions
     * {Object} Alterations of the default position of the labels, could use
     *     keys: "labelSegments" & "labelHeading", with values "start" "middle"
     *     and "end" refered of the current segment; and keys: "labelLength" &
     *     "labelArea" with additional values "center" (of the feature) and
     *     "initial" (initial point of the feature) and also mentioned previus
     *     values. Default is
     *     <OpenLayers.Control.DynamicMeasure.positions>.
     */
    positions: null,

    /**
     * APIProperty: maxSegments
     * {Integer|Null} Maximum number of visible segments measures, default is 1.
     *
     * To avoid soiling the track is desirable to reduce the number of visible
     *     segments.
     */
    maxSegments: 1,

    /**
     * APIProperty: maxHeadings
     * {Integer|Null} Maximum number of visible headings measures, default is 1.
     *
     * To avoid soiling the track is desirable to reduce the number of visible
     *     segments.
     */
    maxHeadings: 1,

    /**
     * APIProperty: layerSegmentsOptions
     * {Object} Any optional properties to be set on the
     *     layer of <layerSegments> of the lengths of the segments. If set to
     *     null the layer does not act.
     *
     *     If `styleMap` options is set then the key "labelSegments" of the
     *     `styles` option is ignored.
     */
    layerSegmentsOptions: undefined,

    /**
     * APIProperty: layerHeadingOptions
     * {Object} Any optional properties to be set on the
     *     layer of <layerHeading> of the angle of the segments. If set to
     *     null the layer does not act.  Default is null, set to {} to use a
     *     <layerHeading> to show headings.
     *
     *     If `styleMap` options is set then the key "labelHeading" of the
     *     `styles` option is ignored.
     */
    layerHeadingOptions: null,

    /**
     * APIProperty: layerLengthOptions
     * {Object} Any optional properties to be set on the
     *     layer of <layerLength> of the total length. If set to null the layer
     *     does not act.
     *
     *     If `styleMap` option is set then the key "labelLength" of the
     *     `styles` option is ignored.
     */
    layerLengthOptions: undefined,

    /**
     * APIProperty: layerAreaOptions
     * {Object} Any optional properties to be set on the
     *     layer of <layerArea> of the total area. If set to null the layer does
     *     not act.
     *
     *     If `styleMap` is set then the key "labelArea" of the `styles` option
     *     is ignored.
     */
    layerAreaOptions: undefined,

    /**
     * APIProperty: drawingLayer
     * {<OpenLayers.Layer.Vector>} Drawing layer to store the drawing when
     *     finished.
     */
    drawingLayer: null,

    /**
     * APIProperty: multi
     * {Boolean} Cast features to multi-part geometries before passing to the
     *     drawing layer, only used if declared a <drawingLayer>.
     * Default is false.
     */
    multi: false,

    /**
     * Property: layerSegments
     * {<OpenLayers.Layer.Vector>} The temporary drawing layer to show the
     *     length of the segments.
     */
    layerSegments: null,

    /**
     * Property: layerLength
     * {<OpenLayers.Layer.Vector>} The temporary drawing layer to show total
     *     length.
     */
    layerLength: null,

    /**
     * Property: layerArea
     * {<OpenLayers.Layer.Vector>} The temporary drawing layer to show total
     *     area.
     */
    layerArea: null,

    /**
     * Property: dynamicObj
     * {Object} Internal use.
     */
    dynamicObj: null,

    /**
     * Property: isArea
     * {Boolean} Internal use.
     */
    isArea: null,

    /**
     * Constructor: OpenLayers.Control.Measure
     *
     * Parameters:
     * handler - {<OpenLayers.Handler>}
     * options - {Object}
     *
     * Valid options:
     * accuracy - {Integer} Digits measurement accuracy, default is 5.
     * styles - {Object} Alterations of the default styles of the points lines
     *     poligons and labels text, could use keys: "Point",
     *     "Line", "Polygon", "labelSegments", "labelLength", "labelArea".
     * positions - {Object} Alterations of the default position of the labels.
     * handlerOptions - {Object} Used to set non-default properties on the
     *     control's handler. If `layerOptions["styleMap"]` is set then the
     *     keys: "Point", "Line" and "Polygon" of the `styles` option
     *     are ignored.
     * layerSegmentsOptions - {Object} Any optional properties to be set on the
     *     layer of <layerSegments> of the lengths of the segments. If
     *     `styleMap` is set then the key "labelSegments" of the `styles` option
     *     is ignored. If set to null the layer does not act.
     * layerLengthOptions - {Object} Any optional properties to be set on the
     *     layer of <layerLength> of the total length. If
     *     `styleMap` is set then the key "labelLength" of the `styles` option
     *     is ignored. If set to null the layer does not act.
     * layerAreaOptions - {Object} Any optional properties to be set on the
     *     layer of <layerArea> of the total area. If
     *     `styleMap` is set then the key "labelArea" of the `styles` option
     *     is ignored. If set to null the layer does not act.
     * layerHeadingOptions - {Object} Any optional properties to be set on the
     *     layer of <layerHeading> of the angle of the segments. If
     *     `styleMap` is set then the key "labelHeading" of the `styles` option
     *     is ignored. If set to null the layer does not act.
     * drawingLayer - {<OpenLayers.Layer.Vector>} Optional drawing layer to
     *     store the drawing when finished.
     * multi - {Boolean} Cast features to multi-part geometries before passing
     *     to the drawing layer
     */
    initialize: function(handler, options) {

        // Manage options
        options = options || {};

        // handlerOptions: persist & multi
        options.handlerOptions = OpenLayers.Util.extend(
            {persist: !options.drawingLayer}, options.handlerOptions
        );
        if (options.drawingLayer && !('multi' in options.handlerOptions)) {
            options.handlerOptions.multi = options.multi;
        }

        // * styles option
        if (options.drawingLayer) {
            var sketchStyle = options.drawingLayer.styleMap &&
                                 options.drawingLayer.styleMap.styles.temporary;
            if (sketchStyle) {
                options.handlerOptions
                                  .layerOptions = OpenLayers.Util.applyDefaults(
                    options.handlerOptions.layerOptions, {
                        styleMap: new OpenLayers.StyleMap({
                            'default': sketchStyle
                        })
                    }
                );
            }
        }
        var optionsStyles = options.styles || {};
        options.styles = optionsStyles;
        var defaultStyles = OpenLayers.Control.DynamicMeasure.styles;
        // * * styles for handler layer.
        if (!options.handlerOptions.layerOptions ||
            !options.handlerOptions.layerOptions.styleMap) {
            // use the style option for layerOptions of the handler.
            var pointStyle = OpenLayers.Util.applyDefaults(
            	optionsStyles.Point, defaultStyles.Point);
            var lineStyle = OpenLayers.Util.applyDefaults(
                optionsStyles.Line, defaultStyles.Line);
            var polygonStyle = OpenLayers.Util.applyDefaults(
                optionsStyles.Polygon, defaultStyles.Polygon);               
            var style = new OpenLayers.Style(null, {rules: [
                new OpenLayers.Rule({symbolizer: {
                    'Point': pointStyle,
                    'Line': lineStyle,
                    'Polygon': polygonStyle
                }})
            ]});
//            var defaultStyle = {};
//            defaultStyle = OpenLayers.Util.applyDefaults(pointStyle, defaultStyle);
//            defaultStyle = OpenLayers.Util.applyDefaults(lineStyle, defaultStyle);
//            defaultStyle = OpenLayers.Util.applyDefaults(polygonStyle, defaultStyle);
//            style.setDefaultStyle(defaultStyle);
            
            options.handlerOptions = options.handlerOptions || {};
            options.handlerOptions.layerOptions =
                                      options.handlerOptions.layerOptions || {};
            options.handlerOptions.layerOptions.styleMap =
                                    new OpenLayers.StyleMap({'default': style});
        }

        // * positions option
        options.positions = OpenLayers.Util.applyDefaults(
            options.positions,
            OpenLayers.Control.DynamicMeasure.positions
        );

        // force some handler options
        options.callbacks = options.callbacks || {};
        if (options.drawingLayer) {
            OpenLayers.Util.applyDefaults(options.callbacks, {
                create: function(vertex, feature) {
                    this.callbackCreate(vertex, feature);
                    this.drawingLayer.events.triggerEvent(
                        'sketchstarted', {vertex: vertex, feature: feature}
                    );
                },
                modify: function(vertex, feature) {
                    this.callbackModify(vertex, feature);
                    this.drawingLayer.events.triggerEvent(
                        'sketchmodified', {vertex: vertex, feature: feature}
                    );
                },
                done: function(geometry) {
                    this.callbackDone(geometry);
                    this.drawFeature(geometry);
                }
            });
        }
        OpenLayers.Util.applyDefaults(options.callbacks, {
            create: this.callbackCreate,
            point: this.callbackPoint,
            cancel: this.callbackCancel,
            done: this.callbackDone,
            modify: this.callbackModify,
            redo: this.callbackRedo,
            undo: this.callbackUndo
        });

        // do a trick with the handler to avoid blue background in freehand.
        var _self = this;
        var oldOnselectstart = document.onselectstart ?
                              document.onselectstart : OpenLayers.Function.True;
        var handlerTuned = OpenLayers.Class(handler, {
            down: function(evt) {
                document.onselectstart = OpenLayers.Function.False;
                return handler.prototype.down.apply(this, arguments);
            },
            up: function(evt) {
                document.onselectstart = oldOnselectstart;
                return handler.prototype.up.apply(this, arguments);
            },
            move: function(evt) {
                if (!this.mouseDown) {
                    document.onselectstart = oldOnselectstart;
                }
                return handler.prototype.move.apply(this, arguments);
            },
            mouseout: function(evt) {
                if (OpenLayers.Util.mouseLeft(evt, this.map.viewPortDiv)) {
                    if (this.mouseDown) {
                        document.onselectstart = oldOnselectstart;
                    }
                }
                return handler.prototype.mouseout.apply(this, arguments);
            },
            finalize: function() {
                document.onselectstart = oldOnselectstart;
                handler.prototype.finalize.apply(this, arguments);
            }
        }, {
            undo: function() {
                var undone = handler.prototype.undo.call(this);
                if (undone) {
                    this.callback('undo',
                                 [this.point.geometry, this.getSketch(), true]);
                }
                return undone;
            },
            redo: function() {
                var redone = handler.prototype.redo.call(this);
                if (redone) {
                    this.callback('redo',
                                 [this.point.geometry, this.getSketch(), true]);
                }
                return redone;
            }
        });
        // ... and call the constructor
        OpenLayers.Control.Measure.prototype.initialize.call(
                                                   this, handlerTuned, options);

        this.isArea = handler.prototype.polygon !== undefined; // duck typing
    },

    /**
     * APIMethod: destroy
     */
    destroy: function() {
        this.deactivate();
        OpenLayers.Control.Measure.prototype.destroy.apply(this, arguments);
    },

    /**
     * Method: draw
     * This control does not have HTML component, so this method should
     *     be empty.
     */
    draw: function() {},

    /**
     * APIMethod: activate
     */
    activate: function() {
        var response = OpenLayers.Control.Measure.prototype.activate.apply(
                                                               this, arguments);
        if (response) {
            // Create dynamicObj
            this.dynamicObj = {};
            // Create layers
            var _optionsStyles = this.styles || {},
                _defaultStyles = OpenLayers.Control.DynamicMeasure.styles,
                _self = this;
            var _create = function(styleName, initialOptions) {
                if (initialOptions === null) {
                    return null;
                }
                var options = OpenLayers.Util.extend({
                    displayInLayerSwitcher: false,
                    calculateInRange: OpenLayers.Function.True
                    // ?? ,wrapDateLine: this.citeCompliant
                }, initialOptions);
                if (!options.styleMap) {
                    var style = _optionsStyles[styleName];

                    options.styleMap = new OpenLayers.StyleMap({
                        'default': OpenLayers.Util.applyDefaults(style,
                           
                                                      _defaultStyles[styleName])
                    });
                }
                var layer = new OpenLayers.Layer.Vector(
                                   _self.CLASS_NAME + ' ' + styleName, options);
                _self.map.addLayer(layer);
                _self.map.setLayerIndex(layer, 99);
                return layer;
            };
            this.layerSegments =
                            _create('labelSegments', this.layerSegmentsOptions);
            this.layerHeading =
                            _create('labelHeading', this.layerHeadingOptions);
            this.layerLength = _create('labelLength', this.layerLengthOptions);
            if (this.isArea) {
                this.layerArea = _create('labelArea', this.layerAreaOptions);
            }
        }
        return response;
    },

    /**
     * APIMethod: deactivate
     */
    deactivate: function() {
        var response = OpenLayers.Control.Measure.prototype.deactivate.apply(
                                                               this, arguments);
        if (response) {
            this.layerSegments && this.layerSegments.destroy();
            this.layerLength && this.layerLength.destroy();
            this.layerHeading && this.layerHeading.destroy();
            this.layerArea && this.layerArea.destroy();
            this.dynamicObj = null;
            this.layerSegments = null;
            this.layerLength = null;
            this.layerHeading = null;
            this.layerArea = null;
        }
        return response;
    },

    /**
     * APIMethod: setImmediate
     * Sets the <immediate> property. Changes the activity of immediate
     * measurement.
     */
    setImmediate: function(immediate) {
        this.immediate = immediate;
    },

    /**
     * Method: callbackCreate
     */
    callbackCreate: function() {
        var dynamicObj = this.dynamicObj;
        dynamicObj.drawing = false;
        dynamicObj.freehand = false;
        dynamicObj.fromIndex = 0;
        dynamicObj.countSegments = 0;
    },

    /**
     * Method: callbackCancel
     */
    callbackCancel: function() {
        this.destroyLabels();
    },

    /**
     * Method: callbackDone
     * Called when the measurement sketch is done.
     *
     * Parameters:
     * geometry - {<OpenLayers.Geometry>}
     */
    callbackDone: function(geometry) {
        var feature = new OpenLayers.Feature.Vector(geometry);
        this.mapPanel.measureLayer.addFeatures([feature.clone()]);
        var clone = function(e) { return e.clone(); };
        if (this.layerArea) {
            this.mapPanel.measureLayerArea.addFeatures(this.layerArea.features.map(clone));
        }
        this.mapPanel.measureLayerLength.addFeatures(this.layerLength.features.map(clone));
        this.mapPanel.measureLayerSegments.addFeatures(this.layerSegments.features.map(clone));

        this.measureComplete(geometry);
        if (!this.persist) {
            this.destroyLabels();
        }
    },

    /**
     * Method: drawFeature
     */
    drawFeature: function(geometry) {
        var feature = new OpenLayers.Feature.Vector(geometry);
        var proceed = this.drawingLayer.events.triggerEvent(
            'sketchcomplete', {feature: feature}
        );
        if (proceed !== false) {
            feature.state = OpenLayers.State.INSERT;
            //this.drawingLayer.addFeatures(this.layerArea.features.map(function(feature) { return feature.clone(); }));
            this.featureAdded && this.featureAdded(feature);// for compatibility
            this.events.triggerEvent('featureadded', {feature: feature});
        }
    },

    /**
     * Method: callbackCancel
     */
    destroyLabels: function() {
        
        this.layerSegments && this.layerSegments.destroyFeatures(null, {silent: true});

        this.layerLength && this.layerLength.destroyFeatures(null, {silent: true});
        
        this.layerHeading && this.layerHeading.destroyFeatures(
                                                          null, {silent: true});
        this.layerArea && this.layerArea.destroyFeatures(null, {silent: true});
    },

    /**
     * Method: callbackPoint
     */
    callbackPoint: function(point, geometry) {
        var dynamicObj = this.dynamicObj;
        if (!dynamicObj.drawing) {
            this.destroyLabels();
        }
        if (!this.handler.freehandMode(this.handler.evt)) {
            dynamicObj.fromIndex = this.handler.getCurrentPointIndex() - 1;
            dynamicObj.freehand = false;
            dynamicObj.countSegments++;
        } else if (!dynamicObj.freehand) {
            // freehand has started
            dynamicObj.fromIndex = this.handler.getCurrentPointIndex() - 1;
            dynamicObj.freehand = true;
            dynamicObj.countSegments++;
        }

        this.measurePartial(point, geometry);
        dynamicObj.drawing = true;
    },

    /**
     * Method: callbackUndo
     */
    callbackUndo: function(point, feature) {
        var _self = this,
            undoLabel = function(layer) {
                if (layer) {
                    var features = layer.features,
                        lastSegmentIndex = features.length - 1,
                        lastSegment = features[lastSegmentIndex],
                        lastSegmentFromIndex = lastSegment.attributes.from,
                        lastPointIndex = _self.handler.getCurrentPointIndex();
                    if (lastSegmentFromIndex >= lastPointIndex) {
                        var dynamicObj = _self.dynamicObj;
                        layer.destroyFeatures(lastSegment);
                        lastSegment = features[lastSegmentIndex - 1];
                        dynamicObj.fromIndex = lastSegment.attributes.from;
                        dynamicObj.countSegments = features.length;
                    }
                }
            };
        undoLabel(this.layerSegments);
        undoLabel(this.layerHeading);
        this.callbackModify(point, feature, true);
    },

    /**
     * Method: callbackRedo
     */
    callbackRedo: function(point, feature) {
        var line = this.handler.line.geometry,
            currIndex = this.handler.getCurrentPointIndex();
        var dynamicObj = this.dynamicObj;
        this.showLabelSegment(
            dynamicObj.countSegments,
            dynamicObj.fromIndex,
            line.components.slice(dynamicObj.fromIndex, currIndex)
        );
        dynamicObj.fromIndex = this.handler.getCurrentPointIndex() - 1;
        dynamicObj.countSegments++;
        this.callbackModify(point, feature, true);
    },

    /**
     * Method: callbackModify
     */
    callbackModify: function(point, feature, drawing) {
        if (this.immediate) {
            this.measureImmediate(point, feature, drawing);
        }

        var dynamicObj = this.dynamicObj;
        if (dynamicObj.drawing === false) {
           return;
        }

        var line = this.handler.line.geometry,
            currIndex = this.handler.getCurrentPointIndex();
        if (!this.handler.freehandMode(this.handler.evt) &&
                                                          dynamicObj.freehand) {
            // freehand has stopped
            dynamicObj.fromIndex = currIndex - 1;
            dynamicObj.freehand = false;
            dynamicObj.countSegments++;
        }

        // total measure
        var totalLength = this.getBestLength(line);
        if (!totalLength[0]) {
           return;
        }
        var positions = this.positions,
            positionGet = {
            center: function() {
                var center = feature.geometry.getBounds().clone();
                center.extend(point);
                center = center.getCenterLonLat();
                return [center.lon, center.lat];
            },
            initial: function() {
                var initial = line.components[0];
                return [initial.x, initial.y];
            },
            start: function() {
                var start = line.components[dynamicObj.fromIndex];
                return [start.x, start.y];
            },
            middle: function() {
                var start = line.components[dynamicObj.fromIndex];
                return [(start.x + point.x) / 2, (start.y + point.y) / 2];
            },
            end: function() {
                return [point.x, point.y];
            }
        };
        if (this.layerLength) {
            this.showLabel(
                        this.layerLength, 1, 0, totalLength,
                        positionGet[positions['labelLength']](), 1);
        }
        if (this.isArea) {
            if (this.layerArea) {
                this.showLabel(this.layerArea, 1, 0,
                     this.getBestArea(feature.geometry),
                     positionGet[positions['labelArea']](), 1);
            }
            if (this.showLabelSegment(
                      1, 0, [line.components[0], line.components[currIndex]])) {
                dynamicObj.countSegments++;
            }
        }
        this.showLabelSegment(
            dynamicObj.countSegments,
            dynamicObj.fromIndex,
            line.components.slice(dynamicObj.fromIndex, currIndex + 1)
        );
    },

    /**
     * Function: showLabelSegment
     *
     * Parameters:
     * labelsNumber- {Integer} Number of the labels to be on the label layer.
     * fromIndex - {Integer} Index of the last point on the measured feature.
     * points - Array({<OpenLayers.Geometry.Point>})
     *
     * Returns:
     * {Boolean}
     */
    showLabelSegment: function(labelsNumber, fromIndex, _points) {
        var layerSegments = this.layerSegments,
            layerHeading = this.layerHeading;
        if (!layerSegments && !layerHeading) {
            return false;
        }
        // clone points
        var points = [],
            pointsLen = _points.length;
        for (var i = 0; i < pointsLen; i++) {
            points.push(_points[i].clone());
        }
        var from = points[0],
            to = points[pointsLen - 1],
            segmentLength =
                 this.getBestLength(new OpenLayers.Geometry.LineString(points)),
            positions = this.positions,
            positionGet = {
                start: function() {
                    return [from.x, from.y];
                },
                middle: function() {
                    return [(from.x + to.x) / 2, (from.y + to.y) / 2];
                },
                end: function() {
                    return [to.x, to.y];
                }
            },
            created = false;
        if (layerSegments) {
            created = this.showLabel(layerSegments, labelsNumber, fromIndex,
                            segmentLength,
                            positionGet[positions['labelSegments']](),
                            this.maxSegments);
        }
        if (layerHeading && segmentLength[0] > 0) {
            var heading = Math.atan2(to.y - from.y, to.x - from.x),
                bearing = 90 - heading * 180 / Math.PI;
            if (bearing < 0) {
                bearing += 360;
            }
            created = created || this.showLabel(layerHeading,
                            labelsNumber, fromIndex,
                            [bearing, '°'],
                            positionGet[positions['labelHeading']](),
                            this.maxHeadings);
        }
        return created;
    },

    /**
     * Function: showLabel
     *
     * Parameters:
     * layer - {<OpenLayers.Layer.Vector>} Layer of the labels.
     * labelsNumber- {Integer} Number of the labels to be on the label layer.
     * fromIndex - {Integer} Index of the last point on the measured feature.
     * measure - Array({Float|String}) Measure provided by OL Measure control.
     * points - Array({Fload}) Array of x and y of the point to draw the label.
     * maxSegments - {Integer|Null} Maximum number of visible segments measures
     *
     * Returns:
     * {Boolean}
     */
    showLabel: function(
                     layer, labelsNumber, fromIndex, measure, xy, maxSegments) {
        var featureLabel, featureAux,
            features = layer.features;
        if (features.length < labelsNumber) {
        // add a label
            if (measure[0] === 0) {
                return false;
            }
            featureLabel = new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.Point(xy[0], xy[1]),
                {from: fromIndex}
            );
            this.setMesureAttributes(featureLabel.attributes, measure);
            layer.addFeatures([featureLabel]);
            if (maxSegments !== null) {
                var hide = (features.length - maxSegments) - 1;
                if (hide >= 0) {
                    featureAux = features[hide];
                    featureAux.style = {display: 'none'};
                    layer.drawFeature(featureAux);
                }
            }
            return true;
        } else {
        // update a label
            featureLabel = features[labelsNumber - 1];
            var geometry = featureLabel.geometry;
            geometry.x = xy[0];
            geometry.y = xy[1];
            geometry.clearBounds();
            this.setMesureAttributes(featureLabel.attributes, measure);
            layer.drawFeature(featureLabel);
            if (maxSegments !== null) {
                var show = (features.length - maxSegments);
                if (show >= 0) {
                    featureAux = features[show];
                    if (featureAux.style) {
                        delete featureAux.style;
                        layer.drawFeature(featureAux);
                    }
                }
            }
            return false;
        }
    },

    /**
     * Method: setMesureAttributes
     * Format measure[0] with digits of <accuracy>. Could internationalize the
     *     format customizing <OpenLayers.Number.thousandsSeparator> and
     *     <OpenLayers.Number.decimalSeparator>
     *
     * Parameters:
     * attributes - {object} Target attributes.
     * measure - Array({*})
     */
    setMesureAttributes: function(attributes, measure) {
        attributes.measure = OpenLayers.Number.format(measure[0].toFixed(this.accuracy), null);
                           //Number(measure[0].toPrecision(this.accuracy)), null);
        attributes.units = measure[1];
    },

    CLASS_NAME: 'OpenLayers.Control.DynamicMeasure'
});

/**
 * Constant: OpenLayers.Control.DynamicMeasure.styles
 * Contains the keys: "Point", "Line", "Polygon",
 *     "labelSegments", "labelHeading", "labelLength" and
 *     "labelArea" as a objects with style keys.
 */
OpenLayers.Control.DynamicMeasure.styles = {
    'Point': {
    	pointRadius: 0,
        graphicName: 'square',
        fillColor: 'white',
        fillOpacity: 1,
        strokeWidth: 1,
        strokeOpacity: 1,
        strokeColor: '#333333'
    },
    'Line': {
        strokeWidth: 2,
        strokeOpacity: 1,
        strokeColor: '#666666',
        strokeDashstyle: 'dash'
    },
    'Polygon': {
        strokeWidth: 2,
        strokeOpacity: 1,
        strokeColor: '#666666',
        strokeDashstyle: 'solid',
        fillColor: 'white',
        fillOpacity: 0.3
    },
    labelSegments: {
        pointRadius: 0,
        label: '${measure} ${units}',
        fontSize: '12px',
        fontColor: '#800517',
        fontFamily: 'Verdana',
        labelOutlineColor: '#eeeeee',
        labelAlign: 'cm',
        labelOutlineWidth: 2
    },
    labelLength: {
        pointRadius: 0,
        label: '${measure} ${units}\n',
        fontSize: '12px',
        fontWeight: 'bold',
        fontColor: '#800517',
        fontFamily: 'Verdana',
        labelOutlineColor: '#eeeeee',
        labelAlign: 'lb',
        labelOutlineWidth: 3
    },
    labelArea: {
        pointRadius: 0,
        label: '${measure}\n${units}²\n',
        fontSize: '11px',
        fontWeight: 'bold',
        fontColor: '#800517',
        fontFamily: 'Verdana',
        labelOutlineColor: '#dddddd',
        labelAlign: 'cm',
        labelOutlineWidth: 3
    },
    labelHeading: {
        pointRadius: 0,
        label: '${measure} ${units}',
        fontSize: '11px',
        fontColor: '#800517',
        fontFamily: 'Verdana',
        labelOutlineColor: '#dddddd',
        labelAlign: 'cm',
        labelOutlineWidth: 3
    }
};

/**
 * Constant: OpenLayers.Control.DynamicMeasure.positions
 * Contains the keys: "labelSegments", "labelHeading",
 *     "labelLength" and "labelArea" as a strings with values 'start',
 *     'middle' and 'end' allowed for all keys (refered of last segment) and
 *     'center' and 'initial' (refered of the measured feature and only allowed
 *     for "labelLength" and "labelArea" keys)
 */
OpenLayers.Control.DynamicMeasure.positions = {
    labelSegments: 'middle',
    labelLength: 'end',
    labelArea: 'center',
    labelHeading: 'start'
};

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * ###Main map client class
 *  
 * Typical use case is to call the method configure(config) where config is a valid configuration object (usually parsed from JSON).
 * ###[Integration example](https://github.com/Sundsvallskommun/OpenEMap-WebUserInterface/blob/master/README.md)
 */
Ext.define('OpenEMap.Client', {
                                                  
                              
                                        
                                            
                                            
                                                           
                                                             
    version: '1.6.1',
    /**
     * OpenLayers Map instance
     * 
     * @property {OpenLayers.Map}
     */
    map: null,
    /**
     * Overlay used by drawing actions
     * 
     * StyleMap can be overridden if more specific styling logic is required. 
     * 
     * Here is an example that changes style on scale:
     * 
     *     var style = new OpenLayers.Style();
     * 
     *     var ruleLow = new OpenLayers.Rule({
     *       symbolizer: {
     *         pointRadius: 10,
     *         fillColor: 'green',
     *         fillOpacity: 1
     *       },
     *       maxScaleDenominator: 10000
     *     });
     * 
     *     var ruleHigh = new OpenLayers.Rule({
     *       symbolizer: {
     *         pointRadius: 10,
     *         fillColor: 'red',
     *         fillOpacity: 1   
     *      },
     *      minScaleDenominator: 10000
     *     });
     * 
     *     style.addRules([ruleLow, ruleHigh]);
     * 
     *     var styleMap = new OpenLayers.StyleMap(style);
     *     mapClient.drawLayer.styleMap = styleMap;
     *     
     * Here is an example style to display labels for features with attribute property "type" == "label":
     * 
     *     var labels = new OpenLayers.Rule({
     *       filter: new OpenLayers.Filter.Comparison({
     *         type: OpenLayers.Filter.Comparison.EQUAL_TO,
     *         property: "type",
     *         value: "label",
     *       }),
     *       symbolizer: {
     *         label: "${label}"
     *       }
     *     });
     * 
     * See [OpenLayers documentation][1] on feature styling for more examples.
     * 
     * [1]: http://docs.openlayers.org/library/feature_styling.html
     * 
     * @property {OpenLayers.Layer.Vector}
     */
    drawLayer: undefined,
    /**
     * Configure map
     * 
     * If this method is to be used multiple times, make sure to call destroy before calling it.
     * 
     * @param {Object} config Map configuration object
     * @param {Object} options Additional MapClient options
     * @param {Function options.callback Callback that is called when the client is configured and ready 
     * @param {Object} options.gui Options to control GUI elements. Each property in this object is
     * essentially a config object used to initialize an Ext JS component. If a property is undefined or false
     * that component will not be initialized except for the map component. If a property is a defined
     * but empty object the component will be rendered floating over the map. To place a component into a 
     * predefined html tag, use the config property renderTo.
     * @param {Object} options.gui.map If undefined or false MapClient will create the map in a full page viewport
     * @param {Object} options.gui.layers Map layers tree list
     * @param {Object} options.gui.baseLayers Base layer switcher intended to be used as a floating control
     * @param {Object} options.gui.searchCoordinate Simple coordinate search and pan control
     * @param {Object} options.gui.objectConfig A generic form to configure feature attributes similar to a PropertyList
     * @param {Object} options.gui.zoomTools Zoom slider and buttons intended to be used as a floating control
     * @param {Object} options.gui.searchFastighet Search "fastighet" control
     * @param {Object} options.gui.showCoordinate Simple control to show map coordinates 
     * 
     * For more information about the possible config properties for Ext JS components see Ext.container.Container.
     */
     configure: function(config, options) {
        options = Ext.apply({}, options);
        
        this.initialConfig = Ext.clone(config);
        this.initialOptions = Ext.clone(options);
        
        Ext.tip.QuickTipManager.init();
        
        var parser = Ext.create('OpenEMap.config.Parser');

        this.map = parser.parse(config);
        this.gui = Ext.create('OpenEMap.Gui', {
            config: config,
            gui: options.gui,
            map: this.map,
            client: this,
            orginalConfig: this.initialConfig
        });
        this.mapPanel = this.gui.mapPanel;
        this.drawLayer = this.gui.mapPanel.drawLayer;
        
        if (this.gui.controlToActivate) {
            this.gui.controlToActivate.activate();
        }
        
        if (options.callback) {
            options.callback.call(this);
        }
    },
    getPermalinkdata: function() {
        var features = this.drawLayer.features;
        var format = new OpenLayers.Format.GeoJSON();
        var geojson = format.write(features);
    
        return {
            version: this.version,
            config: this.getConfig(),
            options: this.initialOptions,
            extent: this.map.getExtent().toArray(),
            drawLayer: {
                geojson: geojson
            }
        };
    },
    /**
     * @param {boolean} includeLayerRef include reference to OpenLayers layer if available
     * @return {Object} Object representation of current Open eMap configuration
     */
    getConfig: function(includeLayerRef) {
       return this.gui.mapLayers.getConfig(includeLayerRef); 
    },
    /**
     * @param {String=} Name of layout to use (default is to use first layout as reported by server)
     * @return {String} JSON encoding of current map for MapFish Print module
     */
    encode: function(layout) {
        return JSON.stringify(this.mapPanel.encode(layout));
    },
    /**
     * Helper method to add GeoJSON directly to the draw layer
     * 
     * @param {string} geojson
     */
    addGeoJSON: function(geojson) {
        var format = new OpenLayers.Format.GeoJSON();
        var feature = format.read(geojson, "Feature");
        
        if (feature.attributes.config) {
            var objectFactory = Ext.create('OpenEMap.ObjectFactory');
            feature = objectFactory.create(feature.attributes.config, feature.attributes);
        }
        
        this.drawLayer.addFeatures([feature]);
    },
    /**
     * Allows you to override the sketch style at runtime
     * 
     * @param {OpenLayers.StyleMap} styleMap
     */
    setSketchStyleMap: function(styleMap) {
        this.map.controls.forEach(function(control) {
            if (control instanceof OpenLayers.Control.DrawFeature) {
                control.handler.layerOptions.styleMap = styleMap;
                if (control.handler.layer) {
                    control.handler.layer.styleMap = styleMap;
                }
            }
        });
    },
    /**
     * Enable additional labels for polygon edges
     * NOTE: deactivation not yet implemented
     * @param style hash of style properties that will override a default label style
     * @param {number} [accuracy=2] number of digits for edge labels 
     */
    toggleEdgeLabels: function(style, accuracy) {
        var styleOverride = style || {};
		function isInt(value) {
		  return !isNaN(value) && 
		         parseInt(Number(value)) == value && 
		         !isNaN(parseInt(value, 10));
		}
        if (!isInt(accuracy)) {
        	accuracy = 2;
        }
        
        var drawLabels = function() {
            var createEdgeLabels = function(feature) {
                var geometry = feature.geometry;
                
                if (geometry.CLASS_NAME != "OpenLayers.Geometry.Polygon") return [];
                
                var linearRing = geometry.components[0];
                
                var edgeLabels = linearRing.components.slice(0, linearRing.components.length-1).map(function(point, i) {
                    var start = linearRing.components[i].clone();
                    var end = linearRing.components[i+1].clone();
                    var lineString = new OpenLayers.Geometry.LineString([start, end]);
                    var centroid = lineString.getCentroid({weighted: true});
                    var style = Ext.applyIf(Ext.clone(styleOverride), {
                        label: lineString.getLength().toFixed(accuracy).toString() + " m",
                        strokeColor: "#000000",
                        strokeWidth: 0,
                        labelAlign: 'cm',
                        pointRadius: 0
                    });
                    var feature = new OpenLayers.Feature.Vector(centroid, null, style);
                    return feature;
                });
                
                return edgeLabels;
            };
            
            if (this.labelLayer) {
            	this.labelLayer.destroyFeatures();
            }
            
            var edgeLabelsArrays = this.drawLayer.features.map(createEdgeLabels);
            if (edgeLabelsArrays.length > 0) {
                var edgeLabels = edgeLabelsArrays.reduce(function(a, b) {
                    return a.concat(b);
                });
                this.labelLayer.addFeatures(edgeLabels);
            }
        };
        
        if (this.labelLayer === undefined) {
            this.labelLayer = new OpenLayers.Layer.Vector();
            this.map.addLayer(this.labelLayer);
            
            this.drawLayer.events.on({
                "featuremodified": drawLabels,
                "vertexmodified": drawLabels,
                "featuresadded": drawLabels,
                "featuresremoved": drawLabels,
                scope: this 
            });
        } else {
            // TODO: disable edge labels
        }
        
        drawLabels.apply(this);
    },
    /**
     * Helper method to add add a new vector layer to map.
     * @param {string} geojson GeoJSON with features that should be added to map 
     * @param {string} layername Layer name 
     * @param {string} [idAttribute='id'] Name of the attribute stored in each feture that holds the a unique id. Defaults to 'id'. Must be unique.
     * @param {string} [popupTextAttribute='popupText'] Name of the attribute stored in each feture that holds the information to be shown in a popup defaults to 'popupText'
     * @param {string} [popupTextPrefix=''] Prefix to be shown in popup before the value in popupTextAttribute 
     * @param {string} [popupTextSuffix=''] Suffix to be shown in popup before the value in popupTextAttribute
     * @param {string} [popupTitleAttribute=null] Title for the popup
     * @param {OpenLayers.Feature.Vector.Stylemap} [stylemap=deafult style] Stylemap used when drawing features in the layer. Uses default style if not specified
     * @param {string} [epsg='EPSG:3006'] Coordinate system reference according to EPSG-standard, defaults to 'EPSG:3006' (Sweref 99 TM) 
     * @param {Boolean} [zoomToBounds=true] Flags whether map should be zoomed to extent of features when the layer is added, defaults to true
     * @return {OpenLayers.Layer} Returns the layer added. null if layer cant be created
     */
    addPopupLayer: function(geojson, layername, idAttribute, popupTextAttribute, popupTextPrefix, popupTextSuffix, popupTitleAttribute, stylemap, epsg, zoomToBounds) {
        if (!geojson) {
			Ext.Error.raise('GeoJSON-string is null.');
        }
        if (!layername) {
        	// set default layer name 
        	layername = "VectorLayer";
        } 
        if (!idAttribute) {
        	idAttribute = 'id';
        }
        
        if (!popupTextAttribute) {
        	popupTextAttribute = 'popupText';
        }

		if (!popupTextPrefix) {
			popupTextPrefix ='';
		}
		if (!popupTextSuffix) {
			popupTextSuffix = '';
		}
		if (!popupTitleAttribute) {
			popupTitleAttribute =null;
		}
		if (!epsg) {
			epsg = 'EPSG:3006';
		} 
		if (!Proj4js.defs[epsg])
		{
			Ext.Error.raise('Unknown coordinate system: ' + epsg + '\nAdd coordinate system using proj4.defs(\'Name\', \'Definition\')');
		}
		if (zoomToBounds === null) {
			zoomToBounds = true;
		} 

        var format = new OpenLayers.Format.GeoJSON();

		//  Projection settings 
 		var fromProjection = epsg;
        var toProjection = this.map.projection;
        format.internalProjection = new OpenLayers.Projection(toProjection);
        format.externalProjection = new OpenLayers.Projection(fromProjection);

        var features = format.read(geojson, "FeatureCollection");
	    if (!features) {
			Ext.Error.raise('Can not read features from GeoJSON due to malformed syntax.' );
	    }
 		
        // allow testing of specific renderers via "?renderer=Canvas", etc
        var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
        renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
		
	    // creating a vector layer with specific options that apply to popup layers
	    var popupLayer = new OpenLayers.Layer.Vector(layername, {renderers: renderer, idAttribute: idAttribute, popupTextAttribute: popupTextAttribute, popupTextPrefix: popupTextPrefix, popupTextSuffix: popupTextSuffix, popupTitleAttribute: popupTitleAttribute} );
	    if (!popupLayer){
			Ext.Error.raise('Can not create popup layer: ' + layername);
	    }

		// Creates stylemap to use when drawing features of popup layer
        if (stylemap) {
			popupLayer.styleMap = stylemap;
        }
        
        // Add layer to map 
	  	this.map.addLayer(popupLayer);
 
		// Add features to layer
		popupLayer.addFeatures(features);
		
		// Set feature render intent an draws them
		var featureBounds = new OpenLayers.Bounds();
 		features.forEach(function(feature) {
 			feature.renderIntent='default';
 			featureBounds.extend(feature.geometry.getBounds());
 			popupLayer.drawFeature(feature);
		});
		
		popupLayer.popup = [];
		
		// Zoom to bounds of all features
		if (zoomToBounds) {
			popupLayer.map.zoomToExtent(featureBounds);
		}

	    return popupLayer;
    },
    /**
     * Helper method to remove a popup layer
     * @param {OpenLayers.Layer.Vector} [layer] Layer to remove
     */
    removePopupLayer: function(layer) {
		// remove any popup windows too
		if (layer.popup) { 
			// Remove any popup window
			layer.popup.forEach(function(p) {
				p.destroy();
				p = null;
			});
			layer.popup = [];
    	}
    	// Remove the layer
		mapClient.map.removeLayer(layer);
    },
    /**
     * Show popup for a feature
     * @private 
     * @param {OpenLayers.Layer.Vector} [popupLayer] layer to search for features
     * @param {OpenLayers.Feature} [feature] feature to show popup on
     */
	showPopupFeaturePopup: function(popupLayer, feature) {
    	// Destroy previously created popup
    	if (popupLayer.popup) { 
			// Remove any popup window
			popupLayer.popup.forEach(function(item) {item.destroy();});
    	}

    	// get text to populate popup 
    	var popupText = popupLayer.popupTextPrefix+feature.attributes[popupLayer.popupTextAttribute]+popupLayer.popupTextSuffix;
    	var popupTitle = '';
    	if (popupLayer.popupTitleAttribute) {
    		popupTitle = feature.attributes[popupLayer.popupTitleAttribute];
    	}

    	// Create popup 
    	var popup = new OpenEMap.view.PopupResults({mapPanel : this.gui.mapPanel, location: feature, popupText: popupText, feature: feature, title: popupTitle});

		// Show popup
        popup.show();
		
		// Adds popup to array of popups in map  
        popupLayer.popup.push(popup);
	},
    /**
     * Search for a feature inside a popup layer and show a popup if it matches. 
     * @param {OpenLayers.Layer.Vector} [popupLayer] Layer to search for features
     * @param {number} [featureId] Feature-id to search for
     * @param {boolean} [center] Whether to center over clicked position or not.  
     */
    showPopupFeature: function(popupLayer, featureId, center) {
    	if (!popupLayer) {
			Ext.Error.raise('Popup layer undefined.');
    	}
    	if (!featureId) {
			Ext.Error.raise('Feature id undefined.');
    	}
   	
    	var features = popupLayer.getFeaturesByAttribute(popupLayer.idAttribute, featureId);
    	// Check if there are any features matching id
    	if (features) {
    		// Check if there are more then one feature matching id
    		if (features.length == 1) {
	    		// Remove highlight feature
                var parser = Ext.create('OpenEMap.config.Parser');
                var popupLayers = parser.extractPopupLayers(popupLayer.map.layers);
				popupLayers.forEach(function(popupLayer) {
		    		popupLayer.features.forEach(function(feature) {
		    			if (feature.renderIntent == 'select') {
				    		feature.renderIntent = 'default';
				    		feature.layer.drawFeature(feature);
					    	// Fire action "popupfeatureunselected" on the feature including layer and featureid
					    	feature.layer.map.events.triggerEvent("popupfeatureunselected",{layer: popupLayer, featureid: feature.attributes[popupLayer.idAttribute]});
				    	}
		    		});
		    		// Remove popups too
		    		popupLayer.popup.forEach(function(item) {item.destroy();});
				});
				
	    		// Shows the first feature matching the id
	    		this.showPopupFeaturePopup(popupLayer, features[0]);

	    		// Highlight feature
	    		features[0].renderIntent = 'select';
	    		if (center) {
	    			var centerPoint = features[0].geometry.getCentroid();
	    			features[0].layer.map.setCenter([centerPoint.x,centerPoint.y]);
	    		}
	    		features[0].layer.drawFeature(features[0]);

		    	// Fire action "popupfeatureselected" on the feature including layer and featureid
		    	features[0].layer.map.events.triggerEvent("popupfeatureselected",{layer: popupLayer, featureid: features[0].attributes[popupLayer.idAttribute]});
    		} else {
				Ext.Error.raise('More then one feature with specified id: ' + featureId);
    		}    		
    	} else {
			Ext.Error.raise('No feature with specified id: ' + featureId);
    	}
    },
    /**
     * Helper method to destroy all popup layers 
     */
    destroyPopupLayers: function() {
        var parser = Ext.create('OpenEMap.config.Parser');
    	var popupLayers = parser.extractPopupLayers(this.map.layers);
		if (popupLayers) {
			// Remove popup layers
			popupLayers.forEach(function(layer) {
				this.removePopupLayer(layer);
			});
		}
    },
    /**
     * Clean up rendered elements
     */
    destroy: function() {
        if (this.map) {
        	if (this.map.controls) {
	            this.map.controls.forEach(function(control) { control.destroy(); });
	            this.map.controls = null;
            }
	        if (this.map.layers) {
				// Remove popup layers
				this.destroyPopupLayers();
	        }
        }
        if (this.gui) this.gui.destroy();
    }
});

Ext.ns('OpenEMap');

Ext.apply(OpenEMap, {
    lmUser: 'sundsvall',
    /**
     * Base path to be used for mapfish print servlet requests
     * 
     * @property {string}
     */
    basePathMapFish: '/print/pdf',
    /**
     * Base path to be used for all AJAX requests against search-lm REST API
     * 
     * @property {string}
     */
    basePathLM: '/search/lm/',
    /**
     * @property {string} 
     * Base path to be used for all AJAX requests against Elasticsearch REST API
     */
    basePathES: '/search/es/',
    /**
     * Base path to be used for all image resources
     * 
     * @property {string}
     */
    basePathImages: 'resources/images/',

    basePathWMS: '/geoserver/wms',
    
    /**
     * URL/paths related to WMS usage / advanced layer list
     */
    wmsURLs: {
        /**
         * URL to be used to fetch WMS capabilities etc. for add layer UI
         */
        basePath: '/geoserver/wms',
        /**
         * URL to be used when WMS layer has been added to config
         */
        url: 'https://extmaptest.sundsvall.se/geoserver/wms',
        /**
         * URL to getcapabilities document. Must include request parameter (eg. https://extmap.sundsvall.se/geoserver/wms?request=GetCapabilities&version=1.1.1) 
         */
        getCapabilities: 'https://extmaptest.sundsvall.se/getcapabilities/wms.xml'
    },

    /**
     * @property {string} 
     * Base path to proxy to be used for WFS-post
     */
    basePathProxy: '/cgi-bin/proxy.py?url=',

    /**
     * @property {Object} [wsUrls] WS paths to be used for AJAX requests
     * @property {string} [wsUrls.basePath] basepath to Open eMap Admin services 
     * @property {string} [wsUrls.configs] relative path to publig configs within Open eMap Admin services
     * @property {string} [wsUrls.adminconfigs] path to admin config service within Open eMap Admin services 
     * @property {string} [wsUrls.permalinks] path to Open eMap Permalink service 
     * @property {string} [wsUrls.metadata]  path to Open eMap Geo Metadata service
     * @property {string} [wsUrls.metadataAbstract] path to Open eMap Geo Metadata Abstract service
     * @property {string} [wsUrls.servers] unused
     * @property {string} [wsUrls.layers] unused 
     */
    wsUrls: {
        basePath:   	'/openemapadmin',
        permalinks:     '/openemappermalink/permalinks',
        configs:    	'/configs',
        adminconfigs: 	'/adminconfigs',
        servers:    	'settings/servers',
        layers:     	'layers/layers',
        metadata:   	'geometadata/getmetadatabyid', 
        metadata_abstract: 'geometadata/getabstractbyid'
    },
    /**
     * @property {String} user to use when saving map config 
     */
    username: null
});

Ext.apply(OpenEMap, {
    /**
     * Wrapped Ext.Ajax.request method that applies base path and user
     */
    requestLM: function(config) {
        config.url = OpenEMap.basePathLM + config.url + '&lmuser=' + OpenEMap.lmUser;
        Ext.Ajax.request(config);
    }
});


OpenLayers.Layer.Vector.prototype.renderers = ["Canvas", "SVG", "VML"];
// Setting resolution to coerce with GeoWebCache
OpenLayers.DOTS_PER_INCH = 90.71446714322;
 						   
	/**
	 * @event popupfeatureselected 
	 * fires when a feature in a popup layer is selected
	 * @param {OpenLayers.layer} layer popup layer
	 * @param {number} featureid id of selected feature 
	 */
	/**
	 * @event popupfeatureunselected 
	 * fires when a previously selected feature in a popup layer gets unselected
	 * @param {OpenLayers.layer} layer popup layer
	 * @param {number} featureid id of unselected feature 
	 */

/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
Ext.define("OpenEMap.locale.sv_SE.Gui", {
    override: "OpenEMap.Gui",
    objectConfigWindowTitle: "Objektkonfiguration"
});

Ext.define("OpenEMap.locale.sv_SE.view.ObjectConfig", {
    override: "OpenEMap.view.ObjectConfig",
    typeLabel: "Typ",
    widthLabel: "Bredd",
    lengthLabel: "Längd",
    m1Label: "M1",
    m2Label: "M2",
    angleLabel: "Vinkel"
});
/*    
    Copyright (C) 2014 Härnösands kommun

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * TODO: needs to be implemented
 * Is not in use
 */
Ext.define('OpenEMap.Search', {
    constructor: function(config) {
        initConfig();
    },
    doSearch: function() {
        // TODO: generic search logic..?
    }
});

