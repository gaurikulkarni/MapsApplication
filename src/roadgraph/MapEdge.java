package roadgraph;

import geography.GeographicPoint;

//Stores information about the edges connected to the vertex
	public class MapEdge {
		private MapNode start; //Start location for edge
		private MapNode end; //End location for edge
		private String roadName; //Name of the edge
		private String roadType; //Type of the edge
		private double roadDuration; //Duration of the edge
		
		/** The length of the road segment*/
		private double length;
		
		static final double DEFAULT_LENGTH = 0.01;
		
		/** 
		 * Create a new MapEdge object
		 * @param roadName  The name of the road
		 * @param roadType  The type of the road
		 * @param n1 The point at one end of the segment
		 * @param n2 The point at the other end of the segment
		 */
		MapEdge(String roadName, String roadType, MapNode n1, MapNode n2) 
		{
			this(roadName, roadType, n1, n2, DEFAULT_LENGTH);
			calculateDuration();
		}
		
		/** 
		 * Create a new MapEdge object
		 * @param roadName  The name of the road
		 * @param roadType  The type of the road
		 * @param n1 The point at one end of the segment
		 * @param n2 The point at the other end of the segment
		 * @param length The length of the road segment
		 */	
		MapEdge(String roadName, String roadType,
				MapNode n1, MapNode n2, double length) 
		{
			this.roadName = roadName;
			start = n1;
			end = n2;
			this.roadType = roadType;
			this.length = length;
			calculateDuration();
		}
		
		/**
		 * return the MapNode for the end point
		 * @return the MapNode for the end point
		 */
		MapNode getEndNode() {
		   return end;
		}
		
		/**
		 * Return the location of the start point
		 * @return The location of the start point as a GeographicPoint
		 */
		GeographicPoint getStartPoint()
		{
			return start.getLocation();
		}
		
		/**
		 * Return the location of the end point
		 * @return The location of the end point as a GeographicPoint
		 */
		GeographicPoint getEndPoint()
		{
			return end.getLocation();
		}
		
		/**
		 * Return the length of this road segment
		 * @return the length of the road segment
		 */
		double getLength()
		{
			return length;
		}
		
		/**
		 * Get the road's name
		 * @return the name of the road that this edge is on
		 */
		public String getRoadName()
		{
			return roadName;
		}
		
		/**
		 * Get the road's type
		 * @return the type of the road that this edge is on
		 */
		public String getRoadType()
		{
			return roadType;
		}

		/**
		 * Given one of the nodes involved in this edge, get the other one
		 * @param node The node on one side of this edge
		 * @return the other node involved in this edge
		 */
		MapNode getOtherNode(MapNode node)
		{
			if (node.equals(start)) 
				return end;
			else if (node.equals(end))
				return start;
			throw new IllegalArgumentException("Looking for " +
				"a point that is not in the edge");
		}
		
		/**
		 * Return a String representation for this edge.
		 */
		@Override
		public String toString()
		{
			String toReturn = "[EDGE between ";
			toReturn += "\n\t" + start.getLocation();
			toReturn += "\n\t" + end.getLocation();
			toReturn += "\nRoad name: " + roadName + " Road type: " + roadType +
					" Segment length: " + String.format("%.3g", length) + "km";
			
			return toReturn;
		}
		
		/**
		 * Get the road's duration (time = distance/speed)
		 * @return the type of the road that this edge is on
		 */
		public double getRoadDuration()
		{
			return roadDuration;
		}
		
		private void calculateDuration(){
			switch(roadType){
			case "motorway": //Assume speed limit 70mph
				roadDuration = length/70;
			break;
			case "trunk": //Assume speed limit 60mph
				roadDuration = length/60;
			break;
			case "primary": //Assume speed limit 55mph
				roadDuration = length/55;
			break;
			case "secondary": //Assume speed limit 50mph
				roadDuration = length/50;
			break;
			case "tertiary": //Assume speed limit 45mph
				roadDuration = length/45;
			break;
			case "unclassified": //Assume speed limit 30mph
			case "trunk_link": //Assume speed limit 30mph
			case "primary_link": //Assume speed limit 30mph
				roadDuration = length/30;
			break;
			case "residential": //Assume speed limit 25mph
				roadDuration = length/25;
			break;
			case "service": //Assume speed limit 20mph
			case "secondary_link": //Assume speed limit 20mph
				roadDuration = length/20;
			break;
			case "motorway_link": //Assume speed limit 35mph
				roadDuration = length/35;
			break;
			case "tertiary_link": //Assume speed limit 15mph
			case "living_street": //Assume speed limit 15mph
			case "pedestrian": //Assume speed limit 15mph
			case "track": //Assume speed limit 15mph
				roadDuration = length/15;
			break;			
			default: //Default speed limit
				roadDuration = length/20;
			}
		}
	}