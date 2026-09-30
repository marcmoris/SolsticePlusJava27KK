<head>  
<script language="javascript" type="text/javascript">  

 /*Usage
  * var request = getHTTPObject();
  * if(request){
  * AJAX CODE HERE
  * }
  *
  * If getHTTPObject returns false, the browser isn't Ajax compatible. The if
  * statement checks to see if it exists, then runs the code.
  */
 function getHTTPObject() 
 {
	var xhr = false;//set to false, so if it fails, do nothing
	if(window.XMLHttpRequest) 
	{//detect to see if browser allows this method
	   var xhr = new XMLHttpRequest();//set var the new request
 	} 
 	else if(window.ActiveXObject) 
 	{//detect to see if browser allows this method
 	  try 
 	  {
	    var xhr = new ActiveXObject("Msxml2.XMLHTTP");//try this method first
   	  } catch(e) 
   	  {//if it fails move onto the next
 	    try 
 	    {
 	       var xhr = new ActiveXObject("Microsoft.XMLHTTP");//try this method next
 	    } catch(e) 
 	    {//if that also fails return false.
 	    xhr = false;
            }
          }
        }
   return xhr;//return the value of xhr
}
var xmlHttp  
function showState(str)  
{
   xmlHttp=getHTTPObject();
   //getXmlHttpObject();  
   alert("1");  
   if (xmlHttp==null)  
   {
     alert("inside if");  
     alert ("Browser does not support HTTP Request")  
     return  
   }   
   alert("outside");  
   var url="getState.jsp"  
   url=url+"?project="+str  
   xmlHttp.onreadystatechange=stateChange  
   xmlHttp.open("GET",url,true)  
   xmlHttp.send(null)  
}  

function stateChange()   
{   
  if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")  
  {   
    document.getElementById("phase").innerHTML=xmlHttp.responseText   
  }   
}   
</script>  
</head>  
<body>  
<select name='project' onchange="showState(this.value)">  
<option value="none" selected>Select One</option>  
<option value='1000003'>project india</option>  
</select>  
<br>  
<div id='phase'>  
<select name='phase' >  
<option value='-1'>pickone</option>  
</select>  
</div>  
</body>  