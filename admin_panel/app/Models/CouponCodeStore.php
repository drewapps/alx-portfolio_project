<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class CouponCodeStore extends Model
{
    use HasFactory;

    protected $table = "coupon_code_store";

    protected $fillable = [
        'user_id','code'
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }
}
