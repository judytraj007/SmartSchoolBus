<?php
include 'dbConnect.php';
include 'checkstatus.php';
$id = $_POST['child']; 
$body=checkStatus($id);
	
$ch = curl_init("https://fcm.googleapis.com/fcm/send");
$token = "ceo1hJQnt-0:APA91bGkSgAGP274nyng7c3s8IixNtCrxKuWQIO1ZwRWCUgpzL4CZEoGGwxlEQIQGedrhjvOl1L-faEEA0MeO0sxvgw35hPoGGzY92w6zyUncalTT-jGJq3IdxSy2hI-VBoP2OZi1ZUD";

//Title of the Notification.
$title = "Your child is safe!";


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
//}
?>