/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */
(function() {
    var major = 2,
        minor = 0,
        patch = 3,
        label = 'dev',
        environment = [],
        v = '';

    // Concatenate GeoExt version.
    v = 'v' + major + '.' + minor + '.' + patch + (label ? '.' + label : '');

    // Grab versions of libraries in the environment
    if ( Ext.versions.extjs.version ) {
        environment.push('ExtJS: ' + Ext.versions.extjs.version);
    }
    if ( window.OpenLayers ) {
        environment.push('OpenLayers: ' + OpenLayers.VERSION_NUMBER);
    }
    environment.push('GeoExt: ' + v);

    /**
     * A singleton class holding the properties #version with the current
     * GeoExt version and #environment with a string about the surrounding
     * libraries ExtJS and OpenLayers.
     */
    Ext.define('GeoExt.Version', {
        singleton: true,

        /**
         * The version number of GeoExt.
         *
         * @property {String} version
         */
        version: v,

        /**
         * Lists the versions of the currently loaded libraries and contains the
         * versions of `ExtJS`, `OpenLayers` and `GeoExt`.
         *
         * @property {String} environment
         */
        environment: (environment.join(', '))
    }, function() {
        /**
         * The GeoExt root object.
         *
         * @class GeoExt
         * @singleton
         */
        /**
         * @inheritdoc GeoExt.Version#version
         * @member GeoExt
         * @property version
         */
        GeoExt.version = GeoExt.Version.version;
    });
})();

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * Action class to create GeoExt.Action
 *
 * Sample code to create a toolbar with an OpenLayers control into it.
 *
 * Example:
 *
 *     var action = Ext.create('GeoExt.Action', {
 *         text: "max extent",
 *         control: new OpenLayers.Control.ZoomToMaxExtent(),
 *         map: map
 *     });
 *     var toolbar = Ext.create('Ext.toolbar.Toolbar', action);
 *
 * @class GeoExt.Action
 */
Ext.define('GeoExt.Action', {
    extend:  Ext.Action ,
    alias : 'widget.gx_action',
               
                        
      

    /**
     * The OpenLayers control wrapped in this action.
     *
     * @cfg {OpenLayers.Control}
     */
    control: null,

    /**
     * Activate the action's control when the action is enabled.
     *
     * @property {Boolean} activateOnEnable
     */
    /**
     * Activate the action's control when the action is enabled.
     *
     * @cfg {Boolean} activateOnEnable
     */
    activateOnEnable: false,

    /**
     * Deactivate the action's control when the action is disabled.
     *
     * @property {Boolean} deactivateOnDisable
     */
    /**
     * Deactivate the action's control when the action is disabled.
     *
     * @cfg {Boolean} deactivateOnDisable
     */
    deactivateOnDisable: false,

    /**
     * The OpenLayers map that the control should be added to. For controls that
     * don't need to be added to a map or have already been added to one, this
     * config property may be omitted.
     *
     * @cfg {OpenLayers.Map}
     */
    map: null,

    /**
     * The user-provided scope, used when calling uHandler, uToggleHandler,
     * and uCheckHandler.
     *
     * @property {Object}
     * @private
     */
    uScope: null,

    /**
     * References the function the user passes through the "handler" property.
     *
     * @property {Function}
     * @private
     */
    uHandler: null,

    /**
     * References the function the user passes through the "toggleHandler"
     * property.
     *
     * @property {Function}
     * @private
     */
    uToggleHandler: null,

    /**
     * References the function the user passes through the "checkHandler"
     * property.
     *
     * @property {Function}
     * @private
     */
    uCheckHandler: null,

    /**
     * Create a GeoExt.Action instance. A GeoExt.Action is created to insert
     * an OpenLayers control in a toolbar as a button or in a menu as a menu
     * item. A GeoExt.Action instance can be used like a regular Ext.Action,
     * look at the Ext.Action API doc for more detail.
     *
     * @param {Object} config (optional) Config object.
     * @private
     */
    constructor: function(config){
        // store the user scope and handlers
        this.uScope = config.scope;
        this.uHandler = config.handler;
        this.uToggleHandler = config.toggleHandler;
        this.uCheckHandler = config.checkHandler;

        config.scope = this;
        config.handler = this.pHandler;
        config.toggleHandler = this.pToggleHandler;
        config.checkHandler = this.pCheckHandler;

        // set control in the instance, the Ext.Action
        // constructor won't do it for us
        this.control = config.control;
        var ctrl = this.control;
        delete config.control;

        this.activateOnEnable = !!config.activateOnEnable;
        delete config.activateOnEnable;
        this.deactivateOnDisable = !!config.deactivateOnDisable;
        delete config.deactivateOnDisable;

        // register "activate" and "deactivate" listeners
        // on the control
        if (ctrl) {
            // If map is provided in config, add control to map.
            if (config.map) {
                config.map.addControl(ctrl);
                delete config.map;
            }
            if((config.pressed || config.checked) && ctrl.map) {
                ctrl.activate();
            }
            if (ctrl.active) {
                config.pressed = true;
                config.checked = true;
            }
            ctrl.events.on({
                activate: this.onCtrlActivate,
                deactivate: this.onCtrlDeactivate,
                scope: this
            });

        }

        this.callParent(arguments);

    },

    /**
     * The private handler.
     *
     * @param {Ext.Component} The component that triggers the handler.
     * @private
     */
    pHandler: function(cmp){
        var ctrl = this.control;
        if (ctrl &&
        ctrl.type == OpenLayers.Control.TYPE_BUTTON) {
            ctrl.trigger();
        }
        if (this.uHandler) {
            this.uHandler.apply(this.uScope, arguments);
        }
    },

    /**
     * The private toggle handler.
     *
     * @param {Ext.Component} cmp The component that triggers the toggle 
     *     handler.
     * @param {Boolean} state The state of the toggle.
     * @private
     */
    pToggleHandler: function(cmp, state){
        this.changeControlState(state);
        if (this.uToggleHandler) {
            this.uToggleHandler.apply(this.uScope, arguments);
        }
    },

    /**
     * The private check handler.
     *
     * @param {Ext.Component} cmp The component that triggers the check handler.
     * @param {Boolean} state The state of the toggle.
     * @private
     */
    pCheckHandler: function(cmp, state){
        this.changeControlState(state);
        if (this.uCheckHandler) {
            this.uCheckHandler.apply(this.uScope, arguments);
        }
    },

    /**
     * Change the control state depending on the state boolean.
     *
     * @param {Boolean} state The state of the toggle.
     * @private
     */
    changeControlState: function(state){
        if(state) {
            if(!this._activating) {
                this._activating = true;
                this.control.activate();
                // update initialConfig for next component created from this action
                this.initialConfig.pressed = true;
                this.initialConfig.checked = true;
                this._activating = false;
            }
        } else {
            if(!this._deactivating) {
                this._deactivating = true;
                this.control.deactivate();
                // update initialConfig for next component created from this action
                this.initialConfig.pressed = false;
                this.initialConfig.checked = false;
                this._deactivating = false;
            }
        }
    },

    /**
     * Called when this action's control is activated.
     *
     * @private
     */
    onCtrlActivate: function(){
        var ctrl = this.control;
        if(ctrl.type == OpenLayers.Control.TYPE_BUTTON) {
            this.enable();
        } else {
            // deal with buttons
            this.safeCallEach("toggle", [true]);
            // deal with check items
            this.safeCallEach("setChecked", [true]);
        }
    },

    /**
     * Called when this action's control is deactivated.
     *
     * @private
     */
    onCtrlDeactivate: function(){
        var ctrl = this.control;
        if(ctrl.type == OpenLayers.Control.TYPE_BUTTON) {
            this.disable();
        } else {
            // deal with buttons
            this.safeCallEach("toggle", [false]);
            // deal with check items
            this.safeCallEach("setChecked", [false]);
        }
    },

   /**
    * Called when the control which should get toggled
    * is not of type OpenLayers.Control.TYPE_BUTTON
    *
    * @private
    */
   safeCallEach: function(fnName, args) {
       var cs = this.items;
       for(var i = 0, len = cs.length; i < len; i++){
           if(cs[i][fnName]) {
               cs[i].rendered ?
                   cs[i][fnName].apply(cs[i], args) :
                   cs[i].on({
                       "render": Ext.Function.bind(cs[i][fnName], cs[i], args),
                       single: true
                   });
           }
       }
   },

   /**
    * Override method on super to optionally deactivate controls on disable.
    *
    * @param {Boolean} v Disable the action's components.
    * @private
    */
   setDisabled : function(v) {
       if (!v && this.activateOnEnable && this.control && !this.control.active) {
           this.control.activate();
       }
       if (v && this.deactivateOnDisable && this.control && this.control.active) {
           this.control.deactivate();
       }
       return GeoExt.Action.superclass.setDisabled.apply(this, arguments);
   }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires OpenLayers/Feature/Vector.js
 * @include OpenLayers/Geometry/Point.js
 * @include OpenLayers/Geometry/LinearRing.js
 * @include OpenLayers/Geometry/Polygon.js
 * @include OpenLayers/Geometry/LineString.js
 * @include OpenLayers/Renderer/SVG.js
 * @requires GeoExt/Version.js
 */

/**
 * The feature renderer
 *
 * @class GeoExt.FeatureRenderer
 */
Ext.define('GeoExt.FeatureRenderer', {
    extend:  Ext.Component ,
    alias: 'widget.gx_renderer',
               
                        
      

    /**
     * Optional vector to be drawn.  If not provided, and if `symbolizers`
     * is configured with an array of plain symbolizer objects, `symbolType`
     * should be configured.
     *
     * @cfg {OpenLayers.Feature.Vector}
     */
    feature: undefined,

    /**
     * An array of `OpenLayers.Symbolizer` instances or plain symbolizer
     * objects (in painters order) for rendering a  feature.  If no
     * symbolizers are provided, the OpenLayers default will be used. If a
     * symbolizer is an instance of `OpenLayers.Symbolizer`, its type will
     * override the symbolType for rendering.
     *
     * @cfg {Object[]}
     */
    symbolizers: [OpenLayers.Feature.Vector.style["default"]],

    /**
     * One of `"Point"`, `"Line"`, `"Polygon"` or `"Text"`.  Only
     * pertinent if OpenLayers.Symbolizer objects are not used.  If `feature`
     * is provided, it will be preferred.
     *
     * @cfg {String}
     */
    symbolType: "Polygon",

    /**
     * The resolution for the renderer.
     *
     * @property {Number}
     * @private
     */
    resolution: 1,

    /**
     * @property {Number}
     * @private
     */
    minWidth: 20,

    /**
     * @property {Number}
     * @private
     */
    minHeight: 20,

    /**
     * List of supported Renderer classes. Add to this list to add support for
     * additional renderers. The first renderer in the list that returns
     * `true` for the `supported` method will be used, if not defined in
     * the `renderer` config property.
     *
     * @property {String[]}
     * @private
     */
    renderers: ["SVG", "VML", "Canvas"],

    /**
     * Options for the renderer. See `OpenLayers.Renderer` for supported
     * options.
     *
     * @property {Object}
     * @private
     */
    rendererOptions: null,

    /**
     * Feature with point geometry.
     *
     * @property {OpenLayers.Feature.Vector}
     * @private
     */
    pointFeature: undefined,

    /**
     * Feature with LineString geometry.  Default zig-zag is provided.
     *
     * @property {OpenLayers.Feature.Vector}
     * @private
     */
    lineFeature: undefined,

    /**
     * Feature with Polygon geometry.  Default is a soft cornered rectangle.
     *
     * @property {OpenLayers.Feature.Vector}
     * @private
     */
    polygonFeature: undefined,

    /**
     * Feature with invisible Point geometry and text label.
     *
     * @property {OpenLayers.Feature.Vector}
     * @private
     */
    textFeature: undefined,

    /**
     * @private
     * @property {OpenLayers.Renderer}
     */
    renderer: null,

    initComponent: function(){
        var me = this;

        this.autoEl = {
            tag: "div",
            "class": (this.imgCls ? this.imgCls : ""),
            id: this.getId()
        };
        me.callParent(arguments);

        Ext.applyIf(this, {
            pointFeature: new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.Point(0, 0)
                ),
            lineFeature: new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.LineString([
                    new OpenLayers.Geometry.Point(-8, -3),
                    new OpenLayers.Geometry.Point(-3, 3),
                    new OpenLayers.Geometry.Point(3, -3),
                    new OpenLayers.Geometry.Point(8, 3)
                    ])
                ),
            polygonFeature: new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.Polygon([
                    new OpenLayers.Geometry.LinearRing([
                        new OpenLayers.Geometry.Point(-8, -4),
                        new OpenLayers.Geometry.Point(-6, -6),
                        new OpenLayers.Geometry.Point(6, -6),
                        new OpenLayers.Geometry.Point(8, -4),
                        new OpenLayers.Geometry.Point(8, 4),
                        new OpenLayers.Geometry.Point(6, 6),
                        new OpenLayers.Geometry.Point(-6, 6),
                        new OpenLayers.Geometry.Point(-8, 4)
                        ])
                    ])
                ),
            textFeature: new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.Point(0, 0)
            )
        });
        if(!this.feature) {
            this.setFeature(null, {
                draw: false
            });
        }
        this.addEvents(
            /**
             * Fires when the feature is clicked on.
             *
             * Listener arguments:
             *
             *  * renderer - GeoExt.FeatureRenderer This feature renderer.
             *
             * @event
             */
            "click"
        );
    },

    /**
     * (Re-)Initializes our custom event listeners, mainly #onClick.
     *
     * @private
     */
    initCustomEvents: function() {
        this.clearCustomEvents();
        this.el.on("click", this.onClick, this);

    },

    /**
     * Unbinds previously bound listeners on #el.
     *
     * @private
     */
    clearCustomEvents: function() {
        if (this.el && this.el.removeAllListeners) {
            this.el.removeAllListeners();
        }
    },

    /**
     * Bound to the click event on the #el, this fires the click event.
     *
     * @private
     */
    onClick: function() {
        this.fireEvent("click", this);
    },

    /**
     * When we are rendered, we setup our own DOM structure and eventually
     * call #drawFeature.
     *
     * @private
     */
    onRender: function(ct, position) {
        if(!this.el) {
            this.el = document.createElement("div");
            this.el.id = this.getId();
//            document.body.appendChild(this.el);

        }
        if(!this.renderer || !this.renderer.supported()) {
            this.assignRenderer();
        }
        // monkey-patch renderer so we always get a resolution
        this.renderer.map = {
            //            getResolution: (function() {
            //                return this.resolution;
            //            }).createDelegate(this)
            getResolution: Ext.Function.bind(function() {
                return this.resolution;
            }, this)
        };
        this.callParent(arguments);
        this.drawFeature();
    },

    /**
     * After rendering we setup our own custom events using #initCustomEvents.
     *
     * @private
     */
    afterRender: function() {
        this.callParent(arguments);
        this.initCustomEvents();
    },

    /**
     * When resizing has happened, we might need to re-set the renderer's
     * dimensions via #setRendererDimensions.
     *
     * @private
     */
    onResize: function(w, h) {
        this.setRendererDimensions();
        this.callParent(arguments);
    },

    /**
     * Sets the dimensions of the renderer according to the #feature geometry
     * and our own dimensions.
     *
     * @private
     */
    setRendererDimensions: function() {
        var gb = this.feature.geometry.getBounds();
        var gw = gb.getWidth();
        var gh = gb.getHeight();
        /*
         * Determine resolution based on the following rules:
         * 1) always use value specified in config
         * 2) if not specified, use max res based on width or height of element
         * 3) if no width or height, assume a resolution of 1
         */
        var resolution = this.initialConfig.resolution;
        if(!resolution) {
            resolution = Math.max(gw / this.width || 0, gh / this.height || 0) || 1;
        }
        this.resolution = resolution;
        // determine height and width of element
        var width = Math.max(this.width || this.minWidth, gw / resolution);
        var height = Math.max(this.height || this.minHeight, gh / resolution);
        // determine bounds of renderer
        var center = gb.getCenterPixel();
        var bhalfw = width * resolution / 2;
        var bhalfh = height * resolution / 2;
        var bounds = new OpenLayers.Bounds(
            center.x - bhalfw, center.y - bhalfh,
            center.x + bhalfw, center.y + bhalfh
            );
        this.renderer.setSize(new OpenLayers.Size(Math.round(width), Math.round(height)));
        this.renderer.setExtent(bounds, true);
    },

    /**
     * Iterate through the available renderer implementations and selects
     * and assign the first one whose `supported` method returns `true`.
     *
     * @private
     */
    assignRenderer: function()  {
        for(var i=0, len=this.renderers.length; i<len; ++i) {
            var Renderer = OpenLayers.Renderer[this.renderers[i]];
            if(Renderer && Renderer.prototype.supported()) {
                this.renderer = new Renderer(
                    this.el, this.rendererOptions
                    );
                break;
            }
        }
    },

    /**
     * Update the symbolizers used to render the feature.
     *
     * @param symbolizers {Object[]} An array of symbolizers
     * @param options {Object}
     * @param options.draw {Boolean} Draw the feature after setting it.
     *     Default is `true`.
     */
    setSymbolizers: function(symbolizers, options) {
        this.symbolizers = symbolizers;
        if(!options || options.draw) {
            this.drawFeature();
        }
    },

    /**
     * Create a new feature based on the geometry type and render it.
     *
     * @param type {String} One of the `symbolType` strings.
     * @param options {Object}
     * @param options.draw {Boolean} Draw the feature after setting it.
     *     Default is `true`.
     *
     */
    setSymbolType: function(type, options) {
        this.symbolType = type;
        this.setFeature(null, options);
    },

    /**
     * Update the feature and redraw.
     *
     * @param feature {OpenLayers.Feature.Vector} The feature to be rendered.
     *     If none is provided, one will be created based on `symbolType`.
     * @param options {Object}
     * @param options.draw {Boolean} Draw the feature after setting it.
     *     Default is `true`.
     *
     */
    setFeature: function(feature, options) {
        this.feature = feature || this[this.symbolType.toLowerCase() + "Feature"];
        if(!options || options.draw) {
            this.drawFeature();
        }
    },

    /**
     * Render the feature with the symbolizers.
     *
     * @private
     */
    drawFeature: function() {
        this.renderer.clear();
        this.setRendererDimensions();
        var symbolizer, feature, geomType;
        for (var i=0, len=this.symbolizers.length; i<len; ++i) {
            symbolizer = this.symbolizers[i];
            feature = this.feature;
            if (symbolizer instanceof OpenLayers.Symbolizer) {
                symbolizer = symbolizer.clone();
                if (OpenLayers.Symbolizer.Text &&
                    symbolizer instanceof OpenLayers.Symbolizer.Text &&
                    symbolizer.graphic === false) {
                        // hide the point geometry
                        symbolizer.fill = symbolizer.stroke = false;
                }
                if (!this.initialConfig.feature) {
                    geomType = symbolizer.CLASS_NAME.split(".").pop().toLowerCase();
                    feature = this[geomType + "Feature"];
                }
            } else {
                // TODO: remove this when OpenLayers.Symbolizer is used everywhere
                symbolizer = Ext.apply({}, symbolizer);
            }
            this.renderer.drawFeature(
                feature.clone(),
                symbolizer
            );
        }
    },

    /**
     * Update the `symbolType` or `feature` and `symbolizer` and redraw
     * the feature.
     *
     * Valid options:
     *
     * @param options {Object} Object with properties to be updated.
     * @param options.feature {OpenLayers.Feature.Vector} The new or updated
     *     feature. If provided, the feature gets precedence over `symbolType`.
     * @param options.symbolType {String} One of the allowed `symbolType`
     *     values.
     * @param options.symbolizers {Object[]} An array of symbolizer objects.
     */
    update: function(options) {
        options = options || {};
        if(options.feature) {
            this.setFeature(options.feature, {
                draw: false
            });
        } else if(options.symbolType) {
            this.setSymbolType(options.symbolType, {
                draw: false
            });
        }
        if(options.symbolizers) {
            this.setSymbolizers(options.symbolizers, {
                draw: false
            });
        }
        this.drawFeature();
    },

    /**
     * Private method called during the destroy sequence.
     *
     * @private
     */
    beforeDestroy: function() {
        this.clearCustomEvents();
        if (this.renderer) {
            this.renderer.destroy();
        }
    }
});



/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires OpenLayers/Filter/Comparison.js
 * @requires OpenLayers/Filter/Logical.js
 * @requires GeoExt/Version.js
 */

/**
 * A set of useful static functions to work with forms.
 * 
 * @class GeoExt.Form
 * @singleton
 */

(function() {

    var FILTER_MAP = {
        "eq": OpenLayers.Filter.Comparison.EQUAL_TO,
        "ne": OpenLayers.Filter.Comparison.NOT_EQUAL_TO,
        "lt": OpenLayers.Filter.Comparison.LESS_THAN,
        "le": OpenLayers.Filter.Comparison.LESS_THAN_OR_EQUAL_TO,
        "gt": OpenLayers.Filter.Comparison.GREATER_THAN,
        "ge": OpenLayers.Filter.Comparison.GREATER_THAN_OR_EQUAL_TO,
        "like": OpenLayers.Filter.Comparison.LIKE
    };

    var REGEXES = {
        "text": new RegExp(
            "^(text|string)$", "i"
        ),
        "number": new RegExp(
            "^(number|float|decimal|double|int|long|integer|short)$", "i"
        ),
        "boolean": new RegExp(
            "^(boolean)$", "i"
        ),
        "date": new RegExp(
            "^(date|dateTime)$", "i"
        )
    };

    Ext.define('GeoExt.Form', {
                   
                            
          

        singleton: true,

        /**
         * Use `GeoExt.Form.ENDS_WITH` as the `wildcard` param to `#toFilter`
         * if you want wildcards to be prepended to LIKE field values.
         * 
         * @property {Number} ENDS_WITH
         */
        ENDS_WITH: 1,

        /**
         * Use `GeoExt.Form.STARTS_WITH` as the `wildcard` param to `#toFilter`
         * if you want wildcards to be appended to LIKE field values.
         * 
         * @property {Number} STARTS_WITH
         */
        STARTS_WITH: 2,

        /**
         * Use `GeoExt.Form.CONTAINS` as the `wildcard` param to `#toFilter`
         * if you want a wildcards to be both prepended and appended to LIKE
         * field values.
         * 
         * @property {Number} CONTAINS
         */
        CONTAINS: 3,

        /**
         * Create an `OpenLayers.Filter` object from an `Ext.form.Basic`
         * or an `Ext.form.Panel` instance.
         * 
         * @param {Ext.form.Form/Ext.form.Panel} form The form.
         * @param {String} logicalOp Either `OpenLayers.Filter.Logical.AND` or
         *     `OpenLayers.Filter.Logical.OR`. If null or undefined, we use 
         *     `OpenLayers.Filter.Logical.AND`
         * @param {Integer} wildcard Determines the wildcard behaviour of LIKE
         *     queries. See #ENDS_WITH, #STARTS_WITH and #CONTAINS.
         * @return `OpenLayers.Filter`
         */
        toFilter: function(form, logicalOp, wildcard) {
            if(form instanceof Ext.form.Panel) {
                form = form.getForm();
            }
            var filters = [], values = form.getValues(false);
            for(var prop in values) {
                var s = prop.split("__");

                var value = values[prop], type;

                if(s.length > 1 && 
                   (type = FILTER_MAP[s[1]]) !== undefined) {
                    prop = s[0];
                } else {
                    type = OpenLayers.Filter.Comparison.EQUAL_TO;
                }

                if (type === OpenLayers.Filter.Comparison.LIKE) {
                    switch(wildcard) {
                        case GeoExt.Form.ENDS_WITH:
                            value = '.*' + value;
                            break;
                        case GeoExt.Form.STARTS_WITH:
                            value += '.*';
                            break;
                        case GeoExt.Form.CONTAINS:
                            value = '.*' + value + '.*';
                            break;
                        default:
                            // do nothing, just take the value
                            break;
                    }
                }

                filters.push(
                    new OpenLayers.Filter.Comparison({
                        type: type,
                        value: value,
                        property: prop
                    })
                );
            }

            return filters.length == 1 &&
                        logicalOp != OpenLayers.Filter.Logical.NOT ?
                filters[0] :
                new OpenLayers.Filter.Logical({
                    type: logicalOp || OpenLayers.Filter.Logical.AND,
                    filters: filters
                });
        },

        /**
         * This function can be used to create an `Ext.form.Field` from
         * an `Ext.data.Model` object containing `name`, `type`,
         * `restriction` and `label` fields.
         *
         * @param {Ext.data.Model} record Typically from an Attribute Store.
         * @param {Object} options Optional object literal. Valid options:
         *
         * * `checkboxLabelProperty` - `String` The name of the property
         *       used to set the label in the checkbox. Only applies if the
         *       record is of the "boolean" type. Possible values are "boxLabel"
         *       and "fieldLabel". Default is "boxLabel".
         * * `mandatoryFieldLabelStyle` - `String` A CSS style specification
         *       string to apply to the field label if the field is not nillable
         *       (that is, the corresponding record has the "nillable" attribute
         *       set to `false`). Default is `"font-weight: bold;"`.
         * * `labelTpl` - `Ext.Template` or `String` or `Array` If set, the
         *       field label is obtained by applying the record's data hash to
         *       this  template. This allows for very customizable field labels. 
         *
         * See for instance :
         *
         *     var formPanel = Ext.create('GeoExt.Form', {
         *         autoScroll: true,
         *         attributeStore: store,
         *         recordToFieldOptions: {
         *             mandatoryFieldLabelStyle: 'font-style:italic;',
         *             labelTpl: new Ext.XTemplate(
         *                 '<span ext:qtip="{[this.getTip(values)]}">{name}</span>',
         *                 {
         *                     compiled: true,
         *                     disableFormats: true,
         *                     getTip: function(v) {
         *                         if (!v.type) {
         *                             return '';
         *                         }
         *                         var type = v.type.split(":").pop();
         *                         return OpenLayers.i18n(type) + 
         *                             (v.nillable ? '' : ' (required)');
         *                     }
         *                 }
         *             )
         *         }
         *     });
         *
         * @return `Object` An object literal with a xtype property, use
         *     `Ext.ComponentMgr.create` to create an `Ext.form.Field` from this
         *     object.
         */
        recordToField: function(record, options) {

            options = options || {};

            var type = record.get("type");
            if(typeof type === "object" && type.xtype) {
                // we have an xtype'd object literal in the type
                // field, just return it
                return type;
            }
            type = type.split(":").pop(); // remove ns prefix
            
            var field;
            var name = record.get("name");
            var restriction = record.get("restriction") || {};
            var nillable = record.get("nillable") || false;
            
            var label = record.get("label");
            var labelTpl = options.labelTpl;
            if (labelTpl) {
                var tpl = (labelTpl instanceof Ext.Template) ?
                    labelTpl :
                    new Ext.XTemplate(labelTpl);
                label = tpl.apply(record.data);
            } else if (label == null) {
                // use name for label if label isn't defined in the record
                label = name;
            }
            
            var baseOptions = {
                name: name,
                labelStyle: nillable ? '' : 
                                options.mandatoryFieldLabelStyle != null ? 
                                    options.mandatoryFieldLabelStyle : 
                                    'font-weight:bold;'
            };

            var r = REGEXES;

            if (restriction.enumeration) {
                field = Ext.apply({
                    xtype: "combo",
                    fieldLabel: label,
                    mode: "local",
                    forceSelection: true,
                    triggerAction: "all",
                    editable: false,
                    store: restriction.enumeration
                }, baseOptions);
            } else if(type.match(r["text"])) {
                var maxLength = restriction["maxLength"] !== undefined ?
                    parseFloat(restriction["maxLength"]) : undefined;
                var minLength = restriction["minLength"] !== undefined ?
                    parseFloat(restriction["minLength"]) : undefined;
                field = Ext.apply({
                    xtype: "textfield",
                    fieldLabel: label,
                    maxLength: maxLength,
                    minLength: minLength
                }, baseOptions);
            } else if(type.match(r["number"])) {
                var maxValue = restriction["maxInclusive"] !== undefined ?
                    parseFloat(restriction["maxInclusive"]) : undefined;
                var minValue = restriction["minInclusive"] !== undefined ?
                    parseFloat(restriction["minInclusive"]) : undefined;
                field = Ext.apply({
                    xtype: "numberfield",
                    fieldLabel: label,
                    maxValue: maxValue,
                    minValue: minValue
                }, baseOptions);
            } else if(type.match(r["boolean"])) {
                field = Ext.apply({
                    xtype: "checkbox"
                }, baseOptions);
                var labelProperty = options.checkboxLabelProperty || "boxLabel";
                field[labelProperty] = label;
            } else if(type.match(r["date"])) {
                field = Ext.apply({
                    xtype: "datefield",
                    fieldLabel: label,
                    format: 'c'
                }, baseOptions);
            }

            return field;
        }
    });
})();

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * The GeoExt.Lang singleton is created when the library is loaded.
 * Include all relevant language files after this file in your build.
 *
 * @class GeoExt.Lang
 */
Ext.define('GeoExt.Lang', {
    extend:  Ext.util.Observable ,
    singleton: true,
               
                        
      

    /**
     * The current language tag.  Use `#set` to set the locale. Defaults
     * to the browser language where available.
     *
     * @cfg {String}
     */
    locale: navigator.language || navigator.userLanguage,

    /**
     * Dictionary of string lookups per language.
     *
     * @property {Object}
     * @private
     */
    dict: null,

    /**
     * Construct the Lang singleton.
     *
     * @private
     */
    constructor: function() {
        this.addEvents(
            /**
             * Fires when localized strings are set.  Listeners will receive a
             * single `locale` event with the language tag.
             *
             * @event
             */
            "localize"
        );
        this.dict = {};
        this.callParent();
    },

    /**
     * Add translation strings to the dictionary.  This method can be called
     * multiple times with the same language tag (locale argument) to extend
     * a single dictionary.
     *
     * @param {String} locale A language tag that follows the "en-CA"
     *     convention (http://www.ietf.org/rfc/rfc3066.txt).
     * @param {Object} lookup An object with properties that are dot
     *     delimited names of objects with localizable strings (e.g.
     *     "GeoExt.VectorLegend.prototype").  The values for these properties
     *     are objects that will be used to extend the target objects with
     *     localized strings (e.g. {untitledPrefix: "Untitiled "})
     */
    add: function(locale, lookup) {
        var obj = this.dict[locale];
        if (!obj) {
            this.dict[locale] = Ext.apply({}, lookup);
        } else {
            for (var key in lookup) {
                obj[key] = Ext.apply(obj[key] || {}, lookup[key]);
            }
        }
        if (!locale || locale === this.locale) {
            this.set(locale);
        } else if (this.locale.indexOf(locale + "-") === 0) {
            // current locale is regional variation of added strings
            // call set so newly added strings are used where appropriate
            this.set(this.locale);
        }
    },

    /**
     * Set the language for all GeoExt components.  This will use any localized
     * strings in the dictionary (set with the `#add` method) that
     * correspond to the complete matching language tag or any "higher order"
     * tag (e.g. setting "en-CA" will use strings from the "en" dictionary if
     * matching strings are not found in the "en-CA" dictionary).
     *
     * @param {String} locale Language identifier tag following recommendations
     *     at http://www.ietf.org/rfc/rfc3066.txt.
     */
    set: function(locale) {
        // compile lookup based on primary and all subtags
        var tags = locale ? locale.split("-") : [];
        var id = "";
        var lookup = {}, parent, str, i, ii;
        for (i=0, ii=tags.length; i<ii; ++i) {
            id += (id && "-" || "") + tags[i];
            if (id in this.dict) {
                parent = this.dict[id];
                for (str in parent) {
                    if (str in lookup) {
                        Ext.apply(lookup[str], parent[str]);
                    } else {
                        lookup[str] = Ext.apply({}, parent[str]);
                    }
                }
            }
        }

        // now extend all objects given by dot delimited names in lookup
        for (str in lookup) {
            var obj = window;
            var parts = str.split(".");
            var missing = false;
            for (i=0, ii=parts.length; i<ii; ++i) {
                var name = parts[i];
                if (name in obj) {
                    obj = obj[name];
                } else {
                    missing = true;
                    break;
                }
            }
            if (!missing) {
                Ext.apply(obj, lookup[str]);
            }
        }
        this.locale = locale;
        this.fireEvent("localize", locale);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Util.js
 * @requires GeoExt/Version.js
 */

/**
 * The legend image.
 *
 * @class GeoExt.LegendImage
 */
Ext.define('GeoExt.LegendImage', {
    extend :  Ext.Component ,
    alias : 'widget.gx_legendimage',
               
                        
      
    /**
     * The url of the image to load.
     *
     * @cfg {String} url
     */
    url: null,

    /**
     * Path to image that will be used if the legend image fails
     * to load.  Default is Ext.BLANK_IMAGE_URL.
     *
     * @cfg {String} defaultImgSrc
     */
    defaultImgSrc: null,

    /**
     * Optional CSS class to apply to img tag.
     *
     * @cfg {String} imgCls
     */
    imgCls: null,

    /**
     * CSS class applied to img tag when no image is available or the default
     * image was loaded.
     * 
     * @cfg {String}
     */
    noImgCls: "gx-legend-noimage",

    initComponent: function(){
        var me = this;
        me.addEvents('legendimageloaded');
        me.callParent(arguments);
        if(this.defaultImgSrc === null) {
            this.defaultImgSrc = Ext.BLANK_IMAGE_URL;
        }
        this.autoEl = {
            tag: "img",
            "class": (this.imgCls ? this.imgCls + " " + this.noImgCls : this.noImgCls),
            src: this.defaultImgSrc
        };
    },

    /**
     * Sets the url of the legend image.
     *
     * @param {String} url The new URL.
     */
    setUrl: function(url) {
        this.url = url;
        var el = this.getEl();
        if (el) {
            el.un("load", this.onImageLoad, this);
            el.on("load", this.onImageLoad, this, {single: true});
            el.un("error", this.onImageLoadError, this);
            el.on("error", this.onImageLoadError, this, {
                single: true
            });
            el.dom.src = url;
        }
    },

    /**
     * Private method called when the legend image component is being rendered.
     *
     * @private
     */
    onRender: function(ct, position) {
        this.callParent(arguments);
        if(this.url) {
            this.setUrl(this.url);
        }
    },

    /**
     * Private method called during the destroy sequence.
     *
     * @private
     */
    onDestroy: function() {
        var el = this.getEl();
        if(el) {
            el.un("load", this.onImageLoad, this);
            el.un("error", this.onImageLoadError, this);
        }
        this.callParent();
    },

    /**
     * Private method called if the legend image fails loading.
     *
     * @private
     */
    onImageLoadError: function() {
        var el = this.getEl();
        el.addCls(this.noImgCls);
        el.dom.src = this.defaultImgSrc;
    },

    /**
     * Private method called after the legend image finished loading.
     *
     * @private
     */
    onImageLoad: function() {
        var el = this.getEl();
        if (!OpenLayers.Util.isEquivalentUrl(el.dom.src, this.defaultImgSrc)) {
            el.removeCls(this.noImgCls);
        }
        this.fireEvent('legendimageloaded', this);
    }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/**
 * A component encapsulating an OpenLayers.Control.OverviewMap control.
 *
 * When you use this component in an application, make sure to include the
 * stylesheet 'overviewmap.css' or add the following to your own stylesheet.
 *
 * <code>
 * .gx-overview-map .olControlOverviewMapElement { padding: 0; }
 * </code>
 *
 * @class GeoExt.OverviewMap
 */
Ext.define('GeoExt.OverviewMap', {
    extend:  Ext.Component ,
    alias: 'widget.gx_overviewmap',
               
                        
      

    /**
     * Custom CSS class added to this components #cls.
     *
     * @property {String}
     */
    ovCls: 'gx-overview-map',

    /**
     * The OpenLayers.Map that this overview is bound to. If not set by the user
     * a gx_mappanel's map will be guessed.
     *
     * @cfg {OpenLayers.Map}
     */
    map: null,

    /**
     * If set to true the overview will be reinitialized on "baselayerchange"
     * events of its bound map.
     * This can be used to make sure that the overview shows the same baselayer
     * as the map.
     *
     * @cfg {Boolean}
     */
    dynamic: false,

    /**
     * The overview options that the underlying OpenLayers.Control.OverviewMap
     * will be initialized with. Following settings are defaults and should
     * generally not be overridden:
     *
     * - "div" configuration will default to the containers DOM element
     * - "size" will default to the containers actual dimensions
     * - "maximized" will always be true to make the overview visible
     *
     * If you want to hide the overview map, simple use the components show/hide
     * methods.
     *
     * @cfg {Object}
     */
    overviewOptions: null,

    /**
     * Reference to the OpenLayers.Control.OverviewMap control.
     *
     * @property @readonly {OpenLayers.Control.OverviewMap}
     */
    ctrl: null,

    initComponent: function() {
        if (!this.map) {
            this.map = GeoExt.panel.Map.guess().map;
        }

        // add gx class making sure it won't be overridden on accident
        this.addCls(this.ovCls);

        // bind to the components lifecycle events to make sure the overview is
        // added and removed from the map when the component is (in-)visible.
        this.on({
            'show': this.reinitControl,
            'resize': this.reinitControl,
            'hide': this.destroyControl,
            scope: this
        });

        if (this.dynamic) {
            this.map.events.on({
                changebaselayer: this.reinitControl,
                scope: this
            });
        }

        this.callParent();
    },

    /**
     * Destroys the encapsulated OpenLayers.Control.OverviewMap removing it from
     * the map controls and unbinds all events from this component.
     * Deletes the components ctrl, map and overviewOptions members.
     *
     * @private
     */
    destroy: function() {
        this.destroyControl();

        this.un({
            'show': this.reinitControl,
            'resize': this.reinitControl,
            'hide': this.destroyControl,
            scope: this
        });

        this.map.events.un({
            changebaselayer: this.onChangeBaseLayer,
            scope: this
        });

        delete this.ctrl;
        delete this.map;
        delete this.overviewOptions;

        this.callParent(arguments);
    },

    /**
     * Helper method that refers to the private initControl and destroyControl
     * methods to force an update of the overview map by bluntly creating a new one.
     * This can be called to update the map after setting new #overviewOptions.
     */
    reinitControl: function() {
        this.destroyControl();
        this.initControl();
    },

    /**
     * Initializes an OpenLayers.Control.OverviewMap control adding it to the
     * configured map.
     *
     * @private
     */
    initControl: function() {
        var map = this.map,
            size = this.getSize(),
            options = Ext.apply({
                div: this.getEl().dom,
                size: new OpenLayers.Size(size.width, size.height),
                maximized: true
            }, this.overviewOptions),
            baselayer;

        // If map is configured with allOverlays as true and layers option is
        // not set by user, the overview control will fail on construction.
        // This is to determine any layer to be shown in overview map.
        if (map.allOverlays) {
            if (!options.layers && map.layers && map.layers.length > 0) {
                baselayer = map.layers[0].clone();
                baselayer.setIsBaseLayer(true);
                options.layers = [ baselayer ];
            }
        }

        this.ctrl = new OpenLayers.Control.OverviewMap(options);
        map.addControl(this.ctrl);
    },

    /**
     * Destroys the OpenLayers.Control.OverviewMap control after removing it
     * from this components bound map.
     *
     * @private
     */
    destroyControl: function() {
        if (this.ctrl && (this.ctrl instanceof OpenLayers.Control.OverviewMap)) {
            this.map.removeControl(this.ctrl);
            this.ctrl.destroy();
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * Base class for components of GeoExt.panel.Legend.
 *
 * @class GeoExt.container.LayerLegend
 */
Ext.define('GeoExt.container.LayerLegend', {
    extend :  Ext.container.Container ,
               
                         
                        
      
    alias : 'widget.gx_layerlegend',
    alternateClassName : 'GeoExt.LayerLegend',

    statics : {
        /**
         * Gets an array of legend xtypes that support the provided layer
         * record, with optionally provided preferred types listed first.
         *
         * @param {GeoExt.data.LayerRecord} layerRecord A layer record to get
         *     legend types for. If not provided, all registered types will be
         *     returned.
         * @param {Array} preferredTypes Types that should be considered.
         *     first. If not provided, all registered legend types will be
         *     returned in the order of their score for support of the provided
         *     layerRecord.
         * @return {Array} xtypes of legend types that can be used with
         *     the provided layerRecord.
         */
        getTypes: function(layerRecord, preferredTypes) {
            var types = (preferredTypes || []).concat(),
                scoredTypes = [], score, type;
            for (type in this.types) {
                score = this.types[type].supports(layerRecord);
                if(score > 0) {
                    // add to scoredTypes if not preferred
                    if (Ext.Array.indexOf(types, type) == -1) {
                        scoredTypes.push({
                            type: type,
                            score: score
                        });
                    }
                } else {
                    // preferred, but not supported
                    Ext.Array.remove(types, type);
                }
            }
            scoredTypes.sort(function(a, b) {
                return a.score < b.score ? 1 : (a.score == b.score ? 0 : -1);
            });
            var len = scoredTypes.length, goodTypes = new Array(len);
            for (var i=0; i<len; ++i) {
                goodTypes[i] = scoredTypes[i].type;
            }
            // take the remaining preferred types, and add other good types
            return types.concat(goodTypes);
        },
        /**
         * Checks whether this legend type supports the provided layerRecord.
         *
         * @param {GeoExt.data.LayerRecord} layerRecord The layer record
         *     to check support for.
         * @return {Integer} score indicating how good the legend supports the
         *     provided record. 0 means not supported.
         */
        supports: function(layerRecord) {
            // to be implemented by subclasses
        },
        /**
         * An object containing a name-class mapping of LayerLegend subclasses.
         * To register as LayerLegend, a subclass should add itself to this
         * object:
         *
         *     Ext.define('GeoExt.container.WmsLegend', {
         *         extend: 'GeoExt.container.LayerLegend'
         *         // ...
         *     }, function() {
         *         GeoExt.container.LayerLegend.types["gx_wmslegend"] =
         *             GeoExt.container.WmsLegend;
         *     });
         *
         * @cfg {Object}
         */
        types: {}
    },

    /**
     * The layer record for the legend
     *
     * @cfg {GeoExt.data.LayerRecord}
     */
    layerRecord: null,

    /**
     * Whether or not to show the title of a layer. This can be overridden
     * on the #layerStore record using the hideTitle property.
     *
     * @cfg {Boolean}
     */
    showTitle: true,

    /**
     * Optional title to be displayed instead of the layer title.  If this is
     * set, the value of `#showTitle` will be ignored (assumed to be true).
     *
     * @cfg {String}
     */
    legendTitle: null,

    /**
     * Optional CSS class to use for the layer title labels.
     *
     * @cfg {String}
     */
    labelCls: null,

    /**
     * @property layerStore {GeoExt.data.LayerStore}
     * @private
     */
    layerStore: null,

    /**
     * Initializes the LayerLegend component.
     */
    initComponent: function(){
        var me = this;
        me.callParent(arguments);
        me.autoEl = {};
        me.add({
            xtype: "label",
            html: this.getLayerTitle(this.layerRecord),
            cls: 'x-form-item x-form-item-label' +
            (this.labelCls ? ' ' + this.labelCls : '')
        });
        if (me.layerRecord && me.layerRecord.store) {
            me.layerStore = me.layerRecord.store;
            me.layerStore.on("update", me.onStoreUpdate, me);
            me.layerStore.on("add", me.onStoreAdd, me);
            me.layerStore.on("remove", me.onStoreRemove, me);
        }
    },

    /**
     * Get the label text of the legend.
     *
     * @private
     * @return {String}
     */
    getLabel: function() {
        var label = this.items.get(0);
        return label.rendered ? label.el.dom.innerHTML : label.html;
    },

    /**
     * Handler for remove event of the layerStore.
     *
     * @param {Ext.data.Store} store The store from which the record was
     *     removed.
     * @param {Ext.data.Record} record The record object corresponding
     *     to the removed layer.
     * @param {Integer} index The index in the store at which the record
     *     was remvoed.
     * @private
     */
    onStoreRemove: function(store, record, index) {
        // to be implemented by subclasses if needed
    },

    /**
     * Handler for add event of the layerStore.
     *
     * @param {Ext.data.Store} store The store to which the record was
     *     added.
     * @param {Ext.data.Record} record The record object corresponding
     *     to the added layer.
     * @param {Integer} index The index in the store at which the record
     *     was added.
     * @private
     */
    onStoreAdd: function(store, record, index) {
        // to be implemented by subclasses if needed
    },

    /**
     * Updates the legend. Gets called when the store fires the update event.
     * This usually means the visibility of the layer, its style or title
     * has changed.
     *
     * @param {Ext.data.Store} store The store in which the record was
     *     changed.
     * @param {Ext.data.Record} record The record object corresponding
     *     to the updated layer.
     * @param {String} operation The type of operation.
     * @private
     */
    onStoreUpdate: function(store, record, operation) {
        // if we don't have items, we are already awaiting garbage
        // collection after being removed by LegendPanel::removeLegend, and
        // updating will cause errors
        if (record === this.layerRecord && this.items.getCount() > 0) {
            var layer = record.getLayer();
            this.setVisible(layer.getVisibility() &&
                layer.calculateInRange() && layer.displayInLayerSwitcher &&
                !record.get('hideInLegend'));
            this.update();
        }
    },

    /**
     * Updates the legend.
     *
     * @private
     */
    update: function() {
        var title = this.getLayerTitle(this.layerRecord);
        var item = this.items.get(0);
        if (item instanceof Ext.form.Label && this.getLabel() !== title) {
            // we need to update the title
            item.setText(title, false);
        }
    },

    /**
     * Get a title for the layer. If the record doesn't have a title, the
     * name will be returned.
     *
     * @param {GeoExt.data.LayerRecord} record
     * @return {String} The title of the layer.
     * @private
     */
    getLayerTitle: function(record) {
        var title = this.legendTitle || "";
        if (this.showTitle && !title) {
            if (record && !record.get("hideTitle")) {
                title = record.get("title") ||
                record.get("name") ||
                record.getLayer().name || "";
            }
        }
        return title;
    },

    /**
     * Unbinds event listeners prior to destroying.
     *
     * @private
     */
    beforeDestroy: function() {
        if (this.layerStore) {
            this.layerStore.un("update", this.onStoreUpdate, this);
            this.layerStore.un("remove", this.onStoreRemove, this);
            this.layerStore.un("add", this.onStoreAdd, this);
        }
        this.callParent();
    },

    /**
     * Nullifies members #layerRecord and #layerStore when the legend is being
     * destroyed.
     *
     * @private
     */
    onDestroy: function() {
        this.layerRecord = null;
        this.layerStore = null;
        this.callParent(arguments);
    }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/container/LayerLegend.js
 * @requires GeoExt/LegendImage.js
 */

/**
 * Show a legend image in a BoxComponent and make sure load errors are
 * dealt with.
 *
 * @class GeoExt.container.UrlLegend
 */
Ext.define('GeoExt.container.UrlLegend', {
    extend :  GeoExt.container.LayerLegend ,
                                     
    alias: 'widget.gx_urllegend',
    alternateClassName: 'GeoExt.UrlLegend',

    statics : {
        /**
         * Checks whether the given layer record supports an URL legend.
         *
         * @param {Geoext.data.LayerModel} layerRecord A layer record.
         * @return {Number} Either `10` when URL legends are supported or `0`.
         */
        supports: function(layerRecord) {
            return Ext.isEmpty(layerRecord.get("legendURL")) ? 0 : 10;
        }
    },

    /**
     * The WMS spec does not say if the first style advertised for a layer in
     * a Capabilities document is the default style that the layer is
     * rendered with. We make this assumption by default. To be strictly WMS
     * compliant, set this to false, but make sure to configure a STYLES
     * param with your WMS layers, otherwise LegendURLs advertised in the
     * GetCapabilities document cannot be used.
     *
     * @cfg {Boolean}
     */
    defaultStyleIsFirst: true,

    /**
     * Should we use the optional `SCALE` parameter in the SLD WMS
     * GetLegendGraphic request?
     *
     * @cfg {Boolean}
     */
    useScaleParameter: true,

    /**
     * Optional parameters to add to the legend url, this can e.g. be used to
     * support vendor-specific parameters in a SLD WMS GetLegendGraphic
     * request. To override the default MIME type of image/gif use the
     * FORMAT parameter in baseParams.
     *
     * Example:
     *
     *     var legendPanel = Ext.create('GeoExt.panel.Legend', {
     *         map: map,
     *         title: 'Legend Panel',
     *         defaults: {
     *             style: 'padding:5px',
     *             baseParams: {
     *                 LEGEND_OPTIONS: 'forceLabels:on'
     *             }
     *         }
     *     });
     *
     * @cfg {Object}
     */
    baseParams: null,

    /**
     * Initializes this UrlLegend.
     */
    initComponent: function(){
        var me = this;
        me.addEvents('legendimageloaded');
        me.callParent(arguments);
        this.add(Ext.create('GeoExt.LegendImage', {
            url: this.layerRecord.get("legendURL"),
            listeners: {
                'legendimageloaded': function() {
                    this.fireEvent('legendimageloaded', this);
                },
                scope: this
            }
        }));
    },

    /**
     * Update the legend, adding, removing or updating
     * the per-sublayer box component.
     *
     * @private
     */
    update: function() {
        this.callParent(arguments);
        this.items.get(1).setUrl(this.layerRecord.get("legendURL"));
    }
}, function() {
    GeoExt.container.LayerLegend.types["gx_urllegend"] =
        GeoExt.container.UrlLegend;
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/container/LayerLegend.js
 * @requires GeoExt/FeatureRenderer.js
 */

/**
 * Create a vector legend.
 *
 * @class GeoExt.container.VectorLegend
 */
Ext.define('GeoExt.container.VectorLegend', {
    extend:  GeoExt.container.LayerLegend ,
    alias: 'widget.gx_vectorlegend',
               
                                      
                                
      
    alternateClassName: 'GeoExt.VectorLegend',

    statics: {
        /**
         * Checks whether the given layer record supports a vector legend.
         *
         * @param {GeoExt.data.LayerRecord} layerRecord Record containing a
         *     vector layer.
         * @return {Number} Either `1` when vector legends are supported or `0`.
         */
        supports: function(layerRecord) {
            return layerRecord.getLayer() instanceof OpenLayers.Layer.Vector ? 1 : 0;
        }
    },

    /**
     * The record containing a vector layer that this legend will be based on.
     * One of `#layerRecord`, `#layer`,  or `#rules` must be specified in
     * the config.
     *
     * @cfg {GeoExt.data.LayerRecord}
     */
    layerRecord: null,

    /**
     * The layer that this legend will be based on.  One of `#layer`,
     * `#rules`, or `#layerRecord` must be specified in the config.
     *
     * @cfg {OpenLayers.Layer.Vector}
     */
    layer: null,

    /**
     * List of rules.  One of `#rules`, `#layer`, or `#layerRecord` must be
     * specified in the config.  The `#symbolType` property must also be
     * provided if only `#rules` are given in the config.
     *
     * @cfg {Array}
     */
    rules: null,

    /**
     * The symbol type for legend swatches.  Must be one of `"Point"`, `"Line"`,
     * or `"Polygon"`.  If not provided, the `#layer` or `#layerRecord` config
     * property must be specified, and the geometry type of the first feature
     * found on the layer will be used. If a rule does not have a symbolizer for
     * `#symbolType`, we look at the symbolizers for the rule, and see if it has
     * a `"Point"`, `"Line"` or `"Polygon"` symbolizer, which we use for
     * rendering a swatch of the respective geometry type.
     *
     * @cfg {String}
     */
    symbolType: null,

    /**
     * The prefix to use as a title for rules with no title or name. Prefix will
     * be appended with a number that corresponds to the index of the rule (1
     * for first rule).
     *
     * @cfg {String}
     */
    untitledPrefix: "Untitled ",

    /**
     * Set cursor style to "pointer" for symbolizers.  Register for the
     * `#symbolclick` event to handle clicks.  Note that click events are fired
     * regardless of this value.  If `false`, no cursor style will be set.
     *
     * @cfg {Boolean}
     */
    clickableSymbol: false,

    /**
     * Set cursor style to "pointer" for rule titles.  Register for the
     * `#titleclick` event to handle clicks.  Note that click events are fired
     * regardless of this value.  If `false`, no cursor style will be set.
     *
     * @cfg {Boolean}
     */
    clickableTitle: false,

    /**
     * Set to true if a rule should be selected by clicking on the symbol or
     * title. Selection will trigger the `#ruleselected` event, and a click on
     * a selected rule will unselect it and trigger the `#ruleunselected` event.
     *
     * @cfg {Boolean}
     */
    selectOnClick: false,

    /**
     * Allow drag and drop of rules.
     *
     * @cfg {Boolean}
     */
    enableDD: false,

    /**
     * Show a border around the legend panel.
     *
     * @cfg {Boolean}
     */
    bodyBorder: false,

    /**
     * Cached feature for rendering.
     *
     * @property {OpenLayers.Feature.Vector}
     * @private
     */
    feature: null,

    /**
     * The rule that is currently selected.
     *
     * @property {OpenLayers.Rule}
     * @private
     */
    selectedRule: null,

    /**
     * The current scale denominator of any map associated with this legend. Use
     * `#setCurrentScaleDenominator` to change this.  If not set, an entry for
     * each rule will be rendered.  If set, only rules that apply for the given
     * scale will be rendered.
     *
     * @property {Number}
     * @private
     */
    currentScaleDenominator: null,

    /**
     * Initializes this VectorLegend.
     */
    initComponent: function(){
        var me = this;
        me.callParent();

        if (this.layerRecord) {
            this.layer = this.layerRecord.getLayer();
            if (this.layer.map) {
                this.map = this.layer.map;
                this.currentScaleDenominator = this.layer.map.getScale();
                this.layer.map.events.on({
                    "zoomend": this.onMapZoom,
                    scope: this
                });
            }
        }

        // determine symbol type
        if (!this.symbolType) {
            if (this.feature) {
                this.symbolType = this.symbolTypeFromFeature(this.feature);
            } else if (this.layer) {
                if (this.layer.features.length > 0) {
                    var feature = this.layer.features[0].clone();
                    feature.attributes = {};
                    this.feature = feature;
                    this.symbolType = this.symbolTypeFromFeature(this.feature);
                } else {
                    this.layer.events.on({
                        featuresadded: this.onFeaturesAdded,
                        scope: this
                    });
                }
            }
        }

        // set rules if not provided
        if (this.layer && this.feature && !this.rules) {
            this.setRules();
        }

        this.rulesContainer = new Ext.container.Container({
            autoEl: {}
        });

        this.add(this.rulesContainer);

        this.addEvents(
            /**
             * Fires when a rule title is clicked.
             *
             * @event titleclick
             * @param {GeoExt.VectorLegend} comp This component.
             * @param {OpenLayers.Rule} rule The rule whose title was clicked.
             */
            "titleclick",

            /**
             * Fires when a rule symbolizer is clicked.
             *
             * @event symbolclick
             * @param {GeoExt.VectorLegend} comp This component.
             * @param {OpenLayers.Rule} rule The rule whose symbol was clicked.
             */
            "symbolclick",

            /**
             * Fires when a rule entry is clicked (fired with symbolizer or
             * title click).
             *
             * @event ruleclick
             * @param {GeoExt.VectorLegend} comp This component.
             * @param {OpenLayers.Rule} rule The rule that was clicked.
             */
            "ruleclick",

            /**
             * Fires when a rule is clicked and `selectOnClick` is set to
             * `true`.
             *
             * @event ruleselected
             * @param {GeoExt.VectorLegend} comp This component.
             * @param {OpenLayers.Rule} rule The rule that was selected.
             */
            "ruleselected",

            /**
             * Fires when the selected rule is clicked and `#selectOnClick`
             * is set to `true`, or when a rule is unselected by selecting a
             * different one.
             *
             * @event ruleunselected
             * @param {GeoExt.VectorLegend} comp This component.
             * @param {OpenLayers.Rule} rule The rule that was unselected.
             */
            "ruleunselected",

            /**
             * Fires when a rule is moved.
             *
             * @event rulemoved
             * @param {GeoExt.VectorLegend} comp This component.
             * @param {OpenLayers.Rule} rule The rule that was moved.
             */
            "rulemoved"
        );

        this.update();

    },

   /**
    * Listener for map zoomend.
    *
    * @private
    */
    onMapZoom: function() {
        this.setCurrentScaleDenominator(
            this.layer.map.getScale()
        );
    },

    /**
     * Determine the symbol type given a feature.
     *
     * @param {OpenLayers.Feature.Vector} feature
     * @private
     */
    symbolTypeFromFeature: function(feature) {
        var match = feature.geometry.CLASS_NAME.match(/Point|Line|Polygon/);
        return (match && match[0]) || "Point";
    },

    /**
     * Set as a one time listener for the `featuresadded` event on the layer if
     * it was provided with no features originally.
     *
     * @private
     */
    onFeaturesAdded: function() {
        this.layer.events.un({
            featuresadded: this.onFeaturesAdded,
            scope: this
        });
        var feature = this.layer.features[0].clone();
        feature.attributes = {};
        this.feature = feature;
        this.symbolType = this.symbolTypeFromFeature(this.feature);
        if (!this.rules) {
            this.setRules();
        }
        this.update();
    },

    /**
     * Sets the `#rules` property for this.  This is called when the component
     * is constructed without rules.  Rules will be derived from the layer's
     * style map if it has one.
     *
     * @private
     */
    setRules: function() {
        var style = this.layer.styleMap && this.layer.styleMap.styles["default"];
        if (!style) {
            style = new OpenLayers.Style();
        }
        if (style.rules.length === 0) {
            this.rules = [
                new OpenLayers.Rule({
                    title: style.title,
                    symbolizer: style.createSymbolizer(this.feature)
                })
            ];
        } else {
            this.rules = style.rules;
        }
    },

    /**
     * Set the current scale denominator. This will hide entries for any rules
     * that don't apply at the current scale.
     *
     * @param {Number} scale The scale denominator.
     */
    setCurrentScaleDenominator: function(scale) {
        if (scale !== this.currentScaleDenominator) {
            this.currentScaleDenominator = scale;
            this.update();
        }
    },

    /**
     * Get the item corresponding to the rule.
     *
     * @param {OpenLayers.Rule} rule
     * @return {Ext.Container}
     * @private
     */
    getRuleEntry: function(rule) {
        var idxOfRule = Ext.Array.indexOf(this.rules, rule);
        return this.rulesContainer.items.get(idxOfRule);
    },

    /**
     * Add a new rule entry in the rules container. This method does not add the
     * rule to the rules array.
     *
     * @param {OpenLayers.Rule} rule The rule to add.
     * @param {Boolean} noDoLayout Don't call doLayout after adding rule.
     * @private
     */
    addRuleEntry: function(rule, noDoLayout) {
        this.rulesContainer.add(this.createRuleEntry(rule));
        if (!noDoLayout) {
            this.doLayout();
        }
    },

    /**
     * Remove a rule entry from the rules container, this method assumes the
     * rule is in the rules array, and it does not remove the rule from the
     * rules array.
     *
     * @param {OpenLayers.Rule} rule The rule to remove.
     * @param {Boolean} noDoLayout Don't call doLayout after removing rule.
     * @private
     */
    removeRuleEntry: function(rule, noDoLayout) {
        var ruleEntry = this.getRuleEntry(rule);
        if (ruleEntry) {
            this.rulesContainer.remove(ruleEntry);
            if (!noDoLayout) {
                this.doLayout();
            }
        }
    },

    /**
     * Selects a rule entry by adding a CSS class.
     *
     * Fires the #ruleselected event.
     *
     * @param {OpenLayers.Rule} rule The rule whose entry shall be selected.
     * @private
     */
    selectRuleEntry: function(rule) {
        var newSelection = rule != this.selectedRule;
        if (this.selectedRule) {
            this.unselect();
        }
        if (newSelection) {
            var ruleEntry = this.getRuleEntry(rule);
            ruleEntry.body.addCls("x-boundlist-selected");
            this.selectedRule = rule;
            this.fireEvent("ruleselected", this, rule);
        }
    },

    /**
     * Unselects all rule entries by removing a CSS class.
     *
     * Fires the #ruleunselected event for every rule item.
     *
     * @private
     */
    unselect: function() {
        this.rulesContainer.items.each(function(item, i) {
            if (this.rules[i] == this.selectedRule) {
                item.body.removeCls("x-boundlist-selected");
                this.selectedRule = null;
                this.fireEvent("ruleunselected", this, this.rules[i]);
            }
        }, this);
    },

    /**
     * Creates the rule entry for the given OpenLayers.Rule and bind appropriate
     * event listeners to select rule entries on click (see #selectRuleEntry).
     *
     * @param {OpenLayers.Rule} rule
     * @return {Ext.panel.Panel}
     * @private
     */
    createRuleEntry: function(rule) {
        var applies = true;
        if (this.currentScaleDenominator != null) {
            if (rule.minScaleDenominator) {
                applies = applies && (this.currentScaleDenominator >= rule.minScaleDenominator);
            }
            if (rule.maxScaleDenominator) {
                applies = applies && (this.currentScaleDenominator < rule.maxScaleDenominator);
            }
        }
        return {
            xtype: "panel",
            layout: "column",
            border: false,
            hidden: !applies,
            bodyStyle: this.selectOnClick ? {cursor: "pointer"} : undefined,
            defaults: {
                border: false
            },
            items: [
                this.createRuleRenderer(rule),
                this.createRuleTitle(rule)
            ],
            listeners: {
                render: function(comp){
                    this.selectOnClick && comp.getEl().on({
                        click: function(comp){
                            this.selectRuleEntry(rule);
                        },
                        scope: this
                    });
                    if (this.enableDD == true) {
                        this.addDD(comp);
                    }
                },
                scope: this
            }
        };
    },

    /**
     * Create a renderer for the rule.
     *
     * @param {OpenLayers.Rule} rule
     * @return {GeoExt.FeatureRenderer}
     * @private
     */
    createRuleRenderer: function(rule) {
        var types = [this.symbolType, "Point", "Line", "Polygon"];
        var type, haveType;
        var symbolizers = rule.symbolizers;
        var i, len;
        if (!symbolizers) {
            // TODO: remove this when OpenLayers.Symbolizer is used everywhere
            var symbolizer = rule.symbolizer;
            for (i=0, len=types.length; i<len; ++i) {
                type = types[i];
                if (symbolizer[type]) {
                    symbolizer = symbolizer[type];
                    haveType = true;
                    break;
                }
            }
            symbolizers = [symbolizer];
        } else {
            var Type;
            outer: for (i=0, ii=types.length; i<ii; ++i) {
                type = types[i];
                Type = OpenLayers.Symbolizer[type];
                if (Type) {
                    for (var j=0, jj=symbolizers.length; j<jj; ++j) {
                        if (symbolizers[j] instanceof Type) {
                            haveType = true;
                            break outer;
                        }
                    }
                }
            }
        }
        return {
            xtype: "gx_renderer",
            symbolType: haveType ? type : this.symbolType,
            symbolizers: symbolizers,
            style: this.clickableSymbol ? {cursor: "pointer"} : undefined,
            listeners: {
                click: function() {
                    if (this.clickableSymbol) {
                        this.fireEvent("symbolclick", this, rule);
                        this.fireEvent("ruleclick", this, rule);
                    }
                },
                scope: this
            }
        };
    },

    /**
     * Create a title component for the rule.
     *
     * @param {OpenLayers.Rule} rule
     * @return {Ext.Component}
     * @private
     */
    createRuleTitle: function(rule) {
        return {
            cls: "x-form-item",
            style: "padding: 0.2em 0.5em 0;", // TODO: css
            bodyStyle: Ext.applyIf({background: "transparent"},
                this.clickableTitle ? {cursor: "pointer"} : undefined),
            html: this.getRuleTitle(rule),
            listeners: {
                render: function(comp) {
                    this.clickableTitle && comp.getEl().on({
                        click: function() {
                            this.fireEvent("titleclick", this, rule);
                            this.fireEvent("ruleclick", this, rule);
                        },
                        scope: this
                    });
                },
                scope: this
            }
        };
    },

    /**
     * Adds drag & drop functionality to a rule entry.
     *
     * @param {Ext.Component} component
     * @private
     */
    addDD: function(component) {
        var ct = component.ownerCt;
        var panel = this;
        new Ext.dd.DragSource(component.getEl(), {
            ddGroup: ct.id,
            onDragOut: function(e, targetId) {
                var target = Ext.getCmp(targetId);
                target.removeCls("gx-ruledrag-insert-above");
                target.removeCls("gx-ruledrag-insert-below");
                return Ext.dd.DragZone.prototype.onDragOut.apply(this, arguments);
            },
            onDragEnter: function(e, targetId) {
                var target = Ext.getCmp(targetId);
                var cls;
                var sourcePos = Ext.Array.indexOf(ct.items, component);
                var targetPos = Ext.Array.indexOf(ct.items, target);
                if (sourcePos > targetPos) {
                    cls = "gx-ruledrag-insert-above";
                } else if (sourcePos < targetPos) {
                    cls = "gx-ruledrag-insert-below";
                }
                cls && target.addCls(cls);
                return Ext.dd.DragZone.prototype.onDragEnter.apply(this, arguments);
            },
            onDragDrop: function(e, targetId) {
                var indexOf = Ext.Array.indexOf,
                    idxOfComp = indexOf(ct.items, component),
                    idxOfTarget = indexOf(ct.items, Ext.getCmp(targetId));
                panel.moveRule(idxOfComp, idxOfTarget);
                return Ext.dd.DragZone.prototype.onDragDrop.apply(this, arguments);
            },
            getDragData: function(e) {
                var sourceEl = e.getTarget(".x-column-inner");
                if(sourceEl) {
                    var d = sourceEl.cloneNode(true);
                    d.id = Ext.id();
                    return {
                        sourceEl: sourceEl,
                        repairXY: Ext.fly(sourceEl).getXY(),
                        ddel: d
                    };
                }
            }
        });
        new Ext.dd.DropTarget(component.getEl(), {
            ddGroup: ct.id,
            notifyDrop: function() {
                return true;
            }
        });
    },

    /**
     * Update rule titles and symbolizers.
     */
    update: function() {
        this.callParent(arguments);
        if (this.symbolType && this.rules) {
            this.rulesContainer.removeAll(false);
            for (var i=0, ii=this.rules.length; i<ii; ++i) {
                this.addRuleEntry(this.rules[i], true);
            }
            this.doLayout();
            // make sure that the selected rule is still selected after update
            if (this.selectedRule) {
                this.getRuleEntry(this.selectedRule).body.addCls("x-boundlist-selected");
            }
        }
    },

    /**
     * Update the renderer and the title of a rule.
     *
     * @param {OpenLayers.Rule} rule
     * @private
     */
    updateRuleEntry: function(rule) {
        var ruleEntry = this.getRuleEntry(rule);
        if (ruleEntry) {
            ruleEntry.removeAll();
            ruleEntry.add(this.createRuleRenderer(rule));
            ruleEntry.add(this.createRuleTitle(rule));
            ruleEntry.doLayout();
        }
    },

    /**
     * Called while dragging/dropping. Moves the rule specified by sourcePos to
     * targetPos and fires the rulemoved event.
     *
     * @private
     */
    moveRule: function(sourcePos, targetPos) {
        var srcRule = this.rules[sourcePos];
        this.rules.splice(sourcePos, 1);
        this.rules.splice(targetPos, 0, srcRule);
        this.update();
        this.fireEvent("rulemoved", this, srcRule);
    },

    /**
     * Get a rule title by a rule-object.
     *
     * @return {String}
     * @private
     */
    getRuleTitle: function(rule) {
        var title = rule.title || rule.name || "";
        if (!title && this.untitledPrefix) {
            title = this.untitledPrefix + (Ext.Array.indexOf(this.rules, rule) + 1);
        }
        return title;
    },

    /**
     * Unbind various event listeners and deletes #layer, #map and #rules
     * properties.
     */
    beforeDestroy: function() {
        if (this.layer) {
            if (this.layer.events) {
                this.layer.events.un({
                    featuresadded: this.onFeaturesAdded,
                    scope: this
                });
            }
            if (this.layer.map && this.layer.map.events) {
                this.layer.map.events.un({
                    "zoomend": this.onMapZoom,
                    scope: this
                });
            }
        }
        delete this.layer;
        delete this.map;
        delete this.rules;
        this.callParent(arguments);
    },

    /**
     * Handler for remove event of the layerStore.
     *
     * @param {Ext.data.Store} store The store from which the record was
     *     removed.
     * @param {Ext.data.Record} record The record object corresponding
     *     to the removed layer.
     * @param {Integer} index The index in the store.
     * @private
     */
    onStoreRemove: function(store, record, index) {
        if (record.getLayer() === this.layer) {
            if (this.map && this.map.events) {
                this.map.events.un({
                    "zoomend": this.onMapZoom,
                    scope: this
                });
            }
        }
    },

    /**
     * Handler for add event of the layerStore.
     *
     * @param {Ext.data.Store} store The store to which the record was
     *     added.
     * @param {Ext.data.Record[]} records The record object(s) corresponding
     *     to the added layer(s).
     * @param {Integer} index The index in the store at which the record
     *     was added.
     * @private
     */
    onStoreAdd: function(store, records, index) {
        for (var i=0, len=records.length; i<len; i++) {
            var record = records[i];
            if (record.getLayer() === this.layer) {
                if (this.layer.map && this.layer.map.events) {
                    this.layer.map.events.on({
                        "zoomend": this.onMapZoom,
                        scope: this
                    });
                }
            }
       }
    }

}, function() {
    GeoExt.container.LayerLegend.types["gx_vectorlegend"] =
        GeoExt.container.VectorLegend;
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Layer/WMS.js
 * @include OpenLayers/Util.js
 * @requires GeoExt/container/LayerLegend.js
 * @requires GeoExt/LegendImage.js
 */

/**
 * Show a legend image for a WMS layer. The image can be read from the styles
 * field of a layer record (if the record comes e.g. from a
 * GeoExt.data.WMSCapabilitiesReader). If not provided, a
 * GetLegendGraphic request will be issued to retrieve the image.
 *
 * @class GeoExt.container.WmsLegend
 */
Ext.define('GeoExt.container.WmsLegend', {
    extend:  GeoExt.container.LayerLegend ,
    alias: 'widget.gx_wmslegend',
                                     
    alternateClassName: 'GeoExt.WMSLegend',

    statics: {
        /**
         * Checks whether the given layer record supports an URL legend.
         *
         * @param {GeoExt.data.LayerRecord} layerRecord Record containing a
         *     WMS layer.
         * @return {Number} Either `1` when WMS legends are supported or `0`.
         */
        supports: function(layerRecord) {
            return layerRecord.getLayer() instanceof OpenLayers.Layer.WMS ? 1 : 0;
        }
    },

    /**
     * The WMS spec does not say if the first style advertised for a layer in
     * a Capabilities document is the default style that the layer is
     * rendered with. We make this assumption by default. To be strictly WMS
     * compliant, set this to false, but make sure to configure a STYLES
     * param with your WMS layers, otherwise LegendURLs advertised in the
     * GetCapabilities document cannot be used.
     *
     * @cfg {Boolean}
     */
    defaultStyleIsFirst: true,

    /**
     * Should we use the optional SCALE parameter in the SLD WMS
     * GetLegendGraphic request?
     *
     * @cfg {Boolean}
     */
    useScaleParameter: true,

    /**
     * Optional parameters to add to the legend url, this can e.g. be used to
     * support vendor-specific parameters in a SLD WMS GetLegendGraphic
     * request. To override the default MIME type of `image/gif` use the
     * `FORMAT` parameter in baseParams.
     *
     * Example:
     *
     *     var legendPanel = Ext.create('GeoExt.panel.Legend', {
     *         map: map,
     *         title: 'Legend Panel',
     *         defaults: {
     *             style: 'padding:5px',
     *             baseParams: {
     *                 FORMAT: 'image/png',
     *                 LEGEND_OPTIONS: 'forceLabels:on'
     *             }
     *         }
     *     });
     *
     * @cfg {Object}
     */
    baseParams: null,

    initComponent: function(){
        var me = this;
        me.addEvents('legendimageloaded');
        me.callParent();
        var layer = me.layerRecord.getLayer();
        me._noMap = !layer.map;
        layer.events.register("moveend", me, me.onLayerMoveend);
        me.update();
    },

    /**
     * Called when `moveend` fires on the associated layer. Might call #update
     * to be in sync with layer style.
     *
     * @private
     * @param {Object} e
     */
    onLayerMoveend: function(e) {
        if ((e.zoomChanged === true && this.useScaleParameter === true) ||
            this._noMap) {
            delete this._noMap;
            this.update();
        }
    },

    /**
     * Get the legend URL of a sublayer.
     *
     * @param {String} layerName A sublayer.
     * @param {Array} layerNames The array of sublayers, read from #layerRecord
     *     if not provided.
     * @return {String} The legend URL.
     * @private
     */
    getLegendUrl: function(layerName, layerNames) {
        var rec = this.layerRecord;
        var url;
        var styles = rec && rec.get("styles");
        var layer = rec.getLayer();
        layerNames = layerNames || [layer.params.LAYERS].join(",").split(",");

        var styleNames = layer.params.STYLES &&
        [layer.params.STYLES].join(",").split(",");
        var idx = Ext.Array.indexOf(layerNames, layerName);
        var styleName = styleNames && styleNames[idx];
        // check if we have a legend URL in the record's
        // "styles" data field
        if(styles && styles.length > 0) {
            if(styleName) {
                Ext.each(styles, function(s) {
                    url = (s.name == styleName && s.legend) && s.legend.href;
                    return !url;
                });
            } else if(this.defaultStyleIsFirst === true && !styleNames &&
                !layer.params.SLD && !layer.params.SLD_BODY) {
                url = styles[0].legend && styles[0].legend.href;
            }
        }
        if(!url) {
            url = layer.getFullRequestString({
                REQUEST: "GetLegendGraphic",
                WIDTH: null,
                HEIGHT: null,
                EXCEPTIONS: "application/vnd.ogc.se_xml",
                LAYER: layerName,
                LAYERS: null,
                STYLE: (styleName !== '') ? styleName: null,
                STYLES: null,
                SRS: null,
                FORMAT: null,
                TIME: null
            });
        }
        var params = Ext.apply({}, this.baseParams);
        if (layer.params._OLSALT) {
            // update legend after a forced layer redraw
            params._OLSALT = layer.params._OLSALT;
        }
        url = Ext.urlAppend(url, Ext.urlEncode(params));
        if (url.toLowerCase().indexOf("request=getlegendgraphic") != -1) {
            if (url.toLowerCase().indexOf("format=") == -1) {
                url = Ext.urlAppend(url, "FORMAT=image%2Fgif");
            }
            // add scale parameter - also if we have the url from the record's
            // styles data field and it is actually a GetLegendGraphic request.
            if (this.useScaleParameter === true) {
                var scale = layer.map.getScale();
                url = Ext.urlAppend(url, "SCALE=" + scale);
            }
        }
        return url;
    },

    /**
     * Update the legend, adding, removing or updating
     * the per-sublayer box component.
     *
     * @private
     */
    update: function() {
        var layer = this.layerRecord.getLayer();
        // In some cases, this update function is called on a layer
        // that has just been removed, see ticket #238.
        // The following check bypass the update if map is not set.
        if(!(layer && layer.map)) {
            return;
        }
        this.callParent();

        var layerNames, layerName, i, len;

        layerNames = [layer.params.LAYERS].join(",").split(",");

        var destroyList = [];
        var textCmp = this.items.get(0);
        this.items.each(function(cmp) {
            i = Ext.Array.indexOf(layerNames, cmp.itemId);
            if(i < 0 && cmp != textCmp) {
                destroyList.push(cmp);
            } else if(cmp !== textCmp){
                layerName = layerNames[i];
                var newUrl = this.getLegendUrl(layerName, layerNames);
                if(!OpenLayers.Util.isEquivalentUrl(newUrl, cmp.url)) {
                    cmp.setUrl(newUrl);
                }
            }
        }, this);
        for(i = 0, len = destroyList.length; i<len; i++) {
            var cmp = destroyList[i];
            // cmp.destroy() does not remove the cmp from
            // its parent container!
            this.remove(cmp);
            cmp.destroy();
        }

        for(i = 0, len = layerNames.length; i<len; i++) {
            layerName = layerNames[i];
            if(!this.items || !this.getComponent(layerName)) {
                this.add({
                    xtype: "gx_legendimage",
                    url: this.getLegendUrl(layerName, layerNames),
                    itemId: layerName,
                    listeners: {
                        'legendimageloaded': function() {
                            this.fireEvent('legendimageloaded', this);
                        },
                        scope: this
                    }
                });
            }
        }
        this.doLayout();
    },

    /**
     * Unregisters the moveend-listener prior to destroying.
     */
    beforeDestroy: function() {
        if (this.useScaleParameter === true) {
            var layer = this.layerRecord.getLayer();
            layer && layer.events &&
            layer.events.unregister("moveend", this, this.onLayerMoveend);
        }
        this.callParent();
    }
}, function() {
    GeoExt.container.LayerLegend.types["gx_wmslegend"] =
        GeoExt.container.WmsLegend;
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 * @include OpenLayers/Format/WFSDescribeFeatureType.js
 */

/**
 * A reader to create model objects from a DescribeFeatureType structure. Uses
 * `OpenLayers.Format.WFSDescribeFeatureType` internally for the parsing.
 *
 * Example:
 *
 *     Ext.define('My.model.Model', {
 *         field: ['name', 'type'],
 *         proxy: {
 *             type: 'ajax',
 *             url: 'http://wftgetfeaturetype',
 *             reader: {
 *                 type: 'gx_attribute'
 *             }
 *         }
 *     });
 *
 * `gx_attribute` is the alias to the Attribute reader.
 *
 * @class GeoExt.data.reader.Attribute
 */
Ext.define('GeoExt.data.reader.Attribute', {
    extend:  Ext.data.reader.Json ,
               
                         
                        
      
    alternateClassName: 'GeoExt.data.AttributeReader',
    alias: 'reader.gx_attribute',
    /**
     * Should we keep the raw parsed result? If true, the result will be stored
     * under the #raw property. Default is false.
     * @cfg {Boolean}
     */
    keepRaw: false,
    /**
     * The raw parsed result, only set if #keepRaw is true.
     * @cfg {Object}
     */
    raw: null,
    config: {
        /**
         * A parser for transforming the XHR response into an array of objects
         * representing attributes.
         *
         * Defaults to `OpenLayers.Format.WFSDescribeFeatureType` parser.
         *
         * @cfg {OpenLayers.Format}
         */
        format: null,

        /**
         * Properties of the ignore object should be field names. Values are
         * either arrays or regular expressions.
         *
         * @cfg {Object}
         */
        ignore: null,

        /**
         * A vector feature. If provided records created by the reader will
         * include a field named "value" referencing the attribute value as
         * set in the feature.
         *
         * @cfg {OpenLayers.Feature.Vector}
         */
        feature: null
    },

    /**
     * Create a new reader.
     *
     * @param {Object} [config] Config object.
     */
    constructor: function(config) {
        if (!this.model) {
            this.model = 'GeoExt.data.AttributeModel';
        }

        this.callParent([config]);

        if (this.feature) {
            this.setFeature(this.feature);
        }
    },

    /**
     * @private
     */
    destroyReader: function() {
        var me = this;
        delete me.raw;
        this.callParent();
    },

    /**
     * Appends an Ext.data.Field to this #model.
     */
    applyFeature: function(feature) {
        var f = Ext.create('Ext.data.Field', {
            name: "value",
            defaultValue: undefined // defaultValue defaults to ""
                                    // in Ext.data.Field, we may
                                    // need to change that...
        });
        this.model.prototype.fields.add(f);
        return feature;
    },

    /**
     * Function called by the parent to deserialize a DescribeFeatureType
     * response into Model objects.
     *
     * @param {Object} request The XHR object that contains the
     *     DescribeFeatureType response.
     */
    getResponseData: function(request) {
        var data = request.responseXML;
        if(!data || !data.documentElement) {
            data = request.responseText;
        }
        return this.readRecords(data);
    },

    /**
     * Function called by
     * {@link Ext.data.reader.Reader#read Ext.data.reader.Reader's read method}
     * to do the actual deserialization.
     *
     * @param {DOMElement/String/Array} data A document element or XHR
     *     response string.  As an alternative to fetching attributes data from
     *     a remote source, an array of attribute objects can be provided given
     *     that the properties of each attribute object map to a provided field
     *     name.
     */
    readRecords: function(data) {
        if (!this.getFormat()) {
            this.setFormat(new OpenLayers.Format.WFSDescribeFeatureType());
        }
        var attributes;
        if(data instanceof Array) {
            attributes = data;
        } else {
            // only works with one featureType in the doc
            var result = this.getFormat().read(data);
            if (this.keepRaw) {
                this.raw = result;
            }
            
                if (Object.keys(result).length === 0){
                    return null;
                }
                attributes = result.featureTypes[0].properties;
            
        }
        var feature = this.feature;
        var fields = this.model.prototype.fields;
        var numFields = fields.length;
        var attr, values, name, record, ignore, value, field, records = [];
        for(var i=0, len=attributes.length; i<len; ++i) {
            ignore = false;
            attr = attributes[i];
            values = {};
            for(var j=0; j<numFields; ++j) {
                field = fields.items[j];
                name = field.name;
                value = attr[name];
                if(this.ignoreAttribute(name, value)) {
                    ignore = true;
                    break;
                }
                values[name] = value;
            }
            if(feature) {
                value = feature.attributes[values.name];
                if(value !== undefined) {
                    if(this.ignoreAttribute("value", value)) {
                        ignore = true;
                    } else {
                        values.value = value;
                    }
                }
            }
            if(!ignore) {
                records[records.length] = values;
            }
        }
        return this.callParent([records]);
    },

    /**
     * Determine if the attribute should be ignored.
     *
     * @param {String} name The field name.
     * @param {String} value The field value.
     * @return {Boolean} True if the attribute should be ignored.
     */
    ignoreAttribute: function(name, value) {
        var ignore = false;
        if(this.ignore && this.ignore[name]) {
            var matches = this.ignore[name];
            if(typeof matches == "string") {
                ignore = (matches === value);
            } else if(matches instanceof Array) {
                ignore = (Ext.Array.indexOf(matches, value) > -1);
            } else if(matches instanceof RegExp) {
                ignore = (matches.test(value));
            }
        }
        return ignore;
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/data/reader/Attribute.js
 */

/**
 * @class GeoExt.data.AttributeModel
 *
 * A specific model for WFS DescribeFeatureType records.
 *
 * Preconfigured with an Ajax proxy and a GeoExt.data.reader.Attribute.
 */
Ext.define('GeoExt.data.AttributeModel', {
    alternateClassName: 'GeoExt.data.AttributeRecord',
    extend:  Ext.data.Model ,
               
                              
                                      
      
    alias: 'model.gx_attribute',
    fields: [
        {name: 'name', type: 'string'},
        {name: 'type', defaultValue: null},
        {name: 'restriction', defaultValue: null},
        {name: 'nillable', type: 'bool'}
        // No 'value' field by default. The 'value' field gets added by the
        // GeoExt.data.reader.Attribute constructor if it is given a feature.
    ],
    proxy: {
        type: 'ajax',
        reader: {
            type: 'gx_attribute'
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * Small Base class to make creating stores for remote OWS information sources
 * easier.
 *
 * NOTE: This is a BASE CLASS and is not designed for direct use in an
 * application. Instead, one should extend from this class in any situation in
 * which a you need a {@link Ext.data.proxy.Server} (ex: 'ajax', 'jsonp', etc)
 * and a reader which requires an {@link OpenLayers.Format} to parse the data.
 *
 * @class GeoExt.data.OwsStore
 */
Ext.define('GeoExt.data.OwsStore', {
    extend:  Ext.data.Store ,
               
                        
      
    alternateClassName: ['GeoExt.data.OWSStore'],

    config: {
        /**
         * The URL from which to retrieve the OWS document.
         *
         * @cfg {String}
         */
        url : null,

        /**
         * A parser for transforming the XHR response into an array of objects
         * representing attributes.
         *
         * @cfg {OpenLayers.Format}
         */
        format : null,

        /**
         * Any baseParams to use on this store.
         *
         * @cfg {Object}
         */
        baseParams: null
    },

    /**
     * @private
     */
    constructor: function(config) {
        // At this point, we have to copy the complex objects from the config
        // into the prototype. This is because Ext.data.Store's constructor
        // creates deep copies of these objects.
        if (config.format) {
            this.format = config.format;
            delete config.format;
        }

        this.callParent([config]);

        if(config.url) {
            this.setUrl(config.url);
        }
        if(this.format) {
            this.setFormat(this.format);
        }
        var proxy = this.getProxy();
        if (proxy) {
            proxy.startParam = false;
            proxy.limitParam = false;
            proxy.pageParam = false;
        }
        if (config.baseParams) {
            this.setBaseParams(config.baseParams);
        }
    },

    /**
     * @private
     */
    applyBaseParams: function(newParams) {
        if (newParams && Ext.isObject(newParams)) {
            var proxy = this.getProxy();
            if(proxy) {
                proxy.extraParams = newParams;
            }
        }
    },

    /**
     * @private
     */
    applyUrl: function(newValue) {
        if(newValue && Ext.isString(newValue)) {
            var proxy = this.getProxy();
            if(proxy) {
                proxy.url = newValue;
            }
        }
    },

    /**
     * @private
     */
    applyFormat: function(newFormat) {
        var proxy = this.getProxy();
        var reader = (proxy) ? proxy.getReader() : null;
        if(reader) {
            reader.format = newFormat;
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/data/AttributeModel.js
 * @requires GeoExt/data/OwsStore.js
 */

/**
 * A store to work with DescribeFeatureType responses. This is a regular
 * Ext store preconfigured with a {@link GeoExt.data.AttributeModel}.
 *
 * Example:
 *
 *     Ext.create('GeoExt.data.AttributeStore', {
 *         ignore: {type: 'xsd:string'},
 *         url: 'http://host.wfsdescribefeaturetype'
 *     });
 *
 * @class GeoExt.data.AttributeStore
 */
Ext.define('GeoExt.data.AttributeStore', {
    extend:  GeoExt.data.OwsStore ,
    model: 'GeoExt.data.AttributeModel',

    config: {
        /**
         * The ignore object passed to the reader.
         *
         *     // ... ignore attributes of certain type or with specified name:
         *     var ignoreObj = {
         *         type: [
         *             'gml:GeometryPropertyType',
         *             'gml:PointPropertyType',
         *             'gml:LineStringPropertyType',
         *             'gml:SurfacePropertyType',
         *             'gml:MultiPointPropertyType',
         *             'gml:MultiLineStringPropertyType',
         *             'gml:MultiSurfacePropertyType'
         *         ],
         *         name: [
         *             'id',
         *             'another_ignored_attr'
         *         ]
         *     };
         *
         * @cfg {Object}
         */
        ignore: null,

        /**
         * The OpenLayers.Feature.Vector passed to the reader.
         *
         * @cfg {Object}
         */
        feature: null
    },

    /**
     * @private
     */
    constructor: function(config) {
        config = Ext.apply({}, config);
        // At this point, we have to copy the complex objects from the config
        // into the prototype. This is because Ext.data.Store's constructor
        // creates deep copies of these objects.
        var data;
        if (config.feature) {
            this.feature = config.feature;
            delete config.feature;
        }
        // if we have a feature AND data, then we need to remove the data so
        // that the reader is not called before it is ready. We load the data in
        // the store AFTER the store & its dependent objects have been
        // constructed
        if (this.feature && config.data) {
            data = config.data;
            delete config.data;
        }

        this.callParent([config]);

        if (config.ignore) {
            this.setIgnore(config.ignore);
        }

        if (this.feature) {
            this.setFeature(this.feature);
        }

        if (data) {
            this.loadRawData(data);
        }
    },

    /**
     * We're setting the sample feature for the reader.
     *
     * @param {OpenLayers.Feature} feature
     * @private
     */
    applyFeature: function(feature) {
        var reader = this.getProxy().getReader();
        if(reader) {
            reader.setFeature(feature);
        }
    },

    /**
     * We're setting the ignore property in the reader.
     *
     * @param {Object} ignore
     * @private
     */
    applyIgnore: function(ignore) {
        var reader = this.getProxy().getReader();
        if(reader) {
            reader.setIgnore(ignore);
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Format/CSWGetRecords.js
 * @requires GeoExt/Version.js
 */

/**
 * Data reader class to create an array of records from a CSW GetRecords
 * response. The raw response from the OpenLayers parser is available through
 * the jsonData property.
 *
 * Example:
 *
 *     var store = Ext.create('Ext.data.Store', {
 *         proxy: Ext.create('GeoExt.data.proxy.Protocol', {
 *             protocol: new OpenLayers.Protocol.CSW({
 *                 url: "http://demo.geonode.org/geonetwork/srv/en/csw"
 *             })
 *         }),
 *         reader: Ext.create('GeoExt.data.reader.CswRecords')
 *     });
 *
 * @class GeoExt.data.reader.CswRecords
 */
Ext.define('GeoExt.data.reader.CswRecords', {
    alternateClassName: ['GeoExt.data.CSWRecordsReader'],
    extend:  Ext.data.reader.Json ,
    alias: 'reader.gx_cswrecords',
               
                        
      
    /**
     * Creates new Reader.
     *
     * @param {Object} [config] Config object.
     */
    constructor: function(config) {
        if (!this.model) {
            this.model = 'GeoExt.data.CswRecordsModel';
        }
        this.callParent([config]);
        if (!this.format) {
            this.format = new OpenLayers.Format.CSWGetRecords();
        }
    },

    /**
     * @param {XMLHttpRequest/OpenLayers.Protocol.Response} data If a
     *     ProtocolProxy is configured with OpenLayers.Protocol.CSW data will be
     *     OpenLayers.Protocol.Response. Otherwise data will be the
     *     XMLHttpRequest object.
     * @return  {Object} A data block which is used by an Ext.data.Store as a
     *     cache of Ext.data.Model objects.
     * @private
     */
    read: function(data) {
        var o = data.data;
        if (!o) {
            o = data.responseXML;
            if(!o || !o.documentElement) {
                o = data.responseText;
            }
        }
        return this.readRecords(o);
    },

    /**
     * Create a data block containing Ext.data.Records from an XML document.
     *
     * @param {DOMElement/String/Object} data A document element or XHR
     *     response string.  As an alternative to fetching capabilities data
     *     from a remote source, an object representing the capabilities can
     *     be provided given that the structure mirrors that returned from the
     *     capabilities parser.
     * @return  {Object} A data block which is used by an Ext.data.Store
     *     as a cache of Ext.data.Model objects.
     * @private
     */
    readRecords: function(data) {
        if(typeof data === "string" || data.nodeType) {
            data = this.format.read(data);
        }
        var result = this.callParent([data.records]);
        // post-process so we flatten simple objects with a value property
        Ext.each(result.records, function(record) {
            for (var key in record.data) {
                var value = record.data[key];
                if (value instanceof Array) {
                    for (var i=0, ii=value.length; i<ii; ++i) {
                        if (value[i] instanceof Object) {
                            value[i] = value[i].value;
                        }
                    }
                }
            }
        });
        if (data.SearchResults) {
            delete result.totalRecords;
            result.total = data.SearchResults.numberOfRecordsMatched;
        }
        return result;
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/data/reader/CswRecords.js
 */

/**
 * The model for the structure returned by CS-W GetRecords.
 *
 * @class GeoExt.data.CswRecordsModel
 */
Ext.define('GeoExt.data.CswRecordsModel',{
    extend:  Ext.data.Model ,
               
                                
                                       
      
    alias: 'model.gx_cswrecords',
    fields: [
        {name: "title"},
        {name: "subject"},
        {name: "URI"},
        {name: "bounds"},
        {name: "projection", type: "string"}
    ],
    proxy: {
        type: 'memory',
        reader: {
            type: 'gx_cswrecords'
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Feature/Vector.js
 * @requires GeoExt/Version.js
 */

/**
 * Used to read the attributes of a feature.
 *
 * @class GeoExt.data.reader.Feature
 */
Ext.define('GeoExt.data.reader.Feature', {
    extend:  Ext.data.reader.Json ,
    alias : 'reader.feature',
               
                        
      
    /**
     * Force to have our convertRecordData.
     *
     * @private
     */
    buildExtractors: function() {
        this.callParent(arguments);
        this.convertRecordData = this.convertFeatureRecordData;
    },

    /**
     * Copy feature attribute to record.
     *
     * @param {Array} convertedValues
     * @param {Object} feature
     * @param {Object} record
     * @private
     */
    convertFeatureRecordData: function(convertedValues, feature, record) {
        if (feature) {
            var fields = record.fields;
            var values = {};
            if (feature.attributes) {
                for (var j = 0, jj = fields.length; j < jj; j++){
                    var field = fields.items[j];
                    var v;
                    if (/[\[\.]/.test(field.mapping)) {
                        try {
                            v = new Function("obj", "return obj." + field.mapping)(feature.attributes);
                        } catch(e){
                            v = field.defaultValue;
                        }
                    }
                    else {
                        v = feature.attributes[field.mapping || field.name] || field.defaultValue;
                    }
                    if (field.convert) {
                        v = field.convert(v, record);
                    }
                    convertedValues[field.name] = v;
                }
            }
            record.state = feature.state;
            if (feature.state == OpenLayers.State.INSERT ||
                    feature.state == OpenLayers.State.UPDATE) {
                record.setDirty();
            }

            // newly inserted features need to be made into phantom records
            var id = (feature.state === OpenLayers.State.INSERT) ? undefined : feature.id;
            convertedValues['id'] = id;
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Feature/Vector.js
 * @requires GeoExt/data/reader/Feature.js
 */

/**
 * A store that synchronizes a features array of an `OpenLayers.Layer.Vector`.
 *
 * @class GeoExt.data.FeatureStore
 */
Ext.define('GeoExt.data.FeatureStore', {
    extend:  Ext.data.Store ,
               
                                    
      

    statics: {
        /**
         * @static
         * @property {Number} LAYER_TO_STORE
         * Bitfield specifying the layer to store sync direction.
         */
        LAYER_TO_STORE: 1,
        /**
         * @static
         * @property {Number} STORE_TO_LAYER
         * Bitfield specifying the store to layer sync direction.
         */
        STORE_TO_LAYER: 2
    },

    /**
     * Fires when the store is bound to a layer.
     *
     * @event bind
     * @param {GeoExt.data.FeatureStore} store
     * @param {OpenLayers.Layer.Vector} layer
     */

    /**
     * True when the vector layer is binded.
     *
     * @private
     * @property {OpenLayers.Layer.Vector}
     */
    isLayerBinded: false,

    /**
     * Layer that this store will be in sync with. If not provided, the
     * store will not be bound to a layer.
     *
     * @cfg {OpenLayers.Layer.Vector} layer
     */

    /**
     * Vector layer that the store is synchronized with, if any.
     *
     * @property {OpenLayers.Layer.Vector} layer
     */
    layer: null,

    /**
     * @cfg {OpenLayers.Layer/Array} features
     * Features that will be added to the store (and the layer, depending on the
     * value of the `initDir` option.
     */

    /**
     * @cfg {Number} initDir
     * Bitfields specifying the direction to use for the initial sync between
     * the layer and the store, if set to 0 then no initial sync is done.
     * Defaults to `GeoExt.data.FeatureStore.LAYER_TO_STORE|GeoExt.data.FeatureStore.STORE_TO_LAYER`
     */

    /**
     * @cfg {OpenLayers.Filter} featureFilter
     * This filter is evaluated before a feature record is added to the store.
     */
    featureFilter: null,

    /**
     * @param {Object} config Creation parameters
     * @private
     */
    constructor: function(config) {
        config = Ext.apply({
            proxy: {
                type: 'memory',
                reader: {
                    type: 'feature',
                    idProperty: 'id'
                }
            }
        }, config);

        if (config.layer) {
            this.layer = config.layer;
            delete config.layer;
        }

        // features option. Alias to data option
        if (config.features) {
            config.data = config.features;
        }
        delete config.features;

        this.callParent([config]);

        var options = {initDir: config.initDir};
        delete config.initDir;

        if (this.layer) {
            this.bind(this.layer, options);
        }
    },

    /**
     * Unbinds own listeners by calling #unbind when being destroyed.
     *
     * @private
     */
    destroy: function() {
        this.unbind();
        this.callParent();
    },

    /**
     * Bind this store to a layer instance. Once bound the store
     * is synchronized with the layer and vice-versa.
     *
     * @param {OpenLayers.Layer.Vector} layer The layer instance.
     * @param {Object} options
     */
    bind: function(layer, options) {
        options = options || {};

        if (this.isLayerBinded) {
            // already bound
            return;
        }
        this.layer = layer;
        this.isLayerBinded = true;

        var initDir = options.initDir;
        if (options.initDir == undefined) {
            initDir = GeoExt.data.FeatureStore.LAYER_TO_STORE |
                GeoExt.data.FeatureStore.STORE_TO_LAYER;
        }

        var features = layer.features.slice(0);

        if (initDir & GeoExt.data.FeatureStore.STORE_TO_LAYER) {
            this.each(function(record) {
                layer.addFeatures([record.raw]);
            }, this);
        }

        if (initDir & GeoExt.data.FeatureStore.LAYER_TO_STORE &&
                layer.features.length > 0) {
            // append a snapshot of the layer's features
            this.loadRawData(features, true);
        }

        this.layer.events.on({
            'featuresadded': this.onFeaturesAdded,
            'featuresremoved': this.onFeaturesRemoved,
            'featuremodified': this.onFeatureModified,
            scope: this
        });
        this.on({
            'load': this.onLoad,
            'clear': this.onClear,
            'add': this.onAdd,
            'remove': this.onRemove,
            'update': this.onStoreUpdate,
            scope: this
        });

        this.fireEvent("bind", this, this.layer);
    },

    /**
     * Unbind this store from his layer instance.
     */
    unbind: function() {
        if (this.isLayerBinded) {
            this.layer.events.un({
                'featuresadded': this.onFeaturesAdded,
                'featuresremoved': this.onFeaturesRemoved,
                'featuremodified': this.onFeatureModified,
                scope: this
            });
            this.un({
                'load': this.onLoad,
                'clear': this.onClear,
                'add': this.onAdd,
                'remove': this.onRemove,
                'update': this.onStoreUpdate,
                scope: this
            });
            this.layer = null;
            this.isLayerBinded = false;
        }
    },

    /**
     * Convenience method to add features.
     *
     * @param {OpenLayers.Feature.Vector[]} features The features to add.
     */
    addFeatures: function(features) {
        return this.loadRawData(features, true);
    },

    /**
     * Convenience method to remove features.
     *
     * @param {OpenLayers.Feature.Vector[]} features The features to remove.
     */
    removeFeatures: function(features) {
        //accept both a single-argument array of records, or any number of record arguments
        if (!Ext.isArray(features)) {
            features = Array.prototype.slice.apply(arguments);
        } else {
            // Create an array copy
            features = features.slice(0);
        }
        Ext.Array.each(features, function(feature) {
            this.remove(this.getByFeature(feature));
        }, this);
    },

    /**
     * Returns the record corresponding to a feature.
     *
     * @param {OpenLayers.Feature} feature An OpenLayers.Feature.Vector object.
     * @return {String} The model instance corresponding to a feature.
     */
    getByFeature: function(feature) {
        return this.getAt(this.findBy(function(record, id) {
            return record.raw == feature;
        }));
    },

    /**
     * Returns the record corresponding to a feature id.
     *
     * @param {String} id An OpenLayers.Feature.Vector id string.
     * @return {String} The model instance corresponding to the given id.
     */
    getById: function(id) {
        return (this.snapshot || this.data).findBy(function(record) {
            return record.raw.id === id;
        });
    },

    /**
     * Adds the given records to the associated layer.
     *
     * @param {Ext.data.Model[]} records
     * @private
     */
    addFeaturesToLayer: function(records) {
        var features = [];
        for (var i = 0, len = records.length; i < len; i++) {
            features.push(records[i].raw);
        }
        this._adding = true;
        this.layer.addFeatures(features);
        delete this._adding;
    },

    /**
     * Handler for layer featuresadded event.
     *
     * @param {Object} evt
     * @private
     */
    onFeaturesAdded: function(evt) {
        if (!this._adding) {
            var features = evt.features,
                toAdd = features;
            if (this.featureFilter) {
                toAdd = [];
                for (var i = 0, len = features.length; i < len; i++) {
                    var feature = features[i];
                    if (this.featureFilter.evaluate(feature) !== false) {
                        toAdd.push(feature);
                    }
                }
            }
            toAdd = this.proxy.reader.read(toAdd).records;
            this._adding = true;
            this.add(toAdd);
            delete this._adding;
        }
    },

    /**
     * Handler for layer featuresremoved event.
     *
     * @param {Object} evt
     * @private
     */
    onFeaturesRemoved: function(evt) {
        if (!this._removing) {
            var features = evt.features;
            for (var i = features.length - 1; i >= 0; i--) {
                var record = this.getByFeature(features[i]);
                if (record) {
                    this._removing = true;
                    this.remove(record);
                    delete this._removing;
                }
            }
        }
    },

    /**
     * Handler for layer featuremodified event.
     *
     * @param {Object} evt
     * @private
     */
    onFeatureModified: function(evt) {
        var record_old = this.getByFeature(evt.feature);
        if (record_old) {
            var record_new = this.proxy.reader.read(evt.feature).records[0];
            Ext.Object.each(record_new.getData(), function(key, value) {
                record_old.set(key, value);
            }, this);
        }
    },

    /**
     * Handler for a store's load event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Model[]} records
     * @param {Boolean} successful
     * @private
     */
    onLoad: function(store, records, successful) {
        if (successful) {
            this._removing = true;
            this.layer.removeAllFeatures();
            delete this._removing;

            this.addFeaturesToLayer(records);
        }
    },

    /**
     * Handler for a store's clear event.
     *
     * @param {Ext.data.Store} store
     * @private
     */
    onClear: function(store) {
        this._removing = true;
        this.layer.removeFeatures(this.layer.features);
        delete this._removing;
    },

    /**
     * Handler for a store's add event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Model[]} records
     * @param {Number} index
     * @private
     */
    onAdd: function(store, records, index) {
        if (!this._adding) {
            // addFeaturesToLayer takes care of setting
            // this._adding to true and deleting it
            this.addFeaturesToLayer(records);
        }
    },

    /**
     * Handler for a store's remove event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Model} record
     * @param {Number} index
     * @private
     */
    onRemove: function(store, record, index) {
        if (!this._removing) {
            var feature = record.raw;
            if (this.layer.getFeatureById(feature.id) != null) {
                this._removing = true;
                this.layer.removeFeatures([feature]);
                delete this._removing;
            }
        }
    },

    /**
     * Handler for a store's update event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Model} record
     * @param {Number} operation
     * @param {Array} modifiedFieldNames
     *
     * @private
     */
    onStoreUpdate: function(store, record, operation, modifiedFieldNames) {
        if (!this._updating) {
            var feature = record.raw;
            if (feature.state !== OpenLayers.State.INSERT) {
                feature.state = OpenLayers.State.UPDATE;
            }
            var cont = this.layer.events.triggerEvent('beforefeaturemodified', {
                feature: feature
            });
            if (cont !== false) {
                Ext.Array.each(modifiedFieldNames, function(field) {
                    feature.attributes[field] = record.get(field);
                });
                this._updating = true;
                this.layer.events.triggerEvent('featuremodified', {
                    feature: feature
                });
                delete this._updating;
            }
        }
    },

    /**
     * @inheritdoc
     *
     * The event firing behaviour of Ext.4.1 is reestablished here. See also:
     * [This discussion on the Sencha forum](http://www.sencha.com/forum/
     * showthread.php?253596-beforeload-is-not-fired-by-loadRawData)
     *
     * In version 4.2.1 this method reads
     *
     *     //...
     *     loadRawData : function(data, append) {
     *         var me      = this,
     *             result  = me.proxy.reader.read(data),
     *             records = result.records;
     *
     *         if (result.success) {
     *             me.totalCount = result.total;
     *             me.loadRecords(records, append ? me.addRecordsOptions : undefined);
     *         }
     *     },
     *     // ...
     *
     * While the previous version 4.1.3 has also
     * the line `me.fireEvent('load', me, records, true);`:
     *
     *     // ...
     *     if (result.success) {
     *         me.totalCount = result.total;
     *         me.loadRecords(records, append ? me.addRecordsOptions : undefined);
     *         me.fireEvent('load', me, records, true);
     *     }
     *     // ...
     *
     * Our overwritten method has the code from 4.1.3, so that the #load-event
     * is being fired.
     *
     * See also the source code of [version 4.1.3](http://docs-origin.sencha.
     * com/extjs/4.1.3/source/Store.html#Ext-data-Store-method-loadRawData) and
     * of [version 4.2.1](http://docs-origin.sencha.com/extjs/4.2.1/source/
     * Store.html#Ext-data-Store-method-loadRawData).
     */
    loadRawData : function(data, append) {
        var me      = this,
            result  = me.proxy.reader.read(data),
            records = result.records;

        if (result.success) {
            me.totalCount = result.total;
            me.loadRecords(records, append ? me.addRecordsOptions : undefined);
            me.fireEvent('load', me, records, true);
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * The layer model class used by the stores.
 *
 * @class GeoExt.data.LayerModel
 */
Ext.define('GeoExt.data.LayerModel',{
    alternateClassName: 'GeoExt.data.LayerRecord',
    extend:  Ext.data.Model ,
               
                                
                               
                        
      
    alias: 'model.gx_layer',
    inheritableStatics: {
        /**
         * Convenience function for creating new layer model instance object
         * using a layer object.
         *
         * @param {OpenLayers.Layer} layer
         * @return {GeoExt.data.LayerModel}
         * @static
         */
        createFromLayer: function(layer) {
            return this.getProxy().reader.readRecords([layer]).records[0];
        }
    },
    fields: [
        'id',
        {name: 'title', type: 'string', mapping: 'name'},
        {name: 'legendURL', type: 'string', mapping: 'metadata.legendURL'},
        {name: 'hideTitle', type: 'bool', mapping: 'metadata.hideTitle'},
        {name: 'hideInLegend', type: 'bool', mapping: 'metadata.hideInLegend'}
    ],
    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        }
    },
    /**
     * Returns the {OpenLayers.Layer} layer object used in this model instance.
     *
     * @return {OpenLayers.Layer}
     */
    getLayer: function() {
        return this.raw;
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/data/LayerModel.js
 */

/**
 * A store that synchronizes a layers array of an OpenLayers.Map with a
 * layer store holding {@link GeoExt.data.LayerModel} instances.
 *
 * @class GeoExt.data.LayerStore
 */
Ext.define('GeoExt.data.LayerStore', {
                                         
    extend:  Ext.data.Store ,
    model: 'GeoExt.data.LayerModel',

    statics: {
        /**
         * Direction: Map to store
         *
         * @static
         * @property {Number}
         */
        MAP_TO_STORE: 1,
        /**
         * Direction: Store to map
         *
         * @static
         * @property {Number}
         */
        STORE_TO_MAP: 2
    },

    /**
     * Fires when the store is bound to a map.
     *
     * @event bind
     * @param {GeoExt.data.LayerStore} store
     * @param {OpenLayers.Map} map
     */

    /**
     * Map that this store will be in sync with. If not provided, the
     * store will not be bound to a map.
     *
     * @cfg {OpenLayers.Map/GeoExt.panel.Map/Object} map
     */

    /**
     * Map that the store is synchronized with, if any.
     *
     * @property {OpenLayers.Map/Object} map
     */
    map: null,

    /**
     * Layers that will be added to the store (and the map, depending on the
     * value of the `initDir` option.
     *
     * @cfg {OpenLayers.Layer/Array} layers
     */

    /**
     *
     * Bitfields specifying the direction to use for the initial sync between
     * the map and the store, if set to 0 then no initial sync is done.
     * Defaults to #MAP_TO_STORE | #STORE_TO_MAP.
     *
     * @cfg {Number} initDir
     */

    /**
     * @param {Object} config Creation parameters.
     * @private
     */
    constructor: function(config) {
        var me = this;

        config = Ext.apply({}, config);

        // "map" option
        var map = (GeoExt.MapPanel && config.map instanceof GeoExt.MapPanel) ?
            config.map.map : config.map;
        delete config.map;

        // "layers" option - is an alias to "data" option
        if(config.layers) {
            config.data = config.layers;
        }
        delete config.layers;

        // "initDir" option
        var options = {initDir: config.initDir};
        delete config.initDir;

        me.callParent([config]);

        if(map) {
            this.bind(map, options);
        }
    },

    /**
     * Bind this store to a map instance, once bound the store
     * is synchronized with the map and vice-versa.
     *
     * @param {OpenLayers.Map} map The map instance.
     * @param {Object} options
     */
    bind: function(map, options) {
        var me = this;

        if(me.map) {
            // already bound
            return;
        }
        me.map = map;
        options = Ext.apply({}, options);

        var initDir = options.initDir;
        if(options.initDir == undefined) {
            initDir = GeoExt.data.LayerStore.MAP_TO_STORE |
                GeoExt.data.LayerStore.STORE_TO_MAP;
        }

        // create a snapshot of the map's layers
        var layers = map.layers.slice(0);

        if(initDir & GeoExt.data.LayerStore.STORE_TO_MAP) {
            me.each(function(record) {
                me.map.addLayer(record.getLayer());
            }, me);
        }
        if(initDir & GeoExt.data.LayerStore.MAP_TO_STORE) {
            me.loadRawData(layers, true);
        }

        map.events.on({
            "changelayer": me.onChangeLayer,
            "addlayer": me.onAddLayer,
            "removelayer": me.onRemoveLayer,
            scope: me
        });
        me.on({
            "load": me.onLoad,
            "clear": me.onClear,
            "add": me.onAdd,
            "remove": me.onRemove,
            "update": me.onStoreUpdate,
            scope: me
        });
        me.data.on({
            "replace" : me.onReplace,
            scope: me
        });
        me.fireEvent("bind", me, map);
    },

    /**
     * Unbind this store from the map it is currently bound.
     */
    unbind: function() {
        var me = this;

        if(me.map) {
            me.map.events.un({
                "changelayer": me.onChangeLayer,
                "addlayer": me.onAddLayer,
                "removelayer": me.onRemoveLayer,
                scope: me
            });
            me.un("load", me.onLoad, me);
            me.un("clear", me.onClear, me);
            me.un("add", me.onAdd, me);
            me.un("remove", me.onRemove, me);
            me.un("update", me.onStoreUpdate, me);

            me.data.un("replace", me.onReplace, me);

            me.map = null;
        }
    },

    /**
     * Handler for layer changes.  When layer order changes, this moves the
     * appropriate record within the store.
     *
     * @param {Object} evt
     * @private
     */
    onChangeLayer: function(evt) {
        var layer = evt.layer;
        var recordIndex = this.findBy(function(rec, id) {
            return rec.getLayer() === layer;
        });
        if(recordIndex > -1) {
            var record = this.getAt(recordIndex);
            if(evt.property === "order") {
                if(!this._adding && !this._removing) {
                    var layerIndex = this.map.getLayerIndex(layer);
                    if(layerIndex !== recordIndex) {
                        this._removing = true;
                        this.remove(record);
                        delete this._removing;
                        this._adding = true;
                        this.insert(layerIndex, [record]);
                        delete this._adding;
                    }
                }
            } else if(evt.property === "name") {
                record.set("title", layer.name);
            } else {
                this.fireEvent("update", this, record, Ext.data.Record.EDIT);
            }
        }
    },

    /**
     * Handler for a map's addlayer event.
     *
     * @param {Object} evt
     * @private
     */
    onAddLayer: function(evt) {
        var me = this;
        if(!me._adding) {
            me._adding = true;
            var result  = me.proxy.reader.read(evt.layer);
            me.add(result.records);
            delete me._adding;
        }
    },

    /**
     * Handler for a map's removelayer event.
     *
     * @param {Object} evt
     * @private
     */
    onRemoveLayer: function(evt){
        //TODO replace the check for undloadDestroy with a listener for the
        // map's beforedestroy event, doing unbind(). This can be done as soon
        // as http://trac.openlayers.org/ticket/2136 is fixed.
        if(this.map.unloadDestroy) {
            if(!this._removing) {
                var layer = evt.layer,
                    rec = this.getByLayer(layer);
                if (rec) {
                    this._removing = true;
                    this.remove(this.getByLayer(layer));
                    delete this._removing;
                }
            }
        } else {
            this.unbind();
        }
    },

    /**
     * Handler for a store's load event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Model[]} records
     * @param {Boolean} successful
     * @private
     */
    onLoad: function(store, records, successful) {
        if (successful) {
            if (!Ext.isArray(records)) {
                records = [records];
            }
            if(!this._addRecords) {
                this._removing = true;
                for (var i = this.map.layers.length - 1; i >= 0; i--) {
                    this.map.removeLayer(this.map.layers[i]);
                }
                delete this._removing;
            }
            var len = records.length;
            if (len > 0) {
                var layers = new Array(len);
                for (var j = 0; j < len; j++) {
                    layers[j] = records[j].getLayer();
                }
                this._adding = true;
                this.map.addLayers(layers);
                delete this._adding;
            }
        }
        delete this._addRecords;
    },

    /**
     * Handler for a store's clear event.
     *
     * @param {Ext.data.Store} store
     * @private
     */
    onClear: function(store) {
        this._removing = true;
        for (var i = this.map.layers.length - 1; i >= 0; i--) {
            this.map.removeLayer(this.map.layers[i]);
        }
        delete this._removing;
    },

    /**
     * Handler for a store's add event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Model[]} records
     * @param {Number} index
     * @private
     */
    onAdd: function(store, records, index) {
        if(!this._adding) {
            this._adding = true;
            var layer;
            for(var i=records.length-1; i>=0; --i) {
                layer = records[i].getLayer();
                this.map.addLayer(layer);
                if(index !== this.map.layers.length-1) {
                    this.map.setLayerIndex(layer, index);
                }
            }
            delete this._adding;
        }
    },

    /**
     * Handler for a store's remove event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Model} record
     * @param {Number} index
     * @private
     */
    onRemove: function(store, record, index){
        if(!this._removing) {
            var layer = record.getLayer();
            if (this.map.getLayer(layer.id) != null) {
                this._removing = true;
                this.removeMapLayer(record);
                delete this._removing;
            }
        }
    },

    /**
     * Handler for a store's update event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Model} record
     * @param {Number} operation
     */
    onStoreUpdate: function(store, record, operation) {
        if(operation === Ext.data.Record.EDIT) {
            if (record.modified && record.modified.title) {
                var layer = record.getLayer();
                var title = record.get("title");
                if(title !== layer.name) {
                    layer.setName(title);
                }
            }
        }
    },

    /**
     * Removes a record's layer from the bound map.
     *
     * @param {Ext.data.Record} record
     * @private
     */
    removeMapLayer: function(record){
        this.map.removeLayer(record.getLayer());
    },

    /**
     * Handler for a store's data collections' replace event.
     *
     * @param {String} key
     * @param {Ext.data.Model} oldRecord In this case, a record that has
     *     been replaced.
     * @param {Ext.data.Model} newRecord In this case, a record that is
     *     replacing oldRecord.
     * @private
     */
    onReplace: function(key, oldRecord, newRecord){
        this.removeMapLayer(oldRecord);
    },

    /**
     * Get the record for the specified layer.
     *
     * @param {OpenLayers.Layer} layer
     * @returns {Ext.data.Model} or undefined if not found
     */
    getByLayer: function(layer) {
        var index = this.findBy(function(r) {
            return r.getLayer() === layer;
        });
        if(index > -1) {
            return this.getAt(index);
        }
    },

    /**
     * Unbinds listeners by calling #unbind prior to being destroyed.
     *
     * @private
     */
    destroy: function() {
        this.unbind();
        this.callParent();
    },

    /**
     * Overload loadRecords to set a flag if `addRecords` is `true`
     * in the load options. Ext JS does not pass the load options to
     * "load" callbacks, so this is how we provide that information
     * to `onLoad`.
     *
     * @private
     */
    loadRecords: function(records, options) {
        if(options && options.addRecords) {
            this._addRecords = true;
        }
        this.callParent(arguments);
    },

    /**
     * @inheritdoc
     *
     * The event firing behaviour of Ext.4.1 is reestablished here. See also:
     * [This discussion on the Sencha forum](http://www.sencha.com/forum/
     * showthread.php?253596-beforeload-is-not-fired-by-loadRawData)
     *
     * In version 4.2.1 this method reads
     *
     *     //...
     *     loadRawData : function(data, append) {
     *         var me      = this,
     *             result  = me.proxy.reader.read(data),
     *             records = result.records;
     *
     *         if (result.success) {
     *             me.totalCount = result.total;
     *             me.loadRecords(records, append ? me.addRecordsOptions : undefined);
     *         }
     *     },
     *     // ...
     *
     * While the previous version 4.1.3 has also
     * the line `me.fireEvent('load', me, records, true);`:
     *
     *     // ...
     *     if (result.success) {
     *         me.totalCount = result.total;
     *         me.loadRecords(records, append ? me.addRecordsOptions : undefined);
     *         me.fireEvent('load', me, records, true);
     *     }
     *     // ...
     *
     * Our overwritten method has the code from 4.1.3, so that the #load-event
     * is being fired.
     *
     * See also the source code of [version 4.1.3](http://docs-origin.sencha.
     * com/extjs/4.1.3/source/Store.html#Ext-data-Store-method-loadRawData) and
     * of [version 4.2.1](http://docs-origin.sencha.com/extjs/4.2.1/source/
     * Store.html#Ext-data-Store-method-loadRawData).
     */
    loadRawData : function(data, append) {
        var me      = this,
            result  = me.proxy.reader.read(data),
            records = result.records;

        if (result.success) {
            me.totalCount = result.total;
            me.loadRecords(records, append ? me.addRecordsOptions : undefined);
            me.fireEvent('load', me, records, true);
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/**
 * Model for trees that use GeoExt tree components. It can also hold plain
 * Ext JS layer nodes.
 *
 * This model adds several fields that are specific to tree extensions
 * provided by GeoExt:
 *
 * * **plugins** Object[]: The plugins for this node.
 * * **layer** OpenLayers.Layer: The layer this node is connected to.
 * * **container** Ext.AbstractPlugin: The instance of a container plugin.
 *   Read only.
 * * **checkedGroup** String: An identifier for a group of mutually exclusive
 *   layers. If set, the node will render with a radio button instead of a
 *   checkbox.
 * * **fixedText** Boolean: Used to determine if a node's name should change.
 *   dynamically if the name of the connected layer changes, if any. Read only.
 * * **component** Ext.Component: The component to be rendered with this node,
 *   if any.
 *
 * A typical configuration that makes use of some of these extended sttings
 * could look like this:
 *
 *     {
 *         plugins: [{ptype: 'gx_layer'}],
 *         layer: myLayerRecord.getLayer(),
 *         checkedGroup: 'natural',
 *         component: {
 *             xtype: "gx_wmslegend",
 *             layerRecord: myLayerRecord,
 *             showTitle: false
 *         }
 *     }
 *
 * The above creates a node with a GeoExt.tree.LayerNode plugin, and connects
 * it to a layer record that was previously assigned to the myLayerRecord
 * variable. The node will be rendered with a GeoExt.container.WmsLegend,
 * configured with the same layer.
 *
 * @class GeoExt.data.LayerTreeModel
 */
Ext.define('GeoExt.data.LayerTreeModel',{
    alternateClassName: 'GeoExt.data.LayerTreeRecord',
    extend:  Ext.data.Model ,
               
                                
                               
                        
      
    alias: 'model.gx_layertree',
    fields: [
        {name: 'text', type: 'string'},
        {name: 'plugins'},
        {name: 'layer'},
        {name: 'container'},
        {name: 'checkedGroup', type: 'string'},
        {name: 'fixedText', type: 'bool'},
        {name: 'component'}
    ],
    proxy: {
        type: "memory"
    },

    /**
     * Fires after the node's fields were modified.
     *
     * @event afteredit
     * @param {GeoExt.data.LayerTreeModel} this This model instance.
     * @param {String[]} modifiedFieldNames The names of the fields that were
     * edited.
     */

    /**
     * @private
     */
    constructor: function(data, id, raw, convertedData) {
        var me = this;

        me.callParent(arguments);

        window.setTimeout(function() {
            var plugins = me.get('plugins');

            if (plugins) {
                var plugin, instance;
                for (var i=0, ii=plugins.length; i<ii; ++i) {
                    plugin = plugins[i];
                    instance = Ext.PluginMgr.create(Ext.isString(plugin) ? {
                        ptype: plugin
                    } : plugin);
                    instance.init(me);
                }
            }
        });
    },

    /**
     * Fires the #afteredit event after the node's fields were modified.
     *
     * @private
     */
    afterEdit: function(modifiedFieldNames) {
        var me = this;
        me.callParent(arguments);
        me.fireEvent('afteredit', this, modifiedFieldNames);
    }
});
/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Format/JSON.js
 * @include OpenLayers/Format/GeoJSON.js
 * @include OpenLayers/Util.js
 * @include OpenLayers/Geometry/Point.js
 * @include OpenLayers/Feature/Vector.js
 * @include OpenLayers/Layer/Vector.js
 * @requires GeoExt/Version.js
 */

/**
 * Provides an interface to a Mapfish or GeoServer print module. For printing,
 * one or more instances of {@link GeoExt.data.PrintPage} are also required
 * to tell the PrintProvider about the scale and extent (and optionally
 * rotation) of the page(s) we want to print.
 *
 * Minimal code to print as much of the current map extent as possible as
 * soon as the print service capabilities are loaded, using the first layout
 * reported by the print service:
 *
 * Example:
 *
 *     var mapPanel = Ext.create('GeoExt.panel.Map', {
 *         renderTo: "mappanel",
 *         layers: [new OpenLayers.Layer.WMS("wms", "/geoserver/wms",
 *             {layers: "topp:tasmania_state_boundaries"})],
 *         center: [146.56, -41.56],
 *         zoom: 7
 *     });
 *     var printProvider = Ext.create('GeoExt.data.MapfishPrintProvider', {
 *         url: "/geoserver/pdf",
 *         listeners: {
 *             "loadcapabilities": function() {
 *                 var printPage = Ext.create('GeoExt.data.PrintPage', {
 *                     printProvider: printProvider
 *                 });
 *                 printPage.fit(mapPanel, true);
 *                 printProvider.print(mapPanel, printPage);
 *             }
 *         }
 *     });
 *
 * @class GeoExt.data.MapfishPrintProvider
 */
Ext.define('GeoExt.data.MapfishPrintProvider', {
    extend:  Ext.util.Observable ,
               
                             
                        
      

    /**
     * Base url of the print service. Will always have a trailing "/".
     *
     * @private
     * @property {String} url
     */
    url: null,

    /**
     * If set to true, the capabilities will be loaded upon instance creation,
     * and `loadCapabilities` does not need to be called manually. Setting this
     * when `capabilities` and no `url` is provided has no effect. Default is
     * false.
     *
     * @cfg {Boolean} autoLoad
     */

    /**
     * Capabilities of the print service. Only required if `url`
     * is not provided. This is the object returned by the `info.json`
     * endpoint of the print service, and is usually obtained by including a
     * script tag pointing to http://path/to/printservice/info.json?var=myvar
     * in the head of the html document, making the capabilities accessible as
     * `window.myvar`.
     *
     * This property should be used when no local print service or proxy is
     * available, or when you do not listen for the `loadcapabilities`
     * events before creating components that require the PrintProvider's
     * capabilities to be available.
     *
     * @cfg {Object} capabilities
     */

    /**
     * Capabilities as returned from the print service.
     *
     * @private
     * @property {Object} capabilities
     */
    capabilities: null,

    /**
     * Either `POST` or `GET` (case-sensitive). Method to use when sending print
     * requests to the servlet. If the print service is at the same origin as
     * the application (or accessible via proxy), then `POST` is recommended.
     * Use `GET` when accessing a remote print service with no proxy available,
     * but expect issues with character encoding and URLs exceeding the maximum
     * length. Default is `POST`.
     *
     * @cfg {String} method
     */

    /**
     * Either `POST` or `GET` (case-sensitive). Method to use when sending print
     * requests to the servlet.
     *
     * @property {String} method
     * @private
     */
    method: "POST",

    /**
     * The encoding to set in the headers when requesting the print service.
     * Prevent character encoding issues, especially when using IE. Default is
     * retrieved from document `charset` or `characterSet` if existing
     * or `UTF-8` if not.
     *
     * @cfg {String} encoding
     */
    encoding: document.charset || document.characterSet || "UTF-8",

    /**
     * Timeout of the POST Ajax request used for the print request (in
     * milliseconds). Default of 30 seconds. Has no effect if `method` is set to
     * `GET`.
     *
     * @cfg {Number} timeout
     */
    timeout: 30000,

    /**
     * Key-value pairs of custom data to be sent to the print service. This is
     * e.g. useful for complex layout definitions on the server side that
     * require additional parameters. Optional.
     *
     * @property {Object} customParams
     */
    customParams: null,

    /**
     * Key-value pairs of base params to be add to every request to the service.
     * Optional.
     *
     * @cfg {Object} baseParams
     */

    /**
     * Read-only. A store representing the scales available.
     *
     * Fields of records in this store:
     *
     * * name - `String` the name of the scale
     * * value - `Float` the scale denominator
     *
     * @property {Ext.data.JsonStore} scales
     */
    scales: null,

    /**
     * Read-only. A store representing the dpis available.
     *
     * Fields of records in this store:
     *
     * * name - `String` the name of the dpi
     * * value - `Float` the dots per inch
     *
     * @property {Ext.data.JsonStore} dpis
     */
    dpis: null,

    /**
     * Read-only. A store representing the layouts available.
     *
     * Fields of records in this store:
     *
     * * name - `String` the name of the layout
     * * size - `Object` width and height of the map in points
     * * rotation - `Boolean` indicates if rotation is supported
     *
     * @property {Ext.data.JsonStore} layouts
     */
    layouts: null,

    /**
     * The record for the currently used resolution. Read-only, use `#setDpi` to
     * set the value.
     *
     * @property {Ext.data.Record} dpi
     */
    dpi: null,

    /**
     * The record of the currently used layout. Read-only, use `#setLayout` to
     * set the value.
     *
     * @property {Ext.data.Record} layout
     */
    layout: null,

    /**
     * Private constructor override.
     *
     * @private
     */
    constructor: function(config) {
        this.initialConfig = config;
        Ext.apply(this, config);

        if(!this.customParams) {
            this.customParams = {};
        }

        /**
         * Triggered when the capabilities have finished loading. This
         * event will only fire when `#capabilities` is not  configured.
         *
         * Listener arguments:
         *
         * * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *   PrintProvider.
         * * capabilities - `Object` the capabilities.
         *
         * @event loadcapabilities
         */

        /**
         * Triggered when the layout is changed.
         *
         * Listener arguments:
         *
         * * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *   PrintProvider.
         * * layout - {@link Ext.data.Record} the new layout.
         *
         * @event layoutchange
         */

        /**
         * Triggered when the dpi value is changed.
         *
         * Listener arguments:
         *
         * * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *   PrintProvider.
         * * dpi - {@link Ext.data.Record} the new dpi record.
         *
         * @event dpichange
         */

        //  TODO: rename this event to beforeencode
        /**
         * Triggered when the print method is called.
         *
         * Listener arguments:
         *
         * * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *   PrintProvider.
         * * map - `OpenLayers.Map` the map being printed.
         * * pages - Array of {@link GeoExt.data.PrintPage} the print
         *   pages being printed.
         * * options - `Object` the options to the print command.
         *
         * @event beforeprint
         */

        /**
         * Triggered when the print document is opened.
         *
         * Listener arguments:
         *
         * * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *   PrintProvider.
         * * url - `String` the url of the print document.
         *
         *  @event print
         */

        /**
         * Triggered when using the `POST` method, when the print backend
         * returns an exception.
         *
         * Listener arguments:
         *
         * * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *   PrintProvider.
         * * response - `Object` the response object of the XHR.
         *
         * @event printexception
         */

        /**
         * Triggered before a layer is encoded. This can be used to exclude
         * layers from the printing, by having the listener return false.
         *
         * Listener arguments:
         *
         * * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *   PrintProvider.
         * * layer - `OpenLayers.Layer` the layer which is about to be
         *   encoded.
         *
         * @event beforeencodelayer
         */

        /**
         * Triggered when a layer is encoded. This can be used to modify
         * the encoded low-level layer object that will be sent to the
         * print service.
         *
         * Listener arguments:
         *
         * * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *   PrintProvider.
         * * layer - `OpenLayers.Layer` the layer which is about to be
         *   encoded.
         * * encodedLayer - `Object` the encoded layer that will be
         *   sent to the print service.
         *
         * @event encodelayer
         */

        /**
         *  Triggered before the PDF is downloaded. By returning false from
         *  a listener, the default handling of the PDF can be cancelled
         *  and applications can take control over downloading the PDF.
         *  TODO: rename to beforeprint after the current beforeprint event
         *  has been renamed to beforeencode.
         *
         *  Listener arguments:
         *
         *  * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *    PrintProvider.
         *  * url - `String` the url of the print document.
         *
         * @event beforedownload
         */

        /**
         * Triggered before the legend is encoded. If the listener
         * returns false, the default encoding based on GeoExt.LegendPanel
         * will not be executed. This provides an option for application
         * to get legend info from a custom component other than
         * GeoExt.LegendPanel.
         *
         * Listener arguments:
         *
         * * printProvider - {@link GeoExt.data.MapfishPrintProvider} this
         *   PrintProvider.
         * * jsonData - `Object` The data that will be sent to the print
         *   server. Can be used to populate jsonData.legends.
         * * legend - `Object` The legend supplied in the options which were
         *   sent to the print function.
         *
         * @event beforeencodelegend
         */

        this.callParent(arguments);

        this.scales = Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: "memory",
                reader: {
                    type: "json",
                    root: "scales"
                }
            },
            fields: [
                "name",
                {name: "value", type: "float"}
            ],
            sortOnLoad: true,
            sorters: { property: 'value', direction : 'DESC' }

        });

        this.dpis = Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: "memory",
                reader: {
                    type: "json",
                    root: "dpis"
                }
            },
            fields: [
                 "name",
                 {name: "value", type: "float"}
            ]
        });

        this.layouts = Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: "memory",
                reader: {
                    type: "json",
                    root: "layouts"
                }
            },
            fields: [
                "name",
                {name: "size", mapping: "map"},
                {name: "rotation", type: "boolean"}
            ]
        });

        if(config.capabilities) {
            this.loadStores();
        } else {
            if(this.url.split("/").pop()) {
                this.url += "/";
            }
            if (this.initialConfig.autoLoad) {
                this.loadCapabilities();
            }
        }
    },

    /**
     * Sets the layout for this printProvider.
     *
     * @param {Ext.data.Record} layout The record of the layout.
     */
    setLayout: function(layout) {
        this.layout = layout;
        this.fireEvent("layoutchange", this, layout);
    },

    /**
     * Sets the dpi for this printProvider.
     *
     * @param {Ext.data.Record} dpi The dpi record.
     *
     */
    setDpi: function(dpi) {
        this.dpi = dpi;
        this.fireEvent("dpichange", this, dpi);
    },

    /**
     * Sends the print command to the print service and opens a new window
     * with the resulting PDF.
     *
     * Valid properties for the `options` argument:
     *
     * * `legend` - {@link GeoExt.LegendPanel} If provided, the legend
     *     will be added to the print document. For the printed result to
     *     look like the LegendPanel, the following `!legends` block
     *     should be included in the `items` of your page layout in the
     *     print module's configuration file:
     *
     *          - !legends
     *              maxIconWidth: 0
     *              maxIconHeight: 0
     *              classIndentation: 0
     *              layerSpace: 5
     *              layerFontSize: 10
     *
     * * `overview` - `OpenLayers.Control.OverviewMap` If provided,
     *     the layers for the overview map in the printout will be taken from
     *     the OverviewMap control. If not provided, the print service will
     *     use the main map's layers for the overview map. Applies only for
     *     layouts configured to print an overview map.
     *
     *  @param {GeoExt.MapPanel/OpenLayers.Map} map The map to print.
     *  @param {GeoExt.data.PrintPage[]/GeoExt.data.PrintPage} pages Page or
     *      pages to print.
     *  @param {Object} options Object with additional options, see above.
     */
    print: function(map, pages, options) {
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

        // ensure that the baseLayer is the first one in the encoded list
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

        if(options.legend && !(this.fireEvent("beforeencodelegend", this, jsonData, options.legend) === false)) {
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

        if(this.method === "GET") {
            var url = Ext.urlAppend(this.capabilities.printURL,
                "spec=" + encodeURIComponent(Ext.encode(jsonData)));
            this.download(url);
        } else {
            Ext.Ajax.request({
                url: this.capabilities.createURL,
                timeout: this.timeout,
                jsonData: jsonData,
                headers: {"Content-Type": "application/json; charset=" + this.encoding},
                success: function(response) {
                    var url = Ext.decode(response.responseText).getURL;
                    this.download(url);
                },
                failure: function(response) {
                    this.fireEvent("printexception", this, response);
                },
                params: this.initialConfig.baseParams,
                scope: this
            });
        }
    },

    /**
     * Actually triggers a 'download' of the passed URL.
     *
     * @param {String} url
     * @private
     */
    download: function(url) {
        if (this.fireEvent("beforedownload", this, url) !== false) {
            if (Ext.isOpera) {
                // Make sure that Opera don't replace the content tab with
                // the pdf
                window.open(url);
            } else {
                // This avoids popup blockers for all other browsers
                window.location.href = url;
            }
        }
        this.fireEvent("print", this, url);
    },

   /**
    *  Loads the capabilities from the print service. If this instance is
    *  configured with either `#capabilities` or a `#url` and `#autoLoad`
    *  set to true, then this method does not need to be called from the
    *  application.
    */
   loadCapabilities: function() {
       if (!this.url) {
           return;
       }
       var url = this.url + "info.json";
       Ext.Ajax.request({
           url: url,
           method: "GET",
           disableCaching: false,
           success: function(response) {
               this.capabilities = Ext.decode(response.responseText);
               this.loadStores();
           },
           params: this.initialConfig.baseParams,
           scope: this
       });
   },

   /**
    * Loads the internal stores and fires the #loadcapabilities event when done.
    *
    * @private
    */
   loadStores: function() {

       this.scales.loadRawData(this.capabilities);
       this.dpis.loadRawData(this.capabilities);
       this.layouts.loadRawData(this.capabilities);

       this.setLayout(this.layouts.getAt(0));
       this.setDpi(this.dpis.getAt(0));
       this.fireEvent("loadcapabilities", this, this.capabilities);
   },

    /**
     * Encodes a given layer according to the definitions in #encoders.
     *
     * @param {OpenLayers.Layer} layer
     * @return {Object}
     * @private
     */
    encodeLayer: function(layer) {
        var encLayer;
        for(var c in this.encoders.layers) {
            if(OpenLayers.Layer[c] && layer instanceof OpenLayers.Layer[c]) {
                if(this.fireEvent("beforeencodelayer", this, layer) === false) {
                    return;
                }
                encLayer = this.encoders.layers[c].call(this, layer);
                this.fireEvent("encodelayer", this, layer, encLayer);
                break;
            }
        }
        // only return the encLayer object when we have a type. Prevents a
        // fallback on base encoders like HTTPRequest.
        return (encLayer && encLayer.type) ? encLayer : null;
    },

    /**
     * Converts the provided url to an absolute url.
     *
     * @param {String} url
     * @return {String}
     * @private
     */
    getAbsoluteUrl: function(url) {
        var a;
        if(Ext.isIE6 || Ext.isIE7 || Ext.isIE8) {
            a = document.createElement("<a href='" + url + "'/>");
            a.style.display = "none";
            document.body.appendChild(a);
            a.href = a.href;
            document.body.removeChild(a);
        } else {
            a = document.createElement("a");
            a.href = url;
        }
        return a.href;
    },

    /**
     * Encoders for all print content.
     *
     * @property {Object} encoders
     * @private
     */
    encoders: {
        "layers": {
            "Layer": function(layer) {
                var enc = {};
                if (layer.options && layer.options.maxScale) {
                    enc.minScaleDenominator = layer.options.maxScale;
                }
                if (layer.options && layer.options.minScale) {
                    enc.maxScaleDenominator = layer.options.minScale;
                }
                return enc;
            },
            "WMS": function(layer) {
                var enc = this.encoders.layers.HTTPRequest.call(this, layer);
                Ext.apply(enc, {
                    type: 'WMS',
                    layers: [layer.params.LAYERS].join(",").split(","),
                    format: layer.params.FORMAT,
                    styles: [layer.params.STYLES].join(",").split(",")
                });
                var param;
                for(var p in layer.params) {
                    param = p.toLowerCase();
                    if(!layer.DEFAULT_PARAMS[param] &&
                    "layers,styles,width,height,srs".indexOf(param) == -1) {
                        if(!enc.customParams) {
                            enc.customParams = {};
                        }
                        enc.customParams[p] = layer.params[p];
                    }
                }
                return enc;
            },
            "OSM": function(layer) {
                var enc = this.encoders.layers.TileCache.call(this, layer);
                return Ext.apply(enc, {
                    type: 'OSM',
                    baseURL: enc.baseURL.substr(0, enc.baseURL.indexOf("$")),
                    extension: "png"
                });
            },
            "TMS": function(layer) {
                var enc = this.encoders.layers.TileCache.call(this, layer);
                return Ext.apply(enc, {
                    type: 'TMS',
                    format: layer.type
                });
            },
            "TileCache": function(layer) {
                var enc = this.encoders.layers.HTTPRequest.call(this, layer);
                return Ext.apply(enc, {
                    type: 'TileCache',
                    layer: layer.layername,
                    maxExtent: layer.maxExtent.toArray(),
                    tileSize: [layer.tileSize.w, layer.tileSize.h],
                    extension: layer.extension,
                    resolutions: layer.serverResolutions || layer.resolutions
                });
            },
            "WMTS": function(layer) {
                var enc = this.encoders.layers.HTTPRequest.call(this, layer);
                return Ext.apply(enc, {
                    type: 'WMTS',
                    layer: layer.layer,
                    version: layer.version,
                    requestEncoding: layer.requestEncoding,
                    tileOrigin: [layer.tileOrigin.lon, layer.tileOrigin.lat],
                    tileSize: [layer.tileSize.w, layer.tileSize.h],
                    style: layer.style,
                    formatSuffix: layer.formatSuffix,
                    dimensions: layer.dimensions,
                    params: layer.params,
                    maxExtent: (layer.tileFullExtent != null) ? layer.tileFullExtent.toArray() : layer.maxExtent.toArray(),
                    matrixSet: layer.matrixSet,
                    zoomOffset: layer.zoomOffset,
                    resolutions: layer.serverResolutions || layer.resolutions
                });
            },
            "KaMapCache": function(layer) {
                var enc = this.encoders.layers.KaMap.call(this, layer);
                return Ext.apply(enc, {
                    type: 'KaMapCache',
                    // group param is mandatory when using KaMapCache
                    group: layer.params['g'],
                    metaTileWidth: layer.params['metaTileSize']['w'],
                    metaTileHeight: layer.params['metaTileSize']['h']
                });
            },
            "KaMap": function(layer) {
                var enc = this.encoders.layers.HTTPRequest.call(this, layer);
                return Ext.apply(enc, {
                    type: 'KaMap',
                    map: layer.params['map'],
                    extension: layer.params['i'],
                    // group param is optional when using KaMap
                    group: layer.params['g'] || "",
                    maxExtent: layer.maxExtent.toArray(),
                    tileSize: [layer.tileSize.w, layer.tileSize.h],
                    resolutions: layer.serverResolutions || layer.resolutions
                });
            },
            "HTTPRequest": function(layer) {
                var enc = this.encoders.layers.Layer.call(this, layer);
                return Ext.apply(enc, {
                    baseURL: this.getAbsoluteUrl(layer.url instanceof Array ?
                        layer.url[0] : layer.url),
                    opacity: (layer.opacity != null) ? layer.opacity : 1.0,
                    singleTile: layer.singleTile
                });
            },
            "Image": function(layer) {
                var enc = this.encoders.layers.Layer.call(this, layer);
                return Ext.apply(enc, {
                    type: 'Image',
                    baseURL: this.getAbsoluteUrl(layer.getURL(layer.extent)),
                    opacity: (layer.opacity != null) ? layer.opacity : 1.0,
                    extent: layer.extent.toArray(),
                    pixelSize: [layer.size.w, layer.size.h],
                    name: layer.name
                });
            },
            "Vector": function(layer) {
                if(!layer.features.length) {
                    return;
                }

                var encFeatures = [];
                var encStyles = {};
                var features = layer.features;
                var featureFormat = new OpenLayers.Format.GeoJSON();
                var styleFormat = new OpenLayers.Format.JSON();
                var nextId = 1;
                var styleDict = {};
                var feature, style, dictKey, dictItem, styleName;
                for(var i=0, len=features.length; i<len; ++i) {
                    feature = features[i];
                    style = feature.style || layer.style ||
                    layer.styleMap.createSymbolizer(feature,
                        feature.renderIntent);
                    dictKey = styleFormat.write(style);
                    dictItem = styleDict[dictKey];
                    if(dictItem) {
                        //this style is already known
                        styleName = dictItem;
                    } else {
                        //new style
                        styleDict[dictKey] = styleName = nextId++;
                        if(style.externalGraphic) {
                            encStyles[styleName] = Ext.applyIf({
                                externalGraphic: this.getAbsoluteUrl(
                                    style.externalGraphic)}, style);
                        } else {
                            encStyles[styleName] = style;
                        }
                    }
                    var featureGeoJson = featureFormat.extract.feature.call(
                        featureFormat, feature);

                    featureGeoJson.properties = OpenLayers.Util.extend({
                        _gx_style: styleName
                    }, featureGeoJson.properties);

                    encFeatures.push(featureGeoJson);
                }
                var enc = this.encoders.layers.Layer.call(this, layer);
                return Ext.apply(enc, {
                    type: 'Vector',
                    styles: encStyles,
                    styleProperty: '_gx_style',
                    geoJson: {
                        type: "FeatureCollection",
                        features: encFeatures
                    },
                    name: layer.name,
                    opacity: (layer.opacity != null) ? layer.opacity : 1.0
                });
            },
            "Markers": function(layer) {
                var features = [];
                for (var i=0, len=layer.markers.length; i<len; i++) {
                    var marker = layer.markers[i];
                    var geometry = new OpenLayers.Geometry.Point(marker.lonlat.lon, marker.lonlat.lat);
                    var style = {externalGraphic: marker.icon.url,
                        graphicWidth: marker.icon.size.w, graphicHeight: marker.icon.size.h,
                        graphicXOffset: marker.icon.offset.x, graphicYOffset: marker.icon.offset.y};
                    var feature = new OpenLayers.Feature.Vector(geometry, {}, style);
                    features.push(feature);
            }
                var vector = new OpenLayers.Layer.Vector(layer.name);
                vector.addFeatures(features);
                var output = this.encoders.layers.Vector.call(this, vector);
                vector.destroy();
                return output;
            }
        },
        "legends": {
            "gx_wmslegend": function(legend, scale) {
                var enc = this.encoders.legends.base.call(this, legend);
                var icons = [];
                for(var i=1, len=legend.items.getCount(); i<len; ++i) {
                    var url = legend.items.get(i).url;
                    if(legend.useScaleParameter === true &&
                       url.toLowerCase().indexOf(
                           'request=getlegendgraphic') != -1) {
                        var split = url.split("?");
                        var params = Ext.urlDecode(split[1]);
                        params['SCALE'] = scale;
                        url = split[0] + "?" + Ext.urlEncode(params);
                    }
                    icons.push(this.getAbsoluteUrl(url));
                }
                enc[0].classes[0] = {
                    name: "",
                    icons: icons
                };
                return enc;
            },
            "gx_urllegend": function(legend) {
                var enc = this.encoders.legends.base.call(this, legend);
                enc[0].classes.push({
                    name: "",
                    icon: this.getAbsoluteUrl(legend.items.get(1).url)
                });
                return enc;
            },
            "base": function(legend){
                return [{
                    name: legend.getLabel(),
                    classes: []
                }];
            }
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/data/LayerStore.js
 * @include OpenLayers/Map.js
 */

/**
 * Create a panel container for a map. The map contained by this panel
 * will initially be zoomed to either the center and zoom level configured
 * by the `center` and `zoom` configuration options, or the configured
 * `extent`, or - if neither are provided - the extent returned by the
 * map's `getExtent()` method.
 *
 * Example:
 *
 *     var mappanel = Ext.create('GeoExt.panel.Map', {
 *         title: 'A sample Map',
 *         map: {
 *             // ...
 *             // optional, can be either
 *             //   - a valid OpenLayers.Map configuration or
 *             //   - an instance of OpenLayers.Map
 *         },
 *         center: '12.31,51.48',
 *         zoom: 6
 *     });
 *
 * A Map created with code like above is then ready to use as any other panel.
 * To have a fullscreen map application, you could e.g. add it to a viewport:
 *
 * Example:
 *
 *     Ext.create('Ext.container.Viewport', {
 *         layout: 'fit',
 *         items: [
 *             mappanel // our variable from above
 *         ]
 *     });
 *
 * @class GeoExt.panel.Map
 */
Ext.define('GeoExt.panel.Map', {
    extend:  Ext.panel.Panel ,
               
                                
      
    alias: 'widget.gx_mappanel',
    alternateClassName: 'GeoExt.MapPanel',

    statics: {
        /**
         * The first map panel found via an the Ext.ComponentQuery.query
         * manager.
         *
         * Convenience function for guessing the map panel of an application.
         * This can reliably be used for all applications that just have one map
         * panel in the viewport.
         *
         * @return {GeoExt.panel.Map}
         * @static
         */
        guess : function() {
            var candidates = Ext.ComponentQuery.query("gx_mappanel");
            return ((candidates && candidates.length > 0)
                ? candidates[0]
                : null);
        }
    },

    /**
     * A location for the initial map center.  If an array is provided, the
     * first two items should represent x & y coordinates. If a string is
     * provided, it should consist of a x & y coordinate seperated by a
     * comma.
     *
     * @cfg {OpenLayers.LonLat/Number[]/String} center
     */
    center: null,

    /**
     * An initial zoom level for the map.
     *
     * @cfg {Number} zoom
     */
    zoom: null,

    /**
     * An initial extent for the map (used if center and zoom are not
     * provided.  If an array, the first four items should be minx, miny,
     * maxx, maxy.
     *
     * @cfg {OpenLayers.Bounds/Number[]} extent
     */
    extent: null,

    /**
     * Set this to true if you want pretty strings in the MapPanel's state
     * keys. More specifically, layer.name instead of layer.id will be used
     * in the state keys if this option is set to true. But in that case
     * you have to make sure you don't have two layers with the same name.
     * Defaults to false.
     *
     * @cfg {Boolean} prettyStateKeys
     */
    /**
     * Whether we want the state key to be pretty. See
     * {@link #cfg-prettyStateKeys the config option prettyStateKeys} for
     * details.
     *
     * @property {Boolean} prettyStateKeys
     */
    prettyStateKeys: false,

    /**
     * A configured map or a configuration object for the map constructor.
     *
     * In most cases you will want your map to be configured with
     * `fallThrough: true`, as other settings affect the dragging behaviour of
     * overlayed `Ext.window.Window` instances in negative way. Such windows
     * cannot be smoothly dragged over the the map panel. If you do not provide
     * a map or map configuration object, the auto-created map will be
     * configured with `fallThrough` being `true`.
     *
     * Having `fallThrough` being `false` is a misconfiguration most of the
     * time, which is why we will issue a warning to the developer console if we
     * detect this setting.
     *
     * A configured map will be available after construction through the
     * {@link GeoExt.panel.Map#property-map} property.
     *
     * @cfg {OpenLayers.Map/Object} map
     */
    /**
     * A map or map configuration.
     *
     * @property {OpenLayers.Map/Object} map
     */
    map: null,

    /**
     * In order for child items to be correctly sized and positioned,
     * typically a layout manager must be specified through the layout
     * configuration option.
     *
     * @cfg {OpenLayers.Map/Object} layout
     */
    /**
     * A layout or layout configuration.
     *
     * @property {OpenLayers.Map/Object} layout
     */
    layout: 'fit',

    /**
     * The layers provided here will be added to this Map's
     * {@link #property-map}.
     *
     * @cfg {GeoExt.data.LayerStore/OpenLayers.Layer[]} layers
     */
    /**
     * A store containing {@link GeoExt.data.LayerModel gx_layer-model}
     * instances.
     *
     * @property {GeoExt.data.LayerStore} layers
     */
    layers: null,

    /**
     * Array of state events.
     *
     * @property {String[]} stateEvents
     * @private
     */
    stateEvents: [
        "aftermapmove",
        "afterlayervisibilitychange",
        "afterlayeropacitychange",
        "afterlayerorderchange",
        "afterlayernamechange",
        "afterlayeradd",
        "afterlayerremove"
    ],

    /**
     * Whether we already rendered an OpenLayers.Map in this panel. Will be
     * updated in #onResize, after the first rendering happened.
     *
     * @property {Boolean} mapRendered
     * @private
     */
    mapRendered: false,

    /**
     * Initializes the map panel. Creates an OpenLayers map if
     * none was provided in the config options passed to the
     * constructor.
     *
     * Such an auto-created map will be configured with
     *
     *     {
     *         allOverlays: true,
     *         fallThrough: true
     *     }
     *
     * See {@link GeoExt.panel.Map#cfg-map} for an explanation why we do this.
     *
     * @private
     */
    initComponent: function(){
        if(!(this.map instanceof OpenLayers.Map)) {
            this.map = new OpenLayers.Map(
                Ext.applyIf(this.map || {}, {
                    allOverlays: true,
                    fallThrough: true
                })
            );
        }

        if (this.map.fallThrough !== true) {
            this.warnMapFallThrough();
        }

        var layers  = this.layers;
        if(!layers || layers instanceof Array) {
            this.layers = Ext.create('GeoExt.data.LayerStore', {
                layers: layers,
                map: this.map.layers.length > 0 ? this.map : null
            });
        }

        if (Ext.isString(this.center)) {
            this.center = OpenLayers.LonLat.fromString(this.center);
        } else if(Ext.isArray(this.center)) {
            this.center = new OpenLayers.LonLat(this.center[0], this.center[1]);
        }
        if (Ext.isString(this.extent)) {
            this.extent = OpenLayers.Bounds.fromString(this.extent);
        } else if(Ext.isArray(this.extent)) {
            this.extent = OpenLayers.Bounds.fromArray(this.extent);
        }

        this.callParent(arguments);

        // The map is renderer and its size is updated when we receive
        // "resize" events.
        this.on('resize', this.onResize, this);

        //TODO This should be handled by a LayoutManager
        this.on("afterlayout", function() {
            //TODO remove function check when we require OpenLayers > 2.11
            if (typeof this.map.getViewport === "function") {
                this.items.each(function(cmp) {
                    if (typeof cmp.addToMapPanel === "function") {
                        cmp.getEl().appendTo(this.body);
                    }
                }, this);
            }
        }, this);

        /**
         * Fires after the map is moved.
         *
         * @event aftermapmove
         */
        /**
         * Fires after a layer changed visibility.
         *
         * @event afterlayervisibilitychange
         */
        /**
         * Fires after a layer changed opacity.
         *
         * @event afterlayeropacitychange
         */
        /**
         * Fires after a layer order changed.
         *
         * @event afterlayerorderchange
         */
        /**
         * Fires after a layer name changed.
         *
         * @event afterlayernamechange
         */
        /**
         * Fires after a layer added to the map.
         *
         * @event afterlayeradd
         */
        /**
         * Fires after a layer removed from the map.
         *
         * @event afterlayerremove
         */

        // bind various listeners to the corresponding OpenLayers.Map-events
        this.map.events.on({
            "moveend": this.onMoveend,
            "changelayer": this.onChangelayer,
            "addlayer": this.onAddlayer,
            "removelayer": this.onRemovelayer,
            scope: this
        });
    },

    /**
     * Logs a warning to the console (if one is present) that tells the user to
     * set the `fallThrough` property of an OpenLayers.Map to true when this map
     * is being used inside of a GeoExt.panel.Map.
     *
     * @private
     */
    warnMapFallThrough: function(){
        Ext.log({
            level: 'warn',
            msg: 'It is recommended to construct a GeoExt.panel.Map with' +
                ' OpenLayers.Map#fallThrough == true. This way dragging' +
                ' interactions with floating components (e.g.' +
                ' Ext.window.Window) on top of the map are smoother.'
        });
    },

    /**
     * The "moveend" listener bound to the
     * {@link GeoExt.panel.Map#property-map}.
     *
     * @param {Object} e
     * @private
     */
    onMoveend: function(e) {
        this.fireEvent("aftermapmove", this, this.map, e);
    },

    /**
     * The "changelayer" listener bound to the
     * {@link GeoExt.panel.Map#property-map}.
     *
     * @param {Object} e
     * @private
     */
    onChangelayer: function(e) {
        var map = this.map;
        if (e.property) {
            if (e.property === "visibility") {
                this.fireEvent("afterlayervisibilitychange", this, map, e);
            } else if (e.property === "order") {
                this.fireEvent("afterlayerorderchange", this, map, e);
            } else if (e.property === "nathis") {
                this.fireEvent("afterlayernathischange", this, map, e);
            } else if (e.property === "opacity") {
                this.fireEvent("afterlayeropacitychange", this, map, e);
            }
        }
    },

    /**
     * The "addlayer" listener bound to the
     * {@link GeoExt.panel.Map#property-map}.
     *
     * @param {Object} e
     * @private
     */
    onAddlayer: function() {
        this.fireEvent("afterlayeradd");
    },

    /**
     * The "removelayer" listener bound to the
     * {@link GeoExt.panel.Map#property-map}.
     *
     * @param {Object} e
     * @private
     */
    onRemovelayer: function() {
        this.fireEvent("afterlayerremove");
    },

    /**
     * Private method called after the panel has been rendered or after it
     * has been laid out by its parent's layout.
     *
     * @private
     */
    onResize: function() {
        var map = this.map;
        if(!this.mapRendered && this.body.dom !== map.div) {
            // the map has not been rendered yet
            map.render(this.body.dom);
            this.mapRendered = true;

            this.layers.bind(map);

            if (map.layers.length > 0) {
                this.setInitialExtent();
            } else {
                this.layers.on("add", this.setInitialExtent, this,
                               {single: true});
            }
        } else {
            map.updateSize();
        }
    },

    /**
     * Set the initial extent of this panel's map.
     *
     * @private
     */
    setInitialExtent: function() {
        var map = this.map;
        if (!map.getCenter()) {
            if (this.center || this.zoom ) {
                // center and/or zoom?
                map.setCenter(this.center, this.zoom);
            } else if (this.extent instanceof OpenLayers.Bounds) {
                // extent
                map.zoomToExtent(this.extent, true);
            }else {
                map.zoomToMaxExtent();
            }
        }
    },

    /**
     * Returns a state of the Map as keyed Object. Depending on the point in
     * time this method is being called, the following keys will be available:
     *
     * * `x`
     * * `y`
     * * `zoom`
     *
     * And for all layers present in the map the object will contain the
     * following keys
     *
     * * `visibility_<XXX>`
     * * `opacity_<XXX>`
     *
     * The &lt;XXX&gt; suffix is either the title or id of the layer record, it
     * can be influenced by setting #prettyStateKeys to `true` or `false`.
     *
     * @return {Object}
     * @private
     */
    getState: function() {
        var me = this,
            map = me.map,
            state = me.callParent(arguments) || {},
            layer;

        // Ext delays the call to getState when a state event
        // occurs, so the MapPanel may have been destroyed
        // between the time the event occurred and the time
        // getState is called
        if(!map) {
            return;
        }

        // record location and zoom level
        var center = map.getCenter();
        // map may not be centered yet, because it may still have zero
        // dimensions or no layers
        center && Ext.applyIf(state, {
            "x": center.lon,
            "y": center.lat,
            "zoom": map.getZoom()
        });

        me.layers.each(function(modelInstance) {
            layer = modelInstance.getLayer();
            layerId = this.prettyStateKeys
                   ? modelInstance.get('title')
                   : modelInstance.get('id');
            state = me.addPropertyToState(state, "visibility_" + layerId,
                layer.getVisibility());
            state = me.addPropertyToState(state, "opacity_" + layerId,
                (layer.opacity === null) ? 1 : layer.opacity);
        }, me);

        return state;
    },

    /**
     * Apply the state provided as an argument.
     *
     * @param {Object} state The state to apply.
     * @private
     */
    applyState: function(state) {
        var me = this;
            map = me.map;
        // if we get strings for state.x, state.y or state.zoom
        // OpenLayers will take care of converting them to the
        // appropriate types so we don't bother with that
        me.center = new OpenLayers.LonLat(state.x, state.y);
        me.zoom = state.zoom;

        // set layer visibility and opacity
        var layer, layerId, visibility, opacity;
        me.layers.each(function(layerRec) {
            layer = layerRec.getLayer();
            layerId = me.prettyStateKeys ? layer.name : layer.id;
            visibility = state["visibility_" + layerId];
            if(visibility !== undefined) {
                // convert to boolean
                visibility = (/^true$/i).test(visibility);
                if(layer.isBaseLayer) {
                    if(visibility) {
                        map.setBaseLayer(layer);
                    }
                } else {
                    layer.setVisibility(visibility);
                }
            }
            opacity = state["opacity_" + layerId];
            if(opacity !== undefined) {
                layer.setOpacity(opacity);
            }
        });
    },

    /**
     * Check if an added item has to take separate actions
     * to be added to the map.
     * See e.g. the GeoExt.slider.Zoom or GeoExt.slider.LayerOpacity
     *
     * @private
     */
    onBeforeAdd: function(item) {
        if(Ext.isFunction(item.addToMapPanel)) {
            item.addToMapPanel(this);
        }
        this.callParent(arguments);
    },

    /**
     * Private method called during the destroy sequence.
     *
     * @private
     */
    beforeDestroy: function() {
        if(this.map && this.map.events) {
            this.map.events.un({
                "moveend": this.onMoveend,
                "changelayer": this.onChangelayer,
                scope: this
            });
        }
        // if the map panel was passed a map instance, this map instance
        // is under the user's responsibility
        if(!this.initialConfig.map ||
           !(this.initialConfig.map instanceof OpenLayers.Map)) {
            // we created the map, we destroy it
            if(this.map && this.map.destroy) {
                this.map.destroy();
            }
        }
        delete this.map;
        this.callParent(arguments);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Geometry/Polygon.js
 * @include OpenLayers/Geometry/LinearRing.js
 * @include OpenLayers/Geometry/Point.js
 * @include OpenLayers/Feature/Vector.js
 * @include GeoExt/panel/Map.js
 */

/**
 * Provides a representation of a print page for
 * {@link GeoExt.data.MapfishPrintProvider}. The extent of the page is stored as
 * `OpenLayers.Feature.Vector`. Widgets can use this to display the print
 * extent on the map.
 *
 * @class GeoExt.data.PrintPage
 */
Ext.define('GeoExt.data.PrintPage', {
    extend:  Ext.util.Observable ,
               
                          
      

    /**
     * The print provider to use with this page.
     *
     * @cfg {GeoExt.data.MapfishPrintProvider} printProvider
     */

    /**
     * @property {GeoExt.data.MapfishPrintProvider} printProvider
     * @private
     */
    printProvider: null,

    /**
     * Feature representing the page extent. To get the extent of the print page
     * for a specific map, use #getPrintExtent. Read-only.
     *
     * @property {OpenLayers.Feature.Vector} feature
     */
    feature: null,

    /**
     * The current center of the page. Read-only.
     *
     * @property {OpenLayers.LonLat} center
     */
    center: null,

    /**
     * The current scale record of the page. Read-only.
     *
     * @property {Ext.data.Record} scale
     */
    scale: null,

    /**
     * The current rotation of the page. Read-only.
     *
     * @property {Float} rotation
     */
    rotation: 0,

    /**
     * Key-value pairs of additional parameters that the printProvider will send
     * to the print service for this page.
     *
     * @cfg {Object} customParams
     */

    /**
     * Key-value pairs of additional parameters that the printProvider will send
     * to the print service for this page.
     *
     * @property {Object} customParams
     */
    customParams: null,

    /**
     * Private constructor override.
     *
     * @private
     */
    constructor: function(config) {
        this.initialConfig = config;
        Ext.apply(this, config);

        if(!this.customParams) {
            this.customParams = {};
        }

        /**
         * Triggered when any of the page properties have changed
         *
         * Listener arguments:
         *
         *  * printPage - {@link GeoExt.data.PrintPage} this printPage.
         *  * modifications - {Object} Object with one or more of
         *      `scale`, `center` and `rotation`, notifying
         *      listeners of the changed properties.
         *
         * @event change
         */

        this.callParent(arguments);

        this.feature = new OpenLayers.Feature.Vector(
            new OpenLayers.Geometry.Polygon([
                new OpenLayers.Geometry.LinearRing([
                    new OpenLayers.Geometry.Point(-1, -1),
                    new OpenLayers.Geometry.Point(1, -1),
                    new OpenLayers.Geometry.Point(1, 1),
                    new OpenLayers.Geometry.Point(-1, 1)
                ])
            ])
        );

        if(this.printProvider.capabilities) {
            this.setScale(this.printProvider.scales.getAt(0));
        } else {
            this.printProvider.on({
                "loadcapabilities": function() {
                    this.setScale(this.printProvider.scales.getAt(0));
                },
                scope: this,
                single: true
            });
        }
        this.printProvider.on({
            "layoutchange": this.onLayoutChange,
            scope: this
        });
    },

    /**
     * Gets this page's print extent for the provided map.
     *
     * @param {OpenLayers.Map/GeoExt.MapPanel} map The map to get the print
     *     extent for.
     * @return {OpenLayers.Bounds}
     */
    getPrintExtent: function(map) {
        map = map instanceof GeoExt.MapPanel ? map.map : map;
        return this.calculatePageBounds(this.scale, map.getUnits());
    },

    /**
     *
     * Updates the page geometry to match a given scale. Since this takes the
     * current layout of the printProvider into account, this can be used to
     * update the page geometry feature when the layout has changed.
     *
     * @param {Ext.data.Record} scale The new scale record.
     * @param {String} units map units to use for the scale calculation.
     *     Optional if the `feature` is on a layer which is added to a map.
     *     If not found, "dd" will be assumed.
     */
    setScale: function(scale, units) {

        var bounds = this.calculatePageBounds(scale, units);
        var geom = bounds.toGeometry();
        var rotation = this.rotation;
        if(Ext.isDefined(rotation) && rotation != 0) {
            geom.rotate(-rotation, geom.getCentroid());
        }
        this.updateFeature(geom, {scale: scale});
    },

    /**
     * Moves the page extent to a new center.
     *
     * @param {OpenLayers.LonLat} center The new center.
     */
    setCenter: function(center) {
        var geom = this.feature.geometry;
        var oldCenter = geom.getBounds().getCenterLonLat();

        var dx = center.lon - oldCenter.lon;
        var dy = center.lat - oldCenter.lat;
        geom.move(dx, dy);
        this.updateFeature(geom, {center: center});
    },

    /**
     * Sets a new rotation for the page geometry.
     *
     * @param {Float} rotation The new rotation.
     * @param {Boolean} force If set to true, the rotation will also be
     *     set when the layout does not support it. Default is false.
     */
    setRotation: function(rotation, force) {
        if(force || this.printProvider.layout.get("rotation") === true) {
            var geom = this.feature.geometry;
            geom.rotate(this.rotation - rotation, geom.getCentroid());
            this.updateFeature(geom, {rotation: rotation});
        }
    },

    /**
     * Fits the page layout to a map or feature extent. If the map extent has
     * not been centered yet, this will do nothing.
     *
     * Available options:
     *
     * * mode - `String` [closest/printer/screen] How to calculate the print
     *     extent? If `closest` the closest matching print extent will be
     *     chosen. If `printer`, the chosen print extent will be the closest one
     *     that can show the entire `fitTo` extent on the printer. If `screen`,
     *     it will be the closest one that is entirely visible inside the
     *     `fitTo` extent. Default is `printer`.
     *
     *  @param {GeoExt.MapPanel/OpenLayers.Map/OpenLayers.Feature.Vector} fitTo
     *      The map or feature to fit the page to.
     *  @param {Object} options Additional options to determine how to fit
     *
     */
    fit: function(fitTo, options) {
        options = options || {};

        var map = fitTo, extent;
        if(fitTo instanceof GeoExt.panel.Map) {
            map = fitTo.map;
        } else if(fitTo instanceof OpenLayers.Feature.Vector) {
            map = fitTo.layer.map;
            extent = fitTo.geometry.getBounds();
        }
        if(!extent) {
            extent = map.getExtent();
            if(!extent) {
                return;
            }
        }
        this._updating = true;
        var center = extent.getCenterLonLat();
        this.setCenter(center);
        var units = map.getUnits();
        var scale = this.printProvider.scales.getAt(0);
        var closest = Number.POSITIVE_INFINITY;
        var mapWidth = extent.getWidth();
        var mapHeight = extent.getHeight();

        this.printProvider.scales.each(function(rec) {
            var bounds = this.calculatePageBounds(rec, units);

            if (options.mode == "closest") {

                var diff =
                    Math.abs(bounds.getWidth() - mapWidth) +
                    Math.abs(bounds.getHeight() - mapHeight);
                if (diff < closest) {
                    closest = diff;
                    scale = rec;
                }
            } else {
                var contains = options.mode == "screen" ?
                    !extent.containsBounds(bounds) :
                    bounds.containsBounds(extent);
                if (contains || (options.mode == "screen" && !contains)) {
                    scale = rec;
                }

                return contains;
            }
        }, this);

        this.setScale(scale, units);
        delete this._updating;
        this.updateFeature(this.feature.geometry, {
            center: center,
            scale: scale
        });
    },

    /**
     * Updates the page feature with a new geometry and notifies listeners
     * of changed page properties.
     *
     * @param {OpenLayers.Geometry} geometry New geometry for the #feature.
     *     If not provided, the existing geometry will be left unchanged.
     * @param {Object} mods An object with one or more of #scale,
     *     #center and #rotation, reflecting the page properties to update.
     * @private
     */
    updateFeature: function(geometry, mods) {
        var f = this.feature;
        var modified = f.geometry !== geometry;
        geometry.id = f.geometry.id;
        f.geometry = geometry;

        if(!this._updating) {
            for(var property in mods) {
                if(mods[property] === this[property]) {
                    delete mods[property];
                } else {
                    this[property] = mods[property];
                    modified = true;
                }
            }
            Ext.apply(this, mods);

            f.layer && f.layer.drawFeature(f);
            modified && this.fireEvent("change", this, mods);
        }
    },

    /**
     * Calculates the page bounds for a given scale.
     *
     * @param {Ext.data.Record} scale Scale record to calculate the page
     *     bounds for.
     * @param {String} units Map units to use for the scale calculation.
     *     Optional if #feature is added to a layer which is added to a
     *     map. If not provided, "dd" will be assumed.
     * @return `OpenLayers.Bounds`
     * @private
     */
    calculatePageBounds: function(scale, units) {
        var s = scale.get("value");
        var f = this.feature;
        var geom = this.feature.geometry;
        var center = geom.getBounds().getCenterLonLat();

        var size = this.printProvider.layout.get("size");

        var units = units ||
            (f.layer && f.layer.map && f.layer.map.getUnits()) ||
            "dd";
        var unitsRatio = OpenLayers.INCHES_PER_UNIT[units];
        var w = size.width / 72 / unitsRatio * s / 2;
        var h = size.height / 72 / unitsRatio * s / 2;

        return new OpenLayers.Bounds(center.lon - w, center.lat - h,
            center.lon + w, center.lat + h);
    },

    /**
     * Handler for the printProvider's layoutchange event.
     *
     * @private
     */
    onLayoutChange: function() {
        if(this.printProvider.layout.get("rotation") === false) {
            this.setRotation(0, true);
        }
        // at init time the print provider triggers layoutchange
        // before loadcapabilities, i.e. before we set this.scale
        // to the first scale in the scales store, we need to
        // guard against that
        if (this.scale) {
            this.setScale(this.scale);
        }
    },

    /**
     * Unbinds layoutchange listener of the #printProvider.
     *
     * @private
     */
    destroy: function() {
        this.printProvider.un("layoutchange", this.onLayoutChange, this);
    }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 * @include OpenLayers/Symbolizer/Raster.js
 */

/**
 * A specific model for Raster Symbolizer classifications.
 *
 * Preconfigured with an Ajax proxy and a JSON reader.
 *
 * @class GeoExt.data.RasterStyleModel
 */
Ext.define('GeoExt.data.RasterStyleModel',{
    extend:  Ext.data.Model ,
                
                               
                         
      
    idProperty: "filter",
    fields: [
        {name: "symbolizers", mapping: function(v) {
            return {
                fillColor: v.color,
                fillOpacity: v.opacity,
                stroke: false
            };
        }, defaultValue: null},
        {name: "filter", mapping: "quantity", type: "float", sortType: 'asFloat', sortDir: 'ASC'},
        {name: "label", mapping: function(v) {
            // fill label with quantity if empty
            return v.label || v.quantity;
        }}
    ],
    proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            root: 'colorMap'
        }
    },
    listeners:{
        idchanged:function(rec){
            for(var i=0;i<rec.stores.length;i++){
                var store = rec.stores[i];
                store.sort();
            }
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * The model for scale values.
 *
 * @class GeoExt.data.ScaleModel
 */
Ext.define('GeoExt.data.ScaleModel', {
    extend:  Ext.data.Model ,
               
                                
                               
                        
      
    alias: 'model.gx_scale',
    fields: [
        {name: "level"},
        {name: "resolution"},
        {name: "scale"}
    ],
    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Util.js
 * @requires GeoExt/data/ScaleModel.js
 * @include GeoExt/panel/Map.js
 */

/**
 * A store that contains a cache of available zoom levels.  The store can
 * optionally be kept synchronized with an {OpenLayers.Map} or
 * GeoExt.panel.Map object.
 *
 * @class GeoExt.data.ScaleStore
 */
Ext.define('GeoExt.data.ScaleStore', {
               
                                 
                          
      
    extend:  Ext.data.Store ,
    model: 'GeoExt.data.ScaleModel',

    /**
     * Optional map or map panel from which to derive scale values.
     *
     * @cfg {OpenLayers.Map/GeoExt.panel.Map}
     */
    map: null,

    /**
     * Construct a ScaleStore from a configuration.  The ScaleStore accepts
     * some custom parameters addition to the fields accepted by Ext.Store.
     *
     * @private
     */
    constructor: function(config) {
        config = config || {};
        var map = (config.map instanceof GeoExt.panel.Map ? config.map.map : config.map);
        delete config.map;
        this.callParent([config]);
        if (map) {
            this.bind(map);
        }
    },

    /**
     * Bind this store to a map; that is, maintain the zoom list in sync with
     * the map's current configuration.  If the map does not currently have a
     * set scale list, then the store will remain empty until the map is
     * configured with one.
     *
     * @param {GeoExt.panel.Map/OpenLayers.Map} map Map to which we should bind.
     */
    bind: function(map, options) {
        this.map = (map instanceof GeoExt.panel.Map ? map.map : map);
        this.map.events.register('changebaselayer', this, this.populateFromMap);
        if (this.map.baseLayer) {
            this.populateFromMap();
        } else {
            this.map.events.register('addlayer', this, this.populateOnAdd);
        }
    },

    /**
     * Un-bind this store from the map to which it is currently bound.  The
     * currently stored zoom levels will remain, but no further changes from
     * the map will affect it.
     */
    unbind: function() {
        if (this.map) {
            if (this.map.events) {
                this.map.events.unregister('addlayer', this, this.populateOnAdd);
                this.map.events.unregister('changebaselayer', this, this.populateFromMap);
            }
            delete this.map;
        }
    },

    /**
     * This method handles the case where we have `#bind` called on a
     * not-fully-configured map so that the zoom levels can be detected when a
     * baselayer is finally added.
     *
     * @param {Object} evt
     * @private
     */
    populateOnAdd: function(evt) {
        if (evt.layer.isBaseLayer) {
            this.populateFromMap();
            this.map.events.unregister('addlayer', this, this.populateOnAdd);
        }
    },

    /**
     * This method actually loads the zoom level information from the
     * OpenLayers.Map and converts it to Ext Records.
     *
     * @private
     */
    populateFromMap: function() {
        var zooms = [];
        var resolutions = this.map.baseLayer.resolutions;
        var units = this.map.baseLayer.units;

        for (var i=resolutions.length-1; i >= 0; i--) {
            var res = resolutions[i];
            zooms.push({
                level: i,
                resolution: res,
                scale: OpenLayers.Util.getScaleFromResolution(res, units)
            });
        }

        this.loadData(zooms);
    },

    /**
     * Unregisters listeners by calling #unbind prior to destroying.
     *
     * @private
     */
    destroy: function() {
        this.unbind();
        this.callParent(arguments);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Format/CQL.js
 * @requires GeoExt/Version.js
 */

/**
 * A specific model for CQL Style Rules.
 *
 * Preconfigured with an Ajax proxy and a JSON reader.
 *
 * @class GeoExt.data.VectorStyleModel
 */
Ext.define('GeoExt.data.VectorStyleModel', {
    extend :  Ext.data.Model ,
                
                              
                        
      
    fields : [{
        name : "elseFilter",
        defaultValue : null
    }, {
        name : "label",
        mapping : "title",
        type : 'string'
    }, "name", "description", "minScaleDenominator", "maxScaleDenominator", {
        name : "symbolizers",
        convert : function(symbolizers, rec) {
            //symbolizers should be an array of OpenLayers.Symbolizer objects
            symbolizers = ( symbolizers instanceof Array) ? symbolizers : [symbolizers];
            for(var i = 0; i < symbolizers.length; i++) {
                var symbolizer = symbolizers[i];
                //due to the way that the initial data provided to a store is processed,
                //the instanceof test no longer works and we need to clone the symbolizer
                //for it to be recognized as a symbolizer class again
                if(!( symbolizer instanceof OpenLayers.Symbolizer) && symbolizer.CLASS_NAME && symbolizer.clone) {
                    symbolizers[i] = symbolizer.clone();
                }
            }
            return symbolizers;
        },
        defaultValue : null
    }, {
        name : "filter",
        convert : function(filter) {
            if( typeof filter === "string") {
                filter = filter ? OpenLayers.Format.CQL.prototype.read(filter) : null;
            }
            return filter;
        },
        defaultValue : null
    }],
    proxy : {
        type : 'memory',
        reader : {
            type : 'json',
            root : "rules"
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 * 
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txttxt for the full
 * text of the license.
 */

/**
 * A smart store that creates records for client-side rendered legends. If
 * its store is configured with an {OpenLayers.Style2} instance as `data`,
 * each record will represent a rule of the Style, and the store will be
 * configured with `symbolizers` (Array of {OpenLayers.Symbolizer}),
 * `filter` ({OpenLayers.Filter}), `label` (String, the rule's title),
 * `name` (String), `description` (String), `elseFilter` (Boolean),
 * `minScaleDenominator` (Number) and `maxScaleDenominator` (Number)
 * fields. If the store's `data` is an {OpenLayers.Symbolizer.Raster}
 * instance, records will represent its ColorMap entries, and the available
 * fields will only be `symbolizers` (object literal with `color` and
 * `opacity` properties from the ColorMapEntry, and stroke set to false),
 * `filter` (String, the ColorMapEntry's quantity) and `label` (String).
 *
 * NOTE: Calling `commitChanges` on the store that is populated with
 * this reader will fail with OpenLayers 2.11 - it requires at least revision
 * https://github.com/openlayers/openlayers/commit/1db5ac3cbe874317968f78832901d6ef887ecca6
 * from 2011-11-28 of OpenLayers.
 *
 * Sample code to create a store that reads from an OpenLayers.Style2
 * object:
 *
 *    var store = Ext.create('GeoExt.data.StyleStore',{
 *        data: myStyle // OpenLayers.Style2 or OpenLayers.Symbolizer.Raster
 *    });
 *
 * @class GeoExt.data.StyleStore
 */
Ext.define('GeoExt.data.StyleStore', {
    extend:  Ext.data.Store ,
               
                                       
                                      
      
    alias: 'store.gx_style',
    constructor: function(config){
        config = Ext.apply({}, config);
        if(config.data && !config.model){
            if (config.data instanceof OpenLayers.Symbolizer.Raster) {
                config.model = 'GeoExt.data.RasterStyleModel';
                config.sorters = [{
                    property: 'filter',
                    direction: 'ASC',
                    root: 'data'
                }];
            } else {
                config.model = 'GeoExt.data.VectorStyleModel';
            }            
        }
        this.callParent([config]);
    }
});
/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Format/WFSCapabilities.js
 * @include OpenLayers/Protocol/WFS.js
 * @include OpenLayers/Strategy/Fixed.js
 * @include OpenLayers/Layer/Vector.js
 * @requires GeoExt/Version.js
 */

/**
 * Data reader class to create {GeoExt.data.WfsCapabilitiesLayerModel[]}
 * from a WFS GetCapabilities response.
 *
 * @class GeoExt.data.reader.WfsCapabilities
 */
Ext.define('GeoExt.data.reader.WfsCapabilities', {
    alternateClassName: [
        'GeoExt.data.reader.WFSCapabilities',
        'GeoExt.data.WFSCapabilitiesReader'
    ],
    extend:  Ext.data.reader.Json ,
    alias: 'reader.gx_wfscapabilities',
               
                        
      
    /**
     * Creates new Reader.
     *
     * @param {Object} [config] Config object.
     */
    constructor: function(config) {
        if (!this.model) {
            this.model = 'GeoExt.data.WfsCapabilitiesLayerModel';
        }
        this.callParent([config]);
        if (!this.format) {
            this.format = new OpenLayers.Format.WFSCapabilities();
        }
    },

    /**
     * Gets the records.
     *
     * @param {Object} request The XHR object which contains the parsed XML
     *     document.
     * @return {Object} A data block which is used by an Ext.data.Store
     *     as a cache of Ext.data.Model objects.
     */
    getResponseData: function(request) {
        var data = request.responseXML;
        if(!data || !data.documentElement) {
            data = request.responseText;
        }
        return this.readRecords(data);
    },

    /**
     * Create a data block containing Ext.data.Records from an XML document.
     *
     * @param {DOMElement/String/Object} data A document element or XHR
     *     response string.  As an alternative to fetching capabilities data
     *     from a remote source, an object representing the capabilities can
     *     be provided given that the structure mirrors that returned from the
     *     capabilities parser.
     * @return  {Object} A data block which is used by an Ext.data.Store
     *     as a cache of Ext.data.Model objects.
     * @private
     */
    readRecords: function(data) {
        if(typeof data === "string" || data.nodeType) {
            data = this.format.read(data);
        }

        var featureTypes = data.featureTypeList.featureTypes;
        var fields = this.getFields();

        var featureType, metadata, field, v, parts, layer;
        var layerOptions, protocolOptions;

        var wfs11version = 1.1,
            url,
            opMeta;
        if (parseFloat(data.version) >= wfs11version) {
            // WFS 1.1.0 needs special treatment
            opMeta = data.operationsMetadata;
            url = opMeta && opMeta.GetFeature.dcp.http.post[0].url;
        } else {
            url = data.capability.request.getfeature.href.post;
        }

        var protocolDefaults = {
            url: url
        };

        var records = [];

        for(var i=0, lenI=featureTypes.length; i<lenI; i++) {
            featureType = featureTypes[i];
            if(featureType.name) {
                metadata = {};

                for(var j=0, lenJ=fields.length; j<lenJ; j++) {
                    field = fields[j];
                    v = featureType[field.name];
                    metadata[field.name] = v;
                }

                metadata['name'] = featureType.name;
                metadata['featureNS'] = featureType.featureNS;

                protocolOptions = {
                    featureType: featureType.name,
                    featureNS: featureType.featureNS
                };
                if(this.protocolOptions) {
                    Ext.apply(protocolOptions, this.protocolOptions,
                        protocolDefaults);
                } else {
                    Ext.apply(protocolOptions, {}, protocolDefaults);
                }

                layerOptions = {
                    metadata: metadata,
                    protocol: new OpenLayers.Protocol.WFS(protocolOptions),
                    strategies: [new OpenLayers.Strategy.Fixed()],
                    projection: featureType.srs
                };
                var metaLayerOptions = this.layerOptions;
                if (metaLayerOptions) {
                    Ext.apply(layerOptions, Ext.isFunction(metaLayerOptions) ?
                        metaLayerOptions() : metaLayerOptions);
                }

                layer = new OpenLayers.Layer.Vector(
                    featureType.title || featureType.name,
                    layerOptions
                );

                records.push(layer);
            }
        }
        return this.callParent([records]);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/data/reader/WfsCapabilities.js
 */

/**
 * The model for WFS layers coming from a WFS GetCapabilities document.
 *
 * @class GeoExt.data.WfsCapabilitiesLayerModel
 */
Ext.define('GeoExt.data.WfsCapabilitiesLayerModel',{
    extend:  GeoExt.data.LayerModel ,
    alternateClassName: [
        'GeoExt.data.WFSCapabilitiesModel',
        'GeoExt.data.WfsCapabilitiesModel'
    ],
                                                     
    alias: 'model.gx_wfscapabilities',
    fields: [
        {name: "name", type: "string", mapping: "metadata.name"},
        {name: "namespace", type: "string", mapping: "metadata.featureNS"},
        {name: "abstract", type: "string", mapping: "metadata.abstract"}
    ],
    proxy: {
        type: 'ajax',
        reader: {
            type: 'gx_wfscapabilities'
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/data/reader/WfsCapabilities.js
 * @requires GeoExt/data/WfsCapabilitiesLayerModel.js
 * @requires GeoExt/data/OwsStore.js
 */

/**
 * Small helper class to make creating stores for remote WFS layer data easier.
 * 
 * The store is pre-configured with a built-in Ext.data.proxy.Ajax and
 * GeoExt.data.reader.WfsCapabilities. The proxy is configured to allow caching
 * and issues requests via GET.
 * 
 * If you require some other proxy/reader combination then you'll have to
 * configure this with your own proxy or create a basic
 * GeoExt.data.LayerStore and configure as needed.
 *
 * @class GeoExt.data.WfsCapabilitiesLayerStore
 */
Ext.define('GeoExt.data.WfsCapabilitiesLayerStore',{
    extend:  GeoExt.data.OwsStore ,
                                                     
    model: 'GeoExt.data.WfsCapabilitiesLayerModel',
    alternateClassName: [
        'GeoExt.data.WFSCapabilitiesStore',
        'GeoExt.data.WfsCapabilitiesStore'
    ]
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Format/WMC.js
 * @requires GeoExt/Version.js
 */

/**
 * Data reader class to create an array of records from a WMC document.
 *
 * @class GeoExt.data.reader.Wmc
 */
Ext.define('GeoExt.data.reader.Wmc', {
    alternateClassName: ['GeoExt.data.WMCReader'],
    extend:  Ext.data.reader.Json ,
    alias: 'reader.gx_wmc',
               
                        
      

    /**
     * Creates new Reader.
     *
     * @param {Object} [config] Config object.
     */
    constructor: function(config) {
        if (!this.model) {
            this.model = 'GeoExt.data.WmcLayerModel';
        }
        this.callParent([config]);
        if (!this.format) {
            this.format = new OpenLayers.Format.WMC();
        }
    },

    /**
     * Gets the records.
     *
     * @param {Object} request The XHR object which contains the parsed XML
     *     document.
     * @return {Object} A data block which is used by an Ext.data.Store
     *     as a cache of Ext.data.Model objects.
     */
    getResponseData: function(request) {
        var data = request.responseXML;
        if(!data || !data.documentElement) {
            data = request.responseText;
        }
        return this.readRecords(data);
    },

    /**
     * Create a data block containing Ext.data.Records from an XML document.
     *
     * @param {DOMElement/String/Object} data A document element or XHR
     *     response string.  As an alternative to fetching capabilities data
     *     from a remote source, an object representing the capabilities can
     *     be provided given that the structure mirrors that returned from the
     *     capabilities parser.
     * @return  {Object} A data block which is used by an Ext.data.Store
     *     as a cache of Ext.data.Model objects.
     * @private
     */
    readRecords: function(data) {
        if(typeof data === "string" || data.nodeType) {
            data = this.format.read(data);
        }
        var layersContext = data ? data.layersContext : undefined;
        var records = [];

        if(layersContext) {
            var fields = this.getFields();
            var i, lenI, j, lenJ, layerContext, metadata, field, layer;
            for (i = 0, lenI = layersContext.length; i < lenI; i++) {
                layerContext = layersContext[i];
                layerContext.metadata = layerContext.metadata || {};
                for(j = 0, lenJ = fields.length; j < lenJ; j++){
                    field = fields[j];
                    layerContext.metadata[field.name] = layerContext[field.name];
                }
                layerContext.metadata.name = layerContext.name;
                layer = this.format.getLayerFromContext(layerContext);
                records.push(layer);
            }
        }

        return this.callParent([records]);
    }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/data/reader/Wmc.js
 */

/**
 * The model for WMS layers coming from a Web Map Context document.
 *
 * @class GeoExt.data.WmcLayerModel
 */
Ext.define('GeoExt.data.WmcLayerModel',{
    extend:  GeoExt.data.LayerModel ,
    alternateClassName: ['GeoExt.data.WMCLayerModel'],
                                         
    alias: 'model.gx_wmc',
    fields: [
        {name: "name", type: "string", mapping: "metadata.name"},
        {name: "abstract", type: "string", mapping: "metadata.abstract"},
        {name: "metadataURL", type: "string", mapping: "metadata.metadataURL"},
        {name: "queryable", type: "boolean", mapping: "metadata.queryable"},
        {name: "formats", mapping: "metadata.formats"}, // array
        {name: "styles", mapping: "metadata.styles"} // array
    ],
    proxy: {
        type: 'ajax',
        reader: {
            type: 'gx_wmc'
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Format/WMSCapabilities.js
 * @include OpenLayers/Layer/WMS.js
 * @include OpenLayers/Util.js
 * @requires GeoExt/Version.js
 */

/**
 * Data reader class to create GeoExt.data.WmsCapabilitiesLayerModel[]
 * from a WMS GetCapabilities response.
 *
 * @class GeoExt.data.reader.WmsCapabilities
 */
Ext.define('GeoExt.data.reader.WmsCapabilities', {
    alternateClassName: [
        'GeoExt.data.reader.WMSCapabilities',
        'GeoExt.data.WMSCapabilitiesReader'
    ],
    extend:  Ext.data.reader.Json ,
    alias: 'reader.gx_wmscapabilities',
               
                        
      
    /**
     * Creates new Reader.
     *
     * @param {Object} [config] Config object.
     */
    constructor: function(config) {
        if (!this.model) {
            this.model = 'GeoExt.data.WmsCapabilitiesLayerModel';
        }
        this.callParent([config]);
        if (!this.format) {
            this.format = new OpenLayers.Format.WMSCapabilities();
        }
    },

    /**
     * Should we keep the raw parsed result? If true, the result will be stored
     * under the #raw property. Default is false.
     * @cfg {Boolean}
     */
    keepRaw: false,

    /**
     * The raw parsed result, only set if #keepRaw is true.
     * @cfg {Object}
     */
    raw: null,

    /**
     * CSS class name for the attribution DOM elements.
     * Element class names append "-link", "-image", and "-title" as
     * appropriate.  Default is "gx-attribution".
     *
     * @cfg {String}
     */
    attributionCls: "gx-attribution",

    /**
     * Gets the records.
     *
     * @param {Object} request The XHR object which contains the parsed XML
     *     document.
     * @return {Object} A data block which is used by an Ext.data.Store
     *     as a cache of Ext.data.Model objects.
     */
    getResponseData: function(request) {
        var data = request.responseXML;
        if(!data || !data.documentElement) {
            data = request.responseText;
        }
        return this.readRecords(data);
    },

    /**
     * @param {String[]} formats An array of service exception format strings.
     * @return {String} The (supposedly) best service exception format.
     * @private
     */
    serviceExceptionFormat: function(formats) {
        if (OpenLayers.Util.indexOf(formats,
            "application/vnd.ogc.se_inimage")>-1) {
            return "application/vnd.ogc.se_inimage";
        }
        if (OpenLayers.Util.indexOf(formats,
            "application/vnd.ogc.se_xml")>-1) {
            return "application/vnd.ogc.se_xml";
        }
        return formats[0];
    },

    /**
     * @param {Object} layer The layer's capabilities object.
     * @return {String} The (supposedly) best mime type for requesting
     *     tiles.
     * @private
     */
    imageFormat: function(layer) {
        var formats = layer.formats;
        if (layer.opaque &&
            OpenLayers.Util.indexOf(formats, "image/jpeg")>-1) {
            return "image/jpeg";
        }
        if (OpenLayers.Util.indexOf(formats, "image/png")>-1) {
            return "image/png";
        }
        if (OpenLayers.Util.indexOf(formats, "image/png; mode=24bit")>-1) {
            return "image/png; mode=24bit";
        }
        if (OpenLayers.Util.indexOf(formats, "image/gif")>-1) {
            return "image/gif";
        }
        return formats[0];
    },

    /**
     * @param {Object} layer The layer's capabilities object.
     * @return {Boolean} The TRANSPARENT param.
     * @private
     */
    imageTransparent: function(layer) {
        return layer.opaque == undefined || !layer.opaque;
    },

    /**
     * @private
     */
    destroyReader: function() {
        var me = this;
        delete me.raw;
        this.callParent();
    },

    /**
     * Create a data block containing Ext.data.Records from an XML document.
     *
     * @param {DOMElement/String/Object} data A document element or XHR
     *     response string.  As an alternative to fetching capabilities data
     *     from a remote source, an object representing the capabilities can
     *     be provided given that the structure mirrors that returned from the
     *     capabilities parser.
     * @return  {Object} A data block which is used by an Ext.data.Store
     *     as a cache of Ext.data.Model objects.
     * @private
     */
    readRecords: function(data) {
        if (Ext.isArray(data)) {
            return this.callParent(data);
        }
        if(typeof data === "string" || data.nodeType) {
            data = this.format.read(data);
        }
        if (!!data.error) {
            Ext.Error.raise({msg: "Error parsing WMS GetCapabilities", arg: data.error});
        }
        if (this.keepRaw) {
            this.raw = data;
        }
        var version = data.version;
        var capability = data.capability || {};
        var url = capability.request && capability.request.getmap &&
            capability.request.getmap.href;
        var layers = capability.layers;
        var formats = capability.exception ? capability.exception.formats : [];
        var exceptions = this.serviceExceptionFormat(formats);
        var records = [];

        if(url && layers) {
            var fields = this.getFields();
            var layer, metadata, options, params, field, v;

            for(var i=0, lenI=layers.length; i<lenI; i++){
                layer = layers[i];
                if(layer.name) {
                    metadata = {};
                    for(var j=0, lenJ=fields.length; j<lenJ; j++) {
                        field = fields[j];
                        metadata[field.name] = layer[field.name];
                    }
                    metadata['name'] = layer.name;
                    options = {
                        attribution: layer.attribution ?
                            this.attributionMarkup(layer.attribution) :
                            undefined,
                        minScale: layer.minScale,
                        maxScale: layer.maxScale,
                        metadata: metadata
                    };
                    if(this.layerOptions) {
                        Ext.apply(options, this.layerOptions);
                    }
                    params = {
                            layers: layer.name,
                            exceptions: exceptions,
                            format: this.imageFormat(layer),
                            transparent: this.imageTransparent(layer),
                            version: version
                    };
                    if (this.layerParams) {
                        Ext.apply(params, this.layerParams);
                    }
                    layer = new OpenLayers.Layer.WMS(
                        layer.title || layer.name, url, params, options
                    );
                    records.push(layer);
                }
            }
        }
        return this.callParent([records]);
    },

    /**
     * Generates attribution markup using the Attribution metadata
     * from WMS Capabilities.
     *
     * @param {Object} attribution The attribution property of the layer
     *     object as parsed from a WMS Capabilities document
     * @return {String} HTML markup to display attribution information.
     * @private
     */
    attributionMarkup : function(attribution){
        var markup = [];

        if (attribution.logo){
            markup.push("<img class='"+this.attributionCls+"-image' "
                        + "src='" + attribution.logo.href + "' />");
        }

        if (attribution.title) {
            markup.push("<span class='"+ this.attributionCls + "-title'>"
                        + attribution.title
                        + "</span>");
        }

        if(attribution.href){
            for(var i = 0; i < markup.length; i++){
                markup[i] = "<a class='" +
                    this.attributionCls +
                    "-link' " +
                    "href=" +
                    attribution.href +
                    ">" +
                    markup[i] +
                    "</a>";
            }
        }

        return markup.join(" ");
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/data/reader/WmsCapabilities.js
 */

/**
 * The model for WMS layers coming from a WMS GetCapabilities document.
 *
 * @class GeoExt.data.WmsCapabilitiesLayerModel
 */
Ext.define('GeoExt.data.WmsCapabilitiesLayerModel',{
    extend:  GeoExt.data.LayerModel ,
    alternateClassName: [
        'GeoExt.data.WMSCapabilitiesModel',
        'GeoExt.data.WmsCapabilitiesModel'
    ],
                                                     
    alias: 'model.gx_wmscapabilities',
    fields: [
        {name: "name", type: "string", mapping: "metadata.name"},
        {name: "abstract", type: "string", mapping: "metadata.abstract"},
        {name: "queryable", type: "boolean", mapping: "metadata.queryable"},
        {name: "opaque", type: "boolean", mapping: "metadata.opaque"},
        {name: "noSubsets", type: "boolean", mapping: "metadata.noSubsets"},
        {name: "cascaded", type: "int", mapping: "metadata.cascaded"},
        {name: "fixedWidth", type: "int", mapping: "metadata.fixedWidth"},
        {name: "fixedHeight", type: "int", mapping: "metadata.fixedHeight"},
        {name: "minScale", type: "float", mapping: "metadata.minScale"},
        {name: "maxScale", type: "float", mapping: "metadata.maxScale"},
        {name: "prefix", type: "string", mapping: "metadata.prefix"},
        {name: "attribution", type: "string"},
        {name: "formats", mapping: "metadata.formats"}, // array
        {name: "infoFormats", mapping: "metadata.infoFormats"}, //array
        {name: "styles", mapping: "metadata.styles"}, // array
        {name: "srs", mapping: "metadata.srs"}, // object
        {name: "dimensions", mapping: "metadata.dimensions"}, // object
        {name: "bbox", mapping: "metadata.bbox"}, // object
        {name: "llbbox", mapping: "metadata.llbbox"}, // array
        {name: "keywords", mapping: "metadata.keywords"}, // array
        {name: "identifiers", mapping: "metadata.identifiers"}, // object
        {name: "authorityURLs", mapping: "metadata.authorityURLs"}, // object
        {name: "metadataURLs", mapping: "metadata.metadataURLs"} // array
    ],
    proxy: {
        type: 'ajax',
        reader: {
            type: 'gx_wmscapabilities'
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/data/reader/WmsCapabilities.js
 * @requires GeoExt/data/WmsCapabilitiesLayerModel.js
 * @requires GeoExt/data/OwsStore.js
 */

/**
 * Small helper class to make creating stores for remote WMS layer data
 * easier. The store is pre-configured with a built-in
 * {Ext.data.proxy.Ajax} and GeoExt.data.reader.WmsCapabilities.
 * The proxy is configured to allow caching and issues requests via GET.
 * If you require some other proxy/reader combination then you'll have to
 * configure this with your own proxy.
 *
 * @class GeoExt.data.WmsCapabilitiesLayerStore
 */
Ext.define('GeoExt.data.WmsCapabilitiesLayerStore',{
    extend:  GeoExt.data.OwsStore ,
                                                     
    model: 'GeoExt.data.WmsCapabilitiesLayerModel',
    alternateClassName: [
        'GeoExt.data.WMSCapabilitiesStore',
        'GeoExt.data.WmsCapabilitiesStore'
    ]
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Format/WMSDescribeLayer.js
 * @requires GeoExt/Version.js
 */

/**
 * Data reader class to create an array of layer description objects from a WMS
 * DescribeLayer response.
 *
 * @class GeoExt.data.reader.WmsDescribeLayer
 */
Ext.define('GeoExt.data.reader.WmsDescribeLayer', {
    alternateClassName: ['GeoExt.data.reader.WMSDescribeLayer', 'GeoExt.data.WMSCapabilitiesReader'],
    extend:  Ext.data.reader.Json ,
    alias: 'reader.gx_wmsdescribelayer',
               
                        
      
    /**
     * Should we keep the raw parsed result? If true, the result will be stored
     * under the #raw property. Default is false.
     * @cfg {Boolean}
     */
    keepRaw: false,
    /**
     * The raw parsed result, only set if #keepRaw is true.
     * @cfg {Object}
     */
    raw: null,
    /**
     * Creates new Reader.
     *
     * @param {Object} config (optional) Config object.
     */
    constructor: function(config) {
        if (!this.model) {
            this.model = 'GeoExt.data.WmsDescribeLayerModel';
        }
        this.callParent([config]);
        if (!this.format) {
            this.format = new OpenLayers.Format.WMSDescribeLayer();
        }
    },

    /**
     * Gets the records.
     *
     * @param {Object} request The XHR object which contains the parsed XML
     *     document.
     * @return {Object} A data block which is used by an Ext.data.Store
     *     as a cache of Ext.data.Model objects.
     */
    getResponseData: function(request) {
        var data = request.responseXML;
        if(!data || !data.documentElement) {
            data = request.responseText;
        }
        return this.readRecords(data);
    },

    /**
     * Create a data block containing Ext.data.Records from an XML document.
     *
     * @param {DOMElement/String/Object} data A document element or XHR
     *     response string.  As an alternative to fetching capabilities data
     *     from a remote source, an object representing the capabilities can
     *     be provided given that the structure mirrors that returned from the
     *     capabilities parser.
     * @return {Object} A data block which is used by an Ext.data.Store
     *     as a cache of Ext.data.Model objects.
     */
    readRecords: function(data) {
        if(typeof data === "string" || data.nodeType) {
            data = this.format.read(data);
        }
        if (!!data.error) {
            Ext.Error.raise({msg: "Error parsing WMS DescribeLayer", arg: data.error});
        }
        if (this.keepRaw) {
            this.raw = data;
        }
        return this.callParent([data]);
    },

    /**
     * @private
     */
    destroyReader: function() {
        var me = this;
        delete me.raw;
        this.callParent();
    }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/data/reader/WmsDescribeLayer.js
 */

/**
 * The model for the structure returned by SLD WMS DescribeLayer.
 *
 * @class GeoExt.data.WmsDescribeLayerModel
 */
Ext.define('GeoExt.data.WmsDescribeLayerModel',{
    extend:  Ext.data.Model ,
               
                              
                                             
      
    alias: 'model.gx_wmsdescribelayer',
    fields: [
        {name: "layerName", type: "string"},
        {name: "owsType", type: "string"},
        {name: "owsURL", type: "string"},
        {name: "typeName", type: "string"}
    ],
    proxy: {
        type: 'ajax',
        reader: {
            type: 'gx_wmsdescribelayer'
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/data/reader/WmsDescribeLayer.js
 * @requires GeoExt/data/WmsDescribeLayerModel.js
 * @requires GeoExt/data/OwsStore.js
 */

/**
 * Small helper class to make creating stores for remote WMS layer description
 * easier. The store is pre-configured with a built-in Ext.data.proxy.Ajax and
 * GeoExt.data.reader.WmsDescribeLayer.
 *
 * The proxy is configured to allow caching and issues requests via GET.
 * If you require some other proxy/reader combination then you'll have to
 * configure this with your own proxy.
 *
 * @class GeoExt.data.WmsDescribeLayerStore
 */
Ext.define('GeoExt.data.WmsDescribeLayerStore',{
    extend:  GeoExt.data.OwsStore ,
                                                      
    model: 'GeoExt.data.WmsDescribeLayerModel',
    alternateClassName: ['GeoExt.data.WMSDescribeLayerStore']
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * A data proxy for use with OpenLayers.Protocol objects.
 *
 * @class GeoExt.data.proxy.Protocol
 */
Ext.define('GeoExt.data.proxy.Protocol', {
    extend:  Ext.data.proxy.Server ,
               
                        
      
    alias: 'proxy.gx_protocol',

    /**
     * The protocol used to fetch features.
     *
     * @cfg {OpenLayers.Protocol}
     */
    protocol: null,

    /**
     * Abort any previous request before issuing another.
     *
     * @cfg {Boolean}
     */
    abortPrevious: true,

    /**
     * Should options.params be set directly on options before passing it into
     * the protocol's read method?
     *
     * @cfg {Boolean}
     */
    setParamsAsOptions: false,

    /**
     * The response returned by the read call on the protocol.
     *
     * @property {OpenLayers.Protocol.Response}
     * @private
     */
    response: null,

    /**
     * Send the request.
     *
     * @param {Ext.data.Operation} operation The Ext.data.Operation object.
     * @param {Function} callback The callback function to call when the
     *     operation has completed.
     * @param {Object} scope The scope in which to execute the callback.
     * @private
     */
    doRequest: function(operation, callback, scope) {
        var me = this,
            params = Ext.applyIf(operation.params || {}, me.extraParams || {}),
            request;

        //copy any sorters, filters etc into the params so they can be sent over the wire
        params = Ext.applyIf(params, me.getParams(operation));

        var o = {
            params: params || {},
            operation: operation,
            request: {
                callback: callback,
                scope: scope,
                arg: operation.arg
            },
            reader: this.getReader()
        };
        var cb = OpenLayers.Function.bind(this.loadResponse, this, o);
        if (this.abortPrevious) {
            this.abortRequest();
        }
        var options = {
            params: params,
            callback: cb,
            scope: this
        };
        Ext.applyIf(options, operation.arg);
        if (this.setParamsAsOptions === true) {
            Ext.applyIf(options, options.params);
            delete options.params;
        }
        this.response = this.protocol.read(options);
    },

    /**
     * Called to abort any ongoing request.
     *
     * @private
     */
    abortRequest: function() {
        if (this.response) {
            this.protocol.abort(this.response);
            this.response = null;
        }
    },

    /**
     * Handle response from the protocol.
     *
     * @param {Object} o
     * @param {OpenLayers.Protocol.Response} response
     * @private
     */
    loadResponse: function(o, response) {
        var me = this;
        var operation = o.operation;
        var scope = o.request.scope;
        var callback = o.request.callback;
        if (response.success()) {
            var result = o.reader.read(response.features || response);
            Ext.apply(operation, {
                response: response,
                resultSet: result
            });

            operation.commitRecords(result.records);
            operation.setCompleted();
            operation.setSuccessful();
        } else {
            me.setException(operation, response);
            me.fireEvent('exception', this, response, operation);
        }
        if (typeof callback == 'function') {
            callback.call(scope || me, operation);
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Form.js
 */

/**
 * A specific `Ext.form.action.Action` to be used when using a form to
 * trigger search requests using an `OpenLayers.Protocol`.
 *
 * When run this action builds an `OpenLayers.Filter` from the form
 * and passes this filter to its protocol's read method. The form fields
 * must be named after a specific convention, so that an appropriate
 * `OpenLayers.Filter.Comparison` filter is created for each
 * field.
 *
 * For example a field with the name `foo__like` would result in an
 * `OpenLayers.Filter.Comparison` of type
 * `OpenLayers.Filter.Comparison.LIKE` being created.
 *
 * Here is the convention:
 *
 * * `<name>__eq: OpenLayers.Filter.Comparison.EQUAL_TO`
 * * `<name>__ne: OpenLayers.Filter.Comparison.NOT_EQUAL_TO`
 * * `<name>__lt: OpenLayers.Filter.Comparison.LESS_THAN`
 * * `<name>__le: OpenLayers.Filter.Comparison.LESS_THAN_OR_EQUAL_TO`
 * * `<name>__gt: OpenLayers.Filter.Comparison.GREATER_THAN`
 * * `<name>__ge: OpenLayers.Filter.Comparison.GREATER_THAN_OR_EQUAL_TO`
 * * `<name>__like: OpenLayers.Filter.Comparison.LIKE`
 *
 * In most cases you would not directly create `GeoExt.form.action.Search`
 * objects, but use `GeoExt.form.FormPanel` instead.
 *
 * Sample code showing how to use a GeoExt Search Action with an Ext
 * form panel:
 *
 *     var formPanel = Ext.create('Ext.form.Panel', {
 *          renderTo: "formpanel",
 *          items: [{
 *              xtype: "textfield",
 *              name: "name__like",
 *              value: "mont"
 *          }, {
 *              xtype: "textfield",
 *              name: "elevation__ge",
 *              value: "2000"
 *          }]
 *      });
 *
 *      var searchAction = Ext.create('GeoExt.form.action.Search', {
 *          form: formPanel.getForm(),
 *          protocol: new OpenLayers.Protocol.WFS({
 *              url: "http://publicus.opengeo.org/geoserver/wfs",
 *              featureType: "tasmania_roads",
 *              featureNS: "http://www.openplans.org/topp"
 *          }),
 *          abortPrevious: true
 *      });
 *
 *     formPanel.getForm().doAction(searchAction, {
 *          callback: function(response) {
 *              // response.features includes the features read
 *              // from the server through the protocol
 *          }
 *      });
 *
 * @class GeoExt.form.action.Search
 */
Ext.define('GeoExt.form.action.Search', {
    extend:  Ext.form.Action ,
    alternateClassName: 'GeoExt.form.SearchAction',
    alias: 'formaction.search',
                              

    /**
     * The action type string.
     *
     * @property {String}
     * @private
     */
    type: "search",

    /**
     * A reference to the response resulting from the search request. Read-only.
     *
     * @property {OpenLayers.Protocol.Response} response
     */

    /**
     * The protocol to use for search requests.
     *
     * @cfg {OpenLayers.Protocol} protocol
     */

    /**
     * The protocol.
     *
     * @property {OpenLayers.Protocol} protocol
     */

    /**
     * (optional) Extra options passed to the protocol's read method.
     *
     * @cfg {Object} readOptions
     */

    /**
     * (optional) Callback function called when the response is received.
     *
     * @cfg {Function} callback
     */

    /**
     * (optional) Scope {@link #callback}.
     *
     * @cfg {Object} scope
     */

    /**
     * If set to true, the abort method will be called on the protocol
     * if there's a pending request. Default is `false`.
     *
     * @cfg {Boolean} abortPrevious
     */

    /**
     * Run the action.
     *
     * @private
     */
    run: function() {
        var form = this.form,
            f = GeoExt.Form.toFilter(form, this.logicalOp, this.wildcard);
        if(this.clientValidation === false || form.isValid()){

            if (this.abortPrevious && form.prevResponse) {
                this.protocol.abort(form.prevResponse);
            }

            this.form.prevResponse = this.protocol.read(
                Ext.applyIf({
                    filter: f,
                    callback: this.handleResponse,
                    scope: this
                }, this.readOptions)
            );

        } else if(this.clientValidation !== false){
            // client validation failed
            this.failureType = Ext.form.action.Action.CLIENT_INVALID;
            form.afterAction(this, false);
        }
    },

    /**
     * Handle the response to the search query.
     *
     * @param {OpenLayers.Protocol.Response} response The response object.
     * @private
     */
    handleResponse: function(response) {
        var form = this.form;
        form.prevResponse = null;
        this.response = response;
        if(response.success()) {
            form.afterAction(this, true);
        } else {
            form.afterAction(this, false);
        }
        if(this.callback) {
            this.callback.call(this.scope, response);
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/form/action/Search.js
 */

/**
 * A specific `Ext.form.Basic` whose `#doAction` method creates
 * a GeoExt.form.action.Search if it is passed the string
 * "search" as its first argument.
 *
 * In most cases one would not use this class directly, but
 * `GeoExt.form.Panel` instead.
 *
 * @class GeoExt.form.Basic
 */
Ext.define('GeoExt.form.Basic', {
    extend:  Ext.form.Basic ,
                                            

    /**
     * The protocol to use for search requests.
     *
     * @cfg {OpenLayers.Protocol} protocol
     */
    /**
     * The protocol.
     *
     * @property {OpenLayers.Protocol} protocol
     */

    /**
     * The response return by a call to  protocol.read method.
     *
     * @property {OpenLayers.Protocol.Response} prevResponse
     * @private
     */

    /**
     * Tells if pending requests should be aborted when a new action
     * is performed. Default is `true`.
     *
     * @cfg {Boolean}
     */
    autoAbort: true,

    /**
     * Performs the action, if the string "search" is passed as the
     * first argument then a {@link GeoExt.form.action.Search} is created.
     *
     * @param {String/Ext.form.action.Action} action Either the name
     *     of the action or a `Ext.form.action.Action` instance.
     * @param {Object} options The options passed to the Action constructor.
     * @return {GeoExt.form.Basic} This form.
     *
     */
    doAction: function(action, options) {
        if(action == "search") {
            options = Ext.applyIf(options || {}, {
                form: this,
                protocol: this.protocol,
                abortPrevious: this.autoAbort
            });
            action = Ext.create('GeoExt.form.action.Search', options);
        }
        return this.callParent([action, options]);
    },

    /**
     * Shortcut to do a search action.
     *
     * @param {Object} options The options passed to the Action constructor.
     * @return {GeoExt.form.Basic} This form.
     */
    search: function(options) {
        return this.doAction("search", options);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/form/Basic.js
 */

/**
 * A specific {@link Ext.form.Panel} whose internal form is a
 * {@link GeoExt.form.Basic} instead of {@link Ext.form.Basic}.
 * One would use this form to do search requests through
 * an `OpenLayers.Protocol` object (`OpenLayers.Protocol.WFS`
 * for example).
 *
 * Look at {@link GeoExt.form.action.Search} to understand how
 * form fields must be named for appropriate filters to be
 * passed to the protocol.
 *
 * Sample code showing how to use a GeoExt form panel.
 *
 *     var formPanel = Ext.create('GeoExt.form.FormPanel', {
 *         renderTo: "formpanel",
 *         protocol: new OpenLayers.Protocol.WFS({
 *             url: "http://publicus.opengeo.org/geoserver/wfs",
 *             featureType: "tasmania_roads",
 *             featureNS: "http://www.openplans.org/topp"
 *         }),
 *         items: [{
 *             xtype: "textfield",
 *             name: "name__ilike",
 *             value: "mont"
 *         }, {
 *             xtype: "textfield",
 *             name: "elevation__ge",
 *             value: "2000"
 *         }],
 *         listeners: {
 *             actioncomplete: function(form, action) {
 *                 // this listener triggers when the search request
 *                 // is complete, the OpenLayers.Protocol.Response
 *                 // resulting from the request is available
 *                 // in "action.response"
 *             }
 *         }
 *     });
 *     formPanel.add({
 *         xtype: 'toolbar',
 *         items: [{
 *             text: "Search",
 *             handler: function() {
 *                 this.search();
 *             },
 *             scope: formPanel
 *         }]
 *     });
 *
 * @class GeoExt.form.Panel
 */
Ext.define('GeoExt.form.Panel', {
    extend:  Ext.form.Panel ,
                                    
    alias: 'widget.gx_form',

    /**
     * The protocol instance this form panel
     * is configured with, actions resulting from this form
     * will be performed through the protocol.
     *
     * @cfg {OpenLayers.Protocol} protocol
     */
    protocol: null,

    /**
     * Create the internal {@link GeoExt.form.Basic} instance.
     *
     * @return {GeoExt.form.Basic} The basic form.
     * @private
     */
    createForm: function() {
        return new GeoExt.form.Basic(this, Ext.applyIf({listeners: {}},
                                     this.initialConfig));
    },

    /**
     * Shortcut to the internal form's search method.
     *
     * @param {Object} options The options passed to the
     * GeoExt.form.action.Search constructor.
     *
     */
    search: function(options) {
        this.getForm().search(options);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/**
 * Creates a combo box that handles results from a geocoding service. By
 * default it uses OSM Nominatim, but it can be configured with a custom store
 * to use other services. If the user enters a valid address in the search
 * box, the combo's store will be populated with records that match the
 * address.  By default, records have the following fields:
 *
 * * name - `String` The formatted address.
 * * lonlat - `Array` Location matching address, for use with
 *     OpenLayers.LonLat.fromArray.
 * * bounds - `Array` Recommended viewing bounds, for use with
 *     OpenLayers.Bounds.fromArray.
 *
 * @class GeoExt.form.field.GeocoderComboBox
 */
Ext.define('GeoExt.form.field.GeocoderComboBox', {
    extend :  Ext.form.field.ComboBox ,
               
                           
                             
                              
      
    alias : 'widget.gx_geocodercombo',
    alternateClassName : 'GeoExt.form.GeocoderComboBox',

    /**
     * Text to display for an empty field (i18n).
     *
     * @cfg {String}
     */
    emptyText: "Search",

    /**
     * The map that will be controlled by
     * this GeoCoderComboBox. Only used if this component is not added as item
     * or toolbar item to a GeoExt.panel.Map.
     *
     * @cfg {GeoExt.panel.Map/OpenLayers.Map} map
     */
    /**
     * @property {OpenLayers.Map} map
     * @private
     */

    /**
     * The srs used by the geocoder service.
     *
     * @cfg {String/OpenLayers.Projection}
     */
    srs: "EPSG:4326",

    /**
     * The minimum zoom level to use when zooming to a location.
     * Not used when zooming to a bounding box.
     *
     * @cfg {Integer}
     */
    zoom: 10,

    /**
     * Delay before the search occurs in ms.
     *
     * @cfg {Number}
     */
    queryDelay: 100,

    /**
     * Field from selected record to use when the combo's
     * getValue method is called.  Default is "bounds". This field is
     * supposed to contain an array of [left, bottom, right, top] coordinates
     * for a bounding box or [x, y] for a location.
     *
     * @cfg {String}
     */
    valueField: "bounds",

    /**
     * The field to display in the combo box. Default is
     * "name" for instant use with the default store for this component.
     *
     * @cfg {String}
     */
    displayField: "name",

    /**
     * The field to get the location from. This field is supposed
     * to contain an array of [x, y] for a location. Default is "lonlat" for
     * instant use with the default store for this component.
     *
     * @cfg {String}
     */
    locationField: "lonlat",

    /**
     * URL template for querying the geocoding service. If a store is
     * configured, this will be ignored. Note that the queryParam will be used
     * to append the user's combo box input to the url.
     *
     * Default is "http://nominatim.openstreetmap.org/search?format=json", for
     * instant use with the OSM Nominatim geolocator. However, if you intend to
     * use that, note the [Nominatim Usage
     * Policy](http://wiki.openstreetmap.org/wiki/Nominatim_usage_policy).
     *
     * @cfg {String}
     */
    url: "http://nominatim.openstreetmap.org/search?format=json",

    /**
     * The query parameter for the user entered search text.
     * Default is "q" for instant use with OSM Nominatim.
     *
     * @cfg {String}
     */
    queryParam: "q",

    /**
     * Minimum number of entered characters to trigger a search.
     *
     * @cfg {Number}
     */
    minChars: 3,

    /**
     * The store used for this combo box. Default is a
     * store with a ScriptTagProxy and the url configured as :obj:`url`
     * property.
     *
     * @cfg {Ext.data.Store}
     */
    store: null,

    /**
     * Last center that was zoomed to after selecting a location in the combo
     * box.
     *
     * @property {OpenLayers.LonLat}
     * @private
     */
    center: null,

    /**
     * Last location provided by the geolocator.
     * Only set if layer is configured.
     *
     * @property {OpenLayers.Feature.Vector}
     * @private
     */
    locationFeature: null,

    initComponent: function() {
        if (this.map) {
            this.setMap(this.map);
        }
        if (Ext.isString(this.srs)) {
            this.srs = new OpenLayers.Projection(this.srs);
        }
        if (!this.store) {
            this.store = Ext.create("Ext.data.JsonStore", {
                root: null,
                fields: [
                    {name: "name", mapping: "display_name"},
                    {name: "bounds", convert: function(v, rec) {
                        var bbox = rec.raw.boundingbox;
                        return [bbox[2], bbox[0], bbox[3], bbox[1]];
                    }},
                    {name: "lonlat", convert: function(v, rec) {
                        return [rec.raw.lon, rec.raw.lat];
                    }}
                ],
                proxy: Ext.create("Ext.data.proxy.JsonP", {
                    url: this.url,
                    callbackKey: "json_callback"
                })
            });
        }

        this.on({
            added: this.findMapPanel,
            select: this.handleSelect,
            focus: function() {
                this.clearValue();
                this.removeLocationFeature();
            },
            scope: this
        });
        return this.callParent(arguments);
    },

    /**
     * Find the MapPanel somewhere up in the hierarchy and set the map.
     *
     * @private
     */
    findMapPanel: function() {
        var mapPanel = this.up('gx_mappanel');
        if (mapPanel) {
            this.setMap(mapPanel);
        }
    },

    /**
     * Zoom to the selected location, and also set a location marker if this
     * component was configured with a layer.
     *
     * @private
     */
    handleSelect: function(combo, rec) {
        if (!this.map) {
            this.findMapPanel();
        }
        var value = this.getValue();
        if (Ext.isArray(value)) {
            var mapProj = this.map.getProjectionObject();
            delete this.center;
            delete this.locationFeature;
            if (value.length === 4) {
                this.map.zoomToExtent(
                    OpenLayers.Bounds.fromArray(value)
                        .transform(this.srs, mapProj)
                );
            } else {
                this.map.setCenter(
                    OpenLayers.LonLat.fromArray(value)
                        .transform(this.srs, mapProj),
                    Math.max(this.map.getZoom(), this.zoom)
                );
            }
            rec = rec[0];
            this.center = this.map.getCenter();
            var lonlat = rec.get(this.locationField);
            if (this.layer && lonlat) {
                var geom = new OpenLayers.Geometry.Point(
                    lonlat[0], lonlat[1]).transform(this.srs, mapProj);
                this.locationFeature = new OpenLayers.Feature.Vector(geom, rec.data);
                this.layer.addFeatures([this.locationFeature]);
            }
        }
    },

    /**
     * Remove the location marker from the `layer` and destroy the
     * `#locationFeature`.
     *
     * @private
     */
    removeLocationFeature: function() {
        if (this.locationFeature) {
            this.layer.destroyFeatures([this.locationFeature]);
        }
    },

    /**
     * Handler for the map's moveend event. Clears the selected location
     * when the map center has changed.
     *
     * @private
     */
    clearResult: function() {
        if (this.center && !this.map.getCenter().equals(this.center)) {
            this.clearValue();
        }
    },

    /**
     * Set the `#map` for this instance.
     *
     * @param {GeoExt.panel.Map/OpenLayers.Map} map
     * @private
     */
    setMap: function(map) {
        if (map instanceof GeoExt.panel.Map) {
            map = map.map;
        }
        this.map = map;
        map.events.on({
            "moveend": this.clearResult,
            "click": this.removeLocationFeature,
            scope: this
        });
    },

    /**
     * Called by a MapPanel if this component is one of the items in the panel.
     * @param {GeoExt.panel.Map} panel
     *
     * @private
     */
    addToMapPanel: Ext.emptyFn,

    /**
     * Unbind various event listeners and deletes #map, #layer and #center
     * properties.
     *
     * @private
     */
    beforeDestroy: function() {
        if (this.map && this.map.events) {
            this.map.events.un({
                "moveend": this.clearResult,
                "click": this.removeLocationFeature,
                scope: this
            });
        }
        this.removeLocationFeature();
        delete this.map;
        delete this.layer;
        delete this.center;
        this.callParent(arguments);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/FeatureRenderer.js
 */

/**
 * An Ext.grid.column.Column pre-configured with a GeoExt.FeatureRenderer
 * 
 * @class GeoExt.grid.column.Symbolizer
 */
Ext.define('GeoExt.grid.column.Symbolizer', {
    extend:  Ext.grid.column.Column ,
    alternateClassName: 'GeoExt.grid.SymbolizerColumn',
    alias: ['widget.gx_symbolizercolumn'],
                                         

    /**
     * The default renderer Method for Features.
     */
    defaultRenderer: function(value, meta, record) {
        if (value) {
            var id = Ext.id();
            var symbolType = "Polygon";
            if (record) {
                var symbolType = "Line";
                var className = record.raw.geometry ? record.raw.geometry.CLASS_NAME : null;
                if (className == "OpenLayers.Geometry.Point" ||
                        className == "OpenLayers.Geometry.MultiPoint") {
                    symbolType = "Point";
                }
                else if (className == "OpenLayers.Geometry.Polygon" ||
                        className == "OpenLayers.Geometry.MultiPolygon") {
                    symbolType = "Polygon";
                }
            }
            window.setTimeout(function() {
                var ct = Ext.get(id);
                // ct for old field may not exist any more during a grid update
                if (ct) {
                    var renderer = Ext.create('GeoExt.FeatureRenderer', {
                        renderTo: ct,
                        symbolizers: value instanceof Array ? value : [value],
                        symbolType: symbolType
                    });
                }
            }, 0);
            meta.css = "gx-grid-symbolizercol";
            return Ext.String.format('<div id="{0}"></div>', id);
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/container/LayerLegend.js
 */

/**
 * A panel showing legends of all layers in a GeoExt.data.LayerStore.
 * Depending on the layer type, a legend renderer will be chosen.
 *
 * The LegendPanel will include legends for all the layers in the
 * `layerStore` it is configured with, unless the layer is configured with
 * `displayInLayerSwitcher: false`, or a layer record has a
 * `hideInLegend` field with a value of `true`. Additional filtering can
 * be done by configuring a `filter` on the LegendPanel.
 *
 * @class GeoExt.panel.Legend
 */
Ext.define('GeoExt.panel.Legend', {
    extend :  Ext.panel.Panel ,
                                               
    alias : 'widget.gx_legendpanel',
    alternateClassName : 'GeoExt.LegendPanel',

    /**
     * If false the LegendPanel will not listen to the add, remove and change
     * events of the LayerStore. So it will load with the initial state of
     * the LayerStore and not change anymore.
     *
     * @cfg {Boolean}
     */
    dynamic: true,

    /**
     * The layer store containing layers to be displayed in the legend
     * container. If not provided it will be taken from the MapPanel.
     *
     * @cfg {Ext.data.Store}
     */
    layerStore: null,

    /**
     * An array of preferred legend types.
     *
     * @cfg {Array}
     */
    preferredTypes: null,

    /**
     * A function, called in the scope of the legend panel, with a layer record
     * as argument. It is expected to return true for layers to be displayed, false
     * otherwise. By default, all layers will be displayed.
     *
     * @cfg {Function}
     * @param {Ext.data.Record} record
     * @return {boolean}
     */
    filter: function(record) {
        return true;
    },

    /**
     * Private method called when the legend panel is being rendered.
     *
     * @private
     */
    onRender: function() {
        this.callParent(arguments);

        if(!this.layerStore) {
            this.layerStore = GeoExt.panel.Map.guess().layers;
        }
        this.layerStore.each(function(record) {
            this.addLegend(record);
        }, this);
        if (this.dynamic) {
            this.layerStore.on({
                "add": this.onStoreAdd,
                "remove": this.onStoreRemove,
                "clear": this.onStoreClear,
                scope: this
            });
        }
    },

    /**
     * Private method to get the panel index for a layer represented by a
     * record.
     *
     * @param {Integer} index The index of the record in the store.
     * @return {Integer} The index of the sub panel in this panel.
     * @private
     */
    recordIndexToPanelIndex: function(index) {
        var store = this.layerStore;
        var count = store.getCount();
        var panelIndex = -1;
        var legendCount = this.items ? this.items.length : 0;
        var record, layer;
        for(var i=count-1; i>=0; --i) {
            record = store.getAt(i);
            layer = record.getLayer();
            var types = GeoExt.container.LayerLegend.getTypes(record);
            if(layer.displayInLayerSwitcher && types.length > 0 &&
                (store.getAt(i).get("hideInLegend") !== true)) {
                ++panelIndex;
                if(index === i || panelIndex > legendCount-1) {
                    break;
                }
            }
        }
        return panelIndex;
    },

    /**
     * Generate an element id that is unique to this panel/layer combo.
     *
     * @param {OpenLayers.Layer} layer
     * @returns {String}
     * @private
     */
    getIdForLayer: function(layer) {
        return this.id + "-" + layer.id;
    },

    /**
     * Private method called when a layer is added to the store.
     *
     * @param {Ext.data.Store} store The store to which the record(s) was added.
     * @param {Ext.data.Record} record The record object(s) corresponding
     *     to the added layers.
     * @param {Integer} index The index of the inserted record.
     * @private
     */
    onStoreAdd: function(store, records, index) {
        var panelIndex = this.recordIndexToPanelIndex(index+records.length-1);
        for (var i=0, len=records.length; i<len; i++) {
            this.addLegend(records[i], panelIndex);
        }
        this.doLayout();
    },

    /**
     * Private method called when a layer is removed from the store.
     *
     * @param {Ext.data.Store} store The store from which the record(s) was
     *     removed.
     * @param {Ext.data.Record} record The record object(s) corresponding
     *     to the removed layers.
     * @param {Integer} index The index of the removed record.
     * @private
     */
    onStoreRemove: function(store, record, index) {
        this.removeLegend(record);
    },

    /**
     * Remove the legend of a layer.
     *
     * @param {Ext.data.Record} record The record object from the layer
     *     store to remove.
     * @private
     */
    removeLegend: function(record) {
        if (this.items) {
            var legend = this.getComponent(this.getIdForLayer(record.getLayer()));
            if (legend) {
                this.remove(legend, true);
                this.doLayout();
            }
        }
    },

    /**
     * Private method called when a layer store is cleared.
     *
     * @param {Ext.data.Store} store The store from which was cleared.
     * @private
     */
    onStoreClear: function(store) {
        this.removeAllLegends();
    },

    /**
     * Remove all legends from this legend panel.
     *
     * @private
     */
    removeAllLegends: function() {
        this.removeAll(true);
        this.doLayout();
    },

    /**
     * Add a legend for the layer.
     *
     * @param {Ext.data.Record} record The record object from the layer
     *     store.
     * @param {Integer} index The position at which to add the legend.
     */
    addLegend: function(record, index) {
        if (this.filter(record) === true) {
            var layer = record.getLayer();
            index = index || 0;
            var legend;
            var types = GeoExt.container.LayerLegend.getTypes(record, this.preferredTypes);
            if(layer.displayInLayerSwitcher && !record.get('hideInLegend') && types.length > 0) {
                this.insert(index,       {
                    xtype: types[0],
                    id: this.getIdForLayer(layer),
                    layerRecord: record,
                    hidden: !((!layer.map && layer.visibility) ||
                        (layer.getVisibility() && layer.calculateInRange()))
                });
            }
        }
    },

    /**
     * Private method called during the destroy sequence.
     *
     * @private
     */
    onDestroy: function() {
        if(this.layerStore) {
            this.layerStore.un("add", this.onStoreAdd, this);
            this.layerStore.un("remove", this.onStoreRemove, this);
            this.layerStore.un("clear", this.onStoreClear, this);
        }
        this.callParent(arguments);
    }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Layer/Vector.js
 * @requires GeoExt/panel/Map.js
 * @requires GeoExt/data/MapfishPrintProvider.js
 * @include GeoExt/data/PrintPage.js
 */

/**
 * A GeoExt.panel.Map that controls scale and center of a print page. Based on
 * the current view (i.e. layers and extent) of a source map, this panel will be
 * sized according to the aspect ratio of the print page. As the user zooms
 * and pans in the `GeoExt.PrintMapPanel`, the print page will update
 * its scale and center accordingly. If the scale on the print page changes
 * (e.g. by setting it using a combo box with a
 * {@link GeoExt.plugins.PrintPageField}), the extent of the
 * {@link GeoExt.PrintMapPanel} will be updated to match the page bounds.
 *
 * The #zoom, #center and #extent config options will have no effect, as
 * they will be determined by the #sourceMap.
 *
 * A map with a "Print..." button. If clicked, a dialog containing a
 * PrintMapPanel will open, with a "Create PDF" button.
 *
 * Example:
 *
 *     var mapPanel = Ext.create("GeoExt.panel.Map", {
 *         renderTo: "map",
 *         layers: [
 *             new OpenLayers.Layer.WMS(
 *                 "Tasmania State Boundaries",
 *                 "http://demo.opengeo.org/geoserver/wms",
 *                 { layers: "topp:tasmania_state_boundaries" },
 *                 { singleTile: true }
 *             )
 *         ],
 *         center: [146.56, -41.56],
 *         zoom: 6,
 *         bbar: [{
 *             text: "Print...",
 *             handler: function() {
 *                 var printDialog = Ext.create("Ext.Window", {
 *                     autoHeight: true,
 *                     width: 350,
 *                     items: [
 *                         Ext.create("GeoExt.panel.PrintMap", {
 *                             sourceMap: mapPanel,
 *                             printProvider: {
 *                                 capabilities: printCapabilities
 *                             }
 *                         })
 *                     ],
 *                     bbar: [{
 *                         text: "Create PDF",
 *                         handler: function() {
 *                             printDialog.items.get(0).print();
 *                         }
 *                     }]
 *                 });
 *                 printDialog.show();
 *             }
 *         }]
 *     });
 *
 * @class GeoExt.panel.PrintMap
 */
Ext.define('GeoExt.panel.PrintMap', {
    extend :  GeoExt.panel.Map ,
               
                         
                                           
                               
      
    alias : 'widget.gx_printmappanel',
    alternateClassName : 'GeoExt.PrintMapPanel',

    /**
     * Optional configuration for the `OpenLayers.Map` object
     * that this PrintMapPanel creates. Useful e.g. to configure a map with a
     * custom set of controls, or to add a `preaddlayer` listener for
     * filtering out layer types that cannot be printed.
     *
     * @cfg {Object} map
     */

    /**
     * The map that is to be printed.
     *
     * @cfg {GeoExt.MapPanel/OpenLayers.Map} sourceMap
     */

    /**
     * @private
     * @property {OpenLayers.Map} sourceMap
     */
    sourceMap: null,

    /**
     * @cfg {GeoExt.data.MapfishPrintProvider/Object} printProvider
     * PrintProvider to use for printing. If an `Object` is provided, a new
     * PrintProvider will be created and configured with the object.
     *
     * The PrintMapPanel requires the printProvider's capabilities
     * to be available upon initialization. This means that a PrintMapPanel
     * configured with an `Object` as `printProvider` will only work
     * when `capabilities` is provided in the printProvider's
     * configuration object. If `printProvider` is provided as an instance
     * of {@link GeoExt.data.MapfishPrintProvider}, the capabilities must be
     * loaded before PrintMapPanel initialization.
     */

    /**
     * PrintProvider for this PrintMapPanel.
     *
     * @property {GeoExt.data.MapfishPrintProvider} printProvider
     */
    printProvider: null,

    /**
     * PrintPage for this PrintMapPanel. Read-only.
     *
     * @property {GeoExt.data.PrintPage} printPage
     */
    printPage: null,

    /**
     * If set to true, the printPage cannot be set to scales that
     * would generate a preview in this {@link GeoExt.PrintMapPanel} with a
     * completely different extent than the one that would appear on the
     * printed map. Default is false.
     *
     * @cfg {Boolean} limitScales
     */

    /**
     * A data store with a subset of the printProvider's scales. By default,
     * this contains all the scales of the printProvider.
     *
     * If `limitScales` is set to true, it will only contain print scales
     * that can properly be previewed with this :class:`GeoExt.PrintMapPanel`.
     *
     * @property {Ext.data.Store} previewScales
     */
    previewScales: null,

    /**
     * A location for the map center. **Do not set**, as this will be overridden
     * with the `sourceMap` center.
     *
     * @cfg {OpenLayers.LonLat/Number[]} center
     */
    center: null,

    /**
     * An initial zoom level for the map. **Do not set**, because the initial
     * extent will be determined by the `sourceMap`.
     *
     * @cfg {Number} zoom
     */
    zoom: null,

    /**
     * An initial extent for the map. **Do not set**, because the initial extent
     * will be determined by the `sourceMap`.
     *
     * @cfg {OpenLayers.Bounds/Number[]} extent
     */
    extent: null,

    /**
     * @private
     * @property {Number} currentZoom
     */
    currentZoom: null,

    /**
     * @private
     */
    initComponent: function() {
        if(this.sourceMap instanceof GeoExt.MapPanel) {
            this.sourceMap = this.sourceMap.map;
        }

        if (!this.map) {
            this.map = {};
        }
        Ext.applyIf(this.map, {
            projection: this.sourceMap.getProjection(),
            maxExtent: this.sourceMap.getMaxExtent(),
            maxResolution: this.sourceMap.getMaxResolution(),
            units: this.sourceMap.getUnits(),
            tileManager: null,
            allOverlays: true,
            fallThrough: true
        });
        if(!(this.printProvider instanceof GeoExt.data.MapfishPrintProvider)) {
            this.printProvider = Ext.create('GeoExt.data.MapfishPrintProvider',
                this.printProvider);
        }
        this.printPage = Ext.create('GeoExt.data.PrintPage', {
            printProvider: this.printProvider
        });

        this.previewScales = Ext.create('Ext.data.Store', {
            fields: [
                 {name: 'name', type: 'string'},
                 {name: 'value', type: 'int'}
            ],
            data: this.printProvider.scales.getRange()
        });

        this.layers = [];
        var layer;
        Ext.each(this.sourceMap.layers, function(layer) {
            if (layer.getVisibility() === true) {
                this.layers.push(layer.clone());
            }
        }, this);

        this.extent = this.sourceMap.getExtent();
        this.callParent(arguments);
    },

    /**
     * Calls the internal adjustSize-function and resizes
     * this {@link GeoExt.panel.PrintMap PrintMapPanel} due
     * to the needed size, defined by the current layout of the #printProvider.
     *
     * The Function was removed from Ext.Panel in ExtJS 4 and is
     * now implemented here.
     *
     * @private
     *
     */
    syncSize: function() {
        var s = this.adjustSize(this.map.size.w, this.map.size.h);
        this.setSize(s.width, s.height);
    },

    /**
     * Register various event listeners.
     *
     * @private
     */
    bind: function() {
        // we have to call syncSize here because of changed
        // rendering order in ExtJS4
        this.syncSize();

        this.printPage.on("change", this.fitZoom, this);
        this.printProvider.on("layoutchange", this.syncSize, this);
        this.map.events.register("moveend", this, this.updatePage);
        this.on("resize", function() {
            this.doComponentLayout();
            this.map.updateSize();
        }, this);

        this.printPage.fit(this.sourceMap);

        if (this.initialConfig.limitScales === true) {
            this.on("resize", this.calculatePreviewScales, this);
            this.calculatePreviewScales();
        }
    },

    /**
     * Private method called after the panel has been rendered.
     *
     * @private
     */
    afterRender: function() {
        var me = this,
            listenerSpec = {
                "afterlayout": {
                    fn: me.bind,
                    scope: me,
                    single: true
                }
            };

        me.callParent(arguments);
        me.doComponentLayout();

        // binding will happen when either we or our container are finished
        // doing the layout.
        if (!me.ownerCt) {
            me.on(listenerSpec);
        } else {
            me.ownerCt.on(listenerSpec);
        }
    },

    /**
     * Private override - sizing this component always takes the aspect ratio
     * of the print page into account.
     *
     * @param {Number} width If not provided or 0, initialConfig.width will
     *     be used.
     * @param {Number} height If not provided or 0, initialConfig.height
     *     will be used.
     * @private
     */
    adjustSize: function(width, height) {
        var printSize = this.printProvider.layout.get("size");
        var ratio = printSize.width / printSize.height;
        // respect width & height when sizing according to the print page's
        // aspect ratio - do not exceed either, but don't take values for
        // granted if container is configured with autoWidth or autoHeight.
        var ownerCt = this.ownerCt;
        var targetWidth = (ownerCt && ownerCt.autoWidth) ? 0 :
            (width || this.initialConfig.width);
        var targetHeight = (ownerCt && ownerCt.autoHeight) ? 0 :
            (height || this.initialConfig.height);
        if (targetWidth) {
            height = targetWidth / ratio;
            if (targetHeight && height > targetHeight) {
                height = targetHeight;
                width = height * ratio;
            } else {
                width = targetWidth;
            }
        } else if (targetHeight) {
            width = targetHeight * ratio;
            height = targetHeight;
        }

        return {width: width, height: height};
    },

    /**
     * Fits this PrintMapPanel's zoom to the print scale.
     *
     * @private
     */
    fitZoom: function() {
        if (!this._updating && this.printPage.scale) {
            this._updating = true;
            var printBounds = this.printPage.getPrintExtent(this.map);
            this.currentZoom = this.map.getZoomForExtent(printBounds);
            this.map.zoomToExtent(printBounds, false);

            delete this._updating;
        }
    },

    /**
     * Updates the print page to match this PrintMapPanel's center and scale.
     *
     * @private
     */
    updatePage: function() {
        if (!this._updating) {
            var zoom = this.map.getZoom();
            this._updating = true;
            if (zoom === this.currentZoom) {
                this.printPage.setCenter(this.map.getCenter());
            } else {
                this.printPage.fit(this.map);
            }
            delete this._updating;
            this.currentZoom = zoom;
        }
    },

    /**
     * Recalculates all preview scales. This is e.g. needed when the size
     * changes.
     *
     * @private
     */
    calculatePreviewScales: function() {
        this.previewScales.removeAll();

        this.printPage.suspendEvents();
        var scale = this.printPage.scale;

        // group print scales by the zoom level they would be previewed at
        var viewSize = this.map.getSize();
        var scalesByZoom = {};
        var zooms = [];
        this.printProvider.scales.each(function(rec) {
            this.printPage.setScale(rec);
            var extent = this.printPage.getPrintExtent(this.map);
            var zoom = this.map.getZoomForExtent(extent);

            var idealResolution = Math.max(
                extent.getWidth() / viewSize.w,
                extent.getHeight() / viewSize.h
            );
            var resolution = this.map.getResolutionForZoom(zoom);
            // the closer to the ideal resolution, the better the fit
            var diff = Math.abs(idealResolution - resolution);
            if (!(zoom in scalesByZoom) || scalesByZoom[zoom].diff > diff) {
                scalesByZoom[zoom] = {
                    rec: rec,
                    diff: diff
                };
                if (Ext.Array.indexOf(zooms, zoom) === -1) {
                    zooms.push(zoom);
                }
            }
        }, this);

        // add only the preview scales that closely fit print extents
        for (var i=0, ii=zooms.length; i<ii; ++i) {
            this.previewScales.add(scalesByZoom[zooms[i]].rec);
        }

        scale && this.printPage.setScale(scale);
        this.printPage.resumeEvents();

        if (scale && this.previewScales.getCount() > 0) {
            var maxScale = this.previewScales.getAt(0);
            var minScale = this.previewScales.getAt(this.previewScales.getCount()-1);
            if (scale.get("value") < minScale.get("value")) {
                this.printPage.setScale(minScale);
            } else if (scale.get("value") > maxScale.get("value")) {
                this.printPage.setScale(maxScale);
            }
        }

        this.fitZoom();
    },

    /**
     * Convenience method for printing the map, without the need to interact
     * with the printProvider and printPage.
     *
     * @param {Object} options options for
     *     the {@link GeoExt.data.MapfishPrintProvider#method-print print}
     *     method.
     *
     */
    print: function(options) {
        this.printProvider.print(this.map, [this.printPage], options);
    },

    /**
     * Private method called during the destroy sequence.
     *
     * @private
     */
    beforeDestroy: function() {
        this.map.events.unregister("moveend", this, this.updatePage);
        this.printPage.un("change", this.fitZoom, this);
        this.printProvider.un("layoutchange", this.syncSize, this);

        this.callParent(arguments);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Layer/Vector.js
 * @include OpenLayers/Control/TransformFeature.js
 * @include GeoExt/data/PrintPage.js
 */

/**
 * Provides a way to show and modify the extents of print pages on the map. It
 * uses a layer to render the page extent and handle features of print pages,
 * and provides a control to modify them. Must be set as a plugin to a
 * {@link GeoExt.MapPanel}.
 *
 * Sample code to create a MapPanel with a PrintExtent, and print it
 * immediately:
 *
 *     var printExtent = Ext.create('GeoExt.plugins.PrintExtent', {
 *         printProvider: Ext.create('GeoExt.data.MapfishPrintProvider', {
 *             capabilities: printCapabilities
 *         })
 *     });
 *     var mapPanel = Ext.create('GeoExt.panel.Map', {
 *         border: false,
 *         renderTo: "div-id",
 *         layers: [new OpenLayers.Layer.WMS("Tasmania", "http://demo.opengeo.org/geoserver/wms",
 *             {layers: "topp:tasmania_state_boundaries"}, {singleTile: true})],
 *         center: [146.56, -41.56],
 *         zoom: 6,
 *         plugins: printExtent
 *     });
 *     printExtent.addPage();
 *     // print the map
 *     printExtent.print();
 *
 * @class GeoExt.plugins.PrintExtent
 */
Ext.define('GeoExt.plugins.PrintExtent', {
    mixins: {
        observable:  Ext.util.Observable 
    },
                                        
    alias : 'widget.gx_printextent',
    alternateClassName : 'GeoExt.PrintExtent',

    /**
     * Holds the initial config object passed to the constructor.
     *
     * @private
     * @property {Object}
     */
    initialConfig: null,

    /**
     * The print provider this form is connected to. Optional if pages are
     * provided.
     *
     * @cfg {GeoExt.data.MapfishPrintProvider} printProvider
     */
    /**
     * The print provider this form is connected to. Read-only.
     *
     * @property {GeoExt.data.MapfishPrintProvider} printProvider
     */
    printProvider: null,

    /**
     * The map the layer and control are added to.
     *
     * @private
     * @property {OpenLayers.Map} map
     */
    map: null,

    /**
     * The layer used to render extent and handle features to. Optional, will
     * be created if not provided.
     *
     * @cfg {OpenLayers.Layer.Vector} layer
     */
    /**
     * The layer used to render extent and handle features to.
     *
     * @property {OpenLayers.Layer.Vector} layer
     * @private
     */
    layer: null,

    /**
     * Optional options for the `OpenLayers.Control.TransformFeature` control.
     *
     * @cfg {Object} transformFeatureOptions
     */
    transformFeatureOptions: null,

    /**
     * The control used to change extent, center, rotation and scale.
     *
     * @property {OpenLayers.Control.TransformFeature} control
     * @private
     */
    control: null,

    /**
     * The pages that this plugin controls. Optional.
     *
     * If not provided, it will be created with one page that is completely
     * contained within the visible map extent. All pages must use the same
     * PrintProvider.
     *
     * @cfg {GeoExt.data.PrintPage[]} pages
     */
    /**
     * The pages that this component controls. Read-only.
     *
     * @property {GeoExt.data.PrintPage[]} pages
     */
    pages: null,

    /**
     * The page currently set for transformation.
     *
     * @property {GeoExt.data.PrintPage} page
     */
    page: null,

    /**
     * Private constructor override.
     *
     * @private
     */
    constructor: function(config) {
        config = config || {};

        Ext.apply(this, config);
        this.initialConfig = config;

        if(!this.printProvider) {
            this.printProvider = this.pages[0].printProvider;
        }

        if(!this.pages) {
            this.pages = [];
        }

        /**
         * Triggered when a page has been selected using the control.
         *
         * Listener arguments:
         *
         * * printPage - {@link GeoExt.data.PrintPage} this printPage
         *
         * @event selectpage
         */


        this.callParent(arguments);
    },

    /**
     * Prints all pages as shown on the map.
     *
     * @param {Object} options Options to send to the PrintProvider's
     *     print method. See the GeoExt.data.MapfishPrintProvider
     *     {@link GeoExt.data.MapfishPrintProvider#method-print print method}.
     */
    print: function(options) {
        this.printProvider.print(this.map, this.pages, options);
    },

    /**
     * Initializes the plugin.
     *
     * @param {GeoExt.panel.Map} mapPanel
     * @private
     */
    init: function(mapPanel) {
        this.map = mapPanel.map;
        mapPanel.on("destroy", this.onMapPanelDestroy, this);

        if (!this.layer) {
            this.layer = new OpenLayers.Layer.Vector(null, {
                displayInLayerSwitcher: false
            });
        }
        this.createControl();

        for(var i=0, len=this.pages.length; i<len; ++i) {
            this.addPage(this.pages[i]);
        }
        this.show();
    },

    /**
     * Adds a page to the list of pages that this plugin controls.
     *
     * @param {GeoExt.data.PrintPage} page The page to add to this plugin.
     *     If not provided, a page that fits the current extent is created.
     * @return {GeoExt.data.PrintPage} page
     */
    addPage: function(page) {
        page = page || Ext.create('GeoExt.data.PrintPage', {
            printProvider: this.printProvider
        });
        if(Ext.Array.indexOf(this.pages, page) === -1) {
            this.pages.push(page);
        }
        this.layer.addFeatures([page.feature]);
        page.on("change", this.onPageChange, this);

        this.page = page;
        var map = this.map;
        if(map.getCenter()) {
            this.fitPage();
        } else {
            map.events.register("moveend", this, function() {
                map.events.unregister("moveend", this, arguments.callee);
                this.fitPage();
            });
        }
        return page;
    },

    /**
     * Removes a page from the list of pages that this plugin controls.
     *
     * @param {GeoExt.data.PrintPage} page The page to remove from this plugin.
     */
    removePage: function(page) {
        Ext.Array.remove(this.pages, page);
        if (page.feature.layer) {
            this.layer.removeFeatures([page.feature]);
        }
        page.un("change", this.onPageChange, this);
    },

    /**
     * Selects the given page (i.e. calls the setFeature on the modify feature
     * control)
     *
     * @param {GeoExt.data.PrintPage} page The page to select.
     */
    selectPage: function(page) {
        this.control.active && this.control.setFeature(page.feature);
        // FIXME raise the feature up so that it is on top
    },

    /**
     * Sets up the plugin, initializing the `OpenLayers.Layer.Vector`
     * layer and `OpenLayers.Control.TransformFeature`, and centering
     * the first page if no pages were specified in the configuration.
     */
    show: function() {
        this.map.addLayer(this.layer);
        this.map.addControl(this.control);
        this.control.activate();

        // if we have a page and if the map has a center then update the
        // transform box for that page, in case the transform control
        // was deactivated when fitPage (and therefore onPageChange)
        // was called.
        if (this.page && this.map.getCenter()) {
            this.updateBox();
        }
    },

    /**
     * Tears down the plugin, removes the `OpenLayers.Control.TransformFeature`
     * control and the `OpenLayers.Layer.Vector` layer.
     */
    hide: function() {
        // note: we need to be extra cautious when destroying OpenLayers
        // objects here (the tests will fail if we're not cautious anyway).
        // We use obj.events to test whether an OpenLayers object is
        // destroyed or not.

        var map = this.map, layer = this.layer, control = this.control;

        if(control && control.events) {
            control.deactivate();
            if(map && map.events && control.map) {
                map.removeControl(control);
            }
        }

        if(map && map.events && layer && layer.map) {
            map.removeLayer(layer);
        }
    },

    /**
     * When the mappanel is destroyed, we need to remove our pages etc.
     *
     * @private
     */
    onMapPanelDestroy: function() {

        var map = this.map;

        for(var len = this.pages.length - 1, i = len; i>=0; i--) {
            this.removePage(this.pages[i]);
        }

        this.hide();

        var control = this.control;
        if(map && map.events &&
           control && control.events) {
            control.destroy();
        }

        var layer = this.layer;
        if(!this.initialConfig.layer &&
           map && map.events &&
           layer && layer.events) {
            layer.destroy();
        }

        delete this.layer;
        delete this.control;
        delete this.page;
        this.map = null;
    },

    /**
     * Creates the OpenLayers.Control.TransformFeature control to interact with
     * the print extent. Registers the appropriate listeners to keep us in sync.
     *
     * @private
     */
    createControl: function() {
        this.control = new OpenLayers.Control.TransformFeature(this.layer, Ext.applyIf({
            preserveAspectRatio: true,
            eventListeners: {
                "beforesetfeature": function(e) {
                    for(var i=0, len=this.pages.length; i<len; ++i) {
                        if(this.pages[i].feature === e.feature) {
                            this.page = this.pages[i];
                            e.object.rotation = -this.pages[i].rotation;
                            break;
                        }
                    }
                },
                "setfeature": function(e) {
                    for(var i=0, len=this.pages.length; i<len; ++i) {
                        if(this.pages[i].feature === e.feature) {
                            this.fireEvent("selectpage", this.pages[i]);
                            break;
                        }
                    }
                },
                "beforetransform": function(e) {
                    this._updating = true;
                    var page = this.page;
                    if(e.rotation) {
                        if(this.printProvider.layout.get("rotation")) {
                            page.setRotation(-e.object.rotation);
                        } else {
                            e.object.setFeature(page.feature);
                        }
                    } else if(e.center) {
                        page.setCenter(OpenLayers.LonLat.fromString(
                            e.center.toShortString()
                        ));
                    } else {
                        page.fit(e.object.box, {mode: "closest"});
                        var minScale = this.printProvider.scales.getAt(0);
                        var maxScale = this.printProvider.scales.getAt(
                            this.printProvider.scales.getCount() - 1);
                        var boxBounds = e.object.box.geometry.getBounds();
                        var pageBounds = page.feature.geometry.getBounds();
                        var tooLarge = page.scale === minScale &&
                            boxBounds.containsBounds(pageBounds);
                        var tooSmall = page.scale === maxScale &&
                            pageBounds.containsBounds(boxBounds);
                        if(tooLarge === true || tooSmall === true) {
                            this.updateBox();
                        }
                    }
                    delete this._updating;
                    return false;
                },
                "transformcomplete": this.updateBox,
                scope: this
            }
        }, this.transformFeatureOptions));
    },

    /**
     * Fits the current print page to the map.
     *
     * @private
     */
    fitPage: function() {
        if(this.page) {
            this.page.fit(this.map, {mode: "screen"});
        }
    },

    /**
     * Updates the transformation box after setting a new scale or layout, or to
     * fit the box to the extent feature after a transform.
     *
     * @private
     */
    updateBox: function() {
        var page = this.page;
        this.control.active &&
            this.control.setFeature(page.feature, {rotation: -page.rotation});
    },

    /**
     * Handler for a page's change event.
     *
     * @private
     */
    onPageChange: function(page, mods) {
        if(!this._updating) {
            this.control.active &&
                this.control.setFeature(page.feature, {rotation: -page.rotation});
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/**
 * A plugin for `Ext.form.Field` components which provides synchronization
 * with a {@link GeoExt.data.PrintPage}. The field name has to match the
 * respective property of the printPage (e.g. `scale`, `rotation`).
 *
 * A form with a combo box for the scale and text fields for rotation and a
 * page title. The page title is a custom parameter of the print module's
 * page configuration:
 *
 *     var printPage = Ext.create('GeoExt.data.PrintPage'{
 *         printProvider: Ext.create('GeoExt.data.MapfishPrintProvider', {
 *             capabilities: printCapabilities
 *         })
 *     });
 *     Ext.create('Ext.form.FormPanel', {
 *         renderTo: "form",
 *         width: 200,
 *         height: 300,
 *         items: [{
 *             xtype: "combo",
 *             displayField: "name",
 *             store: printPage.scales, // printPage.scale
 *             name: "scale",
 *             fieldLabel: "Scale",
 *             typeAhead: true,
 *             mode: "local",
 *             forceSelection: true,
 *             triggerAction: "all",
 *             selectOnFocus: true,
 *             plugins: Ext.create('GeoExt.plugins.PrintPageField',{
 *                 printPage: printPage
 *             })
 *         }, {
 *             xtype: "textfield",
 *             name: "rotation", // printPage.rotation
 *             fieldLabel: "Rotation",
 *             plugins: Ext.create('GeoExt.plugins.PrintPageField',{
 *                 printPage: printPage
 *             })
 *         }, {
 *             xtype: "textfield",
 *             name: "mapTitle", // printPage.customParams["mapTitle"]
 *             fieldLabel: "Map Title",
 *             plugins: Ext.create('GeoExt.plugins.PrintPageField',{
 *                 printPage: printPage
 *             })
 *         }]
 *     });
 *
 * @class GeoExt.plugins.PrintPageField
 */
Ext.define('GeoExt.plugins.PrintPageField', {
    mixins: {
        observable:  Ext.util.Observable 
    },
               
                                
                                  
                                 
      
    alias : 'widget.gx_printpagefield',
    alternateClassName : 'GeoExt.PrintPageField',


    /**
     * The print page to synchronize with.
     *
     * @cfg {GeoExt.data.PrintPage} printPage
     */
    /**
     * The print page to synchronize with. Read-only.
     *
     * @property {GeoExt.data.PrintPage} printPage
     * @private
     */
    printPage: null,

    /**
     * This plugin's target field.
     *
     * @property {Ext.form.Field} target
     * @private
     */
    target: null,

    /**
     * @private
     */
    constructor: function(config) {
        this.initialConfig = config;
        Ext.apply(this, config);

        this.callParent(arguments);
    },

    /**
     * @param {Ext.form.Field} target The component that this plugin extends.
     * @private
     */
    init: function(target) {

        this.target = target;
        var onCfg = {
            "beforedestroy": this.onBeforeDestroy,
            scope: this
        };

        // the old 'check' event of 3.x is gone, only 'change' is supported
        var eventName = "change";
        if (target instanceof Ext.form.ComboBox) {
            eventName = "select";
        }

        onCfg[eventName] = this.onFieldChange;
        target.on(onCfg);
        this.printPage.on({
            "change": this.onPageChange,
            scope: this
        });
        this.printPage.printProvider.on({
            "layoutchange": this.onLayoutChange,
            scope: this
        });
        this.setValue(this.printPage);
    },

    /**
     * Handler for the target field's "valid" or "select" event.
     *
     * @param {Ext.form.Field} field
     * @param {Ext.data.Record[]} records Optional.
     * @private
     */
    onFieldChange: function(field, records) {

        var record;
        if (Ext.isArray(records)) {
            record = records[0];
        } else {
            record = records;
        }

        var printProvider = this.printPage.printProvider;
        var value = field.getValue();
        this._updating = true;
        if(field.store === printProvider.scales || field.name === "scale") {
            this.printPage.setScale(record);
        } else if(field.name == "rotation") {
            !isNaN(value) && this.printPage.setRotation(value);
        } else {
            this.printPage.customParams[field.name] = value;
        }
        delete this._updating;
    },

    /**
     * Handler for the "change" event for the page this plugin is configured
     * with.
     *
     * @param {GeoExt.data.PrintPage} printPage
     * @private
     */
    onPageChange: function(printPage) {
        if(!this._updating) {
            this.setValue(printPage);
        }
    },

    /**
     * Handler for the "layoutchange" event of the printProvider.
     *
     * @param {GeoExt.data.MapfishPrintProvider} printProvider
     * @param {Ext.Record} layout
     * @private
     *
     */
    onLayoutChange: function(printProvider, layout) {
        var t = this.target;
        t.name == "rotation" && t.setDisabled(!layout.get("rotation"));
    },

    /**
     * Sets the value in the target field.
     *
     * @param {GeoExt.data.PrintPage} printPage
     * @private
     */
    setValue: function(printPage) {
        var t = this.target;
        t.suspendEvents();
        if(t.store === printPage.printProvider.scales || t.name === "scale") {
            if(printPage.scale) {
                t.setValue(printPage.scale.get(t.displayField));
            }
        } else if(t.name == "rotation") {
            t.setValue(printPage.rotation);
        }
        t.resumeEvents();
    },

    /**
     * @private
     */
    onBeforeDestroy: function() {
        this.target.un("beforedestroy", this.onBeforeDestroy, this);
        this.target.un("select", this.onFieldChange, this);
        this.target.un("valid", this.onFieldChange, this);
        this.printPage.un("change", this.onPageChange, this);
        this.printPage.printProvider.un("layoutchange", this.onLayoutChange,
            this);
    }
});
/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txttxt for the full
 * text of the license.
 */
 
/*
 * @requires GeoExt/data/MapfishPrintProvider.js
 */

/**
 * A plugin for Ext.form.Field components which provides synchronization
 * with a GeoExt.data.MapfishPrintProvider.
 *
 * A form with combo boxes for layout and resolution, and a text field for a
 * map title. The latter is a custom parameter to the print module, which is
 * a default for all print pages. For setting custom parameters on the page
 * level, use GeoExt.plugins.PrintPageField.
 *
 *     var printProvider = Ext.create('GeoExt.data.MapfishPrintProvider', {
 *         capabilities: printCapabilities
 *     });
 *     Ext.create('Ext.form.FormPanel', {
 *         renderTo: "form",
 *         width: 200,
 *         height: 300,
 *         items: [{
 *             xtype: "combo",
 *             displayField: "name",
 *             store: printProvider.layouts, // printProvider.layout
 *             fieldLabel: "Layout",
 *             typeAhead: true,
 *             mode: "local",
 *             forceSelection: true,
 *             triggerAction: "all",
 *             selectOnFocus: true,
 *             plugins: Ext.create('GeoExt.plugins.PrintProviderField', {
 *                 printProvider: printProvider
 *             })
 *         }, {
 *             xtype: "combo",
 *             displayField: "name",
 *             store: printProvider.dpis, // printProvider.dpi
 *             fieldLabel: "Resolution",
 *             typeAhead: true,
 *             mode: "local",
 *             forceSelection: true,
 *             triggerAction: "all",
 *             selectOnFocus: true,
 *             plugins: Ext.create('GeoExt.plugins.PrintProviderField', {
 *                 printProvider: printProvider
 *             })
 *         }, {
 *             xtype: "textfield",
 *             name: "mapTitle", // printProvider.customParams.mapTitle
 *             fieldLabel: "Map Title",
 *             plugins: Ext.create('GeoExt.plugins.PrintProviderField', {
 *                 printProvider: printProvider
 *             })
 *         }]
 *     }):
 *
 * @class GeoExt.plugins.PrintProviderField
 */
Ext.define('GeoExt.plugins.PrintProviderField', {
    mixins: {
        observable:  Ext.util.Observable 
    },
               
                                           
                                 
      
    alias: 'widget.gx_printproviderfield',
    alternateClassName: 'GeoExt.PrintProviderField',

    /**
     * The print provider to use with this plugin's field. Not required if set
     * on the owner container of the field.
     *
     * @cfg {GeoExt.data.MapfishPrintProvider} printProvider
     */

    /**
     * This plugin's target field.
     *
     * @private
     * @property {Ext.form.Field} target
     */
    target: null,

    /**
     * @private
     */
    constructor: function(config) {
        this.initialConfig = config;
        Ext.apply(this, config);
        this.callParent(arguments);
    },

    /**
     * @private
     * @param {Ext.form.Field} target The component that this plugin extends.
     */
    init: function(target) {
        this.target = target;
        var onCfg = {
            scope: this,
            "render": this.onRender,
            "beforedestroy": this.onBeforeDestroy
        };
        onCfg[target instanceof Ext.form.ComboBox ? "select" : "change"] =
            this.onFieldChange;
        target.on(onCfg);
    },

    /**
     * Handler for the target field's "render" event.
     *
     * @param {Ext.Form.Field} field
     * @private
     */
    onRender: function(field) {
        var printProvider = this.printProvider || field.ownerCt.printProvider;
        if(field.store === printProvider.layouts) {
            field.setValue(printProvider.layout.get(field.displayField));
            printProvider.on({
                "layoutchange": this.onProviderChange,
                scope: this
            });
        } else if(field.store === printProvider.dpis) {
            field.setValue(printProvider.dpi.get(field.displayField));
            printProvider.on({
                "dpichange": this.onProviderChange,
                scope: this
            });
        } else if(field.initialConfig.value === undefined) {
            field.setValue(printProvider.customParams[field.name]);
        }
    },

    /**
     * Handler for the target field's "change" or "select" event.
     *
     * @param {Ext.form.Field} field
     * @param {Ext.data.Record} record Optional.
     * @private
     */
    onFieldChange: function(field, records) {
        var record;
        if (Ext.isArray(records)) {
            record = records[0];
        } else {
            record = records;
        }
        var printProvider = this.printProvider || field.ownerCt.printProvider;
        var value = field.getValue();
        this._updating = true;
        if(record) {
            switch(field.store) {
                case printProvider.layouts:
                    printProvider.setLayout(record);
                    break;
                case printProvider.dpis:
                    printProvider.setDpi(record);
                    break;
            }
        } else {
            printProvider.customParams[field.name] = value;
        }
        delete this._updating;
    },

    /**
     * Handler for the printProvider's dpichange and layoutchange event.
     *
     * @param {GeoExt.data.MapfishPrintProvider}  printProvider
     * @param {Ext.data.Record}  rec
     * @private
     */
    onProviderChange: function(printProvider, rec) {
        if(!this._updating) {
            this.target.setValue(rec.get(this.target.displayField));
        }
    },

    /**
     * Private method called during the destroy sequence.
     *
     * @private
     */
    onBeforeDestroy: function() {
        var target = this.target;
        target.un("beforedestroy", this.onBeforeDestroy, this);
        target.un("render", this.onRender, this);
        target.un("select", this.onFieldChange, this);
        target.un("valid", this.onFieldChange, this);
        var printProvider = this.printProvider || target.ownerCt.printProvider;
        printProvider.un("layoutchange", this.onProviderChange, this);
        printProvider.un("dpichange", this.onProviderChange, this);
    }

});


/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Control/SelectFeature.js
 * @include OpenLayers/Layer/Vector.js
 * @include OpenLayers/Util.js
 * @include OpenLayers/BaseTypes/Class.js
 * @requires GeoExt/Version.js
 */

/**
 * A row selection model which enables automatic selection of features
 * in the map when rows are selected in the grid and vice-versa.
 *
 * Sample code to create a feature grid with a feature selection model:
 *
 * Example:
 *
 *     var gridPanel = Ext.create('Ext.grid.GridPanel', {
 *         title: "Feature Grid",
 *         region: "east",
 *         store: store,
 *         width: 320,
 *         columns: [{
 *             header: "Name",
 *             width: 200,
 *             dataIndex: "name"
 *         }, {
 *             header: "Elevation",
 *             width: 100,
 *             dataIndex: "elevation"
 *         }],
 *         selType: 'featuremodel'
 *     });
 *
 * @class GeoExt.selection.FeatureModel
 */
Ext.define('GeoExt.selection.FeatureModel', {
    extend:  Ext.selection.RowModel ,
    alias: 'selection.featuremodel',
               
                        
      

    /**
     * If true the select feature control is activated and deactivated when
     * binding and unbinding.
     *
     * @cfg {Boolean}
     */
    autoActivateControl: true,

    /**
     * If true, and if the constructor is passed neither a layer nor a select
     * feature control, a select feature control is created using the layer
     * found in the grid's store. Set it to false if you want to manually bind
     * the selection model to a layer.
     *
     * @cfg {Boolean}
     */
    layerFromStore: true,

    /**
     * The select feature control instance. If not provided one will be created.
     *
     * If provided any "layer" config option will be ignored, and its "multiple"
     * option will be used to configure the selectionModel.  If an `Object`
     * is provided here, it will be passed as config to the SelectFeature
     * constructor, and the "layer" config option will be used for the layer.
     *
     * @cfg {OpenLayers.Control.SelectFeature}
     */
    selectControl: null,

    /**
     * The vector layer used for the creation of the select feature control, it
     * must already be added to the map. If not provided, the layer bound to the
     * grid's store, if any, will be used.
     *
     * @cfg {OpenLayers.Layer.Vector} layer
     */

    /**
     * Flag indicating if the selection model is bound.
     *
     * @property {Boolean}
     * @private
     */
    bound: false,

    /**
     * An array to store the selected features.
     *
     * @property {OpenLayers.Feature.Vector[]}
     * @private
     */
    selectedFeatures: [],

    /**
     * If true the map will recenter on feature selection so that the selected
     * features are visible.
     * 
     * @cfg {Boolean}
     */
    autoPanMapOnSelection: false,

    /**
     * @private
     */
    constructor: function(config) {
        config = config || {};
        if (config.selectControl instanceof OpenLayers.Control.SelectFeature) {
            if (!config.singleSelect) {
                var ctrl = config.selectControl;
                config.singleSelect = !(ctrl.multiple || !!ctrl.multipleKey);
            }
        } else if (config.layer instanceof OpenLayers.Layer.Vector) {
            this.selectControl = this.createSelectControl(
                    config.layer, config.selectControl);
            delete config.layer;
            delete config.selectControl;
        }
        if (config.autoPanMapOnSelection) {
            this.autoPanMapOnSelection = true;
            delete config.autoPanMapOnSelection;
        }
        this.callParent(arguments);
    },

    /**
     * Called after this.grid is defined.
     * 
     * @private
     */
    bindComponent: function() {
        this.callParent(arguments);
        if (this.layerFromStore) {
            var layer = this.view.getStore() && this.view.getStore().layer;
            if (layer && !(this.selectControl instanceof
                    OpenLayers.Control.SelectFeature)) {
                this.selectControl = this.createSelectControl(
                        layer, this.selectControl);
            }
        }
        if (this.selectControl) {
            this.bind(this.selectControl);
        }
    },

    /**
     * Create the select feature control.
     *
     * @param {OpenLayers.Layer.Vector} layer The vector layer.
     * @param {Object} config The select feature control config.
     * @private
     */
    createSelectControl: function(layer, config) {
        config = config || {};
        var singleSelect = config.singleSelect !== undefined ?
                config.singleSelect : this.singleSelect;
        config = OpenLayers.Util.extend({
            toggle: true,
            multipleKey: singleSelect ? null :
                (Ext.isMac ? "metaKey" : "ctrlKey")
        }, config);
        var selectControl = new OpenLayers.Control.SelectFeature(
                layer, config);
        layer.map.addControl(selectControl);
        return selectControl;
    },

    /**
     * Bind the selection model to a layer or a SelectFeature control.
     *
     * @param {OpenLayers.Layer.Vector/OpenLayers.Control.SelectFeature} obj
     *     The object this selection model should be bound to, either a vector
     *     layer or a select feature control.
     * @param {Object} options An object with a "controlConfig" property
     *     referencing the configuration object to pass to the
     *     `OpenLayers.Control.SelectFeature` constructor.
     * @return {OpenLayers.Control.SelectFeature} The select feature control
     *     this selection model uses.
     */
    bind: function(obj, options) {
        if (!this.bound) {
            options = options || {};
            this.selectControl = obj;
            if (obj instanceof OpenLayers.Layer.Vector) {
                this.selectControl = this.createSelectControl(
                    obj, options.controlConfig
                );
            }
            if (this.autoActivateControl) {
                this.selectControl.activate();
            }
            var layers = this.getLayers();
            for (var i = 0, len = layers.length; i < len; i++) {
                layers[i].events.on({
                    featureselected: this.featureSelected,
                    featureunselected: this.featureUnselected,
                    scope: this
                });
            }
            this.bound = true;
        }
        return this.selectControl;
    },

    /**
     * Unbind the selection model from the layer or SelectFeature control.
     *
     * @return {OpenLayers.Control.SelectFeature} The select feature control
     *     this selection model used.
     */
    unbind: function() {
        var selectControl = this.selectControl;
        if (this.bound) {
            var layers = this.getLayers();
            for (var i = 0, len = layers.length; i < len; i++) {
                layers[i].events.un({
                    featureselected: this.featureSelected,
                    featureunselected: this.featureUnselected,
                    scope: this
                });
            }
            if (this.autoActivateControl) {
                selectControl.deactivate();
            }
            this.selectControl = null;
            this.bound = false;
        }
        return selectControl;
    },

    /**
     * Handler for when a feature is selected.
     *
     * @param {Object} evt An object with a `feature` property referencing the
     *     selected feature.
     * @private
     */
    featureSelected: function(evt) {
        if (!this._selecting) {
            var store = this.view.store;
            var row = store.findBy(function(record, id) {
                return record.raw == evt.feature;
            });
            if (row != -1 && !this.isSelected(row)) {
                this._selecting = true;
                this.select(row, !this.singleSelect);
                this._selecting = false;
                // focus the row in the grid to ensure it is visible
                this.view.focusRow(row);
            }
        }
    },

    /**
     * Handler for when a feature is unselected.
     *
     * @param {Object} evt An object with a `feature` property referencing the
     *     unselected feature.
     * @private
     */
    featureUnselected: function(evt) {
        if (!this._selecting) {
            var store = this.view.store;
            var row = store.findBy(function(record, id) {
                return record.raw == evt.feature;
            });
            if (row != -1 && this.isSelected(row)) {
                this._selecting = true;
                this.deselect(row);
                this._selecting = false;
                this.view.focusRow(row);
            }
        }
    },

    /**
     * Synchronizes selection on the layer with selection in the grid.
     *
     * @param {Ext.data.Record} record The record.
     * @param {Boolean} isSelected.
     * @private
     */
    onSelectChange: function(record, isSelected) {
        this.callParent(arguments);

        var feature = record.raw;
        if (this.selectControl && !this._selecting && feature) {
            var layers = this.getLayers();
            if (isSelected) {
                for (var i = 0, len = layers.length; i < len; i++) {
                    if (Ext.Array.indexOf(layers[i].selectedFeatures, feature) == -1) {
                        this._selecting = true;
                        this.selectControl.select(feature);
                        this._selecting = false;
                        this.selectedFeatures.push(feature);
                        break;
                    }
                }
                if (this.autoPanMapOnSelection) {
                    this.recenterToSelectionExtent();
                }
            }
            else {
                for (var i = 0, len = layers.length; i < len; i++) {
                    if (Ext.Array.indexOf(layers[i].selectedFeatures, feature) != -1) {
                        this._selecting = true;
                        this.selectControl.unselect(feature);
                        this._selecting = false;
                        OpenLayers.Util.removeItem(this.selectedFeatures, feature);
                        break;
                    }
                }
                if (this.autoPanMapOnSelection && this.selectedFeatures.length > 0) {
                    this.recenterToSelectionExtent();
                }
            }
        }
    },

    /**
     * Gets the layers attached to the select feature control.
     *
     * @return the layers attached to the select feature control.
     * @private
     */
    getLayers: function() {
        return this.selectControl.layers || [this.selectControl.layer];
    },

    /**
     * Centers the map in order to display all selected features.
     *
     * @private
     */
    recenterToSelectionExtent: function() {
        var map = this.selectControl.map;
        var selectionExtent = this.getSelectionExtent();
        var selectionExtentZoom = map.getZoomForExtent(selectionExtent, false);
        if (selectionExtentZoom > map.getZoom()) {
            map.setCenter(selectionExtent.getCenterLonLat());
        }
        else {
            map.zoomToExtent(selectionExtent);
        }
    },

    /**
     * Calculates the max extent which includes all selected features.
     *
     * @return {OpenLayers.Bounds} Returns null if the layer has no features
     *     with geometries.
     */
    getSelectionExtent: function () {
        var maxExtent = null;
        var features = this.selectedFeatures;
        if (features && (features.length > 0)) {
            var geometry = null;
            for (var i = 0, len = features.length; i < len; i++) {
                geometry = features[i].geometry;
                if (geometry) {
                    if (maxExtent === null) {
                        maxExtent = new OpenLayers.Bounds();
                    }
                    maxExtent.extend(geometry.getBounds());
                }
            }
        }
        return maxExtent;
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * A base class for sliders that can be rendered as items of a GeoExt.panel.Map.
 *
 * Usually you will not create instances of this class but rather of the
 * subclasses GeoExt.slider.LayerOpacity and GeoExt.slider.Zoom.
 *
 * This class mostly provides subclasses with the method #addToMapPanel and
 * the method #removeFromMapPanel, which take care of the dimensions of the
 * sliders DOM-elements and register/unregister appropriate eventlisteners.
 *
 * Subclasses may implement #unbind, which will be called when a slider is
 * being removed from the map panel.
 *
 * @class GeoExt.slider.MapPanelItem
 */
Ext.define('GeoExt.slider.MapPanelItem', {
    extend:  Ext.slider.Single ,
               
                        
      

    /**
     * The number of milliseconds to wait (after rendering the slider) before
     * resizing of the slider happens in case this slider is rendered as child
     * of a GeoExt.panel.Map.
     *
     * This defaults to 200 milliseconds, which is not really noticeable, and
     * also rather conservative big.
     *
     * @protected
     */
    resizingDelayMS: 200,

    /**
     * The height in pixels of the slider thumb. Will be used when we need to
     * manually resize ourself in case we are added to a mappanel. This will
     * be the height of the element containing the thumb when we are rendered
     * horizontally (see #vertical).
     *
     * This value shouldn't usually be adjusted, when the default stylesheet of
     * ExtJS is used.
     *
     * @cfg {Number}
     */
    thumbHeight: 14,

    /**
     * The width in pixels of the slider thumb. Will be used when we need to
     * manually resize ourself in case we are added to a mappanel. This will
     * be the width of the element containing the thumb when we are rendered
     * vertically (see #vertical).
     *
     * This value shouldn't usually be adjusted, when the default stylesheet of
     * ExtJS is used.
     *
     * @cfg {Number}
     */
    thumbWidth: 15,

    /**
     * Called by a MapPanel if this component is one of the items in the panel.
     *
     * @param {GeoExt.panel.Map} panel
     * @protected
     */
    addToMapPanel: function(panel) {
        this.on({
            // Once we are rendered and we know that we are a child of a
            // mappanel, we need to make some adjustments to our DOMs
            // box dimensions.
            afterrender: function(){
                var me = this,
                    el = me.getEl(),
                    dim = {
                        // depending on our vertical setting, we need to find
                        // sane values for both width and height.
                        width: me.vertical ? me.thumbWidth : el.getWidth(),
                        height: !me.vertical ? me.thumbHeight : el.getHeight(),
                        top: me.y || 0,
                        left: me.x || 0
                    },
                    resizeFunction,
                    resizeTask;
                // Bind handlers that stop the mouse from interacting with the
                // map below the slider.
                el.on({
                    mousedown: me.stopMouseEvents,
                    click: me.stopMouseEvents
                });

                // This method takes some of the gathered values from above and
                // ensures that we have an expected look.
                resizeFunction = function(){
                    el.setStyle({
                        top: dim.top,
                        left: dim.left,
                        width: "" + dim.width + "px",
                        position: "absolute",
                        height: "" + dim.height + "px",
                        zIndex: panel.map.Z_INDEX_BASE.Control
                    });
                    // This is tricky...
                    if (me.vertical) {
                        // ...for vertical sliders the height of the surrounding
                        // element is controlled by the height of the element
                        // with the 'x-slider-inner'-class
                        el.down('.x-slider-inner').el.setStyle({
                            height: "" + (dim.height - me.thumbWidth) + "px"
                        });
                    } else {
                        // ...but for horizontal sliders, it's the form element
                        // with class 'x-form-item-body' that controls the
                        // height.
                        el.down('.x-form-item-body').el.setStyle({
                            height: "" + me.thumbHeight + "px"
                        });
                    }
                };
                // We delay the execution for a small amount of milliseconds,
                // so that our changes do take effect.
                resizeTask = new Ext.util.DelayedTask(resizeFunction);
                resizeTask.delay(me.resizingDelayMS);
            },
            scope: this
        });
    },

    /**
     * Called by a MapPanel if this component is one of the items in the panel.
     *
     * @param {GeoExt.panel.Map} panel
     * @protected
     */
    removeFromMapPanel: function(panel) {
        var me = this,
            el = me.getEl();
        el.un({
            mousedown: me.stopMouseEvents,
            click: me.stopMouseEvents,
            scope: me
        });
        me.unbind();
    },

    /**
     * Will be called when the slider is being removed from the mappanel.
     * Subclasses may implement custom event unlistening logic in this method.
     *
     * @protected
     */
    unbind: Ext.emptyFn,

    /**
     * Stops the event from propagating.
     *
     * @private
     */
    stopMouseEvents: function(e) {
        e.stopEvent();
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Layer.js
 * @include GeoExt/data/LayerModel.js
 */

/**
 * Create a slider to control the opacity of a layer.
 *
 * Sample code to render a slider outside the map viewport:
 *
 *     var slider = Ext.create('GeoExt.slider.LayerOpacity', {
 *         renderTo: Ext.getBody(),
 *         width: 200,
 *         layer: layer
 *     });
 *
 * Sample code to add a slider to a map panel:
 *
 *     var layer = new OpenLayers.Layer.WMS(
 *         "Global Imagery",
 *         "http://maps.opengeo.org/geowebcache/service/wms",
 *          {layers: "bluemarble"}
 *     );
 *     var panel = Ext.create('GeoExt.panel.Map', {
 *         renderTo: document.body,
 *         height: 300,
 *         width: 400,
 *         map: {
 *             controls: [new OpenLayers.Control.Navigation()]
 *         },
 *         layers: [layer],
 *         extent: [-5, 35, 15, 55],
 *         items: [{
 *             xtype: "gx_opacityslider",
 *             layer: layer,
 *             aggressive: true,
 *             vertical: true,
 *             height: 100,
 *             x: 10,
 *             y: 20
 *         }]
 *     });
 *
 * @class GeoExt.slider.LayerOpacity
 */
Ext.define('GeoExt.slider.LayerOpacity', {
    alternateClassName: "GeoExt.LayerOpacitySlider",
    extend:  GeoExt.slider.MapPanelItem ,
                                       
    alias: 'widget.gx_opacityslider',

    /**
     * The layer this slider changes the opacity of. (required)
     *
     * @cfg {OpenLayers.Layer/GeoExt.data.LayerModel}
     */
    layer: null,

    /**
     * If provided, a layer that will be made invisible (its visibility is
     * set to false) when the slider value is set to its max value. If this
     * slider is used to fade visibility between to layers, setting
     * `complementaryLayer` and `changeVisibility` will make sure that
     * only visible tiles are loaded when the slider is set to its min or max
     * value. (optional)
     *
     * @cfg {OpenLayers.Layer/GeoExt.data.LayerModel}
     */
    complementaryLayer: null,

    /**
     * Time in milliseconds before setting the opacity value to the
     * layer. If the value change again within that time, the original value
     * is not set. Only applicable if aggressive is true.
     *
     * @cfg {Number}
     */
    delay: 5,

    /**
     * Time in milliseconds before changing the layer's visibility.
     * If the value changes again within that time, the layer's visibility
     * change does not occur. Only applicable if changeVisibility is true.
     * Defaults to 5.
     *
     * @cfg {Number}
     */
    changeVisibilityDelay: 5,

    /**
     * If set to true, the opacity is changed as soon as the thumb is moved.
     * Otherwise when the thumb is released (default).
     *
     * @cfg {Boolean}
     */
    aggressive: false,

    /**
     * If set to true, the layer's visibility is handled by the
     * slider, the slider makes the layer invisible when its
     * value is changed to the min value, and makes the layer
     * visible again when its value goes from the min value
     * to some other value. The layer passed to the constructor
     * must be visible, as its visibility is fully handled by
     * the slider. Defaults to false.
     *
     * @cfg {Boolean}
     */
    changeVisibility: false,

    /**
     * The value to initialize the slider with. This value is
     * taken into account only if the layer's opacity is null.
     * If the layer's opacity is null and this value is not
     * defined in the config object then the slider initializes
     * it to the max value.
     *
     * @cfg {Number}
     */
    value: null,

    /**
     * If true, we will work with transparency instead of with opacity.
     * Defaults to false.
     *
     * @cfg {Boolean}
     */
    inverse: false,

    /**
     * Construct the component.
     *
     * @private
     */
    constructor: function(config) {
        if (config.layer) {
            this.layer = this.getLayer(config.layer);
            this.bind();
            this.complementaryLayer = this.getLayer(config.complementaryLayer);
            // before we call getOpacityValue inverse should be set
            if (config.inverse !== undefined) {
                this.inverse = config.inverse;
            }
            config.value = (config.value !== undefined) ?
                config.value : this.getOpacityValue(this.layer);
            delete config.layer;
            delete config.complementaryLayer;
        }
        this.callParent([config]);
    },

    /**
     * Bind the slider to the layer.
     *
     * @private
     */
    bind: function() {
        if (this.layer && this.layer.map) {
            this.layer.map.events.on({
                changelayer: this.update,
                scope: this
            });
        }
    },

    /**
     * Unbind the slider from the layer.
     *
     * @private
     */
    unbind: function() {
        if (this.layer && this.layer.map && this.layer.map.events) {
            this.layer.map.events.un({
                changelayer: this.update,
                scope: this
            });
        }
    },

    /**
     * Registered as a listener for opacity change.  Updates the value of the
     * slider.
     *
     * @private
     */
    update: function(evt) {
        if (evt.property === "opacity" && evt.layer == this.layer &&
            !this._settingOpacity) {
            this.setValue(this.getOpacityValue(this.layer));
        }
    },

    /**
     * Bind a new layer to the opacity slider.
     *
     * @param {OpenLayers.Layer/GeoExt.data.LayerModel} layer
     */
    setLayer: function(layer) {
        this.unbind();
        this.layer = this.getLayer(layer);
        this.setValue(this.getOpacityValue(layer));
        this.bind();
    },

    /**
     * Returns the opacity value for the layer.
     *
     * @param {OpenLayers.Layer/GeoExt.data.LayerModel} layer
     * @return {Integer} The opacity for the layer.
     * @private
     */
    getOpacityValue: function(layer) {
        var value;
        if (layer && layer.opacity !== null) {
            value = parseInt(layer.opacity * (this.maxValue - this.minValue));
        } else {
            value = this.maxValue;
        }
        if (this.inverse === true) {
            value = (this.maxValue - this.minValue) - value;
        }
        return value;
    },

    /**
     * Returns the OpenLayers layer object for a layer record or a plain layer
     * object.
     *
     * @param {OpenLayers.Layer/GeoExt.data.LayerModel} layer
     * @return {OpenLayers.Layer} The OpenLayers layer object
     * @private
     */
    getLayer: function(layer) {
        if (layer instanceof OpenLayers.Layer) {
            return layer;
        } else if (layer instanceof GeoExt.data.LayerModel) {
            return layer.getLayer();
        }
    },

    /**
     * Initialize the component.
     *
     * @private
     */
    initComponent: function() {

        this.callParent();

        if (this.changeVisibility && this.layer &&
            (this.layer.opacity == 0 ||
            (this.inverse === false && this.value == this.minValue) ||
            (this.inverse === true && this.value == this.maxValue))) {
            this.layer.setVisibility(false);
        }

        if (this.complementaryLayer &&
            ((this.layer && this.layer.opacity == 1) ||
             (this.inverse === false && this.value == this.maxValue) ||
             (this.inverse === true && this.value == this.minValue))) {
            this.complementaryLayer.setVisibility(false);
        }

        if (this.aggressive === true) {
            this.on('change', this.changeLayerOpacity, this, {
                buffer: this.delay
            });
        } else {
            this.on('changecomplete', this.changeLayerOpacity, this);
        }

        if (this.changeVisibility === true) {
            this.on('change', this.changeLayerVisibility, this, {
                buffer: this.changeVisibilityDelay
            });
        }

        if (this.complementaryLayer) {
            this.on('change', this.changeComplementaryLayerVisibility, this, {
                buffer: this.changeVisibilityDelay
            });
        }
        this.on("beforedestroy", this.unbind, this);
    },

    /**
     * Updates the `OpenLayers.Layer` opacity value.
     *
     * @param {GeoExt.LayerOpacitySlider} slider
     * @param {Number} value The slider value
     * @private
     */
    changeLayerOpacity: function(slider, value) {
        if (this.layer) {
            value = value / (this.maxValue - this.minValue);
            if (this.inverse === true) {
                value = 1 - value;
            }
            this._settingOpacity = true;
            this.layer.setOpacity(value);
            delete this._settingOpacity;
        }
    },

    /**
     * Updates the `OpenLayers.Layer` visibility.
     *
     * @param {GeoExt.LayerOpacitySlider} slider
     * @param {Number} value The slider value
     * @private
     */
    changeLayerVisibility: function(slider, value) {
        var currentVisibility = this.layer.getVisibility();
        if ((this.inverse === false && value == this.minValue) ||
            (this.inverse === true && value == this.maxValue) &&
            currentVisibility === true) {
            this.layer.setVisibility(false);
        } else if ((this.inverse === false && value > this.minValue) ||
            (this.inverse === true && value < this.maxValue) &&
                   currentVisibility == false) {
            this.layer.setVisibility(true);
        }
    },

    /**
     * Updates the complementary `OpenLayers.Layer` visibility.
     *
     * @param {GeoExt.LayerOpacitySlider} slider
     * @param {Number} value The slider value
     * @private
     */
    changeComplementaryLayerVisibility: function(slider, value) {
        var currentVisibility = this.complementaryLayer.getVisibility();
        if ((this.inverse === false && value == this.maxValue) ||
            (this.inverse === true && value == this.minValue) &&
            currentVisibility === true) {
            this.complementaryLayer.setVisibility(false);
        } else if ((this.inverse === false && value < this.maxValue) ||
            (this.inverse === true && value > this.minValue) &&
                   currentVisibility == false) {
            this.complementaryLayer.setVisibility(true);
        }
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * Create a slider tip displaying `Ext.slider.SingleSlider` values over slider
 * thumbs.
 *
 * Example:
 *
 *     var slider = Ext.create('GeoExt.slider.Zoom', {
 *         map: panel.map,
 *         aggressive: true,
 *         width: 200,
 *         plugins: Ext.create('GeoExt.slider.Tip', {
 *             getText: function(thumb) {
 *                 return Ext.String.format(
 *                     '<div>Scale: 1:{0}</div>',
 *                     thumb.slider.getScale()
 *                 );
 *             }
 *         }),
 *         renderTo: Ext.getBody()
 *     });
 *
 * @class GeoExt.slider.Tip
 */
Ext.define('GeoExt.slider.Tip', {
    extend :  Ext.slider.Tip ,
    alternateClassName : 'GeoExt.SliderTip',
               
                        
      
    /**
     * Display the tip when hovering over the thumb.  If `false`, tip will
     * only be displayed while dragging.  Default is `true`.
     *
     * @cfg {Boolean} hover
     */
    hover: true,

    /**
     * Minimum width of the tip.  Default is 10.
     *
     * @cfg {Number} minWidth
     */
    minWidth: 10,

    /**
     * A two item list that provides x, y offsets for the tip.
     *
     * @cfg {Number[]} offsets
     */
    offsets : [0, -10],

    /**
     * The thumb is currently being dragged.
     *
     * @property {Boolean} dragging
     */
    dragging: false,

    /**
     * Called when the plugin is initialized.
     *
     * @param {Ext.slider.SingleSlider} slider
     * @private
     */
    init: function(slider) {
        this.callParent(arguments);
        if (this.hover) {
            slider.on("render", this.registerThumbListeners, this);
        }

        this.slider = slider;
    },

    /**
     * Set as a listener for 'render' if hover is true.
     *
     * @private
     */
    registerThumbListeners: function() {
        var thumb, el;
        for (var i=0, ii=this.slider.thumbs.length; i<ii; ++i) {
            thumb = this.slider.thumbs[i];
            el = thumb.tracker.el;
            (function(thumb, el) {
                el.on({
                    mouseover: function(e) {
                        this.onSlide(this.slider, e, thumb);
                        this.dragging = false;
                    },
                    mouseout: function() {
                        if (!this.dragging) {
                            this.hide.apply(this, arguments);
                        }
                    },
                    scope: this
                });
            }).apply(this, [thumb, el]);
        }
    },

    /**
     * Listener for dragstart and drag.
     *
     * @param {Ext.slider.SingleSlider} slider
     * @param {Object} e
     * @param {Object} thumb
     * @private
     */
    onSlide: function(slider, e, thumb) {
        this.dragging = true;
        return this.callParent(arguments);
    }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Util.js
 * @requires GeoExt/slider/MapPanelItem.js
 * @requires GeoExt/panel/Map.js
 */

/**
 * Create a slider to control the zoom of a layer.
 * Important: Due to changes in OpenLayers 2.13, you need to set 
 * 'zoomMethod: null' on the map to avoid inconsistent zoom-Levels.
 * This is only needed when using 'aggressive: true' on the slider.
 * The issue has already been fixed in the current trunk of OpenLayers.
 *
 * Sample code to render a slider outside the map viewport:
 *
 * Example:
 *
 *     var slider = Ext.create('GeoExt.slider.Zoom', {
 *         renderTo: document.body,
 *         width: 200,
 *         map: map
 *     });
 *
 *  Sample code to add a slider to a map panel:
 *
 * Example:
 *
 *     var panel = Ext.create('GeoExt.panel.Map', {
 *         renderTo: document.body,
 *         height: 300,
 *         width: 400,
 *         map: {
 *             controls: [new OpenLayers.Control.Navigation()],
 *             maxResolution: 0.703125,
 *             zoomMethod: null
 *         },
 *         layers: [new OpenLayers.Layer.WMS(
 *             "Global Imagery",
 *             "http://maps.opengeo.org/geowebcache/service/wms",
 *             {layers: "bluemarble"}
 *         )],
 *         extent: [-5, 35, 15, 55],
 *         items: [{
 *             xtype: "gx_zoomslider",
 *             aggressive: true,
 *             vertical: true,
 *             height: 100,
 *             x: 10,
 *             y: 20
 *         }]
 *     });
 *
 * @class GeoExt.slider.Zoom
 */
Ext.define('GeoExt.slider.Zoom', {
    extend :  GeoExt.slider.MapPanelItem ,
                
                          
      
    alias : 'widget.gx_zoomslider',
    alternateClassName : 'GeoExt.ZoomSlider',

    /**
     * The map that the slider controls.
     *
     * @cfg {OpenLayers.Map/GeoExt.MapPanel} map
     */
    map: null,

    /**
     * The CSS class name for the slider elements.  Default is "gx-zoomslider".
     *
     * @cfg {String} baseCls
     */
    baseCls: "gx-zoomslider",

    /**
     * If set to true, the map is zoomed as soon as the thumb is moved. Otherwise
     * the map is zoomed when the thumb is released (default).
     *
     * @cfg {Boolean} aggressive
     */
    aggressive: false,

    /**
     * The slider position is being updated by itself (based on map zoomend).
     *
     * @property {Boolean} updating
     */
    updating: false,

    /**
     * The map is zoomed by the slider (based on map change/changecomplete).
     *
     * @property {Boolean} zooming
     */
    zooming: false,

    /**
     * Initialize the component.
     *
     * @private
     */
    initComponent: function(){
        this.callParent(arguments);

        if(this.map) {
            if(this.map instanceof GeoExt.MapPanel) {
                this.map = this.map.map;
            }
            this.bind(this.map);
        }

        if (this.aggressive === true) {
            this.on('change', this.changeHandler, this);
        } else {
            this.on('changecomplete', this.changeHandler, this);
        }
        this.on("beforedestroy", this.unbind, this);
    },

    /**
     * Override onRender to set base CSS class.
     *
     * @private
     */
    onRender: function() {
        this.callParent(arguments);
        this.el.addCls(this.baseCls);
    },

    /**
     * Override afterRender because the render event is fired too early to call
     * update.
     *
     * @private
     */
    afterRender : function(){
        this.callParent(arguments);
        this.update();
    },

    /**
     * The base class takes care of the rendered dimensions of the slider, and
     * we only additionally call #bind in the afterrender-event.
     *
     * @inheritdoc
     */
    addToMapPanel: function(panel) {
        var me = this;
        me.callParent(arguments);
        me.on({
            /**
             * Once we are rendered and we know that we are a child of a
             * mappanel, we bind our event handlers to the map.
             */
            afterrender: function(){
                // bind the map to the slider
                me.bind(panel.map);
            }
        });
    },

    /**
     * Registers the relevant listeners on the #map to be in sync with it.
     *
     * @param {OpenLayers.Map} map
     * @private
     */
    bind: function(map) {
        this.map = map;
        this.map.events.on({
            zoomend: this.update,
            changebaselayer: this.initZoomValues,
            scope: this
        });
        if(this.map.baseLayer) {
            this.initZoomValues();
            this.update();
        }
    },

    /**
     * Unregisters the bound listeners on the #map, e.g. when being destroyed.
     *
     * Will automatically be called from the inherited #removeFromMapPanel
     * method.
     *
     * @private
     */
    unbind: function() {
        if(this.map && this.map.events) {
            this.map.events.un({
                zoomend: this.update,
                changebaselayer: this.initZoomValues,
                scope: this
            });
        }
    },

    /**
     * Set the min/max values for the slider if not set in the config.
     *
     * @private
     */
    initZoomValues: function() {
        var layer = this.map.baseLayer;
        if(this.initialConfig.minValue === undefined) {
            this.minValue = layer.minZoomLevel || 0;
        }
        if(this.initialConfig.maxValue === undefined) {
            this.maxValue = layer.minZoomLevel == null ?
                layer.numZoomLevels - 1 : layer.maxZoomLevel;
        }
    },

    /**
     * Get the zoom level for the associated map based on the slider value.
     *
     * @return {Number} The map zoom level.
     */
    getZoom: function() {
        return this.getValue();
    },

    /**
     * Get the scale denominator for the associated map based on the slider
     * value.
     *
     * @return {Number} The map scale denominator.
     */
    getScale: function() {
        return OpenLayers.Util.getScaleFromResolution(
            this.map.getResolutionForZoom(this.getValue()),
            this.map.getUnits()
        );
    },

    /**
     * Get the resolution for the associated map based on the slider value.
     *
     * @return {Number} The map resolution.
     */
    getResolution: function() {
        return this.map.getResolutionForZoom(this.getValue());
    },

    /**
     * Registered as a listener for slider changecomplete. Zooms the map.
     *
     * @private
     */
    changeHandler: function() {
        if(this.map && !this.updating) {
            this.zooming = true;
            this.map.zoomTo(this.getValue());
        }
    },

    /**
     * Registered as a listener for map zoomend.Updates the value of the slider.
     *
     * @private
     */
    update: function() {
        if(this.rendered && this.map && !this.zooming) {
            this.updating = true;
            this.setValue(this.map.getZoom());
            this.updating = false;
        }
        this.zooming = false;
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Util.js
 * @requires GeoExt/Version.js
 */

/**
 * The permalink provider.
 *
 * Sample code displaying a new permalink each time the map is moved:
 *
 *     // create permalink provider
 *     var permalinkProvider = Ext.create('GeoExt.state.PermalinkProvider', {});
 *     // set it in the state manager
 *     Ext.state.Manager.setProvider(permalinkProvider);
 *     // create a map panel, and make it stateful
 *     var mapPanel = Ext.create('GeoExt.panel.Map', {
 *         renderTo: "map",
 *         layers: [
 *             new OpenLayers.Layer.WMS(
 *                 "Global Imagery",
 *                 "http://maps.opengeo.org/geowebcache/service/wms",
 *                 {layers: "bluemarble"}
 *             )
 *         ],
 *         stateId: "map",
 *         prettyStateKeys: true // for pretty permalinks
 *     });
 *     // display permalink each time state is changed
 *     permalinkProvider.on({
 *         statechange: function(provider, name, value) {
 *             alert(provider.getLink());
 *         }
 *     });
 *
 * @class GeoExt.state.PermalinkProvider
 */
Ext.define('GeoExt.state.PermalinkProvider', {
    extend :  Ext.state.Provider ,
                
                        
      
    alias : 'widget.gx_permalinkprovider',

    /**
     *
     */
    constructor: function(config){
        this.callParent(arguments);
        config = config || {};

        var url = config.url;
        delete config.url;

        Ext.apply(this, config);

        this.state = this.readURL(url);

    },

    /**
     * Specifies whether type of state values should be encoded and decoded.
     * Set it to `false` if you work with components that don't require
     * encoding types, and want pretty permalinks.
     *  
     * @property{Boolean}
     * @private
     */
    encodeType: true,

    /**
     * Create a state object from a URL.
     *
     * @param url {String} The URL to get the state from.
     * @return {Object} The state object.
     * @private
     */
    readURL: function(url) {
        var state = {};
        var params = OpenLayers.Util.getParameters(url);
        var k, split, stateId;
        for(k in params) {
            if(params.hasOwnProperty(k)) {
                split = k.split("_");
                if(split.length > 1) {
                    stateId = split[0];
                    state[stateId] = state[stateId] || {};
                    state[stateId][split.slice(1).join("_")] = this.encodeType ?
                    this.decodeValue(params[k]) : params[k];
                }
            }
        }
        return state;
    },

    /**
     * Returns the permalink corresponding to the current state.
     *
     * @param base {String} The base URL, optional.
     * @return {String} The permalink.
     */
    getLink: function(base) {
        base = base || document.location.href;

        var params = {};

        var id, k, state = this.state;
        for(id in state) {
            if(state.hasOwnProperty(id)) {
                for(k in state[id]) {
                    params[id + "_" + k] = this.encodeType ?
                    unescape(this.encodeValue(state[id][k])) : state[id][k];
                }
            }
        }

        // merge params in the URL into the state params
        OpenLayers.Util.applyDefaults(
            params, OpenLayers.Util.getParameters(base));

        var paramsStr = OpenLayers.Util.getParameterString(params);

        var qMark = base.indexOf("?");
        if(qMark > 0) {
            base = base.substring(0, qMark);
        }

        return Ext.urlAppend(base, paramsStr);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * The LayerNode plugin. This is used to create a node that is connected to
 * a layer, so the checkbox and the layer's visibility are in sync. A basic
 * layer node would be configured like this:
 *
 *     {
 *         plugins: ['gx_layernode'],
 *         layer: myLayer
 *     }
 *
 * See GeoExt.data.LayerTreeModel for more details on GeoExt extensions to the
 * node configuration.
 *
 * @class GeoExt.tree.LayerNode
 */
Ext.define('GeoExt.tree.LayerNode', {
    extend:  Ext.AbstractPlugin ,
    alias: 'plugin.gx_layer',
               
                        
      
    /**
     * The init method is invoked after initComponent method has been run for
     * the client Component. It performs plugin initialization.
     * 
     * @param {Ext.Component} target The client Component which owns this
     *     plugin.
     * @private
     */
    init: function(target) {

        this.target = target;
        var layer = target.get('layer');

        target.set('checked', layer.getVisibility());
        if (!target.get('checkedGroup') && layer.isBaseLayer) {
            target.set('checkedGroup', 'gx_baselayer');
        }
        target.set('fixedText', !!target.text);

        target.set('leaf', true);

        if(!target.get('iconCls')) {
            target.set('iconCls', "gx-tree-layer-icon");
        }

        target.on('afteredit', this.onAfterEdit, this);
        layer.events.on({
            "visibilitychanged": this.onLayerVisibilityChanged,
            scope: this
        });
        this.enforceOneVisible();
    },

    /**
     * Handler for the node's afteredit event.
     *
     * @param {GeoExt.data.LayerTreeModel} node
     * @param {String[]} modifiedFields
     * @private
     */
    onAfterEdit: function(node, modifiedFields) {
        var me = this;

        if(~Ext.Array.indexOf(modifiedFields, 'checked')) {
            me.onCheckChange();
        }
    },

    /**
     * Handler for visibilitychanged events on the layer.
     *
     * @private
     */
    onLayerVisibilityChanged: function() {
        if(!this._visibilityChanging) {
            this.target.set('checked', this.target.get('layer').getVisibility());
        }
    },

    /**
     * Updates the visibility of the layer that is connected to the target
     * node.
     *
     * @private
     */
    onCheckChange: function() {
        var node = this.target,
            checked = this.target.get('checked');

        if(checked != node.get('layer').getVisibility()) {
            node._visibilityChanging = true;
            var layer = node.get('layer');
            if(checked && layer.isBaseLayer && layer.map) {
                layer.map.setBaseLayer(layer);
            } else if(!checked && layer.isBaseLayer && layer.map &&
                      layer.map.baseLayer && layer.id == layer.map.baseLayer.id) {
                // Must prevent the unchecking of radio buttons
                node.set('checked', layer.getVisibility());
            } else {
                layer.setVisibility(checked);
            }
            delete node._visibilityChanging;
        }
        this.enforceOneVisible();
    },

    enforceOneVisible: function() {
        var attributes = this.target.data;
        var group = attributes.checkedGroup;
        // If we are in the baselayer group, the map will take care of
        // enforcing visibility.
        if(group && group !== "gx_baselayer") {
            var layer = this.target.get('layer');
            var checkedNodes = this.target.getOwnerTree().getChecked();
            var checkedCount = 0;
            // enforce "not more than one visible"
            Ext.each(checkedNodes, function(n){
                var l = n.data.layer;
                if(!n.data.hidden && n.data.checkedGroup === group) {
                    checkedCount++;
                    if(l != layer && attributes.checked) {
                        l.setVisibility(false);
                    }
                }
            });
            // enforce "at least one visible"
            if(checkedCount === 0 && attributes.checked == false) {
                layer.setVisibility(true);
            }
        }
    }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/tree/LayerNode.js
 */

/**
 * A loader that will load layers from a GeoExt.data.LayerStore.
 * By default, only layers that have `displayInLayerSwitcher` set to `true`
 * will be included. The childrens' iconCls defaults to "gx-tree-layer-icon".
 *
 * Example:
 *
 *     var loader = Ext.create('GeoExt.tree.LayerLoader', {
 *         baseAttrs: {
 *             iconCls: 'baselayer-icon',
 *             checkedGroup: 'baselayer'
 *         },
 *         filter: function(record) {
 *             var layer = record.getLayer();
 *             return layer.displayInLayerSwitcher === true &&
 *                 layer.isBaseLayer === true;
 *         }
 *     });
 *
 * The above creates a loader which only loads base layers, and configures
 * its nodes with the 'baselayer-icon' icon class and the 'baselayer' group.
 * This is basically the same loader that the GeoExt.tree.BaseLayerContainer
 * uses.
 *
 * @class GeoExt.tree.LayerLoader
 */
Ext.define('GeoExt.tree.LayerLoader', {
    extend:  Ext.util.Observable ,
               
                               
      

    /**
     * Triggered before loading children. Return false to avoid
     * loading children.
     *
     * @event beforeload
     * @param {GeoExt.tree.LayerLoader} this This loader.
     * @param {Ext.data.NodeInterface} node The node that this loader is
     *     configured with.
     */

    /**
     * Triggered after children were loaded.
     *
     * @event load
     * @param {GeoExt.tree.LayerLoader} loader This loader.
     * @param {Ext.data.NodeInterface} node The node that this loader is
     *     configured with.
     */

    /**
     * The layer store containing layers to be added by this loader.
     *
     * @cfg {GeoExt.data.LayerStore} store
     */
    /**
     * The layer store containing layers to be added by this loader.
     *
     * @property {GeoExt.data.LayerStore} store
     */
     store: null,

    /**
     * A function, called in the scope of this loader, with a
     * GeoExt.data.LayerRecord as argument. Is expected to return `true` for
     * layers to be loaded, `false` otherwise. By default, the filter checks
     * for `displayInLayerSwitcher`:
     *
     *     filter: function(record) {
     *         return record.getLayer().displayInLayerSwitcher === true
     *     }
     *
     * @property {Function} filter
     * @param {GeoExt.data.LayerRecord} record
     */
    filter: function(record) {
        return record.getLayer().displayInLayerSwitcher === true;
    },

    /**
     * An object containing attributes to be added to all nodes created by
     * this loader.
     *
     * @cfg
     */
    baseAttrs: null,

    /**
     * @param {GeoExt.data.LayerTreeModel} node The node to add children to.
     * @private
     */
    load: function(node) {
        if (this.fireEvent("beforeload", this, node)) {
            this.removeStoreHandlers();
            while (node.firstChild) {
                node.removeChild(node.firstChild);
            }

            if (!this.store) {
                this.store = GeoExt.MapPanel.guess().layers;
            }
            this.store.each(function(record) {
                this.addLayerNode(node, record);
            }, this);
            this.addStoreHandlers(node);

            this.fireEvent("load", this, node);
        }
    },

    /**
     * Listener for the store's add event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Record[]} records
     * @param {Number} index
     * @param {GeoExt.data.LayerTreeModel} node
     * @private
     */
    onStoreAdd: function(store, records, index, node) {
        if (!this._reordering) {
            var nodeIndex = node.get('container')
                .recordIndexToNodeIndex(index+records.length-1, node);
            for (var i=0, ii=records.length; i<ii; ++i) {
                this.addLayerNode(node, records[i], nodeIndex);
            }
        }
    },

    /**
     * Listener for the store's remove event.
     *
     * @param {Ext.data.Store} store
     * @param {Ext.data.Record} record
     * @param {Integer} index
     * @param {GeoExt.data.LayerTreeModel} node
     * @private
     */
    onStoreRemove: function(layerRecord, node) {
        if (!this._reordering) {
            this.removeLayerNode(node, layerRecord);
        }
    },

    /**
     * Adds a child node representing a layer of the map
     *
     * @param {GeoExt.data.LayerTreeModel} node The node that the layer node
     *     will be added to as child.
     * @param {GeoExt.data.LayerModel} layerRecord The layer record containing
     *     the layer to be added.
     * @param {Integer} index Optional index for the new layer.  Default is 0.
     * @private
     */
    addLayerNode: function(node, layerRecord, index) {
        index = index || 0;
        if (this.filter(layerRecord) === true) {
            var layer = layerRecord.getLayer();
            var child = this.createNode({
                plugins: [{
                    ptype: 'gx_layer'
                }],
                layer: layer,
                text: layer.name,
                listeners: {
                    move: this.onChildMove,
                    scope: this
                }
            });
            if (index !== undefined) {
                node.insertChild(index, child);
            } else {
                node.appendChild(child);
            }
            node.getChildAt(index).on("move", this.onChildMove, this);
        }
    },

    /**
     * Removes a child node representing a layer of the map
     *
     * @param {GeoExt.data.LayerTreeModel} node The node that the layer node
     *     will be removed from as child.
     * @param {GeoExt.data.LayerModel} layerRecord The layer record containing
     *     the layer to be removed.
     * @private
     */
    removeLayerNode: function(node, layerRecord) {
        if (this.filter(layerRecord) === true) {
            var child = node.findChildBy(function(node) {
                return node.get('layer') == layerRecord.getLayer();
            });
            if (child) {
                child.un("move", this.onChildMove, this);
                child.remove();
            }
        }
    },

    /**
     * Listener for child node "move" events.  This updates the order of
     * records in the store based on new node order if the node has not
     * changed parents.
     *
     * @param {GeoExt.data.LayerTreeModel} node
     * @param {GeoExt.data.LayerTreeModel} oldParent
     * @param {GeoExt.data.LayerTreeModel} newParent
     * @param {Integer} index
     * @private
     */
    onChildMove: function(node, oldParent, newParent, index) {
        var me = this,
            record = me.store.getByLayer(node.get('layer')),
            container = newParent.get('container'),
            parentLoader = container.loader;

        // remove the record and re-insert it at the correct index
        me._reordering = true;
        if (parentLoader instanceof me.self && me.store === parentLoader.store) {
            parentLoader._reordering = true;
            me.store.remove(record);
            var newRecordIndex;
            if (newParent.childNodes.length > 1) {
                // find index by neighboring node in the same container
                var searchIndex = (index === 0) ? index + 1 : index - 1;
                newRecordIndex = me.store.findBy(function(r) {
                    return newParent.childNodes[searchIndex]
                        .get('layer') === r.getLayer();
                });
                if (index === 0) {
                    newRecordIndex++;
                }
            } else if (oldParent.parentNode === newParent.parentNode) {
                // find index by last node of a container above
                var prev = newParent;
                do {
                    prev = prev.previousSibling;
                } while (prev &&
                    !(prev.get('container') instanceof container.self &&
                    prev.lastChild));
                if (prev) {
                    newRecordIndex = me.store.findBy(function(r) {
                        return prev.lastChild.get('layer') === r.getLayer();
                    });
                } else {
                    // find indext by first node of a container below
                    var next = newParent;
                    do {
                        next = next.nextSibling;
                    } while (next &&
                        !(next.get('container') instanceof container.self &&
                        next.firstChild));
                    if (next) {
                        newRecordIndex = me.store.findBy(function(r) {
                            return next.firstChild.get('layer') === r.getLayer();
                        });
                    }
                    newRecordIndex++;
                }
            }
            if (newRecordIndex !== undefined) {
                me.store.insert(newRecordIndex, [record]);
            } else {
                me.store.insert(oldRecordIndex, [record]);
            }
            delete parentLoader._reordering;
        }
        delete me._reordering;
    },

    /**
     * Adds appropriate listeners on the store.
     *
     * @param {GeoExt.data.LayerTreeModel} node
     * @private
     */
    addStoreHandlers: function(node) {
        if (!this._storeHandlers) {
            this._storeHandlers = {
                "add": function(store, layerRecords, index) {
                    this.onStoreAdd(store, layerRecords, index, node);
                },
                "remove": function(parent, removedRecord) {
                    this.onStoreRemove(removedRecord, node);
                }
            };
            for (var evt in this._storeHandlers) {
                this.store.on(evt, this._storeHandlers[evt], this);
            }
        }
    },

    /**
     * Removes the bound listeners on the store.
     *
     * @private
     */
    removeStoreHandlers: function() {
        if (this._storeHandlers) {
            for (var evt in this._storeHandlers) {
                this.store.un(evt, this._storeHandlers[evt], this);
            }
            delete this._storeHandlers;
        }
    },

    /**
     * Extend this function to modify the node attributes at creation time.
     *
     * @param {Object} attr attributes for the new node
     */
    createNode: function(attr) {
        if (this.baseAttrs){
            Ext.apply(attr, this.baseAttrs);
        }

        return attr;
    },

    /**
     * Unregisters bound listeners via #removeStoreHandlers
     *
     * @private
     */
    destroy: function() {
        this.removeStoreHandlers();
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/tree/LayerLoader.js
 */

/**
 * A layer node plugin that will collect all layers of an OpenLayers map. Only
 * layers that have `displayInLayerSwitcher` set to `true` will be included.
 * The childrens' iconCls defaults to "gx-tree-layer-icon" and this node'
 * text defaults to "Layers".
 *
 * To create a tree node that holds the layers of a tree, it needs to be
 * configured with the gx_layercontainer plugin that this class provides - like
 * the root node in the example below:
 *
 * When you use the tree in an application, make sure to include the proper
 * stylesheet depending on the Ext theme that you use: `tree-classic.css`,
 * `tree-access.css`, 'tree-gray.css` or `tree-neptune.css`.
 *
 *     var mapPanel = Ext.create('GeoExt.panel.Map', {
 *         layers: [new OpenLayers.Layer('foo')]
 *     });
 *
 *     var treeStore = Ext.create('Ext.data.TreeStore', {
 *         model: 'GeoExt.data.LayerTreeModel',
 *         root: {
 *             plugins: [{
 *                 ptype: 'gx_layercontainer',
 *                 loader: {store: mapPanel.layers}
 *             }],
 *             expanded: true
 *         }
 *     });
 *
 * @class GeoExt.tree.LayerContainer
 */
Ext.define('GeoExt.tree.LayerContainer', {
    extend:  Ext.AbstractPlugin ,
               
                                 
      
    alias: 'plugin.gx_layercontainer',

    /**
     * The loader to use with this container. If an Object is provided, a
     * GeoExt.tree.LayerLoader, configured with the the properties from
     * the provided object, will be created. By default, a LayerLoader for
     * all layers of the first MapPanel found by the ComponentManager will be
     * created.
     *
     * @cfg {GeoExt.tree.LayerLoader/Object} loader
     */

    /**
     * The default text for the target node.
     *
     * @private
     */
    defaultText: 'Layers',

    /**
     * @private
     */
    init: function(target) {
        var me = this;

        var loader = me.loader;

        me.loader = (loader && loader instanceof GeoExt.tree.LayerLoader) ?
            loader : new GeoExt.tree.LayerLoader(loader);

        target.set('container', me);
        if (!target.get('text')) {
            target.set('text', me.defaultText);
            target.commit();
        }
        me.loader.load(target);

    },

    /**
     * @param {Number} index  The record index in the layer store.
     * @returns {Number} The appropriate child node index for the record.
     * @private
     */
    recordIndexToNodeIndex: function(index, node) {
        var me = this;
        var store = me.loader.store;
        var count = store.getCount();
        var nodeCount = node.childNodes.length;
        var nodeIndex = -1;
        for(var i=count-1; i>=0; --i) {
            if(me.loader.filter(store.getAt(i)) === true) {
                ++nodeIndex;
                if(index === i || nodeIndex > nodeCount-1) {
                    break;
                }
            }
        }
        return nodeIndex;
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/tree/LayerContainer.js
 */

/**
 * A layer node plugin that will collect all base layers of an OpenLayers
 * map. Only layers that have `displayInLayerSwitcher` set to `true` will
 * be included. The childrens' iconCls defaults to "gx-tree-baselayer-icon"
 * and the node' text defaults to "Base Layer".
 *
 * Children will be rendered with a radio button instead of a checkbox,
 * showing the user that only one base layer can be active at a time.
 *
 * When you use the tree in an application, make sure to include the proper
 * stylesheet depending on the Ext theme that you use: `tree-classic.css`,
 * `tree-access.css`, 'tree-gray.css` or `tree-neptune.css`.
 *
 * To use this node plugin in a tree node config, configure a node like this:
 *
 *     {
 *         plugins: "gx_baselayercontainer",
 *         text: "My base layers"
 *     }
 *
 * @class GeoExt.tree.BaseLayerContainer
 */
Ext.define('GeoExt.tree.BaseLayerContainer', {
    extend:  GeoExt.tree.LayerContainer ,
    alias: 'plugin.gx_baselayercontainer',

    /**
     * The default text for the target node.
     *
     * @private
     */
    defaultText: 'Base Layers',

    /**
     * @private
     */
    init: function(target) {
        var me = this,
            loader = me.loader;

        me.loader = Ext.applyIf(loader || {}, {
            baseAttrs: Ext.applyIf((loader && loader.baseAttrs) || {}, {
                iconCls: 'gx-tree-baselayer-icon',
                checkedGroup: 'baselayer'
            }),
            filter: function(record) {
                var layer = record.getLayer();
                return layer.displayInLayerSwitcher === true &&
                    layer.isBaseLayer === true;
            }
        });
        me.callParent(arguments);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * A subclass of {@link Ext.tree.Column}, which provides indentation and
 * folder structure markup for a Tree, taking into account depth and
 * position within the tree hierarchy.
 *
 * @class GeoExt.tree.Column
 */
Ext.define('GeoExt.tree.Column', {
    extend:  Ext.tree.Column ,
               
                        
      
    alias: 'widget.gx_treecolumn',

    initComponent: function() {
        var me = this;

        me.callParent();

        var parentRenderer = me.renderer;

        this.renderer = function(value, metaData, record, rowIdx, colIdx, store, view) {

            var buf   = [parentRenderer.call(this, value, metaData, record, rowIdx, colIdx, store, view)];

            // Replace all base layers from checkbox to radio
            if(record.get('checkedGroup')) {
                buf[0] = buf[0].replace(/class="([^-]+)-tree-checkbox([^"]+)?"/, 'class="$1-tree-checkbox$2 gx-tree-radio"'); //"
            }

            // Add a hook to add other components in the tree like legend icons
            buf.push('<div class="gx-tree-component gx-tree-component-off" id="tree-record-'+record.id+'"></div>');

            return buf.join('');
        };

    },

    /**
     * A basic default renderer return only it's passed value.
     */
    defaultRenderer: function(value) {
        return value;
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include GeoExt/tree/LayerContainer.js
 */

/**
 * A layer node plugin that will collect all base layers of an OpenLayers
 * map. Only layers that have `displayInLayerSwitcher` set to `true`
 * will be included. The node's text defaults to 'Overlays'.
 *
 * When you use the tree in an application, make sure to include the proper
 * stylesheet depending on the Ext theme that you use: `tree-classic.css`,
 * `tree-access.css`, 'tree-gray.css` or `tree-neptune.css`.
 *
 * To use this node plugin in a tree node config, configure a node like this:
 *
 *     {
 *         plugins: "gx_overlaylayercontainer",
 *         text: "My overlays"
 *     }
 *
 * @class GeoExt.tree.OverlayLayerContainer
 */
Ext.define('GeoExt.tree.OverlayLayerContainer', {
    extend:  GeoExt.tree.LayerContainer ,
    alias: 'plugin.gx_overlaylayercontainer',

    /**
     * The default text for the target node.
     *
     * @private
     */
    defaultText: 'Overlays',

    /**
     * @private
     */
    init: function(target) {
        var me = this;

        var loader = me.loader;

        me.loader = Ext.applyIf(loader || {}, {
            filter: function(record) {
                var layer = record.getLayer();
                return (layer.displayInLayerSwitcher && !layer.isBaseLayer);
            }
        });
        me.callParent(arguments);
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/Version.js
 */

/**
 * This plugin provides basic tree - map synchronisation functionality for a
 * TreeView.
 *
 * It creates a specialized instance of modify the nodes on the fly and adds
 * event listeners to the tree and the maps to get both in sync.
 *
 * Note that the plugin must be added to the tree view, not to the tree panel.
 * For example using viewConfig:
 *
 *     viewConfig: {
 *         plugins: {
 *             ptype: 'layertreeview'
 *         }
 *     }
 *
 * @class GeoExt.tree.View
 */
Ext.define('GeoExt.tree.View', {
    extend:  Ext.tree.View ,
    alias: 'widget.gx_treeview',
               
                        
      
    initComponent : function() {
        var me = this;

        me.on('itemupdate', this.onItem, this);
        me.on('itemadd', this.onItem, this);
        me.on('createchild', this.createChild, this);

        return me.callParent(arguments);
    },

    /**
     * Called when an item updates or is added.
     *
     * @param {Ext.data.Model} record The model instance
     * @param {Number} index The index of the record/node
     * @param {HTMLElement} node The node that has just been updated
     * @param {Object} options Options.
     */
    onItem: function(records, index, node, options) {
        var me = this;

        if(!(records instanceof Array)) {
            records = [records]
        }

        for(var i=0; i<records.length; i++) {
            this.onNodeRendered(records[i]);
        }
    },

    /**
     * Called when a node is being rendered.
     * 
     * 
     */
    onNodeRendered: function(node) {
        var me = this;

        var el = Ext.get('tree-record-'+node.id);
        if(!el) {
            return;
        }

        if(node.get('layer'))
            me.fireEvent('createchild', el, node);

        if(node.hasChildNodes()) {
            node.eachChild(function(node) {
                me.onNodeRendered(node);
            }, me);
        }
    },

    /**
     * Called when an item was created.
     */
    createChild: function(el, node) {
        var component = node.get('component'),
            isChecked = node.get('checked'),
            cmpObj;

        if(component) {

            cmpObj = Ext.ComponentManager.create(component);

            if(cmpObj.xtype &&
               node.gx_treecomponents &&
               node.gx_treecomponents[cmpObj.xtype]) {

                node.gx_treecomponents[cmpObj.xtype].destroy();
                delete node.gx_treecomponents[cmpObj.xtype];

            }

            if(!node.gx_treecomponents) {
                node.gx_treecomponents = {};
            }
            node.gx_treecomponents[cmpObj.xtype] = cmpObj;

            if (isChecked !== false) {
                cmpObj.render(el);
            }

            el.removeCls('gx-tree-component-off');
        }
    }

});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @requires GeoExt/tree/Column.js
 * @requires GeoExt/tree/View.js
 */

/**
 * An Ext.tree.Panel pre-configured with a GeoExt.tree.Column.
 *
 * When you use the tree in an application, make sure to include the proper
 * stylesheet depending on the Ext theme that you use: `tree-classic.css`,
 * `tree-access.css`, 'tree-gray.css` or `tree-neptune.css`.
 * 
 * @class GeoExt.tree.Panel
 */
Ext.define('GeoExt.tree.Panel', {
    extend:  Ext.tree.Panel ,
    alias: 'widget.gx_treepanel',
               
                             
                          
      
    viewType: 'gx_treeview',
    
    initComponent: function() {
        var me = this;

        if (!me.columns) {
            if (me.initialConfig.hideHeaders === undefined) {
                me.hideHeaders = true;
            }
            me.addCls(Ext.baseCSSPrefix + 'autowidth-table');
            me.columns = [{
                xtype    : 'gx_treecolumn',
                text     : 'Name',
                width    : Ext.isIE6 ? null : 10000,
                dataIndex: me.displayField         
            }];
        }

        me.callParent();
    }
});

/*
 * Copyright (c) 2008-2014 The Open Source Geospatial Foundation
 *
 * Published under the BSD license.
 * See https://github.com/geoext/geoext2/blob/master/license.txt for the full
 * text of the license.
 */

/*
 * @include OpenLayers/Feature/Vector.js
 * @include OpenLayers/Geometry.js
 * @include GeoExt/panel/Map.js
 * @requires GeoExt/Version.js
 */

/**
 * A popup is a specialized window that supports anchoring to a particular
 * location in a {@link GeoExt.panel.Map MapPanel}.
 *
 * When a popup is anchored to a {@link #location}, that means that
 * the popup will visibly point to the location on the map,
 * and move accordingly when the map is panned or zoomed.
 *
 * When you use the popup in an application, make sure to include the proper
 * stylesheet depending on the Ext theme that you use: `popup-classic.css`,
 * `popup-access.css`, 'popup-gray.css` or `popup-neptune.css`.
 *
 * Example:
 *
 *     var popup = Ext.create('GeoExt.window.Popup', {
 *         title: "My Popup",
 *         location: feature,
 *         width: 200,
 *         html: "<div>Popup content</div>",
 *         collapsible: true
 *     });
 *
 * Or create it via `xtype` declaration:
 *
 * Example:
 *
 *     var popup = {
 *         xtype: 'gx_popup',
 *         title: "My Popup",
 *         location: feature,
 *         width: 200,
 *         html: "<div>Popup content</div>",
 *         collapsible: true
 *     };
 */
Ext.define('GeoExt.window.Popup', {
    extend:  Ext.window.Window ,
    alias: 'widget.gx_popup',
    alternateClassName : 'GeoExt.Popup',
               
                        
      

    /*
     * Some Ext.Window defaults need to be overriden here
     * because some Ext.Window behavior is not currently supported.
     */

    /**
     * Whether the popup is currently inside the map viewport.
     *
     * @property {Boolean} insideViewport
     * @private
     */
    insideViewport: null,

    /**
     * Animate the transition when the panel is collapsed. Collapsing animation
     * is not supported yet for popups.
     *
     * @property {Boolean} animCollapse
     * @private
     */
    animCollapse: false,

    /**
     * Enable dragging of this Panel. Because the popup defaults to being
     * anchored, and anchored popups should not be draggable.
     *
     * @property {Boolean} draggable
     * @private
     */
    draggable: false,

    /**
     * Give the popup window a shadow. Because shadows are not supported yet for
     * popups (the shadow does not look good with the anchor).
     *
     * @property {Boolean} shadow
     * @private
     */
    shadow: false,

    /**
     * The popup should have a "unpin" tool that unanchors it from
     * its location.
     *
     * @cfg {Boolean} unpinnable
     */
    unpinnable: true,

    /**
     * The map this popup will be anchored to (only required if `anchored`
     * is set to `true` and the map cannot be derived from the `location`'s
     * layer).
     *
     * @cfg {GeoExt.panel.Map/OpenLayers.Map} map
     */
    map: null,

    /**
     * The popup begins anchored to its location.
     *
     * @cfg {Boolean} anchored
     */
    anchored: true,

    /**
     * The popup should pan the map so that the popup is fully in view when it
     * is rendered.
     *
     * @cfg {Boolean} panIn
     */
    panIn: true,

    /**
     * A location for this popup's anchor.
     *
     * @cfg {OpenLayers.Feature.Vector/OpenLayers.LonLat/OpenLayers.Pixel} location
     */
    location: null,

    /**
     * CSS class name for the popup DOM elements.
     *
     * @property {String} popupCls
     * @private
     */
    popupCls: "gx-popup",

    /**
     * CSS class name for the popup's anchor.
     *
     * @cfg {String} ancCls
     */
    ancCls: null,

    /**
     * Controls the anchor position for the popup. If set to `auto`, the anchor
     * will be positioned on the top or the bottom of the window, minimizing map
     * movement. Supported values are `bottom-left`, `bottom-right`, `top-left`,
     * `top-right` or `auto`.
     *
     * @cfg {String} anchorPosition
     */
    anchorPosition: "auto",


    initComponent: function() {
        if(this.map instanceof GeoExt.panel.Map) {
            this.map = this.map.map;
        }
        if(!this.map && this.location instanceof OpenLayers.Feature.Vector &&
                                                        this.location.layer) {
            this.map = this.location.layer.map;
        }
        if (this.location instanceof OpenLayers.Feature.Vector) {
            this.location = this.location.geometry;
        }
        if (this.location instanceof OpenLayers.Geometry) {
            if (typeof this.location.getCentroid == "function") {
                this.location = this.location.getCentroid();
            }
            this.location = this.location.getBounds().getCenterLonLat();
        } else if (this.location instanceof OpenLayers.Pixel) {
            this.location = this.map.getLonLatFromViewPortPx(this.location);
        } else {
            this.anchored = false;
        }

        var mapExtent =  this.map.getExtent();
        if (mapExtent && this.location) {
            this.insideViewport = mapExtent.containsLonLat(this.location);
        }

        if(this.anchored) {
            this.addAnchorEvents();
        }
        this.elements += ',anc';

        this.callParent(arguments);
    },

    /**
     * The "onRender" listener of this component.
     * Executes when the popup is rendered and creates the anchor div
     *
     * @param {Object} ct
     * @param {Object} position
     * @private
     */
    onRender: function(ct, position) {
        this.callParent(arguments);
        this.addCls(this.popupCls);
        if (!this.ancCls) {
        	this.ancCls = this.popupCls + "-anc";
        }

        //create anchor dom element.
        //this.createElement("anc", this.el.dom);
        var dh = Ext.core.DomHelper; // create shorthand alias
        // specification the anchor div
        var spec = {
            tag: 'div',
            cls: this.ancCls
        };

        var ancDiv = dh.append(
            this.el.dom, // the context element
            spec      // the specification object
        );
        this.anc = Ext.get(ancDiv);
    },

    /**
     * Initializes the tools on the popup.  In particular it adds the 'unpin'
     * tool if the popup is unpinnable.
     *
     * @private
     */
    initTools : function() {
        if(this.unpinnable) {
            if (!this.tools) {
                this.tools = [];
            }
            this.tools.push({
                type:'unpin',
                handler: Ext.bind(this.unanchorPopup, this, [])
            });
        }
        this.callParent(arguments);
    },

    /**
     * Override.
     *
     * @inheritdoc
     * @private
     */
    show: function() {
        this.callParent(arguments);
        if(this.anchored) {
            this.position();
            if(this.panIn && !this._mapMove) {
                this.panIntoView();
            }
        }
    },

    /**
     * Override.
     *
     * @inheritdoc
     * @private
     */
    maximize: function() {
        if(!this.maximized && this.anc) {
            this.unanchorPopup();
        }
        this.callParent(arguments);
    },

    /**
     * Sets the size of the popup, taking into account the size of the anchor.
     *
     * @param {Integer} w the width to apply.
     * @param {Integer} h the height to apply.
     */
    setSize: function(w, h) {
        if(this.anc) {
            var ancSize = this.anc.getSize();

            if(typeof w == 'object') {
                h = w.height - ancSize.height;
                w = w.width;
            } else if(!isNaN(h)){
                h = h - ancSize.height;
            }
        }
        this.callParent([w,h]);
    },

    /**
     * Positions the popup relative to its current location.
     *
     * @private
     */
    position: function() {
        if(this._mapMove === true) {
            this.insideViewport = this.map.getExtent().containsLonLat(this.location);
            if(this.insideViewport !== this.isVisible()) {
                this.setVisible(this.insideViewport);
            }
        }

        if(this.isVisible()) {
            var locationPx = this.map.getPixelFromLonLat(this.location),
                mapBox = Ext.fly(this.map.div).getBox(true),
                top = locationPx.y + mapBox.y,
                left = locationPx.x + mapBox.x,
                elSize = this.el.getSize(),
                ancSize = this.anc.getSize(),
                ancPos = this.anchorPosition;

            if (ancPos.indexOf("right") > -1 || locationPx.x > mapBox.width / 2) {
                // right
                this.anc.addCls("right");
                var ancRight = this.el.getX(true) + elSize.width -
                               this.anc.getX(true) - ancSize.width;
                left -= elSize.width - ancRight - ancSize.width / 2;
            } else {
                // left
                this.anc.removeCls("right");
                var ancLeft = this.anc.getLeft(true);
                left -= ancLeft + this.getBorderPadding().beforeY + ancSize.width / 2;
            }

            if (ancPos.indexOf("bottom") > -1 || locationPx.y > mapBox.height / 2) {
                // bottom
                this.anc.removeCls("top");
                // position the anchor
                var popupHeight = this.getHeight();
                if (isNaN(popupHeight) === false) {
                    this.anc.setTop((popupHeight-1-this.getBorderPadding().afterX) + "px");
                }
                top -= elSize.height + ancSize.height;

            } else {
                // top
                this.anc.removeCls("top");
                // remove eventually set top property (bottom-case)
                this.anc.setTop(-ancSize.height-1-this.getBorderPadding().beforeX + "px");
                top += ancSize.height; // ok
            }

            this.setPosition(left, top);
        }
    },

    /**
     * Unanchors a popup from its location. This removes the popup from its
     * MapPanel and adds it to the page body.
     *
     * @private
     */
    unanchorPopup: function() {
        this.removeAnchorEvents();

        //make the window draggable
        this.draggable = true;
        this.header.addCls("x-window-header-draggable");
        var ddDelegate = '#' + Ext.escapeId(this.header.id),
            ddConfig = Ext.applyIf({
                el: this.el,
                delegate: ddDelegate,
                constrain: this.constrain,
                // `constrainHeader` in an Ext.window.Window maps
                // to `constrainDelegate` of the Ext.util.ComponentDragger
                constrainDelegate: this.constrainHeader ? ddDelegate : false,
                constrainTo: this.constrainTo
            }, this.draggable);
        this.dd = new Ext.util.ComponentDragger(this, ddConfig);

        //remove anchor
        this.anc.remove();
        this.anc = null;

        //hide unpin tool
        this.tools.unpin.hide();
    },

    /**
     * Pans the MapPanel's map so that an anchored popup can come entirely
     * into view, with padding specified as per normal OpenLayers. Map popup
     * padding.
     *
     * @private
     */
    panIntoView: function() {
        var mapBox = Ext.fly(this.map.div).getBox(true);

        //assumed viewport takes up whole body element of map panel
        var popupPos =  this.getPosition(true);
        popupPos[0] -= mapBox.x;
        popupPos[1] -= mapBox.y;

        var panelSize = [mapBox.width, mapBox.height]; // [X,Y]

        var popupSize = this.getSize();

        var newPos = [popupPos[0], popupPos[1]];

        //For now, using native OpenLayers popup padding.  This may not be ideal.
        var padding = this.map.paddingForPopups;

        // X
        if(popupPos[0] < padding.left) {
            newPos[0] = padding.left;
        } else if(popupPos[0] + popupSize.width + this.getBorderPadding().beforeY + this.getBorderPadding().afterY> panelSize[0] - padding.right) {
            newPos[0] = panelSize[0] - padding.right - popupSize.width - this.getBorderPadding().beforeY - this.getBorderPadding().afterY;
        }

        // Y
        if(popupPos[1] < padding.top) {
            newPos[1] = padding.top;
        } else if(popupPos[1] + popupSize.height + this.getBorderPadding().beforeX + this.getBorderPadding().afterX > panelSize[1] - padding.bottom) {
            newPos[1] = panelSize[1] - padding.bottom - popupSize.height - this.getBorderPadding().beforeX - this.getBorderPadding().afterX;
        }

        var dx = popupPos[0] - newPos[0];
        var dy = popupPos[1] - newPos[1];

        this.map.pan(dx, dy);
    },

    /**
     * Called during map movements; does reposition the popup.
     *
     * @private
     */
    onMapMove: function() {
        if (!(this.hidden && this.insideViewport)){
            this._mapMove = true;
            this.position();
            delete this._mapMove;
        }
    },

    /**
     * Adds appropriate anchor events.
     *
     * @private
     */
    addAnchorEvents: function() {
        this.map.events.on({
            "move" : this.onMapMove,
            scope : this
        });

        this.on({
            "resize": this.position,
            scope: this
        });
    },

    /**
     * Removes previously bound anchor events.
     *
     * @private
     */
    removeAnchorEvents: function() {
        //stop position with location
        this.map.events.un({
            "move" : this.onMapMove,
            scope : this
        });

        this.un("resize", this.position, this);
    },

    /**
     * Cleanup events before destroying the popup.
     *
     * @private
     */
    beforeDestroy: function() {
        if(this.anchored) {
            this.removeAnchorEvents();
        }
        this.callParent(arguments);
    }

});

