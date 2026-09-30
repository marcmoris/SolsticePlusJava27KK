/******************************************************************************
 * Product: Solstice
 * 
 *****************************************************************************/
package org.solstice;

import org.compiere.util.*;

/**
 *  Solstice Control Class
 *
 *  @author Marc Morissette
 *  @version $Id: Solstice.java,v 1.1 2007/07/18 15:01:03 marmor01 Exp $
 */
public final class Solstice //extends org.compiere.Compiere 
{
	static private final String	s_file100x30	= "images/S10030.png";
	static private final String	s_file100x30HR	= "images/S10030HR.png";
	/** 48*15 Product Image.    	
	/** Support Email           */
	static private String		s_supportEmail	= "";

	/** Subtitle                */
	static public final String	SUB_TITLE		= " Paie et Ressource Humaine ";
	static public final String	COPYRIGHT		= "\u00A9 2007 Solstice \u00AE";

	/**	Logging								*/
	private static CLogger		log = null;

	/**
	 *  Main Method
	 *
	 *  @param args optional start class
	 */
	public static void main (String[] args)
	{
		org.solstice.util.Splash.getSplash();
//		startup(true);     //  error exit and initUI

		//  Start with class as argument - or if nothing provided with Client
		String className = "org.compiere.Apps.AMenu";
		for (int i = 0; i < args.length; i++)
		{
			if (!args[i].equals("-debug"))  //  ignore -debug
			{
				className = args[i];
				break;
			}
		}
		//
		try
		{
			Class<?> startClass = Class.forName(className);
			startClass.newInstance();
		}
		catch (Exception e)
		{
			System.err.println("Solstice starting: " + className + " - " + e.toString());
			e.printStackTrace();
		}
	}   //  main
}	//	Solstice
