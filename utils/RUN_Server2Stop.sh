#!/bin/sh
# Compiere Server Start
#
# $Id: RUN_Server2Stop.sh,v 1.2 2007/07/19 15:29:43 marmor01 Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi

. ./myEnvironment.sh Server
echo Compiere Server Stop - $COMPIERE_HOME \($COMPIERE_DB_NAME\)

echo "Compiere Server Stop for $COMPIERE_APPS_TYPE"
