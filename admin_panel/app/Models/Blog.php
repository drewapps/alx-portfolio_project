<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Blog extends Model
{
    use HasFactory;
    protected $table = "blog";

    protected $fillable = [
        'title','slug','description','meta_description','meta_keyword','category_id','image','status'
    ];

    public function category()
    {
        return $this->hasOne("App\Models\BlogCategory", "id", "category_id");
    }
}
