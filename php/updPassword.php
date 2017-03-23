<?php
    include 'dbConnect.php';
 
    if($_SERVER['REQUEST_METHOD']=='POST'){
		$pass = $_POST['password'];
        $password = $_POST['newpass'];
		$sql="UPDATE user SET password='".$password."' WHERE password = '".$pass."'";
      if(mysqli_query($con,$sql)){
	 echo 'success';
	 }else{
	 echo 'failed';
	}
    }else{
        echo 'error';
    }
?>