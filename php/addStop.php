<?php
    include 'dbConnect.php';
 
    if($_SERVER['REQUEST_METHOD']=='POST'){
		$id = $_POST['busno'];
        $sno = $_POST['serialno'];
        $name = $_POST['stopid'];
        $am = $_POST['amtime'];
		$pm = $_POST['pmtime'];
		$sql="INSERT INTO route(busno,serialno,stopid,amtime,pmtime) VALUES ('$id','$sno','$name','$am','$pm')";
      if(mysqli_query($con,$sql)){
	 echo 'success';
	 }else{
	 echo 'failed';
	}
    }else{
        echo 'error';
    }
?>