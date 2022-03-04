import java.util.*;


/* Class GraphOperations is the class that contains a Graph object as instance variable and algorithms that find shortest path & most reliable destination.
    It has instance varible private final Graph graph that represents the graph it will work on.

    It contains 4 member functions
    Modifier:
    public GraphOperations(Graph graph);

    public static class VertexComparator implements Comparator<Graph.Vertex>;    Overrides java default compare method. This function will compare the weight (biggest probability edge value) of two vertexes and return 1 if the second vertex is larger than the first vertex. -1 if the first one has greater weight than the second, and 0 if they have equal weight.

    public void resetGraphVertices();    that resets the graph value to default (0 weight and null vertexes).

    HighestProbPathProperty findHighestProbPath (Graph.Vertex startVertex, Graph.Vertex endVertex)    that finds the path with highest probability. The algorithm is to traverse the graph until target vertex is reached. In the process, it will always choose the edge with the highest probability to travel. In case of same probability, it will check a priority queue that stores edges’ largest probability edge and break the tie. The priority queue contains all vertexes and sorts them in no-increasing order of largest edge probability value.

    public Graph.Vertex findMostReliableDest();    is the function that finds the most reliable destination. It is called findHighestProbPath and calculates the reliable destination value for all vertices and returns the vertex that has highest reliable vertex value.
*/

public class GraphOperations {

    private final Graph graph;

    public GraphOperations(Graph graph) {
        this.graph = graph;
    }

    public HighestProbPathProperty findHighestProbPath(Graph.Vertex startVertex,
                                                                   Graph.Vertex endVertex){

        if (startVertex == null || endVertex == null){
            throw (new NullPointerException("Start Vertex and/or End Vertex is/are null"));
        }
        resetGraphVertices();
        List<Graph.Vertex> visitedVertices = new ArrayList<>(); // keeps track of the vertices visited
        List<Graph.Vertex> shortestPath = new ArrayList<>(); // stores the shortest path
        // Priority queue keeps track of the probability of vertices in non-increasing order
        Queue<Graph.Vertex> verticesPQ = new PriorityQueue<>(graph.getVertices().size(), new VertexComparator());

        startVertex.setWeight(1.0);

        verticesPQ.add(startVertex);
        visitedVertices.add(startVertex);

        while(!verticesPQ.isEmpty()){
            Graph.Vertex currVertex = verticesPQ.poll();
            // iterate over all the adjacent vertices of the current vertex
            for(Graph.Edge edge : currVertex.getEdges()){
                Graph.Vertex adjacentVertex = edge.getToVertex();

                if (!visitedVertices.contains(adjacentVertex)){
                    double newWeight = currVertex.getWeight() * edge.getCost();

                    if (newWeight > adjacentVertex.getWeight()){
                        verticesPQ.remove(adjacentVertex);
                        adjacentVertex.setWeight(newWeight);
                        adjacentVertex.setPrevVertex(currVertex);
                        verticesPQ.add(adjacentVertex);
                    }
                }   // end of if
            }   // end of for
            visitedVertices.add(currVertex);
        }   // end of while

        // tracking back to the start vertex from end vertex
        for (Graph.Vertex vertex = endVertex; vertex != null; vertex = vertex.getPrevVertex()){
            shortestPath.add(vertex);
            if (vertex.equals(startVertex)){ break; }
        }
        Collections.reverse(shortestPath);
        return new HighestProbPathProperty(shortestPath, endVertex.getWeight());
    }

    public Graph.Vertex findMostReliableDest(){
        List<Graph.Vertex> vertices = graph.getVertices();

        double max  = 0.0;
        Graph.Vertex maxVertex = null;
        resetGraphVertices();
        for (Graph.Vertex fromVertex : vertices){
            HighestProbPathProperty prop  = findHighestProbPath(fromVertex, fromVertex);
            double sum = 0.0;
            for (Graph.Vertex toVertex : vertices) {
                sum += toVertex.getWeight();
            }
            sum--;
            if (sum > max){
                maxVertex = fromVertex;
                max = sum;
            }
            resetGraphVertices();
        }
        return maxVertex;
    }

    public void resetGraphVertices(){
        graph.getVertices().forEach(v -> { v.setPrevVertex(null); v.setWeight(0.0); });
    }

    public static class VertexComparator implements Comparator<Graph.Vertex>{
        /*
         defines how two vertices can be compared.
         @param: o1 - first vertex
                 o2 - second vertex
        @returns: integer
        */
        @Override
        public int compare(Graph.Vertex o1, Graph.Vertex o2) {
            if (o1.getWeight() < o2.getWeight()){
                return 1;
            }else if(o1.getWeight() > o2.getWeight()){
                return -1;
            }
            return 0;
        }
    }

/* Class HighestProbPathProperty is the class that is used to help recording path with highest property.
    It contains two instance variable
    private final List<Graph.Vertex> shortestPath    that represents the vertex with highest probability path.
    private double probability    represents its probality.

    It has 4 member functions
    Accessors:
    public List<Graph.Vertex> getShortestPath() ;
    public double getProbability();

    Modifier:
    public HighestProbPathProperty(List<Graph.Vertex> path, double probability);

    Member function
    public String toString();    This member function returns the vertex in this class.
*/


    public static class HighestProbPathProperty{
        private final List<Graph.Vertex> shortestPath;
        private double probability = 0.0;

        public HighestProbPathProperty(List<Graph.Vertex> path, double probability){
            this.shortestPath = path;
            this.probability = probability;
        }

        public List<Graph.Vertex> getShortestPath() {
            return shortestPath;
        }

        public double getProbability() {
            return probability;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            shortestPath.forEach(v -> sb.append(v.getValue()).append("-"));
            return sb.substring(0, sb.length() - 1);
        }
    }
}
