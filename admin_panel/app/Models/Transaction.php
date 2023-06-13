<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Transaction extends Model
{
    use HasFactory;
    protected $table = "transaction";

    protected $fillable = [
        'user_id','total_paid',"date",'payment_id',"payment_type","status","subscription_id","payment_receipt","currency"
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }

    // public function subscription()
    // {
    //     return $this->hasOne("App\Models\Subscription", "id", "subscription_id");
    // }

    public function plan()
    {
        return $this->hasOne("App\Models\Plan", "id", "subscription_id");
    }
}
