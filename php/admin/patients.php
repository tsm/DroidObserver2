          <?php           
            connect2db();
            if($_POST['action']=='add')
            {
              mysql_query("INSERT INTO `patients` VALUES ('". $_POST['imie']."', '". $_POST['nazwisko']."', '". $_POST['nr']."', '". $_POST['iducznia']."', '".$_POST['haslo']."');");
            }            
            print "\n<table>\n<tr class=\"naglowek\"><th>Login/ID</th><th>Nazwisko</th><th>Imię</th><th>Telefon</th><th>e-mail</th><th>Choroba</th><th>Hasło</th></tr>\n";
            $wynik=mysql_query("SELECT login, surname, name,iducznia, haslo FROM `patients` ORDER BY nazwisko;");
						while($rekord=mysql_fetch_array($wynik))
						{
						  print "<tr><td>$rekord[0]</td><td>$rekord[1]</td><td>$rekord[2]</td><td>$rekord[3]</td><td>$rekord[4]</td></tr>";
						}
						print "</table>\n<br>\n";
						print '<form action="admin.php?page=patients" method="POST">';
						print "<input type='hidden' name='action' value='add'>\n";
						print	"<table>\n";
						print "\n<table>\n<tr class=\"naglowek\"><th>Nr</th><th>Nazwisko</th><th>Imię</th><th>Login/ID</th><th>Hasło</th></tr>\n<tr>";
						print '<td><input size="2" type="text" name="nr"></td>';
						print '<td><input class="tekst" type="text" name="nazwisko"></td>';
						print '<td><input class="tekst" type="text" name="imie"></td>';
						print '<td><input class="tekst" type="text" name="iducznia"></td>';
						print '<td><input class="tekst" type="text" name="haslo"></td>';
						print	"</tr>\n<tr><td colspan=\"5\"><input type=\"submit\" value=\"Dodaj ucznia\"</td></tr>\n</table>\n";
						print	"</form>\n";
				  ?>
