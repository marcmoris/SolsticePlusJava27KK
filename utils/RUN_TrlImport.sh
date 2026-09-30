# $Id: RUN_TrlImport.sh,v 1.2 2007/07/19 15:29:44 marmor01 Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server

echo	Import Compiere Translation - $COMPIERE_HOME \($COMPIERE_DB_NAME\)

# need to change this to reflect your language
export  AD_LANGUAGE=ca_ES

export  DIRECTORY=$COMPIERE_HOME/data/$AD_LANGUAGE

echo	This Procedure imports language $AD_LANGUAGE from directory $DIRECTORY

$JAVA_HOME/bin/java -cp $CLASSPATH org.compiere.install.Translation $DIRECTORY $AD_LANGUAGE import


echo	Done
