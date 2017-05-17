package roadgraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import geography.GeographicPoint;

public class MapNode {
	private GeographicPoint location;//Location of the vertex
	private HashSet<MapEdge> edges;//List of edges connected to the vertices

	//Priority Distance of node for Dijsktra 
	private double priorityDistance;
	
	//Priority Distance of node for A star
	private double priorityFuncDistance;
	
	//Constructor creates a vertex and initializes list of edges
	public MapNode(GeographicPoint loc){
		this.location = loc;
		edges = new HashSet<MapEdge>();
	}
	
	//Calls MapEdge class function to add edge to the vertex
	public void addMapEdge(MapEdge edge){
		edges.add(edge);
	}
	
	//Returns number of edges for a vertex
	public int getNumEdges(){
		return edges.size();
	}
	
	//Returns neighboring vertices of the specified vertex
	public Set<MapNode> getNeighbors(){
		Set<MapNode> neighbors = new HashSet<MapNode>();
		for (MapEdge edge : edges) {
			neighbors.add(edge.getOtherNode(this));
		}
		return neighbors;
	}
	
	/**
	 * Get the geographic location that this node represents
	 * @return the geographic location of this node
	 */
	GeographicPoint getLocation()
	{
		return location;
	}
	
	/**
	 * return the edges out of this node
	 * @return a set containing all the edges out of this node.
	 */
	Set<MapEdge> getEdges()
	{
		return edges;
	}
	
	/** Returns whether two nodes are equal.
	 * Nodes are considered equal if their locations are the same, 
	 * even if their street list is different.
	 * @param o the node to compare to
	 * @return true if these nodes are at the same location, false otherwise
	 */
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof MapNode) || (o == null)) {
			return false;
		}
		MapNode node = (MapNode)o;
		return node.location.equals(this.location);
	}
	
	/** Because we compare nodes using their location, we also 
	 * may use their location for HashCode.
	 * @return The HashCode for this node, which is the HashCode for the 
	 * underlying point
	 */
	@Override
	public int hashCode()
	{
		return location.hashCode();
	}
	
	/** ToString to print out a MapNode object
	 *  @return the string representation of a MapNode
	 */
	@Override
	public String toString()
	{
		String toReturn = "[NODE at location (" + location + ")";
		toReturn += " intersects streets: ";
		for (MapEdge e: edges) {
			toReturn += e.getRoadName() + ", ";
		}
		toReturn += "]";
		return toReturn;
	}
	
	// For debugging, output roadNames as a String.
	public String roadNamesAsString()
	{
		String toReturn = "(";
		for (MapEdge e: edges) {
			toReturn += e.getRoadName() + ", ";
		}
		toReturn += ")";
		return toReturn;
	}

	// get node priority for Dijsktra or A star
	public double getPriorityDistance() {
		return this.priorityDistance;
	}
	
	// set node priority for Dijsktra or A star
	public void setPriorityDistance(double priorityDistance) {
	    this.priorityDistance = priorityDistance;
	}

	// get node priority for A star
	public double getPriorityFuncDistance() {
		return this.priorityFuncDistance;
	}
	
	// set node priority for A star
	public void setPriorityFuncDistance(double priorityFuncDistance) {
	    this.priorityFuncDistance = priorityFuncDistance;
	}

}
