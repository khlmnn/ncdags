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

	// CR: covered graph; topmost arc points R-ward
	// CL: covered graph; topmost arc points L-ward
	// CH: hidden arc (length one)
	// SR: connected sequence of covered graphs; all topmost arcs point R-ward
	// SL: connected sequence of covered graphs; all topmost arcs point L-ward
	// SM: connected sequence of covered graphs; mixed directions
	// SU: unconnected sequence of covered graphs
	public Counter() {
	}

	private static void update(BigIntegerChart chart, int min, int max, BigInteger nDerivations1) {
		chart.set(min, max, chart.get(min, max).add(nDerivations1));
	}

	private static void update(BigIntegerChart chart, int min, int max, BigInteger nDerivations1, BigInteger nDerivations2) {
		chart.set(min, max, chart.get(min, max).add(nDerivations1.multiply(nDerivations2)));
	}

	public BigInteger getNDerivations(int nNodes) {
		BigIntegerChart chartCR = new BigIntegerChart(nNodes);
		BigIntegerChart chartCL = new BigIntegerChart(nNodes);
		BigIntegerChart chartCH = new BigIntegerChart(nNodes);
		BigIntegerChart chartSR = new BigIntegerChart(nNodes);
		BigIntegerChart chartSL = new BigIntegerChart(nNodes);
		BigIntegerChart chartSM = new BigIntegerChart(nNodes);
		BigIntegerChart chartSU = new BigIntegerChart(nNodes);

		for (int node = 0; node < nNodes - 1; node++) {
			chartCR.set(node, node + 1, BigInteger.ONE); // Adds the arc node -> node + 1.
			chartCL.set(node, node + 1, BigInteger.ONE); // Adds the arc node + 1 -> node.
			chartCH.set(node, node + 1, BigInteger.ONE);
			chartSR.set(node, node + 1, BigInteger.ZERO);
			chartSL.set(node, node + 1, BigInteger.ZERO);
			chartSM.set(node, node + 1, BigInteger.ZERO);
			chartSU.set(node, node + 1, BigInteger.ZERO);
		}

		for (int max = 0; max < nNodes; max++) {
			for (int min = max - 2; min >= 0; min--) {
				chartCR.set(min, max, BigInteger.ZERO);
				chartCL.set(min, max, BigInteger.ZERO);
				chartCH.set(min, max, BigInteger.ZERO);
				chartSR.set(min, max, BigInteger.ZERO);
				chartSL.set(min, max, BigInteger.ZERO);
				chartSM.set(min, max, BigInteger.ZERO);
				chartSU.set(min, max, BigInteger.ZERO);

				// Start sequences.
				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = chartCR.get(min, mid);
					BigInteger nDerivations2 = chartCR.get(mid, max);
					update(chartSR, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = chartCR.get(min, mid);
					BigInteger nDerivations2 = chartCL.get(mid, max);
					update(chartSM, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = chartCR.get(min, mid);
					BigInteger nDerivations2 = chartCH.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = chartCL.get(min, mid);
					BigInteger nDerivations2 = chartCR.get(mid, max);
					update(chartSM, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = chartCL.get(min, mid);
					BigInteger nDerivations2 = chartCL.get(mid, max);
					update(chartSL, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = chartCL.get(min, mid);
					BigInteger nDerivations2 = chartCH.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = chartCH.get(min, mid);
					BigInteger nDerivations2 = chartCR.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = chartCH.get(min, mid);
					BigInteger nDerivations2 = chartCL.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 1; mid < max; mid++) {
					BigInteger nDerivations1 = chartCH.get(min, mid);
					BigInteger nDerivations2 = chartCH.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				// Extend sequences.
				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSR.get(min, mid);
					BigInteger nDerivations2 = chartCR.get(mid, max);
					update(chartSR, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSR.get(min, mid);
					BigInteger nDerivations2 = chartCL.get(mid, max);
					update(chartSM, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSR.get(min, mid);
					BigInteger nDerivations2 = chartCH.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSL.get(min, mid);
					BigInteger nDerivations2 = chartCR.get(mid, max);
					update(chartSM, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSL.get(min, mid);
					BigInteger nDerivations2 = chartCL.get(mid, max);
					update(chartSL, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSL.get(min, mid);
					BigInteger nDerivations2 = chartCH.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSM.get(min, mid);
					BigInteger nDerivations2 = chartCR.get(mid, max);
					update(chartSM, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSM.get(min, mid);
					BigInteger nDerivations2 = chartCL.get(mid, max);
					update(chartSM, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSM.get(min, mid);
					BigInteger nDerivations2 = chartCH.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSU.get(min, mid);
					BigInteger nDerivations2 = chartCR.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSU.get(min, mid);
					BigInteger nDerivations2 = chartCL.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				for (int mid = min + 2; mid < max; mid++) {
					BigInteger nDerivations1 = chartSU.get(min, mid);
					BigInteger nDerivations2 = chartCH.get(mid, max);
					update(chartSU, min, max, nDerivations1, nDerivations2);
				}

				// Cover sequences.
				{
					// Adds the arc min -> max
					BigInteger nDerivations1 = chartSR.get(min, max);
					update(chartCR, min, max, nDerivations1);
				}

				{
					// Adds the arc max -> min
					BigInteger nDerivations1 = chartSL.get(min, max);
					update(chartCL, min, max, nDerivations1);
				}

				{
					// Adds the arc min -> max
					BigInteger nDerivations1 = chartSM.get(min, max);
					update(chartCR, min, max, nDerivations1);
				}

				{
					// Adds the arc max -> min
					BigInteger nDerivations1 = chartSM.get(min, max);
					update(chartCL, min, max, nDerivations1);
				}

				{
					// Adds the arc min -> max
					BigInteger nDerivations1 = chartSU.get(min, max);
					update(chartCR, min, max, nDerivations1);
				}

				{
					// Adds the arc max -> min
					BigInteger nDerivations1 = chartSU.get(min, max);
					update(chartCL, min, max, nDerivations1);
				}
			}
		}

		BigInteger result = BigInteger.ZERO;
		result = result.add(chartCR.get(0, nNodes - 1));
		result = result.add(chartCL.get(0, nNodes - 1));
		result = result.add(chartSR.get(0, nNodes - 1));
		result = result.add(chartSL.get(0, nNodes - 1));
		result = result.add(chartSM.get(0, nNodes - 1));
		result = result.add(chartSU.get(0, nNodes - 1));
		return result;
	}
}
