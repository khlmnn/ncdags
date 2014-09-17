/*
 * See the file "LICENSE" for the full license governing this code.
 */
package se.liu.ida.nlp.ncdag;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Count the number of noncrossing acyclic digraphs. A <em>noncrossing
 * graph</em> on <em>n</em> vertices is a graph drawn on <em>n</em> points
 * numbered from 1 to <em>n</em> in counter-clockwise order on a circle such
 * that the edges lie entirely within the circle and do not cross each other.
 *
 * @author Marco Kuhlmann <marco.kuhlmann@liu.se>
 */
public class CoverageCounter {

	private static CoverageCounter instance = null;

	private CoverageCounter() {
		// Private constructor to prevent instantiation.
	}

	public static CoverageCounter getInstance() {
		if (instance == null) {
			instance = new CoverageCounter();
		}
		return instance;
	}

	public int getCoverage(Graph graph) {
		GraphFilter filter = new GraphFilter(graph);

		int nNodes = graph.getNNodes();

		IntChart nMinMaxCovered = new IntChart(nNodes);
		IntChart nMaxMinCovered = new IntChart(nNodes);
		IntChart nMinMaxConnected = new IntChart(nNodes);
		IntChart nMaxMinConnected = new IntChart(nNodes);
		IntChart nMixConnected = new IntChart(nNodes);
		IntChart nUnconnected = new IntChart(nNodes);

		for (int max = 0; max < nNodes; max++) {
			for (int min = max - 1; min >= 0; min--) {
				// Concatenate two edge-covered graphs.

				// Rule 01
				for (int mid = min + 1; mid < max; mid++) {
					int nCovered1 = nMinMaxCovered.get(min, mid);
					int nCovered2 = nMinMaxCovered.get(mid, max);
					update(nMinMaxConnected, min, max, nCovered1, nCovered2);
				}

				// Rule 02
				for (int mid = min + 1; mid < max; mid++) {
					int nCovered1 = nMaxMinCovered.get(min, mid);
					int nCovered2 = nMaxMinCovered.get(mid, max);
					update(nMaxMinConnected, min, max, nCovered1, nCovered2);
				}

				// Rule 03
				for (int mid = min + 1; mid < max; mid++) {
					int nCovered1 = nMinMaxCovered.get(min, mid);
					int nCovered2 = nMaxMinCovered.get(mid, max);
					update(nMixConnected, min, max, nCovered1, nCovered2);
				}

				// Rule 04
				for (int mid = min + 1; mid < max; mid++) {
					int nCovered1 = nMaxMinCovered.get(min, mid);
					int nCovered2 = nMinMaxCovered.get(mid, max);
					update(nMixConnected, min, max, nCovered1, nCovered2);
				}

				// Concatenate an edge-covered graph and the elementary graph.
				// Rule 05
				if (max - min >= 2) {
					int nCovered = nMinMaxCovered.get(min, max - 1);
					update(nUnconnected, min, max, nCovered);
				}

				// Rule 06
				if (max - min >= 2) {
					int nCovered = nMinMaxCovered.get(min + 1, max);
					update(nUnconnected, min, max, nCovered);
				}

				// Rule 07
				if (max - min >= 2) {
					int nCovered = nMaxMinCovered.get(min, max - 1);
					update(nUnconnected, min, max, nCovered);
				}

				// Rule 08
				if (max - min >= 2) {
					int nCovered = nMaxMinCovered.get(min + 1, max);
					update(nUnconnected, min, max, nCovered);
				}

				// Concatenate a connected graph and an edge-covered graph.
				// Group 1: The first argument is minmax-connected.
				// Rule 09
				for (int mid = min + 2; mid < max; mid++) {
					int nCovered1 = nMinMaxConnected.get(min, mid);
					int nCovered2 = nMinMaxCovered.get(mid, max);
					update(nMinMaxConnected, min, max, nCovered1, nCovered2);
				}

				// Rule 10
				for (int mid = min + 2; mid < max; mid++) {
					int nCovered1 = nMinMaxConnected.get(min, mid);
					int nCovered2 = nMaxMinCovered.get(mid, max);
					update(nMixConnected, min, max, nCovered1, nCovered2);
				}

				// Group 2: The first argument is maxmin-connected.
				// Rule 11
				for (int mid = min + 2; mid < max; mid++) {
					int nCovered1 = nMaxMinConnected.get(min, mid);
					int nCovered2 = nMinMaxCovered.get(mid, max);
					update(nMixConnected, min, max, nCovered1, nCovered2);
				}

				// Rule 12
				for (int mid = min + 2; mid < max; mid++) {
					int nCovered1 = nMaxMinConnected.get(min, mid);
					int nCovered2 = nMaxMinCovered.get(mid, max);
					update(nMaxMinConnected, min, max, nCovered1, nCovered2);
				}

				// Group 3: The first argument is mix-connected.
				// Rule 13
				for (int mid = min + 2; mid < max; mid++) {
					int nCovered1 = nMixConnected.get(min, mid);
					int nCovered2 = nMinMaxCovered.get(mid, max);
					update(nMixConnected, min, max, nCovered1, nCovered2);
				}

				// Rule 14
				for (int mid = min + 2; mid < max; mid++) {
					int nCovered1 = nMixConnected.get(min, mid);
					int nCovered2 = nMaxMinCovered.get(mid, max);
					update(nMixConnected, min, max, nCovered1, nCovered2);
				}

				// Concatenate a connected graph and the elementary graph.
				// Rule 15
				if (max - min >= 3) {
					int nCovered = nMinMaxConnected.get(min, max - 1);
					update(nUnconnected, min, max, nCovered);
				}

				// Rule 16
				if (max - min >= 3) {
					int nCovered = nMaxMinConnected.get(min, max - 1);
					update(nUnconnected, min, max, nCovered);
				}

				// Rule 17
				if (max - min >= 3) {
					int nCovered = nMixConnected.get(min, max - 1);
					update(nUnconnected, min, max, nCovered);
				}

				// Concatenate to an unconnected graph.
				// Rule 18
				for (int mid = min + 2; mid < max; mid++) {
					int nCovered1 = nUnconnected.get(min, mid);
					int nCovered2 = nMinMaxCovered.get(mid, max);
					update(nUnconnected, min, max, nCovered1, nCovered2);
				}

				// Rule 19
				for (int mid = min + 2; mid < max; mid++) {
					int nCovered1 = nUnconnected.get(min, mid);
					int nCovered2 = nMaxMinCovered.get(mid, max);
					update(nUnconnected, min, max, nCovered1, nCovered2);
				}

				// Rule 20
				if (max - min >= 2) { // Applies even to the elementary graph!
					int nCovered = nUnconnected.get(min, max - 1);
					update(nUnconnected, min, max, nCovered);
				}

				// Cover a graph.
				// Group 1: The covering edge is min -> max.
				// Rule 21
				{
					// Adds the edge min -> max
					int nCovered = nMinMaxConnected.get(min, max);
					nCovered += filter.isAdmissible(min, max) ? 1 : 0;
					update(nMinMaxCovered, min, max, nCovered);
				}

				// Rule 22
				{
					// Adds the edge min -> max
					int nCovered = nMixConnected.get(min, max);
					nCovered += filter.isAdmissible(min, max) ? 1 : 0;
					update(nMinMaxCovered, min, max, nCovered);
				}

				// Rule 23
				{
					// Adds the edge min -> max
					int nCovered = nUnconnected.get(min, max);
					nCovered += filter.isAdmissible(min, max) ? 1 : 0;
					update(nMinMaxCovered, min, max, nCovered);
				}

				// Group 2: The covering edge is max -> min.
				// Rule 24
				{
					// Adds the edge max -> min
					int nCovered = nMaxMinConnected.get(min, max);
					nCovered += filter.isAdmissible(max, min) ? 1 : 0;
					update(nMaxMinCovered, min, max, nCovered);
				}

				// Rule 25
				{
					// Adds the edge max -> min
					int nCovered = nMixConnected.get(min, max);
					nCovered += filter.isAdmissible(max, min) ? 1 : 0;
					update(nMaxMinCovered, min, max, nCovered);
				}

				// Rule 26
				{
					// Adds the edge max -> min
					int nCovered = nUnconnected.get(min, max);
					nCovered += filter.isAdmissible(max, min) ? 1 : 0;
					update(nMaxMinCovered, min, max, nCovered);
				}
			}
		}

		int result = 0;
		result = Math.max(result, nMinMaxCovered.get(0, nNodes - 1));
		result = Math.max(result, nMaxMinCovered.get(0, nNodes - 1));
		result = Math.max(result, nMinMaxConnected.get(0, nNodes - 1));
		result = Math.max(result, nMaxMinConnected.get(0, nNodes - 1));
		result = Math.max(result, nMixConnected.get(0, nNodes - 1));
		result = Math.max(result, nUnconnected.get(0, nNodes - 1));
		return result;
	}

