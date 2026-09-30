#!/bin/sh
#
# $Id: RUN_ImportReference.sh,v 1.2 2007/07/19 21:44:51 marmor01 Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server
echo Import Reference - $COMPIERE_HOME \($COMPIERE_DB_NAME\)


echo Re-Create Reference User and import $COMPIERE_HOME/data - \($COMPIERE_DB_NAME\)
echo == The import will show warnings. This is OK ==
ls -lsa $COMPIERE_HOME/data/*.*
echo Press enter to continue ...
read in

# Parameter: <systemAccount> <CompiereID> <CompierePwd>
sh $COMPIERE_DB_PATH/ImportCompiere.sh $COMPIERE_DB_SYSTEM reference reference
