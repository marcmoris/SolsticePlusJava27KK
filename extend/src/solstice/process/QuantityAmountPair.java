/*
 * Created on 2005-08-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;

/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class QuantityAmountPair extends AmountPair
{
	/**
	 *	Construct KeyValue Pair
	 *  @param value value
	 *  @param name string representation
	 */
	public QuantityAmountPair(BigDecimal Quantity, BigDecimal amount)
	{
		super(amount);
		m_Quantity = Quantity;
		if (m_Quantity == null)
			m_Quantity = new BigDecimal(0);
	}   //  QuantityNamePair

	/** The Quantity       */
	private BigDecimal m_Quantity = null;

	/**
	 *	Get Quantity
	 *  @return Quantity
	 */
	public BigDecimal getQuantity()
	{
		return m_Quantity;
	}	//	getQuantity

	/**
	 *	Get ID
	 *  @return Quantity
	 */
	public BigDecimal getID()
	{
		return m_Quantity;
	}	//	getID

	/**
	 *	Equals
	 *  @param obj Object
	 *  @return true, if equal
	 */
	public boolean equals(Object obj)
	{
		if (obj instanceof QuantityAmountPair)
		{
			QuantityAmountPair pp = (QuantityAmountPair)obj;
			if (pp.getAmount() != null && pp.getQuantity() != null &&
				pp.getAmount().equals(getAmount()) && pp.getQuantity().equals(m_Quantity))
				return true;
			return false;
		}
		return false;
	}	//	equals

	/**
	 *  Return Hashcode of Quantity
	 *  @return hascode
	 */
	public int hashCode()
	{
		return m_Quantity.hashCode();
	}   //  hashCode

}	//	QuantityAmountPair

