<?php
include "db.php"; //znajduje si� tam funkcja do zalogowania si� do bazy danych

$target_path  = "./upload/"; //TODO doda� folder pacjenta!!!
$file_name= basename( $_FILES['uploadedfile']['name']);
$target_path = $target_path . $file_name;

connect2db();
mysql_query("INSERT INTO `files` (patient_id, filename) VALUES (1,$file_name);");

if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
 echo "The file ".  basename( $_FILES['uploadedfile']['name']).
 " has been uploaded";
} else{
 echo "There was an error uploading the file, please try again!";
}
?>