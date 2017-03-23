<?php 
include 'dbConnect.php';
	 
$sql = "SELECT * FROM student";
	 
$res = mysqli_query($con,$sql);
 
$result = array();
	 
while($row = mysqli_fetch_array($res)){
	array_push($result,
	array('id'=>$row[0],
	'name'=>$row[1],
	'class'=>$row[2],
	'status'=>$row[3],
	'stopid'=>$row[4],
	'busno'=>$row[5]
	));
	}
	 
echo json_encode(array("result"=>$result));
	 
 mysqli_close($con);
  
 ?>