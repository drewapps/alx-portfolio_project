<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Invoice extends Model
{
    use HasFactory;
    protected $table = "invoice";

    protected $fillable = [
        "payment_id","user_id","subscription_id"
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }
    
    public function plan()
    {
        return $this->hasOne("App\Models\Plan", "id", "subscription_id");
    }
}
