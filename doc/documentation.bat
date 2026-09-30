@Rem call with parameter
@Rem	1 - sourcepath entry
@Rem	2 - destination entry
@Rem	3 - parameters
@Rem assumes that you have a file packages.txt in the calling directory

@Rem $Id: documentation.bat,v 1.1 2007/07/18 15:24:08 marmor01 Exp $
@CALL ..\utils_dev\myDevEnv.bat

@Set CLASSPATH=..\lib\Compiere.jar;..\lib\CCTools.jar;..\lib\oracle.jar;..\lib\db2.jar;..\lib\postgresql.jar;..\lib\jPDF.jar
@Set CLASSPATH=%CLASSPATH%;..\lib\CSTools.jar;..\
@Set CLASSPATH=%CLASSPATH%;..\tools\lib\j2ee.jar;..\tools\lib\junit.jar

javadoc -sourcepath %1 -d %2 -use -author -breakiterator -version -link http://java.sun.com/j2se/1.5.0/docs/api -link http://java.sun.com/j2ee/1.4/docs/api -splitindex -windowtitle "Compiere %COMPIERE_VERSION% API Documentation" -doctitle "Compiere<sup>TM</sup> API Documentation" -header "<b>Compiere %COMPIERE_VERSION%</b>" -bottom "Copyright (c) 1999-2004 ComPiere, Inc. - Author: Jorg Janke" -overview doc\overview.html %3 -J-Xmx180m @packages.txt



