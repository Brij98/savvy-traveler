import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public List<Vertex> getVertices(){ return vertices; }
    public List<Edge> getEdges(){ return edges; }


    public Vertex createVertex(String value){
        Vertex vertex = new Vertex(value);
        vertices.add(vertex);
        return vertex;
    }

    public void addVertex(Vertex vertex){
        if (vertex == null){
            throw (new NullPointerException("Vertex is null"));
        }
        vertices.add(vertex);
    }
    public Vertex findVertex(String value){
        return vertices.stream().
                filter(vertex -> value.equals(vertex.getValue()))
                .findAny()
                .orElse(null);
    }
    public void addEdge(Vertex vertex1, Vertex vertex2, double cost){

        if (vertex1 == null || vertex2 == null){
            throw new NullPointerException("Invalid vertex1 and vertex2");
        }
        Edge edge1 = new Edge(cost, vertex1, vertex2);
        Edge edge2 = new Edge(cost, vertex2, vertex1);

        if (! edges.contains(edge1)){
            vertex1.getEdges().add(edge1);
        }
        if (! edges.contains(edge2)){
            vertex2.getEdges().add(edge2);
        }
    }

    public static class Vertex {
        private String value = "";
        private double weight = 0.0;
        private List<Edge> edges = new ArrayList<>();
        private Vertex prevVertex = null; // stores previous vertex for keeping track of the path

        public Vertex(String value){ this.value = value; }

        public List<Edge> getEdges() { return edges; }

        public String getValue() {
            return value;
        }

        public Vertex getPrevVertex() {
            return prevVertex;
        }

        public void setPrevVertex(Vertex prevVertex) {
            this.prevVertex = prevVertex;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public Edge getEdge(Vertex vertex){ // return the edge which has the 'to' vertex
            for (Edge edge : edges) {
                if (edge.toVertex.equals(vertex)){ return edge; }
            }
            return null;
        }
    }

    public static class Edge{
        private double cost = 0.0;
        private Vertex toVertex = null;
        private Vertex fromVertex = null;

        public Edge(double cost, Vertex from, Vertex to) {
            if (to == null || from == null){
                throw (new NullPointerException("'to' vertex and 'from' cannot be null."));
            }
            this.cost = cost;
            this.toVertex = to;
            this.fromVertex = from;
        }

        public double getCost() { return cost; }

        public void setCost(double cost) { this.cost = cost; }

        public Vertex getToVertex() { return toVertex; }

        public Vertex getFromVertex() { return fromVertex; }
    }
}
