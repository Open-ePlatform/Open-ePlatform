package com.nordicpeak.flowengine.paymentproviders;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.interfaces.InlinePaymentCallback;
import com.nordicpeak.flowengine.interfaces.InvoiceLine;
import com.nordicpeak.flowengine.interfaces.PaymentProvider;
import com.nordicpeak.flowengine.interfaces.PaymentQuery;
import com.nordicpeak.flowengine.interfaces.StandalonePaymentCallback;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;


public class DummyPaymentProvider extends AnnotatedForegroundModule implements PaymentProvider, ViewFragmentModule<ForegroundModuleDescriptor> {

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> fragmentTransformer;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		if(!systemInterface.getInstanceHandler().addInstance(PaymentProvider.class, this)){

			throw new RuntimeException("Unable to register module " + this.moduleDescriptor + " in global instance handler using key " + PaymentProvider.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(PaymentProvider.class, this);		
		
		super.unload();
	}
	
	@Override
	protected void moduleConfigured() {

		fragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getModuleXSLTCache(), this, systemInterface.getEncoding());

	}
	
	@Override
	public ViewFragment pay(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, MutableFlowInstanceManager instanceManager, InlinePaymentCallback callback) throws Exception {

		if(req.getParameter("type") != null) {
			
			log.info("User " + user + " payed flow instance " + instanceManager.getFlowInstance());
			
			callback.paymentComplete(instanceManager, user, req, null, null);
			
			res.sendRedirect(callback.getPaymentSuccessURL(instanceManager, req));
			
			return null;
		}

		log.info("User " + user + " requested payment form for flow instance " + instanceManager);
		
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		doc.appendChild(document);

		Element paymentElement = doc.createElement("InlinePaymentForm");
		document.appendChild(paymentElement);

		appendInvoiceLines(doc, paymentElement, instanceManager);
		
		try {

			return fragmentTransformer.createViewFragment(doc);

		} catch (Exception e) {

			res.sendRedirect(callback.getPaymentFailURL(instanceManager, req));

			return null;

		}
		
	}

	@Override
	public ViewFragment pay(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ImmutableFlowInstanceManager instanceManager, StandalonePaymentCallback callback) throws Exception {

		if(req.getMethod().equalsIgnoreCase("POST") && req.getParameter("type") != null) {
			
			log.info("User " + user + " payed flow instance " + instanceManager.getFlowInstance());
			
			callback.paymentComplete(instanceManager, req, user);
			
			res.sendRedirect(callback.getPaymentSuccessURL(instanceManager, req));
			
			return null;
		}
		
		log.info("User " + user + " requested payment form for flow instance " + instanceManager.getFlowInstance());
		
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		doc.appendChild(document);

		Element paymentElement = doc.createElement("StandalonePaymentForm");
		document.appendChild(paymentElement);

		appendInvoiceLines(doc, paymentElement, instanceManager);
		
		try {

			return fragmentTransformer.createViewFragment(doc);

		} catch (Exception e) {

			res.sendRedirect(callback.getPaymentFailURL(instanceManager, req));

			return null;

		}
	}

	private void appendInvoiceLines(Document doc, Element element, FlowInstanceManager instanceManager) {
		
		List<PaymentQuery> paymentQueries = instanceManager.getQueries(PaymentQuery.class);
		
		if(paymentQueries != null) {
			
			List<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();

			int totalSum = 0;
			
			for(PaymentQuery paymentQuery : paymentQueries) {
				
				if(paymentQuery.getInvoiceLines() != null) {
					
					for(InvoiceLine invoiceLine : paymentQuery.getInvoiceLines()) {
						
						totalSum += invoiceLine.getQuanitity() * invoiceLine.getUnitPrice();
						
						invoiceLines.add(invoiceLine);
					}
					
				}
								
			}
			
			XMLUtils.append(doc, element, invoiceLines);
			XMLUtils.appendNewElement(doc, element, "TotalSum", totalSum);
			
		}
	}
	
	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {

		return this.moduleDescriptor;
	}

	@Override
	public List<LinkTag> getLinkTags() {

		return this.links;
	}

	@Override
	public List<ScriptTag> getScriptTags() {

		return this.scripts;
	}

}
