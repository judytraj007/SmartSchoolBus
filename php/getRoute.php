<?php 
include 'dbConnect.php';
 if($_SERVER['REQUEST_METHOD']=='GET'){

 $id  = $_GET['busno'];
	 
$sql = "SELECT * FROM route WHERE busno='".$id."'";
	 
$res = mysqli_query($con,$sql);
 
$result = array();
	 
while($row = mysqli_fetch_array($res)){
	array_push($result,
	array('serialno'=>$row[0],
	'stopid'=>$row[1],
	'amtime'=>$row[2],
    'pmtime'=>$row[3]
	));
	}
	 
echo json_encode(array("result"=>$result));
	 
 mysqli_close($con);
}
  
 ?>	