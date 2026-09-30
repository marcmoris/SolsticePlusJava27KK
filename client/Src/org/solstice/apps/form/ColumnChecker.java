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

package org.solstice.apps.form;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;

/**
 * @author frafor01
 *
 * Cette classe sert à attraper l'évenement lorsque l'utilisateur clique
 * sur la première colonne du grid. Dans ce cas, on doit sélectioner tous
 * les enregistrements pour modifier le total dans le bas du formulaire
 */
public class ColumnChecker extends MouseAdapter
{
    private MiniTable miniTable;
    
    public ColumnChecker(MiniTable miniTable)
    {
        this.miniTable = miniTable;
    }
    
	public void mouseClicked (MouseEvent e)
	{
	    int columnIndex = this.miniTable.getColumnModel().getColumnIndexAtX(e.getX());
	    this.miniTable.selectAll();
	    
/*	    // Si on est sur la première colonne
	    if(columnIndex == 0) 
	    {  
	    	boolean allSelected = true;
	        for(int i = 0; i < this.miniTable.getRowCount(); i++)
	        {
	            // On coche toutes les rangées à la colonne 0
	            IDColumn column = (IDColumn)this.miniTable.getValueAt(i, 0);
	            if ( ! column.isSelected() )
	            	allSelected = false;
	        }

	    	for(int i = 0; i < this.miniTable.getRowCount(); i++)
	        {
	            // On coche toutes les rangées à la colonne 0
	            IDColumn column = (IDColumn)this.miniTable.getValueAt(i, 0);
	            if ( allSelected)
	            	column.setSelected(false);
	            else
	            	column.setSelected(true);
	            
	            this.miniTable.setValueAt(column, i, 0);
	        }
	    }
*/		
	}
}
