package solstice.process.report;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

/**
 * Méthodes d'alignment pour du texte
 */
public class AlignmentMethod
{

    /**
     * Aligne un texte à droite
     */
    public static void alignRight(Graphics2D g, Object value, Font font, int x, int y)
    {
        if(value != null && !(value instanceof DBNull) && !"".equals(value))
        {
            // Pour aligner un texte, on a besoin de connaitre l'espace que prendra la chaine de
            // caractères. On utilise donc les fonctions de mesure qu'il y a dans le Graphics selon
            // la police de caractères utilisée pour l'impression.
            String stringValue = String.valueOf(value);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            int width = fontMetrics.stringWidth(stringValue);
            g.setFont(font);
            g.drawString(stringValue, x - width, y);
        }
    }
    
    /**
     * Aligne un texte au centre
     */
    public static void alignCenter(Graphics2D g, Object value, Font font, int x, int y)
    {
        if(value != null && !(value instanceof DBNull) && !"".equals(value))
        {
            // Pour aligner un texte, on a besoin de connaitre l'espace que prendra la chaine de
            // caractères. On utilise donc les fonctions de mesure qu'il y a dans le Graphics selon
            // la police de caractères utilisée pour l'impression.
            String stringValue = String.valueOf(value);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            int width = fontMetrics.stringWidth(stringValue) / 2;
            g.setFont(font);
            g.drawString(stringValue, x - width, y);
        }
    }

    /**
     * Aligne un nombre à deux décimales selon la position de la virgule
     */
    public static void alignCurrency(Graphics2D g, Object value, Font font, int x, int y)
    {
        if(value != null)
        {
            if(value instanceof Number)
            {
                // On formatte d'abord le nombre avec 2 chiffres après la virgule
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String stringValue = decimalFormat.format(value).replace(',', '.');
                int dotIndex = stringValue.indexOf('.');
                
                // On mesure ensuite la partie entière pour savoir comment aligner le chiffre
                String entier = stringValue.substring(0, dotIndex);
                FontMetrics fontMetrics = g.getFontMetrics(font);
                int width = fontMetrics.stringWidth(entier);
                
                // On dessine ensuite le chiffre en appliquant le décallage approprié
                g.setFont(font);
                g.drawString(stringValue, x - width, y);
            }
            else
            {
                // On n'a pas un chiffre, on affiche donc simplement la chaine de caractère sans formattage
                AlignmentMethod.alignLeft(g, value, font, x, y);
            }
        }
    }
    
    /**
     * Imprime un texte aligné à gauche
     */
    public static void alignLeft(Graphics2D g, Object value, Font font, int x, int y)
    {
        if(value != null && !(value instanceof DBNull) && !"".equals(value))
        {
            g.setFont(font);
            String stringValue = String.valueOf(value);
            g.drawString(stringValue, x, y);
        }
    }

}
