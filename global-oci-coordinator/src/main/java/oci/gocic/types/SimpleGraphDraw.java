package oci.gocic.types;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import javax.swing.JFrame;

import com.google.common.base.Supplier;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A class that shows the minimal work necessary to load and visualize a graph.
 */
public class SimpleGraphDraw 
{

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws IOException 
	{
		JFrame jf = new JFrame();
		//		Graph g = getGraph();

		// Add some vertices. From above we defined these to be type Integer.
		LocalCoordinator v1 = new LocalCoordinator(1, InetAddress.getByName("127.0.0.1"), "DE");
		LocalCoordinator v2 = new LocalCoordinator(2, InetAddress.getByName("127.0.0.2"), "US");
		LocalCoordinator v3 = new LocalCoordinator(3, InetAddress.getByName("127.0.0.3"), "JP");
		LocalCoordinator v4 = new LocalCoordinator(4, InetAddress.getByName("127.0.0.4"), "DE");
		LocalCoordinator v5 = new LocalCoordinator(5, InetAddress.getByName("127.0.0.5"), "AU");

		Link l1 = new Link(1,100);
		Link l2 = new Link(1,100);
		Link l3 = new Link(5,100);
		Link l4 = new Link(2,100);
		Link l5 = new Link(3,100);
		Link l6 = new Link(5,100);
		Link l7 = new Link(1,100);

		// Graph<V, E> where V is the type of the vertices 
		// and E is the type of the edges
		Graph<LocalCoordinator, Link> g = new SparseMultigraph<LocalCoordinator, Link>();
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3); 
		g.addVertex(v4);
		g.addVertex(v5);
		g.addEdge(l1, v1,v2);
		g.addEdge(l2, v2,v3);
		g.addEdge(l3, v1,v4);
		g.addEdge(l4, v2,v4);
		g.addEdge(l5, v2,v5);
		g.addEdge(l6, v3,v5);
		g.addEdge(l7, v4,v5);

		System.out.println("The graph g = " + g.toString());  

		calcUnweightedShortestPath(g, v1, v5);
//		calcWeightedShortestPath(g, v1, v5);      

		// Graphical Visualization

		VisualizationViewer vv = new VisualizationViewer(new FRLayout(g));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		jf.getContentPane().add(vv);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}

	public static void calcUnweightedShortestPath(Graph<LocalCoordinator, Link> g, LocalCoordinator source, LocalCoordinator sink) {

		DijkstraShortestPath<LocalCoordinator, Link> alg = new DijkstraShortestPath<LocalCoordinator, Link>(g);
		List<Link> l = alg.getPath(source, sink);

		System.out.println("Shortest unweighted path from " + source + " to " + sink
				+ " is:" + l.toString());
	}

//	public static void calcWeightedShortestPath(Graph<LocalCoordinator, Link> g, LocalCoordinator source, LocalCoordinator sink) {
//
//		Transformer<Link, Double> wtTransformer = new Transformer<Link, Double>() {
//			public Double transform(Link link) {
//				return link.weight;
//			}
//		};
//		DijkstraShortestPath<LocalCoordinator, Link> alg = new DijkstraShortestPath(g, wtTransformer);
//
//		List<Link> l = alg.getPath(source, sink);
//		Number dist = alg.getDistance(source, sink);
//
//		System.out.println("The shortest weighted path from " + source + " to "
//				+ sink + " is:");
//		System.out.println(l.toString());
//		System.out.println("and the length of the path is: " + dist);
//	}

	/**
	 * Generates a graph: in this case, reads it from the file
	 * "samples/datasetsgraph/simple.net"
	 * @return A sample undirected graph
	 * @throws IOException if there is an error in reading the file
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Graph getGraph() throws IOException 
	{
		PajekNetReader pnr = new PajekNetReader(new Supplier(){
			public Object get() {
				return new Object();
			}});
		Graph g = new UndirectedSparseGraph();

		pnr.load("src/main/java/oci/gocic/simple.net", g);
		return g;
	}
}
