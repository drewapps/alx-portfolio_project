<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Arr;

class OtherSetting extends Model
{
    protected $table = "other_setting";

    protected $fillable = [
        'key_name', 'key_value',
    ];

    public static function get($key)
    {
        return OtherSetting::whereName($key)->first()->key_value;
    }

    public static function getOtherSetting($key)
    {
        $settings = Arr::pluck(OtherSetting::all()->toArray(), 'key_value', 'key_name');
        return (is_array($key)) ? array_only($settings, $key) : $settings[$key];
    }
}
