@Title Compile + Jar SQLJ
@Rem	@version $Id: compile.bat,v 1.2 2007/07/19 15:27:40 marmor01 Exp $
@Rem
@Rem	Note that some databases require an older Java version
@Rem	and that the Zip is uncompressed
@Rem
@Rem	Oracle: 1.4.2 - (you can use RUN_Build)
@Rem	Sybase: 1.2.2 - 
@Rem
@SET PATH=C:\jdk1.2.2\bin;%PATH%
@SET JAVA_HOME=C:\jdk1.2.2
@java -version

javac -sourcepath src -d lib src/org/compiere/sqlj/Compiere.java src/org/compiere/sqlj/Product.java src/org/compiere/sqlj/Currency.java src/org/compiere/sqlj/BPartner.java src/org/compiere/sqlj/Invoice.java src/org/compiere/sqlj/Payment.java src/org/compiere/sqlj/PaymentTerm.java src/org/compiere/sqlj/Account.java

jar cf0 sqlj.jar -C lib org/compiere/sqlj

pause