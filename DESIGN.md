## Architecture

Controller → Service → DataLoader → In-memory Graph Store

### Components

- **DataLoader**
  Loads JSON into memory and builds:
  - nodeById map
  - childrenByParent map

- **GraphService**
  Core logic:
  - Computes node level
  - Finds parent chain
  - Finds children
  - Detects cycles
  - Depth-Limited Children Tree

- **GraphController**
  Exposes REST endpoints


## Safety
- Cycle detection using visited set
- Null-safe JSON loading
- Graph integrity preserved

## Scalability
- Data in memory for fast access
- Can later migrate to DB-backed storage
