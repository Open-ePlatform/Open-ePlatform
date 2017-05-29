package com.nordicpeak.flowengine.persondatasavinginformer.query;

import java.lang.reflect.Field;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativeQueryUtils;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;

@Table(name = "person_data_informer_query_instances")
@XMLElement
public class PersonDataInformerQueryInstance extends BaseQueryInstance implements FixedAlternativesQueryInstance {
	
	private static final long serialVersionUID = -7761759005604863873L;
	
	public static final Field QUERY_RELATION = ReflectionUtils.getField(PersonDataInformerQueryInstance.class, "query");
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;
	
	@DAOManaged
	@XMLElement
	private boolean accepted;
	
	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private PersonDataInformerQuery query;
	
	public Integer getQueryInstanceID() {
		
		return queryInstanceID;
	}
	
	public void setQueryInstanceID(Integer queryInstanceID) {
		
		this.queryInstanceID = queryInstanceID;
	}
	
	@Override
	public PersonDataInformerQuery getQuery() {
		
		return query;
	}
	
	public void setQuery(PersonDataInformerQuery query) {
		
		this.query = query;
	}
	
	@Override
	public void reset(MutableAttributeHandler attributeHandler) {
		
		super.reset(attributeHandler);
		
		this.accepted = false;
	}
	
	public boolean isAccepted() {
		
		return accepted;
	}
	
	public void setAccepted(boolean accepted) {
		
		this.accepted = accepted;
	}
	
	@Override
	public String toString() {
		
		return this.getClass().getSimpleName() + " (queryInstanceID=" + queryInstanceID + ")";
	}
	
	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {
		
		Element element = getBaseExportXML(doc);
		
		XMLUtils.appendNewElement(doc, element, "Accepted", accepted);
		
		FixedAlternativeQueryUtils.appendExportXMLAlternatives(doc, element, this);
		
		return element;
	}
	
	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {
		
		if (accepted) {
			
			return query.getAlternatives();
		}
		
		return null;
	}
	
	@Override
	public String getFreeTextAlternativeValue() {
		
		return null;
	}
	
	public void defaultQueryValues() {
		
	}
	
	@Override
	public Element toXML(Document doc) {
		
		Element element = super.toXML(doc);
		
		element.appendChild(queryInstanceDescriptor.getQueryDescriptor().getStep().getFlow().getFlowFamily().toXML(doc));
		
		return element;
	}
}
