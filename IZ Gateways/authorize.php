<?php

/* * ************************************************
 * PluginLotto.com                                 *
 * Copyrights (c) 2005-2010. iZAP                  *
 * All rights reserved                             *
 * **************************************************
 * @author iZAP Team "<support@izap.in>"
 * @link http://www.izap.in/
 * @version {version} $Revision: {revision}
 * Under this agreement, No one has rights to sell this script further.
 * For more information. Contact "Tarun Jangra<tarun@izap.in>"
 * For discussion about corresponding plugins, visit http://www.pluginlotto.com/pg/forums/
 * Follow us on http://facebook.com/PluginLotto and http://twitter.com/PluginLotto
 */

class authorize extends gateWayMethods implements paymentGateways {

  private $authorize_url;
  private $response;
  private $raw_response;
  private $data = array();

  public function __construct() {
    $this->authorize_url = 'https://secure.authorize.net/gateway/transact.dll';
    $this->data['x_Version'] = '3.1';
    $this->data['x_Delim_Char'] = '|';
    $this->data['x_Delim_Data'] = 'TRUE';
    $this->data['x_relay_response'] = 'False';
    $this->data['x_Encap_Char'] = '';
    $this->data['x_Type'] = 'AUTH_CAPTURE';
    $this->data['x_Method'] = 'CC';
  }

  public function setParams($array) {
    foreach ($array as $key => $val) {
      $this->data[$key] = $val;
    }
  }

  public function submit($user_guid = 0) {
    if (func_get_gateway_setting('authorize_test_mode', $user_guid) == 'yes') {
      $this->authorize_url = 'https://test.authorize.net/gateway/transact.dll';
    }

    $posted_params = get_input('authorize');

    $options = array(
        "x_Login" => func_get_gateway_setting('authorize_login_key', $user_guid),
        "x_Tran_Key" => func_get_gateway_setting('authorize_authorization_key', $user_guid),
        "x_Amount" => $this->data['grandTotal'],
        "x_Description" => "$this->desc",
        "x_Card_Num" => htmlspecialchars($posted_params['card_number']),
        "x_card_code" => htmlspecialchars($posted_params['card_cvv_number']),
        "x_Exp_Date" => htmlspecialchars($posted_params['card_exp_date']),
    );

    $this->setParams($options);

    unset($this->data['items'], $this->data['grandTotal'], $this->data['custom']);
//    C($this->arrayToPostString($this->data));
//    C($this->authorize_url);EXIT;
    $this->raw_response = $this->sendRequest($this->arrayToPostString($this->data), $this->authorize_url);

    return $this->validate();
  }

  public function getResponse() {
    return $this->response;
  }

  public function getTransactionId() {
    return $this->raw_response[6];
  }

  public function inputForm() {
    $form = '<label>' . elgg_echo('izap_payment:card_type');
    $form .= '<br />';
    $form .= elgg_view('input/dropdown', array(
                'name' => 'authorize[card_type]',
                'options' => array(
                    'Visa',
                    'MasterCard',
                    'American Express',
                ),
            ));
    $form .= '</label>';
    $form .= '<br />';

    $form .= '<label>' . elgg_echo('izap_payment:card_number');
    $form .= '<br />';
    $form .= elgg_view('input/text', array(
                'name' => 'authorize[card_number]',
            ));
    $form .= '</label>';
    $form .= '<br />';

    $form .= '<label>' . elgg_echo('izap_payment:card_cvv');
    $form .= '<br />';
    $form .= elgg_view('input/text', array(
                'name' => 'authorize[card_cvv_number]',
            ));
    $form .= '</label>';
    $form .= '<br />';

    $form .= '<label>' . elgg_echo('izap_payment:exp_date');
    $form .= '</label>';
    $form .= '<br />';
    $form .= elgg_view('input/date', array(
                'name' => 'authorize[card_exp_date]',
                'params' => array(
                    'start_year' => date(Y),
                ),
            ));
    $form .= '<br />';

    return $form;
  }

  public function validate() {
    $this->raw_response = explode($this->data['x_Delim_Char'], $this->raw_response);

    if ($this->raw_response[0] == 1) {
      $this->response['status'] = TRUE;
      $this->response['success_msg'] .= $this->raw_response[3];
    } else {
      $this->response['status'] = FALSE;
      $this->response['error_msg'] .= $this->raw_response[3];
    }

    $this->response['msg'] = $this->approvalResponse();

    return $this->response;
  }

  function approvalResponse() {
    if ($this->raw_response[0] == 1) {
      return "APPROVED";
    }

    if ($this->raw_response[0] == 2) {
      return "DECLINED";
    }

    if ($this->raw_response[0] == 3) {
      return "ERROR";
    }
  }

  public function settingForm() {
    $form = '<label>' . elgg_echo('izap_payment:authorize_login_key');
    $form .= '<br />';
    $form .= elgg_view('input/text',
                    array(
                        'name' => 'params[authorize_login_key]',
                        'value' => get_plugin_usersetting('authorize_login_key', get_loggedin_userid(), GLOBAL_IZAP_PAYMENT_PLUGIN),
                        'class' => 'general-text',
                    )
    );
    $form .= '</label>';

    $form .= '<br />';

    $form .= '<label>' . elgg_echo('izap_payment:authorize_authorization_key');
    $form .= '<br />';
    $form .= elgg_view('input/text',
                    array(
                        'name' => 'params[authorize_authorization_key]',
                        'value' => get_plugin_usersetting('authorize_authorization_key', get_loggedin_userid(), GLOBAL_IZAP_PAYMENT_PLUGIN),
                        'class' => 'general-text',
                    )
    );
    $form .= '</label>';

    $form .= '<br />';

    $form .= '<label>' . elgg_echo('izap_payment:test_mode') . '<br />';
    $form .= elgg_view('input/radio',
                    array(
                        'name' => 'params[authorize_test_mode]',
                        'value' => get_plugin_usersetting('authorize_test_mode', get_loggedin_userid(), GLOBAL_IZAP_PAYMENT_PLUGIN),
                        'class' => 'general-text',
                        'options' => array(
                            elgg_echo('izap_payment:yes') => 'yes',
                            elgg_echo('izap_payment:no') => 'no',
                        ),
                    )
    );
    $form .= '</label>';

    $form .= '<div class="gateway_help"><a href="#" onclick="$(\'#help_div_authorize\').toggle(); return false;">' . elgg_echo('izap_payment:help') . '</a>';
    $form .= '<div style="display: none;" id="help_div_authorize">
      Just add your login key and authorization key and select your payment mode. 
              </div></div>';

    return $form;
  }

}
