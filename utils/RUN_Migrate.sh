#!/bin/sh
#
# $Id: RUN_Migrate.sh,v 1.2 2007/07/19 21:44:43 marmor01 Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server
echo	Compiere Version Migration - $COMPIERE_HOME \($COMPIERE_DB_NAME\)
echo	.
echo	Version Migration is an optional service for a fee.
echo	Please check http://www.compiere.com/migrate/
echo	.

$COMPIERE_JAVA $COMPIERE_JAVA_OPTIONS -cp $CLASSPATH com.compiere.client.Support



