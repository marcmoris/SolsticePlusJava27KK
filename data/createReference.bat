@Title Create Reference Export for Distribution from reference@dev2

@Echo Before Export ...................
sqlplus reference/reference@dev2.compiere.org @..\db\database\Startup\oracle\BeforeExport.sql

@Echo .
@Echo Export ..........................
@Echo .
exp reference/reference@dev2.compiere.org FILE=Compiere.dmp Log=Compiere.log CONSISTENT=Y STATISTICS=NONE

@Echo .
@Echo After Import ....................
@Echo .
sqlplus reference/reference@dev2.compiere.org @..\db\database\Startup\oracle\AfterImport.sql

@Echo .
@Echo Package .........................
@Echo .
del Compiere.jar
jar cvfM Compiere.jar Compiere.dmp Compiere.log

@Echo .
@Echo Copy ............................
@Echo .
copy Compiere.jar seed

@date /t
@time /t
@pause