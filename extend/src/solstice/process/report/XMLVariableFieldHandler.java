package solstice.process.report;

import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Définition d'un champ variable
 */
public class XMLVariableFieldHandler extends XMLFieldHandler
{
    
    private int labelX;
    private int labelY;
    private int labelAlign;
    private XMLFieldTableHandler parentTable;

    /**
     * Constructeur
     */
    XMLVariableFieldHandler(String id, int x, int y, int align, int labelX, int labelY, int labelAlign, int width, Font font,
            String specialMethod, XMLReportHandler parentReport, XMLFieldTableHandler parentTable)
    {
        super(id, x, y, align, width, font, 3, specialMethod, parentReport);
        this.labelX = labelX;
        this.labelY = labelY;
        this.labelAlign = labelAlign;
        this.parentTable = parentTable;
    }
    
    /**
     * La position en X de l'étiquette
     */
    public int getLabelX()
    {
        return this.labelX;
    }
    
    /**
     * La position en Y de l'étiquette
     */
    public int getLabelY()
    {
        return this.labelY;
    }
    
    /**
     * Alignement de l'étiquette
     */
    public int getLabelAlign()
    {
        return this.labelAlign;
    }

    /**
     * La table parent
     */
    public XMLFieldTableHandler getParentTable()
    {
        return this.parentTable;
    }
    
    /**
     * Imprime le champ variable
     */
    public void printLabel(Graphics2D g, String label, int paddingX, int paddingY)
    {
        // Ensuite, on imprime l'étiquette
        switch(this.labelAlign)
        {
            case XMLFieldHandler.ALIGN_CENTER:
            {
                AlignmentMethod.alignCenter(g, label, this.getFont(), this.labelX + paddingX, this.labelY + paddingY);
                break;
            }
            case XMLFieldHandler.ALIGN_RIGHT:
            {
                AlignmentMethod.alignRight(g, label, this.getFont(), this.labelX + paddingX, this.labelY + paddingY);
                break;
            }
            default:
            {
                AlignmentMethod.alignLeft(g, label, this.getFont(), this.labelX + paddingX, this.labelY + paddingY);
                break;
            }
        }
    }
    
}
