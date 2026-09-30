package solstice.process.report;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.lang.reflect.Method;

/**
 * Cette classe sert simplement à contenir les informations d'un
 * élément <field /> dans un xml de rapport
 */
public class XMLFieldHandler
{
 
    //
    // Type d'alignement possibles
    //
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_CURRENCY = 3;
    
    //
    // Type d'accès d'affichage
    //
    public static final int DISPLAYACCESS_EMPLOYEE = 1;
    public static final int DISPLAYACCESS_EMPLOYER = 2;

    private XMLReportHandler parentReport;
    private String id;
    private int x;
    private int y;
    private int width;
    private int align;
    private Font font;
    private int displayAccess = 3;
    private String specialMethod;
    
    /**
     * Constructeur
     */
    XMLFieldHandler(String id, int x, int y, int align, int width, Font font, int displayAccess, String specialMethod, XMLReportHandler parentReport)
    {
        this.parentReport = parentReport;
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.align = align;
        this.font = font;
        this.specialMethod = specialMethod;
        this.displayAccess = displayAccess;
    }

    /**
     * Le nom du champs associé
     */
    public String getID()
    {
        return this.id;
    }

    /**
     * La position relative en X
     */
    public int getX()
    {
        return this.x;
    }

    /**
     * La position relative en Y
     */
    public int getY()
    {
        return this.y;
    }
    
    /**
     * Alignement du champ
     */
    public int getAlign()
    {
        return this.align;
    }
    
    /**
     * La police de caractère à utiliser
     */
    public Font getFont()
    {
        return this.font;
    }
    
    /**
     * Le rapport qui possède ce champ
     */
    public XMLReportHandler getParentReport()
    {
        return this.parentReport;
    }
    
    /**
     * Imprime le champ
     */
    public void print(Graphics2D g, Object value, int paddingX, int paddingY, int printingAccess)
    {
        // Si on a l'accès pour imprimer
        if((printingAccess & this.displayAccess) != 0)
        {
            // On vérifie s'il y a un formattage spécial à faire. Si ce n'est pas le
            // cas, on imprime la valeur directement
            if(this.callSpecialMethod(g, value, paddingX, paddingY) == false)
            {
                if(value != null && "".equals(value) == false && !(value instanceof DBNull))
                {
                    // S'il y a une largeur de spécifié pour la chaine de caractère, on doit
                    // reformatter la sortie pour ajouter des retours de ligne entre chaque mot
                    if(this.width > 0)
                    {
                        this.printMultiline(g, String.valueOf(value), paddingX, paddingY);
                    }
                    else
                    {
                        switch(this.align)
                        {
                            case XMLFieldHandler.ALIGN_CENTER:
                            {
                                AlignmentMethod.alignCenter(g, value, this.font, paddingX + this.x, paddingY + this.y);
                                break;
                            }
                            case XMLFieldHandler.ALIGN_RIGHT:
                            {
                                AlignmentMethod.alignRight(g, value, this.font, paddingX + this.x, paddingY + this.y);
                                break;
                            }
                            case XMLFieldHandler.ALIGN_CURRENCY:
                            {
                                AlignmentMethod.alignCurrency(g, value, this.font, paddingX + this.x, paddingY + this.y);
                                break;
                            }
                            default:
                            {
                                AlignmentMethod.alignLeft(g, value, this.font, paddingX + this.x, paddingY + this.y);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Cette méthode est appelée pour faire une impression multiligne. Ce cas ce présente
     * lorsqu'on a spécifié une largeur pour le champ. On mesure alors les mots pour séparer
     * l'affichage sur plusieurs lignes.
     */
    private void printMultiline(Graphics2D g, String value, int paddingX, int paddingY)
    {
        g.setFont(this.font);
        FontMetrics fontMetrics = g.getFontMetrics();
        String wordBuffer = "";
        String lineBuffer = "";
        char[] input = value.toCharArray();
        for(int i = 0; i < input.length; i++)
        {
            // Une ligne peut être séparée selon certains caractères prédéfinis. Si
            // on rencontre l'un d'eux, on doit donc prendre une nouvelle mesure de
            // la ligne pour déterminer si on doit séparer la ligne.
            if(input[i] == ' ')
            {
                if(fontMetrics.stringWidth(lineBuffer + wordBuffer) > this.width)
                {
                    g.drawString(lineBuffer, this.x + paddingX, this.y + paddingY);
                    paddingY += fontMetrics.getHeight();
                    lineBuffer = wordBuffer + " ";
                }
                else
                {
                    lineBuffer += wordBuffer + " ";
                }
                wordBuffer = "";
            }
            else if(input[i] == '-' || input[i] == '.' || input[i] == ',')
            {
                wordBuffer += String.valueOf(input[i]);
                if(fontMetrics.stringWidth(lineBuffer + wordBuffer) > this.width)
                {
                    g.drawString(lineBuffer, this.x + paddingX, this.y + paddingY);
                    paddingY += fontMetrics.getHeight();
                    lineBuffer = wordBuffer;
                }
                else
                {
                    lineBuffer += wordBuffer;
                }
                wordBuffer = "";
            }
            else
            {
                wordBuffer += String.valueOf(input[i]);
            }
        }
        
        // Il reste à imprimer la dernière ligne en mémoire
        g.drawString(lineBuffer, this.x + paddingX, this.y + paddingY);
    }
    
    /**
     * Appelle la méthode spéciale pour formatter la sortie
     * @return Indique si la méthode a été exécutée correctement
     */
    private boolean callSpecialMethod(Graphics2D graphics, Object value, int paddingX, int paddingY)
    {
        if(this.specialMethod == null)
            return false;
        
        try
        {
            String className = this.specialMethod.substring(0, this.specialMethod.lastIndexOf("."));
            String methodName = this.specialMethod.substring(this.specialMethod.lastIndexOf(".") + 1);
      
            Class c = Class.forName(className);
            Method m = c.getMethod(methodName, new Class[] {Graphics2D.class, XMLFieldHandler.class, Object.class, int.class, int.class});
            m.invoke(null, new Object[] {graphics, this, value, new Integer(paddingX), new Integer(paddingY)});
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            return false;
        }
        
    }

}
