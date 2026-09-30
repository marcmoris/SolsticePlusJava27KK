cd C:\eclipse\workspace\compiere-all\solsticeWebModules\autorisationAbsence
jar -cvf autorisationAbsence.war ./*
move /Y .\autorisationAbsence.war \\SHIRAZ\Compiere2\deploy
pause
