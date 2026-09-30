/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.custom;

import java.util.Properties;

/**
 * @author frafor01
 *
 * Cet inferface founit des méthodes utilitaires et des renseignements sur la
 * connexion d'un utilisateur par les interface WEB
 */
public interface IUserInfo
{
    /*
     * Renseignements
     */
    public int getUserId();
    public int getEmployeeId();
    public String getEmployeeName();
    public String getEmployeeValue();
    public String getEmployeeSQL();
    public String getEmployeeSQL(String additionalCriteria);
    public String getDistributionSQL(String[] selectedFields, boolean orderByValue);
    public String getDistributionSQLFromER(String[] selectedFields, boolean orderByValue);
    public String getDistributionWebSQL(String[] selectedFields, boolean orderByValue);
    public String getDistributionWebSQLAbsence(String[] selectedFields, boolean orderByValue);
    
    public boolean hasPolicy(String key);

    public void setUserId( int UserId );

    //+2011.02.16 Return ctx.
    public Properties getCtx();
    //-2011.02.16 

}
