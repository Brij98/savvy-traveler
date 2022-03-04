import java.util.ArrayList;
import java.util.List;

/*Class graph is the class we will use to represent undirected weighted graphs.
    It contains two private lists that contain vertex (List<vertex> vertices) and edges (List<Edge> edges).
    It contains

    Accessors:
    List<Vertex> getVertices()
    List<Edge> getEdges()
    Modifiers:
    void addVertex(Vertex vertex)
    void addEdge(Vertex vertex1, Vertex vertex2, double cost)
    Vertex createVertex(String value)

    Member function
    Vertex findVertex(String value)
        This member function returns a Vertex object that has the same name as input value.
*/

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

/*Class Vertex is the class used to represent a vertex in graph.
    It contains 4 instance variables
    private String value   represents name of that vertex
    private double weight    represents the value of probability to arrive on time from all other cities (it is this value in project instruction: For city A, the probability to arrive on time from all other cities is = 0.8 (from B) + 0.7 (from C) + 0.9 (from D) + 0.504 (from E) + 0.63 (from F) + 0.72 (from G) + 0.648 (from H))
    private List<Edge> edges   represents all the edges this vertex has.
    private Vertex prevVertex    a helper variable that stores previous vertex when we are doing traversal
    Member functions of this class are all modifiers and accessors.
*/

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

/*Class Edge is the class used to represent an edge
    It contains three instance variables
    private Vertex toVertex    represents the first vertex
    private Vertex fromVertex    represents the second vertex
    private double cost    represents the cost of traveling between these two vertexes
    All member functions are constructors, modifiers, and accessors.
*/

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
