/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.util;


/**
 *	Amount in Words for Thai
 *
 *  @author Sureeraya Limpaibul - http://www.rgagnon.com/javadetails/java-0426.html
 *  @version $Id: AmtInWords_TH.java,v 1.1 2007/07/18 14:42:47 marmor01 Exp $
 */
public class AmtInWords_TH implements AmtInWords
{

        /**
         *      AmtInWords_TH
		 *
		 *	0.23 = ศูนย์ 23/100
		 *	1.23 = หนึ่งบาท 23/100
		 *	11.45 = สิบเอ็ดบาท 45/100
		 *	121.45 = หนึ่งร้อยยี่สิบเอ็ดบาท 45/100
		 *	1231.56 = หนึ่งพันสองร้อยสามสิบเอ็ดบาท 56/100
		 *	12341.78 = หนึ่งหมื่นสองพันสามร้อยสี่สิบเอ็ดบาท 78/100
		 *	123451.89 = หนึ่งแสนสองหมื่นสามพันสี่ร้อยห้าสิบเอ็ดบาท 89/100
		 *	12234571.90 = สิบสองล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	123234571.90 = หนึ่งร้อยยี่สิบสามล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	1987234571.90 = หนึ่งพันเก้าร้อยแปดสิบเจ็ดล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	0.00 = ศูนย์ 00/100
		 *
         */
        public AmtInWords_TH ()
        {
                super ();
        }       //      AmtInWords_TH

