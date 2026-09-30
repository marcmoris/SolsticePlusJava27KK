package solstice.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import org.compiere.model.MLocation;
import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;

import solstice.process.PgiUtil;

/*
import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
*/
import org.w3c.dom.*;



/**
 *  Carra Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Carra extends X_P_Carra
{
	P_Employee Employee;
	P_Period PeriodStart;
	P_Period PeriodEnd;
	P_Year   Year;
	P_Employee_Deduction EmployeeDeduction;
	P_Deduction Deduction;
	P_Deduction_Param Deduction_Param ;
	
	/*
	Nombre de jour Cotisables
	Généralement 260 jours. À l’occasion, le nombre
	de jours cotisables pourrait être différent, par exemple, dans le cas d’une année de 27 paies, il y
	aurait 270 jours cotisables.
	*/

	int ContributoryDays = 260;

	
	/**
	 * 	Get Carra
	 *	@param ctx context
	 * 	@param P_Carra_ID id
	 *	@return Carra
	 */
	public static P_Carra get (Properties ctx, int P_Carra_ID, String trxName)
	{
		Integer key = new Integer (P_Carra_ID);
		P_Carra Carra = (P_Carra)s_cache.get(key);
		if (Carra != null)
			return Carra;
		Carra = new P_Carra (ctx, P_Carra_ID, trxName);
		s_cache.put (key, Carra);
		return Carra;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Carra>	s_cache = new CCache<Integer,P_Carra>("P_Carra", 20);

	/**	Logger							*/
	private static CLogger		log = CLogger.getCLogger (P_Carra.class);

	String trxName;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Carra_ID id
	 */
	public P_Carra (Properties ctx, int P_Carra_ID, String trxName)
	{
		super (ctx, P_Carra_ID, trxName);
		if (P_Carra_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
		
		this.trxName = trxName;
		
	}	//	P_Carra

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Carra (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Carra (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Carra_ID"), trxName);
	}	//	P_Carra


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Carra[ID=")
			.append(this.getP_Carra_ID())
//			.append(",Value=").append(getValue())
//			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	/*
	TYPE DE DÉCLARATION
	Les types possibles de déclaration sont 1, 2 ou 3. Vous trouverez ci-dessous la description de
	chacun des types. Si aucune des situations énumérées ci-dessous ne s’applique, vous devez laisser
	le champ en blanc.
	Vous devez produire une section financière distincte par type de déclaration. Par exemple, pour un
	même employé qui aurait le type « 2 » et le type « 3 » pour la période couverte par le calendrier de
	paie, vous devez produire deux sections financières dont une avec le type « 2 » et l’autre avec le
	type « 3 », avec les données financières se rattachant à chacun des types.
	 */
	public void setDAType()
	{
		this.setDAType( " " );

		/*
		EMPLOYÉ NON VISÉ (TYPE 1)
		Utilisez le type « 1 » dans le cas d’un employé pour lequel vous avez prélevé des cotisations par
		erreur alors qu’il n’était pas visé par un régime de retraite pour la période complète du calendrier de
		paie en application. Il peut s’agir, entre autres, d’un employé à qui vous avez prélevé des cotisations
		alors qu’il avait moins de 18 ans ou d’un employé de plus de 69 ans.
		Dans ces cas, la cotisation prélevée par erreur doit être remboursée à l’employé par l’employeur.
		 */
		if ( 1==0 )
			this.setDAType( P_Carra.DATYPE_1_EmployéNonVisé );
		
		/*
		EMPLOYÉ LIBÉRÉ POUR ACTIVITÉS SYNDICALES (TYPE 2)
		Le type « 2 » est utilisé uniquement par les organismes syndicaux prévus à l’Annexe II.I du
		RREGOP et les associations représentant le personnel d’encadrement prévues à l’Annexe III du
		RRPE. Vous trouverez ces annexes dans le Guide d’administration.
		Lorsqu’un employé est libéré, avec ou sans salaire, pour travailler dans un syndicat ou une
		association, il arrive que le syndicat ou l’association verse à l’employé un salaire plus élevé que
		celui que lui versait son employeur d’origine. Le type « 2 » sert à identifier le salaire supplémentaire
		versé à l’employé par le syndicat ou l’association.
		*/
		if ( 1==0 )
			this.setDAType( P_Carra.DATYPE_2_EmployéLibéréPourActivitésSyndicales );

		/*
		EMPLOYÉ LIBÉRÉ SANS SALAIRE POUR ACTIVITÉ SYNDICALE (TYPE 3)
		Le type « 3 » est utilisé uniquement par les organismes syndicaux prévus à l’Annexe II.I du
		RREGOP et les associations représentant le personnel d’encadrement prévues à l’Annexe III du
		RRPE. Vous trouverez ces annexes dans le Guide d’administration.
		Ce type doit être inscrit dans la déclaration annuelle afin de déclarer le salaire payé par le syndicat
		ou l’association correspondant au salaire que l’employeur d’origine aurait versé si l’employé n’avait
		pas été libéré sans salaire pour activité syndicale.
		*/
		if ( 1==0 )
			this.setDAType( P_Carra.DATYPE_3_EmployéLibéréSansSalaire );
		
	}
	
	
	/*
	 * FACTEUR QUOTIDIEN
		Dans les conditions de travail des participants, nous retrouvons le salaire à l’échelle sur une base
		annuelle de même que le nombre d’heures, de jours ou de semaines associés à ce salaire.
		L’information à inscrire est le nombre de jours ou l’équivalent en nombre de jours.
		Ce champ doit être rempli pour chaque déclaration produite. Le facteur quotidien applicable pour la
		fonction publique est 260,9.
		Si vous croyez avoir un facteur quotidien différent de 260,9, veuillez communiquer avec la
		Commission administrative des régimes de retraite et d’assurances.
	 */
	public void setDayFactor ()
	{
		this.setDayFactorVal( P_Carra.DAYFACTORVAL_FacteurQuotidienSurBaseDe2609Jours);
	}

	/*
	BASE DE RÉMUNÉRATION
	Vous devez indiquer dans ce champ la base de rémunération utilisée. Cette dernière représente le
	nombre de jours cotisables dans une année de service (du 1er janvier au 31 décembre). Pour tous
	Guide des informations et spécifications Fonction publique
	21
	les employés du réseau de la fonction publique qui participent à un régime de retraite administré par
	la CARRA, inscrivez 260.
	*/
	public void setRemunerationBase()
	{
		this.setRemunerationBase( P_Carra.REMUNERATIONBASE_RémunérationSur260Jours);
	}

	/*
	DATE DE DÉBUT
	La « Date de début » est en relation avec le régime de retraite, le groupe, le facteur quotidien, le
	type de déclaration et la période couverte par le calendrier de paie. Pour l’année de la déclaration
	annuelle, une date de début doit être inscrite chaque fois qu’un employé âgé entre 18 et 69 ans :
	• est embauché pour un emploi visé dans la période couverte par le calendrier de paie;
	Inscrivez la date du premier jour où cet employé a accompli du service s’il n’avait pas de lien
	d’emploi au début du calendrier de paie. Pour un participant qui atteint 18 ans en cours d’année,
	vous devez inscrire comme date de début la date de son anniversaire (date à laquelle il doit
	cotiser au régime de retraite).
	• sans droit de rappel dans la fonction publique est réembauché pour un emploi visé;
	Inscrivez la date de début du contrat ou de la période de référence fixe.
	• participe à un second régime de retraite;
	Inscrivez la date du premier jour où il commence à être visé par ce nouveau régime.
	• participe à un second groupe ou régime;
	Inscrivez la date du jour où il commence à être visé par ce nouveau groupe ou régime.
	• doit être déclaré selon plus d’un type de déclaration;
	Inscrivez la date du premier jour où il commence à être visé par ce second type de déclaration.
	• doit être rémunéré en fonction de plus d’un calendrier de paie;
	Inscrivez la date du jour où il commence à être rémunéré en fonction du second calendrier de
	paie.
	Laissez cette case en blanc pour l’année de la déclaration annuelle si l’employé était en fonction ou
	avait un lien d’emploi (employé inscrit sur une liste de rappel ou saisonnier) le dernier jour couvert
	par la dernière période de paie de l’année précédant celle de la déclaration annuelle.
	*/
	public void setStartEmploymentDate ()
	{
	
		setStartEmploymentDate( null );
		
		// Employé a 18 ans dans l'année - inscrire comme date de début la date de son anniversaire
		if ( Employee.getAge(PeriodStart.getStartDate()) < 18 && Employee.getAge(PeriodEnd.getEndDate()) >= 18 )
		{
			setStartEmploymentDate ( TimeUtil.getDay(Year.getYear(), TimeUtil.getMonth(Employee.getBirthDate()), TimeUtil.getDay_Of_Month(Employee.getBirthDate() )) );
		}
			
		// est embauché pour un emploi visé dans la période couverte par le calendrier de paie
		if ( Employee.getDateHired() != null && Employee.getDateHired().compareTo( PeriodStart.getStartDate()) > 0 && TimeUtil.getYear(EmployeeDeduction.getEffectIn()) == Year.getYear())
		{
			setStartEmploymentDate ( Employee.getDateHired() );
		}
		
		
		//Inscrivez la date du jour où il commence à être visé par ce nouveau groupe ou régime
		if ( EmployeeDeduction.getEffectIn().compareTo( PeriodStart.getStartDate() ) >= 0 && TimeUtil.getYear(EmployeeDeduction.getEffectIn()) == Year.getYear() )
		{
			setStartEmploymentDate ( EmployeeDeduction.getEffectIn());
		}
		
	}

	/*
	 DATE DE FIN
	La « Date de fin » est en relation avec le régime de retraite, le groupe, le facteur quotidien, le type
	de déclaration et la période couverte par le calendrier de paie. Pour l’année de la déclaration
	annuelle, une date de fin doit être inscrite chaque fois qu’un employé visé âgé entre 18 et 69 ans :
	Guide des informations et spécifications Fonction publique
	22
	• n’a plus de lien d’emploi avec l’employeur (démission, congédiement, décès, échéance de la
	liste de rappel, échéance du contrat de travail). Cette date de fin d’emploi servira à établir le droit
	à une rente de retraite ou à un remboursement des cotisations;
	Inscrivez la date du jour où cet employé cesse d’être visé puisqu’il n’a plus de lien d’emploi avec
	son employeur.
	• cesse de participer à un régime de retraite;
	Inscrivez la date du jour où il cesse d’être visé par ce régime.
	• cesse de participer à un groupe ou régime;
	Inscrivez la date du jour où il cesse d’être visé par ce groupe ou régime.
	• ne doit plus être identifié par le type de déclaration;
	Inscrivez la date du jour où il n’est plus visé par ce type de déclaration.
	• cesse d’être rémunéré en fonction d’un calendrier de paie;
	Inscrivez la date où cet employé a cessé d’être rémunéré ou d’avoir un lien d’emploi en fonction
	de ce calendrier de paie.
	• atteint 69 ans en cours d’année;
	L’employé qui atteint 69 ans pendant l’année de la déclaration annuelle doit participer au régime
	de retraite jusqu’au 30 décembre. La date à inscrire comme date de fin sera donc le
	30 décembre de l’année de son 69e anniversaire si l’employé a occupé un emploi visé toute
	l’année « Année-12-30 ».
	• reçoit durant la période couverte par le calendrier de paie une prestation de maladie en phase
	terminale du RREGOP, du RRAS ou du RRPE;
	Inscrivez la date de fin d’emploi. Si l’employé n’a pas démissionné, inscrivez la date à laquelle il
	a cessé de participer au RREGOP, au RRAS ou au RRPE, c’est-à-dire celle qui a été indiquée
	quand la CARRA a accepté la demande de prestation de maladie en phase terminale. Ce
	participant ne sera plus déclaré pour les années suivantes, car il n’est plus considéré comme un
	employé au sens du RREGOP, du RRAS ou du RRPE.
	• reçoit durant la période couverte par le calendrier de paie une rente d’invalidité du RRE du RRF;
	Inscrivez la date de fin d’emploi. Si l’employé n’a pas démissionné, inscrivez la date à laquelle il
	a cessé de participer au RRE ou au RRF, c’est-à-dire celle qui a été indiquée par la CARRA.
	Laissez cette case en blanc si l’employé continue d’être en fonction ou conserve un lien d’emploi
	jusqu’au dernier jour de la période couverte par la dernière période de paie du calendrier de paie en
	application pour l’année en cause.
	Guide des informations et spécifications Fonction publique
	23
	Aucune date de fin ne doit être inscrite pour l’employé qui atteint 35 années de service en cours
	d’année et qui continue de travailler après le 31 décembre. Pour plus d’information voir le champ
	« Salaire après 35 années de service (non cotisable) » décrit plus loin.

	 */
	public void setEndEmploymentDate ()
	{
		setEndEmploymentDate( null);
		/*
		L’employé qui atteint 69 ans pendant l’année de la déclaration annuelle doit participer au régime
		de retraite jusqu’au 30 décembre. La date à inscrire comme date de fin sera donc le
		30 décembre de l’année de son 69e anniversaire si l’employé a occupé un emploi visé toute
		l’année « Année-12-30 ».
		*/
		if ( Employee.getAge(PeriodStart.getStartDate()) < 69 && Employee.getAge(PeriodEnd.getEndDate()) >= 69 )
		{
			setEndEmploymentDate ( TimeUtil.getDay(Year.getYear(), 12 , 30 ));
		}

		// Inscrivez la date de fin d’emploi
		if ( Employee.getDateLayoff() != null && Employee.getDateLayoff().compareTo( PeriodEnd.getEndDate()) <= 0 && Employee.getDateLayoff().compareTo( Employee.getDateHired() ) > 0)
		{
			setEndEmploymentDate ( Employee.getDateLayoff() );
		}
		
		
		// Inscrivez la date du jour où il cesse d’être visé par ce groupe ou régime
		if ( EmployeeDeduction.getEffectTo() != null && EmployeeDeduction.getEffectTo().compareTo( PeriodEnd.getEndDate() ) <= 0  )
		{
			setEndEmploymentDate ( EmployeeDeduction.getEffectTo());
		}

	}

	/*
	5.15 SALAIRE ANNUEL DE BASE (SAB)
	Pour chaque déclaration produite, sauf pour le RREFQ et le RRMAN, un salaire annuel de base doit
	être inscrit dans ce champ. Inscrivez le salaire annuel de base que l’employé a eu ou aurait eu (les
	congés de type A à E de la section financière variable doivent être considérés) à la dernière période
	de paie du calendrier de paie en application dans l’année de la déclaration, ou s’il a quitté son
	emploi, à la date de sa démission ou à la date où il cesse de participer.
	
	5.15.5 EMPLOYÉ PARTICIPANT AU RREGOP OU AU RRPE QUI OCCUPE DES EMPLOIS MULTIPLES CHEZ UN MÊME EMPLOYEUR
	Pour cet employé, vous devez faire une seule déclaration annuelle regroupant plusieurs emplois
	occupés simultanément ou consécutivement dans la même année. Le salaire annuel de base à
	inscrire doit être régularisé en tenant compte du service acquis (incluant le service rachetable des
	absences sans salaire) dans chacun de ces emplois.
	*/
	public void setSalaryAnnualBase ( int nbrAssignment)
	{
        // S'il y a une seule affectation alors on prend denier salaire que l'employé a recu
        // sinon on prend le salaire moyen 

        if( nbrAssignment == 1)
        {
        	BigDecimal AnnualSalary = Employee.getAnnualSalary( PeriodEnd.getEndDate());

        	//this.baseSalary = temporarySalary.divide( Annual_Increase,8,BigDecimal.ROUND_HALF_UP ).multiply( new BigDecimal(52) );

    		this.setSalaryAnnualBase( AnnualSalary );
        }
        else
        {
	  /*
	  Exemple :
			Emploi 1 : SAB pour 260,9 jours = • 52 180 $
			Salaire cotisable (100 jours) = • 20 000 $
			Calcul du % = 126 jours ÷ 260 jours = • 48,4615 %
	
			Emploi 2 : SAB pour 260,9 jours = • 26 090 $
			Salaire cotisable (50 jours) = • 5 000 $
			Calcul du % = 50 jours ÷ 260 jours = • 19,2308 %
			Total : Salaire cotisable (150 jours) = • 25 000 $
	
			SAB pondéré = ??
				(52180 x (126/260) + (26090 x (50/260) )
	   */      	

        	BigDecimal SalaryWeighted = CalculWeighted();
        	this.setSalaryAnnualBase( SalaryWeighted );
        }
	}


  /*
	5.15.5 EMPLOYÉ PARTICIPANT AU RREGOP OU AU RRPE QUI OCCUPE DES EMPLOIS MULTIPLES CHEZ UN MÊME EMPLOYEUR

	  Exemple :
			Emploi 1 : SAB pour 260,9 jours = • 52 180 $
			Salaire cotisable (100 jours) = • 20 000 $
			Calcul du % = 126 jours ÷ 260 jours = • 48,4615 %
	
			Emploi 2 : SAB pour 260,9 jours = • 26 090 $
			Salaire cotisable (50 jours) = • 5 000 $
			Calcul du % = 50 jours ÷ 260 jours = • 19,2308 %
			Total : Salaire cotisable (150 jours) = • 25 000 $
	
			SAB pondéré = ??
				(52180 x (126/260) + (26090 x (50/260) )
   */      	
	private BigDecimal 	CalculWeighted()
	{
    	BigDecimal SalaryWeighted = Env.ZERO;

    	
		String sql = "select P_Payment_Gain.P_Assignment_ID, Round( sum( dbo.divide( P_Payment_Gain.QuantityCalc , dbo.divide( P_Assignment_Param.Weekly_Hours, 5 ) )),4) NbrDay , max( AnnualSalary) AnnualSalary " 
            + " from P_Payment_Gain  "
            + "  Inner Join P_Payment On P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID "
            + "  Inner Join P_Assignment_Param on P_Assignment_Param.P_Assignment_Param_ID = P_Payment_Gain.P_Assignment_Param_ID "
            + "  Inner Join P_Gain_Column on P_Gain_Column.P_Gain_Parameter_ID = P_Payment_Gain.P_Gain_Parameter_ID "
            + "               And P_Gain_Column.P_Column_Adm_ID = " + Deduction_Param.getP_Column_Adm_ID()
            + "               And IsAdmissible = 'Y' "
            + " where P_Payment.P_Employee_ID = " + Employee.getP_Employee_ID() 
            + "	 and P_Payment.P_Year_ID = " + Year.getP_Year_ID()

            + " and not exists ( select 1 "
                + " from p_gain tag, p_gain_gaininfo ggi, p_gaininfo info "
                + " where info.p_gaininfo_id = ggi.p_gaininfo_id "
                + " and tag.p_gain_id = ggi.p_gain_id "
                + " and tag.p_gain_id =  P_Payment_Gain.P_Gain_ID "
                + " and info.value = 'REDUC' "
                + " and ggi.To_Consider = 'Y' " 
                + " ) "
			+ " Group By P_Payment.P_Employee_ID, P_Payment_Gain.P_Assignment_ID	"
            ;

/*    	
		String sql = "select P_Assignment_ID, count( Day ) NbrDay, max( AnnualSalary) AnnualSalary" 
            + " from P_Payment_Gain  "
            + "  Inner Join P_Payment On P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID "
            + " where P_Payment.P_Employee_ID = " + Employee.getP_Employee_ID() 
            + "	 and P_Payment.P_Year_ID = " + Year.getP_Year_ID() 
            + " Group By P_Payment.P_Employee_ID, P_Payment_Gain.P_Assignment_ID	"
            ;
*/
	     PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	     try
	     {
		     ResultSet rs = stmt.executeQuery();
		     if (rs.next())
		     {
		    	 SalaryWeighted = SalaryWeighted.add( rs.getBigDecimal("AnnualSalary").multiply( ( rs.getBigDecimal("NbrDay").divide( new BigDecimal( ContributoryDays ), 2, BigDecimal.ROUND_HALF_UP ) ) ));
		     }
		     rs.close();
		     stmt.close();
	     }
	     catch(Exception e)
	     {
			log.log (Level.SEVERE, "P_Carra", e);
	     }
			
	     return SalaryWeighted;
	}

	
	/*
	5.14 SALAIRE APRÈS 35 ANNÉES DE SERVICE (NON COTISABLE)
	Dès qu’un participant du RREGOP, du RRPE, du RRCE, du RRAS, du RRE ou du RRF atteint
	35 années de service pour le calcul de la rente de retraite, le salaire versé devient du salaire après
	35 années de service. Aucune cotisation ne doit être prélevée sur le salaire de ce participant pour la
	partie de l’année suivant l’atteinte de 35 années de service. Il cesse alors de cotiser à son régime de
	retraite. Le salaire après 35 années de service servira à établir le salaire admissible moyen lors du
	calcul de la rente de retraite.
	 */
	
	private void setSalary35Year()
	{
		/*
		Inscrivez pour le participant du RREGOP, du RRPE, du RRAS, du RRCE, du RRE ou du RRF le
		salaire annuel de base de la dernière journée pour laquelle il a été rémunéré, sans vous soucier qu’il
		a atteint ou dépassé 35 années de service.
		*/
		if ( EmployeeDeduction.getDate35() != null && EmployeeDeduction.getDate35().compareTo(PeriodEnd.getEndDate()) <= 0 )
		{
	    	BigDecimal AnnualSalary = Employee.getAnnualSalary( EmployeeDeduction.getDate35() );
			setSalary35Year( AnnualSalary );
		}
	}


/*
	if(this.date35 != null && this.date35.compareTo(rs.getTimestamp("Day")) <= 0)
    {
        // Calcul du salaire non cotisable
        this.nonContributorySalary = this.nonContributorySalary.add( AmountCalc );
    }

*/	
	/*
	5.12 COTISATION SALARIALE
	Indiquez le montant des cotisations salariales versées à la CARRA au cours de l’année en excluant
	toute récupération de cotisations pour les années antérieures et toute somme versée pour un
	rachat. Si vous faites plus d’une section financière, vous devez répartir le montant des cotisations
	sur chacune des déclarations.

	Vous devez inclure dans ce champ le montant des cotisations salariales payées par l’employeur
	pour l’employé, ce qui peut survenir dans le cadre de certaines mesures comme l’aménagement ou
	la réduction du temps de travail (ARTT), par exemple.
	*/
	private void setContributoryEarnings() 
	{
		
		// On adition la part employee et employer pour la gestion des employés en ARTT ou l'employer compense la différence
		String sql = "select sum( Employee_Part + Employer_Part ) Contributory, sum( Salary_Eligible ) ContributoryEarnings " 
            + " from P_Payment_Deduction  "
            + "  Inner Join P_Payment On P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID "
            + " where P_Payment.P_Employee_ID = " + Employee.getP_Employee_ID() 
            + "	 and P_Payment.P_Year_ID = " + Year.getP_Year_ID()
            + "  and P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID()
            ;

	     PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	     try
	     {
		     ResultSet rs = stmt.executeQuery();
		     if (rs.next())
		     {
		    	 setContributoryEarnings( rs.getBigDecimal( "ContributoryEarnings" ));
		    	 setContribution( rs.getBigDecimal( "Contributory" ).divide(new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP));
		     }
		     rs.close();
		     stmt.close();
	     }
	     catch(Exception e)
	     {
			log.log (Level.SEVERE, "P_Carra", e);
	     }
		
	}
	
	
	private int calculNbrWorkDay()
	{
		 Timestamp StartDate = PeriodStart.getStartDate();
		 Timestamp EndDate = PeriodEnd.getEndDate();
		 
    	 if ( this.getStartEmploymentDate() != null  )
    	 {
    		 StartDate = this.getStartEmploymentDate();
    	 }
		 
		 if ( this.getEndEmploymentDate() != null )
		 {
			 EndDate = this.getEndEmploymentDate();
		 }

		 int nbrCalDay = TimeUtil.getDaysBetween( StartDate, EndDate);
		 
		 int nbrDay = 0;
		 
		 for ( int i=0; i<= nbrCalDay; i++)
		 {
			 if ( TimeUtil.getDay_Of_Week( TimeUtil.addDays(StartDate, i)) != Calendar.SUNDAY &&
				  TimeUtil.getDay_Of_Week( TimeUtil.addDays(StartDate, i)) != Calendar.SATURDAY
			    )
				 nbrDay++;
		 }
		 return nbrDay;
	}
	
	/*
	5.13 PARTIEL - % DU TEMPS
	Ce champ numérique à six positions dont quatre après la virgule sert à identifier les employés à
	temps partiel, c’est-à-dire, les employés qui occupent un poste selon un horaire régulier mais
	incomplet. L’employé à contrat, l’employé occasionnel et l’employé saisonnier font partie de cette
	catégorie s’ils ne travaillent pas toute la période couverte par le calendrier de paie de l’année à
	déclarer.
	*/
	private void setPartTime( )
	{
		String sql = "select Round( sum( dbo.divide( P_Payment_Gain.QuantityCalc , dbo.divide( P_Assignment_Param.Weekly_Hours, 5 ) )),4) " 
            + " from P_Payment_Gain  "
            + "  Inner Join P_Payment On P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID "
            + "  Inner Join P_Assignment_Param on P_Assignment_Param.P_Assignment_Param_ID = P_Payment_Gain.P_Assignment_Param_ID "
            + "  Inner Join P_Gain_Column on P_Gain_Column.P_Gain_Parameter_ID = P_Payment_Gain.P_Gain_Parameter_ID "
            + "               And P_Gain_Column.P_Column_Adm_ID = " + Deduction_Param.getP_Column_Adm_ID()
            + "               And IsAdmissible = 'Y' "
            + " where P_Payment.P_Employee_ID = " + Employee.getP_Employee_ID() 
            + "	 and P_Payment.P_Year_ID = " + Year.getP_Year_ID()

            + " and not exists ( select 1 "
                + " from p_gain tag, p_gain_gaininfo ggi, p_gaininfo info "
                + " where info.p_gaininfo_id = ggi.p_gaininfo_id "
                + " and tag.p_gain_id = ggi.p_gain_id "
                + " and tag.p_gain_id =  P_Payment_Gain.P_Gain_ID "
                + " and info.value = 'REDUC' "
                + " and ggi.To_Consider = 'Y' " 
                + " ) ";
		
		if ( this.getStartEmploymentDate() != null || this.getEndEmploymentDate() != null )
		{
			if  ( this.getStartEmploymentDate() != null)
				sql += " and Day >= " + DB.TO_DATE( this.getStartEmploymentDate() )  ;

			if  ( this.getEndEmploymentDate() != null)
				sql += " and Day <= " + DB.TO_DATE( this.getEndEmploymentDate() )  ;
		}

		 int nbrCalDay = calculNbrWorkDay();
    	 BigDecimal nbrWorkDay = Env.ZERO;

	     PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	     try
	     {
	    	 
		     ResultSet rs = stmt.executeQuery();
		     if (rs.next())
		     {
		    	 nbrWorkDay = rs.getBigDecimal( 1 );
		     }
		     rs.close();
		     stmt.close();
	     }
	     catch(Exception e)
	     {
			log.log (Level.SEVERE, "P_Carra", e);
	     }
	     
	     if ( nbrWorkDay  == null)
	    	 nbrWorkDay = Env.ZERO;
	     
     	 setContributoryDays( nbrWorkDay );

	     //+ jours absence.
	     sql = " Select sum( nbrDays) From P_Carra_Absence Where P_Carra_ID = " + this.getP_Carra_ID();
	     stmt = DB.prepareStatement(sql, trxName);
	     try
	     {
	    	 
		     ResultSet rs = stmt.executeQuery();
		     if (rs.next())
		     {
		    	 if ( rs.getBigDecimal( 1 ) != null)
		    		 nbrWorkDay = nbrWorkDay.add( rs.getBigDecimal( 1 ));
		     }	
		     rs.close();
		     stmt.close();
	     }
	     catch(Exception e)
	     {
			log.log (Level.SEVERE, "P_Carra", e);
	     }
	      
		
	     BigDecimal partTime = Env.ZERO;
	     if ( nbrWorkDay != null && nbrCalDay != 0 )
	    	 partTime = nbrWorkDay.divide( new BigDecimal( nbrCalDay ) ,4,BigDecimal.ROUND_HALF_UP)  ; 
//	    if (  partTime.compareTo( new BigDecimal( 100) ) != 0 )
	     
	     partTime = partTime.multiply(new BigDecimal(100));
	     
	     if ( partTime.compareTo( new BigDecimal(100) ) > 0 )
	    	 partTime = new BigDecimal(100);
	     
	     setPartTime( partTime );
	}


	
	/*
	6.1 ABSENCES
	Les différentes absences qui doivent être considérées dans cette sous-section sont réparties selon
	trois catégories : « Absence dont le salaire est non cotisable et dont le service est crédité »,
	« Absence dont le salaire est cotisable et dont le service est crédité», et « Absence dont le salaire
	est non cotisable et dont le service est non crédité ».
	*/
	private void Insert_Absence()
	{
		String delsql = " Delete From P_Carra_Absence Where P_Carra_ID = " + this.getP_Carra_ID();
		DB.executeUpdate(delsql, trxName);

		//
		// le montant calcul est recalculé pour ramender les gains assurance salaires ex : 80% du salaire ) à 100%
		// Normalment le calcul est Taux * facteur * quantity. pour avoir 100% on n'utilise pas le facteur.
		//
		String sql = "Select AbsenceCode, sum( P_Payment_Gain.quantitycalc) quantitycalc , sum ( P_Payment_Gain.Hourly_Rate * P_Payment_Gain.quantitycalc ) AbsenceSalary, sum( P_Payment_Gain.Amountcalc) Amountcalc "
			       + " from P_Payment_Gain " 
			       + " inner join P_Payment on P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID "
			       + " inner join P_Gain on P_Gain.P_Gain_ID = P_Payment_Gain.P_Gain_ID "
			       + " 	WHERE AbsenceCode is not null "
			       + "  AND P_Payment.P_Year_ID = " + this.getP_Year_ID()
			       + "  AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID()
			       ;

		if ( this.getStartEmploymentDate() != null || this.getEndEmploymentDate() != null )
		{
			if  ( this.getStartEmploymentDate() != null)
				sql += " and Day >= " + DB.TO_DATE( this.getStartEmploymentDate() )  ;

			if  ( this.getEndEmploymentDate() != null)
				sql += " and Day <= " + DB.TO_DATE( this.getEndEmploymentDate() )  ;
		}

		sql += " 	GROUP BY AbsenceCode "	       ;

		PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	
		try
		{
	 
		    ResultSet rs = stmt.executeQuery();
		    P_Carra_Absence Absence;
		    P_Assignment_Param Assignment_Param = P_Assignment_Param.get(Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName);
		    BigDecimal nbrDays = Env.ZERO;
		    while(rs.next())
		    {
		    	Absence = new P_Carra_Absence( Env.getCtx(), -1, trxName);
		    	Absence.setP_Carra_ID(this.getP_Carra_ID());
		    	Absence.setAbsenceCode(rs.getString("AbsenceCode"));
		    	Absence.setAbsenceSalary(rs.getBigDecimal("AbsenceSalary" ));

		    	BigDecimal DayHours = Assignment_Param.getWeekly_Hours().divide(new BigDecimal(5), 2, BigDecimal.ROUND_HALF_UP);

		    	if ( rs.getBigDecimal("quantitycalc").compareTo(Env.ZERO) != 0)
		    		nbrDays = rs.getBigDecimal("quantitycalc").divide( DayHours ,4,BigDecimal.ROUND_HALF_UP );
		    	else
		    	{
		    		nbrDays = rs.getBigDecimal("amountCalc").divide( Assignment_Param.getHourly_Rate(), 2,BigDecimal.ROUND_HALF_UP ).divide( DayHours ,4,BigDecimal.ROUND_HALF_UP );
		    		nbrDays = nbrDays.abs();
			    	Absence.setAbsenceSalary(rs.getBigDecimal("amountCalc").abs());

		    	}
		    	
		    	Absence.setNbrDays(nbrDays);
		    	Absence.save();

		    	Absence.setAbsenceRetroAmt( getRetroInfoAbsence( Absence ) );
		    	Absence.save();
		    	
		    }
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			log.log (Level.SEVERE, "P_Carra", e);
		}
		
	}

	private void Insert_Sabbatical()
	{

		String delsql = " Delete From P_Carra_Sabbatical Where P_Carra_ID = " + this.getP_Carra_ID();
		DB.executeUpdate(delsql, trxName);

		String sql = "Select StartDate, EndDate, P_Gain.AbsenceCode " 
				   + " From P_Standard_Time " 
			       + " inner join P_Gain on P_Gain.P_Gain_ID = P_Standard_Time.P_Gain_ID "
				   + " Where P_SelfFinancedLeave_ID is not null "
				   + "   AND P_Employee_ID = " + this.getP_Employee_ID()
				   + "   AND ( EndDate >= " + DB.TO_DATE( PeriodStart.getStartDate() ) + " OR EndDate IS NULL )" 
				   + "   AND ( StartDate <= " + DB.TO_DATE( PeriodEnd.getEndDate() ) + " )" 
				   ;

		if ( this.getStartEmploymentDate() != null || this.getEndEmploymentDate() != null )
		{
			if  ( this.getStartEmploymentDate() != null)
				sql += "   AND ( EndDate >= " + DB.TO_DATE( this.getStartEmploymentDate() ) + " OR EndDate IS NULL )"; 

			if  ( this.getEndEmploymentDate() != null)
				sql += "   AND ( StartDate <= " + DB.TO_DATE( this.getEndEmploymentDate() ) + " )"; 

		}
		
//			|---------------------|  Interval				   
//             |---------|  
//     |---------|
//					|----------------|
// |-----|		                    |----|
		 
/*		String sql = "Select SalaryPercentage, AnnualSalary "
			       + " from P_Payment "
			       + " 	WHERE IsSelfFinancedleave = 'Y'"
			       + "  AND P_Payment.P_Year_ID = " + this.getP_Year_ID()
			       + " 	GROUP BY AbsenceCode "
			       ;
*/
		PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	
		try
		{
	 
		    ResultSet rs = stmt.executeQuery();
		    P_Carra_Sabbatical Sabbatical;
		    while(rs.next())
		    {
		    	Sabbatical = new P_Carra_Sabbatical( Env.getCtx(), -1, trxName);
		    	Sabbatical.setP_Carra_ID(this.getP_Carra_ID());
		    	Sabbatical.setAbsenceCode( rs.getString("AbsenceCode") );
		    	
		    	Sabbatical.setAnnualSalary( Employee.getAnnualSalary( rs.getTimestamp("StartDate" )) );
		    	Sabbatical.setStartDateSabbatical( rs.getTimestamp("StartDate" ) );
		    	if ( Sabbatical.getAbsenceCode() != null )
		    		Sabbatical.save();
		     		
		    }
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			log.log (Level.SEVERE, "P_Carra", e);
		}
		
	}

	/*
	 Salaire Versé 
	*/
	private void setSalaryPay()
	{
		BigDecimal SalaryPay = Env.ZERO;
		this.setSalaryPay(SalaryPay.setScale(2, BigDecimal.ROUND_HALF_UP));
	}

	/*
	6.2 RÉTROACTIVITÉ POUR DES ANNÉES ANTÉRIEURES
	Cette sous-section doit toujours être remplie lorsqu’il y a un montant de rétroactivité pour une ou
	plusieurs années antérieures à déclarer durant une année. Pour se qualifier comme montant de
	rétroactivité au sens de la déclaration annuelle à la CARRA, le montant doit avoir été payé à titre
	d’augmentation ou de rajustement du salaire admissible d’une période antérieure à l’année civile
	pendant laquelle ce montant est versé.
	*/
	private void Insert_Retro()
	{

		String delsql = " Delete From P_Carra_Retro Where P_Carra_ID = " + this.getP_Carra_ID();
		DB.executeUpdate(delsql, trxName);

		String sql = " Select P_Augmentation_Result_Ts.P_Year_ID, sum(P_Time_Sheet_Detail.DayQty ) as RetroAmt "
		           + " From P_Augmentation_Result_Ts " 
		           + " Inner Join P_Time_Sheet_Detail ON P_Augmentation_Result_Ts.P_Time_Sheet_Detail_ID = P_Time_Sheet_Detail.P_Time_Sheet_Detail_ID "
		           + " Inner Join P_Time_Sheet ON P_Time_Sheet.P_Time_Sheet_ID = P_Time_Sheet_Detail.P_Time_Sheet_ID "
		           // Uniquement les gains rétro carra.
		           + " Inner Join P_Gain ON P_Gain.P_Gain_ID = P_Time_Sheet_Detail.P_Gain_ID and flag_Carra = 'RSAL' "
		           + " Where P_Time_Sheet.P_Employee_ID = " + this.getP_Employee_ID()
		           + "   AND P_Time_Sheet.P_Year_ID = " + this.getP_Year_ID()
		           // Rétroactivité pour des années antérieures
		           + "   AND P_Augmentation_Result_Ts.P_Year_ID <> " + this.getP_Year_ID()
		           	;
	
		if ( this.getStartEmploymentDate() != null || this.getEndEmploymentDate() != null )
		{
			if  ( this.getStartEmploymentDate() != null)
				sql += " and P_Time_Sheet_Detail.Day >= " + DB.TO_DATE( this.getStartEmploymentDate() )  ;

			if  ( this.getEndEmploymentDate() != null)
				sql += " and P_Time_Sheet_Detail.Day <= " + DB.TO_DATE( this.getEndEmploymentDate() )  ;
		}
		           
		sql += " Group By P_Augmentation_Result_Ts.P_Year_ID " ;
				   
		PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	
		try
		{
	 
		    ResultSet rs = stmt.executeQuery();
		    P_Carra_Retro Retro;
		    while(rs.next())
		    {
		    	Retro = new P_Carra_Retro( Env.getCtx(), -1, trxName);
		    	Retro.setP_Carra_ID(this.getP_Carra_ID());
		    	if ( rs.getInt("P_Year_ID") == 0)
		    		Retro.setP_Year_ID( this.getP_Year_ID() );
		    	else
		    		Retro.setP_Year_ID( rs.getInt("P_Year_ID") );
		    	Retro.setRetroAmt(rs.getBigDecimal("RetroAmt"));
		    	Retro.save();
		     		
		    }
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			log.log (Level.SEVERE, "P_Carra", e);
		}
		
	}

	public static int getPeriod( String flg, P_Year Year, String trxName )
	{
		String sql = null;
		int id = 0;
		
		if ( flg.equals("Start"))
			sql = " Select min( P_Period_ID ) from P_Period where P_Year_ID = " + Year.getP_Year_ID();
		else
			sql = " Select max( P_Period_ID ) from P_Period where P_Year_ID = " + Year.getP_Year_ID();
			   
		PreparedStatement stmt = DB.prepareStatement(sql, trxName);

		try
		{

		    ResultSet rs = stmt.executeQuery();
		    if(rs.next())
		    {
		    	id = rs.getInt(1);
		    }
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			log.log (Level.SEVERE, "P_Carra", e);
		}
			
		return id;
	}

	
	private P_Deduction_Param getDeductionInfo() 
	{
		P_Deduction_Param Deduction_Param = null;
		
        // On va chercher la olonne d'admissibilité associée à cette déduction à
        // l'aide des dates de début et de fin de la période
        String sql = "Select P_Deduction_Param_ID "
            + " from P_Deduction_Param "
            + " where P_Deduction_ID = " + this.getP_Deduction_ID()
            + " and EffectIn = ("
            + "   select max(A.EffectIn)"
            + "   from P_Deduction_Param A"
            + "   where A.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID"
            + "   and A.EffectIn <= " + DB.TO_DATE(PeriodEnd.getEndDate()) 
            + " )";

		PreparedStatement stmt = DB.prepareStatement(sql, trxName);

		try
		{
	
		    ResultSet rs = stmt.executeQuery();
		    if(rs.next())
		    {
		    	Deduction_Param = P_Deduction_Param.get( Env.getCtx(), rs.getInt("P_Deduction_Param_ID") , trxName);
		    }
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			log.log (Level.SEVERE, "P_Carra", e);
		}

        return Deduction_Param;
	}
	

	private BigDecimal getRetroInfoAbsence( P_Carra_Absence Absence )
	{
		BigDecimal AmtRetro = Env.ZERO;
		
		String sql = " Select sum( dayQty ) AmtRetro"
	           + " From P_Augmentation_Result_Ts " 
	           + " Inner Join P_Time_Sheet_Detail ON P_Augmentation_Result_Ts.P_Time_Sheet_Detail_ID = P_Time_Sheet_Detail.P_Time_Sheet_Detail_ID "
	           + " Inner Join P_Time_Sheet ON P_Time_Sheet.P_Time_Sheet_ID = P_Time_Sheet_Detail.P_Time_Sheet_ID "
	           + " Inner Join P_Payment ON P_Time_Sheet.P_Payment_ID = P_Payment.P_Payment_ID "
	           + " inner join P_Augmentation_Result on P_Augmentation_Result_Ts.P_Augmentation_Result_ID = P_Augmentation_Result.P_Augmentation_Result_ID "
	           + " inner join P_Augmentation ON P_Augmentation_Result.P_Augmentation_ID = P_Augmentation.P_Augmentation_ID " 
	           // Uniquement les gains rétro carra exonéré.
	           + " Inner Join P_Gain ON P_Gain.P_Gain_ID = P_Time_Sheet_Detail.P_Gain_ID and flag_Carra = 'RASS' "

	           + " Where P_Time_Sheet.P_Employee_ID = " + this.getP_Employee_ID()
	           + "   AND P_Time_Sheet.P_Year_ID = " + this.getP_Year_ID()
	           + "   AND P_Augmentation_Result_Ts.P_Year_ID = " + this.getP_Year_ID()
	           + "   AND Exists( select 1 from P_Augmentation_Gain "
	           + "                inner join P_Augmentation_Gain_Detail ON P_Augmentation_Gain.P_Augmentation_Gain_ID = P_Augmentation_Gain_Detail.P_Augmentation_Gain_ID "
	           + "               where P_Augmentation_Gain.P_Augmentation_ID = P_Augmentation.P_Augmentation_ID "
	           + "                 and P_Augmentation_Gain_Detail.P_Gain_ID in ( Select P_Gain_ID From P_Gain Where AbsenceCode = '" + Absence.getAbsenceCode() +"' )"
	           + "   )"

	           ;

		if ( this.getStartEmploymentDate() != null || this.getEndEmploymentDate() != null )
		{
			if  ( this.getStartEmploymentDate() != null)
				sql += " and P_Time_Sheet_Detail.Day >= " + DB.TO_DATE( this.getStartEmploymentDate() )  ;

			if  ( this.getEndEmploymentDate() != null)
				sql += " and P_Time_Sheet_Detail.Day <= " + DB.TO_DATE( this.getEndEmploymentDate() )  ;
		}

		PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	
		try
		{
	
		    ResultSet rs = stmt.executeQuery();
		    if(rs.next())
		    {
		    	AmtRetro = rs.getBigDecimal("AmtRetro");

		    }
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			log.log (Level.SEVERE, "P_Carra", e);
		}
		
		return AmtRetro;
	}

	
	private void setRetroInfo()
	{
		Timestamp DateRetroPay = null;
		BigDecimal AmtRetroNonContributory = Env.ZERO;
		BigDecimal AmtRetro = Env.ZERO;
		
		String sql = " Select max( P_Payment.PayDate) DateRetroPay, sum( case when flag_Carra = 'RASS' then dayQty else 0 end ) AmtRetro"
	           + " From P_Augmentation_Result_Ts " 
	           + " Inner Join P_Time_Sheet_Detail ON P_Augmentation_Result_Ts.P_Time_Sheet_Detail_ID = P_Time_Sheet_Detail.P_Time_Sheet_Detail_ID "
	           + " Inner Join P_Time_Sheet ON P_Time_Sheet.P_Time_Sheet_ID = P_Time_Sheet_Detail.P_Time_Sheet_ID "
	           + " Inner Join P_Payment ON P_Time_Sheet.P_Payment_ID = P_Payment.P_Payment_ID "
	           // Uniquement les gains rétro carra exonéré.
	           + " Inner Join P_Gain ON P_Gain.P_Gain_ID = P_Time_Sheet_Detail.P_Gain_ID  "
	           + " Where P_Time_Sheet.P_Employee_ID = " + this.getP_Employee_ID()
	           + "   AND P_Time_Sheet.P_Year_ID = " + this.getP_Year_ID()
	           ;

		if ( this.getStartEmploymentDate() != null || this.getEndEmploymentDate() != null )
		{
			if  ( this.getStartEmploymentDate() != null)
				sql += " and P_Time_Sheet_Detail.Day >= " + DB.TO_DATE( this.getStartEmploymentDate() )  ;

			if  ( this.getEndEmploymentDate() != null)
				sql += " and P_Time_Sheet_Detail.Day <= " + DB.TO_DATE( this.getEndEmploymentDate() )  ;
		}

		PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	
		try
		{
	
		    ResultSet rs = stmt.executeQuery();
		    if(rs.next())
		    {
		    	DateRetroPay = rs.getTimestamp("DateRetroPay");
		    	AmtRetro = rs.getBigDecimal("AmtRetro");

		    }
			rs.close();
			stmt.close();
		}
		catch(Exception e)
		{
			log.log (Level.SEVERE, "P_Carra", e);
		}
		
    	// note Retro sur assurance sans salaire... xxxx.

		this.setDateRetroPay( DateRetroPay );
		this.setAmtRetroNonContributory( AmtRetroNonContributory);
		this.setAmtRetroRREM( AmtRetro );

	}



	private void calcul () throws Exception
	{
		BigDecimal NoPayCalendar = new BigDecimal(001.00);

		
		Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), trxName);
		Year = P_Year.get( Env.getCtx(), this.getP_Year_ID(), trxName);
		int PeriodStartID = getPeriod("Start", Year, trxName);
		int PeriodEndID = getPeriod("End", Year, trxName);
		PeriodStart = P_Period.get(Env.getCtx(), PeriodStartID, trxName);
		PeriodEnd   = P_Period.get(Env.getCtx(), PeriodEndID, trxName);
		EmployeeDeduction = P_Employee_Deduction.get(Env.getCtx(), this.getP_Employee_ID(), this.getP_Deduction_ID(), PeriodEnd.getEndDate(), trxName);
		Deduction = P_Deduction.get( Env.getCtx(), this.getP_Deduction_ID(), trxName);
		Deduction_Param = this.getDeductionInfo();

        log.log( Level.INFO, "CARRA - Employé : " + Employee.getValue() + " " + Employee.getName() );

        int nbrAssignment = 1;
        nbrAssignment = getNbrAssignment();

//		exportXml();

        
    	this.setAD_Org_ID(Employee.getAD_Org_ID());

        /*
        5.1 RÉGIME DE RETRAITE
        Inscrivez le code de régime qui sert à identifier le régime de retraite de base auquel un employé
        participe. Si l’employé a participé à plus d’un régime de retraite dans l’année visée par le calendrier
        de paie, vous devez produire une déclaration distincte par régime de retraite.
        */
    	this.setPlanTyp( Deduction.getPlanTyp() );
    	
    	/*
    	5.2 GROUPE
		Le champ « Groupe » indique qu’une particularité peut être applicable à certains participants d’un
		régime de retraite. Ce champ est étroitement lié au champ « Régime de retraite » décrit
		précédemment. Il peut s’agir d’un taux de cotisation différent du régime de retraite de base ou du
		versement d’une rente viagère supplémentaire.
		Guide des informations et spécifications Fonction publique
		16
		Si aucune particularité n’est applicable au régime de base indiqué
		*/
    	this.setPlanGroup("--");

    	/*
    	Nombre de jour Cotisables
    	Généralement 260 jours. À l’occasion, le nombre
    	de jours cotisables pourrait être différent, par exemple, dans le cas d’une année de 27 paies, il y
    	aurait 270 jours cotisables.
    	*/
    	if ( PeriodEnd.getPeriodNo() == 27 || PeriodEnd.getPeriodNo() == 53)
    		ContributoryDays = 270;
    	else
    		ContributoryDays = 260;

    	
    	/*
    	 * 5.3 NUMÉRO DE CALENDRIER DE PAIE - CARRA
		Vous devez indiquer le numéro de calendrier de paie que vous avez utilisé pour la déclaration du
		participant. Ce numéro a été attribué par la CARRA lorsque vous avez confirmé les données sur vos
		calendriers de paie.
		Pour chaque participant, la section financière de base doit contenir un seul calendrier de paie. Donc,
		si un employé a été payé en fonction de deux calendriers de paie, vous devrez produire deux
		sections financières distinctes.
		*/
    	this.setNoPayCalendar(NoPayCalendar);

    	
        /*
		5.4 CORPS D’EMPLOI
		Ce champ à dix positions alphanumériques doit être rempli pour chaque déclaration produite.
		SIQ - décision prise de fournir le numéro du group d'emploi
		*/
//        P_Occupation_Group Occupation_Group = P_Occupation_Group.get(Env.getCtx(), Employee.getP_Occupation_Group_ID(), trxName); 
//		this.setJobGroups( Occupation_Group.getValue() );
		this.setP_Occupation_Group_ID( Employee.getP_Occupation_Group_ID());
		
		/*
		5.5 COTISATION PATRONALE
		Lorsque l’employeur est tenu de verser sa cotisation patronale à la CARRA, vous devez inscrire
		« O » pour oui. Lorsqu’il n’est pas tenu de la verser, vous devez indiquer « N » pour non.
		*/
		this.setEmployerContribution( true );
		
		/*
		 5.6 TYPE DE DÉCLARATION
			Les types possibles de déclaration sont 1, 2 ou 3. Vous trouverez ci-dessous la description de
			chacun des types. Si aucune des situations énumérées ci-dessous ne s’applique, vous devez laisser
			le champ en blanc.
			Vous devez produire une section financière distincte par type de déclaration. Par exemple, pour un
			même employé qui aurait le type « 2 » et le type « 3 » pour la période couverte par le calendrier de
			paie, vous devez produire deux sections financières dont une avec le type « 2 » et l’autre avec le
			type « 3 », avec les données financières se rattachant à chacun des types.
		 */
    	this.setDAType();
    	
    	/*
    	 5.7 FACTEUR QUOTIDIEN
		Dans les conditions de travail des participants, nous retrouvons le salaire à l’échelle sur une base
		annuelle de même que le nombre d’heures, de jours ou de semaines associés à ce salaire.
		L’information à inscrire est le nombre de jours ou l’équivalent en nombre de jours.
		Ce champ doit être rempli pour chaque déclaration produite. Le facteur quotidien applicable pour la
		fonction publique est 260,9.
		Si vous croyez avoir un facteur quotidien différent de 260,9, veuillez communiquer avec la
		Commission administrative des régimes de retraite et d’assurances
    	 */
    	this.setDayFactor ();
    	
    	/*
    	5.8 BASE DE RÉMUNÉRATION
		Vous devez indiquer dans ce champ la base de rémunération utilisée. Cette dernière représente le
		nombre de jours cotisables dans une année de service (du 1er janvier au 31 décembre). Pour tous
		Guide des informations et spécifications Fonction publique
		21
		les employés du réseau de la fonction publique qui participent à un régime de retraite administré par
		la CARRA, inscrivez 260.
    	 */
    	this.setRemunerationBase();
    	
    	/*
    	 5.9 DATE DE DÉBUT
		 La « Date de début » est en relation avec le régime de retraite, le groupe, le facteur quotidien, le
		 type de déclaration et la période couverte par le calendrier de paie.
    	 */
    	this.setStartEmploymentDate ();
    	
    	/*
    	5.10 DATE DE FIN
    	La « Date de fin » est en relation avec le régime de retraite, le groupe, le facteur quotidien, le type
    	de déclaration et la période couverte par le calendrier de paie.
    	*/
    	this.setEndEmploymentDate ();
    	
    	/*
    	5.11 SALAIRE COTISABLE
    	Le salaire cotisable correspond généralement au salaire à l’échelle qui est prévu par le contrat de
    	travail ou la convention collective pour l’année couverte par le calendrier de paie.
    	*/
    	this.setContributoryEarnings();
    	
    	/*
    	  Salaire Versé
    	*/
    	
    	this.setSalaryPay();
    	
    	
    	/*
    	5.14 SALAIRE APRÈS 35 ANNÉES DE SERVICE (NON COTISABLE)
    	Dès qu’un participant du RREGOP, du RRPE, du RRCE, du RRAS, du RRE ou du RRF atteint
    	35 années de service pour le calcul de la rente de retraite, le salaire versé devient du salaire après
    	35 années de service. Aucune cotisation ne doit être prélevée sur le salaire de ce participant pour la
    	partie de l’année suivant l’atteinte de 35 années de service. Il cesse alors de cotiser à son régime de
    	retraite. Le salaire après 35 années de service servira à établir le salaire admissible moyen lors du
    	calcul de la rente de retraite.
    	 */
    	this.setSalary35Year();
    	
    	/*
    	5.15 SALAIRE ANNUEL DE BASE (SAB)
    	 */
    	this.setSalaryAnnualBase ( nbrAssignment);

    	
    	
    	/*
    	5.16 SALAIRE ANNUEL DE BASE PONDÉRÉ
    	Si le salaire annuel de base inscrit au champ précédent « Salaire annuel de base (SAB) » est le
    	résultat de la régularisation des salaires annuels de base de plus d’un emploi, vous devez inscrire
    	« oui ».
    	*/
    	if ( nbrAssignment > 1)
    		this.setWeighted( true );
    	else
    		this.setWeighted( false );
    		
    	
    	//
    	this.setRetroInfo();

    	this.save();

		/*
		6. SECTION FINANCIÈRE VARIABLE
		La section financière variable est rattachée à la section financière de base. Par conséquent, étant
		donné que la section financière de base ne doit contenir qu’un seul régime de retraite, un seul
		groupe, un seul calendrier, un seul facteur quotidien et un seul type de déclaration par participant,
		une section financière variable sera générée chaque fois qu’une section financière de base le sera.
		Vous devez donc déclarer les données qui sont reliées.
		*/
		
		/*
		6.1 ABSENCES
		Les différentes absences qui doivent être considérées dans cette sous-section sont réparties selon
		trois catégories : « Absence dont le salaire est non cotisable et dont le service est crédité »,
		« Absence dont le salaire est cotisable et dont le service est crédité», et « Absence dont le salaire
		est non cotisable et dont le service est non crédité ».
		*/
    	this.Insert_Absence();
    	
    	/*
    	6.2 RÉTROACTIVITÉ POUR DES ANNÉES ANTÉRIEURES
    	Cette sous-section doit toujours être remplie lorsqu’il y a un montant de rétroactivité pour une ou
    	plusieurs années antérieures à déclarer durant une année. Pour se qualifier comme montant de
    	rétroactivité au sens de la déclaration annuelle à la CARRA, le montant doit avoir été payé à titre
    	d’augmentation ou de rajustement du salaire admissible d’une période antérieure à l’année civile
    	pendant laquelle ce montant est versé.
    	*/
		this.Insert_Retro();

		this.Insert_Sabbatical();

    	/*
    	5.13 PARTIEL - % DU TEMPS
    	Ce champ numérique à six positions dont quatre après la virgule sert à identifier les employés à
    	temps partiel, c’est-à-dire, les employés qui occupent un poste selon un horaire régulier mais
    	incomplet. L’employé à contrat, l’employé occasionnel et l’employé saisonnier font partie de cette
    	catégorie s’ils ne travaillent pas toute la période couverte par le calendrier de paie de l’année à
    	déclarer.
    	*/
    	this.setPartTime();

    	this.calcul_fe( );
    	this.save();
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */
	
	protected boolean beforeSave (boolean newRecord)
	{

		//+ 2011.07.05 + sécurité par compagnie.
		P_Employee Employee = P_Employee.get(getCtx(), getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.
//		if ( newRecord)
//			calcul();
    
    	return true;
	}

	//
	//
	//
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if ( ! success)
			return success;
		
		
		return true;
	}

	
	private int getNbrAssignment( )
	{
		String sql = "Select count(*) From ( "
			+ " select distinct P_Assignment_ID " 
            + " from P_Payment_Gain  "
            + "  Inner Join P_Payment On P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID "
            + " where P_Payment.P_Employee_ID = " + Employee.getP_Employee_ID() 
            + "	 and P_Payment.P_Year_ID = " + Year.getP_Year_ID()
            + " ) Detail "
            ;

	     int nbrAssignment = 0;

	     PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	     try
	     {
		     ResultSet rs = stmt.executeQuery();
		     if (rs.next())
		     {
		    	 nbrAssignment = rs.getInt( 1 );
		     }
		     rs.close();
		     stmt.close();
	     }
	     catch(Exception e)
	     {
			log.log (Level.SEVERE, "P_Carra", e);
	     }
			
	     return nbrAssignment;
	}

	
	public static int Calcul ( Properties ctx, int P_Year_ID, int P_Employee_ID, int P_Deduction_ID, boolean Update, String trxName )
	{
		
		String sql = "select distinct p_employee_deduction.p_employee_id, p_employee_deduction.p_deduction_id, P_Carra.P_Carra_ID " 
                   + " from p_employee_deduction " 
                   + "    inner join p_deduction on p_deduction.p_deduction_id = p_employee_deduction.p_deduction_id "
                   + "    left outer join P_Carra ON ( P_Carra.P_Employee_ID = P_Employee_Deduction.P_Employee_ID "
      			   + " 		and P_Carra.P_Deduction_ID = P_Employee_Deduction.P_Deduction_ID "
      			   + " 			and P_Carra.P_Year_ID = " + P_Year_ID + " ) "
                   + " where P_Deduction.PlanTyp is not null "
                   + " and exists( select 1 from P_Payment_Deduction " 
                   + "              where P_Payment_Deduction.P_Deduction_ID = P_Employee_Deduction.P_Deduction_ID "
                   + "              and P_Payment_Deduction.P_Employee_ID = P_Employee_Deduction.P_Employee_ID "
                   + "		        and P_Payment_Deduction.P_Year_ID = " + P_Year_ID + " )	"
                   ;
		
		if ( P_Employee_ID != 0 )
			sql += " and P_Employee_Deduction.P_Employee_ID = " + P_Employee_ID;
		
		if ( P_Deduction_ID != 0 )
			sql += " and P_Employee_Deduction.P_Deduction_ID = " + P_Deduction_ID;
		
		if ( ! Update )
			sql += " and not exists( select 1 from P_Carra Where P_Carra.P_Employee_ID = P_Employee_Deduction.P_Employee_ID "
				 + " and P_Carra.P_Deduction_ID = P_Employee_Deduction.P_Deduction_ID "
				 + " and P_Carra.P_Year_ID = " + P_Year_ID + " ) ";
		
		int no = 0;
		
        PreparedStatement stmt = DB.prepareStatement(sql, trxName);
    	try
    	{
     
    		
            ResultSet rs = stmt.executeQuery();
            P_Carra Carra;
            while(rs.next())
            {

            	if ( rs.getInt( "P_Carra_ID") == 0 )
            		Carra = new P_Carra( ctx, -1, trxName);
            	else
            		Carra = new P_Carra( ctx, rs.getInt( "P_Carra_ID"), trxName);
            	
            	if ( ! Carra.isDone() )
            	{
                	Carra.setP_Employee_ID(rs.getInt( "P_Employee_ID"));
                	Carra.setP_Deduction_ID(rs.getInt("P_Deduction_ID"));
                	Carra.setP_Year_ID(P_Year_ID);
                	Carra.save();
                	
                	Carra.calcul();
            
                	no++;
            	}
        	}
    		rs.close();
    		stmt.close();
    	}
    	catch(Exception e)
    	{
    		log.log (Level.SEVERE, "P_Carra", e);
    	}
    	return no;
	}

	private static Document xmlDocument( Properties ctx, int P_Year_ID, int P_Employee_ID, int P_Deduction_ID, String trxName)
	{
		P_Year Year = P_Year.get( ctx, P_Year_ID, trxName);

		Document document = null;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			document = builder.newDocument();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		//	Root
		
		Element root = document.createElement("Emetteur");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:noNamespaceSchemaLocation", "C:\\Pgi\\TransferFichier_2.0.xsd");
		root.setAttribute("NoEmployeurEmetteur", "Q1004746");
		root.setAttribute("anneeTraitement", String.valueOf(Year.getYear() ) );
		

		// NEQ
		// 8822720825
		// No Employeur Emetteur
		// Q1004746
		//Minister
		//562
		document.appendChild(root);

    	MOrg Org = MOrg.get( ctx, Year.getAD_Org_ID() );
    	MOrgInfo OrgInfo = MOrgInfo.get( Env.getCtx(),Org.getAD_Org_ID(), null);
    	MLocation location = MLocation.get( Env.getCtx(), OrgInfo.getC_Location_ID(), null );

		Element elementEmployers = document.createElement("Employeurs");
		root.appendChild(elementEmployers);
		
		Element elementEmployer = document.createElement("Employeur");
/*
		Element elementAdr = document.createElement("ADRESSE");
		createElement( document, elementAdr, "NoCivique", "");
		createElement( document, elementAdr, "RueAveBoul", location.getAddress1() );
		createElement( document, elementAdr, "Appartement", "");
		createElement( document, elementAdr, "CasierPostal", "");
		createElement( document, elementAdr, "Succursale", "");
		createElement( document, elementAdr, "Ville", formatString( location.getCity() ));
		createElement( document, elementAdr, "CodePostal", location.getPostal());
		createElement( document, elementAdr, "Succursale", "");

		P_Employer Employer = P_Employer.get( Env.getCtx(), 1000040, trxName);
		
		elementEmployer.appendChild(elementAdr);
*/		

		elementEmployers.appendChild(elementEmployer);

		
		int no = 0;
		String sql = " Select P_Carra_ID From P_Carra where P_Year_ID = " + P_Year_ID;
		if ( P_Employee_ID != 0)
			sql += " AND P_Employee_ID = " + P_Employee_ID;

		if ( P_Deduction_ID != 0)
			sql += " AND P_Deduction_ID = " + P_Deduction_ID;

        PreparedStatement stmt = DB.prepareStatement(sql, trxName);
    	try
    	{
     
    		Element elementParticipants = document.createElement("Participants");
    		elementEmployer.appendChild(elementParticipants);
    		
            ResultSet rs = stmt.executeQuery();
            P_Carra Carra;
            P_Employee Employee;
            while(rs.next())
            {
            	Carra = P_Carra.get( ctx, rs.getInt("P_Carra_ID"), trxName);
        		Employee = P_Employee.get( Env.getCtx(), Carra.getP_Employee_ID(), trxName);

        		Element element = document.createElement("Participant");
        		elementParticipants.appendChild(element);

        		Element elementLineDas = document.createElement( "LigneDAs");
        		element.appendChild( elementLineDas );

        		//        		
        		Element elementLine = document.createElement("LigneDA");
        		elementLineDas.appendChild(elementLine);
        		
            	no++;

            	String sqlAbsence = " Select * From P_Carra_Absence Where P_Carra_ID = " + Carra.getP_Carra_ID();
                PreparedStatement stmtAbsence = DB.prepareStatement(sqlAbsence, trxName);
                ResultSet rsAbsence = stmtAbsence.executeQuery();


        		Element elementAbsences = document.createElement("Absences");
                
                while(rsAbsence.next())
                {
                	if ( rsAbsence.getRow() == 1)
                	{
                		elementLine.appendChild(elementAbsences);
                	}
                		
            		Element elementAbsence = document.createElement("Absence");
            		createElement( document,  elementAbsence,"CodeAbsence", rsAbsence.getString("AbsenceCode"));
            		createElement( document,  elementAbsence,"NbJour", rsAbsence.getString("NbrDays"));
            		createElement( document,  elementAbsence,"Salaire", rsAbsence.getString("AbsenceSalary"));
            		createElement( document,  elementAbsence,"Retro", rsAbsence.getString("AbsenceRetroAmt"));
            		elementAbsences.appendChild(elementAbsence);
                }
                rsAbsence.close();
                stmtAbsence.close();

            	String sqlSabbatical = " Select * From P_Carra_Sabbatical Where P_Carra_ID = " + Carra.getP_Carra_ID();
                PreparedStatement stmtSabbatical = DB.prepareStatement(sqlSabbatical, trxName);
                ResultSet rsSabbatical = stmtSabbatical.executeQuery();

        		Element EchelleTraitements = document.createElement("EchelleTraitements");

                
                while(rsSabbatical.next())
                {
                	if ( rsSabbatical.getRow() == 1)
                	{
                		elementLine.appendChild(EchelleTraitements);
                	}

                	
                	Element elementSabbatical = document.createElement("EchelleTraitement");
            		createElement( document,  elementSabbatical,"DtApplication", rsSabbatical.getString("StartDateSabbatical").substring(0,10) + "T00:00:00" );
            		createElement( document,  elementSabbatical,"SalaireAnnuelBase", rsSabbatical.getString("AnnualSalary"));
            		EchelleTraitements.appendChild(elementSabbatical);
                }
				rsSabbatical.close();
				stmtSabbatical.close();

            	String sqlRetro = " Select * From P_Carra_Retro inner join P_Year on P_Year.P_Year_ID = P_Carra_Retro.P_Year_ID Where P_Carra_ID = " + Carra.getP_Carra_ID();
                PreparedStatement stmtRetro = DB.prepareStatement(sqlRetro, trxName);
                ResultSet rsRetro = stmtRetro.executeQuery();
                
        		Element elementRetros = document.createElement("Retros");

                while(rsRetro.next())
                {
                	if ( rsRetro.getRow() == 1)
                	{
                		elementLine.appendChild(elementRetros);
                	}
                		
                	Element elementRetro = document.createElement("Retro");
            		createElement( document,  elementRetro,"AnneeRetro", rsRetro.getString( "Year"));
            		createElement( document,  elementRetro,"MtRetro", rsRetro.getString("RetroAmt"));
            		elementRetros.appendChild(elementRetro);
                }
                rsRetro.close();
                stmtRetro.close();

        		createElement( document,  element, "CodeLangue", "F");
        		createElement( document,  element, "DtNaissance", String.valueOf( Employee.getBirthDate()).substring(0,10) + "T00:00:00" );
        		createElement( document,  element, "NAS", Employee.getSin().replace("-", ""));
        		createElement( document,  element, "NoEmploye", Employee.getValue());
        		createElement( document,  element, "Nom", Employee.getSurname());
        		createElement( document,  element, "NomNaissance", "");
        		createElement( document,  element, "Prenom", Employee.getFirstName());
        		createElement( document,  element, "Sexe",Employee.getGender());
//        		MActivity Activity = MActivity.get( Env.getCtx(), this.getC_Activity_ID, trxName);
//        		element.setAttribute("UniteAdministrative", Activity.getValue());

        		

        		createElement( document,  elementLine, "TypeRegime", "0" + Carra.getPlanTyp());
        		
        		if ( Carra.getPlanGroup().equals("--"))
        			createElement( document,  elementLine, "Groupe", null );
        		else
        			createElement( document,  elementLine,"Groupe", Carra.getPlanGroup());
        			
        		createElement( document,  elementLine,"NoCalendrierPaie", String.valueOf(Carra.getNoPayCalendar()));
        		if ( Carra.getNoEmployment().compareTo(Env.ZERO) != 0)
        			createElement( document,  elementLine,"NoEmploi", String.valueOf(Carra.getNoEmployment()));
        		else
        			createElement( document,  elementLine,"NoEmploi", null);
        			


                P_Occupation_Group Occupation_Group = P_Occupation_Group.get(Env.getCtx(), Carra.getP_Occupation_Group_ID(), trxName); 
                createElement( document,  elementLine,"CorpsEmploi", Occupation_Group.getValue() );
        				
        		if  ( Carra.isEmployerContribution() )
        			createElement( document,  elementLine,"CotisationPatronal", "O" );
        		else
        			createElement( document,  elementLine,"CotisationPatronal", "N" );
        		
        		if ( Carra.getDAType().equals(" "))
        			createElement( document,  elementLine,"TypeDA", null);
        		else
        			createElement( document,  elementLine,"TypeDA", Carra.getDAType());
        		
        		if ( Carra.getDayFactorVal().equals(P_Carra.DAYFACTORVAL_FacteurQuotidienSurBaseDe2609Jours))
        			createElement( document,  elementLine,"FacteurQuotidien", "260.9");
        		else
        			createElement( document,  elementLine,"FacteurQuotidien", Carra.getDayFactorVal());
        		createElement( document,  elementLine,"BaseRemuneration", Carra.getRemunerationBase());
        		
        		if ( Carra.getStartEmploymentDate() != null)
        			createElement( document,  elementLine,"DtDebutEmploi", String.valueOf(Carra.getStartEmploymentDate()).substring(0,10) + "T00:00:00");
        		else
        			createElement( document,  elementLine,"DtDebutEmploi", null );

        		if ( Carra.getEndEmploymentDate() != null )
        			createElement( document,  elementLine,"DtFinEmploi", String.valueOf(Carra.getEndEmploymentDate()).substring(0,10) + "T00:00:00");
        		else
        			createElement( document,  elementLine,"DtFinEmploi", null);

        		createElement( document,  elementLine,"SalaireCotisable", String.valueOf( Carra.getContributoryEarnings()));
        		createElement( document,  elementLine,"Cotisation", String.valueOf( Carra.getContribution()));

        		if ( Carra.getPartTime().compareTo( new BigDecimal(100)) != 0 &&  Carra.getPartTime().compareTo( new BigDecimal(0)) != 0 )
        			createElement( document,  elementLine,"PartielTemps", String.valueOf( Carra.getPartTime()));
        		else
        			createElement( document,  elementLine,"PartielTemps", null);

        		createElement( document,  elementLine,"Salaire35Ans", String.valueOf( Carra.getSalary35Year() ));
        		createElement( document,  elementLine,"SalaireAnnuelBase", String.valueOf( Carra.getSalaryAnnualBase() ));
        		
        		createElement( document,  elementLine,"SalaireVerse", String.valueOf( Carra.getSalaryPay() ));
        		if ( Carra.isWeighted() )
        			createElement( document,  elementLine,"Pondere", "O");
        		else
        			createElement( document,  elementLine,"Pondere", "N");

        		
        		if ( Carra.getDateRetroPay() != null)
        			createElement( document,  elementLine,"DtVersementRetro", String.valueOf(Carra.getDateRetroPay()).substring(0,10) + "T00:00:00");
        		else
        			createElement( document,  elementLine,"DtVersementRetro", null );
        			
        		createElement( document,  elementLine,"MtRetroNonCotisable", String.valueOf( Carra.getAmtRetroNonContributory() ));
        		createElement( document,  elementLine,"MtRetroRREM", String.valueOf( Carra.getAmtRetroRREM() ));


                
            }
			rs.close();
			stmt.close();
			
			createElement( document,  elementEmployer, "NEQ", "8822720825");
			createElement( document,  elementEmployer, "NoCentreTraiteur", null);
			createElement( document,  elementEmployer, "NoEmployeur", "Q1004746");
			createElement( document,  elementEmployer, "NoMinistere", "562");

			
			int PeriodStartID = P_Carra.getPeriod("Start", Year, trxName);
			int PeriodEndID = P_Carra.getPeriod("End", Year, trxName);
			P_Period PeriodStart = P_Period.get(Env.getCtx(), PeriodStartID, trxName);
			P_Period PeriodEnd   = P_Period.get(Env.getCtx(), PeriodEndID, trxName);

			createElement( document,  elementEmployer, "DateDebut", String.valueOf( PeriodStart.getStartDate()).substring(0,10) + "T00:00:00");
			createElement( document,  elementEmployer, "DateFin" , String.valueOf( PeriodEnd.getStartDate()).substring(0,10) + "T00:00:00");

		}
		catch(Exception e)
		{
			log.log (Level.SEVERE, "P_Carra", e);
		}


		
		return document;

		
	}
	
	 public static void createElement(org.w3c.dom.Document doc, Element parent, String nodeName, String nodeValue)
	   {
		  if ( nodeValue != null )
		  {
		      Element elem = doc.createElement(nodeName);
		      Text text = doc.createTextNode(nodeValue);
		      elem.appendChild(text);
		      parent.appendChild(elem);
		  }
		  else
		  {
		      Element elem = doc.createElement(nodeName);
		      elem.setAttribute("xsi:nil", "true");
		      parent.appendChild(elem);
			  
		  }
	   }
	 
	public static void exportXml( Properties ctx, int P_Year_ID, int P_Employee_ID, int P_Deduction_ID, String trxName)
	{
		StringBuffer xml = new StringBuffer();
		try
		{
		
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			DOMSource source = new DOMSource( P_Carra.xmlDocument( ctx, P_Year_ID, P_Employee_ID, P_Deduction_ID, trxName));
			TransformerFactory tFactory = TransformerFactory.newInstance();
			
			Transformer transformer = tFactory.newTransformer();

			transformer.setOutputProperty("encoding", "iso-8859-1");
			transformer.setOutputProperty("indent", "yes");


			transformer.transform (source, result);


			StringBuffer newXML = writer.getBuffer();
	
			//
			if (xml.length() != 0)
			{	//	//	<?xml version="1.0" encoding="UTF-8"?>
				int tagIndex = newXML.indexOf("?>");
				if (tagIndex != -1)
					xml.append(newXML.substring(tagIndex+2));
				else
					xml.append(newXML);
			}
			else
				xml.append(newXML);

	    	String Path = PgiUtil.getSolsticeParameter(Env.getCtx(), "TransfertPath");
			P_Year Year = P_Year.get( ctx, P_Year_ID, trxName);
	    	WriteToFile( xml , Path + "\\Carra_" + String.valueOf( Year.getYear()) + ".xml");
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}

	}
	
    private static void WriteToFile(StringBuffer NewFile, String FileName)
    {
    	

    	File aFile = null;
  	    aFile = new File( FileName ); 
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object
        try
        {
              // Create a new file output stream
              out = new FileOutputStream(aFile);

              // Connect print stream to the output stream
              p = new PrintStream( out );

              p.println (NewFile.toString());
//              System.out.println(NewFile.toString());
              p.close();
        }
        catch (Exception e)
        {
        	log.log (Level.SEVERE, "Error writing to file :", e);
        }
   }

    
    // ind = 1 pour nbrDays et 2 pour Salary
    private BigDecimal getAbsenceInfo( String absenceCode, int ind )
    {
    	BigDecimal bd = Env.ZERO;
    	String sql = " Select sum( nbrDays), sum( AbsenceSalary) From P_Carra_Absence Where P_Carra_ID = " + this.getP_Carra_ID()
    	           + " And AbsenceCode in (" + absenceCode + ")"
    	           ;
	     PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	     try
	     {
	    	 
		     ResultSet rs = stmt.executeQuery();
		     if (rs.next())
		     {
		    	 bd = rs.getBigDecimal( ind );
		     }	
		     rs.close();
		     stmt.close();
	     }
	     catch(Exception e)
	     {
			log.log (Level.SEVERE, "P_Carra", e);
	     }

	     if ( bd == null)
	    	 return Env.ZERO;
	     
	     return bd;
    }
    
    // a initialier
    private BigDecimal YMPE;
    private BigDecimal RRSP;
    private BigDecimal annualBase;
    private BigDecimal maximumAmountETC;
    private BigDecimal exemptionPercentage;
    private BigDecimal maximumAmountFE;

    private BigDecimal joursDAbsence = Env.ZERO;
    private BigDecimal BaseSalary = Env.ZERO;
    private BigDecimal contributoryDays = Env.ZERO;
    private BigDecimal disabilityDays;
    private BigDecimal motherhoodDays;
    private int nbrPeriod;
    private BigDecimal joursTRD;
    private BigDecimal calculPercentage;
    private BigDecimal exoneratedSalary;
    private BigDecimal motherhoodSalary;
    private BigDecimal contributorySalary;
    private BigDecimal manualRetro;
    private BigDecimal theoriticalRetro;
    private BigDecimal adjustedDeductibleSalaray;

	private BigDecimal computedService;
    private BigDecimal creditedService;
    private BigDecimal deductibleSalary;
    private BigDecimal annualDeductibleSalaray;
    private BigDecimal exemption;
    private BigDecimal adjustmentFactorStandardAmount;

    private BigDecimal adjustmentFactor;
    private BigDecimal adjustementFactorAmount;

    private BigDecimal nvl ( BigDecimal bd )
    {
    	if ( bd == null )
    		return Env.ZERO;
    	return bd;
    }
    
	private void calcul_fe( ) throws Exception
	{
		BigDecimal bdHundred = new BigDecimal(100);
			
        String sql
        = "select Annual_Base, MaximumAmountETC, MaximumAmountFE, CalculPercentage,"
            + " RRSP, ExemptionPercentage, YMPE, CARRA"
            + " from P_Equivalence_Factor_Param"
            + " where P_Deduction_ID = " + this.getP_Deduction_ID()
            + " and P_Year_ID = " + this.getP_Year_ID()
            + " and IsActive = 'Y'";
        
        PreparedStatement stmt = DB.prepareStatement(sql, trxName);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            this.annualBase = rs.getBigDecimal("Annual_Base");
            this.maximumAmountETC = rs.getBigDecimal("MaximumAmountETC");
            this.YMPE = rs.getBigDecimal("YMPE");
            this.exemptionPercentage = rs.getBigDecimal("ExemptionPercentage");
            this.calculPercentage = rs.getBigDecimal("CalculPercentage");
            this.RRSP = rs.getBigDecimal("RRSP");
            this.maximumAmountFE = rs.getBigDecimal("MaximumAmountFE");
        }
        
        rs.close();
        stmt.close();


//        joursDAbsence = getAbsenceInfo( "'A1','B1','C1','C2','D1','D2','D3','D4','D5','D6','E1','E2','E3'", 1);
        BaseSalary = nvl( this.getSalaryAnnualBase() ); 
        contributoryDays = nvl(  this.getContributoryDays() );
        contributorySalary = nvl(  this.getContributoryEarnings() );
        // jours d'invalidité non cotisables
        disabilityDays = getAbsenceInfo( "'E1','E2','E3'", 1);
        // jours et salaire d'absence pour congé de maternité
        motherhoodDays = getAbsenceInfo( "'B1'", 1);
        motherhoodSalary = getAbsenceInfo( "'B1'", 2);

        
        exoneratedSalary = Env.ZERO; //getAbsenceInfo( "'D3'", 2);


        manualRetro = nvl(  this.getAmtRetroRREM() );

        theoriticalRetro = Env.ZERO;
        adjustedDeductibleSalaray = Env.ZERO;
        
   		nbrPeriod = PeriodEnd.getPeriodNo();

   		// Jours en congé sabbatique (CSTD)
   		joursTRD = getAbsenceInfo( "'C1'", 1);
        
	    
//	    Calcul FE Début
        BigDecimal tempValue = this.annualBase.multiply(new BigDecimal(nbrPeriod).divide(new BigDecimal(26), 4, BigDecimal.ROUND_HALF_UP));

        //service calculés = (jours cot + jours inv + jours mat + jours trd ) / base annuel * nbr periode / 26
        this.computedService = this.contributoryDays.add(this.disabilityDays.add(this.motherhoodDays).add(this.joursTRD));
        this.computedService = this.computedService.divide(tempValue, 4, BigDecimal.ROUND_HALF_UP);
        this.computedService = this.computedService.setScale(2, BigDecimal.ROUND_HALF_UP);
        
        //service crédité = (jours cot + jours inv + jours mat ) / base annuel * nbr periode / 26
        this.creditedService = this.contributoryDays.add(this.disabilityDays.add(this.motherhoodDays));
        this.creditedService = this.creditedService.divide(tempValue, 4, BigDecimal.ROUND_HALF_UP);
        this.creditedService = this.creditedService.setScale(2, BigDecimal.ROUND_HALF_UP);

        //salaire admissible = salaire cotisable + salaire exonéré + salaire de maternité
        this.deductibleSalary = this.contributorySalary.add(this.exoneratedSalary.add(this.motherhoodSalary));

        this.annualDeductibleSalaray = Env.ZERO;
        
        if(this.computedService.compareTo(Env.ZERO) != 0)
        {
        	//si le service calculé est différent de 0, on set annual deductible salary :
        	// salaire admissible / service calculé
        	this.annualDeductibleSalaray = this.deductibleSalary.divide(this.computedService, 4, BigDecimal.ROUND_HALF_UP);
        }
        
        this.theoriticalRetro = Env.ZERO;
        if(this.annualDeductibleSalaray.compareTo(this.maximumAmountETC) > 0 && this.BaseSalary.compareTo(this.maximumAmountETC) < 0)
        {
        	this.theoriticalRetro = this.contributorySalary.subtract(this.BaseSalary);
        	//si la retro theorique est moins que 0, on l'a met a 0
        	if(this.theoriticalRetro.compareTo(Env.ZERO) < 0)
        	{
        		this.theoriticalRetro = Env.ZERO;
        	}
        }
        
        if(this.computedService != null && this.computedService.compareTo(Env.ZERO) != 0)
        {
        	//salaire admissible ajusté = salaire admissible - retro annuel / service calc * service credit
        	this.adjustedDeductibleSalaray = ((this.deductibleSalary.subtract(this.theoriticalRetro)).divide(this.computedService, 4, BigDecimal.ROUND_HALF_UP)).multiply(this.creditedService);
        }
        
        if(this.annualDeductibleSalaray.compareTo(this.YMPE) <= 0)
        {
        	this.exemption = this.exemptionPercentage.multiply(this.annualDeductibleSalaray.multiply(this.creditedService)).divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP);
        }
        else
        {
        	this.exemption = this.exemptionPercentage.multiply(this.YMPE.multiply(this.creditedService)).divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP);
        }
        if(this.exemption.compareTo(Env.ZERO) < 0)
        {
        	this.exemption = Env.ZERO;
        }

        this.adjustmentFactorStandardAmount = ((this.adjustedDeductibleSalaray.subtract(this.exemption))
        										.multiply(this.calculPercentage.divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP))).subtract((this.creditedService.multiply(this.RRSP)));
        
        //System.out.println("adjustedDeductibleSalaray :"+this.adjustedDeductibleSalaray);
        //System.out.println("exemption :"+this.exemption);
        //System.out.println("calculPercentage :"+this.calculPercentage);
        //System.out.println("creditedService :"+this.creditedService);
        //System.out.println("RRSP :"+this.RRSP);
        if(this.adjustmentFactorStandardAmount.compareTo(Env.ZERO) < 0)
        {
        	this.adjustmentFactorStandardAmount = Env.ZERO;
        }
        
        this.adjustmentFactor = this.theoriticalRetro.multiply(this.calculPercentage);
        
        if(this.manualRetro.compareTo(Env.ZERO) != 0)
        {
        	this.adjustmentFactor = this.manualRetro.multiply(this.calculPercentage);
        }
        else
        {
        	this.manualRetro = this.theoriticalRetro;
        }
        
        if(this.adjustmentFactor.compareTo(Env.ZERO) < 0)
        {
        	this.adjustmentFactor = Env.ZERO;
        }
        
        this.adjustementFactorAmount = this.adjustmentFactorStandardAmount.add(this.adjustmentFactor);
        
        if(this.adjustementFactorAmount.compareTo(this.maximumAmountFE) > 0)
        {
        	this.adjustementFactorAmount = this.maximumAmountFE;
        }

        // si le service calculé est égal a 0 et le service crédité
        // alors le montant de Fe = 18% du salaire cotisable.
        if ( this.computedService.compareTo(Env.ZERO) == 0 && this.creditedService.compareTo(Env.ZERO) == 0 )
        {
        	this.adjustmentFactorStandardAmount = this.contributorySalary.multiply(this.calculPercentage.divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP));
        	this.adjustementFactorAmount = this.contributorySalary.multiply(this.calculPercentage.divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP));
        }
        
        this.setComputedService(computedService);
        this.setCreditedService(creditedService);
        this.setAdjustmentFactorAmount(adjustementFactorAmount);
        
	}

	private static String formatString(String theInput)
	{
		theInput = theInput.toUpperCase();
		theInput = theInput.replace('É', 'E');
		theInput = theInput.replace('È', 'E');
		theInput = theInput.replace('Ê', 'E');
		theInput = theInput.replace('À', 'A');
		theInput = theInput.replace('À', 'A');
		theInput = theInput.replace('Â', 'A');
		theInput = theInput.replace('Î', 'I');
		theInput = theInput.replace('Ï', 'I');
		theInput = theInput.replace('Ì', 'I');
		theInput = theInput.replace('Ö', 'O');
		theInput = theInput.replace('Ç', 'C');
		
		return theInput;
	}
	
}	//	P_Carra


