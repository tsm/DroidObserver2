          <?php
            $ile_ogloszen=5;
            
            connect2db();
            if(isset($_GET['str']))
            {
             $str=$_GET['str'];
            }
            else $str=1;
            $offset=($str-1)*$ile_ogloszen;
            $wynik=mysql_query("SELECT date, title, content FROM `news` ORDER BY id DESC LIMIT $ile_ogloszen OFFSET $offset");
						while($rekord=mysql_fetch_array($wynik))
						{
						  print "<div class=\"post\">";
						  print "<i>$rekord[0]</i> &nbsp;&nbsp;&nbsp; <b>$rekord[1]</b><br /><hr class=\"post\" />\n";
        			print "$rekord[2]<br />\n";
						  print "</div>\n";
						}
						$wynik=mysql_query("SELECT count(id) FROM `news`");
						$wynik=mysql_fetch_array($wynik);
						print '<div class="navbar">';
						if (ceil($wynik[0]/$ile_ogloszen)>1)for ($i = 1 ; $i<=(ceil($wynik[0]/$ile_ogloszen)); $i++)
						{
						  if ($i==$str){
						    print "$i&nbsp;";
						  }
						  else
						  {
						    print "<a href=\"index.php?page=news&str=$i\">$i</a>&nbsp;";
						  }
						}
						print '</div>';
				  ?>
