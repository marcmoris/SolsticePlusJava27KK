<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
   <xsl:template match="bankDeposit">
      <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
         <fo:layout-master-set>
            <fo:simple-page-master master-name="all" page-height="11in" page-width="8.5in" margin-top="9mm" margin-bottom="9mm" margin-left="11mm" margin-right="10mm">
               <fo:region-body margin-top="0mm" margin-bottom="0mm" />
               <fo:region-before extent="0mm" />
               <fo:region-after extent="0mm" />
            </fo:simple-page-master>
         </fo:layout-master-set>
         <fo:page-sequence master-reference="all">
            <!-- header with running glossary entries -->
            <fo:static-content flow-name="xsl-region-before">
            </fo:static-content>
            <fo:static-content flow-name="xsl-region-after">
            </fo:static-content>
            <fo:flow flow-name="xsl-region-body">
               <xsl:param name="baseUrl"><xsl:value-of select="baseUrl" /></xsl:param>
               <fo:block font-size="8pt">
                  <xsl:apply-templates select="reportHeader" />
               </fo:block>
               <fo:block font-size="8pt" padding-top="1cm">
                  <xsl:apply-templates select="reportBody" />
               </fo:block>
               <fo:footnote>
                  <fo:inline />
                  <fo:footnote-body>
                     <fo:table table-layout="fixed" height="84mm">
                        <fo:table-column column-width="8.5in - 21mm" />
                        <fo:table-body>
                           <fo:table-row>
                              <fo:table-cell font-size="8pt">
                                 <xsl:apply-templates select="reportFooter" />
                              </fo:table-cell>
                           </fo:table-row>
                        </fo:table-body>
                     </fo:table>
                  </fo:footnote-body>
               </fo:footnote>
            </fo:flow>
         </fo:page-sequence>
      </fo:root>
   </xsl:template>
   <xsl:template match="reportHeader">
      <fo:block text-align="center" font-size="10pt" font-weight="bold">
         <xsl:value-of select="title" />
      </fo:block>
      <fo:table table-layout="fixed" width="8.5in - 21mm">
         <fo:table-column column-width="33mm" />
         <fo:table-column />
         <fo:table-column column-width="55mm" />
         <fo:table-body>
            <fo:table-row>
               <fo:table-cell>
                  <fo:block>
                     <fo:external-graphic src="url('{$baseUrl}logohelico.bmp')" width="3.120cm" height="3.120cm" />
                  </fo:block>
               </fo:table-cell>
               <fo:table-cell font-size="10pt" padding-top="1.25cm">
                  <fo:block>Canadian Helicopters Limited</fo:block>
                  <fo:block>1215 Montée Pilon</fo:block>
                  <fo:block>Les Cedres, Quebec   J7T 1G1</fo:block>
               </fo:table-cell>
               <fo:table-cell background-image="url('{$baseUrl}casepaiement.bmp')" background-repeat="no-repeat" padding-top="3mm" padding-left="3mm" padding-right="3mm" padding-bottom="3mm">
                  <fo:block>
                     <xsl:apply-templates select="payment" />
                  </fo:block>
               </fo:table-cell>
            </fo:table-row>
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="reportBody">
      <fo:block font-size="8pt">
         <xsl:apply-templates select="gains" />
         <xsl:apply-templates select="deductions" />
         <xsl:apply-templates select="netPay" />
      </fo:block>
   </xsl:template>
   <xsl:template match="reportFooter">
      <fo:table table-layout="fixed">
         <fo:table-column column-width="112mm" />
         <fo:table-column column-width="8.5in - 133mm" />
         <fo:table-body>
            <fo:table-row>
               <fo:table-cell vertical-align="top">
                  <fo:block text-align="start">
                     <fo:external-graphic src="url('{$baseUrl}lignedecoupe.bmp')" />
                  </fo:block>
               </fo:table-cell>
               <fo:table-cell vertical-align="top">
                  <fo:block text-align="end">
                     <fo:external-graphic src="url('{$baseUrl}lignedecoupe.bmp')" />
                  </fo:block>
               </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
               <fo:table-cell number-columns-spanned="2" padding-top="5mm" padding-bottom="5mm">
                  <fo:block>
                     <xsl:apply-templates select="credits" />
                  </fo:block>
               </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
               <fo:table-cell number-columns-spanned="2" padding-top="5mm" padding-bottom="5mm">
                  <fo:block font-size="10pt" text-align="center" font-weight="bold">
                     <xsl:value-of select="nonNegociable" />
                  </fo:block>
               </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
               <fo:table-cell padding-top="5mm" padding-bottom="5mm">
                  <xsl:apply-templates select="employeeAddress" />
               </fo:table-cell>
               <fo:table-cell padding-top="5mm" padding-bottom="5mm">
                  <xsl:apply-templates select="bankAccounts" />
                  <xsl:apply-templates select="identification" />
                  <xsl:apply-templates select="jobTitle" />
               </fo:table-cell>
            </fo:table-row>
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="gains">
      <fo:table table-layout="fixed">
         <fo:table-column column-width="57mm" />
         <fo:table-column column-width="10mm" />
         <fo:table-column column-width="38mm" />
         <fo:table-column column-width="35mm" />
         <fo:table-column column-width="23mm" />
         <fo:table-column column-width="29mm" />
         <fo:table-header>
            <xsl:apply-templates select="header" />
         </fo:table-header>
         <fo:table-body>
            <xsl:apply-templates select="body" />
            <xsl:apply-templates select="summaries" />
            <fo:table-row height="6mm" font-size="1pt" />
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="deductions">
      <fo:table table-layout="fixed">
         <fo:table-column column-width="57mm" />
         <fo:table-column column-width="10mm" />
         <fo:table-column column-width="38mm" />
         <fo:table-column column-width="35mm" />
         <fo:table-column column-width="23mm" />
         <fo:table-column column-width="29mm" />
         <fo:table-header>
            <xsl:apply-templates select="header" />
         </fo:table-header>
         <fo:table-body>
            <xsl:apply-templates select="body" />
            <xsl:apply-templates select="summaries" />
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="netPay">
      <fo:table table-layout="fixed">
         <fo:table-column column-width="57mm" />
         <fo:table-column column-width="10mm" />
         <fo:table-column column-width="38mm" />
         <fo:table-column column-width="35mm" />
         <fo:table-column column-width="23mm" />
         <fo:table-column column-width="29mm" />
         <fo:table-body>
            <fo:table-row>
               <fo:table-cell>
                  <fo:block font-weight="bold">
                     <xsl:value-of select="description" />
                  </fo:block>
               </fo:table-cell>
               <fo:table-cell />
               <fo:table-cell />
               <fo:table-cell text-align="end">
                  <fo:block font-weight="bold">
                     <xsl:value-of select="amountPeriod" />
                  </fo:block>
               </fo:table-cell>
               <fo:table-cell />
               <fo:table-cell text-align="end">
                  <fo:block font-weight="bold">
                     <xsl:value-of select="amountCumul" />
                  </fo:block>
               </fo:table-cell>
            </fo:table-row>
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="header">
      <fo:table-row>
         <fo:table-cell>
            <fo:block font-weight="bold">
               <xsl:value-of select="description" />
            </fo:block>
         </fo:table-cell>
         <fo:table-cell text-align="end">
            <fo:block font-weight="bold">
               <xsl:value-of select="hourlyRate" />
            </fo:block>
         </fo:table-cell>
         <fo:table-cell text-align="end">
            <fo:block font-weight="bold">
               <xsl:value-of select="unitaryPeriod" />
            </fo:block>
         </fo:table-cell>
         <fo:table-cell text-align="end">
            <fo:block font-weight="bold">
               <xsl:value-of select="amountPeriod" />
            </fo:block>
         </fo:table-cell>
         <fo:table-cell text-align="end">
            <fo:block font-weight="bold">
               <xsl:value-of select="unitaryCum" />
            </fo:block>
         </fo:table-cell>
         <fo:table-cell text-align="end">
            <fo:block font-weight="bold">
               <xsl:value-of select="amountCumul" />
            </fo:block>
         </fo:table-cell>
      </fo:table-row>
      <fo:table-row height="2mm" font-size="1pt" />
   </xsl:template>
   <xsl:template match="body">
      <xsl:for-each select="detail">
         <fo:table-row>
            <fo:table-cell>
               <fo:block>
                  <xsl:value-of select="description" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="hourlyRate" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="unitaryPeriod" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="amountPeriod" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="unitaryCum" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="amountCumul" />
               </fo:block>
            </fo:table-cell>
         </fo:table-row>
      </xsl:for-each>
      <fo:table-row height="2mm" font-size="1pt" />
   </xsl:template>
   <xsl:template match="summaries">
      <xsl:for-each select="summary">
         <fo:table-row>
            <fo:table-cell>
               <fo:block font-weight="bold">
                  <xsl:value-of select="description" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="hourlyRate" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="unitaryPeriod" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="amountPeriod" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="unitaryCum" />
               </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="end">
               <fo:block>
                  <xsl:value-of select="amountCumul" />
               </fo:block>
            </fo:table-cell>
         </fo:table-row>
      </xsl:for-each>
      <fo:table-row height="2mm" font-size="1pt" />
   </xsl:template>
   <xsl:template match="credits">
      <fo:table table-layout="fixed">
         <fo:table-column column-width="85mm" />
         <fo:table-column column-width="35mm" />
         <fo:table-column column-width="35mm" />
         <fo:table-column column-width="35mm" />
         <fo:table-header>
            <fo:table-row>
               <fo:table-cell>
                  <fo:block font-weight="bold">
                     <xsl:value-of select="header/description" />
                  </fo:block>
               </fo:table-cell>
               <fo:table-cell>
                  <fo:block text-align="end" font-weight="bold">
                     <xsl:value-of select="header/previousUnits" />
                  </fo:block>
               </fo:table-cell>
               <fo:table-cell>
                  <fo:block text-align="end" font-weight="bold">
                     <xsl:value-of select="header/variation" />
                  </fo:block>
               </fo:table-cell>
               <fo:table-cell>
                  <fo:block text-align="end" font-weight="bold">
                     <xsl:value-of select="header/balance" />
                  </fo:block>
               </fo:table-cell>
            </fo:table-row>
         </fo:table-header>
         <fo:table-body>
            <xsl:for-each select="body/detail">
               <fo:table-row>
                  <fo:table-cell>
                     <fo:block font-weight="bold">
                        <xsl:value-of select="description" />
                     </fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                     <fo:block text-align="end">
                        <xsl:value-of select="previousUnits" />
                     </fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                     <fo:block text-align="end">
                        <xsl:value-of select="variation" />
                     </fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                     <fo:block text-align="end">
                        <xsl:value-of select="balance" />
                     </fo:block>
                  </fo:table-cell>
               </fo:table-row>
            </xsl:for-each>
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="bankAccounts">
      <fo:table table-layout="fixed" background-color="#cccccc">
         <fo:table-column column-width="30mm" />
         <fo:table-column column-width="32.5mm" />
         <fo:table-column column-width="20mm" />
         <fo:table-body>
            <xsl:for-each select="bankAccount">
               <fo:table-row>
                  <fo:table-cell>
                     <fo:block>
                        <xsl:value-of select="description" />
                     </fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                     <fo:block>
                        <xsl:value-of select="financialInstitution" />
                     </fo:block>
                  </fo:table-cell>
                  <fo:table-cell text-align="end">
                     <fo:block>
                        <xsl:value-of select="amount" />
                     </fo:block>
                  </fo:table-cell>
               </fo:table-row>
            </xsl:for-each>
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="employeeAddress">
      <fo:block><xsl:value-of select="confidential" /></fo:block>
      <fo:block><xsl:value-of select="companyIdentification" /></fo:block>
      <fo:block><xsl:value-of select="employeeName" /></fo:block>
      <fo:block><xsl:value-of select="address1" /></fo:block>
      <fo:block><xsl:value-of select="address2" /></fo:block>
   </xsl:template>
   <xsl:template match="identification">
      <fo:table table-layout="fixed">
         <fo:table-column column-width="30mm" />
         <fo:table-column column-width="52.5mm" />
         <fo:table-body>
            <fo:table-row>
               <fo:table-cell>
                  <fo:block>
                     <xsl:value-of select="label" />
                  </fo:block>
               </fo:table-cell>
               <fo:table-cell>
                  <fo:block>
                     <xsl:value-of select="value" />
                  </fo:block>
               </fo:table-cell>
            </fo:table-row>
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="jobTitle">
      <fo:table table-layout="fixed">
         <fo:table-column column-width="30mm" />
         <fo:table-column column-width="52.5mm" />
         <fo:table-body>
            <fo:table-row>
               <fo:table-cell>
                  <fo:block>
                     <xsl:value-of select="label" />
                  </fo:block>
               </fo:table-cell>
               <fo:table-cell>
                  <fo:block>
                     <xsl:value-of select="value" />
                  </fo:block>
               </fo:table-cell>
            </fo:table-row>
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="payment">
      <fo:table table-layout="fixed">
         <fo:table-column column-width="24mm" />
         <fo:table-column column-width="10mm" />
         <fo:table-column column-width="5mm" />
         <fo:table-column column-width="5mm" />
         <fo:table-column column-width="5mm" />
         <fo:table-body>
            <xsl:apply-templates select="paymentDate" />
            <xsl:apply-templates select="startPeriodDate" />
            <xsl:apply-templates select="endPeriodDate" />
            <xsl:apply-templates select="dateFormat" />
         </fo:table-body>
      </fo:table>
   </xsl:template>
   <xsl:template match="paymentDate">
      <fo:table-row>
         <xsl:apply-templates select="date" />
         <fo:table-cell />
      </fo:table-row>
   </xsl:template>
   <xsl:template match="startPeriodDate">
      <fo:table-row>
         <xsl:apply-templates select="date" />
         <fo:table-cell number-rows-spanned="2" vertical-align="middle">
            <fo:block>
               <xsl:value-of select="to" />
            </fo:block>
         </fo:table-cell>
      </fo:table-row>
   </xsl:template>
   <xsl:template match="endPeriodDate">
      <fo:table-row>
         <xsl:apply-templates select="date" />
         <fo:table-cell />
      </fo:table-row>
   </xsl:template>
   <xsl:template match="dateFormat">
      <fo:table-row>
         <xsl:apply-templates select="date" />
         <fo:table-cell />
      </fo:table-row>
   </xsl:template>
   <xsl:template match="date">
      <fo:table-cell>
         <fo:block><xsl:value-of select="description" /></fo:block>
      </fo:table-cell>
      <fo:table-cell>
         <fo:block><xsl:value-of select="year" /></fo:block>
      </fo:table-cell>
      <fo:table-cell>
         <fo:block><xsl:value-of select="month" /></fo:block>
      </fo:table-cell>
      <fo:table-cell>
         <fo:block><xsl:value-of select="day" /></fo:block>
      </fo:table-cell>
   </xsl:template>
</xsl:stylesheet>