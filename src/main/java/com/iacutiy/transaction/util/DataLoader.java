package com.iacutiy.transaction.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iacutiy.transaction.model.GraphNode;

import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {

	public Map<String, GraphNode> nodeById = new HashMap<>();
	public Map<String, List<GraphNode>> childrenByParent = new HashMap<>();

	@PostConstruct
	public void load() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		InputStream is = getClass().getResourceAsStream("/transactions-graph-nodes.json");
		JsonNode root = mapper.readTree(is).get("nodes");

		for (JsonNode n : root) {
			GraphNode node = mapper.treeToValue(n, GraphNode.class);
			nodeById.put(node.id, node);
			childrenByParent.computeIfAbsent(node.parentId, k -> new ArrayList<>()).add(node);
		}
	}
}
