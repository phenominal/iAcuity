# Transaction Graph Node Explorer

This project is a Spring Boot application that loads a transaction graph from JSON
and exposes REST APIs to explore the graph.

## Features
- Load transaction graph from JSON at startup
- Query any node by ID
- Compute node level
- Retrieve parent chain
- Retrieve direct children
- Collect own and children transactions
- Detect cycles in graph
- Supports Depth-Limited Children Tree
- Filtered Children Transactions

## Tech Stack
- Java 17
- Spring Boot 3.5.9
- Jackson
- REST API

## Run Project

1. Place `transactions-graph-nodes.json` inside:
   `src/main/resources`

2. Build and run:
   ```bash
   mvn clean install spring-boot:run
