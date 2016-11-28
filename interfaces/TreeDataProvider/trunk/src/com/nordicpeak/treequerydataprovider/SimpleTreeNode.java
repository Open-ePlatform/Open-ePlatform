package com.nordicpeak.treequerydataprovider;

import java.util.LinkedHashMap;


public class SimpleTreeNode extends LinkedHashMap<String, TreeNode> implements TreeNode, Comparable<TreeNode> {

	private static final long serialVersionUID = -4365397104915655961L;

	private String key;

	private String name;

	private String parentNodeKey;

	private boolean folder = true;

	private String icon;

	private TreeNode parent;

	public SimpleTreeNode() {};

	public SimpleTreeNode(String name, String key) {
		super();
		
		this.key = key;
		this.name = name;
	}
	
	@Override
	public TreeNode put(TreeNode node) {

		return put(node.getKey(), node);
	}
	
	@Override
	public TreeNode put(String key, TreeNode node) {

		node.setParent(this);
		return super.put(key, node);
	}
	
	public TreeNode remove(TreeNode node) {

		super.remove(node.getKey());
		node.setParent(null);

		return node;
	}
	
	public TreeNode remove(String key) {

		TreeNode node = super.remove(key);

		if (node != null) {

			node.setParent(null);
		}

		return node;
	}

	@Override
	public TreeNode remove(Object key) {
		
		if (key instanceof TreeNode) {
			
			return remove((TreeNode) key);
		}

		throw new RuntimeException("Wrong key type");
	}
	
	public TreeNode getParent() {

		return parent;
	}

	@Override
	public void setParent(TreeNode parent) {

		this.parent = parent;

		if (parent == null) {

			parentNodeKey = null;

		} else {

			parentNodeKey = parent.getKey();
		}
	}
	
	@Override
	public String getParentNodeKey() {

		return parentNodeKey;
	}
	
	@Override
	public String getKey() {

		return key;
	}

	public void setKey(String key) {

		this.key = key;
	}

	@Override
	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	@Override
	public boolean isFolder() {

		return folder;
	}

	public void setFolder(boolean folder) {

		this.folder = folder;
	}

	@Override
	public String getIcon() {

		return icon;
	}

	public void setIcon(String icon) {

		this.icon = icon;
	}

	@Override
	public int compareTo(TreeNode o) {

		if (isFolder() == o.isFolder()) {

			return name.compareToIgnoreCase(o.getName());

		} else {

			return isFolder() ? -1 : 1;
		}
	}
}
