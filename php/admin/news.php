          <?php
            $ile_ogloszen=5;
            
            connect2db();
            if($_POST['action']=='add')
            {
              mysql_query("INSERT INTO `news` VALUES (NULL , CURRENT_TIMESTAMP ,'". $_POST['title']."', '".$_POST['content']."');");
            }
            if($_GET['action']=='del')
            {
              mysql_query("delete from `news` where id=".$_GET['nr']."; ");
            }
            include('admin/news_form.php');
            if(isset($_GET['str']))
            {
             $str=$_GET['str'];
            }
            else $str=1;
            $offset=($str-1)*$ile_ogloszen;
            $wynik=mysql_query("SELECT date, title, content,id FROM `news` ORDER BY id DESC LIMIT $ile_ogloszen OFFSET $offset");
						while($rekord=mysql_fetch_array($wynik))
						{
						  print "<div class=\"post\">";
						  print "<i>$rekord[0]</i> &nbsp;&nbsp;&nbsp; <b>$rekord[1]</b>&nbsp;&nbsp;&nbsp; <a href=\"admin.php?page=news&action=del&nr=$rekord[3]\">USUŃ</a><br /><hr class=\"post\" />\n";
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
						    print "<a href=\"admin.php?page=news&str=$i\">$i</a>&nbsp;";
						  }						  
						}
						print '</div>';
				  ?>
