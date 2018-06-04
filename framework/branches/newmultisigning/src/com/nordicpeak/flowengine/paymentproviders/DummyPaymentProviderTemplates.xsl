<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="scriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<!-- <xsl:variable name="links">
		/css/authifyclientprovider.css
	</xsl:variable> -->

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/dummypaymentprovider.js
	</xsl:variable>

	<xsl:template match="Document">
	
		<div id="DummyPaymentProvider">
			
			<xsl:apply-templates select="InlinePaymentForm" />
			<xsl:apply-templates select="StandalonePaymentForm" />
			
		</div>
			
	</xsl:template>
	
	<xsl:template match="InlinePaymentForm">
		
		<xsl:call-template name="paymentForm" />

	</xsl:template>
	
	<xsl:template match="StandalonePaymentForm">
		
		<xsl:call-template name="paymentForm" />

	</xsl:template>
	
	<xsl:template name="paymentForm">
		
		<xsl:apply-templates select="validationError" />
		
		<div class="errands-wrapper nomargin nopadding">
			
			<xsl:if test="validationError">
				<xsl:attribute name="class">error</xsl:attribute>
			</xsl:if>
			
			<h3><xsl:value-of select="$i18n.PaymentDescription" /></h3>
			
			<table class="oep-table full bigmarginbottom bigmargintop">
				<thead>
					<tr>
						<th><xsl:value-of select="$i18n.Description" /></th>
						<th><xsl:value-of select="$i18n.Quantity" /></th>
						<th><xsl:value-of select="$i18n.UnitPrice" /></th>
						<th><xsl:value-of select="$i18n.Amount" /></th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="InvoiceLine" />
						<tr>
							<td class="text-algin-left bold"><xsl:value-of select="$i18n.TotalSum" /></td>
							<td />
							<td />
							<td class="bold"><xsl:value-of select="TotalSum" /></td>
						</tr>
				</tbody>
			</table>
			
			<h3><xsl:value-of select="$i18n.ChoosePayment" /></h3>
			
			<div class="floatleft twenty">
			
				<div>
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'invoice'" />
						<xsl:with-param name="name" select="'type'" />
						<xsl:with-param name="value" select="'INVOICE'" />
						<xsl:with-param name="checked" select="true()" />
					</xsl:call-template>
					<label for="invoice" class="radio">
						<xsl:value-of select="$i18n.Invoice" />
					</label>					
				</div>
				<div>
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'visa'" />
						<xsl:with-param name="name" select="'type'" />
						<xsl:with-param name="value" select="'VISA'" />
					</xsl:call-template>
					<label for="visa" class="radio">
						<xsl:value-of select="$i18n.Visa" />
					</label>					
				</div>
				<div>
					<xsl:call-template name="createRadio">
						<xsl:with-param name="id" select="'mastercard'" />
						<xsl:with-param name="name" select="'type'" />
						<xsl:with-param name="value" select="'MASTERCARD'" />
					</xsl:call-template>
					<label for="mastercard" class="radio">
						<xsl:value-of select="$i18n.MasterCard" />
					</label>					
				</div>
			
			</div>
			
			<div class="floatleft bigmargintop bigpadding border lightbackground">
			
				<div id="visaPayment" class="hidden paymentmethods">
					
					<h4><xsl:value-of select="$i18n.Payment" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.Visa" /></h4>
					
					<xsl:call-template name="createCardForm" />
					
				</div>
				
				<div id="mastercardPayment" class="hidden paymentmethods">
					
					<h4><xsl:value-of select="$i18n.Payment" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.MasterCard" /></h4>
					
					<xsl:call-template name="createCardForm" />
					
				</div>
			
				<div id="invoicePayment" class="hidden paymentmethods">
					
					<h4><xsl:value-of select="$i18n.Payment" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.Invoice" /></h4>
					
					<p>En faktura kommer att skickas till din e-postadress och skall betalas inom 10 dagar.</p>
					
				</div>
			
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template name="createCardForm">
		
		<xsl:call-template name="createTextField">
			<xsl:with-param name="name" select="'cardNumber'"/>
			<xsl:with-param name="title" select="$i18n.Visa"/>
			<xsl:with-param name="size" select="'30'" />
			<xsl:with-param name="class" select="'marginright'" />
			<xsl:with-param name="placeholder" select="'Kortnummer'" />
		</xsl:call-template>
		
		<xsl:call-template name="createTextField">
			<xsl:with-param name="name" select="'cvc'"/>
			<xsl:with-param name="title" select="'CVC-kod'"/>
			<xsl:with-param name="size" select="'10'" />
			<xsl:with-param name="class" select="'marginright'" />
			<xsl:with-param name="placeholder" select="'CVC-kod'" />
		</xsl:call-template>
		
		<select name="year" class="marginright" style="width: 140px; height: 32px">
			<option value="">Giltigt år</option>
			<option value="2015">2015</option>
			<option value="2016">2016</option>
			<option value="2017">2017</option>
			<option value="2018">2018</option>
		</select>
		
		<select name="month" style="width: 175px; height: 32px">
			<option value="">Giltigt månad</option>
			<option value="1">01</option>
			<option value="2">02</option>
			<option value="3">03</option>
			<option value="4">04</option>
			<option value="5">05</option>
			<option value="6">06</option>
			<option value="7">07</option>
			<option value="8">08</option>
			<option value="9">09</option>
			<option value="10">10</option>
			<option value="11">11</option>
			<option value="12">12</option>
		</select>
		
	</xsl:template>
	
	<xsl:template match="InvoiceLine">
		
		<tr>
			<td><xsl:value-of select="description" /></td>
			<td><xsl:value-of select="quantity" /></td>
			<td><xsl:value-of select="unitPrice" /></td>
			<td><xsl:value-of select="quantity * unitPrice" /></td>
		</tr>
		
	</xsl:template>
	
	<xsl:template match="validationError">

		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.UnknownValidationError"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template match="validationError[messageKey = 'PaymentFailed']">

		<xsl:call-template name="printValidationError">
			<xsl:with-param name="message" select="$i18n.PaymentFailed"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
	<xsl:template name="printValidationError">
		
		<xsl:param name="message" />
		
		<div class="info-box first error">
			<span>
				<strong data-icon-before="!"><xsl:value-of select="$i18n.PaymentFailedTitle" />.</strong>
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="$message" />
			</span>
			<div class="marker" />
		</div>
		
	</xsl:template>
	
</xsl:stylesheet>