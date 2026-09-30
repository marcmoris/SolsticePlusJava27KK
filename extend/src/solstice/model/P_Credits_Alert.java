package solstice.model;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.X_P_Credits_Alert;


/**
 *  Credits_Alert Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Credits_Alert extends X_P_Credits_Alert
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Credits_Alert
	 *	@param ctx context
	 * 	@param C_Credits_Alert_ID id
	 *	@return Credits_Alert
	 */
	public static P_Credits_Alert get (Properties ctx, int P_Credits_Alert_ID, String trxName)
	{
		Integer key = new Integer (P_Credits_Alert_ID);
		P_Credits_Alert Credits_Alert = (P_Credits_Alert)s_cache.get(key);
		if (Credits_Alert != null)
			return Credits_Alert;
		Credits_Alert = new P_Credits_Alert (ctx, P_Credits_Alert_ID, trxName);
		s_cache.put (key, Credits_Alert);
		return Credits_Alert;
	}	//	get

	public static P_Credits_Alert[] getFromCreditsMethod(Properties ctx, int iP_Credits_ID, int iP_Method_Credits_ID, String trxNamne)
	{
		P_Credits_Alert[] retValue = null;
		int num = 0;
		String sql = "SELECT P_Credits_Alert_ID FROM P_Credits_Alert "
			+ " WHERE P_Credits_ID = "+iP_Credits_ID
			+ " AND P_Method_Credits_ID = "+iP_Method_Credits_ID;
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
			{
				if(rs.getObject("P_Credits_Alert_ID") != null && rs.getInt("P_Credits_Alert_ID") > 0)
				{
					retValue[num] = P_Credits_Alert.get(Env.getCtx(), rs.getInt("P_Credits_Alert_ID"), null);
					num++;
				}
			}
		}
		catch(SQLException e)
		{
			System.out.println("P_Credits_Alert - getFromCredits"+e.toString());
		}
		return retValue;
	}
	/**	Cache						*/
	private static CCache<Integer,P_Credits_Alert>	s_cache = new CCache<Integer,P_Credits_Alert>("P_Credits_Alert", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Credits_Alert_ID id
	 */
	public P_Credits_Alert (Properties ctx, int P_Credits_Alert_ID, String trxName)
	{
		super (ctx, P_Credits_Alert_ID, trxName);
		if (P_Credits_Alert_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Credits_Alert

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Credits_Alert (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Credits_Alert (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Credits_Alert_ID"), trxName);
	}	//	P_Credits_Alert

	protected boolean beforeSave(boolean newRecord)
	{
		boolean retValue = true;
		//on verifie pour etre sur qu'il n'y a pas de recursivite dans
		//le chainage, s'il y a lieu
		//on commence avec la banque jointe et la methode de l'alerte courante
		int iP_Credits_alert_Join = this.getP_Credits_Join_ID();
		int iP_Method_Credits_ID = this.getP_Method_Credits_ID();
		Vector<Integer> vectPCreditJoint = new Vector<Integer>();
		
//		retValue = checkForRecursive(iP_Credits_alert_Join, iP_Method_Credits_ID, vectPCreditJoint);
		if(retValue == false)
		{
			
		}
		return retValue;
	}

	private boolean checkForRecursive(int iP_Credits_alert_Join, int iP_Method_Credits_ID, Vector<Integer> vectPCreditJoint)
	{
		boolean isOK = true;
		boolean allDone = false;
		//on boucle tant qu'on va trouver des banques jointes
		while(isOK == true && allDone == false)
		{
			//si ya une banque jointe
			if(iP_Credits_alert_Join != 0)
			{
				//on verifie pour tous les banques a dates
				for(int idx=0;idx<vectPCreditJoint.size();idx++)
				{
					//si la banque courante a deja ete visitee = BUG RECURSIVITE
					if(((Integer)vectPCreditJoint.get(idx)).intValue() == iP_Credits_alert_Join)
					{
						isOK = false;
					}
				}
				if(isOK == true)
				{
					//rendu ici, on prepare le nouveau tours de boucle
					//on ajoute la banque courante dans la liste de tout celles visitees
					vectPCreditJoint.add(new Integer(iP_Credits_alert_Join));
					//on va chercher les alertes de banque + profile
					P_Credits_Alert pca[] = P_Credits_Alert.getFromCreditsMethod(Env.getCtx(), 
							iP_Credits_alert_Join,
							iP_Method_Credits_ID, 
							null);
					//de l'Alerte, on va chercher la prochaine banque jointe
					if(pca != null)
					{
						for(int idx=0;idx<pca.length;idx++)
						{
							iP_Credits_alert_Join = pca[idx].getP_Credits_Join_ID();
							isOK = checkForRecursive(iP_Credits_alert_Join, iP_Method_Credits_ID, vectPCreditJoint);
						}
					}
					else
						iP_Credits_alert_Join = 0;
				}
			}
			//si ya pas de banque jointe, la verif est terminee
			else
			{
				allDone = true;
			}
		}
		return isOK;		
		
	}

}	//	P_Credits_Alert
