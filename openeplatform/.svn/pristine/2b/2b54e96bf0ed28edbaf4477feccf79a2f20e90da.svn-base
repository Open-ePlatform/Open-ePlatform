package com.nordicpeak.flowengine.persondatasavinginformer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerDataAlternative;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerReasonAlternative;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerStandardText;
import com.nordicpeak.flowengine.persondatasavinginformer.cruds.DataAlternativeCRUD;
import com.nordicpeak.flowengine.persondatasavinginformer.cruds.ReasonAlternativeCRUD;
import com.nordicpeak.flowengine.persondatasavinginformer.cruds.StandardTextsCRUD;

import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.crud.CRUDEventFilter;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class PersonDataInformerAdminModule extends AnnotatedForegroundModule implements CRUDCallback<User> {
	
	protected AnnotatedDAO<InformerStandardText> standardTextsDAO;
	protected AnnotatedDAO<InformerDataAlternative> dataAlternativeDAO;
	protected AnnotatedDAO<InformerReasonAlternative> reasonAlternativeDAO;
	
	protected StandardTextsCRUD standardTextsCRUD;
	protected DataAlternativeCRUD dataAlternativeCRUD;
	protected ReasonAlternativeCRUD reasonAlternativeCRUD;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		standardTextsDAO = daoFactory.getDAO(InformerStandardText.class);
		dataAlternativeDAO = daoFactory.getDAO(InformerDataAlternative.class);
		reasonAlternativeDAO = daoFactory.getDAO(InformerReasonAlternative.class);
		
		AdvancedAnnotatedDAOWrapper<InformerStandardText, Integer> standardTextsDAOWrapper = standardTextsDAO.getAdvancedWrapper(Integer.class);
		AdvancedAnnotatedDAOWrapper<InformerDataAlternative, Integer> dataAlternativeDAOWrapper = dataAlternativeDAO.getAdvancedWrapper(Integer.class);
		AdvancedAnnotatedDAOWrapper<InformerReasonAlternative, Integer> reasonAlternativeDAOWrapper = reasonAlternativeDAO.getAdvancedWrapper(Integer.class);
		
		standardTextsCRUD = new StandardTextsCRUD(standardTextsDAOWrapper, this);
		standardTextsCRUD.addBeanFilter(new CRUDEventFilter<InformerStandardText>(InformerStandardText.class, systemInterface.getEventHandler()));
		
		dataAlternativeCRUD = new DataAlternativeCRUD(dataAlternativeDAOWrapper, this);
		reasonAlternativeCRUD = new ReasonAlternativeCRUD(reasonAlternativeDAOWrapper, this);
	}
	
	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		log.info("User " + user + " listing flows with person data");

		Document doc = createDocument(req, uriParser, user);

		Element listFlowsElement = doc.createElement("ListDataReasonAlternatives");
		
		List<InformerDataAlternative> dataAlternatives = dataAlternativeDAO.getAll();
		
		if (!CollectionUtils.isEmpty(dataAlternatives)) {
			XMLUtils.append(doc, listFlowsElement, "DataAlternatives", dataAlternatives);
		}
		
		List<InformerReasonAlternative> reasonAlternatives = reasonAlternativeDAO.getAll();
		
		if (!CollectionUtils.isEmpty(reasonAlternatives)) {
			XMLUtils.append(doc, listFlowsElement, "ReasonAlternatives", reasonAlternatives);
		}
		
		List<InformerStandardText> standardTexts = standardTextsDAO.getAll();
		
		if (!CollectionUtils.isEmpty(standardTexts)) {
			XMLUtils.append(doc, listFlowsElement, "InformerStandardTexts", standardTexts);
		}

		doc.getDocumentElement().appendChild(listFlowsElement);

		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateStandardText(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return standardTextsCRUD.update(req, res, user, uriParser);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addDataAlternative(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return dataAlternativeCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateDataAlternative(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return dataAlternativeCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteDataAlternative(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return dataAlternativeCRUD.delete(req, res, user, uriParser);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addReasonAlternative(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return reasonAlternativeCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateReasonAlternative(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return reasonAlternativeCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteReasonAlternative(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return reasonAlternativeCRUD.delete(req, res, user, uriParser);
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));

		doc.appendChild(document);

		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return moduleDescriptor.getName();
	}
}