package com.iacutiy.transaction.model;

import java.util.List;

public class NodeResponse {
    public GraphNode node;
    public int level;
    public boolean isRoot;
    public boolean isLeaf;
    public List<GraphNode> parentChain;
    public List<GraphNode> children;
    public List<NodeTransaction> ownTransactions;
    public List<NodeTransaction> childrenTransactions;
}