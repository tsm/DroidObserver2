<?php 
        connect2db();
		$wynik=mysql_query("SELECT DISTINCT trace, login FROM `locations` ORDER BY id DESC;");
			print '<p>Wybierz użytkownika i czas rozpoczęcia namierzania:</p>';
			while($record=mysql_fetch_array($wynik))
			{								  
			  print "<p><b>".$record[1]."</b> <a href=\"admin.php?page=trace&gps=enable&trace=".$record[0]."&user=".$record[1]."\">".$record[0]."</a></p>\n";								  
			}
       /* $wynik=mysql_query("SELECT latitude, longitude, date, login FROM `locations` ORDER BY id DESC;");
								while($record=mysql_fetch_array($wynik))
								{								  
								  print "<p><b>$record[3]</b> $record[2], szerokość: $record[0]  długość: $record[1], <a href=\"admin.php?page=maps&lat=$record[0]&lon=$record[1]\">mapa</a></p>\n";								  
								}*/

?>
