<?php
    include 'dbConnect.php';
 
    if($_SERVER['REQUEST_METHOD']=='POST'){
		$id = $_POST['id'];
        $name = $_POST['name'];
        $class = $_POST['class'];
		$bus = $_POST['busno'];		
		$stop = $_POST['stopid'];
		$sql="UPDATE student SET name='".$name."',class = '".$class."',stopid = '".$stopid."',busno = '".$bus."' WHERE id = '".$id."'";
      if(mysqli_query($con,$sql)){
	 echo 'success';
	 }else{
	 echo 'failed';
	}
    }else{
        echo 'error';
    }
?>