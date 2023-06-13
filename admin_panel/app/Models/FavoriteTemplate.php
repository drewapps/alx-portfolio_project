<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class FavoriteTemplate extends Model
{
    use HasFactory;
    protected $table = "favorite_template";

    protected $fillable = [
        "user_id","template_id"
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }

    public function template()
    {
        return $this->hasOne("App\Models\Templates", "id", "template_id");
    }
}
