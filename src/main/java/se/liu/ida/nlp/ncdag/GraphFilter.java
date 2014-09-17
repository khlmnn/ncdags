/*
 * See the file "LICENSE" for the full license governing this code.
 */
package se.liu.ida.nlp.ncdag;

/**
 *
 * @author Marco Kuhlmann <marco.kuhlmann@liu.se>
 */
public class GraphFilter {

	private final boolean[][] hasEdge;

	public GraphFilter(Graph graph) {
		int nNodes = graph.getNNodes();
		this.hasEdge = new boolean[nNodes][nNodes];
		for (Edge edge : graph.getEdges()) {
			hasEdge[edge.source][edge.target] = true;
		}
	}

	public boolean isAdmissible(int src, int tgt) {
		return hasEdge[src][tgt];
	}
}
