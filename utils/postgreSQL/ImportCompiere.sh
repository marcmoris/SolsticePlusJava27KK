echo	Compiere Database Import		$Revision: 1.2 $

# $Id: ImportCompiere.sh,v 1.2 2007/07/19 21:48:30 marmor01 Exp $

echo	Importing Compiere DB from $COMPIERE_HOME/data/compiereEDB.jar $COMPIERE_DB_NAME

if [ $# -ne 3 ] 
  then
    echo "Usage:		$0 <systemAccountPWD> <CompiereID> <CompierePWD>"
    echo "Example:	$0 compiere compiere compiere"
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
echo edb-psql -d mgmtsvr -U enterprisedb -r $1 -f $COMPIERE_HOME/utils/postgreSQL/CreateDB.sql -v compiereID=$2 -v compierePW=$3
edb-psql -d mgmtsvr -U enterprisedb -r $1 -f $COMPIERE_HOME/utils/postgreSQL/CreateDB.sql -v compiereID=$2 -v compierePW=$3

PGPASSWORD=$3
export PGPASSWORD

echo -------------------------------------
echo Import compiereEDB.jar 
echo This might take more than ten minutes ...
echo -------------------------------------
if [ "$2" != "reference" ]
then
    echo pg_restore -d $COMPIERE_DB_NAME $COMPIERE_HOME/data/edb.compiere.backup -U $2
    pg_restore -d $COMPIERE_DB_NAME $COMPIERE_HOME/data/edb.compiere.backup -U $2
else
    echo pg_restore -d $2 $COMPIERE_HOME/data/edb.compiere.backup -U $2
    pg_restore -d $2 $COMPIERE_HOME/data/edb.compiere.backup -U $2

    echo edb-psql -d $2 -U $2 -r $3 -f $COMPIERE_HOME/Utils/postgreSQL/AlterSchema.sql -v referenceID=$2 -v compiereID=compiere
    edb-psql -d $2 -U $2 -r $3 -f $COMPIERE_HOME/Utils/postgreSQL/AlterSchema.sql -v referenceID=$2 -v compiereID=compiere
fi
