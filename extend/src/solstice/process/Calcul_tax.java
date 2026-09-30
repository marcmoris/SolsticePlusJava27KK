/*
 * Created on 28-Feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.util.Hashtable;

import org.compiere.util.Env;

/**
 * @author marcmori
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Calcul_tax {

	public static BigDecimal getDeductionEmployeeAmountMethod ( Hashtable s_Employee, String Method)
	{
		if ( s_Employee.containsKey( Method ) ) {
			if ( ((BigDecimal)s_Employee.get( Method )).compareTo( Env.ZERO) < 0 )
				return new BigDecimal( 0 );
			return (BigDecimal)s_Employee.get( Method );
		}	
		return new BigDecimal( 0 );

	}

	public static BigDecimal getDeductionEmployerAmountMethod ( Hashtable s_Employer, String Method)
	{
		if ( s_Employer.containsKey( Method ) )
			return (BigDecimal)s_Employer.get( Method );
		
		return new BigDecimal( 0 );

	}

	public static BigDecimal getDeductionSalaryAdmissibleMethod ( Hashtable s_SalaryAdmissible,  String Method )
	{
		if (  s_SalaryAdmissible.containsKey( Method ) )
			return (BigDecimal) s_SalaryAdmissible.get( Method );
		return new BigDecimal( 0 );
	}

	
	public static BigDecimal get_matrix_hoursAdmissible ( Hashtable s_Matrix, int columnAdmin )
	{
		String sColumnAdmin = String.valueOf( columnAdmin );
		QuantityAmountPair vAdm;
		if ( s_Matrix.containsKey(  sColumnAdmin ))
		{
			vAdm = (QuantityAmountPair)s_Matrix.get( sColumnAdmin );
			return vAdm.getQuantity();
		}
		return new BigDecimal( 0 );
		
	}   // get_matrix
	
	public static BigDecimal get_matrix_salaryAdmissible ( Hashtable s_Matrix, int columnAdmin )
	{
		String sColumnAdmin = String.valueOf( columnAdmin );
		QuantityAmountPair vAdm;
		if ( s_Matrix.containsKey(  sColumnAdmin ))
		{
			vAdm = (QuantityAmountPair)s_Matrix.get( sColumnAdmin );
			return vAdm.getAmount();
		}
		return new BigDecimal( 0 );
		
	}   // get_matrix


}
