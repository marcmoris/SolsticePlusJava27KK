/*
 * Created on 1 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import org.compiere.model.MRole;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupCMDescription extends AbstractKNPModel {
	public LookupCMDescription() {
        String sql = "SELECT P_Credits_Movement_Description_ID, Name FROM P_Credits_Movement_Description WHERE IsActive = 'Y'";
        
		sql = MRole.getDefault().addAccessSQL (sql, "P_Credits_Movement_Description", true, false);	// fully qualidfied - RO 

        genericLoadFromSQL(sql);
	}
}
