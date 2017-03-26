<?php
function changeStatus ($con,$val,$regid){
	$sql="UPDATE student SET status = '".$val."' WHERE id='".$regid."'";
	mysqli_query($con,$sql);
}
?>
