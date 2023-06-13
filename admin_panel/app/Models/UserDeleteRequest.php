<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class UserDeleteRequest extends Model
{
    use HasFactory;
    protected $table = "user_delete_request";

    protected $fillable = [
        "user_id"
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }
}
