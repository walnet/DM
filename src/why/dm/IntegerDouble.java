/**
 * Copyright (C) 2012 why
 */
package why.dm;

/**
 * The integer double class
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public class IntegerDouble implements Comparable<IntegerDouble> {

	private int intValue;
	private double doubleValue;
	
	public IntegerDouble() {
		this(0, 0.);
	}
	
	public IntegerDouble(int intValue, double doubleValue) {
		this.intValue = intValue;
		this.doubleValue = doubleValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int value) {
		intValue = value;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double value) {
		doubleValue = value;
	}

	@Override
	public int compareTo(IntegerDouble o) {
		return doubleValue > o.doubleValue ? 1 : (doubleValue == o.doubleValue ? 0 : -1);
		//return doubleValue <= o.doubleValue ? 1 : -1;
	}

}
