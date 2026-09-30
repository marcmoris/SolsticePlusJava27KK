cd "C:\Pgi\Solstice\serverRoot\src\web\Crystal"
jar -cvf crystal.war .\*
move /Y .\crystal.war C:\Compiere2\deploy
pause
