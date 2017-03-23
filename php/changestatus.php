<?php
include 'dbConnect.php';
function changeStatus($val,$regid){
	$sql="UPDATE student SET status = '".$val."' WHERE id='".$regid."'";
	mysqli_query($sql, $con);
}
?>