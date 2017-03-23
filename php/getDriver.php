<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 
 $id  = $_GET['busno'];
 
 require_once('dbConnect.php');
 
 $sql = "SELECT * FROM bus WHERE id='".$id."'";
 
 $r = mysqli_query($con,$sql);
 
 $res = mysqli_fetch_array($r);
 
 $result = array();
 
 array_push($result,array(
 "name"=>$res['driver'],
 "contact"=>$res['contact']));
 echo json_encode(array("result"=>$result));
 
 mysqli_close($con);
 
 }