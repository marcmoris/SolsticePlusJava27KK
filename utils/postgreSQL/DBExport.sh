echo	Compiere PostgreSQL Database Export 	$Revision: 1.2 $

# $Id: DBExport.sh,v 1.2 2007/07/19 21:48:30 marmor01 Exp $

echo Saving database $1@$COMPIERE_DB_NAME to $COMPIERE_HOME/data/ExpDat.dump.tar.gz

pg_dump -F c -f $COMPIERE_HOME/data/ExpDat.dump.tar.gz compiere 

