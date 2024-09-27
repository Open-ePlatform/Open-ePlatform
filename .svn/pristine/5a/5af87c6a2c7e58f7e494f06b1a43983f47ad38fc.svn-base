/*
Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{

	config.disableNativeSpellChecker = false;
	
	config.CustomConfigurationsPath = '' ;

	config.EditorAreaCSS = config.BasePath + 'css/fck_editorarea.css' ;
	config.EditorAreaStyles = '' ;
	config.ToolbarComboPreviewCSS = '' ;

	config.DocType = '' ;

	config.BaseHref = '' ;

	config.FullPage = false ;

	// The following option determines whether the "Show Blocks" feature is enabled or not at startup.
	config.StartupShowBlocks = false ;

	config.Debug = false ;
	config.AllowQueryStringDebug = true ;

	config.SkinPath = config.BasePath + 'skins/default/' ;
	config.PreloadImages = [ config.SkinPath + 'images/toolbar.start.gif', config.SkinPath + 'images/toolbar.buttonarrow.gif' ] ;

	config.PluginsPath = config.BasePath + 'plugins/' ;

	config.extraPlugins = 'MediaEmbed';
	
	// config.Plugins.Add( 'autogrow' ) ;
	// config.Plugins.Add( 'dragresizetable' );
	config.AutoGrowMax = 400 ;

	// config.ProtectedSource.Add( /<%[\s\S]*?%>/g ) ;	// ASP style server side code <%...%>
	// config.ProtectedSource.Add( /<\?[\s\S]*?\?>/g ) ;	// PHP style server side code
	// config.ProtectedSource.Add( /(<asp:[^\>]+>[\s|\S]*?<\/asp:[^\>]+>)|(<asp:[^\>]+\/>)/gi ) ;	// ASP.Net style tags <asp:control>

	config.AutoDetectLanguage	= true ;
	config.DefaultLanguage		= 'en' ;
	config.ContentLangDirection	= 'ltr' ;

	config.ProcessHTMLEntities	= true ;
	config.IncludeLatinEntities	= true ;
	config.IncludeGreekEntities	= true ;

	config.ProcessNumericEntities = false ;

	config.AdditionalNumericEntities = ''  ;		// Single Quote: "'"

	config.FillEmptyBlocks	= true ;

	config.FormatSource		= true ;
	config.FormatOutput		= true ;
	config.FormatIndentator	= '    ' ;

	config.GeckoUseSPAN	= false ;
	config.StartupFocus	= false ;
	config.ForcePasteAsPlainText	= false ;
	config.AutoDetectPasteFromWord = true ;	// IE only.
	config.ShowDropDialog = true ;
	config.ForceSimpleAmpersand	= false ;
	config.TabSpaces		= 0 ;
	config.ShowBorders	= true ;
	config.SourcePopup	= false ;
	config.ToolbarStartExpanded	= true ;
	config.ToolbarCanCollapse	= true ;
	config.IgnoreEmptyParagraphValue = true ;
	config.PreserveSessionOnFileBrowser = false ;
	config.FloatingPanelsZIndex = 10000 ;
	config.HtmlEncodeOutput = false ;

	config.TemplateReplaceAll = true ;
	config.TemplateReplaceCheckbox = true ;

	config.ToolbarLocation = 'In' ;

	config.resize_dir = 'vertical';
	
	config.toolbar = 'Full';
	
	config.toolbar_Full = [
	    
	    ['Source'],['Undo','Redo'],
	    ['PasteText','PasteFromWord'],
	    ['Link','Unlink','Image','MediaEmbed'],
	    ['Bold','Italic','Underline','Strike'],
	    ['NumberedList','BulletedList','Blockquote'],
	    ['JustifyLeft','JustifyCenter','JustifyRight'],
	    ['Format'],['RemoveFormat']  // No comma for the last row.*/
	   
	];

	config.toolbar_Basic = [
	    ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About']
	];

	config.EnterMode = 'p' ;			// p | div | br
	config.ShiftEnterMode = 'br' ;	// p | div | br

	config.ContextMenu = ['Generic','Link','Anchor','Image','Flash','Select','Textarea','Checkbox','Radio','TextField','HiddenField','ImageButton','Button','BulletedList','NumberedList','Table','Form'] ;
	config.BrowserContextMenuOnCtrl = true ;

	config.EnableMoreFontColors = true ;
	config.FontColors = '000000,993300,333300,003300,003366,000080,333399,333333,800000,FF6600,808000,808080,008080,0000FF,666699,808080,FF0000,FF9900,99CC00,339966,33CCCC,3366FF,800080,999999,FF00FF,FFCC00,FFFF00,00FF00,00FFFF,00CCFF,993366,C0C0C0,FF99CC,FFCC99,FFFF99,CCFFCC,CCFFFF,99CCFF,CC99FF,FFFFFF' ;

	config.FontFormats	= 'p;div;pre;address;h1;h2;h3;h4;h5;h6' ;
	config.FontNames		= 'Arial;Comic Sans MS;Courier New;Tahoma;Times New Roman;Verdana' ;
	config.FontSizes		= 'smaller;larger;xx-small;x-small;small;medium;large;x-large;xx-large' ;

	config.StylesXmlPath		= config.EditorPath + 'fckstyles.xml' ;
	config.TemplatesXmlPath	= config.EditorPath + 'fcktemplates.xml' ;

	config.SpellChecker			= 'ieSpell' ;	// 'ieSpell' | 'SpellerPages'
	config.IeSpellDownloadUrl	= 'http://www.iespell.com/download.php' ;
	config.SpellerPagesServerScript = 'server-scripts/spellchecker.php' ;	// Available extension: .php .cfm .pl
	config.FirefoxSpellChecker	= true ;

	config.MaxUndoLevels = 15 ;

	config.DisableObjectResizing = false ;
	config.DisableFFTableHandles = true ;

	config.LinkDlgHideTarget		= false ;
	config.LinkDlgHideAdvanced	= false ;

	config.ImageDlgHideLink		= false ;
	config.ImageDlgHideAdvanced	= false ;

	config.FlashDlgHideAdvanced	= false ;

	config.ProtectedTags = '' ;

	// This will be applied to the body element of the editor
	config.BodyId = '' ;
	config.BodyClass = '' ;

	config.DefaultStyleLabel = '' ;
	config.DefaultFontFormatLabel = '' ;
	config.DefaultFontLabel = '' ;
	config.DefaultFontSizeLabel = '' ;

	config.DefaultLinkTarget = '' ;

	// The option switches between trying to keep the html structure or do the changes so the content looks like it was in Word
	config.CleanWordKeepsStructure = false ;

	// Only inline elements are valid.
	config.RemoveFormatTags = 'b,big,code,del,dfn,em,font,i,ins,kbd,q,samp,small,span,strike,strong,sub,sup,tt,u,var' ;

	config.CustomStyles = 
	{
		'Red Title'	: { Element : 'h3', Styles : { 'color' : 'Red' } }
	};

	// Do not add, rename or remove styles here. Only apply definition changes.
	config.CoreStyles = 
	{
		// Basic Inline Styles.
		'Bold'			: { Element : 'b', Overrides : 'strong' },
		'Italic'		: { Element : 'i', Overrides : 'em' },
		'Underline'		: { Element : 'u' },
		'StrikeThrough'	: { Element : 'strike' },
		'Subscript'		: { Element : 'sub' },
		'Superscript'	: { Element : 'sup' },
		
		// Basic Block Styles (Font Format Combo).
		'p'				: { Element : 'p' },
		'div'			: { Element : 'div' },
		'pre'			: { Element : 'pre' },
		'address'		: { Element : 'address' },
		'h1'			: { Element : 'h1' },
		'h2'			: { Element : 'h2' },
		'h3'			: { Element : 'h3' },
		'h4'			: { Element : 'h4' },
		'h5'			: { Element : 'h5' },
		'h6'			: { Element : 'h6' },
		
		// Other formatting features.
		'FontFace' : 
		{ 
			Element		: 'span', 
			Styles		: { 'font-family' : '#("Font")' }, 
			Overrides	: [ { Element : 'font', Attributes : { 'face' : null } } ]
		},
		
		'Size' :
		{ 
			Element		: 'span', 
			Styles		: { 'font-size' : '#("Size","fontSize")' }, 
			Overrides	: [ { Element : 'font', Attributes : { 'size' : null } } ]
		},
		
		'Color' :
		{ 
			Element		: 'span', 
			Styles		: { 'color' : '#("Color","color")' }, 
			Overrides	: [ { Element : 'font', Attributes : { 'color' : null } } ]
		},
		
		'BackColor'		: { Element : 'span', Styles : { 'background-color' : '#("Color","color")' } }
	};

	// The distance of an indentation step.
	config.IndentLength = 40 ;
	config.IndentUnit = 'px' ;

	// Alternatively, FCKeditor allows the use of CSS classes for block indentation.
	// This overrides the IndentLength/IndentUnit settings.
	config.IndentClasses = [];

	 // [ Left, Center, Right, Justified ]
	//config.justifyClasses = ['text-align-left','text-align-center','text-align-right','text-align-justify'] ;

	// The following value defines which File Browser connector and Quick Upload
	// "uploader" to use. It is valid for the default implementaion and it is here
	// just to make this configuration file cleaner.
	// It is not possible to change this value using an external file or even
	// inline when creating the editor instance. In that cases you must set the
	// values of LinkBrowserURL, ImageBrowserURL and so on.
	// Custom implementations should just ignore it.
	var _FileBrowserLanguage	= 'php' ;	// asp | aspx | cfm | lasso | perl | php | py
	var _QuickUploadLanguage	= 'php' ;	// asp | aspx | cfm | lasso | perl | php | py

	// Don't care about the following line. It just calculates the correct connector
	// extension to use for the default File Browser (Perl uses "cgi").
	var _FileBrowserExtension = _FileBrowserLanguage == 'perl' ? 'cgi' : _FileBrowserLanguage ;

	config.LinkBrowser = true ;
	//config.LinkBrowserURL = config.BasePath + 'filemanager/browser/default/browser.html?Connector=../../connectors/' + _FileBrowserLanguage + '/connector.' + _FileBrowserExtension ;
	//config.LinkBrowserURL = config.BasePath + "filemanager/browser/default/browser.html?Connector=connector" ;
	config.LinkBrowserWindowWidth	= config.ScreenWidth * 0.7 ;		// 70%
	config.LinkBrowserWindowHeight	= config.ScreenHeight * 0.7 ;	// 70%

	config.ImageBrowser = true ;
	//config.ImageBrowserURL = config.BasePath + 'filemanager/browser/default/browser.html?Type=Image&Connector=../../connectors/' + _FileBrowserLanguage + '/connector.' + _FileBrowserExtension ;
	//config.ImageBrowserURL = config.BasePath + "filemanager/browser/default/browser.html?Type=Image&Connector=connector" ;
	config.ImageBrowserWindowWidth  = config.ScreenWidth * 0.7 ;	// 70% ;
	config.ImageBrowserWindowHeight = config.ScreenHeight * 0.7 ;	// 70% ;

	config.FlashBrowser = true ;
	//config.FlashBrowserURL = config.BasePath + 'filemanager/browser/default/browser.html?Type=Flash&Connector=../../connectors/' + _FileBrowserLanguage + '/connector.' + _FileBrowserExtension ;
	//config.FlashBrowserURL = config.BasePath + "filemanager/browser/default/browser.html?Type=Flash&Connector=connector" ;
	config.FlashBrowserWindowWidth  = config.ScreenWidth * 0.7 ;	//70% ;
	config.FlashBrowserWindowHeight = config.ScreenHeight * 0.7 ;	//70% ;

	config.LinkUpload = false ;
	config.LinkUploadURL = config.BasePath + 'filemanager/connectors/' + _QuickUploadLanguage + '/upload.' + _QuickUploadLanguage ;
	config.LinkUploadAllowedExtensions	= "" ;			// empty for all
	config.LinkUploadDeniedExtensions	= ".(html|htm|php|php2|php3|php4|php5|phtml|pwml|inc|asp|aspx|ascx|jsp|cfm|cfc|pl|bat|exe|com|dll|vbs|js|reg|cgi|htaccess|asis|sh|shtml|shtm|phtm)$" ;	// empty for no one

	config.ImageUpload = false ;
	config.ImageUploadURL = config.BasePath + 'filemanager/connectors/' + _QuickUploadLanguage + '/upload.' + _QuickUploadLanguage + '?Type=Image' ;
	config.ImageUploadAllowedExtensions	= ".(jpg|gif|jpeg|png|bmp)$" ;		// empty for all
	config.ImageUploadDeniedExtensions	= "" ;							// empty for no one

	config.FlashUpload = false ;
	config.FlashUploadURL = config.BasePath + 'filemanager/connectors/' + _QuickUploadLanguage + '/upload.' + _QuickUploadLanguage + '?Type=Flash' ;
	config.FlashUploadAllowedExtensions	= ".(swf|fla)$" ;		// empty for all
	config.FlashUploadDeniedExtensions	= "" ;					// empty for no one

	config.SmileyPath	= config.BasePath + 'images/smiley/msn/' ;
	config.SmileyImages	= ['regular_smile.gif','sad_smile.gif','wink_smile.gif','teeth_smile.gif','confused_smile.gif','tounge_smile.gif','embaressed_smile.gif','omg_smile.gif','whatchutalkingabout_smile.gif','angry_smile.gif','angel_smile.gif','shades_smile.gif','devil_smile.gif','cry_smile.gif','lightbulb.gif','thumbs_down.gif','thumbs_up.gif','heart.gif','broken_heart.gif','kiss.gif','envelope.gif'] ;
	config.SmileyColumns = 8 ;
	config.SmileyWindowWidth = 320 ;
	config.SmileyWindowHeight = 240 ;
	 
};
