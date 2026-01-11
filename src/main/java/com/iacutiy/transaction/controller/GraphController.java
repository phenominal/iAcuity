package com.iacutiy.transaction.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.iacutiy.transaction.model.GraphNode;
import com.iacutiy.transaction.service.GraphService;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

	private final GraphService service;

	public GraphController(GraphService service) {
		this.service = service;
	}

	// Explore single node
	@GetMapping("/nodes/{id}")
	public ResponseEntity<?> getNode(@PathVariable String id) {
		try {
			return ResponseEntity.ok(service.explore(id));
		} catch (RuntimeException e) {
			return ResponseEntity.status(404)
					.body(Map.of("error", e.getMessage(), "message", "Graph node " + id + " does not exist"));
		}
	}

	// Depth-limited children tree
	@GetMapping("/nodes/{id}/tree")
	public ResponseEntity<?> getTree(@PathVariable String id, @RequestParam int depth) {

		if (depth < 1) {
			return ResponseEntity.badRequest().body(Map.of("error", "INVALID_DEPTH", "message", "Depth must be >= 1"));
		}

		try {
			List<GraphNode> tree = service.getChildrenTree(id, depth);
			return ResponseEntity.ok(tree);
		} catch (RuntimeException e) {
			return ResponseEntity.status(404)
					.body(Map.of("error", e.getMessage(), "message", "Graph node " + id + " does not exist"));
		}
	}

	@GetMapping("/nodes/{id}/children-transactions")
	public ResponseEntity<?> getFilteredChildTransactions(@PathVariable String id,
			@RequestParam(required = false) Double minAmount, @RequestParam(required = false) Double maxAmount,
			@RequestParam(required = false) String txnType) {

		try {
			return ResponseEntity.ok(service.filteredChildTransactions(id, minAmount, maxAmount, txnType));
		} catch (RuntimeException e) {
			return ResponseEntity.status(404)
					.body(Map.of("error", e.getMessage(), "message", "Graph node " + id + " does not exist"));
		}
	}
}
