<?php           
    connect2db();
	$wynik=mysql_query("SELECT login, filename, date FROM `files` ORDER BY id;");
	print "<table>\n";
	while($rekord=mysql_fetch_array($wynik))
	{
	  print "<tr><td>$rekord[0]</td><td>$rekord[2]</td><td><img src=\"upload/$rekord[1]\" width=\"640\"></td></tr>";
	}
	print "</table>\n";
	//<img src="galeria/klasa.jpg"><br>
?>