<?php

namespace App\Models;

use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class User extends Authenticatable
{
    use HasApiTokens, HasFactory, Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'name',
        'email',
        'password',
        'words_left',
        'image_left',
        'image',
        'oauth_id',
        'oauth_type',
        'status',
        'email_verified_at',
        'user_type',
        'is_subscribe',
        'subscription_id',
        'subscription_start_date',
        'subscription_end_date',
        'is_deleted',
    ];

    /**
     * The attributes that should be hidden for serialization.
     *
     * @var array<int, string>
     */
    protected $hidden = [
        'password',
        'remember_token',
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array<string, string>
     */
    protected $casts = [
        'email_verified_at' => 'datetime',
    ];

    public function subscription()
    {
        return $this->hasOne("App\Models\Subscription", "id", "subscription_id");
    }

    public function plan()
    {
        return $this->hasOne("App\Models\Plan", "id", "subscription_id");
    }
}
