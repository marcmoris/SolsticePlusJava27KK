# $Id: ImportCompiere.sh,v 1.2 2007/07/19 21:48:20 marmor01 Exp $
echo	Compiere Database Import		$Revision: 1.2 $

echo	Importing Compiere DB from $COMPIERE_HOME/data/Compiere.dmp 

if [ $# -le 2 ] 
  then
    echo "Usage:		$0 <systemPassword> <CompiereID> <CompierePWD>"
    echo "Example:	$0 manager compiere compiere"
    exit 1
fi
if [ "$COMPIERE_HOME" = "" -o  "$COMPIERE_DB_NAME" = "" ]
  then
    echo "Please make sure that the environment variables are set correctly:"
    echo "	COMPIERE_HOME	e.g. /Compiere2"
    echo "	COMPIERE_DB_NAME	e.g. compiere.compiere.org"
    exit 1
fi


echo -------------------------------------
echo Re-Create DB user
echo -------------------------------------
echo sqlplus system/$1@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/CreateUser.sql $2 $3
sqlplus system/$1@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/CreateUser.sql $2 $3

echo -------------------------------------
echo Import Compiere.dmp
echo -------------------------------------
echo "imp /system/$1@$COMPIERE_DB_NAME FILE=$COMPIERE_HOME/data/Compiere.dmp FROMUSER=\(reference\) TOUSER=$2"
imp system/$1@$COMPIERE_DB_NAME FILE=$COMPIERE_HOME/data/Compiere.dmp FROMUSER=\(reference\) TOUSER=$2 

if [ "$COMPIERE_DB_TYPE" = "$COMPIERE_DB_PATH" ]
  then
    echo -------------------------------------
    echo Create SQLJ 
    echo -------------------------------------
    $COMPIERE_HOME/utils/$COMPIERE_DB_PATH/create.sh $COMPIERE_DB_USER/$COMPIERE_DB_PASSWORD
fi

echo -------------------------------------
echo Check System
echo Import may show some warnings. This is OK as long as the following does not show errors
echo -------------------------------------
echo sqlplus $2/$3@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/AfterImport.sql
sqlplus $2/$3@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/AfterImport.sql
