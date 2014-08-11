package se.liu.ida.nlp.ncdag;

import java.math.BigInteger;

public class Main {

	private static BigInteger count(int nNodes) {
		if (nNodes < 2) {
			return BigInteger.ONE;
		} else {
			Counter counter = new Counter();
			return counter.getNDerivations(nNodes);
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		int nNodes = Integer.parseInt(args[0]);
		StringBuilder sb = new StringBuilder();
		sb.append(BigInteger.ONE);
		for (int i = 2; i <= nNodes; i++) {
			sb.append(",");
			sb.append(count(i));
		}
		System.out.println(sb);
		System.exit(0);
	}
}
