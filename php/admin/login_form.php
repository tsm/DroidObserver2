<?php 
  print "<form action=\"admin.php?page=".$page."\" method=\"POST\">";
?>
  <input type='hidden' name='action' value='login'>
  <table>
    <tr><td>Login:</td><td><input class="tekst" type="text" name="login"></td></tr>
    <tr><td>Hasło:</td><td><input class="tekst" type="password" name="haslo"></td></tr>
    <tr><td colspan="2"><input type="submit" value="Zaloguj"</td></tr>
  </table>
</form>
