package se.liu.ida.nlp.ncdag;

import java.math.BigInteger;

public class Main {

	private static BigInteger count(int nNodes) {
		return Counter.getInstance().getNDerivations(nNodes);
	}

	public static void main(String[] args) {
		int nNodes = Integer.parseInt(args[0]);
		StringBuilder sb = new StringBuilder();
		sb.append(count(1));
		for (int i = 2; i <= nNodes; i++) {
			sb.append(", ");
			sb.append(count(i));
		}
		System.out.println(sb);
		System.exit(0);
	}
}