        private static final String[]   majorNames      = {
                "",
                "\u0e25\u0e49\u0e32\u0e19 ",
                };
        private static final String[]   hundredThousandNames    = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07\u0e41\u0e2a\u0e19",
                "\u0e2a\u0e2d\u0e07\u0e41\u0e2a\u0e19",
                "\u0e2a\u0e32\u0e21\u0e41\u0e2a\u0e19",
                "\u0e2a\u0e35\u0e48\u0e41\u0e2a\u0e19",
                "\u0e2b\u0e49\u0e32\u0e41\u0e2a\u0e19",
                "\u0e2b\u0e01\u0e41\u0e2a\u0e19",
                "\u0e40\u0e08\u0e47\u0e14\u0e41\u0e2a\u0e19",
                "\u0e41\u0e1b\u0e14\u0e41\u0e2a\u0e19",
                "\u0e40\u0e01\u0e49\u0e32\u0e41\u0e2a\u0e19"
        };

        private static final String[]   tenThousandNames        = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2a\u0e2d\u0e07\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2a\u0e32\u0e21\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2a\u0e35\u0e48\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2b\u0e49\u0e32\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2b\u0e01\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e40\u0e08\u0e47\u0e14\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e41\u0e1b\u0e14\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e40\u0e01\u0e49\u0e32\u0e2b\u0e21\u0e37\u0e48\u0e19"
       };

       private static final String[]    thousandNames   = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07\u0e1e\u0e31\u0e19",
                "\u0e2a\u0e2d\u0e07\u0e1e\u0e31\u0e19",
                "\u0e2a\u0e32\u0e21\u0e1e\u0e31\u0e19",
                "\u0e2a\u0e35\u0e48\u0e1e\u0e31\u0e19",
                "\u0e2b\u0e49\u0e32\u0e1e\u0e31\u0e19",
                "\u0e2b\u0e01\u0e1e\u0e31\u0e19",
                "\u0e40\u0e08\u0e47\u0e14\u0e1e\u0e31\u0e19",
                "\u0e41\u0e1b\u0e14\u0e1e\u0e31\u0e19",
                "\u0e40\u0e01\u0e49\u0e32\u0e1e\u0e31\u0e19"
       };

       private static final String[]    hundredNames    = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2a\u0e2d\u0e07\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2a\u0e32\u0e21\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2a\u0e35\u0e48\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2b\u0e49\u0e32\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2b\u0e01\u0e23\u0e49\u0e2d\u0e22",
                "\u0e40\u0e08\u0e47\u0e14\u0e23\u0e49\u0e2d\u0e22",
                "\u0e41\u0e1b\u0e14\u0e23\u0e49\u0e2d\u0e22",
                "\u0e40\u0e01\u0e49\u0e32\u0e23\u0e49\u0e2d\u0e22"
                };

       private static final String[]    tensNames       = {
                "",
                "\u0e2a\u0e34\u0e1a",
                "\u0e22\u0e35\u0e48\u0e2a\u0e34\u0e1a",
                "\u0e2a\u0e32\u0e21\u0e2a\u0e34\u0e1a",
                "\u0e2a\u0e35\u0e48\u0e2a\u0e34\u0e1a",
                "\u0e2b\u0e49\u0e32\u0e2a\u0e34\u0e1a",
                "\u0e2b\u0e01\u0e2a\u0e34\u0e1a",
                "\u0e40\u0e08\u0e47\u0e14\u0e2a\u0e34\u0e1a",
                "\u0e41\u0e1b\u0e14\u0e2a\u0e34\u0e1a",
                "\u0e40\u0e01\u0e49\u0e32\u0e2a\u0e34\u0e1a"
        };

        private static final String[]   numNames        = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07",
                "\u0e2a\u0e2d\u0e07",
                "\u0e2a\u0e32\u0e21",
                "\u0e2a\u0e35\u0e48",
                "\u0e2b\u0e49\u0e32",
                "\u0e2b\u0e01",
                "\u0e40\u0e08\u0e47\u0e14",
                "\u0e41\u0e1b\u0e14",
                "\u0e40\u0e01\u0e49\u0e32",
                "\u0e2a\u0e34\u0e1a",
                "\u0e2a\u0e34\u0e1a\u0e40\u0e2d\u0e47\u0e14",
                "\u0e2a\u0e34\u0e1a\u0e2a\u0e2d\u0e07",
                "\u0e2a\u0e34\u0e1a\u0e2a\u0e32\u0e21",
                "\u0e2a\u0e34\u0e1a\u0e2a\u0e35\u0e48",
                "\u0e2a\u0e34\u0e1a\u0e2b\u0e49\u0e32",
                "\u0e2a\u0e34\u0e1a\u0e2b\u0e01",
                "\u0e2a\u0e34\u0e1a\u0e40\u0e08\u0e47\u0e14",
                "\u0e2a\u0e34\u0e1a\u0e41\u0e1b\u0e14",
                "\u0e2a\u0e34\u0e1a\u0e40\u0e01\u0e49\u0e32"
                };

        /**
         *      Convert Less Than One Thousand
         *      @param number
         *      @return
         */
        private String convertLessThanOneMillion (int number)
        {
                String soFar = new String();
                // Esta dentro de los 1os. diecinueve?? ISCAP
                System.out.println("[convertLessThanOneMillion] number = " + number);
/*                if (number % 100 < 20)
                {
                        soFar = numNames[(number % 100)];
                        number /= 100;
                }
*/
                if (number != 0)
                {
                        soFar = numNames[number % 10];
                        if (number != 1 && soFar.equals("\u0e2b\u0e19\u0e36\u0e48\u0e07")) {
                              soFar = "\u0e40\u0e2d\u0e47\u0e14";
                        }
                        number /= 10;

                        soFar = tensNames[number % 10] + soFar;
                        number /= 10;

                        soFar = hundredNames[number % 10] + soFar;
                        number /= 10;

                        soFar = thousandNames[number % 10] + soFar;
                        number /= 10;

                        soFar = tenThousandNames[number % 10] + soFar;
                        number /= 10;

                        soFar = hundredThousandNames[number % 10] + soFar;
                        number /= 10;

                }
                if (number == 0) {
                    return soFar;
                }
                return numNames[number] + "\u0e23\u0e49\u0e2d\u0e22\u0e25\u0e49\u0e32\u0e19" + soFar;
        }       //      convertLessThanOneThousand

        /**
         *      Convert
         *      @param number
         *      @return
         */
        private String convert (int number)
        {
                /* special case */
                if (number == 0)
                {
                        return "\u0e28\u0e39\u0e19\u0e22\u0e4c";
                }
                String prefix = "";
                // บาท
//                String subfix = "\u0e1a\u0e32\u0e17";

                if (number < 0)
                {
                        number = -number;
                        prefix = "\u0e25\u0e1a ";
                }
                String soFar = "";
                int place = 0;
                do
                {
                        double d =  number % 1000000;
                        int n =  (int) d;
                        if (n != 0)
                        {
                                String s = convertLessThanOneMillion (n);
                                place = place > 0 ? 1 : 0;

                                soFar = s + majorNames[place] + soFar;
                        }
                        place++;
                        number /= 1000000d;
                } while (number > 0);

                return (prefix + soFar).trim ();
//                return (prefix + soFar + subfix).trim ();
        }       //      convert


        /***********************************************************************
         *      Get Amount in Words
         *      @param amount numeric amount (352.80)
         *      @return amount in words (three*five*two 80/100)
         */
        public String getAmtInWords (String amount) throws Exception
        {
                if (amount == null)
                        return amount;
                //
                StringBuffer sb = new StringBuffer ();
                int pos = amount.lastIndexOf ('.');
                int pos2 = amount.lastIndexOf (',');
                if (pos2 > pos)
                        pos = pos2;
                String oldamt = amount;
                amount = amount.replaceAll (",", "");
                int newpos = amount.lastIndexOf ('.');
                if (newpos != -1) {
                    int pesos = Integer.parseInt(amount.substring(0, newpos));
                    System.out.println(pesos);
                    sb.append(convert(pesos)).append("\u0e1a\u0e32\u0e17");
                    for (int i = 0; i < oldamt.length(); i++) {
                        if (pos == i) { //        we are done
                            String cents = oldamt.substring(i + 1);
                            int stang = Integer.parseInt(cents);
                            if (stang != 0) {
                                sb.append(convert(stang)).append("สตางค์");
                            } else {
                                sb.append("ถ้วน");
                            }
//                                sb.append (' ').append (cents).append ("/100");
                            break;
                        }
                    }
                } else {
                    int pesos = Integer.parseInt(amount);
                    sb.append(convert(pesos)).append("\u0e1a\u0e32\u0e17").append("ถ้วน");
                }
                return sb.toString ();
        }       //      getAmtInWords

        /***********************************************************************
         *      Get Amount in Words
         *      @param amount numeric amount (352.80)
         *      @return amount in words (three*five*two 80/100)
         */
        public String getAmtInWords (String amount, String iso) throws Exception
        {
                if (amount == null)
                        return amount;
                //
                StringBuffer sb = new StringBuffer ();
                int pos = amount.lastIndexOf ('.');
                int pos2 = amount.lastIndexOf (',');
                if (pos2 > pos)
                        pos = pos2;
                String oldamt = amount;
                amount = amount.replaceAll (",", "");
                int newpos = amount.lastIndexOf ('.');
                if (newpos != -1) {
                    int pesos = Integer.parseInt(amount.substring(0, newpos));
                    System.out.println(pesos);
                    if (iso.equals("THB")) {
                        sb.append(convert(pesos)).append("\u0e1a\u0e32\u0e17");
                        for (int i = 0; i < oldamt.length(); i++) {
                            if (pos == i) { //        we are done
                                String cents = oldamt.substring(i + 1);
                                int stang = Integer.parseInt(cents);
                                if (stang != 0) {
                                    sb.append(convert(stang)).append("สตางค์");
                                } else {
                                    sb.append("ถ้วน");
                                }
//                                sb.append (' ').append (cents).append ("/100");
                                break;
                            }
                        }
                    } else {
                        sb.append(convert(pesos)).append("เหรียญ");
                        for (int i = 0; i < oldamt.length(); i++) {
                            if (pos == i) { //        we are done
                                String cents = oldamt.substring(i + 1);
                                int stang = Integer.parseInt(cents);
                                if (stang != 0) {
                                    sb.append(convert(stang)).append("เซ็นต์").append( " [" +iso +"]");
                                }
                                break;
                            }
                        }
                    }
                } else {
                    int pesos = Integer.parseInt(amount);
                    if (iso.equals("THB")) {
                        sb.append(convert(pesos)).append("\u0e1a\u0e32\u0e17").
                                append("ถ้วน");
                    } else {
                        sb.append(convert(pesos)).append("เหรียญ").append( " [" +iso +"]");
                    }
                }
                return sb.toString ();
        }       //      getAmtInWords

        /**
         *      Test Print
         *      @param amt amount
         */
        private void print (String amt)
        {
                try
                {
                        System.out.println(amt + " = " + getAmtInWords(amt));
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }       //      print
        /**
         *      Test Print
         *      @param amt amount
         */
        private void print (String amt, String currency)
        {
                try
                {
                        System.out.println(amt + " = " + getAmtInWords(amt));
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }       //      print

        /**
         *      Test
         *      @param args ignored
         */
        public static void main (String[] args)
        {
                AmtInWords_TH aiw = new AmtInWords_TH();
        //      aiw.print (".23");      Error
//                aiw.print ("0.23");
//                aiw.print ("1.23");
//                aiw.print ("11.45");
//                aiw.print ("121.45");
                aiw.print ("3,026.00");
                aiw.print ("65341.78");
//                aiw.print ("123451.89");
//                aiw.print ("12234571.90");
//                aiw.print ("123234571.90");
//                aiw.print ("1,987,234,571.90");
//              aiw.print ("11123234571.90");
//              aiw.print ("123123234571.90");
//              aiw.print ("2123123234571.90");
//              aiw.print ("23,123,123,234,571.90");
//              aiw.print ("100,000,000,000,000.90");
//                aiw.print ("0.00");
        }       //      main

}       //      AmtInWords_TH
