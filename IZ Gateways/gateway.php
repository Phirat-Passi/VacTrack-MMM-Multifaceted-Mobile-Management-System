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

interface paymentGateways {

  /**
   * to set the input params for the gateways to work with, like loing_id, total etc
   * @param <type> $array 
   */
  public function setParams($array);

  /**
   * to process the payment
   * @param int $user_guid guid of the user to which payment is to be made
   */
  public function submit($user_guid = 0);

  /**
   * to check the validation of the payment of validat the payment request
   */
  public function validate();

  /**
   * generate the admin setting from, to input the gateway information like login id, or other codes.
   */
  public function settingForm();

  /**
   * genrate the input form, to get information form the user
   */
  public function inputForm();

  public function getTransactionId();
  public function getResponse();
}
