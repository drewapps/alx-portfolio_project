<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class AIImage extends Model
{
    use HasFactory;
    protected $table = "ai_image";

    protected $fillable = [
        'user_id',
        'description',
        'image',
        'resolution',
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }
}
