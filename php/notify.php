<?php
	include 'dbConnect.php';
	include 'checkstatus.php';
	 $lat = $_POST['lat1'];
	 $lng = $_POST['lng1'];
	 $id = $_POST['child']; 
	 $key = "AIzaSyDtpKpAphIqUCTn2GyqNsYryDdr1GGcQjs";
	 
	 $body=checkstatus($con);
	 
	
 $ch = curl_init("https://fcm.googleapis.com/fcm/send");
$token = "eI1SBoY2JBE:APA91bGk0z5GOVTiC4vK15yXyuXwp7l2A5AzSaid3iMzhBLUEyaIXwr09T6hOeLg9-Ca70lBos8mAFLR_wj1d3BuU6Z8SXnHNI7cBs8k44Er_FISEaJ694PidhHpEDZ3Wl858OwDuPBW";

//Title of the Notification.
$title = "Smart School Bus";


//Creating the notification array.
$notification = array('title' =>$title , 'body' => $body);

//This array contains, the token and the notification. The 'to' attribute stores the token.
$arrayToSend = array('to' => $token, 'notification' => $notification);

//Generating JSON encoded string form the above array.
$json = json_encode($arrayToSend);

//Setup headers:
$headers = array();
$headers[] = 'Content-Type: application/json';
$headers[] = 'Authorization: key= AAAAgmc0kdA:APA91bGahPBgDsWbJ_bMx5-vMdXKbLpnYd9ewvkQcuaMhGksapsJaYSYBV1tOJ0GldiZiecj2XB8WoFFWgD2zpRMty7DStYUhXrImfMdohLei8rv8esOTj4JWF2GegjG8ZYm0kmI4DOiFvtbSeVei7kh92l1G8IZgQ';

//Setup curl, add headers and post parameters.
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");                                                                     
curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
curl_setopt($ch, CURLOPT_HTTPHEADER,$headers);       

//Send the request
curl_exec($ch);

//Close request
curl_close($ch);

?>
