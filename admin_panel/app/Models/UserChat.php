<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class UserChat extends Model
{
    use HasFactory;
    protected $table = "user_chat";

    protected $fillable = [
        "user_id","chat_id","role","text"
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }
}
