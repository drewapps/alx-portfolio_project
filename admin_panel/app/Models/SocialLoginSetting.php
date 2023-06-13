<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Arr;

class SocialLoginSetting extends Model
{
    protected $table = "social_login_setting";

    protected $fillable = [
        'key_name', 'key_value',
    ];

    public static function get($key)
    {
        return SocialLoginSetting::whereKeyName($key)->first()->key_value;
    }

    public static function getSocialLoginSetting($key)
    {
        $settings = Arr::pluck(SocialLoginSetting::all()->toArray(), 'key_value', 'key_name');
        return (is_array($key)) ? array_only($settings, $key) : $settings[$key];
    }
}
