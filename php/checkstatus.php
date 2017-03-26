<?php
include 'changestatus.php';
function checkStatus($con){
	$sql="SELECT status FROM student WHERE id = 3133482000";
	$result = mysqli_query($con,$sql);
        if ($row = $result->fetch_assoc()) {
           $res = $row["status"];
           }
	if(strcmp($res,'1')==0){
	echo changestatus($con,'0','3133482000');
	$msg = "Your child has alighted at school.";
	}
	else{
	echo changestatus($con,'1','3133482000');
	$msg = "Your child has boarded from school.";
	}
	return $msg;
}
?>
