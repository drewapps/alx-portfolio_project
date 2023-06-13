<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Plan extends Model
{
    use HasFactory;
    protected $table = "plan";

    protected $fillable = [
        'plan_name','plan_price','total_words','number_of_images','rewarded_enable','duration','duration_type','google_product_enable','google_product_id','status','most_popular',
    ];
}
