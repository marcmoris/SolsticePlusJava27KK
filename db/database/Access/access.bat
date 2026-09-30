@Title	Wrap Security Procedures
@Rem $Id: access.bat,v 1.1 2007/07/18 15:21:43 marmor01 Exp $

@Echo	------
@Echo	Wrap
@Echo	------

wrap iname=context_header
wrap iname=context_body

@Echo	------
@Echo	Create
@Echo	------
@sqlplus compiere/compiere @Wrap

@pause
