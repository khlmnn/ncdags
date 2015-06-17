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
 * OEIS: A246756, 1, 3, 25, 335, 5521, 101551, 1998753, 41188543, 877423873,
 * 19166868607
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
	BigIntegerChart nMinMaxEdge = new BigIntegerChart(nNodes);
	BigIntegerChart nMaxMinEdge = new BigIntegerChart(nNodes);
	BigIntegerChart nMinMaxPath = new BigIntegerChart(nNodes);
	BigIntegerChart nMaxMinPath = new BigIntegerChart(nNodes);
	BigIntegerChart nRectangle = new BigIntegerChart(nNodes);

	for (int node = 0; node < nNodes; node++) {
	    nRectangle.set(node, node, BigInteger.ONE);
	}
//	for (int node = 0; node < nNodes - 1; node++) {
//	    nMinMaxEdge.set(node, node + 1, BigInteger.ONE);
//	    nMaxMinEdge.set(node, node + 1, BigInteger.ONE);
//	}

	for (int max = 0; max < nNodes; max++) {
	    for (int min = max - 1; min >= 0; min--) {
		// Concatenate a connected graph and an edge-covered graph.
		// Group 1: The first argument is minmax-connected.
		// Rule 01
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMinMaxPath.get(min, mid);
		    BigInteger nDerivations2 = nMinMaxEdge.get(mid, max);
		    update(nMinMaxPath, min, max, nDerivations1, nDerivations2);
		}

		// Rule 02
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMinMaxPath.get(min, mid);
		    BigInteger nDerivations2 = nMaxMinEdge.get(mid, max);
		    update(nRectangle, min, max, nDerivations1, nDerivations2);
		}

		// Group 2: The first argument is maxmin-connected.
		// Rule 03
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMaxMinPath.get(min, mid);
		    BigInteger nDerivations2 = nMinMaxEdge.get(mid, max);
		    update(nRectangle, min, max, nDerivations1, nDerivations2);
		}

		// Rule 04
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMaxMinPath.get(min, mid);
		    BigInteger nDerivations2 = nMaxMinEdge.get(mid, max);
		    update(nMaxMinPath, min, max, nDerivations1, nDerivations2);
		}

		// Group 3: The first argument is mix-connected.
		// Rule 05
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nRectangle.get(min, mid);
		    BigInteger nDerivations2 = nMinMaxEdge.get(mid, max);
		    update(nRectangle, min, max, nDerivations1, nDerivations2);
		}

		// Rule 06
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nRectangle.get(min, mid);
		    BigInteger nDerivations2 = nMaxMinEdge.get(mid, max);
		    update(nRectangle, min, max, nDerivations1, nDerivations2);
		}

		// Concatenate a connected graph and the elementary graph.
		// Rule 07
		{
		    BigInteger nDerivations = nMinMaxPath.get(min, max - 1);
		    update(nRectangle, min, max, nDerivations);
		}

		// Rule 08
		{
		    BigInteger nDerivations = nMaxMinPath.get(min, max - 1);
		    update(nRectangle, min, max, nDerivations);
		}

		// Rule 09
		{
		    BigInteger nDerivations = nRectangle.get(min, max - 1);
		    update(nRectangle, min, max, nDerivations);
		}

		// Cover a graph.
		// Group 1: The covering edge is min -> max.
		// Rule 10
		{
		    // Adds the edge min -> max
		    BigInteger nDerivations = nMinMaxPath.get(min, max);
		    update(nMinMaxEdge, min, max, nDerivations);
		}

		// Rule 11
		{
		    // Adds the edge min -> max
		    BigInteger nDerivations = nRectangle.get(min, max);
		    update(nMinMaxEdge, min, max, nDerivations);
		}

		// Group 2: The covering edge is max -> min.
		// Rule 12
		{
		    // Adds the edge max -> min
		    BigInteger nDerivations = nMaxMinPath.get(min, max);
		    update(nMaxMinEdge, min, max, nDerivations);
		}

		// Rule 13
		{
		    // Adds the edge max -> min
		    BigInteger nDerivations = nRectangle.get(min, max);
		    update(nMaxMinEdge, min, max, nDerivations);
		}

		// Turn a single item into a sequence item.
		// Rule 14
		{
		    BigInteger nDerivations = nMinMaxEdge.get(min, max);
		    update(nMinMaxPath, min, max, nDerivations);
		}
		
		// Rule 15
		{
		    BigInteger nDerivations = nMaxMinEdge.get(min, max);
		    update(nMaxMinPath, min, max, nDerivations);
		}
	    }
	}

	BigInteger result = BigInteger.ZERO;
	result = result.add(nMinMaxPath.get(0, nNodes - 1));
	result = result.add(nMaxMinPath.get(0, nNodes - 1));
	result = result.add(nRectangle.get(0, nNodes - 1));
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
