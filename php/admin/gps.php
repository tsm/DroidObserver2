<?php 
        connect2db();
        $wynik=mysql_query("SELECT latitude, longitude, date, login FROM `locations` ORDER BY id DESC;");
								while($record=mysql_fetch_array($wynik))
								{								  
								  print "<p><b>$record[3]</b> $record[2], szerokość: $record[0]  długość: $record[1], <a href=\"admin.php?page=maps&lat=$record[0]&lon=$record[1]\">mapa</a></p>\n";								  
								}

?>
