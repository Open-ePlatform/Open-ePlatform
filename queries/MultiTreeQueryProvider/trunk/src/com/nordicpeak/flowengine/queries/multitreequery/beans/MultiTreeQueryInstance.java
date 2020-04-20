package com.nordicpeak.flowengine.queries.multitreequery.beans;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;

@Table(name = "multi_tree_query_instances")
@XMLElement
public class MultiTreeQueryInstance extends BaseQueryInstance implements StringValueQueryInstance, FixedAlternativesQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(MultiTreeQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private MultiTreeQuery query;

	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "multi_tree_query_selected_nodes", remoteValueColumnName = "selectedNodeKey")
	@XMLElement
	private List<String> selectedNodeKeys;

	@DAOManaged
	@OneToMany(autoGet = true, autoAdd = true, autoUpdate = true)
	@XMLElement(fixCase = true)
	private List<StoredTreeNode> storedTreeNodes;

	private StoredTreeNode fullTree;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	@Override
	public MultiTreeQuery getQuery() {

		return query;
	}

	public void setQuery(MultiTreeQuery query) {

		this.query = query;
	}

	public List<String> getSelectedNodeKeys() {

		return selectedNodeKeys;
	}

	public void setSelectedNodeKeys(List<String> selectedNodeKeys) {

		this.selectedNodeKeys = selectedNodeKeys;
	}

	public StoredTreeNode getFullTree() {

		return fullTree;
	}

	public void setFullTree(StoredTreeNode fullTree) {

		this.fullTree = fullTree;
	}

	public List<StoredTreeNode> getStoredTreeNodes() {

		return storedTreeNodes;
	}

	public void setStoredTreeNodes(List<StoredTreeNode> storedTreeNodes) {

		this.storedTreeNodes = storedTreeNodes;
	}

	public List<StoredTreeNode> getSelectedTreeNodes() {

		if (selectedNodeKeys != null) {

			return getTreeNodesFromStoredNodes(selectedNodeKeys);
		}

		return Collections.emptyList();
	}
	
	public StoredTreeNode getTreeNodeFromStoredNodes(String keys) {
		
		if (!CollectionUtils.isEmpty(storedTreeNodes)) {
			
			for (StoredTreeNode node : storedTreeNodes) {
				
				if (keys.contains(node.getKey())) {
					
					return node;
				}
			}
		}
		
		return null;
	}

	public List<StoredTreeNode> getTreeNodesFromStoredNodes(List<String> keys) {

		List<StoredTreeNode> nodes = null;

		if (!CollectionUtils.isEmpty(storedTreeNodes)) {

			for (StoredTreeNode node : storedTreeNodes) {

				if (keys.contains(node.getKey())) {

					nodes = CollectionUtils.addAndInstantiateIfNeeded(nodes, node);
				}
			}
		}

		if (nodes == null) {

			return Collections.emptyList();
		}

		return nodes;
	}

	public StoredTreeNode getSelectedTreeNodeWithParents(String key) {

		List<StoredTreeNode> nodes = getSelectedTreeNodes();
		
		for (StoredTreeNode node : nodes) {
			
			if (node.getKey().equals(key)) {
				
				setParentsForStoredTreeNode(node);
				
				return node;
			}
		}

		return null;
	}

	private void setParentsForStoredTreeNode(StoredTreeNode node) {

		if (node.getParentNodeKey() != null) {

			StoredTreeNode parent = getTreeNodeFromStoredNodes(node.getParentNodeKey());

			if (parent != null) {

				parent.put(node);
				setParentsForStoredTreeNode(parent);
			}
		}
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		selectedNodeKeys = null;
		storedTreeNodes = null;

		if (query.isSetAsAttribute()) {

			resetFlowInstanceAttributes(attributeHandler);
		}

		super.reset(attributeHandler);
	}

	public void setFlowInstanceAttributes(MutableAttributeHandler attributeHandler) {

		attributeHandler.setAttribute(query.getAttributeName() + ".ID", StringUtils.toCommaSeparatedString(selectedNodeKeys));

		String attribute = getStringValue();

		if (attribute != null) {
			
			attributeHandler.setAttribute(query.getAttributeName() + ".Name", attribute);
		}
	}

	public void resetFlowInstanceAttributes(MutableAttributeHandler attributeHandler) {

		attributeHandler.removeAttribute(query.getAttributeName() + ".ID");
		attributeHandler.removeAttribute(query.getAttributeName() + ".Name");
	}

	@Override
	public Element toXML(Document doc) {

		Element element = XMLGenerator.toXML(this, doc);

		if (fullTree != null) {

			Element fullTreeElement = XMLUtils.appendNewElement(doc, element, "FullTree");
			XMLUtils.append(doc, fullTreeElement, fullTree);
		}

		return element;
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + " (queryInstanceID=" + queryInstanceID + ")";
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		XMLUtils.appendNewElement(doc, element, "SelectedNodeKey", selectedNodeKeys);
		XMLUtils.append(doc, element, "TreeNodes", storedTreeNodes);

		return element;
	}

	@Override
	public String getStringValue() {

		List<StoredTreeNode> nodes = getSelectedTreeNodes();

		if (!nodes.isEmpty()) {
			
			return nodes.stream().map(StoredTreeNode::getName).collect(Collectors.joining(", "));
		}
		
		return null;
	}

	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {

		List<ImmutableAlternative> alternatives = null;

		if (selectedNodeKeys != null) {
			alternatives = CollectionUtils.addAndInstantiateIfNeeded(alternatives, query.getSelectedAlternative());
		}

		return alternatives;
	}

	@Override
	public String getFreeTextAlternativeValue() {

		return null;
	}

}
