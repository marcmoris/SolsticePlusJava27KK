#!/bin/sh
# Compiere Server Start
#
# $Id: RUN_Server2.sh,v 1.2 2007/07/19 21:44:37 marmor01 Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi

. ./myEnvironment.sh Server

# To use your own Encryption class (implementing org.compiere.util.SecureInterface),
# you need to set it here (and in the client start script) - example:
# SECURE=-DCOMPIERE_SECURE=org.compiere.util.Secure
SECURE=

# headless option if you don't have X installed on the server
JAVA_OPTS="-server $COMPIERE_JAVA_OPTIONS $SECURE -Djava.awt.headless=true"

echo "Compiere Server Start for $COMPIERE_APPS_TYPE"
