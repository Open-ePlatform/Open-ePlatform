$(function() {
	
	$("#notificationsMenu a.submenu-trigger").click(function(e) {
		
		e.stopPropagation();
		e.preventDefault();
		
		var menu = $(this).parent();
		
		var submenu = menu.find(".submenu");
		
		menu.toggleClass("active");
		
		if (menu.hasClass("active")) {
			
			$(".dd.active").removeClass("active").find(".submenu").hide();
			$(".primary").removeClass("active");
			
			submenu.empty();
			submenu.append($("#notifications-loading").clone().children());
			
			submenu.show();
			
			$.ajax({
				cache: false,
				url: notificationHandlerUrl + "/latest",
				dataType: "html",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(response, textStatus, jqXHR) {
					
					if (jqXHR.getResponseHeader('notifications') == "true") {
					
						submenu.html(response);
						
						submenu.find("article[data-url]").click(function(e) { // Only used if you click on the edges
							
							if (e.target == this) { // Ignore bubbled events
							
								e.stopPropagation();
								e.preventDefault();
								
								var article = $(this);
								
								window.location = article.data("url");
							}
						});
						
						var counter = menu.find(".count");
						var unreadCount = submenu.find(".unread-count").text();
						
						counter.text(unreadCount);
						counter.toggleClass("hidden", !(unreadCount > 0));
						
					} else {
						
						submenu.empty();
						submenu.append($("#notifications-login-error").clone().children());
					}
				},
				error: function (xhr, ajaxOptions, thrownError) {
					
					submenu.empty();
					submenu.append($("#notifications-loading-error").clone().children());
				}
			});
			
		} else {
			
			submenu.hide();
		}
	});
	
});
