package com.nordicpeak.flowengine.queries.treequery.beans;

import java.lang.reflect.Field;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;

import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;

@Table(name = "tree_query_instances")
@XMLElement
public class TreeQueryInstance extends BaseQueryInstance implements StringValueQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(TreeQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private TreeQuery query;

	@DAOManaged
	@XMLElement
	private String selectedNodeKey;

	@DAOManaged
	@OneToMany(autoGet = true, autoAdd = true, autoUpdate = true)
	@XMLElement(fixCase = true)
	private List<StoredTreeNode> selectedTreeNodes;

	private StoredTreeNode fullTree;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	public TreeQuery getQuery() {

		return query;
	}

	public void setQuery(TreeQuery query) {

		this.query = query;
	}

	public String getSelectedNodeKey() {

		return selectedNodeKey;
	}

	public void setSelectedNodeKey(String selectedNodeID) {

		this.selectedNodeKey = selectedNodeID;
	}

	public StoredTreeNode getFullTree() {

		return fullTree;
	}

	public void setFullTree(StoredTreeNode fullTree) {

		this.fullTree = fullTree;
	}

	public List<StoredTreeNode> getSelectedTreeNodes() {

		return selectedTreeNodes;
	}

	public void setSelectedTreeNodes(List<StoredTreeNode> selectedTreeNodes) {

		this.selectedTreeNodes = selectedTreeNodes;
	}

	public StoredTreeNode getSelectedTreeNode() {

		if (selectedNodeKey != null) {

			return getTreeNodeFromSelectedNodes(selectedNodeKey);
		}

		return null;
	}

	public StoredTreeNode getTreeNodeFromSelectedNodes(String key) {

		if (!CollectionUtils.isEmpty(selectedTreeNodes)) {
			for (StoredTreeNode node : selectedTreeNodes) {

				if (node.getKey().equals(key)) {
					return node;
				}
			}
		}

		return null;
	}

	public StoredTreeNode getSelectedTreeNodeWithParents() {

		StoredTreeNode node = getSelectedTreeNode();

		setParentsForSelectedTreeNodes(node);

		return node;
	}

	private void setParentsForSelectedTreeNodes(StoredTreeNode node) {

		if (node != null && node.getParentNodeKey() != null) {

			StoredTreeNode parent = getTreeNodeFromSelectedNodes(node.getParentNodeKey());

			if (parent != null) {

				parent.put(node);
				setParentsForSelectedTreeNodes(parent);
			}
		}
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		selectedNodeKey = null;
		selectedTreeNodes = null;
		
		super.reset(attributeHandler);
	}

	public void copyQueryValues() {

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

		XMLUtils.appendNewElement(doc, element, "SelectedNodeKey", selectedNodeKey);
		XMLUtils.append(doc, element, "TreeNodes", selectedTreeNodes);

		return element;
	}

	@Override
	public String getStringValue() {

		StoredTreeNode selectedNode = getSelectedTreeNode();

		if (selectedNode != null) {

			return selectedNode.getName();
		}

		return null;
	}

}
