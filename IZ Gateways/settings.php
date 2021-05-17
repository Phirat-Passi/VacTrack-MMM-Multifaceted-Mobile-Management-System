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

return array(
        'path'=>array(
                'www'=>array(
                        'page' => $CONFIG->wwwroot . 'pg/' . GLOBAL_IZAP_PAYMENT_PAGEHANDLER . '/',
                        'images' => $CONFIG->wwwroot . 'mod/' . GLOBAL_IZAP_PAYMENT_PLUGIN . '/_graphics/',
                        'action' => $CONFIG->wwwroot . 'action/' . GLOBAL_IZAP_PAYMENT_ACTION . '/',
                ),
                'dir'=>array(
                        'plugin' => dirname(dirname(__FILE__)) . '/',
                        'images' => dirname(dirname(__FILE__)) . '/_graphics/',
                        'actions' => dirname(dirname(__FILE__)) . '/actions/',
                        'class' => dirname(__FILE__).'/classes/',
                        'functions' => dirname(__FILE__).'/functions/',
                        'lib' => dirname(__FILE__) . '/',
                        'views'=>array(
                                'home'=> GLOBAL_IZAP_PAYMENT_PLUGIN . '/',
                                'forms'=> GLOBAL_IZAP_PAYMENT_PLUGIN . '/forms/',
                        ),
                        'pages' => dirname(dirname(__FILE__)).'/pages/',
                ),
        ),

        'plugin'=>array(
                'name'=>GLOBAL_IZAP_PAYMENT_PLUGIN,
                'title'=>'Payment gateways',
                'url_title' => GLOBAL_IZAP_PAYMENT_PAGEHANDLER,

                'actions'=>array(
                        GLOBAL_IZAP_PAYMENT_ACTION . '/choose_gateway' =>  array(
                                'file' => 'choose_gateway.php',
                        ),
                ),

                'action_to_plugin_name' => array(
                        GLOBAL_IZAP_PAYMENT_ACTION => GLOBAL_IZAP_PAYMENT_PLUGIN,
                ),

                'menu'=>array(
                ),


                'submenu'=>array(
                        'settings' => array(
                                'pg/'.GLOBAL_IZAP_PAYMENT_PAGEHANDLER.'/choose_gateway/'.get_loggedin_user()->username.'/' => array(
                                        'title' => 'izap_payment:choose_gateway',
                                        'public' => FALSE,
                                        'admin_only' => izap_gatekeeper(array(
                                        'plugin' => GLOBAL_IZAP_PAYMENT_PLUGIN,
                                        'return' => TRUE,
                                        )),
                                ),
                        ),

                ),

                'events' => array(

                ),

                'hooks' => array(
                        'alertpay' => array(
                                'alert_url' => array(
                                        'izap_alertpay_alert_url',
                                )
                        ),
                ),
                
                'custom' => array(

                        'installed_gateways' => array(
                                'multi' => array('paypal', 'alertpay'),
                                'single' => array('payleap', 'authorize', 'none'),
                        ),

                        'gateways_info' => array(

                                'paypal' => array(
                                        'title' => 'Paypal',
                                ),

                                'payleap' => array(
                                        'title' => 'Credit card',
                                ),

                                'authorize' => array(
                                        'title' => 'Credit card',
                                ),

                                'alertpay' => array(
                                        'title' => 'Alert pay'
                                ),
                        ),
                ),
        ),

        'includes'=>array(
                dirname(__FILE__) . '/gateways' => array(
                        'gateWayMethods.php',
                ),
                dirname(__FILE__) . '/interfaces' => array(
                        'paymentGateways.php',
                ),
                dirname(__FILE__) . '/classes' => array(
                        'IzapPayment.php',
                ),
                dirname(__FILE__) . '/functions' => array(
                        'core.php',
                ),
        ),
);
