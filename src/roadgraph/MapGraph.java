/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Consumer;
import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {	
	private HashMap<GeographicPoint,MapNode> nodes;//HashMap of vertices/nodes for O(1) retrieval
	private HashSet<MapEdge> edges;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		//New Graph called. Initialize HashMap
		nodes = new HashMap<GeographicPoint,MapNode>();
		edges = new HashSet<MapEdge>();
		
	}
	/*
	public static Comparator<MapNode> idComparator = new Comparator<MapNode>(){
			@Override
			public int compare(MapNode c1, MapNode c2) {
	            return (int) (c1.getId() - c2.getId());
	        }
	};*/

	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return nodes.values().size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		return nodes.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		return edges.size();
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		if(nodes.containsKey(location) || location == null){
			//Return False if node exists in list or node is null
			return false;
		}
		nodes.put(location,new MapNode(location));
		return true;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		if(!nodes.containsKey(from) || !nodes.containsKey(to) || from == null 
				|| to == null || roadType == null || length < 0){
			//Throw IllegalArgumentException if points do not exist/are null/length < 0
			throw new IllegalArgumentException("Something went wrong while adding an Edge");
		}
		else{
			MapEdge edge = new MapEdge(roadName, roadType, nodes.get(from), nodes.get(to), length);
			edges.add(edge);
			nodes.get(from).addMapEdge(edge);
		}
	}
	
	/** 
	 * Get a set of neighbor nodes from a mapNode
	 * @param node  The node to get the neighbors from
	 * @return A set containing the MapNode objects that are the neighbors 
	 * 	of node
	 */
	private Set<MapNode> getNeighbors(MapNode node) {
		return node.getNeighbors();
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		if (start == null || goal == null) {
			//Check if start and goal are non null.
			throw new NullPointerException("Start or goal node is null!  No path exists.");
		}
		MapNode startNode = nodes.get(start);
		MapNode endNode = nodes.get(goal);
		//ParentMap for current and Visited nodes
		HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		
		//Perform BFS
		boolean found = bfsSearch(startNode,endNode,parentMap,nodeSearched);
		
		if (!found) {
			//A path from start to end not found. 
			System.out.println("No path exists from"+start+"to"+goal);
			return null;
		}
		
		//Path found. Recreate path and return
		return constructPath(startNode, endNode, parentMap);		
	}
	
	/**BFS function to check if path exists
	 * @param start The starting location
	 * @param goal The goal location
	 * @param Parent Map to keep track of vertices for path
	 * @return if path exists or not 
	 */
	private boolean bfsSearch(MapNode start,MapNode goal,HashMap<MapNode, MapNode> parentMap,Consumer<GeographicPoint> nodeSearched){
		Queue<MapNode> toExplore = new LinkedList<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		toExplore.add(start);
		
		while (!toExplore.isEmpty()) {
			//Continue loop while a neighbor node exists to explore
			MapNode curr = toExplore.remove();
			
			// hook for visualization
			nodeSearched.accept(curr.getLocation());
			
			if (curr.equals(goal)) {
				//Reached goal
				return true;
			}
			Set<MapNode> neighbors = getNeighbors(curr);
			for (MapNode neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					parentMap.put(neighbor, curr);
					toExplore.add(neighbor);
				}
			}
		}

		return false;//path not found
	}
	
	/** Reconstruct a path from start to goal using the parentMap
	 * @param parentMap the HashNode map of children and their parents
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from
	 *   start to goal (including both start and goal).
	 */
	private List<GeographicPoint> constructPath(MapNode start, MapNode goal,
			HashMap<MapNode, MapNode> parentMap)
	{
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();//Path to return
		MapNode curr = goal;//Start constructing from goal to start
		while (curr != start) {
			//While start vertex not reached
			path.addFirst(curr.getLocation());//Add vertex to list
			curr = parentMap.get(curr);//Get next vertex in path
		}
		path.addFirst(start.getLocation());//Add last path
		return path;
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		if (start == null || goal == null) {
			//Check if start and goal are non null.
			System.out.println("Start or goal node is null!  No path exists.");
			return null;
		}
		
		//ParentMap for current and Visited nodes
		HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		MapNode startNode = nodes.get(start);
		MapNode endNode = nodes.get(goal);
		
		//Perform Dijsktra Search
		boolean found = dijsktraSearch(startNode, endNode,parentMap, nodeSearched);
		
		if (!found) {
			//A path from start to end not found. 
			System.out.println("No path exists");
			return null;
		}
		
		//Path found. Recreate path and return
		return constructPath(startNode, endNode, parentMap);		
	}
	
	//Initializing all Distances to infinity
	private void initializeDistances() {
		for (MapNode m : nodes.values()) {
			m.setPriorityDistance(Double.MAX_VALUE);
			m.setPriorityFuncDistance(Double.MAX_VALUE);
		}
	}
	
	//Gets edges with their distances
	private HashMap<MapNode, Double> getEdgeDistance(MapNode curr) {
		HashMap<MapNode, Double> distancesMap = new HashMap<>();
		for (MapEdge ed : curr.getEdges()) {
			distancesMap.put(ed.getEndNode(), ed.getRoadDuration());
		}
		return distancesMap;
	}
	
	public static Comparator<MapNode> dijsktraComparator = new Comparator<MapNode>(){
		
		@Override
		public int compare(MapNode n1, MapNode n2) {
			if (n1.getPriorityDistance() < n2.getPriorityDistance())
	        {
	            return -1;
	        }
	        if (n1.getPriorityDistance() > n2.getPriorityDistance())
	        {
	            return 1;
	        }
	        return 0;
        }
	};
	
	private boolean dijsktraSearch(MapNode start,MapNode goal, HashMap<MapNode, MapNode> parentMap,
			Consumer<GeographicPoint> nodeSearched) {
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>(getNumVertices(),dijsktraComparator);
		HashSet<MapNode> visited = new HashSet<MapNode>();
		int count  = 0;
		initializeDistances();

		//Setting visited node to 0
		start.setPriorityDistance(0.0);

		toExplore.add(start);
		
		while (!toExplore.isEmpty()) {
			//Continue loop while a neighbor node exists to explore
			MapNode curr = toExplore.remove();
			count++;
			
			// hook for visualization
			nodeSearched.accept(curr.getLocation());
			
			if (curr.equals(goal)) {
				//Reached goal
				System.out.println("Dijsktra visited:"+count);
				return true;
			}
			HashMap<MapNode, Double> distancesMap = getEdgeDistance(curr);
			Set<MapNode> neighbors = getNeighbors(curr);
			for (MapNode neighbor : neighbors) {
				if (!visited.contains(neighbor)) {	
					
					visited.add(curr);
					double priority = curr.getPriorityDistance() + distancesMap.get(neighbor);
					//Change priority only if lower than set
					if (priority < neighbor.getPriorityDistance()) {
						neighbor.setPriorityDistance(priority);
						parentMap.put(neighbor, curr);
						toExplore.offer(neighbor);
					}
				}
			}
		}
		return false;
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		if (start == null || goal == null) {
			//Check if start and goal are non null.
			System.out.println("Start or goal node is null!  No path exists.");
			return null;
		}
		
		//ParentMap for current and Visited nodes
		HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		MapNode startNode = nodes.get(start);
		MapNode endNode = nodes.get(goal);
		
		//Perform Dijsktra Search
		boolean found = aStar(startNode, endNode,parentMap, nodeSearched);
		
		if (!found) {
			//A path from start to end not found. 
			System.out.println("No path exists");
			return null;
		}
		
		//Path found. Recreate path and return
		return constructPath(startNode, endNode, parentMap);		
	}

	//Get straightline distance to goal
	private double getStraightLineDistance(MapNode curr,MapNode goal){
		return (curr.getLocation().distance(goal.getLocation())/20);
	}
	
	//Comparator for aStar
	public static Comparator<MapNode> aStarComparator = new Comparator<MapNode>(){
		
		@Override
		public int compare(MapNode n1, MapNode n2) {
			if (n1.getPriorityFuncDistance() < n2.getPriorityFuncDistance())
	        {
	            return -1;
	        }
	        if (n1.getPriorityFuncDistance() > n2.getPriorityFuncDistance())
	        {
	            return 1;
	        }
	        return 0;
        }
	};
	
	
	//aStar Search method
	private boolean aStar(MapNode start,MapNode goal, HashMap<MapNode, MapNode> parentMap,
			Consumer<GeographicPoint> nodeSearched) {
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>(getNumVertices(),aStarComparator);
		HashSet<MapNode> visited = new HashSet<MapNode>();

		initializeDistances();
		int count = 0;
		//Setting visited node to 0
		start.setPriorityFuncDistance(0.0);
		start.setPriorityDistance(0.0);

		toExplore.add(start);
		
		while (!toExplore.isEmpty()) {
			//Continue loop while a neighbor node exists to explore
			MapNode curr = toExplore.remove();
						
			// hook for visualization
			nodeSearched.accept(curr.getLocation());
			
			count++;
						
			if (curr.equals(goal)) {
				System.out.println("aStar visited:"+count);
				//Reached goal
				return true;
			}
			HashMap<MapNode, Double> distancesMap = getEdgeDistance(curr);
			Set<MapNode> neighbors = getNeighbors(curr);
			for (MapNode neighbor : neighbors) {
				if (!visited.contains(neighbor)) {					
					visited.add(curr);					
					double priorityFunc = curr.getPriorityDistance()+ distancesMap.get(neighbor)+ getStraightLineDistance(neighbor,goal);
					double priority = curr.getPriorityDistance()+ distancesMap.get(neighbor);
					//Change priority only if lower than set
					if (priorityFunc < neighbor.getPriorityFuncDistance()) {						
						neighbor.setPriorityFuncDistance(priorityFunc);
						neighbor.setPriorityDistance(priority);
						parentMap.put(neighbor, curr);
						toExplore.offer(neighbor);
					}
				}
			}
		}
		return false;
	}

	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.8648772, -117.2254046);
		testEnd = new GeographicPoint(32.8660691, -117.217393);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		
		/* Use this code in Week 3 End of Week Quiz */
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		
		
	}
}