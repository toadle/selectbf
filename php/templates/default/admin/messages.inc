<tmpl_if name="message">
<table class=navbar width=300>
 <tr class=head>
  <th><img src="{tmpl_var name='IMAGES_DIR'}icon_message.gif" align=absmiddle hspace=2> Notice</th>
 </tr>
 <tr>
  <td class=admin>
   <tmpl_var name="message" escape="none">
   <p>
  </td>
 </tr>
</table>
<p>
</tmpl_if>
<tmpl_if name="error">
<table class=navbar width=300>
 <tr class=axis>
  <th><img src="{tmpl_var name='IMAGES_DIR'}icon_error.gif" align=absmiddle hspace=2> Error</th>
 </tr>
 <tr>
  <td class=admin>
   <tmpl_var name="error" escape="none">
   <p>
  </td>
 </tr>
</table>
<p>
</tmpl_if>  