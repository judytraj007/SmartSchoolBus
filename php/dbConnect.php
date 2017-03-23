
<?php
 
/*
 * All database connection variables
 */
 
define('DB_USER', "u357867664_kat"); // db user
define('DB_PASSWORD', "katniss394"); // db password (mention your db password here)
define('DB_DATABASE', "u357867664_bus"); // database name
define('DB_SERVER', "mysql.hostinger.in"); // db server

$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die('Unable to Connect');
?>