	private static void update(IntChart chart, int min, int max, int nCovered1) {
		chart.set(min, max, Math.max(chart.get(min, max), nCovered1));
	}

	private static void update(IntChart chart, int min, int max, int nCovered1, int nCovered2) {
		chart.set(min, max, Math.max(chart.get(min, max), nCovered1 + nCovered2));
	}

	public static void main(String[] args) throws IOException {
		int nEdges = 0;
		int nEdgesCovered = 0;
		int nGraphs = 0;
		int nGraphsCovered = 0;

		CoverageCounter calculator = CoverageCounter.getInstance();

		for (String arg : args) {
			try (GraphReader reader = new GraphReader(arg)) {
				Graph graph;
				while ((graph = reader.readGraph()) != null) {
					int nEdgesCoveredLocal = calculator.getCoverage(graph);
					nEdges += graph.getNEdges();
					nEdgesCovered += nEdgesCoveredLocal;
					nGraphs += 1;
					nGraphsCovered += nEdgesCoveredLocal == graph.getNEdges() ? 1 : 0;
					if (nEdgesCoveredLocal != graph.getNEdges()) {
						InspectedGraph inspectedGraph = new InspectedGraph(graph);
						if (inspectedGraph.isNoncrossing()) {
							if (graph.getNNodes() < 5) {
								System.out.println("graph " + graph.id + " is noncrossing but cannot be covered");
								for (Edge edge : graph.getEdges()) {
									System.out.println(edge.source + " -> " + edge.target);
								}
							}
						}
					}
//					if (nEdgesCoveredLocal == graph.getNEdges()) {
//						InspectedGraph inspectedGraph = new InspectedGraph(graph);
//						if (!inspectedGraph.isNoncrossing()) {
//							System.out.println("graph " + graph.id + " is not noncrossing but can be covered");
//						}
//					}
				}
			}
		}

		NumberFormat percentFormatter = NumberFormat.getPercentInstance(Locale.US);
		percentFormatter.setMinimumFractionDigits(2);
		percentFormatter.setMaximumFractionDigits(2);

		System.err.format("Upper bound on recall (edges): %s%n", percentFormatter.format((double) nEdgesCovered / (double) nEdges));
		System.err.format("Upper bound on recall (complete graphs): %s%n", percentFormatter.format((double) nGraphsCovered / (double) nGraphs));

		System.exit(0);
	}
}
