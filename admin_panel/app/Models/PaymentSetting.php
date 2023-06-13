<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Arr;

class PaymentSetting extends Model
{
    protected $table = "payment_setting";

    protected $fillable = [
        'key_name', 'key_value',
    ];

    public static function get($key)
    {
        return PaymentSetting::whereKeyName($key)->first()->key_value;
    }

    public static function getPaymentSetting($key)
    {
        $settings = Arr::pluck(PaymentSetting::all()->toArray(), 'key_value', 'key_name');
        return (is_array($key)) ? array_only($settings, $key) : $settings[$key];
    }
}
