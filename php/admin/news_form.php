<form action="admin.php?page=news" method="POST">
  <input type='hidden' name='action' value='add'>
  <table>
    <tr><td>Tytuł(opcjonalny):</td><td><input class="tekst" type="text" name="title"></td></tr>
    <tr><td>Treść:</td><td><textarea name="content" rows=10 cols=60></textarea></td></tr>
    <tr><td colspan="2"><input type="submit" value="Opublikuj"</td></tr>
  </table>
</form>
