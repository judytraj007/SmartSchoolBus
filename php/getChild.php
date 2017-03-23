<?php 
 
 if($_SERVER['REQUEST_METHOD']=='GET'){
 
 $id  = $_GET['id'];
 
 require_once('dbConnect.php');
 
 $sql = "SELECT * FROM student WHERE id='".$id."'";
 
 $r = mysqli_query($con,$sql);
 
 $res = mysqli_fetch_array($r);
 
 $result = array();
 
 array_push($result,array(
 "name"=>$res['name'],
 "class"=>$res['class'],
 "busno"=>$res['busno'],
 "stopid"=>$res['stopid']));
 echo json_encode(array("result"=>$result));
 
 mysqli_close($con);
 
 }
 ?>