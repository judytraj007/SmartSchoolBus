<?php
    include 'dbConnect.php';
 
    if($_SERVER['REQUEST_METHOD']=='POST'){
		$id = $_POST['id'];
		$sno = $_POST['serialno'];
        $name = $_POST['stopid'];
        $am = $_POST['amtime'];
		$pm = $_POST['pmtime'];
		$sql="UPDATE route SET serialno='".$sno."',stopid = '".$name."',amtime = '".$am."',pmtime = '".$pm."' WHERE busno = '".$id."'";
      if(mysqli_query($con,$sql)){
	 echo 'success';
	 }else{
	 echo 'failed';
	}
    }else{
        echo 'error';
    }
?>