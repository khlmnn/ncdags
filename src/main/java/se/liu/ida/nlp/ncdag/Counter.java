/*
 * See the file "LICENSE" for the full license governing this code.
 */
package se.liu.ida.nlp.ncdag;

import java.math.BigInteger;

/**
 * Count the number of noncrossing acyclic digraphs.
 *
 * <p>
 * A <em>noncrossing graph</em> on <em>n</em> vertices is a graph drawn on
 * <em>n</em> points numbered from 1 to <em>n</em> in counter-clockwise order on
 * a circle such that the arcs lie entirely within the circle and do not cross
 * each other.
 *
 * <p>
 * The integer sequence obtained by this counter is
 * <a href="https://oeis.org/A246756">OEIS A246756</a>. Its first few terms are:
 * <pre>
 * 1, 3, 25, 335, 5521, 101551, 1998753, 41188543, 877423873, 19166868607
 * </pre>
 *
 * @author Marco Kuhlmann
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
	BigIntegerChart nMixConnected = new BigIntegerChart(nNodes);
	BigIntegerChart nNonConnected = new BigIntegerChart(nNodes);

	for (int node = 0; node < nNodes; node++) {
	    nMixConnected.set(node, node, BigInteger.ONE);
	}

	for (int max = 0; max < nNodes; max++) {
	    for (int min = max - 1; min >= 0; min--) {
		// Rule 01
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMinMaxEdge.get(min, mid);
		    BigInteger nDerivations2 = nMinMaxEdge.get(mid, max);
		    update(nMinMaxPath, min, max, nDerivations1, nDerivations2);
		}

		// Rule 02
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMinMaxEdge.get(min, mid);
		    BigInteger nDerivations2 = nMaxMinEdge.get(mid, max);
		    update(nMixConnected, min, max, nDerivations1, nDerivations2);
		}

		// Rule 03
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMaxMinEdge.get(min, mid);
		    BigInteger nDerivations2 = nMinMaxEdge.get(mid, max);
		    update(nMixConnected, min, max, nDerivations1, nDerivations2);
		}

		// Rule 04
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMaxMinEdge.get(min, mid);
		    BigInteger nDerivations2 = nMaxMinEdge.get(mid, max);
		    update(nMaxMinPath, min, max, nDerivations1, nDerivations2);
		}

		// Rule 05
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMinMaxPath.get(min, mid);
		    BigInteger nDerivations2 = nMinMaxEdge.get(mid, max);
		    update(nMinMaxPath, min, max, nDerivations1, nDerivations2);
		}

		// Rule 06
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMinMaxPath.get(min, mid);
		    BigInteger nDerivations2 = nMaxMinEdge.get(mid, max);
		    update(nMixConnected, min, max, nDerivations1, nDerivations2);
		}

		// Rule 07
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMaxMinPath.get(min, mid);
		    BigInteger nDerivations2 = nMinMaxEdge.get(mid, max);
		    update(nMixConnected, min, max, nDerivations1, nDerivations2);
		}

		// Rule 08
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMaxMinPath.get(min, mid);
		    BigInteger nDerivations2 = nMaxMinEdge.get(mid, max);
		    update(nMaxMinPath, min, max, nDerivations1, nDerivations2);
		}

		// Rule 09
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMixConnected.get(min, mid);
		    BigInteger nDerivations2 = nMinMaxEdge.get(mid, max);
		    update(nMixConnected, min, max, nDerivations1, nDerivations2);
		}

		// Rule 09a
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nNonConnected.get(min, mid);
		    BigInteger nDerivations2 = nMinMaxEdge.get(mid, max);
		    update(nNonConnected, min, max, nDerivations1, nDerivations2);
		}
		
		// Rule 10
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nMixConnected.get(min, mid);
		    BigInteger nDerivations2 = nMaxMinEdge.get(mid, max);
		    update(nMixConnected, min, max, nDerivations1, nDerivations2);
		}

		// Rule 10a
		for (int mid = min + 1; mid < max; mid++) {
		    BigInteger nDerivations1 = nNonConnected.get(min, mid);
		    BigInteger nDerivations2 = nMaxMinEdge.get(mid, max);
		    update(nNonConnected, min, max, nDerivations1, nDerivations2);
		}
		
		// Rule 11
		{
		    BigInteger nDerivations = nMinMaxEdge.get(min, max - 1);
		    update(nNonConnected, min, max, nDerivations);
		}

		// Rule 12
		{
		    BigInteger nDerivations = nMaxMinEdge.get(min, max - 1);
		    update(nNonConnected, min, max, nDerivations);
		}

		// Rule 13
		{
		    BigInteger nDerivations = nMinMaxPath.get(min, max - 1);
		    update(nNonConnected, min, max, nDerivations);
		}

		// Rule 14
		{
		    BigInteger nDerivations = nMaxMinPath.get(min, max - 1);
		    update(nNonConnected, min, max, nDerivations);
		}

		// Rule 15
		{
		    BigInteger nDerivations = nMixConnected.get(min, max - 1);
		    update(nNonConnected, min, max, nDerivations);
		}

		// Rule 15a
		{
		    BigInteger nDerivations = nNonConnected.get(min, max - 1);
		    update(nNonConnected, min, max, nDerivations);
		}
		
		// Rule 16
		{
		    // Adds the edge min -> max
		    BigInteger nDerivations = nMinMaxPath.get(min, max);
		    update(nMinMaxEdge, min, max, nDerivations);
		}

		// Rule 17
		{
		    // Adds the edge max -> min
		    BigInteger nDerivations = nMaxMinPath.get(min, max);
		    update(nMaxMinEdge, min, max, nDerivations);
		}

		// Rule 18
		{
		    // Adds the edge min -> max
		    BigInteger nDerivations = nMixConnected.get(min, max);
		    update(nMinMaxEdge, min, max, nDerivations);
		}

		// Rule 18a
		{
		    // Adds the edge min -> max
		    BigInteger nDerivations = nNonConnected.get(min, max);
		    update(nMinMaxEdge, min, max, nDerivations);
		}
		
		// Rule 19
		{
		    // Adds the edge max -> min
		    BigInteger nDerivations = nMixConnected.get(min, max);
		    update(nMaxMinEdge, min, max, nDerivations);
		}
		
		// Rule 19a
		{
		    // Adds the edge max -> min
		    BigInteger nDerivations = nNonConnected.get(min, max);
		    update(nMaxMinEdge, min, max, nDerivations);
		}
	    }
	}

	BigInteger result = BigInteger.ZERO;
	result = result.add(nMinMaxEdge.get(0, nNodes - 1));
	result = result.add(nMaxMinEdge.get(0, nNodes - 1));
	result = result.add(nMinMaxPath.get(0, nNodes - 1));
	result = result.add(nMaxMinPath.get(0, nNodes - 1));
	result = result.add(nMixConnected.get(0, nNodes - 1));
	result = result.add(nNonConnected.get(0, nNodes - 1));
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
