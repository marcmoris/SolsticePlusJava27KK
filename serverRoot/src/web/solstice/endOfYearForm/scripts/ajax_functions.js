/**
 * Auteur :             Francis Fortier
 * Date de création :   2007-09-11
 * Description :        Ce fichier contient les fonctions communes pour un
 *                      appel Ajax.
 */

/**
 * Cette méthode sert à envoyer une requête post asynchrone sans paramètre
 * @param xmlhttp Un objet XmlHttpObject
 * @param url L'Url de la page répondante
 */
function SendXmlHttpRequest(xmlhttp, url)
{ 
    xmlhttp.open('POST', url, true); 
    xmlhttp.send(null); 
}

/**
 * Cette méthode sert à instancier un XmlHttpObject pour le bon browser
 * @param handler La fonction appelée au retour de l'appel
 * @return Un objet XmlHttpObject
 */
function GetXmlHttpObject(handler)
{ 
   var objXmlHttp = null;
   if (!window.XMLHttpRequest)
   {
      // Microsoft
      objXmlHttp = GetMSXmlHttp();
      if (objXmlHttp != null)
      {
         objXmlHttp.onreadystatechange = handler;
      }
   } 
   else
   {
      // Mozilla | Netscape | Safari
      objXmlHttp = new XMLHttpRequest();
      if (objXmlHttp != null)
      {
         objXmlHttp.onload = handler;
         objXmlHttp.onerror = handler;
      }
   } 
   return objXmlHttp; 
} 

/**
 * Cette méthode sert à instancier le meilleur objet XmlHttpObject
 * pour Internet Explorer
 * @return Un objet XmlHttpObject
 */
function GetMSXmlHttp()
{
   var xmlHttp = null;
   var clsids = ["Msxml2.XMLHTTP.6.0", "Msxml2.XMLHTTP.5.0",
                 "Msxml2.XMLHTTP.4.0", "Msxml2.XMLHTTP.3.0", 
                 "Msxml2.XMLHTTP.2.6", "Microsoft.XMLHTTP.1.0", 
                 "Microsoft.XMLHTTP.1", "Microsoft.XMLHTTP"];
   
   for(var i = 0; i < clsids.length && xmlHttp == null; i++)
   {
      xmlHttp = CreateXmlHttp(clsids[i]);
   }
   return xmlHttp;
}

/**
 * Cette méthode sert à essayer d'instancier un XmlHttpObject à partir
 * d'un nom de classe ActiveX
 */
function CreateXmlHttp(clsid)
{
   var xmlHttp = null;
   try
   {
      xmlHttp = new ActiveXObject(clsid);
      lastclsid = clsid;
      return xmlHttp;
   }
   catch(e)
   {
      return null;
   }
}

function getXmlHttp()
{
   var xmlHttp;
   try
   {
      // Firefox, Opera 8.0+, Safari
      xmlHttp=new XMLHttpRequest();
   }
   catch (e)
   {
      // Internet Explorer
      try
      {
         xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
      }
      catch (e)
      {
         try
         {
            xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
         }
         catch (e)
         {
            alert("Your browser does not support AJAX!");
            return false;
         }
      }
   }   
   return xmlHttp;
}