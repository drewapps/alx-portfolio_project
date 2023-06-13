<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Arr;

class EmailSetting extends Model
{
    protected $table = "email_setting";

    protected $fillable = [
        'key_name', 'key_value',
    ];

    public static function get($key)
    {
        return EmailSetting::whereName($key)->first()->key_value;
    }

    public static function getEmailSetting($key)
    {
        $settings = Arr::pluck(EmailSetting::all()->toArray(), 'key_value', 'key_name');
        return (is_array($key)) ? array_only($settings, $key) : $settings[$key];
    }
}
