<?php
    include 'dbConnect.php';
 
    if($_SERVER['REQUEST_METHOD']=='POST'){
		$id = $_POST['id'];
        $name = $_POST['parent'];
		$sql="UPDATE student SET parent='".$name."' WHERE id = '".$id."'";
      if(mysqli_query($con,$sql)){
	 echo 'success';
	 }else{
	 echo 'failed';
	}
    }else{
        echo 'error';
    }
?>