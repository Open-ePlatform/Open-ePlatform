package com.nordicpeak.treequerydataprovider;

import java.util.LinkedHashMap;

public class SimpleTreeNode extends LinkedHashMap<String, TreeNode> implements TreeNode, Comparable<SimpleTreeNode> {

	private static final long serialVersionUID = -4365397104915655961L;

	private String key;

	private String name;

	private String parentNodeKey;

	private boolean folder = true;

	private String icon;

	private SimpleTreeNode parent;

	public SimpleTreeNode() {};

	public SimpleTreeNode(String name, String key) {
		super();

		this.key = key;
		this.name = name;
	}
	
	public String getKey() {

		return key;
	}

	public void setKey(String key) {

		this.key = key;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getParentNodeKey() {

		return parentNodeKey;
	}

	public void setParentNodeKey(String parentNodeKey) {

		this.parentNodeKey = parentNodeKey;
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

	public SimpleTreeNode getParent() {

		return parent;
	}

	public void setParent(SimpleTreeNode parent) {

		this.parent = parent;
	}

	public TreeNode put(TreeNode node) {

		return put(node.getKey(), node);
	}
	
	@Override
	public int compareTo(SimpleTreeNode o) {

		if (isFolder() == o.isFolder()) {

			return name.compareToIgnoreCase(o.getName());

		} else {

			return isFolder() ? -1 : 1;
		}
	}	
}
