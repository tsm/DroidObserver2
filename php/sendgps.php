<?php
include "db.php"; //znajduje siê tam funkcja do zalogowania siê do bazy danych

if($_POST['longitude']&&$_POST['latitude']&&$_POST['patient_id'])
{
  connect2db();
  $long=$_POST['longitude'];
  $lati=$_POST['latitude'];
  $p_id=$_POST['patient_id'];
  mysql_query("INSERT INTO `locations` (patient_id, longitude, latitude) VALUES ($p_id,$long,$lati);");
  echo "ok";
}
else
{ 
?>
  <form action="sendgps.php" method="POST">
  <table>
    <tr><td>Patient_id:</td><td><input class="tekst" type="text" name="patient_id"></td></tr>
	<tr><td>Longnitude:</td><td><input class="tekst" type="text" name="longitude"></td></tr>
	<tr><td>Latitude:</td><td><input class="tekst" type="text" name="latitude"></td></tr>
    <tr><td colspan="2"><input type="submit" value="Wyslij"</td></tr>
  </table>
</form>

<?php
}
?>