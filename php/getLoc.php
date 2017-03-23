<?php 
  include 'dbConnect.php';
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 //Importing database

 //Creating sql query with where clause to get an specific employee
 $sql = "SELECT longitude,latitude,speed FROM status ORDER BY id DESC LIMIT 1";
 
 //getting result 
 $r = mysqli_query($con,$sql);
 
 $res = mysqli_fetch_array($r);
 
 $result = array();
 
 array_push($result,array(
 "latitude"=>$res['latitude'],
 "longitude"=>$res['longitude'],
 "spd"=>$res['speed']
 )
 );
 
 echo json_encode(array("result"=>$result));
 
 mysqli_close($con);
 }
 ?>