/*
 * See the file "LICENSE" for the full license governing this code.
 */
package se.liu.ida.nlp.ncdag;

import java.math.BigInteger;

/**
 *
 * @author Marco Kuhlmann <marco.kuhlmann@liu.se>
 */
public class BigIntegerChart {

	private final BigInteger[][] chart;

	public BigIntegerChart(int size) {
		this.chart = new BigInteger[size][];
		for (int i = 0; i < size; i++) {
			chart[i] = new BigInteger[size - i];
		}
	}

	public BigInteger get(int min, int max) {
		BigInteger b = chart[min][max - min];
		if (b == null) {
			b = BigInteger.ZERO;
		}
		return b;
	}

	public void set(int min, int max, BigInteger value) {
		chart[min][max - min] = value;
	}

	public void add(int min, int max, BigInteger value) {
		BigInteger b = chart[min][max - min];
		if (b == null) {
			b = BigInteger.ZERO;
		}
		chart[min][max - min] = b.add(value);
	}
}
