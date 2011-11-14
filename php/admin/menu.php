            <?php
			if($_SESSION['UserID']=='administrator'){
			?>
			<ul id="menu">
              <li><a href="admin.php?page=news"  <?php if ($page==news) print 'class="current"' ?>>Aktualności</a></li>
              <li><a href="admin.php?page=patients" <?php if ($page==patients) print 'class="current"' ?>>Pacjenci</a></li>  
			  <li><a href="admin.php?page=gps" <?php if ($page==gps) print 'class="current"' ?>>Lokalizacje GPS</a></li>
              <li><a href="admin.php?page=photos" <?php if ($page==photos) print 'class="current"' ?>>Zdjęcia</a></li>
                                 
            </ul >
			<?php
			}
			?>

