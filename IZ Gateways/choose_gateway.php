<?php
/**************************************************
* PluginLotto.com                                 *
* Copyrights (c) 2005-2010. iZAP                  *
* All rights reserved                             *
***************************************************
* @author iZAP Team "<support@izap.in>"
* @link http://www.izap.in/
* @version {version} $Revision: {revision}
* Under this agreement, No one has rights to sell this script further.
* For more information. Contact "Tarun Jangra<tarun@izap.in>"
* For discussion about corresponding plugins, visit http://www.pluginlotto.com/pg/forums/
* Follow us on http://facebook.com/PluginLotto and http://twitter.com/PluginLotto
 */

global $CONFIG;

array_walk_recursive($_POST['params'], 'get_input');
$posted_values = $_POST['params'];
$default_values = unserialize($posted_values['default_values']);
$posted_values = array_merge((array)$default_values,  (array)$posted_values);

foreach($posted_values as $key => $value) {
  if(!empty($key)) {
    set_plugin_usersetting(
            $key,
            func_array_to_plugin_settings($value),
            get_loggedin_userid(),
            GLOBAL_IZAP_PAYMENT_PLUGIN
    );
  }
}
system_message(elgg_echo('settings saved'));
forward($_SERVER['HTTP_REFERER']);
exit;
