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

	// CR: covered graph; arc from min to max
	// CL: covered graph; arc from max to min
	// SR: connected graph (not covered); directed path from min to max
	// SL: connected graph (not covered); directed path from max to min
	// SM: connected graph (not covered); no directed path between min and max
	// SU: unconnected graph
	public Counter() {
	}

	private static void update(BigIntegerChart chart, int min, int max, BigInteger nDerivations1) {
		chart.add(min, max, nDerivations1);
	}

	private static void update(BigIntegerChart chart, int min, int max, BigInteger nDerivations1, BigInteger nDerivations2) {
		chart.add(min, max, nDerivations1.multiply(nDerivations2));
	}

	public BigInteger getNDerivations(int nNodes) {
		BigIntegerChart countER = new BigIntegerChart(nNodes);
		BigIntegerChart countEL = new BigIntegerChart(nNodes);
		BigIntegerChart countCR = new BigIntegerChart(nNodes);
		BigIntegerChart countCL = new BigIntegerChart(nNodes);
		BigIntegerChart countCU = new BigIntegerChart(nNodes);
		BigIntegerChart countXX = new BigIntegerChart(nNodes);

		for (int node = 0; node < nNodes - 1; node++) {
			countXX.set(node, node + 1, BigInteger.ONE);
		}

		for (int max = 0; max < nNodes; max++) {
			for (int min = max - 1; min >= 0; min--) {
				// Concatenate two edge-covered graphs.

				// Rule 01
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = countER.get(min, mid);
					BigInteger nDerivations2 = countER.get(mid, max);
					update(countCR, min, max, nDerivations1, nDerivations2);
				}

				// Rule 02
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = countEL.get(min, mid);
					BigInteger nDerivations2 = countEL.get(mid, max);
					update(countCL, min, max, nDerivations1, nDerivations2);
				}

				// Rule 03
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = countER.get(min, mid);
					BigInteger nDerivations2 = countEL.get(mid, max);
					update(countCU, min, max, nDerivations1, nDerivations2);
				}

				// Rule 04
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = countEL.get(min, mid);
					BigInteger nDerivations2 = countER.get(mid, max);
					update(countCU, min, max, nDerivations1, nDerivations2);
				}

				// Concatenate an edge-covered graph and the elementary graph.
				// Rule 05
				if (max - min >= 2) {
					BigInteger nDerivations = countER.get(min, max - 1);
					update(countXX, min, max, nDerivations);
				}

				// Rule 06
				if (max - min >= 2) {
					BigInteger nDerivations = countER.get(min + 1, max);
					update(countXX, min, max, nDerivations);
				}

				// Rule 07
				if (max - min >= 2) {
					BigInteger nDerivations = countEL.get(min, max - 1);
					update(countXX, min, max, nDerivations);
				}

				// Rule 08
				if (max - min >= 2) {
					BigInteger nDerivations = countEL.get(min + 1, max);
					update(countXX, min, max, nDerivations);
				}

				// Concatenate a connected graph and an edge-covered graph.
				// Group 1: The first argument is R-connected.
				// Rule 09
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCR.get(min, mid);
					BigInteger nDerivations2 = countER.get(mid, max);
					update(countCR, min, max, nDerivations1, nDerivations2);
				}

				// Rule 10
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCR.get(min, mid);
					BigInteger nDerivations2 = countEL.get(mid, max);
					update(countCU, min, max, nDerivations1, nDerivations2);
				}

				// Group 2: The first argument is L-connected.
				// Rule 11
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCL.get(min, mid);
					BigInteger nDerivations2 = countER.get(mid, max);
					update(countCU, min, max, nDerivations1, nDerivations2);
				}

				// Rule 12
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCL.get(min, mid);
					BigInteger nDerivations2 = countEL.get(mid, max);
					update(countCL, min, max, nDerivations1, nDerivations2);
				}

				// Group 3: The first argument is U-connected.
				// Rule 13
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCU.get(min, mid);
					BigInteger nDerivations2 = countER.get(mid, max);
					update(countCU, min, max, nDerivations1, nDerivations2);
				}

				// Rule 14
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCU.get(min, mid);
					BigInteger nDerivations2 = countEL.get(mid, max);
					update(countCU, min, max, nDerivations1, nDerivations2);
				}

				// Concatenate a connected graph and the elementary graph.
				// Rule 15
				if (max - min >= 3) {
					BigInteger nDerivations = countCR.get(min, max - 1);
					update(countXX, min, max, nDerivations);
				}

				// Rule 16
				if (max - min >= 3) {
					BigInteger nDerivations = countCL.get(min, max - 1);
					update(countXX, min, max, nDerivations);
				}

				// Rule 17
				if (max - min >= 3) {
					BigInteger nDerivations = countCU.get(min, max - 1);
					update(countXX, min, max, nDerivations);
				}

				// Concatenate to an unconnected graph.
				// Rule 18
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countXX.get(min, mid);
					BigInteger nDerivations2 = countER.get(mid, max);
					update(countXX, min, max, nDerivations1, nDerivations2);
				}

				// Rule 19
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countXX.get(min, mid);
					BigInteger nDerivations2 = countEL.get(mid, max);
					update(countXX, min, max, nDerivations1, nDerivations2);
				}

				// Rule 20
				if (max - min >= 2) { // Applies even to the elementary graph!
					BigInteger nDerivations = countXX.get(min, max - 1);
					update(countXX, min, max, nDerivations);
				}

				// Cover a graph.
				// Group 1: The covering edge is left-to-right.
				// Rule 21
				{
					// Adds the arc min -> max
					BigInteger nDerivations1 = countCR.get(min, max);
					update(countER, min, max, nDerivations1);
				}

				// Rule 22
				{
					// Adds the arc min -> max
					BigInteger nDerivations1 = countCU.get(min, max);
					update(countER, min, max, nDerivations1);
				}

				// Rule 23
				{
					// Adds the arc min -> max
					BigInteger nDerivations1 = countXX.get(min, max);
					update(countER, min, max, nDerivations1);
				}

				// Group 2: The covering edge is right-to-left.
				// Rule 24
				{
					// Adds the arc max -> min
					BigInteger nDerivations1 = countCL.get(min, max);
					update(countEL, min, max, nDerivations1);
				}

				// Rule 25
				{
					// Adds the arc max -> min
					BigInteger nDerivations1 = countCU.get(min, max);
					update(countEL, min, max, nDerivations1);
				}

				// Rule 26
				{
					// Adds the arc max -> min
					BigInteger nDerivations1 = countXX.get(min, max);
					update(countEL, min, max, nDerivations1);
				}
			}
		}

		BigInteger result = BigInteger.ZERO;
		result = result.add(countER.get(0, nNodes - 1));
		result = result.add(countEL.get(0, nNodes - 1));
		result = result.add(countCR.get(0, nNodes - 1));
		result = result.add(countCL.get(0, nNodes - 1));
		result = result.add(countCU.get(0, nNodes - 1));
		result = result.add(countXX.get(0, nNodes - 1));
		return result;
	}
}
