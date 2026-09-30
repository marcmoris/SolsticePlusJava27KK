package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import solstice.model.*;

public class P_TaxCopy extends SvrProcess
{
 
	private Timestamp effectIn;
	private int Tax_ID = 0;
	private String trxName = null;
	private P_Tax Tax;
	private int count;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("EffectIn"))
				effectIn = (Timestamp)para[i].getParameter();
			else
				log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
		Tax_ID = this.getRecord_ID();
	}	//	prepare

	
	protected String doIt () throws Exception
	{
		String ret = "";
		Tax = new P_Tax( getCtx(), -1, null);
		Tax.setEffectIn(effectIn);
		Tax.setEffectinStr(effectIn.toString().substring(0,10));
		Tax.setIsActive(true);
		Tax.setAD_Org_ID(0);
		Tax.save();

		Copy_Tax_Federal();
		Copy_Tax_Federal_Ei();
		Copy_Tax_Federal_Rate();
		Copy_Tax_Federal_Rpc();
		Copy_Tax_Rrq();
		Copy_Tax_Rqap();
		Copy_Tax_Federal_Td1();
		Copy_Tax_Provincial();
		
		count++;
		ret = "@Inserted@ " + count;
		return ret;
		
	}	

	private void Copy_Tax_Federal()
	{
		String sql = "Select * From P_TAX_FEDERAL WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Federal Tax_Federal;

			while (rs.next ()) 
			{
				Tax_Federal = new X_P_Tax_Federal( getCtx(), -1, trxName);
				Tax_Federal.setAD_Org_ID(0);
				Tax_Federal.setAD_Message_ID(rs.getInt("AD_Message_ID"));
				Tax_Federal.setAmount(rs.getBigDecimal("Amount"));
				Tax_Federal.setIsActive(true);
				Tax_Federal.setP_Tax_ID(Tax.getP_Tax_ID());
				Tax_Federal.setValue(rs.getString("Value"));
				Tax_Federal.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
	}

	private void Copy_Tax_Federal_Ei()
	{
		String sql = "Select * From P_Tax_Federal_Ei WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Federal_Ei Tax_Federal_Ei;

			while (rs.next ()) 
			{
				Tax_Federal_Ei = new X_P_Tax_Federal_Ei( getCtx(), -1, trxName);
				Tax_Federal_Ei.setAD_Message_ID(rs.getInt("AD_Message_ID"));
				Tax_Federal_Ei.setAD_Org_ID(0);
				Tax_Federal_Ei.setAmount(rs.getBigDecimal("Amount"));
				Tax_Federal_Ei.setIsActive(true);
				Tax_Federal_Ei.setP_Tax_ID(Tax.getP_Tax_ID());
				Tax_Federal_Ei.setValue(rs.getString("Value"));
				Tax_Federal_Ei.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
		
	}
	
	private void Copy_Tax_Federal_Rate()
	{
		String sql = "Select * From P_Tax_Federal_Rate WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			P_Tax_Federal_Rate Tax_Federal_Rate;

			while (rs.next ()) 
			{
				Tax_Federal_Rate = new P_Tax_Federal_Rate( getCtx(), -1, trxName);
				Tax_Federal_Rate.setAD_Org_ID(0);
				Tax_Federal_Rate.setFederal_Constant_K(rs.getBigDecimal("Federal_Constant_K"));
				Tax_Federal_Rate.setFederal_Tax_Rate(rs.getBigDecimal("Federal_Tax_Rate"));
				Tax_Federal_Rate.setIsActive(true);
				Tax_Federal_Rate.setLimit_Max(rs.getBigDecimal("Limit_Max"));
				Tax_Federal_Rate.setLimit_Min(rs.getBigDecimal("Limit_Min"));
				Tax_Federal_Rate.setP_Tax_ID(Tax.getP_Tax_ID());
				Tax_Federal_Rate.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
	}
	
	private void Copy_Tax_Federal_Rpc()
	{
		String sql = "Select * From P_Tax_Federal_Rpc WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Federal_Rpc Tax_Federal_Rpc;

			while (rs.next ()) 
			{
				Tax_Federal_Rpc = new X_P_Tax_Federal_Rpc( getCtx(), -1, trxName);
				Tax_Federal_Rpc.setAD_Message_ID(rs.getInt("AD_Message_ID"));
				Tax_Federal_Rpc.setAD_Org_ID(0);
				Tax_Federal_Rpc.setAmount(rs.getBigDecimal("Amount"));
				Tax_Federal_Rpc.setIsActive(true);
				Tax_Federal_Rpc.setP_Tax_ID(Tax.getP_Tax_ID());
				Tax_Federal_Rpc.setValue(rs.getString("Value"));
				Tax_Federal_Rpc.save();
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
	}
	
	private void Copy_Tax_Rrq()
	{
		String sql = "Select * From P_Tax_RRQ WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_RRQ Tax_RRQ;

			while (rs.next ()) 
			{
				Tax_RRQ = new X_P_Tax_RRQ( getCtx(), -1, trxName);
				Tax_RRQ.setAD_Message_ID(rs.getInt("AD_Message_ID"));
				Tax_RRQ.setAD_Org_ID(0);
				Tax_RRQ.setAmount(rs.getBigDecimal("Amount"));
				Tax_RRQ.setIsActive(true);
				Tax_RRQ.setP_Tax_ID(Tax.getP_Tax_ID());
				Tax_RRQ.setValue(rs.getString("Value"));
				Tax_RRQ.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
	}

	private void Copy_Tax_Rqap()
	{
		String sql = "Select * From P_Tax_RQAP WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_RQAP Tax_RQAP;

			while (rs.next ()) 
			{
				Tax_RQAP = new X_P_Tax_RQAP( getCtx(), -1, trxName);
				Tax_RQAP.setAD_Message_ID(rs.getInt("AD_Message_ID"));
				Tax_RQAP.setAD_Org_ID(0);
				Tax_RQAP.setAmount(rs.getBigDecimal("Amount"));
				Tax_RQAP.setIsActive(true);
				Tax_RQAP.setP_Tax_ID(Tax.getP_Tax_ID());
				Tax_RQAP.setValue(rs.getString("Value"));
				Tax_RQAP.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
	}

	private void Copy_Tax_Federal_Td1()
	{
		String sql = "Select * From P_Tax_Federal_TD1 WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Federal_Td1 Tax_Federal_TD1;

			while (rs.next ()) 
			{
				Tax_Federal_TD1 = new X_P_Tax_Federal_Td1( getCtx(), -1, trxName);
				Tax_Federal_TD1.setAD_Message_ID(rs.getInt("AD_Message_ID"));
				Tax_Federal_TD1.setAD_Org_ID(0);
				Tax_Federal_TD1.setAmount(rs.getBigDecimal("Amount"));
				Tax_Federal_TD1.setIsActive(true);
				Tax_Federal_TD1.setP_Tax_ID(Tax.getP_Tax_ID());
				Tax_Federal_TD1.setValue(rs.getString("Value"));
				Tax_Federal_TD1.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
	}

	
	private void Copy_Tax_Provincial()
	{
		String sql = "Select * From P_Tax_Provincial WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Provincial Tax_Provincial;

			while (rs.next ()) 
			{
				Tax_Provincial = new X_P_Tax_Provincial( getCtx(), -1, trxName);
				Tax_Provincial.setC_Region_ID(rs.getInt("C_Region_ID"));
				Tax_Provincial.setIsActive(true);
				Tax_Provincial.setP_Tax_ID(Tax.getP_Tax_ID());
				Tax_Provincial.save();

				Copy_Tax_Provincial_Rate( rs.getInt("P_Tax_Provincial_ID"), Tax_Provincial.getP_Tax_Provincial_ID());
				Copy_Tax_Provincial_Td1( rs.getInt("P_Tax_Provincial_ID"), Tax_Provincial.getP_Tax_Provincial_ID());

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
	}

	private void Copy_Tax_Provincial_Rate( int OldID, int P_Tax_Provincial_ID)
	{
		String sql = "Select * From P_Tax_Provincial_Rate WHERE P_Tax_Provincial_ID =  " + OldID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Provincial_Rate Tax_Provincial;

			while (rs.next ()) 
			{
				Tax_Provincial = new X_P_Tax_Provincial_Rate( getCtx(), -1, trxName);
				Tax_Provincial.setAD_Org_ID(0);
				Tax_Provincial.setIsActive(true);
				Tax_Provincial.setLimit_Max(rs.getBigDecimal("Limit_Max"));
				Tax_Provincial.setLimit_Min(rs.getBigDecimal("Limit_Min"));
				Tax_Provincial.setP_Tax_Provincial_ID(P_Tax_Provincial_ID);
				Tax_Provincial.setProvincial_Constant_K(rs.getBigDecimal("Provincial_Constant_K"));
				Tax_Provincial.setProvincial_Tax_Rate(rs.getBigDecimal("Provincial_Tax_Rate"));
				Tax_Provincial.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
	}
	
	private void Copy_Tax_Provincial_Td1( int OldID, int P_Tax_Provincial_ID)
	{
		String sql = "Select * From P_Tax_Provincial_Td1 WHERE P_Tax_Provincial_ID =  " + OldID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Provincial_TD1 Tax_Provincial;

			while (rs.next ()) 
			{
				Tax_Provincial = new X_P_Tax_Provincial_TD1( getCtx(), -1, trxName);
				Tax_Provincial.setAD_Org_ID(0);
				Tax_Provincial.setIsActive(true);
				Tax_Provincial.setP_Tax_Provincial_ID(P_Tax_Provincial_ID);
				Tax_Provincial.setAD_Message_ID(rs.getInt("AD_Message_ID"));
				Tax_Provincial.setAD_Org_ID(0);
				Tax_Provincial.setAmount(rs.getBigDecimal("Amount"));
				Tax_Provincial.setValue(rs.getString("Value"));
				Tax_Provincial.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxCopy - " + sql, e);
		}
		
	}

	
}
