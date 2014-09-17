/*
 * See the file "LICENSE" for the full license governing this code.
 */
package se.liu.ida.nlp.ncdag;

import java.math.BigInteger;

/**
 * Count the number of noncrossing acyclic digraphs. A <em>noncrossing
 * graph</em> on <em>n</em> vertices is a graph drawn on <em>n</em> points
 * numbered from 1 to <em>n</em> in counter-clockwise order on a circle such
 * that the edges lie entirely within the circle and do not cross each other.
 *
 * @author Marco Kuhlmann <marco.kuhlmann@liu.se>
 */
public class Counter {

	private static Counter instance = null;

	private Counter() {
		// Private constructor to prevent instantiation.
	}

	public static Counter getInstance() {
		if (instance == null) {
			instance = new Counter();
		}
		return instance;
	}

	public BigInteger getNDerivations(int nNodes) {
		// Special case
		if (nNodes == 1) {
			return BigInteger.ONE;
		}

		BigIntegerChart nMinMaxCovered = new BigIntegerChart(nNodes);
		BigIntegerChart nMaxMinCovered = new BigIntegerChart(nNodes);
		BigIntegerChart nMinMaxConnected = new BigIntegerChart(nNodes);
		BigIntegerChart nMaxMinConnected = new BigIntegerChart(nNodes);
		BigIntegerChart nMixConnected = new BigIntegerChart(nNodes);
		BigIntegerChart nUnconnected = new BigIntegerChart(nNodes);

		for (int node = 0; node < nNodes - 1; node++) {
			nUnconnected.set(node, node + 1, BigInteger.ONE);
		}

		for (int max = 0; max < nNodes; max++) {
			for (int min = max - 1; min >= 0; min--) {
				// Concatenate two edge-covered graphs.

				// Rule 01
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = nMinMaxCovered.get(min, mid);
					BigInteger nDerivations2 = nMinMaxCovered.get(mid, max);
					update(nMinMaxConnected, min, max, nDerivations1, nDerivations2);
				}

				// Rule 02
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = nMaxMinCovered.get(min, mid);
					BigInteger nDerivations2 = nMaxMinCovered.get(mid, max);
					update(nMaxMinConnected, min, max, nDerivations1, nDerivations2);
				}

				// Rule 03
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = nMinMaxCovered.get(min, mid);
					BigInteger nDerivations2 = nMaxMinCovered.get(mid, max);
					update(nMixConnected, min, max, nDerivations1, nDerivations2);
				}

