# Create Oracle SQLJ
# Author + Copyright 1999-2005 Jorg Janke
# $Id: create.sh,v 1.2 2007/07/19 21:41:45 marmor01 Exp $
#  
# Parameter: <compiereDBuser>/<compiereDBpassword>

# unset CLASSPATH=

echo .
echo Load Oracle SQLJ ...
loadjava -user $1@$COMPIERE_DB_NAME -verbose -force -resolve $COMPIERE_HOME/lib/sqlj.jar

echo .
echo Create Oracle Functions ...
sqlplus $1@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/oracle/createSQLJ.sql $COMPIERE_DB_USER
