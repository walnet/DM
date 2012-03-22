/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * The sigmoid unit class
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public class SigmoidUnit {

	public enum UnitType {
		UNIT_NULL, UNIT_INPUT, UNIT_HIDDEN, UNIT_OUTPUT,
	}

	private ArrayList<Double> ws;
	private double errorTerm;
	private double result;

	public SigmoidUnit(int size) {
		ws = new ArrayList<Double>(size);
		// Randomize
		for (int i = 0; size > i; ++i) {
			ws.add(Math.random());
		}
	}

	public double calculateOutput(ArrayList<Double> inputs) {
		double net = 0;
		for (int i = 0; inputs.size() > i; ++i) {
			net += inputs.get(i) * ws.get(i);
		}
		result = 1 / (1 + Math.exp(-net));
		return result;
	}

	/**
	 * @return the errorTerm
	 */
	public double getErrorTerm() {
		return errorTerm;
	}

	/**
	 * @return the result
	 */
	public double getResult() {
		return result;
	}

	/**
	 * @return the ws
	 */
	public ArrayList<Double> getWs() {
		return ws;
	}

	/**
	 * @param errorTerm
	 *            the errorTerm to set
	 */
	public void setErrorTerm(double errorTerm) {
		this.errorTerm = errorTerm;
	}

	/**
	 * @param ws
	 *            the ws to set
	 */
	public void setWs(ArrayList<Double> ws) {
		this.ws = ws;
	}

	public void trace() {
		System.out.print("[");
		DecimalFormat df = new DecimalFormat("#.00##");
		for (int i = 0; ws.size() > i; ++i) {
			String fmt = df.format(ws.get(i));
			System.out.print(fmt + ",");
		}
		System.out.print("]");
	}

}
