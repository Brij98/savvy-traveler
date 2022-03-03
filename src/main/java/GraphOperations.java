import java.util.*;

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
