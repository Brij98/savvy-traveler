import java.text.DecimalFormat;
import java.util.*;

public class Main {

    private static final Graph graph = new Graph();

    public static void main(String[] args) {

//        if (Objects.equals(args[0], "")){
//            throw (new NullPointerException("No arguments passed"));
//        }
//        String param = args[0].toLowerCase(Locale.ROOT);

        String param = "EXample2".toLowerCase(Locale.ROOT);

        StringBuilder sb = new StringBuilder();

        createGraph(param);

        HighestProbPathProperty pathProperty = null;

        switch (param){
            case "example1": {
                pathProperty = findHighestProbPath(graph, graph.findVertex("A"), graph.findVertex("F"));
            }
            break;
            case "example2":  {
                pathProperty = findHighestProbPath(graph, graph.findVertex("C"), graph.findVertex("A"));
            }
            break;
            case "example3": {
                pathProperty = findHighestProbPath(graph, graph.findVertex("E"), graph.findVertex("C"));
            }
            break;
            default: System.err.println("Invalid argument received");
        }

        pathProperty.getShortestPath().forEach(v -> sb.append(v.getValue()).append(" -> "));

        System.out.println("path: "+ sb.substring(0, sb.length() - 3) + ", probability: " +
                new DecimalFormat("#.####").format(pathProperty.probability));
        System.out.println("Most reliable destination: " + findMostReliableDest(graph).getValue());
    }

    public static void createGraph(String arg){
        Graph.Vertex a = new Graph.Vertex("A");
        graph.addVertex(a);
        Graph.Vertex b = new Graph.Vertex("B");
        graph.addVertex(b);
        Graph.Vertex c = new Graph.Vertex("C");
        graph.addVertex(c);
        Graph.Vertex d = new Graph.Vertex("D");
        graph.addVertex(d);
        Graph.Vertex e = new Graph.Vertex("E");
        graph.addVertex(e);
        Graph.Vertex f = new Graph.Vertex("F");
        graph.addVertex(f);
        Graph.Vertex g = new Graph.Vertex("G");
        graph.addVertex(g);
        Graph.Vertex h = new Graph.Vertex("H");
        graph.addVertex(h);



        switch (arg){
            case "example1":{
                // EXAMPLE 1 GRAPH
                graph.addEdge(a,b,0.8);
                graph.addEdge(a,c,0.7);
                graph.addEdge(a,d,0.9);
                graph.addEdge(b,c,0.8);
                graph.addEdge(b,f,0.6);
                graph.addEdge(b,e,0.6);
                graph.addEdge(c,f,0.9);
                graph.addEdge(d,g,0.8);
                graph.addEdge(d,f,0.6);
                graph.addEdge(e,f,0.8);
                graph.addEdge(e,h,0.6);
                graph.addEdge(f,g,0.7);
                graph.addEdge(f,h,0.7);
                graph.addEdge(g,h,0.9);
            }break;
            case "example2":{
//                 EXAMPLE 2 GRAPH
                graph.addEdge(a,b,0.8);
                graph.addEdge(a,d,0.9);
                graph.addEdge(b,c,0.6);
                graph.addEdge(b,e,0.7);
                graph.addEdge(c,f,0.8);
                graph.addEdge(d,e,0.8);
                graph.addEdge(d,g,0.8);
                graph.addEdge(e,f,0.9);
                graph.addEdge(e,h,0.6);
                graph.addEdge(g,h,0.9);
            }break;
            case "example3": {
                // EXAMPLE 3 GRAPH
                graph.addEdge(a,b,0.8);
                graph.addEdge(a,d,0.9);
                graph.addEdge(a,e,0.8);
                graph.addEdge(b,c,0.6);
                graph.addEdge(b,f,0.7);
                graph.addEdge(c,d,0.9);
                graph.addEdge(c,g,0.6);
                graph.addEdge(d,h,0.7);
                graph.addEdge(e,f,0.6);
                graph.addEdge(e,h,0.8);
                graph.addEdge(f,g,0.9);
                graph.addEdge(h,g,0.6);
            }break;
        }
    }

    public static HighestProbPathProperty findHighestProbPath(Graph graph, Graph.Vertex startVertex,
                                                              Graph.Vertex endVertex){
        if (graph == null){
            throw (new NullPointerException("Graph is null"));
        }

        if (startVertex == null || endVertex == null){
            throw (new NullPointerException("Start Vertex and/or End Vertex is/are null"));
        }

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
        HighestProbPathProperty pathProperty = new HighestProbPathProperty(shortestPath, endVertex.getWeight());
        graph.getVertices().forEach(v -> { v.setPrevVertex(null); v.setWeight(0.0); }); // resetting graph
        return pathProperty;
    }

    public static Graph.Vertex findMostReliableDest(Graph graph){

        if (graph == null){
            throw (new NullPointerException("Graph is null"));
        }
        List<Graph.Vertex> vertices = graph.getVertices();

        double max  = 0.0;
        Graph.Vertex maxVertex = null;
        for (Graph.Vertex fromVertex : vertices){
            double sum = 0.0;
            for (Graph.Vertex toVertex : vertices) {
                sum += findHighestProbPath(graph, fromVertex, toVertex).getProbability();
            }
            sum--;
            if (sum > max){
                maxVertex = fromVertex;
                max = sum;
            }
        }
        return maxVertex;
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

    }
}