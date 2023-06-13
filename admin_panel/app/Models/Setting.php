<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Arr;

class Setting extends Model
{
    protected $table = "setting";

    protected $fillable = [
        'key_name', 'key_value',
    ];

    public static function get($key)
    {
        return Setting::whereName($key)->first()->key_value;
    }

    public static function getSetting($key)
    {
        $settings = Arr::pluck(Setting::all()->toArray(), 'key_value', 'key_name');
        return (is_array($key)) ? array_only($settings, $key) : $settings[$key];
    }
}
