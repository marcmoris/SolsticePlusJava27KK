/*
 * Created on 2005-08-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AmountPair implements Comparator, Serializable, Comparable
{
	/**
	 *  Protected Constructor
	 *  @param   name    (Display) Name of the Pair
	 */
	protected AmountPair (BigDecimal amount)
	{
		m_amount = amount;
		if (m_amount == null)
			m_amount = new BigDecimal(0);
	}   //  amountPair

	/** The amount        */
	private BigDecimal  m_amount;

	/**
	 *  Returns display value
	 *  @return amount
	 */
	public BigDecimal getAmount()
	{
		return m_amount;
	}   //  getamount

	/**
	 *  Returns Key or Value as String
	 *  @return String or null
	 */
	public abstract BigDecimal getID();

	/**
	 *	Comparator Interface (based on toString value)
	 *  @param o1 Object 1
	 *  @param o2 Object 2
	 *  @return compareTo value
	 */
	public int compare (Object o1, Object o2)
	{
//		BigDecimal s1 = (BigDecimal)o1 == null ? new BigDecimal(0) : o1;
//		BigDecimal s2 = (BigDecimal)o2 == null ? new BigDecimal(0) : o2;
		return 0; //s1.compareTo (s2);    //  sort order ??
	}	//	compare

	/**
	 * 	Comparable Interface (based on toString value)
	 *  @param   o the Object to be compared.
	 *  @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 */
	public int compareTo (Object o)
	{
		return compare (this, o);
	}	//	compareTo

	/**
	 *	To String - returns amount
	 *  @return amount
	 */
	public String toString()
	{
		return m_amount.toString();
	}	//	toString

	/**
	 *	To String - detail
	 *  @return String in format ID=amount
	 */
	public String toStringX()
	{
		StringBuffer sb = new StringBuffer (getID().toString());
		sb.append("=").append(m_amount);
		return sb.toString();
	}	//	toStringX

}	//	amountPair
