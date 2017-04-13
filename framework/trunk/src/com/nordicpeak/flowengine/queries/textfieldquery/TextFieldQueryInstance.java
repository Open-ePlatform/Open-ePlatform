package com.nordicpeak.flowengine.queries.textfieldquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.ColumnExportableQueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;

@Table(name = "text_field_query_instances")
@XMLElement
public class TextFieldQueryInstance extends BaseQueryInstance implements StringValueQueryInstance, ColumnExportableQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field VALUES_RELATION = ReflectionUtils.getField(TextFieldQueryInstance.class, "values");
	public static final Field QUERY_RELATION = ReflectionUtils.getField(TextFieldQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName="queryID")
	@ManyToOne
	@XMLElement
	private TextFieldQuery query;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase=true)
	private List<TextFieldValue> values;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}


	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}


	public TextFieldQuery getQuery() {

		return query;
	}


	public void setQuery(TextFieldQuery query) {

		this.query = query;
	}


	@Override
	public String toString() {

		return "TextFieldQueryQueryInstance (queryInstanceID=" + queryInstanceID + ")";
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		if(this.values != null){

			Iterator<TextFieldValue> iterator = this.values.iterator();
			
			while(iterator.hasNext()){
				
				TextFieldValue textFieldValue = iterator.next();
				
				if(textFieldValue.getTextField().isSetAsAttribute()){

					attributeHandler.removeAttribute(textFieldValue.getTextField().getAttributeName());
				}
				
				if(!textFieldValue.getTextField().isDisabled()){
					
					iterator.remove();
				}
			}
		}

		super.reset(attributeHandler);
	}


	public List<TextFieldValue> getValues() {

		return values;
	}


	public void setValues(List<TextFieldValue> values) {

		this.values = values;
	}

	public String getFieldValue(String label) {

		if(this.values != null){

			for(TextFieldValue textFieldValue : this.values){

				if(textFieldValue.getTextField().getLabel().equals(label)){

					return textFieldValue.getValue();
				}
			}
		}

		return null;
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		if(this.values != null){

			//TODO this code needs to be sorted later on as it may lead to different element names than the generated XSD since not all labels are iterated over, preferably the element names of the fields should be set when configuring the query

			ArrayList<String> fieldElementNames = new ArrayList<String>(this.values.size());

			for(TextFieldValue textFieldValue : this.values){

				String elementName;
				
				if(textFieldValue.getTextField().getXSDElementName() != null){
					
					elementName = textFieldValue.getTextField().getXSDElementName();
					
					fieldElementNames.add(elementName);
					
				}else{
					
					elementName = TextFieldQuery.generateElementName(textFieldValue.getTextField().getLabel(), fieldElementNames);
				}				
				
				XMLUtils.appendNewCDATAElement(doc, element, elementName, textFieldValue.getValue());
			}
		}

		return element;
	}
	
	@Override
	public String getStringValue() {

		if(values == null){

			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();

		for(TextFieldValue value : values){

			if(stringBuilder.length() != 0){

				stringBuilder.append(", ");
			}

			stringBuilder.append(value.getTextField().getLabel() + ":" + value.getValue());
		}

		return stringBuilder.toString();
	}
	
	@Override
	public List<String> getColumnLabels(QueryHandler queryHandler) {

		List<String> labels = new ArrayList<String>();

		if (!CollectionUtils.isEmpty(query.getFields())) {

			for (TextField textField : query.getFields()) {

				labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + textField.getLabel());
			}
		}

		return labels;
	}

	@Override
	public List<String> getColumnValues() {

		List<String> values = new ArrayList<String>();

		if (!CollectionUtils.isEmpty(query.getFields())) {

			for (TextField textField : query.getFields()) {
				
				boolean found = false;

				for (TextFieldValue value : this.values) {

					if (textField.getTextFieldID().equals(value.getTextField().getTextFieldID())) {

						values.add(value.getValue());
						found = true;
						break;
					}
				}
				
				if(!found){
					values.add(null);
				}
			}
		}

		return values;
	}


	protected TextFieldValue getTextFieldValue(Integer textFieldID) {

		if(values != null){
			
			for(TextFieldValue textFieldValue : values){
				
				if(textFieldValue.getTextField().getTextFieldID().equals(textFieldID)){
					
					return textFieldValue;
				}
			}
		}
		
		return null;
	}

	public String getFieldValue(Integer textFieldID) {
		
		TextFieldValue textFieldValue = getTextFieldValue(textFieldID);
		
		if(textFieldValue != null){
			
			return textFieldValue.getValue();
		}
		
		return null;
	}	
	
	public void setFieldValue(Integer textFieldID, String value, MutableAttributeHandler attributeHandler) {

		if(value == null){
			
			if(values != null){
				
				for(TextFieldValue textFieldValue : values){
					
					if(textFieldValue.getTextField().getTextFieldID().equals(textFieldID)){
						
						values.remove(textFieldValue);
						
						break;
					}
				}
			}
			
			return;
		}
		
		if(values != null){
			
			for(TextFieldValue textFieldValue : values){
				
				if(textFieldValue.getTextField().getTextFieldID().equals(textFieldID)){
					
					textFieldValue.setValue(value);
					
					if (textFieldValue.getTextField().isSetAsAttribute()) {

						attributeHandler.setAttribute(textFieldValue.getTextField().getAttributeName(), value);
					}
					
					return;
				}
			}
		}
		
		if(getQuery().getFields() != null){
			
			for (TextField textField : getQuery().getFields()) {
				
				if(textField.getTextFieldID().equals(textFieldID)){
					
					if(values == null){
						
						values = new ArrayList<TextFieldValue>(1);
					}
					
					values.add(new TextFieldValue(textField, value));
					
					if (textField.isSetAsAttribute()) {
						
						attributeHandler.setAttribute(textField.getAttributeName(), value);
					}
					
					return;
				}
			}
		}
	}

	public void setDefaultValues() {

		if(query.getFields() != null){
			
			for(TextField textField : query.getFields()){
				
				if(textField.isDisabled() && textField.getDefaultValue() != null){
					
					setFieldValue(textField.getTextFieldID(), textField.getDefaultValue(), null);
				}
			}
		}
	}
}
