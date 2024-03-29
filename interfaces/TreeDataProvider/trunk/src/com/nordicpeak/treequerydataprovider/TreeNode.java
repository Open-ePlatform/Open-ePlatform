package com.nordicpeak.treequerydataprovider;

import java.util.Map;



public interface TreeNode extends Map<String, TreeNode>{

	public String getParentNodeKey();

	public String getKey();

	public String getName();

	public boolean isFolder();

	public String getIcon();
	
	public TreeNode put(TreeNode node);
	
	public TreeNode remove(TreeNode node);
	
	public TreeNode remove(String key);
		
	public void setParent(TreeNode parent);
	
}
