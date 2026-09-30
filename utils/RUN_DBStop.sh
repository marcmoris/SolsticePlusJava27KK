# $Id: RUN_DBStop.sh,v 1.2 2007/07/19 15:29:44 marmor01 Exp $
if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server
echo Stop DataBase Service - $COMPIERE_HOME \($COMPIERE_DB_NAME\)


sh $COMPIERE_DB_PATH/Stop.sh

