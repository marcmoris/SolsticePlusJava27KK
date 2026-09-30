/*
 * Created on 2005-08-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Booklet_Detail extends X_P_Booklet_Detail implements IBookletTimeSheetDetail
{

    /**
     * @param ctx
     * @param P_Booklet_Detail_ID
     * @param trxName
     */
    public P_Booklet_Detail(Properties ctx, int P_Booklet_Detail_ID,
            String trxName)
    {
        super(ctx, P_Booklet_Detail_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Booklet_Detail(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

    public void setRecord_ID(int record_ID) {this.setP_Booklet_Detail_ID(record_ID);}
    public void setParent_ID(int parent_ID) {this.setP_Booklet_ID(parent_ID);}
    public int getRecord_ID() {return this.getP_Booklet_Detail_ID();}
    public int getParent_ID() {return this.getP_Booklet_ID();}
}
