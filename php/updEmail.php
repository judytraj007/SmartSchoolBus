<?php
    include 'dbConnect.php';
 
    if($_SERVER['REQUEST_METHOD']=='POST'){
		$id = $_POST['username'];
        $name = $_POST['newid'];
		$sql="UPDATE user SET userid='".$name."' WHERE userid = '".$id."'";
      if(mysqli_query($con,$sql)){
	 echo 'success';
	 }else{
	 echo 'failed';
	}
    }else{
        echo 'error';
    }
?>