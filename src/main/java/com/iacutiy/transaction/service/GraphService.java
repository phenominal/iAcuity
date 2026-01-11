package com.iacutiy.transaction.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.iacutiy.transaction.model.GraphNode;
import com.iacutiy.transaction.model.NodeResponse;
import com.iacutiy.transaction.model.NodeTransaction;
import com.iacutiy.transaction.util.DataLoader;

@Service
public class GraphService {

	private final DataLoader loader;

	public GraphService(DataLoader loader) {
		this.loader = loader;
	}

	public NodeResponse explore(String id) {

		GraphNode node = loader.nodeById.get(id);
		if (node == null)
			throw new RuntimeException("NODE_NOT_FOUND");

		Set<String> visited = new HashSet<>();
		List<GraphNode> parentChain = new ArrayList<>();

		GraphNode current = node;
		int level = 0;

		// Build parent chain & level
		while (current.parentId != null && loader.nodeById.containsKey(current.parentId)) {
			if (!visited.add(current.parentId))
				throw new RuntimeException("CYCLE_DETECTED");

			current = loader.nodeById.get(current.parentId);
			parentChain.add(0, current);
			level++;
		}

		// Get children
		List<GraphNode> children = loader.childrenByParent.getOrDefault(id, Collections.emptyList());

		NodeResponse response = new NodeResponse();
		response.node = node;
		response.level = level;
		response.parentChain = parentChain;
		response.children = children;
		response.isRoot = node.parentId == null || !loader.nodeById.containsKey(node.parentId);
		response.isLeaf = children.isEmpty();
		response.ownTransactions = node.transactions;

		response.childrenTransactions = children.stream().flatMap(c -> c.transactions.stream()).toList();

		return response;
	}

	// Depth-Limited Tree Traversal
	public List<GraphNode> getChildrenTree(String nodeId, int depth) {

		if (depth < 0)
			throw new IllegalArgumentException("INVALID_DEPTH");

		Set<String> visited = new HashSet<>();
		return dfs(nodeId, depth, visited);
	}

	private List<GraphNode> dfs(String nodeId, int depth, Set<String> visited) {

		List<GraphNode> result = new ArrayList<>();

		if (depth <= 0) {
			return result;
		}

		if (!visited.add(nodeId)) {
			throw new RuntimeException("CYCLE_DETECTED");
		}

		List<GraphNode> children = loader.childrenByParent.getOrDefault(nodeId, Collections.emptyList());

		for (GraphNode child : children) {
			result.add(child);
			result.addAll(dfs(child.id, depth - 1, visited));
		}

		return result;
	}

	public List<NodeTransaction> filteredChildTransactions(String id, Double min, Double max, String type) {

		List<GraphNode> children = loader.childrenByParent.getOrDefault(id, List.of());

		return children.stream().flatMap(n -> n.transactions.stream()).filter(t -> min == null || t.amount >= min)
				.filter(t -> max == null || t.amount <= max)
				.filter(t -> type == null || t.txnType.equalsIgnoreCase(type)).toList();
	}

}