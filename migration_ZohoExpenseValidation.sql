-- =============================================================================
-- Script d'enregistrement de l'interface VZohoExpenseValidation dans Solstice+
-- =============================================================================

DECLARE @AD_Client_ID NUMERIC(10,0) = 0; -- System/Global
DECLARE @AD_Org_ID NUMERIC(10,0) = 0;
DECLARE @Form_ID NUMERIC(10,0);
DECLARE @Menu_ID NUMERIC(10,0);
DECLARE @Parent_Menu_ID NUMERIC(10,0);

-- 1. Verifier si la Form existe deja
SELECT @Form_ID = AD_Form_ID 
FROM AD_Form 
WHERE Classname = 'org.solstice.apps.form.VZohoExpenseValidation';

IF (@Form_ID IS NULL)
BEGIN
    -- Obtenir le prochain ID de sequence AD_Form
    SELECT @Form_ID = MAX(AD_Form_ID) + 1 FROM AD_Form;
    IF (@Form_ID < 1000000) SET @Form_ID = 2000500;

    INSERT INTO AD_Form (
        AD_Form_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
        Name, Description, Help, Classname, AccessLevel
    ) VALUES (
        @Form_ID, @AD_Client_ID, @AD_Org_ID, 'Y', GETDATE(), 100, GETDATE(), 100,
        'Audit & Validation Zoho Expense', 
        'Validation et audit des remboursements Zoho Expense vs Paie Solstice',
        'Detecte les doublons de remboursements et les depenses Zoho en souffrance pour une ou plusieurs periodes de paie.',
        'org.solstice.apps.form.VZohoExpenseValidation',
        '6' -- System + Client
    );

    PRINT 'AD_Form cree avec succes (ID=' + CAST(@Form_ID AS VARCHAR) + ')';
END
ELSE
BEGIN
    UPDATE AD_Form 
    SET Name = 'Audit & Validation Zoho Expense',
        Description = 'Validation et audit des remboursements Zoho Expense vs Paie Solstice',
        Classname = 'org.solstice.apps.form.VZohoExpenseValidation',
        IsActive = 'Y',
        Updated = GETDATE()
    WHERE AD_Form_ID = @Form_ID;
    
    PRINT 'AD_Form mis a jour avec succes (ID=' + CAST(@Form_ID AS VARCHAR) + ')';
END

-- 2. Donner l'acces a tous les roles actifs
INSERT INTO AD_Form_Access (AD_Form_ID, AD_Role_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy, IsReadWrite)
SELECT @Form_ID, r.AD_Role_ID, r.AD_Client_ID, 0, 'Y', GETDATE(), 100, GETDATE(), 100, 'Y'
FROM AD_Role r
WHERE r.IsActive = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM AD_Form_Access fa 
      WHERE fa.AD_Form_ID = @Form_ID AND fa.AD_Role_ID = r.AD_Role_ID
  );

-- 3. Attacher au menu Solstice sous "Paie" ou "Comptes de depenses"
SELECT TOP 1 @Parent_Menu_ID = AD_Menu_ID 
FROM AD_Menu 
WHERE (Name LIKE '%Paie%' OR Name LIKE '%Payroll%') AND IsSummary = 'Y'
ORDER BY AD_Menu_ID;

IF (@Parent_Menu_ID IS NULL)
    SET @Parent_Menu_ID = 1000000;

SELECT @Menu_ID = AD_Menu_ID 
FROM AD_Menu 
WHERE AD_Form_ID = @Form_ID;

IF (@Menu_ID IS NULL)
BEGIN
    SELECT @Menu_ID = MAX(AD_Menu_ID) + 1 FROM AD_Menu;
    IF (@Menu_ID < 1000000) SET @Menu_ID = 2000500;

    INSERT INTO AD_Menu (
        AD_Menu_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
        Name, Description, IsSummary, IsSOTrx, IsReadOnly, Action, AD_Form_ID
    ) VALUES (
        @Menu_ID, @AD_Client_ID, @AD_Org_ID, 'Y', GETDATE(), 100, GETDATE(), 100,
        'Audit & Validation Zoho Expense',
        'Validation des remboursements Zoho Expense et detection des doublons de paie',
        'N', 'N', 'N', 'F', @Form_ID
    );

    PRINT 'AD_Menu cree avec succes (ID=' + CAST(@Menu_ID AS VARCHAR) + ')';
END
GO
