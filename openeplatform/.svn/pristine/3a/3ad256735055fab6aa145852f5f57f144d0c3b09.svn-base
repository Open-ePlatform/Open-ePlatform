Ext.define("OpenEMap.action.Action", {
    extend: GeoExt.Action,
    constructor: function(a) {
        var b = a.mapPanel.map;
        if (a.minScale || a.maxScale) a.minScale || (a.minScale = 0), a.maxScale || (a.maxScale = 99999999999999), b.events.register("zoomend", this, function() {
            b.getScale() >= a.maxScale || b.getScale() <= a.minScale ? this.disable() : this.enable()
        });
        this.callParent(arguments)
    },
    toggle: function() {}
});
Ext.define("OpenEMap.action.MeasureArea", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        a.control = new OpenLayers.Control.DynamicMeasure(OpenLayers.Handler.Polygon, {
            mapPanel: a.mapPanel
        });
        a.iconCls = a.iconCls || "action-measurearea";
        a.tooltip = a.tooltip || "M\x26auml;t area";
        a.toggleGroup = "extraTools";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.Print", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        var b = a.mapPanel.plugins[0],
            c = b.printProvider;
        c.customParams = {
            attribution: a.mapPanel.config.attribution.trim()
        };
        var d = null,
            e = null,
            g = function() {
                d.down("#scale").select(e.scale)
            },
            f = function() {
                d && d.setLoading(!1)
            },
            h = function(a, b) {
                d && d.setLoading(!1);
                Ext.Msg.show({
                    title: "Felmeddelande",
                    msg: "Print failed.\n\n" + b.responseText,
                    icon: Ext.Msg.ERROR
                })
            },
            j = function() {
                c.un("beforedownload", f);
                c.on("printexception", h);
                b.control.events.unregister("transformcomplete",
                    null, g);
                b.removePage(e);
                b.hide();
                d = null;
                k.deactivate()
            };
        a.iconCls = a.iconCls || "action-print";
        a.tooltip = a.tooltip || "Skriv ut";
        a.toggleGroup = "extraTools";
        var k = new(OpenLayers.Class(OpenLayers.Control, {
            initialize: function(a) {
                OpenLayers.Control.prototype.initialize.apply(this, arguments)
            },
            type: OpenLayers.Control.TYPE_TOGGLE,
            activate: function() {
                if (!d) {
                    b.hide();
                    b.show();
                    e = b.addPage();
                    c.dpis.data.items.forEach(function(a) {
                        "56" === a.data.name ? a.data.name = "L\u00e5g" : "127" === a.data.name ? a.data.name = "Medel" : "254" ===
                            a.data.name && (a.data.name = "H\u00f6g")
                    });
                    c.layouts.data.items.forEach(function(a) {
                        /landscape$/.test(a.data.name) ? a.data.displayName = a.data.name.replace("landscape", "liggande") : /portrait$/.test(a.data.name) && (a.data.displayName = a.data.name.replace("portrait", "st\u00e5ende"))
                    });
                    d = new Ext.Window({
                        autoHeight: !0,
                        width: 290,
                        resizable: !1,
                        layout: "fit",
                        bodyPadding: "5 5 0",
                        title: "Utskriftsinst\x26auml;llningar",
                        listeners: {
                            close: j
                        },
                        items: [{
                            xtype: "form",
                            layout: "anchor",
                            defaults: {
                                anchor: "100%"
                            },
                            fieldDefaults: {
                                labelWidth: 120
                            },
                            items: [{
                                xtype: "combo",
                                fieldLabel: "Pappersformat",
                                store: c.layouts,
                                displayField: "displayName",
                                valueField: "name",
                                itemId: "printLayouts",
                                queryMode: "local",
                                value: c.layouts.getAt(0).get("name"),
                                listeners: {
                                    select: function(a, b) {
                                        c.setLayout(b[0])
                                    }
                                }
                            }, {
                                xtype: "combo",
                                fieldLabel: "Kvalit\u00e9",
                                store: c.dpis,
                                displayField: "name",
                                valueField: "value",
                                queryMode: "local",
                                value: c.dpis.first().get("value"),
                                listeners: {
                                    select: function(a, b) {
                                        c.setDpi(b[0])
                                    }
                                }
                            }, {
                                xtype: "combo",
                                fieldLabel: "Skala",
                                store: c.scales,
                                displayField: "name",
                                valueField: "value",
                                queryMode: "local",
                                itemId: "scale",
                                value: c.scales.first().get("value"),
                                listeners: {
                                    select: function(a, b) {
                                        e.setScale(b[0], "m")
                                    }
                                }
                            }]
                        }],
                        bbar: ["-\x3e", {
                            text: "Skriv ut",
                            handler: function() {
                                d.setLoading(!0);
                                b.print()
                            }
                        }]
                    });
                    d.show();
                    d.down("#scale").select(e.scale);
                    var a = d.down("#printLayouts");
                    a.select(a.store.data.get(6));
                    c.setLayout(a.store.data.items[6]);
                    b.control.events.register("transformcomplete", null, g);
                    b.control.events.register("transformcomplete", null, g);
                    c.on("beforedownload", f);
                    c.on("printexception",
                        h);
                    OpenLayers.Control.prototype.activate.apply(this, arguments)
                }
            },
            deactivate: function() {
                d && d.close();
                OpenLayers.Control.prototype.deactivate.apply(this, arguments)
            }
        }))({
            type: OpenLayers.Control.TYPE_TOGGLE
        });
        a.control = k;
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.DrawGeometry", {
    extend: OpenEMap.action.Action,
    isText: function(a) {
        return a && ("Point" === a.geometry || a.geometry instanceof OpenLayers.Geometry.Point) ? a.attributes && a.attributes.type && "label" === a.attributes.type : !1
    },
    constructor: function(a) {
        var b = a.mapPanel.drawLayer;
        a.attributes = a.attributes || {};
        a.geometry = a.geometry || "Polygon";
        var c = OpenLayers.Class(OpenLayers.Control.DrawFeature, {
            drawFeature: function(b) {
                b = new OpenLayers.Feature.Vector(b, a.attributes, a.style);
                !1 !== this.layer.events.triggerEvent("sketchcomplete", {
                    feature: b
                }) && (b.state = OpenLayers.State.INSERT, this.layer.addFeatures([b]), this.featureAdded(b), this.events.triggerEvent("featureadded", {
                    feature: b
                }))
            }
        });
        a.control = new c(b, OpenLayers.Handler[a.geometry]);
        b.events.register("beforefeatureadded", this, function(a) {
            this.isText(a.feature) && Ext.Msg.prompt("Text", "Mata in text:", function(c, g) {
                "ok" == c && (a.feature.attributes.label = g, a.feature.data.label = g, b.redraw())
            })
        });
        a.iconCls = a.iconCls || "action-drawgeometry";
        a.tooltip || (a.tooltip = "Polygon" === a.geometry ?
            "Rita omr\u00e5de" : "Path" === a.geometry ? "Rita linje" : "Point" === a.geometry ? "Rita punkt" : "Rita geometri", this.isText(a) && (a.tooltip = "Placera ut text."));
        a.toggleGroup = "extraTools";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.ModifyText", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        var b = a.mapPanel.drawLayer;
        a.attributes = a.attributes || {};
        a.control = a.mapPanel.selectControl;
        a.control.events.register("deactivate", this, function() {
            console.log("deactivate")
        });
        a.control.events.register("activate", this, function() {
            b.events.register("featureselected", this, function(a) {
                Ext.Msg.prompt("Text", "Mata in text:", function(d, e) {
                    "ok" == d && (a.feature.attributes.label = e, a.feature.data.label = e, b.redraw())
                })
            })
        });
        a.control.events.register("deactivate", this, function() {
            b.events.unregister("featureselected")
        });
        a.iconCls = a.iconCls || "action-selectgeometry";
        a.tooltip = a.tooltip || "\x26Auml;ndra text";
        a.toggleGroup = "extraTools";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.MeasureLine", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        a.control = new OpenLayers.Control.DynamicMeasure(OpenLayers.Handler.Path, {
            maxSegments: null,
            accuracy: 2,
            mapPanel: a.mapPanel
        });
        a.iconCls = a.iconCls || "action-measureline";
        a.tooltip = a.tooltip || "M\u00e4t str\x26auml;cka";
        a.toggleGroup = "extraTools";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.view.IdentifyResults", {
    extend: Ext.panel.Panel,
    autoScroll: !0,
    layout: {
        type: "vbox",
        pack: "start",
        align: "stretch"
    },
    initComponent: function() {
        var a = Ext.create("Ext.data.TreeStore", {
            root: {
                expanded: !0
            }
        });
        this.root = a.getRootNode();
        var b = Ext.create("Ext.grid.property.Grid", {
            flex: 2,
            autoScroll: !0,
            title: "Egenskaper",
            collapsible: !0,
            collapsed: !1,
            xtype: "propertygrid",
            stripeRows: !0,
            clicksToEdit: 100
        });
        this.items = [{
                xtype: "treepanel",
                flex: 1,
                rootVisible: !1,
                store: a,
                minHeight: 200,
                listeners: {
                    select: this.onSelect,
                    scope: this
                }
            },
            b
        ];
        this.callParent(arguments)
    },
    onSelect: function(a, b) {
        var c = {},
            d = b.raw.feature,
            e = b.raw.layer,
            g = function(a) {
                e.metadata.attributes[a] && (c[e.metadata.attributes[a].alias || a] = d.attributes[a])
            };
        d && (e.metadata && e.metadata.attributes ? Object.keys(d.attributes).forEach(g) : c = d.attributes, this.mapPanel.searchLayer.selectedFeatures.forEach(function(a) {
            this.mapPanel.selectControl.unselect(a)
        }, this), b.raw.feature.layer && this.mapPanel.selectControl.select(d));
        var c = Ext.clone(c),
            f = Ext.clone(c);
        Object.keys(c).forEach(function(a) {
            var b =
                f[a];
            b.match("http://") || b.match("//") ? (c[a] = '\x3ca href\x3d"' + b + '" target\x3d"_blank"\x3eL\u00e4nk\x3c/a\x3e', f[a] = {
                renderer: function(a) {
                    return a
                },
                editor: Ext.create("Ext.form.DisplayField")
            }) : f[a] = {
                editor: Ext.create("Ext.form.DisplayField")
            }
        });
        this.down("propertygrid").setSource(c, f)
    },
    addResult: function(a, b) {
        var c = this.root.appendChild({
            text: b.name,
            leaf: !1,
            expanded: !0
        });
        a.forEach(function(a) {
            c.appendChild({
                text: a.attributes[Object.keys(a.attributes)[0]],
                leaf: !0,
                feature: a,
                layer: b
            })
        })
    }
});
Ext.define("OpenEMap.action.Identify", {
    extend: OpenEMap.action.Action,
    popup: null,
    getPopup: function(a) {
        this.popup && this.popup.destroy();
        return this.popup = Ext.create("GeoExt.window.Popup", {
            title: "S\u00f6kresultat",
            location: a.feature,
            anchored: !1,
            unpinnable: !1,
            draggable: !0,
            map: a.mapPanel,
            maximizable: !1,
            minimizable: !1,
            resizable: !0,
            width: 300,
            height: 400,
            layout: "fit",
            items: a.items,
            collapsible: !1,
            x: 200,
            y: 100,
            listeners: {
                close: function() {
                    a.mapPanel.searchLayer.removeAllFeatures()
                }
            }
        })
    },
    constructor: function(a) {
        var b =
            this,
            c = a.mapPanel,
            d = c.searchLayer,
            e = a.map,
            g = a.layers,
            f = OpenLayers.Class(OpenLayers.Control, {
                initialize: function(a) {
                    OpenLayers.Control.prototype.initialize.apply(this, arguments);
                    this.handler = new OpenLayers.Handler.Click(this, {
                        click: this.onClick
                    }, this.handlerOptions)
                },
                onClick: function(a) {
                    c.setLoading(!0);
                    d.destroyFeatures();
                    var f = e.getLonLatFromPixel(a.xy);
                    a = f.lon;
                    var f = f.lat,
                        k = new OpenLayers.Geometry.Point(a, f),
                        l = new OpenLayers.Feature.Vector(k);
                    d.addFeatures([l]);
                    var m = Ext.create("OpenEMap.view.IdentifyResults", {
                        mapPanel: c
                    });
                    b.getPopup({
                        mapPanel: c,
                        location: l,
                        items: m
                    }).show();
                    OpenEMap.requestLM({
                        url: "registerenheter?x\x3d" + a + "\x26y\x3d" + f,
                        success: function(a) {
                            a = Ext.decode(a.responseText);
                            a = new OpenLayers.Feature.Vector(k, {
                                name: a.name
                            });
                            m.addResult([a], {
                                name: "Fastigheter"
                            })
                        },
                        failure: function(a) {
                            Ext.Msg.alert("Fel", a.statusText)
                        },
                        callback: function() {
                            c.setLoading(!1)
                        }
                    });
                    Ext.create("OpenEMap.config.Parser").extractWFS(g).forEach(function(a) {
                        var b = Ext.apply({
                            version: "1.1.0",
                            srsName: "EPSG:3006"
                        }, a.wfs);
                        (new OpenLayers.Protocol.WFS(b)).read({
                            filter: new OpenLayers.Filter({
                                type: OpenLayers.Filter.Spatial.BBOX,
                                value: k.getBounds()
                            }),
                            callback: function(b) {
                                if ((b = b.features) && 0 < b.length) m.addResult(b, a), d.addFeatures(b)
                            }
                        })
                    })
                }
            });
        a.control = new f({
            type: OpenLayers.Control.TYPE_TOGGLE
        });
        a.iconCls = a.iconCls || "action-identify";
        a.tooltip = a.tooltip || "Identifiera";
        a.toggleGroup = "extraTools";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.MetadataInfoColumn", {
    extend: Ext.grid.column.Action,
    requrires: ["Ext.tip.ToolTip", "OpenEMap.data.DataHandler", "OpenEMap.view.MetadataWindow"],
    text: "",
    width: 22,
    menuDisabled: !0,
    xtype: "actioncolumn",
    align: "center",
    iconCls: "action-identify",
    initComponent: function(a) {
        var b = this;
        this.tip = Ext.create("Ext.tip.ToolTip", {
            trackMouse: !0
        });
        this.listeners = {
            mouseover: function(a, d, e, g, f, h) {
                b.tip.setTarget(f.target);
                b.dataHandler && b.dataHandler.getMetadataAbstract(b.getUUIDFromMetadataUrl(h.get("urlToMetadata")),
                    function(a) {
                        a["abstract"] && b.updateTooltip(a["abstract"])
                    })
            },
            mouseout: function() {
                b.tip.update(null);
                b.tip.hide()
            },
            click: function(a, d, e, g, f, h) {
                b.metadataWindow && (b.tip.update(null), b.metadataWindow.showMetadata(b.getUUIDFromMetadataUrl(h.get("urlToMetadata"))))
            }
        };
        this.callParent(arguments)
    },
    updateTooltip: function(a) {
        a && (this.tip.update(a.substr(0, 180) + "..."), this.tip.show())
    },
    getUUIDFromMetadataUrl: function(a) {
        if (a) {
            var b = a.indexOf("id\x3d");
            if (0 < b) return a.substr(b + 3, 36)
        }
        return a
    }
});
Ext.define("OpenEMap.view.DetailReportResults", {
    extend: Ext.view.View,
    autoScroll: !0,
    padding: 5,
    geometry: null,
    initComponent: function() {
        this.store = Ext.create("GeoExt.data.FeatureStore", {
            features: [],
            fields: "COUNT CATEGORY CLARIFICAT DESCRIPTIO REMARK MAPTEXT MAX MIN HEIGHT".split(" ")
        });
        this.tpl = new Ext.XTemplate("\x3ch3\x3e" + this.fbet + "\x3c/h3\x3e", "\x3ch4\x3e" + this.aktbet + "\x3c/h4\x3e", '\x3ctpl for\x3d"."\x3e', '\x3cdiv style\x3d"margin-bottom: 10px;" class\x3d"thumb-wrap"\x3e', "\x3ch4\x3e{COUNT}. {DESCRIPTIO}\x3c/h4\x3e",
            "\x3cp\x3e{REMARK}\x3c/p\x3e", "\x3c/div\x3e", "\x3c/tpl\x3e");
        this.itemSelector = "div.thumb-wrap";
        this.callParent(arguments);
        this.doSearch()
    },
    doSearch: function() {
        var a = this.store,
            b = this.layer,
            c = this.geometry;
        b.destroyFeatures();
        var d = Ext.apply({
            url: "wfs",
            version: "1.1.0",
            srsName: "EPSG:3006",
            featureType: "EgenskapsBestammelser_yta",
            featurePrefix: "RIGES"
        });
        (new OpenLayers.Protocol.WFS(d)).read({
            filter: new OpenLayers.Filter({
                type: OpenLayers.Filter.Spatial.INTERSECTS,
                value: c
            }),
            callback: function(c) {
                if (c = c.features) c.forEach(function(b) {
                    b.attributes.COUNT =
                        a.getCount() + 1;
                    a.addFeatures([b])
                }), b.addFeatures(c)
            }
        })
    },
    onSelect: function(a, b) {
        var c = {},
            d = b.raw.feature,
            e = b.raw.layer,
            g = function(a) {
                e.metadata.attributes[a] && (c[e.metadata.attributes[a].alias || a] = d.attributes[a])
            };
        d && (e.metadata && e.metadata.attributes ? Object.keys(d.attributes).forEach(g) : c = d.attributes, this.mapPanel.searchLayer.selectedFeatures.forEach(function(a) {
            this.mapPanel.selectControl.unselect(a)
        }, this), b.raw.feature.layer && this.mapPanel.selectControl.select(d))
    }
});
Ext.define("OpenEMap.action.DetailReport", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        var b = a.mapPanel,
            c = b.searchLayer,
            d = a.map,
            e = OpenLayers.Class(OpenLayers.Control, {
                initialize: function(a) {
                    OpenLayers.Control.prototype.initialize.apply(this, arguments);
                    this.handler = new OpenLayers.Handler.Click(this, {
                        click: this.onClick
                    }, this.handlerOptions)
                },
                onClick: function(a) {
                    b.setLoading(!0);
                    c.destroyFeatures();
                    var e = d.getLonLatFromPixel(a.xy);
                    a = e.lon;
                    var e = e.lat,
                        h = new OpenLayers.Geometry.Point(a, e),
                        h =
                        new OpenLayers.Feature.Vector(h);
                    c.addFeatures([h]);
                    OpenEMap.requestLM({
                        url: "enhetsomraden?x\x3d" + a + "\x26y\x3d" + e,
                        success: function(a) {
                            a = (new OpenLayers.Format.GeoJSON).read(a.responseText);
                            c.addFeatures(a);
                            var e = c.getDataExtent();
                            d.zoomToExtent(e);
                            var e = a[0].geometry,
                                g = a[0].attributes.name;
                            a = Ext.apply({
                                url: "wfs",
                                version: "1.1.0",
                                srsName: "EPSG:3006",
                                featureType: "DetaljplanGallande_yta",
                                featurePrefix: "RIGES"
                            });
                            (new OpenLayers.Protocol.WFS(a)).read({
                                filter: new OpenLayers.Filter({
                                    type: OpenLayers.Filter.Spatial.INTERSECTS,
                                    value: e
                                }),
                                callback: function(a) {
                                    if ((a = a.features) && 0 < a.length) c.addFeatures(a), a = Ext.create("OpenEMap.view.DetailReportResults", {
                                        mapPanel: b,
                                        fbet: g,
                                        aktbet: a[0].attributes.AKTBET,
                                        geometry: a[0].geometry,
                                        layer: b.drawLayer
                                    }), Ext.create("GeoExt.window.Popup", {
                                        title: "Rapport",
                                        anchored: !1,
                                        unpinnable: !1,
                                        draggable: !0,
                                        map: b,
                                        maximizable: !1,
                                        minimizable: !1,
                                        resizable: !0,
                                        width: 300,
                                        height: 400,
                                        layout: "fit",
                                        items: a,
                                        collapsible: !1
                                    }).show()
                                }
                            })
                        },
                        scope: this,
                        callback: function() {
                            b.setLoading(!1)
                        }
                    })
                }
            });
        a.control = new e({
            type: OpenLayers.Control.TYPE_TOGGLE
        });
        a.iconCls = a.iconCls || "action-detailreport";
        a.tooltip = a.tooltip || "Detaljerad rapport";
        a.toggleGroup = "extraTools";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.DeleteMeasure", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        a.control = new OpenLayers.Control.Button({
            trigger: function() {
                a.mapPanel.measureLayer.removeAllFeatures();
                a.mapPanel.measureLayerArea.removeAllFeatures();
                a.mapPanel.measureLayerLength.removeAllFeatures();
                a.mapPanel.measureLayerSegments.removeAllFeatures();
                a.mapPanel.map.controls.forEach(function(a) {
                    a instanceof OpenLayers.Control.DynamicMeasure && a.deactivate()
                })
            }
        });
        a.iconCls = a.iconCls || "action-deletegeometry";
        a.tooltip = a.tooltip || "Ta bort m\x26auml;tning(ar).";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.DeleteAllFeatures", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        a.control = new OpenLayers.Control.Button({
            trigger: function() {
                a.mapPanel.measureLayer.removeAllFeatures();
                a.mapPanel.measureLayerSegmentsLayer.removeAllFeatures();
                a.mapPanel.map.layers.forEach(function(a) {
                    a instanceof OpenLayers.Layer.Vector && a.removeAllFeatures()
                })
            }
        });
        a.iconCls = a.iconCls || "action-deleteallfeatures";
        a.tooltip = a.tooltip || "Rensa kartan fr\x26aring;n ritade objekt.";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.FullExtent", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        a.control = new OpenLayers.Control.ZoomToMaxExtent;
        a.iconCls = a.iconCls || "action-fullextent";
        a.tooltip = a.tooltip || "Zooma till full utberedning";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.SelectGeometry", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        a.control = a.mapPanel.selectControl;
        a.iconCls = a.iconCls || "action-selectgeometry";
        a.tooltip = a.tooltip || "V\x26auml;lj ritat objekt";
        a.toggleGroup = "extraTools";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.ModifyGeometry", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        var b = a.mapPanel.drawLayer;
        void 0 === a.drag && (a.drag = !0);
        void 0 === a.rotate && (a.rotate = !0);
        void 0 === a.reshape && (a.reshape = !0);
        var c = 0;
        a.drag && (c |= OpenLayers.Control.ModifyFeature.DRAG);
        a.rotate && (c |= OpenLayers.Control.ModifyFeature.ROTATE);
        a.resize && (c |= OpenLayers.Control.ModifyFeature.RESIZE);
        a.reshape && (c |= OpenLayers.Control.ModifyFeature.RESHAPE);
        c = Ext.apply({
            mode: c
        }, a.options);
        a.control = new OpenLayers.Control.ModifyFeature(b,
            c);
        a.control._mode = a.control.mode;
        a.iconCls = a.iconCls || "action-modifygeometry";
        a.tooltip = a.tooltip || "\x26Auml;ndra ritat objekt";
        a.toggleGroup = "extraTools";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.action.DeleteGeometry", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        var b = a.mapPanel,
            c = b.drawLayer;
        a.handler = function() {
            c.selectedFeatures.forEach(function(a) {
                b.map.controls.forEach(function(b) {
                    "OpenLayers.Control.ModifyFeature" == b.CLASS_NAME && b.active && b.unselectFeature(a)
                });
                c.destroyFeatures([a])
            })
        };
        a.iconCls = a.iconCls || "action-deletegeometry";
        a.tooltip = a.tooltip || "Ta bort ritat objekt";
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.ObjectFactory", {
    toPoint: function(a) {
        return new OpenLayers.Geometry.Point(a[0], a[1])
    },
    createR: function(a) {
        var b = a.x,
            c = a.y,
            d = a.l;
        a = a.w;
        b = new OpenLayers.Geometry.LinearRing([
            [b, c],
            [b, c - d],
            [b + a, c - d],
            [b + a, c]
        ].map(this.toPoint));
        return new OpenLayers.Geometry.Polygon([b])
    },
    createL: function(a) {
        var b = a.x,
            c = a.y,
            d = a.l,
            e = a.w,
            g = a.m1;
        a = a.m2;
        b = new OpenLayers.Geometry.LinearRing([
            [b, c],
            [b, c - e],
            [b + g, c - e],
            [b + g, c - a],
            [b + d, c - a],
            [b + d, c]
        ].map(this.toPoint));
        return new OpenLayers.Geometry.Polygon([b])
    },
    createD: function(a) {
        var b = a.x,
            c = a.y,
            d = a.l,
            e = a.w;
        a = (d - a.m1) / 2;
        b = new OpenLayers.Geometry.LinearRing([
            [b, c],
            [b, c - e + a],
            [b + a, c - e],
            [b + d - a, c - e],
            [b + d, c - e + a],
            [b + d, c]
        ].map(this.toPoint));
        return new OpenLayers.Geometry.Polygon([b])
    },
    createO: function(a) {
        var b = a.x,
            c = a.y;
        a = a.m1 / 2 * Math.sqrt(4 + 2 * Math.SQRT2);
        b = new OpenLayers.Geometry.Point(b + a, c - a);
        return OpenLayers.Geometry.Polygon.createRegularPolygon(b, a, 8)
    },
    figurHooks: function(a) {
        var b = a.geometry.move;
        a.geometry.move = function(c, e) {
            a.attributes.config.x += c;
            a.attributes.config.y +=
                e;
            b.apply(a.geometry, arguments)
        };
        var c = a.geometry.rotate;
        a.geometry.rotate = function(b, e) {
            a.attributes.config.angle += b;
            c.apply(a.geometry, arguments)
        }
    },
    create: function(a, b, c) {
        var d;
        d = "R" == a.type ? this.createR(a) : "L" == a.type ? this.createL(a) : "D" == a.type ? this.createD(a) : this.createO(a);
        var e = d.getCentroid(),
            e = new OpenLayers.Geometry.Point(e.x, e.y);
        d.rotate(a.angle, e);
        b = new OpenLayers.Feature.Vector(d, b, c);
        this.figurHooks(b);
        b.attributes.config = a;
        return b
    }
});
Ext.define("OpenEMap.view.ObjectConfig", {
    extend: Ext.form.Panel,
    statics: {
        config: {
            type: "R",
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
    autoHeight: !0,
    width: 400,
    border: !1,
    selectedFeature: void 0,
    layer: void 0,
    factory: void 0,
    hidden: !0,
    defaults: {
        border: !1
    },
    typeLabel: "Type",
    widthLabel: "Width",
    lengthLabel: "Length",
    m1Label: "M1",
    m2Label: "M2",
    angleLabel: "Angle",
    initComponent: function() {
        this.layer = this.mapPanel.drawLayer;
        this.factory = Ext.create("OpenEMap.ObjectFactory");
        var a = [];
        a.push({
            xtype: "radiogroup",
            vertical: !0,
            fieldLabel: this.typeLabel,
            itemId: "type",
            hidden: !0,
            items: [{
                boxLabel: '\x3cdiv class\x3d"objectconfig-radio-r"\x3e\x3c/div\x3e',
                name: "rb",
                inputValue: "R",
                checked: !0
            }, {
                boxLabel: '\x3cdiv class\x3d"objectconfig-radio-l"\x3e\x3c/div\x3e',
                name: "rb",
                enabled: !1,
                inputValue: "L"
            }, {
                boxLabel: '\x3cdiv class\x3d"objectconfig-radio-d"\x3e\x3c/div\x3e',
                name: "rb",
                enabled: !1,
                inputValue: "D"
            }, {
                boxLabel: '\x3cdiv class\x3d"objectconfig-radio-o"\x3e\x3c/div\x3e',
                name: "rb",
                enabled: !1,
                inputValue: "O"
            }],
            listeners: {
                change: function(a,
                    c) {
                    this.config.type = c.rb;
                    this.updateHelpImage(this.config.type);
                    this.setFormItemVisibility(this.config.type);
                    this.createObject()
                },
                scope: this
            }
        });
        a = a.concat([{
            xtype: "numberfield",
            fieldLabel: this.widthLabel,
            itemId: "w",
            minValue: 0,
            listeners: {
                change: function(a, c) {
                    this.config.w = c;
                    this.createObject()
                },
                scope: this
            }
        }, {
            xtype: "numberfield",
            fieldLabel: this.lengthLabel,
            itemId: "l",
            minValue: 0,
            listeners: {
                change: function(a, c) {
                    this.config.l = c;
                    this.createObject()
                },
                scope: this
            }
        }, {
            xtype: "numberfield",
            fieldLabel: this.m1Label,
            itemId: "m1",
            minValue: 0,
            listeners: {
                change: function(a, c) {
                    this.config.m1 = c;
                    this.createObject()
                },
                scope: this
            }
        }, {
            xtype: "numberfield",
            fieldLabel: this.m2Label,
            itemId: "m2",
            minValue: 0,
            listeners: {
                change: function(a, c) {
                    this.config.m2 = c;
                    this.createObject()
                },
                scope: this
            }
        }, {
            xtype: "numberfield",
            itemId: "angle",
            fieldLabel: this.angleLabel,
            value: 0,
            listeners: {
                change: function(a, c) {
                    this.config.angle = c;
                    this.createObject()
                },
                scope: this
            }
        }]);
        this.attributeFields = Ext.create("Ext.container.Container", {
            title: "Attributes"
        });
        a.push(this.attributeFields);
        this.items = [{
            layout: "column",
            defaults: {
                border: !1
            },
            padding: 5,
            items: [{
                width: 180,
                layout: "form",
                items: a
            }, {
                columnWidth: 1,
                padding: 5,
                items: {
                    itemId: "objectimage",
                    border: !1,
                    height: 200
                }
            }]
        }];
        this.layer.events.register("featuremodified", this, this.onFeaturemodified);
        this.layer.events.register("beforefeatureselected", this, this.onBeforefeatureselected);
        this.layer.events.register("featureunselected", this, this.onFeatureunselected);
        this.callParent(arguments)
    },
    setConfig: function(a) {
        void 0 === a ? (this.config = Ext.clone(OpenEMap.view.ObjectConfig.config),
            this.down("#type").show()) : a.type ? (this.config = Ext.clone(a), Ext.applyIf(this.config, OpenEMap.view.ObjectConfig.config), this.down("#type").hide()) : (this.config = Ext.clone(a), Ext.applyIf(this.config, OpenEMap.view.ObjectConfig.config), this.down("#type").show());
        this.setFormValues();
        this.show();
        return this.config
    },
    setFormValues: function() {
        this.config ? (this.down("#type").setValue({
                rb: this.config.type
            }), this.updateHelpImage(this.config.type), this.down("#w").setRawValue(this.config.w), this.down("#l").setRawValue(this.config.l),
            this.down("#m1").setRawValue(this.config.m1), this.down("#m2").setRawValue(this.config.m2), this.down("#angle").setRawValue(this.config.angle), this.setFormItemVisibility(this.config.type), this.down("#angle").show()) : (this.down("#type").hide(), this.down("#w").hide(), this.down("#l").hide(), this.down("#m1").hide(), this.down("#m2").hide(), this.down("#angle").hide(), this.down("#objectimage").hide());
        this.attributeFields.removeAll();
        this.selectedFeature && Object.keys(this.selectedFeature.attributes).forEach(function(a) {
            this.createAttributeField(this.selectedFeature,
                a)
        }, this);
        this.doLayout()
    },
    createAttributeField: function(a, b) {
        if (!("config" == b || "metadata" == b)) {
            var c = a.attributes.metadata;
            (!c || !c[b] || !c[b].hidden) && this.attributeFields.add({
                xtype: "textfield",
                fieldLabel: b,
                value: a.attributes[b],
                listeners: {
                    change: function(a, c) {
                        this.selectedFeature.attributes[b] = c;
                        this.layer.drawFeature(this.selectedFeature)
                    },
                    scope: this
                }
            })
        }
    },
    setFormItemVisibility: function(a) {
        "R" == a ? (this.down("#w").show(), this.down("#l").show(), this.down("#m1").hide(), this.down("#m2").hide()) : "L" ==
            a ? (this.down("#w").show(), this.down("#l").show(), this.down("#m1").show(), this.down("#m2").show()) : "D" == a ? (this.down("#w").show(), this.down("#l").show(), this.down("#m1").show(), this.down("#m2").hide()) : "O" == a && (this.down("#w").hide(), this.down("#l").hide(), this.down("#m1").show(), this.down("#m2").hide())
    },
    onFeaturemodified: function(a) {
        (config = a.feature.attributes.config) && this.down("#angle").setRawValue(config.angle)
    },
    onBeforefeatureselected: function(a) {
        this.selectedFeature = a = a.feature;
        this.config =
            a.attributes.config;
        if ((a = this.gui.activeAction) && a.control instanceof OpenLayers.Control.ModifyFeature) a.control.mode = this.config && a.control._mode & OpenLayers.Control.ModifyFeature.RESHAPE ? a.control._mode ^ OpenLayers.Control.ModifyFeature.RESHAPE : a.control._mode, a.control.resetVertices();
        this.show();
        this.setFormValues()
    },
    onFeatureunselected: function() {
        0 === this.layer.selectedFeatures.length && this.hide();
        this.selectedFeature = void 0
    },
    createObject: function(a, b, c) {
        this.selectedFeature && this.selectedFeature.attributes.config &&
            (a = this.factory.create(this.config, c).geometry, this.selectedFeature.geometry.removeComponent(this.selectedFeature.geometry.components[0]), this.selectedFeature.geometry.addComponent(a.components[0]), this.selectedFeature.modified = !0, this.selectedFeature.geometry.calculateBounds(), this.mapPanel.map.controls.forEach(function(a) {
                "OpenLayers.Control.ModifyFeature" == a.CLASS_NAME && a.active && a.resetVertices()
            }), this.layer.drawFeature(this.selectedFeature), this.layer.events.triggerEvent("featuremodified", {
                feature: this.selectedFeature
            }))
    },
    updateHelpImage: function(a) {
        a = "figur-" + a + "-help.png";
        this.down("#objectimage").show();
        this.down("#objectimage").update('\x3cimg src\x3d"' + OpenEMap.basePathImages + a + '"\x3e\x3c/img\x3e')
    }
});
Ext.define("OpenEMap.action.DrawObject", {
    extend: OpenEMap.action.Action,
    constructor: function(a) {
        this.mapPanel = a.mapPanel;
        this.layer = this.mapPanel.drawLayer;
        this.style = a.style;
        this.attributes = a.attributes;
        this.objectConfig = a.objectConfig;
        this.objectConfigView = a.objectConfigView;
        this.factory = Ext.create("OpenEMap.ObjectFactory");
        this.attributes = this.attributes || {};
        var b = OpenLayers.Class(OpenLayers.Control, {
            initialize: function(a) {
                OpenLayers.Control.prototype.initialize.apply(this, arguments);
                this.handler =
                    new OpenLayers.Handler.Click(this, {
                        click: this.onClick
                    }, this.handlerOptions)
            },
            onClick: Ext.bind(this.onClick, this)
        });
        a.control = new b({
            type: OpenLayers.Control.TYPE_TOGGLE
        });
        a.iconCls = a.iconCls || "action-drawobject";
        a.tooltip = a.tooltip || "Rita f\x26ouml;rdefinierad form.";
        a.toggleGroup = "extraTools";
        this.callParent(arguments)
    },
    onClick: function(a) {
        a = this.mapPanel.map.getLonLatFromPixel(a.xy);
        var b = this.objectConfigView ? this.objectConfigView.config : OpenEMap.view.ObjectConfig.config,
            b = Ext.clone(b);
        b.x = a.lon;
        b.y = a.lat;
        a = this.factory.create(b, this.attributes, this.style);
        this.mapPanel.unselectAll();
        this.layer.addFeatures([a]);
        this.mapPanel.selectControl.select(a)
    },
    toggle: function(a) {
        a && this.objectConfigView.setConfig(this.objectConfig)
    }
});
Ext.define("OpenEMap.view.BaseLayers", {
    extend: Ext.toolbar.Toolbar,
    border: !1,
    cls: "oep-map-tools",
    constructor: function(a) {
        var b = a.mapPanel,
            c = b.map,
            d = b.map.layers.filter(function(a) {
                return a.isBaseLayer
            });
        this.items = d.map(function(a) {
            var b;
            a == d[d.length - 1] && (b = "oep-tools-last");
            return Ext.create("Ext.Button", {
                text: a.name,
                toggleGroup: "baseLayers",
                allowDepress: !1,
                layer: a,
                pressed: a.visibility,
                cls: b,
                listeners: {
                    toggle: function(b, d) {
                        d && c.setBaseLayer(a)
                    }
                }
            })
        });
        c.events.register("changebaselayer", this, this.onChangeBaseLayer);
        this.callParent(arguments)
    },
    onChangeBaseLayer: function(a) {
        this.items.each(function(b) {
            b.toggle(b.layer == a.layer, !0)
        })
    }
});
Ext.define("OpenEMap.view.Map", {
    extend: GeoExt.panel.Map,
    border: !1,
    anchor: "100% 100%",
    constructor: function(a) {
        this.initDefaultLayers(a.config);
        var b = Ext.create("GeoExt.data.MapfishPrintProvider", {
                url: OpenEMap.basePathMapFish,
                autoLoad: !0,
                timeout: 6E4,
                listeners: {
                    encodelayer: function(a, b, c) {
                        c && c.baseURL && (c.baseURL = c.baseURL.replace("gwc/service/", ""))
                    }
                }
            }),
            c = Ext.create("GeoExt.plugins.PrintExtent", {
                printProvider: b
            });
        this.encode = function(a) {
            var e = c.addPage();
            a && (a = b.layouts.findRecord("name", a), b.setLayout(a));
            a = b.encode(c.map, c.pages);
            c.removePage(e);
            return a
        };
        b.encode = function(a, b, c) {
            a instanceof GeoExt.MapPanel && (a = a.map);
            b = b instanceof Array ? b : [b];
            c = c || {};
            if (!1 !== this.fireEvent("beforeprint", this, a, b, c)) {
                var f = Ext.apply({
                        units: a.getUnits(),
                        srs: a.baseLayer.projection.getCode(),
                        layout: this.layout.get("name"),
                        dpi: this.dpi.get("value")
                    }, this.customParams),
                    h = b[0].feature.layer,
                    j = [],
                    k = a.layers.concat();
                Ext.Array.remove(k, a.baseLayer);
                Ext.Array.insert(k, 0, [a.baseLayer]);
                Ext.each(k, function(a) {
                    a !== h && !0 ===
                        a.getVisibility() && (a = this.encodeLayer(a)) && j.push(a)
                }, this);
                f.layers = j;
                var l = [];
                Ext.each(b, function(a) {
                    l.push(Ext.apply({
                        center: [a.center.lon, a.center.lat],
                        scale: a.scale.get("value"),
                        rotation: a.rotation
                    }, a.customParams))
                }, this);
                f.pages = l;
                if (c.overview) {
                    var m = [];
                    Ext.each(c.overview.layers, function(a) {
                        (a = this.encodeLayer(a)) && m.push(a)
                    }, this);
                    f.overviewLayers = m
                }
                if (c.legend && !1 !== this.fireEvent("beforeencodelegend", this, f, c.legend)) {
                    a = c.legend;
                    (b = a.rendered) || (a = a.cloneConfig({
                        renderTo: document.body,
                        hidden: !0
                    }));
                    var n = [];
                    a.items && a.items.each(function(a) {
                        if (!a.hidden) {
                            var b = this.encoders.legends[a.getXType()];
                            n = n.concat(b.call(this, a, f.pages[0].scale))
                        }
                    }, this);
                    b || a.destroy();
                    f.legends = n
                }
                return f
            }
        };
        a.plugins = [c];
        this.callParent(arguments);
        this.layers.add(this.searchLayer);
        this.layers.add(this.drawLayer);
        this.layers.add(this.measureLayer);
        this.layers.add(this.measureLayerArea);
        this.layers.add(this.measureLayerLength);
        this.layers.add(this.measureLayerSegments);
        this.map.setLayerIndex(this.measureLayer,
            98);
        this.map.setLayerIndex(this.measureLayerArea, 98);
        this.map.setLayerIndex(this.measureLayerLength, 98);
        this.map.setLayerIndex(this.measureLayerSegments, 98);
        this.selectControl = new OpenLayers.Control.SelectFeature(this.drawLayer);
        this.map.addControl(this.selectControl)
    },
    unselectAll: function() {
        this.drawLayer.selectedFeatures.forEach(function(a) {
            this.selectControl.unselect(a)
        }, this)
    },
    parseStyle: function(a) {
        var b = {
                Point: {
                    pointRadius: 4,
                    graphicName: "square",
                    fillColor: "#e8ffee",
                    fillOpacity: 0.9,
                    strokeWidth: 1,
                    strokeOpacity: 1,
                    strokeColor: "#29bf4c"
                },
                Line: {
                    strokeWidth: 3,
                    strokeColor: "#29bf4c",
                    strokeOpacity: 1
                },
                Polygon: {
                    strokeWidth: 3,
                    strokeOpacity: 1,
                    strokeColor: "#29bf4c",
                    fillColor: "#e8ffee",
                    fillOpacity: 0.9
                }
            },
            c = function(a) {
                var c = Ext.clone(b);
                a.Point ? (Ext.apply(c.Point, a.Point), Ext.apply(c.Line, a.Line), Ext.apply(c.Polygon, a.Polygon), a.labelSegments && Ext.apply(c.labelSegments, a.labelSegments), a.labelLength && Ext.apply(c.labelLength, a.labelLength)) : (Ext.apply(c.Point, a), Ext.apply(c.Line, a), Ext.apply(c.Polygon, a));
                return c
            },
            d = new OpenLayers.Style(null, {
                rules: [new OpenLayers.Rule({
                    symbolizer: b
                })]
            }),
            e, g, f;
        a && (a["default"] && (d = c(a["default"]), d = new OpenLayers.Style(null, {
                rules: [new OpenLayers.Rule({
                    symbolizer: d
                })]
            })), a.select && (e = c(a.select), e = new OpenLayers.Style(null, {
                rules: [new OpenLayers.Rule({
                    symbolizer: e
                })]
            })), a.temporary && (g = c(a.temporary), g = new OpenLayers.Style(null, {
                rules: [new OpenLayers.Rule({
                    symbolizer: g
                })]
            })), a.labelLength && (f = c(a), f = new OpenLayers.Style(null, {
                rules: [new OpenLayers.Rule({
                    symbolizer: f
                })]
            })),
            a["default"] || (d = c(a), d = new OpenLayers.Style(null, {
                rules: [new OpenLayers.Rule({
                    symbolizer: d
                })]
            })));
        a = {
            "default": d
        };
        e && (a.select = e);
        g && (a.temporary = g);
        f && (a["default"] = f);
        return new OpenLayers.StyleMap(a)
    },
    initDefaultLayers: function(a) {
        a.drawStyle || (a.drawStyle = {
            "default": {
                Point: {
                    pointRadius: 4,
                    graphicName: "square",
                    fillColor: "#ffffff",
                    fillOpacity: 1,
                    strokeWidth: 1,
                    strokeOpacity: 1,
                    strokeColor: "#2969bf"
                },
                Line: {
                    strokeWidth: 3,
                    strokeColor: "#2969bf",
                    strokeOpacity: 1
                },
                Polygon: {
                    strokeWidth: 3,
                    strokeOpacity: 1,
                    strokeColor: "#2969bf",
                    fillOpacity: 0
                }
            },
            select: {
                strokeWidth: 3,
                strokeOpacity: 1,
                fillColor: "#deecff",
                fillOpacity: 0.9,
                strokeColor: "#2969bf"
            },
            temporary: {
                strokeWidth: 3,
                strokeOpacity: 0.75,
                fillColor: "#ff00ff",
                fillOpacity: 0,
                strokeColor: "#ff00ff"
            }
        });
        this.drawLayer = new OpenLayers.Layer.Vector("Drawings", {
            displayInLayerSwitcher: !1,
            styleMap: this.parseStyle(a.drawStyle)
        });
        a.autoClearDrawLayer && this.drawLayer.events.register("beforefeatureadded", this, function() {
            this.drawLayer.destroyFeatures()
        });
        this.drawLayer.events.register("beforefeaturemodified",
            this, function(a) {
                this.selectControl.select(a.feature)
            });
        this.drawLayer.events.register("afterfeaturemodified", this, function() {});
        this.searchLayer = new OpenLayers.Layer.Vector("Searchresult", {
            displayInLayerSwitcher: !1,
            styleMap: this.parseStyle({
                Point: {
                    pointRadius: 4,
                    graphicName: "square",
                    fillColor: "#ffffff",
                    fillOpacity: 1,
                    strokeWidth: 1,
                    strokeOpacity: 1,
                    strokeColor: "#2969bf"
                },
                Line: {
                    strokeWidth: 3,
                    strokeColor: "#2969bf",
                    strokeOpacity: 1
                },
                Polygon: {
                    strokeDashstyle: "dot",
                    strokeWidth: 3,
                    strokeOpacity: 1,
                    strokeColor: "#f58d1e",
                    fillOpacity: 0
                }
            })
        });
        var b = OpenLayers.Control.DynamicMeasure.styles;
        a = new OpenLayers.Style(null, {
            rules: [new OpenLayers.Rule({
                symbolizer: {
                    Point: b.Point,
                    Line: b.Line,
                    Polygon: b.Polygon
                }
            })]
        });
        a = new OpenLayers.StyleMap({
            "default": a
        });
        var c = function(a) {
            return new OpenLayers.StyleMap({
                "default": OpenLayers.Util.applyDefaults(null, b[a])
            })
        };
        this.measureLayer = new OpenLayers.Layer.Vector("MeasureLayer", {
            displayInLayerSwitcher: !1,
            styleMap: a
        });
        this.measureLayerArea = new OpenLayers.Layer.Vector("MeasureLayerArea", {
            displayInLayerSwitcher: !1,
            styleMap: c("labelArea")
        });
        this.measureLayerSegments = new OpenLayers.Layer.Vector("MeasureLayerSegments", {
            displayInLayerSwitcher: !1,
            styleMap: c("labelSegments")
        });
        this.measureLayerLength = new OpenLayers.Layer.Vector("MeasureLayerLength", {
            displayInLayerSwitcher: !1,
            styleMap: c("labelLength")
        })
    },
    setInitialExtent: function() {
        var a = this.map;
        a.getCenter() || (this.center || this.zoom ? a.setCenter(this.center, this.zoom) : this.extent instanceof OpenLayers.Bounds ? a.zoomToExtent(this.extent, !1) : a.zoomToMaxExtent())
    }
});
Ext.define("OpenEMap.view.SearchCoordinate", {
    extend: Ext.container.Container,
    layout: "column",
    defaults: {
        labelWidth: 20
    },
    width: 300,
    border: !1,
    zoom: 5,
    initComponent: function(a) {
        this.items = [{
            itemId: "e",
            fieldLabel: "E",
            xtype: "textfield",
            columnWidth: 0.5
        }, {
            itemId: "n",
            fieldLabel: "N",
            xtype: "textfield",
            columnWidth: 0.5
        }, {
            xtype: "button",
            text: "S\u00f6k",
            handler: function() {
                var a = this.down("#e").getValue(),
                    c = this.down("#n").getValue();
                this.mapPanel.map.setCenter([a, c], this.zoom);
                this.fireEvent("searchcomplete", [a, c])
            },
            scope: this
        }];
        this.addEvents(["searchcomplete"]);
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.form.SearchRegisterenhet", {
    extend: Ext.form.field.ComboBox,
    alias: "widget.searchregisterenhet",
    require: ["Ext.data.*", "Ext.form.*"],
    queryDelay: 800,
    initComponent: function() {
        var a, b;
        this.search && this.search.options && (a = this.search.options.municipalities.join(","), b = this.search.options.zoom);
        var c = this.mapPanel.searchLayer;
        this.store = Ext.create("Ext.data.Store", {
            proxy: {
                type: "ajax",
                url: OpenEMap.basePathLM + "registerenheter",
                extraParams: {
                    lmuser: OpenEMap.lmUser
                },
                reader: {
                    type: "json",
                    root: "features"
                }
            },
            fields: [{
                name: "id",
                mapping: "properties.objid"
            }, {
                name: "name",
                mapping: "properties.name"
            }]
        });
        if (this.store.loading && this.store.lastOperation) {
            var d = Ext.Ajax.requests;
            for (id in d) d.hasOwnProperty(id) && d[id].options == this.store.lastOperation.request && Ext.Ajax.abort(d[id])
        }
        this.store.on("beforeload", function(a, b) {
            a.lastOperation = b
        }, this, {
            single: !0
        });
        this.labelWidth = 60;
        this.displayField = "name";
        this.valueField = "id";
        this.queryParam = "q";
        this.forceSelection = this.typeAhead = !0;
        this.listeners = {
            select: function(a,
                d) {
                var f = d[0].get("id");
                this.mapPanel.setLoading(!0);
                this.mapPanel.searchLayer.destroyFeatures();
                OpenEMap.requestLM({
                    url: "registerenheter/" + f + "/enhetsomraden?",
                    success: function(a) {
                        this.resultPanel.expand();
                        a = (new OpenLayers.Format.GeoJSON).read(a.responseText);
                        c.addFeatures(a);
                        a = c.getDataExtent();
                        b ? this.mapPanel.map.setCenter(a.getCenterLonLat(), b) : this.mapPanel.map.zoomToExtent(a)
                    },
                    failure: function() {
                        Ext.Msg.alert("Fel", "Ingen tr\u00e4ff.")
                    },
                    callback: function() {
                        this.mapPanel.setLoading(!1)
                    },
                    scope: this
                })
            },
            beforequery: function(b) {
                a && null === b.query.match(a) && (b.query = a + " " + b.query)
            },
            scope: this
        };
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.form.SearchAddress", {
    extend: Ext.form.field.ComboBox,
    alias: "widget.searchaddress",
    require: ["Ext.data.*", "Ext.form.*"],
    initComponent: function() {
        var a, b = 11;
        this.search && this.search.options && (a = this.search.options.municipalities.join(","), b = this.search.options.zoom);
        var c = this.mapPanel.searchLayer;
        this.store = Ext.create("Ext.data.Store", {
            proxy: {
                type: "ajax",
                url: OpenEMap.basePathLM + "addresses",
                extraParams: {
                    lmuser: OpenEMap.lmUser
                },
                reader: {
                    type: "array"
                }
            },
            fields: ["id", "name", "x", "y", "fnr"]
        });
        if (this.store.loading && this.store.lastOperation) {
            var d = Ext.Ajax.requests;
            for (id in d) d.hasOwnProperty(id) && d[id].options == this.store.lastOperation.request && Ext.Ajax.abort(d[id])
        }
        this.store.on("beforeload", function(a, b) {
            a.lastOperation = b
        }, this, {
            single: !0
        });
        this.labelWidth = 60;
        this.displayField = "name";
        this.valueField = "id";
        this.queryParam = "q";
        this.forceSelection = this.typeAhead = !0;
        this.listeners = {
            select: function(a, d) {
                var f = d[0].data.fnr,
                    h = d[0].data.x,
                    j = d[0].data.y;
                this.mapPanel.setLoading(!0);
                this.mapPanel.searchLayer.destroyFeatures();
                OpenEMap.requestLM({
                    url: "registerenheter?fnr\x3d" + f,
                    success: function(a) {
                        !1 === Ext.decode(a.responseText).success ? Ext.Msg.alert("Meddelande", "Ingen fastighet kunde hittas p\u00e5 adressen.") : (this.resultPanel.expand(), a = (new OpenLayers.Format.GeoJSON).read(a.responseText, "FeatureCollection"), c.addFeatures(a), a = new OpenLayers.Geometry.Point(h, j), feature = new OpenLayers.Feature.Vector(a), c.addFeatures([feature]), this.mapPanel.map.setCenter([h, j], b))
                    },
                    failure: function() {
                        Ext.Msg.alert("Fel", "Ok\u00e4nt.")
                    },
                    callback: function() {
                        this.mapPanel.setLoading(!1)
                    },
                    scope: this
                })
            },
            beforequery: function(b) {
                a && null === b.query.match(a) && (b.query = a + " " + b.query)
            },
            scope: this
        };
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.form.SearchPlacename", {
    extend: Ext.form.field.ComboBox,
    alias: "widget.searchplacename",
    require: ["Ext.data.*", "Ext.form.*"],
    initComponent: function() {
        var a, b = 5;
        this.search && this.search.options && (a = this.search.options.municipalities.join(","), b = this.search.options.zoom);
        this.store = Ext.create("Ext.data.Store", {
            proxy: {
                type: "ajax",
                url: OpenEMap.basePathLM + "placenames",
                extraParams: {
                    lmuser: OpenEMap.lmUser,
                    kommunkod: a
                },
                reader: {
                    type: "json",
                    root: "features"
                }
            },
            fields: [{
                name: "id",
                mapping: "properties.id"
            }, {
                name: "name",
                mapping: "properties.name"
            }]
        });
        if (this.store.loading && this.store.lastOperation)
            for (id in a = Ext.Ajax.requests, a) a.hasOwnProperty(id) && a[id].options == this.store.lastOperation.request && Ext.Ajax.abort(a[id]);
        this.store.on("beforeload", function(a, b) {
            a.lastOperation = b
        }, this, {
            single: !0
        });
        this.labelWidth = 60;
        this.displayField = "name";
        this.valueField = "id";
        this.queryParam = "q";
        this.forceSelection = this.typeAhead = !0;
        this.listeners = {
            select: function(a, d) {
                var e = d[0].raw.geometry.coordinates;
                this.mapPanel.map.setCenter([e[1],
                    e[0]
                ], b)
            },
            scope: this
        };
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.view.SearchFastighet", {
    extend: Ext.form.Panel,
    border: !1,
    initComponent: function() {
        function a(a) {
            var b = null,
                b = "searchregisterenhet" === a ? this.search && this.search.searchEstates ? this.search.searchEstates : null : "searchaddress" === a ? this.search && this.search.searchAddresses ? this.search.searchAddresses : null : this.search && this.search.searchPlacenames ? this.search.searchPlacenames : null;
            return {
                xtype: a,
                mapPanel: this.mapPanel,
                basePath: this.basePath,
                search: b,
                resultPanel: c
            }
        }
        this.renderTo || (this.title =
            "S\u00f6k fastighet", this.bodyPadding = 5);
        
        var selectedValue = "";
        
        var data = [];
        if (this.search && this.search.searchEstates) {
			data.push(['searchregisterenhet', 'Fastighet' ]);
			selectedValue = "searchregisterenhet";
		}
        if (this.search && this.search.searchAddresses) {
			data.push(['searchaddress', 'Adress']);
			selectedValue = selectedValue == "" ? "searchaddress" : selectedValue;
		}
        if (this.search && this.search.searchPlacenames) {
			data.push(['searchplacename', 'Ort']);
			selectedValue = selectedValue == "" ? "searchplacename" : selectedValue;
		}
		if (this.search && this.search.searchES && this.search.searchES.detaljplan) {
			data.push(['searches', 'Detaljplaner']);
			selectedValue = selectedValue == "" ? "searches" : selectedValue;
		}
        
        var b = Ext.create("GeoExt.data.FeatureStore", {
                layer: this.mapPanel.searchLayer,
                featureFilter: new OpenLayers.Filter.Function({
                    evaluate: function(a) {
                        return a.attributes.name ? !0 : !1
                    }
                }),
                fields: [{
                    name: "name"
                }, {
                    name: "fid"
                }, {
                    name: "objid"
                }]
            }),
            c = Ext.create("Ext.grid.Panel", {
                columns: [{
                    text: "Namn",
                    dataIndex: "name",
                    flex: 1
                }],
                store: b,
                selType: "featuremodel"
            });
        
        this.items = [{
            layout: "column",
            border: !1,
            items: [{
                xtype: "combo",
                width: 110,
                store: data,
                forceSelection: !0,
                queryMode: "local",
                value: selectedValue,
                border: !1,
                listeners: {
                    change: function(b, c) {
                        var g = this.down("#search");
                        this.mapPanel.searchLayer.destroyFeatures();
                        g.removeAll();
                        g.add(a.call(this, c))
                    },
                    scope: this
                }
            }, {
                itemId: "search",
                columnWidth: 1,
                layout: "fit",
                border: !1,
                items: a.call(this, selectedValue)
            }]
        }];
        this.renderTo || this.items.push(c);
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.view.ZoomTools", {
    extend: Ext.panel.Panel,
    bodyStyle: "background : transparent",
    border: !1,
    getTools: function() {
        var a = Ext.util.CSS.getRule(".oep-tools"),
            b = a ? "large" : "medium",
            c = [],
            a = Ext.create("GeoExt.slider.Zoom", {
                height: 200,
                vertical: !0,
                aggressive: !0,
                margin: a ? "5 0 5 0" : "5 0 5 8",
                map: this.mapPanel.map
            });
        c.push({
            xtype: "button",
            iconCls: "zoomtools-plus",
            mapPanel: this.mapPanel,
            scale: b,
            cls: "x-action-btn",
            listeners: {
                click: function() {
                    this.mapPanel.map.zoomIn()
                },
                scope: this
            }
        });
        c.push(a);
        c.push({
            xtype: "button",
            scale: b,
            cls: "x-action-btn",
            iconCls: "zoomtools-minus",
            mapPanel: this.mapPanel,
            listeners: {
                click: function() {
                    this.mapPanel.map.zoomOut()
                },
                scope: this
            }
        });
        return c
    },
    constructor: function(a) {
        Ext.apply(this, a);
        this.items = this.getTools();
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.Gui", {
    activeAction: null,
    objectConfigWindowTitle: "Object configuration",
    constructor: function(a) {
        this.config = a.config;
        this.gui = a.gui;
        this.map = a.map;
        this.orginalConfig = a.orginalConfig;
        this.serverStore = a.serverStore;
        this.search = a.config.search;
        void 0 === this.gui && (this.gui = {
            map: !1,
            toolbar: {},
            zoomTools: {},
            baseLayers: {},
            layers: {},
            searchFastighet: {},
            objectConfig: {},
            searchCoordinate: !1
        });
        this.mapPanel = Ext.create("OpenEMap.view.Map", {
            map: this.map,
            extent: this.config.extent,
            config: this.config,
            listeners: {
                afterrender: function() {
                    if (this.config.attribution) {
                        var a = this.mapPanel.getEl();
                        Ext.DomHelper.append(a, '\x3cspan class\x3d"unselectable attribution"\x3e' + this.config.attribution + "\x3c/span\x3e")
                    }
                },
                scope: this
            }
        });
        this.createPanels();
        this.createToolbar();
        a = [];
        a.push(this.mapPanel);
        this.zoomTools && a.push(this.zoomTools);
        this.leftPanel && a.push(this.leftPanel);
        this.rightPanel && a.push(this.rightPanel);
        this.baseLayers && a.push(this.baseLayers);
        if (this.gui.map) {
            var b = this.gui.map.renderTo ? Ext.get(this.gui.map.renderTo) :
                void 0;
            this.container = Ext.create("Ext.container.Container", Ext.apply({
                layout: "absolute",
                border: !1,
                width: b ? b.getWidth() : void 0,
                height: b ? b.getHeight() : void 0,
                items: a
            }, this.gui.map))
        } else this.container = Ext.create("Ext.container.Viewport", {
            layout: "absolute",
            items: a
        })
    },
    destroy: function() {
        this.mapPanel && this.mapPanel.destroy();
        this.zoomTools && this.zoomTools.destroy();
        this.mapLayers && this.mapLayers.destroy();
        this.searchFastighet && this.searchFastighet.destroy();
        this.searchCoordinate && this.searchCoordinate.destroy();
        this.toolbar && this.toolbar.destroy();
        this.leftPanel && this.leftPanel.destroy();
        this.rightPanel && this.rightPanel.destroy();
        this.baseLayers && this.baseLayers.destroy();
        this.objectConfig && this.objectConfig.destroy();
        this.objectConfigWindow && this.objectConfigWindow.destroy();
        this.container && this.container.destroy()
    },
    onToggle: function(a, b) {
        var c = a.baseAction;
        this.objectConfig && (b && (this.mapPanel.unselectAll(), this.objectConfig.hide(), this.activeAction = c), c.toggle(b))
    },
    createToolbar: function() {
        var a = this.config.basePath,
            b = this.config.layers;
        this.config.tools || (this.config.tools = []);
        var c = this.config.tools.map(function(c) {
                var d;
                if (Ext.isObject(c) && 0 < Ext.Object.getSize(c) || !Ext.isObject(c) && !Ext.isEmpty(c)) {
                    c === this.config.tools[this.config.tools.length - 1] && (d = "oep-tools-last");
                    d = {
                        map: this.map,
                        mapPanel: this.mapPanel,
                        cls: d
                    };
                    c.constructor === Object && (Ext.apply(d, c), c = d.type, delete d.type);
                    if ("ZoomSelector" == c) return Ext.create("OpenEMap.form.ZoomSelector", {
                        map: this.map
                    });
                    "DrawObject" == c ? d.objectConfigView = this.objectConfig :
                        "Identify" == c && (d.basePath = a, d.layers = b);
                    c = Ext.create("OpenEMap.action." + c, d);
                    d.activate && c.control && (this.controlToActivate = c.control);
                    c = Ext.create("Ext.button.Button", c);
                    c.on("toggle", this.onToggle, this);
                    return c
                }
            }, this),
            d = 6;
        c.forEach(function(a) {
            a && (d = a.constructor == String ? d + 1 : a.width ? d + a.width : d + 24, d += 8)
        });
        d += 3;
        this.gui.toolbar && !this.gui.toolbar.renderTo ? this.leftPanel = Ext.create("Ext.toolbar.Toolbar", Ext.apply({
                cls: "oep-tools",
                y: 20,
                x: 80,
                width: d,
                items: c
            }, this.gui.toolbar)) : this.gui.toolbar &&
            this.gui.toolbar.renderTo && (this.toolbar = Ext.create("Ext.toolbar.Toolbar", Ext.apply({
                cls: "oep-tools",
                width: this.gui.toolbar.width || d,
                items: c
            }, this.gui.toolbar)))
    },
    createPanels: function() {
        this.mapLayers = this.gui.layers && "advanced" === this.gui.layers.type ? Ext.create("OpenEMap.view.layer.Advanced", Ext.apply({
            mapPanel: this.mapPanel,
            orginalConfig: this.orginalConfig
        }, this.gui.layers)) : Ext.create("OpenEMap.view.layer.Basic", Ext.apply({
            mapPanel: this.mapPanel
        }, this.gui.layers));
        this.searchFastighet = Ext.create("OpenEMap.view.SearchFastighet",
            Ext.apply({
                mapPanel: this.mapPanel,
                basePath: this.config.basePath,
                search: this.search
            }, this.gui.searchFastighet));
        if (this.gui.layers && !this.gui.layers.renderTo) {
            var a = [this.mapLayers];
            this.gui.searchFastighet && !this.gui.searchFastighet.renderTo && a.push(this.searchFastighet);
            this.rightPanel = Ext.create("Ext.panel.Panel", {
                y: 20,
                layout: {
                    type: "vbox",
                    align: "stretch"
                },
                width: 300,
                border: !1,
                style: {
                    right: "20px"
                },
                bodyStyle: {
                    background: "transparent"
                },
                items: a
            })
        }!this.map.allOverlays && this.gui.baseLayers && (this.baseLayers =
            Ext.create("OpenEMap.view.BaseLayers", Ext.apply({
                mapPanel: this.mapPanel,
                y: 20,
                style: {
                    right: "45%"
                },
                width: 115
            }, this.gui.baseLayers)));
        this.gui.zoomTools && !this.gui.zoomTools.renderTo && (this.zoomTools = Ext.create("OpenEMap.view.ZoomTools", Ext.apply({
            mapPanel: this.mapPanel,
            x: 20,
            y: 20,
            width: 36
        }, this.gui.zoomTools)));
        this.gui.searchCoordinate && this.gui.searchCoordinate.renderTo && (this.searchCoordinate = Ext.create("OpenEMap.view.SearchCoordinate", Ext.apply({
            mapPanel: this.mapPanel
        }, this.gui.searchCoordinate)));
        this.gui.objectConfig && this.gui.objectConfig.renderTo ? this.objectConfig = Ext.create("OpenEMap.view.ObjectConfig", Ext.apply({
            mapPanel: this.mapPanel,
            gui: this
        }, this.gui.objectConfig)) : this.gui.objectConfig && (this.objectConfig = Ext.create("OpenEMap.view.ObjectConfig", Ext.apply({
            mapPanel: this.mapPanel,
            gui: this
        }, this.gui.objectConfig)), this.objectConfigWindow = Ext.create("Ext.window.Window", Ext.apply({
                title: this.objectConfigWindowTitle,
                width: 480,
                height: 300,
                layout: "fit",
                closable: !1,
                collapsible: !0,
                items: this.objectConfig
            },
            this.gui.objectConfig)), this.objectConfigWindow.show())
    }
});
Ext.define("OpenEMap.model.Server", {
    extend: Ext.data.Model,
    fields: ["id", "type", "url", "proxy"]
});
Ext.define("OpenEMap.data.Servers", {
    extend: Ext.data.Store,
    model: "OpenEMap.model.Server",
    storeId: "servers",
    singelton: !0,
    constructor: function(a) {
        a = Ext.apply(this, a);
        this.url && (this.proxy = {
            type: "ajax",
            url: this.url,
            reader: {
                type: "json",
                root: "configs"
            }
        });
        this.callParent([a])
    }
});
Ext.define("OpenEMap.config.Parser", {
    constructor: function(a) {
        Ext.apply(this, a);
        this.callParent(arguments)
    },
    parse: function(a) {
        var b = {
            fallThrough: !0,
            controls: ["Navigation", "KeyboardDefaults"],
            projection: "EPSG:3006",
            resolutions: [280, 140, 70, 28, 14, 7, 4.2, 2.8, 1.4, 0.56, 0.28, 0.14, 0.112],
            extent: [608114, 6910996, 641846, 6932596],
            maxExtent: [487E3, 6887E3, 749144, 7149144],
            units: "m",
            municipalities: ["Sundvsall", "Timr\u00e5", "Kramfors", "\u00d6rnsk\u00f6ldsvik", "H\u00e4rn\u00f6sand"],
            theme: null
        };
        b.resolutions = a.resolutions || b.resolutions;
        b.units = a.units || b.units;
        b.projection = a.projection || b.projection;
        b.maxExtent = a.maxExtent;
        b.extent = a.extent;
        b.municipalities = a.municipalities || b.municipalities;
        b.controls = b.controls.map(this.createControl);
        Ext.apply(b, a.map);
        c = a.layers.map(this.transformLayer);
        a = this.parseLayerTree(c);
        var c = this.extractLayers(a);
        b.allOverlays = !c.some(this.isBaseLayer, this);
        b.layers = c.map(function(a) {
            return a.layer
        });
        b.layers = b.layers.filter(function(a) {
            return a
        });
        b = new OpenLayers.Map(b);
        b.layerTree = a;
        b.layerSwitcherLayerTree =
            this.getLayerSwitcherLayers(a);
        return b
    },
    parseLayerTree: function(a) {
        a.forEach(this.iterateLayers, this);
        return a
    },
    getLayerSwitcherLayers: function(a) {
        return a.filter(function(a) {
            return a.layers || this.isWMSLayer(a) && !this.isBaseLayer(a) ? !0 : !1
        }, this)
    },
    extractLayers: function(a) {
        var b = a.filter(function(a) {
            return !a.layers
        });
        a = a.filter(function(a) {
            return a.layers
        }).map(function(a) {
            return a.layers
        });
        a = [].concat.apply([], a);
        a = b.concat(a);
        a.reverse();
        return a
    },
    extractWFS: function(a) {
        a = this.extractLayers(a);
        return a = a.filter(function(a) {
            return a.wfs
        })
    },
    getOptions: function(a) {
        if (a.wms) return a.wms.options;
        if (a.osm) return a.osm.options;
        if (a.google) return a.google.options;
        if (a.bing) return a.bing.options
    },
    isOpenLayersLayer: function(a) {
        return a.wms || a.osm || a.google || a.bing ? !0 : !1
    },
    isBaseLayer: function(a) {
        return (a = this.getOptions(a)) && a.isBaseLayer ? !0 : !1
    },
    createControl: function(a) {
        return a.constructor == String ? new OpenLayers.Control[a] : new OpenLayers.Control[a.type](a.options)
    },
    isWMSLayer: function(a) {
        return a.wms ?
            !0 : !1
    },
    transformLayer: function(a) {
        a.url && (a.wms = {
            url: a.url,
            params: a.params,
            options: a.options
        });
        return a
    },
    createLayer: function(a) {
        if (a.wms) return new OpenLayers.Layer.WMS(a.name, a.wms.url, a.wms.params, a.wms.options);
        if (a.osm) return new OpenLayers.Layer.OSM(a.name, a.osm.url, a.osm.options);
        if (a.google) return new OpenLayers.Layer.Google(a.name, a.google.options);
        if (a.bing) return new OpenLayers.Layer.Bing(Ext.apply({
            name: a.name
        }, a.bing.options));
        throw Error("Unknown layer type");
    },
    iterateLayers: function(a) {
        a.text =
            a.name;
        a.checked = a.wms && a.wms.options ? a.wms.options.visibility : !1;
        if ("undefined" !== typeof a.serverId && "" !== a.serverId) {
            var b = this.serverStore.getById(a.serverId);
            if (b) {
                if (a.wms && !a.wms.url) {
                    var c = "/wms";
                    a.wms.gwc && (c = "/gwc/service/wms");
                    a.wms.url = b.get("url") + c
                }
                a.wfs && !a.wfs.url && (a.wfs.url = b.get("url"))
            }
        }
        this.isOpenLayersLayer(a) && (a.layer = this.createLayer(a));
        a.layers ? (a.expanded = void 0 == a.expanded ? !0 : a.expanded, a.layers.forEach(arguments.callee, this)) : a.leaf = !0
    }
});
Ext.define("OpenEMap.form.ZoomSelector", {
    extend: Ext.form.ComboBox,
    emptyText: "Zoom Level",
    listConfig: {
        getInnerTpl: function() {
            return "1: {scale:round(0)}"
        }
    },
    width: 120,
    editable: !1,
    triggerAction: "all",
    queryMode: "local",
    initComponent: function() {
        this.store = Ext.create("GeoExt.data.ScaleStore", {
            map: this.map
        });
        this.listeners = {
            select: {
                fn: function(a, b) {
                    this.map.zoomTo(b[0].get("level"))
                },
                scope: this
            }
        };
        this.map.events.register("zoomend", this, function() {
            var a = this.store.queryBy(function(a) {
                return this.map.getZoom() ==
                    a.data.level
            });
            0 < a.length ? (a = a.items[0], this.setValue("1 : " + parseInt(a.data.scale))) : zoomSelector.rendered && this.clearValue()
        });
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.OpenLayers.Control.ModifyFeature", {});
OpenLayers.Control.ModifyFeature = OpenLayers.Class(OpenLayers.Control, {
    bySegment: !1,
    documentDrag: !1,
    geometryTypes: null,
    clickout: !0,
    toggle: !0,
    standalone: !1,
    layer: null,
    feature: null,
    vertex: null,
    vertices: null,
    virtualVertices: null,
    handlers: null,
    deleteCodes: null,
    virtualStyle: null,
    dragHandleStyle: null,
    radiusHandleStyle: null,
    vertexRenderIntent: null,
    mode: null,
    createVertices: !0,
    modified: !1,
    radiusHandle: null,
    dragHandle: null,
    onModificationStart: function() {},
    onModification: function() {},
    onModificationEnd: function() {},
    initialize: function(a, b) {
        b = b || {};
        this.layer = a;
        this.vertices = [];
        this.virtualVertices = [];
        this.virtualStyle = OpenLayers.Util.extend({}, this.layer.style || this.layer.styleMap.createSymbolizer(null, b.vertexRenderIntent));
        this.virtualStyle.fillOpacity = 0.3;
        this.virtualStyle.strokeOpacity = 0.3;
        this.deleteCodes = [46, 68];
        this.mode = OpenLayers.Control.ModifyFeature.RESHAPE;
        OpenLayers.Control.prototype.initialize.apply(this, [b]);
        OpenLayers.Util.isArray(this.deleteCodes) || (this.deleteCodes = [this.deleteCodes]);
        var c =
            this,
            d = {
                documentDrag: this.documentDrag,
                setEvent: function(a) {
                    var b = c.feature;
                    c._lastVertex = b ? b.layer.getFeatureFromEvent(a) : null;
                    OpenLayers.Handler.Drag.prototype.setEvent.apply(this, arguments)
                },
                stopDown: !1
            };
        this.handlers = {
            keyboard: new OpenLayers.Handler.Keyboard(this, {
                keydown: this.handleKeypress
            }),
            drag: new OpenLayers.Handler.Drag(this, {
                down: function() {
                    this.vertex = null;
                    var a = this.layer.getFeatureFromEvent(this.handlers.drag.evt);
                    a ? this.dragStart(a) : this.clickout && (this._unselect = this.feature)
                },
                move: function(a) {
                    delete this._unselect;
                    this.vertex && this.dragVertex(this.vertex, a)
                },
                up: function() {
                    this.handlers.drag.stopDown = !1;
                    this._unselect && (this.unselectFeature(this._unselect), delete this._unselect)
                },
                done: function() {
                    this.vertex && this.dragComplete(this.vertex)
                }
            }, d)
        };
        if (this.bySegment) {
            if (!window.rbush) throw Error("The rbush library is required");
            if (OpenLayers.Control.ModifyFeature.BySegment) OpenLayers.Util.extend(this, OpenLayers.Control.ModifyFeature.BySegment);
            else throw Error("OpenLayers.Control.ModifyFeature.BySegment is missing from the build");
        }
    },
    createVirtualVertex: function(a, b) {
        var c = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point((a.x + b.x) / 2, (a.y + b.y) / 2), null, this.virtualStyle);
        c._sketch = !0;
        return c
    },
    destroy: function() {
        this.map && this.map.events.un({
            removelayer: this.handleMapEvents,
            changelayer: this.handleMapEvents,
            scope: this
        });
        this.layer = null;
        OpenLayers.Control.prototype.destroy.apply(this, [])
    },
    activate: function() {
        return OpenLayers.Control.prototype.activate.apply(this, arguments) ? (this.moveLayerToTop(), this.map.events.on({
            removelayer: this.handleMapEvents,
            changelayer: this.handleMapEvents,
            scope: this
        }), this._lastVertex = null, this.handlers.keyboard.activate() && this.handlers.drag.activate()) : !1
    },
    deactivate: function() {
        var a = !1;
        OpenLayers.Control.prototype.deactivate.apply(this, arguments) && (this.moveLayerBack(), this.map.events.un({
                removelayer: this.handleMapEvents,
                changelayer: this.handleMapEvents,
                scope: this
            }), this.layer.removeFeatures(this.vertices, {
                silent: !0
            }), this.layer.removeFeatures(this.virtualVertices, {
                silent: !0
            }), this.vertices = [], this.handlers.drag.deactivate(),
            this.handlers.keyboard.deactivate(), (a = this.feature) && (a.geometry && a.layer) && this.unselectFeature(a), a = !0);
        return a
    },
    beforeSelectFeature: function(a) {
        return this.layer.events.triggerEvent("beforefeaturemodified", {
            feature: a
        })
    },
    selectFeature: function(a) {
        if (!(this.feature === a || this.geometryTypes && -1 == OpenLayers.Util.indexOf(this.geometryTypes, a.geometry.CLASS_NAME))) {
            !1 !== this.beforeSelectFeature(a) && (this.feature && this.unselectFeature(this.feature), this.feature = a, this.layer.selectedFeatures.push(a),
                this.layer.drawFeature(a, "select"), this.modified = !1, this.resetVertices(), this.onModificationStart(this.feature));
            var b = a.modified;
            if (a.geometry && (!b || !b.geometry)) this._originalGeometry = a.geometry.clone()
        }
    },
    unselectFeature: function(a) {
        this.layer.removeFeatures(this.vertices, {
            silent: !0
        });
        this.vertices = [];
        this.layer.destroyFeatures(this.virtualVertices, {
            silent: !0
        });
        this.virtualVertices = [];
        this.dragHandle && (this.layer.destroyFeatures([this.dragHandle], {
            silent: !0
        }), delete this.dragHandle);
        this.radiusHandle &&
            (this.layer.destroyFeatures([this.radiusHandle], {
                silent: !0
            }), delete this.radiusHandle);
        this.layer.drawFeature(this.feature, "default");
        this.feature = null;
        OpenLayers.Util.removeItem(this.layer.selectedFeatures, a);
        this.onModificationEnd(a);
        this.layer.events.triggerEvent("afterfeaturemodified", {
            feature: a,
            modified: this.modified
        });
        this.modified = !1
    },
    dragStart: function(a) {
        var b = "OpenLayers.Geometry.Point" == a.geometry.CLASS_NAME;
        if (!this.standalone && (!a._sketch && b || !a._sketch)) this.toggle && this.feature === a &&
            (this._unselect = a), this.selectFeature(a);
        if (this.feature && (a._sketch || b && a === this.feature)) this.vertex = a, this.handlers.drag.stopDown = !0
    },
    dragVertex: function(a, b) {
        var c = this.map.getLonLatFromViewPortPx(b),
            d = a.geometry;
        d.move(c.lon - d.x, c.lat - d.y);
        this.modified = !0;
        "OpenLayers.Geometry.Point" == this.feature.geometry.CLASS_NAME ? this.layer.events.triggerEvent("vertexmodified", {
            vertex: a.geometry,
            feature: this.feature,
            pixel: b
        }) : (a._index ? (-1 == a._index && (a._index = OpenLayers.Util.indexOf(a.geometry.parent.components,
            a._next)), a.geometry.parent.addComponent(a.geometry, a._index), delete a._index, OpenLayers.Util.removeItem(this.virtualVertices, a), this.vertices.push(a)) : a == this.dragHandle ? (this.layer.removeFeatures(this.vertices, {
            silent: !0
        }), this.vertices = [], this.radiusHandle && (this.layer.destroyFeatures([this.radiusHandle], {
            silent: !0
        }), this.radiusHandle = null)) : a !== this.radiusHandle && this.layer.events.triggerEvent("vertexmodified", {
            vertex: a.geometry,
            feature: this.feature,
            pixel: b
        }), 0 < this.virtualVertices.length && (this.layer.destroyFeatures(this.virtualVertices, {
            silent: !0
        }), this.virtualVertices = []), this.layer.drawFeature(this.feature, this.standalone ? void 0 : "select"));
        this.layer.drawFeature(a)
    },
    dragComplete: function() {
        this.resetVertices();
        this.setFeatureState();
        this.onModification(this.feature);
        this.layer.events.triggerEvent("featuremodified", {
            feature: this.feature
        })
    },
    setFeatureState: function() {
        if (this.feature.state != OpenLayers.State.INSERT && this.feature.state != OpenLayers.State.DELETE && (this.feature.state = OpenLayers.State.UPDATE, this.modified && this._originalGeometry)) {
            var a =
                this.feature;
            a.modified = OpenLayers.Util.extend(a.modified, {
                geometry: this._originalGeometry
            });
            delete this._originalGeometry
        }
    },
    resetVertices: function() {
        0 < this.vertices.length && (this.layer.removeFeatures(this.vertices, {
            silent: !0
        }), this.vertices = []);
        0 < this.virtualVertices.length && (this.layer.removeFeatures(this.virtualVertices, {
            silent: !0
        }), this.virtualVertices = []);
        this.dragHandle && (this.layer.destroyFeatures([this.dragHandle], {
            silent: !0
        }), this.dragHandle = null);
        this.radiusHandle && (this.layer.destroyFeatures([this.radiusHandle], {
            silent: !0
        }), this.radiusHandle = null);
        this.feature && "OpenLayers.Geometry.Point" != this.feature.geometry.CLASS_NAME && (this.mode & OpenLayers.Control.ModifyFeature.DRAG && this.collectDragHandle(), this.mode & (OpenLayers.Control.ModifyFeature.ROTATE | OpenLayers.Control.ModifyFeature.RESIZE) && this.collectRadiusHandle(), this.mode & OpenLayers.Control.ModifyFeature.RESHAPE && (this.mode & OpenLayers.Control.ModifyFeature.RESIZE || this.collectVertices()))
    },
    handleKeypress: function(a) {
        var b = a.keyCode;
        if (this.feature && -1 !=
            OpenLayers.Util.indexOf(this.deleteCodes, b) && (b = this._lastVertex) && -1 != OpenLayers.Util.indexOf(this.vertices, b) && !this.handlers.drag.dragging && b.geometry.parent) b.geometry.parent.removeComponent(b.geometry), this.layer.events.triggerEvent("vertexremoved", {
            vertex: b.geometry,
            feature: this.feature,
            pixel: a.xy
        }), this.layer.drawFeature(this.feature, this.standalone ? void 0 : "select"), this.modified = !0, this.resetVertices(), this.setFeatureState(), this.onModification(this.feature), this.layer.events.triggerEvent("featuremodified", {
            feature: this.feature
        })
    },
    collectVertices: function() {
        function a(c) {
            var d, e, g;
            if ("OpenLayers.Geometry.Point" == c.CLASS_NAME) e = new OpenLayers.Feature.Vector(c), e._sketch = !0, e.renderIntent = b.vertexRenderIntent, b.vertices.push(e);
            else {
                g = c.components.length;
                "OpenLayers.Geometry.LinearRing" == c.CLASS_NAME && (g -= 1);
                for (d = 0; d < g; ++d) e = c.components[d], "OpenLayers.Geometry.Point" == e.CLASS_NAME ? (e = new OpenLayers.Feature.Vector(e), e._sketch = !0, e.renderIntent = b.vertexRenderIntent, b.vertices.push(e)) : a(e);
                if (b.createVertices &&
                    "OpenLayers.Geometry.MultiPoint" != c.CLASS_NAME) {
                    d = 0;
                    for (g = c.components.length; d < g - 1; ++d) {
                        e = c.components[d];
                        var f = c.components[d + 1];
                        "OpenLayers.Geometry.Point" == e.CLASS_NAME && "OpenLayers.Geometry.Point" == f.CLASS_NAME && (e = b.createVirtualVertex.call(b, e, f), e.geometry.parent = c, e._index = d + 1, b.virtualVertices.push(e))
                    }
                }
            }
        }
        this.vertices = [];
        this.virtualVertices = [];
        var b = this;
        a.call(this, this.feature.geometry);
        this.layer.addFeatures(this.virtualVertices, {
            silent: !0
        });
        this.layer.addFeatures(this.vertices, {
            silent: !0
        })
    },
    collectDragHandle: function() {
        var a = this.feature.geometry,
            b = a.getBounds().getCenterLonLat(),
            b = new OpenLayers.Geometry.Point(b.lon, b.lat),
            c = new OpenLayers.Feature.Vector(b, null, this.dragHandleStyle);
        b.move = function(b, c) {
            OpenLayers.Geometry.Point.prototype.move.call(this, b, c);
            a.move(b, c)
        };
        c._sketch = !0;
        this.dragHandle = c;
        this.dragHandle.renderIntent = this.vertexRenderIntent;
        this.layer.addFeatures([this.dragHandle], {
            silent: !0
        })
    },
    collectRadiusHandle: function() {
        var a = this.feature.geometry,
            b = a.getBounds(),
            c = b.getCenterLonLat(),
            d = new OpenLayers.Geometry.Point(c.lon, c.lat),
            b = new OpenLayers.Geometry.Point(b.right, b.bottom),
            c = new OpenLayers.Feature.Vector(b, null, this.radiusHandleStyle),
            e = this.mode & OpenLayers.Control.ModifyFeature.RESIZE,
            g = this.mode & OpenLayers.Control.ModifyFeature.RESHAPE,
            f = this.mode & OpenLayers.Control.ModifyFeature.ROTATE;
        b.move = function(b, c) {
            OpenLayers.Geometry.Point.prototype.move.call(this, b, c);
            var k = this.x - d.x,
                l = this.y - d.y,
                m = k - b,
                n = l - c;
            if (f) {
                var p = Math.atan2(n, m),
                    p = Math.atan2(l, k) -
                    p,
                    p = p * (180 / Math.PI);
                a.rotate(p, d)
            }
            if (e) {
                var q;
                g ? (l /= n, q = k / m / l) : (m = Math.sqrt(m * m + n * n), l = Math.sqrt(k * k + l * l) / m);
                a.resize(l, d, q)
            }
        };
        c._sketch = !0;
        this.radiusHandle = c;
        this.radiusHandle.renderIntent = this.vertexRenderIntent;
        this.layer.addFeatures([this.radiusHandle], {
            silent: !0
        })
    },
    setMap: function(a) {
        this.handlers.drag.setMap(a);
        OpenLayers.Control.prototype.setMap.apply(this, arguments)
    },
    handleMapEvents: function(a) {
        ("removelayer" == a.type || "order" == a.property) && this.moveLayerToTop()
    },
    moveLayerToTop: function() {
        var a =
            Math.max(this.map.Z_INDEX_BASE.Feature - 1, this.layer.getZIndex()) + 1;
        this.layer.setZIndex(a)
    },
    moveLayerBack: function() {
        var a = this.layer.getZIndex() - 1;
        a >= this.map.Z_INDEX_BASE.Feature ? this.layer.setZIndex(a) : this.map.setLayerZIndex(this.layer, this.map.getLayerIndex(this.layer))
    },
    CLASS_NAME: "OpenLayers.Control.ModifyFeature"
});
OpenLayers.Control.ModifyFeature.RESHAPE = 1;
OpenLayers.Control.ModifyFeature.RESIZE = 2;
OpenLayers.Control.ModifyFeature.ROTATE = 4;
OpenLayers.Control.ModifyFeature.DRAG = 8;
Ext.define("OpenEMap.OpenLayers.Control.DynamicMeasure", {});
OpenLayers.Control.DynamicMeasure = OpenLayers.Class(OpenLayers.Control.Measure, {
    accuracy: 5,
    persist: !1,
    styles: null,
    positions: null,
    maxSegments: 1,
    maxHeadings: 1,
    layerSegmentsOptions: void 0,
    layerHeadingOptions: null,
    layerLengthOptions: void 0,
    layerAreaOptions: void 0,
    drawingLayer: null,
    multi: !1,
    layerSegments: null,
    layerLength: null,
    layerArea: null,
    dynamicObj: null,
    isArea: null,
    initialize: function(a, b) {
        b = b || {};
        b.handlerOptions = OpenLayers.Util.extend({
            persist: !b.drawingLayer
        }, b.handlerOptions);
        b.drawingLayer && !("multi" in
            b.handlerOptions) && (b.handlerOptions.multi = b.multi);
        if (b.drawingLayer) {
            var c = b.drawingLayer.styleMap && b.drawingLayer.styleMap.styles.temporary;
            c && (b.handlerOptions.layerOptions = OpenLayers.Util.applyDefaults(b.handlerOptions.layerOptions, {
                styleMap: new OpenLayers.StyleMap({
                    "default": c
                })
            }))
        }
        c = b.styles || {};
        b.styles = c;
        var d = OpenLayers.Control.DynamicMeasure.styles;
        if (!b.handlerOptions.layerOptions || !b.handlerOptions.layerOptions.styleMap) c = new OpenLayers.Style(null, {
            rules: [new OpenLayers.Rule({
                symbolizer: {
                    Point: OpenLayers.Util.applyDefaults(c.Point,
                        d.Point),
                    Line: OpenLayers.Util.applyDefaults(c.Line, d.Line),
                    Polygon: OpenLayers.Util.applyDefaults(c.Polygon, d.Polygon)
                }
            })]
        }), b.handlerOptions = b.handlerOptions || {}, b.handlerOptions.layerOptions = b.handlerOptions.layerOptions || {}, b.handlerOptions.layerOptions.styleMap = new OpenLayers.StyleMap({
            "default": c
        });
        b.positions = OpenLayers.Util.applyDefaults(b.positions, OpenLayers.Control.DynamicMeasure.positions);
        b.callbacks = b.callbacks || {};
        b.drawingLayer && OpenLayers.Util.applyDefaults(b.callbacks, {
            create: function(a,
                b) {
                this.callbackCreate(a, b);
                this.drawingLayer.events.triggerEvent("sketchstarted", {
                    vertex: a,
                    feature: b
                })
            },
            modify: function(a, b) {
                this.callbackModify(a, b);
                this.drawingLayer.events.triggerEvent("sketchmodified", {
                    vertex: a,
                    feature: b
                })
            },
            done: function(a) {
                this.callbackDone(a);
                this.drawFeature(a)
            }
        });
        OpenLayers.Util.applyDefaults(b.callbacks, {
            create: this.callbackCreate,
            point: this.callbackPoint,
            cancel: this.callbackCancel,
            done: this.callbackDone,
            modify: this.callbackModify,
            redo: this.callbackRedo,
            undo: this.callbackUndo
        });
        var e = document.onselectstart ? document.onselectstart : OpenLayers.Function.True,
            c = OpenLayers.Class(a, {
                down: function(b) {
                    document.onselectstart = OpenLayers.Function.False;
                    return a.prototype.down.apply(this, arguments)
                },
                up: function(b) {
                    document.onselectstart = e;
                    return a.prototype.up.apply(this, arguments)
                },
                move: function(b) {
                    this.mouseDown || (document.onselectstart = e);
                    return a.prototype.move.apply(this, arguments)
                },
                mouseout: function(b) {
                    OpenLayers.Util.mouseLeft(b, this.map.viewPortDiv) && this.mouseDown && (document.onselectstart =
                        e);
                    return a.prototype.mouseout.apply(this, arguments)
                },
                finalize: function() {
                    document.onselectstart = e;
                    a.prototype.finalize.apply(this, arguments)
                }
            }, {
                undo: function() {
                    var b = a.prototype.undo.call(this);
                    b && this.callback("undo", [this.point.geometry, this.getSketch(), !0]);
                    return b
                },
                redo: function() {
                    var b = a.prototype.redo.call(this);
                    b && this.callback("redo", [this.point.geometry, this.getSketch(), !0]);
                    return b
                }
            });
        OpenLayers.Control.Measure.prototype.initialize.call(this, c, b);
        this.isArea = void 0 !== a.prototype.polygon
    },
    destroy: function() {
        this.deactivate();
        OpenLayers.Control.Measure.prototype.destroy.apply(this, arguments)
    },
    draw: function() {},
    activate: function() {
        var a = OpenLayers.Control.Measure.prototype.activate.apply(this, arguments);
        if (a) {
            this.dynamicObj = {};
            var b = this.styles || {},
                c = OpenLayers.Control.DynamicMeasure.styles,
                d = this,
                e = function(a, e) {
                    if (null === e) return null;
                    var h = OpenLayers.Util.extend({
                        displayInLayerSwitcher: !1,
                        calculateInRange: OpenLayers.Function.True
                    }, e);
                    h.styleMap || (h.styleMap = new OpenLayers.StyleMap({
                        "default": OpenLayers.Util.applyDefaults(b[a],
                            c[a])
                    }));
                    h = new OpenLayers.Layer.Vector(d.CLASS_NAME + " " + a, h);
                    d.map.addLayer(h);
                    d.map.setLayerIndex(h, 99);
                    return h
                };
            this.layerSegments = e("labelSegments", this.layerSegmentsOptions);
            this.layerHeading = e("labelHeading", this.layerHeadingOptions);
            this.layerLength = e("labelLength", this.layerLengthOptions);
            this.isArea && (this.layerArea = e("labelArea", this.layerAreaOptions))
        }
        return a
    },
    deactivate: function() {
        var a = OpenLayers.Control.Measure.prototype.deactivate.apply(this, arguments);
        a && (this.layerSegments && this.layerSegments.destroy(),
            this.layerLength && this.layerLength.destroy(), this.layerHeading && this.layerHeading.destroy(), this.layerArea && this.layerArea.destroy(), this.layerArea = this.layerHeading = this.layerLength = this.layerSegments = this.dynamicObj = null);
        return a
    },
    setImmediate: function(a) {
        this.immediate = a
    },
    callbackCreate: function() {
        var a = this.dynamicObj;
        a.drawing = !1;
        a.freehand = !1;
        a.fromIndex = 0;
        a.countSegments = 0
    },
    callbackCancel: function() {
        this.destroyLabels()
    },
    callbackDone: function(a) {
        var b = new OpenLayers.Feature.Vector(a);
        this.mapPanel.measureLayer.addFeatures([b.clone()]);
        b = function(a) {
            return a.clone()
        };
        this.layerArea && this.mapPanel.measureLayerArea.addFeatures(this.layerArea.features.map(b));
        this.mapPanel.measureLayerLength.addFeatures(this.layerLength.features.map(b));
        this.mapPanel.measureLayerSegments.addFeatures(this.layerSegments.features.map(b));
        this.measureComplete(a);
        this.persist || this.destroyLabels()
    },
    drawFeature: function(a) {
        a = new OpenLayers.Feature.Vector(a);
        !1 !== this.drawingLayer.events.triggerEvent("sketchcomplete", {
            feature: a
        }) && (a.state = OpenLayers.State.INSERT,
            this.featureAdded && this.featureAdded(a), this.events.triggerEvent("featureadded", {
                feature: a
            }))
    },
    destroyLabels: function() {
        this.layerSegments && this.layerSegments.destroyFeatures(null, {
            silent: !0
        });
        this.layerLength && this.layerLength.destroyFeatures(null, {
            silent: !0
        });
        this.layerHeading && this.layerHeading.destroyFeatures(null, {
            silent: !0
        });
        this.layerArea && this.layerArea.destroyFeatures(null, {
            silent: !0
        })
    },
    callbackPoint: function(a, b) {
        var c = this.dynamicObj;
        c.drawing || this.destroyLabels();
        this.handler.freehandMode(this.handler.evt) ?
            c.freehand || (c.fromIndex = this.handler.getCurrentPointIndex() - 1, c.freehand = !0, c.countSegments++) : (c.fromIndex = this.handler.getCurrentPointIndex() - 1, c.freehand = !1, c.countSegments++);
        this.measurePartial(a, b);
        c.drawing = !0
    },
    callbackUndo: function(a, b) {
        var c = this,
            d = function(a) {
                if (a) {
                    var b = a.features,
                        d = b.length - 1,
                        h = b[d],
                        j = h.attributes.from,
                        k = c.handler.getCurrentPointIndex();
                    j >= k && (j = c.dynamicObj, a.destroyFeatures(h), h = b[d - 1], j.fromIndex = h.attributes.from, j.countSegments = b.length)
                }
            };
        d(this.layerSegments);
        d(this.layerHeading);
        this.callbackModify(a, b, !0)
    },
    callbackRedo: function(a, b) {
        var c = this.handler.line.geometry,
            d = this.handler.getCurrentPointIndex(),
            e = this.dynamicObj;
        this.showLabelSegment(e.countSegments, e.fromIndex, c.components.slice(e.fromIndex, d));
        e.fromIndex = this.handler.getCurrentPointIndex() - 1;
        e.countSegments++;
        this.callbackModify(a, b, !0)
    },
    callbackModify: function(a, b, c) {
        this.immediate && this.measureImmediate(a, b, c);
        var d = this.dynamicObj;
        if (!1 !== d.drawing) {
            var e = this.handler.line.geometry;
            c = this.handler.getCurrentPointIndex();
            !this.handler.freehandMode(this.handler.evt) && d.freehand && (d.fromIndex = c - 1, d.freehand = !1, d.countSegments++);
            var g = this.getBestLength(e);
            if (g[0]) {
                var f = this.positions,
                    h = {
                        center: function() {
                            var c = b.geometry.getBounds().clone();
                            c.extend(a);
                            c = c.getCenterLonLat();
                            return [c.lon, c.lat]
                        },
                        initial: function() {
                            var a = e.components[0];
                            return [a.x, a.y]
                        },
                        start: function() {
                            var a = e.components[d.fromIndex];
                            return [a.x, a.y]
                        },
                        middle: function() {
                            var b = e.components[d.fromIndex];
                            return [(b.x + a.x) / 2, (b.y + a.y) / 2]
                        },
                        end: function() {
                            return [a.x,
                                a.y
                            ]
                        }
                    };
                this.layerLength && this.showLabel(this.layerLength, 1, 0, g, h[f.labelLength](), 1);
                this.isArea && (this.layerArea && this.showLabel(this.layerArea, 1, 0, this.getBestArea(b.geometry), h[f.labelArea](), 1), this.showLabelSegment(1, 0, [e.components[0], e.components[c]]) && d.countSegments++);
                this.showLabelSegment(d.countSegments, d.fromIndex, e.components.slice(d.fromIndex, c + 1))
            }
        }
    },
    showLabelSegment: function(a, b, c) {
        var d = this.layerSegments,
            e = this.layerHeading;
        if (!d && !e) return !1;
        for (var g = [], f = c.length, h = 0; h < f; h++) g.push(c[h].clone());
        var j = g[0],
            k = g[f - 1],
            h = this.getBestLength(new OpenLayers.Geometry.LineString(g));
        c = this.positions;
        g = {
            start: function() {
                return [j.x, j.y]
            },
            middle: function() {
                return [(j.x + k.x) / 2, (j.y + k.y) / 2]
            },
            end: function() {
                return [k.x, k.y]
            }
        };
        f = !1;
        d && (f = this.showLabel(d, a, b, h, g[c.labelSegments](), this.maxSegments));
        e && 0 < h[0] && (d = 90 - 180 * Math.atan2(k.y - j.y, k.x - j.x) / Math.PI, 0 > d && (d += 360), f = f || this.showLabel(e, a, b, [d, "\u00b0"], g[c.labelHeading](), this.maxHeadings));
        return f
    },
    showLabel: function(a, b, c, d, e, g) {
        var f;
        f = a.features;
        if (f.length < b) {
            if (0 === d[0]) return !1;
            b = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(e[0], e[1]), {
                from: c
            });
            this.setMesureAttributes(b.attributes, d);
            a.addFeatures([b]);
            null !== g && (d = f.length - g - 1, 0 <= d && (f = f[d], f.style = {
                display: "none"
            }, a.drawFeature(f)));
            return !0
        }
        b = f[b - 1];
        c = b.geometry;
        c.x = e[0];
        c.y = e[1];
        c.clearBounds();
        this.setMesureAttributes(b.attributes, d);
        a.drawFeature(b);
        null !== g && (d = f.length - g, 0 <= d && (f = f[d], f.style && (delete f.style, a.drawFeature(f))));
        return !1
    },
    setMesureAttributes: function(a,
        b) {
        a.measure = OpenLayers.Number.format(b[0].toFixed(2), null);
        a.units = b[1]
    },
    CLASS_NAME: "OpenLayers.Control.DynamicMeasure"
});
OpenLayers.Control.DynamicMeasure.styles = {
    Point: {
        pointRadius: 4,
        graphicName: "square",
        fillColor: "white",
        fillOpacity: 1,
        strokeWidth: 1,
        strokeOpacity: 1,
        strokeColor: "#333333"
    },
    Line: {
        strokeWidth: 2,
        strokeOpacity: 1,
        strokeColor: "#666666",
        strokeDashstyle: "dash"
    },
    Polygon: {
        strokeWidth: 2,
        strokeOpacity: 1,
        strokeColor: "#666666",
        strokeDashstyle: "solid",
        fillColor: "white",
        fillOpacity: 0.3
    },
    labelSegments: {
        label: "${measure} ${units}",
        fontSize: "12px",
        fontColor: "#800517",
        fontFamily: "Verdana",
        labelOutlineColor: "#eeeeee",
        labelAlign: "cm",
        labelOutlineWidth: 2
    },
    labelLength: {
        label: "${measure} ${units}\n",
        fontSize: "12px",
        fontWeight: "bold",
        fontColor: "#800517",
        fontFamily: "Verdana",
        labelOutlineColor: "#eeeeee",
        labelAlign: "lb",
        labelOutlineWidth: 3
    },
    labelArea: {
        label: "${measure}\n${units}\u00b2\n",
        fontSize: "11px",
        fontWeight: "bold",
        fontColor: "#800517",
        fontFamily: "Verdana",
        labelOutlineColor: "#dddddd",
        labelAlign: "cm",
        labelOutlineWidth: 3
    },
    labelHeading: {
        label: "${measure} ${units}",
        fontSize: "11px",
        fontColor: "#800517",
        fontFamily: "Verdana",
        labelOutlineColor: "#dddddd",
        labelAlign: "cm",
        labelOutlineWidth: 3
    }
};
OpenLayers.Control.DynamicMeasure.positions = {
    labelSegments: "middle",
    labelLength: "end",
    labelArea: "center",
    labelHeading: "start"
};
Ext.ns("OpenEMap");
Ext.apply(OpenEMap, {
    lmUser: "sundsvall",
    basePathMapFish: "/print/pdf",
    basePathLM: "/search/lm/",
    basePathImages: "resources/images/",
    wsUrls: {
        basePath: "/openemapadmin/",
        configs: "configurations/configs",
        servers: "settings/servers",
        layers: "layers/layers",
        metadata: "geometadata/getmetadatabyid",
        metadata_abstract: "geometadata/getabstractbyid"
    }
});
Ext.apply(OpenEMap, {
    requestLM: function(a) {
        a.url = OpenEMap.basePathLM + a.url + "\x26lmuser\x3d" + OpenEMap.lmUser;
        Ext.Ajax.request(a)
    }
});
Ext.define("OpenEMap.Client", {
    version: "1.0.4",
    map: null,
    drawLayer: null,
    destroy: function() {
        this.map && (this.map.controls.forEach(function(a) {
            a.destroy()
        }), this.map.controls = null);
        this.gui && this.gui.destroy()
    },
    configure: function(a, b) {
        b = Ext.apply({}, b);
        this.initialConfig = Ext.clone(a);
        Ext.tip.QuickTipManager.init();
        this.map = Ext.create("OpenEMap.config.Parser").parse(a);
        this.gui = Ext.create("OpenEMap.Gui", {
            config: a,
            gui: b.gui,
            map: this.map,
            orginalConfig: this.initialConfig
        });
        this.mapPanel = this.gui.mapPanel;
        this.drawLayer = this.gui.mapPanel.drawLayer;
        this.gui.controlToActivate && this.gui.controlToActivate.activate()
    },
    encode: function(a) {
        return JSON.stringify(this.mapPanel.encode(a))
    },
    addGeoJSON: function(a) {
        a = (new OpenLayers.Format.GeoJSON).read(a, "Feature");
        a.attributes.config && (a = Ext.create("OpenEMap.ObjectFactory").create(a.attributes.config, a.attributes));
        this.drawLayer.addFeatures([a])
    },
    setSketchStyleMap: function(a) {
        this.map.controls.forEach(function(b) {
            b instanceof OpenLayers.Control.DrawFeature &&
                (b.handler.layerOptions.styleMap = a, b.handler.layer && (b.handler.layer.styleMap = a))
        })
    },
    toggleEdgeLabels: function(a) {
        var b = a || {};
        a = function() {
            this.labelLayer.destroyFeatures();
            var a = this.drawLayer.features.map(function(a) {
                a = a.geometry;
                if ("OpenLayers.Geometry.Polygon" != a.CLASS_NAME) return [];
                var c = a.components[0];
                return c.components.slice(0, c.components.length - 1).map(function(a, d) {
                    var h = c.components[d].clone(),
                        j = c.components[d + 1].clone(),
                        j = new OpenLayers.Geometry.LineString([h, j]),
                        h = j.getCentroid({
                            weighted: !0
                        }),
                        j = Ext.applyIf(Ext.clone(b), {
                            label: j.getLength().toFixed(2).toString() + " m",
                            strokeColor: "#000000",
                            strokeWidth: 3,
                            labelAlign: "cm"
                        });
                    return new OpenLayers.Feature.Vector(h, null, j)
                })
            });
            0 < a.length && (a = a.reduce(function(a, b) {
                return a.concat(b)
            }), this.labelLayer.addFeatures(a))
        };
        null == this.labelLayer && (this.labelLayer = new OpenLayers.Layer.Vector, this.map.addLayer(this.labelLayer), this.drawLayer.events.on({
            featuremodified: a,
            vertexmodified: a,
            featuresadded: a,
            featuresremoved: a,
            scope: this
        }));
        a.apply(this)
    }
});
Ext.ns("OpenEMap");
Ext.apply(OpenEMap, {
    lmUser: "sundsvall",
    basePathMapFish: "/print/pdf",
    basePathLM: "/search/lm/",
    basePathImages: "resources/images/",
    wsUrls: {
        basePath: "/openemapadmin/",
        configs: "configs",
        servers: "settings/servers",
        layers: "layers/layers",
        metadata: "geometadata/getmetadatabyid",
        metadata_abstract: "geometadata/getabstractbyid"
    }
});
Ext.apply(OpenEMap, {
    requestLM: function(a) {
        a.url = OpenEMap.basePathLM + a.url + "\x26lmuser\x3d" + OpenEMap.lmUser;
        Ext.Ajax.request(a)
    }
});
OpenLayers.Layer.Vector.prototype.renderers = ["Canvas", "SVG", "VML"];
Ext.define("OpenEMap.locale.sv_SE.Gui", {
    override: "OpenEMap.Gui",
    objectConfigWindowTitle: "Objektkonfiguration"
});
Ext.define("OpenEMap.locale.sv_SE.view.ObjectConfig", {
    override: "OpenEMap.view.ObjectConfig",
    typeLabel: "Typ",
    widthLabel: "Bredd",
    lengthLabel: "L\u00e4ngd",
    m1Label: "M1",
    m2Label: "M2",
    angleLabel: "Vinkel"
});
Ext.define("OpenEMap.Search", {
    constructor: function() {
        initConfig()
    },
    doSearch: function() {}
});
Ext.define("OpenEMap.data.DataHandler", {
    metadataAbstractWsUrl: null,
    metadataWsUrl: null,
    layersWsUrl: null,
    metadataAbstractCache: {},
    constructor: function(a) {
        this.wsUrls = OpenEMap.wsUrls;
        Ext.apply(this, a)
    },
    getLayer: function(a, b) {
        this.wsUrls.layers && a && this.doRequest({
            url: this.wsUrls.basePath + this.wsUrls.layers + "/" + a
        }, function(a) {
            b(a)
        })
    },
    getLayers: function(a) {
        this.wsUrls.layers && this.doRequest({
            url: this.wsUrls.basePath + this.wsUrls.layers
        }, a)
    },
    getMetadata: function(a, b) {
        a && this.wsUrls.metadata && this.doRequest({
            url: this.wsUrls.basePath +
                this.wsUrls.metadata + "/" + a
        }, b)
    },
    getMetadataAbstract: function(a, b) {
        if (a && this.wsUrls.metadata_abstract) {
            var c = this;
            c.metadataAbstractCache[a] ? b(c.metadataAbstractCache[a]) : this.doRequest({
                url: this.wsUrls.basePath + this.wsUrls.metadata_abstract + "/" + a
            }, function(d) {
                b(d);
                c.metadataAbstractCache[a] = d
            })
        }
    },
    updateConfiguration: function(a, b, c) {
        this.doRequest({
            url: this.wsUrls.basePath + this.wsUrls.configs + "/" + a,
            method: "PUT",
            jsonData: b
        }, c)
    },
    saveNewConfiguration: function(a, b) {
        this.doRequest({
            url: this.wsUrls.basePath +
                this.wsUrls.configs,
            method: "POST",
            jsonData: a
        }, b)
    },
    deleteConfiguration: function(a, b, c) {
        this.doRequest({
            url: this.wsUrls.basePath + this.wsUrls.configs + "/" + a,
            method: "DELETE",
            jsonData: b
        }, c)
    },
    doRequest: function(a, b) {
        var c = this;
        if (a && a.method && ("POST" === a.method && "PUT" === a.method) && !b) return c.onFailure("no callback function"), !1;
        Ext.Ajax.request(Ext.apply({
            success: function(a) {
                if (a && a.responseText) a = Ext.decode(a.responseText), b && b(a);
                else c.onFailure()
            },
            failure: function(b) {
                c.onFailure(b.status + " " + b.statusText +
                    ", " + a.url)
            }
        }, a ? a : {}))
    },
    onFailure: function(a) {
        console.error(a)
    }
});
Ext.define("OpenEMap.model.GroupedLayerTreeModel", {
    extend: Ext.data.Model,
    fields: [{
        name: "text",
        type: "string"
    }, {
        name: "checkedGroup",
        type: "string"
    }, {
        name: "layer"
    }, {
        name: "layerId"
    }, {
        name: "name",
        type: "string"
    }, {
        name: "isSearchable"
    }, {
        name: "urlToMetadata"
    }, {
        name: "wms"
    }, {
        name: "wfs"
    }, {
        name: "serverId"
    }, {
        name: "legendURL"
    }]
});
Ext.define("OpenEMap.data.GroupedLayerTree", {
    extend: Ext.data.TreeStore,
    model: "OpenEMap.model.GroupedLayerTreeModel",
    defaultRootProperty: "layers",
    proxy: {
        type: "memory"
    },
    maxLayerIndex: 1E3,
    listeners: {
        beforeinsert: function(a, b, c) {
            return this.onBeforeInsert(a, b, c)
        },
        beforeappend: function(a, b) {
            return this.onBeforeAppend(a, b)
        },
        insert: function(a, b) {
            this.onInsertAndAppend(b)
        },
        append: function(a, b) {
            this.onInsertAndAppend(b)
        },
        remove: function(a, b, c) {
            this.onRemove(a, b, c)
        }
    },
    constructor: function(a) {
        a = Ext.apply({},
            a);
        this.callParent([a])
    },
    getLayerConfiguration: function() {
        var a = [];
        this.getRootNode().childNodes.forEach(function(b, c) {
            a[c] = {
                name: b.get("name"),
                layers: []
            };
            b.childNodes.forEach(function(b) {
                a[c].layers.push({
                    name: b.get("name"),
                    wms: "string" === typeof b.get("wms") ? {} : b.get("wms"),
                    wfs: "string" === typeof b.get("wfs") ? {} : b.get("wfs"),
                    metadata: "string" === typeof b.get("metadata") ? {} : b.get("metadata")
                })
            })
        });
        return a
    },
    onBeforeAppend: function(a, b) {
        return a && !a.isRoot() && !b.isLeaf() ? !1 : !0
    },
    onBeforeInsert: function(a,
        b, c) {
        return !c.parentNode.isRoot() && !b.isLeaf() ? !1 : !0
    },
    onInsertAndAppend: function(a) {
        this._inserting || (this._inserting = !0, a.cascadeBy(function(a) {
            var c = a.get("layer");
            a.getLayer = function() {
                return this.get("layer")
            };
            this.addWMSLegend(a);
            c && "" !== c && this.map && null === this.map.getLayer(c) && (c && !0 === c.displayInLayerSwitcher) && this.map.addLayer(c)
        }, this), this.reorderLayersOnMap(), delete this._inserting)
    },
    onRemove: function(a, b, c) {
        !this._removing && !c && (this._removing = !0, b.cascadeBy(function(a) {
            (a = a.get("layer")) &&
            a.map && this.map.removeLayer(a)
        }, this), delete this._removing)
    },
    reorderLayersOnMap: function() {
        var a = this.getRootNode();
        if (a) {
            var b = this.maxLayerIndex;
            a.cascadeBy(function(a) {
                if (a = a.get("layer")) a.setZIndex(b), b--
            }, this)
        }
    },
    addWMSLegend: function(a) {
        var b = a.get("layer");
        if (b) {
            if (Ext.isIE9) return a;
            b.legendURL ? (a.set("legendURL", b.legendURL), a.gx_urllegend = Ext.create("GeoExt.container.UrlLegend", {
                layerRecord: a,
                showTitle: !1,
                hidden: !0,
                deferRender: !0,
                cls: "legend"
            })) : "OpenLayers.Layer.WMS" == b.CLASS_NAME && (a.gx_wmslegend =
                Ext.create("GeoExt.container.WmsLegend", {
                    layerRecord: a,
                    showTitle: !1,
                    hidden: !0,
                    deferRender: !0,
                    cls: "legend"
                }))
        }
        return a
    },
    unbind: function() {
        this.un("beforeinsert", this.onBeforeInsert, this);
        this.un("beforeappend", this.onBeforeAppend, this);
        this.un("insert", this.onInsertAndAppend, this);
        this.un("append", this.onInsertAndAppend, this);
        this.un("remove", this.onRemove, this);
        this.map = null
    },
    destroy: function() {}
});
Ext.define("OpenEMap.model.MapConfig", {
    extend: Ext.data.Model,
    fields: ["configId", "name"]
});

Ext.define("OpenEMap.data.SavedMapConfigs", {
    extend: Ext.data.Store,
    model: "OpenEMap.model.MapConfig",
    storeId: "savedMapConfigs",
    autoLoad: !0,
    proxy: {
        type: "rest",
        appendId: !0,
        url: OpenEMap && OpenEMap.wsUrls && OpenEMap.wsUrls.basePath ? OpenEMap.wsUrls.basePath : "" + (OpenEMap && OpenEMap.wsUrls && OpenEMap.wsUrls.configs) ? OpenEMap.wsUrls.configs : "",
        reader: {
            type: "json",
            root: "configs"
        },
        writer: {
            type: "json"
        }
    }
});

Ext.define("OpenEMap.view.MetadataWindow", {
    extend: Ext.Window,
    title: "Metadata",
    width: 600,
    height: 500,
    border: 0,
    layout: "fit",
    closeAction: "hide",
    TRANSLATION: {
        sv: {
            tag: {
                "gmd:citation": "",
                "gmd:CI_Address": "",
                "gmd:CI_Citation": "",
                "gmd:CI_Contact": "",
                "gmd:CI_Date": "",
                "gmd:CI_Telephone": "",
                "gmd:CI_ResponsibleParty": "",
                "gmd:identificationInfo": "",
                "gmd:EX_BoundingPolygon": "",
                "gmd:EX_Extent": "",
                "gmd:EX_GeographicBoundingBox": "",
                "gmd:EX_GeographicDescription": "",
                "gmd:EX_TemporalExtent": "",
                "gmd:EX_VerticalExtent": "",
                "gmd:MD_BrowseGraphic": "",
                "gmd:MD_Constraints": "",
                "gmd:MD_Identifier": "",
                "gmd:MD_Keywords": "",
                "gmd:MD_LegalConstraints": "",
                "gmd:MD_Metadata": "",
                "gmd:MD_MaintenanceInformation": "",
                "gmd:MD_SecurityConstraints": "",
                "gmd:thesaurusName": "",
                "gmd:voice": "",
                "srv:SV_ServiceIdentification": "",
                "gmd:accessConstraints": "Nyttjanderestriktioner",
                "gmd:abstract": "Sammanfattning",
                "gmd:address": "Adress",
                "gmd:alternateTitle": "Alternativ titel",
                "gmd:city": "Stad",
                "gmd:classification": "Klassificering",
                "gmd:contact": "Metadatakontakt",
                "gmd:contactInfo": "Kontaktinformation",
                "gmd:date": "Datum",
                "gmd:dateStamp": "Datum",
                "gmd:dateType": "Datumtyp",
                "gmd:descriptiveKeywords": "Nyckelordslista",
                "gmd:electronicMailAddress": "E-post",
                "gmd:fileIdentifier": "Identifierare f\u00f6r metadatam\u00e4ngden",
                "gmd:graphicOverview": "Exempelbild",
                "gmd:hierarchyLevel": "Hierarkisk niv\u00e5 (Resurstyp)",
                "gmd:individualName": "Person",
                "gmd:identifier": "Identifierare",
                "gmd:keyword": "Nyckelord",
                "gmd:language": "Spr\u00e5k",
                "gmd:metadataStandardName": "Metadatastandardversion",
                "gmd:metadataStandardVersion": "Metadatastandard",
                "gmd:organisationName": "Organisation",
                "gmd:otherConstraints": "Andra restriktioner",
                "gmd:phone": "Telefonnummer",
                "gmd:pointOfContact": "Kontakt",
                "gmd:resourceConstraints": "Restriktioner och begr\u00e4nsningar",
                "gmd:role": "Ansvarsomr\u00e5de",
                "gmd:status": "Status",
                "gmd:title": "Titel",
                "gmd:type": "Typ",
                "gmd:useLimitation": "Anv\u00e4ndbarhetsbegr\u00e4nsningar"
            },
            codeListValue: {
                swe: "Svenska",
                service: "Tj\u00e4nst",
                pointOfContact: "Kontakt"
            }
        }
    },
    initComponent: function() {
        this.overviewTab =
            new Ext.Panel({
                title: "\u00d6versikt"
            });
        this.metadataTab = new Ext.Panel({
            title: "Information om metadata"
        });
        this.dataTab = new Ext.Panel({
            title: "Information om data"
        });
        this.qualityTab = new Ext.Panel({
            title: "Kvalitet"
        });
        this.distributionTab = new Ext.Panel({
            title: "Distribution"
        });
        this.restTab = new Ext.Panel({
            title: "Rest"
        });
        this.items = Ext.create("Ext.tab.Panel", {
            activeTab: 0,
            defaults: {
                autoScroll: !0
            },
            items: [this.overviewTab, this.metadataTab, this.dataTab, this.qualityTab, this.distributionTab, this.restTab]
        });
        this.callParent(arguments)
    },
    showMetadata: function(a) {
        var b = this;
        this.dataHandler.getMetadata(a, function(a) {
            a.children && (a = b.parseMetadata(a.children), b.overviewTab.html = a.overview, b.metadataTab.html = a.metadata_info, b.dataTab.html = a.data_info, b.qualityTab.html = a.quality, b.distributionTab.html = a.distribution, b.restTab.html = a.rest, b.show())
        })
    },
    translate: function(a, b) {
        var c = null;
        try {
            c = this.TRANSLATION.sv[a][b], "string" !== typeof c && (c = b)
        } catch (d) {
            translateTag = null
        }
        return c
    },
    parseMetadataTextTag: function(a) {
        var b = null;
        a.tag && (b = this.translate("tag",
            a.tag), b = null !== b ? "" !== b ? "\x3cb\x3e" + b + "\x3c/b\x3e" : "" : null);
        a.text && (b = a.text);
        a.attributes && a.attributes.codeListValue && (b = this.translate("codeListValue", a.attributes.codeListValue));
        return b
    },
    getGroups: function(a, b) {
        var c = [];
        for (key in b)
            for (var d = 0; d < b[key].length; d++) - 1 !== a.indexOf(b[key][d]) && c.push(key);
        0 === c.length && c.push("rest");
        return c
    },
    metadataIterator: function(a, b, c, d) {
        var e = this.parseMetadataTextTag(a);
        d = ("undefined" !== typeof d ? d + "\x3e" : "") + a.tag;
        for (var g = this.getGroups(d, c), f = 0; f <
            g.length; f++) {
            var h = g[f];
            "string" !== typeof b[h] && (b[h] = "");
            if (null !== e) {
                b[h] += "\x3cli\x3e";
                b[h] += e;
                if (a.children && 0 === f) {
                    b[h] += "" !== e ? "\x3cul\x3e" : "";
                    for (var j = 0; j < a.children.length; j++) this.metadataIterator(a.children[j], b, c, d);
                    b[h] += "" !== e ? "\x3c/ul\x3e" : ""
                }
                b[h] += "\x3c/li\x3e"
            }
        }
    },
    parseMetadata: function(a) {
        var b = {};
        this.metadataIterator(a[0], b, {
            overview: ["gmd:MD_Metadata\x3egmd:identificationInfo\x3esrv:SV_ServiceIdentification\x3egmd:citation\x3egmd:CI_Citation\x3egmd:title", "gmd:MD_Metadata\x3egmd:identificationInfo\x3esrv:SV_ServiceIdentification\x3egmd:abstract",
                "gmd:MD_Metadata\x3egmd:identificationInfo\x3esrv:SV_ServiceIdentification\x3egmd:descriptiveKeywords", "gmd:MD_Metadata\x3egmd:identificationInfo\x3esrv:SV_ServiceIdentification\x3egmd:graphicOverview"
            ],
            metadata_info: "gmd:MD_Metadata\x3egmd:fileIdentifier gmd:MD_Metadata\x3egmd:language gmd:MD_Metadata\x3egmd:dateStamp gmd:MD_Metadata\x3egmd:hierarchyLevel gmd:MD_Metadata\x3egmd:metadataStandardName gmd:MD_Metadata\x3egmd:metadataStandardVersion gmd:MD_Metadata\x3egmd:contact".split(" "),
            data_info: ["gmd:MD_Metadata\x3egmd:identificationInfo"],
            quality: ["gmd:MD_Metadata\x3egmd:dataQualityInfo"],
            distribution: ["gmd:MD_Metadata\x3egmd:distributionInfo"]
        });
        return b
    }
});

/*Ext.define("OpenEMap.view.SavedMapConfigs", {
    extend: Ext.grid.Panel,
    autoScroll: !0,
    hideHeaders: !0,
    id: "savedMapConfigsGrid",
    selModel: Ext.create("Ext.selection.CheckboxModel", {
        mode: "SINGLE",
        checkOnly: !0,
        listeners: {
            select: function(a, b) {
                var c = b.get("configId");
                init(OpenEMap.wsUrls.basePath + OpenEMap.wsUrls.configs + "/" + c)
            }
        }
    }),
    store: Ext.create("OpenEMap.data.SavedMapConfigs"),
    columns: [{
        header: "Name",
        dataIndex: "name",
        flex: 1
    }, {
        xtype: "actioncolumn",
        width: 40,
        iconCls: "action-remove",
        tooltip: "Ta bort",
        handler: function(a,
            b, c, d, e, g) {
            Ext.MessageBox.confirm("Ta bort", "Vill du verkligen ta bort konfigurationen?", function(c) {
                "yes" === c && (c = a.getStore(), a.panel.dataHandler.deleteConfiguration(g.get("configId"), {
                    configId: g.get("configId")
                }), c.removeAt(b))
            });
            e.stopEvent();
            return !1
        }
    }],
    constructor: function() {
        this.callParent(arguments)
    }
});*/

Ext.define("OpenEMap.view.layer.Tree", {
    extend: Ext.tree.Panel,
    rootVisible: !1,
    hideHeaders: !0,
    initComponent: function() {
        !this.store && this.mapPanel && (this.store = Ext.create("OpenEMap.data.GroupedLayerTree", {
            root: {
                text: this.mapPanel.config && this.mapPanel.config.name ? this.mapPanel.config.name : "Lager",
                expanded: !0,
                layers: this.mapPanel.map.layerSwitcherLayerTree
            },
            map: this.mapPanel.map
        }));
        this.on("checkchange", function(a, b) {
            var c = a.parentNode;
            b ? (a.cascadeBy(function(a) {
                a.set("checked", b);
                (a = a.get("layer")) &&
                a.setVisibility(!0)
            }), c.isRoot() || c.set("checked", b)) : (a.cascadeBy(function(a) {
                a.set("checked", !1);
                (a = a.get("layer")) && a.setVisibility(!1)
            }), !c.isRoot() && !c.childNodes.some(function(a) {
                return a.get("checked")
            }) && c.set("checked", b))
        });
        this.on("cellclick", function(a, b, c, d) {
            if ((d.gx_wmslegend || d.gx_urllegend) && d.store) a = d.gx_wmslegend || d.gx_urllegend, a.isHidden() ? (a.rendered || a.render(b), a.show()) : a.hide()
        });
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.view.layer.TreeFilter", {
    extend: Ext.AbstractPlugin,
    alias: "plugin.treefilter",
    collapseOnClear: !0,
    allowParentFolders: !1,
    init: function(a) {
        this.tree = a;
        a.filter = Ext.Function.bind(this.filter, this);
        a.clearFilter = Ext.Function.bind(this.clearFilter, this)
    },
    filter: function(a, b, c) {
        var d = this,
            e = d.tree,
            g = [],
            f = e.getRootNode();
        b = b || "text";
        c = c || RegExp(a, "ig");
        var h = [],
            j;
        Ext.isEmpty(a) ? d.clearFilter() : (e.expandAll(), f.cascadeBy(function(a) {
                a.get(b).match(c) && g.push(a)
            }), !1 === d.allowParentFolders &&
            Ext.each(g, function(a) {
                a.isLeaf() || Ext.Array.remove(g, a)
            }), Ext.each(g, function(a) {
                f.cascadeBy(function(b) {
                    !0 == b.contains(a) && h.push(b)
                });
                !0 === d.allowParentFolders && !a.isLeaf() && a.cascadeBy(function(a) {
                    h.push(a)
                });
                h.push(a)
            }), f.cascadeBy(function(a) {
                if (j = Ext.fly(e.getView().getNode(a))) j.setVisibilityMode(Ext.Element.DISPLAY), j.setVisible(Ext.Array.contains(h, a))
            }))
    },
    clearFilter: function() {
        var a = this.tree,
            b = a.getRootNode();
        this.collapseOnClear && a.collapseAll();
        b.cascadeBy(function(b) {
            (viewNode = Ext.fly(a.getView().getNode(b))) &&
            viewNode.show()
        })
    }
});
Ext.define("OpenEMap.view.layer.Add", {
    extend: OpenEMap.view.layer.Tree,
    title: "L\u00e4gg till lager",
    width: 250,
    height: 550,
    headerPosition: "top",
    collapsible: !0,
    collapseMode: "header",
    collapseDirection: "right",
    titleCollapse: !0,
    viewConfig: {
        plugins: {
            ptype: "treeviewdragdrop",
            enableDrop: !1
        },
        copy: !0
    },
    plugins: {
        ptype: "treefilter",
        allowParentFolders: !0
    },
    dockedItems: [{
        xtype: "toolbar",
        dock: "top",
        layout: "fit",
        items: [{
            xtype: "trigger",
            triggerCls: "x-form-clear-trigger",
            onTriggerClick: function() {
                this.reset();
                this.focus()
            },
            listeners: {
                change: function(a, b) {
                    a.up("treepanel").filter(b)
                },
                buffer: 250
            }
        }]
    }],
    initComponent: function() {
        var a = this;
        this.on("checkchange", function(b, c) {
            b.cascadeBy(function(b) {
                c ? a.loadLayer(b) : a.unLoadLayer(b)
            })
        });
        this.columns = [{
                xtype: "treecolumn",
                flex: 1,
                dataIndex: "text"
            },
            a.metadataColumn
        ];
        this.store = Ext.create("OpenEMap.data.GroupedLayerTree");
        this.serverStore = Ext.create("OpenEMap.data.Servers", {
            proxy: {
                url: OpenEMap.wsUrls.basePath + OpenEMap.wsUrls.servers,
                type: "ajax",
                reader: {
                    type: "json",
                    root: "configs"
                }
            }
        });
        this.serverStore.load({
            callback: function() {
                a.dataHandler.getLayers(function(b) {
                    b && (b = (new OpenEMap.config.Parser({
                        serverStore: a.serverStore
                    })).parseLayerTree(b), a.store.setRootNode({
                        text: "Lager",
                        expanded: !0,
                        layers: b
                    }))
                })
            }
        });
        this.callParent(arguments)
    },
    loadLayer: function(a) {
        if ((a = a.get("layer")) && "" !== a && this.mapPanel) a.setVisibility(!0), a.displayInLayerSwitcher = !1, this.mapPanel.layers.add(a)
    },
    unLoadLayer: function(a) {
        (a = a.get("layer")) && ("" !== a && this.mapPanel) && this.mapPanel.layers.remove(a)
    }
});
Ext.define("OpenEMap.view.layer.Advanced", {
    extend: Ext.container.Container,
    layout: {
        type: "hbox",
        pack: "end",
        align: "stretch"
    },
    width: 500,
    height: 650,
    initComponent: function() {
        var a = this;
        this.dataHandler = Ext.create("OpenEMap.data.DataHandler");
        this.metadataWindow = Ext.create("OpenEMap.view.MetadataWindow", {
            dataHandler: this.dataHandler
        });
        this.savedMapConfigs = Ext.create("OpenEMap.view.SavedMapConfigs", {
            dataHandler: this.dataHandler
        });
        this.showOnMapLayerView = Ext.create("OpenEMap.view.layer.Tree", {
            title: "Visas p\u00e5 kartan",
            width: 250,
            height: 500,
            region: "north",
            mapPanel: this.mapPanel,
            rootVisible: !0,
            viewConfig: {
                plugins: {
                    ptype: "treeviewdragdrop",
                    allowContainerDrops: !0,
                    allowParentInserts: !0
                }
            },
            columns: [{
                    xtype: "gx_treecolumn",
                    flex: 1,
                    dataIndex: "text"
                },
                Ext.create("OpenEMap.action.MetadataInfoColumn", {
                    metadataWindow: this.metadataWindow,
                    dataHandler: this.dataHandler
                }), {
                    xtype: "actioncolumn",
                    width: 40,
                    iconCls: "action-remove",
                    tooltip: "Ta bort",
                    handler: function(a, c) {
                        for (var d = a.getStore().getAt(c), e = 0; e < d.childNodes.length; e++) d.removeChild(d.childNodes[e]);
                        d.remove()
                    },
                    dataHandler: this.dataHandler
                }
            ],
            buttons: [{
                text: "Spara kartinneh\u00e5ll",
                handler: function() {
                    if (a.orginalConfig) {
                        var b = Ext.clone(a.orginalConfig);
                        Ext.MessageBox.prompt("Namn", "Ange ett namn:", function(c, d) {
                            if ("ok" == c && 0 < d.length) {
                                var e = a.showOnMapLayerView.getStore().getLayerConfiguration();
                                if (b.layers) {
                                    var g = b.layers.filter(function(a) {
                                        return a.wms && a.wms.options.isBaseLayer || a.wfs ? a : !1
                                    });
                                    b.layers = g.concat(e)
                                }
                                d !== b.name ? (b.name = d, a.dataHandler.saveNewConfiguration(b, function() {
                                        a.savedMapConfigs.getStore().load()
                                    })) :
                                    b.configId && a.dataHandler.updateConfiguration(b.configId, b)
                            }
                        }, this, !1, b.name)
                    }
                }
            }]
        });
        this.items = [Ext.create("OpenEMap.view.layer.Add", {
            mapPanel: this.mapPanel,
            dataHandler: this.dataHandler,
            metadataColumn: Ext.create("OpenEMap.action.MetadataInfoColumn", {
                metadataWindow: this.metadataWindow,
                dataHandler: this.dataHandler
            })
        }), {
            xtype: "panel",
            layout: "border",
            width: "50%",
            border: !1,
            items: [a.showOnMapLayerView, {
                title: "Sparade kartor",
                region: "center",
                xtype: "panel",
                border: !1,
                layout: "fit",
                collapsible: !0,
                titleCollapse: !0,
                items: a.savedMapConfigs
            }]
        }];
        this.callParent(arguments)
    }
});
Ext.define("OpenEMap.view.layer.Basic", {
    extend: OpenEMap.view.layer.Tree,
    overflowY: "auto",
    rootVisible: !1,
    height: 300,
    border: !1,
    initComponent: function() {
        this.renderTo || (this.title = "Lager", this.bodyPadding = 5, this.collapsible = !0);
        this.callParent(arguments)
    }
});