package com.iacutiy.transaction.model;

import java.util.List;

public class GraphNode {
    public String id;
    public String parentId;
    public String name;
    public String accountNumber;
    public List<NodeTransaction> transactions;
}