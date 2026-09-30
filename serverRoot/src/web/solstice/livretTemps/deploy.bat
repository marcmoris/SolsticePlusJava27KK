cd C:\eclipse\workspace\compiere-all\solsticeWebModules\livretTemps
jar -cvf livretTemps.war ./*
move /Y .\livretTemps.war \\SHIRAZ\Compiere2\deploy
pause
