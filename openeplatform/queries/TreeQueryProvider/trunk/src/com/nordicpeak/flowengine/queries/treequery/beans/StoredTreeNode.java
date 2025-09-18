package com.nordicpeak.flowengine.queries.treequery.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.treequerydataprovider.TreeNode;

@Table(name = "tree_query_nodes")
@XMLElement(name="TreeNode")
public class StoredTreeNode extends LinkedHashMap<String, StoredTreeNode> implements Elementable, Serializable, Comparable<StoredTreeNode> {

	private static final long serialVersionUID = 7634780772195411122L;

	public static final Field QUERY_INSTANCE_RELATION = ReflectionUtils.getField(StoredTreeNode.class, "queryInstanceID");

	@DAOManaged(columnName = "value")
	@Key
	@XMLElement
	private String key;

	@DAOManaged
	@XMLElement
	private String name;

	@DAOManaged
	@Key
	@ManyToOne
	private TreeQueryInstance queryInstanceID;

	@DAOManaged
	@XMLElement
	private String parentNodeKey;

	@DAOManaged
	@XMLElement
	private boolean folder = true;

	@XMLElement
	private String icon;

	private StoredTreeNode parent;

	public StoredTreeNode() {};

	public StoredTreeNode(String name, String key) {
		super();

		this.key = key;
		this.name = name;
	}

	public StoredTreeNode(TreeNode otherNode) {

		key = otherNode.getKey();
		name = otherNode.getName();
		folder = otherNode.isFolder();
	}

	public StoredTreeNode(StoredTreeNode otherNode) {

		key = otherNode.getKey();
		name = otherNode.getName();
		folder = otherNode.isFolder();
	}
	
	public StoredTreeNode put(StoredTreeNode node) {

		return put(node.getKey(), node);
	}

	@Override
	public StoredTreeNode put(String key, StoredTreeNode node) {

		node.setParent(this);
		return super.put(key, node);
	}
	
	public StoredTreeNode remove(StoredTreeNode node) {

		super.remove(node.getKey());
		node.setParent(null);

		return node;
	}
	
	public StoredTreeNode remove(String key) {

		StoredTreeNode node = super.remove(key);

		if (node != null) {

			node.setParent(null);
		}

		return node;
	}

	@Override
	public StoredTreeNode remove(Object key) {

		throw new RuntimeException("Wrong key type");
	}

	public StoredTreeNode getParent() {

		return parent;
	}

	protected void setParent(StoredTreeNode parent) {

		this.parent = parent;

		if (parent == null) {

			parentNodeKey = null;

		} else {

			parentNodeKey = parent.getKey();
		}
	}

	public String getParentNodeKey() {

		return parentNodeKey;
	}

	public String getKey() {

		return key;
	}

	public void setKey(String id) {

		this.key = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public TreeQueryInstance getQueryInstance() {

		return queryInstanceID;
	}

	public void setQueryInstance(TreeQueryInstance queryInstance) {

		this.queryInstanceID = queryInstance;
	}

	public boolean isFolder() {

		return folder;
	}

	public void setFolder(boolean folder) {

		this.folder = folder;
	}

	public String getIcon() {

		return icon;
	}

	public void setIcon(String icon) {

		this.icon = icon;
	}

	@Override
	public Element toXML(Document doc) {

		Element element = XMLGenerator.toXML(this, doc);

		if (!isEmpty()) {

			List<StoredTreeNode> children = new ArrayList<StoredTreeNode>(values());

			Collections.sort(children);

			XMLUtils.append(doc, element, "Children", children);
		}

		return element;
	}

	public StoredTreeNode find(String key) {

		if (this.key.equals(key)) {
			return this;
		}

		StoredTreeNode node = get(key);

		if (node != null) {
			return node;
		}

		for (StoredTreeNode subNode : values()) {

			node = subNode.find(key);

			if (node != null) {
				return node;
			}
		}

		return null;
	}

	public void fillMap(Map<String, StoredTreeNode> treeMap) {

		for (Map.Entry<String, StoredTreeNode> entry : entrySet()) {
			treeMap.put(entry.getKey(), entry.getValue());
		}

		for (StoredTreeNode subNode : values()) {
			subNode.fillMap(treeMap);
		}
	}

	public StoredTreeNode getDirectParentsTree() {

		StoredTreeNode newNode = new StoredTreeNode(this);

		if (parent != null) {

			newNode.setParent(parent.getDirectParentsTree());
		}

		return newNode;
	}

	public List<StoredTreeNode> getDirectParents() {

		List<StoredTreeNode> nodes = new ArrayList<StoredTreeNode>();

		StoredTreeNode node = getDirectParentsTree();

		while (node != null) {

			nodes.add(node);
			node = node.getParent();
		}

		return nodes;
	}

	@Override
	public int compareTo(StoredTreeNode o) {

		if (isFolder() == o.isFolder()) {

			return name.compareToIgnoreCase(o.getName());

		} else {

			return isFolder() ? -1 : 1;
		}
	}

}
