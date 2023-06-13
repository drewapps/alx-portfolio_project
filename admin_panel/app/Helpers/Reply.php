<?php

namespace App\Helpers;

use Illuminate\Contracts\Validation\Validator;

class Reply {

	/** Return success response
	 * @param $messageOrData
	 * @return array
	 */

	public static function success($messageOrData, $data = null) {
		$response = [
			'status' => 'success',
		];

		if (!empty($messageOrData) && !is_array($messageOrData)) {
			$response['message'] = $messageOrData;
		}

		if (is_array($data)) {
			$response = array_merge($data, $response);
		}

		if (is_array($messageOrData)) {
			$response = array_merge($messageOrData, $response);
		}

		return $response;
	}

	/** Return error response
	 * @param $message
	 * @return array
	 */

	public static function error($message, $errorName = null, $errorData = []) {
		return [
			'status' => 'fail',
			'error_name' => $errorName,
			'data' => $errorData,
			'message' => $message,
		];
	}

	/** Return validation errors
	 * @param \Illuminate\Validation\Validator|Validator $validator
	 * @return array
	 */

	public static function formErrors($validator) {
		return [
			'status' => 'fail',
			'errors' => $validator->getMessageBag()->toArray(),
		];
	}

	/** Response with redirect action. This is meant for ajax responses and is not meant for direct redirecting
	 * to the page
	 * @param $url string to redirect to
	 * @param null $message Optional message
	 * @return array
	 */

	public static function redirect($url, $message = null) {
		if ($message) {
			return [
				'status' => 'success',
				'message' => $message,
				'action' => 'redirect',
				'url' => $url,
			];
		} else {
			return [
				'status' => 'success',
				'action' => 'redirect',
				'url' => $url,
			];
		}
	}
}