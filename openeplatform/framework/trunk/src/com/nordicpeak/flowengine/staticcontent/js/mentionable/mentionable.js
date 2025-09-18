(function($) {
    $.fn.extend({
        mentionable: function(options) {
            
        	var $this = $(this);
        	
        	this.opts = {
                users: [],
                delimiter: '@',
                sensitive: false,
                emptyQuery: false,
                queryBy: ['Name', 'Username'],
                typeaheadOpts: {}
            };

            var settings = $.extend({}, this.opts, options),
                _checkDependencies = function() {
                    if (typeof $ == 'undefined') {
                        throw new Error("jQuery is Required");
                    }
                    else {
                        if (typeof $.fn.typeahead == 'undefined') {
                            throw new Error("Typeahead is Required");
                        }
                    }
                    return true;
                },
                _extractCurrentQuery = function(query, caratPos) {
                    var i;
                    for (i = caratPos; i >= 0; i--) {
                        if (query[i] == settings.delimiter) {
                            break;
                        }
                    }
                    return query.substring(i, caratPos);
                },
                _matcher = function(itemProps) {
                    var i;
                    
                    if(settings.emptyQuery){
	                    var q = (this.query.toLowerCase()),
	                    	caratPos = this.$element[0].selectionStart,
	                    	lastChar = q.slice(caratPos-1,caratPos);
	                    if(lastChar==settings.delimiter){
		                    return true;
	                    }
                    }
                    
                    for (i in settings.queryBy) {
                        if (itemProps[settings.queryBy[i]]) {
                            var item = itemProps[settings.queryBy[i]].toLowerCase(),
                                usernames = (this.query.toLowerCase()).match(new RegExp(settings.delimiter + '\\w+', "g")),
                                j;
                            if ( !! usernames) {
                                for (j = 0; j < usernames.length; j++) {
                                    var username = (usernames[j].substring(1)).toLowerCase(),
                                        re = new RegExp(settings.delimiter + item, "g"),
                                        used = ((this.query.toLowerCase()).match(re));

                                    if (item.indexOf(username) != -1 && used === null) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                },
                _updater = function(item) {
                    var data = this.query,
                        caratPos = this.$element[0].selectionStart,
                        i;
                    
                    for (i = caratPos; i >= 0; i--) {
                        if (data[i] == settings.delimiter) {
                            break;
                        }
                    }
                    
                    var parts = item.split(":");
                    
                    var replace = data.substring(i, caratPos),
                    	textBefore = data.substring(0, i),
                    	textAfter = data.substring(caratPos),
                    	data = textBefore + settings.delimiter + parts[0] + textAfter + " ";
                    
                    this.tempQuery = data;

                    var $store = $this.data("store");
                    var mentions = $this.data("mentions");
                    
                    mentions[settings.delimiter + parts[0]] = "$mention{" + item + "}";
                    
                    $store.val(data);

                    _updateHiddenField(data);
                    
                    return data;
                },
                _sorter = function(items) {
                    if (items.length && settings.sensitive) {
                        var currentUser = _extractCurrentQuery(this.query, this.$element[0].selectionStart).substring(1),
                            i, len = items.length,
                            priorities = {
                                highest: [],
                                high: [],
                                med: [],
                                low: []
                            }, finals = [];
                        if (currentUser.length == 1) {
                            for (i = 0; i < len; i++) {
                                var currentRes = items[i];

                                if ((currentRes.Username[0] == currentUser)) {
                                    priorities.highest.push(currentRes);
                                }
                                else if ((currentRes.Username[0].toLowerCase() == currentUser.toLowerCase())) {
                                    priorities.high.push(currentRes);
                                }
                                else if (currentRes.Username.indexOf(currentUser) != -1) {
                                    priorities.med.push(currentRes);
                                }
                                else {
                                    priorities.low.push(currentRes);
                                }
                            }
                            for (i in priorities) {
                                var j;
                                for (j in priorities[i]) {
                                    finals.push(priorities[i][j]);
                                }
                            }
                            return finals;
                        }
                    }
                    return items;
                },
                _render = function(items) {
                    var that = this;
                    items = $(items).map(function(i, item) {

                        i = $(that.options.item).attr('data-value', item.Username + ":" + item.ID);

                        var _linkHtml = $('<div />');

                        if (item.Image) {
                            _linkHtml.append('<img class="mention_image" src="' + item.Image + '">');
                        }
                        if (item.Name) {
                            _linkHtml.append('<b class="mention_name">' + item.Name + '</b>');
                        }
                        if (item.Username) {
                            _linkHtml.append('<span class="mention_username"> ' + settings.delimiter + item.Username + '</span>');
                        }

                        i.find('a').html(that.highlighter(_linkHtml.html()));
                        return i[0];
                    });

                    items.first().addClass('active');
                    this.$menu.html(items);
                    return this;
                };
                _updateHiddenField = function(data) {
                	
                	var mentions = $this.data("mentions");
                	var rows = data.split("\n");
                	
                	var newValue = "";
                	
                	for(var i = 0; i < rows.length; i++) {
                		
                		var row = rows[i];
                		
                		var words = row.split(" ");
                		
                		for(var j = 0; j < words.length; j++) {
                			
                			var word = words[j];
                			
                			if(word.indexOf(settings.delimiter) === 0 && mentions[word] != undefined) {
                				word = mentions[word];
                    		}
                			
                			newValue += word + " ";
                			
                		}

                		if(i != (rows.length - 1)) {
            				newValue += "\n";
            			}

                	}
                	
                	$this.data("store").val(newValue.trim());
                	
                };

            $.fn.typeahead.Constructor.prototype.render = _render;

            var count = 0;
            
            return this.each(function() {
                var _this = $(this);
                if (_checkDependencies()) {
                    _this.typeahead($.extend({
                        source: settings.users,
                        matcher: _matcher,
                        updater: _updater,
                        sorter: _sorter
                    }, settings.typeaheadOpts));
                }

                var name = _this.attr("name");
                
                _this.attr("name", name + "_mention_" + count++);
                
                var $store = $("<input type='hidden' name='" + name + "' />");

                _this.after($store);
                _this.data("store", $store);
                _this.data("mentions", {});
                
                _this.keyup(function(event) {
                	_updateHiddenField(_this.val());                	
                });
                
            });

        }
    });
    
})(jQuery);


$(function() {
	
	var $elements = $('.show-mentions:contains("$mention{")');

	$elements.each(function() {
		
		var $this = $(this);
		var oldHtml = $this.html();
		var newHtml = oldHtml.replace(/\$mention{(.*?):(.*?)\}/g,"<span class='mention-highlight'>@$1</span>");
		$this.html(newHtml);
		
	});
	
});
