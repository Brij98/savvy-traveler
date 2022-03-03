import java.text.DecimalFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {

//        if (Objects.equals(args[0], "")){
//            throw (new NullPointerException("No arguments passed"));
//        }
//        String param = args[0].toLowerCase(Locale.ROOT);

        String param = "EXample3".toLowerCase(Locale.ROOT);

        Graph graph = new Graph();
        addVerticesAndEdges(graph, param);
        GraphOperations graphOperations = new GraphOperations(graph);
        GraphOperations.HighestProbPathProperty pathProperty = null;

        switch (param){
            case "example1": {
                pathProperty = graphOperations.findHighestProbPath(graph.findVertex("A"),
                        graph.findVertex("F"));
            }
            break;
            case "example2":  {
//                pathProperty = findHighestProbPath(graph.findVertex("C"), graph.findVertex("A"));
                pathProperty = graphOperations.findHighestProbPath(graph.findVertex("C"),
                        graph.findVertex("A"));
            }
            break;
            case "example3": {
                pathProperty = graphOperations.findHighestProbPath(graph.findVertex("E"),
                        graph.findVertex("C"));
            }
            break;
            default: System.err.println("Invalid argument received");
        }

        System.out.println(
                "path " + pathProperty.toString() +
                        ", probability " + new DecimalFormat("#.####").format(pathProperty.getProbability()) +
                        ", and city " + graphOperations.findMostReliableDest().getValue()
        );
    }

    public static void addVerticesAndEdges(Graph graph, String arg){
        Graph.Vertex a = graph.createVertex("A");
        Graph.Vertex b = graph.createVertex("B");
        Graph.Vertex c = graph.createVertex("C");
        Graph.Vertex d = graph.createVertex("D");
        Graph.Vertex e = graph.createVertex("E");
        Graph.Vertex f = graph.createVertex("F");
        Graph.Vertex g = graph.createVertex("G");
        Graph.Vertex h = graph.createVertex("H");

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
        }   // end of switch
    }
}