<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Arr;

class InvoiceSetting extends Model
{
    protected $table = "invoice_setting";

    protected $fillable = [
        'key_name', 'key_value',
    ];

    public static function get($key)
    {
        return InvoiceSetting::whereName($key)->first()->key_value;
    }

    public static function getInvoiceSetting($key)
    {
        $settings = Arr::pluck(InvoiceSetting::all()->toArray(), 'key_value', 'key_name');
        return (is_array($key)) ? array_only($settings, $key) : $settings[$key];
    }
}
