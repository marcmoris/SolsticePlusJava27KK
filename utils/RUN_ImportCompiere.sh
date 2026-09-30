#!/bin/sh
#
# $Id: RUN_ImportCompiere.sh,v 1.2 2007/07/19 21:44:58 marmor01 Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server
echo Import Compiere - $COMPIERE_HOME \($COMPIERE_DB_NAME\)
ls -lsa $COMPIERE_HOME/data/*.*

echo == The import will show warnings. This is OK ==
echo Press enter to continue ...
read in

# Parameter: <systemPassword> <CompiereID> <CompierePwd>
sh $COMPIERE_DB_PATH/ImportCompiere.sh $COMPIERE_DB_SYSTEM $COMPIERE_DB_USER $COMPIERE_DB_PASSWORD
