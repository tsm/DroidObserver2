          <?php           
            connect2db();
            if($_POST['action']=='add')
            {
              mysql_query("INSERT INTO `patients` VALUES ('". $_POST['login']."', '". $_POST['pass']."', '". $_POST['mobile']."', '". $_POST['name']."', '".$_POST['surname']."', '".$_POST['disease']."', '".$_POST['email']."',null);");
            }            
            print "\n<table>\n<caption align=\"center\">Pacjenci</caption>\n<tr class=\"naglowek\"><th>Login/ID</th><th>Nazwisko</th><th>Imię</th><th>Telefon</th><th>e-mail</th><th>Choroba</th><th>Hasło</th><th>Ostatnie IP</th></tr>\n";
            $wynik=mysql_query("SELECT login, surname, name, mobile, email, disease, password, last_ip FROM `patients` ORDER BY surname;");
						while($rekord=mysql_fetch_array($wynik))
						{
						  print "<tr><td>$rekord[0]</td><td>$rekord[1]</td><td>$rekord[2]</td><td>$rekord[3]</td><td>$rekord[4]</td><td>$rekord[5]</td><td>$rekord[6]</td><td>$rekord[7]</td></tr>";
						}
						print "</table>\n<br>\n";
						print '<form action="admin.php?page=patients" method="POST">';
						print "<input type='hidden' name='action' value='add'>\n";
						print	"<table>\n";
						print "\n<table>\n<caption align=\"center\">Nowy pacjent</caption>\n<tr class=\"naglowek\"><th>Login/ID</th><th>Nazwisko</th><th>Imię</th><th>Telefon</th><th>e-mail</th><th>Choroba</th><th>Hasło</th></tr>\n";
						print '<td><input size="tekst" type="text" name="login" size="10"></td>';
						print '<td><input class="tekst" type="text" name="surname" size="10"></td>';
						print '<td><input class="tekst" type="text" name="name" size="10"></td>';
						print '<td><input class="tekst" type="text" name="mobile" size="10"></td>';
						print '<td><input class="tekst" type="text" name="email" size="10"></td>';
						print '<td><input class="tekst" type="text" name="disease" size="10"></td>';
						print '<td><input class="tekst" type="text" name="pass" size="10"></td>';
						print	"</tr>\n<tr><td colspan=\"7\"><input type=\"submit\" value=\"Dodaj pacjenta\"</td></tr>\n</table>\n";
						print	"</form>\n";
				  ?>
