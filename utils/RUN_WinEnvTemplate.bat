@Title Set Windows Environment
@Rem $Id: RUN_WinEnvTemplate.bat,v 1.2 2007/07/19 15:29:42 marmor01 Exp $

@Echo ===================================
@Echo Setup Client Environment
@Echo ===================================

@cscript //nologo @COMPIERE_HOME@\utils\WinEnv.js "@COMPIERE_HOME@" "@JAVA_HOME@"

