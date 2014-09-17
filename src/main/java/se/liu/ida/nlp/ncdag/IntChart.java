/*
 * See the file "LICENSE" for the full license governing this code.
 */
package se.liu.ida.nlp.ncdag;

/**
 *
 * @author Marco Kuhlmann <marco.kuhlmann@liu.se>
 */
public class IntChart {

	private final int[][] chart;

	public IntChart(int size) {
		this.chart = new int[size][];
		for (int i = 0; i < size; i++) {
			chart[i] = new int[size - i];
		}
	}

	public int get(int min, int max) {
		return chart[min][max - min];
	}

	public void set(int min, int max, int value) {
		chart[min][max - min] = value;
	}
}
