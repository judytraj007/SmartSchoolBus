<?php
include 'dbConnect.php';
include 'changestatus.php';
function checkStatus($regid){
	$sql="SELECT status FROM student WHERE id='".$regid."'";
	$result = mysqli_query($sql, $con);
	if($result=="1"){
	echo changestatus("0",$regid);
	$msg = "Your child has alighted at school";
	}
	else{
	echo changestatus("1",$regid);
	$msg = "Your child has boarded from school";
	}
	return $msg;
}
?>