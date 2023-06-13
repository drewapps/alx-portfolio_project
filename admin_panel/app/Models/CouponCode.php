<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class CouponCode extends Model
{
    use HasFactory;

    protected $table = "coupon_code";

    protected $fillable = [
        'code','discount','status','limit','title','description','valid_until','subscription_id'
    ];

    public function subscription()
    {
        return $this->hasOne("App\Models\Subscription", "id", "subscription_id");
    }
}
