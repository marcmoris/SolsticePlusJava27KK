/*
 * Created on 29 août 2005
 */
package solstice.custom;

/**
 * @author frafor01
 *
 * Cet inferface founit des méthodes utilitaires et des renseignements sur la
 * connexion d'un utilisateur par les interface WEB
 */
public interface IUserInfo_Old
{
    /*
     * Renseignements
     */
    public int getUserId();
    public String getUserName();
    public String getUserEMail();
    public int getRoleId();
    public int getEmployeeId();
    public int getManagerId();
    public String getEmployeeName();
    public String getEmployeeValue();
    public int getDistributionBookletId();
    public int[] getDistributionList();
    public boolean isSubstitute();
    
    /*
     * Méthodes utilitaires
     */
    
    /**
     * Cette méthode retourne la liste des employés que l'employé courrant peut consulter.
     * P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'
     */
    public String getEmployeeSQL();
    
    /**
     * Renvoit un select retournant tous les codes de distribution
     */
    public String getDistributionSQL(String[] selectedFields);
    public String getDistributionSQL(String[] selectedFields, boolean orderByValue);
}
