<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Arr;

class AdsSetting extends Model
{
    protected $table = "ads_setting";

    protected $fillable = [
        'key_name', 'key_value',
    ];

    public static function get($key)
    {
        return AdsSetting::whereName($key)->first()->key_value;
    }

    public static function getAdsSetting($key)
    {
        $settings = Arr::pluck(AdsSetting::all()->toArray(), 'key_value', 'key_name');
        return (is_array($key)) ? array_only($settings, $key) : $settings[$key];
    }
}
