<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Arr;

class AppUpdateSetting extends Model
{
    protected $table = "app_update_setting";

    protected $fillable = [
        'key_name', 'key_value',
    ];

    public static function get($key)
    {
        return AppUpdateSetting::whereName($key)->first()->key_value;
    }

    public static function getAppUpdateSetting($key)
    {
        $settings = Arr::pluck(AppUpdateSetting::all()->toArray(), 'key_value', 'key_name');
        return (is_array($key)) ? array_only($settings, $key) : $settings[$key];
    }
}
