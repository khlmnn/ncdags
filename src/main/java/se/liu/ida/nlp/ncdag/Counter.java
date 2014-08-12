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

		BigIntegerChart countEMinMax = new BigIntegerChart(nNodes);
		BigIntegerChart countEMaxMin = new BigIntegerChart(nNodes);
		BigIntegerChart countCMinMax = new BigIntegerChart(nNodes);
		BigIntegerChart countCMaxMin = new BigIntegerChart(nNodes);
		BigIntegerChart countCMix = new BigIntegerChart(nNodes);
		BigIntegerChart countU = new BigIntegerChart(nNodes);

		for (int node = 0; node < nNodes - 1; node++) {
			countU.set(node, node + 1, BigInteger.ONE);
		}

		for (int max = 0; max < nNodes; max++) {
			for (int min = max - 1; min >= 0; min--) {
				// Concatenate two edge-covered graphs.

				// Rule 01
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = countEMinMax.get(min, mid);
					BigInteger nDerivations2 = countEMinMax.get(mid, max);
					update(countCMinMax, min, max, nDerivations1, nDerivations2);
				}

				// Rule 02
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = countEMaxMin.get(min, mid);
					BigInteger nDerivations2 = countEMaxMin.get(mid, max);
					update(countCMaxMin, min, max, nDerivations1, nDerivations2);
				}

				// Rule 03
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = countEMinMax.get(min, mid);
					BigInteger nDerivations2 = countEMaxMin.get(mid, max);
					update(countCMix, min, max, nDerivations1, nDerivations2);
				}

				// Rule 04
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = countEMaxMin.get(min, mid);
					BigInteger nDerivations2 = countEMinMax.get(mid, max);
					update(countCMix, min, max, nDerivations1, nDerivations2);
				}

				// Concatenate an edge-covered graph and the elementary graph.
				// Rule 05
				if (max - min >= 2) {
					BigInteger nDerivations = countEMinMax.get(min, max - 1);
					update(countU, min, max, nDerivations);
				}

				// Rule 06
				if (max - min >= 2) {
					BigInteger nDerivations = countEMinMax.get(min + 1, max);
					update(countU, min, max, nDerivations);
				}

				// Rule 07
				if (max - min >= 2) {
					BigInteger nDerivations = countEMaxMin.get(min, max - 1);
					update(countU, min, max, nDerivations);
				}

				// Rule 08
				if (max - min >= 2) {
					BigInteger nDerivations = countEMaxMin.get(min + 1, max);
					update(countU, min, max, nDerivations);
				}

				// Concatenate a connected graph and an edge-covered graph.
				// Group 1: The first argument is minmax-connected.
				// Rule 09
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCMinMax.get(min, mid);
					BigInteger nDerivations2 = countEMinMax.get(mid, max);
					update(countCMinMax, min, max, nDerivations1, nDerivations2);
				}

				// Rule 10
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCMinMax.get(min, mid);
					BigInteger nDerivations2 = countEMaxMin.get(mid, max);
					update(countCMix, min, max, nDerivations1, nDerivations2);
				}

				// Group 2: The first argument is maxmin-connected.
				// Rule 11
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCMaxMin.get(min, mid);
					BigInteger nDerivations2 = countEMinMax.get(mid, max);
					update(countCMix, min, max, nDerivations1, nDerivations2);
				}

				// Rule 12
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCMaxMin.get(min, mid);
					BigInteger nDerivations2 = countEMaxMin.get(mid, max);
					update(countCMaxMin, min, max, nDerivations1, nDerivations2);
				}

				// Group 3: The first argument is U-connected.
				// Rule 13
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCMix.get(min, mid);
					BigInteger nDerivations2 = countEMinMax.get(mid, max);
					update(countCMix, min, max, nDerivations1, nDerivations2);
				}

				// Rule 14
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countCMix.get(min, mid);
					BigInteger nDerivations2 = countEMaxMin.get(mid, max);
					update(countCMix, min, max, nDerivations1, nDerivations2);
				}

				// Concatenate a connected graph and the elementary graph.
				// Rule 15
				if (max - min >= 3) {
					BigInteger nDerivations = countCMinMax.get(min, max - 1);
					update(countU, min, max, nDerivations);
				}

				// Rule 16
				if (max - min >= 3) {
					BigInteger nDerivations = countCMaxMin.get(min, max - 1);
					update(countU, min, max, nDerivations);
				}

				// Rule 17
				if (max - min >= 3) {
					BigInteger nDerivations = countCMix.get(min, max - 1);
					update(countU, min, max, nDerivations);
				}

				// Concatenate to an unconnected graph.
				// Rule 18
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countU.get(min, mid);
					BigInteger nDerivations2 = countEMinMax.get(mid, max);
					update(countU, min, max, nDerivations1, nDerivations2);
				}

				// Rule 19
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = countU.get(min, mid);
					BigInteger nDerivations2 = countEMaxMin.get(mid, max);
					update(countU, min, max, nDerivations1, nDerivations2);
				}

				// Rule 20
				if (max - min >= 2) { // Applies even to the elementary graph!
					BigInteger nDerivations = countU.get(min, max - 1);
					update(countU, min, max, nDerivations);
				}

				// Cover a graph.
				// Group 1: The covering edge is min -> max.
				// Rule 21
				{
					// Adds the edge min -> max
					BigInteger nDerivations1 = countCMinMax.get(min, max);
					update(countEMinMax, min, max, nDerivations1);
				}

				// Rule 22
				{
					// Adds the edge min -> max
					BigInteger nDerivations1 = countCMix.get(min, max);
					update(countEMinMax, min, max, nDerivations1);
				}

				// Rule 23
				{
					// Adds the edge min -> max
					BigInteger nDerivations1 = countU.get(min, max);
					update(countEMinMax, min, max, nDerivations1);
				}

				// Group 2: The covering edge is max -> min.
				// Rule 24
				{
					// Adds the edge max -> min
					BigInteger nDerivations1 = countCMaxMin.get(min, max);
					update(countEMaxMin, min, max, nDerivations1);
				}

				// Rule 25
				{
					// Adds the edge max -> min
					BigInteger nDerivations1 = countCMix.get(min, max);
					update(countEMaxMin, min, max, nDerivations1);
				}

				// Rule 26
				{
					// Adds the edge max -> min
					BigInteger nDerivations1 = countU.get(min, max);
					update(countEMaxMin, min, max, nDerivations1);
				}
			}
		}

		BigInteger result = BigInteger.ZERO;
		result = result.add(countEMinMax.get(0, nNodes - 1));
		result = result.add(countEMaxMin.get(0, nNodes - 1));
		result = result.add(countCMinMax.get(0, nNodes - 1));
		result = result.add(countCMaxMin.get(0, nNodes - 1));
		result = result.add(countCMix.get(0, nNodes - 1));
		result = result.add(countU.get(0, nNodes - 1));
		return result;
	}

	private static void update(BigIntegerChart chart, int min, int max, BigInteger nDerivations1) {
		chart.add(min, max, nDerivations1);
	}

	private static void update(BigIntegerChart chart, int min, int max, BigInteger nDerivations1, BigInteger nDerivations2) {
		chart.add(min, max, nDerivations1.multiply(nDerivations2));
	}
}
