# $Id: RUN_DBStart.sh,v 1.2 2007/07/19 15:29:43 marmor01 Exp $
if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server
echo Start DataBase Service - $COMPIERE_HOME \($COMPIERE_DB_NAME\)


sh $COMPIERE_DB_PATH/Start.sh

