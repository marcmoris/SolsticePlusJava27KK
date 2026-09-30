package solstice.process.report;

import java.awt.Graphics2D;

/**
 * Méthode spéciales d'impression. Ces méthodes doivent toutes avoir un prototype
 * semblable à celui-ci :
 * 
 *      public static void METHOD(Graphics2D g, XMLFieldHandler field,
 *              Object value, int paddingX, int paddingY);
 */
public class CustomReportMethod
{

    /**
     * Dessine le MODIFIÉ si le formulaire est de type M
     */
    public static void drawModified(Graphics2D g, XMLFieldHandler field, Object value, int paddingX, int paddingY)
    {
        if("M".equals(value))
        {
            AlignmentMethod.alignCenter(g, "MODIFIÉ", field.getFont(), field.getX() + paddingX, field.getY() + paddingY);
        }
    }
    
    /**
     * Dessine le bon code de relevé à partir du type de formulaire
     */
    public static void drawCodeReleve(Graphics2D g, XMLFieldHandler field, Object value, int paddingX, int paddingY)
    {
        g.setFont(field.getFont());
        if("O".equals(value))
            g.drawString("0", paddingX + field.getX(), paddingY + field.getY());
        else if("M".equals(value))
            g.drawString("1", paddingX + field.getX(), paddingY + field.getY());
    }
    
}