				// Rule 04
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = nMaxMinCovered.get(min, mid);
					BigInteger nDerivations2 = nMinMaxCovered.get(mid, max);
					update(nMixConnected, min, max, nDerivations1, nDerivations2);
				}

				// Concatenate an edge-covered graph and the elementary graph.
				// Rule 05
				if (max - min >= 2) {
					BigInteger nDerivations = nMinMaxCovered.get(min, max - 1);
					update(nUnconnected, min, max, nDerivations);
				}

				// Rule 06
				if (max - min >= 2) {
					BigInteger nDerivations = nMinMaxCovered.get(min + 1, max);
					update(nUnconnected, min, max, nDerivations);
				}

				// Rule 07
				if (max - min >= 2) {
					BigInteger nDerivations = nMaxMinCovered.get(min, max - 1);
					update(nUnconnected, min, max, nDerivations);
				}

				// Rule 08
				if (max - min >= 2) {
					BigInteger nDerivations = nMaxMinCovered.get(min + 1, max);
					update(nUnconnected, min, max, nDerivations);
				}

				// Concatenate a connected graph and an edge-covered graph.
				// Group 1: The first argument is minmax-connected.
				// Rule 09
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = nMinMaxConnected.get(min, mid);
					BigInteger nDerivations2 = nMinMaxCovered.get(mid, max);
					update(nMinMaxConnected, min, max, nDerivations1, nDerivations2);
				}

				// Rule 10
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = nMinMaxConnected.get(min, mid);
					BigInteger nDerivations2 = nMaxMinCovered.get(mid, max);
					update(nMixConnected, min, max, nDerivations1, nDerivations2);
				}

				// Group 2: The first argument is maxmin-connected.
				// Rule 11
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = nMaxMinConnected.get(min, mid);
					BigInteger nDerivations2 = nMinMaxCovered.get(mid, max);
					update(nMixConnected, min, max, nDerivations1, nDerivations2);
				}

				// Rule 12
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = nMaxMinConnected.get(min, mid);
					BigInteger nDerivations2 = nMaxMinCovered.get(mid, max);
					update(nMaxMinConnected, min, max, nDerivations1, nDerivations2);
				}

				// Group 3: The first argument is mix-connected.
				// Rule 13
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = nMixConnected.get(min, mid);
					BigInteger nDerivations2 = nMinMaxCovered.get(mid, max);
					update(nMixConnected, min, max, nDerivations1, nDerivations2);
				}

				// Rule 14
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = nMixConnected.get(min, mid);
					BigInteger nDerivations2 = nMaxMinCovered.get(mid, max);
					update(nMixConnected, min, max, nDerivations1, nDerivations2);
				}

				// Concatenate a connected graph and the elementary graph.
				// Rule 15
				if (max - min >= 3) {
					BigInteger nDerivations = nMinMaxConnected.get(min, max - 1);
					update(nUnconnected, min, max, nDerivations);
				}

				// Rule 16
				if (max - min >= 3) {
					BigInteger nDerivations = nMaxMinConnected.get(min, max - 1);
					update(nUnconnected, min, max, nDerivations);
				}

				// Rule 17
				if (max - min >= 3) {
					BigInteger nDerivations = nMixConnected.get(min, max - 1);
					update(nUnconnected, min, max, nDerivations);
				}

				// Concatenate to an unconnected graph.
				// Rule 18
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = nUnconnected.get(min, mid);
					BigInteger nDerivations2 = nMinMaxCovered.get(mid, max);
					update(nUnconnected, min, max, nDerivations1, nDerivations2);
				}

				// Rule 19
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = nUnconnected.get(min, mid);
					BigInteger nDerivations2 = nMaxMinCovered.get(mid, max);
					update(nUnconnected, min, max, nDerivations1, nDerivations2);
				}

				// Rule 20
				if (max - min >= 2) { // Applies even to the elementary graph!
					BigInteger nDerivations = nUnconnected.get(min, max - 1);
					update(nUnconnected, min, max, nDerivations);
				}

				// Cover a graph.
				// Group 1: The covering edge is min -> max.
				// Rule 21
				{
					// Adds the edge min -> max
					BigInteger nDerivations = nMinMaxConnected.get(min, max);
					update(nMinMaxCovered, min, max, nDerivations);
				}

				// Rule 22
				{
					// Adds the edge min -> max
					BigInteger nDerivations = nMixConnected.get(min, max);
					update(nMinMaxCovered, min, max, nDerivations);
				}

				// Rule 23
				{
					// Adds the edge min -> max
					BigInteger nDerivations = nUnconnected.get(min, max);
					update(nMinMaxCovered, min, max, nDerivations);
				}

				// Group 2: The covering edge is max -> min.
				// Rule 24
				{
					// Adds the edge max -> min
					BigInteger nDerivations = nMaxMinConnected.get(min, max);
					update(nMaxMinCovered, min, max, nDerivations);
				}

				// Rule 25
				{
					// Adds the edge max -> min
					BigInteger nDerivations = nMixConnected.get(min, max);
					update(nMaxMinCovered, min, max, nDerivations);
				}

				// Rule 26
				{
					// Adds the edge max -> min
					BigInteger nDerivations = nUnconnected.get(min, max);
					update(nMaxMinCovered, min, max, nDerivations);
				}
			}
		}

		BigInteger result = BigInteger.ZERO;
		result = result.add(nMinMaxCovered.get(0, nNodes - 1));
		result = result.add(nMaxMinCovered.get(0, nNodes - 1));
		result = result.add(nMinMaxConnected.get(0, nNodes - 1));
		result = result.add(nMaxMinConnected.get(0, nNodes - 1));
		result = result.add(nMixConnected.get(0, nNodes - 1));
		result = result.add(nUnconnected.get(0, nNodes - 1));
		return result;
	}

	private static void update(BigIntegerChart chart, int min, int max, BigInteger nDerivations1) {
		chart.add(min, max, nDerivations1);
	}

	private static void update(BigIntegerChart chart, int min, int max, BigInteger nDerivations1, BigInteger nDerivations2) {
		chart.add(min, max, nDerivations1.multiply(nDerivations2));
	}

	public static void main(String[] args) {
		Counter counter = Counter.getInstance();
		int nNodes = Integer.parseInt(args[0]);
		StringBuilder sb = new StringBuilder();
		sb.append(counter.getNDerivations(1));
		for (int i = 2; i <= nNodes; i++) {
			sb.append(", ");
			sb.append(counter.getNDerivations(i));
		}
		System.out.println(sb);
		System.exit(0);
	}
}
